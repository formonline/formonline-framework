<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
	language="java"
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="/erreur.jsp"
	import="java.util.Enumeration, formOnLine.msBeans.*, com.triangle.lightfw.*,formOnLine.Controls"
%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Content-Style-Type" content="text/css">
<!-- LINK href="theme/Master.css" rel="stylesheet" type="text/css"-->
<TITLE>Fiche</TITLE>
</HEAD>
<BODY style="FONT-FAMILY: 'Arial';">

<jsp:useBean id="q" class="formOnLine.msBeans.Questionnaire" scope="session" />
<jsp:useBean id="sf" class="formOnLine.msBeans.SubmitForm" scope="session" />

<p align="center">Proposition d'emploi(s) tremplin(s)</p>
<table width="100%"  border="1" cellpadding="1" cellspacing="0" bordercolor="#666666">
  <tr valign="top">
    <td>Intitul&eacute; poste :<br><%=sf.getPropVal(10)%> </td>
    <td colspan="2">Subvention propos&eacute;e :<br>15.000 € </td>
  </tr>
  <tr valign="top">
    <td colspan="3">Organisme :<br><strong><%=sf.getPropVal(6)%></strong><br>
      Adresse :  <%=sf.getPropVal(33)%> - <%=sf.getPropVal(34)%> <%=sf.getPropVal(35)%></td>
  </tr>
  <tr valign="top">
    <td>Nature juridique :<br><%=sf.getPropVal(7)%> </td>
    <td colspan="2">Pr&eacute;sident(e) :<br><%=sf.getPropVal(39)%> </td>
  </tr>
  <tr valign="top">
    <td>Date de cr&eacute;ation :<br><%=sf.getPropVal(14)%> </td>
    <td colspan="2">Tr&eacute;sorier(e) :<br>??? </td>
  </tr>
  <tr valign="top">
    <td>Budget pr&eacute;visionnel [2005]:<br>??? </td>
    <td colspan="2">Secr&eacute;taire :<br>???</td>
  </tr>
  <tr valign="top">
    <td colspan="3"><%=sf.getPropVal(11)%> 
      Cr&eacute;ation nette&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      
      <%=sf.getPropVal(11)%> Transformation d'un temps partiel en temps plein </td>
  </tr>
  <tr align="center" valign="top">
    <td colspan="3">Opportunit&eacute; du poste &agrave; cr&eacute;&eacute;r</td>
  </tr>
  <tr valign="top">
    <td colspan="3">Objectifs de la cr&eacute;ation (ou transformation) du poste pour l'organisme : <br><%=sf.getPropVal(49)%></td>
  </tr>
  <tr valign="top">
    <td colspan="3">Effets attendus sur le public : <br><%=sf.getPropVal(37)%></td>
  </tr>
  <tr valign="top">
    <td colspan="3">Effets attendus sur le secteur d'activit&eacute; :<br>??? </td>
  </tr>
  <tr valign="top">
    <td colspan="3">Effets attendus sur le territoire :<br>??? </td>
  </tr>
  <tr align="center" valign="top">
    <td colspan="3">Plan de financement pr&eacute;visionnel du poste</td>
  </tr>
  <tr valign="top">
    <td colspan="3"><table width="100%"   border="1" cellpadding="1" cellspacing="0" bordercolor="#666666">
      <tr>
        <td>&nbsp;</td>
        <td align="center">1&egrave;re ann&eacute;e (&euro;) </td>
        <td align="center">%</td>
        <td align="center">2&egrave;me ann&eacute;e (&euro;) </td>
        <td align="center">%</td>
        <td align="center">3&egrave;me ann&eacute;e (&euro;)</td>
        <td align="center">%</td>
      </tr>
      <tr>
        <td>Employeur</td>
        <td align="right">2988</td>
        <td align="right">15</td>
        <td align="right">2988</td>
        <td align="right">15</td>
        <td align="right">2988</td>
        <td align="right">15</td>
      </tr>
      <tr>
        <td>RIDF</td>
        <td align="right">15000</td>
        <td align="right">75.3</td>
        <td align="right">15000</td>
        <td align="right">75.3</td>
        <td align="right">15000</td>
        <td align="right">75.3</td>
      </tr>
      <tr>
        <td>Autre</td>
        <td align="right">1930</td>
        <td align="right">9.7</td>
        <td align="right">1930</td>
        <td align="right">9.7</td>
        <td align="right">1930</td>
        <td align="right">9.7</td>
      </tr>
      <tr>
        <td align="right">TOTAL</td>
        <td align="right">19918</td>
        <td align="right">100</td>
        <td align="right">19918</td>
        <td align="right">100</td>
        <td align="right">19918</td>
        <td align="right">100</td>
      </tr>
    </table></td>
  </tr>
   <tr valign="top">
    <td colspan="3">Antériorité du soutien régional au cours des trois derniers exercices :<br>??? </td>
  </tr>
</table>

<%@ include file = "/footer.jsp" %>
