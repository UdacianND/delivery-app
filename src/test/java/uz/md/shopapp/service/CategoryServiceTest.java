package uz.md.shopapp.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uz.md.shopapp.domain.*;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryAddDTO;
import uz.md.shopapp.dtos.category.CategoryDTO;
import uz.md.shopapp.dtos.category.CategoryEditDTO;
import uz.md.shopapp.dtos.category.CategoryInfoDTO;
import uz.md.shopapp.dtos.product.ProductDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.repository.*;
import uz.md.shopapp.service.contract.CategoryService;
import uz.md.shopapp.util.Mock;
import uz.md.shopapp.util.TestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    private Category category;

    private Institution institution;
    private InstitutionType institutionType;
    private User manager;
    private Location location;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InstitutionTypeRepository institutionTypeRepository;
    @Autowired
    private LocationRepository locationRepository;

    @BeforeEach
    public void init() {
        setupType();
        setupLocation();
        setupManager();
        setupInstitution();
        category = Mock.getCategory(institution);
        categoryRepository.deleteAll();
    }

    @AfterEach
    public void destroy() {
        categoryRepository.deleteAll();
    }

    private void setupManager() {
        Role role = roleRepository.findByName("MANAGER")
                .orElseThrow(() -> new NotFoundException("ROLE NOT FOUND", ""));
        manager = new User(
                "Ali",
                "Yusupov",
                "+998941001010",
                role);
        userRepository.saveAndFlush(manager);
    }

    private void setupType() {
        institutionType = Mock.getInstitutionType();
        institutionTypeRepository.saveAndFlush(institutionType);
    }

    private void setupLocation() {
        location = Mock.getLocation();
        locationRepository.saveAndFlush(location);
    }

    private void setupInstitution() {
        institution = Mock.getInstitution(location, institutionType, manager);
        institutionRepository.saveAndFlush(institution);
    }

    @Test
    @Transactional
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldAddCategory() {

        CategoryAddDTO addDTO = new CategoryAddDTO("category",
                "category",
                "description",
                "description",
                institution.getId());

        ApiResult<CategoryDTO> result = categoryService.add(addDTO);

        assertTrue(result.isSuccess());
        List<Category> all = categoryRepository.findAll();
        Category category1 = all.get(0);

        assertEquals(category1.getNameUz(), addDTO.getNameUz());
        assertEquals(category1.getNameRu(), addDTO.getNameRu());
        assertEquals(category1.getDescriptionUz(), addDTO.getDescriptionUz());
        assertEquals(category1.getDescriptionRu(), addDTO.getDescriptionRu());
        assertEquals(category1.getInstitution().getId(), addDTO.getInstitutionId());
    }

    @Test
    @Transactional
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldNotAddWithoutInstitution() {
        CategoryAddDTO addDTO = new CategoryAddDTO("category",
                "category",
                "description",
                "description",
                150L);
        assertThrows(NotFoundException.class, () -> categoryService.add(addDTO));
    }


    @Test
    @Transactional
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldNotAddWithAlreadyExistedName() {
        categoryRepository.save(category);
        CategoryAddDTO addDTO = new CategoryAddDTO(category.getNameUz(), category.getNameRu(), "d", "d", institution.getId());
        assertThrows(AlreadyExistsException.class, () -> categoryService.add(addDTO));
    }

    // TODO: 2/18/2023 Add mock user to security context and test for permission to add

    @Test
    @Transactional
    void shouldFindById() {

        productRepository.deleteAll();
        categoryRepository.save(category);

        category.setProducts(TestUtil
                .generateMockProducts(5, category));

        categoryRepository.saveAndFlush(category);
        ApiResult<CategoryDTO> result = categoryService.findById(category.getId());
        assertTrue(result.isSuccess());
        CategoryDTO data = result.getData();
        assertNotNull(data.getId());
        assertEquals(data.getId(), category.getId());

        List<ProductDTO> products = data.getProducts();

        List<Product> all = productRepository.findAll();
        TestUtil.checkProductsEquality(products, all);
    }

    @Test
    void shouldNotFindNotExisted() {
        assertThrows(NotFoundException.class, () -> categoryService.findById(10L));
    }

    @Test
    @Transactional
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldEditCategory() {

        categoryRepository.save(category);

        CategoryEditDTO editDTO = new CategoryEditDTO("new name uz",
                "new name uz",
                "description",
                "description",
                institution.getId(),
                category.getId());

        ApiResult<CategoryDTO> result = categoryService.edit(editDTO);

        assertTrue(result.isSuccess());
        CategoryDTO data = result.getData();
        assertEquals(1, categoryRepository.count());

        assertEquals(data.getId(), editDTO.getId());
        assertEquals(data.getNameUz(), editDTO.getNameUz());
        assertEquals(data.getNameRu(), editDTO.getNameRu());
        assertEquals(data.getDescriptionUz(), editDTO.getDescriptionUz());
        assertEquals(data.getDescriptionRu(), editDTO.getDescriptionRu());
        assertEquals(data.getInstitutionId(), editDTO.getInstitutionId());

    }

    @Test
    @Transactional
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldNotEditCategoryWithoutInstitution() {
        categoryRepository.save(category);
        CategoryEditDTO editDTO = new CategoryEditDTO("new name uz",
                "new name uz",
                "description",
                "description",
                15L,
                category.getId());
        assertThrows(NotFoundException.class, () -> categoryService.edit(editDTO));
    }


    @Test
    @Transactional
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldNotEditToAlreadyExistedName() {

        Category another = new Category("",
                "existed",
                "",
                "",
                null,
                institution);

        categoryRepository.saveAndFlush(another);
        categoryRepository.saveAndFlush(category);
        CategoryEditDTO editDTO = new CategoryEditDTO(
                another.getNameUz(),
                category.getNameRu(),
                category.getDescriptionUz(),
                category.getDescriptionRu(),
                institution.getId(),
                category.getId());
        assertThrows(AlreadyExistsException.class, () -> categoryService.edit(editDTO));
    }

    @Test
    @Transactional
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldNotFindWithNotExistedId() {
        CategoryEditDTO editDTO = new CategoryEditDTO("name",
                "",
                "",
                "description",
                institution.getId(),
                15L);
        assertThrows(NotFoundException.class, () -> categoryService.edit(editDTO));
    }

    @Test
    @Transactional
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldNotChangeProductsWhenCategoryEdited() {
        productRepository.deleteAll();
        categoryRepository.save(category);
        List<Product> products1 = TestUtil.generateMockProducts(10, category);
        category.setProducts(products1);
        categoryRepository.saveAndFlush(category);
        CategoryEditDTO editDTO = new CategoryEditDTO("new name", "", "", "description", institution.getId(), category.getId());
        ApiResult<CategoryDTO> result = categoryService.edit(editDTO);

        assertTrue(result.isSuccess());
        CategoryDTO data = result.getData();
        assertEquals(1, categoryRepository.count());

        assertEquals(data.getId(), editDTO.getId());
        assertEquals(data.getNameUz(), editDTO.getNameUz());
        assertEquals(data.getNameRu(), editDTO.getNameRu());
        assertEquals(data.getDescriptionUz(), editDTO.getDescriptionUz());
        assertEquals(data.getDescriptionRu(), editDTO.getDescriptionRu());

        List<ProductDTO> products = data.getProducts();
        List<Product> all = productRepository.findAll();
        TestUtil.checkProductsEquality(products, all);
    }

    @Test
    @Transactional
    void shouldGetAll() {
        categoryRepository.saveAllAndFlush(TestUtil.generateMockCategories(10, institution));
        ApiResult<List<CategoryDTO>> result = categoryService.getAll();
        assertTrue(result.isSuccess());
        List<CategoryDTO> data = result.getData();
        List<Category> all = categoryRepository.findAll();
        TestUtil.checkCategoriesEquality(data, all);
    }

    @Test
    @Transactional
    void shouldGetAllForInfo() {
        categoryRepository.saveAllAndFlush(TestUtil.generateMockCategories(10, institution));
        ApiResult<List<CategoryInfoDTO>> result = categoryService.getAllForInfo();
        assertTrue(result.isSuccess());
        List<CategoryInfoDTO> data = result.getData();
        List<Category> all = categoryRepository.findAll();
        TestUtil.checkCategoriesInfoEquality(data, all);
    }

    @Test
    @Transactional
    void shouldGetAllForInfoByInstitutionId() {
        categoryRepository.saveAllAndFlush(TestUtil.generateMockCategories(6, institution));

        Institution another = institutionRepository
                .saveAndFlush(new Institution(
                        "another",
                        "another",
                        "",
                        "",
                        "",
                        new Location(12.0, 12.0),
                        institutionType,
                        manager));

        categoryRepository
                .saveAndFlush(new Category(
                        "drinks",
                        "drinks",
                        "",
                        "",
                        new ArrayList<>(),
                        another));

        ApiResult<List<CategoryInfoDTO>> result = categoryService
                .getAllByInstitutionId(institution.getId());

        assertTrue(result.isSuccess());
        List<CategoryInfoDTO> data = result.getData();
        List<Category> all = categoryRepository.findAll();
        TestUtil.checkCategoriesInfoEquality(data, all.subList(0, 6));
    }

    @Test
    @Transactional
    void shouldGetAllByInstitutionIdAndPage() {

        Institution another = institutionRepository
                .saveAndFlush(new Institution(
                        "another",
                        "another",
                        "",
                        "",
                        "",
                        locationRepository
                                .saveAndFlush(new Location(15.0, 18.0)),
                        institutionType,
                        manager));

        categoryRepository
                .saveAllAndFlush(TestUtil
                        .generateMockCategories(5, another));

        categoryRepository.saveAllAndFlush(TestUtil
                .generateMockCategories(10, institution));

        ApiResult<List<CategoryInfoDTO>> result = categoryService
                .getAllByInstitutionIdAndPage(institution.getId(), "0-4");

        assertTrue(result.isSuccess());
        List<CategoryInfoDTO> data = result.getData();
        List<Category> all = categoryRepository.findAll();
        TestUtil.checkCategoriesInfoEquality(data, all.subList(5, 9));
    }

    @Test
    @Transactional
    void shouldGetAllByInstitutionIdAndPage2() {

        categoryRepository.saveAllAndFlush(TestUtil
                .generateMockCategories(10, institution));

        Institution another = institutionRepository
                .saveAndFlush(new Institution(
                        "another",
                        "another",
                        "",
                        "",
                        "",
                        locationRepository
                                .saveAndFlush(new Location(15.0, 18.0)),
                        institutionType,
                        manager));

        categoryRepository
                .saveAllAndFlush(TestUtil
                        .generateMockCategories(5, another));

        ApiResult<List<CategoryInfoDTO>> result = categoryService
                .getAllByInstitutionIdAndPage(institution.getId(), "1-4");

        assertTrue(result.isSuccess());
        List<CategoryInfoDTO> data = result.getData();
        List<Category> all = categoryRepository.findAll();
        TestUtil.checkCategoriesInfoEquality(data, all.subList(4, 8));
    }

    @Test
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldDeleteById() {
        categoryRepository.saveAllAndFlush(TestUtil.generateMockCategories(10, institution));
        categoryRepository.saveAndFlush(category);
        ApiResult<Void> delete = categoryService.delete(category.getId());
        assertTrue(delete.isSuccess());
        Optional<Category> byId = categoryRepository.findById(category.getId());
        assertTrue(byId.isEmpty());
    }

    @Test
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldNotDeleteByIdNotExisted() {
        categoryRepository.saveAllAndFlush(TestUtil.generateMockCategories(3, institution));
        assertThrows(NotFoundException.class, () -> categoryService.delete(150L));
    }

    @Test
    @WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY, GET_CATEGORY"})
    void shouldDeleteCategoryAndItsProducts() {
        productRepository.deleteAll();
        categoryRepository.saveAndFlush(category);
        category.setProducts(
                TestUtil.generateMockProducts(5, category)
        );
        categoryRepository.saveAndFlush(category);
        ApiResult<Void> delete = categoryService.delete(category.getId());
        assertTrue(delete.isSuccess());
        assertEquals(0, productRepository.count());
    }

}
