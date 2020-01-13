package net.thumbtack.school.concert.daoimpl;

import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.model.User;

import java.util.List;

import static net.thumbtack.school.concert.database.DataBase.*;

public class UserDaoImpl implements UserDao {
    @Override
    public void insertUser(User user) {
        getUserList().add(user);
    }

    @Override
    public void deleteUser(User user) {
        getUserList().remove(user);
    }

    @Override
    public List<User> getUsers() {
        return getUserList();
    }

    @Override
    public void setUsers(List<User> users) {
        if (users!=null) getUserList().addAll(users);
    }

    @Override
    public User getUserByLogin(String login,String pass) {
        for (User user: getUserList()
             ) {
           if  (user.getLogin().equals(login)&&user.getPassword().equals(pass))
            return user;
        }    
        return null;
    }

    @Override
    public User getUserByLogin(String login) {
        for (User user: getUserList()
        ) {
            if  (user.getLogin().equals(login))
                return user;
        }
        return null;
    }

    @Override
    //vstrokan: видно, не слишком эффективно вы организовали хранение юзеров, раз вам приходится Map итерировать. Подумайте, как сделать быстрее.
        //avzhukov: исправил
    public User getUserByToken(String token) {
        return getUserListSession().get(token);
    }
}
