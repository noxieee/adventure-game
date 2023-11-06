package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.IBag;
import cz.vse.java.kedv00.adventura.api.IItem;

/*******************************************************************************
 * Instance třídy {@code Bag} představuje úložiště,
 * do nějž hráči ukládají objekty sebrané v jednotlivých prostorech,
 * aby je mohli přenést do jiných prostorů a/nebo použít.
 * Úložiště má konečnou kapacitu definující maximální povolený
 * součet vah objektů vyskytujících se v úložišti.
 * <p>
 * V této hře jsou tímto úložištěm ruce teenagera.
 * s kapacitou 3 položky.
 *
 * @author  Vojtěch KEDER
 * @version 2023_Summer
 */
public class Bag extends AItemContainer implements IBag
{
    // CLASS ATTRIBUTES ////////////////////////////////////////////////////////

    /** Maximální kapacita batohu */
    private static final int MAX_CAPACITY = 3;

    /** Jediná instance batohu. */
    private static final Bag BAG = new Bag();

    // CLASS METHODS ///////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí jedinou existující instanci batohu.
     *
     * @return Požadovaná instance
     */
    public static Bag getInstance() { return BAG; }

    // INSTANCE ATTRIBUTES /////////////////////////////////////////////////////

    /** Zbývající kapacita batohu. */
    private int remainingCapacity;

    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////

    /** Soukromý konstruktor batohu zajistí jedinou instanci batohu. */
    private Bag() { super("Ruce"); }

    // INSTANCE METHODS ////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí kapacitu batohu, tj. maximální povolený součet vah objektů,
     * které je možno současně uložit do batohu.
     *
     * @return Kapacita batohu
     */
    @Override
    public int capacity() { return MAX_CAPACITY; }

    /***************************************************************************
     * Inicializuje batoh na počátku hry. Vedle inicializace obsahu,
     * inicializuje i informaci o zbývající kapacitě.
     */
    @Override
    public void initialize()
    {
        super.initializeItems();
        remainingCapacity = MAX_CAPACITY;
    }

    /***************************************************************************
     * Odebere zadaný objekt z kontejneru a vrátí informaci o tom,
     * jestli se mu to podařilo.
     *
     * @param item Odebíraný objekt
     * @return Podařilo-li se objekt odebrat, vrátí {@code true}
     */
    @Override
    public boolean removeItem(IItem item) {
        boolean result = super.removeItem(item);

        if (result) { remainingCapacity += item.weight(); }

        return result;
    }

    /***************************************************************************
     * Vejde-li se zadaný h-objekt do batohu, tak jej tam přidá a vrátí
     * {@code true}; nevejde-li se, vrátí {@code false}.
     *
     * @param item H-objekt, který se má přidat do batohu
     * @return Informace o tom, zda se přidání podařilo
     */
    @Override
    public boolean addItem(IItem item) {
        if (item.weight() > remainingCapacity) { return false; }

        super.addItem(item);
        remainingCapacity -= item.weight();

        return true;
    }
}
