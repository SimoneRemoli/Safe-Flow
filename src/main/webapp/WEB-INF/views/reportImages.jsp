<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="it.web.routex.model.ReportImageAttachment" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%
    List<ReportImageAttachment> images = (List<ReportImageAttachment>) request.getAttribute("images");
    String notificationKey = request.getAttribute("notificationKey") == null ? "" : request.getAttribute("notificationKey").toString();
    String encodedNotificationKey = URLEncoder.encode(notificationKey, StandardCharsets.UTF_8);
    String reportMessage = request.getAttribute("reportMessage") == null ? "" : StringEscapeUtils.escapeHtml4(request.getAttribute("reportMessage").toString());
    String role = session != null && session.getAttribute("ruolo") != null ? session.getAttribute("ruolo").toString() : "";
    String backTarget = "ADMIN".equalsIgnoreCase(role) ? "reviewTravelerCommunications" : "viewNotifications";
    String galleryModeClass = "ADMIN".equalsIgnoreCase(role) ? " admin-gallery" : "";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Report Images</title>
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        body {
            margin: 0;
            min-height: 100vh;
            background: #f3f6f4;
            color: #14241d;
            font-family: "Inter", "Segoe UI", Arial, sans-serif;
        }

        .gallery-shell {
            width: min(1180px, calc(100% - 32px));
            margin: 76px auto 28px;
        }

        .gallery-header {
            padding: 24px;
            border-radius: 16px;
            background: #ffffff;
            border: 1px solid #d8e4de;
            box-shadow: 0 18px 42px rgba(20, 36, 29, 0.08);
        }

        .gallery-header h1 {
            margin: 0 0 10px;
            font-size: clamp(1.8rem, 3vw, 2.5rem);
            line-height: 1.08;
        }

        .gallery-header p {
            margin: 0;
            color: #405147;
            line-height: 1.6;
        }

        .gallery-actions {
            margin-top: 16px;
        }

        .gallery-actions a {
            display: inline-flex;
            text-decoration: none;
            color: #31443a;
            background: #f7faf8;
            border: 1px solid #d8e4de;
            border-radius: 8px;
            padding: 11px 15px;
            font-weight: 800;
        }

        .image-grid {
            margin-top: 18px;
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(220px, 260px));
            gap: 16px;
            justify-content: start;
        }

        .image-frame {
            overflow: hidden;
            width: 100%;
            max-width: 260px;
            border-radius: 14px;
            background: #ffffff;
            border: 1px solid #d8e4de;
            box-shadow: 0 14px 32px rgba(20, 36, 29, 0.08);
        }

        .image-frame img {
            display: block;
            width: 100%;
            height: 180px;
            object-fit: cover;
            background: #e8efeb;
        }

        .admin-gallery .image-grid {
            grid-template-columns: repeat(auto-fill, minmax(160px, 200px));
            gap: 14px;
        }

        .admin-gallery .image-frame {
            max-width: 200px;
            border-radius: 10px;
            box-shadow: 0 10px 24px rgba(20, 36, 29, 0.06);
        }

        .admin-gallery .image-frame img {
            height: 140px;
        }

        .empty-state {
            margin-top: 18px;
            padding: 34px;
            text-align: center;
            color: #607267;
            border-radius: 14px;
            background: #ffffff;
            border: 1px solid #d8e4de;
        }
    </style>
</head>
<body>
<%@ include file="/header.jspf" %>
<main class="gallery-shell<%= galleryModeClass %>">
    <section class="gallery-header">
        <h1>Report images</h1>
        <p><%= reportMessage.isBlank() ? "Attached images for this report." : reportMessage %></p>
        <div class="gallery-actions">
            <a href="<%= backTarget %>">Back</a>
        </div>
    </section>

    <% if (images == null || images.isEmpty()) { %>
    <div class="empty-state">No images are attached to this report.</div>
    <% } else { %>
    <section class="image-grid" aria-label="Attached report images">
        <% for (ReportImageAttachment image : images) {
            String encodedFileName = URLEncoder.encode(image.getFileName(), StandardCharsets.UTF_8);
        %>
        <a class="image-frame" href="reportImage?notificationKey=<%= encodedNotificationKey %>&amp;file=<%= encodedFileName %>" target="_blank" rel="noopener">
            <img src="reportImage?notificationKey=<%= encodedNotificationKey %>&amp;file=<%= encodedFileName %>" alt="Attached report image">
        </a>
        <% } %>
    </section>
    <% } %>
</main>
</body>
</html>
