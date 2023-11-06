package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.IPlace;
import cz.vse.java.kedv00.adventura.api.TypeOfStep;

import java.util.*;
import java.util.stream.Collectors;

/*******************************************************************************
 * Instance třídy {@code Place} představují prostory ve hře.
 * Každý prostor zná své aktuální bezprostřední sousedy
 * a ví, jaké objekty se v něm v daném okamžiku nacházejí.
 * Sousedé daného prostoru i v něm se nacházející objekty
 * se mohou v průběhu hry měnit.
 *
 * @author  Vojtěch KEDER
 * @version 2023_Summer
 */
public class Place extends AItemContainer implements IPlace
{
    // INSTANACE ATTRIBUTES ////////////////////////////////////////////////////

    /** Stručná charakteristika daného prostoru. */
    private final String description;

    /** Názvy sousedů daného prostoru po startu hry. */
    private final List<String> initialNeighborNames;

    /** Kolekce sousedů daného prostoru */
    private final Map<String, Place> NAME_TO_NEIGHBOR;

    /** Exportovaná kolekce aktuálních sousedů. */
    private final Collection<Place> exportedNeighbors;

    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////

    /***************************************************************************
     * Konstruktor prostoru.
     *
     * @param name Jméno prostoru
     * @param description Popis prostoru
     * @param initialNeighborNames Názvy sousedních prostorů
     * @param initialItemNames Názvy počátečních předmětů v prostoru
     */
    public Place(String name,  String description,
                 String[]  initialNeighborNames,
                 String... initialItemNames)
    {
        super(name, initialItemNames);
        this.description = description;
        this.initialNeighborNames = Arrays.stream(initialNeighborNames).
                map(String::toLowerCase).collect(Collectors.toList());

        NAME_TO_NEIGHBOR = new HashMap<>();
        exportedNeighbors = Collections.unmodifiableCollection(
                NAME_TO_NEIGHBOR.values());
    }

    // INSTANCE METHODS ////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí stručný popis daného prostoru.
     *
     * @return Stručný popis daného prostoru
     */
    @Override
    public String description()
    {
        return description;
    }

    /***************************************************************************
     * Vrátí kolekci sousedů daného prostoru, tj. kolekci prostorů,
     * do nichž je možno se z tohoto prostoru přesunout příkazem typu
     * {@link TypeOfStep#tsGOTO TypeOfStep.tsGOTO}.
     *
     * @return Kolekce sousedů
     */
    @Override
    public Collection<Place> neighbors()
    {
        return exportedNeighbors;
    }

    /***************************************************************************
     * Inicializuje daný prostor, tj. přiřadí mu počáteční sadu sousedů
     * a umístí do něj počáteční sadu objektů.
     */
    @Override
    public void initialize()
    {
        initializeNeighbors();
        super.initializeItems();
    }

    /***************************************************************************
     * Inicializuje sousedy prostoru.
     */
    private void initializeNeighbors()
    {
        World world = World.getInstance();
        NAME_TO_NEIGHBOR.clear();
        initialNeighborNames.forEach(name -> NAME_TO_NEIGHBOR.put(name,
                world.place(name)));
    }
}
