package tvz.jwafp.api.auth;

import tvz.jwafp.api.entity.BaseEntity;

import java.util.Date;
import java.util.UUID;

public class JwToken extends BaseEntity {
    String token;
    String username;
    Date validUntil;

    public JwToken(final String id, final String token, final String username, final Date validUntil) {
        super(id);
        this.token = token;
        this.username = username;
        this.validUntil = validUntil;
    }

    public JwToken(final String token, final String username, final Date validUntil) {
        super(UUID.randomUUID().toString());
        this.token = token;
        this.username = username;
        this.validUntil = validUntil;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }
}
