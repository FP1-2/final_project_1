package com.facebook.utils;

import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.appuser.GenAppUser;
import com.facebook.dto.groups.GroupRequest;
import com.facebook.dto.post.CommentRequest;
import com.facebook.dto.post.PostRequest;
import com.facebook.dto.post.PostResponse;
import com.facebook.dto.post.RepostRequest;
import com.facebook.exception.AlreadyExistsException;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.model.chat.ContentType;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;
import com.facebook.model.posts.Comment;
import com.facebook.model.posts.Like;
import com.facebook.repository.ChatRepository;
import com.facebook.repository.MessageRepository;
import com.facebook.repository.posts.CommentRepository;
import com.facebook.repository.posts.LikeRepository;
import com.facebook.service.AppUserService;
import com.facebook.service.FriendsService;
import com.facebook.service.ChatService;
import com.facebook.service.MessageService;
import com.facebook.service.PostService;
import com.facebook.service.favorites.FavoritesService;
import com.facebook.service.groups.GroupService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Клас Gen призначений для генерації даних та заповнення
 * БД цими даними. За замовчуванням запускається з профілем Dev.
 * Генерує 14 користувачів AppUser.
 * Містить тестового користувача з відомими авторизаційними даними з
 * username "test",
 * password "Password1!".
 */

@Log4j2
@SuppressWarnings("all")
public class Gen {
    private static final String AVATAR = "https://via.placeholder.com/150/66b7d2";
    private static final String HEADER_PHOTO = "https://source.unsplash.com/random?wallpapers";
    private static final Set<Integer> indexAdmins = new HashSet<>(Arrays.asList(19, 22, 23, 45, 51, 68, 73, 79, 81, 98));
    private static final String DEFAULT_USERNAME = "test";
    private static final String DEFAULT_PASSWORD = "Password1!";
    private static final String DEFAULT_USERNAME2 = "test2";
    private static final String DEFAULT_PASSWORD2 = "Password2!";
    public final ApplicationContext context;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AppUserService appUserService;
    private final FriendsService friendsService;
    private final PasswordEncoder passwordEncoder;
    private final AppUserFacade appUserFacade;
    private final PostService postService;
    private final FavoritesService favoritesService;
    private final GroupService groupService;
    private List<AppUser> appUsers1;
    private List<PostResponse> posts;
    private List<Comment> comments;
    private List<Like> likes;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private List<Chat> chats;
    private List<Message> messages;

    private Gen(ApplicationContext context) {
        this.context = context;

        this.appUserService = context.getBean(AppUserService.class);
        this.passwordEncoder = context.getBean(PasswordEncoder.class);
        this.appUserFacade = context.getBean(AppUserFacade.class);
        this.postService = context.getBean(PostService.class);
        this.favoritesService = context.getBean(FavoritesService.class);
        this.friendsService = context.getBean(FriendsService.class);
        this.chatService = context.getBean(ChatService.class);
        this.messageService = context.getBean(MessageService.class);
        this.groupService = context.getBean(GroupService.class);

        this.likeRepository = context.getBean(LikeRepository.class);
        this.commentRepository = context.getBean(CommentRepository.class);
        this.chatRepository = context.getBean(ChatRepository.class);
        this.messageRepository = context.getBean(MessageRepository.class);

        this.appUsers1 = genAppUser();
        this.posts = genPostsAndReposts();
        this.comments = genComments();
        this.likes = genLikes();
        genFriends();
        genFavorites();
        genGrops();
        genGropsMemmbers();
        addedMemmbersToGrops();
        this.chats = genChats();
        this.messages = genMessages();
    }

    public static Gen of(ApplicationContext context) {
        return new Gen(context);
    }

    private <T> List<T> readJsonData(String resourcePath, TypeReference<List<T>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("./dataForGen/" + resourcePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Ресурс не найден: "
                        + "dataForGen/" + resourcePath);
            }
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private final List<GenAppUser> appUsers = readJsonData("users.json",
            new TypeReference<List<GenAppUser>>() {});

    private final String[] password = new String[]{"568D!6s", "S06i7*378", "Ff063?82@",
            "ldsf~gb1K", "oEk%jdf57", "0&98ZnSdfg", "*g!H235h", "WgfIl63?", "2hdsU56F!",
            "gdhC%12U", "0Qwezx&63", "0!H7jfd3?", "09jjI3*U"};

    private List<AppUser> genAppUser() {
        appUsers.forEach(user -> createAppUser(user));
        //Додаємо дефолтних користувачів.
        readJsonData("defaultUsers.json", new TypeReference<List<GenAppUser>>() {})
                .forEach(user -> createAppUser(user));

        return appUserService.findAll();
    }

