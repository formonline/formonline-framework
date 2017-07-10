<%@ include file = "/entete.jsp" %>
<%@ page import="formOnLine.msBeans.*" %>
<%@ page import="com.triangle.lightfw.*" %>
<% 
Question q = (Question)session.getAttribute(Question.SESSION_NAME);
boolean bMaj = (q.getId()>0);
boolean bCreat = !bMaj ;

%>
<form name="formulaire" action='<%=response.encodeURL(request.getContextPath())%>/ServControl' method='post'>

<table class=tabSize>
	<tr>
		<td colspan="2" class="etape">QUESTION</td>
	</tr>

	<tr>
		<td>Id</td>
		<td><% if (bMaj) { %>
		<input name="q_id" value="<%=q.getId() %>" size="10" readonly=readonly>
		<% } %></td>
	</tr>
	<tr>
		<td>Groupe Id</td>
		<td>
		<input name="g_id" value="<%=q.getG_id() %>" size="10">
		</td>
	</tr>
	

	<tr>
		<td>Texte</td>
		<td><textarea  name="q_texte"  cols="80" rows="12" class=text
		><%=q.getTexte() %></textarea></td>
	</tr>


	<tr>
		<td>N° </td>
		<td><input name="q_num" value="<%=q.getNum() %>" size="10"></td>
	</tr>


	<tr>
		<td>Taille </td>
		<td><input name="q_size" value="<%=q.getSize() %>" size="10"></td>
	</tr>

	
	<tr>
		<td>Type</td>
		<td><select name="q_type">
		<option value="<%=Question.TEXTE %>" 
			<%=((q.getType().equals(Question.TEXTE))?"selected":"") %>>Texte
		</option>
		<option value="<%=Question.MULTILIGNE %>" 
			<%=((q.getType().equals(Question.MULTILIGNE))?"selected":"") %>>Texte Multiligne
		</option>
		<option value="<%=Question.NOMBRE %>" 
			<%=((q.getType().equals(Question.NOMBRE))?"selected":"") %>>Nombre
		</option>
		<option value="<%=Question.CHOIX_EXCLUSIF %>" 
			<%=((q.getType().equals(Question.CHOIX_EXCLUSIF))?"selected":"") %>>Choix exclusif
		</option>
		<option value="<%=Question.CHOIX_MULTIPLE %>" 
			<%=((q.getType().equals(Question.CHOIX_MULTIPLE))?"selected":"") %>>Choix multiple
		</option>
		<option value="<%=Question.DATE %>" 
			<%=((q.getType().equals(Question.DATE))?"selected":"") %>>Date
		</option>
		<option value="<%=Question.EMAIL %>" 
			<%=((q.getType().equals(Question.EMAIL))?"selected":"") %>>Email
		</option>
		<option value="<%=Question.EURO %>" 
			<%=((q.getType().equals(Question.EURO))?"selected":"") %>>Euro
		</option>
		<option value="<%=Question.READ_ONLY %>" 
			<%=((q.getType().equals(Question.READ_ONLY))?"selected":"") %>>Lecture seule
		</option>
		<option value="<%=Question.SIRET %>" 
			<%=((q.getType().equals(Question.SIRET))?"selected":"") %>>N° Siret
		</option>
		<option value="<%=Question.RIB %>" 
			<%=((q.getType().equals(Question.RIB))?"selected":"") %>>N° RIB
		</option>
		<option value="<%=Question.COMMUNE %>" 
			<%=((q.getType().equals(Question.COMMUNE))?"selected":"") %>>Code Commune IDF
		</option>
		<option value="<%=Question.FORM_ID %>" 
			<%=((q.getType().equals(Question.FORM_ID))?"selected":"") %>>Id de formulaire
		</option>
		<option value="<%=Question.LIST_FORM %>" 
			<%=((q.getType().equals(Question.LIST_FORM))?"selected":"") %>>Liste de formulaires (pnum => pid titre)
		</option>
		<option value="<%=Question.URL %>" 
			<%=((q.getType().equals(Question.URL))?"selected":"") %>>URL
		</option>
		<option value="<%=Question.UPLOAD %>" 
			<%=((q.getType().equals(Question.UPLOAD))?"selected":"") %>>Upload
		</option>
		</select>
		</td>
	</tr>

		<tr>
		<td>Obligatoire ?</td>
		<td><select name="q_mandatory">
		<option value="<%=Question.MANDATORY_NO %>" 
			<%=((q.getMandatory()==(Question.MANDATORY_NO))?"selected":"") %>>
			<%=Question.MANDATORY_TEXT_NO %>
		</option>
		<option value="<%=Question.MANDATORY_ALERT %>" 
			<%=((q.getMandatory()==(Question.MANDATORY_ALERT))?"selected":"") %>>
			<%=Question.MANDATORY_TEXT_ALERT %>
		</option>
		<option value="<%=Question.MANDATORY_YES %>" 
			<%=((q.getMandatory()==(Question.MANDATORY_YES))?"selected":"") %>>
			<%=Question.MANDATORY_TEXT_YES %>
		</option>
		<option value="<%=Question.MANDATORY_UNIQUE %>" 
			<%=((q.getMandatory()==(Question.MANDATORY_UNIQUE))?"selected":"") %>>
			<%=Question.MANDATORY_TEXT_UNIQUE %>
		</option>
		
		</select>
		</td>
	</tr>

	<tr>
		<td>Champ de recherche</td>
		<td><select name="q_search">
		<option value="1" <%=((q.isSearch())?"selected":"") %>>OUI
		<option value="0" <%=((!q.isSearch())?"selected":"") %>>NON
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
		
		<input type="hidden" value="ADMQ" name="action">
		<input type="hidden" value="<%=request.getParameter("f_id") %>" name="f_id">
		
					</td>
	</tr>


</table>

<%@ include file = "/footer.jsp" %>
