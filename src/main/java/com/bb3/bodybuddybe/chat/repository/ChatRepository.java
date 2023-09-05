package com.bb3.bodybuddybe.chat.repository;

import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByGym_Id(Long gymId);

    List<Chat> findAllByGym_IdAndOwnerUserAndChatType_Direct(Long gymId, User user);
}
