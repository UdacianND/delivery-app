//package uz.md.shopapp.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import uz.md.shopapp.IntegrationTest;
//import uz.md.shopapp.domain.Category;
//import uz.md.shopapp.domain.Product;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.product.ProductAddDTO;
//import uz.md.shopapp.dtos.product.ProductDTO;
//import uz.md.shopapp.dtos.product.ProductEditDTO;
//import uz.md.shopapp.dtos.request.SimpleSearchRequest;
//import uz.md.shopapp.exceptions.AlreadyExistsException;
//import uz.md.shopapp.exceptions.NotFoundException;
//import uz.md.shopapp.repository.CategoryRepository;
//import uz.md.shopapp.repository.ProductRepository;
//import uz.md.shopapp.service.contract.ProductService;
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
//
//    private static final String CATEGORY_NAME = "Laptop";
//    private static final String CATEGORY_DESCRIPTION = " laptops ";
//
//    private static final String DEFAULT_NAME = "HP";
//    private static final String DEFAULT_DESCRIPTION = " hp ";
//    private static final Double DEFAULT_PRICE = 500.0;
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
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//
//    @BeforeEach
//    public void init() {
//        saveCategory();
//        product = new Product();
//        product.setName(DEFAULT_NAME);
//        product.setDescription(DEFAULT_DESCRIPTION);
//        product.setPrice(DEFAULT_PRICE);
//        product.setDeleted(false);
//        product.setActive(true);
//        product.setCategory(category);
//        productRepository.deleteAll();
//    }
//
//    private void saveCategory() {
//        categoryRepository.deleteAll();
//        category = new Category(CATEGORY_NAME, CATEGORY_DESCRIPTION);
//        categoryRepository.save(category);
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
//        assertNotNull(data.getName());
//        assertEquals(data.getName(), product.getName());
//        assertNotNull(data.getDescription());
//        assertEquals(data.getDescription(), product.getDescription());
//        assertNotNull(data.getPrice());
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
//    void shouldAddProduct() {
//
//        ProductAddDTO addDTO = new ProductAddDTO("product", "description", 500.0, category.getId());
//        ApiResult<ProductDTO> result = productService.add(addDTO);
//
//        assertTrue(result.isSuccess());
//        List<Product> all = productRepository.findAll();
//        Product product1 = all.get(0);
//
//        assertEquals(product1.getName(), addDTO.getName());
//        assertEquals(product1.getDescription(), addDTO.getDescription());
//        assertEquals(product1.getCategory().getId(), addDTO.getCategoryId());
//    }
//
//    @Test
//    @Transactional
//    void shouldNotAddWithAlreadyExistedName() {
//        productRepository.saveAndFlush(product);
//        ProductAddDTO addDTO = new ProductAddDTO(product.getName(), "description", 400.0, category.getId());
//        assertThrows(AlreadyExistsException.class, () -> productService.add(addDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldNotAddWithNotExistedCategory() {
//        productRepository.saveAndFlush(product);
//        ProductAddDTO addDTO = new ProductAddDTO("name", "description", 400.0, 20L);
//        assertThrows(NotFoundException.class, () -> productService.add(addDTO));
//    }
//
//    // Edit product test
//
//    @Test
//    @Transactional
//    void shouldEditProduct() {
//
//        productRepository.saveAndFlush(product);
//        ProductEditDTO editDTO = new ProductEditDTO(
//                product.getId(),
//                "new name",
//                "description",
//                500.0,
//                category.getId());
//
//        ApiResult<ProductDTO> result = productService.edit(editDTO);
//
//        assertTrue(result.isSuccess());
//        ProductDTO data = result.getData();
//        assertEquals(1, productRepository.count());
//
//        assertEquals(data.getId(), editDTO.getId());
//        assertEquals(data.getName(), editDTO.getName());
//        assertEquals(data.getDescription(), editDTO.getDescription());
//        assertEquals(data.getPrice(), editDTO.getPrice());
//        assertEquals(data.getCategoryId(), editDTO.getCategoryId());
//
//    }
//
//    @Test
//    @Transactional
//    void shouldNotEditToAlreadyExistedName() {
//        productRepository.saveAndFlush(new Product(ANOTHER_NAME, ANOTHER_DESCRIPTION, ANOTHER_PRICE, category));
//        productRepository.saveAndFlush(product);
//        ProductEditDTO editDTO = new ProductEditDTO(product.getId(), ANOTHER_NAME, "description", 500.0, category.getId());
//        assertThrows(AlreadyExistsException.class, () -> productService.edit(editDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldNotEditToNotExistedCategory() {
//        productRepository.save(product);
//        ProductEditDTO editDTO = new ProductEditDTO(product.getId(), ANOTHER_NAME, "description", 500.0, 50L);
//        assertThrows(NotFoundException.class, () -> productService.edit(editDTO));
//
//    }
//
//    @Test
//    @Transactional
//    void shouldNotFound() {
//        ProductEditDTO editDTO = new ProductEditDTO(15L, "name", "description", 400.0, category.getId());
//        assertThrows(NotFoundException.class, () -> productService.edit(editDTO));
//    }
//
//
//    // Delete a product test
//
//    @Test
//    void shouldDeleteById() {
//
//        productRepository.saveAllAndFlush(new ArrayList<>(List.of(
//                product,
//                new Product("product1", "description", 500.0, category),
//                new Product("product2", "description", 500.0, category),
//                new Product("product3", "description", 500.0, category)
//        )));
//
//        ApiResult<Void> delete = productService.delete(product.getId());
//        assertTrue(delete.isSuccess());
//        Optional<Product> byId = productRepository.findById(product.getId());
//        assertTrue(byId.isEmpty());
//
//    }
//
//    @Test
//    void shouldNotDeleteByNotExistedId() {
//
//        productRepository.saveAllAndFlush(new ArrayList<>(List.of(
//                new Product("product1", "description", 500.0, category),
//                new Product("product2", "description", 500.0, category),
//                new Product("product3", "description", 500.0, category)
//        )));
//
//        assertThrows(NotFoundException.class, () -> productService.delete(150L));
//
//    }
//
//
//    // Get all by category test
//
//    @Test
//    @Transactional
//    void shouldGetAllByCategory() {
//
//        productRepository.saveAllAndFlush(new ArrayList<>(List.of(
//                product,
//                new Product("product1", "description", 500.0, category),
//                new Product("product2", "description", 500.0, category),
//                new Product("product3", "description", 500.0, category)
//        )));
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
//                new Product("Hp laptop 15dy", "description", 500.0, category),
//                new Product("Hp laptop 14dy", "description", 500.0, category),
//                new Product("Acer laptop ", "description", 500.0, category)
//        )));
//
//        SimpleSearchRequest searchRequest = SimpleSearchRequest
//                .builder()
//                .fields(new String[]{"name", "description"})
//                .key("hp")
//                .sortBy("name")
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
//        assertEquals(3, data.size());
//
//    }
//
//}
