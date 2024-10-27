package cz.vse.java.kedv00.adventura.testers;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */

import cz.vse.java.kedv00.adventura.api.IBag;
import cz.vse.java.kedv00.adventura.api.IGame;
import cz.vse.java.kedv00.adventura.api.IItem;
import cz.vse.java.kedv00.adventura.api.IPlace;
import cz.vse.java.kedv00.adventura.testers.util.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;




/*******************************************************************************
 * Knihovní třída  {@code TestUtilitiy} poskytuje užitečné metody
 * pro operace předpokládající využití reflexe.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public final class TestUtilitiy
{
//\CC== CLASS CONSTANTS (CONSTANT CLASS/STATIC ATTRIBUTES/FIELDS) ==============
//\CV== CLASS VARIABLES (VARIABLE CLASS/STATIC ATTRIBUTES/FIELDS) ==============



//##############################################################################
//\CI== CLASS (STATIC) INITIALIZER (CLASS CONSTRUCTOR) =========================
//\CF== CLASS (STATIC) FACTORY METHODS =========================================
//\CG== CLASS (STATIC) GETTERS AND SETTERS =====================================
//\CM== CLASS (STATIC) REMAINING NON-PRIVATE METHODS ===========================

    /***************************************************************************
     * Zjistí, jestli třída zadaná svým class-objektem implementuje
     * návrhový vzor <i>Jedináček</i> (<i>Singleton</i>) a pokud ano,
     * získá její instanci.
     * Při kontrole "jedináčkovství" se kontroluje,
     * zda třída má jediný konstruktor, který je navíc soukromý,
     * a jestli definuje bezparametrickou statickou metodu {@code getInstance},
     * která.vrátí instanci dané třídy. O tu pak pořádá.
     *
     * @param <T> Typ požadovaného objektu nebo jeho rodiče
     * @param cls Class-objekt třídy, jejíž instanci chceme získat
     * @return Instance zadané třídy
     * @throws TestException
     *         Třída reprezentuje neinstanciovatelný typ (abstraktní třídu,
     *         interfejs, pole, primitivní typ, pseudotyp {@code void}),
     *         anebo třída nemá dostupný bezparametrický (nulární) konstruktor,
     *         anebo byla při konstrukci instance vyhozena nějaká výjimka.
     */
    public static <T> T getInstanceOf(Class<? extends T> cls)
           throws TestException
    {
        T instance;
        try {
            instance = cls.getConstructor().newInstance();
        }
        catch (IllegalAccessException | IllegalArgumentException |
               InstantiationException | NoSuchMethodException    |
               SecurityException      | InvocationTargetException ex) {
            throw new TestException(
                "Nepodařilo se vytvořit instanci třídy " + cls.getName()
              + "\npravděpodobně nemá dostupný bezparametrický konstruktor"
              , ex);
        }
        return instance;
    }


    /***************************************************************************
     * Zjistí, jestli třída zadaná svým class-objektem implementuje
     * návrhový vzor <i>Jedináček</i> (<i>Singleton</i>) a pokud ano,
     * získá její instanci.
     * Při kontrole "jedináčkovství" se kontroluje,
     * zda třída má jediný konstruktor, který je navíc soukromý,
     * a jestli definuje bezparametrickou statickou metodu {@code getInstance},
     * která.vrátí instanci dané třídy. O tu pak pořádá.
     *
     * @param <T> Typ požadovaného objektu nebo jeho rodiče
     * @param cls Class-objekt třídy, jejíž jedinou instanci chceme získat
     * @return Instance zadané třídy
     * @throws TestException Objeven nějaký problém
     */
    public static <T> T getSingletonOf(Class<? extends T> cls)
        throws IllegalArgumentException
    {
        verifySingleAndPrivateConstructor(cls);
        T instance = verifyGetInstanceMethod(cls);
        return instance;
    }


    /***************************************************************************
     * Vrátí textový řetězec s popisem aktuálního stavu hry
     *
     * @param game Hra, jejíž popis vytváříme
     * @return Požadovaný řetězec
     */
    public static String gameStateDescription(IGame game)
    {
        IPlace place = game.world().currentPlace();
        IBag  bag  = game.bag();
        Collection<? extends IPlace> neighbors = place.neighbors();
        Collection<? extends IItem> inPlace  = place.items();
        Collection<? extends IItem> inBag   = bag .items();

        StringBuilder sb = new StringBuilder();
        sb.append("Prostor: ")   .append(place.name())
          .append("\nSousedé:  ").append(Util.colINamedToString(neighbors))
          .append("\nObjekty:  ").append(Util.colINamedToString(inPlace))
          .append("\nV batohu: ").append(Util.colINamedToString(inBag));
        return sb.toString();
    }


    /***************************************************************************
     * Zjistí, jestli třída zadaná svým class-objektem definuje statickou
     * tovární metodu {@code getInstance}
     * a požádá nalezenou metodu o instanci dané třídy.
     *
     * @param <T> Typ, jehož instanci má tovární metoda vracet
     * @param cls Class-objekt třídy, jejíž instanci chceme získat
     * @return    Jediná instance zadané třídy
     * @throws TestException Objeven nějaký problém
     */
    @SuppressWarnings("unchecked")
    public static <T> T verifyGetInstanceMethod(Class<? extends T> cls)
    {
        final String mtdName = "getInstance";
        final String mtdSign = mtdName + "()";
        final Method method;
        try {
            method = cls.getMethod(mtdName);
        }
        catch (NoSuchMethodException ex) {
            throw new TestException(
                  "\nTřída hry nemá definovanou tovární metodu " + mtdSign);
        }
        Object o;
        try {
            o = method.invoke(null, new Object[]{});
            System.out.println("invoke().getClass: " + o.getClass()
                           + "\ncls: " + cls);
        }
        catch (IllegalAccessException    |
               IllegalArgumentException  |
               InvocationTargetException ex)
        {
            throw new RuntimeException(ex);
        }
        if (o.getClass() != cls) {
            throw new TestException(
                      "\nMetoda " + mtdSign + " nevrací objekt typu " +
                      cls.getName());
        }

        T o1, o2;
        try {
            o1 = (T)method.invoke(null);
            o2 = (T)method.invoke(null);
        }
        catch(NullPointerException ex) {
            throw new TestException(
                      "\nMetoda " + mtdSign + " není definována jako statická");
        }
        catch (IllegalAccessException ex) {
            throw new TestException(
                      "\nMetoda " + mtdSign + " není definována jako veřejná");
        }
        catch (IllegalArgumentException ex) {
            throw new TestException(
                      "\nPři testu správné implementace metody " + mtdSign +
                      " byla vyhozena výjimka " + ex, ex);
        }
        catch (InvocationTargetException ex) {
            throw new TestException(
                      "\nMetoda " + mtdSign + " vyhodila výjimku " + ex, ex);
        }

        if (o1 != o2) {
            throw new TestException(
                "\nTovární metoda " + mtdSign + " třídy " + cls +
                "\nnevrací při každém zavolání stejnou instanci");
        }

        return o1;                             //==========>
    }


    /***************************************************************************
     * Otestuje, že zadaná třída má jediný konstruktor, a ten je soukromý.
     *
     * @param cls Class-objekt testované třídy
     * @throws TestException Objeven nějaký problém
     */
    public static void verifySingleAndPrivateConstructor(Class<?> cls)
    {
        Constructor<?>[] constructors = cls.getDeclaredConstructors();
        if ((constructors.length > 1)  ||
            ((constructors[0].getModifiers() & Modifier.PRIVATE) == 0))
        {
            throw new TestException(
               "\nZadaná třída hry nemůže definovat jedináčka, " +
               "\nprotože nemá pouze jediný, a to soukromý konstruktor:\n"
               + cls);
        }
    }




//\CP== CLASS (STATIC) PRIVATE AND AUXILIARY METHODS ===========================




//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============
//\IV== INSTANCE VARIABLES (VARIABLE INSTANCE ATTRIBUTES/FIELDS) ===============



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /** Soukromý konstruktor zabraňující vytvoření instance.*/
    private TestUtilitiy() {}



//\IA== INSTANCE ABSTRACT METHODS ==============================================
//\IG== INSTANCE GETTERS AND SETTERS ===========================================
//\IM== INSTANCE REMAINING NON-PRIVATE METHODS =================================
//\IP== INSTANCE PRIVATE AND AUXILIARY METHODS =================================



//##############################################################################
//\NT== NESTED DATA TYPES ======================================================
}
