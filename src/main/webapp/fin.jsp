
<%@ include file = "/entete.jsp" %>
<table class="tabSize">
<tr><td class="text" align=center><br>
<BR><BR>
<BR><BR>
<BR><BR>
Vous êtes déconnecté<a href='<%=response.encodeURL(request.getContextPath()+"/ServControl?admin") %>' 
class="text" style="text-decoration: none;color: #000000;">.</a><BR>
<BR>

<BR></td></tr>
<tr><td align=center><a href="<%=response.encodeURL(request.getContextPath()+"/ServControl")%>">&gt; Retour</a>
<BR><BR>
<BR><BR>
<BR><BR>
<BR><BR>
</td></tr></table>
<%@ include file = "/footer.jsp" %>
