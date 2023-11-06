package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.INamed;

/*******************************************************************************
 * Instance abstraktní třídy {@code ANamed} představují rodičovské podobjekty
 * instancí tříd pojmenovaných objektů,
 * tj. tříd implementujících interfejs {@link INamed}.
 *
 * @author  Vojtěch KEDER
 * @version 2023_Summer
 */
public abstract class ANamed implements INamed
{
    // INSTANACE ATTRIBUTES ////////////////////////////////////////////////////

    /** Jméno pojmenovaného objektu. */
    private final String NAME;

    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////

    /***************************************************************************
     * Vytvoří rodičovský podobjekt instance objektu se zadaným názvem.
     * Konstruktor přitom zkontroluje, že zadávaný název není prázdný odkaz
     * ani prázdný řetězec, a že není-li pojmenovávaný objekt objektem hry,
     * tak je jednoslovný, tj. neobsahuje bílé znaky.
     *
     * @param name Název dané instance
     */
    public ANamed(String name)
    {
        if(name == null || name.isEmpty())
        {
            throw new IllegalArgumentException(
                    "\nJako název objektu nesmí být zadán " +
                            "prázdný odkaz ani prázdný řetězec");
        }

        if(!name.equals(name.trim()) || name.split("\\s").length > 1)
        {
            throw new IllegalArgumentException(
                    "\nNázvy objektů musejí být jednoslovné, " +
                            "tj. nesmějí obsahovat bílé znaky - Zadáno: «" +
                            name + "»");
        }

        this.NAME = name;
    }

    // INSTANCE METHODS ////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí název dané instance.
     *
     * @return Název instance
     */
    @Override
    public String name() { return NAME; }

    /***************************************************************************
     * Vrátí textový podpis dané instance tvořený názvem její mateřské třídy
     * následovaným znakem podtržení a názvem instance.
     *
     * @return Název instance
     */
    @Override
    public String toString() { return NAME; }
}
