package net.thumbtack.school.concert.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Song {
    private String title;
    private List<String> composers;
    private List<String> authors;
    private String singer;
    //vstrokan: наивно полагать, что песня всегда длится целое число секунд
        //avzhukov: исправил
    private double duration;
    private User proposer;
    private List<Rating> ratings = new ArrayList<>();
    private Set<Comment> comments = new HashSet<>();

    public Song(String title, List<String> composers, List<String> authors, String singer, double duration, User proposer) {
        this.title = title;
        this.composers = composers;
        this.authors = authors;
        this.singer = singer;
        this.duration = duration;
        this.proposer = proposer;
        ratings.add(new Rating(proposer, 5));
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

    public User getProposer() {
        return proposer;
    }

    public void setProposer(User proposer) {
        this.proposer = proposer;
    }

    public void setRating(User user, int rating) {
        ratings.add(new Rating(user, rating));
    }

    public void deleteRating(Rating rating) {
        ratings.remove(rating);
    }

    public List<Rating> getRating() {
        return ratings;
    }

    public int getRatingValue() {
        double avgRating = 0;
        for (Rating rating : ratings) {
            avgRating += rating.getRating() * 100;
        }
        avgRating /= ratings.size();
        return (int) avgRating;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void addComments(String comment, User user) {
        this.comments.add(new Comment(comment,user));
    }
}
