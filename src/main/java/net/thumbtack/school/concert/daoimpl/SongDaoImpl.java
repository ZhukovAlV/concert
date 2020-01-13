package net.thumbtack.school.concert.daoimpl;

import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.exception.ErrorCode;
import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.Rating;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.ServerException;

import java.util.*;

import static net.thumbtack.school.concert.database.DataBase.getSongList;

public class SongDaoImpl implements SongDao {

    @Override
    public void insert(Song song) {
        getSongList().put(song,song.getTitle());
    }

    @Override
    public List<Song> cancelSong(Song songForCancel, User user, User userCommunity) {
        for (Map.Entry<Song,String> song: getSongList().entrySet()
        ) {
            if  (songForCancel.getTitle().equals(song.getKey().getTitle())
                    &&songForCancel.getSinger().equals(song.getKey().getSinger())
                    &&songForCancel.getProposer().equals(song.getKey().getProposer())) {
                if (song.getKey().getRating().size()==1) {
                    getSongList().entrySet().remove(song);
                    break;
                } else {
                    List<Rating> ratings = song.getKey().getRating();
                    for (Rating rating: ratings
                    ) {
                        if (rating.getUser().equals(user)) {
                            song.getKey().deleteRating(rating);
                            song.getKey().setProposer(userCommunity);
                            updateComment(user,userCommunity,song.getKey());
                        }
                    }
                }
            }
        }
        return getSongs();
    }

    @Override
    public void updateComment(User user, User userCommunity,Song song) {
        Set<Comment> commentsList = song.getComments();
        for (Comment comment: commentsList
        ) {
            if (comment.getUser().equals(user)) comment.setUser(userCommunity);
        }
    }

    @Override
    public void changeComment(String nameSong, String comments, User user, User userCommunity) {
        List<Song> songs = getSongsByTitle(nameSong);
        for (Song song: songs
        ) {
            Set<Comment> commentsList = song.getComments();
            for (Comment comment: commentsList
            ) {
                if (comment.getUser().equals(user)&&comment.getJoinedUser().size()==0) comment.setComment(comments);
                else {
                    updateComment(user,userCommunity,song);
                    song.addComments(comments,user);
                }
            }
        }
    }

    @Override
    public void setSongs(List<Song> songs) {
        if (songs!=null) {
            for (Song song: songs
                 ) {
                getSongList().put(song,song.getTitle());
            }
        }

    }

    @Override
    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();
        for (Map.Entry<Song,String> song: getSongList().entrySet()
        ) {
            songs.add(song.getKey());
        }
        return songs;
    }

    @Override
    //vstrokan: этот метод у вас часто используется. Более эффективным мне видится использование Map вместо List, доступ будет быстрее и проще, не нужно будет идти по всем песням, чтобы найти нужную
        //avzhukov: сделал в DataBase Map<Song,String> songList вместо List<Song> songList
    public List<Song> getSongsByTitle(String title) {
        List<Song> songs = new ArrayList<>();
        for (Map.Entry<Song,String> song: getSongList().entrySet()
        ) {
            if  (song.getValue().equals(title))
                songs.add(song.getKey());
        }
        return songs;
    }

    @Override
    public List<Song> getSongsBySinger(String singer) {
        List<Song> songs = new ArrayList<>();
        for (Map.Entry<Song,String> song: getSongList().entrySet()
        ) {
            if  (song.getKey().getSinger().equals(singer))
                songs.add(song.getKey());
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByAuthor(String author) {
        List<Song> songs = new ArrayList<>();
        for (Map.Entry<Song,String> song: getSongList().entrySet()
        ) {
            for (String authorSong: song.getKey().getAuthors()
            ) {
                if  (authorSong.equals(author))
                    songs.add(song.getKey());
            }
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByComposer(String composer) {
        List<Song> songs = new ArrayList<>();
        for (Map.Entry<Song,String> song: getSongList().entrySet()
        ) {
            for (String composerSong: song.getKey().getComposers()
            ) {
                if  (composerSong.equals(composer))
                    songs.add(song.getKey());
            }
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByProposer(User user) {
        List<Song> songs = new ArrayList<>();
        for (Map.Entry<Song,String> song: getSongList().entrySet()
        ) {
            if  (song.getKey().getProposer().equals(user))
                songs.add(song.getKey());
        }
        return songs;
    }

    @Override
    public void setRating(String nameSong, int rating, User user) throws ServerException{
        List<Song> songs = getSongsByTitle(nameSong);
        for (Song song: songs
        ) {
            if (song.getProposer().equals(user)) throw new ServerException(ErrorCode.SONG_RATING_ONLY_READ);
            List<Rating> ratings = song.getRating();
            for (Rating ratingSong: ratings
            ) {
                if (ratingSong.getUser().equals(user)) throw new ServerException(ErrorCode.SONG_RATING_EXIST);
            }
            song.setRating(user,rating);
        }
    }

    @Override
    public void changeRating(String nameSong, int rating, User user) throws ServerException {
        List<Song> songs = getSongsByTitle(nameSong);
        for (Song song: songs
        ) {
            if (song.getProposer().equals(user)) throw new ServerException(ErrorCode.SONG_RATING_ONLY_READ);
            List<Rating> ratings = song.getRating();
            for (Rating ratingSong: ratings
            ) {
                if (ratingSong.getUser().equals(user)) ratingSong.setRating(rating);
            }
        }
    }

    @Override
    public List<Song> deleteRating(String nameSong, User user) throws ServerException {
        List<Song> songs = getSongsByTitle(nameSong);
        List<Rating> ratings;
        for (Song song: songs
        ) {
            if (song.getProposer().equals(user)) throw new ServerException(ErrorCode.SONG_RATING_ONLY_READ);
            ratings = song.getRating();
            for (Rating ratingSong: ratings
            ) {
                if (ratingSong.getUser().equals(user)) {
                    ratings.remove(ratingSong);
                    break;
                }
            }
        }
        return songs;
    }

    @Override
    public void addComment(String nameSong, String comments, User user) {
        List<Song> songs = getSongsByTitle(nameSong);
        for (Song song: songs
        ) {
            song.addComments(comments,user);
        }
    }

    @Override
    public void joinComment(String nameSong,  String comments, User user) {
        List<Song> songs = getSongsByTitle(nameSong);
        for (Song song: songs
        ) {
            Set<Comment> commentsList = song.getComments();
            for (Comment comment: commentsList
            ) {
                if (comment.getComment().equals(comments)) comment.addJoined(user);
            }
        }
    }

/*    @Override
    //vstrokan: ответственность слоя dao - работа с данными (create/read/update/delete). Бизнес логика по фильтрации/сортировке должна быть на уровне сервиса
        //avzhukov: убрал из DAO перенес в сервис
    public List<Song> concertDemoProgram() {
        List<Song> songList = new ArrayList<>();
        List<Song> songs = getSongs();
        songs.sort(Comparator.comparingInt(Song::getRatingValue).reversed());
        for (int i = 0, k = 0; i < songs.size(); i++) {
            k += songs.get(i).getDuration() + 10;
            if (k<=3600) songList.add(songs.get(i));
            else  k -= songs.get(i).getDuration() + 10;
        }
        return songList;
    }*/
}
