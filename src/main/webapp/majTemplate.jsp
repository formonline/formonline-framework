<%@ include file = "/entete.jsp" %>
<%@ page import="formOnLine.msBeans.*" %>
<%@ page import="com.triangle.lightfw.*" %>
<% 
Template t = (Template)session.getAttribute(Template.SESSION_NAME);
boolean bMaj = (t.getId()>0);
boolean bCreat = !bMaj ;

%>
<form name="formulaire" action='<%=response.encodeURL(request.getContextPath())%>/ServControl' method='post'>

<table class=tabSize>
	<tr>
		<td colspan="2" class="etape">Template</td>
	</tr>
	<tr>
		<td colspan="2" ><a href="#" onclick="javascript:window.open('help.htm','Aide','menubar=no, status=no, scrollbars=yes, menubar=no,width=500, height=500');">
		Aide...</a></td>
		
	</tr>
	<tr>
		<td>Id</td>
		<td><% if (bMaj) { %>
		<input name="t_id" value="<%=t.getId() %>" size="10" readonly=readonly>
		<% } %></td>
	</tr>
	<tr>
		<td>Form Id</td>
		<td>
		<input name="f_id" value="<%=t.getFid() %>" size="10">
		</td>
	</tr>
	
	<tr>
		<td>Nom</td>
		<td><input name="t_name" value="<%=t.getName() %>" size="80"></td>
	</tr> 
	
<tr>
		<td>Type</td>
		<td><select name="t_type">
		<option value="<%=Template.HTML %>"  <%=((t.getType().equals(Template.HTML))?"selected":"") %>>HTML (word)	</option>
		<option value="<%=Template.RTF %>" 	<%=((t.getType().equals(Template.RTF))?"selected":"") %>>RTF (word)	</option>
		<option value="<%=Template.CSV %>" 	<%=((t.getType().equals(Template.CSV))?"selected":"") %>>CSV (excel)	</option>
		<option value="<%=Template.MAIL %>"	<%=((t.getType().equals(Template.MAIL))?"selected":"") %>>Mail	</option>
		<option value="<%=Template.MTO %>" 	<%=((t.getType().equals(Template.MTO))?"selected":"") %>>mailto:	</option>
		<option value="<%=Template.SQL %>" 	<%=((t.getType().equals(Template.SQL))?"selected":"") %>>filtre SQL	</option>
		<option value="<%=Template.TXT %>" 	<%=((t.getType().equals(Template.TXT))?"selected":"") %>>Text	</option>
		<option value="<%=Template.LOT %>" 	<%=((t.getType().equals(Template.LOT))?"selected":"") %>>Lot	</option>
		<option value="<%=Template.PDF %>" 	<%=((t.getType().equals(Template.PDF))?"selected":"") %>>(xhtml to) PDF	</option>
		</select>
	
	
	<tr>
		<td>Content</td>
		<td>
		
				
<% if (    t.getType().equals(Template.HTML)
	    || t.getType().equals(Template.MAIL) ) { %>
		
	
<!-- TINY MCE -->
<script type="text/javascript" src="js/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript">

tinyMCE.init({
	// General options 
	// -- mode : "textareas",
	mode : "none",
	theme : "advanced",
	plugins : "safari,spellchecker,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,imagemanager,filemanager,codeprotect",

	// Theme options
	theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect",
	theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
	theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
	theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,spellchecker,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,blockquote,pagebreak,|,insertfile,insertimage",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	theme_advanced_statusbar_location : "bottom",
	theme_advanced_resizing : true,

	
	// Example content CSS (should be your site CSS)
	content_css : "css/style.css",

	// Drop lists for link/image/media/template dialogs
	template_external_list_url : "js/template_list.js",
	external_link_list_url : "js/link_list.js",
	external_image_list_url : "js/image_list.js",
	media_external_list_url : "js/media_list.js",

	// Replace values for the template plugin
	template_replace_values : {
		username : "Some User",
		staffid : "991234"
	}
	
});

</script>

		<a href="#" onclick="tinyMCE.execCommand('mceAddControl',false,'t_content');return false;">Rich Text Edit</a> 
		<a href="#" onclick="tinyMCE.execCommand('mceRemoveControl',false,'t_content');return false;">Text Area</a> 
		
		<br>
<% }  %>

	<textarea  name="t_content" id="t_content"  style="width:100%" rows="20" class=text
		><%=t.getContent() %></textarea>
		
		
		
		
		
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
		
		<input type="hidden" value="MODT" name="action">
		
		
					</td>
	</tr>


</table>

<%@ include file = "/footer.jsp" %>
