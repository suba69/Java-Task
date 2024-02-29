package JavaTask.service;

import JavaTask.dto.UserRegistrationDto;
import JavaTask.entity.User;
import JavaTask.mapper.UserMapper;
import JavaTask.repository.UserRepository;
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
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenManager jwtTokenManager, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
        this.userMapper = userMapper;
    }

    public User createUser(User user) {
        // Шифруємо пароль перед збереженням в базі даних
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(String name, String password) {
        User user = userRepository.findByUsername(name);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user; // Вертаємо користувача, якщо аутентифікація успішна
        } else {
            return null; // Помилка аутентифікації
        }
    }

    @Override
    public boolean checkUserExists(String name) {
        return false;
    }

    @Override
    public String save(UserRegistrationDto userRegistrationDto) {
        userRegistrationDto.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        User user = userMapper.convertToDomain(userRegistrationDto);

        if (user.getUsername() != null && user.getPassword() != null) {
            userRepository.save(user);

            // Генерация токена
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), new ArrayList<>());

            String token = jwtTokenManager.getJwtToken(userDetails);

            return token;
        } else {
            return "Error: Username or password is null.";
        }
    }


    @Override
    public User getByUsername(String username) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));

        return optionalUser.orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }
}

