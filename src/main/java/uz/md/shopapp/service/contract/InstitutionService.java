package uz.md.shopapp.service.contract;

import org.springframework.web.multipart.MultipartFile;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institution.InstitutionAddDTO;
import uz.md.shopapp.dtos.institution.InstitutionDTO;
import uz.md.shopapp.dtos.institution.InstitutionEditDTO;
import uz.md.shopapp.dtos.institution.InstitutionInfoDTO;

import java.util.List;

public interface InstitutionService {
    ApiResult<InstitutionDTO> add(InstitutionAddDTO dto);

    ApiResult<InstitutionDTO> findById(Long id);

    ApiResult<InstitutionDTO> edit(InstitutionEditDTO editDTO);

    ApiResult<Void> delete(Long id);

    ApiResult<List<InstitutionDTO>> getAll();

    ApiResult<List<InstitutionInfoDTO>> getAllForInfo();

    ApiResult<List<InstitutionInfoDTO>> getAllByTypeId(Long typeId);

    ApiResult<List<InstitutionInfoDTO>> getAllByManagerId(Long managerId);

    ApiResult<List<InstitutionInfoDTO>> getAllForInfoByPage(String page);

    ApiResult<Void> setImage(Long institutionId, MultipartFile image);
}
