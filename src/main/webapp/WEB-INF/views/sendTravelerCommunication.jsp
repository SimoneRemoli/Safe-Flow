<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Safe Flow - Traveler Report</title>
    <link rel="stylesheet" href="css/minimal-ui.css">
    <style>
        :root {
            --report-bg: #ffffff;
            --report-surface: #ffffff;
            --report-soft: #f7faf8;
            --report-border: #d8e4de;
            --report-border-strong: #c7d7cf;
            --report-text: #14241d;
            --report-muted: #607267;
            --report-muted-strong: #405147;
            --report-primary: #0e7c66;
            --report-primary-strong: #075f4e;
            --report-primary-soft: #e8f7ef;
            --report-danger: #b42318;
            --report-danger-soft: #fff0ee;
            --report-warning: #8a4b08;
            --report-warning-soft: #fff5df;
            --report-blue: #2563eb;
            --report-blue-soft: #e8efff;
        }

        * { box-sizing: border-box; }

        body.report-page {
            margin: 0 !important;
            min-height: 100vh !important;
            color: var(--report-text) !important;
            font-family: "Segoe UI", "Helvetica Neue", Arial, sans-serif !important;
            background: var(--report-bg) !important;
            padding: 28px 18px !important;
        }

        .report-shell {
            width: min(1120px, 100%);
            margin: 0 auto;
            padding: 8px 0 42px;
        }

        .report-panel {
            border: 0;
            border-radius: 0;
            background: var(--report-surface);
            box-shadow: none;
            overflow: visible;
        }

        .report-header {
            display: grid;
            grid-template-columns: 1fr auto;
            gap: 26px;
            align-items: end;
            padding: 24px 0 28px;
            border-bottom: 1px solid var(--report-border);
            background: #ffffff;
        }

        .report-kicker {
            display: inline-flex;
            margin-bottom: 12px;
            color: var(--report-primary) !important;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            font-size: 0.74rem;
            font-weight: 850;
        }

        .report-header h1 {
            margin: 0;
            color: var(--report-text) !important;
            font-size: clamp(2rem, 3.5vw, 3rem) !important;
            line-height: 1.04 !important;
            letter-spacing: 0 !important;
        }

        .report-subtitle {
            margin: 12px 0 0;
            max-width: 680px;
            color: var(--report-muted) !important;
            line-height: 1.7;
            font-size: 1rem;
        }

        .report-status {
            min-width: 180px;
            padding: 0 0 0 18px;
            border-left: 3px solid var(--report-primary);
            color: var(--report-primary-strong);
            font-weight: 850;
            text-align: left;
        }

        .report-status span {
            display: block;
            margin-top: 4px;
            color: var(--report-muted-strong) !important;
            font-size: 0.82rem;
            font-weight: 700;
        }

        .report-form {
            display: grid;
            grid-template-columns: minmax(0, 1fr) 300px;
            gap: 38px;
            padding: 30px 0 0;
            align-items: start;
        }

        .report-main {
            display: grid;
            gap: 26px;
        }

        .report-section {
            display: grid;
            gap: 16px;
            padding-bottom: 26px;
            border-bottom: 1px solid var(--report-border);
        }

        .report-section-title {
            margin: 0;
            color: var(--report-text) !important;
            font-size: 1rem !important;
            line-height: 1.2 !important;
            letter-spacing: 0 !important;
        }

        .report-section-copy {
            margin: -8px 0 0;
            max-width: 660px;
            color: var(--report-muted) !important;
            font-size: 0.93rem;
            line-height: 1.6;
        }

        .report-protocol {
            position: sticky;
            top: 24px;
            padding: 0 0 0 22px;
            border-left: 1px solid var(--report-border);
        }

        .report-protocol h2 {
            margin: 0 0 14px;
            color: var(--report-text) !important;
            font-size: 1rem !important;
            line-height: 1.2 !important;
            letter-spacing: 0 !important;
        }

        .protocol-list {
            display: grid;
            gap: 14px;
        }

        .protocol-item {
            display: grid;
            grid-template-columns: auto 1fr;
            gap: 11px;
            align-items: start;
        }

        .protocol-index {
            width: 28px;
            height: 28px;
            display: grid;
            place-items: center;
            border-radius: 8px;
            border: 1px solid #bfe8cf;
            background: var(--report-primary-soft);
            color: var(--report-primary-strong);
            font-size: 0.75rem;
            font-weight: 900;
        }

        .protocol-item strong {
            display: block;
            color: var(--report-text) !important;
            font-size: 0.92rem;
            line-height: 1.25;
        }

        .protocol-item span {
            display: block;
            margin-top: 3px;
            color: var(--report-muted) !important;
            font-size: 0.86rem;
            line-height: 1.48;
        }

        .protocol-note {
            margin: 18px 0 0;
            padding-top: 16px;
            border-top: 1px solid var(--report-border);
            color: var(--report-muted) !important;
            font-size: 0.88rem;
            line-height: 1.55;
        }

        .field {
            margin: 0;
        }

        .field label,
        .textarea-label {
            display: block;
            margin-bottom: 8px;
            color: var(--report-muted-strong) !important;
            font-size: 0.92rem;
            font-weight: 800;
        }

        .city-picker {
            position: relative;
        }

        .native-city-select {
            position: absolute;
            width: 1px !important;
            height: 1px !important;
            opacity: 0;
            pointer-events: none;
        }

        .city-picker__button {
            width: 100% !important;
            min-height: 48px !important;
            display: flex !important;
            align-items: center;
            justify-content: space-between;
            gap: 14px;
            padding: 12px 14px !important;
            border: 1px solid var(--report-border) !important;
            border-radius: 12px !important;
            background: #ffffff !important;
            color: var(--report-text) !important;
            font: inherit;
            text-align: left;
            box-shadow: none !important;
            cursor: pointer;
            transition: border-color 0.18s ease, box-shadow 0.18s ease;
        }

        .city-picker__button::after {
            content: "";
            width: 9px;
            height: 9px;
            border-right: 2px solid var(--report-primary);
            border-bottom: 2px solid var(--report-primary);
            transform: translateY(-2px) rotate(45deg);
            transition: transform 0.18s ease;
        }

        .city-picker.open .city-picker__button::after {
            transform: translateY(2px) rotate(225deg);
        }

        .city-picker__button:focus-visible,
        .city-picker__button.invalid {
            outline: none !important;
            border-color: rgba(14, 124, 102, 0.58) !important;
            box-shadow: 0 0 0 4px rgba(14, 124, 102, 0.10) !important;
        }

        .city-picker__label {
            color: var(--report-muted) !important;
        }

        .city-picker.has-value .city-picker__label {
            color: var(--report-text) !important;
        }

        .city-picker__menu {
            position: absolute;
            z-index: 40;
            top: calc(100% + 8px);
            left: 0;
            right: 0;
            display: none;
            padding: 8px;
            border: 1px solid var(--report-border-strong);
            border-radius: 16px;
            background: #ffffff;
            box-shadow: 0 18px 42px rgba(19, 35, 28, 0.16);
        }

        .city-picker.open .city-picker__menu {
            display: grid;
            gap: 4px;
        }

        .city-picker__option {
            width: 100%;
            min-height: 42px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 9px 10px;
            border: 0;
            border-radius: 10px;
            background: transparent;
            color: var(--report-text) !important;
            font: inherit;
            text-align: left;
            cursor: pointer;
        }

        .city-picker__option:hover,
        .city-picker__option.active {
            background: var(--report-primary-soft);
            color: var(--report-primary-strong) !important;
        }

        select,
        input[type="text"],
        textarea {
            width: 100% !important;
            border: 1px solid var(--report-border) !important;
            border-radius: 12px !important;
            background: #ffffff !important;
            color: var(--report-text) !important;
            font: inherit;
            outline: none !important;
            box-shadow: none !important;
            transition: border-color 0.18s ease, box-shadow 0.18s ease;
        }

        select {
            min-height: 48px;
            padding: 12px 44px 12px 14px !important;
            appearance: none;
            cursor: pointer;
        }

        select option {
            color: var(--report-text);
            background: #ffffff;
            font-size: 1rem;
        }

        textarea {
            min-height: 190px;
            padding: 15px !important;
            resize: vertical;
            line-height: 1.55;
        }

        input[type="text"] {
            min-height: 48px;
            padding: 12px 14px !important;
        }

        select:focus,
        input[type="text"]:focus,
        textarea:focus {
            border-color: rgba(14, 124, 102, 0.58) !important;
            box-shadow: 0 0 0 4px rgba(14, 124, 102, 0.10) !important;
        }

        .station-field {
            position: relative;
        }

        .station-suggestions {
            position: absolute;
            z-index: 30;
            top: calc(100% + 8px);
            left: 0;
            right: 0;
            max-height: 290px;
            overflow-y: auto;
            display: none;
            padding: 8px;
            border-radius: 16px;
            border: 1px solid var(--report-border-strong);
            background: #ffffff;
            box-shadow: 0 18px 42px rgba(19, 35, 28, 0.16);
        }

        .station-suggestions.open {
            display: block;
        }

        .station-suggestion {
            width: 100%;
            min-height: 44px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            padding: 9px 10px;
            border: 0;
            border-radius: 10px;
            color: var(--report-text) !important;
            background: transparent;
            font: inherit;
            text-align: left;
            cursor: pointer;
        }

        .station-suggestion:hover,
        .station-suggestion.active {
            background: var(--report-primary-soft);
        }

        .station-suggestion-name {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            font-weight: 750;
        }

        .station-line-icons {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            flex: 0 0 auto;
        }

        .station-line-icons img {
            display: block;
            width: 40px;
            height: 19px;
            object-fit: contain;
        }

        .station-empty {
            padding: 12px 10px;
            color: var(--report-muted) !important;
            font-size: 0.92rem;
        }

        .image-upload-panel {
            padding: 16px 0 0;
            border-top: 1px dashed #bfe8cf;
            border-radius: 0;
            border-right: 0;
            border-bottom: 0;
            border-left: 0;
            background: transparent;
        }

        .image-upload-label {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-height: 42px;
            padding: 10px 14px;
            border-radius: 999px;
            color: var(--report-primary-strong) !important;
            background: var(--report-primary-soft);
            border: 1px solid #bfe8cf;
            font-weight: 800;
            cursor: pointer;
            transition: transform 0.18s ease, border-color 0.18s ease, background 0.18s ease;
        }

        .image-upload-label:hover {
            transform: translateY(-1px);
            background: #ffffff;
            border-color: rgba(14, 124, 102, 0.36);
        }

        .image-upload-input {
            position: absolute;
            width: 1px;
            height: 1px;
            overflow: hidden;
            clip: rect(0, 0, 0, 0);
        }

        .image-upload-help,
        .selected-images {
            margin-top: 10px;
            color: var(--report-muted) !important;
            font-size: 0.9rem;
            line-height: 1.45;
        }

        .toggle-row {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .toggle-input {
            position: absolute;
            opacity: 0;
            pointer-events: none;
        }

        .toggle-button {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-height: 42px;
            padding: 10px 14px;
            border-radius: 999px;
            border: 1px solid var(--report-border);
            background: var(--report-soft);
            color: var(--report-muted-strong) !important;
            font-weight: 800;
            font-size: 0.92rem;
            cursor: pointer;
            transition: transform 0.2s ease, background 0.2s ease, border-color 0.2s ease, color 0.2s ease;
        }

        .toggle-button:hover {
            transform: translateY(-1px);
            border-color: rgba(14, 124, 102, 0.32);
            background: #ffffff;
        }

        .toggle-input:focus-visible + .toggle-button {
            border-color: rgba(14, 124, 102, 0.58);
            box-shadow: 0 0 0 4px rgba(14, 124, 102, 0.10);
        }

        .toggle-input:checked + .toggle-button {
            background: var(--report-danger-soft);
            border-color: #fac7c2;
            color: #8f1f17 !important;
        }

        .toggle-input.fight-toggle:checked + .toggle-button {
            background: var(--report-warning-soft);
            border-color: #f4d58a;
            color: var(--report-warning) !important;
        }

        .toggle-input.crowd-toggle:checked + .toggle-button {
            background: var(--report-primary-soft);
            border-color: #bfe8cf;
            color: var(--report-primary-strong) !important;
        }

        .toggle-input.general-toggle:checked + .toggle-button {
            background: var(--report-blue-soft);
            border-color: #bfd0ff;
            color: #1d4ed8 !important;
            transform: none;
        }

        .toggle-button.general-button {
            border-color: var(--report-border-strong);
        }

        .pickpocket-panel {
            display: none;
            padding: 16px;
            border-radius: 12px;
            border: 1px solid var(--report-border);
            background: var(--report-soft);
        }

        .pickpocket-panel.active {
            display: block;
        }

        .report-actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            padding-top: 0;
        }

        .report-submit,
        .report-link {
            min-height: 46px !important;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 12px 18px !important;
            border-radius: 999px !important;
            font-size: 0.98rem !important;
            font-weight: 850 !important;
            line-height: 1.2;
            text-decoration: none;
            cursor: pointer;
            transition: transform 0.18s ease, background 0.18s ease, border-color 0.18s ease, box-shadow 0.18s ease;
        }

        .report-submit {
            border: 1px solid var(--report-primary) !important;
            color: #ffffff !important;
            background: linear-gradient(135deg, var(--report-primary), #13a085) !important;
            box-shadow: none !important;
        }

        .report-submit:hover {
            transform: translateY(-1px);
            background: linear-gradient(135deg, var(--report-primary-strong), var(--report-primary)) !important;
        }

        .report-link {
            border: 1px solid var(--report-border) !important;
            color: var(--report-muted-strong) !important;
            background: var(--report-soft) !important;
        }

        .report-link:hover {
            transform: translateY(-1px);
            border-color: #c8e2d8 !important;
            color: var(--report-text) !important;
            background: #ffffff !important;
        }

        @media (max-width: 760px) {
            body.report-page {
                padding: 16px !important;
            }

            .report-header {
                grid-template-columns: 1fr;
                padding: 10px 0 24px;
            }

            .report-status {
                width: 100%;
                text-align: left;
                padding-left: 14px;
            }

            .report-form {
                grid-template-columns: 1fr;
                gap: 28px;
                padding: 24px 0 0;
            }

            .report-protocol {
                position: static;
                padding: 24px 0 0;
                border-left: 0;
                border-top: 1px solid var(--report-border);
            }

            .report-actions {
                flex-direction: column;
            }

            .report-submit,
            .report-link {
                width: 100%;
            }
        }
    </style>
</head>
<body class="report-page">
<%@ include file="/header.jspf" %>
<main class="report-shell">
    <section class="report-panel" aria-labelledby="reportTitle">
        <div class="report-header">
            <div>
                <span class="report-kicker">Traveler report</span>
                <h1 id="reportTitle">Send a traveler report</h1>
                <p class="report-subtitle">
                    Submit a clear safety report for review by the Safe Flow administration team.
                </p>
            </div>
            <div class="report-status">
                Review queue
                <span>Admin moderated</span>
            </div>
        </div>

        <form class="report-form" action="submitTravelerCommunication" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
            <div class="report-main">
                <section class="report-section" aria-labelledby="locationTitle">
                    <h2 class="report-section-title" id="locationTitle">Location and category</h2>
                    <p class="report-section-copy">Choose the city and classify the situation so the review team can route the report correctly.</p>
                    <div class="field">
                        <label for="city">City</label>
                        <div class="city-picker" data-city-picker>
                            <select class="native-city-select" id="city" name="city" aria-hidden="true" tabindex="-1">
                                <option value="">Select a city</option>
                                <option value="Rome">Rome</option>
                                <option value="Naples">Naples</option>
                                <option value="Athens">Athens</option>
                                <option value="Budapest">Budapest</option>
                            </select>
                            <button type="button"
                                    class="city-picker__button"
                                    data-city-button
                                    aria-haspopup="listbox"
                                    aria-expanded="false">
                                <span class="city-picker__label" data-city-label>Select a city</span>
                            </button>
                            <div class="city-picker__menu" data-city-menu role="listbox" aria-label="Available cities"></div>
                        </div>
                    </div>

                    <div class="toggle-row" aria-label="Report categories">
                        <input type="checkbox" class="toggle-input" id="pickpocketAlert" name="pickpocketAlert" value="true">
                        <label class="toggle-button" for="pickpocketAlert">Anti pickpockets</label>
                        <input type="checkbox" class="toggle-input fight-toggle" id="fightAlert" name="fightAlert" value="true">
                        <label class="toggle-button" for="fightAlert">Fight alert</label>
                        <input type="checkbox" class="toggle-input crowd-toggle" id="crowdAlert" name="crowdAlert" value="true">
                        <label class="toggle-button" for="crowdAlert">Crowd alert</label>
                        <input type="checkbox" class="toggle-input general-toggle" id="generalAlert" name="generalAlert" value="true">
                        <label class="toggle-button general-button" for="generalAlert">General alert</label>
                    </div>

                    <div class="pickpocket-panel" id="pickpocketPanel">
                        <div class="field station-field">
                            <label for="stationName">Station</label>
                            <input
                                    type="text"
                                    id="stationName"
                                    name="stationName"
                                    placeholder="Confirm the station involved in the alert."
                                    autocomplete="off"
                                    aria-autocomplete="list"
                                    aria-expanded="false"
                                    aria-controls="stationSuggestions">
                            <div class="station-suggestions" id="stationSuggestions" role="listbox"></div>
                        </div>
                    </div>
                </section>

                <section class="report-section" aria-labelledby="detailsTitle">
                    <h2 class="report-section-title" id="detailsTitle">Report details</h2>
                    <p class="report-section-copy">Write only factual information that can help administrators understand and verify the situation.</p>
                    <div>
                        <label class="textarea-label" for="message">Message</label>
                        <textarea id="message" name="message" maxlength="250" placeholder="Describe the issue or information you want to report..."></textarea>
                    </div>

                    <div class="image-upload-panel">
                        <label class="image-upload-label" for="reportImages">Attach images</label>
                        <input
                                class="image-upload-input"
                                id="reportImages"
                                name="reportImages"
                                type="file"
                                accept="image/png,image/jpeg,image/webp,image/gif"
                                multiple>
                        <div class="image-upload-help">Up to 5 images. JPG, PNG, WEBP, or GIF only. Max 5 MB each.</div>
                        <div class="selected-images" id="selectedImages" aria-live="polite"></div>
                    </div>
                </section>

                <div class="report-actions">
                    <button type="submit" class="report-submit">Submit report</button>
                    <a href="reportGuide" class="report-link">How reporting works</a>
                    <a href="travelerHome" class="report-link">Back to home</a>
                </div>
            </div>

            <aside class="report-protocol" aria-labelledby="protocolTitle">
                <h2 id="protocolTitle">Review protocol</h2>
                <div class="protocol-list">
                    <div class="protocol-item">
                        <div class="protocol-index">01</div>
                        <div>
                            <strong>Submit a clear report</strong>
                            <span>City, category, and message are used to route the report.</span>
                        </div>
                    </div>
                    <div class="protocol-item">
                        <div class="protocol-index">02</div>
                        <div>
                            <strong>Admin validation</strong>
                            <span>The Safe Flow team reviews the content before publication.</span>
                        </div>
                    </div>
                    <div class="protocol-item">
                        <div class="protocol-index">03</div>
                        <div>
                            <strong>Public alert feed</strong>
                            <span>Approved reports can become visible to other commuters.</span>
                        </div>
                    </div>
                </div>
                <p class="protocol-note">Reports should be concise, verifiable, and respectful. Avoid personal data unless it is strictly necessary for safety.</p>
            </aside>
        </form>
    </section>
</main>
<script>
    (function () {
        const cityStations = {
            "Rome": ["Battistini", "Cornelia", "Baldo degli Ubaldi", "Valle Aurelia", "Cipro", "Ottaviano", "Lepanto", "Flaminio", "Spagna", "Barberini", "Repubblica", "Termini", "Vittorio Emanuele", "Manzoni", "San Giovanni", "Re di Roma", "Ponte Lungo", "Furio Camillo", "Colli Albani", "Arco di Travertino", "Porta Furba", "Numidio Quadrato", "Lucio Sestio", "Giulio Agricola", "Subaugusta", "Cinecitta", "Anagnina", "Pantano", "Graniti", "Finocchio", "Bolognetta", "Borghesiana", "Due Leoni - Fontana Candida", "Grotte Celoni", "Torre Gaia", "Torre Angela", "Torrenova", "Giardinetti", "Torre Maura", "Torre Spaccata", "Alessandrino", "Parco di Centocelle", "Mirti", "Gardenie", "Teano", "Malatesta", "Pigneto", "Lodi", "Laurentina", "EUR Fermi", "EUR Palasport", "EUR Magliana", "Marconi", "Basilica S. Paolo", "Garbatella", "Piramide", "Circo Massimo", "Colosseo", "Cavour", "Castro Pretorio", "Policlinico", "Bologna", "Tiburtina FS", "Quintiliani", "Monti Tiburtini", "Pietralata", "Santa Maria del Soccorso", "Ponte Mammolo", "Rebibbia", "Annibaliano", "Libia", "Conca D oro", "Jonio"],
            "Naples": ["Pozzuoli Solfatara", "Bagnoli-Agnano Terme", "Campi Flegrei", "Mostra", "P.Leopardi", "Augusto", "Lala", "Mergellina", "Arco Mirelli", "San Pasquale", "Chiaia", "P.Amedeo", "Montesanto", "Museo-Piazza Cavour", "Dante", "Toledo", "Municipio", "Universita", "Duomo", "Garibaldi", "Gianturco", "S.Giovanni-Barra", "Materdei", "Salvator Rosa", "Quattro Giornate", "Vanvitelli", "Medaglie D'Oro", "Montedonzelli", "Rione Alto", "Policlinico", "Colli Aminei", "Frullone", "Chiaiano", "Piscinola", "Mugnano", "Giugliano", "Aversa Ippodromo", "Aversa Centro", "Cavalleggeri Aosta"],
            "Athens": ["Airport", "Koropi", "Paiania-Kantza", "Pallini", "Doukissis Plakentias", "Halandri", "Aghia Paraskevi", "Nomismatokopio", "Holargos", "Ethniki Amyna", "Katehaki", "Panormou", "Ampelokipi", "Megaro Moussikis", "Evangelismos", "Syntagma", "Panipistimo", "Omonia", "Victoria", "Attiki", "Aghios Nikolaos", "Kato Patissia", "Aghios Eleftherios", "Ano Patissia", "Perissos", "Pefkakia", "Nea Ionia", "Iraklio", "Irini", "Neratziotissa", "Maroussi", "KAT", "Kifissia", "Akropoli", "Syngrou Fix", "Aghios Ioannis", "Dafni", "Aghios Dimitrios", "Illioupoli", "Alimos", "Argyroupoli", "Elliniko", "Monastiraki", "Thissio", "Petralona", "Tavros", "Kallithea", "Moschato", "Faliro", "Piraeus", "Dimotiko Theatro", "Maniatika", "Nikaia", "Korydallos", "Aghia Varvara", "Aghia Marina", "Egaleo", "Eleonas", "Kerameikos", "Metaxourghio", "Larissa Station", "Sepolia", "Aghios Antonios", "Peristeri", "Anthoupoli", "Neos Kosmos"],
            "Budapest": ["Ors vezer tere", "Pillango utca", "Puskas Ferenc Stadion", "Keleti Palyaudvar", "Blaha Lujza Ter", "Il. Janos Pal Papa Ter", "Rakoczi Ter", "Kalvin Ter", "Fovam Ter", "Szent Gellert Ter - Muegyetem", "Moricz Zsigmond Korter", "Ujbuda-kozport", "Bikas Park", "Kelenfood Vasutallomas", "Kobanya-Kispes", "Hatar Ut", "Pottyos Utca", "Ecseri Ut", "Nepliget", "Nagyvarad Ter", "Semmelweis Klinikak", "Corvin-negyed", "Ferenciek Tere", "Deak Ferenc Ter", "Vorosmarty Ter", "Bajcsy-Zsilinszky ut", "Opera", "Oktogon", "Vorosmarty Utca", "Kodaly Korond", "Bajza Utca", "Hosok Tere", "Szechenyi-furdo", "Mexikoi Ut", "Ujpest-Kozpont", "Ujpest-Varoskapu", "Gyongyosi Utca", "Forgach Utca", "Goncz Arpad Vkp", "Dozsa Gyorgy Ut", "Lehel Ter", "Nyugati Palyaudva", "Arany Janos Utca", "Kossuth Lajos Ter", "Battyhany Ter", "Szell Kalman Ter", "Deli Palyaudvar", "Astoria"]
        };

        const romeStationLines = {
            "Anagnina": "MA",
            "Cinecitta": "MA",
            "Subaugusta": "MA",
            "Giulio Agricola": "MA",
            "Lucio Sestio": "MA",
            "Numidio Quadrato": "MA",
            "Porta Furba": "MA",
            "Arco di Travertino": "MA",
            "Colli Albani": "MA",
            "Furio Camillo": "MA",
            "Ponte Lungo": "MA",
            "Re di Roma": "MA",
            "San Giovanni": "MA MC",
            "Manzoni": "MA",
            "Vittorio Emanuele": "MA",
            "Termini": "MA MB",
            "Repubblica": "MA",
            "Barberini": "MA",
            "Spagna": "MA",
            "Flaminio": "MA",
            "Lepanto": "MA",
            "Ottaviano": "MA",
            "Cipro": "MA",
            "Valle Aurelia": "MA",
            "Baldo degli Ubaldi": "MA",
            "Cornelia": "MA",
            "Battistini": "MA",
            "Laurentina": "MB",
            "EUR Fermi": "MB",
            "EUR Palasport": "MB",
            "EUR Magliana": "MB",
            "Marconi": "MB",
            "Basilica S. Paolo": "MB",
            "Garbatella": "MB",
            "Piramide": "MB",
            "Circo Massimo": "MB",
            "Colosseo": "MB MC",
            "Cavour": "MB",
            "Castro Pretorio": "MB",
            "Policlinico": "MB",
            "Bologna": "MB",
            "Tiburtina FS": "MB",
            "Quintiliani": "MB",
            "Monti Tiburtini": "MB",
            "Pietralata": "MB",
            "Santa Maria del Soccorso": "MB",
            "Ponte Mammolo": "MB",
            "Rebibbia": "MB",
            "Annibaliano": "MB",
            "Libia": "MB",
            "Conca D oro": "MB",
            "Jonio": "MB",
            "Pantano": "MC",
            "Graniti": "MC",
            "Finocchio": "MC",
            "Bolognetta": "MC",
            "Borghesiana": "MC",
            "Due Leoni - Fontana Candida": "MC",
            "Grotte Celoni": "MC",
            "Torre Gaia": "MC",
            "Torre Angela": "MC",
            "Torrenova": "MC",
            "Giardinetti": "MC",
            "Torre Maura": "MC",
            "Torre Spaccata": "MC",
            "Alessandrino": "MC",
            "Parco di Centocelle": "MC",
            "Mirti": "MC",
            "Gardenie": "MC",
            "Teano": "MC",
            "Malatesta": "MC",
            "Pigneto": "MC",
            "Lodi": "MC"
        };

        const stationLineAssets = {
            "MA": "images/metro/ma.svg",
            "MB": "images/metro/mb.svg",
            "MC": "images/metro/mc.svg"
        };

        const reportForm = document.querySelector(".report-form");
        const citySelect = document.getElementById("city");
        const cityPicker = document.querySelector("[data-city-picker]");
        const cityButton = document.querySelector("[data-city-button]");
        const cityLabel = document.querySelector("[data-city-label]");
        const cityMenu = document.querySelector("[data-city-menu]");
        const pickpocketToggle = document.getElementById("pickpocketAlert");
        const fightToggle = document.getElementById("fightAlert");
        const crowdToggle = document.getElementById("crowdAlert");
        const generalToggle = document.getElementById("generalAlert");
        const pickpocketPanel = document.getElementById("pickpocketPanel");
	        const stationInput = document.getElementById("stationName");
	        const stationSuggestions = document.getElementById("stationSuggestions");
	        const reportImages = document.getElementById("reportImages");
	        const selectedImages = document.getElementById("selectedImages");
	        let activeStationIndex = -1;

        function closeCityMenu() {
            cityPicker.classList.remove("open");
            cityButton.setAttribute("aria-expanded", "false");
        }

        function syncCityLabel() {
            const selectedOption = citySelect.options[citySelect.selectedIndex];
            const hasValue = Boolean(citySelect.value);
            cityLabel.textContent = hasValue && selectedOption ? selectedOption.textContent : "Select a city";
            cityPicker.classList.toggle("has-value", hasValue);
            cityButton.classList.remove("invalid");
            Array.from(cityMenu.querySelectorAll(".city-picker__option")).forEach((option) => {
                option.classList.toggle("active", option.dataset.cityValue === citySelect.value);
            });
        }

        function selectCity(value) {
            citySelect.value = value;
            citySelect.dispatchEvent(new Event("change"));
            syncCityLabel();
            closeCityMenu();
            cityButton.focus();
        }

        function renderCityMenu() {
            cityMenu.innerHTML = "";
            Array.from(citySelect.options)
                    .filter((option) => option.value)
                    .forEach((option) => {
                        const button = document.createElement("button");
                        button.type = "button";
                        button.className = "city-picker__option";
                        button.dataset.cityValue = option.value;
                        button.setAttribute("role", "option");
                        button.textContent = option.textContent;
                        button.addEventListener("click", () => selectCity(option.value));
                        cityMenu.appendChild(button);
                    });
            syncCityLabel();
        }

        function stationLines(station) {
            if (citySelect.value !== "Rome") {
                return [];
            }
            return (romeStationLines[station] || "").split(" ").filter(Boolean);
        }

        function visibleStations() {
            const stations = cityStations[citySelect.value] || [];
            const query = stationInput.value.trim().toLowerCase();
            if (!query) {
                return stations;
            }
            return stations.filter((station) => {
                const lineText = stationLines(station).join(" ").toLowerCase();
                return station.toLowerCase().includes(query) || lineText.includes(query);
            });
        }

        function closeStationSuggestions() {
            stationSuggestions.classList.remove("open");
            stationInput.setAttribute("aria-expanded", "false");
            activeStationIndex = -1;
        }

        function selectStation(station) {
            stationInput.value = station;
            closeStationSuggestions();
        }

        function renderStations() {
            const stations = visibleStations();
            stationSuggestions.innerHTML = "";

            if (!stations.length) {
                const empty = document.createElement("div");
                empty.className = "station-empty";
                empty.textContent = "No station found.";
                stationSuggestions.appendChild(empty);
            }

            stations.slice(0, 90).forEach((station, index) => {
                const button = document.createElement("button");
                button.type = "button";
                button.className = "station-suggestion";
                button.setAttribute("role", "option");
                button.dataset.station = station;
                button.addEventListener("mousedown", (event) => {
                    event.preventDefault();
                    selectStation(station);
                });

                const name = document.createElement("span");
                name.className = "station-suggestion-name";
                name.textContent = station;
                button.appendChild(name);

                const lines = stationLines(station);
                if (lines.length) {
                    const icons = document.createElement("span");
                    icons.className = "station-line-icons";
                    lines.forEach((line) => {
                        const image = document.createElement("img");
                        image.src = stationLineAssets[line];
                        image.alt = line;
                        image.title = line;
                        icons.appendChild(image);
                    });
                    button.appendChild(icons);
                }

                if (index === activeStationIndex) {
                    button.classList.add("active");
                }
                stationSuggestions.appendChild(button);
            });

            stationSuggestions.classList.add("open");
            stationInput.setAttribute("aria-expanded", "true");
        }

        function syncPickpocketState() {
            const active = pickpocketToggle.checked || fightToggle.checked || crowdToggle.checked;
            pickpocketPanel.classList.toggle("active", active);
            stationInput.required = active;
            if (!active) {
                stationInput.value = "";
                closeStationSuggestions();
            }
        }

        function disableGeneralIfSpecificSelected() {
            if (pickpocketToggle.checked || fightToggle.checked || crowdToggle.checked) {
                generalToggle.checked = false;
            }
            syncPickpocketState();
        }

        pickpocketToggle.addEventListener("change", disableGeneralIfSpecificSelected);
        fightToggle.addEventListener("change", disableGeneralIfSpecificSelected);
        crowdToggle.addEventListener("change", disableGeneralIfSpecificSelected);
        generalToggle.addEventListener("change", () => {
            if (generalToggle.checked) {
                pickpocketToggle.checked = false;
                fightToggle.checked = false;
                crowdToggle.checked = false;
            }
            syncPickpocketState();
        });

	        citySelect.addEventListener("change", () => {
	            stationInput.value = "";
	            closeStationSuggestions();
                syncCityLabel();
	            if (document.activeElement === stationInput) {
	                renderStations();
	            }
	        });

        cityButton.addEventListener("click", () => {
            const willOpen = !cityPicker.classList.contains("open");
            cityPicker.classList.toggle("open", willOpen);
            cityButton.setAttribute("aria-expanded", String(willOpen));
        });

        cityButton.addEventListener("keydown", (event) => {
            const options = Array.from(cityMenu.querySelectorAll(".city-picker__option"));
            const currentIndex = Math.max(0, options.findIndex((option) => option.dataset.cityValue === citySelect.value));
            if (event.key === "ArrowDown" || event.key === "Enter" || event.key === " ") {
                event.preventDefault();
                cityPicker.classList.add("open");
                cityButton.setAttribute("aria-expanded", "true");
                (options[currentIndex] || options[0])?.focus();
            } else if (event.key === "Escape") {
                closeCityMenu();
            }
        });

        cityMenu.addEventListener("keydown", (event) => {
            const options = Array.from(cityMenu.querySelectorAll(".city-picker__option"));
            const currentIndex = options.indexOf(document.activeElement);
            if (event.key === "ArrowDown") {
                event.preventDefault();
                (options[Math.min(currentIndex + 1, options.length - 1)] || options[0])?.focus();
            } else if (event.key === "ArrowUp") {
                event.preventDefault();
                (options[Math.max(currentIndex - 1, 0)] || options[0])?.focus();
            } else if (event.key === "Escape") {
                closeCityMenu();
                cityButton.focus();
            }
        });

	        stationInput.addEventListener("focus", renderStations);
	        stationInput.addEventListener("input", () => {
	            activeStationIndex = -1;
	            renderStations();
	        });
	        stationInput.addEventListener("keydown", (event) => {
	            const options = Array.from(stationSuggestions.querySelectorAll(".station-suggestion"));
	            if (!options.length || !stationSuggestions.classList.contains("open")) {
	                return;
	            }
	            if (event.key === "ArrowDown") {
	                event.preventDefault();
	                activeStationIndex = Math.min(activeStationIndex + 1, options.length - 1);
	                renderStations();
	            } else if (event.key === "ArrowUp") {
	                event.preventDefault();
	                activeStationIndex = Math.max(activeStationIndex - 1, 0);
	                renderStations();
	            } else if (event.key === "Enter" && activeStationIndex >= 0) {
	                event.preventDefault();
	                selectStation(options[activeStationIndex].dataset.station);
	            } else if (event.key === "Escape") {
	                closeStationSuggestions();
	            }
	        });
	        document.addEventListener("mousedown", (event) => {
                if (!cityPicker.contains(event.target)) {
                    closeCityMenu();
                }
	            if (!stationInput.contains(event.target) && !stationSuggestions.contains(event.target)) {
	                closeStationSuggestions();
	            }
	        });
        reportForm.addEventListener("submit", (event) => {
            if (!citySelect.value) {
                event.preventDefault();
                cityButton.classList.add("invalid");
                cityButton.focus();
            }
        });
	        if (reportImages && selectedImages) {
	            reportImages.addEventListener("change", () => {
	                const files = Array.from(reportImages.files || []);
	                selectedImages.textContent = files.length
	                    ? files.map((file) => file.name).join(", ")
	                    : "";
	            });
	        }
        renderCityMenu();
        syncPickpocketState();
    }());
</script>
</body>
</html>
