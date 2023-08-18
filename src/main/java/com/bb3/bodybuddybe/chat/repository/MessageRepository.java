package com.bb3.bodybuddybe.chat.repository;

import com.bb3.bodybuddybe.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
