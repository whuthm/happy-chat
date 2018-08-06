package com.whuthm.happychat.service.user;

import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager implements UserService {

    @Autowired
    UserRepository userRepository;

    private Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                user = userOptional.get();
                users.put(userId, user);
            }
        }
        return user;
    }

    @Override
    public void addUser(User user) {
        User newUser = userRepository.save(user);
        users.put(user.getId(), user);
    }
}
