package JavaTask.mapper;

import JavaTask.dto.UserRegistrationDto;
import JavaTask.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User convertToDomain(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(userRegistrationDto.getPassword());
        user.setEmail(userRegistrationDto.getEmail());
        return user;
    }
}