<%@ include file = "/entete.jsp" %>
<jsp:useBean id="sf" class="formOnLine.msBeans.SubmitForm" scope="session" />

<table class="tabSize">

<tr><td  align="center"><br><br><strong>Perte du mot de passe</strong></td></tr>
<tr><td>
<form action='<%=response.encodeURL(request.getContextPath()) %>/ServControl'
method='post'>
        <table width="100%"  border="0" cellpadding="3" cellspacing="0" class="text">
          <tr>
            <td align="right" width=50% valign="top"><br>Votre identifiant : </td>
            <td  valign="middle">
            <input name="login" type="text" value="" size="30"  class=normalInput>
            </td>
          </tr>
          <tr>
            <td align="right"  valign="middle" >

Recopier les chiffres et lettres :<br>
(en respectant les majuscules)  
</td>
<td align="left"  valign="middle" >

			<!-- img src="Captcha"  id="captcha">  (<a href="javascript: refreshCaptcha();">Changer l'image</a>) -->
			<span id="captchaText">...</span> (<a href="javascript: refreshCaptcha();">Changer le texte</a>)
			<br>
			<input name="captcha" width="10"> <br>

<script language="JavaScript">
<!--
function refreshCaptcha(){	
	//timestamp en parametre pour empecher la mise en cache;
	//document.getElementById('captcha').src = 'Captcha?'+(Math.floor((new Date()).getTime() / 1000)); 
	getCaptcha(document.getElementById('captchaText'));
}
refreshCaptcha();
-->
</script>

	
		

            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td><br><input type='submit' name='bok' value='OK'><br><br>
            <a href="<%=response.encodeURL(request.getContextPath()+"/ServControl")%>">Retour</a>
	    <input type='hidden' name='action' value='MPWD'>
	    
	    </td>
          </tr>
        </table>
</form>
</td></tr>
<tr><td>
</td></tr></table>
<%@ include file = "/footer.jsp" %>
