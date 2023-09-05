package com.bb3.bodybuddybe.chat.repository;

import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.UserChat;
import com.bb3.bodybuddybe.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {

    List<UserChat> findAllByUserAndChat_Gym_Id(User user, Long gymId);
    Optional<UserChat> findByUserAndChatId(User user, Long chatId);
    boolean existsByChatAndUser(Chat chat, User user);
}
