package uz.md.shopapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.md.shopapp.client.SmsSender;
import uz.md.shopapp.client.requests.SendRequest;
import uz.md.shopapp.config.security.JwtTokenProvider;
import uz.md.shopapp.controller.AuthController;
import uz.md.shopapp.domain.Role;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.TokenDTO;
import uz.md.shopapp.dtos.user.ClientLoginDTO;
import uz.md.shopapp.dtos.user.EmployeeLoginDTO;
import uz.md.shopapp.dtos.user.EmployeeRegisterDTO;
import uz.md.shopapp.exceptions.*;
import uz.md.shopapp.mapper.UserMapper;
import uz.md.shopapp.repository.RoleRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.AuthService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SmsSender smsSender;

    public AuthServiceImpl(UserRepository userRepository,
                           @Lazy AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           SmsSender smsSender) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.smsSender = smsSender;
    }

    @Value("${app.role.client.name}")
    private String clientRoleName;

    @Value("${app.sms.valid_till}")
    private Long smsValidTill;

    @Value("${app.sms.valid_till_in}")
    private String smsValidTillIn;

    @Override
    public ApiResult<TokenDTO> loginClient(ClientLoginDTO dto) {

        log.info("login client with {}", dto);

        if (dto == null || dto.getPhoneNumber() == null || dto.getSmsCode() == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xato bor")
                    .messageRu("В запросе ошибка")
                    .build();

        log.info("Client login method called: " + dto);

        User user = authenticate(dto.getPhoneNumber(), dto.getSmsCode());

        if (user == null || user.getRole() == null){
            throw NotFoundException.builder()
                    .messageUz("Foydalanuvchi topilmadi")
                    .messageRu("Пользователь не найден")
                    .build();
        }

        if (!user.getRole().getName().equals("CLIENT"))
            throw NotFoundException.builder()
                    .messageUz("Bunday foydalanuvchi topilmadi")
                    .messageRu("Такой пользователь не найден")
                    .build();

        if (user.getCodeValidTill().isBefore(LocalDateTime.now()))
            throw NotAllowedException.builder()
                    .messageUz("SMS kodi yaroqsiz")
                    .messageRu("SMS-код недействителен")
                    .build();

        LocalDateTime tokenIssuedAt = LocalDateTime.now();
        String accessToken = jwtTokenProvider
                .generateAccessToken(user, Timestamp.valueOf(tokenIssuedAt));

        TokenDTO tokenDTO = new TokenDTO(accessToken);

        return ApiResult.successResponse(
                tokenDTO);
    }

    @Override
    public ApiResult<TokenDTO> loginEmployee(EmployeeLoginDTO dto) {

        log.info("login employee dto: {}", dto);

        if (dto == null || dto.getPhoneNumber() == null || dto.getPassword() == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xato bor")
                    .messageRu("В запросе ошибка")
                    .build();

        log.info("Employee login method called: " + dto);

        User user = authenticate(dto.getPhoneNumber(), dto.getPassword());

        if (user==null || user.getRole() == null || !user.getRole().getName().equals("MANAGER"))
            throw NotFoundException.builder()
                    .messageUz("Bunday ishchi topilmadi")
                    .messageRu("Такого работника не найдено")
                    .build();

        LocalDateTime tokenIssuedAt = LocalDateTime.now();
        String accessToken = jwtTokenProvider.generateAccessToken(user,
                Timestamp.valueOf(tokenIssuedAt));

        if (accessToken == null)
            throw IllegalRequestException.builder()
                    .messageUz("Token yaratishda xatolik yuz berdi")
                    .messageRu("Произошла ошибка при создании токена")
                    .build();

        TokenDTO tokenDTO = new TokenDTO(accessToken);

        return ApiResult
                .successResponse(tokenDTO);

    }

    @Override
    public ApiResult<Void> registerEmployee(EmployeeRegisterDTO dto) {

        log.info("Employee registration with {}", dto);

        if (dto == null || dto.getPhoneNumber() == null
                || dto.getRoleId() == null) {
            throw BadRequestException.builder()
                    .messageUz("So'rovda xato bor")
                    .messageRu("В запросе ошибка")
                    .build();
        }

        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber()))
            throw ConflictException.builder()
                    .messageUz("Telefon raqam allaqachon mavjud")
                    .messageRu("Номер телефона уже существует")
                    .build();

        User user = userMapper.fromEmployeeAddDTO(dto);

        Role role = roleRepository
                .findById(dto.getRoleId())
                .orElseThrow(() -> NotFoundException
                        .builder()
                        .messageUz("Role topilmadi")
                        .messageRu("Роль не найдена")
                        .build()
                );

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActive(true);
        user.setRole(role);
        userRepository.save(user);
        return ApiResult.successResponse();
    }

    private User authenticate(String phoneNumber, String password) {

        if (phoneNumber == null || password == null)
            throw BadRequestException.builder()
                    .messageUz("So'rovda xato bor")
                    .messageRu("В запросе ошибка")
                    .build();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            phoneNumber,
                            password
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return (User) authentication.getPrincipal();
        } catch (DisabledException | LockedException | CredentialsExpiredException disabledException) {
            throw NotEnabledException.builder()
                    .messageUz("Foydalanuvchi aktiv emas")
                    .messageRu("Пользователь не активен")
                    .build();
        } catch (UsernameNotFoundException usernameNotFoundException) {
            throw NotFoundException.builder()
                    .messageUz("Telefon raqam topilmadi")
                    .messageRu("Номер телефона не найден")
                    .build();
        } catch (BadCredentialsException badCredentialsException) {
            throw uz.md.shopapp.exceptions.BadCredentialsException.builder()
                    .messageUz("Noto'g'ri telefon raqami yoki parol")
                    .messageRu("неверный номер телефона или пароль")
                    .build();
        }
    }

    @Override
    public ApiResult<Void> registerClient(String phoneNumber) {

        log.info("User registration with " + phoneNumber);

        if (phoneNumber == null) {
            throw BadRequestException.builder()
                    .messageUz("Telefon raqam bo'sh bo'lishi mumkin emas")
                    .messageRu("Номер телефона не может быть пустым")
                    .build();
        }

        if (userRepository.existsByPhoneNumber(phoneNumber))
            throw ConflictException.builder()
                    .messageUz("Telefon raqam allaqachon mavjud")
                    .messageRu("Номер телефона уже существует")
                    .build();

        User user = new User();
        user.setPhoneNumber(phoneNumber);
        Role role = roleRepository
                .findByName(clientRoleName)
                .orElseThrow(() -> NotFoundException
                        .builder()
                        .messageUz("Standart role topilmadi")
                        .messageRu("Роль по умолчанию не найдена")
                        .build());
        user.setActive(true);
        user.setRole(role);
        userRepository.save(user);
        return ApiResult.successResponse();
    }

    @Override
    @Transactional
    public ApiResult<String> getSMSCode(String phoneNumber) {

        if (phoneNumber == null)
            throw BadRequestException.builder()
                    .messageUz("Telefon raqam bo'sh bo'lishi mumkin emas")
                    .messageRu("Номер телефона не может быть пустым")
                    .build();

        if (!userRepository
                .existsByPhoneNumber(phoneNumber))
            registerClient(phoneNumber);

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> NotFoundException
                        .builder()
                        .messageUz(" Ushbu Telefon raqamdagi foydalanuvchi topilmadi ")
                        .messageRu("Пользователь не найден для этого номера телефона")
                        .build());

        if (!user.getRole().getName().equals("CLIENT"))
            throw NotFoundException.builder()
                    .messageUz(" Foydalananuvchi topilmadi ")
                    .messageRu("Пользователь не найден")
                    .build();

        String smsCode = RandomStringUtils.random(5, false, true);

        System.out.println("=========== smsCode  " + smsCode + " =============== ");

        user.setPassword(passwordEncoder.encode(smsCode));
        user.setCodeValidTill(LocalDateTime.now().plus(smsValidTill, ChronoUnit.valueOf(smsValidTillIn)));

        SendRequest sendRequest =
                new SendRequest(
                        user.getPhoneNumber().substring(1),
                        "" + smsCode + "-code:birzumda.uz",
                        4546,
                        "http://localhost:8090/" + AuthController.BASE_URL + "/client/sign-in");
//        smsSender.sendSms(sendRequest);
        userRepository.save(user);
        return ApiResult.successResponse("SMS kod jo'natildi");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(username)
                .orElseThrow(() -> NotFoundException
                        .builder()
                        .messageUz("Ushbu nomli :u Foydalanuvchi topilmadi ".replaceFirst(":u", username))
                        .messageRu("Пользователь с таким именем :u не найден".replaceFirst(":u", username))
                        .build());
    }
}
