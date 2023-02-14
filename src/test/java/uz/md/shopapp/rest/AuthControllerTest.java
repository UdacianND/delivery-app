//package uz.md.shopapp.rest;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import uz.md.shopapp.IntegrationTest;
//import uz.md.shopapp.controller.AuthController;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.TokenDTO;
//import uz.md.shopapp.dtos.user.UserLoginDTO;
//import uz.md.shopapp.dtos.user.UserRegisterDTO;
//import uz.md.shopapp.service.contract.AuthService;
//import uz.md.shopapp.util.TestUtil;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static uz.md.shopapp.controller.AuthController.*;
///**
// * Integration tests for {@link AuthController}
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private AuthService authService;
//
//    @Test
//    void shouldRegister() throws Exception {
//
//        UserRegisterDTO registerDTO = new UserRegisterDTO(
//                "user1",
//                "user1",
//                "+998931112233",
//                "123");
//
//        ApiResult<Void> result = ApiResult.successResponse();
//        when(authService.register(ArgumentMatchers.any())).thenReturn(result);
//        mvc.perform(MockMvcRequestBuilders
//                        .post(BASE_URL+REGISTER_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(registerDTO)))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldLogin() throws Exception {
//        UserLoginDTO userLoginDTO = new UserLoginDTO("yusupov@gmail.com", "123");
//        TokenDTO tokenDTO = new TokenDTO();
//
//        when(authService.login(userLoginDTO)).thenReturn(ApiResult.successResponse(tokenDTO));
//        mvc.perform(MockMvcRequestBuilders
//                        .post(BASE_URL+LOGIN_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userLoginDTO)))
//                .andExpect(status().isOk());
//    }
//
//
//
//
//}
