package cz.vse.java.kedv00.adventura.testers;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/testers/ScenarioTester.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/

import cz.vse.java.kedv00.adventura.api.*;

import java.util.*;
import java.util.stream.Collectors;

import static cz.vse.java.kedv00.adventura.api.TypeOfStep.*;
import static cz.vse.java.kedv00.adventura.api.TypeOfStep.Subtype.stNONSTANDARD;
import static cz.vse.java.kedv00.adventura.testers.util.ConditionalPrinter.*;
import static cz.vse.java.kedv00.adventura.testers.util.FormatStrings.*;
import static cz.vse.java.kedv00.adventura.testers.util.Util.lineWrapper;
import static cz.vse.java.kedv00.adventura.testers.util.Util.z2;


/*******************************************************************************
 * Instance třídy <b>{@code ScenarioTester}</b> testují scénáře hry,
 * tj. prověřují, nakolik scénář odpovídá požadavkům na něj kladeným.
 * Kontrolují zejména vzájemnou konzistenci jednotlivých kroků scénářů,
 * a to včetně křížové konzistence kroků z různých scénářů.
 * Souhrn výsledků předávají volajícímu objektu
 * v přepravce typu {@link ScenariosSummary}.
 * Testují však pouze obsah scénářů,
 * tj. nepokouší se aplikovat scénáře na hru a testovat tak hru.
 * To mají na starosti instance potomků třídy
 * {@link ATester}.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class ScenarioTester
     extends ATester
{
//\CC== CLASS CONSTANTS (CONSTANT CLASS/STATIC ATTRIBUTES/FIELDS) ==============

    /** Maximální povolené délka zalamovaných výstupních řádků. */
    private final int LINE_LENGTH = 80;

    /** Komparátor stringů neberoucí v úvahu velikost písmen. */
    private static final Comparator<String> CIC = String::compareToIgnoreCase;

    /** Seznam jednoparametrických akcí vyžadujících,
     *  aby odpověď hry končila názvem argumentu. */
    private static final List<TypeOfStep> ArgumentAtEnd =
                         List.of(tsTAKE, tsPUT_DOWN, tsNS_1);

    /** ???. */
    private static final List<TypeOfStep> verifyNeedsWrong =
                         List.of(tsNS0_WrongCond, tsNS1_WrongCond,
                                 tsNS2_WrongCond, tsNS3_WrongCond);


//\CV== CLASS VARIABLES (VARIABLE CLASS/STATIC ATTRIBUTES/FIELDS) ==============



//##############################################################################
//\CM== CLASS (STATIC) REMAINING NON-PRIVATE METHODS ===========================

    /***************************************************************************
     * Otestuje všechny scénáře zadané továrny a vrátí přepravku
     * se souhrnnou informací o základních charakteristikách hry
     * odvozenou z testovaných scénářů.
     *
     * @param portal Tovární objekt, jehož scénáře se budou testovat
     * @return Část závěrečného hodnocení testu
     */
    @SuppressWarnings({"empty-statement"})
    static String testScenariosFrom(IPortal portal)
    {
        int            numScen   = LEVEL.minScenarios();
        List<Scenario> scenarios = portal.scenarios().subList(0, numScen);
        boolean[]      results   = new boolean[numScen];
        ScenarioTester tester    = new ScenarioTester(portal);
        boolean        ok        = true;
        tester.happyTested       = false;
        tester.requiredScenarios();

        int i = 0;
        StringBuilder sb = new StringBuilder(HASHES_N + portal.authorString()
                             + "\nTesty scénářů dopadly následovně:\n");
        for (Scenario scenario : scenarios) {
            boolean result = tester.verify(scenario);
            results[i++]   = result;
            ok &= result;
            sb.append(scenario.name() + ": "
                    + (result ? "OK\n" : "NEPROŠEL\n"));
        }
        sb.append(DOUBLELINE_N);

        Scenario           happy  = portal.scenarios().get(0);
        List<ScenarioStep> hsteps = happy.steps();
        ScenarioStep       start  = hsteps.get(0);
        ScenarioStep       stop   = hsteps.get(hsteps.size()-1);

        BasicActions ba;
        if (LEVEL.compareTo(Level.SCENARIOS) < 0) {
            ba = null;
        }
        else {
            ba = tester.deriveBasicActions(LEVEL);
        }
        SCENARIOS_SUMMARY = new ScenariosSummary(
                  ok,
                  tester.allMentionedPlaces,
                  tester.allEnteredActions,
                  tester.allSeenItems,
                  start, stop,
                  ba,
                  tester.nsActionsByArgs,
                  tester.name2typeGroup,
                  scenarios,
                  results);
        return sb.toString();
    }



//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    //-- Následující konstanty si instance pamatuje mezi jednotlivými testy
    //   scénářů a používá je ke kontrole duplicit -----------------------------

    /** Mapa uchovávající typ každé zadané akce.
     *  Slouží ke kontrole konzistentnosti používání příkazů,
     *  tj. že nebude stejná akce omylem použita
     * v navzájem nekonzistentních typech kroků.
     *  Názvy akcí jsou převedeny na velká písmena. */
    private final Map<String, TypeOfStep> action2type = new HashMap<>();

    /** Mapa převádějící typ povinného kroku na název akce,
     *  kterou krok realizuje.
     *  Slouží ke kontrole konzistentnosti používání akcí,
     *  tj. že pro daný typ standardního kroku nebudou použité různé příkazy.
     *  Názvy příkazů jsou převedeny na velká písmena. */
    private final Map<TypeOfStep, String> type2name =
                                          new EnumMap<>(TypeOfStep.class);

    /** Mapa mapující názvy příkazů na skupinu jejich typu. */
    private final Map<String, TypeOfStep> name2typeGroup = new HashMap<>();

    /** Množina všech nestandardních akcí. */
    private final Set<String> enteredNSActions = new TreeSet<>();

    /** Seznam množin názvů nestandardních akcí rozdělených dle počtu argumentů,
     * přičemž index seznamu indikuje počet argumentů dané nestandardní akce. */
    private final List<Set<String>> nsActionsByArgs =
                                    List.of(new HashSet<>(), new HashSet<>(),
                                            new HashSet<>(), new HashSet<>());


    //-- Texty zapamatované ze všech scénářů testovaných touto instancí --------

        /** Množina akcí použitých v dosud prověřených scénářích. */
        private final Set<String> allEnteredActions = new TreeSet<>();

        /** Množina názvů prostorů zmíněných v dosud prověřených scénářích
         *  (např. jako sousedé). */
        private final Set<String> allMentionedPlaces = new TreeSet<>();

        /** Množina názvů objektů zmíněných v navštívených či
         * zmíněných prostorech v dosud prověřených scénářích. */
        private final Set<String> allSeenItems = new TreeSet<>();

        /** Možina všech požadovaných testů pro nestandardní kroky */
        private final Set<String> allNonStandardTests = initializeNonStandardTests();


    //-- Kolekce dat zapamatovaných pro testovaný scénář -----------------------

        /** Množina akcí použitých v testu. */
        private final Set<String> enteredActions = new TreeSet<>();

        /** Množina názvů navštívených prostorů. */
        private final Set<String> visitedPlaces = new TreeSet<>();

        /** Množina názvů prostorů zmíněných ve scénáři (např. jako sousedé). */
        private final Set<String> mentionedPlaces = new TreeSet<>();

        /** Množina použitých objektů. */
        private final Set<String> seenItems = new TreeSet<>();

        /** Aktuální nastavení dodatečných příznaků. */
        private final Map<String, Object> flags = new HashMap<>();