    private void genGropsMemmbers(){
        List<GenAppUser> members = readJsonData("groupMembers.json",
                new TypeReference<List<GenAppUser>>() {});
        List<GenAppUser> admins = readJsonData("groupAdmins.json",
                new TypeReference<List<GenAppUser>>() {});

        for(int i = 0, a = 0, m = 0; i < 109; i++ ) {
            if(indexAdmins.contains(i)) {
                createAppUser(admins.get(a));
                a++;
            }
            else {
                createAppUser(members.get(m));
                m++;
            }
        }
    }

    private void addedMemmbersToGrops(){

        for (int userId = 16; userId <= 124; userId++) {
            // Пропускаємо адмінів існуючих груп
            if (userId == 13 || userId == 14 || userId == 15) {
                continue;
            }

            int groupCount = determineGroupCount();
            Set<Integer> assignedGroups = new HashSet<>();

            for (int i = 0; i < groupCount; i++) {
                int groupId;
                do {
                    groupId = MathUtils.random(1, 4);
                // Гарантуємо унікальність групи для користувача
                } while (!assignedGroups.add(groupId));

                groupService.joinGroup((long) groupId, (long) userId);
            }
        }
    }

    private static int determineGroupCount() {
        int chance = MathUtils.random(1, 100);
        if (chance <= 10) return 4;   // 10% на 4 групи
        if (chance <= 30) return 3;   // 20% на 3 групи
        if (chance <= 60) return 2;   // 30% на 2 групи
        return 1;                     // Решта на 1 групу
    }

    private void createAppUser(GenAppUser dto) {
        AppUser appUser = appUserFacade.convertToAppUser(dto);
        appUser.setRoles(new String[]{"USER"});

        String encodedPassword = Optional.of(dto)
                .filter(user -> DEFAULT_USERNAME.equals(user.getUsername()))
                .map(user -> passwordEncoder.encode(DEFAULT_PASSWORD))
                .orElseGet(() -> Optional.of(dto)
                        .filter(user -> DEFAULT_USERNAME2.equals(user.getUsername()))
                        .map(user -> passwordEncoder.encode(DEFAULT_PASSWORD2))
                        .orElse(passwordEncoder.encode(password[MathUtils.random(0, 12)])));

        appUser.setPassword(encodedPassword);
        appUserService.save(appUser);
    }

    private List<Like> genLikes() {
        posts.forEach(post -> {
            appUsers1.forEach(user -> {
                if (MathUtils.random(0, 20) > 10) {
                    postService.likePost(user.getId(), post.getPostId());
                }
            });
        });

        return likeRepository.findAll();
    }

    private List<Comment> genComments() {
        Faker faker = new Faker();
        posts.forEach(post -> {
            IntStream.range(1, MathUtils.random(4, 10) + 1)
                    .forEach(ignored -> {
                        Long randomUserId = appUsers1
                                .get(MathUtils.random(0, appUsers1.size() - 1)).getId();
                        CommentRequest request = new CommentRequest();
                        request.setPostId(post.getPostId());
                        request.setContent(faker.lorem().sentence());
                        postService.addComment(randomUserId, request);
                    });
        });

        return commentRepository.findAll();
    }

    private List<PostResponse> genPostsAndReposts() {
        List<AppUser> allUsers = appUserService.findAll();
        Faker faker = new Faker();
        List<PostResponse> createdPosts = new ArrayList<>();

        allUsers.forEach(user -> {
            IntStream.rangeClosed(1, MathUtils.random(4, 10)).forEach(ignored -> {
                PostRequest postRequest = createPostRequest(faker);
                PostResponse postResponse = postService.createPost(postRequest, user.getId());
                createdPosts.add(postResponse);

                int repostsCount = MathUtils.random(0, 3);
                for (int i = 0; i < repostsCount; i++) {
                    List<AppUser> potentialReposters = allUsers.stream()
                            .filter(u -> !u.equals(user))
                            .collect(Collectors.toCollection(ArrayList::new));
                    Collections.shuffle(potentialReposters);

                    AppUser repostingUser = potentialReposters.get(0);
                    RepostRequest repostRequest = createRepostRequest(faker, postResponse.getPostId());
                    postService.createRepost(repostRequest, repostingUser.getId());
                }
            });
        });
        return createdPosts;
    }

    private PostRequest createPostRequest(Faker faker) {
        PostRequest postRequest = new PostRequest();
        postRequest.setImageUrl(HEADER_PHOTO);
        postRequest.setTitle(String.join(" ", faker.lorem().words(MathUtils.random(1, 5))));
        postRequest.setBody(faker.lorem().paragraph());
        return postRequest;
    }

