package net.thumbtack.school.concert.server;

import com.google.gson.Gson;
import net.thumbtack.school.concert.exception.ErrorCode;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.service.FileService;
import net.thumbtack.school.concert.service.ServerService;
import net.thumbtack.school.concert.service.SongService;
import net.thumbtack.school.concert.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Server {

    private static final String REG_USER_COMMUNITY = "{\"firstName\":\"Community\",\"lastName\":\"Listeners\",\"login\":\"community\",\"password\":\"community\"}";
    public static String getRegUserCommunity() {
        return REG_USER_COMMUNITY;
    }

    private static String uuid;
    public static String getUuid() {
        return UUID.randomUUID().toString();
    }

    private static boolean isStarted;
    public static boolean isIsStarted() {
        return isStarted;
    }
    public static void setIsStarted(boolean isStarted) {
        Server.isStarted = isStarted;
    }

    private static Gson gson = new Gson();
    public static Gson getGson() {
        return gson;
    }
    public static void setGson(Gson gson) {
        Server.gson = gson;
    }

    private static FileService fileService = new FileService();
    public static FileService getFileService() {
        return fileService;
    }
    public static void setFileService(FileService fileService) {
        Server.fileService = fileService;
    }

    private static UserService userService = new UserService();
    public static UserService getUserService() {
        return userService;
    }
    public static void setUserService(UserService userService) {
        Server.userService = userService;
    }

    private static SongService songService = new SongService();
    public static SongService getSongService() {
        return songService;
    }
    public static void setSongService(SongService songService) {
        Server.songService = songService;
    }

    private static ServerService serverService = new ServerService();
    public static ServerService getServerService() {
        return serverService;
    }
    public static void setServerService(ServerService serverService) {
        Server.serverService = serverService;
    }

    // Производит всю необходимую инициализацию и запускает сервер.
    public static void startServer(File savedDataFileName) throws ServerException {
        ServerService.checkServerNotStart(isIsStarted());
        setIsStarted(true);
        ServerService.startServer(savedDataFileName,isIsStarted(),getGson(),getUserService(),getSongService(),ServerService.getUserDao(),ServerService.getSongDao(),getFileService());
        if (savedDataFileName==null) registerUser(getRegUserCommunity());
    }

    // Останавливает сервер и записывает все его содержимое в файл
    public static void stopServer(File savedDataFile) throws IOException,ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        setIsStarted(false);
        ServerService.stopServer(savedDataFile,isIsStarted(),getGson(),getUserService(),getSongService(),ServerService.getUserDao(),ServerService.getSongDao(),ServerService.getDatarDao(),getFileService());
    }

    public static String registerUser(String regUserJson) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getUserService().registerUser(regUserJson,getUuid(),getGson(),ServerService.getUserDao(),ServerService.getSessionDao()));
    }

    public static String getUser(String token) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getUserService().getUser(token,ServerService.getUserDao()));
    }

    public static String getUsers() throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getUserService().getUsers(ServerService.getUserDao()));
    }

    public static String addSongs(String addSongsJson, String token) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getSongService().addSong(addSongsJson,token,ServerService.getUserDao(),getGson(),getUserService(),ServerService.getSessionDao(),ServerService.getSongDao())); //vstrokan: требование ТЗ выполнено не полностью, юзер не может добавить несколько песен одним запросом: Любой радиослушатель может предложить любое количество песен в программу концерта. При этом он не обязан предложить все песни сразу, а может добавлять их по одной или сразу несколько.
    }

    public static String cancelSong(String cancelSongJson, String token) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        User userCommunity = getUserService().getUserFromJson(getRegUserCommunity(),getGson(),ServerService.getUserDao(),ServerService.getSessionDao());
        User user = getUserService().getUser(token,ServerService.getUserDao());
        return getGson().toJson(getSongService().cancelSongs(cancelSongJson,user,userCommunity,getGson(),ServerService.getSongDao()));
    }

    public static String getTokenByUser(String login, String pass) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getUserService().getToken(login,pass,ServerService.getUserDao(),ServerService.getSessionDao());
    }

    public static String loginUser(String jsonString) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getUserService().loginUser(jsonString,getUuid(),getGson(),ServerService.getUserDao(),ServerService.getSessionDao());
    }

    public static void logoutUser(String token) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        getUserService().logoutUser(token,ServerService.getSessionDao());
    }

    public static void exitUser(String token) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        getUserService().exitUser(token,ServerService.getUserDao(),ServerService.getSessionDao(),ServerService.getSongDao());
    }

    public static String getSongsByProposer (String jsonString) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        User user = getUserService().getUserFromJson(jsonString,getGson(),ServerService.getUserDao(),ServerService.getSessionDao());
        return getGson().toJson(getSongService().getSongs(user,ServerService.getSongDao()));
    }

    //vstrokan: не полностью выполнено требование ТЗ: нельзя получить песни по НЕСКОЛЬКИМ композиторам или по НЕСКОЛЬКИМ авторам слов:
//    В любой момент любой радиослушатель может получить следующие списки
//
//    Все заявленные в концерт песни.
//    Все заявленные в концерт песни указанного композитора или композиторов.
//    Все заявленные в концерт песни указанного автора слов или авторов слов.
        // avzhukov: исправил
    public static String getSongs() throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getSongService().getSongs(ServerService.getSongDao()));
    }

    public static String getSongsByAuthors (String authors) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getSongService().getSongsAuthors(authors,ServerService.getSongDao(),getGson()));
    }

    public static String getSongsByComposers (String composers) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getSongService().getSongsComposers(composers,ServerService.getSongDao(),getGson()));
    }

    public static String getSongsBySinger (String singer) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getSongService().getSongsSinger(singer,ServerService.getSongDao()));
    }

    public static String getSongsByTitle (String titleSong) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getSongService().getSongsByTitle(titleSong,ServerService.getSongDao()));
    }

    //vstrokan: 1. Бизнес-логика по рейтингу должна находиться на уровне сервиса 2. Логика повторяется, имеет смысл инкапсулировать в методе
        //avzhuko: Исправил
    public static void addRating(String nameSong,int rating,String token) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        getSongService().setRating(nameSong, rating, token,ServerService.getUserDao(),getUserService(),ServerService.getSongDao());
    }

    public static void changeRating(String nameSong,int rating,String token) throws ServerException{
        if (!(rating>=0&&rating<=5))  throw new ServerException(ErrorCode.SONG_RATING_OUT);
        ServerService.checkServerIsStart(isIsStarted());
        getSongService().changeRating(nameSong, rating, token,ServerService.getUserDao(),getUserService(),ServerService.getSongDao());
    }

    public static void deleteRating(String nameSong,String token) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        getSongService().deleteRating(nameSong, token,ServerService.getUserDao(),getUserService(),ServerService.getSongDao());
    }

    public static void addComment(String nameSong,String comments, String token) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        getSongService().addComment(nameSong, comments, token,ServerService.getUserDao(),getUserService(),ServerService.getSongDao());
    }

    public static void joinComment(String nameSong,String comments, String token) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        getSongService().joinComment(nameSong, comments, token,ServerService.getUserDao(),getUserService(),ServerService.getSongDao());
    }

    public static void changeComment(String nameSong,String comments, String token, String tokenCommunity) throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        getSongService().changeComment(nameSong, comments, token, tokenCommunity,ServerService.getUserDao(),getUserService(),ServerService.getSongDao());
    }

    public static String concertDemoProgram() throws ServerException{
        ServerService.checkServerIsStart(isIsStarted());
        return getGson().toJson(getSongService().concertDemoProgram(ServerService.getSongDao()));
    }
}
