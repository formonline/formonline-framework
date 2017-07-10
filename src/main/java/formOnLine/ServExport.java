package formOnLine;


import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import formOnLine.actions.TemplateAction;
import formOnLine.msBeans.Template;
import formOnLine.msBeans.UserData;

import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.SessionInfos;

public class ServExport extends HttpServlet implements Servlet
{


  private static final long serialVersionUID = 1L;
  private static final int ERREUR = 0;
  private static final int PDF    = 3; // test PDF
  private static final int RTF    = 4; // generate RTF
  private static final int mRTF   = 5; // modèle template RTF
  private static final int XML  = 6; // exports  XML
  private static final int CSV  = 8; // exports type excel multilignes
  private static final int HTML  = 10; // generate HTML
  private static final int MAIL  = 11; // generate text for MAIL
  private static final int MTO  = 12; // generate text for MAILTO
  private static final int MING  = 13; // mass mailing
  private static final int FILES  = 14; // generation de fichiers dans un répertoire
  private static final int TXT  = 15; // export texte avec data en colonnes 
  
   

  /**
   * 
   *  
   */
  private int init(String sAction)
  {
    int i = ERREUR;
    if (sAction != null)
    {

      if (sAction.equals("PDF"))
        i = PDF;
      if (sAction.equals("RTF"))
        i = RTF;
      if (sAction.equals("mRTF"))
        i = mRTF;
      if (sAction.equals("XML"))
          i = XML;
      if (sAction.equals("CSV"))
          i = CSV;
      if (sAction.equals("HTML"))
          i = HTML;
      if (sAction.equals("MAIL"))
          i = MAIL;
      if (sAction.equals("MTO"))
          i = MTO;
      if (sAction.equals("MING"))
          i = MING;
      if (sAction.equals("FILES"))
          i = FILES;
      if (sAction.equals("TXT"))
              i = TXT;

    }
    return i;
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp)
  throws ServletException, IOException       {
      doPost(req,resp);
      }

  
  /**
   * @see javax.servlet.http.HttpServlet#void
   * (javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException
  {

    HttpSession session = req.getSession(true);
    
    int templateId = -1;
    int action = -1;
    int sid = -1;
    int fid = -1;
    String message = null;
    String datDeb = null;
    String datFin = null;
    int lockedOnly = 1;
    boolean directInWebBrowser = false;

    int sqlFilterId = -1;
    String sqlValId = null;

    // init encodage de sortie
    resp.setCharacterEncoding("UTF-8");
    

    if (req.getParameter("templateId") != null)
        templateId = Integer.parseInt(req.getParameter("templateId"));
    
    lockedOnly =  ((req.getParameter("lockedOnly") != null)?1:-1);

    
    String sAction = req.getParameter("selectExport");
    if (sAction == null) sAction = req.getParameter("templateType");
    if (sAction == null) {
        try {
            sAction = (new TemplateAction().selectOne(templateId).getType());
        } catch (Exception e) {}
    }
    
    if (sAction == null) sAction = "XML";
    action = init(sAction); 
    
    
    if (!Controls.isBlank(req.getParameter("sid")))
        sid = Integer.parseInt(req.getParameter("sid"));
    
    if (!Controls.isBlank(req.getParameter("f_id")))
        fid = Integer.parseInt(req.getParameter("f_id"));
    
    
    if (req.getParameter("directInWebBrowser")!=null || req.getParameter("direct")!=null)
        directInWebBrowser = true;
    
    @SuppressWarnings("unchecked")
    HashMap<String,String> params = (HashMap<String,String>)session.getAttribute("params");
    
    // ajout paramètres URL
    @SuppressWarnings("unchecked")
    Enumeration<String> parameterNames = req.getParameterNames();
    while (parameterNames.hasMoreElements()) {
        String paramName = parameterNames.nextElement();
        params.put(paramName, req.getParameter(paramName));
    }
    
    //  test dates saisies
    if (req.getParameter("date_debut") !=null && req.getParameter("date_fin")!=null)
    message=Controls.checkDates(req.getParameter("date_debut"),req.getParameter("date_fin"));
    if (message!=null) {
      session.setAttribute(ServControl.MESSAGE,message);
      return ;
    }
    
    if (!Controls.isBlank(req.getParameter("date_debut"))) 
        datDeb = req.getParameter("date_debut");
    if (!Controls.isBlank(req.getParameter("date_fin"))) 
        datFin = req.getParameter("date_fin");
    
    datDeb = BasicType.parseDateIsoFromLocal(datDeb,null);
    datFin = BasicType.parseDateIsoFromLocal(datFin,null);
    
    if (!Controls.isBlank(req.getParameter("sqlFilterId"))) {
        try {
            sqlFilterId = Integer.parseInt(req.getParameter("sqlFilterId"));
        } catch (NumberFormatException e) {
            sqlFilterId = -1;
        }
    }
        
    if (!Controls.isBlank(req.getParameter("sqlValId"))) 
        sqlValId = req.getParameter("sqlValId");

    
    /***********************
     * CONTROLE DES DROITS *
     ***********************/
    
