package com.triangle.lightfw;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * 
 * Classe de base à l'ensemble des servlets de l'application. AbstractServlet 
 * déclare les méthodes abstraites génériques des servlets de l'application.
 * 
 * @see AbstractServletPost AbstractServletAvant
 * 
 * @version 16 juil. 02
 * 
 * @author Benoît
 * 
 */
public abstract class AbstractServlet extends HttpServlet
{
  static protected Logger traceLog = Logger.getLogger("FOLE");

  private final static String MESSAGE_SESSION_INTERROMPUE =
    "(Nouvelle session)";

  public final static int NO_ACTION = 0;
  public final static String ID_REQUEST_MESSAGE = "message";
  public final static String ID_SESSION_INFOS = "sessionInfos";

  /**
   * doGet() est redéfinie en final pour exécuter doPost(), celà évite
   * les mauvaises pratiques de développement.
   */
  public final void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException
  {
    doPost(req, res);
  }

  /**
   * doPost() est redéfinie en final, celà évite les mauvaises pratiques de
   * développement. Appelle doAll() qui est à redéfinir dans les classes
   * filles: AbstractServletAvant et AbstractServletPost.
   */
  public final void doPost(HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException
  {

      
      try
    {
      SessionInfos sessionInfos = getSessionInfos(req);
      
      res.setContentType("text/html; charset=UTF-8");
      res.setCharacterEncoding("UTF-8");

      if ((sessionInfos != null)
        || (getServletConfig().getServletName().equals("IndexAvant")))
      {
        if (sessionInfos != null)
        {
          sessionInfos.setActionToDo(NO_ACTION);
          sessionInfos.setDestination(null);
          sessionInfos.setMessage("&nbsp;");
        }
        ValueBean b = doAll(req, res, sessionInfos);
        sessionInfos = getSessionInfos(req);
        if (b != null)
        {
          traceLog.debug(
            "["
              + getClass().getName()
              + "]: Destination='"
              + getDestination(req)
              + "'");
          if (req.getAttribute(ID_REQUEST_MESSAGE) == null)
          {
            req.setAttribute(ID_REQUEST_MESSAGE, sessionInfos.getMessage());
          }
          if (getDestination(req) != null)
          {
            doForward(req, res);
          }
          else
          {
            // Envoi direct de fichier non HTML
            traceLog.info("Pas de destination positionnée !");
            //req.getRequestDispatcher("index").forward(req, res);
          }
        }
        else
        {
          traceLog.error("[" + getClass().getName() + "]: Bean=null");
          req.getRequestDispatcher("index").forward(req, res);
        }
      }
      else
      {
        String message = MESSAGE_SESSION_INTERROMPUE;
        req.setAttribute(ID_REQUEST_MESSAGE, message);

        throw (new ServletException("Pas de session ouverte !"));
      }
    }
    catch (Exception e)
    {
      traceLog.fatal(e.getMessage(), e);
      req.getRequestDispatcher("index").forward(req, res);
      //Normalement on ne doit pas partir en boucle infinie, sinon erreur.html !
      //req.getRequestDispatcher("/jsp/erreur.html").forward(req, res);
    }
    
    req.getSession().invalidate();
  }

  /**
   * Méthode abstraite à redéfinir dans les classes filles pour exécuter
   * le traitement.
   */
  public final ValueBean doAll(
    HttpServletRequest req,
    HttpServletResponse res,
    SessionInfos sessionInfos)
    throws IOException, ServletException, SQLException
  {
    ValueBean b = doInit(req, sessionInfos);
    sessionInfos = getSessionInfos(req);
    b = doAction(req, res, sessionInfos, b);

    return b;
  }

  /**
   * Méthode abstraite à redéfinir dans les classes filles pour initialiser
   * le traitement. Normalement récupère les données d'URL dans un ValueBean et
   * positionne l'action à réaliser.
   */
  public abstract ValueBean doInit(
    HttpServletRequest req,
    SessionInfos sessionInfos)
    throws IOException, ServletException;

  /**
   * Méthode abstraite à redéfinir dans les classes filles pour exécuter
   * le traitement. Normalement appelle une JSP en fin de traitementsi on est
   * dans une ServletAvant, sinon appelle une ServletAvant si on est dans
   * une ServletPost.
   */
  public abstract ValueBean doAction(
    HttpServletRequest req,
    HttpServletResponse res,
    SessionInfos sessionInfos,
    ValueBean b)
    throws IOException, ServletException, SQLException;

  /**
   * Méthode vérifiant la session (et le login effectif) de l'utilisateur.
   * Retourne le Bean de valeurs correspondant, ou null s'il n'est pas dans
   * la session.
   * @param req
   * @return SessionInfos
   */
  protected SessionInfos getSessionInfos(HttpServletRequest req)
  
  {
    HttpSession session = req.getSession();

    return (SessionInfos)session.getAttribute(ID_SESSION_INFOS);
  }

  /**
   * Positionne l'URL de la JSP suivante dans la session, en fonction du
   * répertoire des JSPs actif dans le bean de session.
   * @param req Request.
   * @param dest Nom de la JSP (xxx.jsp).
   * @throws IOException
   * @throws ServletException
   */
  protected void setJspDestination(HttpServletRequest req, String dest)
    throws IOException, ServletException
  {
    SessionInfos sessionInfos = getSessionInfos(req);

    if (sessionInfos != null)
    {
      sessionInfos.setDestination("/jsp/" + dest);
    }
  }

  /**
   * Positionne l'URL de la servlet suivante dans un ValueBean.
   * @param req Request.
   * @param dest Nom de la servlet.
   */
  protected void setServletDestination(HttpServletRequest req, String dest)
  {
    SessionInfos sessionInfos = getSessionInfos(req);

    if (sessionInfos != null)
    {
      sessionInfos.setDestination("/" + dest);
    }
  }

  /**
   * Retourne le chemin complet de l'objet suivant de l'application
   * (servlet ou JSP).
   * @param req Request.
   */
  protected String getDestination(HttpServletRequest req)
  {
    String destination = null;
    SessionInfos sessionInfos = getSessionInfos(req);

    if (sessionInfos != null)
    {
      destination = sessionInfos.getDestination();
    }

    return destination;
  }

  /**
   * Transmet l'exécution du traitement à une servlet ou une JSP, selon le
   * résultat des traitements précédents.
   * @param req Request en cours
   * @param res Response en cours
   */
  protected void doForward(HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException
  {
    req.getRequestDispatcher(getDestination(req)).forward(req, res);
  }

}