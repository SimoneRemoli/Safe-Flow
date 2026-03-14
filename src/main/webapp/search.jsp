<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.web.routex.bean.CityBean" %>
<%@ page import="it.web.routex.domain.SessionAuthUtil" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>RouteX - Metro Finder</title>
    <style>
        :root {
            --bg-deep: #04111f;
            --bg-mid: #0b1f36;
            --panel: rgba(6, 18, 33, 0.76);
            --panel-strong: rgba(8, 24, 44, 0.9);
            --line: rgba(120, 214, 255, 0.24);
            --text: #ecf7ff;
            --muted: #8ba5be;
            --accent: #6ff7ff;
            --accent-2: #53a9ff;
            --success: #a8ffbf;
            --shadow: 0 30px 80px rgba(0, 0, 0, 0.42);
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            overflow-x: hidden;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at top left, rgba(111, 247, 255, 0.16), transparent 28%),
                radial-gradient(circle at 80% 20%, rgba(83, 169, 255, 0.18), transparent 20%),
                radial-gradient(circle at bottom center, rgba(50, 120, 255, 0.18), transparent 32%),
                linear-gradient(135deg, var(--bg-deep), var(--bg-mid) 55%, #050b16);
            position: relative;
        }

        body::before,
        body::after {
            content: "";
            position: fixed;
            inset: 0;
            pointer-events: none;
        }

        body::before {
            background-image:
                radial-gradient(circle, rgba(255, 255, 255, 0.7) 1px, transparent 1px),
                radial-gradient(circle, rgba(111, 247, 255, 0.4) 1px, transparent 1px);
            background-size: 140px 140px, 220px 220px;
            background-position: 0 0, 40px 80px;
            opacity: 0.22;
            animation: drift 22s linear infinite;
        }

        body::after {
            background: linear-gradient(180deg, rgba(255, 255, 255, 0.05), transparent 22%, transparent 78%, rgba(111, 247, 255, 0.06));
        }

        .top-nav {
            position: absolute;
            top: 24px;
            right: 32px;
            display: flex;
            gap: 12px;
            z-index: 5;
        }

        .top-nav a {
            text-decoration: none;
            color: var(--text);
            padding: 10px 16px;
            border-radius: 999px;
            border: 1px solid rgba(255, 255, 255, 0.14);
            background: rgba(255, 255, 255, 0.06);
            backdrop-filter: blur(14px);
            transition: transform 0.25s ease, border-color 0.25s ease, background 0.25s ease;
        }

        .top-nav a:hover {
            transform: translateY(-2px);
            border-color: rgba(111, 247, 255, 0.48);
            background: rgba(111, 247, 255, 0.1);
        }

        .page-shell {
            width: min(1320px, calc(100% - 32px));
            margin: 88px auto 32px;
            padding: 28px;
            border: 1px solid var(--line);
            border-radius: 32px;
            background: linear-gradient(180deg, rgba(8, 24, 44, 0.82), rgba(4, 13, 25, 0.88));
            box-shadow: var(--shadow);
            backdrop-filter: blur(16px);
            position: relative;
            overflow: hidden;
        }

        .page-shell::before {
            content: "";
            position: absolute;
            inset: auto -15% -35% auto;
            width: 420px;
            height: 420px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(111, 247, 255, 0.14), transparent 62%);
            filter: blur(12px);
        }

        .hero {
            display: grid;
            grid-template-columns: 1.1fr 0.9fr;
            gap: 28px;
            align-items: stretch;
        }

        .hero-copy,
        .preview-panel {
            position: relative;
            z-index: 1;
        }

        .eyebrow {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            padding: 8px 14px;
            border-radius: 999px;
            background: rgba(111, 247, 255, 0.08);
            border: 1px solid rgba(111, 247, 255, 0.22);
            color: var(--accent);
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }

        .hero h1 {
            margin: 18px 0 14px;
            font-size: clamp(2.5rem, 4vw, 4.8rem);
            line-height: 0.95;
            letter-spacing: 0.02em;
        }

        .hero p {
            margin: 0;
            max-width: 720px;
            color: var(--muted);
            font-size: 1.05rem;
            line-height: 1.7;
        }

        .preview-panel {
            border-radius: 28px;
            border: 1px solid rgba(111, 247, 255, 0.16);
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.06), transparent),
                radial-gradient(circle at top, rgba(111, 247, 255, 0.16), transparent 54%),
                var(--panel);
            padding: 22px;
            min-height: 280px;
        }

        .preview-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            margin-bottom: 18px;
        }

        .preview-meta strong {
            display: block;
            font-size: 1.15rem;
        }

        .preview-meta span {
            color: var(--muted);
            font-size: 0.9rem;
        }

        .status-pill {
            padding: 8px 12px;
            border-radius: 999px;
            border: 1px solid rgba(168, 255, 191, 0.24);
            color: var(--success);
            background: rgba(168, 255, 191, 0.08);
            white-space: nowrap;
            font-size: 0.85rem;
        }

        .map-frame {
            position: relative;
            border-radius: 24px;
            overflow: hidden;
            border: 1px solid rgba(255, 255, 255, 0.08);
            background: rgba(0, 0, 0, 0.24);
            min-height: 220px;
        }

        .map-frame img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            display: block;
            filter: saturate(1.12) contrast(1.04);
        }

        .map-overlay {
            position: absolute;
            inset: 0;
            background:
                linear-gradient(135deg, rgba(3, 10, 21, 0.1), rgba(3, 10, 21, 0.72)),
                radial-gradient(circle at 20% 20%, rgba(111, 247, 255, 0.16), transparent 34%);
            display: flex;
            align-items: flex-end;
            justify-content: space-between;
            padding: 18px;
        }

        .map-overlay span {
            font-size: 0.95rem;
            color: #d8f8ff;
            text-shadow: 0 1px 8px rgba(0, 0, 0, 0.5);
        }

        .orbit-section {
            margin-top: 30px;
            display: grid;
            grid-template-columns: 1.2fr 0.8fr;
            gap: 28px;
            align-items: start;
        }

        .city-panel,
        .form-panel {
            background: var(--panel);
            border: 1px solid var(--line);
            border-radius: 28px;
            padding: 24px;
            position: relative;
            overflow: hidden;
        }

        .city-panel::before,
        .form-panel::before {
            content: "";
            position: absolute;
            inset: -40% auto auto 55%;
            width: 220px;
            height: 220px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(83, 169, 255, 0.1), transparent 70%);
        }

        .section-title {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            margin-bottom: 22px;
            position: relative;
            z-index: 1;
        }

        .section-title h2 {
            margin: 0;
            font-size: 1.4rem;
        }

        .section-title p {
            margin: 6px 0 0;
            color: var(--muted);
            font-size: 0.94rem;
        }

        .city-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 20px;
            position: relative;
            z-index: 1;
        }

        .city-card {
            position: relative;
            border: 1px solid rgba(255, 255, 255, 0.08);
            border-radius: 24px;
            min-height: 220px;
            padding: 20px;
            background:
                linear-gradient(160deg, rgba(255, 255, 255, 0.04), rgba(255, 255, 255, 0.01)),
                rgba(7, 18, 32, 0.78);
            cursor: pointer;
            transition: transform 0.3s ease, border-color 0.3s ease, box-shadow 0.3s ease;
            overflow: hidden;
        }

        .city-card:hover {
            transform: translateY(-6px);
            border-color: rgba(111, 247, 255, 0.4);
            box-shadow: 0 24px 48px rgba(0, 0, 0, 0.28);
        }

        .city-card.active {
            border-color: rgba(111, 247, 255, 0.8);
            box-shadow: 0 0 0 1px rgba(111, 247, 255, 0.18), 0 24px 54px rgba(21, 124, 255, 0.22);
        }

        .planet-wrap {
            position: relative;
            height: 116px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 16px;
        }

        .orbit-ring,
        .orbit-ring::before {
            position: absolute;
            border-radius: 50%;
            border: 1px solid rgba(255, 255, 255, 0.14);
            content: "";
        }

        .orbit-ring {
            width: 122px;
            height: 122px;
            animation: rotateRing 18s linear infinite;
        }

        .orbit-ring::before {
            inset: 10px;
            border-color: rgba(111, 247, 255, 0.22);
            animation: pulse 5s ease-in-out infinite;
        }

        .planet {
            width: 76px;
            height: 76px;
            border-radius: 50%;
            background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.95), var(--planet-core) 38%, var(--planet-edge));
            box-shadow:
                0 0 0 8px rgba(255, 255, 255, 0.03),
                0 0 36px var(--planet-glow);
            position: relative;
            animation: levitate 5.5s ease-in-out infinite;
        }

        .planet::before,
        .planet::after {
            content: "";
            position: absolute;
            border-radius: 50%;
        }

        .planet::before {
            inset: 12px 10px 24px 18px;
            background: rgba(255, 255, 255, 0.2);
            filter: blur(6px);
        }

        .planet::after {
            width: 108px;
            height: 18px;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%) rotate(-18deg);
            border: 2px solid rgba(255, 255, 255, 0.22);
            opacity: 0.7;
        }

        .city-card h3 {
            margin: 0;
            font-size: 1.28rem;
        }

        .city-card p {
            margin: 8px 0 0;
            color: var(--muted);
            line-height: 1.5;
            font-size: 0.92rem;
        }

        .city-stats {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 14px;
            font-size: 0.84rem;
            color: #d9eeff;
        }

        .city-badge {
            padding: 6px 10px;
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .form-panel form {
            position: relative;
            z-index: 1;
        }

        .selected-city-banner {
            margin-bottom: 18px;
            padding: 16px 18px;
            border-radius: 20px;
            border: 1px solid rgba(111, 247, 255, 0.18);
            background: linear-gradient(135deg, rgba(111, 247, 255, 0.08), rgba(83, 169, 255, 0.06));
        }

        .selected-city-banner strong {
            display: block;
            font-size: 1rem;
            margin-bottom: 4px;
        }

        .selected-city-banner span {
            color: var(--muted);
            font-size: 0.92rem;
        }

        .field-group {
            margin-bottom: 16px;
            position: relative;
        }

        .field-group label {
            display: block;
            margin-bottom: 8px;
            color: #dff8ff;
            font-size: 0.92rem;
            letter-spacing: 0.04em;
        }

        .field-group input {
            width: 100%;
            padding: 16px 18px;
            border-radius: 18px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.05);
            color: var(--text);
            font-size: 1rem;
            outline: none;
            transition: border-color 0.25s ease, box-shadow 0.25s ease, transform 0.25s ease;
        }

        .field-group input::placeholder {
            color: #6f87a1;
        }

        .field-group input:focus {
            border-color: rgba(111, 247, 255, 0.55);
            box-shadow: 0 0 0 4px rgba(111, 247, 255, 0.08);
            transform: translateY(-1px);
        }

        .suggestions-box {
            position: absolute;
            top: calc(100% + 8px);
            left: 0;
            right: 0;
            margin: 0;
            padding: 8px;
            list-style: none;
            border-radius: 18px;
            border: 1px solid rgba(111, 247, 255, 0.18);
            background: rgba(7, 18, 32, 0.96);
            backdrop-filter: blur(16px);
            box-shadow: 0 18px 40px rgba(0, 0, 0, 0.28);
            max-height: 220px;
            overflow-y: auto;
            z-index: 20;
            display: none;
        }

        .suggestions-box.visible {
            display: block;
        }

        .suggestion-item {
            width: 100%;
            padding: 12px 14px;
            border: none;
            border-radius: 14px;
            background: transparent;
            color: var(--text);
            text-align: left;
            cursor: pointer;
            transition: background 0.2s ease, transform 0.2s ease;
        }

        .suggestion-item:hover,
        .suggestion-item.active {
            background: rgba(111, 247, 255, 0.12);
            transform: translateX(2px);
        }

        .suggestion-item mark {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #a9ffe8);
            padding: 0 4px;
            border-radius: 6px;
        }

        .hint-row {
            display: flex;
            justify-content: space-between;
            gap: 16px;
            margin: 12px 0 22px;
            color: var(--muted);
            font-size: 0.84rem;
        }

        .submit-btn {
            width: 100%;
            padding: 17px 24px;
            border: none;
            border-radius: 999px;
            color: #04111f;
            font-weight: 700;
            font-size: 1rem;
            letter-spacing: 0.08em;
            text-transform: uppercase;
            background: linear-gradient(90deg, var(--accent), #89ffd1 52%, #8dd8ff);
            cursor: pointer;
            box-shadow: 0 20px 34px rgba(111, 247, 255, 0.22);
            transition: transform 0.25s ease, box-shadow 0.25s ease, filter 0.25s ease;
        }

        .submit-btn:hover:not(:disabled) {
            transform: translateY(-3px) scale(1.01);
            box-shadow: 0 24px 42px rgba(111, 247, 255, 0.28);
        }

        .submit-btn:disabled {
            opacity: 0.45;
            cursor: not-allowed;
            filter: grayscale(0.2);
            box-shadow: none;
        }

        .logo-mark {
            margin-top: 22px;
            text-align: center;
            opacity: 0.86;
        }

        .logo-mark img {
            max-width: 210px;
            width: 100%;
        }

        @keyframes drift {
            from { transform: translateY(0); }
            50% { transform: translateY(-16px); }
            to { transform: translateY(0); }
        }

        @keyframes levitate {
            0%, 100% { transform: translateY(0) scale(1); }
            50% { transform: translateY(-9px) scale(1.03); }
        }

        @keyframes rotateRing {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }

        @keyframes pulse {
            0%, 100% { opacity: 0.35; transform: scale(1); }
            50% { opacity: 0.9; transform: scale(1.04); }
        }

        @media (max-width: 1080px) {
            .hero,
            .orbit-section {
                grid-template-columns: 1fr;
            }

            .city-grid {
                grid-template-columns: repeat(2, minmax(0, 1fr));
            }
        }

        @media (max-width: 720px) {
            .page-shell {
                width: min(100% - 16px, 1320px);
                margin-top: 92px;
                padding: 18px;
                border-radius: 24px;
            }

            .top-nav {
                top: 18px;
                right: 16px;
                left: 16px;
                justify-content: flex-end;
                flex-wrap: wrap;
            }

            .hero h1 {
                line-height: 1;
            }

            .city-grid {
                grid-template-columns: 1fr;
            }

            .preview-meta,
            .hint-row,
            .section-title {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body>
<%
    session = request.getSession(false);
    boolean loggedIn = SessionAuthUtil.isLoggedIn(session);
    List<CityBean> cities = (List<CityBean>) request.getAttribute("cities2");
%>

<% if (!loggedIn) { %>
<div class="top-nav">
    <a href="index.jsp">Home</a>
    <a href="login.jsp">Login</a>
</div>
<% } else { %>
<div class="top-nav">
    <a href="indexLogged.jsp">Home</a>
    <a href="logout">Logout</a>
    <a href="areaRiservata">Area riservata</a>
</div>
<% } %>

<div class="page-shell">
    <section class="hero">
        <div class="hero-copy">
            <div class="eyebrow">Urban Metro Nexus</div>
            <h1>Choose a city.<br>Launch the network.</h1>
            <p>
                Entra in una mappa urbana immersiva: seleziona il nodo metropolitano come se fosse un pianeta,
                visualizza subito la rete disponibile e prepara il tuo percorso nel cuore della mobilita urbana.
            </p>
        </div>

        <aside class="preview-panel">
            <div class="preview-meta">
                <div>
                    <strong id="previewTitle">Metro constellation ready</strong>
                    <span id="previewSubtitle">Select a city planet to activate its map.</span>
                </div>
                <div class="status-pill" id="previewStatus">Awaiting selection</div>
            </div>

            <div class="map-frame">
                <img id="mapImage" src="images/subway_default.png" alt="Metro Map">
                <div class="map-overlay">
                    <span id="overlayLabel">Transit grid standby</span>
                    <span id="overlayStations">0 stations loaded</span>
                </div>
            </div>
        </aside>
    </section>

    <section class="orbit-section">
        <div class="city-panel">
            <div class="section-title">
                <div>
                    <h2>City Galaxy</h2>
                    <p>Clicca su un pianeta per scegliere la rete metropolitana da esplorare.</p>
                </div>
            </div>

            <div class="city-grid">
                <%
                    if (cities != null && !cities.isEmpty()) {
                        for (CityBean c : cities) {
                %>
                <button
                        type="button"
                        class="city-card"
                        data-city="<%= c.getName() %>">
                    <div class="planet-wrap">
                        <div class="orbit-ring"></div>
                        <div class="planet"></div>
                    </div>
                    <h3><%= c.getName() %></h3>
                    <p class="city-description">Interconnected metro system ready for route exploration.</p>
                    <div class="city-stats">
                        <span class="city-badge">Click to activate</span>
                        <span class="city-count">0 stations</span>
                    </div>
                </button>
                <%
                        }
                    } else {
                %>
                <div class="city-card active" style="cursor: default;">
                    <div class="planet-wrap">
                        <div class="orbit-ring"></div>
                        <div class="planet"></div>
                    </div>
                    <h3>No cities available</h3>
                    <p class="city-description">Il catalogo delle reti metropolitane non e disponibile al momento.</p>
                    <div class="city-stats">
                        <span class="city-badge">Unavailable</span>
                        <span class="city-count">0 stations</span>
                    </div>
                </div>
                <%
                    }
                %>
            </div>
        </div>

        <div class="form-panel">
            <div class="section-title">
                <div>
                    <h2>Build Route</h2>
                    <p>Una volta selezionata la citta, inserisci stazione di partenza e arrivo.</p>
                </div>
            </div>

            <form action="PathControllerGrafico" method="post" name="select" autocomplete="off">
                <input type="hidden" name="city" id="cityInput">

                <div class="selected-city-banner">
                    <strong id="selectedCityLabel">No city selected</strong>
                    <span id="selectedCityHint">Select one of the animated planets to unlock the route builder.</span>
                </div>

                <div class="field-group">
                    <label for="startSearchBox">Start station</label>
                    <input type="text" name="startStation" id="startSearchBox" placeholder="Select a city first" disabled>
                    <ul id="startSuggestions" class="suggestions-box" aria-label="Start station suggestions"></ul>
                </div>

                <div class="field-group">
                    <label for="endSearchBox">Arrival station</label>
                    <input type="text" name="endStation" id="endSearchBox" placeholder="Select a city first" disabled>
                    <ul id="endSuggestions" class="suggestions-box" aria-label="Arrival station suggestions"></ul>
                </div>

                <div class="hint-row">
                    <span id="stationsHint">No station catalog loaded.</span>
                    <span>Tip: i suggerimenti appaiono sotto mentre scrivi.</span>
                </div>

                <button type="submit" id="submitBtn" class="submit-btn" disabled>Find Route</button>

                <div class="logo-mark">
                    <img src="images/logo-no-background.png" alt="RouteX">
                </div>
            </form>
        </div>
    </section>
</div>

<script>
    const cityStations = {
        "Rome": [
            "Battistini", "Cornelia", "Baldo degli Ubaldi", "Valle Aurelia", "Cipro",
            "Ottaviano", "Lepanto", "Flaminio", "Spagna", "Barberini", "Repubblica",
            "Termini", "Vittorio Emanuele", "Manzoni", "San Giovanni", "Re di Roma",
            "Ponte Lungo", "Furio Camillo", "Colli Albani", "Arco di Travertino",
            "Porta Furba", "Numidio Quadrato", "Lucio Sestio", "Giulio Agricola",
            "Subaugusta", "Cinecitta", "Anagnina", "Pantano", "Graniti",
            "Finocchio", "Bolognetta", "Borghesiana", "Due Leoni - Fontana Candida", "Grotte Celoni",
            "Torre Gaia", "Torre Angela", "Torrenova", "Giardinetti", "Torre Maura",
            "Torre Spaccata", "Alessandrino", "Parco di Centocelle", "Mirti",
            "Gardenie", "Teano", "Malatesta", "Pigneto", "Lodi", "San Giovanni", "Laurentina",
            "EUR Fermi", "EUR Palasport", "EUR Magliana", "Marconi", "Basilica S. Paolo",
            "Garbatella", "Piramide", "Circo Massimo", "Colosseo", "Cavour", "Termini",
            "Castro Pretorio", "Policlinico", "Bologna", "Tiburtina FS", "Quintiliani",
            "Monti Tiburtini", "Pietralata", "Santa Maria del Soccorso", "Ponte Mammolo",
            "Rebibbia", "Annibaliano", "Libia", "Conca D oro", "Jonio"
        ],
        "Naples": [
            "Pozzuoli Solfatara", "Bagnoli-Agnano Terme", "Campi Flegrei", "Mostra", "P.Leopardi", "Augusto", "Lala",
            "Mergellina", "Arco Mirelli", "San Pasquale", "Chiaia", "P.Amedeo", "Montesanto", "Museo-Piazza Cavour",
            "Dante", "Toledo", "Municipio", "Universita", "Duomo", "Garibaldi", "Gianturco", "S.Giovanni-Barra",
            "Materdei", "Salvator Rosa", "Quattro Giornate", "Vanvitelli", "Medaglie D'Oro", "Montedonzelli",
            "Rione Alto", "Policlinico", "Colli Aminei", "Frullone", "Chiaiano", "Piscinola", "Mugnano", "Giugliano",
            "Aversa Ippodromo", "Aversa Centro", "Cavalleggeri Aosta"
        ],
        "Athens": [
            "Airport", "Koropi", "Paiania-Kantza", "Pallini", "Doukissis Plakentias", "Halandri",
            "Aghia Paraskevi", "Nomismatokopio", "Holargos", "Ethniki Amyna", "Katehaki", "Panormou",
            "Ampelokipi", "Megaro Moussikis", "Evangelismos", "Syntagma", "Panipistimo", "Omonia",
            "Victoria", "Attiki", "Aghios Nikolaos", "Kato Patissia", "Aghios Eleftherios", "Ano Patissia",
            "Perissos", "Pefkakia", "Nea Ionia", "Iraklio", "Irini", "Neratziotissa", "Maroussi", "KAT",
            "Kifissia", "Akropoli", "Syngrou Fix", "Aghios Ioannis", "Dafni", "Aghios Dimitrios", "Illioupoli",
            "Alimos", "Argyroupoli", "Elliniko", "Monastiraki", "Thissio", "Petralona", "Tavros", "Kallithea",
            "Moschato", "Faliro", "Piraeus", "Dimotiko Theatro", "Maniatika", "Nikaia", "Korydallos",
            "Aghia Varvara", "Aghia Marina", "Egaleo", "Eleonas", "Kerameikos", "Metaxourghio",
            "Larissa Station", "Sepolia", "Aghios Antonios", "Peristeri", "Anthoupoli", "Neos Kosmos"
        ],
        "Budapest": [
            "Ors vezer tere", "Pillango utca", "Puskas Ferenc Stadion", "Keleti Palyaudvar", "Blaha Lujza Ter",
            "Il. Janos Pal Papa Ter", "Rakoczi Ter", "Kalvin Ter", "Fovam Ter", "Szent Gellert Ter - Muegyetem",
            "Moricz Zsigmond Korter", "Ujbuda-kozport", "Bikas Park", "Kelenfood Vasutallomas", "Kobanya-Kispes",
            "Hatar Ut", "Pottyos Utca", "Ecseri Ut", "Nepliget", "Nagyvarad Ter", "Semmelweis Klinikak",
            "Corvin-negyed", "Ferenciek Tere", "Deak Ferenc Ter", "Vorosmarty Ter", "Bajcsy-Zsilinszky ut", "Opera",
            "Oktogon", "Vorosmarty Utca", "Kodaly Korond", "Bajza Utca", "Hosok Tere", "Szechenyi-furdo",
            "Mexikoi Ut", "Ujpest-Kozpont", "Ujpest-Varoskapu", "Gyongyosi Utca", "Forgach Utca",
            "Goncz Arpad Vkp", "Dozsa Gyorgy Ut", "Lehel Ter", "Nyugati Palyaudva", "Arany Janos Utca",
            "Kossuth Lajos Ter", "Battyhany Ter", "Szell Kalman Ter", "Deli Palyaudvar", "Astoria"
        ]
    };

    const cityTheme = {
        "Rome": {
            core: "#ffd66b",
            edge: "#ff6f3c",
            glow: "rgba(255, 140, 72, 0.55)",
            description: "Historic underground arteries with luminous interchange hubs.",
            badge: "Imperial lines"
        },
        "Naples": {
            core: "#7cf5ff",
            edge: "#0a93ff",
            glow: "rgba(10, 147, 255, 0.58)",
            description: "Mediterranean metro energy with art stations and volcanic pulse.",
            badge: "Coastal flow"
        },
        "Athens": {
            core: "#fff1a2",
            edge: "#39c8a4",
            glow: "rgba(57, 200, 164, 0.58)",
            description: "Ancient city layers connected by fast modern transit corridors.",
            badge: "Aegean grid"
        },
        "Budapest": {
            core: "#f7a7ff",
            edge: "#7d66ff",
            glow: "rgba(125, 102, 255, 0.58)",
            description: "Danube-side routes with compact interchanges and high urban cadence.",
            badge: "Danube pulse"
        }
    };

    const cityCards = document.querySelectorAll(".city-card[data-city]");
    const cityInput = document.getElementById("cityInput");
    const startInput = document.getElementById("startSearchBox");
    const endInput = document.getElementById("endSearchBox");
    const startSuggestions = document.getElementById("startSuggestions");
    const endSuggestions = document.getElementById("endSuggestions");
    const submitBtn = document.getElementById("submitBtn");
    const mapImage = document.getElementById("mapImage");
    const previewTitle = document.getElementById("previewTitle");
    const previewSubtitle = document.getElementById("previewSubtitle");
    const previewStatus = document.getElementById("previewStatus");
    const overlayLabel = document.getElementById("overlayLabel");
    const overlayStations = document.getElementById("overlayStations");
    const selectedCityLabel = document.getElementById("selectedCityLabel");
    const selectedCityHint = document.getElementById("selectedCityHint");
    const stationsHint = document.getElementById("stationsHint");

    function getStationsForCurrentCity() {
        return cityStations[cityInput.value] || [];
    }

    function hideSuggestions(listElement) {
        listElement.innerHTML = "";
        listElement.classList.remove("visible");
    }

    function hideAllSuggestions() {
        hideSuggestions(startSuggestions);
        hideSuggestions(endSuggestions);
    }

    function escapeHtml(text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/\"/g, "&quot;")
            .replace(/'/g, "&#39;");
    }

    function buildSuggestionMarkup(station, query) {
        if (!query) {
            return escapeHtml(station);
        }

        const safeStation = escapeHtml(station);
        const safeQuery = escapeHtml(query);
        const pattern = new RegExp("^(" + safeQuery.replace(/[.*+?^${}()|[\]\\]/g, "\\$&") + ")", "i");
        return safeStation.replace(pattern, "<mark>$1</mark>");
    }

    function renderSuggestions(input, listElement) {
        const query = input.value.trim();
        const stations = getStationsForCurrentCity();

        if (!query) {
            hideSuggestions(listElement);
            refreshSubmitState();
            return;
        }

        const matches = stations
            .filter((station) => station.toLowerCase().startsWith(query.toLowerCase()))
            .slice(0, 8);

        if (matches.length === 0) {
            hideSuggestions(listElement);
            refreshSubmitState();
            return;
        }

        listElement.innerHTML = "";
        matches.forEach((station, index) => {
            const item = document.createElement("li");
            const button = document.createElement("button");
            button.type = "button";
            button.className = "suggestion-item" + (index === 0 ? " active" : "");
            button.dataset.station = station;
            button.innerHTML = buildSuggestionMarkup(station, query);
            button.addEventListener("click", () => {
                input.value = station;
                hideSuggestions(listElement);
                refreshSubmitState();
            });
            item.appendChild(button);
            listElement.appendChild(item);
        });

        if (listElement === startSuggestions) {
            hideSuggestions(endSuggestions);
        } else {
            hideSuggestions(startSuggestions);
        }

        listElement.classList.add("visible");
        refreshSubmitState();
    }

    function refreshSubmitState() {
        const ready = cityInput.value.trim() !== "" &&
            startInput.value.trim() !== "" &&
            endInput.value.trim() !== "";
        submitBtn.disabled = !ready;
    }

    function activateCity(card) {
        const city = card.dataset.city;
        const theme = cityTheme[city] || {
            core: "#a7e4ff",
            edge: "#3f87ff",
            glow: "rgba(63, 135, 255, 0.55)",
            description: "Metro network ready for route exploration.",
            badge: "Transit network"
        };

        cityCards.forEach((item) => {
            item.classList.toggle("active", item === card);
        });

        cityInput.value = city;
        const stations = getStationsForCurrentCity();
        startInput.disabled = false;
        endInput.disabled = false;
        startInput.placeholder = "Start typing a departure station";
        endInput.placeholder = "Start typing an arrival station";
        startInput.value = "";
        endInput.value = "";
        hideAllSuggestions();

        mapImage.src = "images/metro-" + city.toLowerCase() + ".jpg";
        mapImage.alt = city + " Metro Map";

        previewTitle.textContent = city + " metro network online";
        previewSubtitle.textContent = theme.description;
        previewStatus.textContent = "City activated";
        overlayLabel.textContent = city + " transit view";
        overlayStations.textContent = stations.length + " stations loaded";
        selectedCityLabel.textContent = city + " selected";
        selectedCityHint.textContent = "Insert departure and destination to compute the best route.";
        stationsHint.textContent = stations.length + " stations available for " + city + ".";

        card.querySelector(".city-description").textContent = theme.description;
        card.querySelector(".city-badge").textContent = theme.badge;
        refreshSubmitState();
    }

    cityCards.forEach((card) => {
        const city = card.dataset.city;
        const theme = cityTheme[city];
        const stations = cityStations[city] || [];
        const planet = card.querySelector(".planet");
        const description = card.querySelector(".city-description");
        const badge = card.querySelector(".city-badge");
        const count = card.querySelector(".city-count");

        if (planet && theme) {
            planet.style.setProperty("--planet-core", theme.core);
            planet.style.setProperty("--planet-edge", theme.edge);
            planet.style.setProperty("--planet-glow", theme.glow);
        }

        if (description && theme) {
            description.textContent = theme.description;
        }

        if (badge && theme) {
            badge.textContent = theme.badge;
        }

        if (count) {
            count.textContent = stations.length + " stations";
        }

        card.addEventListener("click", () => activateCity(card));
    });

    startInput.addEventListener("input", refreshSubmitState);
    endInput.addEventListener("input", refreshSubmitState);

    startInput.addEventListener("input", () => renderSuggestions(startInput, startSuggestions));
    endInput.addEventListener("input", () => renderSuggestions(endInput, endSuggestions));

    startInput.addEventListener("focus", () => renderSuggestions(startInput, startSuggestions));
    endInput.addEventListener("focus", () => renderSuggestions(endInput, endSuggestions));

    document.addEventListener("click", (event) => {
        if (!event.target.closest(".field-group")) {
            hideAllSuggestions();
        }
    });
</script>
</body>
</html>
