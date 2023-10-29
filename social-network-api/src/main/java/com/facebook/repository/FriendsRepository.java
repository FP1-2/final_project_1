package com.facebook.repository;

import com.facebook.model.friends.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {

    @Query(value = "SELECT * FROM FRIENDS WHERE USER_ID = :userId AND FRIEND_ID = :friendId", nativeQuery = true)
    Optional<Friends> findFriendsByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query(value = "SELECT * FROM FRIENDS WHERE USER_ID = :userId AND STATUS = :status", nativeQuery = true)
    List<Friends> findFriendsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    boolean existsByUserIdAndFriendId(Long userId, Long friendId);

    List<Friends> findByUserId(Long userId);

}
