package uz.md.shopapp.service.contract;

import org.springframework.security.core.userdetails.UserDetailsService;
import uz.md.shopapp.dtos.*;
import uz.md.shopapp.dtos.user.ClientLoginDTO;
import uz.md.shopapp.dtos.user.ClientRegisterDTO;
import uz.md.shopapp.dtos.user.EmployeeLoginDTO;

public interface AuthService extends UserDetailsService {

    ApiResult<Void> register(ClientRegisterDTO dto);

    ApiResult<String> getSMSCode(String phoneNumber);

    ApiResult<TokenDTO> loginClient(ClientLoginDTO loginDTO);

    ApiResult<TokenDTO> loginEmployee(EmployeeLoginDTO loginDTO);
}
