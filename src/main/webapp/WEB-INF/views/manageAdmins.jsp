<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.web.routex.bean.AdminUserBean" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Manage Admins</title>
    <style>
        :root {
            --bg-1: #04111f;
            --bg-2: #0a1f37;
            --line: rgba(111, 247, 255, 0.18);
            --text: #ecf7ff;
            --muted: #91abc2;
            --accent: #6ff7ff;
            --success: #89ffd1;
            --danger: #ff8fa3;
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
            padding: 18px;
        }

        .shell {
            width: min(1180px, 100%);
            margin: 24px auto;
            padding: 28px;
            border-radius: 32px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.92));
            box-shadow: 0 32px 84px rgba(0, 0, 0, 0.4);
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
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
            margin: 16px 0 10px;
            font-size: clamp(2.2rem, 4vw, 3.8rem);
            line-height: 0.96;
        }

        .subtitle {
            margin: 0;
            color: var(--muted);
            line-height: 1.75;
            max-width: 760px;
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
        }

        .grid {
            margin-top: 24px;
            display: grid;
            grid-template-columns: 0.95fr 1.05fr;
            gap: 20px;
        }

        .panel {
            padding: 22px;
            border-radius: 28px;
            border: 1px solid rgba(255, 255, 255, 0.08);
            background: rgba(255, 255, 255, 0.04);
        }

        .panel h2 {
            margin: 0 0 8px;
            font-size: 1.35rem;
        }

        .panel p {
            margin: 0 0 18px;
            color: var(--muted);
            line-height: 1.7;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 12px;
        }

        .field-group.full {
            grid-column: 1 / -1;
        }

        .field-group label {
            display: block;
            margin-bottom: 8px;
            font-size: 0.92rem;
            color: #dff8ff;
        }

        .field-group input {
            width: 100%;
            padding: 14px 16px;
            border-radius: 16px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.05);
            color: var(--text);
            font: inherit;
            outline: none;
        }

        .field-group input:focus {
            border-color: rgba(111, 247, 255, 0.5);
            box-shadow: 0 0 0 3px rgba(111, 247, 255, 0.08);
        }

        .admin-list {
            display: grid;
            gap: 12px;
        }

        .admin-row {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            gap: 14px;
            padding: 16px;
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .admin-meta strong {
            display: block;
            font-size: 1rem;
            margin-bottom: 4px;
        }

        .admin-meta span {
            display: block;
            color: var(--muted);
            font-size: 0.9rem;
            line-height: 1.6;
        }

        .selector {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            color: var(--muted);
            font-size: 0.88rem;
        }

        .flash {
            margin-top: 18px;
            padding: 14px 16px;
            border-radius: 18px;
            border: 1px solid rgba(255, 255, 255, 0.12);
        }

        .flash.success {
            background: rgba(68, 227, 164, 0.12);
            border-color: rgba(68, 227, 164, 0.28);
        }

        .flash.error {
            background: rgba(255, 143, 163, 0.12);
            border-color: rgba(255, 143, 163, 0.28);
        }

        .actions {
            margin-top: 18px;
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .btn {
            border: none;
            cursor: pointer;
            padding: 14px 20px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.04em;
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
            box-shadow: 0 18px 32px rgba(111, 247, 255, 0.2);
        }

        .btn.danger {
            color: #fff5f7;
            background: linear-gradient(90deg, #d94b6a, #ff8fa3);
            box-shadow: 0 18px 32px rgba(217, 75, 106, 0.2);
        }

        .empty-state {
            padding: 18px;
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.04);
            color: var(--muted);
        }

        @media (max-width: 920px) {
            .grid,
            .form-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<%
    List<AdminUserBean> admins = (List<AdminUserBean>) request.getAttribute("admins");
    List<AdminUserBean> workers = (List<AdminUserBean>) request.getAttribute("workers");
    List<AdminUserBean> travelers = (List<AdminUserBean>) request.getAttribute("travelers");
    String inlineError = (String) request.getAttribute("inlineError");
    String successMessage = null;
    if (session != null) {
        successMessage = (String) session.getAttribute("alertMessage");
        if (successMessage != null) {
            session.removeAttribute("alertMessage");
        }
    }
%>
<div class="shell">
    <div class="topbar">
        <div>
            <span class="eyebrow">Admin governance</span>
            <h1>Manage administrator accounts</h1>
            <p class="subtitle">Create new admin accounts or remove existing ones. These actions update the database directly.</p>
        </div>

        <div class="nav-actions">
            <a href="adminHub">Home</a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <% if (successMessage != null) { %>
    <div class="flash success"><%= successMessage %></div>
    <% } %>

    <% if (inlineError != null) { %>
    <div class="flash error"><%= inlineError %></div>
    <% } %>

    <div class="grid">
        <section class="panel">
            <h2>Create new admin</h2>
            <p>Fill in the required data to add a new administrator account to the platform.</p>

            <form action="manageAdmins" method="post">
                <input type="hidden" name="action" value="createAdmin">
                <div class="form-grid">
                    <div class="field-group">
                        <label for="nome">Name</label>
                        <input type="text" id="nome" name="nome" required>
                    </div>
                    <div class="field-group">
                        <label for="cognome">Surname</label>
                        <input type="text" id="cognome" name="cognome" required>
                    </div>
                    <div class="field-group full">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    <div class="field-group">
                        <label for="password">Password</label>
                        <input type="text" id="password" name="password" required>
                    </div>
                    <div class="field-group">
                        <label for="codiceFiscale">Tax code</label>
                        <input type="text" id="codiceFiscale" name="codiceFiscale" required>
                    </div>
                </div>

                <div class="actions">
                    <button type="submit" class="btn">Create admin</button>
                </div>
            </form>
        </section>

        <section class="panel">
            <h2>Existing admins</h2>
            <p>Select one or more administrator accounts to remove them from the platform.</p>

            <% if (admins != null && !admins.isEmpty()) { %>
            <form action="manageAdmins" method="post">
                <input type="hidden" name="action" value="deleteAdmins">
                <div class="admin-list">
                    <% for (AdminUserBean admin : admins) { %>
                    <div class="admin-row">
                        <div class="admin-meta">
                            <strong><%= admin.getNome() %> <%= admin.getCognome() %></strong>
                            <span><%= admin.getEmail() %></span>
                            <span><%= admin.getCodiceFiscale() %></span>
                        </div>
                        <label class="selector">
                            <input type="checkbox" name="selectedAdmins" value="<%= admin.getCodiceFiscale() %>">
                            <span>Select</span>
                        </label>
                    </div>
                    <% } %>
                </div>

                <div class="actions">
                    <button type="submit" class="btn danger" onclick="return confirm('Do you want to delete the selected admin accounts?');">Delete selected</button>
                </div>
            </form>
            <% } else { %>
            <div class="empty-state">No admin accounts available.</div>
            <% } %>
        </section>
    </div>

    <div class="grid">
        <section class="panel">
            <h2>Worker accounts</h2>
            <p>Overview of worker accounts with the option to remove selected profiles.</p>

            <% if (workers != null && !workers.isEmpty()) { %>
            <form action="manageAdmins" method="post">
                <input type="hidden" name="action" value="deleteWorkers">
                <div class="admin-list">
                    <% for (AdminUserBean worker : workers) { %>
                    <div class="admin-row">
                        <div class="admin-meta">
                            <strong><%= worker.getNome() %> <%= worker.getCognome() %></strong>
                            <span><%= worker.getEmail() %></span>
                            <span><%= worker.getCodiceFiscale() %></span>
                        </div>
                        <label class="selector">
                            <input type="checkbox" name="selectedWorkers" value="<%= worker.getCodiceFiscale() %>">
                            <span>Select</span>
                        </label>
                    </div>
                    <% } %>
                </div>

                <div class="actions">
                    <button type="submit" class="btn danger" onclick="return confirm('Do you want to delete the selected worker accounts?');">Delete selected workers</button>
                </div>
            </form>
            <% } else { %>
            <div class="empty-state">No worker accounts available.</div>
            <% } %>
        </section>

        <section class="panel">
            <h2>Traveler accounts</h2>
            <p>Overview of traveler accounts with the option to remove selected users and their linked travel data.</p>

            <% if (travelers != null && !travelers.isEmpty()) { %>
            <form action="manageAdmins" method="post">
                <input type="hidden" name="action" value="deleteTravelers">
                <div class="admin-list">
                    <% for (AdminUserBean traveler : travelers) { %>
                    <div class="admin-row">
                        <div class="admin-meta">
                            <strong><%= traveler.getNome() %> <%= traveler.getCognome() %></strong>
                            <span><%= traveler.getEmail() %></span>
                            <span><%= traveler.getCodiceFiscale() %></span>
                        </div>
                        <label class="selector">
                            <input type="checkbox" name="selectedTravelers" value="<%= traveler.getCodiceFiscale() %>">
                            <span>Select</span>
                        </label>
                    </div>
                    <% } %>
                </div>

                <div class="actions">
                    <button type="submit" class="btn danger" onclick="return confirm('Do you want to delete the selected traveler accounts? Linked traveler data will also be removed.');">Delete selected travelers</button>
                </div>
            </form>
            <% } else { %>
            <div class="empty-state">No traveler accounts available.</div>
            <% } %>
        </section>
    </div>
</div>
</body>
</html>
