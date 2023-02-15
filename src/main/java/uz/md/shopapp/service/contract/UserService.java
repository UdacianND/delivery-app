package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.user.UserDTO;

public interface UserService {
    ApiResult<UserDTO> findById(Long id);

    ApiResult<Void> delete(Long id);

    ApiResult<UserDTO> me();
}
