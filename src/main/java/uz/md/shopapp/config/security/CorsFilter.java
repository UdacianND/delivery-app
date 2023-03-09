package uz.md.shopapp.config.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@WebFilter("/*")
@Component
public class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String corsHeader = response.getHeader("Access-Control-Allow-Origin");
//        if(corsHeader == null){
//            response.addHeader("Access-Control-Allow-Origin", "*");
//                // CORS "pre-flight" request
//                response.addHeader("Access-Control-Allow-Methods",
//                        "GET, POST, PUT, DELETE");
//                response.addHeader("Access-Control-Allow-Headers",
//                        "X-Requested-With,Origin,Content-Type, Accept");
//        }
        filterChain.doFilter(request, response);
    }

}