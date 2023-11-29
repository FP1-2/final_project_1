package com.facebook.repository;

import com.facebook.model.AppUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);
    @Query(value =
            "SELECT * FROM users u " +
                    "WHERE CONCAT(LOWER(u.name), ' ', LOWER(u.surname)) " +
                    "LIKE LOWER(CONCAT('%', :input, '%')) " +
                    "AND u.id <> :id", nativeQuery = true)
    List<AppUser> searchUserByNameAndUsername(@Param("input")String keyword, @Param("id")Long excludedAuthUserId, Pageable pageable);

    @Query(value = "SELECT u.* FROM users u INNER JOIN friends f ON u.ID = f.FRIEND_ID AND f.STATUS ='APPROVED'  WHERE f.USER_ID = :userId", nativeQuery = true)
    List<AppUser> findUserFriendsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT u.* FROM users u INNER JOIN friends f ON u.ID = f.USER_ID AND f.STATUS ='PENDING'  WHERE f.FRIEND_ID = :userId", nativeQuery = true)
    List<AppUser> findUserReceivedFriendsRequestsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT u.* FROM users u INNER JOIN friends f ON u.ID = f.FRIEND_ID AND f.STATUS ='PENDING'  WHERE f.USER_ID = :userId", nativeQuery = true)
    List<AppUser> findUserSendFriendsRequestsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT u.* FROM users u INNER JOIN friends f ON u.id = f.friend_id AND f.status ='APPROVED'" +
            "WHERE CONCAT(LOWER(u.name), ' ', LOWER(u.surname)) " +
            "LIKE LOWER(CONCAT('%', :input, '%')) AND u.id <> :id AND f.user_id = :id", nativeQuery = true)
    List<AppUser> searchFriendsByNameAndSurname(@Param("input") String input, @Param("id") Long excludedAuthUserId);
}