package uz.md.shopapp.resource;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.aop.annotation.CheckAuth;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institutionType.InstitutionTypeAddDTO;
import uz.md.shopapp.dtos.institutionType.InstitutionTypeDTO;
import uz.md.shopapp.dtos.institutionType.InstitutionTypeEditDTO;
import uz.md.shopapp.service.contract.InstitutionTypeService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;

@RestController
@RequestMapping(InstitutionTypeResource.BASE_URL + "/")
@RequiredArgsConstructor
@Slf4j
public class InstitutionTypeResource {

    public static final String BASE_URL = AppConstants.BASE_URL + "institutionType";

    private final InstitutionTypeService institutionTypeService;

    @GetMapping
    @Operation(description = "Get all institutionTypes")
    @CheckAuth(permission = PermissionEnum.GET_INSTITUTION_TYPE)
    public ApiResult<List<InstitutionTypeDTO>> getAll() {
        log.info("Getting all institutionTypes");
        return institutionTypeService.getAll();
    }

    @GetMapping("/by-page/{page}")
    @Operation(description = "Get all institutionTypes")
    @CheckAuth(permission = PermissionEnum.GET_INSTITUTION_TYPE)
    public ApiResult<List<InstitutionTypeDTO>> getAllByPage(@PathVariable String page) {
        log.info("Getting all institutionTypes");

        return institutionTypeService.getAllByPage(page);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get an institution type by id")
    @CheckAuth(permission = PermissionEnum.GET_INSTITUTION_TYPE)
    public ApiResult<InstitutionTypeDTO> getById(@PathVariable Long id) {
        log.info("Getting institutionType by id: {}", id);
        return institutionTypeService.findById(id);
    }

    @PostMapping("/add")
    @Operation(description = "Add a institutionType")
    @CheckAuth(permission = PermissionEnum.ADD_INSTITUTION_TYPE)
    public ApiResult<InstitutionTypeDTO> add(@RequestBody @Valid InstitutionTypeAddDTO dto) {
        log.info("adding institutionType");
        log.info("Request body: {} ", dto);
        return institutionTypeService.add(dto);
    }


    @PutMapping("/edit")
    @Operation(description = "Edit a institutionType")
    @CheckAuth(permission = PermissionEnum.EDIT_INSTITUTION_TYPE)
    public ApiResult<InstitutionTypeDTO> edit(@RequestBody @Valid InstitutionTypeEditDTO editDTO) {
        log.info("editing institutionType");
        log.info("Request body : {} ", editDTO);
        return institutionTypeService.edit(editDTO);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(description = "Delete institutionType")
    @CheckAuth(permission = PermissionEnum.DELETE_INSTITUTION_TYPE)
    public ApiResult<Void> delete(@PathVariable Long id) {
        log.info("Deleting institutionType by id {}", id);
        return institutionTypeService.delete(id);
    }


}
