package medved.java.back.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenRepository {
    private final Map<String, String> tokensRepository = new ConcurrentHashMap<>();

    public void saveToken(String auhToken, String username) {
        tokensRepository.put(auhToken, username);
    }

    public void removeToken(String authToken) {
        tokensRepository.remove(authToken);
    }

    public String getUserByToken(String authToken) {
        return tokensRepository.get(authToken);
    }
}
