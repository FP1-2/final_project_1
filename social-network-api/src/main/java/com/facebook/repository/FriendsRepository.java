package com.facebook.repository;

import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {

    @Query(value = "SELECT * FROM FRIENDS WHERE USER_ID = :userId AND FRIEND_ID = :friendId", nativeQuery = true)
    Optional<Friends> findFriendsByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

//    @Query(value = "SELECT * FROM FRIENDS WHERE USER_ID = :userId AND STATUS = 'APPROVED'", nativeQuery = true)
    @Query(value = "SELECT u.* FROM USERS u INNER JOIN FRIENDS f ON u.ID = f.FRIEND_ID AND f.STATUS ='APPROVED'  WHERE f.USER_ID = :userId", nativeQuery = true)
    Optional<AppUser> findFriendsByUserId(@Param("userId") Long userId);

//    @Query(value = "SELECT * FROM FRIENDS WHERE USER_ID = :userId AND STATUS = 'PENDING'", nativeQuery = true)
    @Query(value = "SELECT u.* FROM USERS u INNER JOIN FRIENDS f ON u.ID = f.USER_ID AND f.STATUS ='PENDING'  WHERE f.FRIEND_ID = :userId", nativeQuery = true)
    Optional<Friends> findFriendsRequestsByUserId(@Param("userId") Long userId);

//    @Modifying
//    @Query(value = "DELETE FROM FRIENDS f WHERE (f.USER_ID = :userId AND f.FRIEND_ID = :friendId) OR (f.USER_ID = :friendId AND f.FRIEND_ID = :userId)", nativeQuery = true)
//    void deleteFriendByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query(value = "SELECT * FROM FRIENDS WHERE USER_ID = :userId AND STATUS = :status", nativeQuery = true)
    List<Friends> findFriendsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    boolean existsByUserIdAndFriendId(Long userId, Long friendId);

    List<Friends> findByUserId(Long userId);

}
