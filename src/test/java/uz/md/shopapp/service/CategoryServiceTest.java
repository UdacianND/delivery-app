//package uz.md.shopapp.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import uz.md.shopapp.IntegrationTest;
//import uz.md.shopapp.domain.Category;
//import uz.md.shopapp.domain.Product;
//import uz.md.shopapp.dtos.ApiResult;
//import uz.md.shopapp.dtos.category.CategoryAddDTO;
//import uz.md.shopapp.dtos.category.CategoryDTO;
//import uz.md.shopapp.dtos.category.CategoryEditDTO;
//import uz.md.shopapp.dtos.category.CategoryInfoDTO;
//import uz.md.shopapp.dtos.product.ProductDTO;
//import uz.md.shopapp.exceptions.AlreadyExistsException;
//import uz.md.shopapp.exceptions.NotFoundException;
//import uz.md.shopapp.repository.CategoryRepository;
//import uz.md.shopapp.repository.ProductRepository;
//import uz.md.shopapp.service.contract.CategoryService;
//import uz.md.shopapp.util.TestUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@IntegrationTest
//@Transactional
//@ActiveProfiles("test")
//public class CategoryServiceTest {
//
//    private static final String DEFAULT_NAME_UZ = "Laptop uz";
//    private static final String DEFAULT_NAME_RU = "Laptop ru";
//
//    private static final String DEFAULT_DESCRIPTION_UZ = " laptops ";
//    private static final String DEFAULT_DESCRIPTION_RU = " laptops ";
//
//    private static final String ANOTHER_CATEGORY_NAME = "Mobile Devices";
//    private static final String ANOTHER_CATEGORY_DESCRIPTION = " mobile devices ";
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private CategoryService categoryService;
//
//    private Category category;
//    @Autowired
//    private ProductRepository productRepository;
//
//    @BeforeEach
//    public void init() {
//        category = new Category();
//        category.setNameUz(DEFAULT_NAME_UZ);
//        category.setNameRu(DEFAULT_NAME_RU);
//        category.setDescriptionUz(DEFAULT_DESCRIPTION_UZ);
//        category.setDescriptionRu(DEFAULT_DESCRIPTION_RU);
//        category.setDeleted(false);
//        category.setActive(true);
//        categoryRepository.deleteAll();
//    }
//
//    @Test
//    @Transactional
//    void shouldAddCategory() {
//
//        CategoryAddDTO addDTO = new CategoryAddDTO("category", "category", "description", "description");
//        ApiResult<CategoryDTO> result = categoryService.add(addDTO);
//
//        assertTrue(result.isSuccess());
//        List<Category> all = categoryRepository.findAll();
//        Category category1 = all.get(0);
//
//        assertEquals(category1.getNameUz(), addDTO.getNameUz());
//        assertEquals(category1.getNameRu(), addDTO.getNameRu());
//        assertEquals(category1.getDescriptionUz(), addDTO.getDescriptionUz());
//        assertEquals(category1.getDescriptionRu(), addDTO.getDescriptionRu());
//    }
//
//    @Test
//    @Transactional
//    void shouldNotAddWithAlreadyExistedName() {
//        categoryRepository.save(category);
//        CategoryAddDTO addDTO = new CategoryAddDTO(category.getNameUz(),category.getNameRu(), "d","d");
//        assertThrows(AlreadyExistsException.class, () -> categoryService.add(addDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldFindById() {
//
//        productRepository.deleteAll();
//
//        category.setProducts(new ArrayList<>(List.of(
//                new Product("Hp Laptop", " desc ", 600.0, category),
//                new Product("Acer nitro", " desc ", 600.0, category),
//                new Product("Asus vivobook", " desc ", 600.0, category)
//        )));
//
//        categoryRepository.saveAndFlush(category);
//        ApiResult<CategoryDTO> result = categoryService.findById(category.getId());
//        assertTrue(result.isSuccess());
//        CategoryDTO data = result.getData();
//        assertNotNull(data.getId());
//        assertEquals(data.getId(), category.getId());
//        assertNotNull(data.getName());
//        assertEquals(data.getName(), category.getName());
//        assertNotNull(data.getDescription());
//        assertEquals(data.getDescription(), category.getDescription());
//        List<ProductDTO> products = data.getProducts();
//
//        List<Product> all = productRepository.findAll();
//        TestUtil.checkProductsEquality(products, all);
//    }
//
//    @Test
//    void shouldNotFindNotExisted() {
//        assertThrows(NotFoundException.class, () -> categoryService.findById(10L));
//    }
//
//    @Test
//    @Transactional
//    void shouldEditCategory() {
//
//        categoryRepository.save(category);
//        CategoryEditDTO editDTO = new CategoryEditDTO(category.getId(), "new name", "description");
//        ApiResult<CategoryDTO> result = categoryService.edit(editDTO);
//
//        assertTrue(result.isSuccess());
//        CategoryDTO data = result.getData();
//        assertEquals(1, categoryRepository.count());
//
//        assertEquals(data.getId(), editDTO.getId());
//        assertEquals(data.getName(), editDTO.getName());
//        assertEquals(data.getDescription(), editDTO.getDescription());
//
//    }
//
//    @Test
//    @Transactional
//    void shouldNotEditToAlreadyExistedName() {
//        Category another = new Category(ANOTHER_CATEGORY_NAME, ANOTHER_CATEGORY_DESCRIPTION);
//        categoryRepository.saveAndFlush(another);
//        categoryRepository.saveAndFlush(category);
//        CategoryEditDTO editDTO = new CategoryEditDTO(category.getId(), another.getName(), "description");
//        assertThrows(AlreadyExistsException.class, () -> categoryService.edit(editDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldNotFoundNotExisted() {
//        CategoryEditDTO editDTO = new CategoryEditDTO(15L, "name", "description");
//        assertThrows(NotFoundException.class, () -> categoryService.edit(editDTO));
//    }
//
//    @Test
//    @Transactional
//    void shouldNotChangeProductsIfEditCategory() {
//
//        productRepository.deleteAll();
//        category.setProducts(new ArrayList<>(List.of(
//                new Product("Hp Laptop", " desc ", 600.0, category),
//                new Product("Acer nitro", " desc ", 600.0, category),
//                new Product("Asus vivobook", " desc ", 600.0, category)
//        )));
//        categoryRepository.saveAndFlush(category);
//        CategoryEditDTO editDTO = new CategoryEditDTO(category.getId(), "new name", "description");
//        ApiResult<CategoryDTO> result = categoryService.edit(editDTO);
//
//        assertTrue(result.isSuccess());
//        CategoryDTO data = result.getData();
//        assertEquals(1, categoryRepository.count());
//
//        assertEquals(data.getId(), editDTO.getId());
//        assertEquals(data.getName(), editDTO.getName());
//        assertEquals(data.getDescription(), editDTO.getDescription());
//
//        List<ProductDTO> products = data.getProducts();
//        List<Product> all = productRepository.findAll();
//        TestUtil.checkProductsEquality(products, all);
//    }
//
//    @Test
//    @Transactional
//    void shouldGetAll() {
//
//        categoryRepository.saveAll(List.of(
//                category,
//                new Category("category1", "description"),
//                new Category("category2", "description"),
//                new Category("category3", "description")
//        ));
//        ApiResult<List<CategoryDTO>> result = categoryService.getAll();
//        assertTrue(result.isSuccess());
//        List<CategoryDTO> data = result.getData();
//        List<Category> all = categoryRepository.findAll();
//        TestUtil.checkCategoriesEquality(data, all);
//    }
//
//    @Test
//    @Transactional
//    void shouldGetAllForInfo() {
//
//        categoryRepository.saveAll(new ArrayList<>(List.of(
//                category,
//                new Category("category1", "description"),
//                new Category("category2", "description"),
//                new Category("category3", "description")
//        )));
//        ApiResult<List<CategoryInfoDTO>> result = categoryService.getAllForInfo();
//        assertTrue(result.isSuccess());
//        List<CategoryInfoDTO> data = result.getData();
//        List<Category> all = categoryRepository.findAll();
//        TestUtil.checkCategoriesInfoEquality(data, all);
//    }
//
//    @Test
//    void shouldDeleteById() {
//
//        categoryRepository.saveAllAndFlush(new ArrayList<>(List.of(
//                category,
//                new Category("category1", "description"),
//                new Category("category2", "description"),
//                new Category("category3", "description")
//        )));
//
//        ApiResult<Void> delete = categoryService.delete(category.getId());
//        assertTrue(delete.isSuccess());
//        Optional<Category> byId = categoryRepository.findById(category.getId());
//        assertTrue(byId.isEmpty());
//
//    }
//
//    @Test
//    void shouldNotDeleteByIdNotExisted() {
//
//        categoryRepository.saveAll(new ArrayList<>(List.of(
//                new Category("category1", "description"),
//                new Category("category2", "description"),
//                new Category("category3", "description")
//        )));
//
//        assertThrows(NotFoundException.class, () -> categoryService.delete(150L));
//
//    }
//
//    @Test
//    void shouldDeleteCategoryAndItsProducts() {
//        productRepository.deleteAll();
//        category.setProducts(new ArrayList<>(List.of(
//                new Product("Hp Laptop", " desc ", 600.0, category),
//                new Product("Acer nitro", " desc ", 600.0, category),
//                new Product("Asus vivobook", " desc ", 600.0, category)
//        )));
//        categoryRepository.saveAndFlush(category);
//        ApiResult<Void> delete = categoryService.delete(category.getId());
//        assertTrue(delete.isSuccess());
//        assertEquals(0, productRepository.count());
//    }
//
//}
