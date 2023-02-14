//package uz.md.shopapp.rest;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.json.JacksonJsonParser;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import uz.md.shopapp.IntegrationTest;
//import uz.md.shopapp.controller.AuthController;
//import uz.md.shopapp.controller.UserController;
//import uz.md.shopapp.domain.Role;
//import uz.md.shopapp.domain.User;
//import uz.md.shopapp.domain.enums.PermissionEnum;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.user.UserDTO;
//import uz.md.shopapp.dtos.user.UserLoginDTO;
//import uz.md.shopapp.repository.RoleRepository;
//import uz.md.shopapp.repository.UserRepository;
//import uz.md.shopapp.service.contract.UserService;
//import uz.md.shopapp.util.TestUtil;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static uz.md.shopapp.controller.AuthController.LOGIN_URL;
//import static uz.md.shopapp.controller.UserController.BASE_URL;
//
///**
// * Integration tests for {@link UserController}
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//
//    @Value("${app.admin.firstName}")
//    private String firstName;
//
//    @Value("${app.admin.phoneNumber}")
//    private String phoneNumber;
//
//    @Value("${app.admin.password}")
//    private String password;
//
//    @MockBean
//    private UserService userService;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//
//    private static boolean setUpIsDone = false;
//
//    private String accessToken;
//
//
//    @BeforeEach
//    void init() throws Exception {
//        if (!setUpIsDone) {
//            addAdmin();
//            saveUserRole();
//            setUpIsDone = true;
//        }
//        accessToken = obtainAccessToken();
//    }
//
//    private void saveUserRole() {
//        roleRepository.save(
//                new Role("USER",
//                        "System USER",
//                        Set.of(PermissionEnum.GET_PRODUCT)
//                )
//        );
//    }
//
//    private void addAdmin() {
//        userRepository.save(new User(
//                firstName,
//                "",
//                phoneNumber,
//                passwordEncoder.encode(password),
//                addAdminRole(),
//                true
//        ));
//    }
//
//    private Role addAdminRole() {
//        return roleRepository.save(
//                new Role("ADMIN",
//                        "System owner",
//                        Set.of(PermissionEnum.values())
//                )
//        );
//    }
//
//    @SuppressWarnings("unchecked")
//    private String obtainAccessToken() throws Exception {
//
//        UserLoginDTO userLoginDTO = new UserLoginDTO(phoneNumber, password);
//
//        ResultActions result
//                = mvc.perform(MockMvcRequestBuilders
//                        .post(AuthController.BASE_URL + LOGIN_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userLoginDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"));
//
//        String resultString = result.andReturn().getResponse().getContentAsString();
//
//        JacksonJsonParser jsonParser = new JacksonJsonParser();
//        Map<String, String> data = (LinkedHashMap<String, String>) jsonParser.parseMap(resultString).get("data");
//        System.out.println("data = " + data);
//        return data.get("accessToken");
//    }
//
//    @Test
//    void shouldGetById() throws Exception {
//        UserDTO userDTO = new UserDTO(UUID.randomUUID(), "firstname", "lastname", "+998931668648", false, Set.of(PermissionEnum.GET_PRODUCT));
//
//        ApiResult<UserDTO> result = ApiResult.successResponse(userDTO);
//        when(userService.findById(1L)).thenReturn(result);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL + "/1")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value(userDTO.getId().toString()))
//                .andExpect(jsonPath("$.data.firstName").value(userDTO.getFirstName()))
//                .andExpect(jsonPath("$.data.lastName").value(userDTO.getLastName()))
//                .andExpect(jsonPath("$.data.phoneNumber").value(userDTO.getPhoneNumber()))
//                .andExpect(jsonPath("$.data.admin").value(userDTO.isAdmin()))
//        ;
//    }
//
//    @Test
//    void shouldDelete() throws Exception {
//
//        ApiResult<Void> result = ApiResult.successResponse();
//        when(userService.delete(ArgumentMatchers.any())).thenReturn(result);
//        mvc.perform(MockMvcRequestBuilders
//                        .delete(BASE_URL + "/delete/1")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//
//}
