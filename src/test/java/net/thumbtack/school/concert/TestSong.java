package net.thumbtack.school.concert;

import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.server.ServerException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static net.thumbtack.school.concert.server.Server.isIsStarted;

public class TestSong {

    private static File file = new File("savedDataFile.txt");
    final static String REG_USER_JSON_ONE = "{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"}";
    final static String REG_USER_JSON_TWO = "{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}";
    final static String REG_SONG_JSON_ONE = "{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"comments\":[]}";
    final static String REG_SONG_JSON_TWO = "{\"title\":\"Style Life\",\"composers\":[],\"authors\":[],\"singer\":\"Чиж\",\"duration\":1200.0,\"proposer\":{},\"ratings\":[],\"comments\":[]}";
    final static String REG_SONG_JSON_THREE = "{\"title\":\"Одинокая птица\",\"composers\":[\"Чиж\"],\"authors\":[\"Чиж\", \"Иванов\"],\"singer\":\"Бутусов\",\"duration\":1200.0,\"proposer\":{},\"ratings\":[],\"comments\":[]}";
    final static String REG_SONG_JSON_FOUR = "{\"title\":\"Любовь не прошла\",\"composers\":[\"Иванов\", \"Гребенщиков\"],\"authors\":[\"Иванов\", \"Гребенщиков\"],\"singer\":\"ДДТ\",\"duration\":120.0,\"proposer\":{},\"ratings\":[],\"comments\":[]}";
    final static String REG_SONG_JSON_FIVE = "{\"title\":\"Письмо\",\"composers\":[\"Иванов\"],\"authors\":[],\"singer\":\"Сплин\",\"duration\":1200.0,\"proposer\":{},\"ratings\":[],\"comments\":[]}";

    // Проверяем добавление песни
    @Test
    public void addSong() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        String token = Server.getTokenByUser("bond","007");
        String songs = Server.addSongs(REG_SONG_JSON_ONE,token);
        Assert.assertEquals(Server.getUsers(), "{\"userList\":[{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"},{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"}]}");
        Assert.assertEquals(Server.getSongs(), "{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[]}]}");
        Assert.assertEquals(songs,"{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":500,\"comments\":[]}");
    }

    // Проверяем отмену песни
    @Test
    public void cancelSongs() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        String token = Server.getTokenByUser("bond","007");
        Server.addSongs(REG_SONG_JSON_ONE,token);
        Server.cancelSong(REG_SONG_JSON_ONE,token);
        Assert.assertEquals(Server.getSongs(), "{\"songsList\":[]}");

    }

    // Проверяем загрузку файла с несколькими пользователями и песней
    @Test
    public void recoveryListRegisterUsersAndSong() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token = Server.getTokenByUser("bond","007");
        Server.addSongs(REG_SONG_JSON_ONE,token);
        Server.addSongs(REG_SONG_JSON_TWO,token);
        Server.stopServer(file);
        Server.startServer(file);
        Assert.assertEquals(Server.getUsers(), "{\"userList\":[{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"},{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}]}");
        Assert.assertEquals(Server.getSongs(), "{\"songsList\":[{\"title\":\"Style Life\",\"composers\":[],\"authors\":[],\"singer\":\"Чиж\",\"duration\":1200.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[]},{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[]}]}");
    }

    // Не заполнено название песни
    @Test (expected = ServerException.class)
    public void songTitleEmpty() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        String token = Server.getTokenByUser("bond","007");
        Server.addSongs("{\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"ratings\":[{\"user\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"rating\":5}],\"comments\":[]}",token);
    }

    // Не заполнен композитор
    @Test (expected = ServerException.class)
    public void songComposerEmpty() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        String token = Server.getTokenByUser("bond","007");
        Server.addSongs("{\"title\":\"Greensleeves\",\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"ratings\":[{\"user\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"rating\":5}],\"comments\":[]}",token);
    }

    // Не заполнен автор
    @Test (expected = ServerException.class)
    public void songAuthorEmpty() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        String token = Server.getTokenByUser("bond","007");
        Server.addSongs("{\"title\":\"Greensleeves\",\"composers\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"ratings\":[{\"user\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"rating\":5}],\"comments\":[]}",token);
    }

    // Не заполнен исполнитель
    @Test (expected = ServerException.class)
    public void songSingerEmpty() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        String token = Server.getTokenByUser("bond","007");
        Server.addSongs("{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"duration\":90.0,\"proposer\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"ratings\":[{\"user\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"rating\":5}],\"comments\":[]}",token);
    }

    // Не заполнена продолжительность песни
    @Test (expected = ServerException.class)
    public void songDurationEmpty() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        String token = Server.getTokenByUser("bond","007");
        Server.addSongs("{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"proposer\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"ratings\":[{\"user\":{\"firstName\":\"ivanov\",\"lastName\":\"ivan\",\"login\":\"iv\",\"password\":\"1\"},\"rating\":5}],\"comments\":[]}",token);
    }

    // Добавляем песню дважды
    @Test (expected = ServerException.class)
    public void songDuplicate() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        String token = Server.getTokenByUser("bond","007");
        Server.addSongs(REG_SONG_JSON_ONE,token);
        Server.addSongs(REG_SONG_JSON_ONE,token);
    }

    // Все заявленные в концерт песни.
    @Test
    public void getSongAll() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addSongs(REG_SONG_JSON_TWO,token2);
        Assert.assertEquals(Server.getSongs(),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[]},{\"title\":\"Style Life\",\"composers\":[],\"authors\":[],\"singer\":\"Чиж\",\"duration\":1200.0,\"proposer\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"ratings\":[{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":5}],\"comments\":[]}]}");
    }

    // Все заявленные в концерт песни указанного исполнителя
    @Test
    public void getSongsSinger() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addSongs(REG_SONG_JSON_TWO,token2);
        Assert.assertEquals(Server.getSongsBySinger("Чиж"),"{\"songsList\":[{\"title\":\"Style Life\",\"composers\":[],\"authors\":[],\"singer\":\"Чиж\",\"duration\":1200.0,\"proposer\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"ratings\":[{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":5}],\"comments\":[]}]}");
    }

    // Все заявленные в концерт песни указанного(ых) автора(ов)
    @Test
    public void getSongsByAuthors() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        Server.addSongs(REG_SONG_JSON_THREE,token1);
        Server.addSongs(REG_SONG_JSON_FOUR,token2);
        Server.addSongs(REG_SONG_JSON_FIVE,token2);
        Assert.assertEquals(Server.getSongsByAuthors("{\"name\": [\"Чиж\", \"Гребенщиков\"]}"),"{\"songsList\":[{\"title\":\"Одинокая птица\",\"composers\":[\"Чиж\"],\"authors\":[\"Чиж\",\"Иванов\"],\"singer\":\"Бутусов\",\"duration\":1200.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[]},{\"title\":\"Любовь не прошла\",\"composers\":[\"Иванов\",\"Гребенщиков\"],\"authors\":[\"Иванов\",\"Гребенщиков\"],\"singer\":\"ДДТ\",\"duration\":120.0,\"proposer\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"ratings\":[{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":5}],\"comments\":[]}]}");
    }

    // Все заявленные в концерт песни указанного(ых) композитора(ов)
    @Test
    public void getSongsComposers() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        Server.addSongs(REG_SONG_JSON_THREE,token1);
        Server.addSongs(REG_SONG_JSON_FOUR,token2);
        Server.addSongs(REG_SONG_JSON_FIVE,token2);
        Assert.assertEquals(Server.getSongsByComposers("{\"name\": [\"Бутусов\", \"Иванов\"]}"),"{\"songsList\":[{\"title\":\"Письмо\",\"composers\":[\"Иванов\"],\"authors\":[],\"singer\":\"Сплин\",\"duration\":1200.0,\"proposer\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"ratings\":[{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":5}],\"comments\":[]},{\"title\":\"Любовь не прошла\",\"composers\":[\"Иванов\",\"Гребенщиков\"],\"authors\":[\"Иванов\",\"Гребенщиков\"],\"singer\":\"ДДТ\",\"duration\":120.0,\"proposer\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"ratings\":[{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":5}],\"comments\":[]}]}");
    }
}
