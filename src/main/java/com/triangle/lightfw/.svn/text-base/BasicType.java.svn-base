package com.triangle.lightfw;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

/**
 * 
 * Classe utilitaire regroupant des méthodes statiques nécessaires à la
 * manipulation des données dans l'interface.
 * Méthodes pour les chaînes, dates et nombres, certaines méthodes sont
 * paramètrables par une variable de type Locale (présente dans le bean
 * de session) pour tenir compte des usages locaux.
 *  
 * @version 6 déc. 02
 * 
 * @author Benoit, Pascal
 * 
 */
public class BasicType
{

    static private Logger traceLog = Logger.getLogger("FOLE");

    public final static int YEAR = Calendar.YEAR;
    public final static int MONTH = Calendar.MONTH;
    public final static int DAY = Calendar.DAY_OF_MONTH;

    /* **************************** CHAINE **************************** */

    /**
     * 
     * Chaîne: Méthode supprimant les caractères blancs en début et fin de
     * chaîne, si null est passé en paramètre, retourne la chaîne vide.
     * 
     * @param s Chaîne à modifier
     * 
     * @return String Chaîne modifié
     * 
     */
    public static String trim(String s)
    {
        String returnValue = "";

        if (s != null)
        {
            returnValue = s.trim();
        }

        return returnValue;
    }

    /**
     * 
     * Double les quotes ('), des antislash(\) et des guillemets (") 
     * à l'intérieur d'une chaîne de caractères. Les caractères déjà doublées 
     * le sont à nouveau, les guillemets sont remplacés par des quotes et 
     * le résultat est "trimé". Si la chaîne initiale est nulle, 
     * retourne la chaîne vide.
     * 
     * @param s Chaîne à quoter
     * 
     * @return String Chaîne quotée et trimée
     * 
     */
    public static String quoteQuote(String s)
    {
        String result = s;

        if (s != null)
        {
            // Traitement des '
            int posDep = 0;
            int posFound = result.indexOf("'", posDep);
            while (posFound != -1)
            {
                result =
                        result.substring(0, posFound) + "'" + result.substring(posFound);
                posDep = posFound + 2;
                posFound = result.indexOf("'", posDep);
            }
            // Traitement des \
            posDep = 0;
            posFound = result.indexOf("\\", posDep);
            while (posFound != -1)
            {
                result =
                        result.substring(0, posFound) + "\\" + result.substring(posFound);
                posDep = posFound + 2;
                posFound = result.indexOf("\\", posDep);
            }
            // Traitement des "
            posDep = 0;
            posFound = result.indexOf("\"", posDep);
            while (posFound != -1)
            {
                result =
                        result.substring(0, posFound) + "''" + result.substring(posFound + 1);
                posDep = posFound + 2;
                posFound = result.indexOf("\"", posDep);
            }
            result = trim(result);
        }
        else
        {
            result = "";
        }

        return result;
    }

    /**
     * Vérifie si la chaine suis les règles d'écriture des codes.
     * Caractères compris entre [a-z], [A-Z], [0-9] ou égal à  "_".
     * 
     *  @param s Chaine à controler
     * 
     *  @return boolean à true si la chaine suit les règles, sinon false.
     *
     */
    public static boolean IsCodeValid(String s)
    {
        boolean result = true;
        int i = 0;
        char toBeTested;

        while ((result == true) && (i < s.length()))
        {
            toBeTested = s.charAt(i);
            result =
                    ((('a' <= toBeTested) && (toBeTested <= 'z'))
                            || (('A' <= toBeTested) && (toBeTested <= 'Z'))
                            || (('0' <= toBeTested) && (toBeTested <= '9'))
                            || (toBeTested == '-')
                            || (toBeTested == '_'));
            i++;
        }
        return result;
    }

    /**
     * Enleve les vides et passe la chaine en majuscule.
     * 
     * @param s Chaine à traiter.
     * 
     * @return String Chaine mise en majuscule et trimée.
     * 
     */
    public static String prepareCodeToDb(String s)
    {

        String result = (BasicType.trim(s)).toUpperCase();

        return result;
    }

