package hangman;

import java.util.ArrayList;

public class Database extends DatabaseManager{

    public Database() {
    }

    public boolean checkUserAvailability(String username) {
        ArrayList<UserInfo> userInfos = selectUserInfos();
        for (UserInfo userInfo: userInfos) {
            if (userInfo.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
