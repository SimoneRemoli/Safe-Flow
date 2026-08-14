package it.web.safeflow.model;

import java.sql.Timestamp;

public class PrivateChatThread {

    private final String notificationKey;
    private final String otherTravelerCf;
    private final String reportText;
    private final String city;
    private final String lastMessage;
    private final Timestamp lastMessageAt;
    private final int unreadCount;
    private boolean unread;
    private String otherTravelerDisplayName;
    private String otherTravelerInitials;

    public PrivateChatThread(String notificationKey,
                             String otherTravelerCf,
                             String reportText,
                             String city,
                             String lastMessage,
                             Timestamp lastMessageAt,
                             int unreadCount) {
        this.notificationKey = notificationKey;
        this.otherTravelerCf = otherTravelerCf;
        this.reportText = reportText;
        this.city = city;
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
        this.unreadCount = unreadCount;
        this.unread = unreadCount > 0;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public String getOtherTravelerCf() {
        return otherTravelerCf;
    }

    public String getReportText() {
        return reportText;
    }

    public String getCity() {
        return city;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Timestamp getLastMessageAt() {
        return lastMessageAt;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public String getOtherTravelerDisplayName() {
        return otherTravelerDisplayName;
    }

    public void setOtherTravelerDisplayName(String otherTravelerDisplayName) {
        this.otherTravelerDisplayName = otherTravelerDisplayName;
    }

    public String getOtherTravelerInitials() {
        return otherTravelerInitials;
    }

    public void setOtherTravelerInitials(String otherTravelerInitials) {
        this.otherTravelerInitials = otherTravelerInitials;
    }
}
