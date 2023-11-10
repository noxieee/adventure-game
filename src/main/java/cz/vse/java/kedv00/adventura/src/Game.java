package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.*;
import java.util.*;

import static cz.vse.java.kedv00.adventura.src.Scenarios.*;

/*******************************************************************************
 * Instance třídy {@code Game} má na starosti řízení hry
 * a komunikaci s uživatelským rozhraním.
 * Je schopna akceptovat zadávané příkazy a poskytovat informace
 * o průběžném stavu hry a jejích součástí.
 * <p>
 * <b>Hra musí být definována jako jedináček (singleton)</b>
 * a kromě metod deklarovaných v interfejsu {@link IGame} musí definovat
 * statickou tovární metodu <b>{@code getInstance()}</b>
 * vracející instanci tohoto jedináčka.<br>
 * Splnění této podmínky nemůže prověřit překladač,
 * ale prověří ji až následné testy hry.
 *
 * @author Vojtěch KEDER
 * @version 2023_Summer
 */
public class Game implements IGame
{
    // CLASS ATTRIBUTES ////////////////////////////////////////////////////////

    /** Jediná existující instance hry. */
    private static final Game GAME = new Game();

    /** Názvy základních akcí. */
    private static final BasicActions BASIC_ACTIONS =
            new BasicActions(COMMAND_GOTO, COMMAND_TAKE,
                    COMMAND_PUT, COMMAND_HELP, COMMAND_END);

    // CLASS METHODS ///////////////////////////////////////////////////////////

    /***************************************************************************
     * Tovární metoda vracející odkaz na jedninou existující instanci dané hry.
     *
     * @return Instance dané hry
     */
    public static Game getInstance() { return GAME; }

    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////

    /***************************************************************************
     * Soukromý konstruktor definující jedinou instanci.
     * Protože je soukromý, musí být definován, i když má prázdné tělo.
     */
    private Game() {}

    // INSTANCE METHODS ////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí informaci o tom, je-li hra aktuálně spuštěná.
     * Spuštěnou hru není možno pustit znovu.
     * Chceme-li hru spustit znovu, musíme ji nejprve ukončit.
     *
     * @return Je-li hra spuštěná, vrátí {@code true},
     *         jinak vrátí {@code false}
     */
    @Override
    public boolean isAlive() { return Action.isAlive(); }

    /***************************************************************************
     * Vrátí odkaz na batoh, do nějž bude hráč ukládat sebrané objekty.
     *
     * @return Batoh, do nějž hráč ukládá sebrané objekty
     */
    @Override
    public IBag bag() { return Bag.getInstance(); }

    /***************************************************************************
     * Vrátí kolekci všech příkazů použitelných ve hře.
     *
     * @return Kolekce všech příkazů použitelných ve hře
     */
    @Override
    public Collection<Action> allActions() { return Action.allActions(); }

    /***************************************************************************
     * Vrátí odkaz na přepravku s názvy povinných příkazů, tj. příkazů pro
     * <ul>
     *   <li>přesun hráče do jiného prostoru,</li>
     *   <li>zvednutí objektu (odebrání z prostoru a vložení do batohu),</li>
     *   <li>položení objektu (odebrání z batohu a vložení do prostoru),</li>
     *   <li>vyvolání nápovědy,</li>
     *   <li>okamžité ukončení hry.</li>
     * </ul>
     *
     * @return Přepravka s názvy povinných příkazů
     */
    @Override
    public BasicActions basicActions()
    {
        return BASIC_ACTIONS;
    }

    /***************************************************************************
     * Vrátí odkaz na mapu obsahující aktuální stav příznaků
     * ovlivňujících proveditelnost nestandardních akcí.
     *
     * @return Požadovaná mapa
     */
    @Override
    public Map<String, Object> conditions() { return Action.CONDITIONS; }

    /***************************************************************************
     * Vrátí odkaz na mapu testů ověřujících splnění podmínek
     * nutných pro provedení nestandardních akcí.
     *
     * @return Požadovaná mapa
     */
    @Override
    public Map<String, IAction.ITest> tests()
    {
        return Action.TESTS;
    }

    /***************************************************************************
     * Vrátí odkaz na objekt reprezentující svět, v němž se hra odehrává.
     *
     * @return Svět, v němž se hra odehrává
     */
    @Override
    public IWorld world() { return World.getInstance(); }

    /***************************************************************************
     * Zpracuje zadaný příkaz a vrátí text zprávy pro uživatele.
     *
     * @param command Zadávaný příkaz
     * @return Textová odpověď hry na zadaný příkaz
     */
    @Override
    public String executeCommand(String command)
    {
        return Action.executeCommand(command);
    }

    /***************************************************************************
     * Ukončí celou hru a uvolní alokované prostředky.
     * Zadáním prázdného příkazu lze následně spustit hru znovu.
     */
    @Override
    public void stop()
    {
        Action.stop();
    }
}
