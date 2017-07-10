package com.triangle.lightfw;

import java.io.Serializable;

/**
 *
 * ValueBean est la classe dans laquelle on trouve les propriétés
 * liées au traitement des données. Tous les Beans de données héritent de cette classe.
 *  
 * @version 10 juil. 02
 * 
 * @author Benoît
 * 
 */
public abstract class ValueBean implements Serializable, Comparable
{
  /** Indique si la donnée est sélectionnée (utile dans les listes) */
  private boolean selected = false;
  private String title = null;

  public boolean isSelected()
  {
    return selected;
  }

  public boolean getSelected()
  {
    return selected;
  }

  public void setSelected(boolean b)
  {
    selected = b;
  }

/**
 * @return Returns the title.
 */
public String getTitle() {
    return title;
}

/**
 * @param title The title to set.
 */
public void setTitle(String title) {
    this.title = title;
}

/**
 * Méthode pour la comparaison (ordre alphabêtique) pour tri par Titre du valuebean
 * @param vb
 * @return
 */
public int compareTo(Object vb) {
    return this.title.compareTo(((ValueBean)vb).getTitle());
}

}