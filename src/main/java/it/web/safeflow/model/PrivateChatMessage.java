package it.web.safeflow.model;

import java.sql.Timestamp;

public class PrivateChatMessage {

    private final long id;
    private final String notificationKey;
    private final String senderCf;
    private final String recipientCf;
    private final String text;
    private final Timestamp createdAt;
    private final boolean currentUserSender;
    private String senderDisplayName;
    private String senderInitials;

    public PrivateChatMessage(long id,
                              String notificationKey,
                              String senderCf,
                              String recipientCf,
                              String text,
                              Timestamp createdAt,
                              boolean currentUserSender) {
        this.id = id;
        this.notificationKey = notificationKey;
        this.senderCf = senderCf;
        this.recipientCf = recipientCf;
        this.text = text;
        this.createdAt = createdAt;
        this.currentUserSender = currentUserSender;
    }

    public long getId() {
        return id;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public String getSenderCf() {
        return senderCf;
    }

    public String getRecipientCf() {
        return recipientCf;
    }

    public String getText() {
        return text;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public boolean isCurrentUserSender() {
        return currentUserSender;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    public void setSenderDisplayName(String senderDisplayName) {
        this.senderDisplayName = senderDisplayName;
    }

    public String getSenderInitials() {
        return senderInitials;
    }

    public void setSenderInitials(String senderInitials) {
        this.senderInitials = senderInitials;
    }
}
