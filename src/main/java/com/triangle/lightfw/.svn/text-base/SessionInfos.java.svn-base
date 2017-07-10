package com.triangle.lightfw;

/**
 * 
 * Classe gérant les informations de la session de l'application FOL.
 * 
 * @author pascal
 * @version 27 mai 04
 */
public class SessionInfos extends ValueBean
{
  /**
     * 
     */
    private static final long serialVersionUID = 1L;
/** Servlet ou JSP suivantes */
  private String  destination = null;
  /** Message à l'utilisateur */
  private String message = "&nbsp;";
  /** Action à enchîner */
  private int actionToDo = 0;

  /** Vrai si dans JAHIA */
  private boolean inJahia = false;
  /** Login (Profil JAHIA) */
  private String login = "";
  /** Vrai si a le rôle "REGION" */
  private boolean inRoleRegion = false;
  
  public static int ROLE_PUBLIC = 0;
  public static int ROLE_CONSULTATION = 1;
  public static int ROLE_GESTION = 2;
  public static int ROLE_ADMIN = 3;
  
  private int role = 0;

  public String getDestination()
  {
    return destination;
  }

  public void setDestination(String string)
  {
    destination = string;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String string)
  {
    message = string;
  }

  public int getActionToDo()
  {
    return actionToDo;
  }

  public void setActionToDo(int i)
  {
    actionToDo = i;
  }

  public String getLogin()
  {
    return login;
  }

  public void setLogin(String string)
  {
    login = string;
  }

  public boolean isInJahia()
  {
    return inJahia;
  }

  public void setInJahia(boolean b)
  {
    inJahia = b;
  }

  public boolean isInRoleRegion()
  {
    return inRoleRegion;
  }
  
  public void setInRoleRegion(boolean b)
  {
    inRoleRegion = b;
  }

/**
 * @return Returns the role.
 */
public int getRole() {
    return role;
}

/**
 * @param role The role to set.
 */
public void setRole(int role) {
    this.role = role;
}

}