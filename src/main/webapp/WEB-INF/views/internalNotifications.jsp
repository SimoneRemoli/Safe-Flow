<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.web.safeflow.bean.MessageBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%
    List<MessageBean> notifiche = (List<MessageBean>) request.getAttribute("notifiche");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Internal Notifications</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
            --accent: #6ff7ff;
        }
        * { box-sizing: border-box; }
        body.internal-notifications-page {
            margin: 0 !important;
            min-height: 100vh !important;
            color: #14241d !important;
            font-family: "Segoe UI", "Helvetica Neue", Arial, sans-serif !important;
            background: #ffffff !important;
            padding: 28px 18px !important;
        }
        .internal-shell {
            width: min(1080px, calc(100% - 32px));
            margin: 0 auto;
            padding: 8px 0 34px;
            border-radius: 0;
            border: 0;
            background: #ffffff;
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
            color: #0e7c66;
            border: 1px solid #c8e2d8;
            background: #e8f7ef;
            text-transform: uppercase;
            letter-spacing: 0.18em;
            font-size: 11px;
        }
        h1 {
            margin: 14px 0 8px;
            color: #14241d !important;
            font-size: clamp(2rem, 3.5vw, 3rem);
        }
        .subtitle {
            margin: 0;
            color: #607267 !important;
            line-height: 1.7;
            max-width: 720px;
        }
        .nav-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        .nav-actions a {
            text-decoration: none;
            color: #405147 !important;
            padding: 12px 18px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.04em;
            background: #f7faf8 !important;
            border: 1px solid #d8e4de !important;
        }
        .list {
            margin-top: 26px;
            display: grid;
            gap: 14px;
        }
        .item {
            display: grid;
            grid-template-columns: minmax(0, 1fr) auto;
            gap: 14px;
            align-items: start;
            padding: 18px 20px;
            border-radius: 12px;
            background: rgba(246, 250, 253, 0.96);
            border: 1px solid rgba(210, 222, 232, 0.7);
            transition: transform 0.18s ease, border-color 0.18s ease, box-shadow 0.18s ease;
        }
        .notification-content {
            display: block;
            min-width: 0;
            text-decoration: none;
        }
        a.notification-content.clickable {
            cursor: pointer;
        }
        .item.linkable {
            cursor: pointer;
        }
        .item.linkable:hover,
        .item:has(.notification-content.clickable:hover) {
            transform: translateY(-1px);
            border-color: rgba(14, 124, 102, 0.35);
            box-shadow: none;
        }
        .remove-notification-button {
            min-height: 34px;
            padding: 8px 11px;
            border-radius: 999px;
            border: 1px solid rgba(190, 18, 60, 0.22);
            color: #be123c;
            background: #fff1f2;
            cursor: pointer;
            font: inherit;
            font-size: 0.78rem;
            font-weight: 800;
            transition: transform 0.18s ease, border-color 0.18s ease, background 0.18s ease;
        }
        .remove-notification-button:hover {
            transform: translateY(-1px);
            border-color: rgba(190, 18, 60, 0.42);
            background: #ffe4e6;
        }
        .remove-notification-button:disabled {
            cursor: wait;
            opacity: 0.58;
            transform: none;
        }
        .meta {
            color: #586673;
            font-size: 0.92rem;
            margin-bottom: 8px;
        }
        .message {
            color: #2f3943;
            line-height: 1.7;
            font-weight: 600;
        }

        .item.read .message {
            color: #3b4650;
            font-weight: 400;
        }
        .empty-state {
            margin-top: 26px;
            padding: 40px 24px;
            text-align: center;
            color: #607267 !important;
            border-radius: 0;
            background: #ffffff;
            border: 1px dashed #d8e4de;
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body class="internal-notifications-page">
<%@ include file="/header.jspf" %>
<main class="internal-shell">
    <div class="topbar">
        <div>
            <span class="eyebrow">Private updates</span>
            <h1>Internal notifications</h1>
            <p class="subtitle">This area contains private Safe Flow updates related to your own reports and account activity. These notifications do not appear in the public system alert feed.</p>
        </div>
        <div class="nav-actions">
            <a href="travelerHome">Home</a>
        </div>
    </div>

    <% if (notifiche == null || notifiche.isEmpty()) { %>
    <div class="empty-state">No internal notifications available.</div>
    <% } else { %>
    <div class="list">
        <% for (MessageBean m : notifiche) {
            String actionUrl = m.getActionUrl();
            boolean clickable = actionUrl != null && !actionUrl.isBlank();
            String escapedActionUrl = clickable ? StringEscapeUtils.escapeHtml4(actionUrl) : "";
            String itemClass = "item " + (Boolean.TRUE.equals(m.getLetto()) ? "read" : "unread") + (clickable ? " linkable" : "");
            String notificationKey = m.getNotificationKey() == null ? "" : m.getNotificationKey();
        %>
        <div class="<%= itemClass %>" data-notification-card <% if (clickable) { %>data-action-url="<%= escapedActionUrl %>" tabindex="0" role="link"<% } %>>
        <% if (clickable) { %>
            <a class="notification-content clickable" href="<%= escapedActionUrl %>">
        <% } else { %>
            <div class="notification-content">
        <% } %>
            <div class="meta">
                <%= StringEscapeUtils.escapeHtml4(m.getSenderDisplayName() == null ? "Safe Flow" : m.getSenderDisplayName()) %>
                ·
                <%= sdf.format(m.getDate()) %>
            </div>
            <div class="message"><%= StringEscapeUtils.escapeHtml4(m.getMessage()) %></div>
        <% if (clickable) { %>
            </a>
        <% } else { %>
            </div>
        <% } %>
            <button
                    type="button"
                    class="remove-notification-button"
                    data-remove-notification
                    data-notification-key="<%= StringEscapeUtils.escapeHtml4(notificationKey) %>">
                Remove
            </button>
        </div>
        <% } %>
    </div>
    <% } %>
</main>
<script>
    (function () {
        const cards = Array.from(document.querySelectorAll('[data-notification-card][data-action-url]'));

        cards.forEach((card) => {
            function openCard() {
                const target = card.dataset.actionUrl;
                if (target) {
                    window.location.href = target;
                }
            }

            card.addEventListener('click', (event) => {
                if (event.target.closest('a, button')) {
                    return;
                }
                openCard();
            });

            card.addEventListener('keydown', (event) => {
                if (event.key !== 'Enter' && event.key !== ' ') {
                    return;
                }
                event.preventDefault();
                openCard();
            });
        });
    }());

    (function () {
        const buttons = Array.from(document.querySelectorAll('[data-remove-notification]'));
        if (!buttons.length) {
            return;
        }

        buttons.forEach((button) => {
            button.addEventListener('click', async () => {
                if (button.disabled) {
                    return;
                }

                const card = button.closest('[data-notification-card]');
                button.disabled = true;

                try {
                    const response = await fetch('removeInternalNotification', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                        },
                        body: new URLSearchParams({
                            notificationKey: button.dataset.notificationKey
                        })
                    });

                    const payload = await response.json();
                    if (!response.ok || !payload.removed) {
                        throw new Error(payload.error || 'Unable to remove notification.');
                    }

                    if (card) {
                        card.remove();
                    }

                    if (!document.querySelector('[data-notification-card]')) {
                        const empty = document.createElement('div');
                        empty.className = 'empty-state';
                        empty.textContent = 'No internal notifications available.';
                        document.querySelector('.list')?.replaceWith(empty);
                    }
                } catch (error) {
                    button.disabled = false;
                }
            });
        });
    }());
</script>
</body>
</html>
