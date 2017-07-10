<%@ include file="/entete.jsp"%>

<jsp:useBean id="userdata" 	class="formOnLine.msBeans.UserData" 		scope="session" /> 
<jsp:useBean id="stats" 	class="com.triangle.lightfw.ValueBeanList" 	scope="session" />
<jsp:useBean id="params" 	class="java.util.HashMap" 					scope="session" />
	
<% 
com.triangle.lightfw.ValueBeanList listRep2= userdata.getListRep();
formOnLine.msBeans.SubmitForm rep1= userdata.getRep(userdata.getId());

//gestion des tests en mode pilote
boolean isTest = false || (new java.lang.Boolean((String)params.get("isTest"))).booleanValue() ; // Test pilotes...
String listeTest = "";
listeTest = (String)params.get("listeTest");
String tabTest = "";
tabTest = (String)params.get("tabTest"); 
boolean testId = false;
if (rep1!=null) { 
    if (listeTest.indexOf(","+rep1.getS_id()+",")>0) testId = true;
}


boolean isEspacePerso = false || (new java.lang.Boolean((String)params.get("isEspacePerso"))).booleanValue() 
|| ( tabTest!=null && tabTest.contains("EP") && isTest && testId ) ;  // EP





// gestion du paramètrage onglets
String currentTab = null;
currentTab = (String)params.get("defaultTab");
if (currentTab == null) {
	if (isEspacePerso) currentTab = "EP";
}
String currentContext = "";
%>


<!-- ******************* Bloc  identification *************** -->
<% if (rep1==null) { %>
<table width="100%" id=blocNonIdent>

	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>

	<tr>
		<td width="3%" class="border1">&nbsp;</td>
		<td width="97%" class="etape1">Identification </td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="3%" class="">&nbsp;</td>
		<td>
		<form name=form1
			action='<%=response.encodeURL(request.getContextPath()+"/ServControl") %>'
			method='post'>
		<table width="100%" border="0" cellpadding="0" cellspacing="10"
			class="text">
			<tr>


				<td valign=top><br>
				Merci de vous authentifier ci-dessous.<br>
			

				<table width="100%" border="0" cellpadding="3" cellspacing="0"
					class="text">
					<tr>
						<td align="right">Identifiant :</td>
						<td><input name='login' size="10"></td>
					</tr>
					<tr>
						<td align="right">Mot de passe :</td>
						<td><input name='pwd' type=password size="10"></td>
					</tr>
					<tr>
						<td align="center">&nbsp;</td>
						<td><input type='submit' name='bok' value=' OK '></td>
					</tr>
					<tr>
						<td align="right" colspan=2>
						<input type='hidden' name='action' value='LOGIN'> 
						<input name="loginPid" type="hidden" value="32"> 
						<input name="roleQid" type="hidden" value="32">
						<input name="ldapSource" type="hidden" value="AD">
						
						
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</form>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
</table>
<% } else { 
    
    if (request.getParameter("currentTab")!= null)
        session.setAttribute("currentTab", request.getParameter("currentTab")); 
			    
    if ( session != null && session.getAttribute("currentTab")!=null) 
			        currentTab = (String)session.getAttribute("currentTab");	

    if ( session != null && session.getAttribute("currentContext")!=null) 
        currentContext = (String)session.getAttribute("currentContext");	
    
    
    // ******************* AFFICHAGE identifié *******************     
%><table width="100%" id=blocIdent>
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="3%" class="border1">&nbsp;</td>
		<td class="etape1">Données Utilisateur</td>
		<td rowspan=3><%	// affichage messages	 style="background-color: #F5F5F5;" 
	for (int i=0; i<listRep2.size(); i++) {
		SubmitForm sfi = (SubmitForm )listRep2.get(i);
		if (sfi.getF_id()!=4  ) continue;
		%>
		<div class=greyBox id="msg<%=i%>"><%=sfi.getTitre() %></div>
		<%   } %></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="3%" class="">&nbsp;</td>
		<td align="left"><%=rep1.getTitre() + " (id: "+rep1.getS_id()+")"%> <%  if (rep1.isLocked()) { %>
		<a
			href='<%=response.encodeURL(request.getContextPath()+"/ServControl?action=RDET&qid="+rep1.getF_id()+"&sid="+rep1.getS_id())%>'>Afficher</a>
		<%  } else { %> <a
			href='<%=response.encodeURL(request.getContextPath()+"/ServControl?action=RMAJ&qid="+rep1.getF_id()+"&sid="+rep1.getS_id())%>'>Afficher / Modifier</a>
		<%  }   %> <br>
			
		</td>

	</tr>
</table>



<!--  ONGLETS  ------------------------ -->
<table width="100%" id=blocOnglet>
	<tr>
		<td colspan="2">
		<ul id="tabnav"  class=tabnav>
			<% 
     if (isEspacePerso) { 
	     	out.print("<li style='margin-right:20px' ");
	     	if (currentTab.equals("EP")) out.print(" class='active'");
	     	out.println(" id=#tabEP><a href='");
	     	if (currentTab.equals("EP")) { out.println("#"); } 
	     	else { out.println("ServControl?currentTab=EP"); }
		    out.println("'>Espace personnel</a></li>");
     }

%>
		</ul>
		</td>
	</tr>
	<!--  ONGLETS  ------------------------ -->

</table>
<table width="100%" >

<%
//  ************ Espace personnel  ***************************
if (isEspacePerso && currentTab.equals("EP")) { %>
	<%@ include file="/tab_EP.jsp"%>
<% } %>

</table>


<% // ************************************
   // LOGIN + MOT DE PASSE si accès direct  
   // ************************************
   
if ( sessionInfos != null 
        && !sessionInfos.isInJahia() 
        && sessionInfos.getRole()==0 
        && rep1 == null
        && request.getParameter("admin")!=null ) 
{ %>
<table class=tabSize id=blocAdminIdent>
	<tr>
		<td colspan=2 align="center"><br>
		<br>
		<br>
		<br>
		<br>

		<form name=form2
			action='<%=response.encodeURL(request.getContextPath()+"/ServControl") %>'
			method='post'>
		<table border="0" align="center" cellpadding="6" cellspacing="0">
			<tr>
				<td colspan="2" align="center"  class="etape1">
				Connexion ADMIN</td>
			</tr>
			<tr bgcolor="#F2F2DA">
				<td align="right" class="text">Nom :</td>
				<td><input name="adm_login" size="15"></td>
			</tr>
			<tr bgcolor="#F2F2DA">
				<td align="right" class="text">Mot de passe :</td>
				<td><input name="adm_pwd" size="15" type="password"></td>
			</tr>
			<tr bgcolor="#F2F2DA">
				<td align="right" class="text">&nbsp;</td>
				<td align="left" class="text"><input name="ok" type="submit"
					value="OK"> <input name="loginPid" type="hidden" value="1723"> <input
					name="roleQid" type="hidden" value="32"> <input name="ldapSource"
					type="hidden" value="novell"> <!--  input type='hidden' name='action' value='LOGIN' -->

				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>


<% } // fin if isinjahia %>
<%@ include file="/footer.jsp"%>
