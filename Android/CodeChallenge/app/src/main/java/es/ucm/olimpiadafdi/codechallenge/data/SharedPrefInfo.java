package es.ucm.olimpiadafdi.codechallenge.data;

public class SharedPrefInfo {
    // Shared pref mode
    public static final int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "CodeChallengePref";

    // All Shared Preferences Keys
    public static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NICK = "nick";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // Total time
    public static final String KEY_TOTALTIME = "gameTime";

    // Number of successful lines
    public static final String KEY_CORRECT = "gameCorrect";

    public SharedPrefInfo(){}

}
