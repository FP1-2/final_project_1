package com.facebook.model.groups;

import com.facebook.model.AppUser;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Log4j2
@DataJpaTest
class GroupMembersTest {

    @Autowired
    private TestEntityManager tem;

    @Test
    void testSaveAndRetrieveGroupMember() {
        AppUser user = new AppUser();
        user.setName("Test");
        user.setSurname("User");
        user.setUsername("test.user");
        user.setEmail("test.user@email.com");
        user.setPassword("password");
        user = tem.persistAndFlush(user);

        Group group = new Group();
        group.setName("Some kind of name");
        group.setDescription("Some kind of description");
        group = tem.persistAndFlush(group);

        GroupMembers member = new GroupMembers();
        member.setUser(user);
        member.setGroup(group);
        member.setRoles(new GroupRole[]{GroupRole.MEMBER});
        member = tem.persistAndFlush(member);

        assertNotNull(member.getId());

        GroupMembers foundMember = tem.find(GroupMembers.class, member.getId());

        assertEquals(member.getId(), foundMember.getId());
        assertEquals(member.getUser().getId(), foundMember.getUser().getId());
        assertEquals(member.getGroup().getId(), foundMember.getGroup().getId());
        assertArrayEquals(member.getRoles(), foundMember.getRoles());
    }

}

