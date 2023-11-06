package cz.vse.java.kedv00.adventura.api;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/api/IPlace.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/

import java.util.Collection;



/*******************************************************************************
Instance interfejsu {@code IPlace} představují prostory ve hře.
Dosažení prostoru si můžeme představovat jako částečný cíl,
kterého se hráč ve hře snaží dosáhnout.
Prostory mohou být místnosti, planety, životní etapy atd.
Prostory mohou obsahovat různé objekty,
které mohou hráči pomoci v dosažení cíle hry.
Každý prostor zná své aktuální bezprostřední sousedy
a ví, jaké objekty se v něm v daném okamžiku nacházejí.
Sousedé daného prostoru i v něm se nacházející objekty
se mohou v průběhu hry měnit.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2023_Summer
 */
public interface IPlace
         extends INamed, IItemContainer
{
//##############################################################################
//\AG== ABSTRACT GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí stručný popis daného prostoru.
     *
     * @return Stručný popis daného prostoru
     */
    //@Override
    public String description()
    ;


    /***************************************************************************
     * Vrátí kolekci sousedů daného prostoru, tj. kolekci prostorů,
     * do nichž je možno se z tohoto prostoru přesunout příkazem typu
     * {@link TypeOfStep#tsGOTO
     * TypeOfStep.tsGOTO}.
     *
     * @return Kolekce sousedů
     */
    //@Override
    public Collection<? extends IPlace> neighbors()
    ;



//\DG== DEFAULT GETTERS AND SETTERS ============================================

    /***************************************************************************
     * Inicializuje daný prostor, tj. přiřadí mu počáteční sadu sousedů
     * a umístí do něj počáteční sadu objektů.
     */
    //@Override
    public void initialize()
    ;

}
