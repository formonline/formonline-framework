<html>
<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<body>
<BR>
<h3>Désolé, vous avez déjà répondu à ce questionnaire...</h3>

<%
	// cookies
	javax.servlet.http.Cookie[] cook = request.getCookies();
	for (int i=0; i<cook.length ; i++) { %>
	
		<BR><%=cook[i].getName()%> = <%=cook[i].getValue()%>
	
	<%}%>
</body>
</html>
