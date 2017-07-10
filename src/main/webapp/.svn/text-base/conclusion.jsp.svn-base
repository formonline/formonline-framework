<%@ include file = "/entete.jsp" %>
<jsp:useBean id="q" class="formOnLine.msBeans.Questionnaire" scope="session" />
<jsp:useBean id="sf" class="formOnLine.msBeans.SubmitForm" scope="session" />

<table align="center" class=tabSize>
 <tr>
  <td>
<br><br>

<b><%=q.getConclusion()%><br><br><br>
<span class="etape">
N°: <%=sf.getS_id()%>
<% if (q.showPwd()) out.print(" / mot de passe: "+sf.getPwd()+"<br><br>"); %>
</span></b><br><br>
</td></tr><tr>
  <td>
<br>
<%@ include file='warnings.jsp'%>
    </td>
</tr>
<tr>
    <td>
    <%
    out.println("<a href='"+response.encodeURL(request.getContextPath()+"/ServControl")+"' class='orangeButton'> &gt;&gt; SUITE</a>");
    
    out.println(" &nbsp; ou &nbsp; <a href='ServControl?action=RMAJ&sid="+sf.getS_id()+"&qid="+String.valueOf(sf.getF_id())+"'>Modifier le formulaire</a> ");
    
    if ((sessionInfos != null) && (sessionInfos.getRole()>=SessionInfos.ROLE_GESTION)) { 
    	out.println(" &nbsp; ou &nbsp; <a href='ServControl?action=RDET&sid="+sf.getS_id()+"&qid="+String.valueOf(sf.getF_id())+"'>Consulter le formulaire</a> ");
    	out.println(" &nbsp; ou &nbsp; <a href='ServControl?action=RLIST'>Nouvelle recherche</a> ");
    }
	%></td>
</tr>

</table>

<%@ include file = "/footer.jsp" %>
