<%

	out.println( "<input type=hidden id=modrep value=1>");
	
 

	// ancres
	for (int k=0; k<q.getNbGroupes(); k++) {
		Groupe gk = q.getGroupe(k);
		
	  if (gk.getType().equals(Groupe.PUBLIC) || ((sessionInfos != null) && sessionInfos.isInRoleRegion())) {
		%>
		<a href="#<%=gk.getId() %>" class="anchor">&gt; <%=gk.getNum()%>. <%=gk.getTitre()%></a><br> 
	<% }
	}

    out.println( "<br>");
    
	for (int k=0; k<q.getNbGroupes(); k++) {
		Groupe gk = q.getGroupe(k);
		if (gk.getType().equals(Groupe.PUBLIC) 
		        || ((sessionInfos != null) && sessionInfos.isInRoleRegion())
		        || ((sessionInfos != null) && sessionInfos.getRole()>=SessionInfos.ROLE_ADMIN)) {
		%>
	<div id=group<%=gk.getId() %>>		
		<div class="etape" >
          <strong><%=gk.getNum() %>.</strong> <%=gk.getTitre() %> <a name="<%=gk.getId() %>" /> 
          <a  style='float:right' href='#top'>^</a>
		</div>

		<%
		
			  for (int i=0; i<gk.getNbQuestions(); i++) {
			      Question qi = gk.getQuestion(i);
			      out.print("<fieldset class=blocP id=fieldset"+qi.getId()+">");
			      out.println("<b>"+ qi.getTexte() + "</b><br>");
			      
			      for (int j=0; j<qi.getNbPropositions(); j++) {
						Proposition pj = qi.getProposition(j);
						
						if ((qi.getType().equals(Question.CHOIX_MULTIPLE) 
						        || qi.getType().equals(Question.CHOIX_EXCLUSIF)
						        || qi.getType().equals(Question.LIST_FORM))
						     && Controls.isBlank(sf.getPropVal(pj.getId(), pj.getVal()))) continue;
						
						//if (!Controls.isBlank(sf.getPropVal(pj.getId()))) 
						out.print("<span class=readOnly>");
						
						if (qi.getType().equals(Question.CHOIX_MULTIPLE) 
						        || qi.getType().equals(Question.CHOIX_EXCLUSIF)
						        || qi.getType().equals(Question.LIST_FORM)) {
						   if (!Controls.isBlank(sf.getPropVal(pj.getId(), pj.getVal()))) 
		       				        out.print( pj.getTexte() );
						      
						   
						} else if (qi.getType().equals(Question.FORM_ID)){
						    
							  out.print(pj.getTexte());
							  String relSid = sf.getPropVal(pj.getId());
							  if (relSid !=null )  {
							      if (relSid.equals(String.valueOf( sf.getS_id()))) {
										out.println(relSid );
							      } else {
										out.println("  <a href='" 
								                + response.encodeURL(request.getContextPath()+"/ServControl?action=RDET&sid="+sf.printPropVal(pj.getId()))
								                +"'>"+sf.printPropVal(pj.getId())+"</a>  ");
							      }
							  }	
							 
							  
						} else if (qi.getType().equals(Question.URL)){
						    
							  out.print(pj.getTexte());
							  if (sf.getPropVal(pj.getId())!=null) 
							    //out.println(" [ <a href='" + response.encodeURL(request.getContextPath()+"/files/pdf/"+formOnLine.BasicTools.getLongDirFromPwd(sf.getPwd())+"/"+ sf.printPropVal(pj.getId()))+"' target=_blank>"+sf.printPropVal(pj.getId())+"</a> ] ");
							  	out.println("  <a href='" + sf.printPropVal(pj.getId())+"' target=_blank>"+sf.printPropVal(pj.getId())+"</a>  ");
							  	
							  
						} else if (qi.getType().equals(Question.UPLOAD)){
						    
							  out.print(pj.getTexte());
							  if (sf.getPropVal(pj.getId())!=null) 
								    out.println(" <a href='getFile?sid="+ sf.getS_id() +"&file=" + formOnLine.BasicTools.urlEncode(  sf.getPropVal(pj.getId()) )+"' target=_blank>"+sf.printPropVal(pj.getId())+"</a>  ");
								   
							  
					  	} else {
					  	  java.text.NumberFormat formatter = new java.text.DecimalFormat("00000");
					  	  String elmtId = "fol_teg" + formatter.format(gk.getId()) + "q" + formatter.format(qi.getId()) + "p" + formatter.format(pj.getId());
						    
					  	  out.print( "<span class=greyText>");
						  out.print(pj.getTexte());
						  out.println( "</span>");
						  if (sf.getPropVal(pj.getId())!=null) out.print(" <div style='display:inline;' id='"+elmtId+"'>"+ sf.printPropVal(pj.getId()) + "</div> ");
						  
				  		}
						
						//if (!Controls.isBlank(sf.getPropVal(pj.getId()))) 
						out.print("</span><br>");
						
						//if (i == gk.getNbQuestions()-1) 
						//out.println( "<br>");
				
			      }
			      out.println( "</fieldset>");
			  }
			  out.println( "<br> </div>");
			  
	     }
	}
	

%>

