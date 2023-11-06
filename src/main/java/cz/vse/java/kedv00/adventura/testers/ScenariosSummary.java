package cz.vse.java.kedv00.adventura.testers;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/testers/ScenariosSummary.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/

import cz.vse.java.kedv00.adventura.api.BasicActions;
import cz.vse.java.kedv00.adventura.api.Scenario;
import cz.vse.java.kedv00.adventura.api.ScenarioStep;
import cz.vse.java.kedv00.adventura.api.TypeOfStep;

import java.util.*;

import static cz.vse.java.kedv00.adventura.testers.util.FormatStrings.*;
import static cz.vse.java.kedv00.adventura.testers.util.Util.lineWrapper;



/***************************************************************************
 * Instance třídy {@code ScenariosSummary} představují přepravky
 * pro uchování informacích charakterizujících svět hry
 * na základě vyhodnocení scénářů a jejich kroků.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class ScenariosSummary
{
//##############################################################################
//\IC== INSTANCE CONSTANT ATTRIBUTES (FIELDS) ==================================

    /** Maximální povolené délka zalamovaných výstupních řádků. */
    private final int LINE_LENGTH = 80;

    /** Informace, zda je scénář bez chyb. */
    public final boolean ok;

    /** Množina názvů všech dosud zmíněných prostorů. */
    public final Set<String> mentionedPlaces;

    /** Množina názvů všech doposud zadaných akcí. */
    public final Set<String> enteredActions;

    /** Množina názvů všech dosud zahlédnutých objektů. */
    public final Set<String> seenItems;

    /** Počáteční krok v základním úspěšném scénáři. */
    public final ScenarioStep startStep;

    /** Poslední krok základního úspěšného scénáře. */
    public final ScenarioStep endStep;

    /** Mapa mapující typy základních (povinných) příkazů na jejich názvy. */
    public final BasicActions basicActions;

    /** Pole seznamů názvů nestandardních akcí. */
    private final List<Set<String>> nsActions;

    /** Mapa mapující názvy příkazů na skupinu jejich typu. */
    public final Map<String, TypeOfStep> name2typeGroup;

    /** Seznam prověřovaných scénářů. */
    public final List<Scenario> scenarios;

    /** Výsledky prověření jednotlivých scénářů:
     *  {@code true} vyhověl, {@code false} nevyhověl. */
    public final boolean[] results;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***********************************************************************
     * Konstruktor inicializující atributy přepravky.
     *
     * @param ok              Informace, zda je scénář bez chyb
     * @param mentionedPlaces Množina názvů všech dosud zmíněných prostorů
     * @param enteredActions  Množina názvů všech doposud zadaných příkazů
     * @param seenItems       Množina názvů všech dosud zahlédnutých objektů
     * @param startStep       Počáteční krok v základním úspěšném scénáři
     * @param endStep         Poslední krok základního úspěšného scénáře
     * @param basicActions    Mapa mapující typy příkazů na jejich názvy
     * @param nsActions       Pole seznamů názvů nestandardních akcí
     * @param name2typeGroup  Mapa názvů příkazů na skupinu jejich typu
     * @param scenarios       Seznam prověřovaných scénářů
     * @param results         Výsledky prověření jednotlivých scénářů:
     *                        {@code true} vyhověl, {@code false} nevyhověl
     */
    public ScenariosSummary(boolean ok,
                     Set<String>        mentionedPlaces,
                     Set<String>        enteredActions,
                     Set<String>        seenItems,
                     ScenarioStep       startStep,
                     ScenarioStep       endStep,
                     BasicActions       basicActions,
                     List<Set<String>>  nsActions,
                     Map<String,TypeOfStep>     name2typeGroup,
                     List<Scenario>  scenarios,
                     boolean[]                  results)
    {
        this.ok = ok;
        this.mentionedPlaces= mentionedPlaces;
        this.enteredActions = enteredActions;
        this.seenItems      = seenItems;
        this.startStep      = startStep;
        this.endStep        = endStep;
        this.basicActions   = basicActions;
        this.nsActions      = nsActions;
        this.name2typeGroup = name2typeGroup;
        this.scenarios      = Collections.unmodifiableList(scenarios);
        this.results        = results;
    }


    /***********************************************************************
     * Konstruktor vytvářející prázdnou přepravku pro dočasné použití.
     */
    @SuppressWarnings( {"unchecked", "rawtypes"})
    public ScenariosSummary()
    {
        this.ok = false;
        this.mentionedPlaces= Collections.EMPTY_SET;
        this.enteredActions = Collections.EMPTY_SET;
        this.seenItems      = Collections.EMPTY_SET;
        this.startStep      = new ScenarioStep("AUXILIARY-START");
        this.endStep        = new ScenarioStep("AUXILIARY-STOP");
        this.basicActions   = new BasicActions("M", "D", "U", "H", "E");
        this.nsActions      = List.of(Collections.EMPTY_SET,
                                      Collections.EMPTY_SET,
                                      Collections.EMPTY_SET,
                                      Collections.EMPTY_SET);
        this.name2typeGroup = Collections.EMPTY_MAP;
        this.scenarios      = Collections.EMPTY_LIST;
        this.results        = new boolean[] {};
    }



//\IM== INSTANCE GENERAL NON-PRIVATE METHODS ===================================

    /***************************************************************************
     * Vrátí textový podpis instance s hodnotami všech jejích atributů.
     *
     * @return Textový podpis instance
     */
    @Override
    public String toString()
    {
        String result = "Souhrnné informace testu scénářů (ScenariosSummary):"
            + "\nÚspěšnost testu:  " + ok
            + lineWrapper(LINE_LENGTH,
              "\nZmíněné prostory: ",  c(mentionedPlaces))
            + lineWrapper(LINE_LENGTH,
              "\nZadané akce:      ",  c(enteredActions))
            + lineWrapper(LINE_LENGTH,
              "\nViděné h.objekty: ",  c(seenItems))
            + lineWrapper(LINE_LENGTH,
              "\nNázvy povinných:  ",  c(basicActions))
            + lineWrapper(LINE_LENGTH,
              "\nPomocné akce:     ",  c(nsActions))
            + lineWrapper(LINE_LENGTH,
              "\nNázev akce -> typ:",  c(name2typeGroup))
            + lineWrapper(LINE_LENGTH,
              "\nTestované scénáře:",  c(scenarios))
            + "\nVýsledky:         " + Arrays.toString(results)
//            + "\nSpolečný startovní krok:"                 + startStep
//            + "\nÚspěšný koncový krok šťastného scénáře: " + endStep
            + N_DOUBLELINE_N;
        return result;
    }



//\IP== INSTANCE PRIVATE AND AUXILIARY METHODS =================================

    private String c(Object o)
    {
        if (o == null) { return "null"; }
        return o.toString();
    }

}
