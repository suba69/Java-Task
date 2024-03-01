package JavaTask.serviceUser.impl;

import JavaTask.dto.UserRegistrationDto;
import JavaTask.entity.User;
import JavaTask.repositoryUser.UserRepository;
import JavaTask.serviceUser.UserService;
import JavaTask.token.JwtTokenManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(String name, String password) {
        User user = userRepository.findByUsername(name);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public boolean checkUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public String save(UserRegistrationDto userRegistrationDto) {
        String username = userRegistrationDto.getUsername();

        // Шифруем пароль
        String encryptedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());

        // Создаем объект User из DTO
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setEmail(userRegistrationDto.getEmail());

        userRepository.save(user);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>());

        return jwtTokenManager.getJwtToken(userDetails);
    }




    @Override
    public User getByUsername(String username) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));

        return optionalUser.orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }
}

