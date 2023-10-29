package com.facebook.repository;

import com.facebook.model.AppUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    @Query(value = "SELECT u.* FROM USERS u INNER JOIN FRIENDS f ON u.ID = f.FRIEND_ID AND f.STATUS ='APPROVED'  WHERE f.USER_ID = :userId", nativeQuery = true)
    List<AppUser> findUserFriendsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT u.* FROM USERS u INNER JOIN FRIENDS f ON u.ID = f.USER_ID AND f.STATUS ='PENDING'  WHERE f.FRIEND_ID = :userId", nativeQuery = true)
    List<AppUser> findUserFriendsRequestsByUserId(@Param("userId") Long userId);

}