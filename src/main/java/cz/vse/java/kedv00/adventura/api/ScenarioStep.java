package cz.vse.java.kedv00.adventura.api;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */

import java.util.*;


/*******************************************************************************
 * Třída {@code ScenarioStep} slouží jako přepravka k uchovávání informaci
 * o jednotlivých zadávaných příkazech scénáře
 * a o očekávaných stavech programu po jejich provedení.
 * <p>
 * Kroky scénáře obsahují informace sloužící k ověření
 * správné reakce hry na příkaz zadávaný v příslušném kroku scénáře.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class ScenarioStep
{
//\CC== CLASS CONSTANTS (CONSTANT CLASS/STATIC ATTRIBUTES/FIELDS) ==============

    /** Zpráva v kroku scénáře, u níž se nebude testovat shoda
     *  se zprávou vrácenou hrou v reakci na zadaný příkaz.  */
    public static final String IGNORED_MESSAGE = "¤¤¤ TEST ¤¤¤";

    /** Fiktivní předstartovní krok používaný některými testy. */
    public static final ScenarioStep NOT_START_STEP;

    /** Množina typů kroků vyžadujících zadání požadovaných podmínek. */
    private static final Set<TypeOfStep> withNeeds =
            Set.of(TypeOfStep.tsNS_0, TypeOfStep.tsNS_1, TypeOfStep.tsNS_2, TypeOfStep.tsNS_3, TypeOfStep.tsSUCCESS,
                   TypeOfStep.tsNS0_WrongCond, TypeOfStep.tsNS1_WrongCond, TypeOfStep.tsNS2_WrongCond,
                   TypeOfStep.tsNS3_WrongCond);

    /** Množina typů kroků vyžadujících zadání nastavovaných podmínek. */
    private static final Set<TypeOfStep> withSets =
            Set.of(TypeOfStep.tsSTART, TypeOfStep.tsNS_0, TypeOfStep.tsNS_1, TypeOfStep.tsNS_2,  TypeOfStep.tsNS_3);



//\CV== CLASS VARIABLES (VARIABLE CLASS/STATIC ATTRIBUTES/FIELDS) ==============

    /** Index posledního vytvořeného kroku scénáře -
     *  z něj se odvozuje index příštího kroku. */
    private static int lastIndex = 0;



//##############################################################################
//\CI== CLASS (STATIC) INITIALIZER (CLASS CONSTRUCTOR) =========================

    static {
        String[] EMPTY_STRING_ARR = {};
        String   TEXT = "NOT_START_STEP";
        NOT_START_STEP = new ScenarioStep(-1, TypeOfStep.tsNOT_START,
                         TEXT, TEXT, TEXT,
                         EMPTY_STRING_ARR, EMPTY_STRING_ARR, EMPTY_STRING_ARR);
    }



//\CG== CLASS (STATIC) GETTERS AND SETTERS =====================================

    /***************************************************************************
     * Vrátí hodnotu, na níž bude navazovat index následně vytvořeného kroku.
     *
     * @return Požadovaná hodnota
     */
    public static int lastIndex()
    {
        return lastIndex;
    }


    /***************************************************************************
     * Nastaví index příštího kroku a vrátí
     * index posledního vytvořeného kroku.
     *
     * @param next Index příštího kroku
     * @return Index posledního vytvořeného kroku
     */
    public static int setIndex(int next)
    {
        int   ret = lastIndex;
        lastIndex = next - 1;
        return ret;
    }



