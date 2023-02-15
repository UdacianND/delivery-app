package uz.md.shopapp.resource;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.role.RoleAddDTO;
import uz.md.shopapp.dtos.role.RoleDTO;
import uz.md.shopapp.dtos.role.RoleEditDTO;
import uz.md.shopapp.service.contract.RoleService;
import uz.md.shopapp.utils.AppConstants;

import java.util.List;

@RestController
@RequestMapping(value = RoleResource.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Role", description = "Endpoints for Role")
@Slf4j
public class RoleResource {

    public static final String BASE_URL = AppConstants.BASE_URL + "role/";
    private final RoleService roleService;

    /**
     * Adds a role
     * @param roleAddDTO role adding params
     * @return added role
     */
    @PostMapping("add")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<RoleDTO> add(@RequestBody @Valid RoleAddDTO roleAddDTO) {
        return roleService.add(roleAddDTO);
    }

    /**
     * Gets all roles
     * @return list of roles
     */
    @GetMapping("all")
    public ApiResult<List<RoleDTO>> getAll() {
        return roleService.getAll();
    }

    /**
     * gets a role by its id
     * @param id role id
     * @return found role
     */
    @GetMapping("{id}")
    public ApiResult<RoleDTO> getById(@PathVariable Integer id) {
        return roleService.getById(id);
    }

    /**
     * Edits the role
     * @param dto role id and editing parameters
     * @return edited role
     */
    @PutMapping("edit")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<RoleDTO> edit(@RequestBody @Valid RoleEditDTO dto) {
        return roleService.edit(dto);
    }

    /**
     * deletes the role
     * @param id deleting role id
     * @param insteadOfRoleId after deletion of role instead of it this role puts
     * @return true if role was deleted successfully or else false
     */
    @DeleteMapping("delete")
    public ApiResult<Boolean> delete(@RequestParam Integer id, @RequestParam Integer insteadOfRoleId) {
        return roleService.delete(id,insteadOfRoleId);
    }


}
