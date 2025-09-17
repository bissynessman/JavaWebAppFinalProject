package tvz.jwafp.api.service;

import org.springframework.stereotype.Service;
import tvz.jwafp.api.auth.JwToken;
import tvz.jwafp.api.repo.RefreshTokenRepository;

@Service
public class RefreshTokenService extends BaseService<JwToken> {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        super(refreshTokenRepository);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public JwToken getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public JwToken create(JwToken refreshToken) {
        repository.create(refreshToken);
        return refreshToken;
    }

    @Override
    public JwToken update(JwToken refreshToken) {
        repository.update(refreshToken);
        return refreshToken;
    }

    public JwToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public int deleteByUsername(String username) {
        return refreshTokenRepository.deleteByUsername(username);
    }
}
