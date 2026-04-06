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
            --bg: #f3f5f8;
            --surface: #ffffff;
            --surface-soft: #f8fafc;
            --border: #d8e0ea;
            --text: #16202a;
            --muted: #64748b;
            --accent: #0f6dff;
            --accent-dark: #0b5fe0;
            --shadow: 0 18px 40px rgba(15, 23, 42, 0.06);
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            background: var(--bg);
            color: var(--text);
            font-family: "Segoe UI", "Helvetica Neue", Arial, sans-serif;
        }

        .top-nav {
            width: min(1180px, calc(100% - 32px));
            margin: 20px auto 0;
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }

        .top-nav a {
            text-decoration: none;
            color: var(--text);
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: 999px;
            padding: 10px 16px;
            font-weight: 600;
        }

        .page-shell {
            width: min(1180px, calc(100% - 32px));
            margin: 16px auto 24px;
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: 24px;
            box-shadow: var(--shadow);
            padding: 28px;
        }

        .hero {
            display: grid;
            grid-template-columns: minmax(0, 1.05fr) minmax(340px, 0.95fr);
            gap: 24px;
            align-items: start;
        }

        .hero-copy {
            padding-right: 8px;
        }

        .eyebrow {
            display: inline-block;
            padding: 7px 12px;
            border-radius: 999px;
            border: 1px solid #cfe0ff;
            background: #eaf1ff;
            color: var(--accent);
            font-size: 0.75rem;
            font-weight: 700;
            letter-spacing: 0.08em;
            text-transform: uppercase;
        }

        .hero h1 {
            margin: 18px 0 14px;
            font-size: clamp(2.3rem, 4vw, 3.8rem);
            line-height: 1.02;
            letter-spacing: -0.03em;
        }

        .hero p {
            margin: 0;
            max-width: 620px;
            color: var(--muted);
            font-size: 1rem;
            line-height: 1.7;
        }

        .preview-panel {
            background: var(--surface-soft);
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 18px;
        }

        .preview-meta {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 12px;
            margin-bottom: 14px;
        }

        .preview-meta strong {
            display: block;
            font-size: 1.1rem;
            margin-bottom: 4px;
        }

        .preview-meta span {
            color: var(--muted);
            line-height: 1.5;
            font-size: 0.92rem;
        }

        .status-pill {
            padding: 8px 12px;
            border-radius: 999px;
            border: 1px solid #cfe0ff;
            background: #eaf1ff;
            color: var(--accent);
            white-space: nowrap;
            font-size: 0.85rem;
            font-weight: 600;
        }

        .map-frame {
            position: relative;
            overflow: hidden;
            border-radius: 18px;
            border: 1px solid var(--border);
            background: #eef3f8;
            aspect-ratio: 1.25 / 1;
        }

        .map-frame img {
            width: 100%;
            height: 100%;
            object-fit: contain;
            display: block;
            background: #eef3f8;
        }

        .map-overlay {
            position: absolute;
            left: 0;
            right: 0;
            bottom: 0;
            display: flex;
            justify-content: space-between;
            gap: 12px;
            padding: 12px 14px;
            background: linear-gradient(180deg, transparent, rgba(255,255,255,0.92));
            font-size: 0.86rem;
            color: var(--muted);
        }

        .orbit-section {
            display: grid;
            grid-template-columns: minmax(0, 1.1fr) minmax(320px, 0.9fr);
            gap: 20px;
            margin-top: 24px;
        }

        .city-panel,
        .form-panel {
            background: var(--surface-soft);
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 20px;
        }

        .section-title {
            margin-bottom: 16px;
        }

        .section-title h2 {
            margin: 0 0 6px;
            font-size: 1.4rem;
        }

        .section-title p {
            margin: 0;
            color: var(--muted);
            line-height: 1.6;
        }

        .city-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 14px;
        }

        .city-card {
            border: 1px solid var(--border);
            background: #ffffff;
            border-radius: 18px;
            padding: 18px;
            text-align: left;
            cursor: pointer;
            min-height: 0;
        }

        .city-card:hover {
            border-color: #bfd1e4;
            background: #fcfdff;
        }

        .city-card.active {
            border-color: var(--accent);
            box-shadow: 0 0 0 3px rgba(15, 109, 255, 0.08);
        }

        .planet-wrap {
            width: 48px;
            height: 48px;
            margin-bottom: 14px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .orbit-ring {
            position: absolute;
            width: 48px;
            height: 48px;
            border-radius: 50%;
            border: 1px solid #d4dfeb;
        }

        .planet {
            width: 22px;
            height: 22px;
            border-radius: 50%;
            background: var(--planet-edge, #0f6dff);
        }

        .city-card h3 {
            margin: 0 0 8px;
            font-size: 1.08rem;
        }

        .city-description {
            margin: 0;
            color: var(--muted);
            line-height: 1.55;
            font-size: 0.93rem;
            min-height: 44px;
        }

        .city-stats {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 10px;
            margin-top: 14px;
        }

        .city-badge {
            display: inline-block;
            padding: 6px 10px;
            border-radius: 999px;
            background: #eef4fb;
            color: var(--accent);
            font-size: 0.82rem;
            font-weight: 600;
        }

        .city-count {
            color: var(--muted);
            font-size: 0.84rem;
        }

        .selected-city-banner {
            margin-bottom: 16px;
            padding: 14px 16px;
            border-radius: 16px;
            border: 1px solid var(--border);
            background: #ffffff;
        }

        .selected-city-banner strong {
            display: block;
            margin-bottom: 4px;
            font-size: 1rem;
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
            font-size: 0.92rem;
            font-weight: 600;
        }

        .field-group input {
            width: 100%;
            padding: 14px 16px;
            border-radius: 14px;
            border: 1px solid var(--border);
            background: #ffffff;
            color: var(--text);
            font-size: 0.98rem;
            outline: none;
        }

        .field-group input:focus {
            border-color: #9ab8f8;
            box-shadow: 0 0 0 3px rgba(15, 109, 255, 0.08);
        }

        .field-group input::placeholder {
            color: #8b98a8;
        }

        .suggestions-box {
            position: absolute;
            top: calc(100% + 8px);
            left: 0;
            right: 0;
            margin: 0;
            padding: 8px;
            list-style: none;
            border-radius: 14px;
            border: 1px solid var(--border);
            background: #ffffff;
            box-shadow: var(--shadow);
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
            padding: 10px 12px;
            border: none;
            border-radius: 10px;
            background: transparent;
            color: var(--text);
            text-align: left;
            cursor: pointer;
        }

        .suggestion-item:hover,
        .suggestion-item.active {
            background: #eef4fb;
        }

        .suggestion-item mark {
            color: var(--accent);
            background: transparent;
            font-weight: 700;
        }

        .hint-row {
            display: flex;
            justify-content: space-between;
            gap: 12px;
            margin: 10px 0 18px;
            color: var(--muted);
            font-size: 0.84rem;
        }

        .submit-btn {
            width: 100%;
            padding: 15px 18px;
            border: 1px solid var(--accent);
            border-radius: 999px;
            background: var(--accent);
            color: #ffffff;
            font-size: 0.96rem;
            font-weight: 700;
            letter-spacing: 0.04em;
            text-transform: uppercase;
            cursor: pointer;
        }

        .submit-btn:hover:not(:disabled) {
            background: var(--accent-dark);
            border-color: var(--accent-dark);
        }

        .submit-btn:disabled {
            opacity: 0.45;
            cursor: not-allowed;
        }

        .logo-mark {
            margin-top: 18px;
            text-align: center;
        }

        .logo-mark img {
            max-width: 170px;
            width: 100%;
            opacity: 0.9;
        }

        @media (max-width: 980px) {
            .hero,
            .orbit-section {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 720px) {
            .top-nav,
            .page-shell {
                width: min(100% - 16px, 1180px);
            }

            .page-shell {
                padding: 18px;
                border-radius: 18px;
                margin-top: 8px;
            }

            .top-nav {
                flex-wrap: wrap;
                justify-content: flex-end;
            }

            .city-grid {
                grid-template-columns: 1fr;
            }

            .preview-meta,
            .hint-row {
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
    <a href="areaRiservata">Reserved Area</a>
</div>
<% } %>

<div class="page-shell">
    <section class="hero">
        <div class="hero-copy">
            <div class="eyebrow">Metro Search</div>
            <h1>Choose a city. Launch the network.</h1>
            <p>
                Select a metro network, preview the map instantly, and build your route
                with a cleaner, more readable interface and no unnecessary visual noise.
            </p>
        </div>

        <aside class="preview-panel">
            <div class="preview-meta">
                <div>
                    <strong id="previewTitle">Metro network ready</strong>
                    <span id="previewSubtitle">Select a city to activate its map.</span>
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
                <h2>Select city</h2>
                <p>Choose the metro network you want to explore.</p>
            </div>

            <div class="city-grid">
                <%
                    if (cities != null && !cities.isEmpty()) {
                        for (CityBean c : cities) {
                %>
                <button type="button" class="city-card" data-city="<%= c.getName() %>">
                    <div class="planet-wrap">
                        <div class="orbit-ring"></div>
                        <div class="planet"></div>
                    </div>
                    <h3><%= c.getName() %></h3>
                    <p class="city-description">Metro network ready for route exploration.</p>
                    <div class="city-stats">
                        <span class="city-badge">Select</span>
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
                    <p class="city-description">The metro network catalog is currently unavailable.</p>
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
                <h2>Build route</h2>
                <p>Enter your departure and arrival stations after choosing a city.</p>
            </div>

            <form action="PathControllerGrafico" method="post" name="select" autocomplete="off">
                <input type="hidden" name="city" id="cityInput">

                <div class="selected-city-banner">
                    <strong id="selectedCityLabel">No city selected</strong>
                    <span id="selectedCityHint">Select a city to unlock the route builder.</span>
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
                    <span>Suggestions appear while you type.</span>
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
            planet.style.background = theme.edge;
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
