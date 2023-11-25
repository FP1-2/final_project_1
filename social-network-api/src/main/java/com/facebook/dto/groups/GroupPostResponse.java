package com.facebook.dto.groups;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Клас DTO для представлення детальної інформації про пости у групах.
 * Цей клас є розширенням базового DTO {@link GroupPostBase} та додатково містить інформацію
 * про оригінальний пост, у випадку якщо поточний пост є репостом.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupPostResponse extends GroupPostBase {
    private GroupPostResponse originalPost;

}