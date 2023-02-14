package uz.md.shopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.aop.annotation.CheckAuth;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.user.UserDTO;
import uz.md.shopapp.service.contract.UserService;
import uz.md.shopapp.utils.AppConstants;

@RestController
@RequestMapping(UserController.BASE_URL + "/")
@RequiredArgsConstructor
public class UserController {

    public static final String BASE_URL = AppConstants.BASE_URL + "user";

    private final UserService userService;

    @Operation(description = "get user by id")
    @GetMapping("/{id}")
    @CheckAuth(permission = PermissionEnum.GET_USER)
    public ApiResult<UserDTO> getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @Operation(description = "delete a user")
    @DeleteMapping("/delete/{id}")
    @CheckAuth(permission = PermissionEnum.DELETE_USER)
    public ApiResult<Void> delete(@PathVariable Long id) {
        return userService.delete(id);
    }

}
