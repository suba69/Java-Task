package JavaTask.service;

import JavaTask.dto.UserRegistrationDto;
import JavaTask.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User user);

    User authenticateUser(String name, String password);

    boolean checkUserExists(String name);

    String save(UserRegistrationDto userRegistrationDto);

    User getByUsername(String username);
}
