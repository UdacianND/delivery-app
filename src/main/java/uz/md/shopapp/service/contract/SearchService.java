package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.institution.InstitutionDTO;

import java.util.List;

public interface SearchService {
    ApiResult<List<InstitutionDTO>> getBySearch(String value);
}
