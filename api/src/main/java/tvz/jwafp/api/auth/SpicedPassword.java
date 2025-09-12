package tvz.jwafp.api.auth;

public class SpicedPassword {
    private String passwordHash;
    private String salt;

    private SpicedPassword(final String passwordHash, final String salt) {
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public static SpicedPasswordBuilder builder() {
        return new SpicedPasswordBuilder();
    }

    public static class SpicedPasswordBuilder {
        private String passwordHash;
        private String salt;

        SpicedPasswordBuilder() {}

        public SpicedPasswordBuilder passwordHash(final String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public SpicedPasswordBuilder salt(final String salt) {
            this.salt = salt;
            return this;
        }

        public SpicedPassword build() {
            return new SpicedPassword(this.passwordHash, this.salt);
        }

        public String toString() {
            return "SpicedPassword.SpicedPasswordBuilder["
                    + "passwordHash=" + this.passwordHash
                    + ", salt=" + this.salt
                    + "]";
        }
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
