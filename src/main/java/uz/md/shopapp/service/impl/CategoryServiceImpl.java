package uz.md.shopapp.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.md.shopapp.domain.Category;
import uz.md.shopapp.domain.Institution;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryAddDTO;
import uz.md.shopapp.dtos.category.CategoryDTO;
import uz.md.shopapp.dtos.category.CategoryEditDTO;
import uz.md.shopapp.dtos.category.CategoryInfoDTO;
import uz.md.shopapp.exceptions.AlreadyExistsException;
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.exceptions.NotFoundException;
import uz.md.shopapp.mapper.CategoryMapper;
import uz.md.shopapp.repository.CategoryRepository;
import uz.md.shopapp.repository.InstitutionRepository;
import uz.md.shopapp.service.contract.CategoryService;
import uz.md.shopapp.utils.CommonUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final InstitutionRepository institutionRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper,
                               InstitutionRepository institutionRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.institutionRepository = institutionRepository;
    }

    @Override
    public ApiResult<CategoryDTO> add(CategoryAddDTO dto) {

        User currentUser = CommonUtils.getCurrentUser();
        Institution institution = institutionRepository
                .findById(dto.getInstitutionId())
                .orElseThrow(() -> new NotFoundException("INSTITUTION NOT FOUND"));

        if (!currentUser.getRole().getName().equals("ADMIN"))
            if (!institution.getManager().getId().equals(currentUser.getId()))
                throw new NotAllowedException("YOU HAVE NO PERMISSION");

        if (categoryRepository.existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu()) ||
                categoryRepository.existsByNameUzOrNameRu(dto.getNameUz(), dto.getNameRu()))
            throw new AlreadyExistsException("CATEGORY_NAME_ALREADY_EXISTS");
        Category category = categoryMapper
                .fromAddDTO(dto);
        category.setInstitution(institution);

        return ApiResult
                .successResponse(categoryMapper
                        .toDTO(categoryRepository
                                .save(categoryRepository.save(category))));
    }

    @Override
    public ApiResult<CategoryDTO> findById(Long id) {
        return ApiResult.successResponse(categoryMapper
                .toDTO(categoryRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND"))));
    }

    @Override
    public ApiResult<CategoryDTO> edit(CategoryEditDTO editDTO) {

        User currentUser = CommonUtils.getCurrentUser();
        Institution institution = institutionRepository
                .findById(editDTO.getInstitutionId())
                .orElseThrow(() -> new NotFoundException("INSTITUTION NOT FOUND"));

        if (!currentUser.getRole().getName().equals("ADMIN"))
            if (!institution.getManager().getId().equals(currentUser.getId()))
                throw new NotAllowedException("YOU HAVE NO PERMISSION");

        Category editing = categoryRepository
                .findById(editDTO.getId())
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND"));

        if (categoryRepository.existsByNameUzOrNameRuAndIdIsNot(editDTO.getNameUz(), editDTO.getNameRu(), editing.getId()))
            throw new AlreadyExistsException("CATEGORY_NAME_ALREADY_EXISTS");

        Category category = categoryMapper.fromEditDTO(editDTO, editing);
        category.setInstitution(institution);
        return ApiResult.successResponse(categoryMapper
                .toDTO(categoryRepository.save(category)));
    }

    @Override
    public ApiResult<List<CategoryDTO>> getAll() {
        return ApiResult.successResponse(
                categoryMapper.toDTOList(
                        categoryRepository.findAll()
                )
        );
    }

    @Override
    public ApiResult<List<CategoryInfoDTO>> getAllForInfo() {
        return ApiResult.successResponse(
                categoryMapper.toInfoDTOList(
                        categoryRepository.findAllForInfo()
                ));
    }

    @Override
    public ApiResult<List<CategoryInfoDTO>> getAllByInstitutionId(Long id) {
        return ApiResult.successResponse(categoryRepository
                .findAllForInfoByInstitutionId(id));
    }

    @Override
    public ApiResult<List<CategoryInfoDTO>> getAllByInstitutionIdAndPage(Long id, String page) {
        int[] paged = {Integer.parseInt(page.split("-")[0]),
                Integer.parseInt(page.split("-")[1])};
        return ApiResult.successResponse(categoryRepository
                .findAllForInfoByInstitutionId(id, PageRequest.of(paged[0], paged[1]))
                .getContent());
    }

    @Override
    public ApiResult<Void> delete(Long id) {

        if (!categoryRepository.existsById(id))
            throw new NotFoundException("CATEGORY_NOT_FOUND");
        Long managerId = categoryRepository.findMangerIdByCategoryId(id);
        User currentUser = CommonUtils.getCurrentUser();

        if (!currentUser.getRole().getName().equals("ADMIN"))
            if (!currentUser.getId().equals(managerId))
                throw new NotAllowedException("YOU HAVE NO PERMISSION");

        categoryRepository.deleteById(id);
        return ApiResult.successResponse();
    }
}
