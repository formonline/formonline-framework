package com.triangle.lightfw;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * Classe des listes de ValueBean, permet la gestion des sélections
 * d'éléments. Les ValueBeanList sont elles-même des ValueBean
 * ce qui permet de les utiliser dans le squelette des applications.
 * 
 * @author pascal
 * 
 * @version 10 déc. 03
 *
 */
public class ValueBeanList
extends ValueBean
implements Serializable, List<ValueBean>
{
    

    private static final long serialVersionUID = 1L;
    private LinkedList<ValueBean> internalList = new LinkedList<ValueBean>();
    
    /**
     * Retourne une ValueBeanList chargée des éléments du vecteur passé en
     * paramètre, retourne null si v est null.
     * @param v
     */
    @SuppressWarnings("rawtypes")
    public static ValueBeanList getFromList(LinkedList v)
    {
        ValueBeanList result = null;
        
        if (v != null)
        {
            result = new ValueBeanList();
            
            for (int i = 0; i < v.size(); i++)
            {
                result.add((ValueBean)v.get(i));
            }
        }
        
        return result;
    }
    
    /**
     * trie la liste par le titre
     */
    public void sort()
    {
        this.sort(null);
    }
    
    /**
     * trie la liste par le titre
     */
    public void sort(Comparator<ValueBean> cvb)
    {
        java.util.Collections.sort(this, cvb);
    }
    
    
    /**
     * Renvoie un élément de la liste.
     */
    public ValueBean get(int index)
    {
        return internalList.get(index);
    }
    
    /**
     * Affecte la liste.
     */
    public void setList(LinkedList<ValueBean> list)
    {
        this.internalList = list;
    }
    
    /**
     * indique si tout les éléments sont à cocher ou non.
     */
    public boolean areAllElementsSelected()
    {
        boolean allElementsSelected = true;
        int i = 0;
        
        while (allElementsSelected && (i < internalList.size()))
        {
            allElementsSelected =
                allElementsSelected
                && ((ValueBean)internalList.get(i)).isSelected();
            i++;
        }
        
        return allElementsSelected;
    }
    
    public boolean getAllElementsSelected()
    {
        return areAllElementsSelected();
    }
    
    @SuppressWarnings("rawtypes")
    public LinkedList getList()
    {
        return internalList;
    }
    
    public int size()
    {
        return internalList.size();
    }
    
    /**
     * @return
     */
    public int getSelectedIndex()
    {
        int selectedIndex = 0;
        boolean finded = false;
        
        for (int i = 0;(finded == false) && (i < internalList.size()); i++)
        {
            if (((ValueBean)internalList.get(i)).isSelected())
            {
                selectedIndex = i;
                finded = true;
            }
        }
        
        return selectedIndex;
    }
    
    /**
     * @param i
     */
    public void setSelectedIndex(int i)
    {
        ((ValueBean)internalList.get(i)).setSelected(true);
    }
    
    /**
     * 
     * @return true si la liste a au moins 1 élément sélectionné
     * 
     */
    public boolean hasSelected()
    {
        boolean selectedFound = false;
        int i = 0;
        
        while ((!selectedFound) && (i < internalList.size()))
        {
            selectedFound = ((ValueBean)internalList.get(i)).isSelected();
            i++;
        }
        
        return selectedFound;
    }
    
    /**
     * Met 1 élément à selected = true, les autres à false
     * 
     * @param i
     */
    public void setSingleSelected(int i)
    {
        if (hasSelected())
        {
            for (int j = 0; j < internalList.size(); j++)
            {
                ValueBean b = (ValueBean)internalList.get(j);
                
                if (i == j)
                {
                    b.setSelected(true);
                }
                else
                {
                    b.setSelected(false);
                }
            }
        }
        else
        {
            ValueBean b = (ValueBean)internalList.get(i);
            b.setSelected(true);
        }
    }
    
    /**
     * Passe tous les éléments à selected = false;
     */
    public void unSelectAll()
    {
        for (int i = 0; i < size(); i++)
        {
            ValueBean b = (ValueBean)get(i);
            b.setSelected(false);
        }
    }
    
    // Méthodes de List
    public boolean isEmpty()
    {
        return internalList.isEmpty();
    }
    
    public boolean contains(Object arg0)
    {
        return internalList.contains(arg0);
    }
    
    public Iterator<ValueBean> iterator()
    {
        return internalList.iterator();
    }
    
    public Object[] toArray()
    {
        return internalList.toArray();
    }
    
    @SuppressWarnings("unchecked")
    public Object[] toArray(Object[] arg0) {
        return internalList.toArray(arg0);
    }
    
    public boolean add(ValueBean arg0)
    {
        return internalList.add(arg0);
    }
    


    

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean addAll(Collection arg0)
    {
        if (arg0!=null) {
            return internalList.addAll((Collection<? extends ValueBean>)arg0);
        } else {
            return false;
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean addAll(int arg0, Collection arg1)
    {
        return internalList.addAll(arg0, (Collection<? extends ValueBean>)arg1);
    }
    public boolean remove(Object arg0)
    {
        return internalList.remove(arg0);
    }
    
    @SuppressWarnings("rawtypes")
    public boolean containsAll(Collection arg0)
    {
        return containsAll(arg0);
    }
    @SuppressWarnings("rawtypes")
    public boolean removeAll(Collection arg0)
    {
        return internalList.removeAll(arg0);
    }
    
    @SuppressWarnings("rawtypes")
    public boolean retainAll(Collection arg0)
    {
        return internalList.retainAll(arg0);
    }
    
    public void clear()
    {
        internalList.clear();
    }
    
    public ValueBean set(int arg0, ValueBean arg1)
    {
        return internalList.set(arg0, arg1);
    }
    
    public void add(int arg0, ValueBean arg1)
    {
        internalList.add(arg0, arg1);
    }
    
    public ValueBean remove(int arg0)
    {
        return internalList.remove(arg0);
    }
    
    public int indexOf(Object arg0)
    {
        return internalList.indexOf(arg0);
    }
    
    public int lastIndexOf(Object arg0)
    {
        return internalList.lastIndexOf(arg0);
    }
    
    public ListIterator<ValueBean> listIterator()
    {
        return internalList.listIterator();
    }
    
    public ListIterator<ValueBean> listIterator(int arg0)
    {
        return internalList.listIterator(arg0);
    }
    
    public List<ValueBean> subList(int arg0, int arg1)
    {
        return internalList.subList(arg1, arg0);
    }
}