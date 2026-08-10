<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="it.web.safeflow.model.UserProfileSummary" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
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
    UserProfileSummary profile = (UserProfileSummary) request.getAttribute("publicProfile");
    String encodedCf = profile == null ? "" : URLEncoder.encode(profile.getCodiceFiscale(), StandardCharsets.UTF_8);
    String displayName = profile == null ? "Unknown user" : StringEscapeUtils.escapeHtml4(profile.getDisplayName());
    String role = profile == null ? "" : StringEscapeUtils.escapeHtml4(profile.getRole());
    String bio = profile == null ? "" : StringEscapeUtils.escapeHtml4(profile.getBio());
    String initials = profile == null ? "SF" : StringEscapeUtils.escapeHtml4(profile.getInitials());
    int reportCount = profile == null ? 0 : profile.getStats().getReportCount();
    int cityCount = profile == null ? 0 : profile.getStats().getCityCount();
    int approvalCount = profile == null ? 0 : profile.getStats().getApprovalCount();
    int helpfulScore = profile == null ? 0 : profile.getStats().getHelpfulScore();
    int approvalRate = profile == null ? 0 : profile.getStats().getApprovalRatePercent();
    String rankLabel = profile != null && profile.getStats().getCommunityRank() > 0 ? "#" + profile.getStats().getCommunityRank() : "Unranked";
    String trustLevel = profile == null ? "New Reporter" : StringEscapeUtils.escapeHtml4(profile.getStats().getTrustLevel());
    Map<String, Integer> categoryCounts = profile == null ? Map.of() : profile.getStats().getCategoryCounts();
    Map<String, Integer> monthlyCounts = profile == null ? Map.of() : profile.getStats().getMonthlyReportCounts();
    Map<String, Integer> stationCounts = profile == null ? Map.of() : profile.getStats().getStationCounts();
    int categoryMax = maxValue(categoryCounts);
    int monthlyMax = maxValue(monthlyCounts);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - User Profile</title>
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        body {
            margin: 0;
            min-height: 100vh;
            background: #f3f6f4;
            color: #14241d;
            font-family: "Inter", "Segoe UI", Arial, sans-serif;
        }

        .public-profile-shell {
            width: min(760px, calc(100% - 32px));
            margin: 78px auto 28px;
            background: #ffffff;
            border: 1px solid #d8e4de;
            border-radius: 16px;
            box-shadow: 0 18px 42px rgba(20, 36, 29, 0.08);
            overflow: hidden;
        }

        .public-profile-header {
            display: flex;
            gap: 18px;
            align-items: center;
            padding: 26px;
            border-bottom: 1px solid #d8e4de;
            background: linear-gradient(180deg, #ffffff, #fbfdfc);
        }

        .public-profile-avatar {
            width: 96px;
            height: 96px;
            flex: 0 0 96px;
            border-radius: 50%;
            overflow: hidden;
            display: grid;
            place-items: center;
            color: #ffffff;
            background: #0e7c66;
            border: 4px solid #e5f3ee;
            font-size: 1.35rem;
            font-weight: 850;
        }

        .public-profile-avatar img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .public-profile-title h1 {
            margin: 0 0 8px;
            font-size: 2rem;
            line-height: 1.08;
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

        .public-profile-body {
            padding: 26px;
        }

        .public-profile-body h2 {
            margin: 0 0 10px;
            font-size: 1rem;
            text-transform: uppercase;
            color: #607267;
        }

        .public-profile-body p {
            margin: 0;
            color: #31443a;
            line-height: 1.65;
        }

        .profile-stats {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 12px;
            padding: 20px 26px;
            border-bottom: 1px solid #d8e4de;
            background: #f7faf8;
        }

        .profile-stat {
            padding: 14px;
            border-radius: 12px;
            background: #ffffff;
            border: 1px solid #d8e4de;
        }

        .profile-stat-label {
            display: block;
            color: #607267;
            font-size: 0.74rem;
            font-weight: 850;
            text-transform: uppercase;
        }

        .profile-stat-value {
            display: block;
            margin-top: 4px;
            color: #14241d;
            font-size: 1.35rem;
            font-weight: 900;
        }

        .public-profile-actions {
            padding: 0 26px 26px;
        }

        .public-profile-actions a {
            display: inline-flex;
            text-decoration: none;
            color: #31443a;
            background: #f7faf8;
            border: 1px solid #d8e4de;
            border-radius: 8px;
            padding: 11px 15px;
            font-weight: 800;
        }

        .rank-strip {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 12px;
            padding: 0 26px 20px;
            background: #f7faf8;
            border-bottom: 1px solid #d8e4de;
        }

        .rank-metric {
            padding: 14px;
            border-radius: 12px;
            background: #ffffff;
            border: 1px solid #d8e4de;
        }

        .rank-metric strong {
            display: block;
            color: #14241d;
            font-size: 1.25rem;
            line-height: 1.1;
        }

        .rank-metric span {
            display: block;
            margin-top: 5px;
            color: #607267;
            font-size: 0.74rem;
            font-weight: 850;
            text-transform: uppercase;
        }

        .public-insights {
            padding: 26px;
            border-top: 1px solid #d8e4de;
        }

        .public-insights h2 {
            margin: 0 0 16px;
            font-size: 1rem;
            color: #405147;
            text-transform: uppercase;
            letter-spacing: 0.04em;
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

        .insight-panel h3 {
            margin: 0 0 14px;
            color: #405147;
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.04em;
        }

        .bar-row {
            display: grid;
            grid-template-columns: minmax(88px, 0.9fr) minmax(0, 1.4fr) 32px;
            align-items: center;
            gap: 10px;
            margin-top: 10px;
            color: #405147;
            font-size: 0.84rem;
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
            border-radius: inherit;
            background: linear-gradient(90deg, #0e7c66, #6ee7a8);
        }

        .month-chart {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(46px, 1fr));
            gap: 10px;
            align-items: end;
            min-height: 180px;
        }

        .month-bar {
            display: grid;
            grid-template-rows: 1fr auto auto;
            gap: 8px;
            height: 166px;
            text-align: center;
            color: #607267;
            font-size: 0.7rem;
            font-weight: 800;
        }

        .month-bar-fill {
            align-self: end;
            justify-self: center;
            width: 22px;
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

        @media (max-width: 640px) {
            .public-profile-header {
                align-items: flex-start;
                flex-direction: column;
            }

            .profile-stats {
                grid-template-columns: 1fr;
            }

            .rank-strip,
            .insight-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<%@ include file="/header.jspf" %>
<main class="public-profile-shell">
    <section class="public-profile-header">
        <div class="public-profile-avatar">
            <% if (profile != null && profile.isAvatarPresent()) { %>
            <img src="publicProfileAvatar?cf=<%= encodedCf %>&amp;t=<%= System.currentTimeMillis() %>" alt="">
            <% } else { %>
            <span><%= initials %></span>
            <% } %>
        </div>
        <div class="public-profile-title">
            <h1><%= displayName %></h1>
            <span class="role-pill"><%= role %></span>
        </div>
	    </section>
		    <section class="profile-stats" aria-label="Profile statistics">
		        <div class="profile-stat">
		            <span class="profile-stat-label">Reports made</span>
		            <span class="profile-stat-value"><%= reportCount %></span>
	        </div>
	        <div class="profile-stat">
	            <span class="profile-stat-label">Cities reported</span>
	            <span class="profile-stat-value"><%= cityCount %></span>
	        </div>
	        <div class="profile-stat">
	            <span class="profile-stat-label">Approvals received</span>
	            <span class="profile-stat-value"><%= approvalCount %></span>
		        </div>
		    </section>
            <section class="rank-strip" aria-label="Community rank">
                <div class="rank-metric">
                    <strong><%= rankLabel %></strong>
                    <span>Community rank</span>
                </div>
                <div class="rank-metric">
                    <strong><%= helpfulScore %></strong>
                    <span>Helpful score</span>
                </div>
                <div class="rank-metric">
                    <strong><%= approvalRate %>%</strong>
                    <span>Approval rate</span>
                </div>
            </section>
		    <section class="public-profile-body">
		        <h2>Bio</h2>
		        <p><%= bio.isBlank() ? "No bio available." : bio %></p>
	    </section>
        <section class="public-insights" aria-label="Public profile charts">
            <h2><%= trustLevel %></h2>
            <div class="insight-grid">
                <div class="insight-panel">
                    <h3>Report categories</h3>
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
                    <h3>Top stations</h3>
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
                    <h3>Reports over time</h3>
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
                    <h3>Cities covered</h3>
                    <% if (profile != null && hasValues(profile.getStats().getCityCounts())) { %>
                    <% for (Map.Entry<String, Integer> entry : profile.getStats().getCityCounts().entrySet()) {
                        int value = entry.getValue() == null ? 0 : entry.getValue();
                    %>
                    <div class="bar-row">
                        <span><%= StringEscapeUtils.escapeHtml4(entry.getKey()) %></span>
                        <div class="bar-track">
                            <div class="bar-fill" style="--bar-width: <%= barPercent(value, maxValue(profile.getStats().getCityCounts())) %>%;"></div>
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
	    <div class="public-profile-actions">
	        <a href="viewNotifications">Back to notifications</a>
	    </div>
</main>
</body>
</html>
