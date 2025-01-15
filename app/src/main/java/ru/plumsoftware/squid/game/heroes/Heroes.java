package ru.plumsoftware.squid.game.heroes;

import java.util.ArrayList;
import java.util.List;

import ru.plumsoftware.squid.game.R;

public abstract class Heroes {
    public static final String[] HEROES_NAMES = new String[]{
            "Бомжик",
            "Черепашки ниндзя",
            "Син ку их",
            "Главный игрок"
    };
    public static final int[] HEROES_RES_ID = new int[]{
            R.drawable.hero_1,
            R.drawable.hero_2,
            R.drawable.hero_3,
            R.drawable.hero_4
    };
    public static final int[] HEROES_CLICKS = new int[]{
            1,
            10,
            25,
            75
    };
    public static final int[] HEROES_PRICES = new int[]{
            0,
            100,
            1000,
            5000
    };

    public static List<Hero> buildHeroes() {
        List<Hero> list = new ArrayList<>();
        for (int i = 0; i < HEROES_NAMES.length; i++) {
            list.add(new Hero(HEROES_RES_ID[i], HEROES_NAMES[i], HEROES_PRICES[i], HEROES_CLICKS[i]));
        }
        return list;
    }
}
