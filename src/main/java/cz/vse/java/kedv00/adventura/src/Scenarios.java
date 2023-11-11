package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.Scenario;
import cz.vse.java.kedv00.adventura.api.ScenarioStep;

import java.util.List;
import java.util.Map;

import static cz.vse.java.kedv00.adventura.api.TypeOfScenario.*;
import static cz.vse.java.kedv00.adventura.api.TypeOfStep.*;


/*******************************************************************************
 * Třída {@code Scenarios} obsahuje základní čtveřici scénářů
 * pro hru inspirovanou životem líného teenagera.
 * Podle těchto scénářů je možno hrát či testovat hru.
 * Aby bylo možno jednotlivé scénáře od sebe odlišit, je každý pojmenován
 * a má přiřazen typ, podle které lze blíže určit, k čemu je možno jej použít.
 * Scénáře jsou definovány jako posloupnosti kroků.
 *
 * @author  Vojtěch KEDER
 * @version 2023_Summer
 */
public class Scenarios
{
    // CLASS ATTRIBUTES ////////////////////////////////////////////////////////

    /** Text popisující cíl hry a způsob jeho dosažení
     *  použitý jako uvítání hráče při startu hry a také při vyvolání nápovědy.
     */
    static final String SUBJECT =
            "\nJsi líný teenager bydlící s rodiči. Po škole jen hraješ\n" +
            "videohry a do ničeho jiného se ti moc nechce. Nerad po sobě\n" +
            "uklízíš, podle toho také vypadá tvůj pokoj. Tvou mamku to už\n" +
            "ale přestalo bavit, dnes ti dala za úkol hned po škole umýt\n" +
            "nádobí, zalít kytky a potom nakrmit kočku.\n" +
            "Teprve až to budeš mít hotové, můžeš hrát videohry, protože\n" +
            "pokud se mamka vrátí a nebudou tyto úkoly hotové, zabaví ti\n" +
            "tvůj herní počítač.\n\n" +
            "Tvým úkolem je v celém bytě sebrat nádobí, dát ho do dřezu,\n" +
            "umýt nádobí, zalít kytky, a nakrmit kočku.\n" +
            "Až potom můžeš hrát videohry. Cílem hry je tedy zahrát si.\n";

    /** Text popisující nápovědu */
    static final String HELP_TEXT =
            "NÁPOVĚDA\n" + SUBJECT + "\n" + "MŮŽETE ZADAT TYTO PŘÍKAZY:";

    // Texty popisující reakci hry na start a úspěšné dokončení hry
    static final String GAME_START_TEXT =
            "Vítej hráči!\n" + SUBJECT +
            "\nNebudeš-li si vědět rady, zadej znak ?, jenž zobrazí nápovědu.";
    static final String GAME_WIN_TEXT =
            "Výborně, splnil jsi maminčiny úkoly a nyní\n" +
            "můžeš prokrastinovat u hraní videoher!\n" +
            "Úspěšně jsi dohrál hru. Pro novou hru zmáčkni ENTER.";

    // Názvy příkazů
    public static final String COMMAND_TAKE = "Vezmi";
    public static final String COMMAND_PUT = "Polož";
    public static final String COMMAND_GOTO = "Jdi";
    static final String COMMAND_HELP = "?";
    public final static String COMMAND_END = "Konec";
    static final String COMMAND_WASH = "Umyj";
    static final String COMMAND_WATER = "Zalij";
    static final String COMMAND_FEED = "Nakrm";
    static final String COMMAND_WIN = "Hrát_pc_hry";

    // Názvy předmětů
    static final String PLATE_NAME = "Talíř";
    static final String BOWL_NAME = "Miska";
    static final String KNIFE_NAME = "Nůž";
    static final String CAT_NAME = "Kočka";
    static final String LILY_NAME = "Lilie";
    static final String MONSTERA_NAME = "Monstera";
    static final String TABLE_NAME = "Stůl";

    // Kódy typů předmětů
    static final char UNMOVABLE = '#';
    static final char MOVABLE = '^';

    // Názvy prostorů
    public static final String BEDROOM_NAME = "Pokoj";
    public static final String HALLWAY_NAME = "Chodba";
    public static final String BATHROOM_NAME = "Koupelna";
    public static final String SINK_NAME = "Dřez";
    public static final String BALCONY_NAME = "Balkon";
    public static final String KITCHEN_NAME = "Kuchyně";

    // Reakce hry na úspěšné provedení příkazů
    static final String COMMAND_TAKE_DESC = "Vzal jsi do rukou objekt: ";
    static final String COMMAND_PUT_DESC = "Položil jsi objekt: ";
    static final String COMMAND_GOTO_DESC = "Přesunul ses do prostoru: ";
    static final String COMMAND_END_DESC = "Ukončil jsi hru.\n" +
                                     "Děkuji, že sis zahrál. Pro novou hru zmáčkni ENTER.";
    static final String COMMAND_WASH_DESC = "Umyl jsi objekt: ";
    static final String COMMAND_WATER_DESC = "Zalil jsi objekt: ";
    static final String COMMAND_FEED_DESC = "Nakrmil jsi objekt: ";