    /**
     * Compléte avec des espaces à droite la chaîne jusqu'à la longueur souhaitée.
     * 
     * @param s Chaine à traiter.
     * 
     * @param length Longueur de la chaîne résultat.
     * 
     * @return String résultat.
     * 
     */
    public static String rightPad(String s, int length)
    {
        StringBuffer result;

        // Si la chaîne de départ est trop longue
        if (length < s.length())
        {
            result = new StringBuffer(s.substring(0, length));
        }
        // Sinon on ajoute des blancs
        else
        {
            result = new StringBuffer(s);
            for (int i = s.length(); i < length; i++)
            {
                result.append(' ');
            }
        }

        return result.toString();
    }

    /**
     * Compléte avec des 0 à gauche la chaîne jusqu'à la longueur souhaitée.
     * 
     * @param s Chaine à traiter.
     * 
     * @param length Longueur de la chaîne résultat.
     * 
     * @return String résultat.
     * 
     */
    public static String leftPad(String s, int length)
    {
        StringBuffer result;

        // Si la chaîne de départ est trop longue
        if (length < s.length())
        {
            result = new StringBuffer(s.substring(0, length));
        }
        // Sinon on ajoute des 0
        else
        {
            result = new StringBuffer("");
            for (int i = s.length(); i < length; i++)
            {
                result.append('0');
            }
            result.append(s);
        }

        return result.toString();
    }

    /**
     * 
     * Renvoie une chaine des n premiers caractères de la chaîne initiale.
     * 
     * @param s chaîne à tronquer
     * 
     * @param n nombre de caractères
     * 
     * @return String résultat
     * 
     */
    public static String getShortenedString(String s, int n)
    {
        StringBuffer result = new StringBuffer("");

        if (s.length() < n)
        {
            result.append(s);
        }
        else
        {
            int nbChar = 0;
            int i = 0;

            boolean finded = false;

            while ((nbChar < n) && (i < s.length()))
            {
                finded = false;
                String c = s.substring(i, (i + 1));
                if (c.equals("&"))
                {
                    int posET = s.indexOf("&", (i + 1));
                    int posV = s.indexOf(";", (i + 1));

                    // cas du caractère '&'
                    if (posV == -1)
                    {
                        finded = true;
                    }
                    // cas ...&eacute; .....
                    else if (posET == -1)
                    {
                        result.append(s.substring(i, (posV + 1)));
                        i = posV + 1;
                    } // cas ...&eacute;....&..
                    else if (posET > posV)
                    {
                        result.append(s.substring(i, (posV + 1)));
                        i = posV + 1;
                    } // cas du caractère '&' suivi  de ....&ecute;...
                    else
                    {
                        finded = true;
                    }
                    // autres caractères
                }
                else
                {
                    finded = true;
                }

                // on ajoute le caractère 
                if (finded)
                {
                    result.append(c);
                    i++;
                }

                nbChar++;
            }
        }

        return result.toString();
    }

    /**
     * transforme les \n d'un texte par des <br/>
     * 
     * @param s
     * @return
     */
    public static String preparreCommentaireToHtml(String s)
    {
        int posDep = 0;
        int posFound = s.indexOf("\n", posDep);
        String result = s;
        while (posFound != -1)
        {
            result =
                    result.substring(0, posFound) + "<br/>" + result.substring(posFound + 1);
            posDep = posFound + 2;
            posFound = result.indexOf("\n", posDep);
        }

        return result;
    }

    /* **************************** NOMBRE **************************** */

    /**
     * Retourne l'entier le plus grand des 2 passés en paramètres. 
     * @param a
     * @param b
     */
    public static int max(int a, int b)
    {
        if (a >= b)
            return a;
        else
            return b;
    }

