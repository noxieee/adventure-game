package cz.vse.java.kedv00.adventura.api;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/api/IAuthor.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/



/*******************************************************************************
 * Instance interfejsu {@code IAuthor} umějí na požádání vrátit
 * jméno a identifikační string autora/autorky své třídy.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2023-Summer
 */
public interface IAuthor
{
//\AM== ABSTRACT METHODS =======================================================

    /***************************************************************************
     * Vrátí identifikační řetězec autora/autorky zapsaný VELKÝMI PÍSMENY.
     * Tímto řetězcem bývá většinou login do informačního systému školy.
     *
     * @return Identifikační řetězec autora/autorky
     */
    //@Override
    public String authorID()
    ;


    /***************************************************************************
     * Vrátí jméno autora/autorky programu ve formátu <b>PRIJMENI Krestni</b>
     * psané BEZ diakritiky (tj. bez háčků, čárek, přehlásek apod.).
     * Nejprve příjmení psané velkými písmeny a za ním křestní jméno,
     * u nějž bude velké pouze první písmeno a ostatní písmena budou malá.
     * Má-li autor(ka) programu více křestních jmen, může je uvést všechna.
     *
     * @return Jméno autora/autorky programu ve tvaru PRIJMENI Krestni
     */
    //@Override
    public String authorName()
    ;


    /***************************************************************************
     * Vrátí jméno autora/autorky programu ve formátu <b>PŘÍJMENÍ Křestní</b>,
     * zapsané v jeho/jejím rodném jazyce včetně případné diakritiky.
     *
     * @return Jméno autora/autorky programu v jeho/jejím rodném jazyce
     */
    //@Override
    public String authorNativeName()
    ;


    /***************************************************************************
     * Vrátí identifikační řetězec skupiny, kterou autor navštěvuje.
     * Ten začíná pořadovým číslem dne v týdnu následovaný znakem podtržení.
     * a čtyřčíslím označujícím začátek vyučovací hodiny.
     * Středa 16:15 se tak označí 3_1615.
     *
     * @return Identifikační řetězec skupiny
     */
    //@Override
    public String authorGroup()
    ;



//\DM== DEFAULT METHODS ========================================================

    /***************************************************************************
     * Vrátí string tvořený identifikačními údaji o autoru/autorce programu.
     *
     * @return Souhrn identifikačních údajů o autoru/autorce
     */
    public default String authorString()
    {
        return authorID() + " — " + authorNativeName()
             + " (" + authorName() + " — " + authorGroup() + ")";
    }

}
