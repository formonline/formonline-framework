package formOnLine;


import java.math.BigDecimal;
import java.math.BigInteger; 

import com.triangle.lightfw.BasicType;

import formOnLine.BasicTools;


/**
 * @author SLE + Abb
 *
 */
public class Controls { 

    public final static String MESSAGE_DATE_INVALIDE =
            "Erreur > Date invalide (format attendu : jj/mm/aa)";
    public final static String MESSAGE_DATE_DEBUT_INVALIDE =
            "Erreur > Date de début invalide (format: jj/mm/aa)";
    public final static String MESSAGE_DATE_FIN_INVALIDE =
            "Erreur > Date de fin invalide (format: jj/mm/aa)";
    public final static String MESSAGE_DATES_OBLIGATOIRES =
            "Erreur > Les deux champs DATE doivent être renseignés.";
    public final static String MESSAGE_DATE_DEBUT_SUP_FIN =
            "Erreur > La date de fin doit être postérieure à la date de début.";
    public final static String MESSAGE_INT_INCORRECT =
            "Erreur > Chiffre entier sans virgule attendu. ";
    public final static String MESSAGE_EMAIL_INCORRECT =
            "Erreur > Adresse Mail incorrecte.";
    private final static String MSG_SIRET=
            "Erreur > N°SIRET incorrect.";
    private final static String MSG_RIB=
            "Erreur > N°RIB incorrect.";
    private final static String MESSAGE_EURO_INCORRECT=
            "Erreur > Montant en Euro incorrect.";
    public final static String MESSAGE_DOUBLE_INCORRECT =
            "Erreur > Nombre attendu. ";

    private static final BigDecimal ibanCheckingConstant = new BigDecimal(97);
    
    /**
     * contrôle si la chaîne est nulle, chaîne vide, ou juste des blancs
     * 
     * @param s
     * @return vrai si blanc ou null ou vide
     */
    public static boolean isBlank(String s) {
        return (s == null || s.trim().equals(""));
    }

    /**
     * Contrôle de la date 
     * 
     * @param s
     * @param bMandatory indicateur date obligatoire
     * @return vrai si la date est correcte 
     */
    public static String checkDate(String s, boolean bMandatory) {
        // cas date obligatoire
        if (isBlank(s) && bMandatory) return MESSAGE_DATE_INVALIDE;

        String formatedDate = BasicType.parseDateIsoFromLocal(s, null);
        if (!isBlank(s) && formatedDate == null) return MESSAGE_DATE_INVALIDE;

        return  null;
    }

    /**
     * Contrôle de la date  (date non obligatoire)
     * 
     * @param s date
     * @return vrai si date correcte
     */
    public static String checkDate(String s) {
        return checkDate(s,false);
    }

    /**
     * Contrôle d'une plage de dates
     * 
     * @param date1
     * @param date2
     * @param bMandatory
     * @return
     */
    public static String checkDates(String date1, String date2, boolean bMandatory)
    {
        // cas dates obligatoires
        if ((isBlank(date1) || isBlank(date2))  && bMandatory)
            return MESSAGE_DATES_OBLIGATOIRES;

        // cas date 1 incorrecte
        String dateDebut = BasicType.parseDateIsoFromLocal(date1,null);
        if (!isBlank(date1) && dateDebut == null) 
            return MESSAGE_DATE_DEBUT_INVALIDE;

        // cas date 2 incorrecte
        String dateFin = BasicType.parseDateIsoFromLocal(date2,null);
        if (!isBlank(date2) && dateFin == null) return MESSAGE_DATE_FIN_INVALIDE;

        /*/ cas date2 > date1
	    if (dateFin != null && dateDebut != null && 
					dateFin.compareTo(dateDebut)>0) 
	    	return MESSAGE_DATE_DEBUT_SUP_FIN;  
         */
        return null;
    }


    /**
     * contrôle d'une plage de dates (non obligatoires)
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static String checkDates(String date1, String date2) {
        return checkDates( date1, date2, false);
    }

    /**
     * contrôle d'un int 
     * 
     * @param i
     * @param bMandatory indicateur valeur obligatoire
     * @return
     */
    public static String checkInt(String i, boolean bMandatory) {

        if (bMandatory && isBlank(i))
            return MESSAGE_INT_INCORRECT;

        if (!isBlank(i)) {
            try {
                Integer.parseInt(i);
            } catch (NumberFormatException nfe) {
                return MESSAGE_INT_INCORRECT;
            }
        }

        return null;
    }

