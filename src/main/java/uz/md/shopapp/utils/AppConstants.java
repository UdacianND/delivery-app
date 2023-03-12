package uz.md.shopapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import uz.md.shopapp.controller.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface AppConstants {

    String BASE_URL = "/api/v1/shop/";

    ObjectMapper objectMapper = new ObjectMapper();

    String AUTHORIZATION_HEADER = "Authorization";

    String AUTHENTICATION_HEADER = "Authentication";

    String[] OPEN_PAGES = {
            InstitutionTypeController.BASE_URL+"/*",
            InstitutionController.BASE_URL,
            InstitutionController.BASE_URL+"/all",
            InstitutionController.BASE_URL+"/by-page/*",
            InstitutionController.BASE_URL+"/*",
            InstitutionController.BASE_URL+"/by-type/*",
            InstitutionController.BASE_URL+"/all/by_type/*",
            ProductController.BASE_URL+"/category/*",
            CategoryController.BASE_URL,
            CategoryController.BASE_URL+"/all",
            CategoryController.BASE_URL+"/*",
            CategoryController.BASE_URL+"/institution/*",
            CategoryController.BASE_URL+"/institution/*/by-page/*",
            AuthController.BASE_URL + "/**",
            TelegrambotController.BASE_URL + "/**"
    };

    String[] SWAGGER_PAGES = {
            "/error",
            "/",
            "/favicon.ico",
            "//*.png",
            "//*.gif",
            "//*.svg",
            "//*.jpg",
            "//*.html",
            "//*.css",
            "//*.js",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-resources/",
            "/v3/api-docs/**",
            "/csrf",
            "/webjars/",
            "/v2/api-docs",
            "/configuration/ui"
    };

    String REQUEST_ATTRIBUTE_CURRENT_USER = "User";
    String REQUEST_ATTRIBUTE_CURRENT_USER_ID = "UserId";
    String REQUEST_ATTRIBUTE_CURRENT_USER_PERMISSIONS = "Permissions";

    /**
     * Regexes
     */
    String PhoneNumber_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    /**
    *Telegram bot constants
     */
    String TELEGRAM_BASE_URL = "https://api.telegram.org/bot";
    String TELEGRAM_BOT_WEBAPP_URL = "https://master--melodic-meerkat-622f75.netlify.app/";
    String BOT_TOKEN = "6197877428:AAF1jOddZ97jKfyQ-dFqAi_tMV5o8yLtMbk";
    String TELEGRAM_BOT_FULL_URL = TELEGRAM_BASE_URL.concat(BOT_TOKEN);
    String TELEGRAM_SEND_MESSAGE_URL = TELEGRAM_BOT_FULL_URL + "/sendMessage";
    String GROUP_CHAT_ID = "@delivery_channel";
    String TELEGRAM_SEND_LOCATION_URL = TELEGRAM_BOT_FULL_URL + "/sendLocation?chat_id="+GROUP_CHAT_ID ;

    String BOT_STARTER_MESSAGE = "Salom, ushbu havolani oching";

}
