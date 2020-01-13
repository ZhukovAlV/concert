package net.thumbtack.school.concert.dto.response;

import net.thumbtack.school.concert.model.Song;

import java.util.List;

public class GetSongsDtoResponse {

    private List<Song> songsList;

    public GetSongsDtoResponse(List<Song> songsList) {
        this.songsList = songsList;
    }

    public List<Song> getSongsList() {
        return songsList;
    }

    public void setSongsList(List<Song> songsList) {
        this.songsList = songsList;
    }
}
