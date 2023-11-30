package com.facebook.model.groups;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@DataJpaTest
class GroupTest {

    @Autowired
    private TestEntityManager tem;

    @Test
    void testSaveAndRetrieveGroup() {
        Group group = new Group();
        group.setName("Test Group");
        group.setDescription("Test Description");
        group.setImageUrl("https://example.com/image.jpg");
        group = tem.persistAndFlush(group);

        assertNotNull(group.getId());

        Group foundGroup = tem.find(Group.class, group.getId());

        assertEquals(group.getId(), foundGroup.getId());
        assertEquals(group.getName(), foundGroup.getName());
        assertEquals(group.getDescription(), foundGroup.getDescription());
        assertEquals(group.getImageUrl(), foundGroup.getImageUrl());
    }

}

