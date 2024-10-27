package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.IItem;

import static cz.vse.java.kedv00.adventura.src.Scenarios.MOVABLE;
import static cz.vse.java.kedv00.adventura.src.Scenarios.UNMOVABLE;

/*******************************************************************************
 * Instance třídy {@code Item} přestavují objekty v prostorech.
 * Objekty mohou být jak věci, tak i zvířata.
 *
 * @author  Vojtěch KEDER
 * @version 2023_Summer
 */
public class Item extends ANamed implements IItem
{
    // CLASS ATTRIBUTES ////////////////////////////////////////////////////////

    /** Kapacita batohu. */
    static final int HEAVY = Bag.getInstance().capacity() + 1;

    // INSTANACE ATTRIBUTES ////////////////////////////////////////////////////

    /** Váha daného h-objektu, od níž se odvozuje jeho přenositelnost. */
    private final int weight;

    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////

    /***************************************************************************
     * Vytvoří objekt se zadaným názvem a dalšími zadanými vlastnostmi.
     * Tyto dodatečné vlastnosti se zadávají prostřednictvím předpony
     * vkládané před vlastní název objektu
     *
     * @param name Název vytvářeného objektu
     */
    public Item(String name)
    {
        super(name.substring(1));
        if(name.charAt(0) == UNMOVABLE) { weight = HEAVY; }
        else if (name.charAt(0) == MOVABLE) { weight = 1; }
        else { throw new IllegalArgumentException("\nNeznámý typ předmětu."); }
    }

    // INSTANCE METHODS ////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí váhu předmětu, resp. charakteristiku jí odpovídající.
     * Objekty, které není možno zvednout,
     * mají váhu větší, než je kapacita batohu.
     *
     * @return Váha objektu
     */
    @Override
    public int weight() {
        return weight;
    }
}
