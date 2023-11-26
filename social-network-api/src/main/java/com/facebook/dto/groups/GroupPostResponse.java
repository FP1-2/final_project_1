package com.facebook.dto.groups;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Клас DTO для представлення детальної інформації про пости у групах.
 * Цей клас наслідується від GroupPostBase, що включає базову інформацію про пост,
 * отриману через JPQL запит. GroupMember забезпечуючи додаткову інформацію про учасника групи.
 * Також клас містить поле 'originalPost', яке активується, якщо поточний пост є репостом,
 * надаючи інформацію про оригінальний пост.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupPostResponse extends GroupPostBase{
    private GroupMember authorMember;
    private GroupPostResponse originalPost;

}