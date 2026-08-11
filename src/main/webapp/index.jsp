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
            --accent-soft: #e8f7ef;
            --blue: #2563eb;
            --blue-soft: #e8efff;
            --danger: #b42318;
            --warning: #8a4b08;
        }

        * {
            box-sizing: border-box;
        }

        body.home-page {
            margin: 0 !important;
            min-height: 100vh !important;
            background: #ffffff !important;
            color: var(--text) !important;
            font-family: "Segoe UI", "Helvetica Neue", Arial, sans-serif !important;
            overflow-x: hidden;
        }

        body.home-page .page {
            width: min(1160px, calc(100% - 32px)) !important;
            margin: 0 auto !important;
            min-height: 100vh !important;
            background: #ffffff !important;
            border: 0 !important;
            border-radius: 0 !important;
            box-shadow: none !important;
            padding: 34px 0 30px !important;
        }

        .hero {
            min-height: min(760px, calc(100vh - 92px));
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 36px 0 30px;
            position: relative;
            isolation: isolate;
        }

        .hero::before {
            content: "";
            position: absolute;
            top: 6%;
            left: 50%;
            width: min(760px, 82vw);
            height: min(760px, 82vw);
            transform: translateX(-50%);
            border: 1px solid rgba(14, 124, 102, 0.08);
            border-radius: 999px;
            opacity: 0.7;
            z-index: -1;
        }

        .hero-logo {
            width: min(640px, 100%);
            height: auto;
            display: block;
            margin: 0 auto 22px;
            animation: logoReveal 0.7s ease-out both;
        }

        .hero h1 {
            margin: 0 0 14px;
            max-width: 760px;
            font-size: clamp(2.05rem, 4vw, 3.35rem) !important;
            line-height: 1.04 !important;
            letter-spacing: 0 !important;
            animation: contentRise 0.65s ease-out 0.08s both;
        }

        .hero p {
            margin: 0;
            max-width: 650px;
            color: var(--muted) !important;
            font-size: 1.06rem;
            line-height: 1.72;
            animation: contentRise 0.65s ease-out 0.16s both;
        }

        .alert-badges {
            display: flex;
            justify-content: center;
            gap: 8px;
            flex-wrap: wrap;
            margin-top: 22px;
            animation: contentRise 0.65s ease-out 0.24s both;
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

        body.home-page .actions {
            display: grid !important;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 12px !important;
            width: min(680px, 100%) !important;
            margin-top: 28px !important;
            animation: contentRise 0.65s ease-out 0.32s both;
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

        .transit-signal {
            width: min(700px, 100%);
            margin-top: 30px;
            animation: contentRise 0.65s ease-out 0.4s both;
        }

        .transit-signal__track {
            position: relative;
            height: 2px;
            background: linear-gradient(90deg, transparent, rgba(14, 124, 102, 0.26), rgba(37, 99, 235, 0.24), transparent);
            overflow: hidden;
        }

        .transit-signal__track::before {
            content: "";
            position: absolute;
            top: 50%;
            left: -48px;
            width: 48px;
            height: 2px;
            transform: translateY(-50%);
            background: linear-gradient(90deg, transparent, var(--accent), var(--blue));
            animation: signalSweep 3.8s ease-in-out infinite;
        }

        .transit-signal__nodes {
            display: flex;
            justify-content: space-between;
            margin-top: -6px;
        }

        .transit-signal__nodes span {
            width: 10px;
            height: 10px;
            border-radius: 999px;
            background: #ffffff;
            border: 2px solid rgba(14, 124, 102, 0.36);
        }

        .crm-overview {
            display: grid;
            grid-template-columns: 1.15fr 0.85fr;
            gap: 34px;
            align-items: start;
            padding: 22px 0 34px;
            border-top: 1px solid rgba(216, 228, 222, 0.7);
            border-bottom: 1px solid rgba(216, 228, 222, 0.7);
        }

        .section-kicker {
            margin: 0 0 10px;
            color: var(--accent) !important;
            font-size: 0.76rem;
            font-weight: 850;
            letter-spacing: 0.08em;
            text-transform: uppercase;
        }

        .crm-overview h2 {
            margin: 0;
            max-width: 640px;
            color: var(--text) !important;
            font-size: clamp(1.55rem, 2.3vw, 2.05rem) !important;
            line-height: 1.14 !important;
            letter-spacing: 0 !important;
        }

        .crm-overview__copy {
            margin: 14px 0 0;
            max-width: 650px;
            color: var(--muted) !important;
            line-height: 1.72;
            font-size: 1rem;
        }

        .workflow-list {
            display: grid;
            gap: 12px;
        }

        .workflow-item {
            display: grid;
            grid-template-columns: auto 1fr;
            gap: 13px;
            align-items: start;
            padding: 13px 0;
            border-bottom: 1px solid rgba(216, 228, 222, 0.72);
        }

        .workflow-item:last-child {
            border-bottom: 0;
        }

        .workflow-step {
            width: 32px;
            height: 32px;
            display: grid;
            place-items: center;
            border-radius: 9px;
            color: var(--accent-dark);
            background: var(--accent-soft);
            border: 1px solid #bfe8cf;
            font-size: 0.82rem;
            font-weight: 900;
        }

        .workflow-item strong {
            display: block;
            color: var(--text) !important;
            font-size: 0.98rem;
            line-height: 1.28;
        }

        .workflow-item span {
            display: block;
            margin-top: 4px;
            color: var(--muted) !important;
            font-size: 0.92rem;
            line-height: 1.55;
        }

        .travel-note {
            margin: 0;
            color: var(--muted) !important;
            font-size: 0.93rem;
            line-height: 1.6;
            width: 100%;
        }

        .travel-note strong {
            color: var(--text) !important;
        }

        .travel-note-section {
            margin-top: 0;
            padding-top: 24px;
            border-top: 0;
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
            color: var(--muted) !important;
            line-height: 1.8;
            font-size: 0.98rem;
        }

        @keyframes logoReveal {
            from {
                opacity: 0;
                transform: translateY(10px) scale(0.985);
            }
            to {
                opacity: 1;
                transform: translateY(0) scale(1);
            }
        }

        @keyframes contentRise {
            from {
                opacity: 0;
                transform: translateY(12px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes signalSweep {
            0% {
                left: -48px;
                opacity: 0;
            }
            18% {
                opacity: 1;
            }
            82% {
                opacity: 1;
            }
            100% {
                left: 100%;
                opacity: 0;
            }
        }

        @media (prefers-reduced-motion: reduce) {
            .hero-logo,
            .hero h1,
            .hero p,
            .alert-badges,
            .actions,
            .transit-signal,
            .transit-signal__track::before {
                animation: none;
            }
        }

        @media (max-width: 720px) {
            body.home-page .page {
                width: min(100% - 16px, 1120px);
                min-height: 100vh;
                padding: 18px 0 24px !important;
            }

            .hero {
                min-height: auto;
                padding: 28px 0 32px;
            }

            .hero::before {
                width: 92vw;
                height: 92vw;
                top: 34px;
            }

            body.home-page .actions {
                grid-template-columns: 1fr;
            }

            .crm-overview {
                grid-template-columns: 1fr;
                gap: 20px;
                padding: 24px 0 28px;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body class="home-page">
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

        <div class="transit-signal" aria-hidden="true">
            <div class="transit-signal__track"></div>
            <div class="transit-signal__nodes">
                <span></span>
                <span></span>
                <span></span>
                <span></span>
            </div>
        </div>
    </section>

    <section class="crm-overview" aria-label="Safe Flow operational model">
        <div>
            <p class="section-kicker">Operational CRM</p>
            <h2>Structured reporting, fast review, and clear public transport alerts.</h2>
            <p class="crm-overview__copy">
                Safe Flow keeps traveler reports organized from intake to moderation, helping administrators
                evaluate events and publish city-focused notifications with a calm, reliable workflow.
            </p>
        </div>

        <div class="workflow-list">
            <div class="workflow-item">
                <div class="workflow-step">01</div>
                <div>
                    <strong>Traveler intake</strong>
                    <span>Reports arrive with category, city context, and safety details.</span>
                </div>
            </div>
            <div class="workflow-item">
                <div class="workflow-step">02</div>
                <div>
                    <strong>Administrative review</strong>
                    <span>Moderators evaluate communications before they become visible alerts.</span>
                </div>
            </div>
            <div class="workflow-item">
                <div class="workflow-step">03</div>
                <div>
                    <strong>Public notification</strong>
                    <span>Approved information reaches commuters through a focused alert feed.</span>
                </div>
            </div>
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
