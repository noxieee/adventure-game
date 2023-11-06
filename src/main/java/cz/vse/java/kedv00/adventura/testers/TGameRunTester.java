package cz.vse.java.kedv00.adventura.testers;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */

import cz.vse.java.kedv00.adventura.api.IGame;
import cz.vse.java.kedv00.adventura.api.Scenario;
import cz.vse.java.kedv00.adventura.api.ScenarioStep;
import cz.vse.java.kedv00.adventura.api.TypeOfStep;
import cz.vse.java.kedv00.adventura.api.TypeOfStep.Subtype;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cz.vse.java.kedv00.adventura.testers.util.ConditionalPrinter.*;
import static cz.vse.java.kedv00.adventura.testers.util.FormatStrings.*;


/*******************************************************************************
 * Instance třídy {@code TGameRunTester} slouží k otestování běhu her
 * implementujících interfejs {@code IGame} a testovatelných podle scénářů
 * poskytnutých instancí třídy implementující interfejs
 * {@link cz.vse.java.kedv00.adventura.api.IPortal}.
 * <p>
 * Instance jsou schopny prověřit, zda při spuštění zadaného testovacího
 * scénáře hra reaguje tak, jak je ve scénáři naplánováno.
 * Za testovací se přitom považuje libovolný scénář, který není demonstrační.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class TGameRunTester extends ATester
{
//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Kompletní zpráva o průběhu testu. */
    public final StringBuilder verboseMessageBuilder;

    /** Návštěvník zabezpečující v průběhu testu
     *  provádění některých doplňkových akci. */
    private final TestVisitor testVisitor;

    /** Přeprava se souhrnnými informacemi o testované aplikaci
     *  a o provedených testech. */
    private final GameSummary gameSummary;

    /** Tester jednotlivých kroků testované hry. */
    private final TGameStateTester stateTester;



//\IV== INSTANCE VARIABLES (VARIABLE INSTANCE ATTRIBUTES/FIELDS) ===============
//
//    /** Proměnná udržující informaci o tom, tvrdí-li hra, že běží. */
//    private boolean gameIsAlive;

    /** Prověřovaná hra. */
    private IGame game;

    /** Scénář, který je aktuálně aplikován na testovanou hru. */
    private Scenario scenario;

    /** Aktuálně testovaný krok scénáře (šetří předávání parametru). */
    private ScenarioStep step;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Vytvoří instanci, která bude testovat zadanou hru.
     * Konstruktor předpokládá, že parametry již prošly základní prověrkou.
     *
     * @param gameSummary Přepravka s informacemi o testu a jeho výsledcích
     */
    public TGameRunTester(GameSummary gameSummary)
    {
        super(gameSummary.portal);
        this.gameSummary           = gameSummary;
        this.game                  = gameSummary.game;
        this.verboseMessageBuilder = gameSummary.verboseMessageBuilder;
//        this.portal    = portal;
//        this.game      = portal.game();
//        this.gamename  = game   .getClass().name();
//        this.manager   = portal.getScenarioManager();
        this.testVisitor = gameSummary.visitor;
        this.stateTester = new TGameStateTester(gameSummary);
    }



//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================

    /***************************************************************************
     * Spustí hru řízenou zadaným scénářem;
     * této hře bude postupně zadávat příkazy scénáře
     * a porovnávat její odpovědi s požadavky scénáře.
     *
     * @param scenario    Scénář, kterým testujeme hru
     */
    public String executeScenario(Scenario scenario)
//         throws Throwable
    {
        this.scenario = scenario;
        prf("\n########## Testovaný scénář: ##########\n%s\n"
            + "########################################\n",
            scenario);

        Throwable throwable = null;
//        foundErrors.clear();

        String text = header(scenario) +
                      "===== Test jednotlivých prováděných akcí =====";
        prln(text);
        try {
            verifyIsAlive(ScenarioStep.NOT_START_STEP, false);
            //Ověří, že před spuštěním scénáře hra neběží
            int lastIndex = scenario.steps().size();
            int index = 0;
            for (ScenarioStep ss : scenario.steps()) {
                step = ss;
                boolean theLast = (++index == lastIndex);
                //Ověří krok a vytiskne zprávu o daném kroku
                verifyScenarioStep(theLast);
            }
        } catch(Throwable e) {
            throwable = e;
            gameSummary.setThrowable(throwable);
        } finally {
            updateGameSummary(scenario, throwable);
            testVisitor.afterGameEnd(gameSummary);
        }
        if (throwable != null) {
            gameSummary.game.stop();
//            System.exit(1);
        }
        return scenario.name() + ": "
             + ((throwable == null) ? "OK" : "NEPROŠEL");
    }



