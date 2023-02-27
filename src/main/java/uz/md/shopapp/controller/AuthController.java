package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.TokenDTO;
import uz.md.shopapp.dtos.user.EmployeeRegisterDTO;
import uz.md.shopapp.dtos.user.ClientLoginDTO;
import uz.md.shopapp.dtos.user.EmployeeLoginDTO;
import uz.md.shopapp.service.contract.AuthService;
import uz.md.shopapp.utils.AppConstants;

@RestController
@RequestMapping(AuthController.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints for Auth")
@Slf4j
public class AuthController {

    /**
     * AuthResource URL endpoints
     */

    public static final String BASE_URL = AppConstants.BASE_URL + "auth";
    private final AuthService authService;

    @PostMapping(value = "/employee/sign-up")
    @Operation(description = "register user")
    ApiResult<Void> registerEmployee(@RequestBody @Valid @NotNull EmployeeRegisterDTO dto) {
        log.info("Request body: {}", dto);
        return authService.registerEmployee(dto);
    }

    @Operation(description = " get sms code")
    @PostMapping(value = "get/sms-code")
    ApiResult<String> getSmsCode( @RequestParam String phoneNumber) {
        log.info("Request body: {}", phoneNumber);
        return authService.getSMSCode(phoneNumber);
    }

    @Operation(description = "login with phone number and sms code")
    @PostMapping(value = "/client/signin-or-signup")
    ApiResult<TokenDTO> loginOrRegisterClient(@RequestBody @Valid ClientLoginDTO loginDTO) {
        log.info("Request body: {}", loginDTO);
        return authService.loginOrRegisterClient(loginDTO);
    }

    @Operation(description = "login with phone number and password")
    @PostMapping(value = "/employee/sign-in")
    ApiResult<TokenDTO> loginEmployee(@RequestBody @Valid EmployeeLoginDTO loginDTO) {
        log.info("Request body: {}", loginDTO);
        return authService.loginEmployee(loginDTO);
    }

}
