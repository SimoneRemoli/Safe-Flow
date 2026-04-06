<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.web.routex.bean.TicketBean" %>
<%@ page import="it.web.routex.bean.RouteBean" %>
<%@ page import="it.web.routex.domain.SessionAuthUtil" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>RouteX - Reserved Area</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.84);
            --panel-soft: rgba(10, 26, 47, 0.9);
            --line: rgba(111, 247, 255, 0.2);
            --text: #ecf7ff;
            --muted: #8ba5be;
            --accent: #6ff7ff;
            --accent-2: #89ffd1;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 15% 18%, rgba(111, 247, 255, 0.16), transparent 24%),
                radial-gradient(circle at 82% 18%, rgba(83, 169, 255, 0.18), transparent 22%),
                radial-gradient(circle at bottom center, rgba(67, 112, 255, 0.16), transparent 32%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
            overflow-x: hidden;
            position: relative;
        }

        body::before {
            content: "";
            position: fixed;
            inset: 0;
            background-image:
                radial-gradient(circle, rgba(255,255,255,0.72) 1px, transparent 1px),
                radial-gradient(circle, rgba(111,247,255,0.42) 1px, transparent 1px);
            background-size: 150px 150px, 230px 230px;
            background-position: 0 0, 60px 90px;
            opacity: 0.18;
            pointer-events: none;
        }

        .shell {
            width: min(1440px, calc(100% - 32px));
            margin: 24px auto;
            min-height: calc(100vh - 48px);
            padding: 28px;
            border-radius: 32px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.88), rgba(4, 12, 23, 0.94));
            box-shadow: 0 32px 84px rgba(0, 0, 0, 0.42);
            backdrop-filter: blur(16px);
            position: relative;
            overflow: hidden;
        }

        .shell::after {
            content: "";
            position: absolute;
            width: 420px;
            height: 420px;
            right: -140px;
            top: -160px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(111, 247, 255, 0.12), transparent 66%);
            filter: blur(12px);
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            position: relative;
            z-index: 1;
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 14px;
        }

        .brand img { width: 72px; }
        .brand strong { display: block; font-size: 1.35rem; }
        .brand span { color: var(--muted); font-size: 0.94rem; }

        .nav-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .nav-actions a {
            text-decoration: none;
            color: var(--text);
            padding: 10px 16px;
            border-radius: 999px;
            border: 1px solid rgba(255,255,255,0.12);
            background: rgba(255,255,255,0.06);
            transition: transform 0.25s ease, border-color 0.25s ease;
        }

        .nav-actions a:hover {
            transform: translateY(-2px);
            border-color: rgba(111,247,255,0.4);
        }

        .hero {
            margin-top: 28px;
            position: relative;
            z-index: 1;
        }

        .eyebrow {
            display: inline-flex;
            padding: 8px 14px;
            border-radius: 999px;
            color: var(--accent);
            border: 1px solid rgba(111,247,255,0.2);
            background: rgba(111,247,255,0.08);
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }

        .hero h1 {
            margin: 18px 0 12px;
            font-size: clamp(2.6rem, 4vw, 4.8rem);
            line-height: 0.92;
        }

        .hero p {
            margin: 0;
            color: var(--muted);
            line-height: 1.8;
            max-width: 780px;
        }

        .summary-grid {
            margin-top: 24px;
            display: grid;
            grid-template-columns: repeat(4, minmax(0, 1fr));
            gap: 14px;
        }

        .summary-card {
            padding: 18px;
            border-radius: 24px;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.08);
        }

        .summary-card strong {
            display: block;
            font-size: 1.7rem;
            margin-bottom: 6px;
        }

        .summary-card span {
            color: var(--muted);
            line-height: 1.5;
            font-size: 0.9rem;
        }

        .content-grid {
            margin-top: 24px;
            display: grid;
            grid-template-columns: 1.2fr 0.8fr;
            gap: 18px;
            position: relative;
            z-index: 1;
        }

        .panel {
            border-radius: 28px;
            border: 1px solid var(--line);
            background: var(--panel-soft);
            padding: 22px;
            overflow: hidden;
        }

        .panel h2 {
            margin: 0 0 8px;
            font-size: 1.4rem;
        }

        .panel p.section-copy {
            margin: 0 0 18px;
            color: var(--muted);
            line-height: 1.7;
        }

        .tickets-list {
            display: grid;
            gap: 12px;
        }

        .routes-list {
            display: grid;
            gap: 14px;
        }

        .route-card {
            padding: 18px;
            border-radius: 18px;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.08);
        }

        .route-card-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 12px;
            margin-bottom: 12px;
        }

        .route-title {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .route-title strong {
            font-size: 1rem;
        }

        .route-subtitle {
            color: var(--muted);
            font-size: 0.9rem;
        }

        .route-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 10px 18px;
        }

        .route-item span {
            display: block;
        }

        .route-item span:first-child {
            color: var(--muted);
            font-size: 0.8rem;
            margin-bottom: 3px;
        }

        .ticket-actions {
            display: flex;
            align-items: center;
            gap: 10px;
            flex-wrap: wrap;
            margin-bottom: 18px;
        }

        .action-btn {
            border: 1px solid rgba(255,255,255,0.12);
            border-radius: 999px;
            padding: 10px 16px;
            color: var(--text);
            background: rgba(255,255,255,0.06);
            cursor: pointer;
            transition: transform 0.25s ease, border-color 0.25s ease, background 0.25s ease;
        }

        .action-btn:hover {
            transform: translateY(-2px);
            border-color: rgba(111,247,255,0.38);
        }

        .action-btn.danger {
            background: rgba(255, 92, 92, 0.12);
            border-color: rgba(255, 92, 92, 0.28);
        }

        .ticket-card {
            padding: 16px 18px;
            border-radius: 20px;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.08);
        }

        .ticket-card-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 12px;
            margin-bottom: 6px;
        }

        .ticket-card strong {
            display: block;
            margin-bottom: 6px;
            font-size: 1rem;
        }

        .ticket-selector {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            color: var(--muted);
            font-size: 0.88rem;
        }

        .ticket-meta {
            display: flex;
            justify-content: space-between;
            gap: 12px;
            flex-wrap: wrap;
            color: var(--muted);
            font-size: 0.88rem;
        }

        .empty-state {
            padding: 18px;
            border-radius: 20px;
            background: rgba(255,255,255,0.04);
            color: var(--muted);
        }

        .flash-message {
            margin-top: 18px;
            padding: 14px 16px;
            border-radius: 18px;
            border: 1px solid rgba(255,255,255,0.12);
        }

        .flash-message.success {
            background: rgba(68, 227, 164, 0.12);
            border-color: rgba(68, 227, 164, 0.28);
        }

        .flash-message.error {
            background: rgba(255, 92, 92, 0.12);
            border-color: rgba(255, 92, 92, 0.28);
        }

        .table-shell {
            overflow-x: auto;
            border-radius: 20px;
        }

        @media (max-width: 1180px) {
            .summary-grid,
            .content-grid {
                grid-template-columns: 1fr;
            }

            .route-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 720px) {
            .shell {
                width: min(100% - 16px, 1440px);
                margin: 8px auto;
                min-height: calc(100vh - 16px);
                padding: 18px;
                border-radius: 24px;
            }

            .topbar {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="header.jspf" %>
<%
    List<RouteBean> listaPercorsi = (List<RouteBean>) request.getAttribute("listaPercorsi");
    List<TicketBean> tickets = (List<TicketBean>) request.getAttribute("tickets");
    boolean loggedIn = SessionAuthUtil.isLoggedIn(session);
    int routeCount = (listaPercorsi != null) ? listaPercorsi.size() : 0;
    int ticketCount = (tickets != null) ? tickets.size() : 0;
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<div class="shell">
    <div class="topbar">
        <div class="brand">
            <img src="images/logo-no-background.png" alt="RouteX">
            <div>
                <strong>RouteX</strong>
                <span>Reserved urban dashboard</span>
            </div>
        </div>

        <div class="nav-actions">
            <% if (loggedIn) { %>
            <a href="indexLogged.jsp">Home</a>
            <a href="logout">Logout</a>
            <% } else { %>
            <a href="index.jsp">Home</a>
            <a href="login.jsp">Login</a>
            <% } %>
        </div>
    </div>

    <section class="hero">
        <div class="eyebrow">Reserved Area</div>
        <h1>Your metro activity,<br>organized like a control room.</h1>
        <p>Here you can find your saved routes and purchased tickets in a cleaner, more readable dashboard that stays consistent with the RouteX visual style.</p>

        <div class="summary-grid">
            <div class="summary-card">
                <strong><%= routeCount %></strong>
                <span>Saved routes</span>
            </div>
            <div class="summary-card">
                <strong><%= ticketCount %></strong>
                <span>Purchased tickets</span>
            </div>
            <div class="summary-card">
                <strong><%= loggedIn ? "Active" : "Guest" %></strong>
                <span>Session status</span>
            </div>
            <div class="summary-card">
                <strong>RouteX</strong>
                <span>Personal urban mobility overview</span>
            </div>
        </div>

        <% if (successMessage != null) { %>
        <div class="flash-message success"><%= successMessage %></div>
        <% } %>

        <% if (errorMessage != null) { %>
        <div class="flash-message error"><%= errorMessage %></div>
        <% } %>
    </section>

    <section class="content-grid">
        <div class="panel">
            <h2>Saved Routes</h2>
            <p class="section-copy">A history of generated routes, including line changes, crossed stations, and estimated arrival time.</p>

            <% if (listaPercorsi != null && !listaPercorsi.isEmpty()) { %>
            <form method="post" action="areaRiservata">
                <div class="table-actions">
                    <button type="submit" class="action-btn" name="action" value="deleteSelectedRoutes">Delete selected</button>
                    <button type="submit" class="action-btn danger" name="action" value="deleteAllRoutes" onclick="return confirm('Do you want to delete all saved routes?');">Delete all</button>
                </div>

                <div class="routes-list">
                    <% for (RouteBean r : listaPercorsi) { %>
                    <div class="route-card">
                        <div class="route-card-header">
                            <div class="route-title">
                                <strong><%= r.getPartenza() %> -> <%= r.getArrivo() %></strong>
                                <span class="route-subtitle"><%= r.getCitta() %></span>
                            </div>
                            <label class="ticket-selector">
                                <input type="checkbox" name="selectedRoutes" value="<%= r.getSignature() %>">
                                <span>Select</span>
                            </label>
                        </div>

                        <div class="route-grid">
                            <div class="route-item">
                                <span>Line changes</span>
                                <span><%= r.getnCambi() %></span>
                            </div>
                            <div class="route-item">
                                <span>Change list</span>
                                <span><%= r.getListaCambi() %></span>
                            </div>
                            <div class="route-item">
                                <span>Interchanges</span>
                                <span><%= r.getStazInterscambio() %></span>
                            </div>
                            <div class="route-item">
                                <span>Stations crossed</span>
                                <span><%= r.getnStazAttraversate() %></span>
                            </div>
                            <div class="route-item">
                                <span>Arrival time</span>
                                <span><%= r.getTempoDiArrivo() %></span>
                            </div>
                            <div class="route-item">
                                <span>City stations</span>
                                <span><%= r.getnStazioniCitta() %></span>
                            </div>
                            <div class="route-item">
                                <span>Land used</span>
                                <span><%= r.getPercTerrenoUtilizzato() %>%</span>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
            </form>
            <% } else { %>
            <div class="empty-state">No saved routes available.</div>
            <% } %>
        </div>

        <div class="panel">
            <h2>Purchased Tickets</h2>
            <p class="section-copy">A quick archive of tickets already purchased and linked to your profile.</p>

            <% if (tickets != null && !tickets.isEmpty()) { %>
            <form method="post" action="areaRiservata">
                <div class="ticket-actions">
                    <button type="submit" class="action-btn" name="action" value="deleteSelected">Delete selected</button>
                    <button type="submit" class="action-btn danger" name="action" value="deleteAll" onclick="return confirm('Do you want to delete all purchased tickets?');">Delete all</button>
                </div>

                <div class="tickets-list">
                    <% for (TicketBean t : tickets) { %>
                    <div class="ticket-card">
                        <div class="ticket-card-header">
                            <strong><%= t.getCodice() %></strong>
                            <label class="ticket-selector">
                                <input type="checkbox" name="selectedTickets" value="<%= t.getCodice() %>">
                                <span>Select</span>
                            </label>
                        </div>
                        <div class="ticket-meta">
                            <span><%= t.getCitta() %></span>
                            <span><%= t.getDataAcquisto() %></span>
                        </div>
                    </div>
                    <% } %>
                </div>
            </form>
            <% } else { %>
            <div class="empty-state">No purchased tickets available.</div>
            <% } %>
        </div>
    </section>
</div>

</body>
</html>
