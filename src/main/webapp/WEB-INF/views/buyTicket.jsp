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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>RouteX - Buy Ticket</title>
    <style>
        :root {
            --bg-1: #f4f7fb;
            --bg-2: #ecf2f8;
            --panel: #ffffff;
            --panel-soft: #f8fbfe;
            --line: #d8e1eb;
            --text: #17212b;
            --muted: #66788c;
            --accent: #0f6dff;
            --accent-2: #4fa2ff;
            --accent-3: #76d8ff;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Segoe UI", "Helvetica Neue", Arial, sans-serif;
            background:
                radial-gradient(circle at top left, rgba(15, 109, 255, 0.05), transparent 24%),
                radial-gradient(circle at top right, rgba(79, 162, 255, 0.05), transparent 24%),
                linear-gradient(180deg, var(--bg-1), var(--bg-2));
            overflow-x: hidden;
            position: relative;
        }

        body::before {
            content: "";
            position: fixed;
            inset: 0;
            background-image:
                radial-gradient(circle, rgba(255,255,255,0.72) 1px, transparent 1px),
                radial-gradient(circle, rgba(15,109,255,0.12) 1px, transparent 1px);
            background-size: 150px 150px, 230px 230px;
            background-position: 0 0, 60px 90px;
            opacity: 0.14;
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
            background: rgba(255, 255, 255, 0.92);
            box-shadow: 0 24px 60px rgba(15, 23, 42, 0.08);
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
            background: radial-gradient(circle, rgba(15, 109, 255, 0.08), transparent 66%);
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
            border: 1px solid var(--line);
            background: #ffffff;
            transition: transform 0.25s ease, border-color 0.25s ease;
        }

        .nav-actions a:hover {
            transform: translateY(-2px);
            border-color: #b7c9dd;
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
            border: 1px solid #cfe0ff;
            background: #eaf1ff;
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
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
        }

        .city-card {
            border: 1px solid var(--line);
            border-radius: 999px;
            min-height: 0;
            padding: 10px 16px;
            background: #ffffff;
            cursor: pointer;
            transition: transform 0.2s ease, border-color 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
            overflow: visible;
            width: auto;
            min-width: 0;
        }

        .city-card:hover {
            transform: translateY(-1px);
            border-color: #b7c9dd;
            box-shadow: 0 8px 18px rgba(15, 23, 42, 0.06);
        }

        .city-card.active {
            border-color: var(--accent);
            background: #eef4ff;
            box-shadow: 0 0 0 3px rgba(15,109,255,0.10);
        }

        .city-card h3 {
            margin: 0;
            font-size: 0.95rem;
            font-weight: 700;
            letter-spacing: -0.01em;
        }

        .city-description,
        .city-meta,
        .planet-wrap {
            display: none !important;
        }

        .purchase-form {
            margin-top: 22px;
            display: grid;
            gap: 16px;
        }

        .selected-banner {
            padding: 16px 18px;
            border-radius: 20px;
            border: 1px solid #d8e6fb;
            background: #f4f8ff;
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
            color: var(--text);
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
            border: 1px solid var(--line);
            background: #ffffff;
            color: var(--text);
            font-size: 1rem;
            text-align: center;
            outline: none;
            transition: border-color 0.25s ease, box-shadow 0.25s ease;
        }

        .quantity-shell input:focus {
            border-color: rgba(15,109,255,0.55);
            box-shadow: 0 0 0 4px rgba(15,109,255,0.08);
        }

        .quantity-btn {
            height: 56px;
            border: 1px solid var(--line);
            border-radius: 18px;
            background: #ffffff;
            color: var(--text);
            font-size: 1.5rem;
            cursor: pointer;
            transition: transform 0.25s ease, border-color 0.25s ease, background 0.25s ease;
        }

        .quantity-btn:hover {
            transform: translateY(-2px);
            border-color: #b7c9dd;
            background: #f6f9fd;
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
            color: #ffffff;
            font-weight: 700;
            font-size: 1rem;
            letter-spacing: 0.08em;
            text-transform: uppercase;
            background: linear-gradient(90deg, var(--accent), var(--accent-2));
            cursor: pointer;
            box-shadow: 0 16px 28px rgba(15,109,255,0.18);
            transition: transform 0.25s ease, box-shadow 0.25s ease, filter 0.25s ease;
        }

        .submit-btn:hover:not(:disabled) {
            transform: translateY(-3px) scale(1.01);
            box-shadow: 0 20px 34px rgba(15,109,255,0.24);
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

        @media (max-width: 1080px) {
            .hero {
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
                gap: 10px;
            }

            .quantity-shell {
                grid-template-columns: 48px 1fr 48px;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
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
            <a href="<%= loggedIn ? "travelerHome" : "index.jsp" %>">Home</a>
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
                Ticket selection now follows the same visual language as RouteX:
                choose a metro network through interactive planets, set the quantity,
                and confirm your purchase with a cleaner and more coherent flow.
            </p>
        </div>

        <aside class="ticket-panel">
            <div class="section-eyebrow">Metro Ticket Builder</div>
            <h2>Buy Your Ticket</h2>
            <p>Select a city, set the quantity, and confirm the purchase.</p>

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
                    <p class="city-description">The city catalog is currently unavailable.</p>
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
                    <span>Tip: you can also type the number manually.</span>
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
