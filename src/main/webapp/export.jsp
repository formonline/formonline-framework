<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Page export.jsp -->
<%@ include file="/entete.jsp" %>
<% ValueBeanList listTempl = (ValueBeanList)session.getAttribute(Template.SESSION_LIST);%>
<% ValueBeanList  qlist= (ValueBeanList)session.getAttribute(Questionnaire.SESSION_LIST);%>
<% String currentTab = null;

if (request.getParameter("currentTab")!= null)
    session.setAttribute("currentTab", request.getParameter("currentTab"));
		    
if ( session != null && session.getAttribute("currentTab")!=null) 
		        currentTab = (String)session.getAttribute("currentTab");	

if (currentTab == null || currentTab.length()==0) currentTab="CSV";
 %>


<table class=tabSize>
<tr><td>

	 
	<ul id="tabnav" class=tabnav>
		 <%	if (currentTab.equals("CSV")) { 
	         out.println("<li class=active><a href="); 
	        } else { out.println("<li><a href=ServControl?action=SEXP&currentTab=CSV"); }
		%>>Exports CSV</a></li>
		

<% if ((sessionInfos != null) && (sessionInfos.getRole()>SessionInfos.ROLE_GESTION)) { %>

<% 	if (currentTab.equals("XML")) { 
	         out.println("<li class=active><a href="); 
        } else { out.println("<li><a href=ServControl?action=SEXP&currentTab=XML"); }%>
	    >Exports XML</a></li>
		
		<% 	if (currentTab.equals("TXT")) { 
	         out.println("<li class=active><a href="); 
        } else { out.println("<li><a href=ServControl?action=SEXP&currentTab=TXT"); }%>
	    >Exports TXT</a></li>
		
		<% 	if (currentTab.equals("FILE")) { 
	         out.println("<li class=active><a href="); 
        } else { out.println("<li><a href=ServControl?action=SEXP&currentTab=FILE"); }%>
	    >Fichiers</a></li>
		
		<% 	if (currentTab.equals("IMP")) { 
	         out.println("<li class=active><a href="); 
        } else { out.println("<li><a href=ServControl?action=SEXP&currentTab=IMP"); }%>
	    >Import CSV</a></li>
	    
	    <% 	if (currentTab.equals("LOT")) { 
	         out.println("<li class=active><a href="); 
        } else { out.println("<li><a href=ServControl?action=SEXP&currentTab=LOT"); }%>
	    >Lots</a></li>
	

<% }
if ((sessionInfos != null) && (sessionInfos.getRole()>=SessionInfos.ROLE_GESTION)) { %>
		
		
		<% 	if (currentTab.equals("MAIL")) { 
	         out.println("<li class=active><a href="); 
        } else { out.println("<li><a href=ServControl?action=SEXP&currentTab=MAIL"); }%>
	    >Mailing</a></li>
		
		 <% 	if (currentTab.equals("TEMPL")) { 
	         out.println("<li class=active><a href="); 
        } else { out.println("<li><a href=ServControl?action=SEXP&currentTab=TEMPL"); }%>
	    >TEMPLATES</a></li>
	    
<% } %>	

	</ul>
	
		
		
		</td>
	</tr>
	<tr>
		<td>	

