package net.thumbtack.school.concert.dto.response;

import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.Rating;
import net.thumbtack.school.concert.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongDtoResponse {
    private String title;
    private List<String> composers;
    private List<String> authors;
    private String singer;
    private User proposer;
    private int rating;
    private Set<Comment> comments;

    public SongDtoResponse(String title, List<String> composers, List<String> authors, String singer, User proposer, int rating, Set<Comment> comments) {
        this.title = title;
        this.composers = composers;
        this.authors = authors;
        this.singer = singer;
        this.proposer = proposer;
        this.rating = rating;
        this.comments = comments;
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

    public User getProposer() {
        return proposer;
    }

    public void setProposer(User proposer) {
        this.proposer = proposer;
    }

    public int rating() {
        return rating;
    }

    public void rating(int rating) {
        this.rating = rating;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
