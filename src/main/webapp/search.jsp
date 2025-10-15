<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>RouteX - Metro Finder</title>
    <style>
        /* Stile di base per la pagina */
        .searchBox {
                    width: 90%;
                    padding: 10px;
                    font-size: 18px;
                    border-radius: 10px;
                    border: 1px solid #007bff;
                    margin-bottom: 10px;
                }

                /* Contenitore per i risultati */
                .searchResults {
                    border: 1px solid #ccc;
                    max-height: 200px;
                    overflow-y: auto;
                    background: white;
                    width: 90%;
                    position: absolute;
                    z-index: 1000;
                    display: none;
                }

                /* Stile per ogni risultato */
                .resultItem {
                    padding: 10px;
                    cursor: pointer;
                    border-bottom: 1px solid #ddd;
                }

                .resultItem:hover {
                    background-color: #f0f0f0;
                }
        body {
            background: linear-gradient(to bottom, #e0f7fa, #80deea);
            font-family: 'Arial Rounded MT Bold', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            position: relative;
        }

        /* Container per il form e la mappa */
        .main-container {
            display: flex;
            background: rgba(255, 255, 255, 0.9);
            padding: 40px; /* Aumentato il padding */
            border-radius: 20px; /* Aumentato il border-radius */
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.3); /* Rafforzato lo shadow */
            animation: slideIn 1s ease-out;
            max-width: 1200px; /* Aumentato da 1000px a 1200px */
            width: 95%; /* Aumentato il width */
            gap: 40px; /* Aumentato lo spazio tra i contenitori */
        }

        /* Container per il form */
        .form-container {
            flex: 1;
            text-align: center;
        }

        /* Container per la mappa */
        .map-container {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            border: 2px solid #007bff;
            border-radius: 20px; /* Aumentato il border-radius */
            box-shadow: 0 8px 10px rgba(0, 0, 0, 0.2); /* Rafforzato lo shadow */
            background: #f0f8ff;
        }

        .map-container img {
            max-width: 100%;
            max-height: 100%;
            border-radius: 10px;
        }

        /* Stile per le dropdown */
        select {
            width: 90%; /* Aumentata la larghezza */
            padding: 16px; /* Aumentato il padding */
            margin: 10px 0;
            border: 1px solid #007bff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            font-size: 18px; /* Aumentato il font-size */
            transition: all 0.3s ease;
        }

        /* Stile per il bottone */
        button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 16px 30px; /* Aumentato il padding */
            border-radius: 15px;
            font-size: 20px; /* Aumentato il font-size */
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        /* Effetto hover per il bottone */
        button:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }

        /* Effetto focus per i select */
        select:focus {
            outline: none;
            border-color: #0056b3;
            box-shadow: 0 0 8px rgba(0, 91, 187, 0.5);
        }

        /* Animazione di slide in */
        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Stile per il testo del risultato */
        p {
            font-size: 20px; /* Aumentato il font-size */
            color: #333;
            margin-top: 20px;
        }

        /* Stile per i pulsanti registrati, login e home */
        .button-container-right {
            position: absolute;
            top: 20px;
            right: 40px; /* Spazio dal margine destro */
            display: flex;
            gap: 10px; /* Spazio tra i due pulsanti */
        }

        .button-container-right a, .button-container-left a {
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            font-size: 16px;
            border: none;
            cursor: pointer;
            border-radius: 10px;
            text-decoration: none;
            text-align: center;
        }

        .button-container-right a:hover, .button-container-left a:hover {
            background-color: #0056b3;
        }

        /* Contenitore per il pulsante Home */
        .button-container-left {
            position: absolute;
            top: 20px;
            left: 40px; /* Spazio dal margine sinistro */
        }

        /* Tooltip container per il bottone disabilitato */
        #submitBtn[disabled] {
          position: relative;
          cursor: not-allowed;
        }

        /* Tooltip personalizzato (inizialmente nascosto) */
        #submitBtn[disabled]::after {
          content: attr(data-tooltip);
          position: absolute;
          bottom: 130%; /* sopra il bottone */
          left: 50%;
          transform: translateX(-50%);
          background-color: #333;
          color: #fff;
          padding: 6px 10px;
          border-radius: 6px;
          white-space: nowrap;
          font-size: 14px;
          opacity: 0;
          pointer-events: none;
          transition: opacity 0s;
          visibility: hidden;
          z-index: 1000;
        }

        /* Tooltip visibile subito al passaggio mouse */
        #submitBtn[disabled]:hover::after {
          opacity: 1;
          visibility: visible;
          transition-delay: 0s; /* nessun delay */
        }



    </style>
    <script>
        function validateForm() {
            const city = document.getElementById('citySelect').value.trim();
            const startStation = document.getElementById('startSearchBox').value.trim();
            const endStation = document.getElementById('endSearchBox').value.trim();
            const submitBtn = document.getElementById('submitBtn');

            if (city !== "" && startStation !== "" && endStation !== "") {
                submitBtn.disabled = false;
                submitBtn.removeAttribute('title');
            } else {
                submitBtn.disabled = true;
                submitBtn.setAttribute('title', 'Sono presenti dei campi vuoti');
            }
        }


        // Inizializza lo stato del bottone appena la pagina è caricata
        window.onload = function() {
            validateForm();

            <% if (request.getAttribute("stazioniNonValide") != null) { %>
                alert("La stazione di partenza e/o arrivo non esiste per la città selezionata.");
            <% } %>
        }

    </script>
