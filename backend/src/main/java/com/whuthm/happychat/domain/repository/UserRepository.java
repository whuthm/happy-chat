package com.whuthm.happychat.domain.repository;

import com.whuthm.happychat.domain.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    User findUserByName(String name);

}
