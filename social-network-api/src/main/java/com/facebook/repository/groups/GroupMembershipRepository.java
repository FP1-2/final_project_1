package com.facebook.repository.groups;

import com.facebook.dto.groups.GroupMembershipDto;
import com.facebook.model.groups.GroupMembership;
import com.facebook.model.groups.GroupRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    @Query("""
           SELECT gm
           FROM GroupMembership gm
           WHERE gm.group.id = :groupId
             AND gm.roles LIKE %:adminRole%
           """)
    List<GroupMembership> findAdminsByGroupId(@Param("groupId") Long groupId,
                                              @Param("adminRole") GroupRole adminRole);

    @Query("""
           SELECT gm
           FROM GroupMembership gm
           WHERE gm.group.id = :groupId
           ORDER BY gm.createdDate DESC
           """)
    List<GroupMembership> findLastMembersByGroupId(@Param("groupId") Long groupId,
                                                   Pageable pageable);

}