<body>



    <%
        session = request.getSession(false);
        String nome = null;
        if (session != null) {
            nome = (String) session.getAttribute("nome");
        }

        if (nome == null) {
    %>
        <div class="button-container-right">
            <a href="index.jsp">Home</a>
            <a href="register.jsp">Register</a>
            <a href="login.jsp">Login</a>
        </div>
    <%
        } else {
    %>
        <div class="button-container-right">
            <a href="index.jsp">Home</a>
            <a href="logout" class="logout-link">Logout</a>
            <a href="areaRiservata" method="get">Area riservata</a>
        </div>
    <%
        }
    %>

    <div class="main-container">
        <div class="form-container">
            <h2>RouteX - Find Your Metro Route</h2>
            <form action="ServletDemo" method="post" name="select">

                <select name="city" id="citySelect" onchange="validateForm(); updateStationsAndMap()">
                    <option value="" disabled selected>Select a city</option>
                    <option value="Rome">Rome</option>
                    <option value="Milan">Milan</option>
                    <option value="Naples">Naples</option>
                    <option value="Stockholm">Stockholm</option>
                    <option value="Athens">Athens</option>
                    <option value="Budapest">Budapest</option>
                </select>

                <br>
                    <% if (session.getAttribute("cf") == null) { %>
                        <div class="checkboxContainer">
                            <input type="checkbox" name="disabledTraveler" value="yes" id="disabledTravelerCheckbox">
                            <label for="disabledTravelerCheckbox">I am a disabled traveler</label>
                        </div>
                    <% } %>
                <br><br><br>

                <input type="text" name="startStation" id="startSearchBox" class="searchBox" placeholder="Search start station..."
                       onkeyup="validateForm(); searchStation('startSearchBox', 'startSearchResults')">
                <div id="startSearchResults" class="searchResults"></div>

                <input type="text" name="endStation" id="endSearchBox" class="searchBox" placeholder="Search end station..."
                       onkeyup="validateForm(); searchStation('endSearchBox', 'endSearchResults')">
                <div id="endSearchResults" class="searchResults"></div>

                <button type="submit" id="submitBtn" disabled data-tooltip="Sono presenti dei campi vuoti">
                  Find Route
                </button>

                <br><br><br>

                <img src="images/logo-no-background.png" height="90" width="300" >

            </form>

        </div>

        <div class="map-container">
            <img id="mapImage" src="images/subway_default.png" alt="Metro Map">
        </div>
    </div>

    <script>
        // Dati delle stazioni per ogni città
        const cityStations = {
            "Rome": [
                "Battistini", "Cornelia", "Baldo degli Ubaldi", "Valle Aurelia", "Cipro",
                "Ottaviano", "Lepanto", "Flaminio", "Spagna", "Barberini", "Repubblica",
                "Termini", "Vittorio Emanuele", "Manzoni", "San Giovanni", "Re di Roma",
                "Ponte Lungo", "Furio Camillo", "Colli Albani", "Arco di Travertino",
                "Porta Furba", "Numidio Quadrato", "Lucio Sestio", "Giulio Agricola",
                "Subaugusta", "Cinecitta", "Anagnina", "Pantano", "Graniti",
                "Finocchio", "Bolognetta", "Borghesiana", "Due Leoni - Fontana Candida", "Grotte Celoni",
                "Torre Gaia", "Torre Angela", "Torrenova", "Giardinetti", "Torre Maura",
                "Torre Spaccata", "Alessandrino", "Parco di Centocelle", "Mirti",
                "Gardenie", "Teano", "Malatesta", "Pigneto", "Lodi", "San Giovanni", "Laurentina",
                "EUR Fermi", "EUR Palasport", "EUR Magliana", "Marconi", "Basilica S. Paolo",
                "Garbatella", "Piramide", "Circo Massimo", "Colosseo","Cavour", "Termini",
                "Castro Pretorio", "Policlinico", "Bologna", "Tiburtina FS", "Quintiliani",
                "Monti Tiburtini", "Pietralata", "Santa Maria del Soccorso", "Ponte Mammolo",
                "Rebibbia", "Annibaliano", "Libia", "Conca d'Oro", "Jonio"
            ],

            "Paris": ["La Défense", "Esplanade de La Défense", "Pont de Neuilly", "Les Sablons",
                     "Porte Maillot", "Argentine", "Charles de Gaulle–Étoile", "George V",
                     "Franklin D. Roosevelt", "Champs-Élysées–Clemenceau", "Concorde",
                     "Tuileries", "Palais Royal–Musée du Louvre", "Louvre–Rivoli",
                     "Châtelet", "Hôtel de Ville", "Saint-Paul", "Bastille", "Gare de Lyon",
                     "Reuilly–Diderot", "Nation", "Porte de Vincennes", "Saint-Mandé", "Bérault",
                     "Château de Vincennes", "Porte Dauphine", "Victor Hugo",
                     "Ternes", "Courcelles",
                     "Monceau", "Villiers", "Rome", "Place de Clichy", "Blanche", "Pigalle", "Anvers",
                     "Barbès–Rochechouart", "La Chapelle", "Stalingrad", "Jaurès", "Colonel Fabien",
                     "Belleville", "Couronnes", "Ménilmontant", "Père Lachaise",
                     "Philippe Auguste", "Alexandre Dumas", "Avron", "Pont de Levallois–Bécon",
                     "Anatole France", "Louise Michel", "Porte de Champerret", "Pereire", "Wagram",
                     "Malesherbes", "Europe", "Saint-Lazare", "Havre–Caumartin", "Opéra",
                     "Quatre-Septembre", "Bourse", "Sentier", "Réaumur–Sébastopol", "Arts et Métiers",
                     "Temple", "République", "Parmentier", "Rue Saint-Maur",
                     "Gambetta", "Porte de Bagnolet", "Gallieni", "Porte des Lilas", "Saint-Fargeau",
                     "Pelleport", "Porte de Clignancourt", "Simplon", "Marcadet–Poissonniers",
                     "Château Rouge", "Gare de l'Est",
                     "Château d'Eau", "Strasbourg–Saint-Denis",
                     "Étienne Marcel", "Les Halles", "Cité", "Saint-Michel", "Odéon",
                     "Saint-Germain-des-Prés", "Saint-Sulpice", "Saint-Placide", "Montparnasse–Bienvenüe",
                     "Vavin", "Raspail", "Denfert-Rochereau", "Mouton-Duvernet", "Alésia",
                     "Porte d'Orléans", "Mairie de Montrouge", "Bobigny – Pablo Picasso",
                     "Bobigny – Pantin – Raymond Queneau", "Église de Pantin", "Hoche",
                     "Porte de Pantin", "Ourcq", "Laumière", "Gare du Nord",
                     "Jacques Bonsergent", "Oberkampf", "Richard-Lenoir",
                     "Bréguet–Sabin", "Quai de la Rapée", "Gare d'Austerlitz",
                     "Saint-Marcel", "Campo-Formio", "Place d'Italie", "Kléber", "Boissière", "Trocadéro", "Passy", "Bir-Hakeim", "Dupleix",
                     "La Motte-Picquet – Grenelle", "Cambronne", "Sèvres-Lecourbe", "Pasteur",
                     "Edgar Quinet", "Saint-Jacques", "Glacière", "Corvisart",
                     "Nationale",
                     "Chevaleret", "Quai de la Gare", "Bercy", "Dugommier", "Daumesnil", "Bel-Air",
                     "La Courneuve – 8 Mai 1945", "Fort d'Aubervilliers",
                     "Aubervilliers – Pantin – Quatre Chemins", "Porte de la Villette",
                     "Corentin Cariou", "Crimée", "Riquet", "Louis Blanc",
                     "Château-Landon", "Poissonnière", "Cadet", "Le Peletier",
                     "Chaussée d'Antin – La Fayette", "Pyramides",
                     "Pont Neuf",
                     "Pont Marie", "Sully – Morland", "Jussieu", "Place Monge",
                     "Censier – Daubenton", "Les Gobelins", "Tolbiac", "Maison Blanche",
                     "Le Kremlin-Bicêtre", "Villejuif – Léo Lagrange", "Villejuif – Paul Vaillant-Couturier",
                     "Villejuif – Louis Aragon", "Porte de Choisy", "Porte d'Ivry",
                     "Pierre et Marie Curie", "Mairie d'Ivry", "Bolivar",
                     "Buttes Chaumont", "Botzaris", "Danube", "Place des Fêtes", "Pré Saint-Gervais", "Balard",
                     "Lourmel", "Boucicaut", "Félix Faure", "Commerce",
                     "École Militaire", "La Tour-Maubourg", "Invalides", "Madeleine",
                     "Richelieu–Drouot", "Grands Boulevards", "Bonne Nouvelle",
                     "Filles du Calvaire",
                     "Saint-Sébastien–Froissart", "Chemin Vert", "Ledru-Rollin",
                     "Faidherbe–Chaligny", "Montgallet",
                     "Michel Bizot", "Porte Dorée", "Porte de Charenton", "Liberté", "Charenton–Écoles",
                     "École Vétérinaire de Maisons-Alfort", "Maisons-Alfort–Stade", "Maisons-Alfort–Les Juilliottes",
                     "Créteil–L'Échat", "Créteil–Université", "Créteil–Préfecture", "Créteil–Pointe du Lac", "Pont de Sèvres",
                     "Billancourt", "Marcel Sembat", "Porte de Saint-Cloud", "Exelmans", "Michel-Ange–Molitor",
                     "Michel-Ange–Auteuil", "Jasmin", "Ranelagh", "La Muette", "Rue de la Pompe",
                     "Iéna", "Alma–Marceau", "Saint-Philippe du Roule", "Miromesnil",
                     "Saint-Augustin", "Chaussée d'Antin–La Fayette", "Filles du Calvaire",
                     "Charonne", "Rue des Boulets", "Buzenval",
                     "Maraîchers", "Porte de Montreuil", "Robespierre", "Croix de Chavaux",
                     "Mairie de Montreuil", "Boulogne–Pont de Saint-Cloud", "Boulogne–Jean Jaurès",
                     "Chardon Lagache", "Église d'Auteuil", "Mirabeau",
                     "Javel–André Citroën", "Charles Michels", "Avenue Émile Zola",
                     "Ségur", "Duroc", "Vaneau", "Sèvres–Babylone", "Mabillon", "Cluny–La Sorbonne",
                     "Maubert–Mutualité", "Cardinal Lemoine",
                     "Rambuteau", "Goncourt",
                     "Pyrénées", "Jourdain", "Télégraphe",
                     "Mairie des Lilas", "Front Populaire", "Porte de la Chapelle", "Marx Dormoy",
                     "Jules Joffrin", "Lamarck–Caulaincourt", "Abbesses",
                     "Saint-Georges", "Notre-Dame-de-Lorette", "Trinité–d'Estienne d'Orves",
                     "Assemblée Nationale", "Solférino", "Rue du Bac",
                     "Rennes", "Notre-Dame-des-Champs", "Montparnasse Bienvenüe", "Falguière",
                     "Volontaires", "Vaugirard", "Convention", "Porte de Versailles", "Corentin Celton",
                     "Mairie d'Issy", "Saint-Denis–Université", "Basilique de Saint-Denis", "Saint-Denis–Porte de Paris",
                     "Carrefour Pleyel", "Les Agnettes", "Gabriel Péri", "Mairie de Saint-Ouen", "Garibaldi",
                     "Porte de Saint-Ouen", "Guy Môquet", "La Fourche", "Liège",
                     "Champs-Élysées–Clemenceau", "Varenne",
                     "Saint-François-Xavier", "Gaîté", "Pernety", "Plaisance",
                     "Porte de Vanves", "Malakoff–Plateau de Vanves", "Malakoff–Rue Étienne Dolet",
                     "Châtillon–Montrouge", "Pyramides", "Cour Saint-Émilion", "Bibliothèque François Mitterrand",
                     "Olympiades"],

            "Milan": ["Sesto 1 Maggio FS", "Sesto Rondo", "Sesto Marelli", "Villa San Giovanni", "Precotto", "Gorla", "Turro", "Rovereto",
                      "Pasteur", "Loreto", "Lima", "Porta Venezia", "Palestro", "San Babila", "Duomo", "Cordusio", "Cairoli", "Cadorna FN",
                      "Conciliazione", "Pagano", "De Angeli", "Gambara", "Bande Nere", "Primaticcio", "Inganni", "Bisceglie", "Wagner",
                      "Buonarroti", "Amendola", "Lotto", "QT8", "Lampugnano", "Uruguay", "Bonola", "San Leonardo", "Molino Dorino", "Rho Fiera",
                      "Abbiategrasso", "Piazza Abbiategrasso", "Famagosta", "Romolo", "Porta Genova FS", "Sant'Agostino", "Sant'Ambrogio",
                      "Lanza", "Moscova", "Garibaldi FS", "Gioia", "Centrale FS", "Caiazzo", "Piola", "Lambrate FS", "Udine", "Cimiano",
                      "Crescenzago", "C.Na Gobba", "C.Na Antonietta", "Cologno Sud", "Cologno Centro", "Cologno Nord", "Vimodrone", "Cascina Burrona",
                      "Cernusco sul Naviglio", "Villa Fiorita", "Cassina de' Pecchi", "Bussero", "Villa Pompea", "Gorgonzola", "Gessate",
                      "Comasina", "Affori FN", "Affori Centro", "Dergano", "Maciachini", "Zara", "Sondrio", "Repubblica", "Turati",
                      "Montenapoleone", "Missori", "Crocetta", "Porta Romana", "Lodi T.I.B.B.", "Brenta", "Corvetto", "Porto di Mare",
                      "Rogoredo FS", "San Donato", "San Cristoforo", "Segneri", "Frattini", "Gelsomini", "Tolstoi", "Washington-Bolivar",
                      "De Amicis", "Vetra", "Santa Sofia", "Tricolore", "Dateo", "Susa", "Argonne",
                      "Forlanini FS", "Repetti", "Linate Aeroporto", "Bignami Parco Nord", "Ponale", "Bicocca", "Ca' Granda",
                      "Istria", "Marche", "Isola", "Monumentale", "Cenisio", "Gerusalemme", "Domodossola FN", "Tre Torri", "Portello",
                      "Segesta", "San Siro Ippodromo", "San Siro Stadio", "Coni Zugna", "California", "Assago Milanofiori Forum", "Assago Milanofiori Nord",],

            "Naples": ["Pozzuoli Solfatara", "Bagnoli-Agnano Terme", "Campi Flegrei", "Mostra", "P.Leopardi", "Augusto", "Lala",
                       "Mergellina", "Arco Mirelli", "San Pasquale", "Chiaia", "P.Amedeo", "Montesanto", "Museo-Piazza Cavour",
                       "Dante", "Toledo", "Municipio", "Università", "Duomo", "Garibaldi", "Gianturco", "S.Giovanni-Barra",
                       "Materdei", "Salvator Rosa", "Quattro Giornate", "Vanvitelli", "Medaglie D'Oro", "Montedonzelli",
                       "Rione Alto", "Policlinico", "Colli Aminei", "Frullone", "Chiaiano", "Piscinola", "Mugnano", "Giugliano",
                       "Aversa Ippodromo", "Aversa Centro", "Cavalleggeri Aosta"],

            "London": ["Chesham", "Amersham", "Chalfont&Latimer", "Chorleywood", "Rickmansworth", "Croxley", "Watford", "Moor Park",
                        "Northwood", "Northwood Hills", "Northwick Park", "Pinner", "North Harrow", "Harrow on the Hill", "West Harrow", "Eastcote",
                        "Ruislip Manor", "Ruislip", "Ickenham", "Hillingdon", "Uxbridge", "Kenton", "Harrow&Wealdstone",
                        "Headstone Lane", "Hatch End", "Carpenders Park", "Bushey", "Watford High Street", "Watford Junction",
                        "Preston Road", "Wembley Park",
                        "Finchley Road", "Baker Street", "Great Portland Street", "Euston Square", "Euston",
                        "King's Cross St Pancras International",
                        "Farringdon", "Barbican", "Moorgate", "Liverpool Street", "Bethnal Green", "Cambridge Heath",
                        "London Fields", "Hackney Downs",
                        "Hackney Central", "Homerton", "Hackney Wick", "Rectory Road", "Stoke Newington", "Stamford Hill",
                        "Clapton", "St James Street",
                        "Seven Sisters", "Bruce Grove", "White Hart Lane", "Silver Street", "Edmonton Green", "Bush Hill Park",
                        "Enfield Town", "Southbury",
                        "Turkey Street", "Theobalds Grove", "Cheshunt", "Chingford", "Highams Park", "Wood Street",
                        "South Tottenham", "Tottenham Hale",
                        "Blackhorse Road", "Walthamstow Central", "Walthamstow Queen's Road", "Leyton Midland Road",
                        "Leytonstone High Road", "Wanstead Park",
                        "Forest Gate", "Manor Park", "Ilford", "Seven Kings", "Goodmayes", "Chadwell Heath", "Romford",
                        "Gidea Park", "Harold Wood", "Brentwood",
                        "Shenfield", "Emerson Park", "Upminster", "Upminster Bridge", "Hornchurch", "Elm Park",
                        "Dagenham East", "Dagenham Heathway", "Becontree",
                        "Upney", "Barking Riverside", "Barking", "East Ham", "Upton Park", "Plaistow", "Woodgrange Park",
                        "West Ham", "Abbey Road", "Stratford High Street",
                        "Stratford", "Leyton", "Leytonstone", "Snaresbrook", "South Woodford", "Woodford",
                        "Buckhurst Hill", "Loughton", "Debden", "Theydon Bols", "Epping",
                        "Roding Valley", "Chigwell", "Grange Hill", "Hainault", "Fairlop", "Barkingside", "Newbury Park",
                         "Gants Hill", "Redbridge", "Wanstead",
                        "Stratford International", "Dalston Kingsland", "Canonbury", "Highbury/Islington", "Caledonian Road",
                        "Holloway Road", "Arsenal", "Caledonian Road/Barnsbury",
                        "Finsbury Park", "Manor House", "Turnpike Lane", "Wood Green", "Bounds Green", "Arnos Grove", "Southgate",
                        "Oakwood", "Cockfosters", "Harringay Green Lanes",
                        "Crouch Hill", "New Southgate", "Oakleigh Park", "New Barnet", "Towards Welwyn Garden City", "High Barnet",
                        "Totteridge/Whetstone", "Woodside Park", "West Finchley",
                        "Mill Hill East", "Finchley Central", "East Finchley", "Highgate", "Archway", "Upper Holloway",
                        "Tufnell Park", "Kentish Town", "City Thameslink", "St Paul's",
                        "Monument", "Cannon Street", "Blackfriars", "London Bridge", "Bermondsey", "Canada Water", "Rotherhithe",
                        "Wapping", "Shadwell", "Shadwell/Limehouse", "Limehouse",
                        "Whitechapel", "Aldgate East", "Tower Hill", "Tower Gateway", "Aldgate", "Bethnal Green", "Mile End",
                        "Bow Road", "Bow Church", "Pudding Mill Lane", "Maryland",
                        "Bromley by Bow", "Devons Road", "Langdon Park", "All Saints", "Poplar", "Blackwall", "East India",
                        "Canning Town", "Star Lane", "Westferry", "Royal Victoria",
                        "Custom House", "Prince Regent", "Royal Albert", "Beckton Park", "Cyprus", "Gallions Reach", "Beckton",
                         "West Silvertown", "Pontoon Dock", "London City Airport", "King George V",
                        "Woolwich", "Abbey Wood", "Plumstead", "Woolwich Arsenal", "Charlton", "Westcombe Park", "Maze Hill",
                        "Cutty Sark", "Island Gardens", "Mudchute", "Crossharbour", "South Quay", "Heron Quays",
                        "Canary Wharf", "Canary Wharf South", "Canary Wharf North", "West India Quay", "Greenwich", "Deptford Bridge",
                        "Elverson Road", "Lewisham", "Crofton Park", "Catford", "Bellingham", "Beckenham Hill",
                        "Shortlands", "Bromley South", "Bickley", "St Mary Cray", "Petts Wood", "Orpington", "Swanley",
                        "Towards Sevenoaks", "Slade Green", "Dartford", "Towards Gravesend", "Beckenham Junction",
                        "Beckenham Road", "Avenue Road", "Birkbeck", "Harrington Road", "Elmers End", "Arena", "Woodside",
                        "Blackhorse Lane", "Addiscombe", "Lloyd Park", "Coombe Lane", "Gravel Hill", "Addington Village",
                        "Fieldway", "King Henry's Drive", "New Addington", "Sandilands", "Lebanon Road", "East Croydon",
                        "East Croydon South", "South Croydon", "Purley", "Coulsdon South", "Towards Gatwick Airport",
                        "Norwood Junction", "Anerley", "Penge West", "Crystal Palace", "Sydenham", "Forest Hill", "Honor Oak Park",
                        "Brockley", "New Cross Gate", "New Cross", "Surrey Quays", "Nunhead", "Queens Road Peckham",
                        "Peckham Rye", "Denmark Hill", "Elephant/Castle", "Elephant/Castle Ovest", "Loughborough Junction",
                        "Herne Hill", "Tulse Hill", "Streatham", "Mitcham Eastfields", "Mitcham Junction", "Beddington Lane",
                        "Therapia Lane", "Ampere Way", "Waddon Marsh", "Wandle Park", "Reeves Corner", "Church Street",
                        "Centrale", "West Croydon South", "West Croydon", "Wellesley Road", "George Street", "Hackbridge",
                        "Carshalton",
                        "Sutton", "West Sutton", "Sutton Common", "St Heller", "Morden South", "South Merton", "Wimbledon Chase",
                        "Wimbledon", "Dundonald Road", "Merton Park", "Morden Road", "South Wimbledon", "Phipps Bridge",
                        "Belgrave Walk", "Mitcham", "Colliers Wood", "Tooting Broadway", "Haydons Road", "Tooting Bec", "Balham",
                         "Clapham South", "Clapham Common", "Clapham North", "Clapham High Street", "Stockwell", "Brixton",
                        "Oval", "Kennington", "Waterloo", "Embankment", "Charing Cross", "Piccadilly Circus", "Leicester Square",
                        "Covent Garden", "Holborn", "Chancery Lane", "Russell Square", "Warren Street", "OXFORD Circus",
                        "Tottenham Court Road", "Goodge Street", "Green Park", "Hyde Park Corner", "Knightsbridge",
                        "South Kensington", "Sloane Square", "Victoria", "St James's Park", "Westminster", "Bond Street",
                        "Marble Arch", "Lancaster Gate",
                        "Queensway", "Notting Hill Gate", "Bayswater", "PADDINGTON", "Shepherd's Bush", "Kensington (Olympia)",
                        "High Street Kensington", "Gloucester Road", "Earl's Court", "West Brompton", "Fulham Broadway", "Parsons Green",
                        "Putney Bridge", "Imperial Wharf", "East Putney", "Southfields", "Wimbledon Park", "West Kensington",
                        "Barons Court", "Hammersmith", "Ravenscourt Park", "Stamford Brook", "Goldhawk Road", "Bush Market", "White City",
                        "Wood Lane", "East Acton", "North Acton", "Hanger Lane", "Park Royal", "North Ealing", "Alperton",
                        "Sudbury Town", "Sudbury Hill", "South Harrow", "Perivale", "Greenford", "Northolt", "South Ruislip", "Ruislip Gardens",
                        "West Ruislip", "Acton Main Line", "West Acton", "Acton Central", "South Acton", "Turnham Green",
                        "Chiswick Park", "Gunnersbury", "Acton Town", "Ealing Common", "Ealing Broadway", "West Ealing",
                        "Hanwell", "Southhall", "Hayes/Harlington",
                        "West Drayton", "Iver", "Langley", "Slough", "Burnham", "Taplow", "Maldenhead", "Twyford", "Reading",
                        "Heathrow Terminals 2-3", "Heathrow Terminal 5", "Heathrow Terminal 4", "Richmond", "Kew Gardens",
                        "Hatton Cross", "Hounslow West",
                        "Hounslow Central", "Hounslow East", "Osterley", "Boston Manor", "Northfields", "South Ealing",
                        "Pimlico", "Battersea Power Station", "Nine Elms", "Vauxhall", "Lambeth North", "Southwark",
                        "Borough", "Royal Oak", "Westbourne Park",
                        "Ladbroke Grove", "Latimer Road", "Warwick Avenue", "Malda Vale", "Kilburn Park", "Queen's Park",
                        "Kensal Green", "Kilburn High Road", "Edgware Road", "Marylebone", "Edgware Road", "Regent's Park",
                         "Kensal Rise", "Brondesbury Parks",
                        "Brondesbury", "Harlesden", "Stonebridge Park", "Wembley Central", "North Wembley", "South Kenton",
                        "Willesden Junction", "Stanmore", "Canons Park", "Queensbury", "Kingsbury", "Neasden", "Dollis Hill",
                        "Willesden Green", "Kilburn",
                        "West Hampstead south", "West Hampstead", "West Hampstead Thameslink", "Finchley Road/Frognal",
                        "South Hampstead", "Swiss Cottage", "St John's Wood", "Hampstead Heath", "Belsize Park", "Chalk Farm",
                        "Camden Town", "Mornington Crescent",
                        "Angel", "Old Street", "Dalston Junction", "Haggerston", "Hoxton", "Shoreditch High Street",
                        "Clapham Junction", "Wandsworth Road", "Deptford", "Cricklewood", "Brent Cross West",
                        "Hendon", "Mill Hill Broadway", "Elstree/Borehamwood",
                        "Towards St Albans City and Luton Airport Parkway", "Edgware", "Burnt Oak",
                        "Hendon Central", "Brent Cross", "Golders Green", "Hampstead", "Gospel Oak",
                        "Kentish Town West", "Camden Road", "Tooting", "Stepney Green", "Rayners Lane",
                        "Mansion House", "North Greenwich", "Ravensbourne"],
            "Berlin": ["Alexanderplatz", "Hauptbahnhof", "Potsdamer Platz", "Zoologischer Garten"],
            "Stockholm": ["Morby Centrum", "Danderyds Sjukhus", "Bergshamra", "Universitetet", "Tekniska Hogskolan",
                        "Stadion", "Ostermalmstorg", "Ropsten", "Gardet", "Karlaplan", "T-Centralen", "Gamla Stan",
                        "Kungstradgarden", "Slussen", "Medborgarplatsen", "Skanstull", "Gullmarsplan", "Skarmarbrink",
                        "Globen", "Blasut", "Hammarbyhojden", "Bjorkhagen", "Karrtorp", "Bagarmossen", "Skarpnack",
                        "Sandsborg", "Skogskyrko Garden", "Tallkgrogen", "Gubbangen", "Hokarangen", "Farsta",
                        "Farsta Strand", "Enskede Gard", "Sockenplan", "Svedmyra", "Stureby", "Bandhagen", "Hogdalen",
                        "Ragsved", "Hagsatra", "Mariatorget", "Zinkensdamm", "Hornstull", "Liljeholmen", "Aspudden", "Midsommar Kransen",
                        "Telefonplan", "Hagerstensasen", "Vastertorp", "Fruangen", "Ornsberg", "Axels Berg", "Malar Hojden",
                        "Bredang", "Satra", "Skarholmen", "Varberg", "Varby Gard", "Masmo", "Fittja", "Alby", "Hallunda",
                        "Norsborg", "Hotorget", "Radmansgatan", "Odenplan", "Sankt Eriksplan", "Fridhemsplan", "Thorildsplan",
                        "Rad Huset", "Kristine Berg", "Alvik", "Storamossen", "Abrahamsberg", "Brommaplan", "Akeshov",
                        "Angbyplan", "Islandstorget", "Blackeberg", "Racksta", "Vallingby", "Johannelund", "Hasselby Gard",
                        "Hasselby Strand", "Stadshagen", "Vastra Skogen", "Solna Centrum", "Huvudsta", "Solna Strand",
                        "Sundbybergs Centrum", "Duvbo", "Rissne", "Rinkeby", "Tensta", "Hjulsta", "Nackrosen", "Hallonbergen",
                        "Kymlinge", "Kista", "Husby", "Akalla" ],
            "Athens": ["Airport", "Koropi", "Paiania-Kantza", "Pallini", "Doukissis Plakentias", "Halandri",
                        "Aghia Paraskevi", "Nomismatokopio", "Holargos", "Ethniki Amyna", "Katehaki", "Panormou",
                        "Ampelokipi", "Megaro Moussikis", "Evangelismos", "Syntagma", "Panipistimo","Omonia",
                        "Victoria", "Attiki", "Aghios Nikolaos", "Kato Patissia", "Aghios Eleftherios", "Ano Patissia",
                        "Perissos", "Pefkakia", "Nea Ionia", "Iraklio", "Irini", "Neratziotissa", "Maroussi", "KAT",
                        "Kifissia", "Akropoli", "Syngrou Fix", "Aghios Ioannis", "Dafni", "Aghios Dimitrios", "Illioupoli",
                        "Alimos", "Argyroupoli", "Elliniko", "Monastiraki", "Thissio", "Petralona", "Tavros", "Kallithea",
                        "Moschato", "Faliro", "Piraeus", "Dimotiko Theatro", "Maniatika", "Nikaia", "Korydallos",
                        "Aghia Varvara", "Aghia Marina", "Egaleo", "Eleonas", "Kerameikos", "Metaxourghio",
                        "Larissa Station", "Sepolia", "Aghios Antonios", "Peristeri", "Anthoupoli", "Neos Kosmos"],

            "Budapest": ["Ors vezer tere", "Pillango utca", "Puskas Ferenc Stadion", "Keleti Palyaudvar", "Blaha Lujza Ter",
                        "Il. Janos Pal Papa Ter", "Rakoczi Ter", "Kalvin Ter", "Fovam Ter", "Szent Gellert Ter - Muegyetem",
                        "Moricz Zsigmond Korter", "Ujbuda-kozport", "Bikas Park", "Kelenfood Vasutallomas", "Kobanya-Kispes",
                        "Hatar Ut", "Pottyos Utca", "Ecseri Ut", "Nepliget", "Nagyvarad Ter", "Semmelweis Klinikak",
                        "Corvin-negyed", "Ferenciek Tere", "Deak Ferenc Ter", "Vorosmarty Ter", "Bajcsy-Zsilinszky ut", "Opera",
                        "Oktogon", "Vorosmarty Utca", "Kodaly Korond", "Bajza Utca", "Hosok Tere", "Szechenyi-furdo",
                        "Mexikoi Ut", "Ujpest-Kozpont", "Ujpest-Varoskapu", "Gyongyosi Utca", "Forgach Utca",
                        "Goncz Arpad Vkp", "Dozsa Gyorgy Ut", "Lehel Ter", "Nyugati Palyaudva", "Arany Janos Utca",
                        "Kossuth Lajos Ter", "Battyhany Ter", "Szell Kalman Ter", "Deli Palyaudvar", "Astoria"]
        };


        function searchStation(inputId, resultsId) {
                    let input = document.getElementById(inputId).value.toLowerCase();
                    let resultsDiv = document.getElementById(resultsId);
                    resultsDiv.innerHTML = ""; // Pulisce i risultati precedenti

                    if (input.length < 1) {
                        resultsDiv.style.display = "none";
                        return;
                    }

                    let selectedCity = document.getElementById("citySelect").value;

                    let suggestions = cityStations[selectedCity].filter(station => station.toLowerCase().includes(input));

                    if (suggestions.length > 0) {
                        resultsDiv.style.display = "block"; // Mostra i risultati
                    } else {
                        resultsDiv.style.display = "none"; // Nasconde se non ci sono risultati
                    }

                    suggestions.forEach(station => {
                        let div = document.createElement("div");
                        div.classList.add("resultItem");
                        div.textContent = station;

                        div.onclick = function () {
                            document.getElementById(inputId).value = station;
                            resultsDiv.style.display = "none"; // Nasconde i suggerimenti dopo la selezione
                        };

                        resultsDiv.appendChild(div);
                    });
                }

        function updateStationsAndMap() {
            const citySelect = document.getElementById('citySelect');
            const startSearchBox = document.getElementById('startSearchBox');
            const endSearchBox = document.getElementById('endSearchBox');
            const mapImage = document.getElementById('mapImage');
            const selectedCity = citySelect.value;

            // Se non è selezionata una città, non cambiare l'immagine
            if (!selectedCity) {
                return;
            }

            // Aggiorna l'immagine della mappa in base alla città selezionata
            mapImage.src = 'images/metro-' + selectedCity.toLowerCase() + '.jpg';

            // Aggiorna i placeholder delle caselle di testo
            startSearchBox.placeholder = `Search stations in ${selectedCity}...`;
            endSearchBox.placeholder = `Search stations in ${selectedCity}...`;

        }

    </script>

</body>
</html>