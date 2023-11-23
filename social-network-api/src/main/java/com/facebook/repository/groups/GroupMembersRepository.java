package com.facebook.repository.groups;

import com.facebook.model.groups.GroupMembers;
import com.facebook.model.groups.GroupRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupMembersRepository extends JpaRepository<GroupMembers, Long> {
    @Query("""
           SELECT gm
           FROM GroupMembers gm
           WHERE gm.group.id = :groupId
             AND gm.roles LIKE %:adminRole%
           """)
    List<GroupMembers> findAdminsByGroupId(@Param("groupId") Long groupId,
                                           @Param("adminRole") GroupRole adminRole);

    @Query("""
           SELECT gm
           FROM GroupMembers gm
           WHERE gm.group.id = :groupId
           ORDER BY gm.createdDate DESC
           """)
    List<GroupMembers> findLastMembersByGroupId(@Param("groupId") Long groupId,
                                                Pageable pageable);

}

