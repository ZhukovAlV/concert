package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.model.User;

import java.util.Map;

public interface SessionDao {
    String getTokenByUser(User user);
    void insertSession(String token,User user);
    void deleteSession(String token);
    Map<String,User> getUserListSession();
}