//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Index daného kroku scénáře - typicky jeho pořadí v rámci scénáře. */
    public final int index;

    /** Typ daného kroku scénáře. */
    public final TypeOfStep typeOfStep;

    /** Zadávaný příkaz. */
    public final String command;

    /** Zpráva vydaná v reakci na zadaný příkaz;
     *  obsahuje-li {@code null}, nepočítá se s ověřováním reakce hry. */
    public final String message;

    /** Prostor, v němž se bude hráč nacházet po vykonaní příkazu. */
    public final String place;

    /** Názvy prostorů, které bezprostředně sousedí s prostorem,
     *  v němž se bude hráč nacházet po vykonaní příkazu,
     *  a které jsou proto z tohoto prostoru přímo dostupné. */
    public final String[] neighbors;

    /** Názvy objektů aktuálně se nacházejících v prostoru,
     *  v němž se bude hráč nacházet po vykonaní příkazu. */
    public final String[] items;

    /** Názvy objektů v batohu po vykonání příkazu. */
    public final String[] bag;

    /** Zadává, které hodnoty pomocných proměnných jsou pro úspěšné
     *  vykonání zadaného příkazu vyžadovány. */
    public final Map<String, Object> needs;

    /** Zadává, které pomocné proměnné daný příkaz nastavuje a jak. */
    public final Map<String, Object> sets;

    /** Názvy testů spouštějících metody či lambda-výrazy realizující
     *  složitější testy. */
    public final List<String> tests;


