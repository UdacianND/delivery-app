package uz.md.shopapp.service.contract;

import org.springframework.security.core.userdetails.UserDetailsService;
import uz.md.shopapp.dtos.*;
import uz.md.shopapp.dtos.user.EmployeeRegisterDTO;
import uz.md.shopapp.dtos.user.ClientLoginDTO;
import uz.md.shopapp.dtos.user.EmployeeLoginDTO;

public interface AuthService extends UserDetailsService {

    ApiResult<Void> registerClient(ClientLoginDTO dto);

    ApiResult<String> getSMSCode(String phoneNumber);

    ApiResult<TokenDTO> loginOrRegisterClient(ClientLoginDTO loginDTO);

    ApiResult<TokenDTO> loginEmployee(EmployeeLoginDTO loginDTO);

    ApiResult<Void> registerEmployee(EmployeeRegisterDTO dto);
}
