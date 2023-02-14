package uz.md.shopapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uz.md.shopapp.domain.Role;
import uz.md.shopapp.dtos.role.RoleAddDTO;
import uz.md.shopapp.dtos.role.RoleDTO;
import uz.md.shopapp.dtos.role.RoleEditDTO;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<Role, RoleDTO> {

    Role fromAddDTO(RoleAddDTO dto);

    Role fromEditDTO(@MappingTarget Role role, RoleEditDTO dto);
}
