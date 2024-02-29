package JavaTask.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UserRegistrationDto {

    private String id;
    private String username;
    private String password;
    private String email;
}
