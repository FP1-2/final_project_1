package com.facebook.utils;

import com.facebook.dto.appuser.GenAppUser;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

/**
 *  Клас Gen призначений для генерації даних та заповнення
 *  БД цими даними. За замовчуванням запускається з профілем Dev.
 *  Генерує 14 користувачів AppUser.
 *  Містить тестового користувача з відомими авторизаційними даними з
 *  username "test",
 *  password "Password1!".
 */

@Log4j2
@SuppressWarnings("all")
@RequiredArgsConstructor
public class Gen {

    private static final String AVATAR = "https://via.placeholder.com/150/66b7d2";

    private static final String HEADER_PHOTO = "https://source.unsplash.com/random?wallpapers";

    private static final String DEFAULT_PASSWORD = "Password1!";

    private static final String DEFAULT_USERNAME = "test";

    public final ApplicationContext context;

    private final AppUserFacade facade;

    public static Gen of(ApplicationContext context, AppUserFacade facade) {
        return new Gen(context, facade);
    }

    public final List<GenAppUser> appUsers = List.of(
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

    public final String[] password = new String[]{"568D!6s", "S06i7*378", "Ff063?82@",
            "ldsf~gb1K", "oEk%jdf57", "0&98ZnSdfg", "*g!H235h", "WgfIl63?", "2hdsU56F!",
            "gdhC%12U", "0Qwezx&63", "0!H7jfd3?", "09jjI3*U"};

    public void genAppUser() {
        AppUserService service = context.getBean(AppUserService.class);
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);

        for (GenAppUser dto : appUsers) {
            createAppUser(dto, service, encoder);
        }
        //Окремо додаємо дефолтного користувача.
        createAppUser(new GenAppUser("Clementina DuBuque",
                "test",
                DEFAULT_USERNAME,
                "test@test.biz",
                "Address 11",
                AVATAR,
                HEADER_PHOTO,
                51), service, encoder);
    }

    private void createAppUser(GenAppUser dto,
                               AppUserService service,
                               PasswordEncoder encoder
                               ){
        AppUser appUser = facade.convertToAppUser(dto);
        appUser.setRoles(new String[]{"USER"});

        String encodedPassword = Optional.of(dto)
                .filter(user -> DEFAULT_USERNAME.equals(user.getUsername()))
                .map(user -> encoder.encode(DEFAULT_PASSWORD))
                .orElse(encoder.encode(password[MathUtils.random(0, 12)]));

        appUser.setPassword(encodedPassword);
        service.save(appUser);
    }
}


