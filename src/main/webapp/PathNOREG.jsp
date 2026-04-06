<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.web.routex.domain.SessionAuthUtil" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>RouteX - Route Visualization</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #091d33;
            --panel: rgba(7, 20, 36, 0.82);
            --panel-soft: rgba(10, 26, 47, 0.88);
            --line: rgba(111, 247, 255, 0.2);
            --text: #ecf7ff;
            --muted: #8ba5be;
            --accent: #6ff7ff;
            --accent-2: #53a9ff;
            --accent-3: #89ffd1;
            --warning: #ffd66b;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 15% 15%, rgba(111, 247, 255, 0.16), transparent 22%),
                radial-gradient(circle at 85% 18%, rgba(83, 169, 255, 0.16), transparent 22%),
                radial-gradient(circle at bottom center, rgba(70, 117, 255, 0.14), transparent 28%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
            overflow-x: hidden;
            position: relative;
        }

        body::before {
            content: "";
            position: fixed;
            inset: 0;
            background-image:
                radial-gradient(circle, rgba(255, 255, 255, 0.72) 1px, transparent 1px),
                radial-gradient(circle, rgba(111, 247, 255, 0.42) 1px, transparent 1px);
            background-size: 160px 160px, 240px 240px;
            background-position: 0 0, 60px 90px;
            opacity: 0.18;
            pointer-events: none;
            animation: drift 18s linear infinite;
        }

        .shell {
            width: min(1400px, calc(100% - 32px));
            margin: 20px auto;
            min-height: calc(100vh - 40px);
            padding: 24px;
            border-radius: 32px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.86), rgba(4, 12, 23, 0.92));
            box-shadow: 0 32px 84px rgba(0, 0, 0, 0.42);
            backdrop-filter: blur(16px);
            position: relative;
            overflow: hidden;
        }

        .shell::after {
            content: "";
            position: absolute;
            width: 460px;
            height: 460px;
            right: -180px;
            bottom: -180px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(111, 247, 255, 0.12), transparent 66%);
            filter: blur(12px);
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 18px;
            margin-bottom: 22px;
            position: relative;
            z-index: 1;
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 14px;
        }

        .brand img {
            width: 70px;
        }

        .brand strong {
            display: block;
            font-size: 1.35rem;
        }

        .brand span {
            color: var(--muted);
            font-size: 0.94rem;
        }

        .actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .actions a,
        .actions button {
            text-decoration: none;
            color: var(--text);
            padding: 11px 16px;
            border-radius: 999px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.06);
            cursor: pointer;
            transition: transform 0.25s ease, border-color 0.25s ease, background 0.25s ease;
            font-size: 0.95rem;
        }

        .actions a:hover,
        .actions button:hover {
            transform: translateY(-2px);
            border-color: rgba(111, 247, 255, 0.42);
            background: rgba(111, 247, 255, 0.08);
        }

        .layout {
            display: grid;
            grid-template-columns: 360px 1fr;
            gap: 24px;
            align-items: start;
            position: relative;
            z-index: 1;
        }

        .panel {
            background: var(--panel);
            border: 1px solid var(--line);
            border-radius: 28px;
            overflow: hidden;
        }

        .route-panel {
            padding: 22px;
            position: sticky;
            top: 20px;
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

        .route-panel h1 {
            margin: 16px 0 8px;
            font-size: 2rem;
            line-height: 1;
        }

        .route-panel p {
            margin: 0;
            color: var(--muted);
            line-height: 1.7;
        }

        .route-list {
            list-style: none;
            margin: 24px 0 0;
            padding: 0;
            display: grid;
            gap: 14px;
            max-height: calc(100vh - 290px);
            overflow-y: auto;
        }

        .route-list li {
            display: grid;
            grid-template-columns: 42px 1fr;
            gap: 14px;
            align-items: start;
            padding: 14px;
            border-radius: 22px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
            opacity: 0;
            transform: translateX(-16px);
            animation: stationEnter 0.55s ease forwards;
            animation-delay: var(--delay, 0s);
        }

        .station-index {
            width: 42px;
            height: 42px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, var(--accent), #9fffe7);
            color: #04111f;
            font-weight: 700;
            box-shadow: 0 0 24px rgba(111, 247, 255, 0.25);
        }

        .station-card strong {
            display: block;
            font-size: 1.02rem;
            margin-bottom: 6px;
        }

        .line-chip {
            display: inline-flex;
            width: fit-content;
            padding: 6px 10px;
            border-radius: 999px;
            background: rgba(111, 247, 255, 0.08);
            border: 1px solid rgba(111, 247, 255, 0.14);
            color: #dffbff;
            font-size: 0.82rem;
        }

        .route-empty {
            margin-top: 22px;
            padding: 18px;
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.04);
            color: var(--muted);
        }

        .content-panel {
            padding: 24px;
            background: var(--panel-soft);
        }

        .hero {
            display: grid;
            grid-template-columns: 1.05fr 0.95fr;
            gap: 20px;
            align-items: stretch;
        }

        .hero-copy h2 {
            margin: 16px 0 10px;
            font-size: clamp(2.2rem, 4vw, 4rem);
            line-height: 0.95;
        }

        .hero-copy p {
            margin: 0;
            color: var(--muted);
            line-height: 1.8;
            max-width: 720px;
        }

        .route-visual {
            border-radius: 28px;
            padding: 20px;
            border: 1px solid rgba(111, 247, 255, 0.16);
            background:
                radial-gradient(circle at 20% 18%, rgba(111, 247, 255, 0.14), transparent 30%),
                linear-gradient(180deg, rgba(8, 18, 32, 0.86), rgba(6, 14, 25, 0.96));
            position: relative;
            overflow: hidden;
            min-height: 280px;
        }

        .route-track {
            position: absolute;
            left: 34px;
            right: 34px;
            top: 50%;
            height: 6px;
            transform: translateY(-50%);
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.08);
            overflow: hidden;
        }

        .route-track::after {
            content: "";
            position: absolute;
            inset: 0;
            width: 0;
            background: linear-gradient(90deg, var(--accent), #89ffd1, var(--accent-2));
            animation: fillTrack 2.8s ease forwards;
        }

        .metro-stop {
            position: absolute;
            top: 50%;
            width: 16px;
            height: 16px;
            border-radius: 50%;
            background: #fff;
            border: 4px solid var(--accent);
            box-shadow: 0 0 16px rgba(111, 247, 255, 0.35);
            transform: translate(-50%, -50%) scale(0);
            animation: stopReveal 0.45s ease forwards;
            animation-delay: var(--delay, 0s);
        }

        .metro-stop::after {
            content: attr(data-name);
            position: absolute;
            top: 22px;
            left: 50%;
            transform: translateX(-50%);
            color: #dff8ff;
            font-size: 0.78rem;
            white-space: nowrap;
        }

        .metro-stop.start,
        .metro-stop.end {
            width: 20px;
            height: 20px;
        }

        .metro-stop.start::after,
        .metro-stop.end::after {
            font-weight: 700;
        }

        .visual-meta {
            position: absolute;
            left: 22px;
            right: 22px;
            bottom: 22px;
            display: flex;
            justify-content: space-between;
            gap: 12px;
            color: var(--muted);
            font-size: 0.88rem;
        }

        .stat-grid {
            margin-top: 22px;
            display: grid;
            grid-template-columns: repeat(4, minmax(0, 1fr));
            gap: 14px;
        }

        .stat-card {
            padding: 18px;
            border-radius: 24px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .stat-card strong {
            display: block;
            font-size: 1.7rem;
            margin-bottom: 6px;
        }

        .stat-card span {
            color: var(--muted);
            line-height: 1.5;
            font-size: 0.9rem;
        }

        .details-grid {
            margin-top: 22px;
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 18px;
        }

        .detail-panel {
            padding: 20px;
            border-radius: 26px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .detail-panel h3 {
            margin: 0 0 14px;
            font-size: 1.18rem;
        }

        .detail-table {
            display: grid;
            gap: 12px;
        }

        .detail-row {
            display: grid;
            grid-template-columns: 180px 1fr;
            gap: 14px;
            padding-bottom: 12px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.06);
        }

        .detail-row:last-child {
            border-bottom: none;
            padding-bottom: 0;
        }

        .detail-label {
            color: var(--muted);
        }

        .detail-value {
            color: var(--text);
            font-weight: 600;
        }

        .chips {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .chip {
            padding: 8px 12px;
            border-radius: 999px;
            background: rgba(111, 247, 255, 0.08);
            border: 1px solid rgba(111, 247, 255, 0.14);
            color: #dffbff;
            font-size: 0.85rem;
        }

        .download-card {
            margin-top: 20px;
            padding: 22px;
            border-radius: 28px;
            background: linear-gradient(135deg, rgba(111, 247, 255, 0.1), rgba(83, 169, 255, 0.08));
            border: 1px solid rgba(111, 247, 255, 0.18);
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
        }

        .download-card p {
            margin: 0;
            color: var(--muted);
            line-height: 1.7;
        }

        .download-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            color: #04111f;
            background: linear-gradient(90deg, var(--accent), var(--accent-3), #8dd8ff);
            padding: 15px 22px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.05em;
            white-space: nowrap;
            box-shadow: 0 18px 34px rgba(111, 247, 255, 0.22);
            transition: transform 0.25s ease, box-shadow 0.25s ease;
        }

        .download-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 22px 38px rgba(111, 247, 255, 0.28);
        }

        @keyframes drift {
            0% { transform: translateY(0); }
            50% { transform: translateY(-14px); }
            100% { transform: translateY(0); }
        }

        @keyframes stationEnter {
            from {
                opacity: 0;
                transform: translateX(-16px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        @keyframes fillTrack {
            from { width: 0; }
            to { width: 100%; }
        }

        @keyframes stopReveal {
            from {
                opacity: 0;
                transform: translate(-50%, -50%) scale(0);
            }
            to {
                opacity: 1;
                transform: translate(-50%, -50%) scale(1);
            }
        }

        @media (max-width: 1180px) {
            .layout,
            .hero,
            .details-grid,
            .stat-grid {
                grid-template-columns: 1fr;
            }

            .route-panel {
                position: static;
            }

            .detail-row {
                grid-template-columns: 1fr;
                gap: 6px;
            }
        }

        @media (max-width: 720px) {
            .shell {
                width: min(100% - 16px, 1400px);
                margin: 8px auto;
                min-height: calc(100vh - 16px);
                padding: 18px;
                border-radius: 24px;
            }

            .topbar {
                flex-direction: column;
                align-items: flex-start;
            }

            .download-card {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        .detail-panel h3,
        .detail-panel .detail-label,
        .detail-panel .detail-value {
            color: #101828 !important;
        }

        .detail-panel .detail-label {
            font-weight: 500;
        }

        .detail-panel .detail-value {
            font-weight: 700;
        }
    </style>
</head>
<body>
<%
    List<String> dati = (List<String>) request.getAttribute("percorsi");
    List<String> linee = (List<String>) request.getAttribute("linee");
    int numeroCambi = (int) request.getAttribute("numero_cambi");
    int numeroStazioni = (int) request.getAttribute("numero");
    String status = (String) request.getAttribute("status");
    Double minutaggio = (Double) request.getAttribute("minutaggio");
    String startStation = (String) request.getAttribute("inizio");
    String endStation = (String) request.getAttribute("fine");
    String city = (String) request.getAttribute("city");
    int stazioniTotali = (int) request.getAttribute("stazionitotali");
    Double suolo = (Double) request.getAttribute("suolometropolitano");
    List<String> sequenzaCambi = (List<String>) request.getAttribute("listacambi");
    List<String> sequenzaCruciali = (List<String>) request.getAttribute("nodicruciali");
    String newcity = city.toLowerCase();
    int routeSize = (dati != null) ? dati.size() : 0;
    boolean loggedIn = SessionAuthUtil.isLoggedIn(session);
    String homePage = loggedIn ? "travelerHome" : "index.jsp";
%>

<div class="shell">
    <div class="topbar">
        <div class="brand">
            <img src="images/logo-no-background.png" alt="RouteX">
            <div>
                <strong>RouteX</strong>
                <span>Metro path visualization dashboard</span>
            </div>
        </div>

        <div class="actions">
            <form action="PathControllerGrafico" method="get" style="margin:0;">
                <button type="submit">Back To Search</button>
            </form>
            <a href="<%= homePage %>">Home</a>
        </div>
    </div>

    <div class="layout">
        <aside class="panel route-panel">
            <div class="eyebrow">Shortest Path Sequence</div>
            <h1>Best Route</h1>
            <p>Stations light up in sequence like a metro line activating progressively.</p>

            <% if (dati != null && !dati.isEmpty()) { %>
            <ul class="route-list">
                <% for (int i = 0; i < dati.size(); i++) { %>
                <li style="--delay:<%= (i * 0.12) %>s;">
                    <div class="station-index"><%= (i + 1) %></div>
                    <div class="station-card">
                        <strong><%= dati.get(i) %></strong>
                        <span class="line-chip"><%= linee.get(i) %></span>
                    </div>
                </li>
                <% } %>
            </ul>
            <% } else { %>
            <div class="route-empty">Nessun dato disponibile per il percorso selezionato.</div>
            <% } %>
        </aside>

        <section class="panel content-panel">
            <div class="hero">
                <div class="hero-copy">
                    <div class="eyebrow">Metro Route Scan</div>
                    <h2><%= startStation %> to<br><%= endStation %></h2>
                    <p>
                        The route is displayed as an urban line in continuous scan mode.
                        Each node represents a station on the path, while key points highlight line changes.
                    </p>
                </div>

                <div class="route-visual">
                    <div class="route-track"></div>
                    <% if (dati != null && !dati.isEmpty()) { %>
                    <div
                            class="metro-stop start"
                            data-name="<%= dati.get(0) %>"
                            style="left:8%; --delay:0.45s;">
                    </div>
                    <div
                            class="metro-stop end"
                            data-name="<%= dati.get(dati.size() - 1) %>"
                            style="left:92%; --delay:0.8s;">
                    </div>
                    <% } %>
                    <div class="visual-meta">
                        <span><%= city %> metro network</span>
                        <span><%= routeSize %> active nodes</span>
                    </div>
                </div>
            </div>

            <div class="stat-grid">
                <div class="stat-card">
                    <strong><%= numeroCambi %></strong>
                    <span>Line changes detected</span>
                </div>
                <div class="stat-card">
                    <strong><%= numeroStazioni %></strong>
                    <span>Stations on the shortest path</span>
                </div>
                <div class="stat-card">
                    <strong><%= String.format("%.0f", minutaggio) %> min</strong>
                    <span>Estimated travel time</span>
                </div>
                <div class="stat-card">
                    <strong><%= String.format("%.2f", suolo) %>%</strong>
                    <span>Metro land usage ratio</span>
                </div>
            </div>

            <div class="details-grid">
                <div class="detail-panel">
                    <h3>Route Briefing</h3>
                    <div class="detail-table">
                        <div class="detail-row">
                            <div class="detail-label">Start station</div>
                            <div class="detail-value"><%= startStation %></div>
                        </div>
                        <div class="detail-row">
                            <div class="detail-label">End station</div>
                            <div class="detail-value"><%= endStation %></div>
                        </div>
                        <div class="detail-row">
                            <div class="detail-label">Metropolitan city</div>
                            <div class="detail-value"><%= city %></div>
                        </div>
                        <div class="detail-row">
                            <div class="detail-label">Traveler profile</div>
                            <div class="detail-value"><%= status %></div>
                        </div>
                        <div class="detail-row">
                            <div class="detail-label">Total stations in city</div>
                            <div class="detail-value"><%= stazioniTotali %></div>
                        </div>
                    </div>
                </div>

                <div class="detail-panel">
                    <h3>Metro Interchanges</h3>
                    <div class="detail-table">
                        <div class="detail-row">
                            <div class="detail-label">Line changes</div>
                            <div class="detail-value">
                                <div class="chips">
                                    <% if (sequenzaCambi != null && !sequenzaCambi.isEmpty()) {
                                        for (String linea : sequenzaCambi) { %>
                                    <span class="chip"><%= linea %></span>
                                    <%  }
                                    } else { %>
                                    <span class="chip">No change needed</span>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                        <div class="detail-row">
                            <div class="detail-label">Interchange stations</div>
                            <div class="detail-value">
                                <div class="chips">
                                    <% if (sequenzaCruciali != null && !sequenzaCruciali.isEmpty()) {
                                        for (String stazione : sequenzaCruciali) { %>
                                    <span class="chip"><%= stazione %></span>
                                    <%  }
                                    } else { %>
                                    <span class="chip">Direct route</span>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="download-card">
                <p>
                    Download the metro map of <strong><%= city %></strong> to visually compare the newly generated route
                    with the city's full network.
                </p>
                <a href="images/metro-<%= newcity %>.jpg" class="download-btn" download="<%= city %>_Map.jpg">Download Metro Map</a>
            </div>
        </section>
    </div>
</div>
</body>
</html>
