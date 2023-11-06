package cz.vse.java.kedv00.adventura.api;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */


import java.util.List;

/*******************************************************************************
 * Instance třídy {@code CK_Scenarios} představují scénáře,
 * podle kterých je možno hrát hru, pro kterou jsou určeny.
 * Aby bylo možno jednotlivé scénáře od sebe odlišit,
 * je každý pojmenován a má přiřazen typ, podle které lze blíže určit,
 * k čemu je možno daný scénář použít.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class Scenario
{
//\CC== CLASS CONSTANTS (CONSTANT CLASS/STATIC ATTRIBUTES/FIELDS) ==============

    /** Povinné názvy prvních pěti scénářů. */
    List<String> SCEN_NAME = List.of("HAPPY", "BASIC", "MISTAKES",
                                     "MISTAKES_NS",    "FOURTH");



//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Typ scénáře. */
    private final TypeOfScenario type;

    /** Kroky scénáře. */
    private final List<ScenarioStep> steps;

    /** Název scénáře. */
    private final String name;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Vytvoří scénář zadaného typu.
     *
     * @param type  Typ vytvářeného scénáře
     * @param steps Kroky vytvářeného scénáře
     */
    public Scenario(TypeOfScenario type, ScenarioStep... steps)
    {
        this(type, type.requiredName(), steps);
    }

    /***************************************************************************
     * Vytvoří scénář zadaného typu.
     *
     * @param type  Typ vytvářeného scénáře
     * @param steps Kroky vytvářeného scénáře
     */
    public Scenario(TypeOfScenario type, String name, ScenarioStep... steps)
    {
        this.type  = type;
        this.name  = name;
        this.steps = List.of(steps);
    }



//\IG== INSTANCE GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí název daného scénáře.
     *
     * @return Název daného scénáře
     */
    public String name() {
        return name;
    }


    /***************************************************************************
     * Vrátí seznam kroků daného scénáře.
     *
     * @return Seznam kroků daného scénáře
     */
    public List<ScenarioStep> steps() {
        return this.steps;
    }


    /***************************************************************************
     * Vrátí seznam kroků daného scénáře.
     *
     * @return Seznam kroků daného scénáře
     */
    public String toString() {
        return name;
    }


    /***************************************************************************
     * Vrátí typ daného scénáře.
     *
     * @return Typ daného scénáře
     */
    public TypeOfScenario type() {
        return this.type;
    }



    /***************************************************************************
     * Vypíše na standardní výstup simulovaný průběh hry podle daného scénáře,
     * přičemž u každého kroku vypíše pouze zadávaný příkaz a odpověď hry.
     */
    public void simulateSimple()
    {
        for(ScenarioStep step : steps()) {
            System.out.println(
                "\n" + step.index + ". " + step.command
                  + "\n----------"
                  + "\n" + step.message
                  + "\n====================================================="
            );
        }
    }


    /***************************************************************************
     * Vypíše na standardní výstup simulovaný průběh hry podle daného scénáře,
     * přičemž u každého kroku vypíše zadávaný příkaz, odpověď hry<br>
     * a za nimi informace o plánovaném stavu hry po provedeném příkazu.
     */
    public void simulateComplex()
    {
        for(ScenarioStep step : steps()) {
            System.out.println(step
                + "\n==================================================\n"
            );
        }
    }


}
