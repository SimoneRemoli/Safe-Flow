<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.web.safeflow.controller.applicativo.ViewInternalNotificationsControllerApplicativo" %>
<%@ page import="it.web.safeflow.controller.applicativo.PrivateTravelerChatControllerApplicativo" %>
<%@ page import="it.web.safeflow.exception.BrondiException" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    int internalNotificationsCount = 0;
    int directMessagesCount = 0;
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
        try {
            directMessagesCount = new PrivateTravelerChatControllerApplicativo()
                    .unreadCount(internalSession.getAttribute("codiceFiscale").toString());
        } catch (BrondiException ignored) {
            directMessagesCount = 0;
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Traveler Console</title>
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        :root {
            --traveler-bg: #ffffff;
            --traveler-soft: #f7faf8;
            --traveler-border: #d8e4de;
            --traveler-text: #14241d;
            --traveler-muted: #607267;
            --traveler-muted-strong: #405147;
            --traveler-primary: #0e7c66;
            --traveler-primary-strong: #075f4e;
            --traveler-primary-soft: #e8f7ef;
            --traveler-blue-soft: #e8efff;
        }

        * {
            box-sizing: border-box;
        }

        body.traveler-home-page {
            margin: 0 !important;
            min-height: 100vh !important;
            color: var(--traveler-text) !important;
            font-family: "Segoe UI", "Helvetica Neue", Arial, sans-serif !important;
            background: var(--traveler-bg) !important;
            padding: 28px 18px !important;
        }

        .traveler-shell {
            width: min(1120px, 100%);
            margin: 0 auto;
            padding: 8px 0 42px;
        }

        .traveler-topbar {
            display: grid;
            grid-template-columns: 1fr auto;
            gap: 24px;
            align-items: center;
            padding: 18px 0 24px;
            border-bottom: 1px solid var(--traveler-border);
        }

        .traveler-brand strong {
            display: block;
            color: var(--traveler-text) !important;
            font-size: 1.22rem;
            line-height: 1.2;
        }

        .traveler-brand span {
            display: block;
            margin-top: 4px;
            color: var(--traveler-muted) !important;
            font-size: 0.94rem;
        }

        .traveler-nav {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            justify-content: flex-end;
        }

        .traveler-nav a {
            min-height: 42px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 10px 15px !important;
            border: 1px solid var(--traveler-border) !important;
            border-radius: 999px !important;
            background: var(--traveler-soft) !important;
            color: var(--traveler-muted-strong) !important;
            font-weight: 850;
            text-decoration: none;
            transition: transform 0.18s ease, background 0.18s ease, border-color 0.18s ease;
        }

        .traveler-nav a:hover {
            transform: translateY(-1px);
            border-color: #c8e2d8 !important;
            background: #ffffff !important;
            color: var(--traveler-text) !important;
        }

        .traveler-nav .notification-link.has-alert {
            border-color: #bfe8cf !important;
            background: var(--traveler-primary-soft) !important;
            color: var(--traveler-primary-strong) !important;
        }

        .notification-badge {
            min-width: 22px;
            height: 22px;
            padding: 0 6px;
            border-radius: 999px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            background: var(--traveler-primary);
            color: #ffffff;
            font-size: 0.78rem;
            font-weight: 850;
            line-height: 1;
        }

        .traveler-nav .direct-message-link.has-alert {
            position: relative;
            overflow: visible;
            border-color: #fecdd3 !important;
            background: #fff1f2 !important;
            color: #be123c !important;
        }

        .direct-message-badge {
            position: absolute !important;
            top: -9px !important;
            right: -8px !important;
            min-width: 20px !important;
            height: 20px !important;
            margin-left: 0 !important;
            padding: 0 6px !important;
            border: 2px solid #ffffff !important;
            background: #dc2626 !important;
            color: #ffffff !important;
            font-size: 0.72rem !important;
            font-weight: 900 !important;
            box-shadow: 0 3px 10px rgba(220, 38, 38, 0.28);
            z-index: 2;
        }

        .traveler-hero {
            display: grid;
            grid-template-columns: minmax(0, 1fr) 300px;
            gap: 38px;
            align-items: start;
            padding: 34px 0 30px;
            border-bottom: 1px solid var(--traveler-border);
        }

        .traveler-kicker {
            display: inline-flex;
            margin-bottom: 12px;
            color: var(--traveler-primary) !important;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            font-size: 0.74rem;
            font-weight: 850;
        }

        .traveler-hero h1 {
            margin: 0;
            max-width: 780px;
            color: var(--traveler-text) !important;
            font-size: clamp(2.2rem, 4vw, 3.65rem) !important;
            line-height: 1.03 !important;
            letter-spacing: 0 !important;
        }

        .traveler-hero p {
            margin: 14px 0 0;
            max-width: 690px;
            color: var(--traveler-muted) !important;
            font-size: 1.02rem;
            line-height: 1.75;
        }

        .traveler-actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 26px;
        }

        .traveler-primary-action,
        .traveler-secondary-action {
            min-height: 46px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 12px 18px !important;
            border-radius: 999px !important;
            font-size: 0.98rem;
            font-weight: 850;
            text-decoration: none;
            transition: transform 0.18s ease, background 0.18s ease, border-color 0.18s ease;
        }

        .traveler-primary-action {
            border: 1px solid var(--traveler-primary) !important;
            color: #ffffff !important;
            background: linear-gradient(135deg, var(--traveler-primary), #13a085) !important;
        }

        .traveler-primary-action:hover {
            transform: translateY(-1px);
            background: linear-gradient(135deg, var(--traveler-primary-strong), var(--traveler-primary)) !important;
            color: #ffffff !important;
        }

        .traveler-secondary-action {
            border: 1px solid var(--traveler-border) !important;
            color: var(--traveler-muted-strong) !important;
            background: var(--traveler-soft) !important;
        }

        .traveler-secondary-action:hover {
            transform: translateY(-1px);
            border-color: #c8e2d8 !important;
            background: #ffffff !important;
            color: var(--traveler-text) !important;
        }

        .traveler-status {
            padding: 0 0 0 22px;
            border-left: 1px solid var(--traveler-border);
        }

        .traveler-status h2 {
            margin: 0 0 14px;
            color: var(--traveler-text) !important;
            font-size: 1rem !important;
            line-height: 1.2 !important;
            letter-spacing: 0 !important;
        }

        .status-list {
            display: grid;
            gap: 14px;
        }

        .status-item {
            display: grid;
            grid-template-columns: auto 1fr;
            gap: 11px;
            align-items: start;
        }

        .status-index {
            width: 28px;
            height: 28px;
            display: grid;
            place-items: center;
            border-radius: 8px;
            border: 1px solid #bfe8cf;
            background: var(--traveler-primary-soft);
            color: var(--traveler-primary-strong);
            font-size: 0.75rem;
            font-weight: 900;
        }

        .status-item strong {
            display: block;
            color: var(--traveler-text) !important;
            font-size: 0.92rem;
            line-height: 1.25;
        }

        .status-item span {
            display: block;
            margin-top: 3px;
            color: var(--traveler-muted) !important;
            font-size: 0.86rem;
            line-height: 1.48;
        }

        .traveler-workspace {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 22px;
            padding: 30px 0 0;
        }

        .workspace-item {
            padding-top: 18px;
            border-top: 3px solid var(--traveler-primary-soft);
        }

        .workspace-item:nth-child(2) {
            border-top-color: var(--traveler-blue-soft);
        }

        .workspace-item:nth-child(3) {
            border-top-color: #fff5df;
        }

        .workspace-item strong {
            display: block;
            color: var(--traveler-text) !important;
            font-size: 1rem;
            line-height: 1.25;
        }

        .workspace-item span {
            display: block;
            margin-top: 8px;
            color: var(--traveler-muted) !important;
            line-height: 1.62;
            font-size: 0.94rem;
        }

        @media (max-width: 900px) {
            .traveler-topbar,
            .traveler-hero,
            .traveler-workspace {
                grid-template-columns: 1fr;
            }

            .traveler-nav {
                justify-content: flex-start;
            }

            .traveler-status {
                padding: 24px 0 0;
                border-left: 0;
                border-top: 1px solid var(--traveler-border);
            }
        }

        @media (max-width: 620px) {
            body.traveler-home-page {
                padding: 16px !important;
            }

            .traveler-shell {
                padding-bottom: 28px;
            }

            .traveler-actions,
            .traveler-nav {
                flex-direction: column;
            }

            .traveler-primary-action,
            .traveler-secondary-action,
            .traveler-nav a {
                width: 100%;
            }
        }
    </style>
</head>
<body class="traveler-home-page">
<%@ include file="/header.jspf" %>
<main class="traveler-shell">
    <header class="traveler-topbar">
        <div class="traveler-brand">
            <strong>Safe Flow</strong>
            <span>Traveler safety console</span>
        </div>

        <nav class="traveler-nav" aria-label="Traveler navigation">
            <a href="areaRiservata">Reserved Area</a>
            <a href="internalNotifications" class="notification-link <%= internalNotificationsCount > 0 ? "has-alert" : "" %>">
                Notifications
                <% if (internalNotificationsCount > 0) { %>
                <span class="notification-badge"><%= internalNotificationsCount %></span>
                <% } %>
            </a>
            <a href="directMessages" class="notification-link direct-message-link <%= directMessagesCount > 0 ? "has-alert" : "" %>">
                Direct messages
                <% if (directMessagesCount > 0) { %>
                <span class="notification-badge direct-message-badge"><%= directMessagesCount %></span>
                <% } %>
            </a>
        </nav>
    </header>

    <section class="traveler-hero" aria-labelledby="travelerHomeTitle">
        <div>
            <span class="traveler-kicker">Traveler console</span>
            <h1 id="travelerHomeTitle">Report incidents and track alerts.</h1>
            <p>
                Use Safe Flow to send structured safety reports and review notifications about events on public transport.
            </p>

            <div class="traveler-actions">
                <a class="traveler-primary-action" href="travelerReport">Send Report</a>
                <a class="traveler-secondary-action" href="viewNotifications">View Public Alerts</a>
            </div>
        </div>

        <aside class="traveler-status" aria-labelledby="travelerFlowTitle">
            <h2 id="travelerFlowTitle">Traveler workflow</h2>
            <div class="status-list">
                <div class="status-item">
                    <div class="status-index">01</div>
                    <div>
                        <strong>Report</strong>
                        <span>Send city-based safety information to the review queue.</span>
                    </div>
                </div>
                <div class="status-item">
                    <div class="status-index">02</div>
                    <div>
                        <strong>Review</strong>
                        <span>Administrators validate content before public visibility.</span>
                    </div>
                </div>
                <div class="status-item">
                    <div class="status-index">03</div>
                    <div>
                        <strong>Notify</strong>
                        <span>Approved alerts help other commuters understand local risks.</span>
                    </div>
                </div>
            </div>
        </aside>
    </section>

    <section class="traveler-workspace" aria-label="Traveler workspace">
        <div class="workspace-item">
            <strong>Traveler reports</strong>
            <span>Submit safety communications about public transport incidents with city and category context.</span>
        </div>
        <div class="workspace-item">
            <strong>Reserved area</strong>
            <span>Open your personal area to review account information and private platform activity.</span>
        </div>
        <div class="workspace-item">
            <strong>Notifications</strong>
            <span>Track the outcome of your traveler reports and review updates from Safe Flow.</span>
        </div>
    </section>
</main>
</body>
</html>
