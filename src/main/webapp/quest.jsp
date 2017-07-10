<%@ include file = "/entete.jsp" %>
<% boolean bCaptcha = false; %>

<table width="100%"> 
 <tr>
  <td>  

<%@ page import = "formOnLine.msBeans.*,formOnLine.BasicTools" %>
<jsp:useBean id="q" class="formOnLine.msBeans.Questionnaire" scope="session" />
<jsp:useBean id="sf" class="formOnLine.msBeans.SubmitForm" scope="session" />


<% 
//  *****************************************************************
//	action 4 = reponse,
//	action 6 = modif formulaire, 
//	action 8 = modif réponse
//  ***************************************************************** 

int action = sessionInfos.getActionToDo(); 
// contexte de la webapp en dur pour appels ajax dans l'environnement Jahia
String webContext = "formOnLine";    
%>

<h3><jsp:getProperty name="q" property="titre"/></h3> 
<% if (action == 8) { %>
<%@ include file='warnings.jsp'%>
<%} %>

<jsp:getProperty name="q" property="introduction"/>
<% if (action == 6) { // = modif formulaire, %>
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=ADMF&f_id="+q.getId())%>">[maj]</a> (F: <%=q.getId()%>) 
	<a href="#" onclick="javascript:window.open('help2.htm','Aide','menubar=no, status=no, scrollbars=yes, menubar=no,width=500, height=500');">
Aide...</a>
<% } %>	
<br><a name="top"></a>
<form name="formulaire" 
	action='<%=response.encodeURL(request.getContextPath())%>/ServControl' 
	method='post'
	onSubmit="return valideFormulaire(document.forms['formulaire']);"
	enctype="multipart/form-data"> 
	
	
<%
String messageMandatoryJ = (String)request.getAttribute(formOnLine.ServControl.MESSAGE);
String s_id_parent = (String)request.getParameter("s_id_parent");
if (s_id_parent == null) s_id_parent = String.valueOf(sf.getS_id_parent());

String typeInputSidParent = "hidden";
if ((sessionInfos != null) && (sessionInfos.getRole()>=SessionInfos.ROLE_GESTION)) 
    typeInputSidParent = "text";
%>
<br>Parent : <input type='<%=typeInputSidParent %>' name='s_id_parent' id='s_id_parent' value='<%=s_id_parent%>' size=10> 
<%=(s_id_parent !=null && !s_id_parent.equals("-1")?s_id_parent + " / ":"")%> 
<%


