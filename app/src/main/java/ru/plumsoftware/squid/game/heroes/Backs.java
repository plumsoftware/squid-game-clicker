package ru.plumsoftware.squid.game.heroes;

import java.util.ArrayList;
import java.util.List;

import ru.plumsoftware.squid.game.R;

public abstract class Backs {
    public static final String[] BACKS_NAMES = new String[]{
            "Фон Не шевелись",
            "Печеньки",
            "Маршировка"
    };
    public static final int[] BACKS_RES_ID = new int[]{
            R.drawable.back_1,
            R.drawable.back_2,
            R.drawable.back_3
    };
    public static final int[] BACKS_PRICES = new int[]{
            0,
            15000,
            50000
    };

    public static List<Back> buildHeroes() {
        List<Back> list = new ArrayList<>();
        for (int i = 0; i < BACKS_NAMES.length; i++) {
            list.add(new Back(BACKS_RES_ID[i], BACKS_NAMES[i], BACKS_PRICES[i]));
        }
        return list;
    }
}
