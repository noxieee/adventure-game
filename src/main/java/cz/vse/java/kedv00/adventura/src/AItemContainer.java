package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.IItem;
import cz.vse.java.kedv00.adventura.api.IItemContainer;


import java.util.*;

/*******************************************************************************
 * Instance abstraktní třídy {@code AItemContainer} jsou
 * rodičovskými podobjekty objektů, sloužícími jako kontejnery h-objektů,
 * konkrétně batohu a prostorů.
 *
 * @author  Vojtěch KEDER
 * @version 2023_Summer
 */
abstract class AItemContainer extends ANamed implements IItemContainer
{
    // INSTANACE ATTRIBUTES ////////////////////////////////////////////////////

    /** Názvy h-objektů obsažených v kontejneru na počátku každé hry. */
    private final List<String> initialNames;

    /** Názvy aktuálně obsažených h-objektů. */
    private final List<String> itemNames;

    /** Aktuálně obsažené h-objekty. */
    private final List<Item> items;

    /** Aktuálně obsažené h-objekty. */
    private final List<IItem> itemsView;

    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////

    /***************************************************************************
     * Vytvoří rodičovský podobjekt kontejneru h-objektům,
     * který na počátku hry bosahuje h-objekty se zadanými názvy.
     *
     * @param name Zadané jméno pojmenovaného objektu
     * @param initialNames Názvy h-objektů obsažených na počátku každé hry
     */
    AItemContainer(String name, String... initialNames)
    {
        super(name);
        this.initialNames = Arrays.asList(initialNames);
        this.itemNames    = new ArrayList<>();
        this.items        = new ArrayList<>();
        this.itemsView    = Collections.unmodifiableList(items);
    }

    // INSTANCE METHODS ////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí kolekci objektů nacházejících se v daném kontejneru.
     *
     * @return Kolekce objektů nacházejících se v daném kontejneru
     */
    @Override
    public Collection<IItem> items() { return itemsView; }

    /***************************************************************************
     * Je li v kontejneru objekt se zadaným názvem, vrátí jej,
     * není-li tam, vrátí prázdný odkaz {@code null}.
     *
     * @param  name Název hledaného objektu
     * @return Hledaný objekt nebo prázdný odkaz {@code null}.
     */
    @Override
    public Item item(String name)
    {
        int index = itemNames.indexOf(name);
        return index >= 0 ? items.get(index) : null;
    }

    /***************************************************************************
     * Přidá zadaný objekt do kontejneru a vrátí informaci o tom,
     * jestli se to podařilo.
     *
     * @param item Přidávaný objekt
     * @return Podařilo-li se objekt přidat, vrátí {@code true}
     */
    @Override
    public boolean addItem(IItem item)
    {
        items.add((Item)item);
        itemNames.add(item.name().toLowerCase());
        return true;
    }

    /***************************************************************************
     * Odebere zadaný objekt z kontejneru a vrátí informaci o tom,
     * jestli se to podařilo.
     *
     * @param item Odebíraný objekt
     * @return Podařilo-li se objekt odebrat, vrátí {@code true}
     */
    @Override
    public boolean removeItem(IItem item)
    {
        int index = itemNames.indexOf(item.name().toLowerCase());

        if(index < 0) { return false; }

        itemNames.remove(index);
        items.remove(index);

        return true;
    }

    /***************************************************************************
     * Inicializuje kontejner na počátku hry.
     * Po inicializace bude obsahovat příslušnou výchozí sadu objektů.
     */
    @Override
    public void initializeItems()
    {
        itemNames.clear();
        items.clear();

        for (String iName : initialNames)
        {
            itemNames.add(iName.substring(1).toLowerCase());
            items.add(new Item(iName));
        }
    }
}
