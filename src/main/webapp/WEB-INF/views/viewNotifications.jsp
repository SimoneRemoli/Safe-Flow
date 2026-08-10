<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="it.web.safeflow.bean.MessageBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="it.web.safeflow.model.NotificationComment" %>
<%@ page import="it.web.safeflow.utility.RomeMetroLineResolver" %>
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
        .nav-actions button,
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
        .nav-actions button:hover,
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

        .safe-flow-notifications .nav-actions a,
        .safe-flow-notifications .nav-actions button {
            min-height: 38px !important;
            padding: 9px 13px !important;
            border-radius: 8px !important;
            color: #31443a !important;
            background: #ffffff !important;
            border: 1px solid #d8e4de !important;
            font-size: 0.9rem !important;
            font-weight: 750 !important;
        }

        .safe-flow-notifications .nav-actions button {
            font: inherit !important;
            cursor: pointer !important;
        }

        .safe-flow-notifications .nav-actions a.primary-action {
            color: #ffffff !important;
            background: #0e7c66 !important;
            border-color: #0e7c66 !important;
            box-shadow: 0 10px 18px rgba(14, 124, 102, 0.16) !important;
        }

        .safe-flow-notifications .nav-actions .analytics-trigger {
            color: #04111f !important;
            background: linear-gradient(90deg, #89ffd1, #6ff7ff 55%, #b6f2ff) !important;
            border-color: rgba(14, 124, 102, 0.2) !important;
            box-shadow: 0 10px 18px rgba(111, 247, 255, 0.16) !important;
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
        .safe-flow-notifications .message-detail,
        .safe-flow-notifications .view-images-button,
        .safe-flow-notifications .metro-line-badge {
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

        .safe-flow-notifications .metro-line-badge {
            display: inline-flex !important;
            align-items: center !important;
            gap: 6px !important;
            color: #14241d !important;
            background: #ffffff !important;
            border: 1px solid #d8e4de !important;
            letter-spacing: 0.04em !important;
        }

        .safe-flow-notifications .metro-line-badge img {
            display: block !important;
            width: 38px !important;
            height: 18px !important;
            object-fit: contain !important;
        }

        .safe-flow-notifications .view-images-button {
            display: inline-flex !important;
            align-items: center !important;
            text-decoration: none !important;
            color: #075f4e !important;
            background: #e5f3ee !important;
            border: 1px solid #c8e2d8 !important;
        }

        .safe-flow-notifications .date-cell {
            color: #607267 !important;
            font-size: 0.92rem !important;
            white-space: nowrap !important;
        }

        .safe-flow-notifications .type-cell {
            white-space: nowrap !important;
        }

        .safe-flow-notifications .source-cell {
            white-space: normal !important;
        }

        .safe-flow-notifications .author-link,
        .safe-flow-notifications .author-static {
            display: inline-flex !important;
            align-items: center !important;
            gap: 10px !important;
            min-width: 0 !important;
            margin-bottom: 10px !important;
            color: #14241d !important;
            text-decoration: none !important;
        }

        .safe-flow-notifications .author-link:hover .author-name {
            color: #0e7c66 !important;
            text-decoration: underline !important;
        }

        .safe-flow-notifications .author-avatar {
            width: 38px !important;
            height: 38px !important;
            flex: 0 0 38px !important;
            border-radius: 50% !important;
            overflow: hidden !important;
            display: grid !important;
            place-items: center !important;
            color: #ffffff !important;
            background: #0e7c66 !important;
            border: 2px solid #e5f3ee !important;
            font-size: 0.72rem !important;
            font-weight: 850 !important;
            line-height: 1 !important;
        }

        .safe-flow-notifications .author-avatar img {
            width: 100% !important;
            height: 100% !important;
            object-fit: cover !important;
            display: block !important;
        }

        .safe-flow-notifications .author-copy {
            min-width: 0 !important;
            display: flex !important;
            flex-direction: column !important;
            gap: 2px !important;
        }

        .safe-flow-notifications .author-name {
            max-width: 180px !important;
            overflow: hidden !important;
            text-overflow: ellipsis !important;
            white-space: nowrap !important;
            color: #14241d !important;
            font-size: 0.92rem !important;
            font-weight: 850 !important;
            line-height: 1.2 !important;
        }

        .safe-flow-notifications .author-role {
            color: #607267 !important;
            font-size: 0.74rem !important;
            font-weight: 800 !important;
            text-transform: uppercase !important;
            line-height: 1.2 !important;
        }

        .safe-flow-notifications .top-reporter-badge {
            display: inline-flex !important;
            align-items: center !important;
            width: fit-content !important;
            max-width: 180px !important;
            padding: 4px 7px !important;
            border-radius: 6px !important;
            color: #03120d !important;
            background: #1ee7a5 !important;
            border: 1px solid #0fb87f !important;
            font-size: 0.66rem !important;
            font-weight: 900 !important;
            line-height: 1.15 !important;
            text-transform: uppercase !important;
            white-space: nowrap !important;
            overflow: hidden !important;
            text-overflow: ellipsis !important;
        }

        .safe-flow-notifications .like-cell {
            white-space: nowrap !important;
            text-align: center !important;
            vertical-align: middle !important;
        }

        .safe-flow-notifications .like-button {
            display: inline-flex !important;
            align-items: center !important;
            justify-content: center !important;
            gap: 7px !important;
            min-width: 72px !important;
            min-height: 38px !important;
            padding: 8px 11px !important;
            border-radius: 999px !important;
            color: #405147 !important;
            background: #ffffff !important;
            border: 1px solid #d8e4de !important;
            cursor: pointer !important;
            font: inherit !important;
            font-weight: 850 !important;
            transition: transform 0.18s ease, border-color 0.18s ease, color 0.18s ease, background 0.18s ease !important;
        }

        .safe-flow-notifications .like-button:hover {
            transform: translateY(-1px) !important;
            border-color: #f2a6b3 !important;
            color: #be123c !important;
        }

        .safe-flow-notifications .like-button.is-liked {
            color: #be123c !important;
            background: #fff1f2 !important;
            border-color: #fecdd3 !important;
        }

        .safe-flow-notifications .like-heart {
            font-size: 1.08rem !important;
            line-height: 1 !important;
        }

        .safe-flow-notifications .admin-like-placeholder {
            color: #8a9a91 !important;
            font-size: 0.82rem !important;
            font-weight: 800 !important;
        }

        .safe-flow-notifications .row-actions {
            display: inline-flex !important;
            flex-direction: column !important;
            gap: 7px !important;
            align-items: center !important;
        }

        .safe-flow-notifications .remove-notification-button {
            min-height: 32px !important;
            padding: 7px 10px !important;
            border-radius: 999px !important;
            border: 1px solid rgba(190, 18, 60, 0.22) !important;
            color: #be123c !important;
            background: #fff1f2 !important;
            cursor: pointer !important;
            font: inherit !important;
            font-size: 0.76rem !important;
            font-weight: 900 !important;
            transition: transform 0.18s ease, background 0.18s ease, border-color 0.18s ease !important;
        }

        .safe-flow-notifications .remove-notification-button:hover {
            transform: translateY(-1px) !important;
            border-color: rgba(190, 18, 60, 0.42) !important;
            background: #ffe4e6 !important;
        }

        .safe-flow-notifications .remove-notification-button:disabled {
            cursor: wait !important;
            opacity: 0.58 !important;
            transform: none !important;
        }

        .safe-flow-notifications .comments-row td {
            padding-top: 0 !important;
            background: #ffffff !important;
        }

        .safe-flow-notifications .comments-panel {
            margin: 0 0 12px !important;
            padding: 14px !important;
            border-radius: 12px !important;
            border: 1px solid #d8e4de !important;
            background: #f7faf8 !important;
        }

        .safe-flow-notifications .comments-toggle {
            display: inline-flex !important;
            align-items: center !important;
            gap: 8px !important;
            min-height: 34px !important;
            padding: 7px 11px !important;
            border-radius: 999px !important;
            border: 1px solid #c8e2d8 !important;
            color: #075f4e !important;
            background: #ffffff !important;
            cursor: pointer !important;
            font: inherit !important;
            font-size: 0.78rem !important;
            font-weight: 900 !important;
        }

        .safe-flow-notifications .comments-count {
            display: inline-grid !important;
            place-items: center !important;
            min-width: 22px !important;
            height: 22px !important;
            padding: 0 6px !important;
            border-radius: 999px !important;
            color: #03120d !important;
            background: #89ffd1 !important;
            font-size: 0.72rem !important;
        }

        .safe-flow-notifications .comments-content {
            margin-top: 12px !important;
        }

        .safe-flow-notifications .comments-content.hidden {
            display: none !important;
        }

        .safe-flow-notifications .comments-list {
            display: grid !important;
            gap: 10px !important;
        }

        .safe-flow-notifications .comment-item {
            display: grid !important;
            grid-template-columns: 34px minmax(0, 1fr) !important;
            gap: 10px !important;
            align-items: start !important;
            padding: 10px !important;
            border-radius: 10px !important;
            background: #ffffff !important;
            border: 1px solid #e5eee9 !important;
        }

        .safe-flow-notifications .comment-item.targeted-comment {
            border-color: #0e7c66 !important;
            box-shadow: 0 0 0 3px rgba(14, 124, 102, 0.14) !important;
        }

        .safe-flow-notifications .comment-avatar {
            width: 34px !important;
            height: 34px !important;
            border-radius: 50% !important;
            overflow: hidden !important;
            display: grid !important;
            place-items: center !important;
            color: #ffffff !important;
            background: #0e7c66 !important;
            font-size: 0.68rem !important;
            font-weight: 900 !important;
            text-decoration: none !important;
        }

        .safe-flow-notifications .comment-avatar img {
            width: 100% !important;
            height: 100% !important;
            object-fit: cover !important;
            display: block !important;
        }

        .safe-flow-notifications .comment-meta {
            display: flex !important;
            align-items: baseline !important;
            gap: 8px !important;
            flex-wrap: wrap !important;
        }

        .safe-flow-notifications .comment-author {
            color: #14241d !important;
            font-size: 0.86rem !important;
            font-weight: 900 !important;
            text-decoration: none !important;
        }

        .safe-flow-notifications .comment-date {
            color: #7b8f84 !important;
            font-size: 0.74rem !important;
            font-weight: 800 !important;
        }

        .safe-flow-notifications .comment-text {
            margin: 4px 0 0 !important;
            color: #31443a !important;
            line-height: 1.45 !important;
            font-size: 0.9rem !important;
            overflow-wrap: anywhere !important;
        }

        .safe-flow-notifications .comment-reply-context {
            display: inline-flex !important;
            margin-top: 6px !important;
            padding: 4px 7px !important;
            border-radius: 6px !important;
            color: #075f4e !important;
            background: #e8f7ef !important;
            border: 1px solid #c8e2d8 !important;
            font-size: 0.72rem !important;
            font-weight: 900 !important;
        }

        .safe-flow-notifications .comment-actions {
            margin-top: 7px !important;
            display: flex !important;
            gap: 8px !important;
            align-items: center !important;
        }

        .safe-flow-notifications .comment-reply-button,
        .safe-flow-notifications .comment-reply-cancel,
        .safe-flow-notifications .comment-like-button {
            border: none !important;
            padding: 0 !important;
            color: #0e7c66 !important;
            background: transparent !important;
            cursor: pointer !important;
            font: inherit !important;
            font-size: 0.78rem !important;
            font-weight: 900 !important;
        }

        .safe-flow-notifications .comment-like-button {
            display: inline-flex !important;
            align-items: center !important;
            gap: 5px !important;
            min-width: 42px !important;
            color: #607267 !important;
            transition: color 0.18s ease, transform 0.18s ease !important;
        }

        .safe-flow-notifications .comment-like-button:hover {
            color: #be123c !important;
            transform: translateY(-1px) !important;
        }

        .safe-flow-notifications .comment-like-button.is-liked {
            color: #be123c !important;
        }

        .safe-flow-notifications .comment-like-heart {
            font-size: 0.94rem !important;
            line-height: 1 !important;
        }

        .safe-flow-notifications .comments-empty {
            color: #7b8f84 !important;
            font-size: 0.84rem !important;
            font-weight: 800 !important;
            padding: 8px 2px !important;
        }

        .safe-flow-notifications .comment-form {
            margin-top: 12px !important;
            display: grid !important;
            grid-template-columns: minmax(0, 1fr) auto !important;
            gap: 10px !important;
            align-items: end !important;
        }

        .safe-flow-notifications .comment-reply-target {
            grid-column: 1 / -1 !important;
            display: none !important;
            align-items: center !important;
            justify-content: space-between !important;
            gap: 10px !important;
            padding: 8px 10px !important;
            border-radius: 9px !important;
            color: #31443a !important;
            background: #ffffff !important;
            border: 1px solid #d8e4de !important;
            font-size: 0.82rem !important;
            font-weight: 850 !important;
        }

        .safe-flow-notifications .comment-reply-target.active {
            display: flex !important;
        }

        .safe-flow-notifications .comment-form textarea {
            width: 100% !important;
            min-height: 42px !important;
            max-height: 120px !important;
            resize: vertical !important;
            border-radius: 10px !important;
            border: 1px solid #d8e4de !important;
            background: #ffffff !important;
            color: #14241d !important;
            padding: 10px 12px !important;
            font: inherit !important;
            line-height: 1.35 !important;
        }

        .safe-flow-notifications .comment-form textarea:focus {
            outline: none !important;
            border-color: #0e7c66 !important;
            box-shadow: 0 0 0 3px rgba(14, 124, 102, 0.12) !important;
        }

        .safe-flow-notifications .comment-form > button[type="submit"] {
            min-height: 42px !important;
            padding: 9px 14px !important;
            border-radius: 9px !important;
            border: 1px solid #0e7c66 !important;
            color: #ffffff !important;
            background: #0e7c66 !important;
            cursor: pointer !important;
            font: inherit !important;
            font-weight: 900 !important;
        }

        .safe-flow-notifications .comment-form-status {
            min-height: 18px !important;
            color: #8f1f17 !important;
            font-size: 0.78rem !important;
            font-weight: 800 !important;
        }

        .safe-flow-notifications .analytics-modal {
            position: fixed !important;
            inset: 0 !important;
            z-index: 1200 !important;
            display: grid !important;
            place-items: center !important;
            padding: 22px !important;
            background: rgba(4, 17, 31, 0.58) !important;
            backdrop-filter: blur(10px) !important;
        }

        .safe-flow-notifications .analytics-modal.hidden {
            display: none !important;
        }

        .safe-flow-notifications .analytics-dialog {
            width: min(980px, 100%) !important;
            max-height: min(840px, calc(100vh - 44px)) !important;
            overflow: auto !important;
            border-radius: 18px !important;
            color: #14241d !important;
            background: #ffffff !important;
            border: 1px solid #d8e4de !important;
            box-shadow: 0 32px 90px rgba(4, 17, 31, 0.34) !important;
        }

        .safe-flow-notifications .analytics-header {
            display: flex !important;
            justify-content: space-between !important;
            gap: 16px !important;
            align-items: flex-start !important;
            padding: 22px 24px !important;
            border-bottom: 1px solid #d8e4de !important;
            background: linear-gradient(135deg, #ffffff, #f5fffb 52%, #effbff) !important;
        }

        .safe-flow-notifications .analytics-kicker {
            display: inline-flex !important;
            padding: 5px 9px !important;
            border-radius: 6px !important;
            color: #075f4e !important;
            background: #e5f3ee !important;
            border: 1px solid #c8e2d8 !important;
            font-size: 0.7rem !important;
            font-weight: 900 !important;
            text-transform: uppercase !important;
        }

        .safe-flow-notifications .analytics-header h2 {
            margin: 10px 0 6px !important;
            color: #14241d !important;
            font-size: clamp(1.45rem, 3vw, 2.1rem) !important;
            line-height: 1.08 !important;
        }

        .safe-flow-notifications .analytics-header p {
            margin: 0 !important;
            max-width: 640px !important;
            color: #607267 !important;
            line-height: 1.55 !important;
        }

        .safe-flow-notifications .analytics-close {
            width: 38px !important;
            height: 38px !important;
            border-radius: 50% !important;
            border: 1px solid #d8e4de !important;
            color: #31443a !important;
            background: #ffffff !important;
            cursor: pointer !important;
            font: inherit !important;
            font-size: 1.3rem !important;
            line-height: 1 !important;
        }

        .safe-flow-notifications .analytics-body {
            padding: 22px 24px 24px !important;
        }

        .safe-flow-notifications .analytics-controls {
            display: grid !important;
            grid-template-columns: minmax(210px, 0.8fr) minmax(0, 1.6fr) !important;
            gap: 16px !important;
            align-items: start !important;
            margin-bottom: 18px !important;
        }

        .safe-flow-notifications .analytics-control {
            padding: 14px !important;
            border-radius: 14px !important;
            background: #f7faf8 !important;
            border: 1px solid #d8e4de !important;
        }

        .safe-flow-notifications .analytics-label {
            display: block !important;
            margin-bottom: 8px !important;
            color: #405147 !important;
            font-size: 0.76rem !important;
            font-weight: 900 !important;
            text-transform: uppercase !important;
        }

        .safe-flow-notifications .analytics-select {
            width: 100% !important;
            min-height: 42px !important;
            border-radius: 10px !important;
            border: 1px solid #c8e2d8 !important;
            color: #14241d !important;
            background: #ffffff !important;
            padding: 8px 10px !important;
            font: inherit !important;
            font-weight: 800 !important;
        }

        .safe-flow-notifications .station-picker {
            display: flex !important;
            flex-wrap: wrap !important;
            gap: 8px !important;
        }

        .safe-flow-notifications .station-chip {
            display: inline-flex !important;
            align-items: center !important;
            gap: 7px !important;
            min-height: 34px !important;
            padding: 7px 10px !important;
            border-radius: 999px !important;
            color: #405147 !important;
            background: #ffffff !important;
            border: 1px solid #d8e4de !important;
            cursor: pointer !important;
            font-size: 0.82rem !important;
            font-weight: 850 !important;
        }

        .safe-flow-notifications .station-chip:has(input:checked) {
            color: #04111f !important;
            border-color: rgba(14, 124, 102, 0.32) !important;
            background: linear-gradient(90deg, #dffff4, #e8fbff) !important;
            box-shadow: 0 8px 18px rgba(14, 124, 102, 0.1) !important;
        }

        .safe-flow-notifications .station-chip input {
            width: 16px !important;
            height: 16px !important;
        }

        .safe-flow-notifications .analytics-hint {
            display: block !important;
            margin-top: 10px !important;
            min-height: 18px !important;
            color: #607267 !important;
            font-size: 0.78rem !important;
            font-weight: 800 !important;
        }

        .safe-flow-notifications .analytics-chart-card {
            position: relative !important;
            overflow: hidden !important;
            padding: 18px !important;
            border-radius: 16px !important;
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.96), rgba(247, 250, 248, 0.98)),
                radial-gradient(circle at 15% 10%, rgba(111, 247, 255, 0.28), transparent 24%) !important;
            border: 1px solid #d8e4de !important;
        }

        .safe-flow-notifications .analytics-chart-card::before {
            content: "" !important;
            position: absolute !important;
            inset: 0 !important;
            pointer-events: none !important;
            background:
                linear-gradient(rgba(14, 124, 102, 0.05) 1px, transparent 1px),
                linear-gradient(90deg, rgba(14, 124, 102, 0.05) 1px, transparent 1px) !important;
            background-size: 42px 42px !important;
            mask-image: linear-gradient(180deg, rgba(0,0,0,0.9), transparent 92%) !important;
        }

        .safe-flow-notifications .analytics-chart-top {
            position: relative !important;
            z-index: 1 !important;
            display: flex !important;
            justify-content: space-between !important;
            gap: 14px !important;
            align-items: flex-start !important;
            margin-bottom: 16px !important;
        }

        .safe-flow-notifications .analytics-total {
            display: block !important;
            color: #14241d !important;
            font-size: 1.65rem !important;
            font-weight: 950 !important;
            line-height: 1 !important;
        }

        .safe-flow-notifications .analytics-total-label,
        .safe-flow-notifications .analytics-percent {
            display: block !important;
            margin-top: 5px !important;
            color: #607267 !important;
            font-size: 0.78rem !important;
            font-weight: 850 !important;
        }

        .safe-flow-notifications .analytics-chart {
            position: relative !important;
            z-index: 1 !important;
            display: grid !important;
            grid-template-columns: 44px minmax(0, 1fr) !important;
            gap: 12px !important;
            min-height: 300px !important;
        }

        .safe-flow-notifications .chart-axis {
            display: grid !important;
            grid-template-rows: repeat(5, 1fr) !important;
            color: #7b8f84 !important;
            font-size: 0.72rem !important;
            font-weight: 850 !important;
            text-align: right !important;
        }

        .safe-flow-notifications .chart-plot {
            position: relative !important;
            display: grid !important;
            grid-template-columns: repeat(3, minmax(90px, 1fr)) !important;
            gap: 16px !important;
            align-items: end !important;
            min-height: 300px !important;
            padding: 12px 4px 0 !important;
            border-left: 1px solid #c8e2d8 !important;
            border-bottom: 1px solid #c8e2d8 !important;
        }

        .safe-flow-notifications .chart-plot::before {
            content: "" !important;
            position: absolute !important;
            inset: 0 0 0 0 !important;
            background: repeating-linear-gradient(
                to top,
                rgba(14, 124, 102, 0.08) 0,
                rgba(14, 124, 102, 0.08) 1px,
                transparent 1px,
                transparent 25%
            ) !important;
            pointer-events: none !important;
        }

        .safe-flow-notifications .chart-bar-wrap {
            position: relative !important;
            z-index: 1 !important;
            display: grid !important;
            grid-template-rows: minmax(0, 1fr) auto !important;
            align-items: end !important;
            min-width: 0 !important;
            min-height: 288px !important;
        }

        .safe-flow-notifications .chart-bar {
            position: relative !important;
            display: grid !important;
            align-items: start !important;
            justify-items: center !important;
            min-height: 8px !important;
            border-radius: 13px 13px 4px 4px !important;
            background: linear-gradient(180deg, var(--bar-a), var(--bar-b)) !important;
            box-shadow: 0 16px 30px var(--bar-shadow) !important;
            transform-origin: bottom !important;
            animation: chart-rise 0.7s cubic-bezier(.2,.8,.2,1) both !important;
        }

        .safe-flow-notifications .chart-bar::after {
            content: "" !important;
            position: absolute !important;
            inset: 0 !important;
            border-radius: inherit !important;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.42), transparent) !important;
            transform: translateX(-120%) !important;
            animation: chart-sheen 1.4s ease 0.25s both !important;
        }

        .safe-flow-notifications .chart-value {
            margin-top: 8px !important;
            padding: 5px 7px !important;
            border-radius: 999px !important;
            color: #04111f !important;
            background: rgba(255, 255, 255, 0.88) !important;
            border: 1px solid rgba(255, 255, 255, 0.7) !important;
            font-size: 0.74rem !important;
            font-weight: 950 !important;
            box-shadow: 0 8px 18px rgba(4, 17, 31, 0.12) !important;
        }

        .safe-flow-notifications .chart-label {
            margin-top: 10px !important;
            color: #31443a !important;
            font-size: 0.82rem !important;
            font-weight: 900 !important;
            text-align: center !important;
            overflow-wrap: anywhere !important;
        }

        .safe-flow-notifications .analytics-empty {
            position: relative !important;
            z-index: 1 !important;
            padding: 40px 16px !important;
            text-align: center !important;
            color: #607267 !important;
            font-weight: 850 !important;
        }

        .safe-flow-notifications .analytics-empty.hidden,
        .safe-flow-notifications .analytics-chart.hidden {
            display: none !important;
        }

        @keyframes chart-rise {
            from { transform: scaleY(0.1); opacity: 0.35; }
            to { transform: scaleY(1); opacity: 1; }
        }

        @keyframes chart-sheen {
            to { transform: translateX(120%); }
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

            .safe-flow-notifications .analytics-controls {
                grid-template-columns: 1fr !important;
            }

            .safe-flow-notifications .analytics-chart {
                grid-template-columns: 34px minmax(0, 1fr) !important;
            }

            .safe-flow-notifications .chart-plot {
                grid-template-columns: repeat(3, minmax(74px, 1fr)) !important;
                gap: 10px !important;
                overflow-x: auto !important;
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
            <button type="button" class="analytics-trigger" data-analytics-open>Station analytics</button>
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
			                            <th style="width: 50%">Alert details</th>
			                            <th style="width: 24%">Source</th>
			                            <th style="width: 16%">Published</th>
			                            <th style="width: 10%">Actions</th>
	                        </tr>
                        </thead>
                        <tbody>
                        <% if (cityEntry.getValue().isEmpty()) { %>
                        <tr>
	                            <td colspan="4" class="empty-state">No notifications available for this city.</td>
                        </tr>
	                        <% } else {
	                            for (MessageBean m : cityEntry.getValue()) {
	                            boolean adminReport = "ADMIN".equalsIgnoreCase(m.getSenderRole());
	                            boolean senderProfileAvailable = Boolean.TRUE.equals(m.getSenderProfileAvailable());
	                            boolean currentUserSender = Boolean.TRUE.equals(m.getCurrentUserSender());
	                            boolean senderHasAvatar = Boolean.TRUE.equals(m.getSenderAvatarPresent());
	                            String senderCf = m.getSenderCf();
	                            String encodedSenderCf = senderCf == null ? "" : URLEncoder.encode(senderCf, StandardCharsets.UTF_8);
	                            String authorHref = currentUserSender ? "profile" : "publicProfile?cf=" + encodedSenderCf;
	                            String authorAvatarSrc = currentUserSender ? "profileAvatar?t=" + System.currentTimeMillis() : "publicProfileAvatar?cf=" + encodedSenderCf + "&amp;t=" + System.currentTimeMillis();
		                            String authorName = StringEscapeUtils.escapeHtml4(m.getSenderDisplayName() == null ? "Safe Flow Team" : m.getSenderDisplayName());
		                            String authorInitials = StringEscapeUtils.escapeHtml4(m.getSenderInitials() == null ? "SF" : m.getSenderInitials());
		                            String authorRole = StringEscapeUtils.escapeHtml4(adminReport ? "Admin" : "Traveler");
		                            int reporterRank = m.getSenderCommunityRank() == null ? 0 : m.getSenderCommunityRank();
		                            boolean topReporter = !adminReport && reporterRank >= 1 && reporterRank <= 3;
		                            String reporterTrustLevel = StringEscapeUtils.escapeHtml4(m.getSenderTrustLevel() == null ? "Active Reporter" : m.getSenderTrustLevel());
		                            String notificationKey = StringEscapeUtils.escapeHtml4(m.getNotificationKey() == null ? "" : m.getNotificationKey());
		                            String encodedNotificationKey = m.getNotificationKey() == null ? "" : URLEncoder.encode(m.getNotificationKey(), StandardCharsets.UTF_8);
		                            int likeCount = m.getLikeCount() == null ? 0 : m.getLikeCount();
		                            boolean likedByCurrentUser = Boolean.TRUE.equals(m.getLikedByCurrentUser());
		                            int imageCount = m.getImageCount() == null ? 0 : m.getImageCount();
		                            List<RomeMetroLineResolver.MetroLine> metroLines = RomeMetroLineResolver.linesFor(m.getCity(), m.getStationName());
		                            List<NotificationComment> comments = m.getComments() == null ? List.of() : m.getComments();
		                            int commentCount = m.getCommentCount() == null ? comments.size() : m.getCommentCount();
		                            String stationName = m.getStationName() == null ? "" : m.getStationName().trim();
		                            String escapedStationName = StringEscapeUtils.escapeHtml4(stationName);
		                            String escapedCityName = StringEscapeUtils.escapeHtml4(cityEntry.getKey());
		                            String reportRowAttributes = "data-public-notification-row data-notification-key=\"" + notificationKey + "\"";
		                            if (!adminReport && !stationName.isBlank()) {
		                                reportRowAttributes += " data-report-row data-report-city=\"" + escapedCityName + "\" data-report-station=\"" + escapedStationName + "\"";
		                            }
			                        %>
	                        <tr <%= reportRowAttributes %>>
	                            <td class="message-cell <%= adminReport ? "admin-message" : "" %>">
	                                <div><%= StringEscapeUtils.escapeHtml4(m.getMessage()) %></div>
			                                <% if (Boolean.TRUE.equals(m.getPickpocketAlert()) || Boolean.TRUE.equals(m.getFightAlert()) || Boolean.TRUE.equals(m.getCrowdAlert()) || Boolean.TRUE.equals(m.getGeneralAlert()) || !metroLines.isEmpty() || (m.getStationName() != null && !m.getStationName().isBlank()) || (m.getSuspectClothing() != null && !m.getSuspectClothing().isBlank()) || imageCount > 0) { %>
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
	                                        <% for (RomeMetroLineResolver.MetroLine metroLine : metroLines) { %>
	                                        <span class="metro-line-badge <%= metroLine.cssClass() %>">
	                                            <img src="<%= metroLine.assetPath() %>" alt="<%= metroLine.label() %>">
	                                        </span>
	                                        <% } %>
	                                        <% if (m.getStationName() != null && !m.getStationName().isBlank()) { %>
	                                        <span class="message-detail">Station: <%= StringEscapeUtils.escapeHtml4(m.getStationName()) %></span>
	                                        <% } %>
		                                        <% if (m.getSuspectClothing() != null && !m.getSuspectClothing().isBlank()) { %>
		                                        <span class="message-detail">Details: <%= StringEscapeUtils.escapeHtml4(m.getSuspectClothing()) %></span>
		                                        <% } %>
		                                        <% if (!adminReport && imageCount > 0) { %>
		                                        <a class="view-images-button" href="reportImages?notificationKey=<%= encodedNotificationKey %>">View images</a>
		                                        <% } %>
			                                </div>
		                                <% } %>
	                            </td>
	                            <td class="type-cell source-cell">
	                                <% if (senderProfileAvailable || currentUserSender) { %>
	                                <a class="author-link" href="<%= authorHref %>">
	                                    <span class="author-avatar">
	                                        <% if (senderHasAvatar) { %>
	                                        <img src="<%= authorAvatarSrc %>" alt="">
	                                        <% } else { %>
	                                        <span><%= authorInitials %></span>
	                                        <% } %>
	                                    </span>
	                                    <span class="author-copy">
	                                        <strong class="author-name"><%= authorName %></strong>
	                                        <small class="author-role"><%= authorRole %></small>
	                                        <% if (topReporter) { %>
	                                        <span class="top-reporter-badge">#<%= reporterRank %> - <%= reporterTrustLevel %></span>
	                                        <% } %>
	                                    </span>
	                                </a>
	                                <% } else { %>
	                                <div class="author-static">
	                                    <span class="author-avatar"><span><%= authorInitials %></span></span>
	                                    <span class="author-copy">
	                                        <strong class="author-name"><%= authorName %></strong>
	                                        <small class="author-role"><%= authorRole %></small>
	                                        <% if (topReporter) { %>
	                                        <span class="top-reporter-badge">#<%= reporterRank %> - <%= reporterTrustLevel %></span>
	                                        <% } %>
	                                    </span>
	                                </div>
	                                <% } %>
	                                <span class="report-badge <%= adminReport ? "admin" : "user" %>">
	                                    <%= adminReport ? "Admin report" : "User report" %>
	                                </span>
	                            </td>
	                            <td class="date-cell"><%= sdf.format(m.getDate()) %></td>
	                            <td class="like-cell">
	                                <div class="row-actions">
	                                <% if (!adminReport && isTravelerView && !notificationKey.isBlank()) { %>
	                                <button
	                                        type="button"
	                                        class="like-button <%= likedByCurrentUser ? "is-liked" : "" %>"
	                                        data-like-button
	                                        data-notification-key="<%= notificationKey %>"
	                                        aria-label="<%= likedByCurrentUser ? "Unlike report" : "Like report" %>">
	                                    <span class="like-heart" aria-hidden="true">&#9829;</span>
	                                    <span data-like-count><%= likeCount %></span>
	                                </button>
	                                <% } else { %>
	                                <span class="admin-like-placeholder">-</span>
	                                <% } %>
	                                </div>
		                            </td>
		                        </tr>
		                        <% if (!adminReport && isTravelerView && !notificationKey.isBlank()) { %>
		                        <tr class="comments-row" data-public-comments-row data-notification-key="<%= notificationKey %>">
		                            <td colspan="4">
		                                <section class="comments-panel" data-comments-panel data-notification-key="<%= notificationKey %>">
		                                    <button
		                                            type="button"
		                                            class="comments-toggle"
		                                            data-comments-toggle
		                                            aria-expanded="false">
		                                        Comments
		                                        <span class="comments-count" data-comment-count><%= commentCount %></span>
		                                    </button>
		                                    <div class="comments-content hidden" data-comments-content>
		                                        <div class="comments-list" data-comments-list>
		                                            <% if (comments.isEmpty()) { %>
		                                            <div class="comments-empty" data-comments-empty>No comments yet.</div>
		                                            <% } else {
		                                                for (NotificationComment comment : comments) {
		                                                    String commentAuthorCf = comment.getAuthorCf() == null ? "" : comment.getAuthorCf();
		                                                    String encodedCommentAuthorCf = URLEncoder.encode(commentAuthorCf, StandardCharsets.UTF_8);
		                                                    String commentAuthorHref = comment.isCurrentUserAuthor() ? "profile" : "publicProfile?cf=" + encodedCommentAuthorCf;
		                                                    String commentAvatarSrc = comment.isCurrentUserAuthor() ? "profileAvatar?t=" + System.currentTimeMillis() : "publicProfileAvatar?cf=" + encodedCommentAuthorCf + "&amp;t=" + System.currentTimeMillis();
		                                                    String escapedCommentId = StringEscapeUtils.escapeHtml4(comment.getId());
		                                                    int commentLikeCount = comment.getLikeCount();
		                                                    boolean commentLikedByCurrentUser = comment.isLikedByCurrentUser();
		                                            %>
		                                            <article class="comment-item" id="comment-<%= escapedCommentId %>" data-comment-id="<%= escapedCommentId %>" data-comment-author="<%= StringEscapeUtils.escapeHtml4(comment.getAuthorDisplayName()) %>">
		                                                <a class="comment-avatar" href="<%= commentAuthorHref %>" aria-label="Open comment author profile">
		                                                    <% if (comment.isAuthorAvatarPresent()) { %>
		                                                    <img src="<%= commentAvatarSrc %>" alt="">
		                                                    <% } else { %>
		                                                    <span><%= StringEscapeUtils.escapeHtml4(comment.getAuthorInitials()) %></span>
		                                                    <% } %>
		                                                </a>
		                                                <div class="comment-body">
		                                                    <div class="comment-meta">
		                                                        <a class="comment-author" href="<%= commentAuthorHref %>"><%= StringEscapeUtils.escapeHtml4(comment.getAuthorDisplayName()) %></a>
		                                                        <span class="comment-date"><%= sdf.format(comment.getCreatedAt()) %></span>
		                                                    </div>
		                                                    <% if (comment.getReplyToDisplayName() != null && !comment.getReplyToDisplayName().isBlank()) { %>
		                                                    <span class="comment-reply-context">Replying to <%= StringEscapeUtils.escapeHtml4(comment.getReplyToDisplayName()) %></span>
		                                                    <% } %>
		                                                    <p class="comment-text"><%= StringEscapeUtils.escapeHtml4(comment.getText()) %></p>
		                                                    <div class="comment-actions">
		                                                        <button
		                                                                type="button"
		                                                                class="comment-like-button <%= commentLikedByCurrentUser ? "is-liked" : "" %>"
		                                                                data-comment-like-button
		                                                                data-notification-key="<%= notificationKey %>"
		                                                                data-comment-id="<%= escapedCommentId %>"
		                                                                aria-label="<%= commentLikedByCurrentUser ? "Unlike comment" : "Like comment" %>">
		                                                            <span class="comment-like-heart" aria-hidden="true">&#9829;</span>
		                                                            <span data-comment-like-count><%= commentLikeCount %></span>
		                                                        </button>
		                                                        <button type="button" class="comment-reply-button" data-reply-to-comment="<%= escapedCommentId %>" data-reply-to-author="<%= StringEscapeUtils.escapeHtml4(comment.getAuthorDisplayName()) %>">Reply</button>
		                                                    </div>
		                                                </div>
		                                            </article>
		                                            <% }} %>
		                                        </div>
		                                        <form class="comment-form" data-comment-form data-notification-key="<%= notificationKey %>" accept-charset="UTF-8">
		                                            <div class="comment-reply-target" data-reply-target>
		                                                <span data-reply-target-label></span>
		                                                <button type="button" class="comment-reply-cancel" data-reply-cancel>Cancel</button>
		                                            </div>
		                                            <input type="hidden" name="parentCommentId" value="">
		                                            <textarea name="commentText" maxlength="600" placeholder="Write a comment..." required></textarea>
		                                            <button type="submit">Post</button>
		                                            <span class="comment-form-status" data-comment-form-status></span>
		                                        </form>
		                                    </div>
		                                </section>
		                            </td>
		                        </tr>
		                        <% } %>
	                        <% }
	                        } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
        <% } %>

</div>
<div class="analytics-modal hidden" data-analytics-modal aria-hidden="true">
    <div class="analytics-dialog" role="dialog" aria-modal="true" aria-labelledby="stationAnalyticsTitle">
        <div class="analytics-header">
            <div>
                <span class="analytics-kicker">Station risk map</span>
                <h2 id="stationAnalyticsTitle">Station report analytics</h2>
                <p>Select a city and compare up to three stations. The chart shows how many reports each selected station has and its share of the city total.</p>
            </div>
            <button type="button" class="analytics-close" data-analytics-close aria-label="Close analytics">&times;</button>
        </div>
        <div class="analytics-body">
            <div class="analytics-controls">
                <div class="analytics-control">
                    <label class="analytics-label" for="analyticsCitySelect">City</label>
                    <select class="analytics-select" id="analyticsCitySelect" data-analytics-city></select>
                </div>
                <div class="analytics-control">
                    <span class="analytics-label">Stations</span>
                    <div class="station-picker" data-station-picker></div>
                    <span class="analytics-hint" data-analytics-hint>Choose up to 3 stations.</span>
                </div>
            </div>
            <div class="analytics-chart-card">
                <div class="analytics-chart-top">
                    <div>
                        <span class="analytics-total" data-analytics-total>0</span>
                        <span class="analytics-total-label">city station reports</span>
                    </div>
                    <span class="analytics-percent" data-analytics-percent>0% selected</span>
                </div>
                <div class="analytics-chart" data-analytics-chart>
                    <div class="chart-axis" data-chart-axis></div>
                    <div class="chart-plot" data-chart-plot></div>
                </div>
                <div class="analytics-empty hidden" data-analytics-empty>No station reports available for this city.</div>
            </div>
        </div>
    </div>
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

	    (function () {
	        const openButton = document.querySelector('[data-analytics-open]');
	        const modal = document.querySelector('[data-analytics-modal]');
	        const closeButton = document.querySelector('[data-analytics-close]');
	        const citySelect = document.querySelector('[data-analytics-city]');
	        const stationPicker = document.querySelector('[data-station-picker]');
	        const hint = document.querySelector('[data-analytics-hint]');
	        const totalEl = document.querySelector('[data-analytics-total]');
	        const percentEl = document.querySelector('[data-analytics-percent]');
	        const chart = document.querySelector('[data-analytics-chart]');
	        const axis = document.querySelector('[data-chart-axis]');
	        const plot = document.querySelector('[data-chart-plot]');
	        const emptyState = document.querySelector('[data-analytics-empty]');
	        const colors = [
	            ['#89ffd1', '#0e7c66', 'rgba(14, 124, 102, 0.22)'],
	            ['#6ff7ff', '#1d4ed8', 'rgba(29, 78, 216, 0.2)'],
	            ['#f9a8d4', '#be123c', 'rgba(190, 18, 60, 0.18)']
	        ];

	        if (!openButton || !modal || !closeButton || !citySelect || !stationPicker || !plot || !axis || !emptyState) {
	            return;
	        }

	        const cityNames = Array.from(document.querySelectorAll('[data-city-switch]'))
	            .map((button) => button.dataset.citySwitch)
	            .filter(Boolean);
	        const cityStats = new Map(cityNames.map((city) => [city, new Map()]));

	        Array.from(document.querySelectorAll('[data-report-row]')).forEach((row) => {
	            const city = row.dataset.reportCity || 'Rome';
	            const station = (row.dataset.reportStation || '').trim();
	            if (!station) {
	                return;
	            }
	            if (!cityStats.has(city)) {
	                cityStats.set(city, new Map());
	            }
	            const stations = cityStats.get(city);
	            stations.set(station, (stations.get(station) || 0) + 1);
	        });

	        let selectedStations = [];

	        function stationsForCity(city) {
	            return Array.from((cityStats.get(city) || new Map()).entries())
	                .map(([name, count]) => ({name, count}))
	                .sort((a, b) => b.count - a.count || a.name.localeCompare(b.name));
	        }

	        function cityTotal(city) {
	            return stationsForCity(city).reduce((sum, station) => sum + station.count, 0);
	        }

	        function setModalOpen(open) {
	            modal.classList.toggle('hidden', !open);
	            modal.setAttribute('aria-hidden', String(!open));
	            document.body.style.overflow = open ? 'hidden' : '';
	            if (open) {
	                renderCity();
	                citySelect.focus();
	            }
	        }

	        function renderCityOptions() {
	            citySelect.innerHTML = '';
	            cityNames.forEach((city) => {
	                const option = document.createElement('option');
	                option.value = city;
	                option.textContent = city;
	                citySelect.appendChild(option);
	            });
	        }

	        function renderCity() {
	            const stations = stationsForCity(citySelect.value);
	            selectedStations = stations.slice(0, 3).map((station) => station.name);
	            renderStationPicker();
	            renderChart();
	        }

	        function renderStationPicker() {
	            const stations = stationsForCity(citySelect.value);
	            stationPicker.innerHTML = '';

	            if (!stations.length) {
	                hint.textContent = 'No station reports available for this city.';
	                return;
	            }

	            stations.forEach((station) => {
	                const label = document.createElement('label');
	                label.className = 'station-chip';

	                const checkbox = document.createElement('input');
	                checkbox.type = 'checkbox';
	                checkbox.value = station.name;
	                checkbox.checked = selectedStations.includes(station.name);
	                checkbox.disabled = !checkbox.checked && selectedStations.length >= 3;

	                const text = document.createElement('span');
	                text.textContent = station.name + ' (' + station.count + ')';

	                checkbox.addEventListener('change', () => {
	                    if (checkbox.checked && selectedStations.length >= 3) {
	                        checkbox.checked = false;
	                        hint.textContent = 'You can compare up to 3 stations.';
	                        return;
	                    }

	                    selectedStations = checkbox.checked
	                            ? selectedStations.concat(station.name)
	                            : selectedStations.filter((name) => name !== station.name);
	                    renderStationPicker();
	                    renderChart();
	                });

	                label.appendChild(checkbox);
	                label.appendChild(text);
	                stationPicker.appendChild(label);
	            });

	            hint.textContent = selectedStations.length
	                    ? selectedStations.length + '/3 stations selected.'
	                    : 'Choose at least one station.';
	        }

	        function renderAxis(maxValue) {
	            axis.innerHTML = '';
	            for (let step = 4; step >= 0; step -= 1) {
	                const label = document.createElement('span');
	                label.textContent = String(Math.ceil((maxValue * step) / 4));
	                axis.appendChild(label);
	            }
	        }

	        function renderChart() {
	            const city = citySelect.value;
	            const allStations = stationsForCity(city);
	            const total = cityTotal(city);
	            const selected = selectedStations
	                .map((name) => allStations.find((station) => station.name === name))
	                .filter(Boolean);
	            const selectedCount = selected.reduce((sum, station) => sum + station.count, 0);
	            const maxValue = Math.max(1, ...selected.map((station) => station.count));

	            if (totalEl) {
	                totalEl.textContent = String(total);
	            }
	            if (percentEl) {
	                const selectedShare = total ? Math.round((selectedCount / total) * 100) : 0;
	                percentEl.textContent = selectedShare + '% covered by selected stations';
	            }

	            const empty = !allStations.length || !selected.length;
	            chart.classList.toggle('hidden', empty);
	            emptyState.classList.toggle('hidden', !empty);
	            if (empty) {
	                emptyState.textContent = allStations.length
	                        ? 'Select at least one station to render the chart.'
	                        : 'No station reports available for this city.';
	                plot.innerHTML = '';
	                axis.innerHTML = '';
	                return;
	            }

	            renderAxis(maxValue);
	            plot.innerHTML = '';
	            plot.style.gridTemplateColumns = 'repeat(' + Math.max(1, selected.length) + ', minmax(90px, 1fr))';

	            selected.forEach((station, index) => {
	                const palette = colors[index % colors.length];
	                const percent = total ? Math.round((station.count / total) * 100) : 0;
	                const height = Math.max(8, Math.round((station.count / maxValue) * 100));
	                const wrap = document.createElement('div');
	                wrap.className = 'chart-bar-wrap';

	                const bar = document.createElement('div');
	                bar.className = 'chart-bar';
	                bar.style.height = height + '%';
	                bar.style.setProperty('--bar-a', palette[0]);
	                bar.style.setProperty('--bar-b', palette[1]);
	                bar.style.setProperty('--bar-shadow', palette[2]);
	                bar.style.animationDelay = (index * 90) + 'ms';

	                const value = document.createElement('span');
	                value.className = 'chart-value';
	                value.textContent = station.count + ' reports · ' + percent + '%';
	                bar.appendChild(value);

	                const label = document.createElement('span');
	                label.className = 'chart-label';
	                label.textContent = station.name;

	                wrap.appendChild(bar);
	                wrap.appendChild(label);
	                plot.appendChild(wrap);
	            });
	        }

	        renderCityOptions();
	        if (cityNames.length) {
	            citySelect.value = cityNames[0];
	        }

	        openButton.addEventListener('click', () => setModalOpen(true));
	        closeButton.addEventListener('click', () => setModalOpen(false));
	        modal.addEventListener('click', (event) => {
	            if (event.target === modal) {
	                setModalOpen(false);
	            }
	        });
	        citySelect.addEventListener('change', renderCity);
	        document.addEventListener('keydown', (event) => {
	            if (event.key === 'Escape' && !modal.classList.contains('hidden')) {
	                setModalOpen(false);
	            }
	        });
	    }());

	    (function () {
	        const likeButtons = Array.from(document.querySelectorAll('[data-like-button]'));
	        if (!likeButtons.length) {
	            return;
	        }

	        likeButtons.forEach((button) => {
	            button.addEventListener('click', async () => {
	                if (button.disabled) {
	                    return;
	                }

	                const counter = button.querySelector('[data-like-count]');
	                const previousCount = counter ? counter.textContent : '0';
	                const wasLiked = button.classList.contains('is-liked');

	                button.disabled = true;
	                button.classList.toggle('is-liked', !wasLiked);
	                if (counter) {
	                    counter.textContent = String(Math.max(0, Number(previousCount) + (wasLiked ? -1 : 1)));
	                }

	                try {
	                    const response = await fetch('toggleNotificationLike', {
	                        method: 'POST',
	                        headers: {
	                            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
	                        },
	                        body: new URLSearchParams({
	                            notificationKey: button.dataset.notificationKey
	                        })
	                    });

	                    if (!response.ok) {
	                        throw new Error('Like update failed');
	                    }

	                    const payload = await response.json();
	                    button.classList.toggle('is-liked', Boolean(payload.liked));
	                    button.setAttribute('aria-label', payload.liked ? 'Unlike report' : 'Like report');
	                    if (counter) {
	                        counter.textContent = String(payload.likeCount);
	                    }
	                } catch (error) {
	                    button.classList.toggle('is-liked', wasLiked);
	                    if (counter) {
	                        counter.textContent = previousCount;
	                    }
	                } finally {
	                    button.disabled = false;
	                }
	            });
	        });
		    }());

		    (function () {
		        const removeButtons = Array.from(document.querySelectorAll('[data-remove-public-notification]'));
		        if (!removeButtons.length) {
		            return;
		        }

		        removeButtons.forEach((button) => {
		            button.addEventListener('click', async () => {
		                if (button.disabled) {
		                    return;
		                }

		                const notificationKey = button.dataset.notificationKey;
		                const row = button.closest('[data-public-notification-row]');
		                const commentsRow = Array.from(document.querySelectorAll('[data-public-comments-row]'))
		                    .find((candidate) => candidate.dataset.notificationKey === notificationKey);
		                button.disabled = true;

		                try {
		                    const response = await fetch('removeNotification', {
		                        method: 'POST',
		                        headers: {
		                            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
		                        },
		                        body: new URLSearchParams({
		                            notificationKey: notificationKey
		                        })
		                    });

		                    const payload = await response.json();
		                    if (!response.ok || !payload.removed) {
		                        throw new Error(payload.error || 'Unable to remove notification.');
		                    }

		                    if (commentsRow) {
		                        commentsRow.remove();
		                    }
		                    if (row) {
		                        row.remove();
		                    }

		                    document.querySelectorAll('[data-city-group]').forEach((cityGroup) => {
		                        if (!cityGroup.querySelector('[data-public-notification-row]')) {
		                            const tbody = cityGroup.querySelector('tbody');
		                            if (tbody) {
		                                tbody.innerHTML = '<tr><td colspan="4" class="empty-state">No notifications available for this city.</td></tr>';
		                            }
		                        }
		                    });
		                } catch (error) {
		                    button.disabled = false;
		                }
		            });
		        });
		    }());

		    (function () {
		        const panels = Array.from(document.querySelectorAll('[data-comments-panel]'));
		        if (!panels.length) {
		            return;
		        }

		        function createComment(payload) {
		            const item = document.createElement('article');
		            item.className = 'comment-item';
		            item.dataset.commentId = payload.id || '';
		            item.dataset.commentAuthor = payload.authorDisplayName || 'Unknown traveler';
		            if (payload.id) {
		                item.id = 'comment-' + payload.id;
		            }

		            const avatar = document.createElement('a');
		            avatar.className = 'comment-avatar';
		            avatar.href = payload.authorProfileUrl || '#';
		            avatar.setAttribute('aria-label', 'Open comment author profile');

		            if (payload.authorAvatarPresent) {
		                const image = document.createElement('img');
		                image.src = payload.authorAvatarUrl;
		                image.alt = '';
		                avatar.appendChild(image);
		            } else {
		                const initials = document.createElement('span');
		                initials.textContent = payload.authorInitials || 'U';
		                avatar.appendChild(initials);
		            }

		            const body = document.createElement('div');
		            body.className = 'comment-body';
		            const meta = document.createElement('div');
		            meta.className = 'comment-meta';

		            const author = document.createElement('a');
		            author.className = 'comment-author';
		            author.href = payload.authorProfileUrl || '#';
		            author.textContent = payload.authorDisplayName || 'Unknown traveler';

		            const date = document.createElement('span');
		            date.className = 'comment-date';
		            date.textContent = payload.createdAt || '';

		            const text = document.createElement('p');
		            text.className = 'comment-text';
		            text.textContent = payload.text || '';

		            meta.appendChild(author);
		            meta.appendChild(date);
		            body.appendChild(meta);
		            if (payload.replyToDisplayName) {
		                const replyContext = document.createElement('span');
		                replyContext.className = 'comment-reply-context';
		                replyContext.textContent = 'Replying to ' + payload.replyToDisplayName;
		                body.appendChild(replyContext);
		            }
		            body.appendChild(text);

		            const actions = document.createElement('div');
		            actions.className = 'comment-actions';
		            const likeButton = document.createElement('button');
		            likeButton.type = 'button';
		            likeButton.className = 'comment-like-button' + (payload.likedByCurrentUser ? ' is-liked' : '');
		            likeButton.dataset.commentLikeButton = '';
		            likeButton.dataset.notificationKey = payload.notificationKey || '';
		            likeButton.dataset.commentId = payload.id || '';
		            likeButton.setAttribute('aria-label', payload.likedByCurrentUser ? 'Unlike comment' : 'Like comment');

		            const heart = document.createElement('span');
		            heart.className = 'comment-like-heart';
		            heart.setAttribute('aria-hidden', 'true');
		            heart.textContent = '\u2665';
		            const likeCounter = document.createElement('span');
		            likeCounter.dataset.commentLikeCount = '';
		            likeCounter.textContent = String(payload.likeCount || 0);
		            likeButton.appendChild(heart);
		            likeButton.appendChild(likeCounter);

		            const replyButton = document.createElement('button');
		            replyButton.type = 'button';
		            replyButton.className = 'comment-reply-button';
		            replyButton.dataset.replyToComment = payload.id || '';
		            replyButton.dataset.replyToAuthor = payload.authorDisplayName || 'Unknown traveler';
		            replyButton.textContent = 'Reply';
		            actions.appendChild(likeButton);
		            actions.appendChild(replyButton);
		            body.appendChild(actions);

		            item.appendChild(avatar);
		            item.appendChild(body);
		            return item;
		        }

		        panels.forEach((panel) => {
		            const toggle = panel.querySelector('[data-comments-toggle]');
		            const content = panel.querySelector('[data-comments-content]');
		            const form = panel.querySelector('[data-comment-form]');
		            const count = panel.querySelector('[data-comment-count]');
		            const list = panel.querySelector('[data-comments-list]');
		            const status = panel.querySelector('[data-comment-form-status]');
		            const parentInput = form ? form.querySelector('input[name="parentCommentId"]') : null;
		            const replyTarget = form ? form.querySelector('[data-reply-target]') : null;
		            const replyTargetLabel = form ? form.querySelector('[data-reply-target-label]') : null;
		            const replyCancel = form ? form.querySelector('[data-reply-cancel]') : null;

		            if (toggle && content) {
		                toggle.addEventListener('click', () => {
		                    const hidden = content.classList.toggle('hidden');
		                    toggle.setAttribute('aria-expanded', String(!hidden));
		                });
		            }

		            if (!form || !list) {
		                return;
		            }

		            function clearReplyTarget() {
		                if (parentInput) {
		                    parentInput.value = '';
		                }
		                if (replyTarget) {
		                    replyTarget.classList.remove('active');
		                }
		                if (replyTargetLabel) {
		                    replyTargetLabel.textContent = '';
		                }
		            }

		            function setReplyTarget(commentId, authorName) {
		                if (!commentId) {
		                    return;
		                }
		                if (parentInput) {
		                    parentInput.value = commentId;
		                }
		                if (replyTargetLabel) {
		                    replyTargetLabel.textContent = 'Replying to ' + (authorName || 'this traveler');
		                }
		                if (replyTarget) {
		                    replyTarget.classList.add('active');
		                }
		                const textarea = form.querySelector('textarea[name="commentText"]');
		                if (textarea) {
		                    textarea.focus();
		                }
		            }

		            async function toggleCommentLike(button) {
		                if (button.disabled) {
		                    return;
		                }

		                const counter = button.querySelector('[data-comment-like-count]');
		                const previousCount = counter ? counter.textContent : '0';
		                const wasLiked = button.classList.contains('is-liked');

		                button.disabled = true;
		                button.classList.toggle('is-liked', !wasLiked);
		                button.setAttribute('aria-label', wasLiked ? 'Like comment' : 'Unlike comment');
		                if (counter) {
		                    counter.textContent = String(Math.max(0, Number(previousCount) + (wasLiked ? -1 : 1)));
		                }

		                try {
		                    const response = await fetch('toggleCommentLike', {
		                        method: 'POST',
		                        headers: {
		                            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
		                        },
		                        body: new URLSearchParams({
		                            notificationKey: button.dataset.notificationKey,
		                            commentId: button.dataset.commentId
		                        })
		                    });

		                    const payload = await response.json();
		                    if (!response.ok) {
		                        throw new Error(payload.error || 'Unable to update comment like.');
		                    }

		                    button.classList.toggle('is-liked', Boolean(payload.liked));
		                    button.setAttribute('aria-label', payload.liked ? 'Unlike comment' : 'Like comment');
		                    if (counter) {
		                        counter.textContent = String(payload.likeCount);
		                    }
		                } catch (error) {
		                    button.classList.toggle('is-liked', wasLiked);
		                    button.setAttribute('aria-label', wasLiked ? 'Unlike comment' : 'Like comment');
		                    if (counter) {
		                        counter.textContent = previousCount;
		                    }
		                } finally {
		                    button.disabled = false;
		                }
		            }

		            list.addEventListener('click', (event) => {
		                const likeButton = event.target.closest('[data-comment-like-button]');
		                if (likeButton) {
		                    toggleCommentLike(likeButton);
		                    return;
		                }

		                const replyButton = event.target.closest('[data-reply-to-comment]');
		                if (!replyButton) {
		                    return;
		                }
		                setReplyTarget(replyButton.dataset.replyToComment, replyButton.dataset.replyToAuthor);
		            });

		            if (replyCancel) {
		                replyCancel.addEventListener('click', clearReplyTarget);
		            }

		            form.addEventListener('submit', async (event) => {
		                event.preventDefault();
		                const textarea = form.querySelector('textarea[name="commentText"]');
		                const submitButton = form.querySelector('button[type="submit"]');
		                const commentText = textarea ? textarea.value.trim() : '';

		                if (!commentText) {
		                    if (status) {
		                        status.textContent = 'Write a comment before posting.';
		                    }
		                    return;
		                }

		                if (status) {
		                    status.textContent = '';
		                }
		                if (submitButton) {
		                    submitButton.disabled = true;
		                }

		                try {
		                    const response = await fetch('addNotificationComment', {
		                        method: 'POST',
		                        headers: {
		                            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
		                        },
		                        body: new URLSearchParams({
		                            notificationKey: form.dataset.notificationKey,
		                            commentText: commentText,
		                            parentCommentId: parentInput ? parentInput.value : ''
		                        })
		                    });

		                    const payload = await response.json();
		                    if (!response.ok) {
		                        throw new Error(payload.error || 'Unable to add comment.');
		                    }

		                    const emptyState = list.querySelector('[data-comments-empty]');
		                    if (emptyState) {
		                        emptyState.remove();
		                    }
		                    list.appendChild(createComment(payload));
		                    if (count) {
		                        count.textContent = String(Number(count.textContent || '0') + 1);
		                    }
		                    textarea.value = '';
		                    clearReplyTarget();
		                } catch (error) {
		                    if (status) {
		                        status.textContent = error.message || 'Unable to add comment.';
		                    }
		                } finally {
		                    if (submitButton) {
		                        submitButton.disabled = false;
		                    }
		                }
		            });
		        });

		        const params = new URLSearchParams(window.location.search);
		        const targetNotificationKey = params.get('notificationKey');
		        const targetCommentId = params.get('commentId');
		        if (!targetNotificationKey) {
		            return;
		        }

		        const targetPanel = panels.find((panel) => panel.dataset.notificationKey === targetNotificationKey);
		        if (!targetPanel) {
		            return;
		        }

		        const cityGroup = targetPanel.closest('[data-city-group]');
		        if (cityGroup) {
		            const city = cityGroup.dataset.cityGroup;
		            Array.from(document.querySelectorAll('[data-city-switch]')).forEach((button) => {
		                if (button.dataset.citySwitch === city) {
		                    button.click();
		                }
		            });
		        }

		        const targetToggle = targetPanel.querySelector('[data-comments-toggle]');
		        const targetContent = targetPanel.querySelector('[data-comments-content]');
		        if (targetContent) {
		            targetContent.classList.remove('hidden');
		        }
		        if (targetToggle) {
		            targetToggle.setAttribute('aria-expanded', 'true');
		        }

		        let scrollTarget = targetPanel;
		        if (targetCommentId) {
		            const targetComment = Array.from(targetPanel.querySelectorAll('[data-comment-id]'))
		                .find((comment) => comment.dataset.commentId === targetCommentId);
		            if (targetComment) {
		                targetComment.classList.add('targeted-comment');
		                scrollTarget = targetComment;
		            }
		        }

		        window.setTimeout(() => {
		            scrollTarget.scrollIntoView({behavior: 'smooth', block: 'center'});
		        }, 120);
		    }());
		</script>
</body>
</html>