    // Popisy prostorů po přesunutí se do nich
    static final String BEDROOM_DESC =
            "Zde se náchází tvůj stůl s herním počítačem.\n" +
            "Na stole leží/ležel špinavý talíř.\n" +
            "Sousední prostory: " + HALLWAY_NAME;
    static final String HALLWAY_DESC =
            "Zde se nachází/nacházela hladová kočka Macík.\n" +
            "Sousední prostory: " +
                    BEDROOM_NAME + ", " +
                    BALCONY_NAME + ", " +
                    KITCHEN_NAME + ", " +
                    BATHROOM_NAME;
    static final String BATHROOM_DESC =
            "Zde na okně stojí/stála kytka monstera.\n" +
            "Sousední prostory: " + HALLWAY_NAME;
    static final String SINK_DESC =
            "Sem můžeš pokládat předměty a mýt je\n" +
            "příkazem Umyj (název předmětu).\n" +
            "Sousední prostory: " + KITCHEN_NAME;
    static final String BALCONY_DESC =
            "Zde se nachází/nacházela kytka lilie.\n" +
            "Sousední prostory: " + HALLWAY_NAME;
    static final String KITCHEN_DESC =
            "Zde na stole je/byla špinavá miska a nůž.\n" +
            "Sousední prostory: " + SINK_NAME + ", " + HALLWAY_NAME;

    // Reakce hry na špatně provedené příkazy
    static final String ERR_EMPTY_COMMAND_GAME_RUNNING =
            "Prázdný příkaz lze použít pouze pro start hry. Hra už běží.";
    static final String ERR_START_COMMAND_NOT_EMPTY =
            "Prvním příkazem není startovací příkaz.\n" +
                    "Hru, která neběží, lze spustit pouze startovacím\n" +
                    "příkazem, který je prázdný řetězec.\n";
    static final String ERR_UNKNOWN_COMMAND = "Neznámý příkaz: ";
    static final String ERR_MOVE_NO_ARG =
            "Nevím, kam chceš jít\n" +
                    "Nebylo zadáno jméno prostoru.";
    static final String ERR_TAKE_NO_ARG =
            "Nevím, co chceš zvednout.\n" +
                    "Nebylo zadáno jméno předmětu.";
    static final String ERR_PUT_NO_ARG =
            "Nevím, co chceš položit.\n" +
                    "Nebylo zadáno jméno předmětu.";
    static final String ERR_BAD_NEIGHBOUR =
            "Do zadaného prostoru se odtud nedá jít: ";
    static final String ERR_BAD_ITEM = "Zadaný předmět v prostoru není: ";
    static final String ERR_UNMOVABLE = "Zadaný předmět nelze zvednout: ";
    static final String ERR_BAG_FULL = "Zadaný předmět už neuneseš: ";
    static final String ERR_NOT_IN_BAG = "Zadaný předmět nedržíš v rukou: ";
    static final String ERR_FEEDABLE_NOT_PRESENT =
            "V daném prostoru se nenachází nakrmitelný objekt.";
    static final String ERR_WASH_COMMAND_NOT_IN_SINK =
            "V tomto prostoru nemůžeš umýt objekt: ";
    static final String ERR_WASH_NO_ARG = "Nevím, co mám umýt.";
    static final String ERR_WATER_NO_ARG = "Nevím, co mám zalít.";
    static final String ERR_FEEDED_NOT_PRESENT =
            "V prostoru se nenachází nakrmená kočka.";
    static final String ERR_FEED_NOT_WASHED_WATERED =
            "Není umyté nádobí a zalité kytky.\n" +
                    "Objekt nelze nakrmit: ";
    static final String ERR_CAT_NOT_FEEDED = "Kočka ještě nebyla nakrmena.";
    static final String ERR_UNWATERABLE = "Objekt nelze zalít: ";
    static final String ERR_UNWASHABLE = "Objekt nelze umýt: ";
    static final String ERR_ALREADY_WASHED = "Objekt byl již umyt: ";
    static final String ERR_ALREADY_WATERED = "Objekt byl již zalit: ";
    static final String ERR_ALREADY_FEEDED = "Objekt byl již nakrmen: ";

    /** Společný startovní krok všech scénářů. */
    private static final ScenarioStep START_STEP = new ScenarioStep(
            tsSTART,
            "",
            GAME_START_TEXT,
            BEDROOM_NAME,                // Aktuální prostor
            new String[] {HALLWAY_NAME},      // Sousední prostory
            new String[] {PLATE_NAME, TABLE_NAME},   // Objekty v prostoru
            new String[] {},              // Objekty v batohu
            null,                         // Potřebné příznaky
            List.of(                      // Názvy testovacích funkcí
                    "argumentPresent",
                    "feedablePresent",
                    "feededPresent",
                    "argumentWashable",
                    "argumentWaterable",
                    "allWashedWatered",
                    "placeIsSink"
            ),
            Map.of(                      // Názvy příznaků a výchozí hodnoty
                    "cat.feeded", false,
                    "plate.washed", false,
                    "bowl.washed", false,
                    "knife.washed", false,
                    "lily.watered", false,
                    "monstera.watered", false,
                    "washable", List.of(PLATE_NAME.toLowerCase(),
                            BOWL_NAME.toLowerCase(), KNIFE_NAME.toLowerCase()),
                    "waterable", List.of(MONSTERA_NAME.toLowerCase(),
                            LILY_NAME.toLowerCase()),
                    "feedable", List.of(CAT_NAME.toLowerCase())
            )
    );

