package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institutionType.InstitutionTypeAddDTO;
import uz.md.shopapp.dtos.institutionType.InstitutionTypeDTO;
import uz.md.shopapp.dtos.institutionType.InstitutionTypeEditDTO;

import java.util.List;

public interface InstitutionTypeService {
    ApiResult<InstitutionTypeDTO> add(InstitutionTypeAddDTO dto);

    ApiResult<InstitutionTypeDTO> findById(Long id);

    ApiResult<InstitutionTypeDTO> edit(InstitutionTypeEditDTO editDTO);

    ApiResult<Void> delete(Long id);

    ApiResult<List<InstitutionTypeDTO>> getAll();

    ApiResult<List<InstitutionTypeDTO>> getAllByPage(String page);
}
