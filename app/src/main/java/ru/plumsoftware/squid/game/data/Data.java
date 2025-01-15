package ru.plumsoftware.squid.game.data;

public abstract class Data {
    public static final String SP_NAME = "ru_plumsoftware_squid_game_clicker_base_data";
    public static final String SP_CLICK = "click";
    public static final String SP_SCORE = "score";
    public static final String SP_IMAGE_RES_ID = "imageResId";
    public static final String SP_IMAGE_BACK_RES_ID = "imageBackResId";

    public static final String SP_IS_BUY = "isBuy";

    public static final String[] SP_HEROES_IS_BUY = new String[]{
            "hero_1",
            "hero_2",
            "hero_3",
            "hero_4"
    };

    public static final String[] SP_BACK_IS_BUY = new String[]{
            "back_1",
            "back_2",
            "back_3",
    };
}