package cz.vse.java.kedv00.adventura.api;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/api/IPortal.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/

import java.lang.reflect.InvocationTargetException;
import java.util.List;



/*******************************************************************************
 * Instance interfejsu {@code IPortal} představují tovární objekty,
 * které jsou schopny na požádání dodat instance klíčových objektů aplikace,
 * konkrétně aktuální hry, jejích scénářů a uživatelského rozhraní.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2023_Summer
 */
public interface IPortal
         extends IAuthor
{
//\CM== CLASS (STATIC) METHODS =================================================

    /***************************************************************************
     * Vrátí odkaz na instanci zadané tovární třídy. Předpokládá přitom,
     * že tato třída má dostupný nulární (= bezparametrický) konstruktor.
     *
     * @param <T>         Typ tovární třídy
     * @param portalClass Class-objekt tovární třídy
     * @return Požadovaný odkaz
     * @throws IllegalArgumentException Instanci zadané třídy
     *                                  se nepodařilo vytvořit
     */
    public static <T extends IPortal>
           T getInstanceOfFactory(Class<T> portalClass)
    {
        T result;
        try {
            result = portalClass.getDeclaredConstructor().newInstance();
        }
        catch (IllegalAccessException | IllegalArgumentException |
               InstantiationException | NoSuchMethodException    |
               SecurityException      | InvocationTargetException  ex)
        {
            throw new IllegalArgumentException(
                "\nNepodařilo se vytvořit instanci třídy " + portalClass, ex);
        }
        return result;
    }



//##############################################################################
//\AG== ABSTRACT GETTERS AND SETTERS ===========================================
//\DG== DEFAULT GETTERS AND SETTERS ============================================
////////// Metody vracející odkazy na klíčové objekty aplikace //////////

    /***************************************************************************
     * Vrátí seznam definovaných scénářů.
     * <ul>
     *   <li>
     *     Scénářem s indexem 0 musí být základní úspěšný scénář dané hry
     *     definující možný postup vedoucí k úspěšnému ukončení hry.<br>
     *     &nbsp;</li>
     *   <li>
     *     Scénářem s indexem 1 musí být základní povinný scénář, jenž definuje
     *     postup, při němž se demonstruje reakce na korektně zadané příkazy
     *     vyvolávající některou ze základní šestice povinných akcí.<br>
     *     &nbsp;</li>
     *   <li>
     *     Scénářem s indexem 2 musí být základní chybový scénář
     *     definující reakce hry na všechny možné uživatelské chyby
     *     při aktivaci některé ze základní šestice povinných akcí.<br>
     *     &nbsp;</li>
     *   <li>
     *     Scénářem s indexem 3 musí být nadstavbový chybový scénář
     *     definující reakce hry na všechny běžné uživatelské chyby
     *     specifikující reakce hry na možné uživatelské chyby
     *     při aktivaci některé rozšiřujících akcí.<br>
     *     &nbsp;</li>
     * </ul>
     * Výše uvedeným scénářům budou při jejich vytvářený automaticky
     * přiděleny předem definované názvy.
     * Názvy a účely dalších scénářů jsou již na libovůli autora.<br>
     *
     * @return Seznam spravovaných scénářů
     */
    //@Override
    default
    public List<Scenario> scenarios()
    {
        throw new UnsupportedOperationException("Není korektně definována "
                + "metoda vracející seznam scénářů.");
    }


    /***************************************************************************
     * Vrátí odkaz na (jedinou) instanci textové verze hry;
     * dokud ještě hra neexistuje, vyhazuje po zavolání výjimku
     * {@link UnsupportedOperationException}.
     *
     * @return Požadovaný odkaz
     * @throws UnsupportedOperationException
     *         Potomek metodu korektně nepřebil
     */
    //@Override
    default
    public IGame game()
    {
        throw new UnsupportedOperationException("Není korektně definována "
                + "tovární metoda vracející instanci hry");
    }

}
