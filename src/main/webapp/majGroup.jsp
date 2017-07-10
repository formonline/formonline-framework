<%@ include file = "/entete.jsp" %>
<%@ page import="formOnLine.msBeans.*" %>
<%@ page import="com.triangle.lightfw.*" %>
<% 
Groupe g = (Groupe)session.getAttribute(Groupe.SESSION_NAME);
boolean bMaj = (g.getId()>0);
boolean bCreat = !bMaj ;

%>
<form name="formulaire" action='<%=response.encodeURL(request.getContextPath())%>/ServControl' method='post'>

<table class=tabSize>
	<tr>
		<td colspan="2" class="etape">GROUPE</td>
	</tr>

	<tr>
		<td>Id</td>
		<td><% if (bMaj) { %>
		<input name="g_id" value="<%=g.getId() %>" size="10"  readonly=readonly>
		<% } %></td>
	</tr>
	<tr>
		<td>Form Id</td>
		<td>
		<input name="f_id" value="<%=g.getF_id() %>" size="10">
		</td>
	</tr>
	<tr>
		<td>Titre</td>
		<td><input name="g_titre" value="<%=g.getTitre() %>" size="30"></td>
	</tr>

	<tr>
		<td>Texte</td>
		<td><textarea  name="g_texte"  cols="60" rows="10" class=text
		><%=g.getTexte() %></textarea></td>
	</tr>


	<tr>
		<td>N° </td>
		<td><input name="g_num" value="<%=g.getNum() %>" size="10"></td>
	</tr>

	
	<tr>
		<td>Type</td>
		<td><select name="g_type">
		<option value="<%=Groupe.PUBLIC %>" 
			<%=((g.getType().equals(Groupe.PUBLIC))?"selected":"") %>>Public
			</option>
		<option value="<%=Groupe.PRIVATE %>" 
			<%=((g.getType().equals(Groupe.PRIVATE))?"selected":"") %>>Privé
			</option>
			</select>
		</td>
	</tr>

	

	<tr>
		<td>&nbsp;</td>
		<td><% if (bMaj) { %>
			<input type="submit" value="Modifier" name="bok_maj">

		<% } 
		if (bCreat) { %>
			<input type="submit" value="Créer" name="bok_creat">
		<% } %> 
		
		<input type="hidden" value="ADMG" name="action">
					</td>
	</tr>


</table>

<%@ include file = "/footer.jsp" %>
