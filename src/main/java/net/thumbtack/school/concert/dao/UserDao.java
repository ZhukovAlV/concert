package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.model.User;

import java.util.List;

public interface UserDao {
    void insertUser(User user);
    void deleteUser(User user);
    List<User> getUsers();
    void setUsers(List<User> userList);
    User getUserByLogin(String login,String pass);
    User getUserByLogin(String login);
    User getUserByToken(String token);
}