<% if (currentTab.equals("XML")) { %>
<div id=divXML class="greyBox">
<form name="form1" method="post" action="<%=response.encodeURL(request.getContextPath() + "/ServControl?action=EXPO")%>">
    <strong>Exports XML : </strong>
   
    
    <br>form ID <input name="f_id" type="text" value="" size="5" maxlength="5">
     &nbsp;&nbsp;&nbsp;
    ou Numéro SID :
    <input name="sid" type="text" value="" size="5" maxlength="10" class=normalInput>
&nbsp;&nbsp;&nbsp;&nbsp;
    <br>
      Filtre : 
    <select name="sqlFilterId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.SQL)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> {%id%}:  <input name="sqlValId" value="" size=5>
    
    <br>
    ...du 
    <input name="date_debut" type="text" value="" size="10" maxlength="10">
     au 
    <input name="date_fin" type="text" value="" size="10" maxlength="10">
    (inclus)
    <input type="submit" name="ExportDemandes" value="EXPORTER"> 
    <input name=lockedOnly type="checkbox" checked="checked"> Verrouillés uniquement
   
   <br>
   </form>
   
</div>

<% }
if (currentTab.equals("CSV")) { %>

<div id=divCSV class="greyBox">
   
   
   <strong>Exports CSV :</strong>
    <form name="formExp" 
    method="post" 
    action="<%=response.encodeURL(request.getContextPath() + "/ServControl")%>">
    <select name="templateId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.CSV)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> &nbsp;
    
    
    <br>
    Filtre : 
    <select name="sqlFilterId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.SQL)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> {%id%}:  <input name="sqlValId" value="" size=5>
    
    <br>
    ...du 
    <input name="date_debut" type="text" value="" size="12" maxlength="10">
     au 
    <input name="date_fin" type="text" value="" size="12" maxlength="10">
    &nbsp;
    
    <input name="action" value="EXPO" 		type="hidden">
    <input name="templateType" value="CSV" 	type="hidden">
    <input name="sid" value="-1" 			type="hidden"> 
      
      <input name=bokMailing type=submit value="Exporter">
      <input name=lockedOnly type="checkbox" checked="checked"> Verrouillés uniquement
    </form>
	
   
</div>

<% }
if (currentTab.equals("TXT")) { %>

<div id=divTXT class="greyBox">
   
   
   <strong>Exports TXT :</strong>
    <form name="formExp" 
    method="post" 
    action="<%=response.encodeURL(request.getContextPath() + "/ServControl")%>">
    <select name="templateId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.TXT)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> &nbsp;
    
    
    <br>
    Filtre : 
    <select name="sqlFilterId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.SQL)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> {%id%}:  <input name="sqlValId" value="" size=5>
    
    <br>
    ...du 
    <input name="date_debut" type="text" value="" size="12" maxlength="10">
     au 
    <input name="date_fin" type="text" value="" size="12" maxlength="10">
    &nbsp;
    
    <input name="action" value="EXPO" 		type="hidden">
    <input name="templateType" value="TXT" 	type="hidden">
    <input name="sid" value="-1" 			type="hidden"> 
      
      <input name=bokMailing type=submit value="Exporter">
      <input name=lockedOnly type="checkbox" checked="checked"> Verrouillés uniquement
    </form>
	
   
</div>

<% }
if (currentTab.equals("MAIL")) { %>

<div  id=divMAIL class=greyBox>
		
   
   <strong>Mailing</strong><br>
    <form name="formMailing" 
    method="post" 
    action="<%=response.encodeURL(request.getContextPath() + "/ServControl")%>"
    onsubmit="return confirm('Etes-vous sur de vouloir lancer un mailing ?');">
    <select name="templateId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.MAIL)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> &nbsp;
    
      
      <br>
    Filtre : 
    <select name="sqlFilterId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.SQL)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> {%id%}:  <input name="sqlValId" value="" size=5>
    
      <br>
    ...du 
    <input name="date_debut" type="text" value="" size="12" maxlength="10">
     au 
    <input name="date_fin" type="text" value="" size="12" maxlength="10">
    &nbsp;
    <input name="action" value="EXPO" type="hidden">
    <input name="templateType" value="MING" type="hidden">
    <input name="sid" value="-1" type="hidden"> 
      
      
      <input name=bokMailing type=submit value="GO">
      <input name=lockedOnly type="checkbox" checked="checked"> Verrouillés uniquement
  
    </form>
</div>

<% }
if (currentTab.equals("FILE")) { %>

<div  id=divFILE class=greyBox>
   	
    <strong>Génération de fichiers</strong><br>
    <form name="formFiles" 
    method="post" 
    action="<%=response.encodeURL(request.getContextPath() + "/ServControl")%>"
    onsubmit="return confirm('Etes-vous sur de vouloir lancer une génération de fichiers ?');">
    <select name="templateId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.HTML)) { %>
		<option value=<%=t.getId()%>><%=formOnLine.BasicTools.cleanFusionMarks(t.getName()) %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> &nbsp;
    <br>
    
    Filtre : 
    <select name="sqlFilterId" >
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.SQL)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> {%id%}:  <input name="sqlValId" value="" size=5>
    
   
     
     <br>
     ...du 
    <input name="date_debut" type="text" value="" size="10" maxlength="10">
     au 
    <input name="date_fin" type="text" value="" size="10" maxlength="10">
     &nbsp;
      
    
    <input name="action" value="EXPO" type="hidden">
    <input name="templateType" value="FILES" type="hidden">
    <input name="sid" value="-1" type="hidden">
      
      <input name=bokFiles type=submit value="GO">
       <input name=lockedOnly type="checkbox" checked="checked"> Verrouillés uniquement
  
		
		</form>
		