    /**
     * 
     * Analyse, en fonction d'un paramétrage local, une chaîne représentant
     * un nombre décimal et retourne sa valeur sous la forme d'une variable
     * de type BigDecimal, si la chaîne ne représente pas un nombre, retourne
     * null. La chaîne est analysé à partir de la fin de la manière la plus
     * permissive possible, le premier symbole rencontré est considéré comme
     * le séparateur décimal, qui ne doit pas être rencontré une seconde fois !
     * Utilisé pour contrôler et charger les valeurs saisies par un utilisateur.
     * 
     * @param s Chaîne a analyser
     * 
     * @param l Locale sevant au parsing, si null la valeur est Locale.FRANCE
     * 
     * @return BigDecimal La variable résultat, ou null sinon
     * 
     */
    public static BigDecimal parseBigDecimal(String s, Locale l)
    {
        BigDecimal n = null;

        if (s != null)
        {
            s = trim(s);
            if (s.length() != 0)
            {
                if (l == null)
                {
                    l = Locale.FRANCE;
                }
                DecimalFormatSymbols dfs = new DecimalFormatSymbols(l);
                char localGroupingSeparator = dfs.getGroupingSeparator();
                if (" ,.".indexOf(localGroupingSeparator) == -1)
                {
                    localGroupingSeparator = ' ';
                }
                char decimalSeparator = dfs.getDecimalSeparator();
                boolean decimalSeparatorFound = false;
                boolean parsingIsGood = true;
                StringBuffer sb = new StringBuffer();

                // On commence par la fin pour trouver le séparateur décimal !
                for (int i = s.length() - 1; i >= 0 && parsingIsGood; i--)
                {
                    char c = s.charAt(i);

                    if (('0' <= c) && (c <= '9')) // C'est un chiffre
                    {
                        sb.insert(0, c);
                    }
                    else if (
                            (('a' <= c) && (c <= 'z'))
                            || (('A' <= c) && (c <= 'Z'))) // C'est une lettre
                    {
                        parsingIsGood = false;
                    }
                    else if ((c == '+') || (c == '-')) // C'est un signe (+ ou -) !
                    {
                        if (i == 0) // Le signe est bien placé !
                        {
                            if (c == '-')
                            {
                                sb.insert(0, c);
                            }
                        }
                        else // Erreur le signe n'est pas où il faut !
                        {
                            parsingIsGood = false;
                        }
                    }
                    else if (!decimalSeparatorFound) // Pas la virgule, c'est elle ?
                    {
                        if ((c != localGroupingSeparator)
                                || ((sb.length() % 3) != 0)) // C'est vraiment elle !
                        {
                            sb.insert(0, '.');
                            decimalSeparatorFound = true;
                            decimalSeparator = c;
                        }
                    }
                    else if (c == decimalSeparator) // Séparateur des milliers ?!
                    {
                        parsingIsGood = false;
                    }
                }
                if (parsingIsGood)
                {
                    // cas du point tout seul !
                    if ((sb.length() == 1) && (sb.charAt(0) == '.'))
                    {
                        sb.insert(0, '0');
                    }
                    n = new BigDecimal(new String(sb));
                }
            }
        }

        return n;
    }

    /**
     * Formate, sous la forme d'une chaîne et en fonction d'un paramétrage local,
     * un nombre représenté par une variable de type BigDecimal. Utilisé pour
     * afficher les valeurs numériques dans l'interface. Retourne la chaîne 
     * vide "", si le BigDecimal passé est null.
     * @param n Nombre à formater
     * @param l Locale servant au formatage, si null la valeur est Locale.FRANCE
     */
    public static String formatBigDecimal(BigDecimal n, Locale l)
    {
        StringBuffer sb = new StringBuffer();

        if (n != null)
        {
            if (l == null)
            {
                l = Locale.FRANCE;
            }
            DecimalFormatSymbols dfs = new DecimalFormatSymbols(l);
            char localGroupingSeparator = dfs.getGroupingSeparator();
            if (" ,.".indexOf(localGroupingSeparator) == -1) // Bug Locale.FRANCE 
            {
                localGroupingSeparator = ' ';
            }
            char decimalSeparator = dfs.getDecimalSeparator();
            String s = n.toString();
            int pos = s.indexOf('.');

            sb.append(decimalSeparator);
            if (pos != -1) // Partie décimale
            {
                sb.append(s.substring((pos + 1), s.length()));
                s = s.substring(0, pos);
            }
            // On complète avec des 0 pour faire 2 décimales !
            for (int i = sb.length(); i < 3; i++)
            {
                sb.append('0');
            }

            // Partie entière, on commence par la fin pour gérer le signe en dernier !
            for (int i = s.length() - 1; i >= 0; i--)
            {
                char c = s.charAt(i);

                if (('0' <= c) && (c <= '9')) // C'est un chiffre
                {
                    // On insert le séparateur uniquement entre les groupes de 3 chiffres
                    if ((i != s.length() - 1)
                            && // C'est pas le chiffre des unitées
                            (
                                    ((s.length() - 1) - i) % 3 == 0)) // 1er chiffre d'un groupe de 3
                    {
                        sb.insert(0, localGroupingSeparator);
                    }
                }
                sb.insert(0, c);
            }
        }
        return sb.toString();
    }

