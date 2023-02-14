package uz.md.shopapp.service.contract;

import org.springframework.security.core.userdetails.UserDetailsService;
import uz.md.shopapp.dtos.*;
import uz.md.shopapp.dtos.user.UserLoginDTO;
import uz.md.shopapp.dtos.user.UserRegisterDTO;

public interface AuthService extends UserDetailsService {

    ApiResult<Void> register(UserRegisterDTO dto);

    ApiResult<TokenDTO> login(UserLoginDTO dto);

    ApiResult<String> getSMSCode(String phoneNumber);
}
