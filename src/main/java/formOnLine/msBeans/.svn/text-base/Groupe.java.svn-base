/*
 * Créé le 18 oct. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.msBeans;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Element;

import formOnLine.BasicTools;

import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.ValueBean;

/**
 * @author S. Leridon
 *
 */

public class Groupe extends ValueBean {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String PUBLIC = "PU";
    public final static String PRIVATE = "PR";
    public final static String SESSION_NAME = "groupe";
    public final static String SESSION_LIST = "liste_groupe";
    
	private String texte ;
	private String type ;
	private String titre ;
	private int num ;
	private int id ;
	private LinkedList<Question> questions  ;
    private int f_id =-1;
	
	public Groupe () {
		questions = new LinkedList<Question>();
	}

    public String toString() {
        String s= "gid=" + id +" | ";
        s += " fid=" + f_id +" | ";
        s += " type=" + type +" | ";
        s += " titre=" + titre +" | ";
        
        
        return s;
        
    }
    
    /**
     * @param e
     */
    public void setAttributes(Element e) {
        
        e.setAttribute( "id",String.valueOf(getId()));
        e.setAttribute( "titre",getTitre());
        e.setAttribute( "type",getType());
    }
    
	public String toXML() throws UnsupportedEncodingException {
		String s="<groupe ";
		s += " id=\"" + String.valueOf(id) +"\"";
		//s += " titre=\"" + URLEncoder.encode(titre,FormatExport.XML_ENCODE) +"\"";
		s += " titre=\"" + BasicTools.xmlEncode(titre) +"\"";
		s += " type=\"" + type +"\"";
		s +=">";
		if (questions!=null) {
			for (int i=0;i<questions.size();i++) {
				Question q =(Question) questions.get(i);
				s+= q.toXML();
			}
		}
		s += "</groupe>";
		
		return s;
	}
	
    public String toBalises(String fid)  {
        String s="";
        if (questions!=null) {
            for (int i=0;i<questions.size();i++) {
                Question q =(Question) questions.get(i);
                s+= q.toBalises(fid);
            }
        }
        return s;
    }
    
    /**
     * sérialisation HTML du groupe
     * 
     * @return chaîne HTML
     */
    public StringBuffer toFullHtml(boolean isComplete) 
    {
        StringBuffer s = new StringBuffer();
        s.append("<div class='titreGroupe'>").append(num).append(". ").append(titre).append("</div>") ;
        
        for (Question q:questions ) {
             
            s.append(q.toFullHtml(isComplete));
        }
        
        return s;
    }
    
    /**
     * sérialisation CSV du groupe
     * 
     * @return chaîne CSV  */
    public StringBuffer toFullCsv() 
    {
        StringBuffer s = new StringBuffer();
        
        for (Question q:questions ) {
             
            s.append( q.toCsv().toString());
        }
        
        return s;
    }
    
    /**
     * Récupération titres colonnes pour sérialisation CSV du groupe
     * 
     * @return chaîne CSV  */
    public StringBuffer getFullCsvTitles() 
    {
        StringBuffer s = new StringBuffer();
        
        for (Question q:questions ) {
             
            //s.append( BasicTools.replaceCrLf(BasicTools.cleanHTML(q.getTexte()).replaceAll(";"," / "), " / ")+ ";");
            s.append(q.getCSVTitle());
        }
        
        return s;
    }
    
    
    /**
     * sérialisatin JSON du groupe
     * 
     * @return chaîne json
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON()  {
        
        JSONObject json = new JSONObject();
        
        json.put("id",id);
        json.put("titre", getTitre());
        
        JSONArray listq = new JSONArray();
        
        if (questions!=null) {
            for (int i=0;i<questions.size();i++) {
                Question q =(Question) questions.get(i);
                listq.add( q.toJSON());
            }
        }
        
        
        json.put("questions", listq);
        
        return json;
    }
    
    
	public void addQuestion (Question q) {
		questions.add(q);
	}

	public Question getQuestion (int i) {
		return (Question)questions.get(i);
	}
	
	public Question getQuestionById (int id) {
		Question res = null;
		for (int i=0; i<questions.size(); i++) {
			Question q  = (Question)questions.get(i); 
			if (q.getId() == id) {
				res = q ;
				break;
			} 
		}
		return res;
	}
	
	public int getNbQuestions () {
		return questions.size();
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
	 * @return Returns the titre.
	 */
	public String getTitre() {
		return titre;
	}
	/**
	 * @param titre The titre to set.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

    /**
     * @return Returns the f_id.
     */
    public int getF_id() {
        return f_id;
    }

    /**
     * @param f_id The f_id to set.
     */
    public void setF_id(int f_id) {
        this.f_id = f_id;
    }

    /**
     * @return Returns the questions.
     */
    public LinkedList<Question> getQuestions() {
        return questions;
    }

    /**
     * @param questions The questions to set.
     */
    public void setQuestions(LinkedList<Question> questions) {
        this.questions = questions;
    }
}
