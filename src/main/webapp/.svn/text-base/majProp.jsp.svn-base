<%@ include file = "/entete.jsp" %>
<%@ page import="formOnLine.msBeans.*" %>
<%@ page import="com.triangle.lightfw.*" %>
<% 
Proposition p = (Proposition)session.getAttribute(Proposition.SESSION_NAME);
boolean bMaj = (p.getId()>0);
boolean bCreat = !bMaj ;

%>
<form name="formulaire" action='<%=response.encodeURL(request.getContextPath())%>/ServControl' method='post'>

<table class=tabSize>
	<tr>
		<td colspan="2" class="etape">PROPOSITION</td>
	</tr>

	<tr>
		<td>Id</td>
		<td><% if (bMaj) { %>
		<input name="p_id" value="<%=p.getId() %>" size="10" readonly=readonly>
		<% } %></td>
	</tr>
	<tr>
		<td>Question Id</td>
		<td>
		<input name="q_id" value="<%=p.getQid() %>" size="10">
		</td>
	</tr>
	

	<tr>
		<td>Texte</td>
		<td><textarea  name="p_texte"  cols="100" rows="10" class=text
		><%=p.getTexte() %></textarea></td>
	</tr>


	<tr>
		<td>N° </td>
		<td><input name="p_num" value="<%=p.getNum() %>" size="10"></td>
	</tr>
	
	<tr>
		<td>Alerte  </td>
		<td><input name="p_alert" value="<%=((p.getAlert()!=null)?p.getAlert():"") %>" size="40"> 
		<span class=txtGris>exemple de condition : "=1"</span></td>
	</tr>
	
	<tr>
		<td>Stats  </td>
		<td>
		
		<select name="p_stat">
		<option value=0 <%=((p.getStat()==0)?"selected":"") %>> - </option>
		<option value=1 <%=((p.getStat()==1)?"selected":"") %>>Décompte</option>
		<option value=2 <%=((p.getStat()==2)?"selected":"") %>>Somme</option>
		<option value=3 <%=((p.getStat()==3)?"selected":"") %>>Décompte des occurences</option>


		</select>
		
<tr>
		<td>Initialisation</td>
		<td>
		
		<select name="p_initload">
		<option value=0 <%=((p.getInitload()==0)?"selected":"") %>> - </option>
		<option value=1 <%=((p.getInitload()==1)?"selected":"") %>>A l'identification</option>


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
		
		<input type="hidden" value="ADMP" name="action">
		<input type="hidden" value="<%=request.getParameter("f_id") %>" name="f_id">
		
					</td>
	</tr>


</table>

<%@ include file = "/footer.jsp" %>
