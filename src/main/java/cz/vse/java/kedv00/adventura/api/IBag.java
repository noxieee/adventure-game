package cz.vse.java.kedv00.adventura.api;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/api/IBag.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/



/*******************************************************************************
 * Instance interfejsu {@code IBag} představuje úložiště,
 * do nějž hráči ukládají objekty sebrané v jednotlivých prostorech,
 * aby je mohli přenést do jiných prostorů a/nebo použít.
 * Úložiště má konečnou kapacitu definující maximální povolený
 * součet vah objektů vyskytujících se v úložišti.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2023_Summer
 */
public interface IBag
         extends IItemContainer, Observable
{
//\AG== ABSTRACT GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí kapacitu batohu, tj. maximální povolený součet vah objektů,
     * které je možno současně uložit do batohu.
     *
     * @return Kapacita batohu
     */
    //@Override
    public int capacity()
    ;

    /***************************************************************************
     * Vrátí zbávající kapacitu batohu.
     *
     * @return Zbývající kapacita batohu
     */
    public int remainingCapacity()
    ;

//\AM== REMAINING ABSTRACT METHODS =============================================

    /***************************************************************************
     * Inicializuje batoh na počátku hry. Vedle inicializace obsahu,
     * inicializuje i informaci o zbývající kapacitě.
     */
    //@Override
    public void initialize()
    ;

}
