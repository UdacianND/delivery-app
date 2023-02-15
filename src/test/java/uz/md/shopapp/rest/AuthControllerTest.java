//package uz.md.shopapp.rest;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import uz.md.shopapp.IntegrationTest;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.TokenDTO;
//import uz.md.shopapp.dtos.user.ClientLoginDTO;
//import uz.md.shopapp.dtos.user.ClientRegisterDTO;
//import uz.md.shopapp.resource.AuthResource;
//import uz.md.shopapp.service.contract.AuthService;
//import uz.md.shopapp.util.TestUtil;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static uz.md.shopapp.resource.AuthResource.*;
///**
// * Integration tests for {@link AuthResource}
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
//        ClientRegisterDTO registerDTO = new ClientRegisterDTO(
//                "user1",
//                "user1",
//                "+998931112233");
//
//        ApiResult<Void> result = ApiResult.successResponse();
//        when(authService.register(ArgumentMatchers.any())).thenReturn(result);
//        mvc.perform(MockMvcRequestBuilders
//                        .post(BASE_URL + REGISTER_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(registerDTO)))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldClientLogin() throws Exception {
//        ClientLoginDTO userLoginDTO = new ClientLoginDTO("yusupov@gmail.com", "2002");
//        TokenDTO tokenDTO = new TokenDTO();
//        when(authService.loginClient(userLoginDTO)).thenReturn(ApiResult.successResponse(tokenDTO));
//        mvc.perform(MockMvcRequestBuilders
//                        .post(BASE_URL + "/client/sign-in")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userLoginDTO)))
//                .andExpect(status().isOk());
//    }
//
//
//}
