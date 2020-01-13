package net.thumbtack.school.concert.dto.request;

import java.util.ArrayList;
import java.util.List;

public class SongDtoRequest {
    private String title;
    private List<String> composers;
    private List<String> authors;
    private String singer;
    private double duration;
    /*    private List<Rating> ratings = new ArrayList<>();
    private Set<Comment> comments = new HashSet<>();*/

    public SongDtoRequest(String title, List<String> composers, List<String> authors, String singer, Integer duration) {
        this.title = title;
        this.composers = composers;
        this.authors = authors;
        this.singer = singer;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getComposers() {
        return composers;
    }

    public void setComposers(List<String> composers) {
        this.composers = composers;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