if (action == 8 && messageMandatoryJ == null ) {
   out.print(" N°: "+String.valueOf(sf.getS_id())+"</b><br>");
   if (q.showPwd()) out.print("mot de passe: "+String.valueOf(sf.getPwd()));
}
if (action != 4 && sf != null && sf.getS_id()>0 ) {
    out.print("<input type='hidden' name='sid' id='sid' value="+String.valueOf(sf.getS_id())+">");    
}

	java.text.NumberFormat formatter = new java.text.DecimalFormat("00000");
	out.println("<br>");
	
	// ancres
	for (int k=0; k<q.getNbGroupes(); k++) {
		Groupe gk = q.getGroupe(k);
		
	  if (gk.getType().equals(Groupe.PUBLIC) || ((sessionInfos != null) && (sessionInfos.getRole()>SessionInfos.ROLE_GESTION))) {
		%>
		<a href="#g<%=gk.getId() %>" class="anchor">&gt; <%=gk.getNum()%>. <%=gk.getTitre()%></a><br> 
	<% }
	}
	
	for (int k=0; k<q.getNbGroupes(); k++) {
		Groupe gk = q.getGroupe(k);
		
	  if (gk.getType().equals(Groupe.PUBLIC) || ((sessionInfos != null) && (sessionInfos.getRole()>=SessionInfos.ROLE_GESTION))) {
		%>
		<br><br>
		
	<div id=group<%=gk.getId() %>>		
		<div class="etape" >
          <strong><%=gk.getNum() %>.</strong> <%=gk.getTitre() %> <a name="g<%=gk.getId() %>" /> 
          		
		<%if (action == 6) { %>
		<a href="<%="ServControl?action=ADMG&f_id="+q.getId()+"&g_id="+gk.getId()%>">
		[maj]</a> (G:<%=gk.getId()%>) 
		<% }  %>
		
		<a  style='float:right' href='#top'>^</a>
		</div>
		
		<%=gk.getTexte()%>
			
		<%
		for (int i=0; i<gk.getNbQuestions(); i++) {
			Question qi = gk.getQuestion(i);
			out.print("<fieldset class=blocP id=fieldset"+qi.getId()+"><strong>");
			out.println( qi.getTexte() + "</strong>");
			
			if (action == 6) { %>
			<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=ADMQ&f_id="+q.getId()+"&g_id="+gk.getId()+"&q_id="+qi.getId())%>">
			[maj]</a> (Q:<%= qi.getId()%>) n°<%=qi.getNum() %>
			<span class=greyText><i><%=BasicTools.getHtmComment(qi.getTexte()) %></i></span>
			<% }
			
			//if (!qi.getType().equals(Question.READ_ONLY)) 
			out.println("<br>");
			
			for (int j=0; j<qi.getNbPropositions(); j++) {
				Proposition pj = qi.getProposition(j);
				String sVal = BasicTools.encodeQuotes(pj.getsVal());
				
				//if (sVal==null || action==4 || action ==6) sVal="";
				if (sVal==null) sVal="";
				
				// CHOIX_EXCLUSIF en mode réponse
				if (qi.getType().equals(Question.CHOIX_EXCLUSIF)&&(action != 6)) {
					
				    if (j==0) {
					    String inputName = "fol_rag" + formatter.format(gk.getId())
					    	+ "q" + formatter.format(qi.getId()) ;
					    out.print("<label for="+ inputName +">&nbsp;&nbsp;</label>");
						out.print("<select name=");
						out.print( inputName + " id=" +inputName  );
						
						if (qi.getMandatory() < Question.MANDATORY_ALERT) out.print( " class=normalInput ");
						if (qi.getMandatory() == Question.MANDATORY_ALERT) out.print(" class='mandatoryInput' ");
						if (qi.getMandatory() >= Question.MANDATORY_YES) out.print(" class='requestedInput' IdF_Mandatory=\"" + qi.getMandatory() + "\" IdF_Text=\"" + qi.getTexte() + "\"");
						
						
						out.print(BasicTools.getHtmComment(qi.getTexte()));
						out.print(">");
					}
					out.print("<option value=" + formatter.format(pj.getId()));
					if (sVal.equals("1")) out.print(" selected=selected");
					out.println(">"+pj.getTexte()+"</option>");
					
					if (j==(qi.getNbPropositions()-1)) out.print("</select><br>");
				}
				
				// CHOIX_EXCLUSIF en mode modif de formulaire
				if (qi.getType().equals(Question.CHOIX_EXCLUSIF)&&(action == 6)) {
					//radioboutons
					String inputName = "fol_rag" + formatter.format(gk.getId()) 
				    		+ "q" + formatter.format(qi.getId()) + "p" 
				    		+ formatter.format(pj.getId()) ;
					out.print("&nbsp;&nbsp;<input type='radio' name='");
					out.print(inputName + "' value=" + formatter.format(pj.getId()));
					if (sVal.equals("1")) out.print(" checked");
					out.print(">");
					out.println(" <label for=\""+inputName + "\">" 
					        + pj.getTexte() + "</label>" );
				}
				
				// CHOIX_MULTIPLE standard
				if (qi.getType().equals(Question.CHOIX_MULTIPLE) ) {
				    
				    String inputName = "fol_chg" + formatter.format(gk.getId()) 
				    		+ "q" + formatter.format(qi.getId()) + "p" 
				    		+ formatter.format(pj.getId()) ;
					out.print("&nbsp;<input type='checkbox' name='");
					out.print( inputName + "' id=" + inputName );
					if (sVal.equals("1")) out.print(" checked=checked");
					out.print(">");
					out.println(" <label for=\""+inputName + "\">" 
					        + pj.getTexte() + "</label>" );
				}
				
				//	CHOIX_MULTIPLE issu d'une liste de formulaires
				if (qi.getType().equals(Question.LIST_FORM)) {
				    
				    String inputName = "fol_lfg" + formatter.format(gk.getId()) 
				    		+ "q" + formatter.format(qi.getId()) 
				    		+ "p" + formatter.format(pj.getId()) 
							+ "r" + formatter.format(pj.getVal()) ;
					out.print("&nbsp;<input type='checkbox' name='");
					out.print( inputName + "' id=" + inputName );
					
					if (sVal.equals("1")) out.print(" checked=checked");
					out.print(">");
					out.println(" <label for=\""+inputName + "\">" 
					        + pj.getTexte() + "</label>" );
				}
				
				// FORM_ID = liste issue des réponses à un autre type de formulaire
				// traitement dans le readonly pour le moment
				// if (qi.getType().equals(Question.FORM_ID)) {
				    
				    // if (j==0) {
					    // out.print("&nbsp;&nbsp;<select name=");
						// out.print( "fol_rag" + formatter.format(gk.getId())  );
						// out.print( "q" + formatter.format(qi.getId()) +" class=normalInput");
						// out.print(">");
					// }
					// out.print("<option value=" + formatter.format(pj.getId()));
					// if (sVal.equals(String.valueOf(pj.getId()))) out.print(" selected");
					// out.println(">"+pj.getTexte()+"</option>");
					
					// if (j >= (qi.getNbPropositions()-1)) out.print("</select><br>");
									
					
				// }
				
				// TEXTE + EMAIL
				if (qi.getType().equals(Question.TEXTE) || qi.getType().equals(Question.EMAIL)  ) {
				    String inputName = "fol_teg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p" + formatter.format(pj.getId());
				    
				    out.println(" <span class=greyText><label for=\""+ inputName + "\">"+pj.getTexte()+"</label></span>");
					out.print("&nbsp;<input type=text size=50 maxlength="+qi.getSize()+" name=");
					out.print( inputName + " id=" + inputName );
					if (sVal.length()>0) out.print(" value=\""+BasicTools.htmlConvert(sVal)+"\""); //BasicTools.deEncode(
					if (qi.getMandatory() == Question.MANDATORY_ALERT) out.print(" class='mandatoryInput' ");
					if (qi.getMandatory() >= Question.MANDATORY_YES) out.print(" class='requestedInput' IdF_Mandatory=\"" + qi.getMandatory() + "\" IdF_Text=\"" + qi.getTexte() + "\"");
					
					out.print(BasicTools.getHtmComment(pj.getTexte()));
					if (qi.getMandatory() == Question.MANDATORY_UNIQUE) 
					    out.print(" onblur=\"testUnique(this,document.getElementById('" 
					            + inputName 
					            + "_msgu'),"
					            + sf.getS_id()
					            + ")\" ");
					out.print(">");
					if (qi.getMandatory() == Question.MANDATORY_UNIQUE) 
					    out.print("<span name='"+inputName+"_msgu' id='"+inputName+"_msgu' class=errorText ></span>");
				}
				
				// SIRET
				if ( qi.getType().equals(Question.SIRET) ) {
				    String inputName = "fol_teg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p" +formatter.format(pj.getId());
					
				    out.println(" <span class=greyText><label for=\""+ inputName + "\">"+pj.getTexte()+"</label></span>");
					out.print("&nbsp;<input type=text size=50 maxlength="+qi.getSize());
					out.print( " name=" + inputName );
					out.print( " id=" + inputName );
					
					if (sVal.length()>0) out.print(" value=\""+BasicTools.htmlConvert(sVal)+"\""); //BasicTools.deEncode(
					if (qi.getMandatory() != Question.MANDATORY_NO) 
					    out.print(" class='mandatoryInput' IdF_Mandatory=\"" + qi.getMandatory() + "\" IdF_Text=\"" + qi.getTexte() + "\"");
				    
					out.print(" onkeyup=\"testSiret(this,document.getElementById('" 
				            + inputName 
				            + "_msgs'),'/')\" ");
					
					if (qi.getMandatory() == Question.MANDATORY_UNIQUE) {
						
						String input1 = "document.getElementById('"+ inputName +"')";
						String webCall = "'/" + webContext 
		+"/XmlAjaxControl?action=TEST_UNIQUE&value='+"
		+input1+".value+'&input='+"+input1+".name+'&s_id="+sf.getS_id()+"'";
		
						out.print(" onblur=\"testUnique(this,document.getElementById('" 
					            + inputName 
					            + "_msgu'),"
					            + sf.getS_id()
								+ ","
								+ webCall
					            + ")\" ");
					}

					out.print(">");
					
					out.print("<span name='"+inputName+"_msgs' id='"+inputName+"_msgs' class=errorText ></span>");
					if (qi.getMandatory() == Question.MANDATORY_UNIQUE) 
					    out.print("<span name='"+inputName+"_msgu' id='"+inputName+"_msgu' class=errorText ></span>");
				}
				
				
//				 RIB
				if ( qi.getType().equals(Question.RIB) ) {
				    String inputName = "fol_teg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p" +formatter.format(pj.getId());
					
				    out.println(" <span class=greyText><label for=\""+ inputName + "\">"+pj.getTexte()+"</label></span>");
					out.print("&nbsp;<input type=text size=50 maxlength="+qi.getSize());
					out.print( " name=" + inputName );
					out.print( " id=" + inputName );
					
					if (sVal.length()>0) out.print(" value=\""+BasicTools.htmlConvert(sVal)+"\""); //BasicTools.deEncode(
					if (qi.getMandatory() != Question.MANDATORY_NO) 
					    out.print(" class='mandatoryInput' IdF_Mandatory=\"" + qi.getMandatory() + "\" IdF_Text=\"" + qi.getTexte() + "\"");
				    
					out.print(" onkeyup=\"testRib(this,document.getElementById('" 
				            + inputName 
				            + "_msgs'))\" ");
					
					if (qi.getMandatory() == Question.MANDATORY_UNIQUE) {
						
						String input1 = "document.getElementById('"+ inputName +"')";
						String webCall = "'/" + webContext 
		+"/XmlAjaxControl?action=TEST_UNIQUE&value='+"
		+input1+".value+'&input='+"+input1+".name+'&s_id="+sf.getS_id()+"'";
		
						out.print(" onblur=\"testUnique(this,document.getElementById('" 
					            + inputName 
					            + "_msgu'),"
					            + sf.getS_id()
								+ ","
								+ webCall
					            + ")\" ");
					}

					out.print(">");
					
					out.print("<span name='"+inputName+"_msgs' id='"+inputName+"_msgs' class=errorText ></span>");
					if (qi.getMandatory() == Question.MANDATORY_UNIQUE) 
					    out.print("<span name='"+inputName+"_msgu' id='"+inputName+"_msgu' class=errorText ></span>");
					
				}

				
				// COMMUNE
				if (qi.getType().equals(Question.COMMUNE)) {
					out.println(" <span class=greyText>"+pj.getTexte()+"</span>");
					
					String numc = "g" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p" + formatter.format(pj.getId()) ;
										
					// récup libelle commune en AJAX  
					out.print("<select name=\"selDept"+numc+"\" id=\"selDept"+numc+"\"");
					
					String input1 = "document.forms['formulaire'].elements['selDept"
					 + numc + "']";
					String input2 = "document.forms['formulaire'].elements['fol_te"
					 + numc + "']";
					 
					String webCall = "'comidf/comidf'+"
					+ input1+".options["+input1+".selectedIndex].value"
					+ "+'.xml'";
										
					out.print(" onchange=\"getCommunes(");
					out.print(input1+","+input2+","+ webCall);
					out.println(")\"> ");
					
					
					out.println("	<option value=\"x\" selected>Dept...</option>");
					out.println("	<option value=\"75\">75</option><option value=\"77\">77</option><option value=\"78\">78</option><option value=\"91\">91</option><option value=\"92\">92</option><option value=\"93\">93</option><option value=\"94\">94</option><option value=\"95\">95</option>");
					out.println("</select>&nbsp;");
					out.print("<select name=\"fol_te"+numc+"\" id=\"fol_te"+numc+"\" ");

					// init valeur si on est en modif
					if (sVal.length()>0) out.println(" <option value=\""+BasicTools.htmlConvert(sVal)+"\" selected>"+BasicTools.htmlConvert(sVal)+"</option>");

					out.println("></select>&nbsp;");
					
				}
				
				// NOMBRE + DATE + EURO + FILE
				if (qi.getType().equals(Question.NOMBRE) 
				        || qi.getType().equals(Question.DATE) 
				        || qi.getType().equals(Question.EURO)
				        || (qi.getType().equals(Question.FORM_ID)
				                && sessionInfos != null
				                && sessionInfos.getRole()>=SessionInfos.ROLE_GESTION )
				        || (qi.getType().equals(Question.URL)
						        && sessionInfos != null
						        && sessionInfos.getRole()>=SessionInfos.ROLE_GESTION )
						
				) {
					String inputName = "fol_nbg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p" + formatter.format(pj.getId());

				    out.println(" <span class=greyText><label for=\""+ inputName + "\">"+pj.getTexte()+"</label></span>");
					out.print("&nbsp;<input type=text size=20 maxlength="+((qi.getType().equals(Question.DATE))?"10":""+qi.getSize())+" name=");
					out.print( inputName + " id=" + inputName);
					//if (qi.getType().equals(Question.EURO) && sVal.length()==0) sVal="0";
					if (sVal.length()>0) out.print(" value='"+sVal+"'");
					if (qi.getType().equals(Question.EURO)) out.print(" style='text-align:right;' ");
					if (qi.getMandatory() != Question.MANDATORY_NO) out.print(" class='mandatoryInput' IdF_Mandatory=\"" + qi.getMandatory() + "\" IdF_Text=\"" + qi.getTexte() + "\"");
					out.print(BasicTools.getHtmComment(pj.getTexte()));
					if (qi.getMandatory() == Question.MANDATORY_UNIQUE) 
					    out.print(" onblur=\"testUnique(this,document.getElementById('" 
					            + inputName 
					            + "_msgu'),"
					            + sf.getS_id()
					            + ")\" ");
					if (qi.getMandatory() != Question.MANDATORY_UNIQUE
					        && qi.getType().equals(Question.EURO)) 
					    out.print(" onchange=\"nbStrip(this)\" ");
					out.print(">");
					
					if (qi.getType().equals(Question.EURO)) out.print(" &euro; ");
					if (qi.getMandatory() == Question.MANDATORY_UNIQUE) 
					    out.print("<span name='"+inputName+"_msgu' id='"+inputName+"_msgu' class=errorText ></span>");
					
				}
				
				//	UPLOAD
				if (qi.getType().equals(Question.UPLOAD) ) {
				    String inputName = "fol_upg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p" + formatter.format(pj.getId());
				    
				    out.println(" <span class=greyText><label for=\""+ inputName + "\">"+pj.getTexte()+"</label></span>");
					
				    if (sVal.length()>0) {
				        out.print(" value=\""+BasicTools.htmlConvert(sVal)+"\""); 
						out.println("<br>");
				    } 
				        
				    out.print("Nouveau : <input type=file size=50 maxlength="+qi.getSize()+" name=");
					out.print( inputName + " id=" + inputName );
				    
				    if (qi.getMandatory() == Question.MANDATORY_ALERT) out.print(" class='mandatoryInput' ");
					if (qi.getMandatory() >= Question.MANDATORY_YES) out.print(" class='requestedInput' IdF_Mandatory=\"" + qi.getMandatory() + "\" IdF_Text=\"" + qi.getTexte() + "\"");
					
					out.print(BasicTools.getHtmComment(pj.getTexte()));
					out.print(">");
					// pas de controle mandatory unique
				}
				
				
				// MULTILIGNE
				if (qi.getType().equals(Question.MULTILIGNE)) {
				    String inputName = "fol_mlg" + formatter.format(gk.getId()) + "q" 
				    		+ formatter.format(qi.getId()) + "p" 
				    		+ formatter.format(pj.getId()) ;
					
				    
				    out.println(" <span class=greyText><label for=\""+ inputName + "\">"+pj.getTexte()+"</label></span>");
				    out.print("&nbsp;<textarea  rows="+qi.getSize()+" name=");
					out.print(inputName + " id=" + inputName + " ");
					out.print(BasicTools.getHtmComment(pj.getTexte()));
				    if (qi.getMandatory() != Question.MANDATORY_NO) out.print(" class='mandatoryInput' IdF_Mandatory=\"" + qi.getMandatory() + "\" IdF_Text=\"" + qi.getTexte() + "\"");
					out.print(">");
					if (sVal.length()>0) out.print(BasicTools.deEncode(sVal));
					out.print("</textarea>");
				}
				
				// READ_ONLY  (+ FORM_ID  si profil < gestion)
				if (qi.getType().equals(Question.READ_ONLY)
				        || (qi.getType().equals(Question.FORM_ID)
				                && (sessionInfos == null
						                || sessionInfos.getRole()<SessionInfos.ROLE_GESTION ))
						
				    ) {
				    
				    
					out.println(" <span class=greyText>"+pj.getTexte()+"</span>");
					String inputName ="fol_teg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) 
						+ "p" + formatter.format(pj.getId()) ;
					out.print("&nbsp;<input type=hidden id=");
					out.print(inputName);
					out.print(" name=");
					out.print(inputName);
					if (sVal!=null ) out.print(" value=\""+sVal+"\" "); //out.print(" value=\""+BasicTools.htmlConvert(sVal)+"\" ");
					out.print(BasicTools.getHtmComment(pj.getTexte()));
					out.print(">");
					
				 	out.print("<span id=span_"+ inputName+" class=readOnly>");
					out.print(BasicTools.htmlConvert(sVal));
					out.print("</span>");
					
						
					
				}
				if (action == 6 ) { //&& !qi.getType().equals(Question.FORM_ID)) { %>
				<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=ADMP&f_id="+q.getId()+"&g_id="+gk.getId()+"&q_id="+qi.getId()+"&p_id="+pj.getId())%>">
				[maj]</a> (P:<%= pj.getId()%>) n°<%=pj.getNum() %>
							<span class=greyText><i><%=BasicTools.getHtmComment(pj.getTexte()) %></i></span>
				<% }
				
				// retour à la ligne 
				// sauf si on est en train d'afficher les items d'une liste de sélection
				
				if ( !(qi.getType().equals(Question.CHOIX_EXCLUSIF) 
				        && (action != 6))
				        && (j < (qi.getNbPropositions()-1)) 
				        ) { //&& !qi.getType().equals(Question.FORM_ID))) {
					out.print("<br>");
				}
			} // for proposition

			if (action == 6 ) { //&& !qi.getType().equals(Question.FORM_ID)) { // = modif formulaire %>
			<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=ADMP&f_id="+q.getId()+"&g_id="+gk.getId()+"&q_id="+qi.getId())%>">
			[Nouvelle proposition Q<%= qi.getId()%>]</a><br>
			<% }
			
			out.println("</fieldset>");
		} //for question
		
		
		
		if (action == 6) { // = modif formulaire %>
		<br><br><a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=ADMQ&f_id="+q.getId()+"&g_id="+gk.getId())%>">
		[Nouvelle Question G<%= gk.getId()%>]</a>
		<% }
		
		out.println("</div>");
		
		
	  } else {
	  // Modification de la réponse (action 8) :
	  // on génère des HIDDENFIELD pour conserver les données modifiées réservées ADMIN RIF
	   if (action==8) {
	   
	    
		for (int i=0; i<gk.getNbQuestions(); i++) {
		  Question qi = gk.getQuestion(i);
		  
			for (int j=0; j<qi.getNbPropositions(); j++) {
				Proposition pj = qi.getProposition(j);
				String sVal = BasicTools.encodeQuotes(pj.getsVal());
				
				if (sVal==null) sVal="";
				if (qi.getType().equals(Question.CHOIX_EXCLUSIF)
					&& (sVal.equals("1"))) {
						out.print( "<input type=\"hidden\" name=\"");
						out.print( "fol_rag" + formatter.format(gk.getId())  );
						out.print( "q" + formatter.format(qi.getId()));
						out.println( "\" value=\"" + pj.getId() + "\">");
				}
				if (qi.getType().equals(Question.CHOIX_MULTIPLE)
					&& (sVal.equals("1"))) {
					    out.print( "<input type=\"hidden\" name=\"");
						out.print( "fol_chg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p");
						out.print( formatter.format(pj.getId()) );
						out.println( "\" value=\"1\">");				
				}
				if ( !(sVal.equals("")) 
					&& (qi.getType().equals(Question.TEXTE) 
						|| qi.getType().equals(Question.EMAIL) 
						|| qi.getType().equals(Question.SIRET)
						|| qi.getType().equals(Question.READ_ONLY)
						|| qi.getType().equals(Question.FORM_ID)
						|| qi.getType().equals(Question.URL) )) {
						
						out.print( "<input type=\"hidden\" name=\"");
						out.print("fol_teg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p");
						out.print(formatter.format(pj.getId()) );
						out.println("\" value=\""+sVal+"\">"); 
					
				}
				if ( !(sVal.equals("")) 
					&& (qi.getType().equals(Question.NOMBRE) 
						|| qi.getType().equals(Question.DATE) 
						|| qi.getType().equals(Question.EURO))) {
												
						out.print( "<input type=\"hidden\" name=\"");
						out.print("fol_nbg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p");
						out.print(formatter.format(pj.getId()) );
						out.println("\" value=\""+sVal+"\">"); 
				}
				if ( !(sVal.equals("")) 
					&& (qi.getType().equals(Question.MULTILIGNE))) {
					
					out.print( "<input type=\"hidden\" name=\"");
					out.print( "fol_mlg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p");
					out.print( formatter.format(pj.getId()) );
					out.println("\" value=\""+sVal+"\">"); 
				}
			
			
		   }	
		}  	
	   }
	  } //if public
	} //for group
	%>
	
