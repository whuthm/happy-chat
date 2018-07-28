package com.whuthm.happychat.domain.repository;

import com.whuthm.happychat.domain.model.Message;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
}
