package com.bb3.bodybuddybe.chat.repository;

import com.bb3.bodybuddybe.chat.entity.GroupChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatMemberRepository extends JpaRepository<GroupChatMember, Long> {

}
