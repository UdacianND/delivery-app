package uz.md.shopapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.md.shopapp.config.security.JwtTokenProvider;
import uz.md.shopapp.domain.Role;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.TokenDTO;
import uz.md.shopapp.dtos.user.UserLoginDTO;
import uz.md.shopapp.dtos.user.UserRegisterDTO;
import uz.md.shopapp.exceptions.ConflictException;
import uz.md.shopapp.exceptions.NotEnabledException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.UserMapper;
import uz.md.shopapp.repository.RoleRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.AuthService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           @Lazy AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public ApiResult<TokenDTO> login(UserLoginDTO dto) {

        log.info("User login method called: " + dto);
        User user;
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getPhoneNumber(),
                            dto.getSmsCode()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            user = (User) authentication.getPrincipal();
        } catch (DisabledException | LockedException | CredentialsExpiredException disabledException) {
            throw new NotEnabledException("USER_IS_DISABLED");
        } catch (BadCredentialsException | UsernameNotFoundException badCredentialsException) {
            throw new NotFoundException("INVALID_USERNAME_OR_PASSWORD");
        }

        LocalDateTime tokenIssuedAt = LocalDateTime.now();
        String accessToken = jwtTokenProvider.generateAccessToken(user, Timestamp.valueOf(tokenIssuedAt));
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        TokenDTO tokenDTO = new TokenDTO(accessToken, refreshToken);

        return ApiResult.successResponse(
                tokenDTO);
    }

    @Override
    public ApiResult<Void> register(UserRegisterDTO dto) {
        log.info("User registration with " + dto);

        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber()))
            throw new ConflictException("PHONE_NUMBER_ALREADY_EXISTS");

        User user = userMapper.fromAddDTO(dto);
        Role role = roleRepository
                .findByName("USER")
                .orElseThrow(() -> new NotFoundException("DEFAULT_ROLE_NOT_FOUND"));
        user.setActive(false);
        user.setRole(role);
        userRepository.save(user);
        return ApiResult.successResponse();
    }

    @Override
    public ApiResult<String> getSMSCode(String phoneNumber) {
        User user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("USER NOT FOUND WITH PHONE NUMBER"));
        String smsCode = RandomStringUtils.random(4, false, true);
        user.setSmsCode(passwordEncoder.encode(smsCode));
        user.setCodeGivenTime(LocalDateTime.now());
        userRepository.save(user);
        System.out.println("smsCode = " + smsCode);
        return ApiResult.successResponse(smsCode);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(username).orElseThrow(() -> new UsernameNotFoundException("User Not found with username " + username));
    }
}
