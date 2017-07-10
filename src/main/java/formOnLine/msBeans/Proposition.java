/*
 * Créé le 18 oct. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.msBeans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.w3c.dom.Element;

import formOnLine.BasicTools;
import formOnLine.Controls;
import formOnLine.FormatExport;

import com.triangle.lightfw.ValueBean;

/**
 * @author SLE
 * 
 */
public class Proposition extends ValueBean {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String texte;
	private int num;
	private int id;
	private int val =0;
	private int default_val;
	private String sVal;
	private int qid;
	private String alert;
    private int stat;
    private int initload;
    
    public static String SESSION_NAME = "proposition";
    public static String SESSION_LIST = "liste_proposition";
    
    
    /**
     * @return Returns the qid.
     */
    public int getQid() {
        return qid;
    }
    /**
     * @param qid The qid to set.
     */
    public void setQid(int qid) {
        this.qid = qid;
    }
	public Proposition() {
		
	}
	
	public Proposition(int id, String texte, int num, int default_val) {
		setId(id);
		setTexte(texte);
		setNum(num);
		setDefault_val(default_val);
	}
	
    /**
     * ajout des attributs de la balise XML <proposition>
     * 
     * @param e
     */
    public void setAttributes(Element e) {
        
        e.setAttribute( "id",String.valueOf(getId()));
        e.setAttribute( "texte", BasicTools.cleanHTML(getTexte()));
        e.setAttribute( "rval" , String.valueOf(val));
    }
    
	public String toXML() throws UnsupportedEncodingException {
		String s="<proposition ";
		s += " id=\"" + String.valueOf(id) +"\"";
		//s += " texte=\"" + URLEncoder.encode(texte,FormatExport.XML_ENCODE) +"\"";
		s += " texte=\"" + BasicTools.xmlEncode(texte) +"\"";
		s +=">";
		//if (!Controls.isBlank(sVal)) s+= URLEncoder.encode(sVal,FormatExport.XML_ENCODE);
		if (!Controls.isBlank(sVal)) s+= BasicTools.xmlEncode(sVal);
		
		s += "</proposition>";
		
		return s;
	}
	
    public String toString(){
        String s="pid=" + String.valueOf(id) +" | ";
        s += " qid=" + qid +" | ";
        s += " num=" + num +" | ";
        s += " rVal=" + val + " | ";
        s += " sVal=" + sVal + " | ";
        s += " texte=" + texte ;
        
        return s;
    }
    
    public String toBalises(String fid) {
        
        return "{%F" + fid + "P" + BasicTools.formatWith0(id,5) + "%};";
    }
    
    
    /**
     * sérialisation HTML de la proposition
     * 
     * @return chaîne HTML
     */
    public StringBuffer toFullHtml()    {
        
         StringBuffer s = new StringBuffer();
         s.append("<div id='p").append(getId()).append( "' class='texteProposition'>")
         	.append("<span id='lib_p").append(getId()).append("'>").append(texte).append("</span>")
         	.append("</div>")
         	.append("<span id='val_p").append(getId()).append("'>").append(BasicTools.htmlConvert(sVal)).append("</span>");
         return s;
    }
    
    
    /**
     * sérialisatin JSON de la proposition
     * 
     * @return chaîne json
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON()  {
        
        JSONObject json = new JSONObject();
        
        json.put("id" ,id);
        json.put("titre" , getTexte() );
        json.put("num" , getNum() );
        json.put("val",getVal() );
        json.put("sVal" , getsVal() );
        
        
        return json;
    }
    
    
	/**
	 * @return
	 */
	public String getTexte() {
		return texte;
	}

	/**
	 * @param string
	 */
	public void setTexte(String string) {
		texte = string;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * @param i
	 */
	public void setNum(int i) {
		num = i;
	}

	/**
	 * @return
	 */
	public int getDefault_val() {
		return default_val;
	}

	/**
	 * @return
	 */
	public int getVal() {
		return val;
	}

	public String getsVal() {
		return sVal;
	}

	public String printVal() throws UnsupportedEncodingException {
		if (sVal==null) return ""; 
		return URLEncoder.encode(sVal,FormatExport.HTML_ENCODE);
	}

	
	/**
	 * @param i
	 */
	public void setDefault_val(int i) {
		default_val = i;
	}

	/**
	 * @param i
	 */
	public void setVal(int i) {
		val = i;
	}	
	
	public void setsVal(String s) {
		sVal = s;
	}
    
    /**
     * @return Returns the alert.
     */
    public String getAlert() {
        return alert;
    }

    /**
     * @param alert The alert to set.
     */
    public void setAlert(String alert) {
        this.alert = alert;
    }
    /**
     * @return Returns the stat.
     */
    public int getStat() {
        return stat;
    }
    /**
     * @param stat The stat to set.
     */
    public void setStat(int stat) {
        this.stat = stat;
    }
    /**
     * @return Returns the initload.
     */
    public int getInitload() {
        return initload;
    }
    /**
     * @param initload The initload to set.
     */
    public void setInitload(int initload) {
        this.initload = initload;
    }

}
