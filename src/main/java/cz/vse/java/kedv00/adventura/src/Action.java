package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.*;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;

import static cz.vse.java.kedv00.adventura.src.Scenarios.*;
import static java.util.Map.entry;

/*******************************************************************************
 * Třída {@code Action} má na starosti interpretaci příkazů
 * zadávaných uživatelem hrajícím hru.
 * Název spouštěné akce je první slovo příkazu zadávaného z klávesnice
 * a další slova pak bývají interpretována jako argumenty.
 *
 * @author Vojtěch KEDER
 * @version 2023_Summer
 */
public class Action extends ANamed implements IAction
{
    // STATIC CLASS ATTRIBUTES /////////////////////////////////////////////////

    /** Mapa definovaných akcí. */
    private static final Map<String, Action> NAME_TO_ACTION;

    /** Mapa typu (Jméno->Hodnota) testovaných a nastavovaných příznaků. */
    private static final Map<String, Object> NAME_TO_FLAG;

    /** Exportovaný pohled na mapu testovaných a nastavovaných příznaků,
     *  jenž neumožňuje hodnotu těchto příznaků změnit. */
    static final Map<String, Object> CONDITIONS;

    /** Mapa typu (Jméno->Odkaz_na_test) testů, z nichž každý prověřuje
     *  jeden aspekt stavu hry potřebný k provedení některé pomocné akce. */
    private static final Map<String, IAction.ITest> NAME_2_TEST;

    /** Exportovaný pohled na mapu testovaných a nastavovaných příznaků,
     *  jenž neumožňuje hodnotu těchto příznaků změnit. */
    static final Map<String, IAction.ITest> TESTS;

    /** Mapa typu (Jméno->identifikátor) konvertující jméno ze scénáře
     *  na identifikátor používaný v programu. */
    private static final Map<String, String> NAME_TO_ID;

    /** Rozhodování, jestli hra běží nebo ne */
    private static boolean isAlive = false;

    /** Random generator */
    private static final Random randomGenerator = new Random();

    // STATIC CLASS CONSTRUCTORS ///////////////////////////////////////////////

    static
    {
        NAME_TO_ID = Map.of("kočka", "cat",
                            "talíř", "plate",
                            "miska", "bowl",
                            "nůž", "knife",
                            "lilie", "lily",
                            "monstera", "monstera");

        NAME_TO_ACTION = Map.ofEntries(
                entry(COMMAND_GOTO.toLowerCase(),
                        new Action(COMMAND_GOTO, Action::GOTO,
                       "Přesune tě do zadaného sousedního prostoru.")),
                entry(COMMAND_TAKE.toLowerCase(),
                        new Action(COMMAND_TAKE, Action::TAKE,
                        "Vezme zadaný předmět z prostoru do tvých " +
                        "rukou.\nPředmět musí být v aktuálním prostoru" +
                        ", musí být přenositelný \na v rukou na něj musíš mít" +
                         " místo.")),
                entry(COMMAND_PUT.toLowerCase(),
                        new Action(COMMAND_PUT, Action::PUT,
                        "Zadaný předmět položí z " +
                        "rukou do aktuálního prostoru.")),
                entry(COMMAND_HELP.toLowerCase(),
                        new Action(COMMAND_HELP, Action::HELP,
                        "Zobrazí seznam dostupných akcí spolu " +
                        "s jejich stručnými popisy.")),
                entry(COMMAND_END.toLowerCase(),
                        new Action(COMMAND_END, Action::END,
                        "Předčasné ukončení hry.")),
                entry(COMMAND_WATER.toLowerCase(),
                        new Action(COMMAND_WATER, Action::WATER,
                        "Zalije zadaný předmět, " +
                        "pokud se dá zalít a nachází se v prostoru.")),
                entry(COMMAND_WASH.toLowerCase(),
                        new Action(COMMAND_WASH, Action::WASH,
                        "Umyje zadaný předmět, " +
                        "pokud je umytelný a zároveň je položený ve dřezu.")),
                entry(COMMAND_FEED.toLowerCase(),
                        new Action(COMMAND_FEED, Action::FEED,
                        "Nakrmí nakrmitelný objekt v prostoru.")),
                entry(COMMAND_WIN.toLowerCase(),
                        new Action(COMMAND_WIN, Action::PLAY,
                        "Příkaz vyhraje hru, pokud " +
                        "se v prostoru nachází nakrmený objekt.")),
                entry(COMMAND_IDKFA.toLowerCase(),
                        new Action(COMMAND_IDKFA, Action::IDKFA,
                        "Příkaz sebere náhodné předměty z prostorů do rukou."))
        );

        NAME_TO_FLAG = new HashMap<>();
        CONDITIONS  = Collections.unmodifiableMap(NAME_TO_FLAG);

        NAME_2_TEST = Map.ofEntries(
                entry("argumentPresent", Action::ARGUMENT_PRESENT),
                entry("feedablePresent", Action::FEEDABLE_PRESENT),
                entry("feededPresent", Action::FEEDED_PRESENT),
                entry("argumentWashable", Action::ARGUMENT_WASHABLE),
                entry("argumentWaterable", Action::ARGUMENT_WATERABLE),
                entry("allWashedWatered", Action::ALL_WASHED_WATERED),
                entry("placeIsSink", Action::PLACE_IS_SINK)
        );

        TESTS = NAME_2_TEST;
    }

