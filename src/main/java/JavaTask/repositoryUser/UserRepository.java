package JavaTask.repositoryUser;

import JavaTask.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String name);

    boolean existsByUsername(String username);
}
