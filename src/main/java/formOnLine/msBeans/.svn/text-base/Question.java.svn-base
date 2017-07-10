/*
 * Créé le 18 oct. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.msBeans;


import java.io.UnsupportedEncodingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Element;

import formOnLine.BasicTools;
import formOnLine.Controls;

import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;

/**
 * @author S. Leridon
 *
 */

public class Question extends ValueBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String CHOIX_MULTIPLE = "CM";
	public final static String CHOIX_EXCLUSIF = "CE";
	public final static String NOMBRE = "NB";
	public final static String TEXTE = "TE";
	public final static String MULTILIGNE = "ML";
	public final static String READ_ONLY = "RO";
	public final static String EMAIL = "EM";
	public final static String DATE = "DA";
	public final static String EURO = "EU";
	public final static String SIRET = "SI"; 
	public final static String RIB = "RI"; 
	public final static String COMMUNE = "CO"; 
	public final static String FORM_ID = "ID"; 
	public final static String LIST_FORM = "LF"; 
	public final static String FILE = "FI"; 
	public final static String URL = "UR"; 
	public final static String UPLOAD = "UP";






	public final static int MANDATORY_NO = 0;
	public final static int MANDATORY_ALERT = 1;
	public final static int MANDATORY_YES = 2;
	public final static int MANDATORY_UNIQUE = 3;

	public final static String MANDATORY_TEXT_NO = "Non";
	public final static String MANDATORY_TEXT_ALERT = "Alerte";
	public final static String MANDATORY_TEXT_YES = "Obligatoire";
	public final static String MANDATORY_TEXT_UNIQUE = "Obligatoire & unique";

	private String texte ;
	private String type ;
	private int num ;
	private int id ;
	private int size ;
	private int mandatory ;
	private boolean search ;
	private ValueBeanList propositions  ;
	private int g_id = 0;


	public static String SESSION_NAME = "question";
	public static String SESSION_LIST = "liste_question";

	public Question () {
		propositions = new ValueBeanList();
	}

	public Question (String texte,int id,String type) {
		propositions = new ValueBeanList();
		setTexte(texte);
		setId(-1);
		setType(type);
	}

	/**
	 * @param e
	 */
	public void setAttributes(Element e) {

		e.setAttribute( "id",String.valueOf(getId()));
		e.setAttribute( "texte", BasicTools.cleanHTML(getTexte()));
		e.setAttribute( "type",getType());
		e.setAttribute( "mandatory", String.valueOf(mandatory) );
	}


	public String toXML() throws UnsupportedEncodingException {
		String s="<question ";
		s += " id=\"" + String.valueOf(id) +"\"";
		//s += " texte=\"" + URLEncoder.encode(texte,FormatExport.XML_ENCODE) +"\"";
		s += " texte=\"" + BasicTools.xmlEncode(texte) +"\"";
		s += " type=\"" + type +"\"";
		s += " mandatory=\"" + String.valueOf(mandatory) +"\"";
		s +=">";
		if (propositions!=null) {
			for (int i=0;i<propositions.size();i++) {
				Proposition p =(Proposition) propositions.get(i);
				if (!Controls.isBlank(p.getsVal())) s+= p.toXML();
			}
		}
		s += "</question>";

		return s;
	}

	public String toString(){
		String s="qid=" + String.valueOf(id) +" | ";
		s += " type=" + type +" | ";
		s += " nbProp=" + propositions.size()+" | " ;
		s += " texte=" + texte ;

		return s;
	}

	public String toBalises(String fid)  {
		String s="";

		if (type.equals(CHOIX_EXCLUSIF) || type.equals(CHOIX_MULTIPLE)) {
			return "{%F" + fid + "Q" + BasicTools.formatWith0(id,5) + "%};";
		} else {
			if (propositions!=null) {
				for (int i=0;i<propositions.size();i++) {
					Proposition p =(Proposition) propositions.get(i);
					s+= p.toBalises(fid);
				}
			}
		}


		return s;
	}


	/**
	 * sérialisation HTML de la question
	 * 
	 * @return chaîne HTML
	 */
	public StringBuffer toFullHtml(boolean isComplete)     {

		StringBuffer s = new StringBuffer();
		s.append("<div id='q" + getId() + "' class='titreQuestion'>").append(texte).append("</div>") ;

		if (type.equals(CHOIX_EXCLUSIF) || type.equals(CHOIX_MULTIPLE) || type.equals(LIST_FORM)) {
			if (isComplete) {
				// on affiche toutes les propositions, par exemple : 
				// "[x] Oui [ ] Non"
				for (ValueBean p: propositions ) {
					Proposition pp = ((Proposition)p);
					if(pp.getsVal().equals("1")) {
						if (type.equals(CHOIX_EXCLUSIF)) {
							s.append( "<span class=radioButtonChecked>" + pp.getTexte() + "</span>");								
						}
						if (type.equals(CHOIX_MULTIPLE)) {
							s.append( "<span class=puceOK>" + pp.getTexte() + "</span><br/>");
						}

					} else {
						if (type.equals(CHOIX_EXCLUSIF)) {
							s.append( "<span class=radioButtonUnchecked>" + pp.getTexte() + "</span>");								
						}
						if (type.equals(CHOIX_MULTIPLE)) {
							s.append( "<span class=puce>" + pp.getTexte() + "</span><br/>");
						}

					}
				}

			} else {
				// on n'affiche que la proposition sélectionnée, par exemple :
				// "Oui"
				for (ValueBean p: propositions ) {
					Proposition pp = ((Proposition)p);
					if(pp.getsVal().equals("1")) {
						s.append("<span class='titreQuestion' id='q" + getId() + "'>" +  pp.getTexte() + "</span>");
						if (type.equals(CHOIX_MULTIPLE)) s.append("<br/>");
					}
				}
			}
		} else { 
			for (ValueBean p: propositions ) {
				s.append( ((Proposition)p).toFullHtml( ));
			}
		}

		return s;
	}
	
	/**
	 * sérialisation CSV de la question
	 * 
	 * @return chaîne CSV
	 */
	public StringBuffer toCsv()     {

		StringBuffer s = new StringBuffer();

		if (type.equals(CHOIX_EXCLUSIF)  || type.equals(LIST_FORM)) {
			for (ValueBean p: propositions ) {
				Proposition pp = ((Proposition)p);
				boolean isFirst = true;
				if (pp.getsVal().equals("1")) {
                    if (type.equals(LIST_FORM) && !isFirst) s.append(" / ");
					s.append(  BasicTools.replaceCrLf(BasicTools.cleanHTML( pp.getTexte() ).replaceAll(";"," / "), " / ") );
					isFirst = false ;
				}
			}
			s.append(";");
		} else { 
		    
			for (ValueBean p: propositions ) {
                Proposition pp = ((Proposition)p);
			    s.append( BasicTools.replaceCrLf(BasicTools.cleanHTML( pp.getsVal() ).replaceAll(";"," / "), " / "));

	            s.append(";");
				
			}
		}

		return s;
	}

    /**
     * retourne les entêtes pour l'export CSV correspondant à la question
     * il peut y en avoir plusieurs s'il y a plusieurs propositions associées à la question
     * 
     * les "retour à la ligne" et les ";" sont remplacés par des "/"
     * 
     * @return chaîne CSV
     */
    public StringBuffer getCSVTitle()     {

        StringBuffer s = new StringBuffer();      
        
            if (type.equals(CHOIX_EXCLUSIF)  || type.equals(LIST_FORM)) {
                s.append( BasicTools.replaceCrLf(BasicTools.cleanHTML( this.getTexte()  ).replaceAll(";"," / "), " / ") + ";");
                
            } else {
                for (ValueBean p: propositions ) {
                    Proposition pp = (Proposition)p ;
                    s.append( BasicTools.replaceCrLf(BasicTools.cleanHTML( this.getTexte() + " / " + pp.getTexte() ).replaceAll(";"," / "), " / ") + ";");
                }
            }

        return s;
    }

    
	/**
	 * sérialisation JSON de la question
	 * 
	 * @return chaîne json
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON()  {

		JSONObject json = new JSONObject();

		json.put("id",id);
		json.put("titre", getTexte()) ;
		json.put("type", getType() ) ;
		json.put("num", getNum() ) ;
		json.put("mandatory" , getMandatory() ) ;
		json.put("size", getSize() ) ;

		JSONArray listp = new JSONArray();

		if (propositions!=null) {
			for (int i=0;i<propositions.size();i++) {
				Proposition p =(Proposition) propositions.get(i);
				listp.add( p.toJSON() );
			}
		}

		json.put("propositions", listp);

		return json;
	}


	public void addProposition (Proposition p) {
		propositions.add(p);
	}

	public Proposition getProposition (int i) {
		return (Proposition)propositions.get(i);
	}

	public Proposition getPropositionById (int id) {
		Proposition res = null;
		for (int i=0; i<propositions.size(); i++) {
			Proposition p  = (Proposition)propositions.get(i); 
			if (p.getId() == id) {
				res = p ;
				break;
			} 
		}
		return res;
	}

	public Proposition getPropositionByIdAndVal (int id, int val) {
		Proposition res = null;
		for (int i=0; i<propositions.size(); i++) {
			Proposition p  = (Proposition)propositions.get(i); 
			if (p.getId() == id && p.getVal()==val) {
				res = p ;
				break;
			} 
		}
		return res;
	}

	public int getNbPropositions () {
		return propositions.size();
	}


	/**
	 * @return
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @return
	 */
	public String getTexte() {
		return texte;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param i
	 */
	public void setNum(int i) {
		num = i;
	}

	/**
	 * @param string
	 */
	public void setTexte(String string) {
		texte = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * @return Returns the propositions.
	 */
	public ValueBeanList getPropositions() {
		return propositions;
	}
	/**
	 * @param propositions The propositions to set.
	 */
	public void setPropositions(ValueBeanList propositions) {
		this.propositions = propositions;
	}
	/**
	 * @return Returns the mandatory.
	 */
	public int getMandatory() {
		return mandatory;
	}
	/**
	 * @return Returns the mandatory text.
	 */
	public String getMandatoryText() {
		switch (mandatory)
		{
		case MANDATORY_NO:
			return MANDATORY_TEXT_NO;
		case MANDATORY_ALERT:
			return MANDATORY_TEXT_ALERT;
		case MANDATORY_YES:
			return MANDATORY_TEXT_YES;
		case MANDATORY_UNIQUE:
			return MANDATORY_TEXT_UNIQUE;
		default:
			return null;
		}
	}
	/**
	 * @param mandatory The mandatory to set.
	 */
	public void setMandatory(int mandatory) {
		this.mandatory = mandatory;
	}
	/**
	 * @return Returns the size.
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size The size to set.
	 */
	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * @return Returns the search.
	 */
	public boolean isSearch() {
		return search;
	}
	/**
	 * @param search The search to set.
	 */
	public void setSearch(boolean search) {
		this.search = search;
	}

	/**
	 * @return Returns the g_id.
	 */
	public int getG_id() {
		return g_id;
	}

	/**
	 * @param g_id The g_id to set.
	 */
	public void setG_id(int g_id) {
		this.g_id = g_id;
	}

	/** réinitialisation des réponses aux propositions à ""
	 * 
	 */
	public void resetPropositions() {
		for (int i=0; propositions != null && i<propositions.size(); i++) {
			Proposition p  = (Proposition)propositions.get(i); 
			p.setsVal(""); 
		}
	}

}
