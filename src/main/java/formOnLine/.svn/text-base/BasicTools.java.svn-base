package formOnLine;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Random;

import org.apache.commons.lang.StringEscapeUtils;



/**
 * @author seleridon
 *  
 */
public class BasicTools {
    
    private static MessageDigest digester;

    static {
        try {
            digester = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cryptage MD5 
     * 
     * @param chaine à crypter
     * @return chaine cryptée
     */
    public static String crypt(String str) {
        if (str == null || str.length() == 0) {
            return "String to encript cannot be null or zero length";
        }

        digester.update(str.getBytes());
        byte[] hash = digester.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            }
            else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }

    /**
     * Récupère un int à partir d'une chaîne formatée exemple :
     * f('initG00111Q00222P00333','Q')=222
     * @param s chaîne de départ
     * @param c caractère (ou chaîne) recherché(e)
     * @return le int sur 5 digits juste après le dernier 'c'
     */
    public static int getIntFrom5DigitFormatedInputName(String s, String c)  {
        int res = -1;

        if (s.indexOf(c) >= 0)    {
            try  {

                int index;
                index = s.lastIndexOf(c) + c.length();
                String ss = s.substring(index, index + 5);
                res = Integer.parseInt(ss);
            }
            catch (Exception e)  {
                //on retourne -1 par défaut
            }
        }

        return res;
    }

    /**
     * Ajoute des 0 devant un chiffre pour faire N digits
     * Si le chiffre dépasse déjà les N digits, on renvoie le chiffre.
     * @param int chiffre à formater
     * @return int nb de digits (N)
     */
    public static String formatWith0(int x, int nb0)  {
        String res="";

        res = Integer.toString(x);

        if (nb0==0 || res.length()>=nb0) return res;
        int borne = nb0-res.length();

        for (int i=0; i< borne; i++) {
            res = "0"+res;
        }

        return res;
    }

    /**
     * Récupère une liste de données à partir d'une chaîne de données séparées
     * par un séparateur : 
     * "1;2;3" => list("1","2","3")
     * @param line chaîne de départ
     * @param s1 séparateur (ou chaîne) recherché(e)
     * 
     * @return LinkedList<String>
     */
    public static LinkedList<String> parseLineToList(String line, String s)
    {
        LinkedList<String> l= new LinkedList<String>();

        if (line == null || s == null) return null;

        // plusieurs données 
        int pos=0;
        while ((pos <= line.length() && line.indexOf(s,pos) >= 0)) {
            l.add( line.substring(pos, line.indexOf(s,pos)) );

            pos = line.indexOf(s,pos) +s.length() ;
        }

        if (!Controls.isBlank(line.substring(pos))) {
            l.add( line.substring(pos) );
        }

        return l ;

    }

    /**
     * Ajoute des caractères devant une chaîne pour faire une longueur donnée
     * Si la chaîne dépasse déjà les N char, on tronque
     * @param s chaîne à traiter
     * @param c charactere pour compléter
     * @param nb  de digits (N)
     * @param alignLeft si vrai, aligner le texte à gauche, sinon à droite
     * @return
     */
    public static String formatWithChar(String s, char c, int nb, boolean alignLeft)  {

        if (alignLeft) { //on ajoute à droite de la chaîne
            // cas ou la chaîne dépasse déjà
            if ( s.length()>=nb) return s.substring(0,nb);

            // completion
            int borne = nb - s.length();
            for (int i=0; i< borne; i++) {
                s = s + c ;
            }
        } else { // on ajoute à gauche de la chaîne
            // cas ou la chaîne dépasse déjà
            if ( s.length()>=nb) return s.substring(s.length()-nb);

            // completion
            int borne = nb - s.length();
            for (int i=0; i< borne; i++) {
                s =  c + s ;
            }
        }

        return s;
    }


    /**
     * retourne le pourcentage a/b avec nbdec décimales
     * 
     * @param a
     * @param b
     * @param nbDec
     * @return
     */
    public static String getPourcent(int a, int b, int nbDec) {
        String p="";

        if (b==0) return ".";

        double d = Math.floor(a*100/b);

        p = String.valueOf( d ) + " %";



        return p ;
    }

    /**
     * Réencode les quotes en HTML
     * 
     * @param s
     * @return
     */
    public static String encodeQuotes(String s)
    {
        if (s == null)
            return null;
        return s.replaceAll("\"", "&quot;");
    }

    /**
     * Encode les caractères interdits en XML
     * @param String texte à encoder (non null)
     * @return String encodée
     */
    public static String xmlEncode(String text)
    {
        if (text != null)
        {
            StringBuffer sb = new StringBuffer();
            int len = text.length();

            for (int i = 0; i < len; i++)
            {
                char ch = text.charAt(i);
                switch (ch)
                {
                case 60: // '<'
                    sb.append("&lt;");
                    break;
                case 62: // '>'
                    sb.append("&gt;");
                    break;
                case 38: // '&'
                    sb.append("&amp;");
                    break;
                case 34: // '"'
                    sb.append("&quot;");
                    break;
                case 39: // '\''
                    sb.append("&apos;");
                    break;
                default:
                    sb.append(ch);
                    break;
                }
            }
            text = sb.toString();
        }

        return text;
    }

    /**
     * Remplace par des espaces les caractères CR et LF
     * @param String texte à encoder (non null)
     * @return String encodée
     */
    public static String replaceCrLf(String text, String sep)
    {
        if (text != null)
        {
            StringBuffer sb = new StringBuffer();
            int len = text.length();

            for (int i = 0; i < len; i++)
            {
                char ch = text.charAt(i);
                switch (ch)
                {
                case 10: // 'LF'
                    sb.append(sep);
                    break;
                case 13: // 'CR'
                    // rien 
                    break;
                default:
                    sb.append(ch);
                    break;
                }
            }
            text = sb.toString();
        }

        return text;
    }

    /**
     * Remplace par des espaces les caractères CR et LF
     * @param String texte à encoder (non null)
     * @return String encodée
     */
    public static String replaceCrLf(String text)
    {
        return replaceCrLf(text," / ");
    }
    
    /** 
     * modif / suppression de répertoire
     */
    static public boolean moveDirectory(File oldPath, File newPath) { 
        boolean resultat = true; 

        if( !oldPath.exists() ) return false;

        File[] files = oldPath.listFiles(); 
        for(int i=0; i<files.length; i++) { 
            if(files[i].isDirectory()) { 
                // to do
                //resultat &= moveDirectory(files[i]); 
            } else { 
                if( !newPath.exists() ) newPath.mkdir();
                resultat &= moveFile(files[i], 
                        new File( newPath + File.separator + files[i].getName() )); 
            } 
        } 

        resultat &= oldPath.delete(); 
        return( resultat ); 
    } 

    /** 
     * copie le fichier source dans le fichier resultat
     * retourne vrai si cela réussit
     */
    public static boolean copyFile(String oldPath, String newPath){
        File source = new File(oldPath);
        File dest = new File(newPath);

        return copyFile(source, dest);
    }

    /** 
     * copie le fichier source dans le fichier resultat
     * retourne vrai si cela réussit
     */
    public static boolean copyFile(File source, File dest){


        FileChannel in = null; // canal d'entrée
        FileChannel out = null; // canal de sortie

        boolean success = true;

        try {
            // Init
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();

            // Copie depuis le in vers le out
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
            e.printStackTrace(); // n'importe quelle exception
            success = false;
        } finally { // finalement on ferme
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    success = false;
                }
            }
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    success = false;
                }
            }
        }
        return success;
    }


    /** déplace le fichier source dans le fichier resultat
     * retourne vrai si cela réussit
     */  
    public static boolean moveFile(String oldPath, String newPath){
        File source = new File(oldPath);
        File dest = new File(newPath);

        return moveFile(source, dest);
    }

    /** déplace le fichier source dans le fichier resultat
     * retourne vrai si cela réussit
     */
    public static boolean moveFile(File source,File destination) {
        if( !destination.exists() ) {
            // On essaye avec renameTo
            boolean result = source.renameTo(destination);
            if( !result ) {
                // On essaye de copier
                result = true;
                result &= copyFile(source,destination);
                if(result) result &= source.delete();

            } return(result);
        } else {
            // Si le fichier destination existe, on annule ...
            return(false);
        } 
    } 




    /*
     * retourne le texte compris entre les balises de commentaire HTML
     * ex : getHtmComment("xx <!-- yy --> zz") == " yy "
     */
    public static String getHtmComment(String s) {
        int start = s.indexOf("<!--");
        int end = s.indexOf("-->");
        if (start >= 0 && end > start ){
            return s.substring(start+4, end  ); 
        }

        return "";
    }

    /*
     * retourne a valeur d'un attribut (valeur entre simples cotes ')
     * ex : getAttribute("attribut", "xx <!-- attribut='valeur' --> zz") == "valeur"
     */
    public static String getAttribute(String att, String s) {
        int start = s.indexOf(att);
        int end = s.indexOf("'", start + att.length() + 2); // +2 pour prendre en compte ='
        if (start >= 0 && end > start + att.length() + 2){
            return s.substring(start+ att.length() + 2, end  ); 
        }

        return "";
    }

    /**
     * Retrait d'une balise HTML du texte
     * @param val
     * @return
     */
    static public String  cleanTag(String val, String tag) {

        if (val==null) return null;
        
        int deb = -1;
        
        // retrait balises ouvrantes
        while (val.indexOf("<"+tag, deb+1) >=0) {
            deb = val.indexOf("<"+tag);
            int fin = val.indexOf(">",deb);
            if (fin > (deb+tag.length()) ) val = val.substring(0,deb) + val.substring(fin +1);
        }

        // retrait balises fermantes (appel récursif)
        if (!tag.startsWith("/")) val = cleanTag(val, "/"+tag);

        return val; 
    }

    /**
     * Retrait des champs de fusion d'un texte de type {%xxxXxxXxxXxx%} 
     * @param val
     * @return la chaîne nettoyée
     */
    static public String  cleanFusionMarks(String val) {

        // retrait champs de fusion {%xxxXxxXxxXxx%}
        while (val.indexOf("{%") >=0) {
            int deb = val.indexOf("{%");
            int fin = val.indexOf("%}");
            if (fin > 0 && deb >=0 && fin>deb) val = val.substring(0,deb) + val.substring(fin+2);
        }

        
        return val; 
    }

    
    /**
     * Retrait des balises HTML du texte
     * @param val
     * @return
     */
    static public String  cleanHTML(String val) {


        // on retire quelques balises html
        val = cleanTag(val,"div");
        val = cleanTag(val,"a");
        val = cleanTag(val,"!--");
        val = cleanTag(val,"br");
        val = cleanTag(val,"strong");
        val = cleanTag(val,"i");
        val = cleanTag(val,"span");
        val = cleanTag(val,"script");
        return val;
    }

    
    /** réencodage caractères windows
     * @param s
     * @return string
     */
    public static String cleanWordChars(String s)
    {
        /*byte[] caracteresCP1252 = null;

        try {
            //caracteresCP1252 = s.getBytes("windows-1252"); // recuperer les valeurs ici
            caracteresCP1252 = s.getBytes("ISO-8859-1"); // recuperer les valeurs ici
        } catch (Exception e) {};


        String texteIndependantDuCharset = new String(caracteresCP1252);
        //byte[] caracteresISO88591 = texteIndependantDuCharset.getBytes("ISO-8859-1");

        return texteIndependantDuCharset; */


        s = s.replaceAll("Œ","Oe");
        s = s.replaceAll("œ","oe");
        s = s.replaceAll("–","-");
        s = s.replaceAll("—","-");
        s = s.replaceAll("‘","'");
        s = s.replaceAll("’","'");
        s = s.replaceAll("…","...");
        s = s.replaceAll("“","'");
        s = s.replaceAll("”","'");
        s = s.replaceAll("•","-");


        return s.toString(); 
    }
    /**
     * Retrait des caractères interdits pour les noms de fichier windows
     * @param val
     * @return
     */
    static public String  cleanFileNameForWindows(String fileName) {


        // on retire quelques balises html
        fileName = fileName.replaceAll("/", "_");
        fileName = fileName.replaceAll("\\\\", "_");
        fileName = fileName.replaceAll("\\*", "_");
        fileName = fileName.replaceAll("\\?", "_");
        fileName = fileName.replaceAll(":", "_");
        fileName = fileName.replaceAll("<", "_");
        fileName = fileName.replaceAll(">", "_");
        fileName = fileName.replaceAll("\\|", "_");

        return fileName;
    }



    /**
     * Encodage URL
     * @param url à encoder 
     * @return url encodée
     */
    public static String urlEncode(String s) {

        try {
            
            return StringEscapeUtils.escapeXml (URLEncoder.encode(s, "UTF-8"));
            
            /*.replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~"); //utf 8 semble obligatoire dans ce cas ? */
            
        } catch (Exception ur) {
            return s;
        }

    }

    /*
     * Nettoyage de caractères windows codé en HTML numériquement, 
     * type &#8217; (= '’' )
     * (pour pouvoir formater correctement un export CSV séparé par point-virgule)
     */
    public static String deEncode(String s) {
        String res=s;
        String escHtm="&#";
        int indexDebut,indexFin;
        String val="";

        while (res.indexOf(escHtm) >= 0)
        {

            indexDebut = res.indexOf(escHtm) + escHtm.length();
            indexFin = res.indexOf(";",indexDebut);
            try {
                val = res.substring(indexDebut, indexFin);
                res = res.replaceAll(escHtm+val+";", ""+(char)Integer.parseInt(val)); 
            } catch (Exception e) { 
                return res;
            }    

        }


        return res;
    }

    public static String cleanEuroValue(String s) {
        String res = s;
        res = res.replace('€',' ');  //symbole euro
        res = res.replace(',','.'); // virgule
        res = res.replaceAll(" ","");  //blancs
        res = res.replaceAll("Eu","");  //symbole euro
        res = res.replaceAll("EU","");  //symbole euro

        return res;
    }

    public static String utf8Encode(String s)
    {
        String res = "";
        try
        {
            res = URLEncoder.encode(s, FormatExport.XML_ENCODE);
        }
        catch (Exception e)
        {
            return e.getMessage();

        }

        return res;
    }







    /**
     * conversion d'une chaîne en HTML
     * @param s
     * @return
     */
    public static String htmlConvert(String s)
    {
        if (s !=null && s.startsWith("<!-- html -->"))  return s;

        StringBuffer sb = new StringBuffer();
        s = deEncode(s);

        int n = s.length();

        for (int i = 0; i < n; i++)
        {
            char c = s.charAt(i);
            switch (c)
            {
            case '\n':
                sb.append("<br/>");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&#039;");   // et pas "&apos;" qui est inconnu de IE...
                break;
            case 'à':
                sb.append("&agrave;");
                break;
            case 'À':
                sb.append("&Agrave;");
                break;
            case 'â':
                sb.append("&acirc;");
                break;
            case 'Â':
                sb.append("&Acirc;");
                break;
            case 'ä':
                sb.append("&auml;");
                break;
            case 'Ä':
                sb.append("&Auml;");
                break;
            case 'å':
                sb.append("&aring;");
                break;
            case 'Å':
                sb.append("&Aring;");
                break;
            case 'æ':
                sb.append("&aelig;");
                break;
            case 'Æ':
                sb.append("&AElig;");
                break;
            case 'ç':
                sb.append("&ccedil;");
                break;
            case 'Ç':
                sb.append("&Ccedil;");
                break;
            case 'é':
                sb.append("&eacute;");
                break;
            case 'É':
                sb.append("&Eacute;");
                break;
            case 'è':
                sb.append("&egrave;");
                break;
            case 'È':
                sb.append("&Egrave;");
                break;
            case 'ê':
                sb.append("&ecirc;");
                break;
            case 'Ê':
                sb.append("&Ecirc;");
                break;
            case 'ë':
                sb.append("&euml;");
                break;
            case 'Ë':
                sb.append("&Euml;");
                break;
            case 'ï':
                sb.append("&iuml;");
                break;
            case 'Ï':
                sb.append("&Iuml;");
                break;
            case 'ô':
                sb.append("&ocirc;");
                break;
            case 'Ô':
                sb.append("&Ocirc;");
                break;
            case 'ö':
                sb.append("&ouml;");
                break;
            case 'Ö':
                sb.append("&Ouml;");
                break;
            case 'ø':
                sb.append("&oslash;");
                break;
            case 'Ø':
                sb.append("&Oslash;");
                break;
            case 'ß':
                sb.append("&szlig;");
                break;
            case 'ù':
                sb.append("&ugrave;");
                break;
            case 'Ù':
                sb.append("&Ugrave;");
                break;
            case 'û':
                sb.append("&ucirc;");
                break;
            case 'Û':
                sb.append("&Ucirc;");
                break;
            case 'ü':
                sb.append("&uuml;");
                break;
            case 'Ü':
                sb.append("&Uuml;");
                break;
            case '®':
                sb.append("&reg;");
                break;
            case '©':
                sb.append("&copy;");
                break;
            case '€':
                sb.append("&euro;");
                break;


                //ajout extra caractères windows
            case 'Œ':
                sb.append("&OElig;");
                break;
            case 'œ':
                sb.append("&oelig;");
                break;
            case 'ˆ':
                sb.append("&circ;");
                break;
            case '˜':
                sb.append("&tilde;");
                break;
            case '–':
                sb.append("&ndash;");
                break;
            case '—':
                sb.append("&mdash;");
                break;
            case '‘':
                sb.append("&lsquo;");
                break;
            case '’':
                sb.append("&rsquo;");
                break;
            case '‚':
                sb.append("&sbquo;");
                break;
            case '“':
                sb.append("&ldquo;");
                break;
            case '”':
                sb.append("&rdquo;");
                break;
            case '„':
                sb.append("&bdquo;");
                break;
            case '•':
                sb.append("&bull;");
                break;
            case '…':
                sb.append("&hellip;");
                break;
            case '‰':
                sb.append("&permil;");
                break;
            case '‹':
                sb.append("&lsaquo;");
                break;
            case '›':
                sb.append("&rsaquo;");
                break;


            case '&':
                if ((i+1<n) && (s.charAt(i+1)!='#')) {
                    sb.append("&amp;");
                } else { 
                    sb.append('&');
                }
                break;

                // be carefull with this one (non-breaking whitee space)
                //case ' ': sb.append("&nbsp;");break;

            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }

    
    /**
     * Contrôle de l'extension d'un fichier par rapport à une liste du type : ".doc.xls.pdf"
     * 
     * @param fileName
     * @param extentionsList
     * @return vrai si l'extension est dans la liste
     */
    public static boolean checkFileExtention(String fileName, String extentionsList)
    {
        boolean result = false;
        String ext = "";
        
        try {
        int pointIndex = fileName.lastIndexOf('.');
        
        if (pointIndex > 0) {
            ext = fileName.substring(pointIndex).toLowerCase();
            
            if (extentionsList != null && extentionsList.toLowerCase().indexOf(ext)>=0) 
                result= true;
                
        }
        } catch (Exception e) {}
        
        return result;
    }
    
    /**
     * fonction générant un password aléatoire sur 8 digits avec 
     * chiffres, lettres alphabet majuscules et minuscules
     * 
     * @return le password
     */
    public static String getPwd()
    {
        return getPwd(true, true, true, 8);
    }
    
    /*
      * fonction générant un password aléatoire sur <nbchars> digits avec (au choix)
     * chiffres <bNum>, lettres alphabet majuscules <bMaj> et minuscules <bMin>
     */
    public static String getPwd(boolean bNum, boolean bMaj, boolean bMin,
            int nbChars)
    {
        // attente 2ms, sinon le random renvoie deux fois la même valeur 
        //  pendant la même milliseconde  
        try { Thread.sleep(2);
        } catch (InterruptedException e) {} 

        Random gen = new Random();
        String pwd = "";

        for (int i = 0; i < nbChars; i++)
        {
            int j = 0;
            while (!((j >= 97 && j <= 122 && bMin) //minuscules
                    || (j >= 48 && j <= 57 && bNum) //chiffres
                    || (j >= 65 && j <= 90 && bMaj)))
                //majuscules
                j = gen.nextInt() % 122;

            pwd += (char)j;
        }
        return pwd;
    }



    public static String getLongDirFromPwd(String pwd) {

        long l = Long.valueOf("3894873068933547").longValue();
        for (int i=0; pwd != null && pwd.length()>0 && i< pwd.length() ; i++) {
            l =  l * pwd.charAt(i);
        }

        if (l<0) l = -l;

        return ""+l;
    }

} // class
