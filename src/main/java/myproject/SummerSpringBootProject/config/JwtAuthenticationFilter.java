package myproject.SummerSpringBootProject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import myproject.SummerSpringBootProject.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        final String userRole;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response); //jeigu nera authorization header'io, tai perduodam kitiems filtrams be papildomos autentifikacijos naudojant JWT tokena.
            return;
        }
        //jeigu toks header'is yra, darom tokeno validacija, istraukiam duomenis is jo

        jwt = authHeader.substring(7); //istraukiam tokena nuo 7 pozicijos, kadangi reikia nukirpti "Bearer "
        userEmail = jwtService.extractUsernameFromToken(jwt);
        userRole = jwtService.extractRoleFromToken(jwt);
        //jeigu userEmail surado ir varotojas dar nera autentifikuotas (dabartine uzklausa dar nepraejo autentifikacijos), tai surenkam user'i is tokeno
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEmail, null, List.of(new SimpleGrantedAuthority(userRole)));
            //slaptazodi nenurodom, nes cia mums jau atsiunte token'a, vadinasi jau buvo slaptazodis nurodytas anksciau
            SecurityContextHolder.getContext().setAuthentication(token); //issaugom tokena kontekste
        }

        filterChain.doFilter(request, response);
    }
}