    /**
     * Retourne la partie entière d'un BigDecimal sous la forme d'une chaîne,
     * en fonction de la locale. 
     * @param n Nombre à formater
     * @param l Locale servant au formatage, si null la valeur est Locale.FRANCE
     */
    public static String formatBigDecimalIntegerPart(BigDecimal n, Locale l)
    {
        StringBuffer sb = new StringBuffer();

        if (n != null)
        {
            if (l == null)
            {
                l = Locale.FRANCE;
            }
            DecimalFormatSymbols dfs = new DecimalFormatSymbols(l);
            char localGroupingSeparator = dfs.getGroupingSeparator();
            if (" ,.".indexOf(localGroupingSeparator) == -1) // Bug Locale.FRANCE 
            {
                localGroupingSeparator = ' ';
            }
            String s = n.toString();
            int pos = s.indexOf('.');

            if (pos != -1) // Partie décimale, on laisse tomber
            {
                s = s.substring(0, pos);
            }

            // Partie entière, on commence par la fin pour gérer le signe en dernier !
            for (int i = s.length() - 1; i >= 0; i--)
            {
                char c = s.charAt(i);

                if (('0' <= c) && (c <= '9')) // C'est un chiffre
                {
                    // On insert le séparateur uniquement entre les groupes de 3 chiffres
                    if ((i != s.length() - 1)
                            && // C'est pas le chiffre des unitées
                            (
                                    ((s.length() - 1) - i) % 3 == 0)) // 1er chiffre d'un groupe de 3
                    {
                        sb.insert(0, localGroupingSeparator);
                    }
                }
                sb.insert(0, c);
            }
        }
        return sb.toString();
    }

    /**
     * Compare un chiffre saisi avec les limites du champs numérique
     * de la base de donnée.
     * 
     * @param valeurSaisie BigDecimal
     * @param limiteMin 
     * @param limiteMax
     * 
     * @return boolean true si la valeur est conforme, false sinon.
     * 
     */
    public static boolean isMontantValid(
            BigDecimal valeurSaisie,
            BigDecimal limiteMin,
            BigDecimal limiteMax)
    {
        boolean result = true;

        if ((0 > valeurSaisie.compareTo(limiteMax))
                || (valeurSaisie.compareTo(limiteMin) < 0))
        {
            result = false;
        }

        return result;
    }

