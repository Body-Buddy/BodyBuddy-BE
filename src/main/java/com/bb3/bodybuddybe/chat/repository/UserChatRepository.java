package com.bb3.bodybuddybe.chat.repository;

import com.bb3.bodybuddybe.chat.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {

}
