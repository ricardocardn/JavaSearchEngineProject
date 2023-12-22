package controller.sessions.cookie;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.multimap.MultiMap;
import controller.sessions.SessionHandler;
import model.User;

import java.security.SecureRandom;
import java.util.Base64;

public class SessionHazelcastHandler implements SessionHandler {
    private final IMap<String, User> userSessions;

    public SessionHazelcastHandler() {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        userSessions = hazelcastInstance.getMap("userSessions");
    }

    @Override
    public String getSessionToken(User user) {
        int tokenSize = 32;

        byte[] randomBytes = new byte[tokenSize];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        String sessionToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        userSessions.put(sessionToken, user);

        return sessionToken;
    }

    @Override
    public User hasValidSession(String session) {
        return userSessions.get(session);
    }
}
