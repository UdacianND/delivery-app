//package uz.md.shopapp.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import uz.md.shopapp.domain.Role;
//import uz.md.shopapp.domain.User;
//import uz.md.shopapp.domain.enums.PermissionEnum;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.TokenDTO;
//import uz.md.shopapp.exceptions.ConflictException;
//import uz.md.shopapp.exceptions.NotFoundException;
//import uz.md.shopapp.repository.RoleRepository;
//import uz.md.shopapp.repository.UserRepository;
//import uz.md.shopapp.service.contract.AuthService;
//
//import java.util.List;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//public class AuthServiceTest {
//
//    private static final String DEFAULT_PHONE_NUMBER = "+998931668648";
//    private static final String DEFAULT_FIRSTNAME = "Ali";
//    private static final String DEFAULT_LASTNAME = "Yusupov";
//    private static final String DEFAULT_PASSWORD = "123";
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthService authService;
//
//    private User user;
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @BeforeEach
//    public void init() {
//        user = new User();
//        user.setSmsCode(passwordEncoder.encode(DEFAULT_PASSWORD));
//        user.setActive(true);
//        user.setDeleted(false);
//        user.setPhoneNumber(DEFAULT_PHONE_NUMBER);
//        user.setFirstName(DEFAULT_FIRSTNAME);
//        user.setLastName(DEFAULT_LASTNAME);
//        user.setRole(roleRepository.save(new Role("ADMIN","develop", Set.of(PermissionEnum.values()))));
//    }
//
//    @Test
//    @Transactional
//    void shouldAddUser() {
//
//        userRepository.deleteAll();
//        UserRegisterDTO registerDTO = new UserRegisterDTO("user1", "user1", "+998931112233");
//        ApiResult<Void> result = authService.register(registerDTO);
//
//        assertTrue(result.isSuccess());
//        List<User> all = userRepository.findAll();
//        User added = all.get(0);
//
//        assertEquals(added.getFirstName(), registerDTO.getFirstName());
//        assertEquals(added.getLastName(), registerDTO.getLastName());
//        assertEquals(added.getPhoneNumber(), registerDTO.getPhoneNumber());
//    }
//
//    @Test
//    @Transactional
//    void shouldNotAddWithAlreadyExistedPhoneNumber() {
//        userRepository.saveAndFlush(user);
//        UserRegisterDTO registerDTO = new UserRegisterDTO("user1", "user1", user.getPhoneNumber());
//        assertThrows(ConflictException.class, () -> authService.register(registerDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldLoginUser() {
//        userRepository.saveAndFlush(user);
//
//        UserLoginDTO userLoginDTO = new UserLoginDTO(user.getPhoneNumber(), DEFAULT_PASSWORD);
//
//        ApiResult<TokenDTO> login = authService.login(userLoginDTO);
//        assertTrue(login.isSuccess());
//        TokenDTO data = login.getData();
//        assertNotNull(data.getAccessToken());
//        assertNotNull(data.getRefreshToken());
//    }
//
//
//    @Test
//    @Transactional
//    void shouldNotLoginWithNotFound() {
//        userRepository.deleteAll();
//        user.setDeleted(true);
//        userRepository.saveAndFlush(user);
//        UserLoginDTO userLoginDTO = new UserLoginDTO(user.getPhoneNumber(), user.getSmsCode());
//        assertThrows(NotFoundException.class, () -> authService.login(userLoginDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldNotLoginWithWrongUsernameOrPassword() {
//        userRepository.saveAndFlush(user);
//        UserLoginDTO userLoginDTO = new UserLoginDTO(DEFAULT_PHONE_NUMBER, "wrong");
//        assertThrows(NotFoundException.class, () -> authService.login(userLoginDTO));
//    }
//
//
//}