    /**
     * contrôle d'un int 
     * 
     * @param i
     * @return
     */
    public static String checkInt(String i) {
        return checkInt( i, false);
    }

    /**
     * contrôle d'un double 
     * 
     * @param i
     * @return
     */
    public static String checkDouble(String i) {
        return checkDouble( i, false);
    }

    /**
     * contrôle d'un Double 
     * 
     * @param i
     * @param bMandatory indicateur valeur obligatoire
     * @return
     */
    public static String checkDouble(String s, boolean bMandatory) {

        if (bMandatory && isBlank(s))
            return MESSAGE_DOUBLE_INCORRECT;

        if (!isBlank(s)) {
            try {
                Double dd=  Double.parseDouble(s);
            } catch (NumberFormatException nfe) {
                return MESSAGE_DOUBLE_INCORRECT;
            }
        }

        return null;
    }

    /**
     * contrôle d'un montant en euros
     * 
     * @param s
     * @param bMandatory
     * @return
     */
    public static String checkEuro(String s, boolean bMandatory) {

        if (bMandatory && isBlank(s))
            return MESSAGE_EURO_INCORRECT;

        if (!isBlank(s)) {
            try {
                s = BasicTools.cleanEuroValue(s);
            } catch (NumberFormatException nfe) {
                return MESSAGE_EURO_INCORRECT;
            }
        }

        return null;
    }

    /**
     * contrôle d'un montant en euros
     * 
     * @param s
     * @return
     */
    public static String checkEuro(String i) {
        return checkEuro( i, false);
    }


    /**
     * Contrôle validité email :
     * - présence un seul "@"
     * - présence au moins un "."
     * - non présence des caractères ",;:& #/?=+<>"
     * 
     * @param s : l'adresse email
     * @param bMandatory : adresse obligatoire si TRUE
     * @return null si ok, un message d'erreur sinon
     */
    public static String checkEmail(String s, boolean bMandatory) {
        //null
        if (isBlank(s) && !bMandatory) return null;
        if (isBlank(s) && bMandatory) return MESSAGE_EMAIL_INCORRECT;


        // les contrôles
        int pos = -1;

        //blancs,;:&/?=
        if (s.indexOf(" ")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf(",")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf(";")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf(":")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf("&")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf("#")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf("/")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf("?")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf("=")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf("+")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf("<")>=0) return MESSAGE_EMAIL_INCORRECT;
        if (s.indexOf(">")>=0) return MESSAGE_EMAIL_INCORRECT;

        //@
        pos = s.indexOf("@");
        if (pos < 0) return MESSAGE_EMAIL_INCORRECT;
        pos = s.indexOf("@",s.indexOf("@")+1);
        if (pos >= 0) return MESSAGE_EMAIL_INCORRECT;

        //.
        pos = s.indexOf(".");
        if (pos < 0) return MESSAGE_EMAIL_INCORRECT;


        //ok
        return null;
    } 

    /**
     * Contrôle validité email :
     * - présence un seul "@"
     * - présence au moins un "."
     * - non présence des caractères ",;:& #/?=+<>"
     * 
     * @param s : l'adresse email
     * @return null si ok, un message d'erreur sinon
     */
    public static String checkEmail(String s) {
        return checkEmail(s,false);
    }

