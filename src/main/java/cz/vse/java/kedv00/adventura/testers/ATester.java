package cz.vse.java.kedv00.adventura.testers;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/testers/ATester.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/

import cz.vse.java.kedv00.adventura.api.IPortal;
import cz.vse.java.kedv00.adventura.api.Scenario;
import cz.vse.java.kedv00.adventura.api.ScenarioStep;

import java.util.ArrayList;
import java.util.List;

import static cz.vse.java.kedv00.adventura.testers.util.ConditionalPrinter.*;
import static cz.vse.java.kedv00.adventura.testers.util.FormatStrings.*;



/*******************************************************************************
 * Třída {@code ATester} slouží jako rodičovská třída různých testovacích tříd,
 * jejichž instancím poskytuje metody testující některé základní vlastnosti
 * spolu s metodou definující reakci na chybu;
 * její instance si navíc pamatuje portál testované aplikace.
 * <p>
 * Třída nedefinuje abstraktní testovací metodu,
 * protože každý z jejich potomků může testovat něco jiného
 * s jinými požadovanými vstupními informacemi.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public abstract class ATester
{
//\CC== CLASS (STATIC) CONSTANT ATTRIBUTES (FIELDS) ============================

    /** Maximální povolená délka řádku. */
    public static final int MAX_LINE_LENGTH = 80;

    /** Seznam lokalizovaných zpráv. */
    private static final List<String> ERR_MSG;

    /** Index zprávy o vyhození výjimky. */
    private static final int EXC_THROWN_MSG;

    /** Index zprávy o nepovolené délce řádku. */
    private static final int LINE_LENGTH_MSG;



//\CV== CLASS (STATIC) VARIABLE ATTRIBUTES (FIELDS) ============================

    /** Testovaná hladina rozpracovanosti. */
    protected static Level LEVEL = Level.PORTAL;

    /** Přepravka se souhrnem informací zjištěných ze scénářů spravovaných
     *  testovaným správcem scénářů. */
    protected static ScenariosSummary SCENARIOS_SUMMARY = null;



//##############################################################################
//\CI== CLASS (STATIC) INITIALIZER (CLASS CONSTRUCTOR) =========================

    /***************************************************************************
     * Statický konstruktor nastavuje:<br>
     *  - implicitní ošetření nezachycených přerušení
     */
    static {
        ERR_MSG = new ArrayList<>();

        EXC_THROWN_MSG = ERR_MSG.size();
        ERR_MSG.add("Výjimka byla vyhozena ve vláknu %s:\n%s\n");

        LINE_LENGTH_MSG = ERR_MSG.size();
        ERR_MSG.add("Délka řádku je %s znaků, což je více než povolených "
             + "%d znaků.");

        Thread.setDefaultUncaughtExceptionHandler(
            (Thread t, Throwable e) ->
            {
                prf(N_DOUBLELINE_N +
                    ERR_MSG.get(EXC_THROWN_MSG),
                    t, stackTrace(e));
            });
    }



//\CM== CLASS (STATIC) GENERAL NON-PRIVATE METHODS =============================

    /***************************************************************************
     * Statická verze chybové metody vytiskne chybové hlášení
     * a vyhodí výjimku {@code TestException}.
     *
     * @param format Formát tisku chybového hlášení ve stylu {@code printf}.
     *               Bude ještě doplněn společnou úvodní a závěrečnou sekvencí.
     * @param arguments Případné další parametry k tisku
     */
    public static void ERRs(String format, Object... arguments)
    {
        String        message   = String.format(format, arguments);
        String        ensemble  = N_BEFORE_N + message + N_AFTER_N;
        TestException exception = new TestException(ensemble);

        throw exception;
    }


    /***************************************************************************
     * Instanční verze chybové metody vytiskne zadané chybové hlášení
     * a vyhodí výjimku {@code TestException}.
     *
     * @param reason    Důvod vyhození výjimky
     * @param scenario  Testovaný scénář
     * @param step      Testovaný krok
     * @param expected  Očekávaný stav chybného objektu
     * @param obtained  Obdržený stav chybného objektu
     */
    public void ERRi(String reason,
                     Scenario scenario, ScenarioStep step,
                     Object expected, Object obtained)
    {
        String message = String.format("\n" + ("E".repeat(70)+'\n').repeat(3) +
                "Řešení autora " + portal.authorNativeName() + '\n'
              + "Při vyhodnocování " + step.index + ". kroku scénáře "
              + scenario.name() + " byla odhalena chyba:\n"
              + "-".repeat(60)
              + "\nChybný objekt:    " + reason
              + "\nOčekávaný objekt: " + expected
              + "\nObdržený objekt:  " + obtained
              + "\n" + "=".repeat(60) + "\n"
        );
        System.out.println(message);
        System.out.println(
                "Očekávaný stav hry po provedení testovaného příkazu:\n"
              + step);
        System.out.println(
                "Obdržený stav hry po provedení testovaného příkazu:\n"
              + portal.game().state2string());
        TestException exception = new TestException(message);
        throw exception;
    }



//##############################################################################
//\IC== INSTANCE CONSTANT ATTRIBUTES (FIELDS) ==================================

    /** Portál identifikující autora a poskytující odkazy na objekty,
     * které se mají testovat. */
    protected final IPortal portal;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Konstruktor rodičovského podobjektu testerů.
     *
     * @param portal Portál zprostředkující odkazy na klíčové objekty aplikace
     */
    protected ATester(IPortal portal)
    {
        this.portal = portal;
    }


    /***************************************************************************
     * Konstruktor rodičovského podobjektu testeru portálu,
     * který je mimo jiné zodpovědný za nastavení hladiny testování,
     * kterou další testery získávají od společné rodičovské třídy.
     *
     * @param portal Portál poskytující informace o autorovi
     *               a zprostředkující odkazy na klíčové objekty aplikace
     * @param level  Hladina testování určující spouštěnou sadu testů.
     */
    protected ATester(IPortal portal, Level level)
    {
        this(portal);
        Class obtained  = this.getClass();
        Class requested = PortalTester.class;
        if (obtained != requested) {
            ERRs("Tento rodičovský konstruktor smí používat pouze třída "
                + "PortalTester.\nByl však použit ve třídě "
                + portal.getClass());
        }
        ATester.LEVEL = level;
    }



//\IM== INSTANCE GENERAL NON-PRIVATE METHODS ===================================

    /***************************************************************************
     * Prověří délku řádků zadaného textu.
     * Žádný řádek nesmí mít více než {@link #MAX_LINE_LENGTH} znaků.
     * Tento test využívají testery scénářů spolu s testery běhu hry.
     *
     * @param description Popis prověřovaného textu
     * @param text        Prověřovaný text
     */
    protected void checkMessageLength(String description, String text)
    {
        String[] lines = text.split("\\n");
        for (String line : lines) {
            if (line.length() > MAX_LINE_LENGTH) {
                ERRs(ERR_MSG.get(LINE_LENGTH_MSG),
                      description, MAX_LINE_LENGTH);
                return;
            }
        }
    }

}
