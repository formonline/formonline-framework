<%
// alertes champs obligatoires
String msgMandatory =q.checkMandatoryAlert();

if (!msgMandatory.equals("")) { %>
<span class=redText>
<%=msgMandatory%><br>
Merci de compléter votre questionnaire.<br><br></span>
<% }

// contrôles champs incorrects
String msgValues =q.checkValues();

if (!msgValues.equals("")) { %>
<span class=redText><%=msgValues%><br>
Merci de corriger votre formulaire.<br><br></span>
<% }

//contrôles spécifiques elections : totaux
/*
if (FALSE && q.getId()==5) {
    int [] tabGid = {9,9,9,9,9,9,9}; 
    int [] tabQid = {24,24,24,24,24,24,86};
    int [] tabPid = {46,47,48,45,136,137,135};
    
    msgValues =q.checkSumsNotSup(tabGid,tabQid,tabPid,
            8,34,49,
            8,35,50);     
}
       
	
if (!msgValues.equals("")) { %>
<span class=redText><%=msgValues%><br>
Merci de corriger le formulaire.<br><br></span>
<% }
*/


// contrôles spécifiques montants financements ET
/* if (FALSE && q.getId()==2 && msgValues.equals("") && msgMandatory.equals("")) {
   formOnLine.EtTools et = new formOnLine.EtTools(sf);
   String msgMt = et.checkMontants();
   if (!msgMt.equals("")) { %>
<span class=redText>ATTENTION ! Certains montants sont incorrects :<br>
<%=msgMt%><br>
Merci de corriger votre questionnaire.<br><br></span>
<% }
}

// contrôle SIRET
if (FALSE && q.getId()==1 && msgValues.equals("") && msgMandatory.equals("")) {
   formOnLine.EtTools et = new formOnLine.EtTools(sf);
   String msgSiret = Controls.checkSiret(sf.getPropVal(29));
   if (msgSiret!=null && !msgSiret.equals("")) { %>
<span class=redText>ATTENTION ! Certains champs sont incorrects :<br>
<%=msgSiret%><br>
Merci de corriger votre questionnaire.<br><br></span>
<% }
} */
%>

