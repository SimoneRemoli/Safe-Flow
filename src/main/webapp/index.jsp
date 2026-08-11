<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Safe Flow - Public Transport Safety CRM</title>
    <style>
        :root {
            --bg: #ffffff;
            --surface: #ffffff;
            --surface-soft: #f7faf8;
            --border: #d8e4de;
            --text: #14241d;
            --muted: #607267;
            --accent: #0e7c66;
            --accent-dark: #075f4e;
            --danger: #b42318;
            --warning: #8a4b08;
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
            margin: 0 auto;
            min-height: 100vh;
            background: var(--surface);
            padding: 28px;
        }

        .hero {
            min-height: calc(100vh - 112px);
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 34px 0 26px;
        }

        .hero-logo {
            width: min(680px, 100%);
            height: auto;
            display: block;
            margin: 0 auto 24px;
        }

        h1 {
            margin: 0 0 14px;
            font-size: clamp(2rem, 4vw, 3.25rem);
            line-height: 1.06;
            letter-spacing: 0;
        }

        .hero p {
            margin: 0;
            max-width: 620px;
            color: var(--muted);
            font-size: 1.05rem;
            line-height: 1.7;
        }

        .alert-badges {
            display: flex;
            justify-content: center;
            gap: 8px;
            flex-wrap: wrap;
            margin-top: 22px;
        }

        .alert-badge {
            display: inline-flex;
            align-items: center;
            min-height: 30px;
            padding: 6px 10px;
            border-radius: 6px;
            font-size: 0.72rem;
            font-weight: 850;
            text-transform: uppercase;
        }

        .alert-badge.pickpocket {
            color: #8f1f17;
            background: #fff0ee;
            border: 1px solid #fac7c2;
        }

        .alert-badge.fight {
            color: var(--warning);
            background: #fff5df;
            border: 1px solid #f4d58a;
        }

        .alert-badge.crowd {
            color: #075f4e;
            background: #e8f7ef;
            border: 1px solid #bfe8cf;
        }

        .alert-badge.general {
            color: #405147;
            background: #f4f7f5;
            border: 1px solid #d8e4de;
        }

        .actions {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 12px;
            width: min(680px, 100%);
            margin-top: 30px;
        }

        .actions form {
            margin: 0;
        }

        .actions button,
        .actions a {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-height: 48px;
            border-radius: 8px;
            padding: 13px 16px;
            font-size: 0.96rem;
            font-weight: 850;
            text-decoration: none;
            cursor: pointer;
            transition: transform 0.18s ease, border-color 0.18s ease, background 0.18s ease;
        }

        .home-actions .primary-action {
            border: 1px solid var(--border) !important;
            background: var(--surface-soft) !important;
            color: var(--text) !important;
            box-shadow: none !important;
        }

        .home-actions .primary-action:hover {
            background: var(--surface-soft) !important;
            border-color: #c8e2d8 !important;
            color: var(--text) !important;
            box-shadow: none !important;
        }

        .actions .secondary-action,
        .actions .info-button {
            border: 1px solid var(--border);
            background: var(--surface-soft);
            color: var(--text);
        }

        .actions .info-button {
            color: var(--muted);
        }

        .actions button:hover,
        .actions a:hover {
            transform: translateY(-1px);
        }

        .actions .info-button:hover {
            border-color: #c8e2d8;
            color: var(--text);
            background: #ffffff;
        }

        .travel-note {
            margin: 0;
            color: var(--muted);
            font-size: 0.93rem;
            line-height: 1.6;
            width: 100%;
        }

        .travel-note strong {
            color: var(--text);
        }

        .travel-note-section {
            margin-top: 0;
            padding-top: 24px;
            border-top: 1px solid rgba(216, 228, 222, 0.8);
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

        @media (max-width: 720px) {
            .page {
                width: min(100% - 16px, 1120px);
                min-height: 100vh;
                padding: 18px;
            }

            .hero {
                min-height: auto;
                padding-top: 28px;
            }

            .actions {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<main class="page">
    <section class="hero">
        <img class="hero-logo" src="images/safe-flow-home-logo.svg" alt="Safe Flow">
        <h1>Report public transport safety events clearly.</h1>
        <p>
            A focused CRM for traveler reports, admin moderation, and city-based safety alerts.
        </p>
        <div class="alert-badges" aria-label="Alert categories">
            <span class="alert-badge pickpocket">Pickpocket alert</span>
            <span class="alert-badge fight">Fight alert</span>
            <span class="alert-badge crowd">Crowd alert</span>
            <span class="alert-badge general">General alert</span>
        </div>

        <div class="actions home-actions">
            <a class="primary-action" href="login.jsp">Open Reserved Area</a>
            <a class="secondary-action" href="registerTraveler">Create Traveler Account</a>
            <button type="button" class="info-button" data-open-about>What is Safe Flow?</button>
        </div>
    </section>

    <section class="travel-note-section" aria-label="travel advice">
        <p class="travel-note">
            For any information, write to <strong>simoneremoli00@gmail.com</strong>. Every improvement proposal is welcome.
        </p>
    </section>

</main>

<div class="about-modal" id="aboutModal" aria-hidden="true">
    <div class="about-modal__panel" role="dialog" aria-modal="true" aria-labelledby="aboutModalTitle">
        <div class="about-modal__topbar">
            <div>
                <span class="about-modal__eyebrow">About Safe Flow</span>
                <h2 class="about-modal__title" id="aboutModalTitle">Safe Flow, explained clearly.</h2>
            </div>
            <button type="button" class="about-modal__close" data-close-about aria-label="Close dialog">&times;</button>
        </div>

        <div class="about-modal__content">
            <p>
                Safe Flow is a safety-focused CRM for public transport. Travelers can submit reports about
                pickpockets and criminal events, while administrators can review communications and manage
                platform notifications.
            </p>
            <p>
                The idea behind Safe Flow was conceived by Simone Remoli, a computer engineering student at the
                University of Rome Tor Vergata.
            </p>
            <p>
                As someone who has relied on public transportation for most of his daily commuting and has rarely
                used a car, he began to reflect on a simple yet meaningful question:
                "What would the world look like if every commuter had access to an easy-to-use application
                designed to report risky situations on metropolitan travel?"
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
