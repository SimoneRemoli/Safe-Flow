<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.web.routex.bean.MessageBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<MessageBean> notifiche = (List<MessageBean>) request.getAttribute("notifiche");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Internal Notifications</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
            --accent: #6ff7ff;
        }
        * { box-sizing: border-box; }
        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 15% 22%, rgba(111, 247, 255, 0.16), transparent 24%),
                radial-gradient(circle at 85% 18%, rgba(83, 169, 255, 0.18), transparent 22%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
        }
        .shell {
            width: min(1080px, calc(100% - 32px));
            margin: 24px auto;
            padding: 28px;
            border-radius: 30px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.9));
        }
        .topbar {
            display: flex;
            justify-content: space-between;
            gap: 16px;
            align-items: center;
            flex-wrap: wrap;
        }
        .eyebrow {
            display: inline-flex;
            padding: 8px 14px;
            border-radius: 999px;
            color: var(--accent);
            border: 1px solid rgba(111, 247, 255, 0.2);
            background: rgba(111, 247, 255, 0.08);
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }
        h1 {
            margin: 14px 0 8px;
            font-size: clamp(2.2rem, 4vw, 3.6rem);
        }
        .subtitle {
            margin: 0;
            color: var(--muted);
            line-height: 1.7;
            max-width: 720px;
        }
        .nav-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        .nav-actions a {
            text-decoration: none;
            color: var(--text);
            padding: 12px 18px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.04em;
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
        }
        .list {
            margin-top: 26px;
            display: grid;
            gap: 14px;
        }
        .item {
            padding: 18px 20px;
            border-radius: 22px;
            background: rgba(246, 250, 253, 0.96);
            border: 1px solid rgba(210, 222, 232, 0.7);
        }
        .meta {
            color: #586673;
            font-size: 0.92rem;
            margin-bottom: 8px;
        }
        .message {
            color: #2f3943;
            line-height: 1.7;
            font-weight: 600;
        }

        .item.read .message {
            color: #3b4650;
            font-weight: 400;
        }
        .empty-state {
            margin-top: 26px;
            padding: 40px 24px;
            text-align: center;
            color: var(--muted);
            border-radius: 22px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<div class="shell">
    <div class="topbar">
        <div>
            <span class="eyebrow">Private updates</span>
            <h1>Internal notifications</h1>
            <p class="subtitle">This area contains private RouteX updates related to your own reports and account activity. These notifications do not appear in the public system alert feed.</p>
        </div>
        <div class="nav-actions">
            <a href="travelerHome">Home</a>
            <a href="viewNotifications">System alerts</a>
            <a href="travelerReport">Send report</a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <% if (notifiche == null || notifiche.isEmpty()) { %>
    <div class="empty-state">No internal notifications available.</div>
    <% } else { %>
    <div class="list">
        <% for (MessageBean m : notifiche) { %>
        <div class="item <%= Boolean.TRUE.equals(m.getLetto()) ? "read" : "unread" %>">
            <div class="meta">RouteX Admin Team · <%= sdf.format(m.getDate()) %></div>
            <div class="message"><%= m.getMessage() %></div>
        </div>
        <% } %>
    </div>
    <% } %>
</div>
</body>
</html>
