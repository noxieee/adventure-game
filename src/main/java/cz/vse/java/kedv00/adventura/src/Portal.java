package cz.vse.java.kedv00.adventura.src;

import cz.vse.java.kedv00.adventura.api.IGame;
import cz.vse.java.kedv00.adventura.api.IPortal;
import cz.vse.java.kedv00.adventura.api.Scenario;
import cz.vse.java.kedv00.adventura.testers.Level;
import cz.vse.java.kedv00.adventura.testers.PortalTester;

import java.util.List;
import java.util.Scanner;


/*******************************************************************************
 * Instance třídy {@code Portal} představují tovární objekty,
 * které jsou schopny na požádání dodat informace o autorovi
 * a odkazy na instance klíčových objektů aplikace,
 * konkrétně aktuální hry a jejích scénářů.
 *
 * @author Vojtěch KEDER
 * @version 2023_Summer
 */

public class Portal implements IPortal
{
    // INSTANCE CONSTRUCTORS ///////////////////////////////////////////////////

    /*******************************
     * Žádné
     */
    public Portal() {}

    // INSTANCE METHODS ////////////////////////////////////////////////////////

    /***************************************************************************
     * Vrátí identifikační řetězec autora programu
     * zapsaný VELKÝMI PÍSMENY.
     * Tímto řetězcem je login do informačního systému školy.
     *
     * @return Identifikační řetězec autora programu
     */
    @Override
    public String authorID() {
        return "KEDV00";
    }

    /***************************************************************************
     * Vrátí jméno autora programu ve formátu <b>PRIJMENI Krestni</b>
     * psané BEZ diakritiky (tj. bez háčků, čárek, přehlásek apod.).
     * Nejprve příjmení psané velkými písmeny a za ním křestní jméno,
     * u nějž bude velké pouze první písmeno a ostatní písmena budou malá.
     *
     * @return Jméno autora programu ve tvaru PRIJMENI Krestni
     */
    @Override
    public String authorName() {
        return "KEDER Vojtech";
    }

    /***************************************************************************
     * Vrátí jméno autora programu ve formátu <b>PŘÍJMENÍ Křestní</b>,
     * zapsané v jeho rodném jazyce včetně případné diakritiky.
     *
     * @return Jméno autora programu v jeho/jejím rodném jazyce
     */
    @Override
    public String authorNativeName() {
        return "KEDER Vojtěch";
    }

    /***************************************************************************
     * Vrátí identifikační řetězec skupiny, kterou autor navštěvuje.
     * Ten začíná pořadovým číslem dne v týdnu následovaný znakem podtržení.
     * a čtyřčíslím označujícím začátek vyučovací hodiny.
     * Středa 16:15 se tak označí 3_1615.
     *
     * @return Identifikační řetězec skupiny
     */
    @Override
    public String authorGroup() {
        return "3_1615";
    }

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
    @Override
    public List<Scenario> scenarios()
    {
        return Scenarios.scenarios();
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
    @Override
    public IGame game() { return Game.getInstance(); }
}
