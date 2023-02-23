package uz.md.shopapp.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.md.shopapp.domain.User;
import uz.md.shopapp.exceptions.NotAllowedException;

import java.util.Optional;
import java.util.Random;

import static uz.md.shopapp.utils.AppConstants.AUTHENTICATION_HEADER;

public class CommonUtils {

    public static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature
                            .FAIL_ON_IGNORED_PROPERTIES,
                    false);

    public static User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            User currentUser = (User) authentication.getPrincipal();

            if (currentUser == null) {
                throw NotAllowedException.builder()
                        .messageUz("Error! Access is not possible")
                        .messageRu("")
                        .build();
            }
            return currentUser;
        } catch (Exception e) {
            throw NotAllowedException.builder()
                    .messageUz("Error! Access is not possible")
                    .messageRu("")
                    .build();
        }
    }

    public static String getCurrentUserPhoneNumber() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            if (principal != null) return principal.getUsername();
            else throw NotAllowedException.builder()
                    .messageUz("Error! Access is not possible")
                    .messageRu("")
                    .build();
        } catch (Exception e) {
            throw NotAllowedException.builder()
                    .messageUz("Error! Access is not possible")
                    .messageRu("")
                    .build();
        }
    }

    public static HttpServletRequest currentRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional
                .ofNullable(servletRequestAttributes)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    public static String getUserIdFromRequest(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(AUTHENTICATION_HEADER);
    }

    public static String getUserPermissionsFromRequest(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(AppConstants.REQUEST_ATTRIBUTE_CURRENT_USER_PERMISSIONS);
    }


    public static int[] getPagination(String pagination) {
        String[] split = pagination.split("-");
        return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
    }

    public static String generateString(int length) {
        Random random = new Random();
        return random.ints(0, 26)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
