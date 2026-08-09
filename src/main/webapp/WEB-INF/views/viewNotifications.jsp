<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="it.web.routex.bean.MessageBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<MessageBean> notifiche = (List<MessageBean>) request.getAttribute("notifiche");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    boolean isTravelerView = Boolean.TRUE.equals(request.getAttribute("isTravelerView"));
    int totalNotifications = notifiche == null ? 0 : notifiche.size();
    List<String> supportedNotificationCities = List.of("Rome", "Naples", "Athens", "Budapest");
    Map<String, List<MessageBean>> notificationsByCity = new LinkedHashMap<>();
    for (String supportedCity : supportedNotificationCities) {
        notificationsByCity.put(supportedCity, new ArrayList<>());
    }
    if (notifiche != null) {
        for (MessageBean notification : notifiche) {
            String cityName = notification.getCity() == null || notification.getCity().isBlank()
                    ? "Rome"
                    : notification.getCity();
            if (notificationsByCity.containsKey(cityName)) {
                notificationsByCity.get(cityName).add(notification);
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Notifications</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.84);
            --panel-soft: rgba(255, 255, 255, 0.05);
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
            --accent: #6ff7ff;
            --accent-2: #8dd8ff;
            --success: #89ffd1;
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
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
        }

        .shell {
            width: min(1220px, calc(100% - 32px));
            margin: 24px auto;
            padding: 28px;
            border-radius: 30px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.9));
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.38);
            backdrop-filter: blur(16px);
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            gap: 16px;
            align-items: center;
            flex-wrap: wrap;
        }

        .eyebrow {
            display: inline-flex;
            padding: 8px 14px;
            border-radius: 999px;
            color: var(--accent);
            border: 1px solid rgba(111, 247, 255, 0.2);
            background: rgba(111, 247, 255, 0.08);
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }

        h1 {
            margin: 14px 0 8px;
            font-size: clamp(2.2rem, 4vw, 3.6rem);
        }

        .subtitle {
            margin: 0;
            color: var(--muted);
            line-height: 1.7;
            max-width: 760px;
        }

        .nav-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .nav-actions a,
        .save-button {
            text-decoration: none;
            border: none;
            cursor: pointer;
            color: var(--text);
            padding: 12px 18px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.04em;
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
            transition: transform 0.25s ease, border-color 0.25s ease;
        }

        .save-button {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            box-shadow: 0 18px 32px rgba(111, 247, 255, 0.2);
        }

        .nav-actions a.primary-action {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            border-color: rgba(137, 255, 209, 0.48);
            box-shadow: 0 18px 32px rgba(111, 247, 255, 0.2);
        }

        .nav-actions a:hover,
        .save-button:hover {
            transform: translateY(-2px);
            border-color: rgba(111, 247, 255, 0.4);
        }

        .table-panel {
            margin-top: 26px;
            border-radius: 26px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
            overflow: hidden;
        }

        .city-switcher {
            margin-top: 22px;
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .city-switch {
            appearance: none;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.06);
            color: var(--text);
            padding: 10px 16px;
            border-radius: 999px;
            font: inherit;
            font-weight: 700;
            cursor: pointer;
            transition: background 0.2s ease, border-color 0.2s ease, transform 0.2s ease;
        }

        .city-switch:hover {
            transform: translateY(-1px);
            border-color: rgba(111, 247, 255, 0.28);
        }

        .city-switch.active {
            background: rgba(111, 247, 255, 0.12);
            border-color: rgba(111, 247, 255, 0.44);
            color: #ffffff;
        }

        .city-group {
            margin-top: 26px;
        }

        .city-group.hidden {
            display: none;
        }

        .city-title {
            margin: 0 0 12px;
            display: inline-flex;
            align-items: center;
            gap: 10px;
            color: #ffffff;
            font-size: 1.05rem;
            font-weight: 700;
        }

        .city-title-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 34px;
            padding: 6px 10px;
            border-radius: 999px;
            color: #111111;
            background: rgba(109, 40, 217, 0.22);
            border: 1px solid rgba(167, 139, 250, 0.32);
            font-size: 0.76rem;
            letter-spacing: 0.06em;
            text-transform: uppercase;
        }

        .table-wrap {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 18px 20px;
            text-align: left;
            border-bottom: 1px solid rgba(255, 255, 255, 0.08);
        }

        th {
            color: var(--accent);
            font-size: 0.85rem;
            letter-spacing: 0.08em;
            text-transform: uppercase;
            background: rgba(111, 247, 255, 0.06);
        }

        td {
            color: var(--text);
            vertical-align: top;
        }

        tr:hover td {
            background: rgba(255, 255, 255, 0.03);
        }

        .message-cell {
            color: #e8f7ff;
            line-height: 1.65;
        }

        .message-cell.admin-message {
            font-weight: 700;
            color: #ffffff;
        }

        .message-meta {
            margin-top: 8px;
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
        }

        .message-meta.stack {
            flex-direction: column;
            align-items: flex-start;
            gap: 8px;
        }

        .message-detail {
            display: inline-flex;
            align-items: center;
            padding: 6px 10px;
            border-radius: 999px;
            color: #dbeafe;
            background: rgba(37, 99, 235, 0.16);
            border: 1px solid rgba(96, 165, 250, 0.22);
            font-size: 0.76rem;
        }

        .message-detail.station-detail {
            color: #1d4ed8;
            background: rgba(34, 197, 94, 0.16);
            border: 1px solid rgba(34, 197, 94, 0.34);
        }

        .date-cell {
            color: var(--muted);
            white-space: nowrap;
        }

        .type-cell {
            white-space: nowrap;
        }

        .report-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 7px 12px;
            border-radius: 999px;
            font-size: 0.74rem;
            font-weight: 700;
            letter-spacing: 0.06em;
            text-transform: uppercase;
        }

        .report-badge.admin {
            color: #111111;
            background: rgba(239, 68, 68, 0.22);
            border: 1px solid rgba(239, 68, 68, 0.4);
        }

        .report-badge.user {
            color: #111111;
            background: rgba(249, 115, 22, 0.22);
            border: 1px solid rgba(249, 115, 22, 0.38);
        }

        .report-badge.pickpocket {
            color: #111827;
            background: rgba(239, 68, 68, 0.22);
            border: 1px solid rgba(239, 68, 68, 0.4);
        }

        .report-badge.fight {
            color: #1d4ed8;
            background: rgba(250, 204, 21, 0.22);
            border: 1px solid rgba(250, 204, 21, 0.4);
        }

        .report-badge.crowd {
            color: #14532d;
            background: rgba(34, 197, 94, 0.2);
            border: 1px solid rgba(34, 197, 94, 0.38);
        }

        .report-badge.general {
            color: #111111;
            background: rgba(255, 255, 255, 0.96);
            border: 1px solid rgba(148, 163, 184, 0.9);
        }

        .check-cell {
            text-align: center;
        }

        input[type="checkbox"] {
            width: 20px;
            height: 20px;
            accent-color: #89ffd1;
            cursor: pointer;
        }

        .empty-state {
            padding: 46px 24px;
            text-align: center;
            color: var(--muted);
        }

        .footer-actions {
            margin-top: 20px;
            display: flex;
            justify-content: flex-end;
        }

        @media (max-width: 768px) {
            .shell {
                width: min(100% - 20px, 100%);
                margin: 10px auto;
                padding: 18px;
                border-radius: 22px;
            }

            th, td {
                padding: 14px 12px;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        body.safe-flow-notifications {
            background: #f3f6f4 !important;
            color: #14241d !important;
            font-family: "Inter", "Segoe UI", Arial, sans-serif !important;
        }

        .safe-flow-notifications .shell {
            width: min(1240px, calc(100% - 40px)) !important;
            margin: 22px auto !important;
            padding: 0 !important;
            border-radius: 16px !important;
            border: 1px solid #d8e4de !important;
            background: #ffffff !important;
            box-shadow: 0 18px 42px rgba(20, 36, 29, 0.08) !important;
            overflow: hidden !important;
        }

        .safe-flow-notifications .topbar {
            padding: 24px 28px !important;
            align-items: flex-start !important;
            border-bottom: 1px solid #d8e4de !important;
            background: linear-gradient(180deg, #ffffff, #fbfdfc) !important;
        }

        .safe-flow-notifications .eyebrow {
            padding: 6px 10px !important;
            border-radius: 6px !important;
            color: #0e6f5d !important;
            background: #e5f3ee !important;
            border: 1px solid #c8e2d8 !important;
            font-size: 0.72rem !important;
            font-weight: 800 !important;
            text-transform: uppercase !important;
        }

        .safe-flow-notifications h1 {
            margin: 12px 0 8px !important;
            font-size: clamp(1.9rem, 3vw, 2.55rem) !important;
            line-height: 1.08 !important;
            color: #14241d !important;
        }

        .safe-flow-notifications .subtitle {
            max-width: 680px !important;
            color: #5c6f65 !important;
            line-height: 1.6 !important;
        }

        .safe-flow-notifications .nav-actions {
            justify-content: flex-end !important;
        }

        .safe-flow-notifications .nav-actions a {
            min-height: 38px !important;
            padding: 9px 13px !important;
            border-radius: 8px !important;
            color: #31443a !important;
            background: #ffffff !important;
            border: 1px solid #d8e4de !important;
            font-size: 0.9rem !important;
            font-weight: 750 !important;
        }

        .safe-flow-notifications .nav-actions a.primary-action {
            color: #ffffff !important;
            background: #0e7c66 !important;
            border-color: #0e7c66 !important;
            box-shadow: 0 10px 18px rgba(14, 124, 102, 0.16) !important;
        }

        .safe-flow-notifications .alerts-summary {
            display: grid !important;
            grid-template-columns: repeat(3, minmax(0, 1fr)) !important;
            gap: 12px !important;
            padding: 18px 28px !important;
            background: #f7faf8 !important;
            border-bottom: 1px solid #d8e4de !important;
        }

        .safe-flow-notifications .summary-item {
            padding: 14px 16px !important;
            border-radius: 12px !important;
            background: #ffffff !important;
            border: 1px solid #d8e4de !important;
        }

        .safe-flow-notifications .summary-label {
            display: block !important;
            color: #607267 !important;
            font-size: 0.78rem !important;
            font-weight: 800 !important;
            text-transform: uppercase !important;
        }

        .safe-flow-notifications .summary-value {
            display: block !important;
            margin-top: 6px !important;
            color: #14241d !important;
            font-size: 1.2rem !important;
            font-weight: 850 !important;
        }

        .safe-flow-notifications .city-switcher {
            margin: 0 !important;
            padding: 18px 28px 0 !important;
            gap: 8px !important;
            background: #ffffff !important;
        }

        .safe-flow-notifications .city-switch {
            border-radius: 8px !important;
            padding: 9px 13px !important;
            background: #f7faf8 !important;
            color: #405147 !important;
            border: 1px solid #d8e4de !important;
            font-size: 0.9rem !important;
            font-weight: 800 !important;
        }

        .safe-flow-notifications .city-switch.active {
            background: #14241d !important;
            border-color: #14241d !important;
            color: #ffffff !important;
        }

        .safe-flow-notifications .city-group {
            margin: 0 !important;
            padding: 22px 28px 28px !important;
        }

        .safe-flow-notifications .city-title {
            margin: 0 0 12px !important;
            color: #14241d !important;
            font-size: 1.05rem !important;
            font-weight: 850 !important;
        }

        .safe-flow-notifications .city-title-badge {
            border-radius: 6px !important;
            color: #607267 !important;
            background: #f7faf8 !important;
            border: 1px solid #d8e4de !important;
            font-size: 0.68rem !important;
        }

        .safe-flow-notifications .table-panel {
            margin-top: 0 !important;
            border-radius: 12px !important;
            background: #ffffff !important;
            border: 1px solid #d8e4de !important;
            box-shadow: none !important;
        }

        .safe-flow-notifications table {
            table-layout: fixed !important;
            border-collapse: separate !important;
            border-spacing: 0 !important;
        }

        .safe-flow-notifications th {
            padding: 12px 14px !important;
            color: #607267 !important;
            background: #f7faf8 !important;
            border-bottom: 1px solid #d8e4de !important;
            font-size: 0.74rem !important;
            font-weight: 850 !important;
            text-transform: uppercase !important;
        }

        .safe-flow-notifications td {
            padding: 15px 14px !important;
            color: #14241d !important;
            border-bottom: 1px solid #edf2ef !important;
            background: #ffffff !important;
            vertical-align: top !important;
        }

        .safe-flow-notifications tbody tr:last-child td {
            border-bottom: none !important;
        }

        .safe-flow-notifications tr:hover td {
            background: #fbfdfc !important;
        }

        .safe-flow-notifications .message-cell {
            color: #14241d !important;
            line-height: 1.55 !important;
            font-weight: 500 !important;
        }

        .safe-flow-notifications .message-cell.admin-message {
            font-weight: 650 !important;
        }

        .safe-flow-notifications .message-meta {
            margin-top: 10px !important;
            display: flex !important;
            flex-direction: row !important;
            gap: 6px !important;
        }

        .safe-flow-notifications .report-badge,
        .safe-flow-notifications .message-detail {
            border-radius: 6px !important;
            padding: 5px 8px !important;
            font-size: 0.68rem !important;
            font-weight: 850 !important;
            text-transform: uppercase !important;
        }

        .safe-flow-notifications .report-badge.admin {
            color: #8f1f17 !important;
            background: #fff0ee !important;
            border: 1px solid #fac7c2 !important;
        }

        .safe-flow-notifications .report-badge.user {
            color: #075f4e !important;
            background: #e5f3ee !important;
            border: 1px solid #c8e2d8 !important;
        }

        .safe-flow-notifications .report-badge.pickpocket {
            color: #8f1f17 !important;
            background: #fff0ee !important;
            border: 1px solid #fac7c2 !important;
        }

        .safe-flow-notifications .report-badge.fight {
            color: #8a4b08 !important;
            background: #fff5df !important;
            border: 1px solid #f4d58a !important;
        }

        .safe-flow-notifications .report-badge.crowd {
            color: #075f4e !important;
            background: #e8f7ef !important;
            border: 1px solid #bfe8cf !important;
        }

        .safe-flow-notifications .report-badge.general,
        .safe-flow-notifications .message-detail {
            color: #405147 !important;
            background: #f4f7f5 !important;
            border: 1px solid #d8e4de !important;
        }

        .safe-flow-notifications .date-cell {
            color: #607267 !important;
            font-size: 0.92rem !important;
            white-space: nowrap !important;
        }

        .safe-flow-notifications .type-cell {
            white-space: nowrap !important;
        }

        .safe-flow-notifications .empty-state {
            padding: 34px 18px !important;
            color: #607267 !important;
            background: #ffffff !important;
            box-shadow: none !important;
            border: none !important;
        }

        @media (max-width: 760px) {
            .safe-flow-notifications .shell {
                width: min(100% - 16px, 1240px) !important;
            }

            .safe-flow-notifications .alerts-summary {
                grid-template-columns: 1fr !important;
            }

            .safe-flow-notifications .nav-actions {
                width: 100% !important;
                justify-content: flex-start !important;
            }

            .safe-flow-notifications table {
                min-width: 760px !important;
            }
        }
    </style>
</head>
<body class="safe-flow-notifications">
<%@ include file="/header.jspf" %>
<div class="shell">
    <div class="topbar">
        <div>
            <span class="eyebrow"><%= isTravelerView ? "Traveler alerts" : "Alert management" %></span>
            <h1>System alerts</h1>
            <p class="subtitle">
                Review approved Safe Flow notifications and stay informed about relevant events on public transport.
            </p>
        </div>

        <div class="nav-actions">
            <% if (isTravelerView) { %>
            <a href="travelerReport" class="primary-action">Send Report</a>
            <% } %>
            <a href="travelerHome">Home</a>
        </div>
    </div>

    <div class="alerts-summary" aria-label="Notification summary">
        <div class="summary-item">
            <span class="summary-label">Published alerts</span>
            <span class="summary-value"><%= totalNotifications %></span>
        </div>
        <div class="summary-item">
            <span class="summary-label">Cities monitored</span>
            <span class="summary-value"><%= supportedNotificationCities.size() %></span>
        </div>
        <div class="summary-item">
            <span class="summary-label">View</span>
            <span class="summary-value"><%= isTravelerView ? "Traveler" : "Admin" %></span>
        </div>
    </div>

    <div class="city-switcher" role="tablist" aria-label="Notification cities">
            <% for (String cityName : notificationsByCity.keySet()) { %>
            <button type="button" class="city-switch" data-city-switch="<%= cityName %>"><%= cityName %></button>
            <% } %>
    </div>
    <% for (Map.Entry<String, List<MessageBean>> cityEntry : notificationsByCity.entrySet()) { %>
    <section class="city-group" data-city-group="<%= cityEntry.getKey() %>">
            <h2 class="city-title">
                <span><%= cityEntry.getKey() %></span>
                <span class="city-title-badge">City</span>
            </h2>
            <div class="table-panel">
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
	                            <th style="width: 58%">Alert details</th>
	                            <th style="width: 18%">Source</th>
	                            <th style="width: 24%">Published</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% if (cityEntry.getValue().isEmpty()) { %>
                        <tr>
                            <td colspan="3" class="empty-state">No notifications available for this city.</td>
                        </tr>
                        <% } else {
                            for (MessageBean m : cityEntry.getValue()) {
                            boolean adminReport = "ADMIN".equalsIgnoreCase(m.getSenderRole());
                        %>
                        <tr>
                            <td class="message-cell <%= adminReport ? "admin-message" : "" %>">
                                <div><%= m.getMessage() %></div>
	                                <% if (Boolean.TRUE.equals(m.getPickpocketAlert()) || Boolean.TRUE.equals(m.getFightAlert()) || Boolean.TRUE.equals(m.getCrowdAlert()) || Boolean.TRUE.equals(m.getGeneralAlert()) || (m.getStationName() != null && !m.getStationName().isBlank()) || (m.getSuspectClothing() != null && !m.getSuspectClothing().isBlank())) { %>
                                <div class="message-meta stack">
                                    <% if (Boolean.TRUE.equals(m.getPickpocketAlert())) { %>
                                    <span class="report-badge pickpocket">Pickpocket alert</span>
                                    <% } %>
                                    <% if (Boolean.TRUE.equals(m.getFightAlert())) { %>
                                    <span class="report-badge fight">Fight alert</span>
                                    <% } %>
                                    <% if (Boolean.TRUE.equals(m.getCrowdAlert())) { %>
                                    <span class="report-badge crowd">Crowd alert</span>
                                    <% } %>
	                                    <% if (Boolean.TRUE.equals(m.getGeneralAlert())) { %>
	                                    <span class="report-badge general">General alert</span>
	                                    <% } %>
                                        <% if (m.getStationName() != null && !m.getStationName().isBlank()) { %>
                                        <span class="message-detail">Station: <%= m.getStationName() %></span>
                                        <% } %>
                                        <% if (m.getSuspectClothing() != null && !m.getSuspectClothing().isBlank()) { %>
                                        <span class="message-detail">Details: <%= m.getSuspectClothing() %></span>
                                        <% } %>
	                                </div>
                                <% } %>
                            </td>
                            <td class="type-cell">
                                <span class="report-badge <%= adminReport ? "admin" : "user" %>">
                                    <%= adminReport ? "Admin report" : "User report" %>
                                </span>
                            </td>
                            <td class="date-cell"><%= sdf.format(m.getDate()) %></td>
                        </tr>
                        <% }
                        } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
        <% } %>

</div>
<script>
    (function () {
        const switches = Array.from(document.querySelectorAll('[data-city-switch]'));
        const groups = Array.from(document.querySelectorAll('[data-city-group]'));

        if (!switches.length || !groups.length) {
            return;
        }

        function selectCity(city) {
            switches.forEach((button) => {
                button.classList.toggle('active', button.dataset.citySwitch === city);
            });

            groups.forEach((group) => {
                group.classList.toggle('hidden', group.dataset.cityGroup !== city);
            });
        }

        switches.forEach((button) => {
            button.addEventListener('click', () => selectCity(button.dataset.citySwitch));
        });

        selectCity(switches[0].dataset.citySwitch);
    }());
</script>
</body>
</html>