    /**
     * Calcule le montant dans la devise de référence.
     * 
     * @param montant, taux
     * @return BigDecimal
     */
    public static BigDecimal getMontantDeviseRef(
            BigDecimal montant,
            BigDecimal taux)
    {
        BigDecimal result = montant.multiply(taux);
        return result.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    /* **************************** DATE **************************** */

    /**
     * 
     * Analyse une chaîne représentant une date au format ISO (aaaa-mm-jj) 
     * et retourne la date correspondante sous la forme d'une variable
     * de type GregorianCalendar, si la chaîne ne représente pas une date
     * retourne null.
     * 
     * @param source
     * 
     * @return GregorianCalendar
     * 
     */
    public static GregorianCalendar parseCalendarFromDateIso(String source) {
        GregorianCalendar dest = null;

        try {
            String y = source.substring(0, 4);
            String m = source.substring(5, 7);
            String d = source.substring(8, 10);

            int year = 0;
            int month = 0;
            int date = 0;

            year = Integer.parseInt(y);
            month = Integer.parseInt(m) - 1; // mois codé de 0 à 11
            date = Integer.parseInt(d);

            if (date != 0) {
                dest = new GregorianCalendar();
                dest.set(year, month, date);
            }
        } catch (Exception e) {
            traceLog.debug(e);
        }

        return dest;
    }

    /**
     * 
     * Formate, sous la forme d'une chaîne et en fonction d'un paramétrage local,
     * une date représentée par une variable de type GregorianCalendar.
     * 
     * @param g Date à formater
     * 
     * @param l Locale servant au formatage, si null la valeur est Locale.FRANCE
     * 
     * @return String Chaîne représentant la date au format  jj/mm/aaaa
     * 
     */
    public static String formatCalendarToLocal(GregorianCalendar g, Locale l)     {

        if (l == null) l = Locale.FRANCE;

        if (g == null) return "";

        Date date = g.getTime();
        //DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT, l); // format dd/MM/yy
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(date);

    }

    /**
     * 
     * Analyse une chaîne représentant une date au format local 
     * et retourne la date correspondante sous la forme d'une chaîne ISO
     * (aaaa-mm-jj), si la chaîne ne représente pas une date retourne null.
     * Utilisé pour contrôler et charger les valeurs saisies par un utilisateur.
     * 
     * @param s Chaîne représentant la date
     * 
     * @param l Locale servant à l'analyse, si null la valeur est Locale.FRANCE
     * 
     * @return String Chaîne représentant la date au format ISO
     * 
     */
    public static String parseDateIsoFromLocal(String s, Locale l)
    {
        String result = null;

        if (s != null && !s.equals(""))
        {
            try
            {
                int lg = s.length();

                if (l == null)
                {
                    l = Locale.FRANCE;
                }
                SimpleDateFormat sdf =
                        (SimpleDateFormat)SimpleDateFormat.getDateInstance(
                                DateFormat.SHORT,
                                l);
                String pattern = sdf.toPattern();
                StringBuffer parseOrder = new StringBuffer();
                char patternSeparator = '!';

                // On détermine l'ordre et le séparateur du pattern
                for (int i = 0; i < pattern.length(); i++)
                {
                    char c = pattern.charAt(i);

                    if ((parseOrder.length() == 0)
                            || ((parseOrder.length() > 0)
                                    && (parseOrder.charAt(parseOrder.length() - 1) != c)))
                    {
                        if ((c != 'd') && (c != 'M') && (c != 'y'))
                        {
                            patternSeparator = c;
                        }
                        else
                        {
                            parseOrder.append(c);
                        }
                    }
                }

                // On simplifie en fonction de la taille de la chaîne à parser :
                String day = null;
                String month = null;
                String year = null;
                String today = getTodaysDateIso();

                if ((1 <= lg)
                        && (lg <= 2)) // Représente le jour (mois, année en cours)
                {
                    parseOrder = new StringBuffer("d");
                    day = s;
                    month = today.substring(5, 7);
                    year = today.substring(0, 4);
                }
                else if (
                        ((3 <= lg) && (lg < 5))
                        || // Représente les jour et mois
                        (
                                (lg == 5)
                                && (s.indexOf(patternSeparator)
                                        == s.lastIndexOf(patternSeparator))))
                {
                    if (parseOrder.charAt(2) == 'y')
                    {
                        parseOrder = new StringBuffer(parseOrder.substring(0, 2));
                    }
                    else
                    {
                        parseOrder = new StringBuffer(parseOrder.substring(1, 3));
                    }
                    if (s.charAt(1) != patternSeparator)
                    {
                        if (parseOrder.charAt(0) == 'd') // dM
                        {
                            day = s.substring(0, 2);
                            if (s.charAt(2) != patternSeparator)
                            {
                                month = s.substring(2);
                            }
                            else
                            {
                                month = s.substring(3);
                            }
                        }
                        else // Md
                        {
                            month = s.substring(0, 2);
                            if (s.charAt(2) != patternSeparator)
                            {
                                day = s.substring(2);
                            }
                            else
                            {
                                day = s.substring(3);
                            }
                        }
                    }
                    else // "x/yy"
                    {
                        if (parseOrder.charAt(0) == 'd') // dM
                        {
                            day = s.substring(0, 1);
                            month = s.substring(2);
                        }
                        else // Md
                        {
                            month = s.substring(0, 1);
                            day = s.substring(2);
                        }
                    }
                    year = today.substring(0, 4);
                }
                else // Représente jour, mois et année
                {
                    int posDeb = 0;
                    int posFin = 0;
                    boolean hasSeparator = true;
                    if (s.indexOf(patternSeparator) == -1) // Pas de séparateurs !
                    {
                        hasSeparator = false;
                    }

                    for (int i = 0; i < 2; i++) // On parse les 2 premiers nombres
                    {
                        switch (parseOrder.charAt(i))
                        {
                        case 'd' :
                        {
                            if (hasSeparator)
                            {
                                posFin = s.indexOf(patternSeparator, posDeb);
                                day = s.substring(posDeb, posFin);
                                posDeb = posFin + 1;
                            }
                            else
                            {
                                posFin = posDeb + 2;
                                day = s.substring(posDeb, posFin);
                                posDeb = posFin;
                            }
                            break;
                        }
                        case 'M' :
                        {
                            if (hasSeparator)
                            {
                                posFin = s.indexOf(patternSeparator, posDeb);
                                month = s.substring(posDeb, posFin);
                                posDeb = posFin + 1;
                            }
                            else
                            {
                                posFin = posDeb + 2;
                                month = s.substring(posDeb, posFin);
                                posDeb = posFin;
                            }
                            break;
                        }
                        case 'y' :
                        {
                            if (hasSeparator)
                            {
                                posFin = s.indexOf(patternSeparator, posDeb);
                                year = s.substring(posDeb, posFin);
                                posDeb = posFin + 1;
                            }
                            else
                            {
                                posFin = posDeb + (lg - 4);
                                // Année sur 4 ou 2, chaîne 8 ou 6
                                year = s.substring(posDeb, posFin);
                                posDeb = posFin;
                            }
                            break;
                        }
                        }
                    }
                    switch (parseOrder.charAt(2)) // On parse le dernier nombre
                    {
                    case 'd' :
                    {
                        day = s.substring(posDeb);
                        break;
                    }
                    case 'M' :
                    {
                        month = s.substring(posDeb);
                        break;
                    }
                    case 'y' :
                    {
                        year = s.substring(posDeb);
                        break;
                    }
                    }
                }
                // traceLog.debug("Day " + day + " Month " + month + " Year " + year);
                // On met l'année sur 4, 2000 + saisie !
                if (year.length() <= 2)
                {
                    if (year.length() == 1)
                    {
                        year = "200" + year;
                    }
                    else
                    {
                        year = "20" + year;
                    }
                }
                // On parse les valeurs et on vérifie la saisie
                int d = Integer.parseInt(day);
                int m = Integer.parseInt(month) - 1;
                int y = Integer.parseInt(year);
                //traceLog.debug("D " + d + " M " + m + " Y " + y);

                GregorianCalendar gc = new GregorianCalendar(y, m, d);
                Date date = gc.getTime();

                if ((d == gc.get(GregorianCalendar.DATE))
                        && (m == gc.get(GregorianCalendar.MONTH))
                        && (y == gc.get(GregorianCalendar.YEAR))) // Bonne date
                {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    result = sdf.format(date);
                }
            }
            catch (Exception e)
            {
                traceLog.debug(e);
            }
        }

        return result;
    }

    /**
     * 
     * Formate, sous la forme d'une chaîne et en fonction d'un paramétrage local,
     * une date représentée par une chaîne au format ISO (aaaa-mm-jj). Utilisé 
     * pour afficher les dates dans l'interface.
     * 
     * @param iso Date ISO à formater
     * 
     * @param l Locale servant au formatage, si null la valeur est Locale.FRANCE
     * 
     * @return String Chaîne représentant la date au format court (ex jj/mm/aa)
     * 
     */
    public static String formatDateIsoToLocal(String iso, Locale l)     {
        String result = null;

        if (iso == null || iso.trim().equals("")) return null;
        if (l == null) l = Locale.FRANCE;

        try {
            GregorianCalendar c = BasicType.parseCalendarFromDateIso(iso);
            result = BasicType.formatCalendarToLocal(c, l);
        
        } catch (Exception e) {
            traceLog.debug("Pb formatDateIsoToLocal : " + iso);
            return null;
        }


        return result;
    }

    /**
     * 
     * Formate, sous la forme d'une chaîne et en fonction d'un paramétrage local,
     * une date représentée par une chaîne au format ISO (aaaa-mm-jj). La forme du
     * résultat est représenté dans une chaîne modèle (pattern).
     * ex: iso = 1977-11-18
     *     pattern = dd MMMM yyyy dd
     *     locale = Locale.FRANCE;
     *     Donne le résultat : 18 Novembre 1977
     *
     * @see SimpleDateFormat
     *  
     * @param iso Chaîne ISO représentant la date à formater
     * 
     * @param pattern Modèle de formatage
     * 
     * @param l Locale servant au formatage, si null la valeur est Locale.FRANCE 
     * 
     * @return String Chaîne représentant la date formatée
     * 
     */
    public static String formatDateIsoToPattern(
            String iso,
            String pattern,
            Locale l)   {

        if (l == null)  l = Locale.FRANCE;
        if (pattern == null) pattern = "dd/MM/yy";


        String result = null;

        if (iso != null && !iso.equals("")) {

            GregorianCalendar c = parseCalendarFromDateIso(iso);
            if (c != null)
            {
                Date date = c.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat(pattern, l);
                result = sdf.format(date);
            }

        }
        return result;
    }


    public static String formatDateIsoToLocalDateTime(String inputDate)   {

        String format = "yyyy-MM-dd HH:mm:ss";

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date d = sdf.parse(inputDate);

            sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");

            return sdf.format(d);

        } catch (ParseException e) {
            return "?";
        }


    }


