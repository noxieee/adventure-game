/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤
 * Check: «Stereotype», Section mark-§, Copyright-©, Alpha-α, Beta-β, Smile-☺
 */
package cz.vse.java.kedv00.adventura.testers; //ITest<T>



/*******************************************************************************
//%L+ CZ
 * Instance interfejsu <b>{@code ITest}</b> reprezentují testovací objekty
 * schopné otestovat objekty, které jsou instancemi daného typu –
 * třídy nebo interfejsu.
 *
 * @param <T> Typ, jehož instance je daný objekt schopen otestovat
//%Lx EN
 * {@code Interface} interface instances represent ...
//%L-
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 0.00.0000 — 20yy-mm-dd
 */
public interface ITest<T>
{
//\AM== REMAINING ABSTRACT METHODS =============================================

    /***************************************************************************
     * Otestuje zadanou instanci.
     *
     * @param instance Testovaná instance
     */
    public void test();


}
