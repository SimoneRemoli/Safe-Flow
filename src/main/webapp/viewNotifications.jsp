<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.web.routex.bean.MessageBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<MessageBean> notifiche = (List<MessageBean>) request.getAttribute("notifiche");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Notifiche Worker</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.84);
            --panel-soft: rgba(255, 255, 255, 0.05);
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
            --accent: #6ff7ff;
            --accent-2: #8dd8ff;
            --success: #89ffd1;
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
            width: min(1220px, calc(100% - 32px));
            margin: 24px auto;
            padding: 28px;
            border-radius: 30px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.9));
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.38);
            backdrop-filter: blur(16px);
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
            max-width: 760px;
        }

        .nav-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .nav-actions a,
        .save-button {
            text-decoration: none;
            border: none;
            cursor: pointer;
            color: var(--text);
            padding: 12px 18px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.04em;
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
            transition: transform 0.25s ease, border-color 0.25s ease;
        }

        .save-button {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            box-shadow: 0 18px 32px rgba(111, 247, 255, 0.2);
        }

        .nav-actions a:hover,
        .save-button:hover {
            transform: translateY(-2px);
            border-color: rgba(111, 247, 255, 0.4);
        }

        .table-panel {
            margin-top: 26px;
            border-radius: 26px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
            overflow: hidden;
        }

        .table-wrap {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 18px 20px;
            text-align: left;
            border-bottom: 1px solid rgba(255, 255, 255, 0.08);
        }

        th {
            color: var(--accent);
            font-size: 0.85rem;
            letter-spacing: 0.08em;
            text-transform: uppercase;
            background: rgba(111, 247, 255, 0.06);
        }

        td {
            color: var(--text);
            vertical-align: top;
        }

        tr:hover td {
            background: rgba(255, 255, 255, 0.03);
        }

        .message-cell {
            color: #e8f7ff;
            line-height: 1.65;
        }

        .date-cell {
            color: var(--muted);
            white-space: nowrap;
        }

        .check-cell {
            text-align: center;
        }

        input[type="checkbox"] {
            width: 20px;
            height: 20px;
            accent-color: #89ffd1;
            cursor: pointer;
        }

        .empty-state {
            padding: 46px 24px;
            text-align: center;
            color: var(--muted);
        }

        .footer-actions {
            margin-top: 20px;
            display: flex;
            justify-content: flex-end;
        }

        @media (max-width: 768px) {
            .shell {
                width: min(100% - 20px, 100%);
                margin: 10px auto;
                padding: 18px;
                border-radius: 22px;
            }

            th, td {
                padding: 14px 12px;
            }
        }
    </style>
</head>
<body>
<div class="shell">
    <div class="topbar">
        <div>
            <span class="eyebrow">Alert management</span>
            <h1>Notifiche di sistema</h1>
            <p class="subtitle">
                Rivedi i messaggi operativi ricevuti e marca quelli gia' gestiti senza uscire dal nuovo layout worker.
            </p>
        </div>

        <div class="nav-actions">
            <a href="dashboardWorker.jsp">Home</a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <form action="updateNotifications" method="post">
        <div class="table-panel">
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th style="width: 55%">Messaggio</th>
                        <th style="width: 25%">Data</th>
                        <th style="width: 20%; text-align: center;">Risolta</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (notifiche == null || notifiche.isEmpty()) { %>
                        <tr>
                            <td colspan="3" class="empty-state">Nessuna notifica disponibile.</td>
                        </tr>
                    <% } else {
                        for (MessageBean m : notifiche) {
                    %>
                        <tr>
                            <td class="message-cell"><%= m.getMessage() %></td>
                            <td class="date-cell"><%= sdf.format(m.getDate()) %></td>
                            <td class="check-cell">
                                <input type="checkbox" name="risolte"
                                       value="<%= m.getDate().getTime() + "|" + m.getMessage() %>">
                            </td>
                        </tr>
                    <%  }
                    } %>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="footer-actions">
            <button type="submit" class="save-button">Salva modifiche</button>
        </div>
    </form>
</div>
</body>
</html>
