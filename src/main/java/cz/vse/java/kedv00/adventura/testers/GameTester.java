/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package cz.vse.java.kedv00.adventura.testers;

import cz.vse.java.kedv00.adventura.api.IPortal;
import cz.vse.java.kedv00.adventura.api.Scenario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.vse.java.kedv00.adventura.testers.util.ConditionalPrinter.*;
import static cz.vse.java.kedv00.adventura.testers.util.FormatStrings.*;


/*******************************************************************************
 * Instance třídy {@code GameTester} testují aplikaci
 * zadanou továrním objektem podle požadavků zadaného
 * zadavatele – instance potomka třídy {@link TestVisitor}.
 * Tento zadavatel specifikuje sadu scénářů, podle nichž se testuje, spolu
 * s návštěvníkem, který ve vhodnou chvíli ověřuje požadované skutečnosti.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class GameTester
     extends ATester
{
//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Mapa konvertující název scénáře na daný scénář. */
    private final Map<String, Scenario> name2scenario;

//    /** Seznam scénářů, podle nichž se řešení testuje. */
//    protected final List<Scenario> testingScenarios;

    /** Návštěvník používaný při testu hry. */
    private final Class<? extends TestVisitor> visClass;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Vytvoří novou instanci testovací třídy testující hru poskytnutou
     * zadaným portálem, přičemž pro test používá instanci zadané
     * třídy návštěvníka.
     *
     * @param portal   Tovární objekt zprostředkovávající přístup
     *                 ke klíčovým objektům aplikace
     * @param visClass Třída návštěvníka, jejíž instance realizuje specifika
     *                ; testování zadané úlohy
     */
    public GameTester(IPortal portal,  Class<? extends TestVisitor> visClass)
    {
        super(portal);
        this.visClass      = visClass;
        this.name2scenario = new HashMap<>();
        for (Scenario scenario : portal.scenarios()) {
            name2scenario.put(scenario.name(), scenario);
        }
    }


//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================

    /***************************************************************************
     * Prověří hru tak, že ji postupně "zahraje" podle scénářů
     * se zadanými názvy. Zadané scénáře musí být testovací.
     */
    public String testGame()
    {
//        GameSummary gameSummary = new GameSummary(solutionTester, portal);

        //Prověř dodržení základních kontraktů
        new TGameInitTester(portal).verify();
        TestVisitor visitor = null;
        try {
            Object object = visClass.getConstructor(IPortal.class)
                                    .newInstance(portal);
            visitor = (TestVisitor)object;
        }
        catch (Exception ex) {
            throw new RuntimeException(
                "Chyba frameworku: nepodařilo se vytvořit návštěvníka", ex);
        }
        GameSummary gameSummary = new GameSummary(portal, visitor);
        TGameRunTester   tester = new TGameRunTester(gameSummary);
        StringBuilder        sb = new StringBuilder(
                "Testy hry podle jednotlivých scénářů skončily následovně:\n"
        );
        List<Scenario> scenarios = portal.scenarios();
        for (int index : LEVEL.getTestIndexes()) {
            Scenario scenario = scenarios.get(index);
            sb.append(tester.executeScenario(scenario)).append('\n');
        }
//        for (String name : LEVEL.testSequence()) {
//            Scenario scenario = name2scenario.get(name);
//            sb.append(tester.executeScenario(scenario)).append('\n');
//        }
//        StringBuilder msgBuilder = new StringBuilder(
//                                       "\nAccording the test results ");
//        if (gameSummary.getThrowable() != null) {
//            msgBuilder.append("PROGRAM CONTAIN CERTAIN ERRORS");
//        }
//        else {
//            msgBuilder.append(
//                    "program probably does not contain any serious error.");
//            gameSummary.addToScore(1);
//        }
//        msgBuilder.append(N_HASHES_N).append(HASHES_N);
//        String txt = msgBuilder.toString();
//        gameSummary.verboseMessageBuilder.append(txt);
//        prln(txt);
        return sb.toString();
    }

}
