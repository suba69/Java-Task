package JavaTask.service;

import JavaTask.dto.JwtRequest;
import JavaTask.token.JwtTokenManager;
import com.mongodb.annotations.Beta;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenManager jwtTokenManager;

    public String getToken(@RequestBody JwtRequest jwtRequest) throws BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),
                jwtRequest.getPassword()));
        userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        return jwtTokenManager.getJwtToken(userDetailsService.loadUserByUsername(jwtRequest.getUsername()));
    }
}
