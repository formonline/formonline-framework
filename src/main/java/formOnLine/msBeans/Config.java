/*
 * Créé le 23/12/10
 */
package formOnLine.msBeans;

import java.io.UnsupportedEncodingException;

import formOnLine.BasicTools;

import com.triangle.lightfw.ValueBean;



/**
 * @author SLE
 *
 */
public class Config extends ValueBean{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int c_id =-1;
	private String c_type ="";
    private String c_param ="" ;
    private String c_val ="" ;
    private String c_texte ="" ;
    
    public static String SESSION_NAME = "params";
	
    
public Config () {}

	
	public String toXML() throws UnsupportedEncodingException {
		String s="<config ";
		s+= "id=\"" + String.valueOf(c_id) + "param=\"" + c_param +"\">";
		
		//s+= URLEncoder.encode(sVal,FormatExport.XML_ENCODE);
		s+= BasicTools.xmlEncode(c_val);
		s+= "</config>";
		return s;
	}
    
    public String toString() {
        return "c_id="+c_id+" c_type="+c_type+" c_param="+c_param+" c_val="+c_val;
    }
	
	
	
	public String printVal() throws UnsupportedEncodingException {
		if (c_val==null) return ""; 
		return BasicTools.htmlConvert(c_val);
	}


    /**
     * @return Returns the c_id.
     */
    public int getId() {
        return c_id;
    }


    /**
     * @param c_id The c_id to set.
     */
    public void setId(int c_id) {
        this.c_id = c_id;
    }


    /**
     * @return Returns the c_param.
     */
    public String getParam() {
        return c_param;
    }


    /**
     * @param c_param The c_param to set.
     */
    public void setParam(String c_param) {
        this.c_param = c_param;
    }


    /**
     * @return Returns the c_type.
     */
    public String getType() {
        return c_type;
    }


    /**
     * @param c_type The c_type to set.
     */
    public void setType(String c_type) {
        this.c_type = c_type;
    }


    /**
     * @return Returns the c_val.
     */
    public String getVal() {
        return c_val;
    }
    

    /**
     * @param c_val The c_val to set.
     */
    public void setVal(String c_val) {
        this.c_val = c_val;
    }


    /**
     * @return Returns the c_texte.
     */
    public String getTexte() {
        return c_texte;
    }


    /**
     * @param c_texte The c_texte to set.
     */
    public void setTexte(String c_texte) {
        this.c_texte = c_texte;
    }
	
}
