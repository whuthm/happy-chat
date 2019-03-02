package com.whuthm.happychat.domain.repository;

import com.whuthm.happychat.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

    User findUserByName(String name);

    User findUserByEmail(String email);

}
