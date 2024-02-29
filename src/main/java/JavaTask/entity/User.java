package JavaTask.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetailsService;

@Document(collection = "user")
@Data
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
}
