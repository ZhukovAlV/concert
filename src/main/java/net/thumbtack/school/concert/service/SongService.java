package net.thumbtack.school.concert.service;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.SessionDao;
import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.dto.request.AuthorsDtoRequest;
import net.thumbtack.school.concert.dto.request.ComposersDtoRequest;
import net.thumbtack.school.concert.dto.request.SongDtoRequest;
import net.thumbtack.school.concert.dto.response.GetSongsDtoResponse;
import net.thumbtack.school.concert.dto.response.SongDtoResponse;
import net.thumbtack.school.concert.exception.ErrorCode;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.ServerException;

import java.util.*;

public class SongService {

    public SongDtoResponse addSong(String jsonString, String token, UserDao userDao, Gson gson, UserService userService, SessionDao sessionDao, SongDao songDao) throws ServerException{
        User proposer = userService.getUser(token,userDao);
        if (sessionDao.getUserListSession().get(token)==null) throw new ServerException(ErrorCode.USER_TOKEN_NOT_VALID);
        SongDtoRequest songDtoRequest = gson.fromJson(jsonString, SongDtoRequest.class);
        if (songDtoRequest.getTitle()==null) throw new ServerException(ErrorCode.SONG_TITLE_IS_EMPTY);
        if (songDtoRequest.getComposers()==null) throw new ServerException(ErrorCode.SONG_COMPOSER_IS_EMPTY);
        if (songDtoRequest.getAuthors()==null) throw new ServerException(ErrorCode.SONG_AUTHOR_IS_EMPTY);
        if (songDtoRequest.getSinger()==null) throw new ServerException(ErrorCode.SONG_SINGER_IS_EMPTY);
        if (songDtoRequest.getDuration()==0.0) throw new ServerException(ErrorCode.SONG_DURATION_IS_EMPTY);
        Song song = new Song(songDtoRequest.getTitle(), songDtoRequest.getComposers(),
                songDtoRequest.getAuthors(), songDtoRequest.getSinger(), songDtoRequest.getDuration(), proposer);
        if (!songDao.getSongsByTitle(songDtoRequest.getTitle()).isEmpty()) throw new ServerException(ErrorCode.SONG_IS_EXIST);
        songDao.insert(song);
        return new SongDtoResponse(song.getTitle(),song.getComposers(),song.getAuthors(),song.getSinger(),song.getProposer(),song.getRatingValue(),song.getComments());
    }

    public List<Song> cancelSongs(String jsonString, User user, User userCommunity,Gson gson, SongDao songDao) {
        SongDtoRequest songDtoRequest = gson.fromJson(jsonString, SongDtoRequest.class);
        Song songForCancel = new Song(songDtoRequest.getTitle(), songDtoRequest.getComposers(),
                songDtoRequest.getAuthors(), songDtoRequest.getSinger(), songDtoRequest.getDuration(),user);
        return songDao.cancelSong(songForCancel,user,userCommunity);
    }

    public void setSongs(List<Song> song, SongDao songDao) {
        songDao.setSongs(song);
    }

    public GetSongsDtoResponse getSongs(SongDao songDao) {
        return new GetSongsDtoResponse(songDao.getSongs());
    }

    public GetSongsDtoResponse getSongsSinger(String singer, SongDao songDao) {
        List<Song> songsList = new ArrayList<>();
        if (singer != null) {
            songsList = songDao.getSongsBySinger(singer);
        }
        return new GetSongsDtoResponse(songsList);
    }

    public GetSongsDtoResponse getSongsAuthors(String authorsJson, SongDao songDao, Gson gson) {
        List<Song> songsFindList = new ArrayList<>();
        AuthorsDtoRequest authorsDtoRequest = gson.fromJson(authorsJson,AuthorsDtoRequest.class);
        List<Song> songsList = songDao.getSongs();
        if (authorsDtoRequest!=null) {
            List<String> authors = authorsDtoRequest.getName();
            for (String author: authors
            ) {
                for (Song song: songsList
                ) {
                    if (song.getAuthors().contains(author)) songsFindList.add(song);
                }
            }
        }
        return new GetSongsDtoResponse(songsFindList);
    }

