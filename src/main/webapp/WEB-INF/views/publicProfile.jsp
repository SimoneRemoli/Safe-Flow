<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="it.web.routex.model.UserProfileSummary" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%
    UserProfileSummary profile = (UserProfileSummary) request.getAttribute("publicProfile");
    String encodedCf = profile == null ? "" : URLEncoder.encode(profile.getCodiceFiscale(), StandardCharsets.UTF_8);
    String displayName = profile == null ? "Unknown user" : StringEscapeUtils.escapeHtml4(profile.getDisplayName());
    String role = profile == null ? "" : StringEscapeUtils.escapeHtml4(profile.getRole());
    String bio = profile == null ? "" : StringEscapeUtils.escapeHtml4(profile.getBio());
    String initials = profile == null ? "SF" : StringEscapeUtils.escapeHtml4(profile.getInitials());
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - User Profile</title>
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        body {
            margin: 0;
            min-height: 100vh;
            background: #f3f6f4;
            color: #14241d;
            font-family: "Inter", "Segoe UI", Arial, sans-serif;
        }

        .public-profile-shell {
            width: min(760px, calc(100% - 32px));
            margin: 78px auto 28px;
            background: #ffffff;
            border: 1px solid #d8e4de;
            border-radius: 16px;
            box-shadow: 0 18px 42px rgba(20, 36, 29, 0.08);
            overflow: hidden;
        }

        .public-profile-header {
            display: flex;
            gap: 18px;
            align-items: center;
            padding: 26px;
            border-bottom: 1px solid #d8e4de;
            background: linear-gradient(180deg, #ffffff, #fbfdfc);
        }

        .public-profile-avatar {
            width: 96px;
            height: 96px;
            flex: 0 0 96px;
            border-radius: 50%;
            overflow: hidden;
            display: grid;
            place-items: center;
            color: #ffffff;
            background: #0e7c66;
            border: 4px solid #e5f3ee;
            font-size: 1.35rem;
            font-weight: 850;
        }

        .public-profile-avatar img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .public-profile-title h1 {
            margin: 0 0 8px;
            font-size: 2rem;
            line-height: 1.08;
        }

        .role-pill {
            display: inline-flex;
            padding: 7px 10px;
            border-radius: 999px;
            background: #e5f3ee;
            color: #075f4e;
            border: 1px solid #c8e2d8;
            font-size: 0.75rem;
            font-weight: 850;
            text-transform: uppercase;
        }

        .public-profile-body {
            padding: 26px;
        }

        .public-profile-body h2 {
            margin: 0 0 10px;
            font-size: 1rem;
            text-transform: uppercase;
            color: #607267;
        }

        .public-profile-body p {
            margin: 0;
            color: #31443a;
            line-height: 1.65;
        }

        .public-profile-actions {
            padding: 0 26px 26px;
        }

        .public-profile-actions a {
            display: inline-flex;
            text-decoration: none;
            color: #31443a;
            background: #f7faf8;
            border: 1px solid #d8e4de;
            border-radius: 8px;
            padding: 11px 15px;
            font-weight: 800;
        }

        @media (max-width: 640px) {
            .public-profile-header {
                align-items: flex-start;
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
<%@ include file="/header.jspf" %>
<main class="public-profile-shell">
    <section class="public-profile-header">
        <div class="public-profile-avatar">
            <% if (profile != null && profile.isAvatarPresent()) { %>
            <img src="publicProfileAvatar?cf=<%= encodedCf %>&amp;t=<%= System.currentTimeMillis() %>" alt="">
            <% } else { %>
            <span><%= initials %></span>
            <% } %>
        </div>
        <div class="public-profile-title">
            <h1><%= displayName %></h1>
            <span class="role-pill"><%= role %></span>
        </div>
    </section>
    <section class="public-profile-body">
        <h2>Bio</h2>
        <p><%= bio.isBlank() ? "No bio available." : bio %></p>
    </section>
    <div class="public-profile-actions">
        <a href="viewNotifications">Back to notifications</a>
    </div>
</main>
</body>
</html>