//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Vytvoří krok umožňující otestování správné funkce hry,
     * přičemž tento krok bude mít zadaný index, na nějž budou navazovat
     * indexy následně vytvořených kroků bez explicitně zadaného indexu.
     *
     * Očekává splnění zadaných podmínek a nastavuje pomocné proměnné.
     * Používá se při zadávání příkazů aktivujících rozšiřující akce.
     *
     * @param index      Číslo, které by mělo určovat pořadí daného kroku
     *                   v rámci jeho scénáře
     * @param typeOfStep O který typ kroku se jedná
     * @param command    Zadaný příkaz
     * @param message    Zprava vydaná v reakci na zadaný příkaz
     * @param place      Název prostoru, v němž bude hráč vykonání příkazu
     * @param neighbors  Názvy prostorů, kam se je odsud možno přesunout
     * @param items      Názvy objektů v aktuálním prostoru
     * @param bag        Názvy objektů v batohu
     * @param needs      Zadává které hodnoty kterých pomocných proměnných
     *                   musí být nastaveny
     * @param tests      Názvy testů identifikující metody či lambda-výrazy
     *                   realizující složitější testy
     * @param sets       Zadává které hodnoty kterých pomocných proměnných
     *                   daný příkaz nastavuje
     */
    public ScenarioStep(int     index,     TypeOfStep typeOfStep,
                        String   command,   String   message, String   place,
                        String[] neighbors, String[] items,   String[] bag,
                        Map<String, Object> needs,   List<String> tests,
                        Map<String, Object> sets)
    {
        if (command == null)  {
            throw new NullPointerException(
                      "\nPříkaz musí být platný řetězec");
        }
        this.index      = lastIndex = index;
        this.typeOfStep = typeOfStep;
        this.command    = command;
        this.message    = message;
        this.place      = place;
        this.neighbors  = neighbors;
        this.items      = items;
        this.bag        = bag;
        if (needs == null) { this.needs = Map.of(); }
                     else  { this.needs = Collections.unmodifiableMap(needs); }
        if (tests == null) { this.tests = List.of(); }
                     else  { this.tests = Collections.unmodifiableList(tests); }
        if (sets  == null) { this.sets  = Map.of(); }
                     else  { this.sets  = Collections.unmodifiableMap(sets); }
    }

    /***************************************************************************
     * Vytvoří krok umožňující otestování správné funkce hry.
     * Index tohoto kroku bude o jedničku větší než index kroku vytvořeného
     * před ním, přesněji než poslední zapamatovaný index.
     * Používá se pro definici příkazů aktivujících rozšiřující akce.
     *
     * @param typeOfStep O který typ kroku se jedná
     * @param command    Zadaný příkaz
     * @param message    Zprava vydaná v reakci na zadaný příkaz
     * @param place      Název prostoru, v němž bude hráč vykonání příkazu
     * @param neighbors  Názvy prostorů, kam se je odsud možno přesunout
     * @param items      Názvy objektů v aktuálním prostoru
     * @param bag        Názvy objektů v batohu
     * @param needs      Zadává které hodnoty kterých pomocných proměnných
     *                   musí být nastaveny
     * @param tests      Názvy testů identifikující metody či lambda-výrazy
     *                   realizující složitější testy
     * @param sets       Zadává které hodnoty kterých pomocných proměnných
     *                   daný příkaz nastavuje
     */
    public ScenarioStep(TypeOfStep typeOfStep,
                        String   command,   String   message, String   place,
                        String[] neighbors, String[] items,   String[] bag,
                        Map<String, Object> needs,   List<String> tests,
                        Map<String, Object> sets)
    {
        this(++lastIndex, typeOfStep, command, message, place,
             neighbors, items, bag, needs, tests, sets);
    }


    /***************************************************************************
     * Vytvoří startovní krok umožňující otestování správné funkce hry,
     * přičemž tento krok bude mít index 0, na nějž budou navazovat
     * indexy následně vytvořených kroků bez explicitně zadaného indexu.
     *
     * Nastavuje pouze očekávání -
     * používá se u testů špatného zadání pomocných akcí.
     *
     * @param typeOfStep O který typ kroku se jedná
     * @param command    Zadaný příkaz
     * @param message    Zprava vydaná v reakci na zadaný příkaz
     * @param place      Název prostoru, v němž bude hráč vykonání příkazu
     * @param neighbors  Názvy prostorů, kam se je odsud možno přesunout
     * @param items      Názvy objektů v aktuálním prostoru
     * @param bag        Názvy objektů v batohu
     * @param needs      Zadává které hodnoty kterých pomocných proměnných
     *                   musí být nastaveny
     * @param tests      Názvy testů identifikující metody či lambda-výrazy
     *                   realizující složitější testy
     */
    public ScenarioStep(TypeOfStep typeOfStep,
                        String command,     String   message, String   place,
                        String[] neighbors, String[] items,   String[] bag,
                        Map<String, Object> needs,   List<String> tests)
    {
        this(0, typeOfStep, command, message, place, neighbors, items, bag,
             needs, tests, null);
    }


    /***************************************************************************
     * Vytvoří krok umožňující otestování správné funkce hry,
     * přičemž tento krok bude mít zadaný index, na nějž budou navazovat
     * indexy následně vytvořených kroků bez explicitně zadaného indexu.
     *
     * Nemá žádná očekávání a nic nenastavuje.
     * Používá se pro definici reakce na zadání příkazu při vypnuté hře.
     *
     * @param index      Číslo, které by mělo určovat pořadí daného kroku
       *                 v rámci jeho scénáře
     * @param typeOfStep O který typ kroku se jedná
     * @param command    Zadaný příkaz
     * @param message    Zprava vydaná v reakci na zadaný příkaz
     * @param place      Název prostoru, v němž bude hráč vykonání příkazu
     * @param neighbors  Názvy prostorů, kam se je odsud možno přesunout
     * @param items      Názvy objektů v aktuálním prostoru
     * @param bag        Názvy objektů v batohu
     */
    public ScenarioStep(int     index,     TypeOfStep typeOfStep,
                        String   command,   String   message, String   place,
                        String[] neighbors, String[] items,   String[] bag)
    {
        this(index, typeOfStep, command, message, place, neighbors, items, bag,
             null, null, null);
    }


    /***************************************************************************
     * Vytvoří krok umožňující otestování správné funkce hry.
     * Index tohoto kroku bude o jedničku větší než index kroku vytvořeného
     * před ním, přesněji než poslední zapamatovaný index.
     *
     * Nemá žádná očekávání a nic nenastavuje.
     * Používá se pro zadání povinných příkazů s výjimkou startovního.
     *
     * @param typeOfStep O který typ kroku se jedná
     * @param command    Zadaný příkaz
     * @param message    Zprava vydaná v reakci na zadaný příkaz
     * @param place      Název prostoru, v němž bude hráč vykonání příkazu
     * @param neighbors  Názvy prostorů, kam se je odsud možno přesunout
     * @param items      Názvy objektů v aktuálním prostoru
     * @param bag        Názvy objektů v batohu
     */
    public ScenarioStep(TypeOfStep typeOfStep,
                        String   command,   String   message, String   place,
                        String[] neighbors, String[] items,   String[] bag)
    {
        this(typeOfStep, command, message, place, neighbors, items, bag,
             null, null, null);
    }


    /***************************************************************************
     * Vytvoří krok kopírující stav v zadaném kroku s výjimkou indexu.
     *
     * @param index Číslo, které by mělo určovat pořadí daného kroku
     *              v rámci jeho scénáře
     * @param step  Kopírovaný krok
     */
    public ScenarioStep(int index, ScenarioStep step)
    {
        this(index, step.typeOfStep,
                    step.command,   step.message, step.place,
                    step.neighbors, step.items,   step.bag,
                    step.needs,     step.tests,   step.sets);
    }


    /***************************************************************************
     * Vytvoří krok kopírující stav v zadaném kroku s výjimkou indexu,
     * který nastaví o jedničku větší než v předchozím kroku.
     *
     * @param step       Kopírovaný krok
     */
    public ScenarioStep(ScenarioStep step)
    {
        this(step.typeOfStep,
             step.command,   step.message, step.place,
             step.neighbors, step.items,   step.bag,
             step.needs,     step.tests,   step.sets);
    }


    /***************************************************************************
     * Vytvoří další krok zjednodušeného scénáře NEsloužícího k testování,
     * ale pouze k demonstraci funkce hry a jejího rozhraní, přičemž jeho index
     * bude o jedničku větší než index kroku vytvořeného před ním.
     *
     * @param command Zadaný příkaz
     */
    public ScenarioStep(String command)
    {
        this (TypeOfStep.tsDEMO, command, "msg", "place",
              null, null, null, null, null);
    }