    // STATIC CLASS METHODS ////////////////////////////////////////////////////

    /***********************************************************************
     * Metoda realizující akci IDKFA.
     *
     * @return Reakce hry na zadaný příkaz.
     */
    private static String IDKFA(String[] arguments)
    {
        IGame game = Game.getInstance();
        int remainingBagCapacity = game.bag().remainingCapacity();

        if(remainingBagCapacity == 0) { return "Už neuneseš žádné další předměty."; }

        List<String> itemsObtained = new ArrayList<>();

        Collection<? extends IPlace> allPlaces = game.world().places();
        List<Pair<IItem, IPlace>> itemToPlace = new ArrayList<>();

        for(IPlace place : allPlaces)
        {
            Collection<IItem> items = place.items();

            for(IItem item : items)
            {
                if(item.weight() != Item.HEAVY)
                {
                    itemToPlace.add(new Pair<>(item, place));
                }
            }
        }

        if(itemToPlace.isEmpty()) { return "V prostorech se nenachází žádné zvednutelné předměty."; }

        for(int i = 0; i < remainingBagCapacity; i++)
        {
            int randomIndex = randomGenerator.nextInt(itemToPlace.size());
            Pair<IItem, IPlace> itemIPlacePair = itemToPlace.get(randomIndex);
            IItem item = itemIPlacePair.getKey();
            IPlace placeOfItem = itemIPlacePair.getValue();

            itemsObtained.add(item.name());

            placeOfItem.removeItem(item);
            game.bag().addItem(item);

            itemToPlace.remove(randomIndex);
        }

        StringBuilder sb = new StringBuilder();

        for(String itemName : itemsObtained)
        {
            sb.append(itemName.toLowerCase());

            if(!itemsObtained.get(itemsObtained.size() - 1).equals(itemName))
            {
                sb.append(", ");
            }
        }

        return COMMAND_IDKFA_DESC + sb;
    }

    /***************************************************************************
     * Metoda realizující akci Jdi.
     *
     * @return Reakce hry na zadaný příkaz.
     */
    private static String GOTO(String[] arguments)
    {
        if(arguments.length < 2) { return ERR_MOVE_NO_ARG; }

        String destinationName = arguments[1];
        World world = World.getInstance();
        Place currentPlace = world.currentPlace();
        Place destination = INamed.get(destinationName,
                currentPlace.neighbors());

        if(destination == null) { return ERR_BAD_NEIGHBOUR + destinationName; }

        world.setCurrentPlace(destination);

        return COMMAND_GOTO_DESC + destinationName +
                '\n' + destination.description();
    }

