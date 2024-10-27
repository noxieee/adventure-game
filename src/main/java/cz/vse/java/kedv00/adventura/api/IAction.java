package cz.vse.java.kedv00.adventura.api;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/api/IAction.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/



/*******************************************************************************
 * Instance interfejsu {@code IAction}
 * mají na starosti interpretaci příkazů zadávaných uživatelem hrajícím hru.
 * Název spouštěné akce je první slovo zadávaného příkazu;
 * další slova pak jsou interpretována jako argumenty.
 * <p>
 * Lze ale definovat i akci, která odstartuje konverzaci
 * (např. s osobou přítomnou v místnosti) a tím systém přepne do režimu,
 * v němž se zadávané texty neinterpretují jako příkazy,
 * ale předávají se definovanému objektu až do chvíle, kdy bude rozhovor
 * ukončen a hra se přepne zpět do režimu klasických příkazů.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2023_Summer
 */
public interface IAction
         extends INamed
{
//\AG== ABSTRACT GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí popis příkazu s vysvětlením jeho funkce,
     * významu jednotlivých parametrů
     * a možností (resp. účelu) použití daného příkazu.
     * Tento popis tak může sloužit jako nápověda k použití daného příkazu.
     *
     * @return Popis příkazu
     */
    //@Override
    public String description()
    ;



//\AM== REMAINING ABSTRACT METHODS =============================================

    /***************************************************************************
     * Metoda realizující reakci hry na zadání daného příkazu.
     * Předávané pole je vždy neprázdné,
     * protože jeho nultý prvek je zadaný název vyvolaného příkazu.
     * Počet argumentů je závislý na konkrétním příkazu,
     * např. příkazy <i>konec</i> a <i>nápověda</i> nemají parametry,
     * příkazy <i>jdi</i> a <i>seber</i> očekávají zadání jednoho argumentu,
     * příkaz <i>použij</i> muže mít dva argumenty atd.
     *
     * @param arguments Parametry příkazu;
     *                  jejich celkový počet muže byt pro každý příkaz jiný,
     *                  ale nultý prvek vždy obsahuje název příkazu
     * @return Text zprávy vypsané po provedeni příkazu
     */
    //@Override
    public String execute(String... arguments)
    ;

    /***************************************************************************
     * Funkční interfejs implementovaný testovacími funkcemi prověřujícími
     * vykonatelnost pomocných akcí.
     */
    @FunctionalInterface
    interface ITest
    {
        /***********************************************************************
         * Obdrží v argumentech jednotlivá slova zadaného příkazu
         * a prověří, jestli je splněna podmínka pro úspěšné provedení
         * zadané akce.
         *
         * @param args Slova zadaného příkazu
         * @return  Zda příslušná nutná podmínka platí
         */
        boolean test(String... args);
    }
}
