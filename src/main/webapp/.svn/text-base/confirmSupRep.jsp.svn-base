<%@ include file = "/entete.jsp" %>
<!-- jsp:useBean id="sf" class="formOnLine.msBeans.SubmitForm" scope="session" /-->
<table class=tabSize>
<tr><td class='errorText' align="center"><br><br><strong>Demande de confirmation</strong></td></tr>
<tr><td>
<form action='ServControl' method='post'>
        <table width="100%"  border="0" cellpadding="3" cellspacing="0" class="text">
          <tr>
            <td align="center">Confirmez vous la suppression du formulaire [<%=request.getParameter("sid")%>] ?<br>
	    (Attention : toute suppression est définitive)</td>
          </tr>
          <tr>
            <td align="center"><br><input type='submit' name='bok' value='SUPPRIMER'>&nbsp;&nbsp;<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl")%>">[Annuler]</a>
	    <input type='hidden' name='action' value='RDEL'>
	    <input type='hidden' name='sid' value='<%=request.getParameter("sid")%>'>
	    
	    <input type='hidden' name='qid' value='<%=request.getParameter("qid")%>'>
	    </td>
          </tr>
        </table>
</form>
</td></tr>
<tr><td>
</td></tr></table>
<%@ include file = "/footer.jsp" %>
