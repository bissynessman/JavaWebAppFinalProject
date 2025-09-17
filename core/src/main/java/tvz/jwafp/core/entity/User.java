package tvz.jwafp.core.entity;

import lombok.Data;
import tvz.jwafp.core.enums.Role;

import java.util.UUID;

@Data
public class User {
    private String id;
    private String email;
    private String username;
    private String password;
    private Role role;

    private String userUuid;

    public User() {}

    private User(final String id,
                 final String email,
                 final String username,
                 final String password,
                 final Role role,
                 final String userUuid) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.userUuid = userUuid;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String id = UUID.randomUUID().toString();
        private String email;
        private String username;
        private String password;
        private Role role;
        private String userUuid;

        UserBuilder() {}

        public UserBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserBuilder username(final String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(final String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(final Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder userUuid(final String userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public User build() {
            return new User(this.id, this.email, this.username, this.password, this.role, this.userUuid);
        }

        public String toString() {
            return "User.UserBuilder[id=" + this.id
                    + ", email=" + this.email
                    + ", username=" + this.username
                    + ", password=" + this.password
                    + ", role=" + this.role
                    + ", userUuid=" + this.userUuid
                    + "]";
        }
    }
}
