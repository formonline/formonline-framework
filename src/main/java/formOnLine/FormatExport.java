package formOnLine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import formOnLine.actions.PropositionAction;
import formOnLine.actions.QuestionAction;
import formOnLine.actions.QuestionnaireAction;
import formOnLine.actions.ReponseAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.actions.TemplateAction;
import formOnLine.io.XmlBuilder;
import formOnLine.msBeans.Proposition;
import formOnLine.msBeans.Question;
import formOnLine.msBeans.Questionnaire;
import formOnLine.msBeans.Reponse;
import formOnLine.msBeans.SubmitForm;
import formOnLine.msBeans.Template;
import formOnLine.msBeans.UserData;

import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.SessionInfos;
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;


/**
 * Classe gérant les exportations et éditions des formulaires. Implémente
 * l'interface XmlRpcHandler (méthode execute), afin d'être appelable via
 * XML-RPC (activation par la servlet ServExportRPC)
 * 
 * @author Pascal
 */
/**
 * @author SeLERIDON
 *
 */
public class FormatExport { 

    public static final String XML_ENCODE = "UTF-8";
    public static final String HTML_ENCODE = "UTF-8";

    public static final String startTag = "<%";
    public static final String endTag = "%>";



    /***************************************************************************
     * Exporte un fichier csv
     * 
     * @param templateId
     * @param out
     * @param datDeb
     * @param datFin
     * @param lockedOnly
     * @param sqlFilterId
     * @param sqlValId
     * @param params
     * @param ud
     * @return nb d'enregistrements exportés
     */
    static public int exportCsvFromTemplate(int templateId, OutputStream out,
            String datDeb, String datFin, int lockedOnly,
            int sqlFilterId, String sqlValId, HashMap<String,String> params, UserData ud) {

        SubmitFormAction sfa = new SubmitFormAction();
        try {
            // récup template
            TemplateAction ta = new TemplateAction();
            Template t = ta.selectOne(templateId);

            Template tmplSqlFilter = null;
            String sqlFilter = null;

            if (sqlFilterId>=0) {
                tmplSqlFilter = ta.selectOne(sqlFilterId);
                sqlFilter = tmplSqlFilter.getContent();

                // recherche et remplacement de la chaîne {%id%}
                if (sqlValId != null && sqlFilter!=null) 
                    sqlFilter= sqlFilter.replaceAll("\\{%id%\\}", sqlValId);
            }

            // récupération des submitform
            ValueBeanList list = sfa.selectAllByFormId(t.getFid(), -1, lockedOnly,
                    datDeb, datFin, sqlFilter);

            // Alimentation questionnaire correspondant au template 
            Questionnaire q = new QuestionnaireAction().getFullQuest(t.getFid());

            // contrôle droits
            if (!isAllowed(ud,q, -1))  return -1;

            // Préparation envoi du contenu RTF
            PrintStream fichier = new PrintStream(out, true, "UTF-8");

            // envoi lignes
            StringBuffer txt = null;
            
            // envoi des entêtes BOM utf-8 pour excel
            fichier.write(0xEF);   // 1st byte of BOM
            fichier.write(0xBB);
            fichier.write(0xBF);   // last byte of BOM

            // envoi premiére ligne entêtes
            txt = getHeader(t);
            fichier.println(txt); //.toString().getBytes("UTF-8"));


            for (int i = 0; list != null && i < list.size(); i++) {
                // fusion du template
                SubmitForm sf = (SubmitForm) list.get(i);

                txt = getContent(t.getContent(), sf.getS_id(), q, " / ", true, 0 , params, ud, null);
                fichier.println(txt); //.toString().getBytes("UTF-8"));

            }

            out.flush();
        } catch (Exception e) {
            InitServlet.traceLog.error("Erreur lors de l'exportation" + e);
            return -1;
        }

        return 0;
    }

    /***************************************************************************
     * Exporte un fichier csv
     * 
     * @param
     * @return
     */
    static public int exportTxtFromTemplate(int templateId, OutputStream out,
            String datDeb, String datFin, int lockedOnly,
            int sqlFilterId, String sqlValId, UserData ud) {

        SubmitFormAction sfa = new SubmitFormAction();
        try {
            // récup template
            TemplateAction ta = new TemplateAction();
            Template t = ta.selectOne(templateId);

            Template tmplSqlFilter = null;
            String sqlFilter = null;

            if (sqlFilterId>=0) {
                tmplSqlFilter = ta.selectOne(sqlFilterId);
                sqlFilter = tmplSqlFilter.getContent();

                // recherche et remplacement de la chaîne {%id%}
                if (sqlValId != null && sqlFilter!=null) 
                    sqlFilter= sqlFilter.replaceAll("\\{%id%\\}", sqlValId);
            }

            // récupération des submitform
            ValueBeanList list = sfa.selectAllByFormId(t.getFid(), -1,  lockedOnly,
                    datDeb, datFin, sqlFilter);

            // Alimentation questionnaire correspondant au template 
            Questionnaire q = new QuestionnaireAction().getFullQuest(t.getFid());

            // contrôle droits
            if (!isAllowed(ud,q, -1))  return -1;

            // Préparation envoi du contenu RTF
            PrintStream fichier = new PrintStream(out, true, "UTF-8");

            // envoi lignes
            StringBuffer txt = null;

            // envoi première ligne entêtes
            //txt = getHeader(t);
            //fichier.println(txt);


            for (int i = 0; list != null && i < list.size(); i++) {
                // fusion du template
                SubmitForm sf = (SubmitForm) list.get(i);

                txt = getContent(t.getContent(), sf.getS_id(), q, " / ", true, ud, null);
                fichier.println((txt.toString() + "\r").getBytes("UTF-8")); // ajour du \r pour avoir un CR avant le LF

            }

            out.flush();
        } catch (Exception e) {
            InitServlet.traceLog.error("Erreur lors de l'exportation" + e);
            return -1;
        }

        return 0;
    }


    /***************************************************************************
     * Envoi d'un mailing
     * 
     * @param templateId
     * @param out
     * @param datDeb
     * @param datFin
     * @return nb de mails
     */
    static public int sendMailingFromTemplate(int templateId, OutputStream out,
            String datDeb, String datFin, int lockedOnly,
            int sqlFilterId, String sqlValId, UserData ud) {

        SubmitFormAction sfa = new SubmitFormAction();
        ReponseAction ra = new ReponseAction();
        TemplateAction ta = new TemplateAction();
        QuestionnaireAction qa = new QuestionnaireAction();

        try {
            // récup template

            Template t = ta.selectOne(templateId);

            Template tmplSqlFilter = null;
            String sqlFilter = null;

            if (sqlFilterId>=0) {
                tmplSqlFilter = ta.selectOne(sqlFilterId);
                sqlFilter = tmplSqlFilter.getContent();

                // recherche et remplacement de la chaîne {%id%}
                if (sqlValId != null && sqlFilter!=null) 
                    sqlFilter= sqlFilter.replaceAll("\\{%id%\\}", sqlValId);
            }

            // récupération des submitform
            ValueBeanList list = sfa.selectAllByFormId(t.getFid(), -1,  lockedOnly,
                    datDeb, datFin, sqlFilter);

            Questionnaire q = qa.selectById(t.getFid());

            // contrôle droits
            if (!isAllowed(ud,q,-1))  return -1;



            String from = q.getF_mail_adm();
            String subject = t.getName();
            String to="?";

            // Préparation envoi du contenu 
            PrintStream fichier = new PrintStream(out, true, "UTF-8");
            StringBuffer txt = null;
            String errMsg = null;

            // init flux d'info html des envois de mail
            fichier.write((BasicType.getCurentTime(null)
                    + " : Début Mailing [ " + t.getName() + " ]").getBytes("UTF-8"));

            for (int i = 0; list != null && i < list.size(); i++) {

                SubmitForm sf = (SubmitForm) list.get(i);
                Reponse r = null; 

                int p_id_mail = q.getP_id_mail();
                if (p_id_mail > 0 ) r = ra.selectOne(p_id_mail, sf.getS_id());                    
                if (r==null) r= ra.selectOne(p_id_mail, sf.getS_id_parent()); 

                to="";
                if (r!=null) to = r.getSVal();

                // fusion du titre du mail
                StringBuffer subb = getContent(t.getName(), sf.getS_id(), q, " - ", true, 0 , null, ud, null);

                if (subb!=null ) {
                    subject = subb.toString(); 
                } else {
                    subject = t.getName(); 
                }

                // fusion du corps du mail
                txt = getContent(t.getContent(), sf.getS_id(), q,  "<br/>", false, ud, null);
                fichier.write((BasicType.getCurentTime(null) + " : mail [ "
                        + sf.getS_id() + " ] to: " + to + " - from: " + from).getBytes("UTF-8"));

                errMsg = Mailer.sendHtmlMail(from, to, null, subject, 
                        txt.toString(),
                        BasicTools.cleanHTML(txt.toString()));

                //tempo 0,5 s
                Thread.sleep(500);

                if (errMsg != null)
                    fichier.println(errMsg);

                if (i % 10 == 0) out.flush();

            }

            //          fin flux d'info html des envois de mail
            fichier.write((BasicType.getCurentTime(null)
                    + " : FIN Mailing [ " + t.getName() + " ]").getBytes("UTF-8"));
            
            out.flush();
        } catch (Exception e) {
            InitServlet.traceLog.error("Erreur lors de l'exportation" + e);
            return -1;
        }

        return 0;
    }

