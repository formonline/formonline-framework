package formOnLine;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import formOnLine.actions.PropositionAction;
import formOnLine.actions.QuestionAction;
import formOnLine.actions.ReponseAction;
import formOnLine.actions.SubmitFormAction;
import formOnLine.msBeans.Proposition;
import formOnLine.msBeans.Question;
import formOnLine.msBeans.Reponse;
import formOnLine.msBeans.SubmitForm;

import com.triangle.lightfw.BasicType;
import com.triangle.lightfw.ValueBean;
import com.triangle.lightfw.ValueBeanList;

/**
 * @version   1.0
 * @author
 */
public class ServListRep extends HttpServlet implements Servlet {
  
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static String SEARCHQ1OPTIONS ="searchQ1options"; 
    private static String SEARCHQ2OPTIONS ="searchQ2options"; 
    private static String FINDQ1OPTIONS ="findQ1options"; 
    private static String FINDQ2OPTIONS ="findQ2options"; 
    private static String RLIST ="rlist"; 
    
    private static String ALERT = "alert";
    
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        // Auto-generated method stub
        doPost(req,res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
      
    HttpSession session = req.getSession(true);
    SubmitFormAction sfa = new SubmitFormAction();
    String message=""; 
    
    ValueBeanList searchQ1options = (ValueBeanList)session.getAttribute(SEARCHQ1OPTIONS);
    ValueBeanList searchQ2options = (ValueBeanList)session.getAttribute(SEARCHQ2OPTIONS);
    
    Question q1=null,q2=null;
    
    int sid =-1;
    String datDeb = null;
    String datFin = null;
    int afficheLocked = -1; 
    boolean searchParents = false;
    
    // reinit valueBean de la session
    //session.removeAttribute(RLIST);
    
    QuestionAction qa = new QuestionAction(); 
  
  try {
      
//    init alertes
      if (req.getParameter("RechRep")==null) {
        PropositionAction pa = new PropositionAction();
        
        
        ValueBeanList sfAlertList = null;
        ValueBeanList propAlertList = pa.selectAllByAlert();
        for (int i=0; propAlertList!=null && i< propAlertList.size(); i++) {
            Proposition p = (Proposition)propAlertList.get(i);
            Question q = qa.selectById(p.getQid());
            if (sfAlertList == null) sfAlertList=new ValueBeanList();
            sfAlertList.addAll( 
                    sfa.selectByAlert(
                            p.getTexte() + " : ", 
                            p.getId(),
                            p.getAlert(),
                            q.getType(),
                            null,null,false));
        }
        
        
        if (sfAlertList !=null)  req.setAttribute(ALERT,sfAlertList );
      } 
    
    // init listbox questions si besoin
    if (searchQ1options==null) {
      
      searchQ1options = qa.selectSearchQuest();
      if (searchQ1options == null) searchQ1options = new ValueBeanList();
      searchQ2options = new ValueBeanList();
      
      // ajout ligne vide dans les select
      Question qvide1 = new Question();
      qvide1.setTexte(" ");
      qvide1.setId(-1);
      qvide1.setType(" ");
      
      Question qvide2 = new Question();
      qvide2.setTexte(" ");
      qvide2.setId(-1);
      qvide2.setType(" ");
      
      searchQ1options.add(0,qvide1);
      searchQ2options.add(0,qvide2);
      
      
      //searchQ1options.setSelectedIndex(searchQ1options.size()-1);
      //searchQ2options.setSelectedIndex(searchQ2options.size()-1);
            
      searchQ1options.setSelectedIndex(0);
      searchQ2options.setSelectedIndex(0);
      
      session.removeAttribute(SEARCHQ1OPTIONS);
      session.removeAttribute(SEARCHQ2OPTIONS);
      
      session.setAttribute(SEARCHQ1OPTIONS,searchQ1options);
      session.setAttribute(SEARCHQ2OPTIONS,searchQ2options);
      
      return;
    }
    

    
    
    //récup questions
    String searchQ1="", searchQ2="";
    String findQ1="", findQ2="" ; 
    
    if (!(Controls.isBlank(req.getParameter("searchQ1")) &&
            Controls.isBlank(req.getParameter("searchQ2"))) &&
            req.getParameter("RechRep")==null) {
      
      searchQ1 = req.getParameter("searchQ1");
      searchQ1options.unSelectAll();
      searchQ1options.setSelectedIndex(Integer.parseInt(searchQ1));
      
      q1 = (Question)searchQ1options.get(searchQ1options.getSelectedIndex());
      session.setAttribute(SEARCHQ1OPTIONS,searchQ1options);

      
      // chaînage des questions du même formulaire
      searchQ2 = req.getParameter("searchQ2");
      
      if (q1 !=null  && Integer.parseInt(searchQ2)<=0) {
          searchQ2options = qa.selectSearchQuestByQid(q1.getId());
          if (searchQ2options == null) searchQ2options = new ValueBeanList();
          
          // ajout d'un ligne vide
          searchQ2options.add(0,new Question(" ",-1," "));
      }
      
      searchQ2options.unSelectAll();
      searchQ2options.setSelectedIndex(Integer.parseInt(searchQ2));
      
      q2 = (Question)searchQ2options.get(searchQ2options.getSelectedIndex());
      session.setAttribute(SEARCHQ2OPTIONS,searchQ2options);
    
      session.removeAttribute(FINDQ1OPTIONS);
      session.removeAttribute(FINDQ2OPTIONS);
      
      //alimentation des bean pour listbox esclaves
      PropositionAction pa = new PropositionAction(); 
      if (q1!=null && q1.getId()>=0) {
          ValueBeanList listOptionsQ1 = pa.selectAllByQuestId(q1.getId());
          
          // si la question est du type "liste forms", on substitue la proposition par la liste de forms  
          if (q1.getType().equals(Question.LIST_FORM) && listOptionsQ1 !=null) {
              Proposition prop = (Proposition)listOptionsQ1.get(0);
              
              int pid = prop.getNum(); // les formulaires souhaités sont retrouvés via le p_num = pid du champ de sélection
              if (pid>0) {
                  ValueBeanList listRep = new ReponseAction().selectAllByPId(pid,true);
                  // réinit pour remplacer par les valeurs trouvées dans la base
                  listOptionsQ1 = new ValueBeanList();
                  
                  if (listRep !=null) {
                      for (ValueBean rep : listRep) {
                          Proposition p = new Proposition( prop.getId(), ((Reponse)rep).getSVal(), 0, 0);
                          p.setQid(prop.getQid());
                          p.setVal(((Reponse)rep).getS_id());
                          listOptionsQ1.add(p);
                      }
                  }
              }
          }
          
          session.setAttribute(FINDQ1OPTIONS,listOptionsQ1);
      }
      
      if (q2!=null && q2.getId()>=0){
          ValueBeanList listOptionsQ2 = pa.selectAllByQuestId(q2.getId());
          
          // si la question est du type "liste forms", on substitue la proposition par la liste de forms  
          if (q2.getType().equals(Question.LIST_FORM) && listOptionsQ2 !=null) {
              Proposition prop = (Proposition)listOptionsQ2.get(0);
              
              int pid = prop.getNum(); // les formulaires souhaités sont retrouvés via le p_num = pid du champ de sélection
              if (pid>0) {
                  ValueBeanList listRep = new ReponseAction().selectAllByPId(pid,true);
                  // réinit pour remplacer par les valeurs trouvées dans la base
                  listOptionsQ2 = new ValueBeanList();
                  
                  if (listRep !=null) {
                      for (ValueBean rep : listRep) {
                          Proposition p = new Proposition( prop.getId(), ((Reponse)rep).getSVal(), 0, 0);
                          p.setQid(prop.getQid());
                          p.setVal(((Reponse)rep).getS_id());
                          listOptionsQ2.add(p);
                      }
                  }
              }
          }
          
          session.setAttribute(FINDQ2OPTIONS,listOptionsQ2);
      }
      
      return ; //pas de traitement : attente des valeurs FINDxx
    }
    
    
      
    
    
      
    /////////////////////////////
    //tests
    
    // test numéro de dossier saisi
    String sNumdos = req.getParameter("numdos");
    if (sNumdos != null) sNumdos = sNumdos.trim();
    
    message=Controls.checkInt(sNumdos);
    if (message!=null) {
      session.setAttribute(ServControl.MESSAGE,message);
      return ;
    }
    if (!Controls.isBlank(sNumdos)) sid = Integer.parseInt(sNumdos);
    
    //  test case à cocher "rechercher dossiers parents"
    if (req.getParameter("chkparents")!=null) searchParents = true;
    
    // test dates saisies
    message=Controls.checkDates(req.getParameter("date_debut"),req.getParameter("date_fin"));
    if (message!=null) {
      session.setAttribute(ServControl.MESSAGE,message);
      return ;
    }
    
    
    if (!Controls.isBlank(req.getParameter("date_debut"))) datDeb = req.getParameter("date_debut");
    if (!Controls.isBlank(req.getParameter("date_fin"))) datFin = req.getParameter("date_fin");
    
    // test case à cocher "afficher uniquement les verrouillés"
    if (req.getParameter("chklock")!=null) {
        try {
            afficheLocked = Integer.parseInt(req.getParameter("chklock"));
        } catch (Exception e) {}
    }
    
    
    //////////////////////////////
    // début traitement
    findQ1 = req.getParameter("findQ1"); 
    findQ2 = req.getParameter("findQ2");
    
    datDeb = BasicType.parseDateIsoFromLocal(datDeb,null);
    datFin = BasicType.parseDateIsoFromLocal(datFin,null);
    
    ValueBeanList v=null;
    if (!(Controls.isBlank(findQ1)&&Controls.isBlank(findQ2))) {
        int idP1=-1,idP2=-1;
        int idVal1=0,idVal2=0;
        String valP1="", valP2="";
      
      ValueBeanList findQ1options = (ValueBeanList)session.getAttribute("findQ1options");
      ValueBeanList findQ2options = (ValueBeanList)session.getAttribute("findQ2options");
      
      q1 = (Question)searchQ1options.get(searchQ1options.getSelectedIndex());
      q2 = (Question)searchQ2options.get(searchQ2options.getSelectedIndex());
      
      if (findQ1options!=null && findQ1options.size()>0) {
        if (q1.getType().equals(Question.CHOIX_EXCLUSIF)|| q1.getType().equals(Question.CHOIX_MULTIPLE) 
                || q1.getType().equals(Question.LIST_FORM)) {
            Proposition prop = (Proposition)findQ1options.get(Integer.parseInt(findQ1));
            idP1=prop.getId();
            idVal1 = prop.getVal();
            valP1="1";
            
            findQ1options.unSelectAll();
            findQ1options.setSelectedIndex(Integer.parseInt(findQ1));
            session.setAttribute("findQ1options",findQ1options);
        
        } else {
          Proposition prop = (Proposition)findQ1options.get(0);
          idP1=prop.getId();
          valP1=findQ1;
        }
      }
      
      if (findQ2options!=null && findQ2options.size()>0) {
        if ((q2.getType().equals(Question.CHOIX_EXCLUSIF) 
                || q2.getType().equals(Question.CHOIX_MULTIPLE) 
                || q2.getType().equals(Question.LIST_FORM) ) 
            &&
                findQ2options!=null) {
          Proposition prop = (Proposition)findQ2options.get(Integer.parseInt(findQ2));
          idP2=prop.getId();
          idVal2 = prop.getVal();
          valP2="1";
          
          findQ2options.unSelectAll();
          findQ2options.setSelectedIndex(Integer.parseInt(findQ2));
          session.setAttribute("findQ2options",findQ2options);
          
        } else {
          Proposition prop = (Proposition)findQ2options.get(0);
          idP2=prop.getId();
          valP2=findQ2;
        }
      }
      
      //recherche dans la base (sauf si aucun critere de recherche saisi)
      if (!Controls.isBlank(valP1)
              || !Controls.isBlank(valP2)
              || !Controls.isBlank(datDeb)
              || !Controls.isBlank(datFin) ) 
          
          
          
          v = sfa.selectBySearchInfos(idP1,idP2,idVal1, idVal2, 
                  valP1,valP2,
              q1.getType(),q2.getType(),
              datDeb,datFin,
              -1,
              afficheLocked, null);
      
    } else {
      // recherche juste sur l'ID
        if (req.getParameter("RechRep")!=null && 
                (sid >-1 || !Controls.isBlank(datDeb) || !Controls.isBlank(datFin)))
            v = sfa.selectByFormIdSidDate(sid,datDeb,datFin,afficheLocked);
        
        if (searchParents && v != null && v.size() ==1) {
            SubmitForm sf = (SubmitForm)v.get(0);
            ValueBeanList v2 = sfa.selectAllChildren(sf.getS_id());
            if (v2 != null) v.addAll(v2);
            SubmitForm sfp = sfa.selectBySId(sf.getS_id_parent());
            if (sfp != null) v.add(0,sfp);
        }
    }
    
    // chargement valeurs des champs 'initval' (seulement si <100)
    if (v!=null && v.size()>0 && v.size()<100) sfa.setInitVals(v);
    
    //session.removeAttribute(RLIST);
    if (v!=null)  req.setAttribute(RLIST,v);
    
  
  } catch (SQLException e ) {
    message = e.getMessage(); // erreur SQL
  }
    session.removeAttribute(ServControl.MESSAGE);
    if (message!=null) session.setAttribute(ServControl.MESSAGE,message);
    
    return ;
  }


}
