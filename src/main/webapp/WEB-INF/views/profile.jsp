<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="it.web.routex.model.UserProfile" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%
    UserProfile profile = (UserProfile) request.getAttribute("profile");
    String nome = session != null && session.getAttribute("nome") != null ? session.getAttribute("nome").toString() : "";
    String cognome = session != null && session.getAttribute("cognome") != null ? session.getAttribute("cognome").toString() : "";
    String ruolo = session != null && session.getAttribute("ruolo") != null ? session.getAttribute("ruolo").toString() : "";
    String bio = profile != null && profile.getBio() != null ? StringEscapeUtils.escapeHtml4(profile.getBio()) : "";
    boolean hasAvatar = profile != null && profile.isAvatarPresent();
    boolean saved = "1".equals(request.getParameter("saved"));
    String errore = (String) request.getAttribute("errore");
    String homeTarget = "ADMIN".equalsIgnoreCase(ruolo) ? "adminHub" : "travelerHome";
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Profile</title>
    <style>
        body {
            margin: 0;
            min-height: 100vh;
            background: #f3f6f4;
            color: #14241d;
            font-family: "Inter", "Segoe UI", Arial, sans-serif;
        }

        .profile-shell {
            width: min(980px, calc(100% - 32px));
            margin: 72px auto 24px;
            display: grid;
            grid-template-columns: 300px minmax(0, 1fr);
            gap: 18px;
        }

        .profile-card,
        .profile-form {
            background: #ffffff;
            border: 1px solid #d8e4de;
            border-radius: 16px;
            box-shadow: 0 18px 42px rgba(20, 36, 29, 0.08);
            padding: 24px;
        }

        .profile-card {
            text-align: center;
            height: fit-content;
        }

        .profile-avatar-large {
            width: 128px;
            height: 128px;
            margin: 0 auto 18px;
            border-radius: 50%;
            overflow: hidden;
            display: grid;
            place-items: center;
            color: #ffffff;
            background: #0e7c66;
            border: 4px solid #e5f3ee;
            font-size: 2rem;
            font-weight: 850;
        }

        .profile-avatar-large img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        h1 {
            margin: 0 0 8px;
            font-size: 1.9rem;
            line-height: 1.1;
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

        .profile-form h2 {
            margin: 0 0 8px;
            font-size: 1.35rem;
        }

        .profile-form p {
            margin: 0 0 20px;
            color: #607267;
            line-height: 1.6;
        }

        label {
            display: block;
            margin: 16px 0 8px;
            color: #405147;
            font-weight: 800;
        }

        textarea,
        input[type="file"] {
            width: 100%;
            border: 1px solid #d8e4de;
            border-radius: 12px;
            background: #ffffff;
            color: #14241d;
            padding: 12px;
            font: inherit;
        }

        textarea {
            min-height: 160px;
            resize: vertical;
        }

        .profile-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            margin-top: 18px;
        }

        .profile-actions button,
        .profile-actions a {
            border-radius: 8px;
            padding: 11px 15px;
            font-weight: 800;
            text-decoration: none;
            border: 1px solid #d8e4de;
            cursor: pointer;
        }

        .profile-actions button {
            color: #ffffff;
            background: #0e7c66;
            border-color: #0e7c66;
        }

        .profile-actions a {
            color: #31443a;
            background: #f7faf8;
        }

        .profile-alert {
            margin-bottom: 16px;
            padding: 12px 14px;
            border-radius: 12px;
            font-weight: 750;
        }

        .profile-alert.success {
            color: #147a4b;
            background: #e8f7ef;
            border: 1px solid #bfe8cf;
        }

        .profile-alert.error {
            color: #b42318;
            background: #fff0ee;
            border: 1px solid #fac7c2;
        }

        @media (max-width: 760px) {
            .profile-shell {
                grid-template-columns: 1fr;
                margin-top: 82px;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<main class="profile-shell">
    <aside class="profile-card">
        <div class="profile-avatar-large">
            <% if (hasAvatar) { %>
            <img src="profileAvatar?t=<%= System.currentTimeMillis() %>" alt="">
            <% } else { %>
            <span><%= (!nome.isBlank() ? nome.substring(0, 1).toUpperCase() : "S") %><%= (!cognome.isBlank() ? cognome.substring(0, 1).toUpperCase() : "F") %></span>
            <% } %>
        </div>
        <h1><%= nome %> <%= cognome %></h1>
        <span class="role-pill"><%= ruolo %></span>
    </aside>

    <section class="profile-form">
        <% if (saved) { %>
        <div class="profile-alert success">Profilo aggiornato correttamente.</div>
        <% } %>
        <% if (errore != null && !errore.isBlank()) { %>
        <div class="profile-alert error"><%= errore %></div>
        <% } %>

        <h2>Profilo personale</h2>
        <p>Carica una foto profilo e scrivi una breve bio visibile nella tua area personale.</p>

        <form action="profile" method="post" enctype="multipart/form-data">
            <label for="avatar">Foto profilo</label>
            <input id="avatar" name="avatar" type="file" accept="image/png,image/jpeg,image/webp,image/gif">

            <label for="bio">Bio</label>
            <textarea id="bio" name="bio" maxlength="500" placeholder="Scrivi una breve descrizione..."><%= bio %></textarea>

            <div class="profile-actions">
                <button type="submit">Save profile</button>
                <a href="<%= homeTarget %>">Back to home</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
