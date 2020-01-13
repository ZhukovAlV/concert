package net.thumbtack.school.concert;

import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.server.ServerException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static net.thumbtack.school.concert.server.Server.isIsStarted;

public class TestRating {

    private static File file = new File("savedDataFile.txt");
    final static String REG_USER_JSON_ONE = "{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"}";
    final static String REG_USER_JSON_TWO = "{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}";
    final static String REG_SONG_JSON_ONE = "{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"comments\":[]}";

    // Ставим оценку песне
    @Test
    public void addRating() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        String nameSong = "Greensleeves";
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addRating(nameSong, 2, token2);
        Assert.assertEquals(Server.getSongsByTitle(nameSong),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5},{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":2}],\"comments\":[]}]}");
    }

    // Ставим оценку песне второй раз любым пользователем - не создателем предложения
    @Test (expected = ServerException.class)
    public void addRatingPropose() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        String nameSong = "Greensleeves";
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addRating(nameSong, 2, token2);
        Server.addRating(nameSong, 2, token2);
    }

    // Ставим оценку песне второй раз создателем песни
    @Test (expected = ServerException.class)
    public void addRatingSecond() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        String token = Server.getTokenByUser("bond","007");
        String nameSong = "Greensleeves";
        Server.addSongs(REG_SONG_JSON_ONE,token);
        Server.addRating(nameSong, 2, token);
    }

    // Радиослушатели вправе изменить свою оценку
    @Test
    public void changeRating() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        String nameSong = "Greensleeves";
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addRating(nameSong, 2, token2);
        Server.changeRating(nameSong, 5, token2);
        Assert.assertEquals(Server.getSongsByTitle(nameSong),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5},{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":5}],\"comments\":[]}]}");
    }

    // Радиослушатели вправе удалить свою оценку
    @Test
    public void deleteRating() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur","221b");
        String nameSong = "Greensleeves";
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addRating(nameSong, 2, token2);
        Server.deleteRating(nameSong, token2);
        Assert.assertEquals(Server.getSongsByTitle(nameSong),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[]}]}");
    }

    // Радиослушатели могут ставить свои оценки предлагаемым в программу песням по шкале 1..5.
    @Test (expected = ServerException.class)
    public void addRatingOut() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        String nameSong = "Greensleeves";
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addRating(nameSong, 8, token2);
    }
}
