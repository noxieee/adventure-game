/* The file is saved in UTF-8 codepage.
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package cz.vse.java.kedv00.adventura.testers;

import cz.vse.java.kedv00.adventura.api.IGame;
import cz.vse.java.kedv00.adventura.api.IPortal;
import cz.vse.java.kedv00.adventura.api.Scenario;
import cz.vse.java.kedv00.adventura.api.ScenarioStep;


/*******************************************************************************
 * Instance třídy {@code TestVisitor} slouží především jako
 * rodičovské podobjekty návštěvníků schopných ovlivnit
 * způsob testování hry podle jim známých dispozic.
 *
 * Slouží především k tomu, aby bylo možno doplnit základní testy
 * o doplňkové kontroly prověřující, zda byly správně zapracovány
 * všechny úpravy vyžadované při obhajobách aplikace.
 *
 * Tato třída definuje všechny virtuální metody návštěvníka jako prázdné s tím,
 * že pro každé zadání požadované modifikace pro obhajobu
 * bude definován odpovídající potomek kontrolující správnost řešení.
 *
 * Takto definovaný návštěvník je použitelný pro základní verzi hry.
 * Návštěvníci pro modifikované verze doplní některé dodatečné kontroly
 * a případně i s nimi spojené inicializační akce.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class TestVisitor
{
//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Tovární objekt schopný dodat hlavní objekty aplikace. */
    protected final IPortal portal;

    /** Testovaná hra. */
    protected final IGame game;



//\IV== INSTANCE VARIABLES (VARIABLE INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Testovací scénář. */
    protected Scenario scenario;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===============================

    /***********************************************************************
     * Vytvoří návštěvníka, schopného prověřit, nakolik testované
     * řešení splňuje modifikované zadání prověřované zadaným testerem.
     *
     * @param portal Portál do testované aplikace schopný identifikovat
     *               autora a dodat klíčové objekty testované aplikace
     */
    public TestVisitor(IPortal portal)
    {
        this.portal = portal;
        IGame iGame;
        try                 { iGame = portal.game(); }
        catch(Exception ex) { iGame = null; }
        this.game = iGame;
    }



//\IG== INSTANCE GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí testovanou hru. Nutnost použít zjišťovací metody zabezpečí,
     * aby potomci nemohli omylem tento odkaz změnit
     *
     * @return Testovaná hra
     */
    public final IGame game()
    {
        return game;
    }


    /***************************************************************************
     * Vrátí informaci o tom, je-li v prověřovaném zadání povoleno
     * testování hry i v případě, kdy správce scénářů neprojde verifikací.
     *
     * @return Je-li v prověřovaném zadání povoleno testování hry
     *         i v případě, kdy správce scénářů neprojde verifikací,
     *         vrátí {@code true}, jinak vrátí {@code false}
     */
    public boolean isAllowedImperfectSM()
    {
        return false;
    }



//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =============================

    /***********************************************************************
     * Akce, která se má provést před spuštěním hry.
     * Cílem metody je prověřit scénáře a případné další pomocné kódy.
     *
     * @param scenario Scénář, podle nějž se hra testuje
     */
    public void beforeGameStart(Scenario scenario)
    {
    }


    /***********************************************************************
     * Akce, která se má provést po provedení startovacího kroku hry
     * (tj. ve chvíli, kde je hra již inicializována), ale před jeho
     * testem, tj. před ověřením, že stav hry odpovídá scénáři.
     * Cílem metody je připravit potřebné informace o testované hře.
     *
     * @param scenario Scénář, podle nějž se hra testuje
     */
//    @Override
    public void afterGameStart(Scenario scenario)
    {
    }


    /***********************************************************************
     * Akce, která se má provést před zadáním příkazu hře.
     * Používá se zřídka. Při její definici je třeba vzít v úvahu,
     * že se u startovacího kroku provede ještě před odstartováním hry,
     * tj. před metodou afterGameStart.
     *
     * @param step     Aktuálně testovaný krok scénáře
     * @param answer   Zpráva vrácená hrou v daném kroku
     */
//    @Override
    public void beforeEnteringCommand(ScenarioStep step, String answer)
    {
    }


    /***********************************************************************
     * Akce, která se má provést po provedení kroku hry,
     * ale před jeho testem, tj. před ověření, že stav hry odpovídá scénáři.
     *
     * @param step     Aktuálně testovaný krok scénáře
     * @param answer   Zpráva vrácená hrou v daném kroku
     */
//    @Override
    public void beforeStepTest(ScenarioStep step, String answer)
    {
    }


    /***********************************************************************
     * Akce, která se má provést po testu aktuálního kroku.
     *
     * @param step      Aktuálně testovaný krok scénáře
     * @param answer   Zpráva vrácená hrou v daném kroku
     */
//    @Override
    public void afterStepTest(ScenarioStep step, String answer)
    {
    }


    /***********************************************************************
     * Akce, která se má provést po testu posledního kroku.
     *
     * @param throwable      Objekt případně vyhozené chyby či výjimky
     * @param verboseMessage Kompletní zpráva o průběhu testu
     */
//    @Override
    public void afterGameEnd(Throwable throwable, String verboseMessage)
    {
    }


    /***************************************************************************
     * Akce, která se má provést po testu posledního kroku.
     *
     * @param summary Přepravka s kompletními informacemi o průběhu hry
     */
    public void afterGameEnd(GameSummary summary)
    {
        afterGameEnd(summary.getThrowable(), summary.getVerboseMessage());
    }

}
