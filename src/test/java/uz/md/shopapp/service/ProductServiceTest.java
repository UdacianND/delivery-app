//package uz.md.shopapp.service;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import uz.md.shopapp.IntegrationTest;
//import uz.md.shopapp.WithManager;
//import uz.md.shopapp.domain.*;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.product.ProductAddDTO;
//import uz.md.shopapp.dtos.product.ProductDTO;
//import uz.md.shopapp.dtos.product.ProductEditDTO;
//import uz.md.shopapp.dtos.request.SimpleSearchRequest;
//import uz.md.shopapp.exceptions.AlreadyExistsException;
//import uz.md.shopapp.exceptions.NotAllowedException;
//import uz.md.shopapp.exceptions.NotFoundException;
//import uz.md.shopapp.repository.*;
//import uz.md.shopapp.service.contract.ProductService;
//import uz.md.shopapp.util.Mock;
//import uz.md.shopapp.util.TestUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@IntegrationTest
//@ActiveProfiles("test")
//@Transactional
//public class ProductServiceTest {
//
//    private static final String ANOTHER_NAME = "Acer";
//    private static final String ANOTHER_DESCRIPTION = " acer ";
//    private static final Double ANOTHER_PRICE = 500.0;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ProductService productService;
//
//    private Product product;
//    private Category category;
//    private Institution institution;
//    private User manager;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private LocationRepository locationRepository;
//    @Autowired
//    private InstitutionTypeRepository institutionTypeRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private InstitutionRepository institutionRepository;
//
//
//    @BeforeEach
//    public void init() {
//        setupManager();
//        setupInstitution();
//        setupCategory();
//        product = Mock.getProduct(category);
//        productRepository.deleteAll();
//    }
//
//    private void setupInstitution() {
//        Location location = Mock.getLocation();
//        locationRepository.saveAndFlush(location);
//        InstitutionType institutionType = Mock.getInstitutionType();
//        institutionTypeRepository.saveAndFlush(institutionType);
//        institution = Mock.getInstitution(location, institutionType, manager);
//        institutionRepository.saveAndFlush(institution);
//    }
//
//    private void setupManager() {
//        Role role = roleRepository.findByName("MANAGER")
//                .orElseThrow(() -> new NotFoundException("ROLE NOT FOUND"));
//        manager = new User(
//                "Ali",
//                "Yusupov",
//                "+998941001010",
//                role);
//        userRepository.saveAndFlush(manager);
//    }
//
//    private void setupCategory() {
//        categoryRepository.deleteAll();
//        category = Mock.getCategory(institution);
//        categoryRepository.save(category);
//    }
//
//
//    private void addAdmin() {
//        userRepository.save(new User(
//                "Bobur",
//                "Yusupov",
//                "+998951111111",
//                "123",
//                roleRepository
//                        .findByName("ADMIN")
//                        .orElseThrow(() -> new NotFoundException("MANAGER ROLE NOT FOUND"))
//        ));
//    }
//
//
//    @AfterEach
//    public void destroy() {
//        productRepository.deleteAll();
//    }
//
//    // Find by id test
//
//    @Test
//    @Transactional
//    void shouldFindById() {
//
//        productRepository.saveAndFlush(product);
//        ApiResult<ProductDTO> result = productService.findById(product.getId());
//
//        assertTrue(result.isSuccess());
//        ProductDTO data = result.getData();
//        assertNotNull(data.getId());
//        assertEquals(data.getId(), product.getId());
//        assertNotNull(data.getNameUz());
//        assertEquals(data.getNameUz(), product.getNameUz());
//        assertEquals(data.getNameRu(), product.getNameRu());
//        assertEquals(data.getDescriptionUz(), product.getDescriptionUz());
//        assertEquals(data.getDescriptionRu(), product.getDescriptionRu());
//        assertEquals(data.getPrice(), product.getPrice());
//
//    }
//
//    @Test
//    @Transactional
//    void shouldNotFindNotExisted() {
//        assertThrows(NotFoundException.class, () -> productService.findById(15L));
//    }
//
//    // add test
//    @Test
//    @Transactional
//    @WithManager
//    void shouldAddProduct() {
//
//        ProductAddDTO addDTO = new ProductAddDTO(
//                " Shashlik",
//                "Shashlik",
//                " Uzbek Shashlik",
//                " Uzbek Shashlik ",
//                500.0,
//                category.getId());
//        ApiResult<ProductDTO> result = productService.add(addDTO);
//
//        assertTrue(result.isSuccess());
//        List<Product> all = productRepository.findAll();
//        Product product1 = all.get(0);
//
//        assertEquals(product1.getNameUz(), addDTO.getNameUz());
//        assertEquals(product1.getNameRu(), addDTO.getNameRu());
//        assertEquals(product1.getDescriptionUz(), addDTO.getDescriptionUz());
//        assertEquals(product1.getDescriptionRu(), addDTO.getDescriptionRu());
//        assertEquals(product1.getCategory().getId(), addDTO.getCategoryId());
//    }
//
//    @Test
//    @Transactional
//    void shouldNotAddWithoutManager() {
//        ProductAddDTO addDTO = new ProductAddDTO(
//                " Shashlik",
//                "Shashlik",
//                " Uzbek Shashlik",
//                " Uzbek Shashlik ",
//                500.0,
//                category.getId());
//        assertThrows(NotAllowedException.class, () -> productService.add(addDTO));
//    }
//
//    @Test
//    @Transactional
//    @WithManager
//    void shouldNotAddAnotherManager() {
//        institution.setManager(userRepository
//                .saveAndFlush(new User(
//                        "Bobur",
//                        "Yusupov",
//                        "+998941234567",
//                        roleRepository
//                                .findByName("MANAGER")
//                                .orElseThrow(() -> new NotFoundException("MANAGER ROLE NOT FOUND"))
//                )));
//        institutionRepository.saveAndFlush(institution);
//        ProductAddDTO addDTO = new ProductAddDTO(
//                " Shashlik",
//                "Shashlik",
//                " Uzbek Shashlik",
//                " Uzbek Shashlik ",
//                500.0,
//                category.getId());
//
//        assertThrows(NotAllowedException.class, () -> productService.add(addDTO));
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username = "+998951111111", password = "123")
//    void shouldAddAdmin() {
//        addAdmin();
//        institution.setManager(userRepository
//                .saveAndFlush(new User(
//                        "Bobur",
//                        "Yusupov",
//                        "+998941234567",
//                        "123",
//                        roleRepository
//                                .findByName("MANAGER")
//                                .orElseThrow(() -> new NotFoundException("MANAGER ROLE NOT FOUND"))
//                )));
//        institutionRepository.saveAndFlush(institution);
//        ProductAddDTO addDTO = new ProductAddDTO(
//                " Shashlik",
//                "Shashlik",
//                " Uzbek Shashlik",
//                " Uzbek Shashlik ",
//                500.0,
//                category.getId());
//        ApiResult<ProductDTO> result = productService.add(addDTO);
//
//        assertTrue(result.isSuccess());
//        List<Product> all = productRepository.findAll();
//        Product product1 = all.get(0);
//
//        assertEquals(product1.getNameUz(), addDTO.getNameUz());
//        assertEquals(product1.getNameRu(), addDTO.getNameRu());
//        assertEquals(product1.getDescriptionUz(), addDTO.getDescriptionUz());
//        assertEquals(product1.getDescriptionRu(), addDTO.getDescriptionRu());
//        assertEquals(product1.getCategory().getId(), addDTO.getCategoryId());
//    }
//
//    @Test
//    @Transactional
//    @WithManager
//    void shouldNotAddWithAlreadyExistedName() {
//        productRepository.saveAndFlush(product);
//        ProductAddDTO addDTO = new ProductAddDTO(product.getNameUz(), "", "description", "", 400.0, category.getId());
//        assertThrows(AlreadyExistsException.class, () -> productService.add(addDTO));
//    }
//
//    @Test
//    @Transactional
//    @WithManager
//    void shouldNotAddWithNotExistedCategory() {
//        productRepository.saveAndFlush(product);
//        ProductAddDTO addDTO = new ProductAddDTO("name", "", "", "description", 400.0, 20L);
//        assertThrows(NotFoundException.class, () -> productService.add(addDTO));
//    }
//
//    // Edit product test
//
//    @Test
//    @Transactional
//    @WithManager
//    void shouldEditProduct() {
//
//        productRepository.saveAndFlush(product);
//        ProductEditDTO editDTO = new ProductEditDTO(
//                "new name",
//                "new name",
//                "description",
//                "description",
//                500.0,
//                category.getId(),
//                product.getId()
//        );
//
//        ApiResult<ProductDTO> result = productService.edit(editDTO);
//
//        assertTrue(result.isSuccess());
//        ProductDTO data = result.getData();
//        assertEquals(1, productRepository.count());
//
//        assertEquals(data.getId(), editDTO.getId());
//        assertEquals(data.getNameUz(), editDTO.getNameUz());
//        assertEquals(data.getNameRu(), editDTO.getNameRu());
//        assertEquals(data.getDescriptionUz(), editDTO.getDescriptionUz());
//        assertEquals(data.getDescriptionRu(), editDTO.getDescriptionRu());
//        assertEquals(data.getPrice(), editDTO.getPrice());
//        assertEquals(data.getCategoryId(), editDTO.getCategoryId());
//
//    }
//
//    @Test
//    @Transactional
//    @WithManager
//    void shouldNotEditToAlreadyExistedName() {
//        productRepository.saveAndFlush(new Product(ANOTHER_NAME, "", null, "", ANOTHER_DESCRIPTION, ANOTHER_PRICE, category));
//        productRepository.saveAndFlush(product);
//        ProductEditDTO editDTO = new ProductEditDTO(ANOTHER_NAME, "", "", "description", 500.0, category.getId(), product.getId());
//        assertThrows(AlreadyExistsException.class, () -> productService.edit(editDTO));
//    }
//
//    @Test
//    @Transactional
//    @WithManager
//    void shouldNotEditToNotExistedCategory() {
//        productRepository.save(product);
//        ProductEditDTO editDTO = new ProductEditDTO(ANOTHER_NAME, "", "", "description", 500.0, 50L, product.getId());
//        assertThrows(NotFoundException.class, () -> productService.edit(editDTO));
//    }
//
//    @Test
//    @Transactional
//    @WithManager
//    void shouldNotFound() {
//        ProductEditDTO editDTO = new ProductEditDTO("name", "", "", "description", 400.0, category.getId(), 15L);
//        assertThrows(NotFoundException.class, () -> productService.edit(editDTO));
//    }
//
//
//    // Delete a product test
//
//    @Test
//    @Transactional
//    @WithManager
//    void shouldDeleteById() {
//        productRepository.saveAllAndFlush(TestUtil.generateMockProducts(10, category));
//        productRepository.saveAndFlush(product);
//        ApiResult<Void> delete = productService.delete(product.getId());
//        assertTrue(delete.isSuccess());
//        Optional<Product> byId = productRepository.findById(product.getId());
//        assertTrue(byId.isEmpty());
//    }
//
//    @Test
//    @WithManager
//    void shouldNotDeleteByNotExistedId() {
//        productRepository.saveAllAndFlush(TestUtil.generateMockProducts(3, category));
//        assertThrows(NotFoundException.class, () -> productService.delete(150L));
//    }
//
//
//    // Get all by category test
//
//    @Test
//    @Transactional
//    void shouldGetAllByCategory() {
//
//        productRepository.saveAllAndFlush(TestUtil.generateMockProducts(5, category));
//        ApiResult<List<ProductDTO>> result = productService.getAllByCategory(category.getId());
//        assertTrue(result.isSuccess());
//        List<ProductDTO> data = result.getData();
//        List<Product> all = productRepository.findAll();
//        TestUtil.checkProductsEquality(data, all);
//    }
//
//    @Test
//    @Transactional
//    void shouldGetAllByNotExistedCategory() {
//        assertThrows(NotFoundException.class, () -> productService.getAllByCategory(15L));
//    }
//
//    // Finds all products by simple search
//
//    @Test
//    void shouldFindProductsBySimpleSearch() {
//        productRepository.saveAllAndFlush(new ArrayList<>(List.of(
//                product,
//                new Product("Hp laptop 15dy", "", null, "", "description", 500.0, category),
//                new Product("Hp laptop 14dy", "", null, "", "description", 500.0, category),
//                new Product("Acer laptop ", "", null, "", "description", 500.0, category)
//        )));
//
//        SimpleSearchRequest searchRequest = SimpleSearchRequest
//                .builder()
//                .fields(new String[]{"nameUz", "nameRu", "descriptionUz", "descriptionRu"})
//                .key("hp")
//                .sortBy("nameUz")
//                .sortDirection(Sort.Direction.DESC)
//                .page(0)
//                .pageCount(10)
//                .build();
//
//        ApiResult<List<ProductDTO>> result = productService.findAllBySimpleSearch(searchRequest);
//
//        assertTrue(result.isSuccess());
//        List<ProductDTO> data = result.getData();
//        System.out.println("data = " + data);
//        assertEquals(2, data.size());
//
//    }
//
//}
