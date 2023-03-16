package uz.md.shopapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.TokenDTO;
import uz.md.shopapp.dtos.user.ClientLoginDTO;
import uz.md.shopapp.dtos.user.EmployeeRegisterDTO;
import uz.md.shopapp.exceptions.BadCredentialsException;
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.exceptions.NotEnabledException;
import uz.md.shopapp.repository.RoleRepository;
import uz.md.shopapp.repository.UserRepository;
import uz.md.shopapp.service.contract.AuthService;
import uz.md.shopapp.util.Mock;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    private User client;
    private User employee;

    @Autowired
    private RoleRepository roleRepository;


    @BeforeEach
    public void init() {
        client = Mock.getMockClient();
        roleRepository.save(client.getRole());
        employee = Mock.getEmployeeUser();
        roleRepository.save(employee.getRole());
    }

    @Test
    void shouldLogin() {
        ClientLoginDTO clientLoginDTO = new ClientLoginDTO(client.getPhoneNumber(), client.getPassword());
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        User client1 = userRepository.saveAndFlush(client);
        ApiResult<TokenDTO> result = authService.loginClient(clientLoginDTO);
        Assertions.assertNotNull(result);
        TokenDTO data = result.getData();
        Assertions.assertNotNull(data);
        String accessToken = data.getAccessToken();
        String tokenType = data.getTokenType();
        Assertions.assertNotNull(accessToken);
        Assertions.assertNotNull(tokenType);
        Assertions.assertEquals(tokenType, "Bearer ");
    }

    @Test
    @Transactional
    void shouldNotLoginWithWrongPhoneNumber() {
        userRepository.deleteAll();
        client.setDeleted(true);
        userRepository.saveAndFlush(client);
        ClientLoginDTO clientLoginDTO = new ClientLoginDTO(client.getPhoneNumber(), "1221");
        assertThrows(InternalAuthenticationServiceException.class, () -> authService.loginClient(clientLoginDTO));
    }

    @Test
    @Transactional
    void shouldNotLoginWithWrongSMSCode() {
        client.setPassword(passwordEncoder.encode("1212"));
        client.setCodeValidTill(LocalDateTime.now().plusMinutes(5));
        userRepository.saveAndFlush(client);
        ClientLoginDTO clientLogin = new ClientLoginDTO(client.getPhoneNumber(), "5555");
        assertThrows(BadCredentialsException.class, () -> authService.loginClient(clientLogin));
    }

    @Test
    @Transactional
    void shouldThrowNotEnabledException() {
        client.setPassword(passwordEncoder.encode("1212"));
        client.setCodeValidTill(LocalDateTime.now().plusMinutes(5));
        client.setActive(false);
        userRepository.saveAndFlush(client);
        ClientLoginDTO clientLogin = new ClientLoginDTO(client.getPhoneNumber(), "5555");
        assertThrows(NotEnabledException.class, () -> authService.loginClient(clientLogin));
    }

    @Test
    @Transactional
    void shouldNotLoginWithInvalidSMSCode() {
        client.setPassword(passwordEncoder.encode("1212"));
        client.setCodeValidTill(LocalDateTime.now().minusMinutes(15));
        userRepository.saveAndFlush(client);
        ClientLoginDTO clientLogin = new ClientLoginDTO(client.getPhoneNumber(), "1212");
        assertThrows(NotAllowedException.class, () -> authService.loginClient(clientLogin));
    }

    @Test
    void shouldRegisterEmployee() {
        EmployeeRegisterDTO employeeRegisterDTO = new EmployeeRegisterDTO(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber(),
                employee.getPassword(),
                employee.getRole().getId()
        );
        ApiResult<Void> result = authService.registerEmployee(employeeRegisterDTO);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isSuccess());
    }

    @Test
    void shouldRegisterClient() {
        ApiResult<Void> result = authService.registerClient("+998951002020");
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isSuccess());
        Optional<User> byPhoneNumber = userRepository.findByPhoneNumber("+998951002020");
        Assertions.assertNotNull(byPhoneNumber);
        Assertions.assertTrue(byPhoneNumber.isPresent());
    }

    @Test
    void shouldRegisterClientIfPhoneNumberIsNotRegistered() {
        ApiResult<String> response = authService.getSMSCode("+998951002020");
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());
        Optional<User> byPhoneNumber = userRepository.findByPhoneNumber("+998951002020");
        Assertions.assertNotNull(byPhoneNumber);
        Assertions.assertTrue(byPhoneNumber.isPresent());
    }


}

