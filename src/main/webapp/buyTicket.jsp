<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <meta charset="UTF-8">
    <title>RouteX - Buy Ticket</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.82);
            --panel-soft: rgba(10, 26, 47, 0.9);
            --line: rgba(111, 247, 255, 0.2);
            --text: #ecf7ff;
            --muted: #8ba5be;
            --accent: #6ff7ff;
            --accent-2: #53a9ff;
            --accent-3: #89ffd1;
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
                radial-gradient(circle, rgba(111,247,255,0.45) 1px, transparent 1px);
            background-size: 150px 150px, 230px 230px;
            background-position: 0 0, 60px 90px;
            opacity: 0.18;
            pointer-events: none;
            animation: drift 18s linear infinite;
        }

        .shell {
            width: min(1320px, calc(100% - 32px));
            margin: 24px auto;
            min-height: calc(100vh - 48px);
            padding: 28px;
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
            width: 420px;
            height: 420px;
            right: -120px;
            bottom: -160px;
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
            display: grid;
            grid-template-columns: 1.04fr 0.96fr;
            gap: 24px;
            align-items: start;
            position: relative;
            z-index: 1;
        }

        .hero-copy .eyebrow,
        .section-eyebrow {
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

        .hero-copy h1 {
            margin: 18px 0 16px;
            font-size: clamp(2.8rem, 5vw, 5rem);
            line-height: 0.92;
        }

        .hero-copy p {
            margin: 0;
            color: var(--muted);
            line-height: 1.8;
            max-width: 700px;
        }

        .signal-grid {
            margin-top: 28px;
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 14px;
        }

        .signal-card {
            padding: 18px;
            border-radius: 24px;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.08);
        }

        .signal-card strong {
            display: block;
            font-size: 1.4rem;
            margin-bottom: 6px;
        }

        .signal-card span {
            color: var(--muted);
            line-height: 1.6;
            font-size: 0.9rem;
        }

        .ticket-panel {
            background: var(--panel-soft);
            border: 1px solid var(--line);
            border-radius: 30px;
            padding: 24px;
        }

        .ticket-panel h2 {
            margin: 16px 0 8px;
            font-size: 1.8rem;
        }

        .ticket-panel p {
            margin: 0;
            color: var(--muted);
            line-height: 1.7;
        }

        .city-grid {
            margin-top: 22px;
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 16px;
        }

        .city-card {
            position: relative;
            border: 1px solid rgba(255,255,255,0.08);
            border-radius: 24px;
            min-height: 210px;
            padding: 18px;
            background:
                linear-gradient(160deg, rgba(255,255,255,0.04), rgba(255,255,255,0.01)),
                rgba(7, 18, 32, 0.78);
            cursor: pointer;
            transition: transform 0.3s ease, border-color 0.3s ease, box-shadow 0.3s ease;
            overflow: hidden;
        }

        .city-card:hover {
            transform: translateY(-6px);
            border-color: rgba(111,247,255,0.4);
            box-shadow: 0 24px 48px rgba(0,0,0,0.28);
        }

        .city-card.active {
            border-color: rgba(111,247,255,0.8);
            box-shadow: 0 0 0 1px rgba(111,247,255,0.18), 0 24px 54px rgba(21,124,255,0.22);
        }

        .planet-wrap {
            position: relative;
            height: 110px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 14px;
        }

        .orbit-ring,
        .orbit-ring::before {
            position: absolute;
            border-radius: 50%;
            border: 1px solid rgba(255,255,255,0.14);
            content: "";
        }

        .orbit-ring {
            width: 118px;
            height: 118px;
            animation: rotateRing 18s linear infinite;
        }

        .orbit-ring::before {
            inset: 10px;
            border-color: rgba(111,247,255,0.22);
            animation: pulse 5s ease-in-out infinite;
        }

        .planet {
            width: 72px;
            height: 72px;
            border-radius: 50%;
            background: radial-gradient(circle at 30% 30%, rgba(255,255,255,0.95), var(--planet-core) 38%, var(--planet-edge));
            box-shadow: 0 0 0 8px rgba(255,255,255,0.03), 0 0 36px var(--planet-glow);
            position: relative;
            animation: levitate 5.5s ease-in-out infinite;
        }

        .planet::after {
            content: "";
            position: absolute;
            width: 102px;
            height: 18px;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%) rotate(-18deg);
            border-radius: 50%;
            border: 2px solid rgba(255,255,255,0.22);
            opacity: 0.72;
        }

        .city-card h3 { margin: 0; font-size: 1.24rem; }
        .city-card p { margin: 8px 0 0; color: var(--muted); line-height: 1.5; font-size: 0.92rem; }

        .city-meta {
            margin-top: 14px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            font-size: 0.84rem;
            color: #d9eeff;
        }

        .city-badge {
            padding: 6px 10px;
            border-radius: 999px;
            background: rgba(255,255,255,0.06);
            border: 1px solid rgba(255,255,255,0.08);
        }

        .purchase-form {
            margin-top: 22px;
            display: grid;
            gap: 16px;
        }

        .selected-banner {
            padding: 16px 18px;
            border-radius: 20px;
            border: 1px solid rgba(111,247,255,0.18);
            background: linear-gradient(135deg, rgba(111,247,255,0.08), rgba(83,169,255,0.06));
        }

        .selected-banner strong {
            display: block;
            font-size: 1rem;
            margin-bottom: 4px;
        }

        .selected-banner span {
            color: var(--muted);
            font-size: 0.92rem;
        }

        .field-group label {
            display: block;
            margin-bottom: 8px;
            color: #dff8ff;
            font-size: 0.92rem;
            letter-spacing: 0.04em;
        }

        .quantity-shell {
            display: grid;
            grid-template-columns: 56px 1fr 56px;
            gap: 12px;
            align-items: center;
        }

        .quantity-shell input {
            width: 100%;
            padding: 16px 18px;
            border-radius: 18px;
            border: 1px solid rgba(255,255,255,0.12);
            background: rgba(255,255,255,0.05);
            color: var(--text);
            font-size: 1rem;
            text-align: center;
            outline: none;
            transition: border-color 0.25s ease, box-shadow 0.25s ease;
        }

        .quantity-shell input:focus {
            border-color: rgba(111,247,255,0.55);
            box-shadow: 0 0 0 4px rgba(111,247,255,0.08);
        }

        .quantity-btn {
            height: 56px;
            border: 1px solid rgba(255,255,255,0.12);
            border-radius: 18px;
            background: rgba(255,255,255,0.06);
            color: var(--text);
            font-size: 1.5rem;
            cursor: pointer;
            transition: transform 0.25s ease, border-color 0.25s ease, background 0.25s ease;
        }

        .quantity-btn:hover {
            transform: translateY(-2px);
            border-color: rgba(111,247,255,0.4);
            background: rgba(111,247,255,0.08);
        }

        .hint-row {
            display: flex;
            justify-content: space-between;
            gap: 12px;
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
            background: linear-gradient(90deg, var(--accent), var(--accent-3) 52%, #8dd8ff);
            cursor: pointer;
            box-shadow: 0 20px 34px rgba(111,247,255,0.22);
            transition: transform 0.25s ease, box-shadow 0.25s ease, filter 0.25s ease;
        }

        .submit-btn:hover:not(:disabled) {
            transform: translateY(-3px) scale(1.01);
            box-shadow: 0 24px 42px rgba(111,247,255,0.28);
        }

        .submit-btn:disabled {
            opacity: 0.45;
            cursor: not-allowed;
            filter: grayscale(0.2);
            box-shadow: none;
        }

        @keyframes drift {
            0% { transform: translateY(0); }
            50% { transform: translateY(-14px); }
            100% { transform: translateY(0); }
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
            .hero, .signal-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 720px) {
            .shell {
                width: min(100% - 16px, 1320px);
                margin: 8px auto;
                min-height: calc(100vh - 16px);
                padding: 18px;
                border-radius: 24px;
            }

            .topbar {
                flex-direction: column;
                align-items: flex-start;
            }

            .city-grid {
                grid-template-columns: 1fr;
            }

            .quantity-shell {
                grid-template-columns: 48px 1fr 48px;
            }
        }
    </style>
</head>
<body>
<%@ include file="header.jspf" %>
<%
    boolean loggedIn = SessionAuthUtil.isLoggedIn(session);
    List<CityBean> cities = (List<CityBean>) request.getAttribute("cities");
%>
<div class="shell">
    <div class="topbar">
        <div class="brand">
            <img src="images/logo-no-background.png" alt="RouteX">
            <div>
                <strong>RouteX</strong>
                <span>Futuristic ticket purchase console</span>
            </div>
        </div>

        <div class="nav-actions">
            <a href="<%= loggedIn ? "indexLogged.jsp" : "index.jsp" %>">Home</a>
            <% if (loggedIn) { %>
            <a href="logout">Logout</a>
            <% } else { %>
            <a href="login.jsp">Login</a>
            <% } %>
        </div>
    </div>

    <section class="hero">
        <div class="hero-copy">
            <div class="eyebrow">Ticket Nexus</div>
            <h1>Choose a city planet.<br>Generate your ride pass.</h1>
            <p>
                La selezione del biglietto entra nello stesso universo visivo di RouteX:
                scegli la rete metropolitana tramite pianeti interattivi, imposta la quantità e conferma l'acquisto
                con un flusso più coerente, più scenico e più urbano.
            </p>

            <div class="signal-grid">
                <div class="signal-card">
                    <strong>City Driven</strong>
                    <span>La città non si sceglie più da una select, ma da un hub orbitale interattivo.</span>
                </div>
                <div class="signal-card">
                    <strong>Quick Purchase</strong>
                    <span>Conferma diretta del numero di biglietti senza cambiare il contratto lato server.</span>
                </div>
                <div class="signal-card">
                    <strong>Unified Design</strong>
                    <span>Stesso linguaggio visivo delle pagine di esplorazione e routing.</span>
                </div>
            </div>
        </div>

        <aside class="ticket-panel">
            <div class="section-eyebrow">Metro Ticket Builder</div>
            <h2>Buy Your Ticket</h2>
            <p>Attiva una città, imposta la quantità e conferma l'operazione.</p>

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
                    <p class="city-description">Metro ticket network ready for purchase.</p>
                    <div class="city-meta">
                        <span class="city-badge">Click to select</span>
                        <span>Urban access</span>
                    </div>
                </button>
                <%      }
                    } else { %>
                <div class="city-card active" style="cursor: default;">
                    <div class="planet-wrap">
                        <div class="orbit-ring"></div>
                        <div class="planet"></div>
                    </div>
                    <h3>No cities available</h3>
                    <p class="city-description">Il catalogo città non è disponibile al momento.</p>
                    <div class="city-meta">
                        <span class="city-badge">Unavailable</span>
                        <span>0 networks</span>
                    </div>
                </div>
                <% } %>
            </div>

            <form action="buyTicket" method="post" class="purchase-form" autocomplete="off">
                <input type="hidden" name="city" id="cityInput">

                <div class="selected-banner">
                    <strong id="selectedCityLabel">No city selected</strong>
                    <span id="selectedCityHint">Select one of the animated planets to enable ticket configuration.</span>
                </div>

                <div class="field-group">
                    <label for="quantity">Number of Tickets</label>
                    <div class="quantity-shell">
                        <button type="button" class="quantity-btn" id="decreaseBtn">-</button>
                        <input type="text" id="quantity" name="quantity" value="1" inputmode="numeric" pattern="[0-9]*" disabled>
                        <button type="button" class="quantity-btn" id="increaseBtn">+</button>
                    </div>
                </div>

                <div class="hint-row">
                    <span id="purchaseHint">Choose a city to unlock the ticket amount.</span>
                    <span>Tip: puoi anche digitare il numero manualmente.</span>
                </div>

                <button type="submit" class="submit-btn" id="submitBtn" disabled>Confirm Purchase</button>
            </form>
        </aside>
    </section>
