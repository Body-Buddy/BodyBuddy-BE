package com.bb3.bodybuddybe.chat.repository;

import com.bb3.bodybuddybe.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByGym_Id(Long gymId);

    @Query("SELECT c FROM Chat c " +
            "WHERE c.gym.id = :gymId AND c.chatType = 'DIRECT' " +
            "AND EXISTS (SELECT cp1 FROM c.participants cp1 WHERE cp1.user.id = :userId1) " +
            "AND EXISTS (SELECT cp2 FROM c.participants cp2 WHERE cp2.user.id = :userId2)")
    Chat findDirectChatBetweenUsers(@Param("gymId") Long gymId,
                                    @Param("userId1") Long userId1,
                                    @Param("userId2") Long userId2);
}
