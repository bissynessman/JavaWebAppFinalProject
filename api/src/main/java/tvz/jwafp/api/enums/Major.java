package tvz.jwafp.api.enums;

import lombok.Getter;

@Getter
public enum Major {
    COMPUTER_SCIENCE("Computer scienece"),
    SOFTWARE_ENGINEERING("Software engineering"),
    ENGINEERING("Engineering"),
    CONSTRUCTION("Construction");

    private final String name;

    Major(String name) {
        this.name = name;
    }
}
