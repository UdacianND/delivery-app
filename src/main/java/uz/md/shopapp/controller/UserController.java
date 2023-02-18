package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.md.shopapp.aop.annotation.CheckAuth;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.service.contract.UserService;
import uz.md.shopapp.utils.AppConstants;

@RestController
@RequestMapping(UserController.BASE_URL + "/")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for User")
@Slf4j
public class UserController {

    public static final String BASE_URL = AppConstants.BASE_URL + "user";

    private final UserService userService;

    @Operation(description = "delete a user")
    @DeleteMapping("/delete/{id}")
    @CheckAuth(permission = PermissionEnum.DELETE_USER)
    public ApiResult<Void> delete(@PathVariable Long id) {
        return userService.delete(id);
    }


}
