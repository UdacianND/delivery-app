package uz.md.shopapp.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.ErrorData;
import uz.md.shopapp.utils.AppConstants;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        log.error("Responding with unauthorized error. URL -  {}, Message - {}", httpServletRequest.getRequestURI(), e.getMessage());
        ApiResult<ErrorData> errorDataApiResult = ApiResult.errorResponse("Kirish taqiqlangan","",e.getMessage(), "Forbidden", 403);
        httpServletResponse.getWriter().write(AppConstants.objectMapper.writeValueAsString(errorDataApiResult));
        httpServletResponse.setStatus(403);
        httpServletResponse.setContentType("application/json");
    }

}
