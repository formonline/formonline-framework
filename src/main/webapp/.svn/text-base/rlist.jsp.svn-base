<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file = "/entete.jsp" %>
<%@ page import = "formOnLine.msBeans.*" %>
<jsp:useBean id="rlist" class="com.triangle.lightfw.ValueBeanList" scope="request" />
<jsp:useBean id="alert" class="com.triangle.lightfw.ValueBeanList" scope="request" />
<jsp:useBean id="searchQ1options" class="com.triangle.lightfw.ValueBeanList" scope="session" />
<jsp:useBean id="searchQ2options" class="com.triangle.lightfw.ValueBeanList" scope="session" />
<jsp:useBean id="findQ1options" class="com.triangle.lightfw.ValueBeanList" scope="session" />
<jsp:useBean id="findQ2options" class="com.triangle.lightfw.ValueBeanList" scope="session" />
<jsp:useBean id="userdata" class="formOnLine.msBeans.UserData" scope="session" />
<% java.text.NumberFormat formatter = new java.text.DecimalFormat("00000"); %>

<SCRIPT language="JavaScript">
function submitForm() {
  document.form1.submit();
}
</SCRIPT>
<table class=tabSize > 
<tr><td>

<table align="center" class="text" cellpadding=10 >
<tr><td class="greyBox" nowrap>
<form name="form1" method="post" action="<%=response.encodeURL(request.getContextPath() + "/ServControl?action=RLIST")%>">
    Numéro formulaire :
    <input name=numdos type="text" value="" size="10" maxlength="10" class=normalInput>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
    <input name="chkparents" type="checkbox" id="idchkp"> 
    <label for="idchkp">Rechercher les formulaires associés</label>
    <br>
    Formulaires enregistrés du :  
    <input name="date_debut" type="text" value="" size="12" maxlength="10" class=normalInput>
     au 
    <input name="date_fin" type="text" value="" size="12" maxlength="10" class=normalInput>
    (inclus) <br>
    Champs complémentaires de recherche : <br>

<% // LISTBOX Q1 --------------------------------------------- %>
    <select name='searchQ1' onchange="javascript:submitForm()" class=normalInput>
<%  String txt;
	for (int j=0; j<searchQ1options.size(); j++) {
       Question q = (Question)searchQ1options.get(j);
       out.print("<option value="+String.valueOf(j));
       if (q.isSelected()) out.print(" selected");
       out.println(">");
       txt=q.getTexte();
       if (txt.indexOf("<!--") > 0 ) txt = txt.substring(0,txt.indexOf("<!--"));
       if (txt.length() <= 70) {
       		out.println(txt);
       	} else {
       		out.println(txt.substring(0,70) + "...");
       	}
       	out.println("</option>");
   }		
%>
    </select>&nbsp;=&nbsp;<%
// LISTBOX reponses Q1 ------------------------------------------- -- 
 if (!(findQ1options != null && findQ1options.size()>1)) { 
%><input name='findQ1' type="text" value="" size="20" class=normalInput>
<% } else {
   out.print("<select name='findQ1' class=normalInput>");
   for (int j=0; j<findQ1options.size(); j++) {
       Proposition p = (Proposition)findQ1options.get(j);
       out.print("<option value="+String.valueOf(j));
       if (p.isSelected()) out.print(" selected");
       out.println(">"+p.getTexte()+"</option>");
   }		
   out.print("</select>");
 } %>
     <br> (ET) <br>

<% // LISTBOX Q2 ------------------------------------------- -- %>
    <select name='searchQ2' onchange="javascript:submitForm()" class=normalInput>
<%  for (int j=0; j<searchQ2options.size(); j++) {
       Question q = (Question)searchQ2options.get(j);
       out.print("<option value="+String.valueOf(j));
       if (q.isSelected()) out.print(" selected");
       out.println(">");
       txt=q.getTexte();
       if (txt.indexOf("<!--") > 0 ) txt = txt.substring(0,txt.indexOf("<!--"));
       if (txt.length() <= 70) {
       		out.println(txt);
       	} else {
       		out.println(txt.substring(0,70) + "...");
       	}
       	out.println("</option>");
   }		
%>
    </select>&nbsp;=&nbsp;<%