//\IP== INSTANCE PRIVATE AND AUXILIARY METHODS =================================

    /***************************************************************************
     * Vrátí hlavičku testu zadaného scénáře.
     *
     * @param scenario
     * @return String s hlavičkou testu zadaného scénáře
     */
    private String header(Scenario scenario)
    {
        IGame  game = gameSummary.game;
        String text = N_HASHES
             + "\n##### Author:     " + portal.authorString()
             + "\n##### Scenario:   " + scenario.name()
             + "\n##### Game class: " + game.getClass().getName()
             + "\n##### Time:       " + new Date()
             + N_HASHES_N;
        return text;
    }


    /***************************************************************************
     * Zpracuje ukončení testu, které se musí provést nezávisle na tom,
     * zda test proběhl korektně nebo byla v jeho průběhu vyhozena výjimka.
     *
     * @param scenario  Scénář, podle nějž se testovalo
     * @param throwable Případná zachycená vyhozená výjimka či {@code null}
     */
    private void updateGameSummary(Scenario scenario, Throwable throwable)
    {
        prf("\n===== End of test of the executed actions =====%s\n",
            header(scenario));
        if (throwable != null) {
            gameSummary.setThrowable(throwable);
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw)) {
                pw.print("End was caused by throwing an exception:\n");
                throwable.printStackTrace(pw);
                String txt = sw.toString();
                verboseMessageBuilder.append(txt);
                prln(txt);
            }
        }
    }


    /***************************************************************************
     * Prověří, jestli testovaná hra správně hlásí svoji spuštěnost
     * (před startem a po skončení ne, jinak ano).
     *
     * @param step    Aktuální krok scénáře
     * @param theLast Daný krok je posledním krokem scénáře
     */
    private void verifyIsAlive(ScenarioStep step, boolean theLast)
    {
        boolean gameIsAlive = gameSummary.game.isAlive();
        if ((step.typeOfStep == TypeOfStep.tsNOT_START)  || theLast) {
            if (gameIsAlive) {
                String prefix = theLast
                              ? "Po ukončení hry "
                              : "Před odstartováním hry ";
                ERRs(prefix + " je hra spuštěná, přestože by být neměla.");
            }
        }
        else {
            if (! gameIsAlive) {
                ERRs("Hra tvrdí, že neběží, přestože by měla být spuštěná");
            }
        }
    }


    /***************************************************************************
     * Prověří správnou funkci hry v zadaném kroku testu.
     *
     * @param  theLast  Informace o tom, jedná-li se o poslední krok scénáře
     * @throws IllegalStateException Stav hry neodpovídá tomu,
     *         který podle hodnot dané instance kroku testu očekáváme
     */
    private void verifyScenarioStep(boolean theLast)
    {
        String command = step.command;
        String message;
        verifyBeforeNSStep();
        try {
            message = gameSummary.game.executeCommand(command);
        }
        catch (Exception ex) {
            throw new TestException(
                    "\nPři provádění příkazu: «" + command + "»"
                  + "\nvyhodila hra výjimku " + ex.getClass().getSimpleName()
                  + "\noznamující: " + ex.getMessage()
                  , ex);
        }
        if (step.typeOfStep == TypeOfStep.tsSTART) {
            verifyAfterStartStep();
            testVisitor.afterGameStart(scenario);
        }
        testVisitor.beforeStepTest(step, message);
        verifyAfterNSStep();
        String inspection = stateTester.verify(step, message, theLast);
        verboseMessageBuilder.append(inspection);
        prln(inspection);
        verifyIsAlive(step, theLast);
        testVisitor.afterStepTest(step, message);
    }


    /***************************************************************************
     * Po provedení startovního ktoku prověří ty aspekty stavu hry,
     * které se při dalších krocích neprověřují.
     */
    private void verifyAfterStartStep()
    {
        verify_initial_conditions();
        verify_tests();
    }


    /***************************************************************************
     * Prověří stav hry před provedením následujícího pomocného kroku,
     * jmenovitě platnost podmínek a nastavení příznaků.
     */
    private void verifyBeforeNSStep()
    {
        Subtype    subtype = step.typeOfStep.subtype();
        String[] arguments = step.command.split("\\s+");
        switch (subtype) {
            case stNONSTANDARD, stSUCCESS -> {
                for (String flag : step.needs.keySet()) {
                    Object expected = step.needs.get(flag);
                    Object obtained = game.conditions().get(flag);
                    if (expected != obtained) {
                        ERRi("Neodpovídá hodnota příznaku " + flag,
                             scenario, step, expected, obtained);
                    }
                }
                for (String testName : step.tests) {
                    boolean result = game.tests().get(testName).test(arguments);
                    if (! result) {
                        ERRi("Neodpovídá hodnota testu " + testName,
                             scenario, step, true, result);
                    }
                }
            }
            case stMISTAKE_NS -> {
                for (String key : step.needs.keySet()) {
                    Object wrong    = step.needs.get(key);
                    Object obtained = game.conditions().get(key);
                    if (wrong.equals(obtained)) {
                        ERRi("Hodnota příznaku " + key + " neodpovídá",
                             scenario, step, "Nemá být " + wrong, obtained);
                    }
                }
                for (String testName : step.tests) {
                    boolean tst = game.tests().get(testName).test(arguments);
                    if (tst) {
                        ERRi("Dle scénáře neměl test " + testName + " projít",
                             scenario, step, false, tst);
                    }
                }
            }
        }
    }


    /***************************************************************************
     * Prověří stav hry po provedením posledního pomocného kroku,
     * jmenovitě nastavení požadovaných příznaků.
     */
    private void verifyAfterNSStep()
    {
        Subtype    subtype = step.typeOfStep.subtype();
        if (subtype == Subtype.stNONSTANDARD) {
            var expected = step.sets;
            var obtanied  = game.conditions();
            Map<String, Object> set = new HashMap<>();
            for (String key : expected.keySet()) {
                if (! obtanied.containsKey(key)) {
                    Object value = expected.get(key);
                    ERRi("Mapa příznaků nedefinuje příznak " + key +
                         "\njenž měla poslední akce nastavit na hodnotu "
                       + value,
                         scenario, step, "(" + key + " -> " + value + ")",
                         obtanied);
                }
                if (expected.get(key) != obtanied.get(key)) {
                    Object value = expected.get(key);
                    ERRi("Dle scénáře měla hra nastavit příznak " + key
                        + " na hodnotu " + value,
                          scenario, step, expected, obtanied);
                }
            }
        }
    }


    private void verify_initial_conditions()
    {
        var expected = scenario.steps().get(0).sets.entrySet();
        var obtained = game.conditions().entrySet();
        for (Map.Entry entry : expected) {
            if (! obtained.contains(entry)) {
                ERRs("Mezi příznaky dodanými metodou conditions()\n"
                   + "chybí dvojice " + entry
                   + "\nobjednaná ve startovním kroku scénáře HAPPY\n"
                   + "   Očekáváno: " + expected + '\n'
                   + "   Obdrženo:  " + obtained
                   + "=".repeat(60) + '\n');
            }
        }
    }


    private void verify_tests()
    {
        var expected = scenario.steps().get(0).tests;
        var obtained = game.tests().keySet();
        for (String test : expected) {
            if (! obtained.contains(test)) {
                ERRs("Mezi testy dodanými metodou tests() chybí test '"
                    + test + "\n"
                    + "objednaný ve startovním kroku scénáře HAPPY\n"
                    + "   Očekáváno: " + expected + '\n'
                    + "   Obdrženo:  " + obtained + '\n'
                    + "=".repeat(60) + '\n');
            }
        }
    }
}
