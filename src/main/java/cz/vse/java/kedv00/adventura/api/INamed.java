package cz.vse.java.kedv00.adventura.api;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/api/INamed.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/

import java.util.Collection;



/*******************************************************************************
 * Interfejs {@code INamed} je společným rodičem všech interfejsů
 * implementovaných třídami s pojmenovanými instancemi.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public interface INamed
{
//\CM== CLASS (STATIC) METHODS =================================================

    /***************************************************************************
     * Vyhledá v kolekci objekt se zadaným názvem a vrátí jej .
     * Je-li objektů s daným názvem více, vrátí první nalezený.
     * Nenalezne-li jej, vrátí prázdný odkaz.
     *
     * @param <E>        Typ prvků prohledávané kolekce
     * @param memberName Název hledaného objektu
     * @param collection Prohledávaná kolekce
     * @return Nalezený objekt nebo {@code null}
     */
    public static <E extends INamed>
    E get(String memberName, Collection<E> collection)
    {
        for (E iNamed : collection) {
            String name = iNamed.name();
            if (iNamed.name().equalsIgnoreCase(memberName)) {
                return iNamed;
            }
        }
        return null;
    }



//##############################################################################
//\AG== ABSTRACT GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí název dané instance.
     *
     * @return Název instance
     */
    //@Override
    public String name()
    ;



//\DM== REMAINING DEFAULT METHODS ==============================================

    /***************************************************************************
     * Vrátí textový podpis dané instance tvořený názvem její mateřské třídy
     * následovaným znakem podtržení a názvem instance.
     *
     * @return Název instance
     */
    default public String toStringWithClass()
    {
        return getClass().getSimpleName() + "_" + name();
    }

}