    private RepostRequest createRepostRequest(Faker faker, Long originalPostId) {
        RepostRequest repostRequest = new RepostRequest();
        repostRequest.setImageUrl(HEADER_PHOTO);
        repostRequest.setTitle(String.join(" ", faker.lorem().words(MathUtils.random(1, 5))));
        repostRequest.setBody(faker.lorem().paragraph());
        repostRequest.setOriginalPostId(originalPostId);
        return repostRequest;
    }

    /**
     * Генерація запитів на додавання та підтвердження дружби між користувачами.
     */
    private void genFriends() {
        // Знаходимо користувачів за дефолтом
        AppUser defaultUser1 = appUserService.findByUsername(DEFAULT_USERNAME)
                .orElseThrow(() -> new NotFoundException("Default user not found!"));

        AppUser defaultUser2 = appUserService.findByUsername(DEFAULT_USERNAME2)
                .orElseThrow(() -> new NotFoundException("Default user 2 not found!"));

        try {
            // Надсилаємо запит на дружбу між користувачами та підтверджуємо його
            friendsService.sendFriendRequest(defaultUser1.getId(), defaultUser2.getId());
            friendsService.changeFriendsStatus(defaultUser1.getId(), defaultUser2.getId(), true);
        } catch (AlreadyExistsException e) {
            //log.info("Friend request already exists between default users.");
        }

        // Генерація запитів на додавання друзів для інших користувачів
        appUsers1.forEach(user -> appUsers1.stream()
                .filter(potentialFriend -> !user.equals(potentialFriend) && MathUtils.random(0, 20) > 17)
                .forEach(potentialFriend -> {
                    try {
                        friendsService.sendFriendRequest(user.getId(), potentialFriend.getId());
                    } catch (AlreadyExistsException e) {
                        //log.info("Запит у друзі вже надіслано: {}", e.getMessage());
                    }
                })
        );

        // Генерація відповідей на запити на додавання до друзів
        appUsers1.forEach(user -> {
            if (MathUtils.random(0, 10) < 4) {
                List<AppUserResponse> friendsRequestList = friendsService.getFriendsRequest(user.getId());
                //log.info("user: {} {}", user.getId(), friendsRequestList);
                try {
                    friendsRequestList.forEach(friendship -> {
                        boolean acceptFriendship = MathUtils.random(0, 10) < 5;
                        friendsService.changeFriendsStatus(
                                friendship.getId(),
                                user.getId(),
                                acceptFriendship
                        );
                    });
                } catch (NotFoundException | AlreadyExistsException e) {
                    //log.info("Запит на додавання до друзів не прийнято не знайдено id: {}", e.getMessage());
                }
            }
        });
    }

    private List<Chat> genChats() {
        Optional<AppUser> test = appUserService.findByUsername("test");
        Optional<AppUser> greak = appUserService.findByUsername("Greak");
        Optional<AppUser> bretNickname = appUserService.findByUsername("BretNickname");

        Chat chat1 = Chat.of(test.get(), greak.get());
        chatRepository.save(chat1);
        Chat chat2 = Chat.of(bretNickname.get(), test.get());
        chatRepository.save(chat2);

        return chatRepository.findAll();
    }

    private List<Message> genMessages() {
        Faker faker = new Faker();
        chats.forEach(chat -> {
            chat.getChatParticipants().forEach(participant -> {
                String text = faker.lorem().sentence();
                Message message = Message.of(ContentType.TEXT, text, participant, chat, MessageStatus.SENT);
                messageRepository.save(message);
            });
        });
        return messageRepository.findAll();
    }

    private void genFavorites() {
        appUsers1.forEach(user -> {
            int randomFavoritesCount = MathUtils.random(0, 10);
            for (int i = 0; i < randomFavoritesCount; i++) {
                PostResponse randomPost = posts.get(MathUtils.random(0, posts.size() - 1));

                try {
                    favoritesService.addToFavorites(randomPost.getPostId(), user.getId());
                } catch (AlreadyExistsException e) {
                }
            }
        });
    }

    private void genGrops(){
        //groups.json
     List<GroupRequest> g = readJsonData("groups.json",
             new TypeReference<List<GroupRequest>>() {});

       for(int userId = 13, groupId = 0 ; userId < 16; userId++, groupId++) {
           groupService.createGroup(g.get(groupId), (long) userId);
       };
        groupService.createGroup(g.get(3), 14L);
    }

}
