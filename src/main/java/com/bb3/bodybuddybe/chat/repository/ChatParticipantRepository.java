package com.bb3.bodybuddybe.chat.repository;

import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.ChatParticipant;
import com.bb3.bodybuddybe.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findAllByUserAndChat_Gym_Id(User user, Long gymId);
    Optional<ChatParticipant> findByUserAndChatId(User user, Long chatId);
    boolean existsByChatAndUser(Chat chat, User user);
}
