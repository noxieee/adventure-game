package cz.vse.java.kedv00.adventura.testers;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */

import cz.vse.java.kedv00.adventura.api.IGame;
import cz.vse.java.kedv00.adventura.api.IPortal;


/*******************************************************************************
 * Instance třídy {@code GameSummary} představují přepravky
 * uchovávající informace o výsledcích testu hry.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class GameSummary
{
//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

//    /** Objekt vystupující jako zadavatel poskytující pomocné objekty –
//     *  testovací scénáře a návštěvníka. */
//    public final ATester tester;

    /** Tovární objekt poskytující klíčové objekty aplikace. */
    public final IPortal portal;

    /** Instance testované hry. */
    public final IGame game;

    /** Kompletní zpráva o průběhu testu. */
    public final StringBuilder verboseMessageBuilder;

    /** Mapa konvertující název scénáře na daný scénář. */
    public final TestVisitor visitor;



//\IV== INSTANCE VARIABLES (VARIABLE INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Objekt případné vyhozené chyby či výjimky. */
    private Throwable throwable;

//    /** Celkový bodový zisk. */
//    private double score;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Přepravka schraňující informace zjištěné v průběhu testu hry.
     *
     * @param portal  Tovární objekt, poskytující klíčové objekty aplikace
     * @param visitor Návštěvník používaný při testování běhu hry
     */
    public GameSummary(IPortal portal, TestVisitor visitor)
    {
        this.portal  = portal;
        this.game    = portal.game();
        this.visitor = visitor;
        this.verboseMessageBuilder = new StringBuilder();
        this.throwable = null;
//        this.score     = 0;
    }



//\IG== INSTANCE GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí informaci o tom, zda dosavadní testy proběhly bez chyb.
     *
     * @return Informace, zda dosavadní testy proběhly bez chyb
     */
    public boolean isOk()
    {
        return (throwable == null);
    }


//    /***************************************************************************
//     * Vrátí aktuální bodové hodnocení testovaného programu.
//     *
//     * @return Informace, zda dosavadní testy proběhly bez chyb
//     */
//    public double getScore()
//    {
//        return score;
//    }


//    /***************************************************************************
//     * Přidá k dosavadnímu bodovému hodnocení zadaný přírůstek.
//     *
//     * @param increment Přírůstek bodového hodnocení
//     */
//    public void addToScore(double increment)
//    {
//        this.score += increment;
//    }


    /***************************************************************************
     * Vrátí vyhozenou chybu či výjimku; není-li taková, vrátí {@code null}.
     *
     * @return Vyhozená chyba či výjimka nebo {@code null}
     */
    public Throwable getThrowable()
    {
        return throwable;
    }


    /***************************************************************************
     * Nastaví vyhazovanou chybu či výjimku.
     *
     * @param throwable Vyhazovaná chyba či výjimka
     */
    public void setThrowable(Throwable throwable)
    {
        if (throwable == null)  {
            throw new TestException("Do GameSummary nelze zadat "
                                  + "prázdný odkaz na vyhazovaný objekt ");
        }
        this.throwable = throwable;
    }


    /***************************************************************************
     * Vrátí textový řetězec s aktuální podobou podrobné zprávy.
     *
     * @return Textový řetězec s aktuální podobou podrobné zprávy
     */
    public String getVerboseMessage()
    {
        return verboseMessageBuilder.toString();
    }

}
