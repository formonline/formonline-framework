var debug = false;
/******************************************************************************/

function valideFormulaire(frm)
/*
 RÃ´le: ContrÃ´le de la validité de la saisie effectuée sur l'ensemble des éléments d'un formulaire
 !! On suppose qu'il n'y a qu'un seul formulaire sur la page !!

 Parametres :
 - frm: Formulaire a valider

 Les contrÃ´les sont effectués, sur le formulaire, élément par élément.
 Des qu'un problème est rencontre, on l'affiche et les contrÃ´les sont arrÃªtés.
 Un formulaire ne peut Ãªtre soumis tant que des problÃªmes sont détectés.

 Retour true si saisie correcte sur tout le formulaire, false sinon

 Utilisation :
 Cette fonction est systématiquement appelée lors de la soumission d'un formulaire
 (évènement onSubmit du formulaire ou onClick du bouton déclenchant la soumission du formulaire)
 */
{
   var i;
   if(debug) alert('Controles Début --> valideFormulaire');
   for(i = 0; i < frm.length; i++)
   {
      targetElement = frm[i];
      if(!valideChamp(targetElement))
      {
         return false;
      }
   }
   if(debug) alert('Controles OK --> Soumission formulaire');
   return true;
}
/******************************************************************************/

function valideChamp(targetElement)
/*
 Role: Validation de la saisie effectuée sur un champ

 Parametres :
 - targetElement: Element a controler

 Retour true si controles OK, false sinon
 */
{
   var msgErreur = '';
   // Lecture des attributs
   // IdF_Mandatory : Le champ est obligatoire si 2 ou 3
   // IdF_Text : Description associee a l'element
   IdF_Mandatory = targetElement.getAttribute("IdF_Mandatory");
   IdF_Text = targetElement.getAttribute("IdF_Text");
   if(debug) alert("Elmt M=" + IdF_Mandatory + " T=" + IdF_Text);
   // Controle que les champs obligatoires du formulaire sont renseignes
   if(estVide(targetElement))
   {
      if((IdF_Mandatory == 2) || (IdF_Mandatory == 3))
      {
         msgErreur = 'Le champ ['+IdF_Text + '] est obligatoire.\n';
         alert(msgErreur);
         focusElement(targetElement);
         return(false);
      }
   }
   return(true);
}
/******************************************************************************/

function estVide(targetElement)
/*
 Role: Verifie qu'un element n'est pas vide (le controle doit implementer la methode value)
 
 Parametres:
 - targetElement: element a controler

 Retour: true si champ vide, false sinon
 */
{
   return(estValeurVide(targetElement.value));
}
/******************************************************************************/

function estValeurVide(valeur)
/*
 Role: Verifie qu'une valeur n'est pas vide

 Parametres:
 - valeur: valeur a controler

 Retour: true si valeur vide, false sinon
 */
{
   if(valeur == null)
   {
      return(true);
   }
   else 
   {
   		var regExpBeginning = /^\s+/;
		var regExpEnd = /\s+$/;  

		// return((valeur).replace(/[ ]/g, '') == '');
	    return (valeur.replace(regExpBeginning, "").replace(regExpEnd, "")== '');
	}
}
/******************************************************************************/

function focusElement(targetElement)
/*
Role : Donne le focus a l'element passe en parametre s'il est accessible
parametres :
- targetElement  : element a controler
 */
{
   if(!targetElement.disabled)
   {
      targetElement.focus();
      if(targetElement.type == "text")
      {
         targetElement.select();
      }
   }
}
/******************************************************************************/
function setDate(input) {
/*
Role : Affecte la date du jour Ã  l'element passe en parametre
parametres :
- input  : element a controler
 */
	if (input.value == '' || input.value == null) {
		today = new Date ();
		input.value = today.getDate()+"/"+(today.getMonth()+1)+"/"+(today.getFullYear());  
	}
}
/******************************************************************************/
function setSelectedDateItem(selectInput) {
/*
Role : Affecte la date du jour Ã  l'element dont l'Id correspond a l'option 
selectionnee dans l'element selectInput passe en parametre
parametres :
- selectInput  : select de reference
 */
	for (i = 0; i < selectInput.length; i++) {
		if (selectInput[i].selected) {
			if(debug) alert(selectInput[i].value);
			var x = document.getElementsByTagName('input');

			for (var j=0;j<x.length;j++) {
				 if (x[j].getAttribute('prop_id') == selectInput[i].value){
					  if(debug) alert(x[j].getAttribute('prop_id'));
					  setDate( x[j]  );
				}
			}
			
			/*if (document.getElementsByTagName( selectInput[i].value ) != null) {	
				setDate( document.getElementById( selectInput[i].value ) );
			}*/
		}
	}
} 