<input type='hidden' name='f_id' id='f_id' value='<%=q.getId()%>'>
	
<% if (action == 6) { %>
	<tr><td class="tdGris"><br>
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=ADMG&f_id="+q.getId())%>">[Nouveau Groupe]</a> 
	</td></tr>
<% } %>	
<%
// captcha si nouveau formulaire
if (q != null 
        && q.getT_id_mail_on_create()>0 
        && q.getS_id() <= 0
        && bCaptcha) { %>
<br>Merci de recopier les chiffres et lettres présentes dans l'image (en respectant les majuscules) :<br>
<img src="Captcha"> <br><input name="captcha" width="10"> <br><br>

<%}

if (action == 4 
        && q!=null && q.getNbGroupes()>0) { %>
    <br><input type='submit' name='bok' id='bok'  value='ENREGISTRER' >
	<input type='hidden' name='action' value='REP'>
	
	
<% }
if (action == 8 
        && q!=null  && q.getNbGroupes()>0) {
    		out.println("<br><input type='submit' name='bok' id='bok' value='ENREGISTRER' >" +
   			 "<input type='hidden' name='action' value='REP'>");

			out.println(" &nbsp; <a href='ServControl?action=LOGIN'>Annuler</a>");
}

// passage des valeurs init non publiques (réponse non validée, profil public)
if (action == 4 && !(sessionInfos != null && sessionInfos.isInRoleRegion())) {
	java.util.Enumeration e = request.getParameterNames();
    while (e.hasMoreElements()) {
          String sParam = e.nextElement().toString();
          
		  if (sParam.length()>5 && sParam.startsWith("H_fol")) {
		  	   String sVal = request.getParameter(sParam);
		  	   sParam =  sParam.substring(2); 
    	       out.println("<input type='hidden' name='"+sParam+"' value='"+sVal+"'>");
    	       //out.println("<br><br>paraAAm : "+sParam +"="+sVal);
    	  }
    	       
    }
}