    /**
     * Retourne l'heure du serveur au format HH:mm:ss.
     */
    public static String getTimeIso()
    {
        return getCurentTime("HH:mm:ss");
    }


    /**
     * oups
     */
    public static String getCurentTime(String format)    {

        return getCurrentTime( format);
    }


    /**
     * Retourne l'heure du moment, formatèe selon la chaine ,passée en paramètre.
     * HH, mm,  ss, ...
     */
    public static String getCurrentTime(String format)
    {
        if (format == null) format = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = new Date();

        return sdf.format(d);
    }

    /**
     * Retourne l'année courante 
     * aaaa
     */
    public static String getCurrentYear()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date d = new Date();

        return sdf.format(d);
    }

    /**
     * 
     * Retourne la date du jour au format LOCAL (jj/mm/aaaa).
     * 
     * @return String Chaîne ISO représentant la date du jour.
     * 
     */
    public static String getTodaysDateLocal()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();

        return sdf.format(d);
    }

    /**
     * 
     * Retourne la date du jour au format ISO (aaaa-mm-jj).
     * 
     * @return String Chaîne ISO représentant la date du jour.
     * 
     */
    public static String getTodaysDateIso()
    {
        return getTodaysDateIso(false);
    }

    /**
     * 
     * Retourne la date/heure du jour au format ISO (aaaa-mm-jj hh:mm).
     * 
     * @return String Chaîne ISO représentant la date/heure du jour.
     * 
     */
    public static String getTodaysDateIso(boolean withTime)
    {
        String format = "yyyy-MM-dd";
        if (withTime) format += " HH:mm:ss" ;

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date d = new Date();

        return sdf.format(d);
    }


    /**
     * 
     * Retourne le nb de jour écoulé entre 2 dates.
     * Les dates sont des chaînes au format ISO (aaaa-mm-jj).
     * 
     * d2 - d1
     * 
     * @return int représentant le nombre de jour écoulé.
     * 
     */
    public static int getDateDiff(String date1, String date2)
    {
        GregorianCalendar g1 = parseCalendarFromDateIso(date1);
        GregorianCalendar g2 = parseCalendarFromDateIso(date2);

        long d1 = (g1.getTime()).getTime();
        long d2 = (g2.getTime()).getTime();

        long diff = (d2 - d1) / 1000; // différence en seconde

        int result = (int)(diff / (60 * 60 * 24)); // différence en jour

        return result;
    }

    /**
     * 
     * Ajoute à une date un nombre de jours, de mois, ou d'années.
     * 
     * @param dateIso La chaine ISO de la date à laquelle on ajoute un nb de jours/mois/années.
     * @param field Un entier qui indique si on ajoute un mois (Calendar.MONTH), un jour 
     * (Calendar.DATE) ou une année (Calendar.YEAR).
     * @param amount Valeur a ajouter.
     * @return String retourne la date obtenue en format ISO.
     */
    public static String addDateIso(String date, int field, int amount)
    {
        Calendar calendar = parseCalendarFromDateIso(date);
        calendar.add(field, amount);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(calendar.getTime());
    }

    /**
     * 
     * Compare 2 dates au format ISO. yyyy-mm-dd
     * 
     * @param date1 date au format ISO
     * @param date2 date au format ISO
     * @return long Retourne -1 si date1 avant date2 , 0 si date1 = date2, 1 si date1 après date2
     */
    public static long compareDateIso(String date1, String date2)
    {
        GregorianCalendar g1 = parseCalendarFromDateIso(date1);
        GregorianCalendar g2 = parseCalendarFromDateIso(date2);
        long d1 = (g1.getTime()).getTime();
        long d2 = (g2.getTime()).getTime();

        return (d1 - d2);
    }

    /**
     * 
     * Compare 2 dates au format locale (dd/mm/yyyy)
     * 
     * @param date1 date au format locale
     * @param date2 date au format locale
     * @return long Retourne -1 si date1 avant date2 , 0 si date1 = date2, 1 si date1 après date2
     */
    public static long compareDateLocale(String date1, String date2)
    {
        GregorianCalendar g1 = parseCalendarFromDateIso(parseDateIsoFromLocal(date1,null));
        GregorianCalendar g2 = parseCalendarFromDateIso(parseDateIsoFromLocal(date2,null));
        long d1 = (g1.getTime()).getTime();
        long d2 = (g2.getTime()).getTime();

        return (d1 - d2);
    }

    /**
     * 
     * Retourne true si la date ISO date est comprise entre ]start et end]
     * 
     * @param date à comparer
     * @param start date début inclusive
     * @param end date fin inclusive
     * @return true ou false
     * 
     */
    public static boolean isDateBetween(String date, String start, String end)
    {
        boolean isBetween = false;
        if ((date.compareTo(start) >= 0) && (date.compareTo(end) <= 0))
        {
            isBetween = true;
        }

        return isBetween;
    }

    /**
     * 
     * On renvoie une date sous format ISO.
     * 
     * @param String jour
     * @param String dateDebutPeriode
     * @param String dateFinPeriode
     * @return String Date du jour du frais en format ISO.
     */
    public static String getDateFrais(
            int jour,
            String dateDebutPeriode,
            String dateFinPeriode)
    {
        StringBuffer dateJourFrais = new StringBuffer("");
        int jourDebutPrd = Integer.parseInt(dateDebutPeriode.substring(8, 10));
        String moisAnneeDebutPrd = dateDebutPeriode.substring(0, 8);
        String moisAnneeFinPrd = dateFinPeriode.substring(0, 8);
        String jourDateFrais = Integer.toString(jour);
        if (jourDateFrais.length() == 1)
        {
            jourDateFrais = "0" + jourDateFrais;
        }

        // jour appartient au mm mois que la date de début de période.
        if (jour > jourDebutPrd)
        {
            dateJourFrais.append(moisAnneeDebutPrd + jourDateFrais);
        }
        //  jour appartient au mois de la date de fin de période.
        else
        {
            dateJourFrais.append(moisAnneeFinPrd + jourDateFrais);
        }

        return dateJourFrais.toString();
    }

    /* ***************************************** main ********
    public static void main(String[] args)
    {
      String s1 = "abcd efgh ijkl mnop";
      String s2 = "ab&eacute;cd efgh ijkl";
      String s3 = "a&agrave;ed &jfir oo&eacute;fd";
      String s4 = "0123&5678&gt;CEF0123456789ABCEF";

      System.out.println("s1: " + s1 + " taille: 12, valeur: " + getShortenedString(s1,12));
      System.out.println("s2: " + s2 + " taille: 12, valeur: " + getShortenedString(s2,12));
      System.out.println("s3: " + s3 + " taille: 12, valeur: " + getShortenedString(s3,12));
      System.out.println("s4: " + s4 + " taille: 15, valeur: " + getShortenedString(s4,15));
    }*/

}