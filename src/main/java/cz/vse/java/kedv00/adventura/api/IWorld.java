package cz.vse.java.kedv00.adventura.api;
/* J:/p2_ILS/p2_Adv23s_FW/adv23s/api/IWorld.java
Příliš žluťoučký kůň úpěl ďábelské ó - PŘÍLIŠ ŽLUŤOUČKÝ KŮŇ ÚPĚL ĎÁBELSKÉ Ó.
*/

import java.util.Collection;



/*******************************************************************************
 * Instance interfejsu {@code IWorld} reprezentuje svět hry.
 * V dané hře musí být definována jako jedináček.
 * Má na starosti uspořádání jednotlivých prostorů a udržuje informaci o tom,
 * ve kterém z nich se hráč právě nachází.
 * Vzájemné uspořádání prostorů se může v průběhu hry měnit –
 * prostory mohou v průběhu hry získávat a ztrácet sousedy.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2023_Summer
 */
public interface IWorld extends Observable
{
//\AG== ABSTRACT GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí kolekci odkazů na všechny prostory vystupující ve hře.
     *
     * @return Kolekce odkazů na všechny prostory vystupující ve hře
     */
    //@Override
    public Collection<? extends IPlace> places()
    ;


    /***************************************************************************
     * Vrátí odkaz na aktuální prostor,
     * tj. na prostor, v němž se hráč pravé nachází.
     *
     * @return Prostor, v němž se hráč pravé nachází
     */
    //@Override
    public IPlace currentPlace()
    ;


    /***************************************************************************
     * Je li ve světě hry prostor se zadaným názvem, vrátí jej,
     * není-li tam, vrátí prázdný odkaz {@code null}.
     *
     * @param  name Název hledané prostoru
     * @return Hledaný prostor nebo prázdný odkaz {@code null}
     */
    //@Override
    public IPlace place(String name);


    /***************************************************************************
     * Nastaví zadaný prosto jako aktuální, tj. jako prostor,
     * v němž se aktuálně nachází hráč.
     *
     * @param destinationRoom Nastavovaný prostor
     */
    //@Override
    public void setCurrentPlace(IPlace destinationRoom)
    ;



//\AM== REMAINING ABSTRACT METHODS =============================================

    /***************************************************************************
     * Inicializuje svět hry, tj. inicializuje propojení prostorů
     * a jejich obsah a nastaví výchozí aktuální prostor.
     */
    public void initialize()
    ;

}
