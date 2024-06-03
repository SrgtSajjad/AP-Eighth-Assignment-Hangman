package hangman;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class DatabaseManager {

    public void insertUserInfo(UserInfo userInfo) {
        Connection c;
        PreparedStatement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (insertUserInfo)");

            stmt = c.prepareStatement("INSERT INTO userinfo(Name, Username, Password) VALUES (?, ?, ?);");
            stmt.setString(1, userInfo.getName());
            stmt.setString(2, userInfo.getUsername());
            stmt.setString(3, userInfo.getPassword());
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            System.out.println("Operation done successfully (insertUserInfo)");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public ArrayList<UserInfo> selectUserInfos() {
        Connection c;
        Statement stmt;
        ArrayList<UserInfo> userInfos = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (selectUserInfos)");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM UserInfo;");
            userInfos = new ArrayList<>();
            while (rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setName(rs.getString("name"));
                userInfo.setUsername(rs.getString("username"));
                userInfo.setPassword(rs.getString("password"));
                userInfos.add(userInfo);
            }
            rs.close();
            stmt.close();
            c.close();
            System.out.println("Operation done successfully (selectUserInfos)");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return userInfos;
    }

    public UserInfo selectUserInfo(String username) {
        Connection c;
        PreparedStatement stmt;
        UserInfo userInfo = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (selectUserInfo (" + username + "))");

            stmt = c.prepareStatement("SELECT * FROM UserInfo WHERE Username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            userInfo = new UserInfo();
            while (rs.next()) {
                userInfo.setName(rs.getString("Name"));
                userInfo.setUsername(rs.getString("Username"));
                userInfo.setPassword(rs.getString("Password"));
            }
            rs.close();
            stmt.close();
            c.close();
            System.out.println("Operation done successfully (selectUserInfo (" + username + "))");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return userInfo;
    }

    public void deleteUserInfo(String username) {
        Connection c;
        PreparedStatement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (deleteUserInfo)");

            stmt = c.prepareStatement("DELETE FROM userinfo WHERE Username = ?;");
            stmt.setString(1, username);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            System.out.println("Operation done successfully (deleteUserInfo)");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void insertGameInfo(GameInfo gameInfo) {
        Connection c;
        PreparedStatement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (insertGameInfo)");

            stmt = c.prepareStatement("INSERT INTO gameinfo(GameID, Username, Word, WrongGuesses, Time,Win) VALUES (?, ?, ?, ?, ?, ?);");
            stmt.setObject(1, gameInfo.getGameID());
            stmt.setString(2, gameInfo.getUsername());
            stmt.setString(3, gameInfo.getWord());
            stmt.setInt(4, gameInfo.getWrongGuesses());
            stmt.setInt(5, gameInfo.getTime());
            stmt.setBoolean(6, gameInfo.isWin());
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            System.out.println("Operation done successfully (insertGameInfo)");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public ArrayList<GameInfo> selectGameInfos() {
        Connection c;
        Statement stmt;
        ArrayList<GameInfo> gameInfos = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (selectGameInfos)");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM GameInfo;");
            gameInfos = new ArrayList<>();
            while (rs.next()) {
                GameInfo gameInfo = new GameInfo();
                gameInfo.setGameID(UUID.fromString(rs.getString("GameID")));
                gameInfo.setUsername(rs.getString("Username"));
                gameInfo.setUserInfo(selectUserInfo(gameInfo.getUsername()));
                gameInfo.setWord(rs.getString("Word"));
                gameInfo.setWrongGuesses(rs.getInt("WrongGuesses"));
                gameInfo.setTime(rs.getInt("Time"));
                gameInfo.setWin(rs.getBoolean("Win"));
                gameInfos.add(gameInfo);
            }
            rs.close();
            stmt.close();
            c.close();
            System.out.println("Operation done successfully (selectGameInfos)");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return gameInfos;
    }

    public GameInfo selectGameInfo(UUID gameID) {
        Connection c;
        PreparedStatement stmt;
        GameInfo gameInfo = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (selectGameInfo)");

            stmt = c.prepareStatement("SELECT * FROM GameInfo WHERE GameID = ?");
            stmt.setObject(1, gameID);
            ResultSet rs = stmt.executeQuery();
            gameInfo = new GameInfo();
            while (rs.next()) {
                gameInfo.setGameID(UUID.fromString(rs.getString("GameID")));
                gameInfo.setUsername(rs.getString("Username"));
                gameInfo.setUserInfo(selectUserInfo(gameInfo.getUsername()));
                gameInfo.setWord(rs.getString("Word"));
                gameInfo.setWrongGuesses(rs.getInt("WrongGuesses"));
                gameInfo.setTime(rs.getInt("Time"));
                gameInfo.setWin(rs.getBoolean("Win"));
            }
            rs.close();
            stmt.close();
            c.close();
            System.out.println("Operation done successfully (selectGameInfo)");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return gameInfo;
    }

    public void updateGameInfo(GameInfo gameInfo) {
        Connection c;
        PreparedStatement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (updateGameInfo)");

            stmt = c.prepareStatement("UPDATE gameinfo SET Username = ?, Word = ?, WrongGuesses = ?, Time = ?, Win = ? WHERE GameID = ?;");
            stmt.setString(1, gameInfo.getUsername());
            stmt.setString(2, gameInfo.getWord());
            stmt.setInt(3, gameInfo.getWrongGuesses());
            stmt.setInt(4, gameInfo.getTime());
            stmt.setBoolean(5, gameInfo.isWin());
            stmt.setObject(6, gameInfo.getGameID());
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            System.out.println("Operation done successfully (updateGameInfo)");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void deleteGameInfo(UUID gameID) {
        Connection c;
        PreparedStatement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (deleteGameInfo)");

            stmt = c.prepareStatement("DELETE FROM gameinfo WHERE GameID = ?;");
            stmt.setObject(1, gameID);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            c.close();
            System.out.println("Operation done successfully (deleteGameInfo)");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
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

    public ArrayList<LeaderBoard> selectLeaderBoard() {
        Connection c;
        Statement stmt;
        ArrayList<LeaderBoard> leaderBoards = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (selectLeaderboard)");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Username, COUNT(Win) as Wins FROM public.gameinfo WHERE Win = true Group BY Username ORDER BY Wins desc;");
            leaderBoards = new ArrayList<>();
            while (rs.next()) {
                LeaderBoard leaderBoard = new LeaderBoard();
                leaderBoard.setUsername(rs.getString("Username"));
                leaderBoard.setWins(rs.getInt("Wins"));
                leaderBoards.add(leaderBoard);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully (selectLeaderboard)");
        return leaderBoards;
    }

    public ArrayList<PastGame> selectPastGames(UserInfo userInfo) {
        Connection c;
        PreparedStatement stmt;
        ArrayList<PastGame> pastGames = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:1234/Hangman", "postgres", "sajjad2005");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully (selectPastGames)");

            stmt = c.prepareStatement("SELECT word, time, win FROM public.gameinfo WHERE username = ?;");
            stmt.setString(1, userInfo.getUsername());
            ResultSet rs = stmt.executeQuery();
            pastGames = new ArrayList<>();
            while (rs.next()) {
                PastGame pastGame = new PastGame();
                pastGame.setWord(rs.getString("word"));
                pastGame.setTime(rs.getInt("time"));
                pastGame.setWin(rs.getBoolean("win"));
                pastGames.add(pastGame);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully (selectLeaderboard)");
        return pastGames;
    }

    public static class LeaderBoard {
        String username;
        int wins;

        public LeaderBoard() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getWins() {
            return wins;
        }

        public void setWins(int wins) {
            this.wins = wins;
        }
    }

    public static class PastGame {
        String word;
        int time;
        boolean win;

        public PastGame() {
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public boolean isWin() {
            return win;
        }

        public void setWin(boolean win) {
            this.win = win;
        }
    }

}