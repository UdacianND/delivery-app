package uz.md.shopapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uz.md.shopapp.domain.Role;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.user.ClientLoginDTO;
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.repository.RoleRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.AuthService;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthServiceTest {

    private static final String DEFAULT_PHONE_NUMBER = "+998931668648";
    private static final String DEFAULT_FIRSTNAME = "Ali";
    private static final String DEFAULT_LASTNAME = "Yusupov";

    private static final String CLIENT_PHONE_NUMBER = "+998941001010";
    private static final String CLIENT_FIRSTNAME = "Muhammadqodir";
    private static final String CLIENT_LASTNAME = "Yusupov";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    private User user;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void init() {
        saveDefaultRole();
        user = new User();
        user.setActive(true);
        user.setDeleted(false);
        user.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setRole(roleRepository
                .save(new Role("ADMIN", "develop",
                        Set.of(PermissionEnum.values()))));
    }

    private void saveDefaultRole() {
        roleRepository.save(new Role("CLIENT", " system Client ", Set.of(
                PermissionEnum.GET_PRODUCT,
                PermissionEnum.GET_INSTITUTION_TYPE,
                PermissionEnum.GET_INSTITUTION,
                PermissionEnum.GET_USER,
                PermissionEnum.GET_CATEGORY,
                PermissionEnum.GET_ORDER
        )));
    }
//
//    @Test
//    @Transactional
//    void shouldRegisterClient() {
//        userRepository.deleteAll();
//        ClientLoginDTO clientLoginDTO = new ClientLoginDTO("+998954554545", "1212");
//        ApiResult<Void> result = authService.registerClient(clientLoginDTO);
//        assertTrue(result.isSuccess());
//        List<User> all = userRepository.findAll();
//        User added = all.get(0);
//        assertEquals(added.getFirstName(), CLIENT_FIRSTNAME);
//        assertEquals(added.getLastName(), CLIENT_LASTNAME);
//        assertEquals(added.getPhoneNumber(), CLIENT_PHONE_NUMBER);
//    }

//    @Test
//    @Transactional
//    void shouldNotRegisterWithAlreadyExistedPhoneNumber() {
//        userRepository.saveAndFlush(user);
//        ClientRegisterDTO registerDTO = new ClientRegisterDTO("user1", "user1", user.getPhoneNumber());
//        assertThrows(ConflictException.class, () -> authService.registerClient(registerDTO));
//    }

//    @Test
//    @Transactional
//    void shouldNotRegisterWithoutDefaultClientRole() {
//        roleRepository.deleteAll();
//        ClientRegisterDTO registerDTO = new ClientRegisterDTO("user1", "user1", "+998961001010");
//        assertThrows(NotFoundException.class, () -> authService.registerClient(registerDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldLoginClient() {
//        user.setPassword(passwordEncoder.encode("1212"));
//        user.setCodeValidTill(LocalDateTime.now().plusMinutes(5));
//        userRepository.saveAndFlush(user);
//        ClientLoginDTO clientLoginDTO = new ClientLoginDTO(user.getPhoneNumber(), "1212");
//        ApiResult<TokenDTO> login = authService.loginOrRegisterClient(clientLoginDTO);
//        assertTrue(login.isSuccess());
//        TokenDTO data = login.getData();
//        assertNotNull(data.getAccessToken());
//        assertNotNull(data.getRefreshToken());
//    }

    @Test
    @Transactional
    void shouldNotLoginWithWrongPhoneNumber() {
        userRepository.deleteAll();
        user.setDeleted(true);
        userRepository.saveAndFlush(user);
        ClientLoginDTO clientLoginDTO = new ClientLoginDTO(user.getPhoneNumber(), "1221");
        assertThrows(BadCredentialsException.class, () -> authService.loginClient(clientLoginDTO));
    }

    @Test
    @Transactional
    void shouldNotLoginWithWrongSMSCode() {
        user.setPassword(passwordEncoder.encode("1212"));
        user.setCodeValidTill(LocalDateTime.now().plusMinutes(5));
        userRepository.saveAndFlush(user);
        ClientLoginDTO clientLogin = new ClientLoginDTO(DEFAULT_PHONE_NUMBER, "5555");
        assertThrows(BadCredentialsException.class, () -> authService.loginClient(clientLogin));
    }

    @Test
    @Transactional
    void shouldNotLoginWithInvalidSMSCode() {
        user.setPassword(passwordEncoder.encode("1212"));
        user.setCodeValidTill(LocalDateTime.now().minusMinutes(15));
        userRepository.saveAndFlush(user);
        ClientLoginDTO clientLogin = new ClientLoginDTO(user.getPhoneNumber(), "1212");
        assertThrows(NotAllowedException.class, () -> authService.loginClient(clientLogin));
    }

}
