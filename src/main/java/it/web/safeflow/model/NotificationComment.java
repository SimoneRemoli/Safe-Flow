package it.web.safeflow.model;

import java.sql.Timestamp;

public class NotificationComment {

    private final String id;
    private final String notificationKey;
    private final String authorCf;
    private final String parentCommentId;
    private final String replyToCf;
    private final String text;
    private final Timestamp createdAt;
    private String authorDisplayName;
    private String authorInitials;
    private boolean authorAvatarPresent;
    private boolean currentUserAuthor;
    private String replyToDisplayName;
    private int likeCount;
    private boolean likedByCurrentUser;

    public NotificationComment(String id,
                               String notificationKey,
                               String authorCf,
                               String text,
                               Timestamp createdAt) {
        this(id, notificationKey, authorCf, null, null, text, createdAt);
    }

    public NotificationComment(String id,
                               String notificationKey,
                               String authorCf,
                               String parentCommentId,
                               String replyToCf,
                               String text,
                               Timestamp createdAt) {
        this.id = id;
        this.notificationKey = notificationKey;
        this.authorCf = authorCf;
        this.parentCommentId = parentCommentId;
        this.replyToCf = replyToCf;
        this.text = text;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public String getAuthorCf() {
        return authorCf;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public String getReplyToCf() {
        return replyToCf;
    }

    public String getText() {
        return text;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    public String getAuthorInitials() {
        return authorInitials;
    }

    public void setAuthorInitials(String authorInitials) {
        this.authorInitials = authorInitials;
    }

    public boolean isAuthorAvatarPresent() {
        return authorAvatarPresent;
    }

    public void setAuthorAvatarPresent(boolean authorAvatarPresent) {
        this.authorAvatarPresent = authorAvatarPresent;
    }

    public boolean isCurrentUserAuthor() {
        return currentUserAuthor;
    }

    public void setCurrentUserAuthor(boolean currentUserAuthor) {
        this.currentUserAuthor = currentUserAuthor;
    }

    public String getReplyToDisplayName() {
        return replyToDisplayName;
    }

    public void setReplyToDisplayName(String replyToDisplayName) {
        this.replyToDisplayName = replyToDisplayName;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }
}
