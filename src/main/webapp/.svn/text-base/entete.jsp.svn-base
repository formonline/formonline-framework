<!doctype html>
<html>
<head>
<%@ page 
	language="java"
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" 
	import="formOnLine.msBeans.*, com.triangle.lightfw.*,formOnLine.Controls"
%>
<% SessionInfos sessionInfos = (SessionInfos)request.getSession().getAttribute(com.triangle.lightfw.AbstractServlet.ID_SESSION_INFOS);%>
<jsp:useBean id="message" class="java.lang.String" scope="session" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<META HTTP-EQUIV="pragma" CONTENT="no-store, no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-store, no-cache, must-revalidate, post-check=0, pre-check=0">
<META NAME="ROBOTS" CONTENT="NOINDEX, NOFOLLOW">
<META NAME="Expires" CONTENT="0">
<link rel="icon" type="image/png" href="favicon.png" />

<meta name="viewport" content="width=device-width, user-scalable=yes" />

<link type="text/css" href="theme/jquery-ui-1.8.13.custom.css?v=<%=formOnLine.InitServlet.getVersion()%>" rel="stylesheet" >
<link type="text/css" href="theme/Master.css?v=<%=formOnLine.InitServlet.getVersion()%>" rel="stylesheet" >
<link type="text/css" href="theme/tabnav.css?v=<%=formOnLine.InitServlet.getVersion()%>" rel="stylesheet" media="print, projection, screen" />
<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>


<script type="text/javascript" language="JavaScript" content="text/html; charset=UTF-8" 
	src="js/IdF_functions.js"></script>
	<script type="text/javascript" language="javascript" content="text/html; charset=UTF-8" 
	src="js/get_xml.js"></script>
	

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js" ></script>
<script type="text/javascript" src="js/jquery.ui.datepicker-fr.js" ></script>
<script type="text/javascript" src="js/jquery-ui-timepicker-addon.js" ></script>
<script type="text/javascript" src="js/jquery.limit-1.2.source.js" ></script>

<!-- link href='http://fonts.googleapis.com/css?family=ABeeZee|Arimo:400,700' rel='stylesheet' type='text/css'  -->

<TITLE>Intranet-RH</TITLE>
</head>
<BODY>
<noscript>
  !!! Cette application utilise le JAVASCRIPT, vous devez activer l'option JAVASCRIPT de votre navigateur.
</noscript>

<% //base URL pour les images
StringBuffer url = request.getRequestURL();
String uri = request.getRequestURI();
String ctx = request.getContextPath();
String baseURL = url.substring(0, url.length() - uri.length() + ctx.length()) + "/";
%>
<base href="<%=baseURL%>" >

<div class="tabSize">
<table width="100%" cellpadding="10px">
	<tr>
		<td align="left" bgcolor="FFFFFF"  >
		<span style="font-family:Raleway, Verdana;font-weight: 200;font-size:48px;color:#CC6600">form<span style="color:#F3B935">O</span>n<span style="color:#F3B935">L</span>ine</span>
		
		</td>
		<td align=right  bgcolor="FFFFFF" ><img name="idf" src="theme/IDF_miniGIF.gif" 
		border=0 alt="idf" ></td>
	</tr>
</table>
<table  
<%=((sessionInfos!=null&&sessionInfos.isInJahia())?"width=100%":" width=\"100%\" ")%>>
	<tr><td style="text-align: right;" class="menuRH" cellpadding="10px">
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=LOGIN")%>">Accueil</a> 
	<% if ((sessionInfos != null) && (sessionInfos.getRole()>SessionInfos.ROLE_PUBLIC)) { %>
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=RLIST")%>">Rechercher</a>  
	<% } %>
	<% if ((sessionInfos != null) && (sessionInfos.getRole()>=SessionInfos.ROLE_GESTION)) { %>
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=SEXP")%>">Exports</a>  
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=STAT")%>">Stats</a>  
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=QLIST")%>">Admin</a> 
	
	<% } %>
	<% if ((sessionInfos != null) && (sessionInfos.getRole()>=SessionInfos.ROLE_ADMIN)) { %>
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=MODTAB")%>">Config</a> 
	<% } %>
	<a href="<%=response.encodeURL(request.getContextPath()+"/ServControl?action=FIN")%>">Quitter</a>
	</td></tr>

	 <tr >
	 <td style="text-align: center;">
	 <% if (!Controls.isBlank(message)) { %>
	 <div class='errorText'><strong><%=message%></div>
	 <script type="text/javascript" language="JavaScript">alert("<%=message%>");</script>
	 <% } %>&nbsp;
	 </td>
	 </tr>

</table>