/******************************************************************************/
function replaceAll(text, oldString, newString) {
/*
Role : remplace toutes les occurences de oldString par newString dans le champ input
- input  : input  de reference
- oldString : chaine a chercher
- newString : chaine de remplacement
retourne la chaÃ®ne modifiée
 */
	return  String(text).replace(new RegExp(oldString,"g"), newString);
}

/******************************************************************************/
function nbStrip(nbInput) {
/*
Role : remplace les blancs, virgules et caractères â‚¬ dans le champ (pour obtenir un format correct de nombre)
- nbInput  : input  de reference
 */
	nbInput.value = String(nbInput.value).replace(new RegExp(",","g"), ".");
	nbInput.value = String(nbInput.value).replace(new RegExp("â‚¬","g"), "");
	nbInput.value = String(nbInput.value).replace(new RegExp(" ","g"), "");
	}
/******************************************************************************/
function nbStripVal(nbInput) {
/*
Role : remplace les blancs, virgules et caracteres euro  dans le champ (pour obtenir un format correct de nombre)
 */
	nbInput = String(nbInput).replace(new RegExp(",","g"), ".");
	nbInput = String(nbInput).replace(new RegExp("â‚¬","g"), "");
	nbInput = String(nbInput).replace(new RegExp(" ","g"), "");
	
	return nbInput;
	}

function mult2(x,y) {
/*
Role : renvoie le résultat de la multiplication des valeurs des deux élements 
envoyés en paramètre, Ã  deux décimales
 */
 	a = document.getElementById(x);
 	b = document.getElementById(y);
	if (a.value == '' || b.value == null) {
		return 0;
	} else {
		nbStrip(a);
		nbStrip(b);
		a=String(a.value);
		b=String(b.value);
		
		var deciA =  (a.split('\.')[1] != null )?a.split('\.')[1].length:0;
		var deciB =  (b.split('\.')[1] != null )?b.split('\.')[1].length:0;
		var c= Number(a) * Number(b);
		if (c.toString() == "NaN") return 0;
		var expo= (Math.pow(10,Math.max(deciA,deciB)+1))
		var result=((Math.round(c*expo)/expo).toFixed(2));
		return result;
		
	}
}
/******************************************************************************/
function som2(a,b) {
/*
Role : renvoie le résultat de la somme des valeurs des deux elements en parametre, 
a deux decimales
 */
	if (a.value == '' || a.value == null) a.value = 0;
	if (b.value == '' || b.value == null) b.value = 0;

	nbStrip(a);
	nbStrip(b);
	
	a=String(a.value);
	b=String(b.value);
	c= Number(a) + Number(b);
	
	return c.toFixed(2);	
}
/******************************************************************************/
function pourcent(x,y, nbdec) {
	/*
	Role : renvoie le pourcentage x/y  a [nbdec] decimales
	 */
		if (x=='' || isNaN(x) || y=='' || y==0 ||isNaN(y)) return "?" ;
		
		x = parseFloat(nbStripVal(x));
		y = parseFloat(nbStripVal(y));
		
		var z = x / y ;
		if (z.toString() == "NaN") return "?";
		
		return z.toFixed(nbdec);
			
		
	}

/******************************************************************************/
function som4Elmt(a,b,c,d) {
	
	if (document.getElementById(a).value == '' 
		|| document.getElementById(a).value == null) 
			document.getElementById(a).value = 0;
	if (document.getElementById(b).value == '' 
		|| document.getElementById(b).value == null) 
			document.getElementById(b).value = 0;
	if (document.getElementById(c).value == '' 
		|| document.getElementById(c).value == null) 
			document.getElementById(c).value = 0;
	if (document.getElementById(d).value == '' 
		|| document.getElementById(d).value == null) 
			document.getElementById(d).value = 0;

	nbStrip(document.getElementById(a));
	nbStrip(document.getElementById(b));
	nbStrip(document.getElementById(c));
	nbStrip(document.getElementById(d));
	
	aa=String(document.getElementById(a).value);
	bb=String(document.getElementById(b).value);
	cc=String(document.getElementById(c).value);
	dd=String(document.getElementById(d).value);
	
	if (!isNaN(Number(aa) + Number(bb)+ Number(cc)+ Number(dd))) {
		res = Number(aa) + Number(bb)+ Number(cc)+ Number(dd);
		res = res.toFixed(2);
	} else {
		res = "";
	}
	
	return res;	
}
/******************************************************************************/
/******************************************************************************/

