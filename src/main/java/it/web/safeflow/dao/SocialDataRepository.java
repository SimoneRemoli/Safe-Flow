package it.web.safeflow.dao;

import it.web.safeflow.exception.DAOExceptionRemoli;
import it.web.safeflow.model.NotificationComment;
import it.web.safeflow.model.NotificationLikeState;
import it.web.safeflow.model.ReportImageAttachment;
import it.web.safeflow.utility.factory.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SocialDataRepository {

    public record StoredFile(String fileName, String contentType, byte[] data) {
    }

    public record StoredProfile(String bio, String avatarContentType, byte[] avatarData) {
        public boolean hasAvatar() {
            return avatarData != null && avatarData.length > 0;
        }
    }

    public NotificationLikeState toggleReportLike(String notificationKey, String codiceFiscale) throws DAOExceptionRemoli {
        return toggleLike(
                "sf_notification_likes",
                "notification_key",
                notificationKey,
                codiceFiscale
        );
    }

    public Map<String, NotificationLikeState> reportLikeStates(Set<String> notificationKeys, String codiceFiscale)
            throws DAOExceptionRemoli {
        return likeStates("sf_notification_likes", "notification_key", notificationKeys, codiceFiscale);
    }

    public boolean isReportLikeNotificationSent(String notificationKey, String likerCf) throws DAOExceptionRemoli {
        return existsNotificationMarker("REPORT_LIKE", notificationKey, likerCf);
    }

    public void markReportLikeNotificationSent(String notificationKey, String likerCf) throws DAOExceptionRemoli {
        saveNotificationMarker("REPORT_LIKE", notificationKey, likerCf);
    }

    public NotificationLikeState toggleCommentLike(String commentId, String codiceFiscale) throws DAOExceptionRemoli {
        return toggleLike("sf_comment_likes", "comment_id", commentId, codiceFiscale);
    }

    public Map<String, NotificationLikeState> commentLikeStates(Set<String> commentIds, String codiceFiscale)
            throws DAOExceptionRemoli {
        return likeStates("sf_comment_likes", "comment_id", commentIds, codiceFiscale);
    }

    public boolean isCommentLikeNotificationSent(String commentId, String likerCf) throws DAOExceptionRemoli {
        return existsNotificationMarker("COMMENT_LIKE", commentId, likerCf);
    }

    public void markCommentLikeNotificationSent(String commentId, String likerCf) throws DAOExceptionRemoli {
        saveNotificationMarker("COMMENT_LIKE", commentId, likerCf);
    }

    public void saveComment(NotificationComment comment) throws DAOExceptionRemoli {
        String sql = """
                INSERT INTO sf_notification_comments
                    (id, notification_key, author_cf, parent_comment_id, reply_to_cf, text, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, comment.getId());
            ps.setString(2, comment.getNotificationKey());
            ps.setString(3, normalizeCf(comment.getAuthorCf()));
            ps.setString(4, blankToNull(comment.getParentCommentId()));
            ps.setString(5, normalizeNullableCf(comment.getReplyToCf()));
            ps.setString(6, comment.getText());
            ps.setTimestamp(7, comment.getCreatedAt());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to save the report comment in the database.", e);
        }
    }

    public List<NotificationComment> commentsFor(String notificationKey) throws DAOExceptionRemoli {
        String sql = """
                SELECT id, notification_key, author_cf, parent_comment_id, reply_to_cf, text, created_at
                FROM sf_notification_comments
                WHERE notification_key = ?
                ORDER BY created_at ASC
                """;
        List<NotificationComment> comments = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notificationKey);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comments.add(new NotificationComment(
                            rs.getString("id"),
                            rs.getString("notification_key"),
                            rs.getString("author_cf"),
                            rs.getString("parent_comment_id"),
                            rs.getString("reply_to_cf"),
                            rs.getString("text"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
            return comments;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to load report comments from the database.", e);
        }
    }

    public void saveInternalNotificationTarget(String internalNotificationKey, String reportNotificationKey, String commentId)
            throws DAOExceptionRemoli {
        String sql = """
                INSERT INTO sf_internal_notification_targets
                    (internal_notification_key, report_notification_key, comment_id)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    report_notification_key = VALUES(report_notification_key),
                    comment_id = VALUES(comment_id)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, internalNotificationKey);
            ps.setString(2, reportNotificationKey);
            ps.setString(3, blankToNull(commentId));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to save the internal notification target in the database.", e);
        }
    }

    public Optional<String> internalNotificationTarget(String internalNotificationKey) throws DAOExceptionRemoli {
        String sql = """
                SELECT report_notification_key, comment_id
                FROM sf_internal_notification_targets
                WHERE internal_notification_key = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, internalNotificationKey);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                String reportKey = rs.getString("report_notification_key");
                String commentId = rs.getString("comment_id");
                return Optional.of(reportKey + "|" + (commentId == null ? "" : commentId));
            }
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to load the internal notification target from the database.", e);
        }
    }

    public void dismissNotification(String type, String travelerCf, String notificationKey) throws DAOExceptionRemoli {
        String sql = """
                INSERT IGNORE INTO sf_notification_dismissals
                    (notification_type, traveler_cf, notification_key)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setString(2, normalizeCf(travelerCf));
            ps.setString(3, notificationKey);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to save removed notification in the database.", e);
        }
    }

    public Set<String> dismissedNotificationKeys(String type, String travelerCf) throws DAOExceptionRemoli {
        String sql = """
                SELECT notification_key
                FROM sf_notification_dismissals
                WHERE notification_type = ? AND traveler_cf = ?
                """;
        Set<String> keys = new java.util.HashSet<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setString(2, normalizeCf(travelerCf));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    keys.add(rs.getString("notification_key"));
                }
            }
            return keys;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to load removed notifications from the database.", e);
        }
    }

    public void saveProfile(String cf, String bio, StoredFile avatar) throws DAOExceptionRemoli {
        String sql = """
                INSERT INTO sf_user_profiles
                    (codice_fiscale, bio, avatar_content_type, avatar_data)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    bio = VALUES(bio),
                    avatar_content_type = COALESCE(VALUES(avatar_content_type), avatar_content_type),
                    avatar_data = COALESCE(VALUES(avatar_data), avatar_data)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, normalizeCf(cf));
            ps.setString(2, bio == null ? "" : bio);
            if (avatar == null) {
                ps.setNull(3, java.sql.Types.VARCHAR);
                ps.setNull(4, java.sql.Types.BLOB);
            } else {
                ps.setString(3, avatar.contentType());
                ps.setBytes(4, avatar.data());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to save the user profile in the database.", e);
        }
    }

    public void removeAvatar(String cf) throws DAOExceptionRemoli {
        String sql = """
                INSERT INTO sf_user_profiles (codice_fiscale, bio, avatar_content_type, avatar_data)
                VALUES (?, '', NULL, NULL)
                ON DUPLICATE KEY UPDATE avatar_content_type = NULL, avatar_data = NULL
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, normalizeCf(cf));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to remove the user avatar from the database.", e);
        }
    }

    public Optional<StoredProfile> profile(String cf) throws DAOExceptionRemoli {
        String sql = """
                SELECT bio, avatar_content_type, avatar_data
                FROM sf_user_profiles
                WHERE codice_fiscale = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, normalizeCf(cf));
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new StoredProfile(
                        rs.getString("bio"),
                        rs.getString("avatar_content_type"),
                        rs.getBytes("avatar_data")
                ));
            }
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to load the user profile from the database.", e);
        }
    }

    public void saveReportImages(String notificationKey, List<StoredFile> images) throws DAOExceptionRemoli {
        String deleteSql = "DELETE FROM sf_report_images WHERE notification_key = ?";
        String insertSql = """
                INSERT INTO sf_report_images (notification_key, file_name, content_type, image_data)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement delete = conn.prepareStatement(deleteSql);
                 PreparedStatement insert = conn.prepareStatement(insertSql)) {
                delete.setString(1, notificationKey);
                delete.executeUpdate();
                for (StoredFile image : images) {
                    insert.setString(1, notificationKey);
                    insert.setString(2, image.fileName());
                    insert.setString(3, image.contentType());
                    insert.setBytes(4, image.data());
                    insert.addBatch();
                }
                insert.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to save report images in the database.", e);
        }
    }

    public List<ReportImageAttachment> reportImages(String notificationKey) throws DAOExceptionRemoli {
        String sql = """
                SELECT file_name, content_type
                FROM sf_report_images
                WHERE notification_key = ?
                ORDER BY file_name ASC
                """;
        List<ReportImageAttachment> images = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notificationKey);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    images.add(new ReportImageAttachment(
                            rs.getString("file_name"),
                            rs.getString("content_type")
                    ));
                }
            }
            return images;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to load report images from the database.", e);
        }
    }

    public Optional<StoredFile> reportImage(String notificationKey, String fileName) throws DAOExceptionRemoli {
        String sql = """
                SELECT file_name, content_type, image_data
                FROM sf_report_images
                WHERE notification_key = ? AND file_name = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notificationKey);
            ps.setString(2, fileName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new StoredFile(
                        rs.getString("file_name"),
                        rs.getString("content_type"),
                        rs.getBytes("image_data")
                ));
            }
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to load the report image from the database.", e);
        }
    }

    public List<String> reportImageNotificationKeys() throws DAOExceptionRemoli {
        String sql = "SELECT DISTINCT notification_key FROM sf_report_images";
        List<String> keys = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                keys.add(rs.getString("notification_key"));
            }
            return keys;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to load report image keys from the database.", e);
        }
    }

    private NotificationLikeState toggleLike(String table, String keyColumn, String itemKey, String codiceFiscale)
            throws DAOExceptionRemoli {
        String cf = normalizeCf(codiceFiscale);
        String existsSql = "SELECT 1 FROM " + table + " WHERE " + keyColumn + " = ? AND traveler_cf = ?";
        String insertSql = "INSERT INTO " + table + " (" + keyColumn + ", traveler_cf) VALUES (?, ?)";
        String deleteSql = "DELETE FROM " + table + " WHERE " + keyColumn + " = ? AND traveler_cf = ?";
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                boolean liked;
                try (PreparedStatement exists = conn.prepareStatement(existsSql)) {
                    exists.setString(1, itemKey);
                    exists.setString(2, cf);
                    try (ResultSet rs = exists.executeQuery()) {
                        liked = !rs.next();
                    }
                }
                try (PreparedStatement change = conn.prepareStatement(liked ? insertSql : deleteSql)) {
                    change.setString(1, itemKey);
                    change.setString(2, cf);
                    change.executeUpdate();
                }
                int count = countLikes(conn, table, keyColumn, itemKey);
                conn.commit();
                return new NotificationLikeState(count, liked);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to update likes in the database.", e);
        }
    }

    private Map<String, NotificationLikeState> likeStates(String table,
                                                          String keyColumn,
                                                          Set<String> itemKeys,
                                                          String codiceFiscale) throws DAOExceptionRemoli {
        Map<String, NotificationLikeState> states = new HashMap<>();
        if (itemKeys == null || itemKeys.isEmpty()) {
            return states;
        }
        String cf = codiceFiscale == null || codiceFiscale.isBlank()
                ? ""
                : normalizeCf(codiceFiscale);
        String countSql = "SELECT COUNT(*) FROM " + table + " WHERE " + keyColumn + " = ?";
        String likedSql = "SELECT 1 FROM " + table + " WHERE " + keyColumn + " = ? AND traveler_cf = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement count = conn.prepareStatement(countSql);
             PreparedStatement liked = conn.prepareStatement(likedSql)) {
            for (String itemKey : itemKeys) {
                if (itemKey == null || itemKey.isBlank()) {
                    continue;
                }
                count.setString(1, itemKey);
                int likeCount;
                try (ResultSet rs = count.executeQuery()) {
                    likeCount = rs.next() ? rs.getInt(1) : 0;
                }
                boolean likedByCurrentUser = false;
                if (!cf.isBlank()) {
                    liked.setString(1, itemKey);
                    liked.setString(2, cf);
                    try (ResultSet rs = liked.executeQuery()) {
                        likedByCurrentUser = rs.next();
                    }
                }
                states.put(itemKey, new NotificationLikeState(likeCount, likedByCurrentUser));
            }
            return states;
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to load likes from the database.", e);
        }
    }

    private int countLikes(Connection conn, String table, String keyColumn, String itemKey) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM " + table + " WHERE " + keyColumn + " = ?")) {
            ps.setString(1, itemKey);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private boolean existsNotificationMarker(String type, String itemKey, String actorCf) throws DAOExceptionRemoli {
        String sql = """
                SELECT 1
                FROM sf_like_notification_markers
                WHERE marker_type = ? AND item_key = ? AND actor_cf = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setString(2, itemKey);
            ps.setString(3, normalizeCf(actorCf));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to read notification markers from the database.", e);
        }
    }

    private void saveNotificationMarker(String type, String itemKey, String actorCf) throws DAOExceptionRemoli {
        String sql = """
                INSERT IGNORE INTO sf_like_notification_markers (marker_type, item_key, actor_cf)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ps.setString(2, itemKey);
            ps.setString(3, normalizeCf(actorCf));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOExceptionRemoli("Unable to save notification markers in the database.", e);
        }
    }

    private String normalizeCf(String codiceFiscale) {
        return codiceFiscale == null ? "" : codiceFiscale.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeNullableCf(String codiceFiscale) {
        String cf = normalizeCf(codiceFiscale);
        return cf.isBlank() ? null : cf;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
