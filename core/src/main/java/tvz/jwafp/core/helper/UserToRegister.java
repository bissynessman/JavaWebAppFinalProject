package tvz.jwafp.core.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tvz.jwafp.core.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserToRegister {
    private Integer id;
    private String username;
    private String password;
    private String repeatPassword;
    private Role role;
}
