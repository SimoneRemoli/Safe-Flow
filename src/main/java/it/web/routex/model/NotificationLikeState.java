package it.web.routex.model;

public class NotificationLikeState {

    private final int likeCount;
    private final boolean likedByCurrentUser;

    public NotificationLikeState(int likeCount, boolean likedByCurrentUser) {
        this.likeCount = likeCount;
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }
}
