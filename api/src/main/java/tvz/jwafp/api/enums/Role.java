package tvz.jwafp.api.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("admin"),
    PROFESSOR("professor"),
    STUDENT("student");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
