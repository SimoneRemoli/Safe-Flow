<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="it.web.routex.bean.PathInfoBean"%>
<%@ page import="it.web.routex.bean.MessageBean"%>

<%
    List<PathInfoBean> pathList = (List<PathInfoBean>) request.getAttribute("pathList");
    Set<String> utenti = new HashSet<>();
    if(pathList != null){
        for(PathInfoBean p : pathList){
            utenti.add(p.getUtente());
        }
    }
%>



<!DOCTYPE html>
<html>
<head>
    <title>RouteX - Reports & Statistics</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        body { margin:0; padding:0; font-family:'Arial Rounded MT Bold', sans-serif; height:100vh; display:flex; align-items:center; background:url('images/light.jpg') no-repeat center center/cover; overflow:hidden; position:relative; color:white; }
        .background-blur { position:absolute; top:0; left:0; width:100%; height:100%; backdrop-filter:blur(6px); z-index:-1; }
        .main-container { display:flex; flex-direction:row; align-items:center; width:100%; height:100%; }
        .left-box { flex:1; padding:30px; background:rgba(0,0,0,0.6); display:flex; flex-direction:column; align-items:center; justify-content:center; height:100%; box-shadow:2px 0 10px rgba(0,0,0,0.5); }
        .left-box img { width:150px; margin-bottom:20px; }
        .left-box h1 { font-size:24px; text-align:center; margin-bottom:10px; }
        .left-box p { font-size:18px; text-align:center; line-height:1.5; }
        .right-content { flex:3; display:flex; flex-direction:column; align-items:center; justify-content:flex-start; padding:40px; overflow-y:auto; height:100vh; }
        .button-container { position:absolute; top:20px; right:40px; display:flex; gap:10px; }
        .button-container a { background-color:#007bff; color:white; padding:10px 15px; font-size:16px; border:none; cursor:pointer; border-radius:10px; text-decoration:none; text-align:center; transition:background-color 0.3s ease; }
        .button-container a:hover { background-color:#0056b3; }
        .stats-container { display:grid; grid-template-columns:repeat(auto-fit, minmax(250px, 1fr)); gap:20px; width:90%; margin-top:40px; }
        .stat-card { background:rgba(255,255,255,0.9); border-radius:15px; box-shadow:0 4px 15px rgba(0,0,0,0.3); color:#000; padding:20px; text-align:center; transition:transform 0.3s ease; }
        .stat-card:hover { transform:scale(1.05); }
        .stat-card i { font-size:40px; color:#007bff; margin-bottom:10px; }
        .stat-card h3 { margin:10px 0; }
        .stat-card p { font-size:20px; font-weight:bold; }
        .action-buttons { display:flex; justify-content:center; align-items:center; gap:10px; margin-top:30px; }
        .action-buttons button, .action-buttons input { padding:8px 12px; font-size:16px; border-radius:5px; border:1px solid #007bff; }
        .action-buttons button { background:#007bff; color:white; cursor:pointer; }
        .action-buttons button:hover { background:#0056b3; }
        .action-buttons input { width:200px; }
        #userList {
            list-style:none;
            padding-left:0;
            margin:0;
            position:absolute;
            top:100%;
            left:0;
            width:100%;
            background:white;
            color:black;
            border:1px solid #ccc;
            border-radius:5px;
            max-height:200px;
            overflow-y:auto;
            display:none; /* <-- inizialmente nascosto */
            z-index:100;
        }
        #userList li { padding:5px 10px; cursor:pointer; }
        #userList li:hover { background:#007bff; color:white; }
        table { width:90%; margin-top:40px; border-collapse: collapse; background:rgba(255,255,255,0.9); color:#000; border-radius:10px; overflow:hidden; font-size:14px; table-layout: fixed; word-wrap: break-word; }
        th, td { border: 1px solid #ccc; padding: 6px 8px; text-align: center; }
        th { background-color: #007bff; color: white; font-weight: bold; }
        tr:nth-child(even) { background-color: #fafafa; }
        tr:hover { background-color: #e6f0ff; }
    </style>
</head>

<body>
<div class="background-blur"></div>

<div class="main-container">
    <!-- SINISTRA -->
    <div class="left-box">
        <img src="images/logo-no-background.png" alt="Logo">
        <h1>RouteX</h1>
        <p>Analyze your metro travels with RouteX!</p>
    </div>

    <!-- DESTRA -->
    <div class="right-content">
        <!-- Home e Logout -->
        <div class="button-container">
            <a href="indexAdmin.jsp">Home</a>
            <a href="logout">Logout</a>
        </div>

        <h1 style="margin-top:20px;">Reports & Statistics</h1>

        <!-- STATISTICHE -->
        <div class="stats-container">
            <div class="stat-card">
                <i class="fas fa-route"></i>
                <h3>Total Trips</h3>
                <p><%= pathList != null ? pathList.size() : 0 %></p>
            </div>
            <div class="stat-card">
                <i class="fas fa-road"></i>
                <h3>Total Distance</h3>
                <p>
                    <%
                        double totalDistance = 0;
                        if (pathList != null) {
                            for (PathInfoBean p : pathList) {
                                totalDistance += p.getPercTerrenoUtilizzato();
                            }
                        }
                        String formattedDistance = String.format("%.2f", totalDistance);
                        out.print(formattedDistance + " km");
                    %>
                </p>
            </div>
            <div class="stat-card">
                <i class="fas fa-clock"></i>
                <h3>Total Time</h3>
                <p>
                    <%
                        double totalTime = 0;
                        if(pathList != null){
                            for(PathInfoBean p : pathList){
                                totalTime += p.getTempoDiArrivo();
                            }
                        }
                        out.print(totalTime + " h");
                    %>
                </p>
            </div>
        </div>

        <!-- BACK E RICERCA UTENTI -->
        <div class="action-buttons">
            <button id="backBtn" onclick="goBack()">Back</button>
            <div style="position:relative;">
                <input type="text" id="searchInput" placeholder="Search user...">
                <ul id="userList">
                    <% for(String u : utenti){ %>
                        <li onclick="selectUser('<%=u%>')"><%=u%></li>
                    <% } %>
                </ul>
            </div>
        </div>

    </div>
</div>

<script>
    function goBack() {
        window.history.back();
    }

    const searchInput = document.getElementById('searchInput');
    const userList = document.getElementById('userList');

    // Mostra lista quando si clicca sull'input
    searchInput.addEventListener('focus', () => {
        userList.style.display = 'block';
    });

    // Filtra utenti mentre digiti
    searchInput.addEventListener('keyup', () => {
        const filter = searchInput.value.toLowerCase();
        const items = userList.querySelectorAll('li');
        items.forEach(li => {
            if(li.textContent.toLowerCase().includes(filter)){
                li.style.display = '';
            } else {
                li.style.display = 'none';
            }
        });
    });

    // Nascondi lista se clicchi fuori
    document.addEventListener('click', (e) => {
        if (!searchInput.contains(e.target) && !userList.contains(e.target)) {
            userList.style.display = 'none';
        }
    });

    // Popup dettagli percorso
    function selectUser(user) {
        searchInput.value = user;

        const popup = window.open('', 'userTable', 'width=1000,height=600,scrollbars=yes');
        popup.document.write('<html><head><title>Paths for ' + user + '</title>');
        popup.document.write('<style>');
        popup.document.write('body{font-family:Arial, sans-serif; background:#f4f4f4; padding:20px;}');
        popup.document.write('table { width:100%; border-collapse:collapse; font-size:14px; }');
        popup.document.write('th, td { border:1px solid #ccc; padding:6px 8px; text-align:center; }');
        popup.document.write('th { background-color:#007bff; color:white; font-weight:bold; }');
        popup.document.write('tr:nth-child(even) { background-color:#fafafa; }');
        popup.document.write('tr:hover { background-color:#e6f0ff; }');
        popup.document.write('</style></head><body>');
        popup.document.write('<h2 style="text-align:center;">Paths for ' + user + '</h2>');
        popup.document.write('<table><tr>');
        popup.document.write('<th>Start</th><th>End</th><th>City</th><th>Tipo Viaggiatore</th><th>Numero Cambi</th><th>Lista Cambi</th><th>Stazione Intercambio</th><th>Stazioni Attraversate</th><th>Tempo Arrivo (min.)</th><th>Stazioni Città</th><th>Perc Terreno</th><th>Utente</th>');
        popup.document.write('</tr>');

        <% if(pathList != null){ %>
            <% for(PathInfoBean p : pathList){ %>
                if('<%= p.getUtente() %>' === user) {
                    popup.document.write('<tr>');
                    popup.document.write('<td><%= p.getStartStation() %></td>');
                    popup.document.write('<td><%= p.getEndStation() %></td>');
                    popup.document.write('<td><%= p.getCity() %></td>');
                    popup.document.write('<td><%= p.getTipoViaggiatore() %></td>');
                    popup.document.write('<td><%= p.getNCambi() %></td>');
                    popup.document.write('<td><%= p.getListaCambi() %></td>');
                    popup.document.write('<td><%= p.getStazioneDiInterscambio() %></td>');
                    popup.document.write('<td><%= p.getNStazioniAttraversate() %></td>');
                    popup.document.write('<td><%= p.getTempoDiArrivo() %></td>');
                    popup.document.write('<td><%= p.getNStazioniCitta() %></td>');
                    popup.document.write('<td><%= String.format("%.2f", p.getPercTerrenoUtilizzato()) %></td>');
                    popup.document.write('<td><%= p.getUtente() %></td>');
                    popup.document.write('</tr>');
                }
            <% } %>
        <% } %>

        popup.document.write('</table></body></html>');
        popup.document.close();
    }
</script>

</body>
</html>