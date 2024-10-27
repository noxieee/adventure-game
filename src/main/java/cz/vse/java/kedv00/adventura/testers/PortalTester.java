package cz.vse.java.kedv00.adventura.testers;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */

import cz.vse.java.kedv00.adventura.api.IPortal;
import cz.vse.java.kedv00.adventura.api.Scenario;
import cz.vse.java.kedv00.adventura.api.ScenarioStep;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cz.vse.java.kedv00.adventura.testers.util.ConditionalPrinter.*;
import static cz.vse.java.kedv00.adventura.testers.util.FormatStrings.*;


/*******************************************************************************
 * Instance třídy {@code PortalTester} umějí otestovat
 * splnění základních povinných vlastností portálu.
 * Testují však pouze korektnost identifikací autora a přístupových metod.
 * Podle zadané hladiny testování pak případně spouští testování scénářů
 * a příslušného stupně rozpracovanosti hry.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class PortalTester
     extends ATester
{
//\CC== CLASS CONSTANTS (CONSTANT CLASS/STATIC ATTRIBUTES/FIELDS) ==============

    /** Toto není konstanta, ale proměnná.
     *  Je uváděna na začátku, aby byla snadno dohledatelná.
     *  Ovlivňuje, zda se při testu mají vypisovat podrobnější
     *  zprávy s uvedením všech kontrolovaných atributů ({@code true}),
     *  anebo jen příkaz a odpověď hry ({@code false}).
     */
    public static boolean VERBOSE = true;

    /** Název pseudokořenového balíčku aplikace. */
    public static final String ROOT = "adv23s";

    /** Seznam lokalizovaných zpráv. */
    private static final List<String> ERR_MSG;

    /** Index zprávy o různých jménech autorů. */
    private static final int INSTANCE_NOT_GET_MSG;

    /** Index textu "správce scénářů". */
    private static final int SCEANRIO_MGR;

    /** Index textu "hry". */
    private static final int GAME;

    /** Index textu "uživatelského rozhraní". */
    private static final int UI;



//##############################################################################
//\CI== CLASS (STATIC) INITIALIZER (CLASS CONSTRUCTOR) =========================

    static {
        ERR_MSG = new ArrayList<>();

        SCEANRIO_MGR = ERR_MSG.size();
        ERR_MSG.add("správce scénářů");

        GAME = ERR_MSG.size();
        ERR_MSG.add("hry");

        UI = ERR_MSG.size();
        ERR_MSG.add("správce scénářů");

        INSTANCE_NOT_GET_MSG = ERR_MSG.size();
        ERR_MSG.add("Při pokusu o získání instance %s byla vyhozena výjimka %s"
           + "\nTřída použitého generátoru: %s");

    }



//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Kompletní zpráva o průběhu testu. */
    private final StringBuilder verboseMessageBuilder;

    /** Návštěvník používaný při testu hry. */
    private final Class<? extends TestVisitor> visClass;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Vytvoří objekt schopný testovat správce scénářů.
     *
     * @param portal Portál poskytující informace o autorovi
     *               a zprostředkující odkazy na klíčové objekty aplikace
     * @param level  Hladina testování určující spouštěnou sadu testů.
     */
    public PortalTester(IPortal portal, Level level)
    {
        this(portal, level, TestVisitor.class);
    }


    /***************************************************************************
     * Vytvoří objekt schopný testovat portál.
     *
     * @param portal Portál poskytující informace o autorovi
     *               a zprostředkující odkazy na klíčové objekty aplikace
     * @param level  Hladina testování určující spouštěnou sadu testů.
     */
    public PortalTester(IPortal portal, Level level,
                        Class<? extends TestVisitor> visClass)
    {
        super(portal, level);
        this.visClass = visClass;
        this.verboseMessageBuilder = new StringBuilder();
    }



