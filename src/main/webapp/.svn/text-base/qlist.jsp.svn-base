<%@ include file = "/entete.jsp" %>
<%@ page import = "formOnLine.msBeans.*" %>
<jsp:useBean id="qlist" class="com.triangle.lightfw.ValueBeanList" scope="session" />
<table class=tabSize>
<tr><td>
<strong>Admin questionnaires</strong><br><br><%
		
		for (int i=0; qlist != null && i<qlist.size(); i++) {
			Questionnaire qi = (Questionnaire)qlist.get(i);
			out.println("<div class=greyBox><span class=etape1>"+qi.getId()+ ".</span> "+qi.getTitre() );
			if ((sessionInfos != null) && (sessionInfos.getRole()>SessionInfos.ROLE_GESTION)) { 
				out.println(" <a href='ServControl?action=QMAJ&qid="+qi.getId()+"' class=fleche_orange>Modifier</a> ");
			}
			out.println(" <a href='ServControl?action=QUEST&qid="+qi.getId()+"' class=fleche_orange>Créer</a></div>");
		}
out.println(" <br>nb quest: "+qlist.size() );

%>

<% if ((sessionInfos != null) && (sessionInfos.getRole()>SessionInfos.ROLE_GESTION)) { %>
<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=ADMF")%>">[New]</a>
<% } %> 
<br>
<br>
</td></tr></table>
<%@ include file = "/footer.jsp" %>
