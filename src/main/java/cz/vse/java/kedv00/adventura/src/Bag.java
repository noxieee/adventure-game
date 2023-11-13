package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public class Bag extends AItemContainer implements IBag, Observable
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

    /** Mapa observerů. */
    private final Map<TypeOfChange, Set<Observer>> observerMap = new HashMap<>();

    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////

    /** Soukromý konstruktor batohu zajistí jedinou instanci batohu. */
    private Bag()
    {
        super("Ruce");

        for(TypeOfChange typeOfChange : TypeOfChange.values())
        {
            observerMap.put(typeOfChange, new HashSet<>());
        }
    }

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

        notifyObservers(TypeOfChange.CHANGE_OF_BAG_ITEMS);

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

        notifyObservers(TypeOfChange.CHANGE_OF_BAG_ITEMS);

        return true;
    }

    /*********************************************************
     * Metoda pro zaregistrování observeru.
     *
     * @param typeOfChange Typ změny
     * @param observer Pozorovatel
     */
    @Override
    public void registerObserver(TypeOfChange typeOfChange, Observer observer)
    {
        observerMap.get(typeOfChange).add(observer);
    }

    /******************************************************
     * Metoda pro upozornění observerů ohledně změny
     *
     * @param typeOfChange typ změny
     */
    private void notifyObservers(TypeOfChange typeOfChange)
    {
        for(Observer obs : observerMap.get(typeOfChange))
        {
            obs.update();
        }
    }
}
