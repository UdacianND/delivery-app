package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.category.CategoryAddDTO;
import uz.md.shopapp.dtos.category.CategoryDTO;
import uz.md.shopapp.dtos.category.CategoryEditDTO;
import uz.md.shopapp.dtos.category.CategoryInfoDTO;

import java.util.List;

public interface CategoryService {

    ApiResult<CategoryDTO> add(CategoryAddDTO dto);

    ApiResult<CategoryDTO> findById(Long id);

    ApiResult<CategoryDTO> edit(CategoryEditDTO editDTO);

    ApiResult<Void> delete(Long id);

    ApiResult<List<CategoryDTO>> getAll();

    ApiResult<List<CategoryInfoDTO>> getAllForInfo();

    ApiResult<List<CategoryInfoDTO>> getAllByInstitutionId(Long id);

    ApiResult<List<CategoryInfoDTO>> getAllByInstitutionIdAndPage(Long id, String page);
}
