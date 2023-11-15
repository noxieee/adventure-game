package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.*;
import cz.vse.java.kedv00.adventura.api.Observer;

import java.util.*;
import java.util.stream.Collectors;

import static cz.vse.java.kedv00.adventura.src.Scenarios.*;

/*******************************************************************************
 * Instance třídy {@code World} reprezentuje byt líného teenagera.
 * V dané hře je definována jako jedináček.
 * Má na starosti uspořádání jednotlivých prostorů a udržuje informaci o tom,
 * ve kterém z nich se hráč právě nachází.
 * Vzájemné uspořádání prostorů se v průběhu této hry nemění,
 * takže prostory v průběhu hry nezískávají ani neztrácejí sousedy.
 * <p>
 * V této hře je světem hry byt líného teenagera žijícího s rodiči.
 * a jednotlivé prostory jsou pokoj, chodba, koupelna, kuchyně, dřez a balkon.
 *
 * @author Vojtěch KEDER
 * @version 2023_Summer
 */
public class World implements IWorld
{
    // CLASS ATTRIBUTES ////////////////////////////////////////////////////////

    /** Jediná instance světa. */
    private static final World WORLD = new World();

    // CLASS METHODS ///////////////////////////////////////////////////////////

    /***************************************************************************
     * Tovární metoda vracející odkaz na jedninou existující instanci dané hry.
     *
     * @return Instance dané hry
     */
    public static World getInstance() { return WORLD; }

    // INSTANCE ATTRIBUTES /////////////////////////////////////////////////////

    /** Mapa převádějící název prostoru na objekt daného prostoru hry. */
    private final Map<String, Place> NAME_TO_PLACE;

    /** Kolekce všech prostorů světa hry poskytovaná tazatelům. */
    private final Collection<Place> ALL_PLACES;

    /** Výchozí aktuální prostor na počátku hry. */
    private final Place START_PLACE;

    /** Aktuální prostor, v němž se nachází hráč. */
    private Place currentPlace;

    /** Určuje, zda je teleport již odemknutý. */
    private boolean teleportUnlocked;

    /** Historie prostorů, jak jimi hráč procházel. */
    private final List<String> placeHistory = new ArrayList<>();

    /** Mapa observerů. */
    private final Map<TypeOfChange, Set<Observer>> observerMap = new HashMap<>();

    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////

    /** Soukromý konstruktor světa zajistí jedinou instanci světa. */
    private World()
    {
        List<Place> places = List.of(
                new Place(BEDROOM_NAME, BEDROOM_DESC,
                        new String[] {HALLWAY_NAME},
                        MOVABLE + PLATE_NAME,
                        UNMOVABLE + TABLE_NAME
                ),
                new Place(HALLWAY_NAME, HALLWAY_DESC,
                        new String[] {BEDROOM_NAME, BATHROOM_NAME,
                                BALCONY_NAME, KITCHEN_NAME},
                        MOVABLE + CAT_NAME
                ),
                new Place(BATHROOM_NAME, BATHROOM_DESC,
                        new String[] {HALLWAY_NAME},
                        MOVABLE + MONSTERA_NAME
                ),
                new Place(BALCONY_NAME, BALCONY_DESC,
                        new String[] {HALLWAY_NAME},
                        MOVABLE + LILY_NAME
                ),
                new Place(KITCHEN_NAME, KITCHEN_DESC,
                        new String[] {HALLWAY_NAME, SINK_NAME},
                        MOVABLE + BOWL_NAME,
                        MOVABLE + KNIFE_NAME,
                        UNMOVABLE + TABLE_NAME
                ),
                new Place(SINK_NAME, SINK_DESC,
                        new String[] {KITCHEN_NAME}
                )
        );

        Map<String, Place> nameToPlace = places.stream().collect(
                Collectors.toMap(x -> x.name().toLowerCase(), x -> x)
        );

        ALL_PLACES = places;
        NAME_TO_PLACE = Map.copyOf(nameToPlace);
        START_PLACE = places.get(0);

        for(TypeOfChange typeOfChange : TypeOfChange.values())
        {
            observerMap.put(typeOfChange, new HashSet<>());
        }

        teleportUnlocked = false;
    }

    // INSTANCE METHODS ////////////////////////////////////////////////////////

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

    /***************************************************************************
     * Vrátí kolekci odkazů na všechny prostory vystupující ve hře.
     *
     * @return Kolekce odkazů na všechny prostory vystupující ve hře
     */
    @Override
    public Collection<Place> places()
    {
        return ALL_PLACES;
    }

    /***************************************************************************
     * Vrátí odkaz na aktuální prostor,
     * tj. na prostor, v němž se hráč pravé nachází.
     *
     * @return Prostor, v němž se hráč pravé nachází
     */
    @Override
    public Place currentPlace()
    {
        return currentPlace;
    }

    /***************************************************************************
     * Je li ve světě hry prostor se zadaným názvem, vrátí jej,
     * není-li tam, vrátí prázdný odkaz {@code null}.
     *
     * @param name Název hledaného prostoru
     * @return Hledaný prostor nebo prázdný odkaz {@code null}
     */
    @Override
    public Place place(String name)
    {
        return NAME_TO_PLACE.get(name.toLowerCase());
    }

    /***************************************************************************
     * Nastaví zadaný prosto jako aktuální, tj. jako prostor,
     * v němž se aktuálně nachází hráč.
     *
     * @param destinationRoom Nastavovaný prostor
     */
    @Override
    public void setCurrentPlace(IPlace destinationRoom)
    {
        currentPlace = (Place)destinationRoom;

        notifyObservers(TypeOfChange.CHANGE_OF_PLACE);

        if(placeHistory.size() == 4)
        {
            placeHistory.remove(0);
            placeHistory.add(3, destinationRoom.name().toLowerCase());
        }
        else
        {
            placeHistory.add(destinationRoom.name().toLowerCase());
        }

        if(!teleportUnlocked && placeHistory.size() == 4 &&
                placeHistory.get(0).equals(placeHistory.get(2)) && placeHistory.get(1).equals(placeHistory.get(3)))
        {
            teleportUnlocked = true;
            notifyObservers(TypeOfChange.CHANGE_OF_TELEPORT_UNLOCKED);
        }
    }

    /***************************************************************************
     * Inicializuje svět hry, tj. inicializuje propojení prostorů
     * a jejich obsah a nastaví výchozí aktuální prostor.
     */
    @Override
    public void initialize()
    {
        for(Place place : NAME_TO_PLACE.values())
        {
            place.initialize();
        }

        setCurrentPlace(START_PLACE);

        placeHistory.clear();
        teleportUnlocked = false;

        notifyObservers(TypeOfChange.CHANGE_OF_TELEPORT_UNLOCKED);
    }
}