    /** Šťastný scénář */
    private static final Scenario HAPPY = new Scenario(scHAPPY,
            START_STEP,
            new ScenarioStep(1, tsTAKE,
                    COMMAND_TAKE + " " + PLATE_NAME,
                    COMMAND_TAKE_DESC + PLATE_NAME,
                    BEDROOM_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {TABLE_NAME},        // Objekty v prostoru
                    new String[] {PLATE_NAME}        // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + HALLWAY_NAME,
                    COMMAND_GOTO_DESC + HALLWAY_NAME + "\n" +
                    HALLWAY_DESC,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME},       // Objekty v prostoru
                    new String[] {PLATE_NAME}       // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + KITCHEN_NAME,
                    COMMAND_GOTO_DESC + KITCHEN_NAME + "\n" +
                    KITCHEN_DESC,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    // Objekty v prostoru
                    new String[] {BOWL_NAME, KNIFE_NAME, TABLE_NAME},
                    new String[] {PLATE_NAME}        // Objekty v batohu
            ),
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + BOWL_NAME,
                    COMMAND_TAKE_DESC + BOWL_NAME,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {KNIFE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {PLATE_NAME, BOWL_NAME} // Objekty v batohu
            ),
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + KNIFE_NAME,
                    COMMAND_TAKE_DESC + KNIFE_NAME,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {TABLE_NAME},            // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {PLATE_NAME, BOWL_NAME, KNIFE_NAME}
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + SINK_NAME,
                    COMMAND_GOTO_DESC + SINK_NAME + "\n" +
                    SINK_DESC,
                    SINK_NAME,                // Aktuální prostor
                    new String[] {KITCHEN_NAME},  // Sousední prostory
                    new String[] {},           // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {PLATE_NAME, BOWL_NAME, KNIFE_NAME}
            ),
            new ScenarioStep(tsPUT_DOWN,
                    COMMAND_PUT + " " + PLATE_NAME,
                    COMMAND_PUT_DESC + PLATE_NAME,
                    SINK_NAME,                    // Aktuální prostor
                    new String[] {KITCHEN_NAME},       // Sousední prostory
                    new String[] {PLATE_NAME},         // Objekty v prostoru
                    new String[] {KNIFE_NAME, BOWL_NAME} // Objekty v batohu
            ),
            new ScenarioStep(tsPUT_DOWN,
                    COMMAND_PUT + " " + BOWL_NAME,
                    COMMAND_PUT_DESC + BOWL_NAME,
                    SINK_NAME,                      // Aktuální prostor
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME},  // Objekty v prostoru
                    new String[] {KNIFE_NAME}              // Objekty v batohu
            ),
            new ScenarioStep(tsPUT_DOWN,
                    COMMAND_PUT + " " + KNIFE_NAME,
                    COMMAND_PUT_DESC + KNIFE_NAME,
                    SINK_NAME,                      // Aktuální prostor
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    // Objekty v prostoru
                    new String[] {PLATE_NAME, BOWL_NAME, KNIFE_NAME},
                    new String[] {}                   // Objekty v batohu
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WASH + " " + PLATE_NAME,
                    COMMAND_WASH_DESC + PLATE_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    // Objekty v prostoru
                    new String[] {PLATE_NAME, BOWL_NAME, KNIFE_NAME},
                    new String[] {},                   // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "plate.washed", false  // Talíř ještě neumyt
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWashable",
                            "placeIsSink"
                    ),
                    Map.of("plate.washed", true)
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WASH + " " + BOWL_NAME,
                    COMMAND_WASH_DESC + BOWL_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    // Objekty v prostoru
                    new String[] {PLATE_NAME, BOWL_NAME, KNIFE_NAME},
                    new String[] {},                   // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "bowl.washed", false  // Talíř ještě neumyt
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWashable",
                            "placeIsSink"
                    ),
                    Map.of("bowl.washed", true)
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WASH + " " + KNIFE_NAME,
                    COMMAND_WASH_DESC + KNIFE_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    // Objekty v prostoru
                    new String[] {PLATE_NAME, BOWL_NAME, KNIFE_NAME},
                    new String[] {},                   // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "knife.washed", false  // Talíř ještě neumyt
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWashable",
                            "placeIsSink"
                    ),
                    Map.of("knife.washed", true)
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + KITCHEN_NAME,
                    COMMAND_GOTO_DESC + KITCHEN_NAME + "\n" +
                    KITCHEN_DESC,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {TABLE_NAME},         // Objekty v prostoru
                    new String[] {}                 // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + HALLWAY_NAME,
                    COMMAND_GOTO_DESC + HALLWAY_NAME + "\n" +
                    HALLWAY_DESC,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME},            // Objekty v prostoru
                    new String[] {}                 // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + BATHROOM_NAME,
                    COMMAND_GOTO_DESC + BATHROOM_NAME + "\n" +
                    BATHROOM_DESC,
                    BATHROOM_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},       // Sousední prostory
                    new String[] {MONSTERA_NAME},         // Objekty v prostoru
                    new String[] {}                 // Objekty v batohu
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WATER + " " + MONSTERA_NAME,
                    COMMAND_WATER_DESC + MONSTERA_NAME,
                    BATHROOM_NAME,
                    new String[] {HALLWAY_NAME},            // Sousední prostory
                    new String[] {MONSTERA_NAME},          // Objekty v prostoru
                    new String[] {},                    // Objekty v batohu
                    Map.of(                              // Potřebné příznaky
                            "monstera.watered", false    // Monstera nezalita
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWaterable"
                    ),
                    Map.of("monstera.watered", true)
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + HALLWAY_NAME,
                    COMMAND_GOTO_DESC + HALLWAY_NAME + "\n" +
                    HALLWAY_DESC,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME},              // Objekty v prostoru
                    new String[] {}                     // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + BALCONY_NAME,
                    COMMAND_GOTO_DESC + BALCONY_NAME + "\n" +
                    BALCONY_DESC,
                    BALCONY_NAME,                        // Aktuální prostor
                    new String[] {HALLWAY_NAME},         // Sousední prostory
                    new String[] {LILY_NAME},            // Objekty v prostoru
                    new String[] {}     // Objekty v batohu
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WATER + " " + LILY_NAME,
                    COMMAND_WATER_DESC + LILY_NAME,
                    BALCONY_NAME,
                    new String[] {HALLWAY_NAME},        // Sousední prostory
                    new String[] {LILY_NAME},              // Objekty v prostoru
                    new String[] {},                      // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "lily.watered", false       // Lilie nezalita
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWaterable"
                    ),
                    Map.of("lily.watered", true)
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + HALLWAY_NAME,
                    COMMAND_GOTO_DESC + HALLWAY_NAME + "\n" +
                            HALLWAY_DESC,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME},              // Objekty v prostoru
                    new String[] {}                     // Objekty v batohu
            ),
            new ScenarioStep(tsNS_0,
                    COMMAND_FEED,
                    COMMAND_FEED_DESC + CAT_NAME,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME},              // Objekty v prostoru
                    new String[] {},                     // Objekty v batohu
                    Map.of("cat.feeded", false),
                    List.of(                    // Testovací funkce
                        "feedablePresent",
                        "allWashedWatered"
                    ),
                    Map.of("cat.feeded", true)
            ),
            new ScenarioStep(tsSUCCESS,
                    COMMAND_WIN,
                    GAME_WIN_TEXT,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME},              // Objekty v prostoru
                    new String[] {},                     // Objekty v batohu
                    Map.of("cat.feeded", true),
                    List.of("feededPresent"),
                    null
            )
    );

    /** Basic scénář */
    public static final Scenario BASIC = new Scenario(scBASIC,
            START_STEP,
            new ScenarioStep(HAPPY.steps().get(1)),
            new ScenarioStep(HAPPY.steps().get(2)),
            new ScenarioStep(tsPUT_DOWN,
                    COMMAND_PUT + " " + PLATE_NAME,
                    COMMAND_PUT_DESC + PLATE_NAME,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME, PLATE_NAME},  // Objekty v prostoru
                    new String[] {}                  // Objekty v batohu
            ),
            new ScenarioStep(tsHELP,
                    COMMAND_HELP,
                    HELP_TEXT,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME, PLATE_NAME},  // Objekty v prostoru
                    new String[] {}                  // Objekty v batohu
            ),
            new ScenarioStep(tsEND,
                    COMMAND_END,
                    COMMAND_END_DESC,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME, PLATE_NAME},  // Objekty v prostoru
                    new String[] {}                  // Objekty v batohu
            )
    );

    /** Mistakes scénář */
    private static final Scenario MISTAKES = new Scenario(scMISTAKES,
            new ScenarioStep(-1, tsNOT_START, "Start",
                    ERR_START_COMMAND_NOT_EMPTY,
                    "",
                    new String[] {},
                    new String[] {},
                    new String[] {}
            ),
            START_STEP,
            new ScenarioStep(1, tsEMPTY, "",
                    ERR_EMPTY_COMMAND_GAME_RUNNING,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}               // Objekty v batohu
            ),
            new ScenarioStep(tsUNKNOWN, "Něco",
                    ERR_UNKNOWN_COMMAND + "Něco",
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}               // Objekty v batohu
            ),
            new ScenarioStep(tsMOVE_WA, "Jdi",
                    ERR_MOVE_NO_ARG,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}               // Objekty v batohu
            ),
            new ScenarioStep(tsTAKE_WA, "Vezmi",
                    ERR_TAKE_NO_ARG,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}               // Objekty v batohu
            ),
            new ScenarioStep(tsPUT_DOWN_WA, "Polož",
                    ERR_PUT_NO_ARG,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}               // Objekty v batohu
            ),
            new ScenarioStep(tsBAD_NEIGHBOR, COMMAND_GOTO + " " +
                    "Les",
                    ERR_BAD_NEIGHBOUR + "Les",
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}               // Objekty v batohu
            ),
            new ScenarioStep(tsBAD_ITEM, COMMAND_TAKE + " " + "Auto",
                    ERR_BAD_ITEM + "Auto",
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}               // Objekty v batohu
            ),
            new ScenarioStep(tsUNMOVABLE, COMMAND_TAKE + " " +
                    TABLE_NAME,
                    ERR_UNMOVABLE + TABLE_NAME,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}               // Objekty v batohu
            ),
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + PLATE_NAME,
                    COMMAND_TAKE_DESC + PLATE_NAME,
                    BEDROOM_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {TABLE_NAME},        // Objekty v prostoru
                    new String[] {PLATE_NAME}        // Objekty v batohu
            ),
            new ScenarioStep(HAPPY.steps().get(2)),  // Jdi na chodbu
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + CAT_NAME,
                    COMMAND_TAKE_DESC + CAT_NAME,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {},              // Objekty v prostoru
                    new String[] {PLATE_NAME, CAT_NAME}   // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + KITCHEN_NAME,
                    COMMAND_GOTO_DESC + KITCHEN_NAME + "\n" +
                            KITCHEN_DESC,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    // Objekty v prostoru
                    new String[] {BOWL_NAME, KNIFE_NAME, TABLE_NAME},
                    new String[] {PLATE_NAME, CAT_NAME}  // Objekty v batohu
            ),
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + BOWL_NAME,
                    COMMAND_TAKE_DESC + BOWL_NAME,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {KNIFE_NAME, TABLE_NAME}, // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {PLATE_NAME, BOWL_NAME, CAT_NAME}
            ),
            new ScenarioStep(tsBAG_FULL,
                    COMMAND_TAKE + " " + KNIFE_NAME,
                    ERR_BAG_FULL + KNIFE_NAME,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {KNIFE_NAME, TABLE_NAME}, // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {PLATE_NAME, BOWL_NAME, CAT_NAME}
            ),
            new ScenarioStep(tsNOT_IN_BAG,
                    COMMAND_PUT + " " + KNIFE_NAME,
                    ERR_NOT_IN_BAG + KNIFE_NAME,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {KNIFE_NAME, TABLE_NAME}, // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {PLATE_NAME, BOWL_NAME, CAT_NAME}
            ),
            new ScenarioStep(tsHELP,
                    COMMAND_HELP,
                    HELP_TEXT,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {KNIFE_NAME, TABLE_NAME}, // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {PLATE_NAME, BOWL_NAME, CAT_NAME}
            ),
            new ScenarioStep(tsEND,
                    COMMAND_END,
                    COMMAND_END_DESC,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {KNIFE_NAME, TABLE_NAME}, // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {PLATE_NAME, BOWL_NAME, CAT_NAME}
            )
    );

