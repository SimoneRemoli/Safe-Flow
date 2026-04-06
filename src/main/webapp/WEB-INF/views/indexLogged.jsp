<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.web.routex.controller.applicativo.ViewInternalNotificationsControllerApplicativo" %>
<%@ page import="it.web.routex.exception.BrondiException" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    int internalNotificationsCount = 0;
    HttpSession internalSession = request.getSession(false);
    if (internalSession != null && internalSession.getAttribute("codiceFiscale") != null) {
        try {
            ViewInternalNotificationsControllerApplicativo internalNotificationsService =
                    new ViewInternalNotificationsControllerApplicativo();
            internalNotificationsCount = internalNotificationsService
                    .unreadCount(internalSession.getAttribute("codiceFiscale").toString());
        } catch (BrondiException ignored) {
            internalNotificationsCount = 0;
        }
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>RouteX - Metro Finder</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.8);
            --line: rgba(111, 247, 255, 0.22);
            --text: #ecf7ff;
            --muted: #8ba7c0;
            --accent: #6ff7ff;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 15% 22%, rgba(111, 247, 255, 0.16), transparent 24%),
                radial-gradient(circle at 85% 18%, rgba(83, 169, 255, 0.18), transparent 22%),
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
                radial-gradient(circle, rgba(255, 255, 255, 0.72) 1px, transparent 1px),
                radial-gradient(circle, rgba(111, 247, 255, 0.45) 1px, transparent 1px);
            background-size: 150px 150px, 230px 230px;
            background-position: 0 0, 60px 90px;
            opacity: 0.18;
            pointer-events: none;
            animation: drift 18s linear infinite;
        }

        .shell {
            width: min(1280px, calc(100% - 32px));
            margin: 24px auto;
            min-height: calc(100vh - 48px);
            padding: 28px;
            border-radius: 32px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.9));
            box-shadow: 0 32px 84px rgba(0, 0, 0, 0.4);
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
            top: -160px;
            border-radius: 50%;
            background: radial-gradient(circle, rgba(111, 247, 255, 0.14), transparent 66%);
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

        .brand img {
            width: 72px;
        }

        .brand strong {
            display: block;
            font-size: 1.35rem;
        }

        .brand span {
            color: var(--muted);
            font-size: 0.94rem;
        }

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
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.06);
            transition: transform 0.25s ease, border-color 0.25s ease;
        }

        .nav-actions a:hover {
            transform: translateY(-2px);
            border-color: rgba(111, 247, 255, 0.4);
        }

        .notification-link {
            position: relative;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .notification-link.has-alert {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            border-color: transparent;
            box-shadow: 0 16px 28px rgba(111, 247, 255, 0.22);
        }

        .notification-badge {
            min-width: 22px;
            height: 22px;
            padding: 0 6px;
            border-radius: 999px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            background: rgba(4, 17, 31, 0.92);
            color: #f8fcff;
            font-size: 0.78rem;
            font-weight: 700;
            line-height: 1;
        }

        .hero {
            margin-top: 28px;
            display: block;
        }

        .hero-copy {
            padding: 20px 8px 20px 0;
            display: flex;
            flex-direction: column;
            justify-content: center;
            position: relative;
            z-index: 1;
        }

        .eyebrow {
            display: inline-flex;
            width: fit-content;
            padding: 8px 14px;
            border-radius: 999px;
            color: var(--accent);
            border: 1px solid rgba(111, 247, 255, 0.2);
            background: rgba(111, 247, 255, 0.08);
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }

        .hero-copy h1 {
            margin: 18px 0 16px;
            font-size: clamp(3rem, 5vw, 5.5rem);
            line-height: 0.92;
        }

        .hero-copy p {
            margin: 0;
            color: var(--muted);
            font-size: 1.05rem;
            line-height: 1.8;
            max-width: 700px;
        }

        .cta-row {
            display: flex;
            gap: 14px;
            margin-top: 26px;
            flex-wrap: wrap;
        }

        .cta-row button,
        .cta-row a {
            border: none;
            text-decoration: none;
            cursor: pointer;
            padding: 16px 24px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.06em;
            transition: transform 0.25s ease, box-shadow 0.25s ease;
        }

        .primary-cta {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            box-shadow: 0 20px 36px rgba(111, 247, 255, 0.24);
        }

        .secondary-cta {
            color: var(--text);
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
        }

        .cta-row button:hover,
        .cta-row a:hover {
            transform: translateY(-3px);
        }

        .info-grid {
            margin-top: 28px;
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 16px;
        }

        .info-card {
            padding: 18px;
            border-radius: 24px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .info-card strong {
            display: block;
            font-size: 1.4rem;
            margin-bottom: 6px;
        }

        .info-card span {
            color: var(--muted);
            line-height: 1.6;
        }

        @keyframes drift {
            0% { transform: translateY(0); }
            50% { transform: translateY(-14px); }
            100% { transform: translateY(0); }
        }

        @media (max-width: 1024px) {
            .info-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 720px) {
            .shell {
                width: min(100% - 16px, 1280px);
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
<%@ include file="/header.jspf" %>
<div class="shell">
    <div class="topbar">
        <div class="brand">
            <img src="images/logo-no-background.png" alt="RouteX">
            <div>
                <strong>RouteX</strong>
                <span>Metro intelligence for urban exploration</span>
            </div>
        </div>

        <div class="nav-actions">
            <a href="travelerHome">Home</a>
            <a href="areaRiservata">Reserved Area</a>
            <a href="internalNotifications" class="notification-link <%= internalNotificationsCount > 0 ? "has-alert" : "" %>">
                Notifications
                <% if (internalNotificationsCount > 0) { %>
                <span class="notification-badge"><%= internalNotificationsCount %></span>
                <% } %>
            </a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <section class="hero">
        <div class="hero-copy">
            <div class="eyebrow">Connected User Console</div>
            <h1>Your urban network<br>is ready to move.</h1>
            <p>
                You now have access to your features on the platform. Choose what you want to do.
            </p>

            <div class="cta-row">
                <form action="PathControllerGrafico" method="get" style="margin:0;">
                    <button class="primary-cta" type="submit">Start Exploring</button>
                </form>
                <form action="buyTicket" method="get" style="margin:0;">
                    <button class="secondary-cta" type="submit">Buy Ticket</button>
                </form>
                <a class="secondary-cta" href="travelerReport">Send Report</a>
            </div>

            <div class="info-grid">
                <div class="info-card">
                    <strong>Path Search</strong>
                    <span>Quick access to city selection and route building.</span>
                </div>
                <div class="info-card">
                    <strong>Reserved Area</strong>
                    <span>Open your personal area to review saved routes and purchased tickets.</span>
                </div>
                <div class="info-card">
                    <strong>Ticket Flow</strong>
                    <span>Move directly to ticket services and related traveler actions.</span>
                </div>
                <div class="info-card">
                    <strong>Notifications</strong>
                    <span>Open your private notification center and track the outcome of your traveler reports.</span>
                </div>
            </div>
        </div>
    </section>
</div>
</body>
</html>
