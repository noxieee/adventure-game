package cz.vse.java.kedv00.adventura.testers;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/testers/ArchitectureTester.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/

import cz.vse.java.kedv00.adventura.api.*;

import static cz.vse.java.kedv00.adventura.testers.util.FormatStrings.*;


/*******************************************************************************
 * Instance třídy {@code ArchitectureTester} testují základní architekturu hry,
 * tj. prověřují, zda portál opravdu odkazuje na instanci třídy implementující
 * interfejs {@code IGame} a zda její metody vracejí požadované objekty.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public class ArchitectureTester
     extends ATester
{
//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Odkaz na instanci testované hry. */
    private final IGame game;

    /* Odkaz na startovní krok šťastného scénáře testované hry. */
    private final ScenarioStep startStep;



//\IV== INSTANCE VARIABLES (VARIABLE INSTANCE ATTRIBUTES/FIELDS) ===============

    /* Úvodní část textu řady testovacích metod. */
    private String msgStart;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Konstruktor rodičovského podobjektu testerů.
     *
     * @param portal Portál zprostředkující odkazy na klíčové objekty aplikace
     */
    public ArchitectureTester(IPortal portal)
    {
        super(portal);
        game        = portal.game();
        startStep   = SCENARIOS_SUMMARY.startStep;
    }



//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================

    /***************************************************************************
     * Předpokládá, že testy scénářů již proběhly a uložily užitečné informace
     * do rodičovské {@code SCENARIOS_SUMMARY}.
     */
    public void run()
    {
        setMsgStart(game);
        System.out.println(msgStart + BEFORE_N + SCENARIOS_SUMMARY
                                    + AFTER_N);
        verify_isAlive();
        verify_basicActions();
        verify_allActions();
//        verify_conditions();
//        verify_tests();
        verify_executeCommand();
        verify_stop();
        verify_bag();
        verify_world();
    }