/** Mistakes_NS scénář */
    public static final Scenario MISTAKES_NS = new Scenario(scMISTAKES_NS,
            START_STEP,
            new ScenarioStep(1, tsNS0_WrongCond,
                    COMMAND_FEED,
                    ERR_FEEDABLE_NOT_PRESENT,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {},              // Objekty v batohu
                    null,
                    List.of("feedablePresent"),
                    null
            ),
            new ScenarioStep(tsNS1_WrongCond,
                    COMMAND_WATER + " " + MONSTERA_NAME,
                    ERR_BAD_ITEM + MONSTERA_NAME,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {},              // Objekty v batohu
                    null,
                    List.of("argumentPresent"),
                    null
            ),
            new ScenarioStep(tsNS1_WrongCond,
                    COMMAND_WASH + " " + BOWL_NAME,
                    ERR_BAD_ITEM + BOWL_NAME,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {},              // Objekty v batohu
                    null,
                    List.of("argumentPresent"),
                    null
            ),
            new ScenarioStep(tsNS1_WrongCond,
                    COMMAND_WASH + " " + PLATE_NAME,
                    ERR_WASH_COMMAND_NOT_IN_SINK + PLATE_NAME,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {},              // Objekty v batohu
                    null,
                    List.of("placeIsSink"),
                    null
            ),
            new ScenarioStep(tsNS1_0Args,
                    COMMAND_WASH,
                    ERR_WASH_NO_ARG,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},        // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}                  // Objekty v batohu
            ),
            new ScenarioStep(tsNS1_0Args,
                    COMMAND_WATER,
                    ERR_WATER_NO_ARG,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},        // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {}                  // Objekty v batohu
            ),
            new ScenarioStep(tsNOT_SUCCESS,
                    COMMAND_WIN,
                    ERR_FEEDED_NOT_PRESENT,
                    BEDROOM_NAME,
                    new String[] {HALLWAY_NAME},          // Sousední prostory
                    new String[] {PLATE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {},                  // Objekty v batohu
                    null,
                    List.of("feededPresent"),
                    null
            ),
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + PLATE_NAME,
                    COMMAND_TAKE_DESC + PLATE_NAME,
                    BEDROOM_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},      // Sousední prostory
                    new String[] {TABLE_NAME},        // Objekty v prostoru
                    new String[] {PLATE_NAME}        // Objekty v batohu
            ),
            new ScenarioStep(HAPPY.steps().get(2)),      // Jdi na chodbu
            new ScenarioStep(tsNS0_WrongCond,
                    COMMAND_FEED,
                    ERR_FEED_NOT_WASHED_WATERED,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME},              // Objekty v prostoru
                    new String[] {PLATE_NAME},              // Objekty v batohu
                    null,
                    List.of("allWashedWatered"),      // Testovací funkce
                    null
            ),
            new ScenarioStep(tsNOT_SUCCESS,
                    COMMAND_WIN,
                    ERR_CAT_NOT_FEEDED,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME},              // Objekty v prostoru
                    new String[] {PLATE_NAME},              // Objekty v batohu
                    null,
                    List.of(                    // Testovací funkce
                            "feededPresent"
                    ),
                    null
            ),
            new ScenarioStep(tsNS1_WrongCond,
                    COMMAND_WATER + " " + CAT_NAME,
                    ERR_UNWATERABLE + CAT_NAME,
                    HALLWAY_NAME,
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {CAT_NAME},   // Objekty v prostoru
                    new String[] {PLATE_NAME},     // Objekty v batohu
                    null,
                    List.of(                    // Testovací funkce
                            "argumentWaterable"
                    ),
                    null
            ),
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + CAT_NAME,
                    COMMAND_TAKE_DESC + CAT_NAME,
                    HALLWAY_NAME,
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {},   // Objekty v prostoru
                    new String[] {PLATE_NAME, CAT_NAME}     // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + KITCHEN_NAME,
                    COMMAND_GOTO_DESC + KITCHEN_NAME + "\n" +
                    KITCHEN_DESC,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    // Objekty v prostoru
                    new String[] {BOWL_NAME, KNIFE_NAME, TABLE_NAME},
                    new String[] {PLATE_NAME, CAT_NAME}     // Objekty v batohu
            ),
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + BOWL_NAME,
                    COMMAND_TAKE_DESC + BOWL_NAME,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {KNIFE_NAME, TABLE_NAME}, // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {PLATE_NAME, BOWL_NAME, CAT_NAME} 
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + SINK_NAME,
                    COMMAND_GOTO_DESC + SINK_NAME + "\n" +
                    SINK_DESC,
                    SINK_NAME,                // Aktuální prostor
                    new String[] {KITCHEN_NAME},  // Sousední prostory
                    new String[] {},           // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {PLATE_NAME, BOWL_NAME, CAT_NAME}
            ),
            new ScenarioStep(tsPUT_DOWN,
                    COMMAND_PUT + " " + PLATE_NAME,
                    COMMAND_PUT_DESC + PLATE_NAME,
                    SINK_NAME,                    // Aktuální prostor
                    new String[] {KITCHEN_NAME},       // Sousední prostory
                    new String[] {PLATE_NAME},         // Objekty v prostoru
                    new String[] {CAT_NAME, BOWL_NAME} // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + KITCHEN_NAME,
                    COMMAND_GOTO_DESC + KITCHEN_NAME + "\n" +
                            KITCHEN_DESC,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {KNIFE_NAME, TABLE_NAME}, // Objekty v prostoru
                    new String[] {CAT_NAME, BOWL_NAME}      // Objekty v batohu
            ),
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + KNIFE_NAME,
                    COMMAND_TAKE_DESC + KNIFE_NAME,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {TABLE_NAME},            // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {CAT_NAME, BOWL_NAME, KNIFE_NAME} 
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + SINK_NAME,
                    COMMAND_GOTO_DESC + SINK_NAME + "\n" +
                            SINK_DESC,
                    SINK_NAME,                // Aktuální prostor
                    new String[] {KITCHEN_NAME},   // Sousední prostory
                    new String[] {PLATE_NAME},     // Objekty v prostoru
                    // Objekty v batohu
                    new String[] {CAT_NAME, BOWL_NAME, KNIFE_NAME} 
            ),
            new ScenarioStep(tsPUT_DOWN,
                    COMMAND_PUT + " " + BOWL_NAME,
                    COMMAND_PUT_DESC + BOWL_NAME,
                    SINK_NAME,                      // Aktuální prostor
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME},  // Objekty v prostoru
                    new String[] {KNIFE_NAME, CAT_NAME}     // Objekty v batohu
            ),
            new ScenarioStep(tsPUT_DOWN,
                    COMMAND_PUT + " " + KNIFE_NAME,
                    COMMAND_PUT_DESC + KNIFE_NAME,
                    SINK_NAME,                      // Aktuální prostor
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    // Objekty v prostoru
                    new String[] {PLATE_NAME, BOWL_NAME,KNIFE_NAME},
                    new String[] {CAT_NAME}           // Objekty v batohu
            ),
            new ScenarioStep(tsPUT_DOWN,
                    COMMAND_PUT + " " + CAT_NAME,
                    COMMAND_PUT_DESC + CAT_NAME,
                    SINK_NAME,                      // Aktuální prostor
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME,
                            KNIFE_NAME, CAT_NAME}, // Objekty v prostoru
                    new String[] {}           // Objekty v batohu
            ),
            new ScenarioStep(tsNS1_WrongCond,
                    COMMAND_WASH + " " + CAT_NAME,
                    ERR_UNWASHABLE + CAT_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME,  // Objekty v prostoru
                            KNIFE_NAME, CAT_NAME},
                    new String[] {},                   // Objekty v batohu
                    null,
                    List.of("argumentWashable"),
                    null
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WASH + " " + PLATE_NAME,
                    COMMAND_WASH_DESC + PLATE_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME,  // Objekty v prostoru
                            KNIFE_NAME, CAT_NAME},
                    new String[] {},                   // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "plate.washed", false  // Talíř ještě neumyt
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWashable",
                            "placeIsSink"
                    ),
                    Map.of("plate.washed", true)
            ),
            new ScenarioStep(tsNS1_WrongCond,
                    COMMAND_WASH + " " + PLATE_NAME,
                    ERR_ALREADY_WASHED + PLATE_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME,  // Objekty v prostoru
                            KNIFE_NAME, CAT_NAME},
                    new String[] {},                   // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "plate.washed", false  // Talíř byl již umyt
                    ),
                    null,
                    null
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WASH + " " + BOWL_NAME,
                    COMMAND_WASH_DESC + BOWL_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME,  // Objekty v prostoru
                            KNIFE_NAME, CAT_NAME},
                    new String[] {},                   // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "bowl.washed", false  // Talíř ještě neumyt
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWashable",
                            "placeIsSink"
                    ),
                    Map.of("bowl.washed", true)
            ),
            new ScenarioStep(tsNS1_WrongCond,
                    COMMAND_WASH + " " + BOWL_NAME,
                    ERR_ALREADY_WASHED + BOWL_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME,  // Objekty v prostoru
                            KNIFE_NAME, CAT_NAME},
                    new String[] {},                   // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "bowl.washed", false  // Talíř byl již umyt
                    ),
                    null,
                    null
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WASH + " " + KNIFE_NAME,
                    COMMAND_WASH_DESC + KNIFE_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME,  // Objekty v prostoru
                            KNIFE_NAME, CAT_NAME},
                    new String[] {},                   // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "knife.washed", false  // Talíř ještě neumyt
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWashable",
                            "placeIsSink"
                    ),
                    Map.of("knife.washed", true)
            ),
            new ScenarioStep(tsNS1_WrongCond,
                    COMMAND_WASH + " " + KNIFE_NAME,
                    ERR_ALREADY_WASHED + KNIFE_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    new String[] {PLATE_NAME, BOWL_NAME,  // Objekty v prostoru
                            KNIFE_NAME, CAT_NAME},
                    new String[] {},                   // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "knife.washed", false  // Talíř byl již umyt
                    ),
                    null,
                    null
            ),
            new ScenarioStep(tsTAKE,
                    COMMAND_TAKE + " " + CAT_NAME,
                    COMMAND_TAKE_DESC + CAT_NAME,
                    SINK_NAME,
                    new String[] {KITCHEN_NAME},         // Sousední prostory
                    // Objekty v prostoru
                    new String[] {PLATE_NAME, BOWL_NAME, KNIFE_NAME},
                    new String[] {CAT_NAME}     // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + KITCHEN_NAME,
                    COMMAND_GOTO_DESC + KITCHEN_NAME + "\n" +
                    KITCHEN_DESC,
                    KITCHEN_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME,SINK_NAME}, // Sousední prostory
                    new String[] {TABLE_NAME},// Objekty v prostoru
                    new String[] {CAT_NAME}        // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + HALLWAY_NAME,
                    COMMAND_GOTO_DESC + HALLWAY_NAME + "\n" +
                    HALLWAY_DESC,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {},              // Objekty v prostoru
                    new String[] {CAT_NAME}        // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + BATHROOM_NAME,
                    COMMAND_GOTO_DESC + BATHROOM_NAME + "\n" +
                    BATHROOM_DESC,
                    BATHROOM_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},       // Sousední prostory
                    new String[] {MONSTERA_NAME},        // Objekty v prostoru
                    new String[] {CAT_NAME}           // Objekty v batohu
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WATER + " " + MONSTERA_NAME,
                    COMMAND_WATER_DESC + MONSTERA_NAME,
                    BATHROOM_NAME,
                    new String[] {HALLWAY_NAME},            // Sousední prostory
                    new String[] {MONSTERA_NAME},          // Objekty v prostoru
                    new String[] {CAT_NAME},              // Objekty v batohu
                    Map.of(                              // Potřebné příznaky
                            "monstera.watered", false    // Monstera nezalita
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWaterable"
                    ),
                    Map.of("monstera.watered", true)
            ),
            new ScenarioStep(tsNS1_WrongCond,
                    COMMAND_WATER + " " + MONSTERA_NAME,
                    ERR_ALREADY_WATERED + MONSTERA_NAME,
                    BATHROOM_NAME,
                    new String[] {HALLWAY_NAME},            // Sousední prostory
                    new String[] {MONSTERA_NAME},          // Objekty v prostoru
                    new String[] {CAT_NAME},              // Objekty v batohu
                    Map.of(                              // Potřebné příznaky
                            "monstera.watered", false    // Monstera nezalita
                    ),
                    null,
                    null
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + HALLWAY_NAME,
                    COMMAND_GOTO_DESC + HALLWAY_NAME + "\n" +
                    HALLWAY_DESC,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {},              // Objekty v prostoru
                    new String[] {CAT_NAME}              // Objekty v batohu
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + BALCONY_NAME,
                    COMMAND_GOTO_DESC + BALCONY_NAME + "\n" +
                    BALCONY_DESC,
                    BALCONY_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},            // Sousední prostory
                    new String[] {LILY_NAME},             // Objekty v prostoru
                    new String[] {CAT_NAME}     // Objekty v batohu
            ),
            new ScenarioStep(tsNS_1,
                    COMMAND_WATER + " " + LILY_NAME,
                    COMMAND_WATER_DESC + LILY_NAME,
                    BALCONY_NAME,
                    new String[] {HALLWAY_NAME},        // Sousední prostory
                    new String[] {LILY_NAME},             // Objekty v prostoru
                    new String[] {CAT_NAME},             // Objekty v batohu
                    Map.of(                             // Potřebné příznaky
                            "lily.watered", false       // Lilie nezalita
                    ),
                    List.of(                    // Testovací funkce
                            "argumentPresent",
                            "argumentWaterable"
                    ),
                    Map.of("lily.watered", true)
            ),
            new ScenarioStep(tsNS1_WrongCond,
            COMMAND_WATER + " " + LILY_NAME,
            ERR_ALREADY_WATERED + LILY_NAME,
            BALCONY_NAME,
            new String[] {HALLWAY_NAME},        // Sousední prostory
            new String[] {LILY_NAME},               // Objekty v prostoru
            new String[] {CAT_NAME},             // Objekty v batohu
            Map.of(                             // Potřebné příznaky
                    "lily.watered", false       // Lilie nezalita
            ),
            null,
            null
            ),
            new ScenarioStep(tsPUT_DOWN,
                    COMMAND_PUT + " " + CAT_NAME,
                    COMMAND_PUT_DESC + CAT_NAME,
                    BALCONY_NAME,
                    new String[] {HALLWAY_NAME},        // Sousední prostory
                    new String[] {LILY_NAME, CAT_NAME},    // Objekty v prostoru
                    new String[] {}             // Objekty v batohu
            ),
            new ScenarioStep(tsNOT_SUCCESS,
                    COMMAND_WIN,
                    ERR_CAT_NOT_FEEDED,
                    BALCONY_NAME,
                    new String[] {                // Sousední prostory
                        HALLWAY_NAME
                    },
                    new String[] {CAT_NAME, LILY_NAME},   // Objekty v prostoru
                    new String[] {},              // Objekty v batohu
                    Map.of("cat.feeded", true),
                    null,
                    null
            ),
            new ScenarioStep(tsNS_0,
                    COMMAND_FEED,
                    COMMAND_FEED_DESC + CAT_NAME,
                    BALCONY_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},        // Sousední prostory
                    new String[] {CAT_NAME, LILY_NAME},   // Objekty v prostoru
                    new String[] {},                     // Objekty v batohu
                    null,
                    List.of(                    // Testovací funkce
                            "allWashedWatered"
                    ),
                    Map.of("cat.feeded", true)
            ),
            new ScenarioStep(tsNS0_WrongCond,
                    COMMAND_FEED,
                    ERR_ALREADY_FEEDED + CAT_NAME,
                    BALCONY_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},        // Sousední prostory
                    new String[] {CAT_NAME, LILY_NAME},   // Objekty v prostoru
                    new String[] {},                     // Objekty v batohu
                    Map.of(                              // Potřebné příznaky
                            "cat.feeded", false
                    ),
                    null,
                    null
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + HALLWAY_NAME,
                    COMMAND_GOTO_DESC + HALLWAY_NAME + "\n" +
                    HALLWAY_DESC,
                    HALLWAY_NAME,                // Aktuální prostor
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {},              // Objekty v prostoru
                    new String[] {}                     // Objekty v batohu
            ),
            new ScenarioStep(tsNOT_SUCCESS,
                    COMMAND_WIN,
                    ERR_FEEDED_NOT_PRESENT,
                    HALLWAY_NAME,
                    new String[] {                // Sousední prostory
                            BEDROOM_NAME,
                            BATHROOM_NAME,
                            BALCONY_NAME,
                            KITCHEN_NAME
                    },
                    new String[] {},   // Objekty v prostoru
                    new String[] {},              // Objekty v batohu
                    null,
                    List.of("feededPresent"),
                    null
            ),
            new ScenarioStep(tsGOTO,
                    COMMAND_GOTO + " " + BALCONY_NAME,
                    COMMAND_GOTO_DESC + BALCONY_NAME + "\n" +
                            BALCONY_DESC,
                    BALCONY_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},           // Sousední prostory
                    new String[] {LILY_NAME, CAT_NAME},   // Objekty v prostoru
                    new String[] {}     // Objekty v batohu
            ),
            new ScenarioStep(tsSUCCESS,
                    COMMAND_WIN,
                    GAME_WIN_TEXT,
                    BALCONY_NAME,                // Aktuální prostor
                    new String[] {HALLWAY_NAME},
                    new String[] {CAT_NAME, LILY_NAME},    // Objekty v prostoru
                    new String[] {},                     // Objekty v batohu
                    Map.of("cat.feeded", true),
                    List.of("feededPresent"),
                    null
            )
    );

    // CLASS METHODS ///////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí seznam definovaných základních scénářů.
     *
     * @return Seznam základních scénářů
     */
    public static List<Scenario> scenarios() {
        return List.of(HAPPY, BASIC, MISTAKES, MISTAKES_NS);
    }
}
