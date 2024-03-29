package Security.SpringSecurity.config;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    private final UserDetailsService  userDetailsService;
    public JwtAuthenticationFilter() {
        jwtService = null;
        userDetailsService = null;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
                                    throws ServletException, IOException {
            final String authHeader= request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            jwt=authHeader.substring(7);
            userEmail=jwtService.extractUserName(jwt);
            if(userEmail != null  && SecurityContextHolder.getContext().getAuthentication() ==null){
                UserDetails userDetails= this.userDetailsService.loadUserByUsername(userEmail);
            }
    }
}
