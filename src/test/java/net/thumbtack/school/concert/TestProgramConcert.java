package net.thumbtack.school.concert;

import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.server.ServerException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static net.thumbtack.school.concert.server.Server.isIsStarted;

public class TestProgramConcert {

    private static File file = new File("savedDataFile.txt");
    final static String REG_USER_JSON_ONE = "{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"}";
    final static String REG_USER_JSON_TWO = "{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}";
    final static String REG_SONG_JSON_ONE = "{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":1200.0,\"comments\":[]}";
    final static String REG_SONG_JSON_TWO = "{\"title\":\"Style Life\",\"composers\":[],\"authors\":[],\"singer\":\"Чиж\",\"duration\":1200.0,\"proposer\":{},\"ratings\":[],\"comments\":[]}";
    final static String REG_SONG_JSON_THREE = "{\"title\":\"Одинокая птица\",\"composers\":[],\"authors\":[],\"singer\":\"Бутусов\",\"duration\":1200.0,\"proposer\":{},\"ratings\":[],\"comments\":[]}";
    final static String REG_SONG_JSON_FOUR = "{\"title\":\"Любовь не прошла\",\"composers\":[],\"authors\":[],\"singer\":\"ДДТ\",\"duration\":120.0,\"proposer\":{},\"ratings\":[],\"comments\":[]}";
    final static String REG_SONG_JSON_FIVE = "{\"title\":\"Письмо\",\"composers\":[],\"authors\":[],\"singer\":\"Сплин\",\"duration\":1200.0,\"proposer\":{},\"ratings\":[],\"comments\":[]}";

    // Пробная программа концерта
    @Test
    public void concertDemoProgram() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        String comment1="моя любимая песня";
        String comment2="лучшая в мире";
        String nameSong1="Greensleeves";
        String nameSong2="Style Life";
        String nameSong3="Одинокая птица";
        String nameSong4="Любовь не прошла";
        String nameSong5="Письмо";
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addSongs(REG_SONG_JSON_TWO,token2);
        Server.addSongs(REG_SONG_JSON_THREE,token2);
        Server.addSongs(REG_SONG_JSON_FOUR,token1);
        Server.addSongs(REG_SONG_JSON_FIVE,token1);
        Server.addComment(nameSong1,comment1,token1);
        Server.addComment(nameSong2,comment2,token2);
        Server.addRating(nameSong1, 5, token2);
        Server.addRating(nameSong2, 2, token1);
        Server.addRating(nameSong3, 3, token1);
        Server.addRating(nameSong4, 1, token2);
        Server.addRating(nameSong5, 4, token2);
        Assert.assertEquals(Server.concertDemoProgram(),"[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":500,\"comments\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"comment\":\"моя любимая песня\",\"joinedUser\":[]}]},{\"title\":\"Письмо\",\"composers\":[],\"authors\":[],\"singer\":\"Сплин\",\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":450,\"comments\":[]},{\"title\":\"Любовь не прошла\",\"composers\":[],\"authors\":[],\"singer\":\"ДДТ\",\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":300,\"comments\":[]}]");
    }

}
