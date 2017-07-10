
	var http_request = false;
	var objDDL = null;
	
	function makeRequest(url) {

		http_request = false;

		if (window.XMLHttpRequest) { // Mozilla, Safari,...
			http_request = new XMLHttpRequest();
			if (http_request.overrideMimeType) {
				http_request.overrideMimeType('text/xml');
			}
		} else if (window.ActiveXObject) { // IE
			try {
				http_request = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					http_request = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {}
			}
		}

		if (!http_request) {
                    alert('Giving up :( Cannot create an XMLHTTP instance');
                    return false;
		}

		http_request.onreadystatechange = doWhatYouGotToDo;
		http_request.open('GET', url, true);
		http_request.send(null);

	}
	

	function doWhatYouGotToDo() {

		if (http_request.readyState == 4) {
			if (http_request.status == 200) {
				
				var text="";
				var xmldoc = http_request.responseXML;
				
				// message
				try {
					var root_node = xmldoc.getElementsByTagName('message').item(0);
					objDDL.innerHTML = "";
					objDDL.innerHTML = root_node.firstChild.data;
					//alert(root_node.firstChild.data);
					
				}   catch (e) {}
				
				
				
				// liste
				try {
					var root_node = xmldoc.getElementsByTagName('liste').item(0);
					var listXML = root_node.getElementsByTagName('commune');
                        
					//var objDDL = list;
					objDDL.options.length =0;
			
					var option = new Option("Choisissez une commune...","");
					try {
						objDDL.add(option,null);
					}   catch (e) {
						objDDL.add(option,-1);
					}	
			
		
					var max = listXML.length
					for (i = 0; i < max; i++) {
						//var option = new Option(listXML.item(i).firstChild.data,listXML.item(i).getAttribute('ci'));
						var option = new Option(listXML.item(i).firstChild.data,listXML.item(i).firstChild.data);
						try {
							objDDL.add(option,null);
						}   catch (e) {
							objDDL.add(option,-1);
						}
				    
					}
				}   catch (e) {}
				
				
				
				
			} else {
				alert('There was a problem with the request.');
			}
		}
	}

	
	
	function getCommunes(list1,list2,webcall) {
	
		if (list1.selectedIndex != 0) {
			//var index = list1.selectedIndex
			//var dept = list1.options[index].value
			//var url = 'ListCommunes?action=GETXML&ci='+dept
			var url = webcall ; //+'comidf/comidf'+dept+'.xml'

			objDDL = list2
   		  	makeRequest(url);

			//return true;
		} else {
			objDDL = list2;
			objDDL.options.length =0;
		}
	}

	function testRib(ribInput, msgBox) {
		
		msgBox.innerHTML = "";
		
		if ( ribInput.value.length == 23 ) {
			var rib = ribInput.value;
			//var url = webroot +'XmlAjaxControl?action=TEST_SIRET&value='+siretInput.value;
			var url = 'XmlAjaxControl?action=TEST_RIB&value='+ribInput.value;			
			objDDL = msgBox;
			makeRequest(url);

		} else {
			var dif = (23  - ribInput.value.length )
			if (dif >0 ) {
				msgBox.innerHTML  = "Il manque "+dif+" caract&egrave;re(s)";
			} else {
				dif = -dif ;
				msgBox.innerHTML  = "Il y a "+dif+" caract&egrave;re(s) en trop";
			
			}
		}
	}

	function testSiret(siretInput, msgBox, webroot) {
		
		msgBox.innerHTML = "";
		
		if ( siretInput.value.length == 14 ) {
			var siret = siretInput.value;
			//var url = webroot +'XmlAjaxControl?action=TEST_SIRET&value='+siretInput.value;
			var url = 'XmlAjaxControl?action=TEST_SIRET&value='+siretInput.value;			
			objDDL = msgBox;
			makeRequest(url);

		} else {
			var dif = (14  - siretInput.value.length )
			if (dif >0 ) {
				msgBox.innerHTML  = "Il manque "+dif+" caract&egrave;re(s)";
			} else {
				dif = -dif ;
				msgBox.innerHTML  = "Il y a "+dif+" caract&egrave;re(s) en trop";
			
			}
		}
	}
	
	function getParentData(sid, pid, msgBox) {
		
			var url = 'XmlAjaxControl?action=PARENT_DATA&p_id='+pid+'&s_id='+sid;
			
			objDDL = msgBox;
			makeRequest(url);
 
	}
	
	function testUnique(input, msgBox, sid) {
		
		if ( input.value.length > 0 ) {

			var url = 'XmlAjaxControl?action=TEST_UNIQUE&value='+input.value+'&input='+input.name+'&s_id='+sid;
			
			objDDL = msgBox;
			makeRequest(url);

		} 
	}
	
	
	
	function getCaptcha(divId) {
			var url = 'XmlAjaxControl?action=CAPTCHA';
			
			objDDL = divId;
			makeRequest(url);
	}

	