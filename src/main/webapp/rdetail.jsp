<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file = "/entete.jsp" %>
<table align="center" width="100%">
 <tr>
  <td> 

<%@ page import = "formOnLine.msBeans.*" %>  
<jsp:useBean id="q" class="formOnLine.msBeans.Questionnaire" scope="session" />
<jsp:useBean id="sf" class="formOnLine.msBeans.SubmitForm" scope="session" />
<% 
ValueBeanList listTemplate = (ValueBeanList)session.getAttribute(Template.SESSION_LIST);%>
<a name="top"> </a>
<h3><jsp:getProperty name="q" property="titre"/></h3>
<%@ include file='warnings.jsp'%>
<% if (sf.getS_id()>0) { 

out.print("N°: ");
if (sf.getS_id_parent()>0) out.print("<span class='greyText'>" + sf.getS_id_parent() + " / </span>");
out.print("    <span class='mandatoryInput'>"+String.valueOf(sf.getS_id())+"</span> ");
if (sessionInfos != null && (
        (q.showPwd() && sessionInfos.getRole()!=SessionInfos.ROLE_CONSULTATION)
        || sessionInfos.getRole()>=SessionInfos.ROLE_ADMIN  )
    )
	out.print(" (mot de passe: "+String.valueOf(sf.getPwd())+")");
out.print("<input type='hidden' id='sid' value='"+String.valueOf(sf.getS_id())+"'> ");
if (sf.isLocked()) {
    out.print("<span class='greyText'>[Validé]</span>");
} else {
    out.print("<span class='greyText'>[Non Validé]</span>");     
}
out.println(" &nbsp; ");
if ( (!sf.isLocked() && sessionInfos != null && sessionInfos.getRole()!=SessionInfos.ROLE_CONSULTATION)
		|| ((sessionInfos != null) && (sessionInfos.getRole()>=SessionInfos.ROLE_GESTION))) {

    out.print(" <a href='ServControl?action=RMAJ&sid="+sf.getS_id()+"&qid="+String.valueOf(sf.getF_id())+"' class='btlink'>Modifier</a>  ");
}
if ((!sf.isLocked() && q.getF_suppr()==1) || (q.getF_suppr()==1 && sessionInfos.getRole()>=SessionInfos.ROLE_GESTION) || sessionInfos.getRole()>=SessionInfos.ROLE_ADMIN) {
    out.println(" <a href='ServControl?action=RSUP&sid="+sf.getS_id()+"&qid="+String.valueOf(sf.getF_id())+"'  class='btlink'>Supprimer</a>   ");
}

if ((sessionInfos != null) && (sessionInfos.isInRoleRegion() || sessionInfos.getRole()>=SessionInfos.ROLE_GESTION)) {

	if (!sf.isLocked()) {
    out.println(" <a href='ServControl?action=LOCK&sid="+sf.getS_id()+"&qid="+String.valueOf(sf.getF_id())+"'  class='btlink'>Valider</a>  ");
	} else {
		out.println(" <a href='ServControl?action=UNLOCK&sid="+sf.getS_id()+"&qid="+String.valueOf(sf.getF_id())+"'  class='btlink'>Invalider</a>  ");
	} 
}

if (sessionInfos != null && sessionInfos.getRole()>=SessionInfos.ROLE_ADMIN && q.getF_authent()==1) {
	out.println(" <a href='ServControl?action=LOGIN&login="+sf.getS_id()+"&roleQid=0'  class='btlink'>connect as</a>  ");
}

out.println(" <a href='ServControl'>Retour</a><br>");

if ((sessionInfos != null) && (sessionInfos.isInRoleRegion() || sessionInfos.getRole()>=SessionInfos.ROLE_CONSULTATION)) {
	
    out.println(" <br> ");
    
	for (int i=0; listTemplate!=null && i<listTemplate.size() ; i++) {
        Template t = (Template)listTemplate.get(i);
		if (t.getFid() == sf.getF_id() 
		        && !t.getType().equals("CSV")
		        && !t.getType().equals("MAIL")
		        && !t.getType().equals("TXT")
		        && !t.getType().equals("LOT")
		        && !t.getType().equals("SQL")) {
		    out.println(" <a href='ServControl?action=EXPO&sid="+sf.getS_id()+"&selectExport="+t.getType()+"&templateId="+t.getId()+"&fid="+String.valueOf(sf.getF_id())+"&direct' target='_blank' class=fleche_orange>"+formOnLine.BasicTools.cleanFusionMarks(t.getName())+"</a>&nbsp;");
			out.println("<a href='ServControl?action=EXPO&sid="+sf.getS_id()+"&selectExport="+t.getType()+"&templateId="+t.getId()+"&fid="+String.valueOf(sf.getF_id())+"' target='_blank'><span style='color:#75B3C7;font-weight:bold'>W</span></a>  &nbsp; ");
		}
   }
}
%>
<br><br>

<%@ include file='afficheRep.jsp'%>
<% //@ include file='afficheRepTemplate.jsp'%> 


<br>

<% if ( (!sf.isLocked() && sessionInfos != null && sessionInfos.getRole()!=SessionInfos.ROLE_CONSULTATION)
		|| ((sessionInfos != null) && (sessionInfos.getRole()>=SessionInfos.ROLE_GESTION))) {

    out.print("<a href='"+response.encodeURL(request.getContextPath()+"/ServControl?action=RMAJ&sid="+sf.getS_id()+"&qid="+String.valueOf(sf.getF_id()))+"'  class='btlink'>&gt; Modifier</a>   ");
}
out.println(" <a href='"+response.encodeURL(request.getContextPath()+"/ServControl")+"'>Retour</a><br>");
%>
<% } %>
<br>
<br>
</td></tr></table>

<table class="finPage" cellpadding="0" cellspacing="0" >
		<tr><td>&nbsp;</td></tr>
</table>


</body></html>
