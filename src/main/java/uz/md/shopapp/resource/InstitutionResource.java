package uz.md.shopapp.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.aop.annotation.CheckAuth;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institution.InstitutionAddDTO;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.dtos.institution.InstitutionEditDTO;
import uz.md.shopapp.dtos.institution.InstitutionInfoDTO;
import uz.md.shopapp.service.contract.InstitutionService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;

@RestController
@RequestMapping(InstitutionResource.BASE_URL + "/")
@RequiredArgsConstructor
@Tag(name = "Institution", description = "Endpoints for Institution")
@Slf4j
public class InstitutionResource {

    public static final String BASE_URL = AppConstants.BASE_URL + "institution";

    private final InstitutionService institutionService;

    @GetMapping
    @Operation(description = "Get all institutions")
    @CheckAuth(permission = PermissionEnum.GET_INSTITUTION)
    public ApiResult<List<InstitutionDTO>> getAll() {
        log.info("Getting all institutions");
        return institutionService.getAll();
    }

    @GetMapping("/all")
    @Operation(description = "Get all institutions")
    @CheckAuth(permission = PermissionEnum.GET_INSTITUTION)
    public ApiResult<List<InstitutionInfoDTO>> getAllForInfo() {
        log.info("getting all institutions");
        return institutionService.getAllForInfo();
    }

    @GetMapping("/by-page/{page}")
    @Operation(description = "Get all institutions by pagination")
    @CheckAuth(permission = PermissionEnum.GET_INSTITUTION)
    public ApiResult<List<InstitutionInfoDTO>> getAllForInfoByPage(@PathVariable String page) {
        log.info("getting all institutions");
        return institutionService.getAllForInfoByPage(page);
    }

    @GetMapping("/all/by_type/{id}")
    @Operation(description = "Get all institutions by type")
    @CheckAuth(permission = PermissionEnum.GET_INSTITUTION)
    public ApiResult<List<InstitutionInfoDTO>> getAllByType(@PathVariable("id") Long typeId) {
        log.info("getting all institutions by type");
        return institutionService.getAllByTypeId(typeId);
    }

    @GetMapping("/all/by_manager/{id}")
    @Operation(description = "Get all institutions by type")
    @CheckAuth(permission = PermissionEnum.GET_INSTITUTION)
    public ApiResult<List<InstitutionInfoDTO>> getAllByManager(@PathVariable("id") Long managerId) {
        log.info("getting all institutions by manager");
        return institutionService.getAllByManagerId(managerId);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get a institution by id")
    @CheckAuth(permission = PermissionEnum.GET_INSTITUTION)
    public ApiResult<InstitutionDTO> getById(@PathVariable Long id) {
        log.info("Getting institution by id: {}", id);
        return institutionService.findById(id);
    }

    @PostMapping("/add")
    @Operation(description = "Add a institution")
    @CheckAuth(permission = PermissionEnum.ADD_INSTITUTION)
    public ApiResult<InstitutionDTO> add(@RequestBody @Valid InstitutionAddDTO dto) {
        log.info("adding institution");
        log.info("Request body: {} ", dto);
        return institutionService.add(dto);
    }


    @PutMapping("/edit")
    @Operation(description = "Edit a institution")
    @CheckAuth(permission = PermissionEnum.EDIT_INSTITUTION)
    public ApiResult<InstitutionDTO> edit(@RequestBody @Valid InstitutionEditDTO editDTO) {
        log.info("editing institution");
        log.info("Request body : {} ", editDTO);
        return institutionService.edit(editDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "Delete institution")
    @CheckAuth(permission = PermissionEnum.DELETE_INSTITUTION)
    public ApiResult<Void> delete(@PathVariable Long id) {
        log.info("Deleting institution by id {}", id);
        return institutionService.delete(id);
    }


}
