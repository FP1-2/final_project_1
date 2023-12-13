package com.facebook.repository.groups;

import com.facebook.dto.groups.GroupJpql;
import com.facebook.model.groups.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("""
            SELECT new com.facebook.dto.groups.GroupJpql(
                g.id,
                g.createdDate,
                g.lastModifiedDate,
                g.imageUrl,
                g.name,
                g.description,
                COUNT(m.id),
                g.isPublic
            )
            FROM Group g
            LEFT JOIN g.members m
            WHERE g.id = :groupId
            GROUP BY g.id,
            g.createdDate,
            g.lastModifiedDate,
            g.imageUrl,
            g.name,
            g.description,
            g.isPublic
            """)
    Optional<GroupJpql> getGroupById(@Param("groupId") Long groupId);
}
