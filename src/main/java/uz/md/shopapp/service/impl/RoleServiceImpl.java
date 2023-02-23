package uz.md.shopapp.service.impl;

import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Role;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.role.RoleAddDTO;
import uz.md.shopapp.dtos.role.RoleDTO;
import uz.md.shopapp.dtos.role.RoleEditDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.RoleMapper;
import uz.md.shopapp.repository.RoleRepository;
import uz.md.shopapp.service.contract.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository,
                           RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public ApiResult<RoleDTO> add(RoleAddDTO dto) {
        if (roleRepository.existsByNameIgnoreCase(dto.getName()))
            throw AlreadyExistsException.builder()
                    .messageUz("ROLE_NAME_ALREADY_EXISTS")
                    .messageRu("")
                    .build();
        Role role = roleMapper.fromAddDTO(dto);
        return ApiResult
                .successResponse(roleMapper
                        .toDTO(roleRepository.save(role)));
    }

    @Override
    public ApiResult<List<RoleDTO>> getAll() {
        return ApiResult
                .successResponse(roleMapper
                        .toDTOList(roleRepository
                                .findAll()));
    }

    @Override
    public ApiResult<RoleDTO> getById(Integer id) {
        return ApiResult
                .successResponse(roleMapper
                        .toDTO(roleRepository
                                .findById(id)
                                .orElseThrow(() -> NotFoundException.builder()
                                        .messageUz("ROLE_NOT_FOUND_WITH_ID " + id)
                                        .messageRu("")
                                        .build())));
    }

    @Override
    public ApiResult<RoleDTO> edit(RoleEditDTO dto) {

        Role role = roleRepository
                .findById(dto.getId())
                .orElseThrow(() -> NotFoundException.builder()
                        .messageUz("ROLE_NOT_FOUND_WITH_ID")
                        .messageRu("")
                        .build());

        Role role1 = roleMapper.fromEditDTO(role, dto);
        return ApiResult
                .successResponse(roleMapper
                        .toDTO(roleRepository.save(role1)));
    }

    @Override
    public ApiResult<Boolean> delete(Integer id, Integer insteadOfRoleId) {
        return null;
    }
}
