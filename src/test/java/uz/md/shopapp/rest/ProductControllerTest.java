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
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import uz.md.shopapp.IntegrationTest;
//import uz.md.shopapp.controller.AuthController;
//import uz.md.shopapp.controller.ProductController;
//import uz.md.shopapp.domain.Role;
//import uz.md.shopapp.domain.User;
//import uz.md.shopapp.domain.enums.PermissionEnum;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.product.ProductAddDTO;
//import uz.md.shopapp.dtos.product.ProductDTO;
//import uz.md.shopapp.dtos.product.ProductEditDTO;
//import uz.md.shopapp.dtos.user.UserLoginDTO;
//import uz.md.shopapp.repository.RoleRepository;
//import uz.md.shopapp.repository.UserRepository;
//import uz.md.shopapp.service.contract.ProductService;
//import uz.md.shopapp.util.TestUtil;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static uz.md.shopapp.controller.AuthResource.LOGIN_URL;
//import static uz.md.shopapp.controller.ProductResource.BASE_URL;
//
///**
// * Integration tests for {@link ProductController}
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class ProductControllerTest {
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
//    @Autowired
//    private MockMvc mvc;
//    @MockBean
//    private ProductService productService;
//
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private UserRepository userRepository;
//
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
//                        .post(AuthResource.BASE_URL + LOGIN_URL)
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
//
//    @Test
//    void shouldAdd() throws Exception {
//
//        ProductAddDTO addDTO = new ProductAddDTO(
//                "product",
//                "description",
//                500.0,
//                1L);
//        ProductDTO productDTO = new ProductDTO(1L, addDTO.getName(), addDTO.getDescription(), 500.0, 1L);
//
//        ApiResult<ProductDTO> result = ApiResult.successResponse(productDTO);
//
//        when(productService.add(ArgumentMatchers.any())).thenReturn(result);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .post(BASE_URL + "/add")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(addDTO)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.name").value("product"))
//                .andExpect(jsonPath("$.data.description").value("description"))
//                .andExpect(jsonPath("$.data.price").value(500.0))
//                .andExpect(jsonPath("$.data.categoryId").value(1L))
//        ;
//    }
//
//    @Test
//    void shouldGetAllByCategory() throws Exception {
//
//        List<ProductDTO> productDTOs = List.of(
//                new ProductDTO(1L, "product1", "description", 500.0, 1L),
//                new ProductDTO(2L, "product2", "description", 500.0, 1L)
//        );
//
//        ApiResult<List<ProductDTO>> result = ApiResult.successResponse(productDTOs);
//        when(productService.getAllByCategory(ArgumentMatchers.any())).thenReturn(result);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL + "/category/1")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data[0].id").value(productDTOs.get(0).getId().intValue()))
//                .andExpect(jsonPath("$.data[0].name").value(productDTOs.get(0).getName()))
//                .andExpect(jsonPath("$.data[0].description").value(productDTOs.get(0).getDescription()))
//                .andExpect(jsonPath("$.data[1].id").value(productDTOs.get(1).getId().intValue()))
//                .andExpect(jsonPath("$.data[1].name").value(productDTOs.get(1).getName()))
//                .andExpect(jsonPath("$.data[1].description").value(productDTOs.get(1).getDescription()))
//        ;
//    }
//
//    @Test
//    void shouldGetById() throws Exception {
//        ProductDTO productDTO = new ProductDTO(1L, "product2", "description", 500.0, 1L);
//
//        ApiResult<ProductDTO> result = ApiResult.successResponse(productDTO);
//        when(productService.findById(1L)).thenReturn(result);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL + "/1")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value(productDTO.getId().intValue()))
//                .andExpect(jsonPath("$.data.name").value(productDTO.getName()))
//                .andExpect(jsonPath("$.data.description").value(productDTO.getDescription()))
//                .andExpect(jsonPath("$.data.price").value(productDTO.getPrice()))
//                .andExpect(jsonPath("$.data.categoryId").value(productDTO.getCategoryId()))
//        ;
//    }
//
//    @Test
//    void shouldEdit() throws Exception {
//
//        ProductEditDTO addDTO = new ProductEditDTO(
//                1L,
//                "product",
//                "description",
//                500.0,
//                1L);
//
//        ProductDTO productDTO = new ProductDTO(1L, addDTO.getName(), addDTO.getDescription(),addDTO.getPrice(),addDTO.getCategoryId());
//        ApiResult<ProductDTO> result = ApiResult.successResponse(productDTO);
//        when(productService.edit(ArgumentMatchers.any())).thenReturn(result);
//        mvc.perform(MockMvcRequestBuilders
//                        .put(BASE_URL + "/edit")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(addDTO)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.name").value("product"))
//                .andExpect(jsonPath("$.data.description").value("description"))
//        ;
//    }
//
//    @Test
//    void shouldDelete() throws Exception {
//
//        ApiResult<Void> result = ApiResult.successResponse();
//        when(productService.delete(ArgumentMatchers.any())).thenReturn(result);
//        mvc.perform(MockMvcRequestBuilders
//                        .delete(BASE_URL + "/delete/1")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//
//}
