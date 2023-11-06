/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package cz.vse.java.kedv00.adventura.testers;

import cz.vse.java.kedv00.adventura.api.TypeOfScenario;

import java.util.Arrays;



/*******************************************************************************
 * Instance výčtového typu {@code Level} reprezentují úrovně testování.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public enum Level
{
//\CE== VALUES OF THE ENUMERATION TYPE =========================================

    /** Portál s identifikací autora a přístupovými metodami.   0_ */
    PORTAL      (0),
    /** Šťastný scénář bez deklarací příznaků.                  1a */
    HAPPY       (1),
    /** 4 základní scénáře včetně deklarací příznaků.           2b */
    SCENARIOS   (4),
    /** Přítomnost základních požadovaných typů a metod.        3c */
    ARCHITECTURE(4),
    /** Hru se podaří úspěšně odstartovat a ukočit.             4d */
    START       (4, 1),
    /** Hra úspěšně vybuduje svůj svět.                         5e */
    WORLD       (4, 1),
    /** Zprovoznění základních akcí.                            6f */
    BASIC       (4, 1, 1),
    /** Základní akce jsou navržené robustní.                   7g */
    MISTAKES    (4, 1, 1, 2),
    /** Zprovoznění hry podle šťastného scénáře.                8h */
    RUNNING     (4, 0, 0, 2),
    /** Včetně testu robustnosti nestandardních akcí. 9         9i */
    WHOLE       (4, 0, 0, 2, 3),
    /** Test aplikace s nadstavbovými úpravami.                10j */
    MODIFIED    (4, 0, 0, 2, 3),
    /** Test upravené aplikace s dalším scénářem.              11k */
    EXTENDED    (5, 0, 0, 2, 3, 4),
    /** Test upravené aplikace s několika dalšími scénáři.     12l */
    FREE    (1, 0),
    ;



//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Minimální požadovaný počet definovaných scénářů. */
    private int minScenarios;

    /** Posloupnost názvů scénářů, jak budou postupně zadávány. */
    private String[] testSequence;

    /** Posloupnost názvů scénářů, jak budou postupně zadávány. */
    private int[] testIndexes;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Vytvoří hladinu se zadaným minimálním požadovaným počtem definovaných
     * scénářů a posloupnost indexů scénářů, jak by měly být postupně zadávány.
     */
    private Level(int minScenarios, int... testSequence)
    {
        this.minScenarios = minScenarios;
        this.testIndexes  = testSequence;
        this.testSequence = new String[testSequence.length];
        int t = 0;
        for (int i : testSequence) {
            int v = TypeOfScenario.values().length;
            this.testSequence[t++] = TypeOfScenario.values()[i]
                                                   .requiredName();
        }
    }



//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================

    /***************************************************************************
     * Vrátí posloupnost indexů scénářů, jak by měly být postupně zadávány.
     */
    public int minScenarios()
    {
        return minScenarios;
    }


    /***************************************************************************
     * Vrátí minimální požadovaný počet definovaných scénářů.
     */
    public String[] testSequence()
    {
        return Arrays.copyOf(testSequence, testSequence.length);
    }


    /***************************************************************************
     * Vrátí minimální požadovaný počet definovaných scénářů.
     */
    public int[] getTestIndexes()
    {
        return Arrays.copyOf(testIndexes, testIndexes.length);
    }


    /***************************************************************************
     * Vrátí minimální požadovaný počet definovaných scénářů.
     */
    public void setTestIndexes(int... sequence)
    {
        if (this != FREE) {
            throw new RuntimeException(
                    "\nNastavovat posloupnost testovacích scénářů \n" +
                    "je možno pouze pro hladinu FREE."
            );
        }
        minScenarios = 0;
        for (int i : sequence) {
            if (i > minScenarios) { minScenarios = i; }
        }
        minScenarios += 1;  //Aby se započítala nula
        testIndexes = Arrays.copyOf(sequence, sequence.length);
    }

}
