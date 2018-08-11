package com.whuthm.happychat.domain.repository;

import com.whuthm.happychat.domain.model.OfflineMessage;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OfflineMessageRepository extends PagingAndSortingRepository<OfflineMessage, Long> {
}