</div>

<% }
if (currentTab.equals("IMP")) { %>


<div  id=divIMP class=greyBox>
		
	<strong>Import de données</strong><br>
    <form name="formImport" 
    method="post" 
    action="<%=response.encodeURL(request.getContextPath() + "/ServControl")%>"
    onsubmit="return confirm('Etes-vous sur de vouloir lancer un import ?');"
    enctype="multipart/form-data" >
    
    <select name="templateId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.CSV)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> &nbsp;
    
    
	
    <input name="action" value="IMP" type="hidden">
    <input name="templateType" value="CSV" type="hidden">
    <input name="file"  width="30" type=file value="Parcourir">&nbsp;&nbsp;
    <input name="sid" value="-1" type="hidden">
      
      <br>
      <input name=bokImp type=submit value="Importer">
      
      <input name=test type="checkbox" checked="checked"> Test import
      <input name=repOnly type="checkbox" > Reponses seules
       
		
		</form>
</div>	
		
<% }
if (currentTab.equals("LOT")) { %>
		
		
<div  id=divLOT class=greyBox>
		
	<strong>Mise à jour de lots de données</strong><br>
    <form name="formlot" 
    method="post" 
    action="<%=response.encodeURL(request.getContextPath() + "/ServControl")%>"
    onsubmit="return confirm('Etes-vous sur de vouloir mettre à jour ce lot de données ?');"
    enctype="multipart/form-data" >
    
    <select name="templateId">
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.LOT)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> &nbsp;
    <br>
    
    Filtre : 
    <select name="sqlFilterId" >
    <option value="-1">&nbsp;</option>
       <% 
       for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
		if (t.getType().equals(Template.SQL)) { %>
		<option value=<%=t.getId()%>><%=t.getName() %> (F<%=t.getFid() %>)</option>
   		<% }
		} %>
    </select> {%id%}:  <input name="sqlValId" value="" size=5>
    
   <br>
     ...du 
    <input name="date_debut" type="text" value="" size="10" maxlength="10">
     au 
    <input name="date_fin" type="text" value="" size="10" maxlength="10">
      
   
    <input name="action" value="IMP" type="hidden">
    <input name="templateType" value="LOT" type="hidden">
   
    
   <input name=bokImp type=submit value="GO">
   <input name=lockedOnly type="checkbox" checked="checked"> Verrouillés uniquement
   
		
		</form>
</div>	

<% }
if (currentTab.equals("TEMPL")) { %>
<div id=divTempl>
<br><br>
<span class=etape>Templates :</span>
    
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=MODT")%>">
	[Nouveau template]</a> 
    <br>
    <br>
    
    
    
    <%
    for(int j=0; qlist !=null && j<qlist.size(); j++  ) {
            formOnLine.msBeans.Questionnaire q = (Questionnaire)qlist.get(j);
     
	
	 out.println( " <a onclick=\"$('#divDetail"+ j +"').toggle();\" class=bigGreyText>"
				+  "<strong>Form "+q.getId()+" : "+q.getTitre()+"</strong>" 
				+" <img id=divImg"+ j 
				+ " src=\"theme/expand.gif\" border=0 width=14 height=14 /> "
     			+" <img id=divImg_"+ j 
		+ " src=\"theme/collapse.gif\" border=0 width=14 height=14 /></a>");

     
	out.println( " <div style=\"display: none;\" id=\"divDetail" 
								+ j +"\" class=greyBox>");
	
	for (int i=0 ; listTempl!= null && i< listTempl.size(); i++) { 
    	Template t = (Template)listTempl.get(i); 
    	if (q.getId() != t.getFid()) continue;
    	%>
        
        
	 <span class="greyText"><%=t.getType() %> </span>     
    <span class="fleche_orange"><%=t.getName() %> </span> 
    <span class="greyText"> [<%=t.getId() %>] </span> 
    <a href='<%=response.encodeURL(request.getContextPath() +
    "/ServControl?action=EXPO&templateType="+t.getType()+"&templateId="+t.getId()+"&sid=-1")%>'>Visu</a>   
    <a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=MODT&t_id="+t.getId())%>">MAJ</a> 
    
     <br>
	<% } %>
	</div>
    <%} %>
</div>
<%} // tabTEmpl%>    


<br>
  </tr></td>
</table>

  </tr></td>
</table>


<%@ include file = "/footer.jsp" %>

