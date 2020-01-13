package net.thumbtack.school.concert.model;

public class Rating {
    private User user;
    private int rating;

    public Rating(User user, int rating) {
        this.user = user;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
