package cz.vse.java.kedv00.adventura.tests;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */

import cz.vse.java.kedv00.adventura.api.IPortal;
import cz.vse.java.kedv00.adventura.src.Portal;
import cz.vse.java.kedv00.adventura.testers.ITest;
import cz.vse.java.kedv00.adventura.testers.Level;
import cz.vse.java.kedv00.adventura.testers.PortalTester;
import org.junit.jupiter.api.Test;


/*******************************************************************************
 * Testuje hru vytvořenou podle zadání ze zimního semestru 2022
 * v podobě, s níž mají studenti přijít na obhajobu.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public class TestWhole
  implements ITest<IPortal>
{
///##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Implicitní konstruktor.
     * Třídu by bylo možné bez změny funkčnosti definovat jako jedináčka.
     */
    public TestWhole() {}



//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================

    /***************************************************************************
     * Otestuje aplikaci zadanou svým portálem.
     *
     */
    @Test
    public void test()
    {
        IPortal portal = new Portal();
        PortalTester tester = new PortalTester(portal, Level.WHOLE);
        tester.test();
    }



//##############################################################################
//\MM== MAIN METHOD ============================================================

    /***************************************************************************
     * Metoda testující demonstrační aplikaci na hladině WHOLE.
     *
     * @param args Parametry příkazového řádku
     */
    public static void main(String[] args)
    {
//        T09_Whole tester = new T09_Whole();
////        IPortal   portal = new adv23s._3_1245.mroa00_mrozek.Portal();
//        IPortal   portal = new adv23s._3_1430.krev15_krejcik.Tanecnik_Portal();
//        tester.test(portal);
    }
}
