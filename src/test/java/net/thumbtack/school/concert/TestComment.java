package net.thumbtack.school.concert;

import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.server.ServerException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static net.thumbtack.school.concert.server.Server.isIsStarted;

public class TestComment {

    private static File file = new File("savedDataFile.txt");
    final static String REG_USER_JSON_ONE = "{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"}";
    final static String REG_USER_JSON_TWO = "{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}";
    final static String REG_SONG_JSON_ONE = "{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"comments\":[]}";
    final static String REG_SONG_JSON_TWO = "{\"title\":\"Style Life\",\"composers\":[],\"authors\":[],\"singer\":\"Чиж\",\"duration\":1200.0,\"proposer\":{},\"ratings\":[],\"comments\":[]}";

    // Добавляем комментарий
    @Test
    public void addComment() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        String comment1="моя любимая песня";
        String comment2="лучшая в мире";
        String nameSong1="Greensleeves";
        String nameSong2="Style Life";
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addSongs(REG_SONG_JSON_TWO,token2);
        Server.addComment(nameSong1,comment1,token1);
        Server.addComment(nameSong2,comment2,token2);
        Assert.assertEquals(Server.getSongsByTitle(nameSong1),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"comment\":\"моя любимая песня\",\"joinedUser\":[]}]}]}");
        Assert.assertEquals(Server.getSongsByTitle(nameSong2),"{\"songsList\":[{\"title\":\"Style Life\",\"composers\":[],\"authors\":[],\"singer\":\"Чиж\",\"duration\":1200.0,\"proposer\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"ratings\":[{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":5}],\"comments\":[{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"comment\":\"лучшая в мире\",\"joinedUser\":[]}]}]}");
    }

    // Присоединяемся к комментарию
    @Test
    public void joinComment() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        String comment1="моя любимая песня";
        String nameSong1="Greensleeves";
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        Server.addSongs(REG_SONG_JSON_ONE,token1);;
        Server.addComment(nameSong1,comment1,token1);
        Server.joinComment(nameSong1,comment1,token2);
        Assert.assertEquals(Server.getSongsByTitle("Greensleeves"),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"comment\":\"моя любимая песня\",\"joinedUser\":[{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}]}]}]}");
    }

    // Изменить комментарий + проверка выхода пользователей
    @Test
    public void changeComment() throws IOException, ServerException {
        if (isIsStarted()) Server.stopServer(file);
        Server.startServer(null);
        String comment1="моя любимая песня";
        String comment2="Повторюсь. Моя любимая песня";
        String nameSong1="Greensleeves";
        Server.registerUser(REG_USER_JSON_ONE);
        Server.registerUser(REG_USER_JSON_TWO);
        String token1 = Server.getTokenByUser("bond","007");
        String token2 = Server.getTokenByUser("artur", "221b");
        String token3 = Server.getTokenByUser("community", "community");
        Server.addSongs(REG_SONG_JSON_ONE,token1);
        Server.addComment(nameSong1,comment1,token1);
        Assert.assertEquals(Server.getSongsByTitle("Greensleeves"),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"comment\":\"моя любимая песня\",\"joinedUser\":[]}]}]}");
        Server.changeComment(nameSong1,comment2,token1,token3);
        Assert.assertEquals(Server.getSongsByTitle("Greensleeves"),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"comment\":\"Повторюсь. Моя любимая песня\",\"joinedUser\":[]}]}]}");
        Server.joinComment(nameSong1,comment2,token2);
        Assert.assertEquals(Server.getSongsByTitle("Greensleeves"),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"comment\":\"Повторюсь. Моя любимая песня\",\"joinedUser\":[{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}]}]}]}");
        Server.changeComment(nameSong1,comment1,token1,token3);
        Assert.assertEquals(Server.getSongsByTitle("Greensleeves"),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"ratings\":[{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"rating\":5}],\"comments\":[{\"user\":{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"},\"comment\":\"Повторюсь. Моя любимая песня\",\"joinedUser\":[{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}]},{\"user\":{\"firstName\":\"Pirs\",\"lastName\":\"Brosnan\",\"login\":\"bond\",\"password\":\"007\"},\"comment\":\"моя любимая песня\",\"joinedUser\":[]}]}]}");
        Server.addRating(nameSong1, 2, token2);
        Server.exitUser(token1);
        Assert.assertEquals(Server.getSongsByTitle("Greensleeves"),"{\"songsList\":[{\"title\":\"Greensleeves\",\"composers\":[],\"authors\":[],\"singer\":\"unknown\",\"duration\":90.0,\"proposer\":{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"},\"ratings\":[{\"user\":{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"},\"rating\":2}],\"comments\":[{\"user\":{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"},\"comment\":\"Повторюсь. Моя любимая песня\",\"joinedUser\":[{\"firstName\":\"Sherlok\",\"lastName\":\"Holms\",\"login\":\"artur\",\"password\":\"221b\"}]},{\"user\":{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"},\"comment\":\"моя любимая песня\",\"joinedUser\":[]}]}]}");
    }
}