    /***************************************************************************
     * Metoda realizující akci Vezmi.
     *
     * @return @return Reakce hry na zadaný příkaz.
     */
    private static String TAKE(String[] arguments)
    {
        if(arguments.length < 2) { return ERR_TAKE_NO_ARG; }

        String itemName = arguments[1];

        if(!ARGUMENT_PRESENT(arguments)) { return ERR_BAD_ITEM + itemName; }

        Item item = getItem(itemName);

        if(item.weight() == Item.HEAVY) { return ERR_UNMOVABLE + itemName; }

        Bag bag = Bag.getInstance();

        Place currentPlace = World.getInstance().currentPlace();

        currentPlace.removeItem(item);

        if(!bag.addItem(item))
        {
            currentPlace.addItem(item);
            return ERR_BAG_FULL + itemName;
        }

        return COMMAND_TAKE_DESC + itemName;
    }

    /***************************************************************************
     * Metoda realizující akci Polož.
     *
     * @return Reakce hry na zadaný příkaz.
     */
    private static String PUT(String[] arguments)
    {
        if(arguments.length < 2) { return ERR_PUT_NO_ARG; }

        String itemName = arguments[1];
        Bag bag = Bag.getInstance();
        Item item = bag.item(itemName);

        if(item == null) { return ERR_NOT_IN_BAG + itemName; }

        Place currentPlace = World.getInstance().currentPlace();
        currentPlace.addItem(item);
        bag.removeItem(item);
        return COMMAND_PUT_DESC + itemName;
    }

