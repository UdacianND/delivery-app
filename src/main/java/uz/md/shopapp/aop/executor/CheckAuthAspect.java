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
import uz.md.shopapp.exceptions.NotAllowedException;
import uz.md.shopapp.utils.AppConstants;
import uz.md.shopapp.utils.CommonUtils;

import java.util.HashMap;
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

    @Before(value = "@annotation(checkAuth)")
    public void checkAuthExecutor(CheckAuth checkAuth) {
        check(checkAuth);
    }

    public void check(CheckAuth checkAuth) {

        HttpServletRequest httpServletRequest = currentRequest();

        String token = getTokenFromRequest(httpServletRequest);

        User userFromBearerToken = jwtAuthenticationFilter.getUserFromBearerToken(token);

        if (userFromBearerToken != null && userFromBearerToken.getId() != null) {

            PermissionEnum[] permission = checkAuth.permission();
            if (permission.length > 0 && notPermission(userFromBearerToken.getRole().getPermissions(), permission)) {
                throw NotAllowedException.builder()
                        .messageUz("FORBIDDEN")
                        .messageRu("")
                        .build();
            }
            httpServletRequest.setAttribute(AppConstants.REQUEST_ATTRIBUTE_CURRENT_USER, userFromBearerToken);
        } else
            throw NotAllowedException.builder()
                    .messageUz("FORBIDDEN")
                    .messageRu("")
                    .build();
    }


    private String getTokenFromRequest(HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader(AppConstants.AUTHORIZATION_HEADER);
            if (Objects.isNull(token) || token.isEmpty()) {
                throw NotAllowedException.builder()
                        .messageUz("FORBIDDEN")
                        .messageRu("")
                        .build();
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

    private void setUserIdAndPermissionFromRequest(HttpServletRequest httpServletRequest) {
        String userId = CommonUtils.getUserIdFromRequest(httpServletRequest);
        String permissions = CommonUtils.getUserPermissionsFromRequest(httpServletRequest);
        httpServletRequest.setAttribute(AppConstants.REQUEST_ATTRIBUTE_CURRENT_USER_ID, userId);
        httpServletRequest.setAttribute(AppConstants.REQUEST_ATTRIBUTE_CURRENT_USER_PERMISSIONS, permissions);
    }
}
