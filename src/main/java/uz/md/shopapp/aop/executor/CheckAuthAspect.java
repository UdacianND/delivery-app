package uz.md.shopapp.aop.executor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import uz.md.shopapp.aop.annotation.CheckAuth;
import uz.md.shopapp.config.security.JwtAuthenticationFilter;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.domain.enums.PermissionEnum;
import uz.md.shopapp.dtos.user.UserDTO;
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.mapper.UserMapper;
import uz.md.shopapp.utils.AppConstants;
import uz.md.shopapp.utils.CommonUtils;

import java.util.Objects;
import java.util.Set;

import static uz.md.shopapp.utils.CommonUtils.currentRequest;


@Slf4j
@Order(value = 1)
@Aspect
@Component
@RequiredArgsConstructor
public class CheckAuthAspect {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserMapper userMapper;

    @Before(value = "@annotation(checkAuth)")
    public void checkAuthExecutor(CheckAuth checkAuth) {
        check(checkAuth);
    }


    public void check(CheckAuth checkAuth) {

        HttpServletRequest httpServletRequest = currentRequest();

        String token = getTokenFromRequest(httpServletRequest);

        User userFromBearerToken = jwtAuthenticationFilter.getUserFromBearerToken(token);

        if (userFromBearerToken != null && userFromBearerToken.getId() != null) {

            UserDTO userDTO = userMapper.toDTO(userFromBearerToken);
            PermissionEnum[] permission = checkAuth.permission();
            if (permission.length > 0 && notPermission(userDTO.getPermissions(), permission)) {
                throw new NotAllowedException("FORBIDDEN");
            }
            httpServletRequest.setAttribute(AppConstants.REQUEST_ATTRIBUTE_CURRENT_USER, userDTO);
        } else
            throw new NotAllowedException("FORBIDDEN");
    }


    private String getTokenFromRequest(HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader(AppConstants.AUTHORIZATION_HEADER);
            if (Objects.isNull(token) || token.isEmpty()) {
                throw new NotAllowedException("FORBIDDEN");
            }
            return token;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private boolean notPermission(Set<PermissionEnum> hasPermission, PermissionEnum[] mustPermission) {
        if (Objects.isNull(hasPermission) || hasPermission.isEmpty()) {
            return true;
        }
        for (PermissionEnum permissionEnum : mustPermission) {
            if (hasPermission.contains(permissionEnum))
                return false;
        }
        return true;
    }

    private boolean notPermission(String permission, PermissionEnum[] mustPermission) {
        if (permission == null || permission.isEmpty())
            return true;
        for (PermissionEnum permissionEnum : mustPermission) {
            if (permission.contains(permissionEnum.name()))
                return false;
        }
        return true;
    }

    private void setUserIdAndPermissionFromRequest(HttpServletRequest httpServletRequest) {
        String userId = CommonUtils.getUserIdFromRequest(httpServletRequest);
        String permissions = CommonUtils.getUserPermissionsFromRequest(httpServletRequest);
        httpServletRequest.setAttribute(AppConstants.REQUEST_ATTRIBUTE_CURRENT_USER_ID, userId);
        httpServletRequest.setAttribute(AppConstants.REQUEST_ATTRIBUTE_CURRENT_USER_PERMISSIONS, permissions);
    }
}
