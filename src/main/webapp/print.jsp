<%@ include file = "/entete.jsp" %>
<jsp:useBean id="userdata" class="formOnLine.msBeans.UserData"
	scope="session" />
<% 
com.triangle.lightfw.ValueBeanList listRep2= userdata.getListRep();
formOnLine.msBeans.SubmitForm rep1= userdata.getRep(userdata.getId());

if (rep1!=null) { 
	for (int i=0; i<listRep2.size(); i++) {
		SubmitForm sfi = (SubmitForm )listRep2.get(i);
		
		if (sfi.getF_id()!=7 && sfi.getF_id()!=10) continue;
%>

<style type='text/css'  media='all'>
<!--
p { font-family: Arial; }
.texte {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 15px;
	font-weight: normal;
}
.titre {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 18px;
}
.titre2 {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 16px;
	font-weight: bold;
}
.cadre {
border-style:solid;
border:solid;
border-width: 1px;
border: 1px;
border-color:#000000;
}
-->
</style>
<!-- pour l'impression seulement -->
<!--
<style type=text/css media=print>
p { font-family: Arial; }
thead {
display: table-header-group;
}
tfoot {
display: table-footer-group;
}
-->
</style>

<span class='texte'>


<table border='0' align='center' width='100%' cellspacing='0'>
<!-- Création de l'entête à répéter -->
<thead>
		<tr>
		<th >
	

 <p class='titre'><%=rep1.getPropVal(7)%> ( <%=rep1.getPropVal(8)%> )</p>
<p  class='titre2'>
Accusé de Télétransmission N. <%=sfi.getS_id()%><br>
 COMITE TECHNIQUE PARITAIRE GENERAL (CTP)<br>
<span class='texte'> Scrutin du 6/11/2008 - 1er tour </span></p>
		  </th>
		</tr>
</thead>

<tbody>
<tr>
	<td>  
	

	<table  border='1' align='center' cellpadding='3' cellspacing='0' bordercolor='#000000'>
      <tr>
        <td align='right' nowrap>Nombre d'inscrits : <br>          </td>
        <td width='45' align='center' valign='middle'>x</td>

        </tr>
      <tr>
        <td align='right' nowrap>Nombre d'agents votant <br>
          par correspondance : <br>          </td>
        <td width='45' align='center' valign='middle'>x</td>

        </tr>
      <tr>
        <td align='right'><strong>Nombre de votants : </strong></td>
        <td width='45' align='center' valign='middle'>x</td>
       
        </tr>
    </table>
	<div align='center'><em><br>
	      </em><strong>
	      Résultats du scrutin</strong> : <br>	  

	      </p>
	  </div>
	<table  border='1' align='center' cellpadding='3' cellspacing='0' bordercolor='#000000'>
      <tr>
        <td width='40%'><div align='center'>1. CFDT</div></td>
        <td align='center'>x</td>

      </tr>
      <tr>
        <td width='40%'><div align='center'>2. FO</div></td>
        <td align='center'>x</td>
        </tr>
      <tr>
        <td width='40%'><div align='center'>3. UNSA</div></td>
        <td align='center'>x</td>
        </tr>
      <tr>
        <td width='40%'><div align='center'>4. FSU</div></td>
        <td align='center'>x</td>
        </tr>
      <tr>
        <td width='40%'><div align='center'>5. CGT</div></td>
        <td align='center'>x</td>
        </tr>
      <tr>
        <td width='40%'><div align='center'>6. SUD</div></td>
        <td align='center'>x</td>
        </tr>
      <tr>
        <td width='40%'><div align='center'>Blancs ou nuls </div></td>
        <td align='center'>x</td>
        </tr>
    </table>
		
<br>
	<br>Signatures :	 <br> 
	<table width='100%' border='1' cellpadding='3' cellspacing='0' bordercolor='#000000'>
      <tr align='left' valign='top'>
        <td width='50%'>Président :<br>      <br>
      <br>            Secrétaire : 
      <br>
      <br>
        </td>
        <td>      Délégués de listes  :</td>
      </tr>
    </table>
</td>
</tr>
</tbody>
</table>



	<% } %>
<% } %>
<%@ include file = "/footer.jsp" %>