function majuscule(s) {
/*
Role : renvoie le texte en majuscules
 */
	//cleanAccents(s);
	str=String(s.value);
	s.value = str.toUpperCase();
}
/******************************************************************************/
function firstMajuscule(s) {
/*
Role : renvoie la première lettre du texte en majuscules
 */
	str=String(s.value);
	first= str.substr(0,1);
	rest= str.substr(1);
	
	s.value =  first.toUpperCase() + rest;
}
/******************************************************************************/
function cleanAccents(s) {
/*
Role : remplace les caractères accentués
*/
	regA= new RegExp("Ã¢","Ã£","Ã¡","Ã ","Ã¤","Ã¥","Ã„","","","","");
	regC= new RegExp("Ã§","Ã‡","Ã§");
	regE= new RegExp("è","é","Ãª","Ã«","Ãª","é","è","ÃŠ");
	regI= new RegExp("Ã¬","Ã­","Ã®","Ã¯","ÃŒ","ÃŽ","Ã�","Ã�");
	regN= new RegExp("Ã±");
	regU= new RegExp("Ã¹","Ãº","Ã»","Ã¼","Ã™","Ãš","Ã›","Ãœ","Ã¹");
	regY= new RegExp("Å¸","Ã�","Ã¿","Ã½");
	regO= new RegExp("Ã²","Ã³","Ã´","Ãµ","Ã¶","Ã’","Ã“","Ã”","Ã•","Ã–");

	s.value = String(s.value).replace(regA, "a");
	s.value = String(s.value).replace(regC, "c");
	s.value = String(s.value).replace(regE, "e");
	s.value = String(s.value).replace(regI, "i");
	s.value = String(s.value).replace(regN, "n");
	s.value = String(s.value).replace(regU, "u");
	s.value = String(s.value).replace(regY, "y");
	s.value = String(s.value).replace(regO, "o");
}
/******************************************************************************/
function ctrlConventionET(s) {
/*
Role : controle le code convention emploi-tremplin
 */
 	
	s.value = String(s.value).replace(new RegExp(" ","g"), "");
	str=String(s.value);
	val= str.substr(0,2);
	if (val.indexOf("ET") != 0 ) {  
		alert("Le code convention doit commencer par ET");
		this.focus();
		return ;
	}
	/*
	val= str.substr(2,2);
	if (val != "ET") {
		alert("Le code convention doit comporter deux chiffres après ET correspondant Ã  l'année");
		return ;
	}
	val= str.substr(4,2);
	if (val != "ET") {
		alert("Le code convention doit commencer par ET");
		return ;
	}
	val= str.substr(0,2);
	if (val != "ET") {
		alert("Le code convention doit commencer par ET");
		return ;
	}
	*/
}
/******************************************
Fonctions javascript de gestion de l'affichage
des div en mode onglets
******************************************/

/******************************************
affiche le DIV 
******************************************/	
function switchDiv(divName,imgName) { 
	if (document.getElementById(divName).style.display == 'none') {
		show(divName);
		document.getElementById(imgName).src='theme/collapse.gif';
	} else {
		hide(divName);
		document.getElementById(imgName).src='theme/expand.gif';
	}
}
/******************************************
affiche le DIV 
******************************************/	
function show(divName) { 
	document.getElementById(divName).style.visibility="visible";
	document.getElementById(divName).style.display="block";
	
}
/******************************************
affiche tous les DIV de la page
******************************************/		
function showAll (){
	parasInDiv = document.getElementsByTagName("div");
	for(var i = 0; i < parasInDiv.length; i++) {
    	parasInDiv[i].style.visibility="visible";
		parasInDiv[i].style.display="block";
	}
}
/******************************************
cache le DIV 
******************************************/	
function hide(divName) { 
	document.getElementById(divName).style.visibility="hidden";
	document.getElementById(divName).style.display="none";
	
}

/******************************************
cache tous les DIV de la page
******************************************/
function hideAll (start){
	parasInDiv = document.getElementsByTagName("div");
	for(var i = 0; i < parasInDiv.length; i++) {
		
		if (parasInDiv[i].id.substr(0,3) == start) {
    		parasInDiv[i].style.visibility="hidden";
			parasInDiv[i].style.display="none";
		}
	}
}

