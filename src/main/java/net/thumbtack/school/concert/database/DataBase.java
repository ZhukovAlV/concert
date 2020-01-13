package net.thumbtack.school.concert.database;

import net.thumbtack.school.concert.model.*;

import java.util.*;

public class DataBase {
    //vstrokan: инкапсуляцию никто не отменял. К полям надо обращаться через методы, а не напрямую.
        //avzhukov: исправил
    private static List<User> userList = new ArrayList<>();
    public static List<User> getUserList() {
        return userList;
    }
    public static void setUserList(List<User> userList) {
        DataBase.userList = userList;
    }

    private static Map<String,User> userListSession = new HashMap<>();
    public static Map<String, User> getUserListSession() {
        return userListSession;
    }
    public static void setUserListSession(Map<String,User> userListSession) {
        DataBase.userListSession = userListSession;
    }

    private static Map<Song,String> songList = new HashMap<>();
    public static Map<Song, String> getSongList() {
        return songList;
    }
    public static void setSongList(Map<Song, String> songList) {
        DataBase.songList = songList;
    }
}