    public GetSongsDtoResponse getSongsComposers(String composersJson, SongDao songDao, Gson gson) {
        List<Song> songsFindList = new ArrayList<>();
        ComposersDtoRequest composersDtoRequest = gson.fromJson(composersJson,ComposersDtoRequest.class);
        List<Song> songsList = songDao.getSongs();
        if (composersDtoRequest!=null) {
            List<String> composers = composersDtoRequest.getName();
            for (String composer: composers
            ) {
                for (Song song: songsList
                ) {
                    if (song.getComposers().contains(composer)) songsFindList.add(song);
                }
            }
        }
        return new GetSongsDtoResponse(songsFindList);
    }

    public GetSongsDtoResponse getSongs(User user, SongDao songDao) {
        return new GetSongsDtoResponse(songDao.getSongsByProposer(user));
    }

    public GetSongsDtoResponse getSongsByTitle(String titleSong, SongDao songDao) {
        return new GetSongsDtoResponse(songDao.getSongsByTitle(titleSong));
    }

    public void setRating(String nameSong, int rating, String token,UserDao userDao,UserService userService, SongDao songDao) throws ServerException {
        if (!(rating>=0&&rating<=5))  throw new ServerException(ErrorCode.SONG_RATING_OUT);
        User user = userService.getUser(token,userDao);
        if (user==null) throw new ServerException(ErrorCode.USER_TOKEN_NOT_VALID);
        songDao.setRating(nameSong,rating,user);
    }

    public void changeRating(String nameSong, int rating, String token,UserDao userDao,UserService userService, SongDao songDao) throws ServerException {
        User user = userService.getUser(token,userDao);
        if (user==null) throw new ServerException(ErrorCode.USER_TOKEN_NOT_VALID);
        songDao.changeRating(nameSong,rating,user);
    }

    public void deleteRating(String nameSong,  String token,UserDao userDao,UserService userService, SongDao songDao) throws ServerException {
        User user = userService.getUser(token,userDao);
        if (user==null) throw new ServerException(ErrorCode.USER_TOKEN_NOT_VALID);
        songDao.deleteRating(nameSong, user);
    }

    public void addComment(String nameSong,  String comments, String token,UserDao userDao,UserService userService, SongDao songDao) throws ServerException {
        User user = userService.getUser(token,userDao);
        if (user==null) throw new ServerException(ErrorCode.USER_TOKEN_NOT_VALID);
        songDao.addComment(nameSong, comments, user);
    }

    public void joinComment(String nameSong,  String comments, String token,UserDao userDao,UserService userService, SongDao songDao) throws ServerException {
        User user = userService.getUser(token,userDao);
        if (user==null) throw new ServerException(ErrorCode.USER_TOKEN_NOT_VALID);
        songDao.joinComment(nameSong, comments, user);
    }

    public void changeComment(String nameSong,  String comments, String token, String tokenCommunity,UserDao userDao,UserService userService, SongDao songDao) throws ServerException {
        User user = userService.getUser(token,userDao);
        User userCommunity = userService.getUser(tokenCommunity,userDao);
        if (user==null) throw new ServerException(ErrorCode.USER_TOKEN_NOT_VALID);
        if (userCommunity==null) throw new ServerException(ErrorCode.USER_COMMUNITY_NOT_EXIST);
        songDao.changeComment(nameSong, comments, user, userCommunity);
    }

    public List<SongDtoResponse> concertDemoProgram(SongDao songDao) {
        List<SongDtoResponse> concertProgramDtoResponse = new ArrayList<>();
        List<Song> songList = new ArrayList<>();
        List<Song> songs = songDao.getSongs();
        songs.sort(Comparator.comparingInt(Song::getRatingValue).reversed());
        for (int i = 0, k = 0; i < songs.size(); i++) {
            k += songs.get(i).getDuration() + 10;
            if (k<=3600) songList.add(songs.get(i));
            else  k -= songs.get(i).getDuration() + 10;
        }
        for (Song song: songList
        ) {
            SongDtoResponse songDtoResponse = new SongDtoResponse(song.getTitle(),song.getComposers(),song.getAuthors(),song.getSinger(),song.getProposer(),song.getRatingValue(),song.getComments());
            concertProgramDtoResponse.add(songDtoResponse);
        }
        return concertProgramDtoResponse;
    }
}