//\IP== INSTANCE PRIVATE AND AUXILIARY METHODS =================================

    private void setMsgStart(Object object)
    {
        String clsName = object.getClass().getName();
        msgStart = "Test třídy: " + clsName + "\n";
    }


    private void ERR(String message)
    {
        ERRs(msgStart + message);
    }


    private void no_exception(String name, Throwable th)
    {
        if (th instanceof TestException) throw (TestException)th;
        ERR("Volání metody " + name + "() "
          + "nesmí vyhazovat výjimku\n" + th);
    }


    private void verify_isAlive()
    {
        try {
            boolean isAlive = game.isAlive();
            if (isAlive == false)  return;
            ERR("Metoda isAlive() nevrací false oznamující, že hra neběží");
        }
        catch (Exception ex) {
            no_exception("isAlive", ex);
        }
    }


    private void verify_basicActions()
    {
        try {
            var expected = SCENARIOS_SUMMARY.basicActions;
            var obtained = game.basicActions();
            if (! expected.equals(obtained)) {
                ERR("Názvy základních akcí dodaných metodou basicActions()\n"
                  + "neodpovídají názvům definovaným ve scénáři HAPPY\n"
                  + "Objednáno: " + expected + "\n"
                  + "Dodáno:    " + obtained);
            }
        }
        catch (Exception ex) {
            no_exception("basicActions", ex);
        }
    }


    private void verify_allActions()
    {
        try {
            var actions = game.allActions();
            if (actions == null) {
                ERR("Metoda allActions() nevrací (alespoň prázdnou) "
                  + "kolekci dostupných akcí");
            }
        }
        catch (Exception ex) {
            no_exception("allActions", ex);
        }
    }


    private void verify_executeCommand()
    {
        String startErr =
              "Odpověď hry po odstartování příkazem executeCommand(\"\")\n";
        try {
            String expected = startStep.message.toLowerCase();
            int    length   = expected.length();
            String answer   = game.executeCommand("");
            if (answer.length() < length) {
                ERR(startErr + "je kratší než objednává scénář HAPPY");
            }
            String obtained = answer.substring(0, length).toLowerCase();
            if (expected.equals(obtained)) { return; }
            ERR(startErr + " se liší od požadované");
        }
        catch (Exception ex) {
            no_exception("executeCommand", ex);
        }
    }


    private void verify_stop()
    {
        try {
            game.stop();
        }
        catch (Exception ex) {
            no_exception("stop", ex);
        }
    }


    private void verify_bag()
    {
        IBag bag = null;
        try {
            bag = game.bag();
            if (bag == null) {
                ERR("Metoda bag() nevrací požadovaný batoh");
            }
            if (game.bag() != bag) {
                ERR("Batoh vracený metodou bag() není jedináček");
            }
        }
        catch (Exception ex) {
            no_exception("bag", ex);
        }
        setMsgStart(bag); {
            verifyBag_capacity(bag);
//            verifyBag_initialize(bag);
            verifyBag_items(bag);
            verifyBag_item(bag);
//            verifyBag_addItem(bag);
//            verifyBag_removeItem(bag);
//            verifyBag_initializeItems(bag);
        } setMsgStart(game);
    }

    private void verifyBag_initializeItems(IBag bag)
    {
        try {
            bag.initializeItems();
        }
        catch (Exception ex) {
            no_exception("initializeItems", ex);
        }
    }

    private void verifyBag_removeItem(IBag bag)
    {
        try {
            bag.removeItem(null);
        }
        catch (Exception ex) {
            no_exception("removeItem", ex);
        }
    }

    private void verifyBag_addItem(IBag bag)
    {
        try {
            bag.addItem(null);
        }
        catch (Exception ex) {
            no_exception("addItem", ex);
        }
    }

    private void verifyBag_item(IBag bag)
    {
        try {
            bag.item("");
        }
        catch (Exception ex) {
            no_exception("item", ex);
        }
    }

    private void verifyBag_items(IBag bag)
    {
        try {
            bag.items();
        }
        catch (Exception ex) {
            no_exception("items", ex);
        }
    }

    private void verifyBag_initialize(IBag bag)
    {
        try {
            bag.initialize();
        }
        catch (Exception ex) {
            no_exception("initialize", ex);
        }
    }

    private void verifyBag_capacity(IBag bag)
    {
        int capacity = 0;
        try {
            capacity = bag.capacity();
        }
        catch (Exception ex) {
            no_exception("capacity", ex);
        }
    }


    private void verify_world()
    {
        IWorld world = null;
        try {
            world = game.world();
            if (world == null) {
                ERR("Metoda world() nevrací požadovaný objekt");
            }
            if (game.world() != world) {
                ERR("Svět vracený metodou world() není jedináček");
            }
        }
        catch (Exception ex) {
            no_exception("world", ex);
        }
        setMsgStart(world); {
//            verifyWorld_places(world);
//            verifyWorld_currentPlace(world);
            verifyWorld_place(world);
//            verifyWorld_setCurrentPlace(world);
//            verifyWorld_initialize(world);
        } setMsgStart(game);
    }


    private void verifyWorld_places(IWorld world)
    {
        try {
            var places = world.places();
            if (places == null) {
                ERR("Metoda places() nevrací požadovanou kolekci");
            }
        }
        catch (Exception ex) {
            no_exception("initialize", ex);
        }
    }


    private void verifyWorld_currentPlace(IWorld world)
    {
        try {
            world.currentPlace();
        }
        catch (Exception ex) {
            no_exception("currentPlace", ex);
        }
    }


    private void verifyWorld_place(IWorld world)
    {
        try {
            if (world.place("") != null) {
                ERR("Dokud není hra rozběhnuta,\n"
                  + "má metoda place(String) vracet prázdný odkaz - null");
            }
        }
        catch (Exception ex) {
            no_exception("place", ex);
        }
    }


    private void verifyWorld_setCurrentPlace(IWorld world)
    {
        try {
            world.initialize();
        }
        catch (Exception ex) {
            no_exception("initialize", ex);
        }
    }


    private void verifyWorld_initialize(IWorld world)
    {
        try {
            world.initialize();
        }
        catch (Exception ex) {
            no_exception("initialize", ex);
        }
    }

}
