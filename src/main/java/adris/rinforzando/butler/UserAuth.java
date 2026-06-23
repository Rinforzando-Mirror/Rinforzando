package adris.rinforzando.butler;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.util.helpers.ConfigHelper;

public class UserAuth {
    private static final String BLACKLIST_PATH = "rinforzando_butler_blacklist.txt";
    private static final String WHITELIST_PATH = "rinforzando_butler_whitelist.txt";
    private UserListFile _blacklist;
    private UserListFile _whitelist;

    public UserAuth(Rinforzando mod) {
        ConfigHelper.ensureCommentedListFileExists(BLACKLIST_PATH, """
                Add butler blacklisted players here.
                Make sure useButlerBlacklist is set to true in the settings file.
                Anything after a pound sign (#) will be ignored.""");
        ConfigHelper.ensureCommentedListFileExists(WHITELIST_PATH, """
                Add butler whitelisted players here.
                Make sure useButlerWhitelist is set to true in the settings file.
                Anything after a pound sign (#) will be ignored.""");

        UserListFile.load(BLACKLIST_PATH, newList -> _blacklist = newList);
        UserListFile.load(WHITELIST_PATH, newList -> _whitelist = newList);
    }

    public boolean isUserAuthorized(String username) {

        // Blacklist gets first priority.
        if (ButlerConfig.getInstance().useButlerBlacklist && _blacklist.containsUser(username)) {
            return false;
        }
        if (ButlerConfig.getInstance().useButlerWhitelist) {
            return _whitelist.containsUser(username);
        }

        // By default accept everyone.
        return true;
    }

}
