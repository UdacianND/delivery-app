package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.aop.annotation.CheckAuth;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryAddDTO;
import uz.md.shopapp.dtos.category.CategoryDTO;
import uz.md.shopapp.dtos.category.CategoryEditDTO;
import uz.md.shopapp.dtos.category.CategoryInfoDTO;
import uz.md.shopapp.service.contract.CategoryService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;

@RestController
@RequestMapping(CategoryController.BASE_URL + "/")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Endpoints for Category")
@Slf4j
public class CategoryController {

    public static final String BASE_URL = AppConstants.BASE_URL + "category";
    private final CategoryService categoryService;

    @GetMapping
    @Operation(description = "Get all categories")
    public ApiResult<List<CategoryDTO>> getAll() {
        log.info("Getting all categories");
        return categoryService.getAll();
    }

    @GetMapping("/all")
    @Operation(description = "Get all categories")
    public ApiResult<List<CategoryInfoDTO>> getAllForInfo() {
        log.info("getting all categories");
        return categoryService.getAllForInfo();
    }

    @GetMapping("/institution/{id}")
    @Operation(description = "Get all categories by institution")
    public ApiResult<List<CategoryInfoDTO>> getAllByInstitution(@PathVariable Long id) {
        log.info("getting all categories by institution");
        return categoryService.getAllByInstitutionId(id);
    }

    @GetMapping("/institution/{id}/by-page/{page}")
    @Operation(description = "Get all categories by institution and page")
    public ApiResult<List<CategoryInfoDTO>> getAllByInstitutionAndPage(@PathVariable Long id, @PathVariable String page) {
        log.info("getting all categories by institution");
        return categoryService.getAllByInstitutionIdAndPage(id,page);
    }


    @GetMapping("/{id}")
    @Operation(description = "Get a category by id")
    public ApiResult<CategoryDTO> getById(@PathVariable Long id) {
        log.info("Getting category by id: {}", id);
        return categoryService.findById(id);
    }

    @PostMapping("/add")
    @Operation(description = "Add a category")
    @CheckAuth(permission = PermissionEnum.ADD_CATEGORY)
    public ApiResult<CategoryDTO> add(@RequestBody @Valid CategoryAddDTO dto) {
        log.info("adding category");
        log.info("Request body: {} ", dto);
        return categoryService.add(dto);
    }

    @PutMapping("/edit")
    @Operation(description = "Edit a category")
    @CheckAuth(permission = PermissionEnum.EDIT_CATEGORY)
    public ApiResult<CategoryDTO> edit(@RequestBody @Valid CategoryEditDTO editDTO) {
        log.info("editing category");
        log.info("Request body : {} ", editDTO);
        return categoryService.edit(editDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Delete category")
    @CheckAuth(permission = PermissionEnum.DELETE_CATEGORY)
    public ApiResult<Void> delete(@PathVariable Long id) {
        log.info("Deleting category by id {}", id);
        return categoryService.delete(id);
    }

}