    /***************************************************************************
     * Génération de fichiers dans le répertoire FILES 
     * 
     * @param templateId
     * @param out
     * @param datDeb
     * @param datFin
     * @return nb de mails
     */
    static public int generateFilesFromTemplate(int templateId, OutputStream out,
            String datDeb, String datFin, int lockedOnly, 
            int sqlFilterId, String sqlValId, HashMap<String,String> params, UserData ud) {

        SubmitFormAction sfa = new SubmitFormAction();
        TemplateAction ta = new TemplateAction();

        String RESULT_DIR = InitServlet.getFileExportDir();
        PrintStream fichier = null;

        try {
            // récup template

            Template t = ta.selectOne(templateId);

            Template tmplSqlFilter = null;
            String sqlFilter = null;

            if (sqlFilterId>=0) {
                tmplSqlFilter = ta.selectOne(sqlFilterId);
                sqlFilter = tmplSqlFilter.getContent();

                // recherche et remplacement de la chaîne {%id%}
                if (sqlValId != null && sqlFilter!=null) 
                    sqlFilter= sqlFilter.replaceAll("\\{%id%\\}", sqlValId);
            }

            // récupération des submitform
            ValueBeanList list = sfa.selectAllByFormId(t.getFid(), -1, lockedOnly,
                    datDeb, datFin, sqlFilter);

            // Alimentation questionnaire correspondant au template 
            Questionnaire q = new QuestionnaireAction().getFullQuest(t.getFid());

            // contrôle droits
            if (!isAllowed(ud,q,-1))  return -1;

            //          Préparation envoi flux info html
            fichier = new PrintStream(out, true, "UTF-8");

            // init flux d'info html des envois de mail
            fichier.write(("<html><body>" + BasicType.getCurentTime(null)
                    + " : Début Génération "+(list!=null?list.size():0)+" Fichiers <br>[ " + t.getName() + " ]<br>").getBytes("UTF-8"));

            fichier.println();
            
            fichier.write(("Répertoire de destination : "+RESULT_DIR+"<br>").getBytes("UTF-8"));
            for (int i = 0;list != null &&  i < list.size(); i++) {

                SubmitForm sf = (SubmitForm) list.get(i);
                sf = sfa.selectBySId(sf.getS_id());

                // titre fichier à fusionner
                StringBuffer fileNameSb = getContent(t.getName(), sf.getS_id(), q, " - ", true, 0 , params, ud, null);

                String fileName = BasicTools.cleanFileNameForWindows(fileNameSb.toString());

                // limite windows nom fichier à 255 ?
                int maxsize = 240 - RESULT_DIR.length() ;
                if (fileName.length() >= maxsize) fileName = fileName.substring( maxsize );

                // ajout date génération + extension
                fileName += " ("+ BasicType.getTodaysDateIso()+").doc";



                FileWriter fw = new FileWriter(RESULT_DIR + File.separator + fileName.replaceAll("/","-") );

                fw.write(getContent(t.getContent(), sf.getS_id(), q, "<br/>", false, 0, params, ud, null).toString());
                fw.close();

                fichier.write((BasicType.getCurentTime(null) + " : fichier généré [ "
                        + fileName + " ] <br>").getBytes("UTF-8"));

                fichier.println();
                
                InitServlet.traceLog.debug("fichier <"+fileName + "> généré");
                

                if (i % 10 == 0) out.flush();

            }

            //          fin flux d'info html 
            fichier.write((BasicType.getCurentTime(null)
                    + " : FIN " + (list!=null?list.size():0) +" fichier(s) généré(s) [ " + t.getName() + " ]").getBytes("UTF-8"));

            out.flush();
        } catch (Exception e) {
            if (fichier !=null)
                fichier.println(e.getLocalizedMessage());
            InitServlet.traceLog.error("Erreur lors de l'exportation" + e);

            return -1;
        }

        return 0;
    }

    /***************************************************************************
     * Exporte un fichier doc 
     * 
     * @param
     * @return
     */
    static public int exportDocFromTemplate(int templateId, int sid,
            OutputStream out, java.util.HashMap<String,String> params, UserData ud) throws Exception {

        // récup template
        TemplateAction ta = new TemplateAction();
        Template t = ta.selectOne(templateId);
        String crlf = "";

        if (t.getType().equals(Template.HTML))
            crlf = "<br/>";
        if (t.getType().equals(Template.RTF))
            crlf = "\\line ";
        if (t.getType().equals(Template.MTO))
            crlf = "<br/>";

        // Alimentation questionnaire correspondant au template 
        Questionnaire q = new QuestionnaireAction().getFullQuest(t.getFid());

        StringBuffer txt = null;


        // contrôle droits
        if (!isAllowed(ud,q, sid))  {
            txt = new StringBuffer("Droits insuffisants");
        } else {
            // fusion du template
            txt = getContent(t.getContent(), sid, q, crlf, false, 0, params, ud, null);
        }


        if (t.getType().equals(Template.MTO))
            txt = prepareMailTo(txt);

        // envoi du contenu RTF
        PrintStream fichier = new PrintStream(out, true, "UTF-8");
        
        if (! (txt.indexOf("<html") >= 0) && t.getType().equals(Template.HTML) )
            fichier.write("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'><meta charset='utf-8'></head><body>".getBytes("UTF-8"));
        
        fichier.write(txt.toString().getBytes("UTF-8"));

        out.flush();


        return 0;
    }

