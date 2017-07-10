<%@ include file = "/entete.jsp" %>
<%@ page import="formOnLine.msBeans.*" %>
<%@ page import="com.triangle.lightfw.*" %>
<% 
Questionnaire q = (Questionnaire)session.getAttribute(Questionnaire.SESSION_NAME);
boolean bMaj = (q.getId()>0);
boolean bCreat = !bMaj ;

%>
<form name="formulaire" action='<%=response.encodeURL(request.getContextPath())%>/ServControl' method='post'>

<table class=tabSize>
	<tr>
		<td colspan="2" class="etape">QUESTIONNAIRE</td>
	</tr>

	<tr>
		<td valign=top>Id</td>
		<td><% if (bMaj) { %>
		<input name="f_id" value="<%=q.getId() %>" size="10"  readonly=readonly>
		<% } %></td>
	</tr>

	<tr>
		<td>Titre</td>
		<td><input name="f_titre" value="<%=q.getTitre() %>" size="50"></td>
	</tr>

	<!--  tr>
		<td>Auteur</td>
		<td><input name="f_auteur" value="<%//=q.getAuteur() %>" size="30"></td>
	</tr -->
	
	<tr>
		<td>Introduction</td>
		<td><textarea  name="f_introduction"  cols="100" rows="15" class=text
		><%=q.getIntroduction() %></textarea></td>
	</tr>



	<tr>
		<td>Conclusion</td>
		<td><textarea name="f_conclusion" cols="100" rows="15" class=text
		><%= q.getConclusion() %></textarea>
		<br><span class=greyText>
		Texte affiché après l'enregistrement du formulaire.
		</span></td>
	</tr>

	<tr>
		<td>Mail admin</td>
		<td><input name="f_mail_adm" value="<%=q.getF_mail_adm() %>" size="30">
		<br><span class=greyText>
		Adresse mail utilisée en copie  lorsqu'il faut envoyer un mail  
		à la validation du formulaire.<br>
		Ce champ est également utilisé comme adresse d'expéditeur ("FROM") dans le cas d'un mailing global.
		</span>
		</td>
	</tr>

	<tr>
		<td>N° Proposition d'intitulé</td>
		<td><input name="p_id_titre" value="<%=q.getP_id_titre() %>" size="10">
		<br><span class=greyText>
		Numéro de proposition dont la valeur est utilisée pour représenter
		le formulaire lorsqu'il est présenté dans une liste.
		</span></td>
	</tr>

	<tr>
		<td>N° Proposition d'adress email</td>
		<td><input name="p_id_mail" value="<%=q.getP_id_mail() %>" size="10">
		<br><span class=greyText>
		Numéro de proposition du formulaire dont la valeur doit être utilisée comme
		adresse mail lorsqu'il faut envoyer un mail à la création ou 
		à la validation du formulaire. Il est possible de saisir un numéro de proposition du formulaire directement parent (via le S_id_parent).
			Ce champ est également utilisé comme adresse destinataire ("TO") dans le cas d'un mailing global.
		</span></td>
	</tr>

	<tr>
		<td>N° Template de mail de création</td>
		<td><input name="t_id_mail_on_create" value="<%=q.getT_id_mail_on_create() %>" size="10">
		<br><span class=greyText>
		Si un numéro de template (type mail) est renseigné, un mail sera envoyé
		à la première création du formulaire à l'adresse e-mail contenue dans 
		la proposition n° P_ID_MAIL (ci-dessus). Exemple : envoi des identifiants.
		</span></td>
	</tr>

	<tr>
		<td>N° Template de mail de validation</td>
		<td><input name="t_id_mail_on_lock" value="<%=q.getT_id_mail_on_lock() %>" size="10">
		<br><span class=greyText>
		Si un numéro de template (type mail) est renseigné, un mail sera envoyé
		à la validation du formulaire à l'adresse e-mail contenue dans 
		la proposition n° P_ID_MAIL (ci-dessus), et mis en copie à l'adresse MAIL_ADMIN (ci-dessus)
		</span>
		</td>
	</tr>
	
	<tr>
		<td>N° Template de mail de suppression</td>
		<td><input name="t_id_mail_on_delete" value="<%=q.getT_id_mail_on_delete() %>" size="10">
		<br><span class=greyText>
		Si un numéro de template (type mail) est renseigné, un mail sera envoyé
		à la suppression du formulaire à l'adresse e-mail contenue dans 
		la proposition n° P_ID_MAIL (ci-dessus), et mis en copie à l'adresse MAIL_ADMIN (ci-dessus)
		</span>
		</td>
	</tr>
	
	<tr>
		<td>Date de début</td>
		<td><input name="f_date_debut" value="<%=BasicType.formatDateIsoToLocal(q.getDate_debut(),null) %>" size="30"></td>
	</tr>

	<tr>
		<td>Date de fin</td>
		<td><input name="f_date_fin" value="<%=BasicType.formatDateIsoToLocal(q.getDate_fin(),null) %>" size="30">
		<br><span class=greyText>
		Les dates conditionnent l'accès aux formulaires
		</span>
		</td>
	</tr>
	
	<tr>
		<td>Mot de passe apparent</td>
		<td><select name="f_show_pwd">
		<option value="1" <%=((q.showPwd())?"selected":"") %>>OUI</option>
		<option value="0" <%=((!q.showPwd())?"selected":"") %>>NON</option>
		</select>
		<br><span class=greyText>
		Si oui, le mot de passe attribué au formulaire est visible pour ceux 
		qui ont les droits d'accès à ce formulaire
		</span></td>
	</tr>
	
		<tr>
		<td>Suppression possible ?</td>
		<td><select name="f_suppr">
		<option value="1" <%=((q.getF_suppr()==1)?"selected":"") %>>OUI</option>
		<option value="0" <%=((q.getF_suppr()==0)?"selected":"") %>>NON</option>
		</select>
		
		<br><span class=greyText>
		Si oui, le lien "supprimer" apparaît si le formulaire n'est pas locké
		</span>
		</td>
	</tr>
	
		</tr>
	
		<tr>
		<td>Formulaire d'authentification ? </td>
		<td><select name="f_authent">
		<option value="1" <%=((q.getF_authent()==1)?"selected":"") %>>OUI</option>
		<option value="0" <%=((q.getF_authent()==0)?"selected":"") %>>NON</option>
		</select>
		
		<br><span class=greyText>
		Si oui, le formulaire permet l'authentification (Id ou login + mot de passe du SF)
		</span>
		</td>
	</tr>
	
	<tr>
		<td>N° proposition pour le login</td>
		<td><%=q.getP_id_login() %>
		<br><span class=greyText>
		Si un numéro d'id de proposition est renseigné, la valeur de la réponse correspondant
		à cette proposition pourra être utilisée comme LOGIN. (A mettre à jour directement en base)
		</span>
		</td>
	</tr>
	
	<tr>
		<td>accès</td>
		<td><select name="f_connected">
		<option value="0" <%=((q.getF_connected()==0)?"selected":"") %>>public</option>
		<option value="1" <%=((q.getF_connected()==1)?"selected":"") %>>connecté</option>
		<option value="2" <%=((q.getF_connected()==2)?"selected":"") %>>restreint</option>
		<option value="3" <%=((q.getF_connected()==3)?"selected":"") %>>admin</option>
		</select>
		
		<br><span class=greyText>
		Option Public : le formulaire est accessible en création sans restriction<br>
		Option connecté : le formulaire est un formulaire fils qui demande 
		au préalable une authentification par un formulaire père<br>
		Option restreint : le formulaire n'est accessible qu'en mode connecté ou ADMIN<br>
		Option admin : le formulaire n'est accessible qu'en ADmin
		
		</span></td>
	</tr>

	<tr>
		<td>Date de création</td>
		<td><%=BasicType.formatDateIsoToLocal(q.getDate_creation(),null) %></td>
	</tr>

	<tr>
		<td>&nbsp;</td>
		<td><% if (bMaj) { %>
			<input type="submit" value="Modifier" name="bok_maj">

		<% } 
		if (bCreat) { %>
			<input type="submit" value="Créer" name="bok_creat">
		<% } %> 
		
		<input type="hidden" value="ADMF" name="action">
					</td>
	</tr>


</table>

<%@ include file = "/footer.jsp" %>