    /** ALGORITHME de contrôle SIRET
     * 
     * Les numéros SIREN, NIC et SIRET intègrent le mécanisme de contrôle
     * de parité connu sous le nom de formule (ou clé) de Luhn. Ce contrôle
     * de parité peut être utilisé pour réduire les risques d'erreurs de 
     * frappe ou de transmission. 
     * L'algorithme consiste à parcourir les chiffres constituant 
     * le numéro SIREN, NIC ou SIRET, à multiplier par 2 les chiffres
     * de rang impair et à faire ensuite la somme de tous les chiffres 
     * obtenus.
     * Attention, il s'agit bien de faire la somme des chiffres obtenus :
     * si après multiplication par 2 d'un des chiffres du numéro on 
     * obtient "12", il faudra ajouter les chiffres 1 puis 2 et non 
     * le nombre 12 (= 1 + 2 = 12 -9)
     * Le contrôle de parité est correct si le résultat obtenu est 
     * un multiple de 10.
     * 
     * exemple : 38836317800020
     * 3   8   8   3   6   3   1   7   8   0   0   0   2   0
     * 6 + 8 + 7 + 3 + 3 + 3 + 2 + 7 + 7 + 0 + 0 + 0 + 4 + 0 = 50 = 5x10 ->ok
     * 
     *    
     * @param s : le siret
     * @param bMandatory : présence obligatoire si TRUE
     * @return null si ok, un message d'erreur sinon
     */
    public static String checkSiret(String s, boolean bMandatory) {

        //	  	null
        if (s==null && !bMandatory) return null;
        if (s==null && bMandatory) return MSG_SIRET;

        //controle (normalement il y a 14 chiffres, mais c'est pas sûr)
        if (s.length() < 9) return MSG_SIRET;
        int som = 0;
        char ch  ;

        for (int i = 0; i < s.length(); i++) {
            ch = s.charAt(i);

            int digit = -1;
            try {
                digit = Integer.parseInt(String.valueOf(ch));
            } catch (NumberFormatException e) {
                return MSG_SIRET;
            }

            if (i%2 == 0) digit = digit *2;
            if (digit>9) digit = digit -9;

            som +=  digit;
        }

        // contrôle de la clé
        if (som %10 != 0) return MSG_SIRET;

        // ok
        return null;

    }

    public static String checkSiret(String s) {
        return checkSiret(s,false);
    }

    /** fonction de contrôle du RIB
     * rib de test : 00000000000000000000097
     * 
     * @param rib
     * @return null si ok (un message d'erreur sinon)
     */
    public static String checkRIB(String rib, boolean bMandatory) {

        StringBuffer extendedRib = new StringBuffer(rib.length());


        //      null
        if (rib == null && !bMandatory) return null;
        if (rib == null && bMandatory) return MSG_RIB;

        //          controle (normalement il y a 23 chiffres)
        if (rib.length() < 23) return MSG_RIB;


        for (int i = 0; i < rib.length(); i++) {
            char currentChar = rib.charAt(i);

            //Works on base 36
            int currentCharValue = Character.digit(currentChar, Character.MAX_RADIX);
            //Convert character to simple digit
            extendedRib.append(currentCharValue<10?currentCharValue:(currentCharValue + (int) StrictMath.pow(2,(currentCharValue-10)/9))%10);
        }

        boolean isOk =  new BigInteger(extendedRib.toString()).remainder(new BigInteger("97")).intValue() == 0;
        if (!isOk) return MSG_RIB; 
        /*
            int b,g,d,c,k, cle;

            try {
                b = Integer.parseInt(rib.substring(0,5));
                g = Integer.parseInt(rib.substring(5,10));
                d = Integer.parseInt(rib.substring(10,16));
                c = Integer.parseInt(rib.substring(16,21));
                k = Integer.parseInt(rib.substring(21,23));

                cle = 97 - ((89 * b + 15 * g + 76 * d + 3 * c) % 97);

                if (k != cle ) return MSG_RIB; 

            } catch (Exception e) {
                return MSG_RIB; 
            }

         */


        return null;
    }
    


    
    	
    	
        
        /**
         * 
         *  fonction de contrôle du IBAN
         *  
         * @param iban
         * @return TRUE si IBAN correct 
         */
        public static boolean checkIban(String iban) {
        	
            //En considerant que iban != null et iban.length > 4
        	
        StringBuffer sbIban = new StringBuffer(iban.substring(4));
    	sbIban.append(iban.substring(0, 4));
    	iban = sbIban.toString();
     
            StringBuilder extendedIban = new StringBuilder(iban.length());
            for(char currentChar : iban.toCharArray()){
                extendedIban.append(Character.digit(currentChar,36));
            }
     
            return new BigDecimal(extendedIban.toString()).remainder(ibanCheckingConstant ).intValue() == 1;
        }
        
    

}
