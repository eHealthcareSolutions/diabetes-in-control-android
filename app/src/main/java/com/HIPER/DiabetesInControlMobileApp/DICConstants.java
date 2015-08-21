package com.HIPER.DiabetesInControlMobileApp;

/**
 * Created by connorwatts on 7/29/15.
 */
public class DICConstants {

    static String fontName = "fonts/OpenSans-Regular.ttf";
    static String favoritesFilepath = "DICFavorites.archive";
    static int articles_per_page = 5;

    static class ScrollView {
        static String selectedColor = "#FFFFA8";
        static String unselectedColor = "#FFFFFF";
    }

    static class Tile {
        static int horizMargin = 20;
        static int vertMargin = 50;

        static double heightPercentagePortrait = 0.3;
        static double heightPercentageLandscape = 0.5;

        static String readMoreSelectedColor = "#008FD6";
        static String readMoreUnselectedColor = "#ACACAC";
    }

    static class URLConvenience {
        static String baseUrl = "http://ec2-184-73-33-13.compute-1.amazonaws.com/html/diabetesincontrol-dev.com/articles";
        static String articlesUrl = "http://ec2-184-73-33-13.compute-1.amazonaws.com/html/diabetesincontrol-dev.com/articles";
        static String feed = "/feed/";
        static String feedSingleArticle = "feed/?withoutcomments=1";
        static String paged = "?paged=";

        static String favoritesURL = "favorites.archive";
        static String cacheURL = "cache.archive";

        // these must be parallel arrays and not dictionaries because dictionaries aren't ordered
        static String[] categories = {"Favorites", "Home", "Case Study", "Clinical Gems", "Did You Know", "Disasters Averted", "Exclusive Interviews",
                "Facts", "Feature", "Homerun Slides", "How It Works", "Items for the Week", "Newsflash", "Press Releases", "Product of the Week", "Studies", "Videos"};
        static String[] categoryUrls = {"", "", "/case-study", "/clinical-gems","/did-you-know", "/practicum", "/exclusive", "/facts", "/feature",
                "/homerun-slides", "/how-glp-1-works", "/diabetes-news", "/newsflash", "/press-releases", "/product-of-the-week", "/this-weeks-study", "/videos"};
    }

}
