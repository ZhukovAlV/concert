package net.thumbtack.school.concert.daoimpl;

import net.thumbtack.school.concert.dao.DataDao;

import static net.thumbtack.school.concert.database.DataBase.*;

public class DataDaoImpl implements DataDao {

    @Override
    public void clear() {
        getUserList().clear();
        getSongList().clear();
        getUserListSession().clear();
    }
}
