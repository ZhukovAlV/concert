package net.thumbtack.school.concert;

import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.server.ServerException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static net.thumbtack.school.concert.server.Server.isIsStarted;

public class TestUser {

    private static File file = new File("savedDataFile.txt");
    final static String REG_USER_JSON_ONE = "{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"}";
    final static String REG_USER_JSON_TWO = "{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}";
    final static String REG_SONG_JSON_ONE = "{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"comments\":[]}";
    static final String REG_USER_COMMUNITY = "{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"}";

    // Регистрируем пользователя и проверяем jSON ответный
    @Test
    public void registerUser() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Assert.assertEquals(Server.registerUser(REG_USER_JSON_ONE), "{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\"}");
    }

    // Регистрируем пользователя несколько раз
    @Test (expected = ServerException.class)
    public void registerUserRepeat() throws IOException,ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_ONE);
    }

    // Регистрируем пользователя с незаполненным именем
    @Test (expected = ServerException.class)
    public void registerUserFirstNameNull() throws IOException,ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser("{\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"}");
    }

    // Регистрируем пользователя с незаполненной фамилией
    @Test (expected = ServerException.class)
    public void registerUserLastNameNull() throws IOException,ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser("{\"firstName\":\"Pirs\",\"login\":\"bond\",\"password\":\"007\"}");
    }

    // Проверяем ошибку, что пользователь не зарегестрирован
    @Test (expected = ServerException.class)
    public void LoginUser() throws IOException,ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.loginUser("{\"login\":\"bond\",\"password\":\"007\"}");
    }

    // Проверяем, что после выхода, посльзователь повторно может зайти и меняется ключ сессии
    @Test
    public void registerAndLogoutAndLoginUser() throws IOException,ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        String loginUserJson = "{\"login\":\"bond\",\"password\":\"007\"}";
        Server.registerUser(REG_USER_JSON_ONE);
        String token1 = Server.getTokenByUser("bond","007");
        Server.logoutUser(token1);
        String token2 = Server.loginUser(loginUserJson);
        Assert.assertNotEquals(token1,token2);
    }

    // Загружаем пользователя из файла
    @Test
    public void recoveryListRegisterUser() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.stopServer(file);
        Server.startServer(file);
        Assert.assertEquals(Server.getUsers(), "{\"userList\":[{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"},{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"}]}");
    }

    // Регистрируем несколько пользователей и проверяем jSON ответный
    @Test
    public void registerUsers() throws IOException,ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        Assert.assertEquals(Server.getUsers(), "{\"userList\":[{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"},{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}]}");
    }

    // Зарегистрированный на сервере радиослушатель может покинуть сервер, в этом случае вся информация о нем удаляется, а список сделанных им предложений обрабатывается как указано ниже.
    @Test
    public void UserExit() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        String nameSong = "Greensleeves";
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addRating(nameSong, 2, token2);
        Server.exitUser(token1);
        Assert.assertNull(Server.getTokenByUser("bond","007"));
        Assert.assertEquals(Server.getSongsByProposer(REG_USER_JSON_ONE),"{\"songsList\":[]}");
        Assert.assertEquals(Server.getSongsByProposer(REG_USER_COMMUNITY),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"},\"ratings\":[{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":2}],\"comments\":[]}]}");
    }
}