%>
</form>
<%
  //String messageMandatoryJ = (String)request.getAttribute(formOnLine.ServControl.MESSAGE);
  if (messageMandatoryJ != null)
  {
    messageMandatoryJ = BasicType.quoteQuote(messageMandatoryJ); %>
    <input type="hidden" value="<%=messageMandatoryJ%>" 
    id="messageMandatoryJs" name="messageMandatoryJs">
    <script>
    var messageMandatoryJs = document.getElementById('messageMandatoryJs').value;
	alert(messageMandatoryJs);
    </script><%
  }
%>
</td></tr></table>


<table class=tabSize><tr><td>
<p class="centremoi"><a href="#top">Retour haut page</a>
<br>


<script>
<!-- tests -->
</script>

<br>


<% 	
if (action == 6 && sessionInfos.getRole()>SessionInfos.ROLE_GESTION) {
String urlreq =   (request.getAttribute("javax.servlet.forward.request_uri")).toString();
String prmtr = (String) request.getAttribute("javax.servlet.forward.query_string");
%>
<a href="<%=urlreq+"?"+prmtr+"&csv"%>">to csv</a> <a href="<%=urlreq+"?"+prmtr+"&sql"%>">to sql</a>  
<br>
<br>
<%
}
if (action == 6 && request.getParameter("sql")!= null) { 
		out.println(q.getSqlView());
	} 
	if (action == 6 && request.getParameter("csv")!= null) { 
	    StringBuffer sb = new StringBuffer(q.toBalises());
	    
	    out.println(formOnLine.FormatExport.getHeader(sb));
	    out.println("<br>");
	    out.println(q.toBalises());
	} %>

<!-- MAITIEN DE LA SESSION -->
<script language="JavaScript">
<!--
function keepSessionAlive() {
	var imag = new Image();
	imag.src = "notimeout.jsp?param=" + Math.random();
	setTimeout('keepSessionAlive()',900000); // refresh 15 min
}
keepSessionAlive()
-->
</script>
<!-- FIN MAINTIEN SESSION -->

</td></tr>
</table>
<%@ include file = "/footer.jsp" %>