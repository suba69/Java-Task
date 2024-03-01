package JavaTask.controller;

import JavaTask.dto.BaseResponse;
import JavaTask.dto.JwtRequest;
import JavaTask.dto.JwtResponse;
import JavaTask.dto.UserRegistrationDto;
import JavaTask.serviceUser.AuthService;
import JavaTask.serviceUser.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private static final String CREATION_MESSAGE = "User created successfully";
    private final AuthService authService;
    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping()
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest jwtRequest) throws BadCredentialsException {
        String token = authService.getToken(jwtRequest);
        return ResponseEntity.ok(new JwtResponse(token, CREATION_MESSAGE));
    }

    @PostMapping("/registration")
    public ResponseEntity<JwtResponse> createNewUser(@RequestBody UserRegistrationDto userRegistrationDto) throws InstantiationException, IllegalAccessException {
        String username = userRegistrationDto.getUsername();

        if (userService.checkUserExists(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JwtResponse(null, "Error: User with this username already exists."));
        }

        String token = userService.save(userRegistrationDto);
        return ResponseEntity.ok(new JwtResponse(token, CREATION_MESSAGE));
    }


    @PostMapping("/login")
    public ResponseEntity<BaseResponse> loginUser(@RequestBody JwtRequest jwtRequest) {
        String username = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(new BaseResponse("Login successful."));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BaseResponse("Authentication failed. Invalid credentials."));
        } catch (Exception e) {
            logger.error("Unexpected error during authentication", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse("Authentication failed. An unexpected error occurred."));
        }
    }
}
