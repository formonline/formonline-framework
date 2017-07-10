/*
 * Créé le 18 oct. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.msBeans;

import java.io.UnsupportedEncodingException;

import com.triangle.lightfw.ValueBean;

/**
 * @author S. Leridon
 *
 */
public class Stat extends ValueBean {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String titre;
	private int num;
	private int val;
    private int total;
    private String comment;
    private String type;
	
    private int pid=-1;
    private int qid=-1;
    private int fid=-1;
    
    public static String SESSION_NAME = "stat";
    public static String SESSION_LIST = "stats";
    
    public static String TYPE_SUM ="S";
    public static String TYPE_COUNT ="C";
    public static String TYPE_OCCURENCE ="O";
    
    
    

    public Stat() {      
        comment="";
    }
    
    public Stat(String titre, int val) {
		
		setTitre(titre);
		setVal(val);
	}
    
    public Stat(String titre, int num, int val, int total, String comment) {      
        this.titre = titre;
        this.num = num;
        this.val = val;
        this.total = total;
        this.comment = comment;
    }
	
	public String toXML() throws UnsupportedEncodingException {
		String s="<stat ";
		s += " titre=\"" + titre +"\"";
		s +=">";
		s+= val;
		s += "</stat>";
		
		return s;
	}
    
    public String toString(){
        String s="titre=" + titre +" | ";
        s += " num=" + num  +" | ";
        s += " val=" + val  +" | ";
        s += " total=" + total  ;
        
        return s;
    }   
    
    public String toJSON(){
        String s="{\"titre\":\"" + titre +"\",";
        s += "\"num\":\"" + num  +"\",";
        s += "\"val\":\"" + val  +"\",";
        s += "\"total\":\"" + total +"\"}" ;
        
        return s;
    }

    /**
     * @return Returns the comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment The comment to set.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return Returns the num.
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num The num to set.
     */
    public void setNum(int num) {
        this.num = num;
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
     * @return Returns the total.
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total The total to set.
     */
    public void setTotal(int total) {
        this.total = total;
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

    /**
     * @return the pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

    /**
     * @return the qid
     */
    public int getQid() {
        return qid;
    }

    /**
     * @param qid the qid to set
     */
    public void setQid(int qid) {
        this.qid = qid;
    }

    /**
     * @return the fid
     */
    public int getFid() {
        return fid;
    }

    /**
     * @param fid the fid to set
     */
    public void setFid(int fid) {
        this.fid = fid;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    

}