//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================

    /***************************************************************************
     * Prověří korektnost metod zadaného továrního objektu,
     * přičemž sada prověřovaných metod je dána zadanou hladinou.
     *
     */
    public void test()
    {
        printProlog();
        if (LEVEL.compareTo(Level.PORTAL) == 0) { return; }
        printInvitation();
        String epilog = ScenarioTester.testScenariosFrom(portal);
        if (LEVEL.compareTo(Level.SCENARIOS) > 0) {
            new ArchitectureTester(portal).run();
            epilog += "Test základní architektury: OK\n";
        }
        if (LEVEL.compareTo(Level.WORLD) >= 0) {
            epilog += new GameTester(portal, visClass).testGame();
        }
        System.out.println(epilog);
    }


    /***************************************************************************
     * Připraví a vytiskne úvodní text oznamující testování dané aplikace.
     */
    private void printProlog() {
        initResultInformation();    //Inicializace
        Date startTime =  new Date();
        String message = String.format("\n\n"
                      +  "Autor testované továrny: %s\n"
                      +  "Instance třídy: %s%n"
                      +  "########## START: %ta %<TF — %<tT\n%s",
                         portal.authorString(),
                         portal.getClass().getName(), startTime, HASHES);
        System.out.println(message);
    }


    /***************************************************************************
     * Připraví a vytiskne úvodní text oznamující testování dané aplikace.
     */
    private void printInvitation() {
        List<Scenario> scenarios;
        String message = null;
        try {
            scenarios = portal.scenarios();
            message   = scenarios.get(0).steps().get(0).message;
        } catch(Exception ex) {
            ERRs("Portál nedodal korektní scénář HAPPY.");
        }
        System.out.println(message + N_HASHES);
    }


//\IP== INSTANCE PRIVATE AND AUXILIARY METHODS =================================

    /***************************************************************************
     * Inicializuje {@link StringBuilder}, do nějž se budou zapisovat výsledky.
     */
    private void initResultInformation()
    {
        verboseMessageBuilder.delete(0, verboseMessageBuilder.length());
    }


    /***************************************************************************
     * Ověří, že udávané jméno autora odpovídá zadaným konvencím,
     * tj. že obsahuje nejméně dvě slova, první z nich je velkými písmeny
     * a druhé začíná velkým písmenem.
     *
     * @return Vektor stringů s jednotlivými slovy jména autora
     *         zbavenými diakritiky
     */
    private String[] verifyAuthor()
    {
        //Jméno zbavíme diakritiky pro snazší následnou kontrolu
        String authorASCII = Normalizer.normalize(portal.authorName(),
                                                  Normalizer.Form.NFD)
                                       .replaceAll("[^\\p{ASCII}]", "");
        if (! authorASCII.equals(authorASCII.trim())) {
            ERRs("Jméno autora obsahuje počáteční či koncové bílé znaky\n«"
                + portal.authorName() + "»");
        }
        String[] wordsInName = authorASCII.split(" ");
        String[] check       = authorASCII.split("\\s+");
        if (wordsInName.length != check.length) {
            ERRs("Špatně použité bílé znaky ve jméně autora\n«"
                + portal.authorName() + "»");
        }
        if ((wordsInName   .length    < 2)  ||
            (wordsInName[0].length() == 0)  ||
            (wordsInName[1].length() == 0))
        {
            ERRs("Autor nemá uvedeno příjmení + křestní jméno: "
                + portal.authorName());
        }
        String surname   = wordsInName[0];
        String firstName = wordsInName[1];
        if (! surname.matches("[A-Z]+")) {
            ERRs("Prvním slovem jména autora není příjmení "
                + "zapsané velkými písmeny\n" + portal.authorName());
        }
        if (! firstName.matches("[A-Z][a-z]+")) {
            ERRs("Druhé slovo jména autora nemá "
                + "první písmeno velké a ostatní malá\n"
                + portal.authorName());
        }
        String id = portal.authorID();
        if (! id.matches("[0-9A-Z_]+")) {
            ERRs("Identifikační řetězec autora obsahuje nepovolené znaky\n"
                + "(neobsahuje jen číslice, velká ASCII-písmena a podtržení)\n«"
                + portal.authorID() + "»");
        }

        return wordsInName;
    }


    /***************************************************************************
     * Vypíše úvodní zprávu hry, ze které by se mělo dát odhadnout,
     * o čem hra bude.
     */
    private void verifyPackage(String id, String surname)
    {
        String pkgName   = (id + '_' + surname).toLowerCase();
        String requested = (ROOT + "._" + portal.authorGroup() + '.'
                         + id + '_' + surname).toLowerCase();
        String obtained  = portal.getClass().getPackage().getName();
        if (obtained.equals(requested)) { return; }

        ERRs("Balíček neodpovídá požadovanému: \n"
            + "  Požadováno: " + requested + "\n"
            + "  Obdrženo:   " + obtained  );
    }


    /***************************************************************************
     * Vypíše úvodní zprávu hry, ze které by se mělo dát odhadnout,
     * o čem hra bude.
     */
    private void writeInvitation()
    {
        ScenarioStep initialStep = portal.scenarios().get(0).steps().get(0);
        prf("Welcome message:\n" +
            "================\n%s\n", initialStep.message);
    }

}
