package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.role.RoleAddDTO;
import uz.md.shopapp.dtos.role.RoleDTO;
import uz.md.shopapp.dtos.role.RoleEditDTO;

import java.util.List;

public interface RoleService {

    ApiResult<RoleDTO> add(RoleAddDTO roleAddDTO);

    ApiResult<List<RoleDTO>> getAll();

    ApiResult<RoleDTO> getById(Integer id);

    ApiResult<RoleDTO> edit(RoleEditDTO dto);

    ApiResult<Boolean> delete(Integer id, Integer insteadOfRoleId);

}