//\IV== INSTANCE VARIABLES (VARIABLE INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Testovaný scénář. */
    private Scenario scenarioInTest;

    /** Příznak toho, že šťastný scénář byl již testován,
     *  a proto je již řada proměnných inicializována. */
    private boolean happyTested = false;

    /** Označení testu zapisované do preambule a postambule. */
    private String descriptonOfScenarioInTest;
//
//    /** Přepravka s názvy základních (=povinných) příkazů. */
//    private BasicActions basicActions;

    /** Doposud nezrealizované typy akcí. */
    private Set<TypeOfStep> notUsedActionTypes;

    /** Počet doposud prověřených kroků scénáře. */
    private int numOfStep;

    /** Atribut, v němž si metody předávají informaci o tom,
     *  zda daný scénář vyhovuje požadavkům.
     *  Při testu každého scénáře je atribut inicializován v metodě
     *  {@link #initialization()}. */
    private boolean scenarioOK;


    //-- Proměnné použité pro test jednoho kroku -------------------------------

//        /** Příznak toho, že byl zadán startovací prázdný příkaz. */
//        private boolean startovacíAkce;

        /** {@code true} před startem prvního kroku, anebo byl-li již vydán
         *  příkaz k ukončení hry a nebyla ještě odstartována hra další. */
        private boolean finished;

        /** Posledně vyhodnocovaný krok scénáře. */
        private ScenarioStep previousStep;

        /** Aktuálně vyhodnocovaný krok scénáře. */
        private ScenarioStep currentStep;

        /** Jednotlivá slova příkazu v testovaném kroku scénáře. */
        private String[] wordsInCommand;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Vytvoří tester pro scénáře spravované správcem dodaným zadanou továrnou.
     *
     * @param portal Tovární objekt schopný dodat objekty,
     *                které se mají testovat
     */
    private ScenarioTester(IPortal portal)
    {
        super(portal);
    }



//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================

    /***************************************************************************
     * Vytiskne chybové hlášení a označí celý scénář za nevyhovující.
     *
     * @param format    Formát tisku chybového hlášení. Bude ještě doplněn
     *                  společnou úvodní a závěrečnou sekvencí.
     * @param arguments Případné další argumenty k tisku
     */
    public void scenERROR(String format, Object... arguments)
    {
        scenarioOK = false;
        ATester.ERRs(format + stepStates(), arguments);
    }


    /***************************************************************************
     * Prověří, že jsou k dispozici scénáře požadované na dané hladině testů.
     *
     * @throws TestException Chyba v testované aplikaci
     */
    private void requiredScenarios()
    {
        int minSc = Math.min(LEVEL.minScenarios(), 4);
        List<Scenario> scenarios = portal.scenarios();
        if (scenarios.size() < minSc) {
            throw new RuntimeException(
                "\nTovárna neposkytuje požadovaný počet scénářů ("
              + minSc + "), ale jen " + scenarios.size());
        }
        for (int i=0;   i < minSc;   i++) {
            Scenario scenario = scenarios.get(i);
            TypeOfScenario tos = TypeOfScenario.values()[i];
            if ((scenario.type().ordinal() != i)  ||
               ! scenario.name().equals(tos.requiredName()) )
            {
                throw new TestException("\n"
                    + "Továrna neposkytuje " + tos.description()
                    + " zařazený jako scénář s indexem " + i
                    + "\nPožadováno: tos=" + tos
                    +            ", name=" + tos.requiredName()
                    + "\nObdrženo:  type=" + scenario.type()
                    +            ", name=" + scenario.name());
            }
        }
    }


//    /***************************************************************************
//     * Základní test ověřující,
//     * jestli portál vyhovuje zadaným okrajovým podmínkám, tj. jestli:
//     * <ul>
//     *   <li>Umí vrátit správně naformátované jméno autora/autorky hry
//     *       a jeho/jejího identifikačního stringu.</li>
//     *   <li>Dodá požadované scénáře.</li>
//     *   <li>Základní úspěšný scénář má následující vlastnosti:
//     *     <ul>
//     *       <li>Startovní příkaz, jehož název je prázdný řetězec</li>
//     *       <li>Minimální požadovaný počet kroků</li>
//     *       <li>Minimální požadovaný počet navštívených místností</li>
//     *       <li>Minimální požadovaný počet "zahlédnutých" místností</li>
//     *       <li>Použití příkazů pro standardní přechod z prostoru do prostoru
//     *         zvednutí nějakého objektu a položení nějakého objektu</li>
//     *       <li>Použití předepsaných rozšiřujících akcí</li>
//     *     </ul>
//     *   </li>
//     *   <li>Základní chybový scénář má následující vlastnosti:
//     *     <ul>
//     *       <li>Startovní příkaz, jehož název je prázdný řetězec</li>
//     *       <li>Použití všech povinných chybových typů kroku
//     *         definovaných ve třídě {@link adv23s.api.TypeOfStep}</li>
//     *       <li>Vyvolání nápovědy</li>
//     *       <li>Ukončení příkazem pro nestandardní ukončení hry</li>
//     *     </ul>
//     *   </li>
//     * </ul>
//     *
//     * @return Vyhovuje-li, vrátí {@code true}, jinak vrátí {@code false}
//     */
//    private void verifyScenarios()
//    {
//        requiredScenarios();
//        scenariosSummary = ScenarioTester.testScenariosFrom(portal, level);
//        boolean ok = scenariosSummary.ok;
//
//        Date stopTime =  new Date();
//        long duration = (stopTime.getTime() - startTime.getTime());
//        message = String.format("The scenario test was %sSUCCESSFUL",
//                                (ok ? "" : "UN"));
//        message = String.format(
//            "########## STOP: %ta %<TF — %<tT\n  —  " +
//            "Test duration: %d ms – %s\n" +
//            "End of scenario test_ver from: %s" + N_HASHES_N,
//            stopTime, duration, message,
//            portal.authorString());
//        verboseMessageBuilder.append(message);
//        prln(message);
//        return scenariosSummary;
//    }