/******************************************
affiche le div donn? en param?tre
cache tous les DIV de la page dont l'id commence par "tab_"
******************************************/
function showTab(idTab) {
	divElements = document.getElementsByTagName("div");
	
	for(var i = 0; i < divElements.length; i++) {
		var curDivId = new String(divElements[i].getAttribute('id'));
		if ( curDivId.substr(0,4) == "tab_") {
	    	if (curDivId == idTab) {
	    		show (curDivId);
	    	} else {
	    		hide (curDivId);
	    	}
	   	}
	}
}	
/******************************************
affiche tous les DIV de la page dont l'id commence par "tab_"
******************************************/
function showAllTab() {
	divElements = document.getElementsByTagName("div");
	
	for(var i = 0; i < divElements.length; i++) {
		var curDivId = new String(divElements[i].getAttribute('id'));
		if ( curDivId.substr(0,4) == "tab_") {
	    		show (curDivId);
	    		document.getElementById(curDivId).style.position="relative";
	    		
	   	}
	}
}	
/******************************************
Role : renvoie le resultat de la somme des valeurs des deux elements en parametre, 
a deux decimales
******************************************/
function directSom2(a,b) {
	if (a == '' || a == null) a = 0;
	if (b == '' || b == null) b = 0;

	a=String(nbStripVal(a));
	b=String(nbStripVal(b));
	c= Number(a) + Number(b);
	
	return c.toFixed(2);	
}
/******************************************
calcul notation échelle région
******************************************/
function calculNote() {
 var tab = [16,16];
 tab.push(16, 16.25, 16.50, 16.75);
 tab.push(17, 17.25, 17.50, 17.75);
 tab.push(18, 18.25, 18.50, 18.75);
 tab.push(19, 19.10, 19.20, 19.30, 19.40, 19.50, 19.60, 19.70, 19.80, 19.90);
 tab.push(20,20,20);
 
 note = document.getElementById('fol_teg00017q00045p00066').value;
 if (debug) alert('note= '+note);
 note = Number(String(nbStripVal(note)));
 
 if (debug) alert('note= '+note);
 
 var i=-1;
 for (var j=0; j<tab.length; j++){
	if (tab[j]== note) {
		i=j ;
		break;
	}
 }

 if (debug) alert('i= '+i);
 
 /* cas du 16 */
 if (i==0) i=2;
 
 if (i>=2) {
  document.getElementById('note1').innerHTML = tab[i];
  document.getElementById('note2').innerHTML = tab[i+1];
  document.getElementById('note3').innerHTML = tab[i+2];	
  document.getElementById('note4').innerHTML = tab[i-1];
  document.getElementById('note5').innerHTML = tab[i-2];
 } else {
  document.getElementById('note1').innerHTML = '?';
  document.getElementById('note2').innerHTML = '?';
  document.getElementById('note3').innerHTML = '?';
  document.getElementById('note4').innerHTML = '?';
  document.getElementById('note5').innerHTML = '?';
 }
}
/****************************************************************/
// cette fonction est appelée Ã  chaque fois que l'on tape sur une touche (onkeypress,onkeyup), que l'on quite un champ(onblur).
// l'aiguillage se fait Ã  l'intérieur.
// A savoir: si l'élément d'éclencheur de l'évenemment a un attibut nomé 'control_length' et qu'il est mis Ã  1, alors on traite l'évenement,
// sinon, on passe la main
function verif_textarea_length(e)
{
var target = e.target || e.srcElement; // récupération de la cible
var is_something_wrong = false; // initialisation variable

if ((target.attributes) && (target.attributes['control_length']) && (target.attributes['control_length'].value = '1')) // 'control_length'?
{
max = target.attributes['maxlength'].value; //récupération de la valeur maxlength de la cible
id_textarea = target.attributes['id'].value; //récupération de la valeur id de la cible
id_control_cell = target.attributes['id_control_cell'].value; //récupération de la valeur id de la div pour le compteur

textarea = document.getElementById(id_textarea); //assignation du textarea dans un objet plus accessible
if (textarea.value.length > max) //est-ce que la taille du texte est trop grande?
{
//oui, on affiche un message et on découpe ce qui traine après la limite
alert('Vous ne pouvez saisir que '+ max +' caracteres maximum');
textarea.value=textarea.value.substring(0,max);
is_something_wrong = true; // on prévient Ã  la sortie que qque chose ne s'est pas bien passé
}
show_counter_value(id_control_cell,textarea.value.length); //on affiche la taille du texte
}
return (!is_something_wrong); //retour Ã  l'envoyeur
}

// affiche le nombre de caractères du textarea
//rien de bien compliqué
function show_counter_value(id_control_cell,counter_value)
{
control_cell_div = document.getElementById(id_control_cell);
control_cell_div.innerHTML = counter_value ; // on met Ã  jour le champs de contrÃ´le.
return true;
}

//mise en place des gachettes (triggers en anglais)
function set_callback_events()
{
if (document.addEventListener){
// alert('FF');
document.addEventListener('keypress', verif_textarea_length, false);
document.addEventListener('keyup', verif_textarea_length, false);
document.addEventListener('blur', verif_textarea_length, false);
} else if (document.attachEvent){
// alert('IE');
document.attachEvent('onkeypress', verif_textarea_length);
document.attachEvent('onkeyup', verif_textarea_length);
document.attachEvent('onblur', verif_textarea_length);
}
}
/**********************************************************************/

function validLock() {
return confirm ("Attention ! Les informations  ne seront plus modifiables apres transmission a la Region.  Confirmez-vous le transfert ?");
}