    boolean access = false;
    UserData userData = (UserData)session.getAttribute(ServControl.USERDATA);
    
    /* ADMIN */
    if (userData != null && userData.getRole() >= SessionInfos.ROLE_CONSULTATION )
        access = true;
        
    /* CONTROLE DES DROITS DE CREATION / MODIF
     *  - si le role PUBLIC + connecté : accès aux submitform fils ou père
     */   
  
    if    ( sid>0 
            && userData != null 
            && userData.isConnected() 
            && userData.isItsOwnData(sid,true, true)  )    access = true;
       
    // sortie si droits insuffisants
    if (!access) {
        
        PrintStream fichier = new PrintStream(resp.getOutputStream(), true, "UTF-8");
        fichier.println("<html><body>");
        fichier.println("Droits insuffisants");
        fichier.println("</body></html>");

        resp.getOutputStream().flush();
        
        return ;
    }
    
    String charset = InitServlet.getExportCharset();
    if (Controls.isBlank(charset)) charset ="UTF-8";
    
    // init du flux de sortie fichier
    if ( directInWebBrowser) {
       
        
        resp.setContentType("text/html;charset="+charset);
    } else {
    
    
    if (action == PDF)
    {
      //			--> PDF
      resp.setContentType("application/pdf;charset="+charset);
      resp.addHeader("Content-Disposition", "attachment;filename=export.pdf;");
    }
    else if (action == RTF || action == mRTF || action == HTML)
    {
      // RTF ou HTML > doc
      resp.setContentType("application/doc;charset="+charset);
      resp.addHeader("Content-Disposition", "attachment;filename=export.doc;");
    }
    else if (action == CSV )
    {
        //--> CSV
        resp.setContentType("application/ms-excel;charset="+charset); //"text/plain");
        resp.addHeader("Content-Disposition", "attachment;filename=export.csv;");
    }
    else if (action == TXT )
    {
        //--> txte
        resp.setContentType("application/text/plain;charset="+charset); 
        resp.addHeader("Content-Disposition", "attachment;filename=export.txt;");
    }
    else if (action == XML)
    {
      //--> XML
      resp.setContentType("application/xml;charset="+charset);
      resp.addHeader("Content-Disposition", "attachment;filename=export.xml;");
    }
}

try {
    // Traitement et Envoi du flux fichier 
    switch (action)
    {

      case XML:
        FormatExport.exportXML(fid, 1, datDeb,datFin,resp.getOutputStream(),sid, false, userData);
        break;
   

      case PDF:
          FormatExport.exportPdfFromTemplate(templateId, sid, resp
                  .getOutputStream(), params, userData); 		

        break;

      case RTF:
      case mRTF:
      case HTML:
      case MAIL:
      case MTO:
          FormatExport.exportDocFromTemplate(templateId, sid, resp
              .getOutputStream(), params, userData); 
          break;
          
      case MING:
          FormatExport.sendMailingFromTemplate(templateId, resp
                  .getOutputStream(), datDeb, datFin, lockedOnly, sqlFilterId, sqlValId, userData); 
      break;

      case FILES:
          FormatExport.generateFilesFromTemplate(templateId, resp
                  .getOutputStream(), datDeb, datFin, lockedOnly, sqlFilterId, sqlValId, params, userData); 
      break;

      case CSV:
          FormatExport.exportCsvFromTemplate(templateId, resp
              .getOutputStream(), datDeb, datFin, lockedOnly, sqlFilterId, sqlValId, params, userData); 
          break;

      case TXT:
          FormatExport.exportTxtFromTemplate(templateId, resp
              .getOutputStream(), datDeb, datFin, lockedOnly, sqlFilterId, sqlValId, userData); 
          break;

      
      case ERREUR:
        break;
    }
    
} catch (Exception e) {
    //  envoi de l'erreur
    
    resp.reset();
    resp.setContentType("text/html;charset=UTF-8");
    
    PrintStream fichier = new PrintStream(resp.getOutputStream());
    fichier.println("<html><body>");
    fichier.println(e.getMessage());
    resp.getOutputStream().flush();
    
}

  }
}