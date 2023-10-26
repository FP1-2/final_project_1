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
}