package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.ServerException;

import java.util.List;

public interface SongDao {
    void insert(Song song);
    List<Song> cancelSong(Song songForCancel, User user, User userCommunity);
    void setSongs(List<Song> songs);
    List<Song> getSongs();
    List<Song> getSongsByTitle(String singer);
    List<Song> getSongsBySinger(String singer);
    List<Song> getSongsByAuthor(String author);
    List<Song> getSongsByComposer(String composer);
    List<Song> getSongsByProposer(User user);
    void updateComment(User user, User userCommunity, Song song);
    void setRating(String nameSong, int rating,User user) throws ServerException;
    void changeRating(String nameSong, int rating,User user) throws ServerException;
    List<Song> deleteRating(String nameSong, User user) throws ServerException;
    void addComment(String nameSong,  String comments, User user);
    void joinComment(String nameSong,  String comments, User user);
    void changeComment(String nameSong,  String comments, User user, User userCommunity);
}
