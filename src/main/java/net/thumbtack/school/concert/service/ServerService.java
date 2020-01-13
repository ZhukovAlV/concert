package net.thumbtack.school.concert.service;


import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.DataDao;
import net.thumbtack.school.concert.dao.SessionDao;
import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.daoimpl.DataDaoImpl;
import net.thumbtack.school.concert.daoimpl.SessionDaoImpl;
import net.thumbtack.school.concert.daoimpl.SongDaoImpl;
import net.thumbtack.school.concert.daoimpl.UserDaoImpl;
import net.thumbtack.school.concert.exception.ErrorCode;
import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.server.ServerException;

import java.io.File;
import java.io.IOException;

public class ServerService {

    private static SessionDao sessionDao = new SessionDaoImpl();
    public static SessionDao getSessionDao() {
        return sessionDao;
    }
    public static void setSessionDao(SessionDao sessionDao) {
        ServerService.sessionDao = sessionDao;
    }

    private static SongDao songDao = new SongDaoImpl();
    public static SongDao getSongDao() {
        return songDao;
    }
    public static void setSongDao(SongDao songDao) {
        ServerService.songDao = songDao;
    }

    private static UserDao userDao = new UserDaoImpl();
    public static UserDao getUserDao() {
        return userDao;
    }
    public static void setUserDao(UserDao userDao) {
        ServerService.userDao = userDao;
    }

    private static DataDao datarDao = new DataDaoImpl();
    public static DataDao getDatarDao() {
        return datarDao;
    }
    public static void setDatarDao(DataDao datarDao) {
        ServerService.datarDao = datarDao;
    }

    // Проверка сервера на Включен
    public static void checkServerIsStart(boolean isStarted) throws ServerException{
        if (!isStarted) throw new ServerException(ErrorCode.SERVER_NOT_START);
    }

    // пПроверка сервера на Выключен
    public static void checkServerNotStart(boolean isStarted) throws ServerException{
        if (isStarted) throw new ServerException(ErrorCode.SERVER_IS_START);
    }

    // Производит всю необходимую инициализацию и запускает сервер.
    public static void startServer(File savedDataFileName, boolean isStarted, Gson gson, UserService userService, SongService songService, UserDao userDao, SongDao songDao, FileService fileService) throws ServerException {
        checkServerIsStart(isStarted);
        if (savedDataFileName!=null) fileService.recoveryDataFromFile(savedDataFileName,gson,userService,songService,userDao,songDao);
    }

    // Останавливает сервер и записывает все его содержимое в файл
    public static void stopServer(File savedDataFile,boolean isStarted, Gson gson, UserService userService, SongService songService, UserDao userDao, SongDao songDao, DataDao dataDao, FileService fileService) throws IOException,ServerException{
        checkServerNotStart(isStarted);
        if (savedDataFile==null) throw new ServerException(ErrorCode.SERVER_FILE_NOT_EXIST);
        fileService.savedDataToFile(savedDataFile, gson.toJson(userService.getUsers(userDao)), gson.toJson(songService.getSongs(songDao)),dataDao);

    }


}
