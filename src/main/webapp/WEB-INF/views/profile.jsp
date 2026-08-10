<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="it.web.safeflow.model.UserProfile" %>
<%@ page import="it.web.safeflow.model.UserProfileStats" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%!
    private int maxValue(Map<String, Integer> values) {
        int max = 0;
        if (values != null) {
            for (Integer value : values.values()) {
                if (value != null && value > max) {
                    max = value;
                }
            }
        }
        return max;
    }

    private boolean hasValues(Map<String, Integer> values) {
        return maxValue(values) > 0;
    }

    private int barPercent(int value, int max) {
        if (max <= 0) {
            return 0;
        }
        return Math.max(8, Math.round((value * 100.0f) / max));
    }
%>
<%
    UserProfile profile = (UserProfile) request.getAttribute("profile");
    String rawNome = session != null && session.getAttribute("nome") != null ? session.getAttribute("nome").toString() : "";
    String rawCognome = session != null && session.getAttribute("cognome") != null ? session.getAttribute("cognome").toString() : "";
    String nome = StringEscapeUtils.escapeHtml4(rawNome);
    String cognome = StringEscapeUtils.escapeHtml4(rawCognome);
    String ruolo = session != null && session.getAttribute("ruolo") != null ? StringEscapeUtils.escapeHtml4(session.getAttribute("ruolo").toString()) : "";
    String bio = profile != null && profile.getBio() != null ? StringEscapeUtils.escapeHtml4(profile.getBio()) : "";
    boolean hasAvatar = profile != null && profile.isAvatarPresent();
    UserProfileStats stats = profile != null ? profile.getStats() : new UserProfileStats(0, 0, 0);
    Map<String, Integer> categoryCounts = stats.getCategoryCounts();
    Map<String, Integer> monthlyCounts = stats.getMonthlyReportCounts();
    Map<String, Integer> stationCounts = stats.getStationCounts();
    int categoryMax = maxValue(categoryCounts);
    int monthlyMax = maxValue(monthlyCounts);
    String rankLabel = stats.getCommunityRank() > 0 ? "#" + stats.getCommunityRank() : "Unranked";
    boolean saved = "1".equals(request.getParameter("saved"));
    boolean imageRemoved = "1".equals(request.getParameter("imageRemoved"));
    String profileError = (String) request.getAttribute("profileError");
    String homeTarget = "ADMIN".equalsIgnoreCase(ruolo) ? "adminHub" : "travelerHome";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Profile</title>
    <style>
        body {
            margin: 0;
            min-height: 100vh;
            background: #f3f6f4;
            color: #14241d;
            font-family: "Inter", "Segoe UI", Arial, sans-serif;
        }

        .profile-shell {
            width: min(980px, calc(100% - 32px));
            margin: 72px auto 24px;
            display: grid;
            grid-template-columns: 300px minmax(0, 1fr);
            gap: 18px;
        }

        .profile-card,
        .profile-form {
            background: #ffffff;
            border: 1px solid #d8e4de;
            border-radius: 16px;
            box-shadow: 0 18px 42px rgba(20, 36, 29, 0.08);
            padding: 24px;
        }

        .profile-card {
            text-align: center;
            height: fit-content;
        }

        .profile-bio-preview {
            margin: 18px 0 0;
            padding: 16px;
            border-radius: 12px;
            color: #31443a;
            background: #f7faf8;
            border: 1px solid #d8e4de;
            line-height: 1.55;
            text-align: left;
            overflow-wrap: anywhere;
        }

        .profile-bio-preview.empty {
            color: #607267;
            font-style: italic;
        }

        .profile-stats {
            margin-top: 16px;
            display: grid;
            grid-template-columns: 1fr;
            gap: 10px;
        }

        .profile-stat {
            padding: 14px;
            border-radius: 12px;
            background: #ffffff;
            border: 1px solid #d8e4de;
            text-align: left;
        }

        .profile-stat-label {
            display: block;
            color: #607267;
            font-size: 0.76rem;
            font-weight: 850;
            text-transform: uppercase;
        }

        .profile-stat-value {
            display: block;
            margin-top: 4px;
            color: #14241d;
            font-size: 1.45rem;
            font-weight: 900;
        }

        .profile-avatar-large {
            width: 128px;
            height: 128px;
            margin: 0 auto 18px;
            border-radius: 50%;
            overflow: hidden;
            display: grid;
            place-items: center;
            color: #ffffff;
            background: #0e7c66;
            border: 4px solid #e5f3ee;
            font-size: 2rem;
            font-weight: 850;
        }

        .profile-avatar-large img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        h1 {
            margin: 0 0 8px;
            font-size: 1.9rem;
            line-height: 1.1;
        }

        .role-pill {
            display: inline-flex;
            padding: 7px 10px;
            border-radius: 999px;
            background: #e5f3ee;
            color: #075f4e;
            border: 1px solid #c8e2d8;
            font-size: 0.75rem;
            font-weight: 850;
            text-transform: uppercase;
        }

        .profile-form h2 {
            margin: 0 0 8px;
            font-size: 1.35rem;
        }

        .profile-form p {
            margin: 0 0 20px;
            color: #607267;
            line-height: 1.6;
        }

        label {
            display: block;
            margin: 16px 0 8px;
            color: #405147;
            font-weight: 800;
        }

        textarea {
            width: 100%;
            border: 1px solid #d8e4de;
            border-radius: 12px;
            background: #ffffff;
            color: #14241d;
            padding: 12px;
            font: inherit;
        }

        textarea {
            min-height: 160px;
            resize: vertical;
        }

        .profile-file-input {
            position: absolute;
            width: 1px;
            height: 1px;
            overflow: hidden;
            clip: rect(0, 0, 0, 0);
        }

        .selected-file {
            margin-top: 10px;
            color: #607267;
            font-size: 0.92rem;
            font-weight: 700;
        }

        .profile-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            margin-top: 18px;
        }

        .profile-actions button,
        .profile-actions a,
        .image-action-button {
            border-radius: 8px;
            padding: 11px 15px;
            font-weight: 800;
            text-decoration: none;
            border: 1px solid #d8e4de;
            cursor: pointer;
        }

        .profile-actions button,
        .image-action-button.primary {
            color: #ffffff;
            background: #0e7c66;
            border-color: #0e7c66;
        }

        .image-action-button.danger {
            color: #b42318;
            background: #fff0ee;
            border-color: #fac7c2;
        }

        .profile-actions a {
            color: #31443a;
            background: #f7faf8;
        }

        .bio-editor {
            display: none;
            margin-top: 16px;
        }

        .bio-editor.open {
            display: block;
        }

        .profile-alert {
            margin-bottom: 16px;
            padding: 12px 14px;
            border-radius: 12px;
            font-weight: 750;
        }

        .profile-alert.success {
            color: #147a4b;
            background: #e8f7ef;
            border: 1px solid #bfe8cf;
        }

        .profile-alert.error {
            color: #b42318;
            background: #fff0ee;
            border: 1px solid #fac7c2;
        }

        .profile-insights {
            margin-top: 24px;
            padding-top: 22px;
            border-top: 1px solid #d8e4de;
        }

        .insight-header {
            display: flex;
            align-items: flex-end;
            justify-content: space-between;
            gap: 14px;
            margin-bottom: 16px;
        }

        .insight-header h3 {
            margin: 0;
            font-size: 1.12rem;
        }

        .rank-pill {
            display: inline-flex;
            align-items: center;
            border-radius: 999px;
            padding: 8px 12px;
            color: #063b2b;
            background: #dff8e9;
            border: 1px solid #bce9cd;
            font-size: 0.8rem;
            font-weight: 900;
            white-space: nowrap;
        }

        .insight-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 16px;
        }

        .insight-panel {
            border: 1px solid #d8e4de;
            border-radius: 14px;
            padding: 16px;
            background: #fbfdfc;
        }

        .insight-panel h4 {
            margin: 0 0 14px;
            font-size: 0.92rem;
            color: #405147;
            text-transform: uppercase;
            letter-spacing: 0.04em;
        }

        .mini-metrics {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 10px;
            margin-bottom: 16px;
        }

        .mini-metric {
            padding: 12px;
            border-radius: 12px;
            background: #ffffff;
            border: 1px solid #d8e4de;
        }

        .mini-metric strong {
            display: block;
            color: #14241d;
            font-size: 1.2rem;
        }

        .mini-metric span {
            display: block;
            margin-top: 3px;
            color: #607267;
            font-size: 0.74rem;
            font-weight: 850;
            text-transform: uppercase;
        }

        .bar-row {
            display: grid;
            grid-template-columns: minmax(90px, 0.9fr) minmax(0, 1.4fr) 34px;
            align-items: center;
            gap: 10px;
            margin-top: 10px;
            color: #405147;
            font-size: 0.86rem;
            font-weight: 750;
        }

        .bar-track {
            height: 10px;
            overflow: hidden;
            border-radius: 999px;
            background: #e8efeb;
        }

        .bar-fill {
            height: 100%;
            width: var(--bar-width);
            min-width: 0;
            border-radius: inherit;
            background: linear-gradient(90deg, #0e7c66, #6ee7a8);
        }

        .month-chart {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(48px, 1fr));
            gap: 10px;
            align-items: end;
            min-height: 190px;
        }

        .month-bar {
            display: grid;
            grid-template-rows: 1fr auto auto;
            gap: 8px;
            height: 178px;
            text-align: center;
            color: #607267;
            font-size: 0.72rem;
            font-weight: 800;
        }

        .month-bar-fill {
            align-self: end;
            justify-self: center;
            width: 24px;
            height: var(--bar-height);
            min-height: 8px;
            border-radius: 999px 999px 6px 6px;
            background: linear-gradient(180deg, #6ee7a8, #0e7c66);
            box-shadow: 0 10px 20px rgba(14, 124, 102, 0.16);
        }

        .empty-insight {
            color: #607267;
            background: #ffffff;
            border: 1px dashed #cddbd4;
            border-radius: 12px;
            padding: 14px;
            line-height: 1.5;
        }

        @media (max-width: 760px) {
            .profile-shell {
                grid-template-columns: 1fr;
                margin-top: 82px;
            }

            .insight-grid,
            .mini-metrics {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<main class="profile-shell">
    <aside class="profile-card">
        <div class="profile-avatar-large">
            <% if (hasAvatar) { %>
            <img src="profileAvatar?t=<%= System.currentTimeMillis() %>" alt="">
            <% } else { %>
            <span><%= (!rawNome.isBlank() ? StringEscapeUtils.escapeHtml4(rawNome.substring(0, 1).toUpperCase()) : "S") %><%= (!rawCognome.isBlank() ? StringEscapeUtils.escapeHtml4(rawCognome.substring(0, 1).toUpperCase()) : "F") %></span>
            <% } %>
        </div>
	        <h1><%= nome %> <%= cognome %></h1>
	        <span class="role-pill"><%= ruolo %></span>
	        <div class="profile-bio-preview <%= bio.isBlank() ? "empty" : "" %>">
	            <%= bio.isBlank() ? "No bio yet." : bio %>
	        </div>
	        <div class="profile-stats" aria-label="Profile statistics">
	            <div class="profile-stat">
	                <span class="profile-stat-label">Reports made</span>
	                <span class="profile-stat-value"><%= stats.getReportCount() %></span>
	            </div>
	            <div class="profile-stat">
	                <span class="profile-stat-label">Cities reported</span>
	                <span class="profile-stat-value"><%= stats.getCityCount() %></span>
	            </div>
	            <div class="profile-stat">
	                <span class="profile-stat-label">Approvals received</span>
	                <span class="profile-stat-value"><%= stats.getApprovalCount() %></span>
	            </div>
	        </div>
	    </aside>

    <section class="profile-form">
        <% if (saved) { %>
        <div class="profile-alert success">Profile updated successfully.</div>
        <% } %>
        <% if (imageRemoved) { %>
        <div class="profile-alert success">Profile image removed successfully.</div>
        <% } %>
        <% if (profileError != null && !profileError.isBlank()) { %>
	        <div class="profile-alert error"><%= StringEscapeUtils.escapeHtml4(profileError) %></div>
        <% } %>

	        <h2>Personal profile</h2>
	        <p>Manage your profile image and personal bio.</p>

	        <form action="profile" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
	            <label for="avatar">Profile image</label>
            <input class="profile-file-input" id="avatar" name="avatar" type="file" accept="image/png,image/jpeg,image/webp,image/gif">
            <div class="profile-actions">
                <% if (hasAvatar) { %>
                <button class="image-action-button danger" type="submit" name="action" value="removeAvatar" formnovalidate>Remove profile image</button>
                <% } else { %>
                <button class="image-action-button primary" type="button" onclick="document.getElementById('avatar').click();">Add profile image</button>
                <% } %>
	            </div>
	            <div class="selected-file" id="selectedFile" aria-live="polite"></div>

	            <div class="profile-actions">
	                <button class="image-action-button primary" type="button" id="toggleBioEditor">Write something about yourself</button>
	            </div>

	            <div class="bio-editor <%= profileError != null && !profileError.isBlank() ? "open" : "" %>" id="bioEditor">
	                <label for="bio">Bio</label>
	                <textarea id="bio" name="bio" maxlength="500" placeholder="Write a short description..."><%= bio %></textarea>
	            </div>

	            <div class="profile-actions">
	                <button type="submit">Save profile</button>
                <a href="<%= homeTarget %>">Back to home</a>
            </div>
        </form>

        <section class="profile-insights" aria-label="Profile insights">
            <div class="insight-header">
                <div>
                    <h3>Safety contribution</h3>
                    <p>Your reporting activity and community impact.</p>
                </div>
                <span class="rank-pill"><%= rankLabel %> - <%= StringEscapeUtils.escapeHtml4(stats.getTrustLevel()) %></span>
            </div>

            <div class="mini-metrics">
                <div class="mini-metric">
                    <strong><%= stats.getApprovalRatePercent() %>%</strong>
                    <span>Approval rate</span>
                </div>
                <div class="mini-metric">
                    <strong><%= stats.getHelpfulScore() %></strong>
                    <span>Helpful score</span>
                </div>
                <div class="mini-metric">
                    <strong><%= stats.getCommunityScore() %></strong>
                    <span>Community score</span>
                </div>
                <div class="mini-metric">
                    <strong><%= stats.getApprovalCount() %></strong>
                    <span>Approved</span>
                </div>
                <div class="mini-metric">
                    <strong><%= stats.getPendingCount() %></strong>
                    <span>Pending</span>
                </div>
                <div class="mini-metric">
                    <strong><%= stats.getRejectedCount() %></strong>
                    <span>Rejected</span>
                </div>
            </div>

            <div class="insight-grid">
                <div class="insight-panel">
                    <h4>Report categories</h4>
                    <% if (hasValues(categoryCounts)) { %>
                    <% for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
                        int value = entry.getValue() == null ? 0 : entry.getValue();
                        if (value <= 0) {
                            continue;
                        }
                    %>
                    <div class="bar-row">
                        <span><%= StringEscapeUtils.escapeHtml4(entry.getKey()) %></span>
                        <div class="bar-track">
                            <div class="bar-fill" style="--bar-width: <%= barPercent(value, categoryMax) %>%;"></div>
                        </div>
                        <strong><%= value %></strong>
                    </div>
                    <% } %>
                    <% } else { %>
                    <div class="empty-insight">No categorized reports yet.</div>
                    <% } %>
                </div>

                <div class="insight-panel">
                    <h4>Top stations</h4>
                    <% if (hasValues(stationCounts)) { %>
                    <% for (Map.Entry<String, Integer> entry : stationCounts.entrySet()) {
                        int value = entry.getValue() == null ? 0 : entry.getValue();
                    %>
                    <div class="bar-row">
                        <span><%= StringEscapeUtils.escapeHtml4(entry.getKey()) %></span>
                        <div class="bar-track">
                            <div class="bar-fill" style="--bar-width: <%= barPercent(value, maxValue(stationCounts)) %>%;"></div>
                        </div>
                        <strong><%= value %></strong>
                    </div>
                    <% } %>
                    <% } else { %>
                    <div class="empty-insight">No station data available yet.</div>
                    <% } %>
                </div>

                <div class="insight-panel">
                    <h4>Reports over time</h4>
                    <% if (hasValues(monthlyCounts)) { %>
                    <div class="month-chart">
                        <% for (Map.Entry<String, Integer> entry : monthlyCounts.entrySet()) {
                            int value = entry.getValue() == null ? 0 : entry.getValue();
                        %>
                        <div class="month-bar">
                            <div class="month-bar-fill" style="--bar-height: <%= barPercent(value, monthlyMax) %>%;"></div>
                            <strong><%= value %></strong>
                            <span><%= StringEscapeUtils.escapeHtml4(entry.getKey()) %></span>
                        </div>
                        <% } %>
                    </div>
                    <% } else { %>
                    <div class="empty-insight">No reporting timeline yet.</div>
                    <% } %>
                </div>

                <div class="insight-panel">
                    <h4>Cities covered</h4>
                    <% if (hasValues(stats.getCityCounts())) { %>
                    <% for (Map.Entry<String, Integer> entry : stats.getCityCounts().entrySet()) {
                        int value = entry.getValue() == null ? 0 : entry.getValue();
                    %>
                    <div class="bar-row">
                        <span><%= StringEscapeUtils.escapeHtml4(entry.getKey()) %></span>
                        <div class="bar-track">
                            <div class="bar-fill" style="--bar-width: <%= barPercent(value, maxValue(stats.getCityCounts())) %>%;"></div>
                        </div>
                        <strong><%= value %></strong>
                    </div>
                    <% } %>
                    <% } else { %>
                    <div class="empty-insight">No city data available yet.</div>
                    <% } %>
                </div>
            </div>
        </section>
    </section>
</main>
<script>
	    const avatarInput = document.getElementById('avatar');
	    const selectedFile = document.getElementById('selectedFile');
	    const toggleBioEditor = document.getElementById('toggleBioEditor');
	    const bioEditor = document.getElementById('bioEditor');

    if (avatarInput && selectedFile) {
        avatarInput.addEventListener('change', () => {
            selectedFile.textContent = avatarInput.files.length > 0
                ? `Selected file: ${avatarInput.files[0].name}`
                : '';
	        });
	    }

	    if (toggleBioEditor && bioEditor) {
	        toggleBioEditor.addEventListener('click', () => {
	            bioEditor.classList.toggle('open');
	            if (bioEditor.classList.contains('open')) {
	                document.getElementById('bio').focus();
	            }
	        });
	    }
	</script>
</body>
</html>
