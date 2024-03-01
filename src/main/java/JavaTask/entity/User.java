package JavaTask.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "user")
@CompoundIndexes({
        @CompoundIndex(name = "unique_username", def = "{'username': 1}", unique = true),
})
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String password;
    private String email;

    private List<String> roles;
}


