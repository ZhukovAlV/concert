package net.thumbtack.school.concert.daoimpl;

import net.thumbtack.school.concert.dao.SessionDao;
import net.thumbtack.school.concert.database.DataBase;
import net.thumbtack.school.concert.model.User;

import java.util.Map;
import java.util.Set;

public class SessionDaoImpl implements SessionDao {

    public Map<String,User> getUserListSession() {
        return DataBase.getUserListSession();
    }

    public String getTokenByUser(User user) {
        Set<Map.Entry<String,User>> entrySet=getUserListSession().entrySet();
        for (Map.Entry<String,User> entry : entrySet) {
            if (entry.getValue().equals(user)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void insertSession(String token, User user) {
        getUserListSession().put(token, user);
    }

    @Override
    public void deleteSession(String token) {
        getUserListSession().remove(token);
    }
}
