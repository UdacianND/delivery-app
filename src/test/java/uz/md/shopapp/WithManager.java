package uz.md.shopapp;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import uz.md.shopapp.domain.User;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "+998941001010", password = "123", authorities = {"ADD_CATEGORY,ADD_PRODUCT, GET_CATEGORY"})
public @interface WithManager {
    Class<? extends UserDetails> userDetailsService() default User.class;
}