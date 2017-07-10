<%@ include file = "/entete.jsp" %>
<%@ page import = "formOnLine.msBeans.*, formOnLine.BasicTools" %>
<jsp:useBean id="stats" class="com.triangle.lightfw.ValueBeanList" scope="session" />
<% 
String currentTab = "Forms";
if (request.getParameter("currentTab")!= null) currentTab= request.getParameter("currentTab");

 %>

<script type="text/javascript" src="js/jquery.peity.js"></script>
<script>
$(document).ready(function() {
 /* camembert */
   $("span.pie").peity("pie"); /*, { colours: ["#C6D9FD", "#4D89F9"],radius: 20 }); */
});
</script>


<table class=tabSize>
<tr><td>

	 
	<ul id="tabnav" class=tabnav>
		<%	if (currentTab.equals("Forms")) { 
	         out.println("<li class=active><a href=#"); 
	        } else { out.println("<li><a href=ServControl?action=STAT&refresh=Forms&currentTab=Forms"); }
		%>>Stats formulaires</a></li>
		
		<%	if (currentTab.equals("Reps")) { 
	         out.println("<li class=active><a href=#"); 
	        } else { out.println("<li><a href=ServControl?action=STAT&refresh=Reps&currentTab=Reps"); }
		%>>Stats réponses</a></li>
		
		
		
	</ul>
		
		</td>
	</tr>
	<tr>
		<td>	


<div id=divStats class="greyBox">


<table style="border=1;border-style:solid;border-color:#CCCCCC;" 
 cellpadding="3" cellspacing="0"  align=center>
  <tr>
  			<td><em>Data</em> </td>
			<td><em>valid </em></td>
			<td><em>total </em></td>
			<td>&nbsp;</td>
		</tr>
<%
		for (int i=0; stats != null && i<stats.size(); i++) {
			Stat s = (Stat)stats.get(i);%>
		<tr>
			<td><%=formOnLine.BasicTools.cleanHTML(s.getTitre())%> </td>
			<td><strong><%=s.getVal() %></strong>   </td>
			<td><%=s.getTotal() %> </td>
			<td nowrap=nowrap>
			<% 
			out.println("<span class=pie>"+s.getVal()+"/"+s.getTotal()+"</span> "+s.getComment());
		    out.println("<span class=greyText>"+BasicTools.getPourcent(s.getVal(), s.getTotal(), 0));
			if (Stat.TYPE_SUM.equals(s.getType())) {
			    out.println(" <span style='color:#CC6600;font-weight:bold'>&#931;</span>");
			    out.println(" / "+s.getNum()+"</span> ");			     
			} 
			%>
			</td>
		</tr>
		<%	
				}
%>
</table>
<br><br>
<center><%=com.triangle.lightfw.BasicType.getCurentTime(null) %><br>



</center>

</td></tr></table>

<%@ include file = "/footer.jsp" %>