//\IP== INSTANCE PRIVATE AND AUXILIARY METHODS =================================

    /***************************************************************************
     * Vytvoří seznam obsahující všechny názvy ze zadaného pole (vektoru)
     * převedené na malá písmena.
     *
     * @param  names  Pole s převáděnými názvy
     * @return Seznam se zadanými názvy v malých písmenech
     */
    private List<? extends String> arr2lstInLC(String[] names)
    {
        List<String> list = new ArrayList<>(names.length);
        for (String name : names) {
            list.add(name.toLowerCase());
        }
        return list;
    }


    /***************************************************************************
     * Při procházení šťastného scénáře zaeviduje použité nestandardní kroky
     * a při procházení chybových scénářů dohlédne na to,
     * aby nebyly zadány nové nestandardní kroky (pravděpodobně překlepem).
     *
     * @param name Zadaný název akce
     */
    private void checkNonstandard(String name)
    {
        if ((scenarioInTest.type() == TypeOfScenario.scHAPPY)  &&
            ! happyTested)
        {
            if (currentStep.typeOfStep.subtype() == stNONSTANDARD)
            {
                if (! enteredNSActions.contains(name)) {
                    enteredNSActions.add(name);
                    int index = currentStep.typeOfStep.group()
                        .paramCount();
                    nsActionsByArgs.get(index).add(name);
                }
            }
        }
        //Jestli se v nonhappy scénáři nepoužívá nová nestandardní akce
        else if (scenarioInTest.type() == TypeOfScenario.scMISTAKES_NS) {
            Subtype st = currentStep.typeOfStep.subtype();
            if (((st == stNONSTANDARD) ||
                     (st == Subtype.stMISTAKE_NS)   )  &&
                    ! enteredNSActions.contains(name) &&
                    currentStep.typeOfStep != tsNOT_SUCCESS)
            {
                scenERROR("Testujete " + scenarioInTest.type().description()
                           + ".\nV něm mohou být jen nestandardní akce "
                           + "použité ve šťasném scénáři.\n"
                           + "Zadali jste nepoužitou akci " + name);
            }
        }
    }


    /***************************************************************************
     * Z hodnot uložených v mapě {@link #type2name} odvodí
     * názvy povinných akcí.
     *
     * @return Přepravka s názvy povinných akcí odvozených ze scénářů
     */
    private BasicActions deriveBasicActions(Level  level)
    {
        if (level.compareTo(Level.HAPPY) <= 0) {
            scenERROR("Na hladině " + level + " se názvy akcí neprověřují.");
        }
        String move = type2name.get(TypeOfStep.tsGOTO);
        String take = type2name.get(TypeOfStep.tsTAKE);
        String put  = type2name.get(TypeOfStep.tsPUT_DOWN);
        String help = type2name.get(TypeOfStep.tsHELP);
        String end  = type2name.get(TypeOfStep.tsEND);

        BasicActions result = new BasicActions(move, take, put, help, end);
        return result;
    }

    /***************************************************************************
     * Vrátí množinu názvů zmíněných, avšak nenavštívených místností.
     *
     * @return Množina názvů nenavštívených místností
     */
    @SuppressWarnings("AssignmentToForLoopParameter")
    private Set<String> discoverNotVisitedPlaces()
    {
        Set<String> notVisited = new LinkedHashSet<>();
        mentionedPlaces.stream().
                map    ((s) -> s.toLowerCase()).
                filter ((s) -> (!visitedPlaces.contains(s))).
                forEach((s) -> {notVisited.add(s);});
        return notVisited;
    }


    /***************************************************************************
     * Definuje úplný název testu sestavený z názvy typu kroku,
     * názvů testované akce a názvu testovaného příznaku či kódu.
     *
     * @param tos       Typ kroku
     * @param action    Název testované akce
     * @param test      Název testovaného příznaku či kódu
     * @return Kompletovaný název testu
     */
    private String fullNameOfNonStandardTest(TypeOfStep tos, String action, String test)
    {
        return (tos.toString() + '|' +  action + '|' + test);
    }


    /***************************************************************************
     * Vrátí argument zadaného příkazu.
     *
     * @return Parametr zadaného příkazu
     */
    private String getCommandArgument()
    {
        if (wordsInCommand.length < 2) {
            scenERROR("Příkaz «%s» vyžadující argument byl zadán bez argumentů",
                  wordsInCommand[0]);
            return "";
        }
        return wordsInCommand[1];
    }


    /***************************************************************************
     * Inicializuje atributy používané v průběhu testu
     * a vytiskne zprávu o zahájení testu daného scénáře.
     */
    private void initialization()
    {
        descriptonOfScenarioInTest =
               "Author:                 " + portal.authorString() +
             "\nScenario manager class: " + portal.getClass() +
             "\nScenario:               " + scenarioInTest.name();
        enteredActions .clear();
        visitedPlaces  .clear();
        mentionedPlaces.clear();
        seenItems      .clear();
        notUsedActionTypes = EnumSet.allOf(TypeOfStep.class);
        numOfStep    = 0;
        scenarioOK   = true;
        finished     = true;
        previousStep = null;
        prf("%s\n===== Start of the test ===== %2$tF — %2$tR\n",
            N_HASHES_N + descriptonOfScenarioInTest, new Date());
    }


    /***************************************************************************
     * Pro pomocné akce ze standardního scénáře připraví názvy testů,
     * které by se měly vyskytnout v testovacím scénáři pomocných akcí.
     */
    private Set<String> initializeNonStandardTests()
    {
        Set<String> nonStandardTests = new HashSet<>();
        Scenario happy = portal.scenarios().get(0);

        for (ScenarioStep step : happy.steps()) {

            TypeOfStep tos = step.typeOfStep;
            if (!tos.subtype().equals(stNONSTANDARD) && !tos.equals(tsSUCCESS))
                continue;

            var cmd    = step.command.toLowerCase();
            var wrd    = cmd.split(" ");
            var action = wrd[0];

            for (String need : step.needs.keySet())
                nonStandardTests.add(fullNameOfNonStandardTest(tos, action, need));
            for (String test : step.tests)
                nonStandardTests.add(fullNameOfNonStandardTest(tos, action, test));

        }

        return nonStandardTests;
    }


    /***************************************************************************
     * Připraví a vytiskne závěrečnou zprávu o testu zadaného scénáře.
     */
    private void finalization()
    {
        allEnteredActions .addAll(enteredActions);
        allMentionedPlaces.addAll(mentionedPlaces);
        allSeenItems      .addAll(seenItems);

        //Zjistí zmíněné, ale nenavštívené prostory
        Set<String> notVisited = discoverNotVisitedPlaces();

        //Zjistí, které typy akcí nebyly použity a které měly být použity
        Set<String> notUsed = notUsedTypesOfStep();
//        Set<String> notUsed = discoverNotUsedStepTypes();

        showResults(notVisited, notUsed);
    }


    /***************************************************************************
     * Vrátí množinu názvů typů kroků, které nebyly použity,
     * i když být použity měly.
     *
     * @return Požadovaná množina
     */
    private Set<String> notUsedTypesOfStep()
    {
        return switch (scenarioInTest.type())
        {
            case scHAPPY
                 -> verifyCompleteness(TypeOfStep.HAPPY_ACTIONS);
            case scBASIC
                 -> verifyCompleteness(TypeOfStep.BASIC_ACTIONS);
            case scMISTAKES
                 -> verifyCompleteness(TypeOfStep.MISTAKE_ACTIONS);
            case scMISTAKES_NS
                 -> verifyCompleteness(TypeOfStep.MISTAKE_NS_ACTIONS);
            case scFOURTH, scGENERAL, scDEMO
                 -> Collections.EMPTY_SET;
        };
    }


    /***************************************************************************
     * Zpracuje aktuální krok scénáře.
     * Odkaz na něj je uložen v atributu {@link #currentStep}.
     *
     * @throws TestException Chyba v testované aplikaci
     * @ throws FrameworkException Objevena chyba frameworku
     */
    @SuppressWarnings("DuplicateBranchesInSwitch")
    private void processStep()
    {
        String message = String.format("%2d. %18s - %s",
                         currentStep.index,
                         currentStep.typeOfStep, currentStep.command);
        prln(message);
        if (currentStep.typeOfStep == tsNOT_START)
        {
            return;
        }
        numOfStep++;
        boolean modifiedStep = (currentStep.typeOfStep.subtype() ==
                                Subtype.stMODIFIED);

        wordsInCommand = currentStep.command.split("\\s+");
        if (! modifiedStep                           &&
            (currentStep.typeOfStep != tsDIALOG)     &&
            (currentStep.typeOfStep != tsUNKNOWN)    &&
            (currentStep.typeOfStep != tsSTART)      &&
            (currentStep.typeOfStep != tsEMPTY)      &&
            (wordsInCommand.length > 0))
        {
            String name = wordsInCommand[0].toLowerCase();
            enteredActions.add(name);
            checkNonstandard(name);
        }
        checkMessageLength("zprávy v " + currentStep.index + ". kroku",
                            currentStep.message);
        recordStepInfo();

        if (! modifiedStep) switch (currentStep.typeOfStep)
        {
            case tsNOT_SET -> {
                String txt = "\nTyp kroku " + currentStep.typeOfStep +
                             " nesmí být používán v testovaných scénářích.";
                scenERROR(txt);
            }
            case tsSTART            -> verifyType_START         ();

            case tsNOT_START        -> verifyType_NOT_START     ();
            case tsEMPTY            -> verifyType_EMPTY         ();
            case tsUNKNOWN          -> verifyType_UNKNOWN       ();

            case tsSUCCESS          -> verifyType_SUCCESS       ();
            case tsNOT_SUCCESS      -> verifyType_NOT_SUCCESS   ();

            case tsHELP             -> verifyType_HELP          ();
            case tsEND              -> verifyType_END           ();

            case tsGOTO             -> verifyType_GOTO          ();
            case tsMOVE_WA          -> verifyType_GOTO_WA       ();
            case tsBAD_NEIGHBOR     -> verifyType_BAD_NEIGHBOR  ();

            case tsTAKE             -> verifyType_TAKE          ();
            case tsTAKE_WA          -> verifyType_TAKE_WA       ();
            case tsBAD_ITEM         -> verifyType_BAD_ITEM      ();
            case tsUNMOVABLE        -> verifyType_UNMOVABLE     ();
            case tsBAG_FULL         -> verifyType_BAG_FULL      ();

            case tsPUT_DOWN         -> verifyType_PUT_DOWN      ();
            case tsPUT_DOWN_WA      -> verifyType_PUT_DOWN_WA   ();
            case tsNOT_IN_BAG       -> verifyType_NOT_IN_BAG    ();

            case tsNS_0             -> verifyType_NON_STANDARDx ();
            case tsNS_1             -> verifyType_NON_STANDARDx ();
            case tsNS_2             -> verifyType_NON_STANDARDx ();
            case tsNS_3             -> verifyType_NON_STANDARDx ();
            case tsNS0_WrongCond    -> verifyType_WRONG_NS      ();
            case tsNS1_WrongCond    -> verifyType_WRONG_NS      ();
            case tsNS2_WrongCond    -> verifyType_WRONG_NS      ();
            case tsNS1_0Args        -> verifyType_WRONG_NS      ();
            case tsNS2_1Args        -> verifyType_WRONG_NS      ();
            case tsNS3_012Args      -> verifyType_WRONG_NS      ();
//            case tsNS1_WRONG_ARG    -> verifyType_WRONG_NS      ();
//            case tsNS2_WRONG_1stARG -> verifyType_WRONG_NS      ();
//            case tsNS2_WRONG_2ndARG -> verifyType_WRONG_NS      ();
//            case tsNS3_WRONG_1stARG -> verifyType_WRONG_NS      ();
//            case tsNS3_WRONG_2ndARG -> verifyType_WRONG_NS      ();
//            case tsNS3_WRONG_3rdARG -> verifyType_WRONG_NS      ();

            case tsDIALOG           -> verifyType_DIALOG        ();
            case tsDEMO             -> verifyType_DEMO          ();

            default ->
                throw new TestException("Nepokrytý typ kroku scénáře: "
                                      + currentStep.typeOfStep);
        }
        previousStep = currentStep;
    }


    /***************************************************************************
     * Zaznamená do "statistik" důležité objekty z tohoto kroku.
     */
    private void recordStepInfo()
    {
        visitedPlaces  .add(currentStep.place.toLowerCase());
        mentionedPlaces.add(currentStep.place.toLowerCase());
        mentionedPlaces.addAll(arr2lstInLC(currentStep.neighbors));
        seenItems      .addAll(arr2lstInLC(currentStep.items));
        seenItems      .addAll(arr2lstInLC(currentStep.bag));
        notUsedActionTypes .remove(currentStep.typeOfStep);
    }


    /***************************************************************************
     * Vytiskne informaci o splnění požadovaného počtu objektů daného typu.
     * U scénáře zjišťuje počet kroků, použitých příkazů,
     * navštívených místností, zmíněných místností apod.
     *
     * @param anounced Počet objektů vyskytujících se ve scénáři
     * @param required Požadovaný minimální počet objektů
     * @return Text zprávy oznamující nalezený počet objektů doplněný v případě
     *         nesplnění podmínky minimálním požadovaným počtem objektů
     */
    private String satisfy(int anounced, int required)
    {
        if (TypeOfScenario.scHAPPY.equals(scenarioInTest.type())) {
            boolean yes = (anounced >= required);
            scenarioOK = (scenarioOK && yes);
            return  yes  ?   "suit "
                         :  ("DOES NOT SUIT  (min = " + required + ") ");
        }
        return "";
    }


    /***************************************************************************
     * Zobrazí výsledky provedeného testu spolu se základní charakteristikou
     * testovaného scénáře.
     *
     * @param notVisited Zmíněné, avšak nenavštívené prostory
     * @param absent     Neprovedené akce, které však měly být provedeny
     */
    private void showResults(Set<String> notVisited, Set<String> absent)
    {
        int numOwnActions = enteredNSActions.size();
        int numVisited    = visitedPlaces      .size();
        int numMentioned  = mentionedPlaces    .size();
        int numEntered    = enteredActions     .size();
        int numNotUsed    = notUsedActionTypes .size();
        int numAbsent     = absent             .size();

        Limits limits = Limits.get();

        String s =
             "===== End of the test =====" +
           "\nPočet kroků:           " + z2(numOfStep) + " - " +
                                satisfy(numOfStep, limits.minSteps) +
           lineWrapper(LINE_LENGTH,
           "\nNestandardních akcí:   " + z2(numOwnActions) + " - ",
                                satisfy(numOwnActions, limits.minOwnActions)
                              + enteredNSActions) +
           lineWrapper(LINE_LENGTH,
           "\nZmíněných prostorů:    " + z2(numMentioned) + " - ",
                                satisfy(numMentioned, limits.minPlaces) +
                                mentionedPlaces) +
           lineWrapper(LINE_LENGTH,
           "\nNavštívených prostorů: " + z2(numVisited) + " - ",
                                satisfy(numVisited, limits.minVisited) +
                                visitedPlaces) +
           lineWrapper(LINE_LENGTH,
           "\nZadaných akcí:         " + z2(numEntered) + " - ",
                                           enteredActions) +
           lineWrapper(LINE_LENGTH,
           "\nNezadané typy kroků:   " + z2(numNotUsed) + " - ",
                                             notUsedActionTypes) +
           lineWrapper(LINE_LENGTH,
           "\nZ toho povinných:      " + z2(numAbsent) + " - ",
                                           absent) +
           lineWrapper(LINE_LENGTH,
           "\nNavštívené prostory:   ",  visitedPlaces) +
           lineWrapper(LINE_LENGTH,
           "\nNenavštívené prostory: ",  notVisited) +
           lineWrapper(LINE_LENGTH,
           "\nZmíněné h-objekty:     ",  seenItems) +
           "\n===== Provedený test\n"       + descriptonOfScenarioInTest +
           "\n===== Test " + (scenarioOK ? "prošel" : "NEPROŠEL") +
           N_HASHES_N;
        prln(s);
    }


    /***************************************************************************
     * Vrátí text se stavem minulého a aktuálního kroku.
     *
     * @return Požadovaný text
     */
    private String stepStates()
    {
        return N_DOUBLELINE
             + "\nMinulý krok: \n------------\n" + previousStep
             + N_DOUBLELINE
             + "\nTento krok:\n-----------\n" + currentStep;
    }


    /***************************************************************************
     * Prověří zadaný scénář, zapíše výsledek na standardní výstup
     * a vrátí logickou hodnotu oznamující, zda scénář úspěšně prošel testem.
     *
     * @param scenario  Prověřovaný scénář
     * @return Informace, zda scénář úspěšně prošel testem
     */
    private boolean verify(Scenario scenario)
    {
        scenarioInTest = scenario;
        initialization();
        if (TypeOfScenario.scDEMO.equals(scenario.type())) {
            verifyDemoScenario(scenario);
        } else {
            verifyTestableScenario();
        }
        finalization();
        return scenarioOK;
    }


    /***************************************************************************
     * Ověří, že se texty v zadaných polích v minulém a tomto zkušebním
     * kroku shodují s výjimkou zadaného názvu, který v jedné verzi chybí.
     *
     * @param pair    Přepravka s dvojicí polí s názvy entit platnými
     *                v minulém a současném kroku scénáře
     * @param absent  Text, který má v jednom z polí chybět
     * @param nowLess {@code true} má-li chybět v současném kroku,
     *                {@code false} má-li chybět v minulém kroku
     */
    private void verifyAbsence(Pair pair, String absent, boolean nowLess)
    {
        Names    names   = pair.pair(this);
        String[] last    = names.last;
        String[] current = names.current;
        Arrays.sort(last,    CIC);
        Arrays.sort(current, CIC);

        boolean agree = true;
        int difference = nowLess ? -1 : 1;
        try {
            if ((current.length - last.length)  !=  difference) {
                agree = false;
            }
            else if ((current.length == 0)  ||  (last.length == 0)) {
                agree = absent.equalsIgnoreCase(nowLess  ?  last[0]
                                                          :  current[0]);
            }
            else {
                for (int m=0, n=0;
                    (n < current.length)  &&  (m < last.length);
                    m++, n++)
                {
                    if (! last[m].equalsIgnoreCase(current[n])) {
                        String divergence;
                        if (nowLess) {
                            divergence = last[m];
                            n--;
                        }
                        else {
                            divergence = current[n];
                            m--;
                        }
                        agree = absent.equalsIgnoreCase(divergence);
                    }
                }
            }
            if (agree)  {
                return;                                 //==========>
            }
        } catch(Exception e) {
            //Nepotřebuje ošetřit - prostě ohlásí chybu
        }
        scenERROR("Rozdíly v seznamech objektů typu %s \n"
            + "mezi minulým a tímto krokem neodpovídají akci typu %s\n"
            + "Má chybět: %s\n"
            + "Minule: %s\nNyní:%s",
              pair.name(), this.currentStep.typeOfStep,
              absent, Arrays.asList(last), Arrays.asList(current));
    }


    /***************************************************************************
     * Ověří, že název právě ohlášené akce je stejného typu jako doposud
     * zadané akce se stejným názvem a jedná-li se o první výskyt akce
     * s daným názvem, přidá jej do seznamu názvů spolu s typem kroku
     * pro kontroly konzistence akcí zadaných v budoucnu.
     */
    private void verifyActionName()
    {
        TypeOfStep typeOfStep = currentStep.typeOfStep;
        String actionName;
        if (wordsInCommand.length >0) {
            actionName = wordsInCommand[0];
        }
        else {
            scenERROR("Prázdný příkaz není možno zadat jako akci typu: %s.",
                  typeOfStep);
            return; //Jen kvůli překladači, sem se program nedostane
        }

        String lowerName = actionName.toLowerCase();
        TypeOfStep type  = action2type.get(lowerName);
        if (type == null)  {    //Ještě nebyl zaznamenán
            action2type.put(lowerName,  typeOfStep);
            if (typeOfStep.subtype() == stNONSTANDARD) {
                enteredNSActions.add(lowerName);
            }
            else if ((type2name.get(typeOfStep) != null) &&
                     (typeOfStep != TypeOfStep.tsUNKNOWN) )
            {
                String rival = type2name.get(typeOfStep);
                scenERROR("Pro akci typu %s byl již použit příkaz %s\n" +
                      "    nyní se pokoušíte použít příkaz %s",
                      typeOfStep, rival, actionName);
            }
            else {
                type2name.put(typeOfStep, lowerName);
            }
        }
        else if (type.group() != typeOfStep.group())  {
            scenERROR("Akce «%s» již byla použita v kroku typu %s",
                  actionName, type);
        }

        if (name2typeGroup.get(lowerName) == null) {
            name2typeGroup.put(lowerName, typeOfStep.group());
        }
        else if ((name2typeGroup.get(lowerName) != typeOfStep.group()) )
        {
            scenERROR("Příkaz %s byl již použit jako příkaz typu %s\n" +
                  "    nyní jej však používáte jako příkaz typu %s",
                  actionName, name2typeGroup.get(lowerName),
                  typeOfStep.group());
        }
    }


    /***************************************************************************
     * Zjistí, zda odpověď hry na jednoparametrický příkaz
     * končí názvem argumentu.
     */
    private void verifyArgumentInAnswer()
    {
        if (! ArgumentAtEnd.contains(currentStep.typeOfStep)) {
            return; // Typ kroku nevyžaduje zprávu končící názvem argumentu
        }
        String argument = wordsInCommand[1].toLowerCase();
        if (currentStep.message.toLowerCase().endsWith(argument)) {
            return; // Zpráva končí názvem argumentu
        }
        scenERROR("Zpráva hry nekončí názvem argumentu " + wordsInCommand[1]);
    }


    /***************************************************************************
     * Zjistí, zda zadané pole textů obsahuje/neobsahuje zadaný text.
     *
     * @param array          Prověřované pole textů
     * @param text           Text, jehož přítomnost zjišťujeme
     * @param shouldContain  {@code true} ptáme-li se, zda pole text obsahuje,
     *                       {@code false} zjišťujeme-li, zda text neobsahuje
     * @return Odpověď na zadanou otázku
     */
    private boolean verifyArrayContent(String[] array, String text,
                                       boolean  shouldContain)
    {
        boolean found = false;
        for (String s : array)  {
            if (s.equalsIgnoreCase(text)) {
                found = true;
                break;
            }
        }
        return (found == shouldContain);
    }


    /***************************************************************************
     * Ověří, zda zadaný scénář obsahuje všechny typy akcí
     * obsažené v zadané množině.
     *
     * @param required Požadované typy testů
     * @return Množina
     */
    private Set<String> verifyCompleteness(EnumSet<TypeOfStep> required)
    {
        EnumSet<TypeOfStep> notUsed = EnumSet.copyOf(required);
        for (ScenarioStep step : scenarioInTest.steps()) {
            TypeOfStep tos = step.typeOfStep;
            notUsed.remove(tos);
        }

        /* Pokud již byla nastaveno scenarioOK v {@link verifyTestableScenario}
         * na {@code false}, není přípustné ho přepsat zpět na {@code true} */
        scenarioOK = (scenarioOK && notUsed.size() == 0);
        return notUsed.stream().map(t -> t.name()).collect(Collectors.toSet());
    }


    /***************************************************************************
     * Vypíše typy jednotlivých kroků a jejich příkazy; nic nekontroluje.
     *
     * @param scenario Prověřovaný scénář
     */
    private void verifyDemoScenario(Scenario scenario)
    {
        int index = 1;
        for (ScenarioStep step : scenario.steps()) {
            String message = String.format("%2d. %14s - %s",
                             index, step.typeOfStep, step.command);
            prln(message);
        }
    }


    /***************************************************************************
     * Ověří, že se v minulém a tomto testovacím kroku shoduje
     * aktuální prostor a s ním i názvy v polích reprezentujících
     * sousedy prostoru, předměty v prostoru a obsah batohu.
     */
    private void verifyEqualityOfAllFields()
    {
        verifySamePlace();
        verifyFieldEquality(Pair.NEIGHBORS, Pair.ITEMS, Pair.BAG);
    }


    /***************************************************************************
     * Ověří, že se seznamy názvů v zadané přepravce
     * v minulém a tomto kroku testu shodují.
     *
     * @param pair Přepravka s porovnávanými poli názvů
     *             v minulém a tomto kroku testu
     */
    private void verifyFieldEquality(Pair pair)
    {
        Names    names   = pair.pair(this);
        String[] last    = names.last;
        String[] current = names.current;
        Arrays.sort(last,    CIC);
        Arrays.sort(current, CIC);

        boolean equals = true;
        if (last.length != current.length) {
            equals = false;
        }
        else {
            for (int i=0;   i < current.length;   i++) {
                if (! last[i].equalsIgnoreCase(current[i])) {
                    equals = false;
                    break;
                }
            }
        }
        if (equals)  { return; }

        scenERROR("Seznamy objektů typu %s v minulém a tomto kroku " +
              "si neodpovídají.\nMinule: %s\nNyní:   %s",
              pair.name(), Arrays.asList(last), Arrays.asList(current));
    }


    /***************************************************************************
     * Ověří, že se texty v zadaných polích (tj. názvy pojmenovaných objektů)
     * v minulém a tomto testovacím kroku shodují
     * (např. že se při přechodu do jiného prostoru nezměnil stav batohu).
     *
     * @param pairs Přepravky s porovnávanými poli textů
     *              v minulém a tomto kroku testu
     */
    private void verifyFieldEquality(Pair... pairs)
    {
        for (Pair pair : pairs) {
            verifyFieldEquality(pair);
        }
    }


    /***************************************************************************
     * Ověří splnění očekávaného nastavení příznaků.
     */
    private void verifyNeeds()
    {
        var expects = currentStep.needs;
        var tests   = currentStep.tests;
        if (((expects == null)  ||  expects.isEmpty())  &&
            ((tests == null)    ||  tests  .isEmpty())  )
        {
            scenERROR("Krok nemá zadány očekávané příznaky %s",
                  currentStep);
        }
        if (expects != null) for (String key : expects.keySet()) {
            if (! flags.containsKey(key)) {
                scenERROR("Očekávaný příznak neexistuje.\n"
                    + "Očekáváno: " + key + "\n"
                    + "Nastaveno: " + flags);
            }
            if (! flags.get(key).equals(expects.get(key))) {
                scenERROR("Zadaný příznak nemá očekávanou hodnotu.\n"
                    + "Očekáváno: (" + key + ", " + expects.get(key) + ")\n"
                    + "Nastaveno: (" + key + ", " + flags  .get(key) + ")");
            }
        }
    }


    /***************************************************************************
     * Ověří splnění očekávaného nastavení příznaků.
     */
    private void verifyNeedsWrong()
    {
        var expects = currentStep.needs;
        var tests   = currentStep.tests;
        var sets    = currentStep.sets;
        var action  = currentStep.command.split(" ")[0].toLowerCase();
        if (((expects == null)  ||  expects.isEmpty())  &&
            ((tests == null)    ||  tests  .isEmpty())  )
        {
            scenERROR("Krok nemá zadány očekávané příznaky %s",
                  currentStep);
        }
        int expectsSize = (expects != null) ? expects.size() : 0;
        int testsSize   = (tests != null) ? tests.size() : 0;
        if (expectsSize + testsSize != 1) {
            scenERROR("Vhybový Kok smí kontrolovat pouze jeden příznak "
                    + "nebo test.\nChybný krok: %s",
                      currentStep);
        }
        if ((tests != null)  &&  !tests.isEmpty()) {
            String testName = fullNameOfNonStandardTest(
                    currentStep.typeOfStep.group(),
                    action,
                    tests.get(0)
            );
            allNonStandardTests.remove(testName);
            return;   // Testy se budou kontrolovat až za běhu
        }
        for (String key : expects.keySet()) {
            if (! flags.containsKey(key)) {
                scenERROR("Očekávaný příznak neexistuje.\n"
                    + "Očekáváno: " + key + "\n"
                    + "Obdrženo:  " + flags);
            }
            if (sets.containsKey(key) &&
                    sets.get(key).equals(expects.get(key))) {
                scenERROR("Nastavovaný příznak má stejnou hodnotu, "
                        + "jako příznak požadovaný.\n"
                        + "Příznak: " + key);
            }
        }
        // Ověří, že podmínka není splněna
        for (String key : expects.keySet()) {
            if (! flags.get(key).equals(expects.get(key))) {
                String testName = fullNameOfNonStandardTest(
                        currentStep.typeOfStep.group(),
                        action,
                        key
                );
                allNonStandardTests.remove(testName);
                return;
            }
        }
        scenERROR("Očekávaná podmínka byla splněna,\n"
            + "nebyl proto důvod k ohlášení chyby.\n"
            + "Očekáváno: " + expects + "\n"
            + "Obdrženo:  " + flags);
    }


    /***************************************************************************
     * Ověří, že byl-li zadán nestartovní příkaz, tak hra již běžela.
     *
     * @return Nastala-li chyba, vrátí {@code true}, jinak vrátí {@code false}
     */
    private boolean verifyNotAlive()
    {
        if (finished  &&
            ! (TypeOfStep.tsSTART    .equals(currentStep.typeOfStep) ||
                   TypeOfStep.tsNOT_START.equals(currentStep.typeOfStep))) {
            scenERROR("Zadaný krok scénáře je před začátkem nebo " +
                "za koncem hry\ntj. hra ještě není odstartovaná " +
                "anebo je již ukončená %s",
                  currentStep);
            return true;
        }
        return false;
    }


    /***************************************************************************
     * Ověří, že počet argumentů zadaných v příkazu odpovídá typu kroku.
     */
    private void verifyNumberOfArguments()
    {
        int arguments = currentStep.typeOfStep.paramCount();
        if (wordsInCommand.length != (arguments+1)) {
            scenERROR("Požadovaný počet argumentů je %d, " +
                  "avšak příkaz má %d argumentů",
                  arguments, (wordsInCommand.length-1));
        }
        if (arguments == 1) {
            verifyArgumentInAnswer();
        }
    }


    /***************************************************************************
     * Ověří, že po vykonání příkazu hráč skončil v prostoru,
     * který byl zadán jako argument.
     */
    private void verifyPlaceReached()
    {
        String place = getCommandArgument();
        if ((place.length() > 0)   && //Byl zadán argument
            (! place.equalsIgnoreCase(currentStep.place)))
        {
            scenERROR("Hráč se nepřesunul do prostoru zadaného v příkazu.");
        }
    }


    /***************************************************************************
     * Ověří, že se nezměnil aktuální prostor.
     */
    private void verifySamePlace()
    {
        if (! previousStep.place.equalsIgnoreCase(currentStep.place)) {
            scenERROR("Při vykonvání tohoto příkazu se nesmí změnit " +
                  "aktuální prostor.");
        }
    }


    /***************************************************************************
     * Ověří splnění očekávaného nastavení příznaků.
     */
    private void verifySets()
    {
        Map<String, Object> expects = currentStep.needs;
        Map<String, Object> sets = currentStep.sets;
        if ((sets == null)  ||  sets.isEmpty()) {
            scenERROR("Krok nemá zadány nastavované příznaky %s",
                  currentStep);
            return;
        }
        for (String key : sets.keySet()) {
            if (! flags.containsKey(key)) {
                scenERROR("Očekávaný příznak neexistuje.\n"
                    + "Očekáváno: " + key + "\n"
                    + "Nastaveno: " + flags);
            }
            if (expects.containsKey(key) &&
                    sets.get(key).equals(expects.get(key))) {
                scenERROR("Nastavovaný příznak má stejnou hodnotu, "
                        + "jako příznak požadovaný.\n"
                        + "Příznak: " + key);
            }
            flags.put(key, sets.get(key));
            if (! flags.get(key).equals(sets.get(key))) {
                scenERROR("Nastavovaný příznak nemá očekávanou hodnotu.\n"
                    + "Očekáváno: (" + key + ", " + sets .get(key) + ")\n"
                    + "Nastaveno: (" + key + ", " + flags.get(key) + ")");
            }
        }
    }


    /***************************************************************************
     * Prověří aktuální scénář a připraví podklady pro zapsání výsledku.
     * Při odhalení chyby vytiskne zprávu
     * a nastaví proměnnou {@link #scenarioOK} na {@code false};
     */
    private void verifyTestableScenario()
    {
        boolean start = true;
        for (ScenarioStep step : scenarioInTest.steps()) {
            currentStep = step;
            if (start) {
                start = false;
                verifyWrongStart(step);
            }
            if (verifyNotAlive()) { break; }
            processStep();
            previousStep = currentStep;
        }
        if (!finished)
        {
            scenERROR("Scénář hru explicitně neukončil.");
        }
        Set<String> notUsed = notUsedTypesOfStep();
        if (scenarioInTest.type() == TypeOfScenario.scHAPPY) {
            happyTested = true;
        }
        if (scenarioInTest.type() == TypeOfScenario.scMISTAKES_NS) {
            notUsed = allNonStandardTests;
            if (allNonStandardTests.size() > 0) scenarioOK = false;
        }
        prln(lineWrapper(LINE_LENGTH, "Nepokryté typy akcí: ", notUsed));
    }

