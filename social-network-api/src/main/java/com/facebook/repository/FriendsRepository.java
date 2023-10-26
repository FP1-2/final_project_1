package com.facebook.repository;

import com.facebook.model.friends.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {

    @Query(value = "SELECT * FROM FRIENDS WHERE USER_ID = :userId AND FRIEND_ID = :friendId", nativeQuery = true)
    Optional<Friends> findFriendsByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query(value = "SELECT * FROM FRIENDS WHERE USER_ID = :userId AND STATUS = 'APPROVED'", nativeQuery = true)
    Optional<Friends> findFriendsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM FRIENDS WHERE USER_ID = :userId AND STATUS = 'PENDING'", nativeQuery = true)
    Optional<Friends> findFriendsRequestsByUserId(@Param("userId") Long userId);

}
