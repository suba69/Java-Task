package JavaTask.service;

import JavaTask.entity.User;

public interface UserService {
    User createUser(User user);

    User authenticateUser(String name, String password);
}