////////////////////////////////////////////////////////////////////////////////

    /***************************************************************************
     * Ověří, že byl zadána příkaz pro zvednutí objektu
     * a že v aktuálním prostoru zadaný objekt doopravdy není.
     * Při zpracování příkazu se nesmí změnit aktuální prostor
     * ani h-objektyv tomto prostoru a ani obsah batohu.
     */
    private void verifyType_BAD_ITEM()
    {
        verifyActionName();
        verifyEqualityOfAllFields();
        verifyNumberOfArguments();
        if (verifyArrayContent(currentStep.items, getCommandArgument(), true))
        {
            scenERROR("Zadaný předmět v aktuálním prostoru je\n%s", currentStep);
        }
    }


    /***************************************************************************
     * Ověří reakci hry na pokus přejít do prostoru,
     * který v daném okamžiku není sousedem aktuálního prostoru.
     * Při zpracování příkazu se nesmí změnit
     * aktuální prostor ani objekty v tomto prostoru a ani obsah batohu.
     */
    private void verifyType_BAD_NEIGHBOR()
    {
        verifyActionName();
        verifyEqualityOfAllFields();
        verifyNumberOfArguments();
        if (verifyArrayContent(currentStep.neighbors,
                               getCommandArgument(),true))
        {
            scenERROR("Zadaného souseda místnost má\n%s", currentStep);
        }
    }


    /***************************************************************************
     * Ověří reakci na pokus zvednout předmět v situaci, kdy je batoh již plný.
     * Při zpracování příkazu se nesmí změnit
     * aktuální prostor ani objekty v tomto prostoru a ani obsah batohu.
     */
    private void verifyType_BAG_FULL() {
        verifyActionName();
        verifyEqualityOfAllFields();
        verifyNumberOfArguments();
    }


    /***************************************************************************
     * Ohlásí chybu, protože demonstrační typ kroku se v testovacím scénáři
     * nesmí objevit.
     */
    private void verifyType_DEMO()
    {
        scenERROR("Demonstrační typ kroku nepatří do testovacího scénáře\n%s",
              currentStep);
    }


    /***************************************************************************
     * Všechny kontroly tohoto typu kroku již byl provedeny v metodě
     * {@link #verify(Scenario)}
     */
    private void verifyType_DIALOG()
    {
    }


    /***************************************************************************
     * Ověří reakci hry na zadaný prázdný text.
     * Při zpracování příkazu se nesmí změnit
     * aktuální prostor ani objekty v tomto prostoru a ani obsah batohu.
     */
    private void verifyType_EMPTY()
    {
        verifyActionName();
        verifyEqualityOfAllFields();
    }


    /***************************************************************************
     * Nastaví příznak konce, aby bylo možno ověřit,
     * že zadaný krok krok testu je skutečně poslední
     * (každý další krok by způsobil chybu),
     * a současně ověří, že se zadáním konce hry nezmění ostatní stavy hry.
     */
    private void verifyType_END()
    {
        verifyActionName();
        verifyEqualityOfAllFields();
        finished = true;
    }


    /***************************************************************************
     * Ověří reakci na přesun hráče z prostoru do prostoru.
     * Při zpracování příkazu se nesmí změnit obsah batohu.
     */
    private void verifyType_GOTO() {
        verifyActionName();
        verifyNumberOfArguments();
        verifyPlaceReached();
        verifyFieldEquality(Pair.BAG);
    }


    /***************************************************************************
     * Ověří reakci na příkaz k přesunu do jiného prostoru zadaný bez argumentu.
     * Při zpracování příkazu se nesmí nic změnit.
     */
    private void verifyType_GOTO_WA()
    {
        verifyActionName();
        verifyNumberOfArguments();
        verifyEqualityOfAllFields();
    }


    /***************************************************************************
     * Ověří, že stav před zadáním příkazu a po jeho vykonání je shodný.
     */
    private void verifyType_HELP()
    {
        verifyActionName();
        verifyEqualityOfAllFields();
    }


    /***************************************************************************
     * Ověří, že tuto akci lze konzistentně zařadit mezi nestandardní,
     * a že příkaz má požadovaný počet argumentů.
     */
    private void verifyType_NON_STANDARDx()
    {
        verifyActionName();
        verifyNumberOfArguments();
        if (LEVEL.ordinal() > Level.HAPPY.ordinal()) {
            verifyNeeds();
            verifySets();
        }
    }


    /***************************************************************************
     * Ověří, že byl zadána příkaz pro položení objektu
     * a že zadaný objekt v batohu doopravdy není.
     * Při zpracování příkazu se nesmí změnit
     * aktuální prostor ani objekty tomto v prostoru a ani obsah batohu.
     */
    private void verifyType_NOT_IN_BAG()
    {
        verifyActionName();
        verifyEqualityOfAllFields();
        if (verifyArrayContent(currentStep.bag, getCommandArgument(), true)) {
            scenERROR("Zadaný objekt v batohu je\n%s", currentStep);
        }
    }


    /***************************************************************************
     * Ověří, že tento příkaz byl zadán jako před spuštěním hry
     * a že jeho název není prázdný.
     */
    private void verifyType_NOT_START()
    {
        if (! finished) {
            scenERROR("Předchozí hra ještě nebyla ukončena.\n" +
                      "Tento typ testovacího kroku se vkládá před start hry.");
        }
        else if ("".equals(currentStep.command))  {
            scenERROR("Příkaz v testu špatného odstartování hry "
                    + "nesmí být prázdný.");
        }
    }


    /***************************************************************************
     * Ověří reakci na příkaz k řádnému ukončení hry, při němž musejí být
     * platné všechny vyžadované podmínky a
     * plánovaný stav před zadáním příkazu a po jeho vykonání bude shodný.
     */
    private void verifyType_NOT_SUCCESS()
    {
        verifyActionName();
        verifyNeedsWrong();
        verifyNumberOfArguments();
        verifyEqualityOfAllFields();
    }


    /***************************************************************************
     * Ověří reakci na položení předmětu.
     * Při zpracování příkazu se nesmí změnit aktuální prostor.
     */
    private void verifyType_PUT_DOWN() {
        verifyActionName();
        verifySamePlace();
        verifyNumberOfArguments();
        verifyFieldEquality(Pair.NEIGHBORS);
        String toPut = getCommandArgument();
        if (toPut.length() > 0) { //Byl zadán argument
            verifyAbsence(Pair.ITEMS, toPut, false);
            verifyAbsence(Pair.BAG,   toPut, true);
        }
    }


    /***************************************************************************
     * Ověří reakci na příkaz k položení předmětu zadaný bez argumentu.
     * Při zpracování příkazu se nesmí nic změnit.
     */
    private void verifyType_PUT_DOWN_WA()
    {
        verifyActionName();
        verifyNumberOfArguments();
        verifyEqualityOfAllFields();
    }


    /***************************************************************************
     * Ověří, že tento příkaz byl zadán jako první a že jeho název je prázdný.
     */
    private void verifyType_START()
    {
        if (! finished) {
            scenERROR("Předchozí hra ještě nebyla ukončena.\n" +
                  "Nová hra může odstartovat až po ukončení té předchozí.");
        }
        else if (! "".equals(currentStep.command))  {
            scenERROR("Úvodní akce každého scénáře musí mít prázdný název \n" +
                  "a musí definovat zprávu a stav hry po odstartování,"
                  + ".");
        }
        else if ((LEVEL.ordinal() > Level.HAPPY.ordinal())  &&
                 ((currentStep.sets == null) ||  currentStep.sets.isEmpty())) {
            scenERROR("Startovní krok nenastavuje výchozí hodnoty příznaků.");
        }
        else {
            flags.clear();
            flags.putAll(currentStep.sets);
            finished = false;
        }
    }


    /***************************************************************************
     * Ověří reakci na příkaz k řádnému ukončení hry, při němž musejí být
     * platné všechny vyžadované podmínky a
     * plánovaný stav před zadáním příkazu a po jeho vykonání bude shodný.
     */
    private void verifyType_SUCCESS()
    {
        verifyActionName();
        verifyNumberOfArguments();
        if (LEVEL.ordinal() > Level.HAPPY.ordinal()) {
            verifyNeeds();
        }
        finished = true;
    }


    /***************************************************************************
     * Ověří reakci na příkaz ke zvednutí objektu.
     * Při zpracování příkazu se nesmí změnit aktuální prostor.
     */
    private void verifyType_TAKE()
    {
        verifyActionName();
        verifySamePlace();
        verifyNumberOfArguments();
        verifyFieldEquality(Pair.NEIGHBORS);
        String toTake = getCommandArgument();
        if (toTake.length() > 0) { //Byl zadán argument
            verifyAbsence(Pair.ITEMS, toTake, true );
            verifyAbsence(Pair.BAG,   toTake, false);
        }
    }


    /***************************************************************************
     * Ověří reakci na příkaz k zvednutí předmětu zadaný bez argumentu.
     * Při zpracování příkazu se nesmí nic změnit.
     */
    private void verifyType_TAKE_WA()
    {
        verifyActionName();
        verifyNumberOfArguments();
        verifyEqualityOfAllFields();
    }


    /***************************************************************************
     * Ověří reakci hry na neznámý příkaz.
     * Při zpracování příkazu se nesmí změnit
     * aktuální prostor ani objekty v tomto prostoru a ani obsah batohu.
     */
    private void verifyType_UNKNOWN()
    {
        verifyActionName();
        verifyEqualityOfAllFields();
    }


    /***************************************************************************
     * Ověří reakci na pokus zvednout nezvednutelný objekt.
     * Při zpracování příkazu se nesmí změnit
     * aktuální prostor ani objekty v tomto prostoru a ani obsah batohu.
     */
    private void verifyType_UNMOVABLE()
    {
        verifyActionName();
        verifyNumberOfArguments();
        verifyEqualityOfAllFields();
    }


    /***************************************************************************
     * Ověří, že tuto akci lze konzistentně zařadit mezi nestandardní,
     * že příkaz má požadovaný počet argumentů
     * a že se stav hry nezměnil, protože příkaz byl uživatelem zadán špatně.
     */
    @SuppressWarnings("fallthrough")
    private void verifyType_WRONG_NS()
    {
        verifyActionName();
        verifyEqualityOfAllFields();
        verifyNumberOfArguments();
        if (verifyNeedsWrong.contains(currentStep.typeOfStep)) {
            verifyNeedsWrong();
        }
    }


    /***************************************************************************
     * Ověří, že chybový scénář začíná správným startovním krokem
     * a odebere jeho typ kroku z nepoužitých.
     *
     * @param step      Testovaný krok
     */
    private void verifyWrongStart(ScenarioStep step)
    {
        if (TypeOfScenario.scMISTAKES.equals(scenarioInTest.type())  &&
            (step.typeOfStep != TypeOfStep.tsNOT_START))
        {
            scenERROR("Chybový scénář musí začínat testem definujícím " +
                  "reakci na \nnestartovní (= neprázdný) příkaz " +
                  "zadaný neběžící hře: %s",
                  currentStep);
        }
        else {
            //Odeber tuto íokci z dosud nerealizovaných
            notUsedActionTypes.remove(tsNOT_START);
        }
    }



