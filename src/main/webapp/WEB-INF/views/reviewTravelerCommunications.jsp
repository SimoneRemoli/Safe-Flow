<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.web.routex.bean.MessageBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<MessageBean> pendingMessages = (List<MessageBean>) request.getAttribute("pendingMessages");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String error = (String) request.getAttribute("errore");
    String successMessage = null;
    if (session != null) {
        successMessage = (String) session.getAttribute("alertMessage");
        if (successMessage != null) {
            session.removeAttribute("alertMessage");
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Review Traveler Reports</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --panel: rgba(7, 20, 36, 0.84);
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
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
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
        }
        .shell {
            width: min(1220px, calc(100% - 32px));
            margin: 24px auto;
            padding: 28px;
            border-radius: 30px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.9));
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
        h1 { margin: 14px 0 8px; font-size: clamp(2.2rem, 4vw, 3.6rem); }
        .subtitle, .flash { color: var(--muted); line-height: 1.7; }
        .nav-actions { margin-top: 18px; display: flex; gap: 10px; flex-wrap: wrap; }
        .nav-actions a, .save-button, .reject-button {
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
        }
        .save-button { color: #04111f; background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff); }
        .reject-button { background: rgba(255, 255, 255, 0.08); }
        .table-panel { margin-top: 26px; border-radius: 26px; background: rgba(255, 255, 255, 0.04); border: 1px solid rgba(255, 255, 255, 0.08); overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 18px 20px; text-align: left; border-bottom: 1px solid rgba(255, 255, 255, 0.08); }
        th { color: var(--accent); font-size: 0.85rem; letter-spacing: 0.08em; text-transform: uppercase; }
        .empty-state { padding: 46px 24px; text-align: center; color: var(--muted); }
        .footer-actions { margin-top: 20px; display: flex; justify-content: flex-end; gap: 10px; flex-wrap: wrap; }
        .flash { margin-top: 18px; padding: 14px 16px; border-radius: 18px; border: 1px solid rgba(255, 255, 255, 0.12); }
        .reason-field {
            width: 100%;
            min-height: 84px;
            border-radius: 16px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.04);
            color: var(--text);
            padding: 12px 14px;
            font: inherit;
            resize: vertical;
        }
        .alert-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 7px 12px;
            border-radius: 999px;
            font-size: 0.74rem;
            font-weight: 700;
            letter-spacing: 0.05em;
            text-transform: uppercase;
        }
        .alert-badge-stack {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            gap: 8px;
        }
        .alert-badge.pickpocket {
            color: #fff1f2;
            background: rgba(239, 68, 68, 0.2);
            border: 1px solid rgba(239, 68, 68, 0.36);
        }
        .alert-badge.standard {
            color: #eff6ff;
            background: rgba(59, 130, 246, 0.18);
            border: 1px solid rgba(59, 130, 246, 0.32);
        }
        .alert-badge.general {
            color: #111111;
            background: rgba(255, 255, 255, 0.96);
            border: 1px solid rgba(148, 163, 184, 0.9);
        }
        .alert-badge.fight {
            color: #1d4ed8;
            background: rgba(250, 204, 21, 0.22);
            border: 1px solid rgba(250, 204, 21, 0.4);
        }
        .alert-badge.crowd {
            color: #14532d;
            background: rgba(34, 197, 94, 0.2);
            border: 1px solid rgba(34, 197, 94, 0.38);
        }
        .detail-text {
            color: var(--muted);
            line-height: 1.55;
        }
        .modal-backdrop {
            position: fixed;
            inset: 0;
            background: rgba(4, 12, 23, 0.58);
            display: none;
            align-items: center;
            justify-content: center;
            padding: 20px;
            z-index: 50;
        }
        .modal-backdrop.open {
            display: flex;
        }
        .modal-panel {
            width: min(460px, 100%);
            padding: 24px;
            border-radius: 24px;
            border: 1px solid rgba(111, 247, 255, 0.18);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.94), rgba(4, 12, 23, 0.98));
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.38);
        }
        .modal-panel h3 {
            margin: 0 0 10px;
            font-size: 1.35rem;
            color: #ecf7ff;
        }
        .modal-panel p {
            margin: 0;
            color: var(--muted);
            line-height: 1.7;
        }
        .modal-actions {
            margin-top: 18px;
            display: flex;
            justify-content: flex-end;
        }
        .modal-actions button {
            border: none;
            cursor: pointer;
            padding: 12px 18px;
            border-radius: 999px;
            font-weight: 700;
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<div class="shell">
    <span class="eyebrow">Traveler moderation</span>
    <h1>Review traveler reports</h1>
    <p class="subtitle">Approve pending traveler messages before they become visible in the public report feed.</p>

    <div class="nav-actions">
        <a href="adminHub">Home</a>
        <a href="logout">Logout</a>
    </div>

    <% if (successMessage != null) { %><div class="flash"><%= successMessage %></div><% } %>
    <% if (error != null) { %><div class="flash"><%= error %></div><% } %>

    <form action="reviewTravelerCommunications" method="post">
        <div class="table-panel">
            <table>
                <thead>
                <tr>
                    <th style="width: 20%">Message</th>
                    <th style="width: 11%">City</th>
                    <th style="width: 12%">Alert type</th>
                    <th style="width: 14%">Station</th>
                    <th style="width: 14%">Appearance</th>
                    <th style="width: 11%">Sender</th>
                    <th style="width: 8%">Status</th>
                    <th style="width: 10%">Date</th>
                    <th style="width: 14%">Reject reason</th>
                    <th style="width: 6%; text-align: center;">Select</th>
                </tr>
                </thead>
                <tbody>
                <% if (pendingMessages == null || pendingMessages.isEmpty()) { %>
                <tr><td colspan="10" class="empty-state">No pending traveler reports.</td></tr>
                <% } else { for (MessageBean m : pendingMessages) { %>
                <tr>
                    <td><%= m.getMessage() %></td>
                    <td><%= m.getCity() != null && !m.getCity().isBlank() ? m.getCity() : "Unknown city" %></td>
                    <td>
                        <div class="alert-badge-stack">
                            <% if (Boolean.TRUE.equals(m.getPickpocketAlert())) { %>
                            <span class="alert-badge pickpocket">Pickpocket alert</span>
                            <% } %>
                            <% if (Boolean.TRUE.equals(m.getFightAlert())) { %>
                            <span class="alert-badge fight">Fight alert</span>
                            <% } %>
                            <% if (Boolean.TRUE.equals(m.getCrowdAlert())) { %>
                            <span class="alert-badge crowd">Crowd alert</span>
                            <% } %>
                            <% if (Boolean.TRUE.equals(m.getGeneralAlert())) { %>
                            <span class="alert-badge general">General alert</span>
                            <% } %>
                            <% if (!Boolean.TRUE.equals(m.getPickpocketAlert()) && !Boolean.TRUE.equals(m.getFightAlert()) && !Boolean.TRUE.equals(m.getCrowdAlert()) && !Boolean.TRUE.equals(m.getGeneralAlert())) { %>
                            <span class="alert-badge standard">Standard report</span>
                            <% } %>
                        </div>
                    </td>
                    <td class="detail-text"><%= m.getStationName() != null && !m.getStationName().isBlank() ? m.getStationName() : "Not specified" %></td>
                    <td class="detail-text"><%= m.getSuspectClothing() != null && !m.getSuspectClothing().isBlank() ? m.getSuspectClothing() : "Not specified" %></td>
                    <td><%= m.getSenderCf() != null ? m.getSenderCf() : "Unknown traveler" %></td>
                    <td><%= m.getStatus() != null ? m.getStatus() : "PENDING" %></td>
                    <td><%= sdf.format(m.getDate()) %></td>
                    <td>
                        <textarea
                                class="reason-field"
                                name="rejectReason_<%= m.getDate().getTime() %>"
                                placeholder="Required only if you reject this report."></textarea>
                    </td>
                    <td style="text-align:center;">
                        <input type="checkbox" name="selectedMessages" value="<%= m.getDate().getTime() + "|" + (m.getSenderCf() != null ? m.getSenderCf() : "") + "|" + m.getMessage() %>">
                    </td>
                </tr>
                <% }} %>
                </tbody>
            </table>
        </div>

        <div class="footer-actions">
            <button type="submit" class="reject-button" name="reviewAction" value="reject">Reject selected</button>
            <button type="submit" class="save-button" name="reviewAction" value="approve">Approve selected</button>
        </div>
    </form>
</div>
<div class="modal-backdrop" id="validationModal" aria-hidden="true">
    <div class="modal-panel" role="dialog" aria-modal="true" aria-labelledby="validationModalTitle">
        <h3 id="validationModalTitle">Review action required</h3>
        <p id="validationModalMessage">Select at least one traveler report before continuing.</p>
        <div class="modal-actions">
            <button type="button" id="validationModalClose">Close</button>
        </div>
    </div>
</div>
<script>
    (function () {
        const form = document.querySelector('form[action="reviewTravelerCommunications"]');
        const modal = document.getElementById('validationModal');
        const modalMessage = document.getElementById('validationModalMessage');
        const closeButton = document.getElementById('validationModalClose');

        function openModal(message) {
            modalMessage.textContent = message;
            modal.classList.add('open');
            modal.setAttribute('aria-hidden', 'false');
        }

        function closeModal() {
            modal.classList.remove('open');
            modal.setAttribute('aria-hidden', 'true');
        }

        function selectedEntries() {
            return Array.from(form.querySelectorAll('input[name="selectedMessages"]:checked'));
        }

        form.addEventListener('submit', function (event) {
            const action = event.submitter ? event.submitter.value : '';
            const selected = selectedEntries();

            if (selected.length === 0) {
                event.preventDefault();
                openModal('Select at least one traveler report before continuing.');
                return;
            }

            if (action === 'reject') {
                for (const checkbox of selected) {
                    const [timestamp] = checkbox.value.split('|', 1);
                    const reasonField = form.querySelector('[name="rejectReason_' + timestamp + '"]');
                    if (!reasonField || !reasonField.value.trim()) {
                        event.preventDefault();
                        openModal('Provide a rejection reason for every selected traveler report.');
                        if (reasonField) {
                            reasonField.focus();
                        }
                        return;
                    }
                }
            }
        });

        closeButton.addEventListener('click', closeModal);
        modal.addEventListener('click', function (event) {
            if (event.target === modal) {
                closeModal();
            }
        });
        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape' && modal.classList.contains('open')) {
                closeModal();
            }
        });
    })();
</script>
</body>
</html>
