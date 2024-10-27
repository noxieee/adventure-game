package cz.vse.java.kedv00.adventura.testers;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */



/*******************************************************************************
 * Instance třídy {@code TestException} představují výjimky vyhazované
 * při odhalení nesrovnalostí v průběhu testování.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
@SuppressWarnings("serial")
public class TestException extends RuntimeException
{
//\CC== CLASS CONSTANTS (CONSTANT CLASS/STATIC ATTRIBUTES/FIELDS) ==============

    /** Počet milisekund, které se pnechají procesu na tisk dosud nevytištěných
     *  informací, než se ohlásí výjimka. */
    private static final int DEALY = 500;



//##############################################################################
//\CM== CLASS (STATIC) GENERAL NON-PRIVATE METHODS =============================

    /***************************************************************************
     * Pozastaví chod vlákna na zadaný čas, aby stihly doběhnout tisky.
     */
    public static void delay()
    {
        try {
            Thread.sleep(DEALY);
        }catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Vytvoří novou výjimku bez explicitně zadané zprávy a příčiny (cause).
     * Příčinu lze dodatečně zadat zavoláním metody {@link #initCause}.
     */
    public TestException()
    {
        delay();
    }


    /***************************************************************************
     * Vytvoří novou výjimku se zadanou detailní zprávou,
     * avšak bez zadané příčiny (cause).
     * Příčinu lze dodatečně zadat zavoláním metody {@link #initCause}.
     *
     * @param message Detailní zpráva, kterou lze následně získat zavoláním
     *                metody {@link #getMessage()}.
     */
    public TestException(String message)
    {
        super(message);
        delay();
    }


    /***************************************************************************
     * Vytvoří novou s explicitně zadanou příčinou a detailní zprávou
     * zadanou podle vzorce {@code (cause==null ? null : cause.toString())}.
     * Tento konstruktor je vhodný pro výjimky, které jsou ve skutečnosti
     * pouhými obaly (wrappers) výjimky {@code cause}.
     *
     * @param cause Výjimka, která typicky způsobila vyhození dané výjimky
     *              a kterou lze později získat voláním {@link #getCause()}.
     *              Hodnota {@code null} je povolena a indikuje, že příčinu
     *              vyhození dané výjimky neznáme a nebo dokonce není.
     */
    public TestException(Throwable cause)
    {
        super(cause);
        delay();
    }


    /***************************************************************************
     * Vytvoří novou výjimku s explicitně zadanou zprávou i příčinou.
     *
     * @param message  Detailní zpráva, kterou lze následně získat zavoláním
     *                 metody {@link #getMessage()}.
     * @param cause    Výjimka, která typicky způsobila vyození dané výjimky
     *                 a kterou lze později získat voláním {@link #getCause()}.
     *                 Hodnota {@code null} je povolena a indikuje, že příčinu
     *                 vyhození dané výjimky neznáme a nebo dokonce není.
     */
    public TestException(String message, Throwable cause)
    {
        super(message, cause);
        delay();
    }

}
