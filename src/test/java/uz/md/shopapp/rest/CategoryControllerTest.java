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
//import uz.md.shopapp.controller.CategoryController;
//import uz.md.shopapp.domain.Role;
//import uz.md.shopapp.domain.User;
//import uz.md.shopapp.domain.enums.PermissionEnum;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.category.CategoryAddDTO;
//import uz.md.shopapp.dtos.category.CategoryDTO;
//import uz.md.shopapp.dtos.category.CategoryEditDTO;
//import uz.md.shopapp.dtos.category.CategoryInfoDTO;
//import uz.md.shopapp.dtos.user.UserLoginDTO;
//import uz.md.shopapp.repository.RoleRepository;
//import uz.md.shopapp.repository.UserRepository;
//import uz.md.shopapp.service.contract.CategoryService;
//import uz.md.shopapp.util.TestUtil;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static uz.md.shopapp.controller.AuthResource.LOGIN_URL;
//import static uz.md.shopapp.controller.CategoryResource.BASE_URL;
//
///**
// * Integration tests for {@link CategoryController}
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class CategoryControllerTest {
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
//    @Autowired
//    private MockMvc mvc;
//    @MockBean
//    private CategoryService categoryService;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private static boolean setUpIsDone = false;
//    private String accessToken;
//
//    @BeforeEach
//    void init() throws Exception {
//        if (!setUpIsDone) {
//            addAdmin();
//            saveUserRole();
//            setUpIsDone = true;
//        }
//
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
//    private String obtainAccessToken(String phoneNumber, String password) throws Exception {
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
//        accessToken = obtainAccessToken(phoneNumber, password);
//        CategoryAddDTO addDTO = new CategoryAddDTO(
//                "category",
//                "description");
//        CategoryDTO categoryDTO = new CategoryDTO(1L, addDTO.getName(), addDTO.getDescription());
//        ApiResult<CategoryDTO> result = ApiResult.successResponse(categoryDTO);
//        when(categoryService.add(ArgumentMatchers.any())).thenReturn(result);
//
//        mvc.perform(post(BASE_URL + "/add")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(addDTO)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.name").value("category"))
//                .andExpect(jsonPath("$.data.description").value("description"))
//        ;
//    }
//
//    @Test
//    void shouldNotAddWithOutPermission() throws Exception {
//
//        Role role = roleRepository.saveAndFlush(new Role("USER", "description", Set.of(PermissionEnum.GET_CATEGORY)));
//        userRepository.saveAndFlush(new User("ali", "ali", "+998931001122", passwordEncoder.encode("123"), role, true));
//
//        accessToken = obtainAccessToken("+998931001122", "123");
//        CategoryAddDTO addDTO = new CategoryAddDTO(
//                "category",
//                "description");
//
//        mvc.perform(post(BASE_URL + "/add")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(addDTO)))
//                .andDo(print())
//                .andExpect(status().is4xxClientError())
//        ;
//    }
//
//    @Test
//    void shouldGetAll() throws Exception {
//        accessToken = obtainAccessToken(phoneNumber, password);
//        List<CategoryDTO> categoryDTOs = List.of(
//                new CategoryDTO(1L, "category1", "description"),
//                new CategoryDTO(2L, "category2", "description")
//        );
//
//        ApiResult<List<CategoryDTO>> result = ApiResult.successResponse(categoryDTOs);
//        when(categoryService.getAll()).thenReturn(result);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL + "/")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data[0].id").value(categoryDTOs.get(0).getId().intValue()))
//                .andExpect(jsonPath("$.data[0].name").value(categoryDTOs.get(0).getName()))
//                .andExpect(jsonPath("$.data[0].description").value(categoryDTOs.get(0).getDescription()))
//                .andExpect(jsonPath("$.data[1].id").value(categoryDTOs.get(1).getId().intValue()))
//                .andExpect(jsonPath("$.data[1].name").value(categoryDTOs.get(1).getName()))
//                .andExpect(jsonPath("$.data[1].description").value(categoryDTOs.get(1).getDescription()))
//        ;
//    }
//
//    @Test
//    void shouldGetAllForInfo() throws Exception {
//        accessToken = obtainAccessToken(phoneNumber, password);
//        List<CategoryInfoDTO> categoryDTOs = List.of(
//                new CategoryInfoDTO(1L, "category1", "description"),
//                new CategoryInfoDTO(2L, "category2", "description")
//        );
//
//        ApiResult<List<CategoryInfoDTO>> result = ApiResult.successResponse(categoryDTOs);
//        when(categoryService.getAllForInfo()).thenReturn(result);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL + "/all")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data[0].id").value(categoryDTOs.get(0).getId().intValue()))
//                .andExpect(jsonPath("$.data[0].name").value(categoryDTOs.get(0).getName()))
//                .andExpect(jsonPath("$.data[0].description").value(categoryDTOs.get(0).getDescription()))
//                .andExpect(jsonPath("$.data[1].id").value(categoryDTOs.get(1).getId().intValue()))
//                .andExpect(jsonPath("$.data[1].name").value(categoryDTOs.get(1).getName()))
//                .andExpect(jsonPath("$.data[1].description").value(categoryDTOs.get(1).getDescription()))
//        ;
//    }
//
//    @Test
//    void shouldGetById() throws Exception {
//        accessToken = obtainAccessToken(phoneNumber, password);
//        CategoryDTO categoryDTO = new CategoryDTO(1L, "category2", "description");
//
//        ApiResult<CategoryDTO> result = ApiResult.successResponse(categoryDTO);
//        when(categoryService.findById(1L)).thenReturn(result);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get(BASE_URL + "/1")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value(categoryDTO.getId().intValue()))
//                .andExpect(jsonPath("$.data.name").value(categoryDTO.getName()))
//                .andExpect(jsonPath("$.data.description").value(categoryDTO.getDescription()))
//        ;
//    }
//
//    @Test
//    void shouldEdit() throws Exception {
//
//        accessToken = obtainAccessToken(phoneNumber, password);
//        CategoryEditDTO addDTO = new CategoryEditDTO(
//                1L,
//                "category",
//                "description");
//
//        CategoryDTO categoryDTO = new CategoryDTO(1L, addDTO.getName(), addDTO.getDescription());
//
//        ApiResult<CategoryDTO> result = ApiResult.successResponse(categoryDTO);
//
//
//        when(categoryService.edit(ArgumentMatchers.any())).thenReturn(result);
//        mvc.perform(MockMvcRequestBuilders
//                        .put(BASE_URL + "/edit")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(addDTO)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.name").value("category"))
//                .andExpect(jsonPath("$.data.description").value("description"))
//        ;
//    }
//
//    @Test
//    void shouldDelete() throws Exception {
//
//        accessToken = obtainAccessToken(phoneNumber, password);
//        ApiResult<Void> result = ApiResult.successResponse();
//        when(categoryService.delete(ArgumentMatchers.any())).thenReturn(result);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .delete(BASE_URL + "/delete/1")
//                        .header("Authorization", "Bearer " + accessToken))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//
//}
