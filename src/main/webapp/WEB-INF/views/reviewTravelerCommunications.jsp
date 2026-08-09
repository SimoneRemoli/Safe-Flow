<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.web.routex.bean.MessageBean" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%
    List<MessageBean> pendingMessages = (List<MessageBean>) request.getAttribute("pendingMessages");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    int pendingCount = pendingMessages == null ? 0 : pendingMessages.size();
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
    <title>Safe Flow - Review Traveler Reports</title>
    <style>
        :root {
            --bg-1: #06110f;
            --bg-2: #0c1f1a;
            --panel: rgba(255, 255, 255, 0.08);
            --line: rgba(139, 231, 196, 0.22);
            --text: #f4fffa;
            --muted: #a5bbb1;
            --accent: #1ee7a5;
            --accent-soft: rgba(30, 231, 165, 0.12);
            --surface: rgba(6, 17, 15, 0.86);
            --surface-strong: rgba(9, 28, 23, 0.96);
            --danger: #ff8a80;
            --warning: #ffd18a;
        }
        * { box-sizing: border-box; }
        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Inter", "Segoe UI", Arial, sans-serif;
            background:
                linear-gradient(90deg, rgba(30, 231, 165, 0.04) 1px, transparent 1px),
                linear-gradient(rgba(30, 231, 165, 0.035) 1px, transparent 1px),
                radial-gradient(circle at 18% 18%, rgba(30, 231, 165, 0.17), transparent 24%),
                radial-gradient(circle at 86% 16%, rgba(14, 124, 102, 0.18), transparent 22%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #030806);
            background-size: 42px 42px, 42px 42px, auto, auto, auto;
        }
        .shell {
            width: min(1480px, calc(100% - 32px));
            margin: 24px auto;
            padding: 24px;
            border-radius: 18px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 24, 19, 0.9), rgba(3, 10, 8, 0.94));
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.34);
        }
        .review-header {
            display: flex;
            justify-content: space-between;
            gap: 18px;
            align-items: flex-start;
            flex-wrap: wrap;
        }
        .review-copy {
            max-width: 760px;
        }
        .eyebrow {
            display: inline-flex;
            padding: 7px 11px;
            border-radius: 8px;
            color: var(--accent);
            border: 1px solid rgba(111, 247, 255, 0.2);
            background: var(--accent-soft);
            text-transform: uppercase;
            letter-spacing: 0.08em;
            font-size: 11px;
            font-weight: 900;
        }
        h1 {
            margin: 12px 0 8px;
            font-size: clamp(2rem, 3vw, 3rem);
            line-height: 1.05;
            letter-spacing: 0;
        }
        .subtitle, .flash { color: var(--muted); line-height: 1.65; }
        .nav-actions { display: flex; gap: 10px; flex-wrap: wrap; }
        .nav-actions a, .save-button, .reject-button {
            text-decoration: none;
            border: none;
            cursor: pointer;
            color: var(--text);
            padding: 11px 15px;
            border-radius: 8px;
            font-weight: 850;
            letter-spacing: 0;
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
        }
        .nav-actions a:hover,
        .save-button:hover,
        .reject-button:hover {
            border-color: rgba(30, 231, 165, 0.5);
            transform: translateY(-1px);
        }
        .save-button {
            color: #03120d;
            background: #1ee7a5;
            border-color: #1ee7a5;
        }
        .reject-button {
            color: #ffcac5;
            background: rgba(180, 35, 24, 0.16);
            border-color: rgba(255, 138, 128, 0.32);
        }
        .review-metrics {
            display: grid;
            grid-template-columns: repeat(2, minmax(150px, 1fr));
            gap: 10px;
            min-width: 330px;
        }
        .metric-card {
            padding: 14px;
            border-radius: 12px;
            border: 1px solid rgba(139, 231, 196, 0.2);
            background: rgba(255, 255, 255, 0.06);
        }
        .metric-label {
            display: block;
            color: var(--muted);
            font-size: 0.72rem;
            font-weight: 900;
            text-transform: uppercase;
        }
        .metric-value {
            display: block;
            margin-top: 6px;
            color: #ffffff;
            font-size: 1.35rem;
            font-weight: 900;
        }
        .table-panel {
            margin-top: 22px;
            border-radius: 14px;
            background: #ffffff;
            border: 1px solid rgba(139, 231, 196, 0.2);
            overflow: hidden;
        }
        .table-tools {
            display: flex;
            justify-content: space-between;
            gap: 14px;
            align-items: center;
            padding: 14px 16px;
            border-bottom: 1px solid rgba(139, 231, 196, 0.18);
            background: #071b14;
        }
        .selection-status {
            color: var(--muted);
            font-size: 0.9rem;
            font-weight: 750;
        }
        .select-all-label {
            display: inline-flex;
            align-items: center;
            gap: 9px;
            color: #dffdf1;
            font-size: 0.9rem;
            font-weight: 850;
            cursor: pointer;
        }
        .table-scroll {
            max-height: 68vh;
            overflow: auto;
            background: #ffffff;
        }
        table {
            width: 100%;
            min-width: 1320px;
            border-collapse: separate;
            border-spacing: 0;
        }
        th, td {
            padding: 14px 16px;
            text-align: left;
            border-bottom: 1px solid #e5eee9;
            vertical-align: top;
        }
        th {
            position: sticky;
            top: 0;
            z-index: 2;
            color: #8be7c4;
            background: #071b14;
            font-size: 0.72rem;
            font-weight: 900;
            letter-spacing: 0.06em;
            text-transform: uppercase;
            white-space: nowrap;
        }
        tbody tr {
            background: #ffffff;
        }
        tbody tr:hover td {
            background: #f7faf8;
        }
        .message-text {
            color: #14241d;
            font-size: 0.95rem;
            font-weight: 700;
            line-height: 1.55;
        }
        .data-pill,
        .status-pill {
            display: inline-flex;
            align-items: center;
            min-height: 28px;
            padding: 5px 9px;
            border-radius: 7px;
            border: 1px solid #d8e4de;
            background: #f7faf8;
            color: #14241d;
            font-size: 0.78rem;
            font-weight: 850;
        }
        .status-pill {
            color: #075f4e;
            background: #e8f7ef;
            border-color: #bfe8cf;
            text-transform: uppercase;
        }
        .empty-state { padding: 46px 24px; text-align: center; color: var(--muted); }
        .footer-actions {
            position: sticky;
            bottom: 0;
            z-index: 4;
            margin-top: 0;
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            flex-wrap: wrap;
            padding: 16px;
            border-top: 1px solid rgba(139, 231, 196, 0.18);
            background: rgba(3, 10, 8, 0.92);
            backdrop-filter: blur(12px);
        }
        .flash {
            margin-top: 18px;
            padding: 14px 16px;
            border-radius: 12px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.05);
        }
        .reason-field {
            width: 100%;
            min-height: 96px;
            border-radius: 10px;
            border: 1px solid #d8e4de;
            background: #ffffff;
            color: #14241d;
            padding: 12px 14px;
            font: inherit;
            resize: vertical;
        }
        .reason-field:focus {
            outline: none;
            border-color: rgba(30, 231, 165, 0.72);
            box-shadow: 0 0 0 3px rgba(30, 231, 165, 0.12);
        }
        .alert-badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 6px 9px;
            border-radius: 7px;
            font-size: 0.68rem;
            font-weight: 900;
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
            color: #8f1f17;
            background: #fff0ee;
            border: 1px solid #fac7c2;
        }
        .alert-badge.standard {
            color: #1d4ed8;
            background: #eff6ff;
            border: 1px solid #bfdbfe;
        }
        .alert-badge.general {
            color: #405147;
            background: #f4f7f5;
            border: 1px solid #d8e4de;
        }
        .alert-badge.fight {
            color: #8a4b08;
            background: #fff5df;
            border: 1px solid #f4d58a;
        }
        .alert-badge.crowd {
            color: #075f4e;
            background: #e8f7ef;
            border: 1px solid #bfe8cf;
        }
        .detail-text {
            color: #607267;
            line-height: 1.55;
            font-size: 0.9rem;
        }
        .sender-code {
            color: #31443a;
            font-family: "SFMono-Regular", Consolas, monospace;
            font-size: 0.78rem;
            line-height: 1.45;
            word-break: break-word;
        }
        .date-cell {
            color: #405147;
            white-space: nowrap;
            font-size: 0.86rem;
            font-weight: 750;
        }
        .select-cell {
            text-align: center;
            vertical-align: middle;
        }
        input[type="checkbox"] {
            width: 22px;
            height: 22px;
            accent-color: #1ee7a5;
            cursor: pointer;
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
        @media (max-width: 900px) {
            .shell {
                width: min(100% - 16px, 1480px);
                margin: 8px auto;
                padding: 18px;
            }

            .review-metrics {
                width: 100%;
                min-width: 0;
            }

            .table-scroll {
                max-height: 62vh;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<div class="shell">
    <div class="review-header">
        <div class="review-copy">
            <span class="eyebrow">Traveler moderation</span>
            <h1>Review traveler reports</h1>
            <p class="subtitle">Approve pending traveler messages before they become visible in the public report feed.</p>
        </div>
        <div class="review-metrics" aria-label="Moderation summary">
            <div class="metric-card">
                <span class="metric-label">Pending reports</span>
                <span class="metric-value"><%= pendingCount %></span>
            </div>
            <div class="metric-card">
                <span class="metric-label">Selected</span>
                <span class="metric-value" id="selectedCount">0</span>
            </div>
        </div>
    </div>

    <div class="nav-actions">
        <a href="adminHub">Home</a>
    </div>

    <% if (successMessage != null) { %><div class="flash"><%= successMessage %></div><% } %>
    <% if (error != null) { %><div class="flash"><%= error %></div><% } %>

    <form action="reviewTravelerCommunications" method="post" accept-charset="UTF-8">
        <div class="table-panel">
            <div class="table-tools">
                <label class="select-all-label">
                    <input type="checkbox" id="selectAllReports">
                    Select all visible reports
                </label>
                <span class="selection-status" id="selectionStatus">No reports selected</span>
            </div>
            <div class="table-scroll">
                <table>
                    <thead>
                    <tr>
                        <th style="width: 25%">Message</th>
                        <th style="width: 9%">City</th>
                        <th style="width: 13%">Alert type</th>
                        <th style="width: 12%">Station</th>
                        <th style="width: 13%">Appearance</th>
                        <th style="width: 11%">Sender</th>
                        <th style="width: 8%">Status</th>
                        <th style="width: 9%">Date</th>
                        <th style="width: 16%">Reject reason</th>
                        <th style="width: 6%; text-align: center;">Select</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (pendingMessages == null || pendingMessages.isEmpty()) { %>
                    <tr><td colspan="10" class="empty-state">No pending traveler reports.</td></tr>
                    <% } else { for (MessageBean m : pendingMessages) { %>
                    <tr>
                        <td class="message-text"><%= StringEscapeUtils.escapeHtml4(m.getMessage()) %></td>
                        <td><span class="data-pill"><%= StringEscapeUtils.escapeHtml4(m.getCity() != null && !m.getCity().isBlank() ? m.getCity() : "Unknown") %></span></td>
                        <td>
                            <div class="alert-badge-stack">
                                <% if (Boolean.TRUE.equals(m.getPickpocketAlert())) { %>
                                <span class="alert-badge pickpocket">Pickpocket</span>
                                <% } %>
                                <% if (Boolean.TRUE.equals(m.getFightAlert())) { %>
                                <span class="alert-badge fight">Fight</span>
                                <% } %>
                                <% if (Boolean.TRUE.equals(m.getCrowdAlert())) { %>
                                <span class="alert-badge crowd">Crowd</span>
                                <% } %>
                                <% if (Boolean.TRUE.equals(m.getGeneralAlert())) { %>
                                <span class="alert-badge general">General</span>
                                <% } %>
                                <% if (!Boolean.TRUE.equals(m.getPickpocketAlert()) && !Boolean.TRUE.equals(m.getFightAlert()) && !Boolean.TRUE.equals(m.getCrowdAlert()) && !Boolean.TRUE.equals(m.getGeneralAlert())) { %>
                                <span class="alert-badge standard">Standard</span>
                                <% } %>
                            </div>
                        </td>
                        <td class="detail-text"><%= StringEscapeUtils.escapeHtml4(m.getStationName() != null && !m.getStationName().isBlank() ? m.getStationName() : "Not specified") %></td>
                        <td class="detail-text"><%= StringEscapeUtils.escapeHtml4(m.getSuspectClothing() != null && !m.getSuspectClothing().isBlank() ? m.getSuspectClothing() : "Not specified") %></td>
                        <td class="sender-code"><%= StringEscapeUtils.escapeHtml4(m.getSenderCf() != null ? m.getSenderCf() : "Unknown traveler") %></td>
                        <td><span class="status-pill"><%= StringEscapeUtils.escapeHtml4(m.getStatus() != null ? m.getStatus() : "PENDING") %></span></td>
                        <td class="date-cell"><%= sdf.format(m.getDate()) %></td>
                        <td>
                            <textarea
                                    class="reason-field"
                                    name="rejectReason_<%= m.getDate().getTime() %>"
                                    placeholder="Required only if rejected."></textarea>
                        </td>
                        <td class="select-cell">
                            <input type="checkbox" name="selectedMessages" value="<%= StringEscapeUtils.escapeHtml4(m.getDate().getTime() + "|" + (m.getSenderCf() != null ? m.getSenderCf() : "") + "|" + m.getMessage()) %>">
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
        const selectAll = document.getElementById('selectAllReports');
        const selectedCount = document.getElementById('selectedCount');
        const selectionStatus = document.getElementById('selectionStatus');

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

        function allReportCheckboxes() {
            return Array.from(form.querySelectorAll('input[name="selectedMessages"]'));
        }

        function syncSelectionState() {
            const selected = selectedEntries().length;
            const total = allReportCheckboxes().length;
            selectedCount.textContent = String(selected);
            selectionStatus.textContent = selected === 0
                ? 'No reports selected'
                : selected + ' of ' + total + ' selected';

            if (selectAll) {
                selectAll.checked = total > 0 && selected === total;
                selectAll.indeterminate = selected > 0 && selected < total;
            }
        }

        form.addEventListener('change', function (event) {
            if (event.target.name === 'selectedMessages') {
                syncSelectionState();
            }
        });

        if (selectAll) {
            selectAll.addEventListener('change', function () {
                allReportCheckboxes().forEach(function (checkbox) {
                    checkbox.checked = selectAll.checked;
                });
                syncSelectionState();
            });
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

        syncSelectionState();
    })();
</script>
</body>
</html>
