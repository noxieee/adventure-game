package cz.vse.java.kedv00.adventura.api;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/api/BasicActions.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;



/*******************************************************************************
 * Instance třídy {@code BasicActions} přestavují přepravky
 * uchovávající názvy povinných příkazů v dané hře.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class BasicActions
{
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Název akce pro přesun z místnosti do místnosti. */
    public final String MOVE_NAME;

    /** Název akce pro položení předmětu. */
    public final String PUT_DOWN_NAME;

    /** Název akce pro zvednutí předmětu. */
    public final String TAKE_NAME;

    /** Název akce pro získání nápovědy. */
    public final String HELP_NAME;

    /** Název akce pro ukončení hry. */
    public final String END_NAME;

    /** Mapa umožňující získání názvu akce zadáním jejího typu. */
    private final Map<TypeOfStep, String> type2name;



//\IV== INSTANCE VARIABLES (VARIABLE INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Podpis instance generovaný metodou toString() -
     *  vytváří se až při prvním požadavku. */
    private String toString;

    /** Hash kód instance – vytváří se až při prvním požadavku. */
    private int hash;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Vytvoří přepravku uchovávající názvy akcí,
     * které musí být implementovány ve všech hrách.
     * Názvy těchto akcí musí být jednoslovné.
     *
     * @param move     Název akce pro přesun z místnosti do místnosti
     * @param take     Název akce pro zvednutí předmětu
     * @param putDown  Název akce pro položení předmětu
     * @param help     Název akce pro získání nápovědy
     * @param end      Název akce pro ukončení hry
     */
    public BasicActions(String move, String take, String putDown,
                        String help, String end)
    {
//        this(move, putDown, take, help, end, Level.START);
//    }
//
//    /***************************************************************************
//     * Vytvoří přepravku uchovávající názvy akcí,
//     * které musí být implementovány ve všech hrách.
//     * Názvy těchto akcí musí být jednoslovné.
//     *
//     * @param move     Název akce pro přesun z místnosti do místnosti
//     * @param take     Název akce pro zvednutí předmětu
//     * @param putDown  Název akce pro položení předmětu
//     * @param help     Název akce pro získání nápovědy
//     * @param end      Název akce pro ukončení hry
//     * @param lever    Hladina testování, tj. úroveň dokončenosti aplikace.
//     *                 Používá se pouze při testování.
//     */
//    public BasicActions(String move, String take, String putDown,
//                        String help, String end,  Level  level)
//    {
//        if (level.ordinal() <= Level.HAPPY.ordinal()) {
//            if ((help==null)  ||  "".equals(help)) { help = "???_HELP_???"; }
//            if ((end ==null)  ||  "".equals(end )) { end  = "???_END_???"; }
//        }
        MOVE_NAME     = move;
        TAKE_NAME     = take;
        PUT_DOWN_NAME = putDown;
        HELP_NAME     = help;
        END_NAME      = end;

        verifyArguments(move, putDown, take, help, end);

        Map<TypeOfStep, String> map = new EnumMap<>(TypeOfStep.class);
        map.put(TypeOfStep.tsGOTO,     move);
        map.put(TypeOfStep.tsPUT_DOWN, putDown);
        map.put(TypeOfStep.tsTAKE,     take);
        map.put(TypeOfStep.tsHELP,     help);
        map.put(TypeOfStep.tsEND,      end);

        type2name = Collections.unmodifiableMap(map);
    }



//\IG== INSTANCE GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí název příkazu zadaného typu.
     *
     * @param typeOfStep Typ příkazu, na jehož název se ptáme
     * @return Název příkazu zadaného typu
     */
    public String get(TypeOfStep typeOfStep)
    {
        String result = type2name.get(typeOfStep.group());
        if (result == null) {
            throw new IllegalArgumentException(
                "\nAkce zadaného typu nepatří mezi základní akce");
        }
        return result;
    }



//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================

    /***************************************************************************
     * Porovná zadaný objekt s aktuálním a vrátí informaci o tom,
     * zda je můžeme považovat za ekvivalentní, což bude platit v případě,
     * že jsou jednotlivé texty shodné bez ohledu na velikost písmen.
     *
     * @param obj Porovnávaný objekt
     * @return Lze-li je považovat za ekvivalentní, vrátí {@code true},
     *         jinak vrátí {@code false}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BasicActions other = (BasicActions) obj;
        boolean equals =
            this .MOVE_NAME    .equalsIgnoreCase(other.MOVE_NAME    ) &&
            this .PUT_DOWN_NAME.equalsIgnoreCase(other.PUT_DOWN_NAME) &&
            this .TAKE_NAME    .equalsIgnoreCase(other.TAKE_NAME    ) &&
            this .END_NAME     .equalsIgnoreCase(other.END_NAME     ) &&
            this .HELP_NAME    .equalsIgnoreCase(other.HELP_NAME    );
        return equals;
    }


    /***************************************************************************
     *Vrátí hash-code dané přepravky.
     *
     * @return Hash-code dané přepravky
     */
    @Override
    public int hashCode()
    {
        if (hash== 0)
        {
            hash = 7;
            hash = 79 * hash + this.MOVE_NAME    .toLowerCase().hashCode();
            hash = 79 * hash + this.PUT_DOWN_NAME.toLowerCase().hashCode();
            hash = 79 * hash + this.TAKE_NAME    .toLowerCase().hashCode();
            hash = 79 * hash + this.END_NAME     .toLowerCase().hashCode();
            hash = 79 * hash + this.HELP_NAME    .toLowerCase().hashCode();
        }
        return hash;
    }


    /***************************************************************************
     * Vrací textový podpis instance sestávající z názvu třídy následovaného
     * výčtem hodnot atributů uzavřeným v hranatých závorkách.
     *
     * @return Podpis instance
     */
    @Override
    public String toString()
    {
        if (toString == null) {
            toString = "BasicActions" +
                       "[MOVE="  + MOVE_NAME     +
                       ", TAKE=" + TAKE_NAME  +
                       ", PUT="  + PUT_DOWN_NAME +
                       ", HELP=" + HELP_NAME     +
                       ", END="  + END_NAME      + "]";
        }
        return toString;
    }



//\IP== INSTANCE PRIVATE AND AUXILIARY METHODS =================================

    /***************************************************************************
     * Prověří korektnost zadaných hodnot,
     * které nesmějí být prázdné odkazy ani prázdné řetězce.
     *
     * @param names Prověřované hodnoty
     */
    private void verifyArguments(String... names)
    {
        for (String name : names) {
            if ((name == null)  ||  name.isEmpty()) {
                throw new IllegalArgumentException(
                    "\nNěkterý ze zadávaných názvů povinných akcí "
                  + "je prázdný odkaz nebo prázdný řetězec\n" + this);
            }
        }
    }

}
