package com.facebook.utils;

import com.facebook.dto.appuser.GenAppUser;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.model.posts.*;
import com.facebook.repository.posts.CommentRepository;
import com.facebook.repository.posts.LikeRepository;
import com.facebook.repository.posts.RepostRepository;
import com.facebook.service.AppUserService;
import com.facebook.service.PostService;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.Optional;
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

    private static final String DEFAULT_PASSWORD = "Password1!";

    private static final String DEFAULT_USERNAME = "test";

    public final ApplicationContext context;

    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;

    private final RepostRepository repostRepository;

    private final AppUserService appUserService;

    private final PasswordEncoder passwordEncoder;

    private final AppUserFacade appUserFacade;

    private final PostService postService;

    private List<AppUser> appUsers1;

    private List<Post> posts;

    private List<Comment> comments;

    private List<Like> likes;

    private List<Repost> reposts;

    private Gen(ApplicationContext context) {
        this.context = context;

        this.appUserService = context.getBean(AppUserService.class);
        this.passwordEncoder = context.getBean(PasswordEncoder.class);
        this.appUserFacade = context.getBean(AppUserFacade.class);
        this.postService = context.getBean(PostService.class);

        this.likeRepository = context.getBean(LikeRepository.class);
        this.commentRepository = context.getBean(CommentRepository.class);
        this.repostRepository = context.getBean(RepostRepository.class);

        this.appUsers1 = genAppUser();
        this.posts = genPosts();

        this.comments = genComments();
        this.likes = genLikes();
        this.reposts = genReposts();
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
        return appUserService.findAll();
    }

    private void createAppUser(GenAppUser dto) {
        AppUser appUser = appUserFacade.convertToAppUser(dto);
        appUser.setRoles(new String[]{"USER"});

        String encodedPassword = Optional.of(dto)
                .filter(user -> DEFAULT_USERNAME.equals(user.getUsername()))
                .map(user -> passwordEncoder.encode(DEFAULT_PASSWORD))
                .orElse(passwordEncoder.encode(password[MathUtils.random(0, 12)]));

        appUser.setPassword(encodedPassword);
        appUserService.save(appUser);
    }

    private static PostStatus getRandomPostStatus() {
        return PostStatus
                .values()[MathUtils.random(0, PostStatus.values().length - 1)];
    }


    private List<Post> genPosts() {
        List<AppUser> appUsers1 = appUserService.findAll();

        Faker faker = new Faker();

        appUsers1.forEach(user -> {
            // Для кожного користувача генеруємо від 1 до 10 постів
            IntStream
                    .rangeClosed(1, MathUtils.random(1, 10))
                    .forEach(ignored -> {
                        Post post = new Post();
                        post.setStatus(getRandomPostStatus());
                        post.setImageUrl(HEADER_PHOTO);
                        post.setUser(user);
                        post.setTitle(String.join(" ",
                                faker
                                        .lorem()
                                        .words(MathUtils.random(1, 5)))
                        );
                        post.setBody(faker.lorem().paragraph()); // Lorem Ipsum
                        postService.save(post);
                    });
        });

        return postService.findAll();
    }

    private List<Like> genLikes() {
        posts.forEach(post -> {
            appUsers1.forEach(user -> {
                // 50% шанс поставити лайк
                if (MathUtils.random(0, 1) == 0) {
                    postService.likePost(user, post);
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
                        postService.addComment(
                                appUsers1.get(MathUtils.random(0, appUsers1.size() - 1)),
                                post,
                                faker.lorem().sentence()
                        );
                    });
        });

        return commentRepository.findAll();
    }

    private List<Repost> genReposts() {
        posts.forEach(post -> {
            appUsers1.forEach(user -> {
                // 33% шанс зробити репост
                if (MathUtils.random(0, 2) == 0) {
                    postService.repost(user, post);
                }
            });
        });

        return repostRepository.findAll();
    }

}


