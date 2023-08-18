package com.bb3.bodybuddybe.chat.repository;

import com.bb3.bodybuddybe.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