//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj ) { return true;  }
        if (obj  == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }

        final ScenarioStep other = (ScenarioStep) obj;
        return ((this.index      != other.index)                     &&
                (this.typeOfStep != other.typeOfStep)                &&
                (Objects.equals   (this.command,   other.command  )) &&
                (Objects.equals   (this.message,   other.message  )) &&
                (Objects.equals   (this.place,     other.place    )) &&
                (Arrays.deepEquals(this.neighbors, other.neighbors)) &&
                (Arrays.deepEquals(this.items,     other.items    )) &&
                (Arrays.deepEquals(this.bag,       other.bag      )) &&
                (Objects.equals   (this.needs,   other.needs  )) &&
                (Objects.equals   (this.sets,      other.sets     ))  );
    }


    /***************************************************************************
     * Vrátí hash kód daného kroku scénáře.
     *
     * @return Hash kód daného kroku scénáře
     */
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + this.index;
        hash = 97 * hash + Objects.hashCode   (this.typeOfStep);
        hash = 97 * hash + Objects.hashCode   (this.command);
        hash = 97 * hash + Objects.hashCode   (this.message);
        hash = 97 * hash + Objects.hashCode   (this.place);
        hash = 97 * hash + Arrays.deepHashCode(this.neighbors);
        hash = 97 * hash + Arrays.deepHashCode(this.items);
        hash = 97 * hash + Arrays.deepHashCode(this.bag);
        hash = 97 * hash + Objects.hashCode   (this.needs);
        hash = 97 * hash + Objects.hashCode   (this.sets);
        return hash;
    }


    /***************************************************************************
     * Podrobný podpis daného kroku scénáře;
     * obsahuje-li atribut {@link #message} hodnotu {@code null},
     * vypíše se pouze index kroku a zadaný příkaz.
     *
     * @return Textový podpis dané instance
     */
    @Override
    public String toString()
    {
        String ret = index + ". krok:  " + command
                + "\n----------------------------"
                + "\n" + message
                + "\n-----------------------------------------------------"
                + "\nProstor:  " + place
                + "\nSousedé:  " + Arrays.toString(neighbors)
                + "\nPředměty: " + Arrays.toString(items)
                + "\nBatoh:    " + Arrays.toString(bag)
                + (this.needs==null  ||  this.needs.isEmpty() ?  ""
                       : "\nOčekává:  " + this.needs)
                + (this.tests==null  ||  this.tests.isEmpty() ?  ""
                       :  "\nTestuje:  " + this.tests)
                + (this.sets ==null  ||  this.sets.isEmpty()  ?  ""
                       :  "\nNastavuje:" + this.sets);
        return ret;
    }

}
