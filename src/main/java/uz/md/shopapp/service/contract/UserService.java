package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;

public interface UserService {
    ApiResult<Void> delete(Long id);
}