    /***************************************************************************
     * Metoda realizující akci ?, která vyvolá nápovědu.
     *
     * @return Reakce hry na zadaný příkaz.
     */
    private static String HELP(String[] arguments)
    {
        Collection<Action> actions = allActions();
        Collection<IItem> placeItems = World.getInstance().currentPlace().items();
        List<IItem> placeItemsAsList = new ArrayList<>(placeItems);
        Collection<IItem> bagItems = Game.getInstance().bag().items();
        List<IItem> bagItemsAsList = new ArrayList<>(bagItems);


        StringBuilder sb = new StringBuilder(HELP_TEXT);
        for(Action action : actions)
        {
            sb.append("\n\n").append(action.name())
                    .append('\n').append(action.description());
        }

        sb.append("\n\n").append("MAPA PROSTORŮ\n\n");
        sb.append("  DŘEZ---KUCHYNĚ\n" +
                  "            |\n" +
                  "BALKON---CHODBA---KOUPELNA\n" +
                  "            |\n" +
                  "          POKOJ");

        sb.append("\n\n").append("Momentální prostor: ");
        sb.append(World.getInstance().currentPlace().name());

        sb.append("\n\n").append("Předměty v tomto prostoru:\n");

        for(IItem item : placeItemsAsList)
        {
            sb.append(item.name());

            if(!placeItemsAsList.get(placeItemsAsList.size() - 1).equals(item))
            {
                sb.append(", ");
            }
        }

        sb.append("\n\n").append("Předměty v rukou:\n");

        for(IItem item : bagItemsAsList)
        {
            sb.append(item.name());

            if(!bagItemsAsList.get(bagItemsAsList.size() - 1).equals(item))
            {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    /***************************************************************************
     * Metoda realizující akci Konec.
     *
     * @return Reakce hry na zadaný příkaz.
     */
    private static String END(String[] arguments)
    {
        stop();
        return COMMAND_END_DESC;
    }

    /***************************************************************************
     * Metoda realizující akci Zalij.
     *
     * @return Reakce hry na zadaný příkaz.
     */
    private static String WATER(String[] arguments)
    {
        if(arguments.length < 2) { return ERR_WATER_NO_ARG; }

        String itemName = arguments[1];

        if(!ARGUMENT_PRESENT(arguments)) { return ERR_BAD_ITEM + itemName; }

        if(!ARGUMENT_WATERABLE(arguments)) { return ERR_UNWATERABLE + itemName;}

        if(getFlag(itemName, "watered"))
        {
            return ERR_ALREADY_WATERED + itemName;
        }

        setFlagTrue(itemName, "watered");

        return COMMAND_WATER_DESC + itemName;
    }

    /***************************************************************************
     * Metoda realizující akci Umyj.
     *
     * @return Reakce hry na zadaný příkaz.
     */
    private static String WASH(String[] arguments)
    {
        if(arguments.length < 2) { return ERR_WASH_NO_ARG; }

        String itemName = arguments[1];

        if(!ARGUMENT_PRESENT(arguments)) { return ERR_BAD_ITEM + itemName; }

        if(!PLACE_IS_SINK(arguments))
        {
            return ERR_WASH_COMMAND_NOT_IN_SINK + itemName;
        }

        if(!ARGUMENT_WASHABLE(arguments)) { return ERR_UNWASHABLE + itemName; }

        if(getFlag(itemName, "washed"))
        {
            return ERR_ALREADY_WASHED + itemName;
        }

        setFlagTrue(itemName, "washed");

        return COMMAND_WASH_DESC + itemName;
    }

    /***************************************************************************
     * Metoda realizující akci Nakrm.
     *
     * @return Reakce hry na zadaný příkaz.
     */
    private static String FEED(String[] arguments)
    {
        if(!FEEDABLE_PRESENT(arguments)) { return ERR_FEEDABLE_NOT_PRESENT; }

        Item item = getFeedable();
        String itemName = item.name().toLowerCase();

        if(!ALL_WASHED_WATERED(arguments))
        {
            return ERR_FEED_NOT_WASHED_WATERED + itemName;
        }

        if(getFlag(itemName, "feeded"))
        {
            return ERR_ALREADY_FEEDED + itemName;
        }

        setFlagTrue(itemName, "feeded");

        return COMMAND_FEED_DESC + itemName;
    }

    /***************************************************************************
     * Metoda realizující akci Hrát_pc_hry.
     *
     * @return Reakce hry na zadaný příkaz.
     */
    private static String PLAY(String[] arguments)
    {
        if(!FEEDABLE_PRESENT(arguments)) { return ERR_FEEDED_NOT_PRESENT; }

        if(!FEEDED_PRESENT(arguments)) { return ERR_CAT_NOT_FEEDED; }

        stop();
        return GAME_WIN_TEXT;
    }

    /***************************************************************************
     * Testovací metoda pro zjištění, jesti se předmět nachází v prostoru
     *
     * @return True/False podle toho, zda v prostoru předmět je nebo není.
     */
    private static boolean ARGUMENT_PRESENT(String[] arguments)
    {
        return getItem(arguments[1]) != null;
    }

    /***************************************************************************
     * Testovací metoda pro zjištění, zda je momentální prostor dřez.
     *
     * @return True/False podle toho, zda je prostorem dřez.
     */
    private static boolean PLACE_IS_SINK(String[] arguments)
    {
        String currentPlaceName = World.getInstance().currentPlace().
                name().toLowerCase();

        String sinkName = SINK_NAME.toLowerCase();

        return sinkName.equals(currentPlaceName);
    }

    /***************************************************************************
     * Testovací metoda pro zjištění, zda je objekt zalitelný.
     *
     * @return True/False podle toho, zda je předmět zalitelný.
     */
    private static boolean ARGUMENT_WATERABLE(String[] arguments)
    {
        List<String> waterables = (List)NAME_TO_FLAG.get("waterable");

        return waterables.contains(arguments[1].toLowerCase());
    }

    /***************************************************************************
     * Testovací metoda pro zjištění, zda je objekt umytelný.
     *
     * @return True/False podle toho, zda je předmět umytelný.
     */
    private static boolean ARGUMENT_WASHABLE(String[] arguments)
    {
        List<String> washables = (List)NAME_TO_FLAG.get("washable");

        return washables.contains(arguments[1].toLowerCase());
    }

    /***************************************************************************
     * Testovací metoda pro zjištění, zda se v prostoru
     * nachází nakrmitelný předmět.
     *
     * @return True/False podle toho, zda se takový předmět v prostoru nachází.
     */
    private static boolean FEEDABLE_PRESENT(String[] arguments)
    {
        return getFeedable() != null;
    }

    /***************************************************************************
     * Testovací metoda pro zjištění, zda se v prostoru
     * nachází nakrmený předmět.
     *
     * @return True/False podle toho, zda se takový předmět v prostoru nachází.
     */
    private static boolean FEEDED_PRESENT(String[] arguments)
    {
        Item item = getFeedable();

        return item != null && getFlag(item.name().toLowerCase(),
                "feeded");
    }

    /***************************************************************************
     * Testovací metoda pro zjištění, zda jsou všechny umytelné předměty
     * umyty a všechny zalitelné zality.
     *
     * @return True/False podle toho, zda tomu tak opravdu je.
     */
    private static boolean ALL_WASHED_WATERED(String[] arguments)
    {
        List<String> waterables = (List)NAME_TO_FLAG.get("waterable");
        List<String> washables = (List)NAME_TO_FLAG.get("washable");
        boolean result = true;

        for(String waterableName : waterables)
        {
            if(!getFlag(waterableName, "watered"))
            {
                result = false;
                break;
            }
        }

        for(String washableName : washables)
        {
            if(!result) { break; }

            if(!getFlag(washableName, "washed"))
            {
                result = false;
                break;
            }
        }

        return result;
    }

    /***************************************************************************
     * Metoda pro získání nakrmitelného předmětu z prostoru.
     *
     * @return Požadovaný předmět, nebo null, pokud v prostoru není.
     */
    private static Item getFeedable()
    {
        Place currentPlace = World.getInstance().currentPlace();
        List<String> feedables = (List)NAME_TO_FLAG.get("feedable");
        Item feedableItem = null;

        for(IItem item : currentPlace.items())
        {
            String itemName = item.name().toLowerCase();

            if(feedables.contains(itemName))
            {
                feedableItem = (Item)item;
                break;
            }
        }

        return feedableItem;
    }

    /***************************************************************************
     * Metoda pro získání předmětu z prostoru
     *
     * @return Požadovaný předmět, nebo null, pokud v prostoru není.
     */
    private static Item getItem(String itemName)
    {
        String lowerName = itemName.toLowerCase();
        return World.getInstance().currentPlace().item(lowerName);
    }

    /***************************************************************************
     * Metoda pro získání zadaného příznaku předmětu.
     *
     * @return Hodnota příznaku.
     */
    private static boolean getFlag(String itemName, String flagName)
    {
        String fullName = NAME_TO_ID.get(itemName.toLowerCase())
                + '.' + flagName;

        return (boolean)NAME_TO_FLAG.get(fullName);
    }

    /***************************************************************************
     * Složí název nastavovaného příznaku a nastaví mu zadanou hodnotu.
     */
    private static void setFlagTrue(String itemName, String flagName)
    {
        String fullName = NAME_TO_ID.get(itemName.toLowerCase())
                + '.' + flagName;

        NAME_TO_FLAG.put(fullName, true);
    }

    /***************************************************************************
     * Metoda zjišťující stav hry a podle toho proveditelnost příkazu.
     *
     * @return hra běží nebo ne
     */
    public static boolean isAlive() { return isAlive; }

    /***************************************************************************
     * Vrátí kolekci všech příkazů použitelných ve hře.
     *
     * @return Kolekce všech příkazů použitelných ve hře
     */
    public static Collection<Action> allActions()
    {
        return Collections.unmodifiableCollection(NAME_TO_ACTION.values());
    }

    /***************************************************************************
     * Zpracuje zadaný příkaz a vrátí text zprávy pro uživatele.
     *
     * @param command Zadávaný příkaz
     * @return Textová odpověď hry na zadaný příkaz
     */
    public static String executeCommand(String command)
    {
        command = command.trim().toLowerCase();

        if (command.isEmpty())
        {
            return executeEmptyCommand();
        }
        else
        {
            return executeStandartCommand(command);
        }
    }

    /***************************************************************************
     * Metoda zastavující hru.
     */
    public static void stop()
    {
        isAlive = false;
    }

    /***************************************************************************
     * Definuje reakci na prázdný příkaz.
     */
    private static String executeEmptyCommand()
    {
        if(isAlive) { return ERR_EMPTY_COMMAND_GAME_RUNNING; }

        isAlive = true;
        initialize();
        return GAME_START_TEXT;
    }

    /***************************************************************************
     * Definuje reakci na neprázdný příkaz.
     *
     * @return Reakce hry na neprázdný příkaz.
     */
    private static String executeStandartCommand(String command)
    {
        if(!isAlive) { return ERR_START_COMMAND_NOT_EMPTY; }

        String[] words = command.trim().split("\\s+");
        String actionName = words[0];
        Action action = NAME_TO_ACTION.get(actionName);

        if(action == null) { return ERR_UNKNOWN_COMMAND + words[0]; }

        return action.execute(words);
    }

    /***************************************************************************
     * Metoda inicializující celou hru.
     */
    private static void initialize()
    {
        World.getInstance().initialize();
        Bag.getInstance().initialize();

        NAME_TO_FLAG.put("washable",List.of(PLATE_NAME.toLowerCase(),
                BOWL_NAME.toLowerCase(), KNIFE_NAME.toLowerCase()));
        NAME_TO_FLAG.put("waterable", List.of(MONSTERA_NAME.toLowerCase(),
                LILY_NAME.toLowerCase()));
        NAME_TO_FLAG.put("feedable", List.of(CAT_NAME.toLowerCase()));
        NAME_TO_FLAG.put("cat.feeded", false);
        NAME_TO_FLAG.put("plate.washed", false);
        NAME_TO_FLAG.put("bowl.washed", false);
        NAME_TO_FLAG.put("knife.washed", false);
        NAME_TO_FLAG.put("lily.watered", false);
        NAME_TO_FLAG.put("monstera.watered", false);
    }

    // INSTANACE ATTRIBUTES ////////////////////////////////////////////////////
    /** Stručná charakteristika dané akce. */
    private final String description;

    /** Metoda realizující danou akci. */
    private final Function<String[], String> action;

    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////
    /***************************************************************************
     * Konstruktor instance akce.
     *
     * @param name Název akce
     * @param description Popis akce
     */
    public Action(String name, Function<String[], String> action,
                  String description)
    {
        super(name);
        this.description = description;
        this.action = action;
    }

    // INSTANCE METHODS ////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí popis příkazu s vysvětlením jeho funkce,
     * významu jednotlivých parametrů
     * a možností (resp. účelu) použití daného příkazu.
     * Tento popis tak může sloužit jako nápověda k použití daného příkazu.
     *
     * @return Popis příkazu
     */
    @Override
    public String description()
    {
        return this.description;
    }

    /***************************************************************************
     * Metoda realizující reakci hry na zadání daného příkazu.
     * Obdržené pole je vždy neprázdné,
     * protože jeho nultým prvkem je název aktivované akce.
     * Počet parametrů dané akce je závislý na konkrétní akci,
     * např. akce typu <i>konec</i> a <i>nápověda</i> nemají parametry,
     * akce typu <i>jdi</i> a <i>seber</i> mají jeden parametr
     * akce typu <i>použij</i> muže mít dva parametry atd.
     *
     * @param arguments Parametry příkazu – akce;
     *                  jejich počet muže byt pro každou akci jiný,
     *                  ale pro všechna spuštění stejné akce je stejný
     * @return Text zprávy vypsané po provedeni příkazu
     */
    @Override
    public String execute(String... arguments)
    {
        return this.action.apply(arguments);
    }
}
