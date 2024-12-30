package ma.m3achaba.plantes.services.imp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ma.m3achaba.plantes.model.Token;
import ma.m3achaba.plantes.repo.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogoutService implements LogoutHandler {
    private final TokenRepository repToken;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        System.out.println("DDD");
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        final String jwt = authHeader.substring(7);
        Token stored = repToken.findByToken(jwt).orElse(null);

        if (stored != null) {
            System.out.println(stored.getToken());
            stored.setExpired(true);
            stored.setRevoked(true);
            repToken.save(stored); // why in this save is not do
        }
        if (stored != null && stored.getRefreshToken() != null) {
            Token refresh = repToken.findByToken(stored.getRefreshToken()).orElse(null);
            if (refresh != null) {
                System.out.println(refresh.getToken());
                refresh.setExpired(true);
                refresh.setRevoked(true);
                repToken.save(refresh); // Persist the changes
            }
        }

    }
}
