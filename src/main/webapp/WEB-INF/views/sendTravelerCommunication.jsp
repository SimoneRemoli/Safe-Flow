<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RouteX - Traveler Report</title>
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
        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Trebuchet MS", "Gill Sans", sans-serif;
            background:
                radial-gradient(circle at 15% 22%, rgba(111, 247, 255, 0.16), transparent 24%),
                radial-gradient(circle at 85% 18%, rgba(83, 169, 255, 0.18), transparent 22%),
                linear-gradient(135deg, var(--bg-1), var(--bg-2) 58%, #040913);
            display: grid;
            place-items: center;
            padding: 18px;
        }

        .panel {
            width: min(820px, 100%);
            padding: 30px;
            border-radius: 30px;
            border: 1px solid var(--line);
            background: linear-gradient(180deg, rgba(7, 20, 36, 0.84), rgba(4, 12, 23, 0.9));
            box-shadow: 0 28px 70px rgba(0, 0, 0, 0.38);
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
            margin: 0 0 24px;
            color: var(--muted);
            line-height: 1.75;
        }

        textarea {
            width: 100%;
            min-height: 190px;
            border-radius: 22px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.04);
            color: var(--text);
            padding: 18px;
            font: inherit;
            resize: vertical;
            outline: none;
        }

        .field {
            margin-bottom: 16px;
        }

        .field label {
            display: block;
            margin-bottom: 8px;
            color: var(--muted);
            font-size: 0.95rem;
        }

        select {
            width: 100%;
            border-radius: 16px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.04);
            color: var(--text);
            padding: 14px 16px;
            font: inherit;
            outline: none;
        }

        input[type="text"] {
            width: 100%;
            border-radius: 16px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.04);
            color: var(--text);
            padding: 14px 16px;
            font: inherit;
            outline: none;
        }

        .toggle-row {
            margin-bottom: 16px;
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
            padding: 12px 16px;
            border-radius: 999px;
            border: 1px solid rgba(255, 255, 255, 0.12);
            background: rgba(255, 255, 255, 0.06);
            color: var(--text);
            font-weight: 700;
            cursor: pointer;
            transition: transform 0.2s ease, background 0.2s ease, border-color 0.2s ease, color 0.2s ease;
        }

        .toggle-input:checked + .toggle-button {
            background: rgba(239, 68, 68, 0.18);
            border-color: rgba(239, 68, 68, 0.38);
            color: #fff1f2;
        }

        .toggle-input.fight-toggle:checked + .toggle-button {
            background: rgba(250, 204, 21, 0.2);
            border-color: rgba(250, 204, 21, 0.42);
            color: #1d4ed8;
        }

        .toggle-input.crowd-toggle:checked + .toggle-button {
            background: rgba(34, 197, 94, 0.18);
            border-color: rgba(34, 197, 94, 0.38);
            color: #14532d;
        }

        .toggle-input.general-toggle:checked + .toggle-button {
            background: rgba(255, 255, 255, 0.96);
            border-color: rgba(148, 163, 184, 0.9);
            color: #0f172a;
            transform: translateX(8px);
        }

        .toggle-button.general-button {
            margin-left: 16px;
            border-color: rgba(148, 163, 184, 0.46);
        }

        .pickpocket-panel {
            display: none;
            margin: 0 0 16px;
            padding: 18px;
            border-radius: 20px;
            border: 1px solid rgba(239, 68, 68, 0.22);
            background: rgba(239, 68, 68, 0.06);
        }

        .pickpocket-panel.active {
            display: block;
        }

        .actions {
            margin-top: 24px;
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .btn,
        .btn-link {
            text-decoration: none;
            border: none;
            cursor: pointer;
            padding: 14px 20px;
            border-radius: 999px;
            font-weight: 700;
            letter-spacing: 0.04em;
        }

        .btn {
            color: #04111f;
            background: linear-gradient(90deg, #6ff7ff, #89ffd1 52%, #8dd8ff);
        }

        .btn-link {
            color: var(--text);
            background: rgba(255, 255, 255, 0.06);
            border: 1px solid rgba(255, 255, 255, 0.12);
        }
    </style>
    <link rel="stylesheet" href="css/minimal-ui.css">
</head>
<body>
<%@ include file="/header.jspf" %>
<div class="panel">
    <span class="eyebrow">Traveler report</span>
    <h1>Send a traveler report</h1>
    <p class="subtitle">
        Write a message for the RouteX admin team. Traveler reports are reviewed by admins before they become visible in the notification system.
    </p>

    <form action="submitTravelerCommunication" method="post">
        <div class="field">
            <label for="city">City</label>
            <select id="city" name="city" required>
                <option value="">Select a city</option>
                <option value="Rome">Rome</option>
                <option value="Naples">Naples</option>
                <option value="Athens">Athens</option>
                <option value="Budapest">Budapest</option>
            </select>
        </div>
        <div class="toggle-row">
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
            <div class="field">
                <label for="stationName">Station</label>
                <input type="text" id="stationName" name="stationName" list="stationSuggestions" placeholder="Confirm the station involved in the alert.">
                <datalist id="stationSuggestions"></datalist>
            </div>
        </div>
        <textarea id="message" name="message" maxlength="250" placeholder="Describe the issue or information you want to report..."></textarea>

        <div class="actions">
            <button type="submit" class="btn">Submit report</button>
            <a href="reportGuide" class="btn-link">How reporting works</a>
            <a href="travelerHome" class="btn-link">Back to home</a>
        </div>
    </form>
</div>
<script>
    (function () {
        const cityStations = {
            "Rome": ["Battistini", "Cornelia", "Baldo degli Ubaldi", "Valle Aurelia", "Cipro", "Ottaviano", "Lepanto", "Flaminio", "Spagna", "Barberini", "Repubblica", "Termini", "Vittorio Emanuele", "Manzoni", "San Giovanni", "Re di Roma", "Ponte Lungo", "Furio Camillo", "Colli Albani", "Arco di Travertino", "Porta Furba", "Numidio Quadrato", "Lucio Sestio", "Giulio Agricola", "Subaugusta", "Cinecitta", "Anagnina", "Pantano", "Graniti", "Finocchio", "Bolognetta", "Borghesiana", "Due Leoni - Fontana Candida", "Grotte Celoni", "Torre Gaia", "Torre Angela", "Torrenova", "Giardinetti", "Torre Maura", "Torre Spaccata", "Alessandrino", "Parco di Centocelle", "Mirti", "Gardenie", "Teano", "Malatesta", "Pigneto", "Lodi", "Laurentina", "EUR Fermi", "EUR Palasport", "EUR Magliana", "Marconi", "Basilica S. Paolo", "Garbatella", "Piramide", "Circo Massimo", "Colosseo", "Cavour", "Castro Pretorio", "Policlinico", "Bologna", "Tiburtina FS", "Quintiliani", "Monti Tiburtini", "Pietralata", "Santa Maria del Soccorso", "Ponte Mammolo", "Rebibbia", "Annibaliano", "Libia", "Conca D oro", "Jonio"],
            "Naples": ["Pozzuoli Solfatara", "Bagnoli-Agnano Terme", "Campi Flegrei", "Mostra", "P.Leopardi", "Augusto", "Lala", "Mergellina", "Arco Mirelli", "San Pasquale", "Chiaia", "P.Amedeo", "Montesanto", "Museo-Piazza Cavour", "Dante", "Toledo", "Municipio", "Universita", "Duomo", "Garibaldi", "Gianturco", "S.Giovanni-Barra", "Materdei", "Salvator Rosa", "Quattro Giornate", "Vanvitelli", "Medaglie D'Oro", "Montedonzelli", "Rione Alto", "Policlinico", "Colli Aminei", "Frullone", "Chiaiano", "Piscinola", "Mugnano", "Giugliano", "Aversa Ippodromo", "Aversa Centro", "Cavalleggeri Aosta"],
            "Athens": ["Airport", "Koropi", "Paiania-Kantza", "Pallini", "Doukissis Plakentias", "Halandri", "Aghia Paraskevi", "Nomismatokopio", "Holargos", "Ethniki Amyna", "Katehaki", "Panormou", "Ampelokipi", "Megaro Moussikis", "Evangelismos", "Syntagma", "Panipistimo", "Omonia", "Victoria", "Attiki", "Aghios Nikolaos", "Kato Patissia", "Aghios Eleftherios", "Ano Patissia", "Perissos", "Pefkakia", "Nea Ionia", "Iraklio", "Irini", "Neratziotissa", "Maroussi", "KAT", "Kifissia", "Akropoli", "Syngrou Fix", "Aghios Ioannis", "Dafni", "Aghios Dimitrios", "Illioupoli", "Alimos", "Argyroupoli", "Elliniko", "Monastiraki", "Thissio", "Petralona", "Tavros", "Kallithea", "Moschato", "Faliro", "Piraeus", "Dimotiko Theatro", "Maniatika", "Nikaia", "Korydallos", "Aghia Varvara", "Aghia Marina", "Egaleo", "Eleonas", "Kerameikos", "Metaxourghio", "Larissa Station", "Sepolia", "Aghios Antonios", "Peristeri", "Anthoupoli", "Neos Kosmos"],
            "Budapest": ["Ors vezer tere", "Pillango utca", "Puskas Ferenc Stadion", "Keleti Palyaudvar", "Blaha Lujza Ter", "Il. Janos Pal Papa Ter", "Rakoczi Ter", "Kalvin Ter", "Fovam Ter", "Szent Gellert Ter - Muegyetem", "Moricz Zsigmond Korter", "Ujbuda-kozport", "Bikas Park", "Kelenfood Vasutallomas", "Kobanya-Kispes", "Hatar Ut", "Pottyos Utca", "Ecseri Ut", "Nepliget", "Nagyvarad Ter", "Semmelweis Klinikak", "Corvin-negyed", "Ferenciek Tere", "Deak Ferenc Ter", "Vorosmarty Ter", "Bajcsy-Zsilinszky ut", "Opera", "Oktogon", "Vorosmarty Utca", "Kodaly Korond", "Bajza Utca", "Hosok Tere", "Szechenyi-furdo", "Mexikoi Ut", "Ujpest-Kozpont", "Ujpest-Varoskapu", "Gyongyosi Utca", "Forgach Utca", "Goncz Arpad Vkp", "Dozsa Gyorgy Ut", "Lehel Ter", "Nyugati Palyaudva", "Arany Janos Utca", "Kossuth Lajos Ter", "Battyhany Ter", "Szell Kalman Ter", "Deli Palyaudvar", "Astoria"]
        };

        const citySelect = document.getElementById("city");
        const pickpocketToggle = document.getElementById("pickpocketAlert");
        const fightToggle = document.getElementById("fightAlert");
        const crowdToggle = document.getElementById("crowdAlert");
        const generalToggle = document.getElementById("generalAlert");
        const pickpocketPanel = document.getElementById("pickpocketPanel");
        const stationInput = document.getElementById("stationName");
        const stationSuggestions = document.getElementById("stationSuggestions");

        function renderStations() {
            const stations = cityStations[citySelect.value] || [];
            stationSuggestions.innerHTML = "";
            stations.forEach((station) => {
                const option = document.createElement("option");
                option.value = station;
                stationSuggestions.appendChild(option);
            });
        }

        function syncPickpocketState() {
            const active = pickpocketToggle.checked || fightToggle.checked || crowdToggle.checked;
            pickpocketPanel.classList.toggle("active", active);
            stationInput.required = active;
            if (!active) {
                stationInput.value = "";
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

        citySelect.addEventListener("change", renderStations);
        renderStations();
        syncPickpocketState();
    }());
</script>
</body>
</html>
