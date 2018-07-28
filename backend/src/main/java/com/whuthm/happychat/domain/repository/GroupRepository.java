package com.whuthm.happychat.domain.repository;

import com.whuthm.happychat.domain.model.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, String> {
}
