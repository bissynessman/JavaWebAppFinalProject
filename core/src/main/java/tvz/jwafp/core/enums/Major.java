package tvz.jwafp.core.enums;

import lombok.Getter;

@Getter
public enum Major implements MessagesKey {
    COMPUTER_SCIENCE("Computer Science"),
    SOFTWARE_ENGINEERING("Software Engineering"),
    ENGINEERING("Engineering"),
    CONSTRUCTION("Construction");

    private final String name;

    Major(String name) {
        this.name = name;
    }

    @Override
    public String getMessagesKey() {
        return "major." + this.name().toLowerCase().replace('_', '-');
    }
}
