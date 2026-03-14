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
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>RouteX - Area Riservata</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
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

        .ticket-card {
            padding: 16px 18px;
            border-radius: 20px;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.08);
        }

        .ticket-card strong {
            display: block;
            margin-bottom: 6px;
            font-size: 1rem;
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

        .table-shell {
            overflow-x: auto;
            border-radius: 20px;
        }

        table.dataTable {
            width: 100% !important;
            border-collapse: separate !important;
            border-spacing: 0 10px !important;
            color: var(--text);
            background: transparent;
        }

        table.dataTable thead th {
            background: rgba(255,255,255,0.05) !important;
            color: #dff8ff !important;
            border: none !important;
            padding: 14px 12px !important;
        }

        table.dataTable tbody tr {
            background: rgba(255,255,255,0.04) !important;
        }

        table.dataTable tbody td {
            border-top: 1px solid rgba(255,255,255,0.08) !important;
            border-bottom: 1px solid rgba(255,255,255,0.08) !important;
            padding: 14px 12px !important;
            color: var(--text) !important;
        }

        table.dataTable tbody td:first-child {
            border-left: 1px solid rgba(255,255,255,0.08) !important;
            border-top-left-radius: 14px;
            border-bottom-left-radius: 14px;
        }

        table.dataTable tbody td:last-child {
            border-right: 1px solid rgba(255,255,255,0.08) !important;
            border-top-right-radius: 14px;
            border-bottom-right-radius: 14px;
        }

        .dataTables_wrapper {
            color: var(--muted);
        }

        .dataTables_wrapper .dataTables_filter input,
        .dataTables_wrapper .dataTables_length select {
            background: rgba(255,255,255,0.06);
            border: 1px solid rgba(255,255,255,0.12);
            color: var(--text);
            padding: 6px 10px;
            border-radius: 10px;
        }

        .dataTables_wrapper .dataTables_paginate .paginate_button {
            color: var(--text) !important;
            background: rgba(255,255,255,0.06) !important;
            border: 1px solid rgba(255,255,255,0.12) !important;
            border-radius: 10px !important;
            margin: 0 4px;
        }

        .dataTables_wrapper .dataTables_paginate .paginate_button.current {
            background: rgba(111,247,255,0.14) !important;
            border-color: rgba(111,247,255,0.34) !important;
            color: #dff8ff !important;
        }

        .dataTables_wrapper .dataTables_paginate .paginate_button:hover {
            background: rgba(111,247,255,0.1) !important;
            border-color: rgba(111,247,255,0.34) !important;
            color: #dff8ff !important;
        }

        @media (max-width: 1180px) {
            .summary-grid,
            .content-grid {
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
</head>
<body>
<%
    List<RouteBean> listaPercorsi = (List<RouteBean>) request.getAttribute("listaPercorsi");
    List<TicketBean> tickets = (List<TicketBean>) request.getAttribute("tickets");
    boolean loggedIn = SessionAuthUtil.isLoggedIn(session);
    int routeCount = (listaPercorsi != null) ? listaPercorsi.size() : 0;
    int ticketCount = (tickets != null) ? tickets.size() : 0;
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
        <p>Qui trovi i percorsi salvati e i biglietti acquistati in una dashboard più pulita, leggibile e coerente con l’estetica futuristica di RouteX.</p>

        <div class="summary-grid">
            <div class="summary-card">
                <strong><%= routeCount %></strong>
                <span>Percorsi salvati</span>
            </div>
            <div class="summary-card">
                <strong><%= ticketCount %></strong>
                <span>Biglietti acquistati</span>
            </div>
            <div class="summary-card">
                <strong><%= loggedIn ? "Active" : "Guest" %></strong>
                <span>Stato sessione</span>
            </div>
            <div class="summary-card">
                <strong>RouteX</strong>
                <span>Monitoraggio personale della mobilità urbana</span>
            </div>
        </div>
    </section>

    <section class="content-grid">
        <div class="panel">
            <h2>Saved Routes</h2>
            <p class="section-copy">Storico dei percorsi generati, con dettagli su cambi, stazioni percorse e tempo medio di arrivo.</p>

            <% if (listaPercorsi != null && !listaPercorsi.isEmpty()) { %>
            <div class="table-shell">
                <table id="routesTable" class="display">
                    <thead>
                    <tr>
                        <th>Partenza</th>
                        <th>Arrivo</th>
                        <th>Città</th>
                        <th>Cambi</th>
                        <th>Lista cambi</th>
                        <th>Interscambi</th>
                        <th>Stazioni</th>
                        <th>Tempo</th>
                        <th>Stazioni città</th>
                        <th>Terreno %</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (RouteBean r : listaPercorsi) { %>
                    <tr>
                        <td><%= r.getPartenza() %></td>
                        <td><%= r.getArrivo() %></td>
                        <td><%= r.getCitta() %></td>
                        <td><%= r.getnCambi() %></td>
                        <td><%= r.getListaCambi() %></td>
                        <td><%= r.getStazInterscambio() %></td>
                        <td><%= r.getnStazAttraversate() %></td>
                        <td><%= r.getTempoDiArrivo() %></td>
                        <td><%= r.getnStazioniCitta() %></td>
                        <td><%= r.getPercTerrenoUtilizzato() %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <div class="empty-state">Nessun percorso salvato disponibile.</div>
            <% } %>
        </div>

        <div class="panel">
            <h2>Purchased Tickets</h2>
            <p class="section-copy">Archivio rapido dei titoli di viaggio già acquistati e associati al tuo profilo.</p>

            <% if (tickets != null && !tickets.isEmpty()) { %>
            <div class="tickets-list">
                <% for (TicketBean t : tickets) { %>
                <div class="ticket-card">
                    <strong><%= t.getCodice() %></strong>
                    <div class="ticket-meta">
                        <span><%= t.getCitta() %></span>
                        <span><%= t.getDataAcquisto() %></span>
                    </div>
                </div>
                <% } %>
            </div>
            <% } else { %>
            <div class="empty-state">Nessun biglietto acquistato disponibile.</div>
            <% } %>
        </div>
    </section>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function () {
        const table = $('#routesTable');
        if (table.length) {
            table.DataTable({
                pageLength: 8,
                lengthChange: false,
                language: {
                    paginate: {
                        previous: "Precedente",
                        next: "Successiva"
                    },
                    info: "Pagina _PAGE_ di _PAGES_",
                    search: "Cerca:"
                }
            });
        }
    });
</script>
</body>
</html>
