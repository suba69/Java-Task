package JavaTask.controller;

import JavaTask.dto.BaseResponse;
import JavaTask.dto.JwtRequest;
import JavaTask.dto.JwtResponse;
import JavaTask.dto.UserRegistrationDto;
import JavaTask.service.AuthService;
import JavaTask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @PostMapping()
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest jwtRequest) throws BadCredentialsException {
        String token = authService.getToken(jwtRequest);
        return ResponseEntity.ok(new JwtResponse(token, CREATION_MESSAGE));
    }

    @PostMapping("/registration")
    public ResponseEntity<JwtResponse> createNewUser(@RequestBody UserRegistrationDto userRegistrationDto) throws InstantiationException, IllegalAccessException {
        String token = userService.save(userRegistrationDto);
        return ResponseEntity.ok(new JwtResponse(token, CREATION_MESSAGE));
    }


}
