<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
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

        .page {
            width: min(1120px, calc(100% - 32px));
            margin: 24px auto;
            min-height: calc(100vh - 48px);
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: 24px;
            box-shadow: var(--shadow);
            padding: 28px;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 14px;
        }

        .brand img {
            width: 64px;
            height: auto;
        }

        .brand strong {
            display: block;
            font-size: 1.2rem;
        }

        .brand span {
            display: block;
            margin-top: 2px;
            color: var(--muted);
            font-size: 0.94rem;
        }

        .topbar a {
            text-decoration: none;
            color: var(--text);
            background: var(--surface-soft);
            border: 1px solid var(--border);
            border-radius: 999px;
            padding: 10px 16px;
            font-weight: 600;
        }

        .hero {
            margin-top: 40px;
            display: grid;
            grid-template-columns: minmax(0, 1.2fr) minmax(280px, 0.8fr);
            gap: 28px;
            align-items: start;
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

        h1 {
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

        .actions {
            display: flex;
            gap: 14px;
            flex-wrap: wrap;
            margin-top: 26px;
        }

        .actions form {
            margin: 0;
        }

        .actions button,
        .actions a {
            border-radius: 999px;
            padding: 14px 20px;
            font-size: 0.96rem;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
        }

        .actions button {
            border: 1px solid var(--accent);
            background: var(--accent);
            color: #ffffff;
        }

        .actions button:hover {
            background: var(--accent-dark);
            border-color: var(--accent-dark);
        }

        .actions a {
            border: 1px solid var(--border);
            background: var(--surface-soft);
            color: var(--text);
        }

        .actions .info-button {
            border: 1px solid var(--border);
            background: transparent;
            color: var(--muted);
        }

        .actions .info-button:hover {
            border-color: #c5d2e0;
            color: var(--text);
            background: #f9fbfd;
        }

        .travel-note {
            margin: 0;
            color: var(--muted);
            font-size: 0.93rem;
            line-height: 1.6;
            max-width: 620px;
        }

        .travel-note strong {
            color: var(--text);
        }

        .travel-note-section {
            margin-top: 44px;
            padding-top: 24px;
            border-top: 1px solid rgba(216, 224, 234, 0.8);
        }

        .about-modal {
            position: fixed;
            inset: 0;
            display: none;
            align-items: center;
            justify-content: center;
            padding: 24px;
            background: rgba(15, 23, 42, 0.28);
            z-index: 1200;
        }

        .about-modal.is-open {
            display: flex;
        }

        .about-modal__panel {
            width: min(760px, 100%);
            background: rgba(255, 255, 255, 0.96);
            border: 1px solid rgba(216, 224, 234, 0.95);
            border-radius: 24px;
            box-shadow: 0 28px 60px rgba(15, 23, 42, 0.16);
            padding: 28px;
        }

        .about-modal__topbar {
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
            gap: 16px;
            margin-bottom: 18px;
        }

        .about-modal__eyebrow {
            display: inline-block;
            margin-bottom: 10px;
            color: var(--accent);
            font-size: 0.76rem;
            font-weight: 700;
            letter-spacing: 0.08em;
            text-transform: uppercase;
        }

        .about-modal__title {
            margin: 0;
            font-size: clamp(1.6rem, 3vw, 2.2rem);
            line-height: 1.08;
            letter-spacing: -0.03em;
        }

        .about-modal__close {
            flex: 0 0 auto;
            border: 1px solid var(--border);
            background: var(--surface-soft);
            color: var(--muted);
            border-radius: 999px;
            width: 38px;
            height: 38px;
            font-size: 1.15rem;
            cursor: pointer;
        }

        .about-modal__close:hover {
            color: var(--text);
            border-color: #c5d2e0;
        }

        .about-modal__content {
            display: grid;
            gap: 16px;
        }

        .about-modal__content p {
            margin: 0;
            color: var(--muted);
            line-height: 1.8;
            font-size: 0.98rem;
        }

        @media (max-width: 920px) {
            .hero {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 720px) {
            .page {
                width: min(100% - 16px, 1120px);
                margin: 8px auto;
                min-height: calc(100vh - 16px);
                padding: 18px;
                border-radius: 18px;
            }

            .topbar {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body>
<%@ include file="header.jspf" %>
<main class="page">
    <div class="topbar">
        <div class="brand">
            <img src="images/logo-no-background.png" alt="RouteX">
            <div>
                <strong>RouteX</strong>
                <span>Metro route planning, kept simple.</span>
            </div>
        </div>

        <a href="login.jsp">Login</a>
    </div>

    <section class="hero">
        <div>
            <div class="eyebrow">Metro Navigation</div>
            <h1>Find the shortest metro path with a cleaner interface.</h1>
            <p>
                RouteX lets you search for a route between two stations in a direct way,
                without visual distractions. Select the city, enter departure and arrival,
                and view the route.
            </p>

            <div class="actions">
                <form action="PathControllerGrafico" method="get">
                    <button type="submit">Start Exploring</button>
                </form>
                <a href="login.jsp">Open Reserved Area</a>
                <button type="button" class="info-button" data-open-about>What is RouteX?</button>
            </div>
        </div>
    </section>

    <section class="travel-note-section" aria-label="travel advice">
        <p class="travel-note">
            <strong>Travel tip:</strong> stay aware of pickpockets while using public transport, especially in crowded stations and trains.
        </p>
    </section>

</main>

<div class="about-modal" id="aboutModal" aria-hidden="true">
    <div class="about-modal__panel" role="dialog" aria-modal="true" aria-labelledby="aboutModalTitle">
        <div class="about-modal__topbar">
            <div>
                <span class="about-modal__eyebrow">About RouteX</span>
                <h2 class="about-modal__title" id="aboutModalTitle">RouteX, explained clearly.</h2>
            </div>
            <button type="button" class="about-modal__close" data-close-about aria-label="Close dialog">&times;</button>
        </div>

        <div class="about-modal__content">
            <p>
                RouteX is a powerful route finder that allows users to search for stations in real-time,
                select a city, and view an interactive metro map. Featuring dynamic autocomplete, RouteX
                helps travelers quickly find the best routes with ease.
            </p>
            <p>
                RouteX was developed by Lorenzo Brondi and Simone Remoli.
            </p>
            <p>
                The idea behind RouteX was conceived by Simone Remoli, a computer engineering student at the
                University of Rome Tor Vergata.
            </p>
            <p>
                As someone who has relied on public transportation for most of his daily commuting and has rarely
                used a car, he began to reflect on a simple yet meaningful question:
                "What would the world look like if every commuter had access to an easy-to-use application
                designed to simplify metropolitan travel?"
            </p>
        </div>
    </div>
</div>

<script>
    (function () {
        const modal = document.getElementById("aboutModal");
        const openButton = document.querySelector("[data-open-about]");
        const closeButton = modal ? modal.querySelector("[data-close-about]") : null;

        if (!modal || !openButton || !closeButton) {
            return;
        }

        const closeModal = function () {
            modal.classList.remove("is-open");
            modal.setAttribute("aria-hidden", "true");
        };

        openButton.addEventListener("click", function () {
            modal.classList.add("is-open");
            modal.setAttribute("aria-hidden", "false");
        });

        closeButton.addEventListener("click", closeModal);

        modal.addEventListener("click", function (event) {
            if (event.target === modal) {
                closeModal();
            }
        });

        document.addEventListener("keydown", function (event) {
            if (event.key === "Escape" && modal.classList.contains("is-open")) {
                closeModal();
            }
        });
    })();
</script>
</body>
</html>
