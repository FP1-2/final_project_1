package com.facebook.utils;

import com.facebook.dto.appuser.GenAppUser;
import com.facebook.dto.post.CommentRequest;
import com.facebook.dto.post.PostRequest;
import com.facebook.dto.post.PostResponse;
import com.facebook.dto.post.RepostRequest;
import com.facebook.exception.AlreadyExistsException;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.model.friends.FriendsStatus;
import com.facebook.model.chat.Chat;
import com.facebook.model.chat.ContentType;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;
import com.facebook.model.posts.Comment;
import com.facebook.model.posts.Like;
import com.facebook.model.posts.PostStatus;
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
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        this.chats = genChats();
        this.messages = genMessages();
    }

    public static Gen of(ApplicationContext context) {
        return new Gen(context);
    }

    private final List<GenAppUser> appUsers = List.of(
            new GenAppUser("George",
                    "Washington",
                    "Greak",
                    "g_rafk@ukr.net",
                    "Westmorland Virginia",
                    AVATAR,
                    HEADER_PHOTO,
                    42),
            new GenAppUser("Bret",
                    "Johnson",
                    "BretNickname",
                    "Sincere@april.biz",
                    "Random Address 1",
                    AVATAR,
                    HEADER_PHOTO,
                    19),
            new GenAppUser("Ervin",
                    "Smith",
                    "ErvinNickname",
                    "Shanna@melissa.tv",
                    "Random Address 2",
                    AVATAR,
                    HEADER_PHOTO,
                    31),
            new GenAppUser("Clementine",
                    "Jones",
                    "ClemNickname",
                    "clementine_b@ukr.net",
                    "Random Address 3",
                    AVATAR,
                    HEADER_PHOTO,
                    27),
            new GenAppUser("Patricia",
                    "Williams",
                    "PattyNickname",
                    "Nathan@yesenia.net",
                    "Random Address 4",
                    AVATAR,
                    HEADER_PHOTO,
                    58),
            new GenAppUser("Karianne",
                    "Brown",
                    "KariNickname",
                    "Julianne.OConner@kory.org",
                    "Random Address 5",
                    AVATAR,
                    HEADER_PHOTO,
                    29),
            new GenAppUser("Kamren",
                    "Taylor",
                    "KamNickname",
                    "Lucio_Hettinger@annie.ca",
                    "Random Address 6",
                    AVATAR,
                    HEADER_PHOTO,
                    19),
            new GenAppUser("Leopoldo_Corkery",
                    "Miller",
                    "LeoNickname",
                    "Karley_Dach@jasper.info",
                    "Random Address 7",
                    AVATAR,
                    HEADER_PHOTO,
                    56),
            new GenAppUser("Elwyn.Skiles",
                    "Davis",
                    "ElwynNickname",
                    "Telly.Hoeger@billy.biz",
                    "Random Address 8",
                    AVATAR,
                    HEADER_PHOTO,
                    45),
            new GenAppUser("Maxime_Nienow",
                    "Garcia",
                    "MaxNickname",
                    "Sherwood@rosamond.me",
                    "Random Address 9",
                    AVATAR,
                    HEADER_PHOTO,
                    98),
            new GenAppUser("Glenna Reichert",
                    "Wilson",
                    "GlenNickname",
                    "Chaim_McDermott@dana.io",
                    "Random Address 10",
                    AVATAR,
                    HEADER_PHOTO,
                    39),
            new GenAppUser("Clementina DuBuque",
                    "Moore",
                    "ClemenNickname",
                    "Rey.Padberg@karina.biz",
                    "Random Address 11",
                    AVATAR,
                    HEADER_PHOTO,
                    51),
            new GenAppUser("Julius",
                    "Caesar",
                    "GaiusJuliusCaesar",
                    "J_Caesar@roma.republic",
                    "Suburra, Ancient Rome",
                    AVATAR,
                    HEADER_PHOTO,
                    55)
    );

    private final String[] password = new String[]{"568D!6s", "S06i7*378", "Ff063?82@",
            "ldsf~gb1K", "oEk%jdf57", "0&98ZnSdfg", "*g!H235h", "WgfIl63?", "2hdsU56F!",
            "gdhC%12U", "0Qwezx&63", "0!H7jfd3?", "09jjI3*U"};

    private List<AppUser> genAppUser() {

        appUsers.forEach(dto -> createAppUser(dto));

        //Окремо додаємо дефолтного користувача.
        createAppUser(new GenAppUser("Clementina DuBuque",
                "test",
                DEFAULT_USERNAME,
                "test@test.biz",
                "Address 11",
                AVATAR,
                HEADER_PHOTO,
                51));

        createAppUser(new GenAppUser("Second Default User",
                "test2",
                DEFAULT_USERNAME2,
                "test2@test.biz",
                "Address 22",
                AVATAR,
                HEADER_PHOTO,
                52));

        return appUserService.findAll();
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

    private static PostStatus getRandomPostStatus() {
        return PostStatus
                .values()[MathUtils.random(0, PostStatus.values().length - 1)];
    }

    private List<Like> genLikes() {
        posts.forEach(post -> {
            appUsers1.forEach(user -> {
                if (MathUtils.random(0, 20) == 20) {
                    postService.likePost(user.getId(), post.getPostId());
                }
            });
        });

        return likeRepository.findAll();
    }

    private List<Comment> genComments() {
        Faker faker = new Faker();
        posts.forEach(post -> {
            IntStream.range(1, MathUtils.random(1, 5) + 1)
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


    private void genFriends() {
        appUsers1.stream()
                .forEach(user -> {
                    appUsers1
                            .stream()
                            .filter(potentialFriend -> {
                                return !user.equals(potentialFriend) && MathUtils.random(0, 10) == 10;
                            })
                            .forEach(potentialFriend -> {
                                try {
                                    friendsService
                                            .sendFriendRequest(user.getId(),
                                                    potentialFriend.getId());
                                } catch (AlreadyExistsException e) {
                                }
                            });
                });

        appUsers1.forEach(user -> {
            if (MathUtils.random(0, 10) < 3) {
                List<Friends> friendsList = friendsService.getFriendsListByUserIdAndStatus(user.getId());

                friendsList.forEach(friendship -> {
                    if (friendship.getStatus() == FriendsStatus.PENDING) {
                        boolean acceptFriendship = MathUtils.random(0, 10) < 5;
                        friendsService.changeFriendsStatus(
                                friendship.getUser().getId(),
                                friendship.getFriend().getId(),
                                acceptFriendship
                        );
                    }
                });
            }
        });

        AppUser defaultUser1 = appUserService.findByUsername(DEFAULT_USERNAME)
                .orElseThrow(() -> new NotFoundException("Default user not found!"));

        AppUser defaultUser2 = appUserService.findByUsername(DEFAULT_USERNAME2)
                .orElseThrow(() -> new NotFoundException("Default user 2 not found!"));

        try {
            friendsService.sendFriendRequest(defaultUser1.getId(), defaultUser2.getId());
            friendsService.changeFriendsStatus(defaultUser1.getId(), defaultUser2.getId(), true);
        } catch (AlreadyExistsException e) {
            log.info("Friend request already exists between default users.");
        }
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
                    } catch (AlreadyExistsException e) {}
                }
            });
        }
}
