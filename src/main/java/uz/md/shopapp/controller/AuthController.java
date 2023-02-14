package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.TokenDTO;
import uz.md.shopapp.dtos.user.UserLoginDTO;
import uz.md.shopapp.dtos.user.UserRegisterDTO;
import uz.md.shopapp.service.contract.AuthService;
import uz.md.shopapp.utils.AppConstants;

@RestController
@RequestMapping(AuthController.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints for Auth")
@Slf4j
public class AuthController {

    /**
     * AuthController URL endpoints
     */
    public static final String BASE_URL = AppConstants.BASE_URL + "auth";
    public static final String REGISTER_URL = "/sign-up";
    public static final String LOGIN_URL = "/sign-in";

    private final AuthService authService;

    @PostMapping(value = REGISTER_URL)
    @Operation(description = "register user")
    ApiResult<Void> register(@RequestBody @Valid UserRegisterDTO dto) {
        log.info("Request body: {}", dto.toString());
        return authService.register(dto);
    }

    @Operation(description = " get sms code")
    @PostMapping(value = "get/sms_code/{phoneNumber}")
    ApiResult<String> getSmsCode(@PathVariable String phoneNumber) {
        log.info("Request body: {}", phoneNumber);
        return authService.getSMSCode(phoneNumber);
    }

    @Operation(description = "login with phone number and sms code")
    @PostMapping(value = LOGIN_URL)
    ApiResult<TokenDTO> login(@RequestBody @Valid UserLoginDTO loginDTO) {
        log.info("Request body: {}", loginDTO);
        return authService.login(loginDTO);
    }

}