</div>

<script>
    const cityTheme = {
        "Rome": {
            core: "#ffd66b",
            edge: "#ff6f3c",
            glow: "rgba(255, 140, 72, 0.55)",
            description: "Historic underground arteries with premium urban coverage.",
            badge: "Imperial access"
        },
        "Naples": {
            core: "#7cf5ff",
            edge: "#0a93ff",
            glow: "rgba(10, 147, 255, 0.58)",
            description: "Mediterranean lines with high-energy inner city mobility.",
            badge: "Coastal access"
        },
        "Athens": {
            core: "#fff1a2",
            edge: "#39c8a4",
            glow: "rgba(57, 200, 164, 0.58)",
            description: "Ancient routes and modern corridors connected by rapid transit.",
            badge: "Aegean access"
        },
        "Budapest": {
            core: "#f7a7ff",
            edge: "#7d66ff",
            glow: "rgba(125, 102, 255, 0.58)",
            description: "Danube-side lines with compact interchanges and efficient flow.",
            badge: "Danube access"
        }
    };

    const cityCards = document.querySelectorAll(".city-card[data-city]");
    const cityInput = document.getElementById("cityInput");
    const quantityInput = document.getElementById("quantity");
    const submitBtn = document.getElementById("submitBtn");
    const increaseBtn = document.getElementById("increaseBtn");
    const decreaseBtn = document.getElementById("decreaseBtn");
    const selectedCityLabel = document.getElementById("selectedCityLabel");
    const selectedCityHint = document.getElementById("selectedCityHint");
    const purchaseHint = document.getElementById("purchaseHint");

    function normalizeQuantity() {
        const digitsOnly = quantityInput.value.replace(/\D/g, "");
        if (digitsOnly === "") {
            quantityInput.value = "";
            refreshSubmitState();
            return;
        }

        const normalized = Math.max(1, parseInt(digitsOnly, 10));
        quantityInput.value = String(normalized);
        refreshSubmitState();
    }

    function refreshSubmitState() {
        const ready = cityInput.value.trim() !== "" &&
            quantityInput.value.trim() !== "" &&
            !quantityInput.disabled;
        submitBtn.disabled = !ready;
    }

    function activateCity(card) {
        const city = card.dataset.city;
        const theme = cityTheme[city] || {
            description: "Metro ticket network ready for purchase.",
            badge: "Transit access"
        };

        cityCards.forEach((item) => {
            item.classList.toggle("active", item === card);
        });

        cityInput.value = city;
        quantityInput.disabled = false;
        quantityInput.value = quantityInput.value.trim() === "" ? "1" : quantityInput.value;
        selectedCityLabel.textContent = city + " selected";
        selectedCityHint.textContent = "Ticket quantity unlocked. Confirm the amount and continue.";
        purchaseHint.textContent = "Buying tickets for " + city + ".";

        card.querySelector(".city-description").textContent = theme.description;
        card.querySelector(".city-badge").textContent = theme.badge;
        refreshSubmitState();
    }

    cityCards.forEach((card) => {
        const city = card.dataset.city;
        const theme = cityTheme[city];
        const planet = card.querySelector(".planet");
        const description = card.querySelector(".city-description");
        const badge = card.querySelector(".city-badge");

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

        card.addEventListener("click", () => activateCity(card));
    });

    increaseBtn.addEventListener("click", () => {
        if (quantityInput.disabled) {
            return;
        }
        const current = parseInt(quantityInput.value || "0", 10);
        quantityInput.value = String(Math.max(1, current + 1));
        refreshSubmitState();
    });

    decreaseBtn.addEventListener("click", () => {
        if (quantityInput.disabled) {
            return;
        }
        const current = parseInt(quantityInput.value || "1", 10);
        quantityInput.value = String(Math.max(1, current - 1));
        refreshSubmitState();
    });

    quantityInput.addEventListener("input", normalizeQuantity);
    refreshSubmitState();
</script>
</body>
</html>
