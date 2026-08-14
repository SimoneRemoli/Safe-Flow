<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="it.web.safeflow.model.PrivateChatThread" %>
<%
    List<PrivateChatThread> threads = (List<PrivateChatThread>) request.getAttribute("threads");
    int unreadCount = request.getAttribute("unreadDirectMessagesCount") == null
            ? 0
            : (Integer) request.getAttribute("unreadDirectMessagesCount");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Direct Messages</title>
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        body.direct-messages-page {
            margin: 0 !important;
            min-height: 100vh !important;
            color: #14241d !important;
            font-family: "Inter", "Segoe UI", Arial, sans-serif !important;
            background: #ffffff !important;
            padding: 88px 18px 34px !important;
        }

        .dm-shell {
            width: min(1160px, 100%);
            margin: 0 auto;
        }

        .dm-topbar {
            display: flex;
            align-items: flex-end;
            justify-content: space-between;
            gap: 16px;
            padding-bottom: 22px;
            border-bottom: 1px solid #d8e4de;
        }

        .dm-kicker {
            display: inline-flex;
            color: #0e7c66;
            font-size: 0.74rem;
            font-weight: 900;
            text-transform: uppercase;
        }

        h1 {
            margin: 10px 0 6px;
            color: #14241d !important;
            font-size: clamp(2rem, 3.5vw, 3rem) !important;
            line-height: 1.05 !important;
        }

        .dm-subtitle {
            margin: 0;
            color: #607267 !important;
            line-height: 1.6;
        }

        .dm-home-link {
            border-radius: 8px;
            border: 1px solid #d8e4de;
            background: #f7faf8;
            color: #405147;
            padding: 11px 14px;
            text-decoration: none;
            font-weight: 850;
        }

        .dm-layout {
            display: grid;
            grid-template-columns: 360px minmax(0, 1fr);
            gap: 22px;
            padding-top: 26px;
        }

        .dm-thread-list,
        .dm-chat-panel {
            border: 1px solid #d8e4de;
            border-radius: 12px;
            background: #ffffff;
            box-shadow: none !important;
            overflow: hidden;
        }

        .dm-thread-list {
            display: grid;
            align-content: start;
        }

        .dm-thread-list-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 10px;
            padding: 14px 16px;
            border-bottom: 1px solid #d8e4de;
            background: #fbfdfc;
            font-weight: 900;
        }

        .dm-count {
            min-width: 24px;
            height: 24px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            border-radius: 999px;
            background: #0e7c66;
            color: #ffffff;
            font-size: 0.78rem;
        }

        .dm-thread {
            display: grid;
            grid-template-columns: 42px minmax(0, 1fr) auto;
            gap: 11px;
            align-items: start;
            width: 100%;
            padding: 13px 15px;
            border: 0;
            border-bottom: 1px solid #edf2ef;
            background: #ffffff;
            color: #14241d;
            text-align: left;
            cursor: pointer;
            font: inherit;
        }

        .dm-thread:hover,
        .dm-thread.active {
            background: #f7faf8;
        }

        .dm-avatar {
            width: 42px;
            height: 42px;
            display: grid;
            place-items: center;
            border-radius: 50%;
            color: #ffffff;
            background: #0e7c66;
            font-size: 0.78rem;
            font-weight: 900;
        }

        .dm-thread-copy {
            min-width: 0;
        }

        .dm-thread-copy strong {
            display: block;
            color: #14241d;
            font-size: 0.94rem;
            line-height: 1.25;
        }

        .dm-thread-copy span {
            display: block;
            margin-top: 4px;
            overflow: hidden;
            color: #607267;
            font-size: 0.84rem;
            line-height: 1.35;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .dm-thread-meta {
            display: grid;
            gap: 7px;
            justify-items: end;
            color: #607267;
            font-size: 0.72rem;
        }

        .dm-unread {
            min-width: 22px;
            height: 22px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            border-radius: 999px;
            color: #ffffff;
            background: #2563eb;
            font-size: 0.72rem;
            font-weight: 900;
        }

        .dm-empty {
            padding: 34px 18px;
            color: #607267;
            text-align: center;
            line-height: 1.6;
        }

        .dm-chat-panel {
            min-height: 620px;
            display: grid;
            grid-template-rows: auto minmax(260px, 1fr) auto;
        }

        .dm-chat-header {
            padding: 16px 18px;
            border-bottom: 1px solid #d8e4de;
            background: #fbfdfc;
        }

        .dm-chat-header strong {
            display: block;
            color: #14241d;
            font-size: 1rem;
        }

        .dm-chat-header span {
            display: block;
            margin-top: 4px;
            color: #607267;
            font-size: 0.84rem;
        }

        .dm-messages {
            display: grid;
            align-content: start;
            gap: 10px;
            overflow-y: auto;
            padding: 18px;
            background: #fbfdfc;
        }

        .dm-message {
            width: fit-content;
            max-width: 78%;
            display: grid;
            gap: 4px;
            padding: 10px 11px;
            border-radius: 10px;
            background: #ffffff;
            border: 1px solid #d8e4de;
        }

        .dm-message.mine {
            justify-self: end;
            color: #ffffff;
            background: #1f6b4d;
            border-color: #1f6b4d;
        }

        .dm-message strong,
        .dm-message p,
        .dm-message small {
            color: inherit !important;
        }

        .dm-message p {
            margin: 0;
            line-height: 1.45;
            overflow-wrap: anywhere;
        }

        .dm-message small {
            opacity: 0.72;
        }

        .dm-form {
            display: grid;
            gap: 10px;
            padding: 14px 18px 16px;
            border-top: 1px solid #d8e4de;
            background: #ffffff;
        }

        .dm-form textarea {
            width: 100%;
            min-height: 86px;
            resize: vertical;
            border: 1px solid #d8e4de;
            border-radius: 8px;
            padding: 10px;
            color: #14241d;
            background: #ffffff;
            font: inherit;
        }

        .dm-form button {
            justify-self: end;
            border: 1px solid #1f6b4d;
            border-radius: 8px;
            background: #1f6b4d;
            color: #ffffff;
            padding: 10px 14px;
            cursor: pointer;
            font: inherit;
            font-weight: 850;
        }

        .dm-status {
            color: #607267;
            font-size: 0.9rem;
        }

        @media (max-width: 860px) {
            .dm-topbar,
            .dm-layout {
                grid-template-columns: 1fr;
            }

            .dm-topbar {
                display: grid;
                align-items: start;
            }
        }
    </style>
</head>
<body class="direct-messages-page">
<%@ include file="/header.jspf" %>
<main class="dm-shell">
    <header class="dm-topbar">
        <div>
            <span class="dm-kicker">Private workspace</span>
            <h1>Direct messages</h1>
            <p class="dm-subtitle">Private traveler conversations connected to public Safe Flow reports.</p>
        </div>
        <a class="dm-home-link" href="travelerHome">Home</a>
    </header>

    <section class="dm-layout" aria-label="Direct message inbox">
        <aside class="dm-thread-list">
            <div class="dm-thread-list-header">
                <span>Conversations</span>
                <% if (unreadCount > 0) { %>
                <span class="dm-count" data-total-unread><%= unreadCount %></span>
                <% } %>
            </div>
            <% if (threads == null || threads.isEmpty()) { %>
            <div class="dm-empty">No private conversations yet.</div>
            <% } else {
                for (PrivateChatThread thread : threads) {
                    String threadName = StringEscapeUtils.escapeHtml4(thread.getOtherTravelerDisplayName() == null ? "Traveler" : thread.getOtherTravelerDisplayName());
                    String threadInitials = StringEscapeUtils.escapeHtml4(thread.getOtherTravelerInitials() == null ? "T" : thread.getOtherTravelerInitials());
                    String threadKey = StringEscapeUtils.escapeHtml4(thread.getNotificationKey());
                    String otherCf = StringEscapeUtils.escapeHtml4(thread.getOtherTravelerCf());
                    String lastMessage = StringEscapeUtils.escapeHtml4(thread.getLastMessage() == null ? "" : thread.getLastMessage());
                    String reportText = StringEscapeUtils.escapeHtml4(thread.getReportText() == null ? "Private conversation" : thread.getReportText());
                    String city = StringEscapeUtils.escapeHtml4(thread.getCity() == null || thread.getCity().isBlank() ? "City not specified" : thread.getCity());
            %>
            <button
                    type="button"
                    class="dm-thread"
                    data-thread-button
                    data-notification-key="<%= threadKey %>"
                    data-traveler-cf="<%= otherCf %>"
                    data-traveler-name="<%= threadName %>"
                    data-report-text="<%= reportText %>">
                <span class="dm-avatar"><%= threadInitials %></span>
                <span class="dm-thread-copy">
                    <strong><%= threadName %></strong>
                    <span><%= lastMessage %></span>
                </span>
                <span class="dm-thread-meta">
                    <span><%= thread.getLastMessageAt() == null ? "" : sdf.format(thread.getLastMessageAt()) %></span>
                    <% if (thread.getUnreadCount() > 0) { %>
                    <span class="dm-unread" data-thread-unread><%= thread.getUnreadCount() %></span>
                    <% } %>
                    <span><%= city %></span>
                </span>
            </button>
            <% }
            } %>
        </aside>

        <section class="dm-chat-panel" aria-label="Selected direct message thread">
            <header class="dm-chat-header">
                <strong data-chat-title>Select a conversation</strong>
                <span data-chat-subtitle>Open a thread from the inbox to read or reply.</span>
            </header>
            <div class="dm-messages" data-chat-messages>
                <div class="dm-empty">No conversation selected.</div>
            </div>
            <form class="dm-form" data-chat-form accept-charset="UTF-8">
                <textarea name="messageText" maxlength="600" placeholder="Write a private message..." required disabled></textarea>
                <span class="dm-status" data-chat-status></span>
                <button type="submit" disabled>Send message</button>
            </form>
        </section>
    </section>
</main>
<script>
    (function () {
        const threadButtons = Array.from(document.querySelectorAll('[data-thread-button]'));
        const title = document.querySelector('[data-chat-title]');
        const subtitle = document.querySelector('[data-chat-subtitle]');
        const messagesBox = document.querySelector('[data-chat-messages]');
        const form = document.querySelector('[data-chat-form]');
        const textarea = form ? form.querySelector('textarea[name="messageText"]') : null;
        const sendButton = form ? form.querySelector('button[type="submit"]') : null;
        const status = document.querySelector('[data-chat-status]');
        const totalUnread = document.querySelector('[data-total-unread]');
        let activeNotificationKey = '';
        let activeTravelerCf = '';

        if (!messagesBox || !form || !textarea || !sendButton) {
            return;
        }

        function setStatus(message) {
            if (status) {
                status.textContent = message || '';
            }
        }

        function renderMessage(message) {
            const item = document.createElement('article');
            item.className = 'dm-message' + (message.currentUserSender ? ' mine' : '');
            const author = document.createElement('strong');
            author.textContent = message.senderDisplayName || (message.currentUserSender ? 'me' : 'Traveler');
            const text = document.createElement('p');
            text.textContent = message.text || '';
            const date = document.createElement('small');
            date.textContent = message.createdAt || '';
            item.appendChild(author);
            item.appendChild(text);
            item.appendChild(date);
            return item;
        }

        function renderMessages(messages) {
            messagesBox.innerHTML = '';
            if (!messages || !messages.length) {
                const empty = document.createElement('div');
                empty.className = 'dm-empty';
                empty.textContent = 'No private messages yet.';
                messagesBox.appendChild(empty);
                return;
            }
            messages.forEach((message) => messagesBox.appendChild(renderMessage(message)));
            messagesBox.scrollTop = messagesBox.scrollHeight;
        }

        function clearUnread(button) {
            const unread = button.querySelector('[data-thread-unread]');
            if (!unread) {
                return;
            }
            const removed = Number(unread.textContent || '0');
            unread.remove();
            if (totalUnread) {
                const next = Math.max(0, Number(totalUnread.textContent || '0') - removed);
                if (next === 0) {
                    totalUnread.remove();
                } else {
                    totalUnread.textContent = String(next);
                }
            }
        }

        async function openThread(button) {
            activeNotificationKey = button.dataset.notificationKey || '';
            activeTravelerCf = button.dataset.travelerCf || '';
            threadButtons.forEach((candidate) => candidate.classList.toggle('active', candidate === button));
            if (title) {
                title.textContent = button.dataset.travelerName || 'Traveler';
            }
            if (subtitle) {
                subtitle.textContent = button.dataset.reportText || 'Private conversation';
            }
            messagesBox.innerHTML = '<div class="dm-empty">Loading conversation...</div>';
            textarea.disabled = false;
            sendButton.disabled = false;
            setStatus('');

            try {
                const query = new URLSearchParams({
                    notificationKey: activeNotificationKey,
                    travelerCf: activeTravelerCf
                });
                const response = await fetch('privateTravelerChat?' + query.toString());
                const payload = await response.json();
                if (!response.ok) {
                    throw new Error(payload.error || 'Unable to load conversation.');
                }
                renderMessages(payload.messages || []);
                clearUnread(button);
                textarea.focus();
            } catch (error) {
                setStatus(error.message || 'Unable to load conversation.');
            }
        }

        threadButtons.forEach((button) => {
            button.addEventListener('click', () => openThread(button));
        });

        form.addEventListener('submit', async (event) => {
            event.preventDefault();
            const messageText = textarea.value.trim();
            if (!messageText) {
                setStatus('Write a message before sending.');
                return;
            }

            setStatus('');
            sendButton.disabled = true;

            try {
                const response = await fetch('privateTravelerChat', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    },
                    body: new URLSearchParams({
                        notificationKey: activeNotificationKey,
                        travelerCf: activeTravelerCf,
                        messageText: messageText
                    })
                });
                const payload = await response.json();
                if (!response.ok) {
                    throw new Error(payload.error || 'Unable to send message.');
                }
                const empty = messagesBox.querySelector('.dm-empty');
                if (empty) {
                    empty.remove();
                }
                messagesBox.appendChild(renderMessage(payload.message));
                messagesBox.scrollTop = messagesBox.scrollHeight;
                textarea.value = '';
            } catch (error) {
                setStatus(error.message || 'Unable to send message.');
            } finally {
                sendButton.disabled = false;
            }
        });

        if (threadButtons.length) {
            openThread(threadButtons[0]);
        }
    }());
</script>
</body>
</html>
