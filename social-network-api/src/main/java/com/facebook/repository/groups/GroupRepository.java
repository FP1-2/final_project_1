package com.facebook.repository.groups;

import com.facebook.model.groups.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {}
