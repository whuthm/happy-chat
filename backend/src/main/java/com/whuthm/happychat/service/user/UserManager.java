package com.whuthm.happychat.service.user;

import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
class UserManager implements UserService {

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
    public User getUserByName(String username) {
        return userRepository.findUserByName(username);
    }

    @Override
    public User addUser(User user) {
        User newUser = userRepository.save(user);
        users.put(user.getId(), newUser);
        return newUser;
    }

    @Override
    public List<User> getUsers() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> page = userRepository.findAll(pageRequest);
        return page.getContent();
    }
}