// LISTBOX reponses Q2 ------------------------------------------- -- 
 if (!(findQ2options != null && findQ2options.size()>1)) { 
 %><input name='findQ2' type="text" value="" size="20" class=normalInput>
<% } else {
   out.print("<select name='findQ2' class=normalInput>");
   for (int j=0; j<findQ2options.size(); j++) {
       Proposition p = (Proposition)findQ2options.get(j);
       out.print("<option value="+String.valueOf(j));
       if (p.isSelected()) out.print(" selected");
       out.println(">"+p.getTexte()+"</option>");
   }		
   out.print("</select>");
 } %>
     <br>
     
<% //CHECKBOX et suite ------------------------------------------- -- %>
<br>
<input type="submit" name="RechRep" value="Rechercher">&nbsp;&nbsp;
<!--  select name="chklock" > 
	<option value="1" selected>Validés uniquement</option>
	<option value="0">NON validés uniquement</option>
	<option value="-1">Tous</option>
	</select -->
	
	
	<input type="radio" value="-1" name="chklock" id="lockA" checked="checked" ><label for="lockA">Tous</label> 
	<input type="radio" value="1"  name="chklock" id="lock1" ><label for="lock1">Validés uniquement </label> 
	<input type="radio" value="0"  name="chklock" id="lock0" ><label for="lock0">NON validés uniquement </label> 
	
	
	<br>
</form>
</td></tr>
<tr><td>
<br>
<%
		// LISTE des formulaires trouvés -------------------
		for (int i=0; i<rlist.size(); i++) {
			SubmitForm sfi = (SubmitForm )rlist.get(i);
			
			out.println("<div class=listRep> "  );
			/*if (sfi.getS_id_parent()>0) 
			  out.println(" <a href='"+
						response.encodeURL(request.getContextPath()+"/ServControl?action=RLIST&numdos="+
			    			sfi.getS_id_parent())+"&chkparents=1&RechRep=1' >"+sfi.getS_id_parent() +"</a> / ");*/
			    			
			  out.println(" <a href='"+
						response.encodeURL(request.getContextPath()+"/ServControl?action=RLIST&numdos="+
						    sfi.getS_id())+"&chkparents=1&RechRep=1'  class='fleche_rouge'>"+sfi.getS_id()+"</a> - ");
			
			out.println(" <span  class='greyText'>"+BasicType.formatDateIsoToLocal(sfi.getS_date(),null)+"</span>" );
			   			
			out.print(" <span class='greyText typForm'>"+sfi.getF_id()+"</span> ");
			
			if (sfi.getF_id()==1) out.print("<strong>");
			out.println(sfi.getTitre() );
			if (sfi.getF_id()==1) out.print("</strong>");
			
			
			out.println(" <a href='ServControl?action=RDET&sid="+sfi.getS_id()+"&qid="+String.valueOf(sfi.getF_id())
					+"' class=fleche_orange>Afficher</a>");
			
			if (userdata.getRole() >= SessionInfos.ROLE_GESTION) {
			out.println(" <a href='ServControl?action=RMAJ&sid="+sfi.getS_id()+"&qid="+String.valueOf(sfi.getF_id())
			        +"' class=fleche_orange>Modifier</a> ");
			}
			
			out.println("</div>");
		}
out.println(" <br>Nb reponses: "+rlist.size() );

%>
<br><br><br>
<% if (alert!=null && alert.size()>0) { %>
<div class=alert>
<span  class="redText">Alertes :</span><br>
<%
		// LISTE des submitform en alerte -------------------
		for (int i=0; alert!=null && i<alert.size(); i++) {
			SubmitForm sfi = (SubmitForm )alert.get(i);
			
			out.println("<div class=listRep> "  );
			out.println(" <a href='"+
						response.encodeURL(request.getContextPath()+"/ServControl?action=RLIST&numdos="+
						    sfi.getS_id())+"&chkparents=1&RechRep=1'  class='fleche_rouge'>"+sfi.getS_id()+"</a> - ");
			out.println(" <span  class='greyText'>"+BasicType.formatDateIsoToLocal(sfi.getS_date(),null)+"</span>" );
	   			
			out.print(" <span class='greyText typForm'>"+sfi.getF_id()+"</span> ");
				
			out.println(sfi.getTitre() );
			
			out.println(" <a href='ServControl?action=RDET&sid="+sfi.getS_id()+"&qid="+String.valueOf(sfi.getF_id())
					+"' class=fleche_orange>Afficher</a>");
			out.println("</div>");
		}
out.println(" <br>Nb alertes: "+alert.size()+"</div>" );
}
%>

</td></tr></table>
</td></tr></table>
<%@ include file = "/footer.jsp" %>
