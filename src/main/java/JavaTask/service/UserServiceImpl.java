package JavaTask.service;

import JavaTask.entity.User;
import JavaTask.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
