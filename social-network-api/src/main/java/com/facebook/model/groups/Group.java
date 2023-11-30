package com.facebook.model.groups;

import com.facebook.model.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Клас, що представляє групу користувачів у соціальній мережі.
 * Група включає в себе інформацію про зображення, назву та опис.
 * Також містить перелік членів групи.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "groups")
@EqualsAndHashCode(callSuper = true)
public class Group extends AbstractEntity {

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Перелік членів групи.
     * Визначається відношенням "один до багатьох".
     */
    @OneToMany(mappedBy = "group")
    private Set<GroupMembers> members = new HashSet<>();

}
