/*
 * Créé le 18 oct. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.msBeans;

import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;

import formOnLine.BasicTools;

import com.triangle.lightfw.ValueBean;



/**
 * @author SLE
 *
 */
public class Reponse extends ValueBean{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int s_id =0;
	private int p_id =0;
	private String sVal ="" ;
    private int val = 0;
	
    
/**
 * constructeur
 */
public Reponse () {}

public Reponse (int sid, int pid, String s, int ival) {
	s_id=sid;
	p_id=pid;
	sVal=s;
    val= ival;
}
	
	public String toXML() throws UnsupportedEncodingException {
		String s="<reponse ";
		s+= "id=\"" + String.valueOf(p_id) + "\">";
		
		//s+= URLEncoder.encode(sVal,FormatExport.XML_ENCODE);
		s+= BasicTools.xmlEncode(sVal);
		s+= "</reponse>";
		return s;
	}
	/**
	 * export JSON
	 * 
	 * @return un objet JSON correspondant à la réponse
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
	    JSONObject json = new JSONObject();
	    json.put("id", p_id);
	    json.put("val",  sVal);
        return json;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "sid="+s_id+" pid="+p_id+" Val="+sVal;
    }
	/**
	 * @return Returns the p_id.
	 */
	public int getP_id() {
		return p_id;
	}
	/**
	 * @param p_id The p_id to set.
	 */
	public void setP_id(int p_id) {
		this.p_id = p_id;
	}
	/**
	 * @return Returns the s_id.
	 */
	public int getS_id() {
		return s_id;
	}
	/**
	 * @param s_id The s_id to set.
	 */
	public void setS_id(int s_id) {
		this.s_id = s_id;
	}
	/**
	 * @return Returns the sVal.
	 */
	public String getSVal() {
		return sVal;
	}
	
	public String printVal() throws UnsupportedEncodingException {
		if (sVal==null) return ""; 
		return BasicTools.htmlConvert(sVal);
	}
	/**
	 * @param val The sVal to set.
	 */
	public void setSVal(String val) {
		sVal = val;
	}
    /**
     * @return Returns the val.
     */
    public int getVal() {
        return val;
    }
    /**
     * @param val The val to set.
     */
    public void setVal(int val) {
        this.val = val;
    }
    
    /** deep copy de la réponse 
     * @return une nouvelle reponse
     */
    public Reponse copy() {
        Reponse newRep = new Reponse(-1, this.p_id, this.sVal, this.val);
        return newRep;
    }
}
