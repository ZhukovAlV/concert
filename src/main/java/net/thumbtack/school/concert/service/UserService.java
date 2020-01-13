package net.thumbtack.school.concert.service;


import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.SessionDao;
import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.dto.request.LoginUserDtoRequest;
import net.thumbtack.school.concert.dto.request.RegisterUserDtoRequest;
import net.thumbtack.school.concert.dto.response.GetRegistersUserDtoResponse;
import net.thumbtack.school.concert.dto.response.RegisterUserDtoResponse;
import net.thumbtack.school.concert.exception.ErrorCode;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.ServerException;

import java.util.List;

public class UserService {

    public RegisterUserDtoResponse registerUser(String jsonString, String token,Gson gson,UserDao userDao,SessionDao sessionDao) throws ServerException{
        RegisterUserDtoRequest registerUserDtoRequest = gson.fromJson(jsonString,RegisterUserDtoRequest.class);
        if (registerUserDtoRequest.getFirstName()==null) throw new ServerException(ErrorCode.USER_FIRST_NAME_IS_EMPTY);
        if (registerUserDtoRequest.getLastName()==null) throw new ServerException(ErrorCode.USER_LAST_NAME_IS_EMPTY);
        if (registerUserDtoRequest.getPassword()==null) throw new ServerException(ErrorCode.USER_PASSWORD_EMPTY);
        if (registerUserDtoRequest.getLogin()==null) throw new ServerException(ErrorCode.USER_LOGIN_EMPTY);
        if (registerUserDtoRequest.getLogin().length()<3) throw new ServerException(ErrorCode.USER_LOGIN_SHORT);
        User user = new User(registerUserDtoRequest.getFirstName(), registerUserDtoRequest.getLastName(),
                registerUserDtoRequest.getLogin(), registerUserDtoRequest.getPassword());
        //vstrokan: вы сможете зарегистрировать юзера с тем же логином, но другим паролем. не думаю, что такое желательно
            // avzhukov: внес поиск по логину (без пароля через перегруженные методы) только на уровне сервиса. На уровне сервера только поиск с паролем используется.
        if (userDao.getUserByLogin(user.getLogin())!=null) throw new ServerException(ErrorCode.USER_REGISTER_DUPLICATE);
        userDao.insertUser(user);
        createSession(user, token,sessionDao);
        return new RegisterUserDtoResponse(user.getFirstName(),user.getLastName());
    }

    private void createSession(User user, String token,SessionDao sessionDao) {
        sessionDao.insertSession(token,user);
    }

    public GetRegistersUserDtoResponse getUsers(UserDao userDao) {
        return new GetRegistersUserDtoResponse(userDao.getUsers());
    }

    public User getUser(String token, UserDao userDao) {
        return userDao.getUserByToken(token);
    }

    public User getUserFromJson(String json, Gson gson, UserDao userDao, SessionDao sessionDao) {
        RegisterUserDtoRequest registerUserDtoRequest = gson.fromJson(json,RegisterUserDtoRequest.class);
        String token = getToken(registerUserDtoRequest.getLogin(),registerUserDtoRequest.getPassword(),userDao,sessionDao);
        return getUser(token,userDao);
    }

    public String getToken(String login, String pass, UserDao userDao, SessionDao sessionDao) {
        User user = userDao.getUserByLogin(login,pass);
        return sessionDao.getTokenByUser(user);
    }

    public void setUsers(List<User> userList,UserDao userDao) {
        userDao.setUsers(userList);
    }

    public String loginUser(String jsonString, String token,Gson gson,UserDao userDao,SessionDao sessionDao) throws ServerException {
        LoginUserDtoRequest loginUserDtoRequest = gson.fromJson(jsonString, LoginUserDtoRequest.class);
        User user = userDao.getUserByLogin(loginUserDtoRequest.getLogin(),loginUserDtoRequest.getPassword());
        if (user==null)
            throw new ServerException(ErrorCode.USER_NOT_EXIST);
        if (!loginUserDtoRequest.getPassword().equals(user.getPassword()))
            throw new ServerException(ErrorCode.USER_LOGIN_FAILED);
        createSession(user, token,sessionDao);
        return token;
    }

    public void logoutUser(String token,SessionDao sessionDao){
        sessionDao.deleteSession(token);
    }

    public void exitUser(String token,UserDao userDao,SessionDao sessionDao,SongDao songDao){ ;
        User userOld = userDao.getUserByToken(token);
        User userNew = userDao.getUserByLogin("community","community");
        List<Song> songs = songDao.getSongsByProposer(userOld);
        //vstrokan: у вас есть SongService.cancelSongs(), который, по идее, должен делать то же самое согласно ТЗ.
        // То же для SongService.changeComment() Почему не используется? Если я поменяю ТЗ, вам придется не в одном месте исправлять, а сразу в нескольких.
        // Сами же об этом забудете, система очень хрупкая получается, неустойчивая к изменениям
            //avzhukov: связал эти 3 метода, чтобы другу через друга выражались. Если есть рейтинг другого пользователя песня не удалится, но если есть только комментарии другого пользователя, а рейтинга нет. то удалится.
        for (Song song: songs
        ) {
            songDao.cancelSong(song, userOld, userNew);
        }
        logoutUser(token,sessionDao);
        userDao.deleteUser(userOld);
    }

}
