package com.facebook.utils;

import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Log4j2
public class Gen {

    public final ApplicationContext context;

    private Gen(ApplicationContext context) {
        this.context = context;
    }

    public static Gen of(ApplicationContext context) {
        return new Gen(context);
    }

    public final List<AppUser> appUsers = List.of(
            new AppUser("George",
                    "Washington",
                    "Greak",
                    "g_rafk@ukr.net",
                    "Westmorland Virginia",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    42),
            new AppUser("Bret",
                    "Johnson",
                    "BretNickname",
                    "Sincere@april.biz",
                    "Random Address 1",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    19),
            new AppUser("Ervin",
                    "Smith",
                    "ErvinNickname",
                    "Shanna@melissa.tv",
                    "Random Address 2",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    31),
            new AppUser("Clementine",
                    "Jones",
                    "ClemNickname",
                    "clementine_b@ukr.net",
                    "Random Address 3",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    27),
            new AppUser("Patricia",
                    "Williams",
                    "PattyNickname",
                    "Nathan@yesenia.net",
                    "Random Address 4",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    58),
            new AppUser("Karianne",
                    "Brown",
                    "KariNickname",
                    "Julianne.OConner@kory.org",
                    "Random Address 5",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    29),
            new AppUser("Kamren",
                    "Taylor",
                    "KamNickname",
                    "Lucio_Hettinger@annie.ca",
                    "Random Address 6",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    19),
            new AppUser("Leopoldo_Corkery",
                    "Miller",
                    "LeoNickname",
                    "Karley_Dach@jasper.info",
                    "Random Address 7",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    56),
            new AppUser("Elwyn.Skiles",
                    "Davis",
                    "ElwynNickname",
                    "Telly.Hoeger@billy.biz",
                    "Random Address 8",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    45),
            new AppUser("Maxime_Nienow",
                    "Garcia",
                    "MaxNickname",
                    "Sherwood@rosamond.me",
                    "Random Address 9",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    98),
            new AppUser("Glenna Reichert",
                    "Wilson",
                    "GlenNickname",
                    "Chaim_McDermott@dana.io",
                    "Random Address 10",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    39),
            new AppUser("Clementina DuBuque",
                    "Moore",
                    "ClemenNickname",
                    "Rey.Padberg@karina.biz",
                    "Random Address 11",
                    "https://via.placeholder.com/150/66b7d2",
                    "https://source.unsplash.com/random?wallpapers",
                    51)
    );

    public final String[] password = new String[]{"568D!6s", "S06i7*378", "Ff063?82@",
            "ldsf~gb1K", "oEk%jdf57", "0&98ZnSdfg", "*g!H235h", "WgfIl63?", "2hdsU56F!",
            "gdhC%12U", "0Qwezx&63", "0!H7jfd3?", "09jjI3*U"};

    public void genAppUser() {
        AppUserService service = context.getBean(AppUserService.class);
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);

        for (AppUser appUser : appUsers) {
            appUser.setRoles(new String[]{"USER"});
            appUser.setPassword(encoder.encode(password[MathUtils.random(0, 12)]));
            service.save(appUser);
        }
    }
}


