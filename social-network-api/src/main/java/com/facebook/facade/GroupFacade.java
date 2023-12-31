package com.facebook.facade;

import com.facebook.dto.groups.GroupMember;
import com.facebook.dto.groups.GroupPostBase;
import com.facebook.dto.groups.GroupPostRequest;
import com.facebook.dto.groups.GroupPostResponse;
import com.facebook.dto.groups.GroupRepostRequest;
import com.facebook.dto.groups.UserGroup;
import com.facebook.model.AppUser;
import com.facebook.model.groups.Group;
import com.facebook.model.groups.GroupMembers;
import com.facebook.model.groups.GroupPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Фасад для перетворення об'єктів, пов'язаних з групами.
 * Використовує ModelMapper для спрощення процесу мапінгу між сутностями і DTO.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class GroupFacade {

    private final ModelMapper modelMapper;

    /**
     * Перетворює об'єкт GroupMembers в DTO.
     *
     * @param members Об'єкт GroupMembers, який необхідно перетворити.
     * @return GroupMembersDto - DTO, який представляє членство у групі.
     */
    public GroupMember mapToGroupMembersDto(GroupMembers members) {
        GroupMember dto = new GroupMember();
        dto.setId(members.getId());
        dto.setUser(modelMapper.map(members.getUser(), UserGroup.class));
        dto.setRoles(members.getRoles());
        return dto;
    }

    /**

     Конвертує запит на створення посту групи в сутність GroupPost.
     Цей метод використовується для створення нового посту в групі на основі даних, отриманих у запиті.
     @param request Запит на створення посту групи, що містить інформацію для створення нового посту.
     @param user Користувач, який створює пост.
     @param group Група, у якій створюється пост.
     @return Створений об'єкт GroupPost, що відображає новий пост у групі.
     */
    public GroupPost convertGroupPostRequestToGroupPost(GroupPostRequest request,
                                                        AppUser user,
                                                        Group group) {
        GroupPost groupPost = modelMapper.map(request, GroupPost.class);
        groupPost.setGroup(group);
        groupPost.setUser(user);
        return groupPost;
    }

    /**
     * Конвертує запит на створення репосту у групі в ентіті GroupPost.
     *
     * @param request Запит на створення репосту.
     * @param user Користувач, який створює репост.
     * @param group Група, у якій створюється репост.
     * @return Екземпляр GroupPost, що відображає новий репост у групі.
     */
    public GroupPost convertGroupRepostRequestToGroupPost(@NotNull GroupRepostRequest request,
                                                          AppUser user,
                                                          Group group) {
        GroupPost groupPost = new GroupPost();
        groupPost.setImageUrl(request.getImageUrl());
        groupPost.setTitle(request.getTitle());
        groupPost.setBody(request.getBody());
        groupPost.setType(request.getType());
        groupPost.setOriginalPostId(request.getOriginalPostId());

        groupPost.setGroup(group);
        groupPost.setUser(user);
        return groupPost;
    }

    /**

     Конвертує базову інформацію про пост групи (GroupPostBase) в повну відповідь GroupPostResponse.
     Цей метод використовується для додавання додаткової інформації про члена групи, який створив пост.
     @param base Об'єкт GroupPostBase, що містить базову інформацію про пост групи.
     @param members Інформація про члена групи, який створив пост.
     @return Об'єкт GroupPostResponse, що відображає деталізовану інформацію про пост групи, включаючи дані про автора поста.
     */
    public GroupPostResponse convertGroupPostBaseToGroupPostResponse(GroupPostBase base,
                                                                     GroupMembers members){
        GroupPostResponse response = modelMapper.map(base, GroupPostResponse.class);
        GroupMember groupMember = modelMapper.map(members, GroupMember.class);
        groupMember.setUser(modelMapper.map(members.getUser(), UserGroup.class));
        response.setAuthorMember(groupMember);
        response.setAuthor(null);
        response.setOriginalPostId(null);
        return response;
    }

    /**
     * Конвертує об'єкт GroupPostBase в GroupPostResponse.
     * Цей метод створює DTO для оригінального поста без рекурсії.
     * Якщо у поста є originalPostId, то створюється DTO для оригінального поста,
     * але без включення вкладеного оригінального поста.
     *
     * @param postBase Об'єкт GroupPostBase, який потрібно конвертувати.
     * @param membersMap Мапа для швидкого доступу до GroupMember за ID користувача.
     * @param originalPostsMap Мапа для швидкого доступу до GroupPostBase оригінальних постів.
     * @return GroupPostResponse сконвертований об'єкт GroupPostResponse.
     */
    public @NotNull GroupPostResponse convertToGroupPostResponse(@NotNull GroupPostBase postBase,
                                                                  @NotNull Map<Long, GroupMember> membersMap,
                                                                  Map<Long, GroupPostBase> originalPostsMap) {
        GroupMember authorMember = membersMap.get(postBase.getAuthor().getUserId());

        // Створення DTO для оригінального поста без рекурсії
        GroupPostResponse originalPostResponse = null;
        if (postBase.getOriginalPostId() != null) {
            GroupPostBase originalPostBase = originalPostsMap.get(postBase.getOriginalPostId());
            if (originalPostBase != null) {
                // Створюємо DTO для оригінального поста, але без внесеного оригінального поста
                originalPostResponse = modelMapper.map(originalPostBase, GroupPostResponse.class);
                GroupMember originalAuthorMember = membersMap.get(originalPostBase.getAuthor().getUserId());
                originalPostResponse.setAuthorMember(originalAuthorMember);
                originalPostResponse.setOriginalPost(null); // Здесь не возвращаем вложенный оригинальный пост
            }
        }

        GroupPostResponse groupPostResponse = modelMapper.map(postBase, GroupPostResponse.class);
        groupPostResponse.setAuthor(null);
        groupPostResponse.setAuthorMember(authorMember);
        groupPostResponse.setOriginalPost(originalPostResponse);

        return groupPostResponse;
    }

    /**
     * Конвертує об'єкт GroupPostBase в GroupPostResponse.
     * Цей метод використовує рекурсію для створення повної ланцюжка репостів,
     * де кожен репост посилається на свій оригінальний пост.
     *
     * @param postBase Об'єкт GroupPostBase, який потрібно конвертувати.
     * @param membersMap Мапа для швидкого доступу до GroupMember за ID користувача.
     * @param originalPostsMap Мапа для швидкого доступу до GroupPostBase оригінальних постів.
     * @return GroupPostResponse сконвертований об'єкт GroupPostResponse.
     */
    public @NotNull GroupPostResponse convertToGroupPostResponse0(@NotNull GroupPostBase postBase,
                                                                   @NotNull Map<Long, GroupMember> membersMap,
                                                                   Map<Long, GroupPostBase> originalPostsMap) {
        GroupMember authorMember = membersMap.get(postBase.getAuthor().getUserId());
        GroupPostResponse originalPostResponse = null;
        if (postBase.getOriginalPostId() != null) {
            GroupPostBase originalPostBase = originalPostsMap.get(postBase.getOriginalPostId());
            if (originalPostBase != null) {
                originalPostResponse = convertToGroupPostResponse(originalPostBase, membersMap, originalPostsMap);
            }
        }
        GroupPostResponse groupPostResponse = modelMapper.map(postBase, GroupPostResponse.class);
        groupPostResponse.setAuthor(null);
        groupPostResponse.setAuthorMember(authorMember);
        groupPostResponse.setOriginalPost(originalPostResponse);
        return groupPostResponse;
    }

}
