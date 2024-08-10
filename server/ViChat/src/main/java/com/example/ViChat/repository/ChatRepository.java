package com.example.ViChat.repository;

import com.example.ViChat.entity.Chat;
import com.example.ViChat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c WHERE c.isGroup=false AND :user Member of c.users AND :reqUser Member of c.users")
    Chat findSingleChatByUserIds(@Param("user")User user, @Param("reqUser") User reqUser);

    @Query("SELECT c FROM Chat c JOIN c.users u WHERE u.id=:userId")
    List<Chat> findChatByUserId(@Param("userId") String userId);
}
