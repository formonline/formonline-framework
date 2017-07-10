<%@ include file = "/entete.jsp" %>
<%@ page import = "formOnLine.*" %>
<jsp:useBean id="params" class="java.util.HashMap"
	scope="session" />

<table class=tabSize>
<tr><td>
<strong>Configuration</strong><br><br>

<form action="ServControl" method="post" name="formtab">

<table align="center" >
<tr>
	<td class=title>Clé</td>
	<td class=title>Valeur</td>
</tr>
<%
java.util.SortedSet<String> cles = new java.util.TreeSet<String>(params.keySet());

//java.util.Set cles = params.keySet();
java.util.Iterator it = cles.iterator();
while (it.hasNext()){
   String cle = (String)it.next(); 
   String valeur = (String)params.get(cle); 
   boolean isBoolean = cle.startsWith("is"); %>
<tr>
	<td class=<%=(valeur.equals("true")?"greenText":"greyText")%> align=right>
		<%=(valeur.equals("true")?"<strong>":"")%>
			<%=cle %>
		<%=(valeur.equals("true")?"</strong>":"")%>
	</td>
	<td align=left> 
	<% if (isBoolean) { %>
	<input type=checkbox name="<%=cle%>" <%=(valeur.equals("true")?"checked=checked":"")%>>
	<% } else { %>
	 <input type=text name="<%=cle%>" value="<%=valeur%>">
	 <%} %>
	 </td>

   </tr>
   
<%}%>
<tr>
	<td colspan=2 align=center>
	<input type="submit" value="Enregistrer">
	<input type="hidden" name="action" value="MODTABMAJ">
	</td>
</tr>
	
</table>
</form>

<br>
<br>



<br>
<br>
</td></tr></table>
<%@ include file = "/footer.jsp" %>