//== EMBEDDED TYPES AND INNER CLASSES ==========================================

    /***************************************************************************
     * Přepravka pro uchování vektoru názvů některého z objektů
     * v minulém a aktuálním kroku testu.
     */
    private static class Names
    {
        final String[] last;
        final String[] current;

        Names(String[] last, String[] current) {
            this.last    = last;
            this.current = current;
        }
    }


    /***************************************************************************
     * Instance třídy {@code Pair} jsou schopny vrátit odpovídající dvojice
     * polí s názvy příslušných objektů (sousedé prostoru, objekty v prostoru,
     * objekty v batohu).
     */
    @SuppressWarnings("PackageVisibleInnerClass")
    enum Pair
    {
        NEIGHBORS {
            @Override
            @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
            Names pair(ScenarioTester tester) {
                return new Names(tester.previousStep.neighbors,
                                 tester.currentStep .neighbors);
            }
        },

        ITEMS {
            @Override
            @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
            Names pair(ScenarioTester tester) {
                return new Names(tester.previousStep.items,
                                 tester.currentStep .items);
            }
        },

        BAG {
            @Override
            @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
            Names pair(ScenarioTester tester) {
                return new Names(tester.previousStep.bag,
                                 tester.currentStep .bag);
            }
        };

        abstract Names pair(ScenarioTester tester);

    }

}
