<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.web.routex.bean.ReportsStatsBean"%>
<%@ page import="it.web.routex.bean.PathInfoBean"%>
<%@ page import="java.util.List"%>
<%
    ReportsStatsBean stats = (ReportsStatsBean) request.getAttribute("stats");
    List<PathInfoBean> pathList = stats.getPaths();
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Reports & Statistics</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
            --accent: #6ff7ff;
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
            width: min(1320px, calc(100% - 32px));
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
            font-size: clamp(2.2rem, 4vw, 3.8rem);
        }

        .subtitle {
            margin: 0;
            color: var(--muted);
            line-height: 1.75;
            max-width: 860px;
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
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
            transition: transform 0.25s ease, border-color 0.25s ease;
        }

        .nav-actions a:hover {
            transform: translateY(-2px);
            border-color: rgba(111, 247, 255, 0.4);
        }

        .stats-container {
            margin-top: 24px;
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 18px;
        }

        .stat-card {
            padding: 22px;
            border-radius: 26px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .stat-card i {
            font-size: 1.7rem;
            color: var(--accent);
            margin-bottom: 12px;
        }

        .stat-card h3 {
            margin: 0 0 10px;
        }

        .stat-card p {
            margin: 0;
            font-size: 1.8rem;
            font-weight: 700;
        }

        .table-panel {
            margin-top: 24px;
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
            padding: 16px 18px;
            text-align: left;
            border-bottom: 1px solid rgba(255, 255, 255, 0.08);
        }

        th {
            color: var(--accent);
            font-size: 0.84rem;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            background: rgba(111, 247, 255, 0.06);
            white-space: nowrap;
        }

        td {
            color: #e8f7ff;
        }

        tr:hover td {
            background: rgba(255, 255, 255, 0.03);
        }

        @media (max-width: 960px) {
            .stats-container {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 768px) {
            .shell {
                width: min(100% - 20px, 100%);
                margin: 10px auto;
                padding: 18px;
                border-radius: 22px;
            }
        }
    </style>
</head>
<body>
<div class="shell">
    <div class="topbar">
        <div>
            <span class="eyebrow">Analytics center</span>
            <h1>Reports & statistics</h1>
            <p class="subtitle">
                Vista amministrativa aggregata dei percorsi RouteX con metriche di utilizzo e dettaglio completo dei viaggi registrati.
            </p>
        </div>

        <div class="nav-actions">
            <a href="indexAdmin.jsp">Home</a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <div class="stats-container">
        <div class="stat-card">
            <i class="fas fa-route"></i>
            <h3>Total trips</h3>
            <p><%= stats.getTotalTrips() %></p>
        </div>

        <div class="stat-card">
            <i class="fas fa-road"></i>
            <h3>Total distance</h3>
            <p><%= String.format("%.2f", stats.getTotalDistance()) %> km</p>
        </div>

        <div class="stat-card">
            <i class="fas fa-clock"></i>
            <h3>Total time</h3>
            <p><%= String.format("%.2f", stats.getTotalTime()) %> h</p>
        </div>
    </div>

    <div class="table-panel">
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Start</th>
                    <th>End</th>
                    <th>City</th>
                    <th>User</th>
                    <th>Changes</th>
                    <th>Time</th>
                    <th>Distance</th>
                </tr>
                </thead>
                <tbody>
                <% for (PathInfoBean p : pathList) { %>
                    <tr>
                        <td><%= p.getStartStation() %></td>
                        <td><%= p.getEndStation() %></td>
                        <td><%= p.getCity() %></td>
                        <td><%= p.getUtente() %></td>
                        <td><%= p.getNCambi() %></td>
                        <td><%= p.getTempoDiArrivo() %></td>
                        <td><%= String.format("%.2f", p.getPercTerrenoUtilizzato()) %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
