<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Report Guide</title>
    <style>
        :root {
            --bg: #f4f7fb;
            --surface: rgba(255, 255, 255, 0.97);
            --line: rgba(15, 23, 42, 0.08);
            --text: #0f172a;
            --muted: #475569;
            --accent: #2563eb;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            padding: 24px;
            background: linear-gradient(180deg, #f8fbff 0%, #f2f6fb 100%);
            color: var(--text);
            font-family: "Segoe UI", sans-serif;
        }

        .shell {
            width: min(980px, 100%);
            margin: 0 auto;
            background: var(--surface);
            border: 1px solid var(--line);
            border-radius: 28px;
            padding: 34px;
            box-shadow: 0 24px 56px rgba(15, 23, 42, 0.08);
        }

        .eyebrow {
            display: inline-flex;
            padding: 8px 12px;
            border-radius: 999px;
            border: 1px solid #cfe0ff;
            background: #eaf1ff;
            color: var(--accent);
            font-size: 0.76rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.08em;
        }

        h1 {
            margin: 18px 0 10px;
            font-size: clamp(2rem, 4vw, 3.2rem);
            letter-spacing: -0.04em;
        }

        .intro {
            margin: 0 0 28px;
            color: var(--muted);
            line-height: 1.75;
            max-width: 760px;
        }

        .section {
            margin-top: 26px;
            padding-top: 26px;
            border-top: 1px solid #e5ebf3;
        }

        .section h2 {
            margin: 0 0 10px;
            font-size: 1.2rem;
        }

        .section p,
        .section li {
            color: var(--muted);
            line-height: 1.7;
        }

        .badge-list {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 14px;
            margin-top: 16px;
        }

        .badge-card {
            border: 1px solid #e2e8f0;
            background: #f8fafc;
            border-radius: 18px;
            padding: 16px;
        }

        .badge {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 7px 12px;
            border-radius: 999px;
            font-size: 0.75rem;
            font-weight: 700;
            letter-spacing: 0.05em;
            text-transform: uppercase;
            margin-bottom: 10px;
        }

        .badge.pickpocket {
            color: #111827;
            background: rgba(239, 68, 68, 0.22);
            border: 1px solid rgba(239, 68, 68, 0.36);
        }

        .badge.fight {
            color: #1d4ed8;
            background: rgba(250, 204, 21, 0.22);
            border: 1px solid rgba(250, 204, 21, 0.42);
        }

        .badge.crowd {
            color: #14532d;
            background: rgba(34, 197, 94, 0.2);
            border: 1px solid rgba(34, 197, 94, 0.38);
        }

        .badge.general {
            color: #111111;
            background: rgba(255, 255, 255, 0.96);
            border: 1px solid rgba(148, 163, 184, 0.9);
        }

        .actions {
            margin-top: 30px;
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .actions a {
            text-decoration: none;
            padding: 12px 18px;
            border-radius: 999px;
            font-weight: 700;
        }

        .primary {
            color: #ffffff;
            background: linear-gradient(90deg, #2563eb, #1d4ed8);
            box-shadow: 0 14px 28px rgba(37, 99, 235, 0.16);
        }

        .secondary {
            color: var(--text);
            background: #ffffff;
            border: 1px solid #d7e0ea;
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<div class="shell">
    <span class="eyebrow">Reporting Guide</span>
    <h1>How to send a traveler report</h1>
    <p class="intro">
        Use this page to understand how RouteX reports work. Every report must include a city, a clear written message, and at least one alert badge. Your report is reviewed by the RouteX admin team before it becomes visible in the shared report feed.
    </p>

    <section class="section">
        <h2>How to submit a report</h2>
        <p>
            Choose the city first, then write a short but precise message describing what is happening. If the report is related to a station or platform area, add the station name through the station field. Once submitted, your report enters the admin review queue.
        </p>
        <p>
            Keep the message direct. The most useful reports explain what is happening, where it is happening, and why other travelers or staff should pay attention.
        </p>
    </section>

    <section class="section">
        <h2>What the badges mean</h2>
        <div class="badge-list">
            <div class="badge-card">
                <span class="badge pickpocket">Anti pickpockets</span>
                <p>Use this when the report concerns theft risk, suspicious stealing behavior, or known pickpocket activity.</p>
            </div>
            <div class="badge-card">
                <span class="badge fight">Fight alert</span>
                <p>Use this when the situation involves aggression, conflict, or an active altercation between people.</p>
            </div>
            <div class="badge-card">
                <span class="badge crowd">Crowd alert</span>
                <p>Use this when there are overcrowding problems, heavy pressure on platforms, or too many people in a critical area.</p>
            </div>
            <div class="badge-card">
                <span class="badge general">General alert</span>
                <p>Use this for broader information that does not belong to the more specific alert categories above.</p>
            </div>
        </div>
    </section>

    <section class="section">
        <h2>Important reporting rules</h2>
        <p>
            At least one badge must be selected, otherwise the report cannot be submitted. If you choose <strong>General alert</strong>, it replaces the three specialized categories because it represents broader information rather than a specific incident type.
        </p>
        <p>
            Reports with the wrong badge can still be rejected by admins, so choose the badge that best matches the real situation. If the report is urgent, mention the exact station in the report form.
        </p>
    </section>

    <div class="actions">
        <a href="travelerReport" class="primary">Open the Report Form</a>
        <a href="travelerHome" class="secondary">Back to Home</a>
    </div>
</div>
</body>
</html>
