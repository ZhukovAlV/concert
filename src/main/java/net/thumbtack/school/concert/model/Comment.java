package net.thumbtack.school.concert.model;

import java.util.HashSet;
import java.util.Set;

public class Comment {
    private User user;
    private String comment;
    private Set<User> joinedUser = new HashSet<>();

    public Comment(String comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    public Comment(User author, String comment, Set<User> joinedUser) {
        this(comment, author);
        this.joinedUser = joinedUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<User> getJoinedUser() {
        return joinedUser;
    }

    public void setJoinedUser(Set<User> joinedUser) {
        this.joinedUser = joinedUser;
    }

    public void addJoined(User joinedUser) {
        this.joinedUser.add(joinedUser);
    }
}