    /***************************************************************************
     * Exporte un fichier PDF 
     * 
     * @param
     * @return
     */
    static public int exportPdfFromTemplate(int templateId, int sid,
            OutputStream out, java.util.HashMap<String,String> params, UserData ud) throws Exception {

        // récup template
        TemplateAction ta = new TemplateAction();
        Template t = ta.selectOne(templateId);
        String crlf = "";

        if (t.getType().equals(Template.HTML))
            crlf = "<br/>";

        // Alimentation questionnaire correspondant au template 
        Questionnaire q = new QuestionnaireAction().getFullQuest(t.getFid());

        StringBuffer txt = null;

        // contrôle droits
        if (!isAllowed(ud,q, sid))  {
            txt = new StringBuffer("Droits insuffisants");
        } else {
            // fusion du template
            txt = getContent(t.getContent(), sid, q, crlf, false, 0, params, ud, null);
        }

        // step 1
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, out);
        // step 3
        document.open();
        // step 4
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(txt.toString().getBytes()));
        //step 5
        document.close();




        out.flush();


        return 0;
    }




    /***************************************************************************
     * Exporte un mail
     * 
     * @param
     * @return
     */
    static public StringBuffer exportMailFromTemplate(int templateId, int sid, UserData ud) {
        try {
            // récup template
            TemplateAction ta = new TemplateAction();
            Template t = ta.selectOne(templateId);
            String crlf = "<br/>";

            // Alimentation questionnaire correspondant au template 
            Questionnaire q = new QuestionnaireAction().getFullQuest(t.getFid());

            // fusion du template
            StringBuffer txt = null;
            txt = getContent(t.getContent(), sid, q, crlf, false, ud, null);

            // envoi du contenu htm
            return txt;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param t
     * @param sid
     * @param crlf
     * @param forCsv
     * @return String buffer le contenu fusionné
     * @throws Exception
     */
    static private StringBuffer getContent(String tcontent, int sid, Questionnaire f, String crlf,
            boolean forCsv, UserData userData, 
            java.util.HashMap<Integer,SubmitForm> listSf)  { 
        return getContent( tcontent,  sid,  f, crlf, forCsv, -1,  null, userData, listSf);
    }

    /**
     * @param t : template avec balises à fusionner
     * @param sid : id du submitform à fusionner 
     * @param f : questionnaire correspondant au submitform
     * @param crlf : caractere de retour à la ligne à utiliser
     * @param forCsv : indicateur d'export CSV (pour traitement spécifique)
     * @param indexVal : valeur de l'index courant (pour l'affichage du champ {%INDEX.........%})
     * @param params : les paramètres de config
     * @param userData : le bean du user connecté pour le contrôle des droits
     * @param listSf : ...
     * @return le contenu fusionné 
     * @throws Exception
     */
    static private StringBuffer getContent(String tcontent, int sid, Questionnaire f, String crlf,
            boolean forCsv, int indexVal, java.util.HashMap<String,String> params, UserData userData,
            java.util.HashMap<Integer,SubmitForm> listSf)  {

        StringBuffer txt = null;
        String curBalise = null;

        try {


            /* CONTROLE DES DROITS DE CONSULTATION
             *  - si le role PUBLIC non connecté : pas d'accés en lecture
             *  - si le role connecté : accès aux submitform fils ou père
             *  (f_connected <= 1)
             *  - si le role CONSULT : accès en lecture à tous les submitform, hors admin & restricted
             *  (f_connected <= 1)
             *  - si le role GESTION : accès à tous les submitform, hors admin & restricted
             *  (f_connected <= 1)
             *  - si le role ADMIN : accès en lecture à tous les submitform
             *  (f_connected 0,1,2,3)
             * 
             */
            boolean access=false;

            if (                 
                    // acces public
                    (f!=null && f.getF_connected()== Questionnaire.ACCESS_PUBLIC)
                    || // accès connecté front office (pour users public ou avec droits sur backoffice)
                    (userData != null && userData.isConnected()
                    && f!=null && f.getF_connected() <= Questionnaire.ACCESS_RESTRICTED      
                    && userData.isItsOwnData(sid,true, true)
                    && userData.getRole() <= SessionInfos.ROLE_GESTION ) 
                    || // accès connecté backoffice
                    (f!=null && f.getF_connected() < Questionnaire.ACCESS_RESTRICTED      
                    && userData != null 
                    && userData.getRole() >= SessionInfos.ROLE_CONSULTATION) 
                    || // admin
                    (userData != null && userData.getRole() == SessionInfos.ROLE_ADMIN ) 
                    ) access = true;

            if (!access) {
                return new StringBuffer("Droits insuffisants !"); // sortie
            }


            // début export (ou sous-export si appel récursif)

            QuestionnaireAction fa  = new QuestionnaireAction();
            SubmitFormAction sfa    = new SubmitFormAction();
            PropositionAction pa    = new PropositionAction();
            QuestionAction qa       = new QuestionAction();
            ReponseAction ra        = new ReponseAction();

            txt = new StringBuffer(tcontent);

            if (crlf == null)
                crlf = "<br/>";

            // recup submitform courant
            if (listSf==null) listSf = new HashMap<Integer, SubmitForm>();
            SubmitForm sf = listSf.get(sid);
            if (sf == null) {
                sf = sfa.selectBySId(sid);
                if (sf!=null) listSf.put(sid, sf); // stockage local pour les appels récursifs
            }


            // recup submitform Maître s'il existe
            SubmitForm sfMaster = null;
            if (sf != null && sf.getS_id_parent() > 0) {
                sfMaster = listSf.get(sf.getS_id_parent());
                if (sfMaster == null) {
                    sfMaster = sfa.selectBySidAndPwd(sf.getS_id_parent(), null, false, false, false);
                    if (sfMaster!=null) listSf.put(sfMaster.getS_id(), sfMaster); // stockage local pour les appels récursifs
                }

            }



            // DEBUT TRAITEMENT
            // recherche / remplace des balises <%xxxxxyyyyy%> ou <%xxxxxyyyyy|z%>
            int index = 0;
            int lengthBalise = 16;
            int loopLastIndex = 0;

            int fid = -1 ;
            int pid = -1 ;
            int qid = -1 ;

            boolean isFormatTextAlignLeft = false;
            boolean isFormatTextAlignRight = false;
            boolean isFormatEuro = false;

            int nbcol = 0;
            
            String currentStartTag = startTag;
            String currentEndTag = endTag;

            // type de tags - {% compatible avec RTE
            if (txt.indexOf("{%")>=0) {
                currentStartTag = "{%";
                currentEndTag = "%}";
            }

            while (sid >= 0 && txt.indexOf(currentStartTag, index) >= 0) {
                int posBalise = txt.indexOf(currentStartTag, index);
                int endBalise = posBalise + lengthBalise;

                curBalise = txt.substring(posBalise, endBalise);
                String val = "";

                // reinit vars
                fid = -1 ; 
                pid = -1 ; 
                qid = -1 ;
                isFormatTextAlignLeft = false;
                isFormatTextAlignRight = false;
                isFormatEuro = false;
                nbcol = 0;
                
                
                // Premier cas simple : fusion d'une balise de type {%F00123P00456%} ou  {%F00123Q00456%}
                if (curBalise.startsWith(currentStartTag+"F")) {


                    fid = BasicTools.getIntFrom5DigitFormatedInputName(curBalise,
                            "F");
                    pid = BasicTools.getIntFrom5DigitFormatedInputName(curBalise,
                            "P");
                    qid = BasicTools.getIntFrom5DigitFormatedInputName(curBalise,
                            "Q");


                    /** formatage de l'affichage du champ demandé :
                     * si |L0000x : formatage à gauche sur x caractères fixes (champ tronqué si trop grand)
                     * si |R0000x : formatage à droites sur x caractères fixes (champ tronqué si trop grand)
                     * si |E0000x : formatage à droite avec 00 sur x caractères (champ tronqué si trop grand)
                     * 
                     * exemple :
                     * {%F00001P00002|L00012%}
                     */
                    isFormatTextAlignLeft = (curBalise.indexOf("|L") >0);
                    isFormatTextAlignRight = (curBalise.indexOf("|R") >0);
                    isFormatEuro = (curBalise.indexOf("|E") >0);

                    nbcol = 0;

                    if (isFormatTextAlignLeft) nbcol = 
                            BasicTools.getIntFrom5DigitFormatedInputName(curBalise,"|L");
                    if (isFormatTextAlignRight) nbcol = 
                            BasicTools.getIntFrom5DigitFormatedInputName(curBalise,"|R");
                    if (isFormatEuro) nbcol = 
                            BasicTools.getIntFrom5DigitFormatedInputName(curBalise,"|E");


                    // question simple > affichage réponse proposition
                    if (sf != null && fid == sf.getF_id() && pid > 0) {
                        if (sf.getPropVal(pid)!=null) val = sf.getPropVal(pid);
                    }

                    // affichage infos form père, sauf s'ils sont du même type
                    if (sfMaster != null && fid == sfMaster.getF_id() && pid > 0 && sfMaster.getF_id()!=sf.getF_id()) {
                        if ( pid > 0) {
                            Reponse r = ra.selectOne(pid, sfMaster.getS_id()); // val = sfMaster.getPropVal(pid);
                            if (r!=null) val = r.getSVal();
                        }
                    }

                    // récupération de la question
                    Question quest = null;
                    if (pid>0) {
                        if (f==null || f.getId()!=sf.getF_id() || f.getNbGroupes()==0) {
                            try {
                                quest = qa.selectById(pa.selectById(pid).getQid());
                            } catch (Exception e) {}
                        } else {
                            quest = f.getQuestByPid(pid);
                        }
                    }

                    if (qid>0) {
                        if (f==null || f.getId()!=sf.getF_id() || f.getNbGroupes()==0) {
                            try {
                                quest = qa.selectById(qid);
                            } catch (Exception e) {}
                        } else {
                            quest = f.getQuestByQid(qid);
                        }
                    }


                    // formatage affichage euros
                    if (quest !=null && quest.getType().equals(Question.EURO)) 
                        val = val.replaceAll("[.]", ",");


                    // choix multiple ou exclusif ou liste forms> affichage texte(s) proposition(s)
                    // choisie(s)
                    if (sf != null && fid == sf.getF_id() && qid > 0 && quest!=null) {

                        int c = 0;

                        ValueBeanList vbl = f.getListProsByQid(qid);
                        if (vbl == null || vbl.size()==0) vbl = pa.selectAllByQuestId(qid);

                        // selection parmi liste de formulaire externes, on va chercher la liste
                        if (quest.getType().equals(Question.LIST_FORM) && vbl !=null)  {
                            Proposition prop = (Proposition)vbl.get(0);
                            int pidFormList = prop.getNum();
                            if (pidFormList>0) {
                                ValueBeanList listRep = new ReponseAction().selectAllByPId(pidFormList,true);
                                // réinit pour remplacer par les valeurs trouvées dans la base
                                vbl = new ValueBeanList();

                                if (listRep !=null) {
                                    for (ValueBean rep : listRep) {
                                        Proposition p = new Proposition( prop.getId(), ((Reponse)rep).getSVal(), 0, 0);
                                        p.setVal(((Reponse)rep).getS_id());
                                        p.setQid(prop.getQid());
                                        p.setVal(((Reponse)rep).getS_id());
                                        vbl.add(p);

                                        if (p!=null && p.getTexte() !=null && sf.getPropVal(p.getId(),p.getVal()).equals("1")) {
                                            // (ajout séparateur  si plusieurs items)
                                            if (c > 0) val += " / " ; //crlf;
                                            val += p.getTexte();
                                            c++;
                                        }
                                    }
                                }
                            }

                        } else {
                            // cas normal des choix exclusifs ou choix multiples 
                            // ... ou questions en readonly multi-items
                            for (int i = 0; i < vbl.size(); i++) {
                                Proposition p = (Proposition) vbl.get(i);

                                if (p!=null && p.getTexte() !=null &&  
                                        (sf.getPropVal(p.getId()).equals("1") || sf.getPropVal(p.getId()).equals("Oui"))) {
                                    // (ajout séparateur  si plusieurs items)
                                    if (c > 0) val += " / " ; //crlf;
                                    val += p.getTexte();
                                    c++;
                                }
                            }    
                        }

                    }

                    // affichage infos form père, sauf s'ils sont du même type
                    if (sfMaster != null && fid == sfMaster.getF_id() && qid > 0 && sfMaster.getF_id()!=sf.getF_id()) {

                        ValueBeanList vbl = pa.selectAllByQuestId(qid);

                        int c = 0;
                        for (int i = 0; i < vbl.size(); i++) {
                            Proposition p = (Proposition) vbl.get(i);

                            String sfMasterVal = "";
                            Reponse r = ra.selectOne(p.getId(), sfMaster.getS_id());
                            if (r!=null) sfMasterVal = r.getSVal();

                            if ("1".equals(sfMasterVal)) {   //if (sfMaster.getPropVal(p.getId()).equals("1")) {
                                // ajout séparateur  si plusieurs items
                                if (c > 0) val += " / " ; //crlf;

                                val += p.getTexte();
                                c++;
                            }
                        }
                    }




                    // demande d'affichage formulaire complet (groupes publics) / cas du {%F00123FULLTX%}
                    if (sf != null && qid <0 && pid < 0 && curBalise.indexOf("FULLTX")>0 ) {
                        Questionnaire qsf = fa.getFullQuest(sf.getF_id(), sf);
                        val = qsf.toFullHtml(true,false).toString();
                    }
                    
                 // demande d'affichage formulaire complet (groupes publics) / cas du {%F00123FULLTX%}
                    if (sf != null && qid <0 && pid < 0 && curBalise.indexOf("FULLTC")>0 ) {
                        Questionnaire qsf = fa.getFullQuest(sf.getF_id(), sf);
                        val = qsf.toFullHtml(true,true).toString();
                    }


                    // demande d'affichage formulaire complet (tous groupes) / cas du {%F00123FULLTP%}
                    if (sf != null && qid <0 && pid < 0 && curBalise.indexOf("FULLTP")>0 ) {
                        Questionnaire qsf = fa.getFullQuest(sf.getF_id(), sf);
                        val = qsf.toFullHtml(false,false).toString();
                    }

                    // demande d'export CSV du formulaire complet  (groupes publics) / cas du {%F00123FULLCS%}
                    if (sf != null && qid <0 && pid < 0 && curBalise.indexOf("FULLCS")>0 ) {
                        Questionnaire qsf = fa.getFullQuest(sf.getF_id(), sf);
                        val =  qsf.toFullCsv(true).toString();
                    }

                    // demande d'export CSV du formulaire complet  (tous groupes) / cas du {%F00123FULLCP%}
                    if (sf != null && qid <0 && pid < 0 && curBalise.indexOf("FULLCP")>0 ) {
                        Questionnaire qsf = fa.getFullQuest(sf.getF_id(), sf);
                        val =  qsf.toFullCsv(false).toString();
                    }

                }

                // balises spécifiques
                else if (curBalise.startsWith(currentStartTag+"DATE"))
                    val = BasicType.formatDateIsoToLocal(BasicType
                            .getTodaysDateIso(), null);
                else if (curBalise.startsWith(currentStartTag+"SDATE"))
                    val = BasicType.formatDateIsoToLocal(sf.getS_date(), null);

                else if (curBalise.startsWith(currentStartTag+"SDATH"))
                    val = BasicType.formatDateIsoToLocalDateTime(sf.getS_date());

                else if (curBalise.startsWith(currentStartTag+"SID"))
                    val = String.valueOf(sid);
                else if (curBalise.startsWith(currentStartTag+"PARENT_SID") && sfMaster != null)
                    val = String.valueOf(sfMaster.getS_id());
                else if (curBalise.startsWith(currentStartTag+"PARENT_SID") && sfMaster == null)
                    val = String.valueOf(sf.getS_id_parent());
                else if (curBalise.startsWith(currentStartTag+"SID_PARENT") && sfMaster != null)
                    val = String.valueOf(sfMaster.getS_id());
                else if (curBalise.startsWith(currentStartTag+"PWD") && f!=null && f.showPwd() )
                    val = sf.getPwd();
                else if (curBalise.startsWith(currentStartTag+"LOCKED"))
                    val = String.valueOf(sf.getLock());
                else if (curBalise.startsWith(currentStartTag+"INDEX"))
                    val = String.valueOf(indexVal);
                else if (curBalise.startsWith(currentStartTag+"LAST_INDEX"))
                    val = String.valueOf(loopLastIndex);
                else if (curBalise.startsWith(currentStartTag+"USERMAJ"))
                    val = String.valueOf(sf.getS_login_maj());
                else if (curBalise.startsWith(currentStartTag+"BASEURL"))
                    val = String.valueOf(userData.getBaseUrl());
                else if (curBalise.startsWith(currentStartTag+"TITLE"))
                    val = sf.getTitre();
                else if (curBalise.startsWith(currentStartTag+"TITRE"))
                    val = sf.getTitre();




                /** balise IF....P : 
                 * Conditionnement d'un fragment du template par une valeur de proposition (fixé par la balise CHECK)
                 * 
                 * exemple :
                 * 
                 * {%IF....P<pid>%} {%CHECK.P<pid>%}value1||value2||...{%/CHECKP<pid>%} 
                 * le contenu à afficher si la valeur de la proposition <pid> = value1 OU value2 OU ...
                 * {%/IF...P<pid>%}
                 *    
                 **/


                else if (curBalise.startsWith(currentStartTag+"IF....P")) {

                    String endIfBalise = currentStartTag+"/IF...P";

                    int ifPid = BasicTools.getIntFrom5DigitFormatedInputName(
                            curBalise, "P");
                    if (ifPid >0) endIfBalise = currentStartTag+"/IF...P"+BasicTools.formatWith0(ifPid,5);

                    int posEndIf = txt.indexOf(endIfBalise, posBalise);

                    String childTemplateContent = txt.substring(posBalise
                            + lengthBalise, posEndIf);

                    // recherche balise CHECK  pour la condition à vérifier :  
                    // condition du IF
                    int filterPid = -1;
                    String valFilter = null;
                    String endFilterBalise = currentStartTag+"/CHECK";

                    int posFilterPid = childTemplateContent.indexOf(currentStartTag+"CHECK", 0);
                    if (posFilterPid >= 0) {
                        filterPid = BasicTools.getIntFrom5DigitFormatedInputName(
                                childTemplateContent.substring(posFilterPid,
                                        posFilterPid + lengthBalise), "P");

                        int posEndFilter = childTemplateContent.indexOf(
                                endFilterBalise + "P"+BasicTools.formatWith0(filterPid, 5) + currentEndTag,
                                0);
                        valFilter = childTemplateContent.substring(
                                posFilterPid + lengthBalise,
                                posEndFilter);

                        // on retire la balise de fin
                        childTemplateContent = childTemplateContent.substring(
                                posEndFilter + lengthBalise);
                    }


                    val = "";
                    try { 
                        // test de la condition
                        LinkedList<String> list = new LinkedList<String>();
                        boolean isValid = false;
                        boolean checkTrue = true;

                        if (valFilter.startsWith("!") && valFilter.length()>=1) {
                            checkTrue = false;
                            valFilter = valFilter.substring(1);
                        }


                        if (valFilter.indexOf("||")>0) { 
                            // condition multiple : choix de valeurs en OU
                            list = BasicTools.parseLineToList(valFilter,"||");
                            for (int i=0; list !=null && i < list.size(); i++ ) {
                                String item = (String)list.get(i);
                                if (item != null && sf.getPropVal(filterPid).equals(item)) {
                                    isValid = true;
                                    break;
                                }
                            }
                        } else {
                            // cas normal pas de liste de valeurs
                            isValid = sf.getPropVal(filterPid).equals(valFilter);
                        }

                        // si on teste le contraire, on inverse le résultat
                        if (!checkTrue) isValid = !isValid ;


                        // la condition est valide
                        if (isValid) {
                            Template childTemplate = new Template();
                            childTemplate.setContent(childTemplateContent);
                            childTemplate.setFid(fid);


                            // appel récursif pour la sous-section s'il faut fusionner des élements
                            if (childTemplate.getContent().contains("{%")) {

                                StringBuffer childText = new StringBuffer();
                                childText.append(getContent(childTemplate.getContent(), sf.getS_id(), f, crlf, false, indexVal, params,  userData, listSf));
                                val = childText.toString();

                            } else {
                                val = childTemplate.getContent();
                            }


                        }

                    } catch (Exception e) { 
                        StringBuffer err = new StringBuffer();
                        err.append("Erreur ");
                        if (curBalise !=null ) err.append("Balise : " ).append(curBalise);
                        err.append(" >> ").append(e.getMessage());
                        return err;
                    }

                    // modif du endBalise pour passer à la suite
                    endBalise = posEndIf + lengthBalise;
                }



                /** balise IF....CONFIG : 
                 * Conditionnement d'un fragment du template par une valeur d'un paramètre de configuration 
                 * 
                 * exemple :
                 * 
                 * {%IF....CONFIG%} {%CHECK.CONFIG%}param{%/CHECKCONFIG%} 
                 * le contenu à afficher si la valeur du paramètre "param" est "TRUE"
                 * {%/IF...CONFIG%}
                 * 
                 * exemple test inverse :
                 * {%IF....CONFIG%} {%CHECK.CONFIG%}!param{%/CHECKCONFIG%} 
                 * le contenu à afficher si la valeur du paramètre "param" est "FALSE"
                 * {%/IF...CONFIG%}
                 * 
                 *   
                 **/


                else if (curBalise.startsWith(currentStartTag+"IF....CONFIG")) {

                    String endIfBalise = currentStartTag+"/IF...CONFIG";

                    int posEndIf = txt.indexOf(endIfBalise, posBalise);

                    String childTemplateContent = txt.substring(posBalise
                            + lengthBalise, posEndIf);

                    // recherche balise CHECK  pour la condition à vérifier :  
                    // condition du IF
                    String valFilter = null;
                    String endFilterBalise = currentStartTag+"/CHECKCONFIG";
                    boolean checkTrueOrFalse =true; 

                    int posFilter = childTemplateContent.indexOf(currentStartTag+"CHECK", 0);
                    if (posFilter >= 0) {

                        int posEndFilter = childTemplateContent.indexOf(endFilterBalise ,0);
                        valFilter = childTemplateContent.substring(
                                posFilter + lengthBalise,
                                posEndFilter);

                        if (valFilter != null && valFilter.startsWith("!")) {
                            valFilter = valFilter.substring(1);
                            checkTrueOrFalse = false;
                        }
                        // on retire les balises <filtre> et leur contenu entre
                        childTemplateContent = childTemplateContent.substring(
                                posEndFilter + lengthBalise);
                    }


                    val = "";

                    try { 
                        // test de la condition
                        if (params !=null && params.get(valFilter)!=null 
                                && (new java.lang.Boolean((String)params.get(valFilter))).booleanValue() == checkTrueOrFalse) {
                            Template childTemplate = new Template();
                            childTemplate.setContent(childTemplateContent);
                            childTemplate.setFid(fid);

                            StringBuffer childText = new StringBuffer();
                            // appel récursif pour la sous-section
                            childText.append(getContent(childTemplate.getContent(), sf.getS_id(), f, crlf, false, indexVal, params, userData, listSf));
                            val = childText.toString();

                        }

                    } catch (Exception e) { return new StringBuffer(e.getMessage()); }

                    // modif du endBalise pour passer à la suite
                    endBalise = posEndIf + lengthBalise;
                }


                /** balise GETPARAM : 
                 * récupération de la valeur d'un paramètre de l'URL, transmis dans l'objet "params" 
                 * 
                 * exemple :
                 * {%GETPARAM....%}name{%/GETPARAM...%}
                 *   
                 **/

                else if (curBalise.startsWith(currentStartTag+"GETPARAM")) {

                    String endGPBalise = currentStartTag+"/GETPARAM...";

                    int posGP = txt.indexOf(currentStartTag+"GETPARAM", posBalise);
                    int posEndGP = txt.indexOf(endGPBalise, posBalise);
                    String param = txt.substring(
                            posGP + lengthBalise,
                            posEndGP);

                    if (params.containsKey(param)) val = params.get(param);

                    // modif du endBalise pour passer à la suite
                    endBalise = posEndGP + lengthBalise;
                }

                /** balise IF....LOCKED : 
                 * Conditionnement d'un fragment du template par une valeur d'un paramètre de configuration 
                 * 
                 * exemple :
                 * 
                 * {%IF....LOCKED%} {%CHECK.LOCKED%}true{%/CHECKLOCKED%} 
                 * le contenu à afficher si le SF est validé / verrouillé
                 * {%/IF...LOCKED%}
                 * 
                 *   
                 **/


                else if (curBalise.startsWith(currentStartTag+"IF....LOCKED")) {

                    String endIfBalise = currentStartTag+"/IF...LOCKED";

                    int posEndIf = txt.indexOf(endIfBalise, posBalise);

                    String childTemplateContent = txt.substring(posBalise
                            + lengthBalise, posEndIf);

                    // recherche balise CHECK  pour la condition à vérifier :  
                    // condition du IF
                    String valFilter = null;
                    String endFilterBalise = currentStartTag+"/CHECKLOCKED";

                    int posFilter = childTemplateContent.indexOf(currentStartTag+"CHECK", 0);
                    if (posFilter >= 0) {

                        int posEndFilter = childTemplateContent.indexOf(endFilterBalise ,0);
                        valFilter = childTemplateContent.substring(
                                posFilter + lengthBalise,
                                posEndFilter);

                        // on retire les balises <filtre> et leur contenu entre
                        childTemplateContent = childTemplateContent.substring(
                                posEndFilter + lengthBalise);
                    }


                    val = "";
                    boolean checkLocked = (new java.lang.Boolean(valFilter)).booleanValue() ;
                    try { 
                        // test de la condition
                        if ( sf.isLocked() == checkLocked ) {
                            Template childTemplate = new Template();
                            childTemplate.setContent(childTemplateContent);
                            childTemplate.setFid(fid);

                            StringBuffer childText = new StringBuffer();
                            // appel récursif pour la sous-section
                            childText.append(getContent(childTemplate.getContent(), sf.getS_id(), f, crlf, false, indexVal, params, userData, listSf));
                            val = childText.toString();
                        }
                    } catch (Exception e) { return new StringBuffer(e.getMessage()); }

                    // modif du endBalise pour passer à la suite
                    endBalise = posEndIf + lengthBalise;
                }



                /** Balise LOOP : {%LOOP..F<fid>%}...{%/LOOP.F<fid>%}
                 *  
                 * Permet d'inclure un template faisant appel
                 * aux données d'autres formulaires liés de type <fid>
                 * Ce fragment de template sera dupliqué autant de fois 
                 * que l'on trouve de formulaires liés. 
                 *
                 * exemple :
                 * {%LOOP..F00123%}...{%/LOOP.F00123%}
                 * 
                 *     
                 * Par défaut, les formulaires liés sont les formulaires fils.
                 * Mais il est possible d'utiliser d'autres balises pour choisir
                 * les formulaires à inclure ("link"), ou les filtrer ("filter")(voir plus bas)
                 * 
                 * cas particuliers : 
                 *  - la balise LOOPR permet un appel récursif sur chaque formulaire fils 
                 *  (avec les mêmes critères de lien ou filtre)
                 * 
                 */

                else if (curBalise.startsWith(currentStartTag+"LOOP")) {

                    boolean isRecursiveCall = (curBalise.indexOf("R")>0) ;
                    
                    fid = BasicTools.getIntFrom5DigitFormatedInputName(curBalise,"F");
                    
                    loopLastIndex = 0;
                    //String endLoopBalise = currentStartTag+"/LOOP.F" 
                    String endLoopBalise = currentStartTag+"/LOOP"+ (isRecursiveCall?"R":".") +"F"
                            + BasicTools.formatWith0(fid, 5) + currentEndTag;
                    int posEndLoop = txt.indexOf(endLoopBalise, posBalise);

                    String childTemplateContent = txt.substring(posBalise
                            + lengthBalise, posEndLoop);


                    String loopChildTemplateContent = txt.substring(posBalise
                            , posEndLoop + lengthBalise);

                    StringBuffer childText = new StringBuffer();


                    /** cas d'un LINK.P : le SID du form courant  est stocké
                     * dans une proposition <pid> du (ou des) forms qu'on cherche à inclure
                     * 
                     * exemple :
                     * {%LINKP.P<pid>%} 
                     */

                    int linkPid = -1;
                    int posLinkPid = childTemplateContent.indexOf(currentStartTag+"LINKP.P", 0);
                    int posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < posLinkPid) posLinkPid = -1; //le LINK s'applique à une autre LOOP 


                    if (posLinkPid >= 0) {
                        linkPid = BasicTools.getIntFrom5DigitFormatedInputName(
                                childTemplateContent.substring(posLinkPid,
                                        posLinkPid + lengthBalise), "P");
                        // on retire les balises {%link... du template fils
                        childTemplateContent = childTemplateContent.substring(
                                posLinkPid + lengthBalise);
                    }


                    /** cas d'un LINKS.P : une proposition (pid) du form courant stocke
                     * le SID du form qu'on cherche à inclure
                     * 
                     * exemple :
                     * {%LINKS.P<pid>%} 
                     */

                    int linkSid = -1;
                    posLinkPid = childTemplateContent.indexOf(currentStartTag+"LINKS.P", 0);
                    posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < posLinkPid) posLinkPid = -1; //le LINK s'applique à une autre LOOP 


                    if (posLinkPid >=0 && childTemplateContent.indexOf(currentStartTag+"LINKS.P", 0)>=0) {

                        linkPid = BasicTools.getIntFrom5DigitFormatedInputName(
                                childTemplateContent.substring(posLinkPid,
                                        posLinkPid + lengthBalise), "P");
                        try {
                            linkSid = Integer.parseInt(sf.getPropVal(linkPid));
                        } catch (NumberFormatException e) {}
                        if (linkSid<0) {
                            try {
                                Reponse rep = ra.selectOne(linkPid, sf.getS_id());
                                if (rep!=null) linkSid = Integer.parseInt(rep.getSVal());
                            } catch (NumberFormatException e) {}
                        }

                        // on retire les balises {%linked... du template fils
                        childTemplateContent = childTemplateContent.substring(
                                posLinkPid + lengthBalise);
                    }


                    /** cas d'un LINKV.P : on recherche les forms qui ont une 
                     * valeur de proposition "valLinkVid" identique à la valeur située
                     * entre les balises "linkv.p"
                     * 
                     * exemple :
                     * {%LINKV.P<pid>%}val{%/LINKVP<pid>%} 
                     */

                    int linkVid = -1;
                    String valLinkVid = null; 
                    int posLinkVid = childTemplateContent.indexOf(currentStartTag+"LINKV.P", 0);
                    String endLinkVBalise = currentStartTag+"/LINKVP";
                    posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < posLinkVid) posLinkVid = -1; //le LINK s'applique à une autre LOOP 

                    if (posLinkVid >= 0) {
                        linkVid = BasicTools.getIntFrom5DigitFormatedInputName(
                                childTemplateContent.substring(posLinkVid,
                                        posLinkVid + lengthBalise), "P"); //"V" ?

                        int posEndLinkVid = childTemplateContent.indexOf(
                                endLinkVBalise + BasicTools.formatWith0(linkVid, 5) + currentEndTag,
                                0);
                        valLinkVid = childTemplateContent.substring(
                                posLinkVid + lengthBalise,
                                posEndLinkVid);

                        if (valLinkVid.indexOf("{%")>=0) {
                            // on fait un appel récursif pour  parser la chaîne, pour récupérer  la valeur à comparer
                            StringBuffer sbuf = getContent(valLinkVid, sid, f, crlf, forCsv, indexVal, params, userData,listSf); 
                            valLinkVid = sbuf.toString(); 
                        }

                        // on retire les balises {%link... du template fils
                        childTemplateContent = childTemplateContent.substring(
                                posEndLinkVid + lengthBalise);
                    }


                    /** cas d'un LINKSID : on recherche le form de type "fid", qui a le SID
                     * correspondant à la valeur située entre les balises "linksid" 
                     * 
                     * exemple :
                     * {%LINKSID.....%}<sid>{%/LINKSID....%} 
                     */ 

                    int posLinkSid = childTemplateContent.indexOf(currentStartTag+"LINKSID", 0);
                    posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < posLinkSid) posLinkSid = -1; //le LINK s'applique à une autre LOOP 

                    if (posLinkSid >=0) {

                        String endLinkSidBalise = currentStartTag+"/LINKSID";

                        int posEndLinkSidBalise = childTemplateContent.indexOf(endLinkSidBalise);
                        try {
                            linkSid = Integer.parseInt( 
                                    childTemplateContent.substring(
                                            childTemplateContent.indexOf(currentStartTag+"LINKSID", 0) + lengthBalise, posEndLinkSidBalise));
                        } catch (NumberFormatException e) {}

                        // on retire les balises {%link... du template fils
                        childTemplateContent = childTemplateContent.substring(
                                posEndLinkSidBalise + lengthBalise);
                    }



                    /** cas d'un LINK_SPARENT : on recherche les formulaires fils du 
                     * formulaire courant.
                     * (C'est le fonctionnement par défaut, mais on peut avoir à le
                     * redéfinir dans le cas d'un appel
                     * récursif pour un sous-template)
                     * 
                     * exemple :
                     * {%LINK_SPARENT%} 
                     * 
                     */
                    int parent_sid = -1;
                    int poslinkParentPid = childTemplateContent.indexOf(
                            currentStartTag+"LINK_SPARENT", 0);
                    posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < poslinkParentPid) poslinkParentPid = -1; //le LINK s'applique à une autre LOOP 

                    if (poslinkParentPid >=0) {
                        parent_sid = sf.getS_id();

                        //                  on retire les balises {%link... du template fils
                        childTemplateContent = childTemplateContent.substring(
                                poslinkParentPid + lengthBalise);
                    }


                    /** cas d'un LINK_SCHILD : on recherche le formulaire pére du 
                     * formulaire courant.
                     * (par défaut il suffit d'utiliser une balise avec le fid du form
                     * pére, mais on peut avoir à le redéfinir dans le cas d'un appel
                     * récursif pour un sous-template)
                     * 
                     * exemple :
                     * {%LINK_SCHILD.%} 
                     * 
                     */

                    int childOf_sid = -1;
                    int poslinkChildOfPid = childTemplateContent.indexOf(
                            currentStartTag+"LINK_SCHILD", 0);
                    posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < poslinkChildOfPid) poslinkChildOfPid = -1; //le LINK s'applique à une autre LOOP 

                    if ( poslinkChildOfPid >=0) {
                        childOf_sid = sf.getS_id_parent();

                        //                  on retire les balises {%link... du template fils
                        childTemplateContent = childTemplateContent.substring(
                                poslinkChildOfPid + lengthBalise);
                    }



                    /** cas d'un LINKQ.P : le SID du form courant  est stocké
                     * dans le champ R_VAL d'une proposition <pid> des forms qu'on cherche à inclure
                     * Le P_id correspond à une question de type "liste dynamique de formulaires" 
                     * le p_num de la proposition correspond au p_id_titre du form courant.
                     * 
                     * exemple :
                     * {%LINKQ.P<pid>%} 
                     */

                    int linkPRid = -1;
                    int posLinkPRid = childTemplateContent.indexOf(currentStartTag+"LINKQ.P", 0);
                    posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < posLinkPRid) posLinkPRid = -1; //le LINK s'applique à une autre LOOP 

                    if (posLinkPRid >= 0) {
                        linkPRid = BasicTools.getIntFrom5DigitFormatedInputName(
                                childTemplateContent.substring(posLinkPRid,
                                        posLinkPRid + lengthBalise), "P");
                        // on retire les balises {%link... du template fils
                        childTemplateContent = childTemplateContent.substring(
                                posLinkPRid + lengthBalise);
                    }





                    /** cas d'un FILTER : on va restreindre les fils du form courant
                     * (par défaut le loop raméne les fils) à ceux dont la valeur de la 
                     * proposition <pid>=value
                     * 
                     * exemple :
                     * {%FILTERP<pid>%}val{%/FILTE<pid>%} 
                     * 
                     * val peut étre une valeur dynamique, 
                     * exemple :  {%F0000xP0000y%} ou {%GETPARAM....%}(...)
                     */

                    // à partir d'un PID d'une question 
                    int filterPid = -1;
                    String valFilter = null;
                    String endFilterBalise = currentStartTag+"/FILTE";
                    int posFilterPid = childTemplateContent.indexOf(currentStartTag+"FILTER", 0);
                    posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < posFilterPid) posFilterPid = -1; //le filter s'applique à une autre LOOP 

                    if (posFilterPid >= 0) {
                        filterPid = BasicTools.getIntFrom5DigitFormatedInputName(
                                childTemplateContent.substring(posFilterPid,
                                        posFilterPid + lengthBalise), "P");

                        int posEndFilter = childTemplateContent.indexOf(
                                endFilterBalise + "P"+BasicTools.formatWith0(filterPid, 5) + currentEndTag,
                                0);
                        valFilter = childTemplateContent.substring(
                                posFilterPid + lengthBalise,
                                posEndFilter);

                        if (valFilter.indexOf(currentStartTag)>=0) {
                            // on refait un appel récursif pour récupérer la valeur dynamiquement !

                            valFilter = getContent(valFilter, sf.getS_id(), f, crlf, false, -1, params, userData, listSf).toString();
                            // /!\ modif pour ruse pour évaluations 2015 /!\
                            //valFilter = getContent(valFilter, sfMaster.getS_id_parent(), f, crlf, false, -1, params, userData).toString();
                        }

                        // on retire les balises <filtre> et leur contenu entre
                        childTemplateContent = childTemplateContent.substring(
                                posEndFilter + lengthBalise);


                    }

                    /** cas d'un LIMIT : on va restreindre les sf aux N premiers
                     * 
                     * exemple :
                     * {%LIMIT.......%}N{%/LIMIT......%} 
                     * 
                     */

                    String valLimit = null;
                    String endLimitBalise = currentStartTag+"/LIMIT";
                    int posLimit = childTemplateContent.indexOf(currentStartTag+"LIMIT", 0);
                    posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < posLimit) posLimit = -1; //le LIMIT s'applique à une autre LOOP 

                    if (posLimit >= 0) {
                        int posEndLimit = childTemplateContent.indexOf(
                                endLimitBalise ,
                                0);
                        valLimit = childTemplateContent.substring(
                                posLimit + lengthBalise,
                                posEndLimit);
                        if (!Controls.isBlank(valLimit)) valLimit = " LIMIT "+valLimit;

                        // on retire les balises <filtre> et leur contenu entre
                        childTemplateContent = childTemplateContent.substring(
                                posEndLimit + lengthBalise);


                    }


                    /** cas d'un SORT : on va trier les SF résultat sur la valeur de la 
                     * proposition <pid>, en mode VAL ("asc" ou "desc")
                     * ! le SORT ne s'applique pas avec un FILTER
                     * ! il faut que tous les submitForm aient bien une réponse existante, 
                     * sinon cela peut retirer des SF de la sélection
                     * 
                     * exemple :
                     * {%SORT..P<pid>%}desc{%/SORT.<pid>%} 
                     * 
                     */

                    // à partir d'un PID d'une question 
                    int sortPid = -1;
                    String sortVal = null;
                    String endSortBalise = currentStartTag+"/SORT.";
                    String sortOrder = null;

                    int posSortPid = childTemplateContent.indexOf(currentStartTag+"SORT", 0);
                    posOtherLoop = childTemplateContent.indexOf(currentStartTag+"LOOP", 0);
                    if (posOtherLoop >=0 && posOtherLoop < posSortPid) posSortPid = -1; //le SORT s'applique à une autre LOOP 


                    if (posSortPid >= 0) {
                        sortPid = BasicTools.getIntFrom5DigitFormatedInputName(
                                childTemplateContent.substring(posSortPid,
                                        posSortPid + lengthBalise), "P");

                        int posEndSort = childTemplateContent.indexOf(
                                endSortBalise + "P"+BasicTools.formatWith0(sortPid, 5) + currentEndTag,
                                0);
                        sortVal = childTemplateContent.substring(
                                posSortPid + lengthBalise,
                                posEndSort);

                        // on retire les balises <Sort> et leur contenu entre
                        childTemplateContent = childTemplateContent.substring(
                                posEndSort + lengthBalise);

                        if (sortPid >0) sortOrder = " ORDER BY r2.r_text ";
                        if (sortOrder!=null && sortVal!=null && sortVal.toLowerCase().equals("desc") ) sortOrder += " DESC ";
                        if (sortOrder!=null) sortOrder += " , rt.r_text ";
                        sortVal = "%";
                    }




                    /////////////////////////////////////////////////////////////////////////////////////////
                    // Etape : récupération des formulaires fils
                    //////////////////////////////////////////////////////////////////////////////////////////


                    ValueBeanList childList = null;
                    if (Controls.isBlank(sortOrder)) sortOrder = " order by rt.r_text";
                    if (!Controls.isBlank(valLimit)) sortOrder += valLimit ;

                    if (linkPid < 0 && filterPid < 0 && parent_sid < 0 
                            && childOf_sid < 0 && linkSid < 0 && linkVid < 0 && linkPRid<0) {
                        // pas de LINK ni FILTRE
                        // recherche directe sur les formulaires fils par défaut
                        if (sortPid >0) {
                            childList = sfa.selectBySearchInfos(-1, sortPid, 0, 0,
                                    null, sortVal, null, Question.READ_ONLY, null, 
                                    null, sid, -1, sortOrder );
                        } else {
                            childList = sfa.selectAllChildrenByFid(sid, fid, sortOrder, false, -1);
                        }

                    } else if (childOf_sid >0 ){
                        // récupération du form pére
                        SubmitForm sf_childOf = sfa.selectBySId(childOf_sid);
                        if (sf_childOf != null) {
                            //sfa.getInitVals(sf_childOf);
                            listSf.put(childOf_sid, sf_childOf);
                            childList = new ValueBeanList();
                            childList.add(sf_childOf);
                        }

                    } else if (linkSid >0 ){
                        // récupération d'un  submitform lié
                        SubmitForm sf_link = 
                                sfa.selectBySId(linkSid);
                        if (sf_link != null) {
                            if (childList == null) childList = new ValueBeanList();
                            childList.add(sf_link);
                        }

                    } else if (linkPRid >0 ){
                        // recherche des submitforms par le R_Val
                        childList = sfa.selectByPidAndValAndRval(linkPRid, "1", sid, false);


                    } else if (linkVid>0 ) {

                        childList = sfa.selectBySearchInfos(linkVid, filterPid, 0, 0,
                                valLinkVid, valFilter, Question.READ_ONLY,
                                Question.READ_ONLY, null, 
                                null, 
                                -1,
                                -1, sortOrder);


                    } else  {
                        // récupération des formulaires fils
                        // le SORT ne s'applique pas avec un FILTER
                        childList = sfa.selectBySearchInfos(linkPid, filterPid, 0, 0,
                                String.valueOf(sid), valFilter, Question.FORM_ID,
                                Question.CHOIX_EXCLUSIF, null, 
                                null, 
                                parent_sid,
                                -1, sortOrder);
                    }



                    Template childTemplate = new Template();

                    ///////////////////////////////////
                    // appel récursif avec le fragment de template pour chaque SF
                    for (int i = 0; childList != null && i < childList.size(); i++) {

                        childTemplate.setContent(childTemplateContent);
                        childTemplate.setFid(fid);

                        SubmitForm childSf = (SubmitForm) childList.get(i);

                        if (childSf.getF_id() != f.getId() ) {
                            f = userData.getQuest(childSf.getF_id());
                            if (f==null || f.getNbGroupes()==0) f = new QuestionnaireAction().getFullQuest(childSf.getF_id());
                        }

                        if (!isRecursiveCall) {

                            // (appel récursif)
                            childText.append(getContent(childTemplate.getContent(), childSf
                                    .getS_id(), f, crlf, false, i+1, params, userData, listSf));

                        } else {
                            // appel arborescent recursif : on réappelle la fusion du template 
                            // mais en conservant les balises loop pour fusionner les petits fils

                            // (appel récursif)
                            childText.append(getContent(childTemplate.getContent(), childSf
                                    .getS_id(), f, crlf, false, indexVal+1, params, userData, listSf));

                            childTemplate = new Template();
                            childTemplate.setContent(loopChildTemplateContent);
                            childTemplate.setFid(fid);
                            childText.append(getContent(childTemplate.getContent(), childSf
                                    .getS_id(), f, crlf, false, indexVal +1, params, userData, listSf));
                        }


                    }
                    val = childText.toString();

                    // modif du endBalise pour remplacer le contenu
                    // entre les balises LOOP
                    endBalise = posEndLoop + lengthBalise;

                    // attribution du looplastindex (affichage nb de lignes)
                    if (childList != null ) {
                        loopLastIndex = childList.size();

                        // traitement du remplacement du LOOP LAST INDEX (total des fils s'il y a eu boucle)
                        int posLastIndexBalise= txt.indexOf(currentStartTag+"LAST_INDEX");
                        if (posLastIndexBalise >=0)
                            txt.replace(posLastIndexBalise, posLastIndexBalise + lengthBalise , String.valueOf(loopLastIndex));
                    }




                } // fin traitement balise LOOP
                
                
                
            // TRAITEMENTS COMPLEMENTAIRES de caractères

                // traitement caractères type &#8217;
                val = BasicTools.deEncode(val);

                // on retire les CRLF pour les adapter à la sortie, 
                // sauf dans le cas
                // des LOOP ou IF ou l'appel récursif ou [fullcsv] est déjà passé par là...
                if (!curBalise.startsWith(currentStartTag+"IF")
                        && !curBalise.startsWith(currentStartTag+"LOOP") 
                        && curBalise.indexOf("FULLC")<0
                        && !val.contains("<br")
                        && !val.contains("<table")) { 
                    val = BasicTools.replaceCrLf(val, crlf);
                }

                // on retire les ; pour ne pas boulverser l'ordre des colonnes,
                // sauf dans le cas
                // des LOOP ou IF ou l'appel récursif ou [fullcsv] est déjà passé par là...
                if (forCsv && !curBalise.startsWith(currentStartTag+"LOOP") && curBalise.indexOf("FULLC")<0 ) {
                    val = BasicTools.replaceCrLf(val);
                    val = val.replaceAll(";", "/");
                }

                // formatage en colonnes 
                if (isFormatTextAlignLeft && nbcol>0) 
                    val= BasicTools.formatWithChar(val,' ',nbcol, true);
                if (isFormatTextAlignRight && nbcol>0) 
                    val= BasicTools.formatWithChar(val,' ',nbcol, false);
                if (isFormatEuro && nbcol>0) {
                    try {
                        Float fe = Float.valueOf(val);
                        Float f100 = new Float(100);
                        fe = new Float (f100.floatValue()*fe.floatValue());
                        val = ""+ fe.longValue() ;
                    } catch (Exception e) {
                        val = "?";
                    }
                    val= BasicTools.formatWithChar(val,'0',nbcol, false);
                }


                // on remplace (enfin) le texte
                txt = txt.replace(posBalise, endBalise, val);

                if (val != null)
                    index = index + val.length();

            } // while

        } catch (Exception e) {
            StringBuffer err = new StringBuffer();
            err.append("Erreur ");
            if (curBalise !=null ) err.append("Balise : " ).append(curBalise);
            err.append(" >> ").append(e.getMessage());
            return err;
        }
        return txt;
    }



    /**
     * @param txt
     * @return
     */
    static private StringBuffer prepareMailTo(StringBuffer txt) {

        String name = "";
        String to = "";
        String subject = "";
        String comments = BasicTools.getHtmComment(txt.toString());

        name = BasicTools.getAttribute("name", comments);
        to = BasicTools.getAttribute("to", comments);
        subject = BasicTools.getAttribute("subject", comments);

        System.out.println(name);
        System.out.println(to);
        System.out.println(subject);

        StringBuffer txtMailto = new StringBuffer();

        txtMailto
        .append("<html><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        txtMailto.append("<TITLE>Email</TITLE>");
        txtMailto
        .append("<link href=\"theme/Master.css\" rel=\"stylesheet\" type=\"text/css\">");

        txtMailto.append("<SCRIPT LANGUAGE=\"JavaScript\">"
                + "function copyit2(theField) {" + "theField.focus(); "
                + "theField.select(); "
                + "therange=document.selection.createRange();"
                + "therange.execCommand(\"Copy\"); " + "} </script>");

        txtMailto.append("</HEAD>");
        txtMailto
        .append("<body><form name=form><br><br><br><br><table align=center valign=center class=etape>");
        txtMailto.append("<tr><td><br>");
        txtMailto.append(name);
        txtMailto.append("v</td></tr>");

        txtMailto
        .append("<tr><td><textarea name=mailbody id=mailbody cols=50 rows=10>");
        txtMailto.append(BasicTools.cleanHTML(txt.toString()));
        txtMailto.append("</textarea></td></tr>");

        txtMailto
        .append("<tr><td><br>Cliquer sur -Copier- pour récupérer le texte du mail<br>");
        txtMailto
        .append("Cliquer ensuite sur -Envoyer le message-,<br> et coller le texte du mail (CTRL+V)<br>");
        txtMailto
        .append("<br><input type='button' name='copy' onclick='javascript:copyit2(document.form.mailbody);' value='Copier'>");
        txtMailto
        .append("&nbsp;&nbsp;&nbsp;<input type='button' name='mail' onclick=\"location.href='mailto:");
        txtMailto.append(to);
        txtMailto.append("?subject=");
        txtMailto.append(BasicTools.htmlConvert(subject));
        txtMailto.append("'\" value='Envoyer le message'>");
        txtMailto
        .append("&nbsp;&nbsp;&nbsp;(<a href='ServControl'>Retour</a>)");
        txtMailto.append("<br></td></tr></table></form>");

        txtMailto.append("</body></html>");

        System.out.println(txtMailto);

        return txtMailto;
    }


    /** Contrôle droits pour l'export en focntion du réle du user connecté 
     * et du niveau de protection du formulaire
     * 
     * 
     * @param ud
     * @param f
     * @return
     */
    static public boolean isAllowed(UserData ud, Questionnaire f, int sid) {
        // contrôle droits
        if (f.getF_connected() >= Questionnaire.ACCESS_CONNECTED && (ud == null || !ud.isConnected())) 
            return false;

        if (f.getF_connected() >= Questionnaire.ACCESS_RESTRICTED 
                && f.getF_connected() > ud.getRole()
                && !ud.isItsOwnData(sid, true, true))
            return false;

        return true;

    }

    static public StringBuffer getHeader(Template t) throws Exception {
        return getHeader(new StringBuffer(t.getContent()));
    }

    static public StringBuffer getHeader(StringBuffer txt) throws Exception {

        QuestionAction qa = new QuestionAction();
        PropositionAction pa = new PropositionAction();

        // DEBUT TRAITEMENT
        // recherche / remplace des balises <%...%>
        int index = 0;
        int lengthBalise = 16;

        String currentStartTag = startTag;
        String currentEndTag = endTag;

        if (txt.indexOf("{%")>=0) {
            currentStartTag = "{%";
            currentEndTag = "%}";
        }

        while (txt.indexOf(currentStartTag, index) >= 0) {
            int posBalise = txt.indexOf(currentStartTag, index);

            int endBalise = txt.indexOf(currentEndTag, index) + currentEndTag.length(); //posBalise + lengthBalise;
            lengthBalise = endBalise - posBalise;


            String curBalise = txt.substring(posBalise, posBalise
                    + lengthBalise);
            String val = "";

            // int fid =
            // BasicTools.getIntFrom5DigitFormatedInputName(curBalise,"F");
            int pid = BasicTools.getIntFrom5DigitFormatedInputName(curBalise,
                    "P");
            int qid = BasicTools.getIntFrom5DigitFormatedInputName(curBalise,
                    "Q");

            try {


                // proposition : récup id question
                Proposition p = null;
                if (pid > 0) {
                    p = pa.selectById(pid);
                    qid = p.getQid();
                    if (!Controls.isBlank(p.getTexte()))
                        val = "Q" + qid + ":" + p.getTexte();
                }

                int nbCarMax = 40;

                // > affichage libelle question
                if (qid > 0) {
                    Question q = qa.selectById(qid);
                    val = q.getTexte();

                    // traitement caractères type &#8217;
                    val = BasicTools.deEncode(val);
                    val = BasicTools.replaceCrLf(val);
                    val = BasicTools.cleanHTML(val);

                    if (val.length() > nbCarMax)
                        val = val.substring(0, nbCarMax) + "...";

                    if (p != null && !Controls.isBlank(p.getTexte())) {
                        String pval = p.getTexte();
                        pval = BasicTools.deEncode(pval);
                        pval = BasicTools.replaceCrLf(pval);
                        pval = BasicTools.cleanHTML(pval);

                        if (pval.length() > nbCarMax) {
                            val += " - " + pval.substring(0, nbCarMax);
                        } else {
                            val += " - " + pval;
                        }
                    }

                }

                if (val!=null) val=val.replaceAll(";"," / ");

            } catch (Exception e) { val="?"; }

            // balises spécifiques
            if (curBalise.startsWith(currentStartTag + "DATE"))
                val = "DATE";
            if (curBalise.startsWith(currentStartTag + "SDATE"))
                val = "DATE";
            if (curBalise.startsWith(currentStartTag + "SID"))
                val = "N°";
            if (curBalise.startsWith(currentStartTag + "PWD"))
                val = "Pwd";
            if (curBalise.startsWith(currentStartTag + "LOCKED"))
                val = "Lock";
            if (curBalise.startsWith(currentStartTag + "PARENT_SID"))
                val = "sid_parent";

            if (curBalise.contains("FULLCS")) {
                QuestionnaireAction qast = new QuestionnaireAction();
                int fid = BasicTools.getIntFrom5DigitFormatedInputName(curBalise.substring(0, 8), "F");
                Questionnaire f = qast.getFullQuest(fid);

                val = f.getFullCsvTitles(true).toString();
            }

            if (curBalise.contains("FULLCP")) {
                QuestionnaireAction qast = new QuestionnaireAction();
                int fid = BasicTools.getIntFrom5DigitFormatedInputName(curBalise.substring(0, 8), "F");
                Questionnaire f = qast.getFullQuest(fid);

                val = f.getFullCsvTitles(false).toString();
            }


            if (curBalise.startsWith(currentStartTag + "LINK")) val = "";
            if (curBalise.startsWith(currentStartTag + "FILTER")) val = "";
            if (curBalise.startsWith(currentStartTag + "CHECK")) val = "";

            txt = txt.replace(posBalise, (posBalise + lengthBalise), val);
            if (val != null)
                index = index + val.length();

        }

        return txt;
    }

    /**
     * récupération en base du texte d'une proposition de question multiple si la proposition est sélectionnée
     * @param sf
     * @param qid
     * @return le texte de la proposition sélectionnée
     */
    static private String getPropTexte(SubmitForm sf, int qid) {
        String res = "";
        PropositionAction pa = new PropositionAction();
        Question q = new Question();
        try {
            q.setPropositions(pa.selectAllByQuestId(qid));

            for (int i = 0; i < q.getNbPropositions(); i++) {
                Proposition p = q.getProposition(i);
                if (sf.getPropVal(p.getId()).equals("1")) {
                    res = p.getTexte();
                    break;
                }
            }

        } catch (Exception e) {
            return null;
        }
        return res;
    }



    /***************************************************************************
     * Exporte des SubmitForm au format XML
     * 
     * @param
     * @return
     */
    static public int exportSFXML(int fid, int lockedOnly, OutputStream out) {
        int result = 0;
        SubmitFormAction sfa = new SubmitFormAction();
        ValueBeanList v = null;

        try {

            v = sfa.selectAllByFormId(fid, lockedOnly);
            if (v != null) {
                PrintStream fichier = new PrintStream(out, true, "UTF-8");
                
                fichier.write("<?xml version='1.0' encoding='UTF-8'?>".getBytes("UTF-8"));
                fichier.write(("<export date=\""
                        + BasicType.getTodaysDateIso() + " "
                        + BasicType.getTimeIso() + "\">").getBytes("UTF-8"));

                for (Iterator iter = v.iterator(); iter.hasNext();) {
                    SubmitForm s = (SubmitForm) iter.next();
                    SubmitForm sFull = sfa.selectBySId(s.getS_id());

                    fichier.write(sFull.toXML().getBytes("UTF-8"));
                    result++;
                }
                fichier.println("</export>".getBytes("UTF-8"));
                out.flush();
            }
        } catch (Exception e) {
            InitServlet.traceLog.error("Erreur lors de l'exportation" + e);
            result = -1;
        }

        return result;
    }


    /**
     * Ecrit dans un flux le contenu de tous les formulaires d'un type donné.
     * 
     * @param fid
     * @param lockedOnly
     * @param dateMin
     * @param dateMax
     * @param out
     * @param sid
     * @param norep
     * @return
     */
    static public int exportXML(int fid, int lockedOnly, String dateMin,
            String dateMax, OutputStream out, int sid, boolean norep, UserData ud) {
        return exportXML( fid,  lockedOnly,  dateMin,
                dateMax,  out,  sid, -1,  norep, -1, null, ud);
    }
    /**
     * Ecrit dans un flux le contenu de tous les formulaires d'un type donné.
     * 
     * @param fid
     * @param lockedOnly
     * @param dateMin
     * @param dateMax
     * @param out
     * @param sid
     * @param norep
     * @return
     */
    static public int exportXML(int fid, int lockedOnly, String dateMin,
            String dateMax, OutputStream out, int sid, int parentSid, boolean norep, 
            int pidRequired, String valRequired, UserData ud) {

        int result = 0;
        SubmitFormAction sfa = new SubmitFormAction();
        QuestionnaireAction qa = new QuestionnaireAction();
        Questionnaire f = null;
        ValueBeanList list = null;
        ValueBeanList listSfId =  null;




        try {
            f = qa.getFullQuest(fid);

            // contrôle droits
            if (!isAllowed(ud,f, sid)) return -1;


            if (pidRequired >0 ) {
                // selection avec un filtre sur une valeur de PID
                listSfId = sfa.selectAllByPropIdAndVal(pidRequired, valRequired, lockedOnly);

            } else if (fid > 0 && parentSid >= 0) {
                // sélection sur le parentSId + FID
                listSfId = sfa.selectAllChildrenByFid(parentSid, fid, null, false, lockedOnly);

            } else if (fid > 0 && sid < 0) {
                // sélection sur le f_id
                listSfId = sfa.selectAllByFormId(fid, lockedOnly, dateMin, dateMax);
            }

            if (!norep) {
                // on alimente les données de chaque formulaire
                for (int i=0; listSfId != null && i< listSfId.size(); i++) {
                    SubmitForm sf = (SubmitForm)listSfId.get(i);
                    if (list==null) list = new ValueBeanList();
                    list.add(sfa.selectBySId(sf.getS_id()));
                }
            } else {
                // on alimente juste les données 'init on load'les données de chaque formulaire
                for (int i=0; listSfId != null && i< listSfId.size(); i++) {
                    SubmitForm sf = (SubmitForm)listSfId.get(i);
                    if (list==null) list = new ValueBeanList();
                    sfa.getInitVals(sf);
                    list.add(sf);
                }
            }



            if (sid > 0) {
                list = new ValueBeanList();
                SubmitForm s = sfa.selectBySId(sid);
                list.add(s);
                fid = s.getF_id();
            }


            InitServlet.traceLog.info("# Export XMLRPC : lancement de sendForms / paramètres : "
                    + " fid=" +  fid  + " lockedOnly=" +  lockedOnly + " dateMin=" +  String.valueOf(dateMin)
                    + " dateMax=" + String.valueOf(dateMax) + " sid=" +  sid  + " parentSid=" +  parentSid
                    + " norep=" +  norep + " pidrequired=" + pidRequired  + " valRequired=" + String.valueOf(valRequired) );

            XmlBuilder xmlbuilder = new XmlBuilder();
            xmlbuilder.sendForms(f, list, out, norep);

            InitServlet.traceLog.info("# Export XMLRPC : fin export sendForms / nb forms : " + (list!=null?list.size():"0"));



        } catch (Exception e) {
            InitServlet.traceLog.error("Erreur lors de l'exportation" + e);
            result = -1;
        }

        return result;
    }






}