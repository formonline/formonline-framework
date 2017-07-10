/*
 * Créé le 18 oct. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package formOnLine.msBeans;



import formOnLine.BasicTools;

import com.triangle.lightfw.ValueBean;

/**
 * @author S. Leridon
 *
 */
public class Captcha extends ValueBean {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String value;
	
    public static String NAME = "captcha";
    
    public boolean isCorrect(String userValue) {
        if (userValue.equals(value)) {
             return true ;
        } else { return false; }
    }

    public Captcha() {      
        value= BasicTools.getPwd();
    }



    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    
	public String toString() {
        return value;
    }
    

}
