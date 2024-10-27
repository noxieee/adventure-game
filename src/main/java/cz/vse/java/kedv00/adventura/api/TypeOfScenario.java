package cz.vse.java.kedv00.adventura.api;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */



/*******************************************************************************
 * Instance výčtového typu {@code TypeOfScenario} představují možné typy
 * scénářů hry.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public enum TypeOfScenario
{
//\CE== VALUES OF THE ENUMERATION TYPE =========================================

    /** Scénář procházející možnou cestu vedoucí k dosažení cíle
     *  a obsahující současně informace pro otestování reakcí hry
     *  na zadávané příkazy. Tento druh scénáře musí každý správce nabízet.
     *  Tento scénář musí vyhovovat sadě požadavků, mezi něž patří např.
     *  <ul>
     *  <li>minimální počet kroků scénáře,</li>
     *  <li>minimální počet navštívených prostorů,</li>
     *  <li>minimální počet různých druhů příkazů,</li>
     *  <li>provedení základních povinných akcí, tj.
     *  <ul><li>přechodu mezi prostory,</li>
     *      <li>zvednutí objektu v aktuálním prostoru,</li>
     *      <li>položení objektu v aktuálního prostoru.</li>
     *  </ul>
     *  <li>úspěšné ukončení hry.</li>
     *  </ul>
     *  Správce scénářů musí spravovat právě jeden scénář tohoto typu.
     */
    scHAPPY("základní úspěšný scénář",
            "úspěšné zvládnutí hry"),

    /** Scénář definující příkazy aktivující povinné akce,
     *  a to v pořadí tsSTART, tsGOTO, tsTAKE, tsPUT_DOWN, tsHELP, tsEND.
     *  Pokud náhodou začíná něčí hra plným batohem, je možno příkazy typu
     *  tsTAKE a tsPUT_DOWN prohodit.
     *  Scénář pomáhá při rozběhnutí základní funkcionality hry.
     *  Správce scénářů musí spravovat právě jeden scénář tohoto typu.
     */
    scBASIC("Základní testovací scénář",
             "reakce na korektně vyvolané povinné akce"),

    /** Scénář sloužící k otestování reakcí hry
     *  na chybně zadané příkazy uživatele.
     *  Scénář musí obsahovat všechny druhy kroků, které jsou ve výčtovém typu
     *  {@code cz.vse.adv_fw.scenario.TypeOfStep}
     *  uvedeny jako povinné, tj. kroků, které mají podtyp
     *  {@code TypeOfStep.Subtype.stMISTAKE}.
     *  Mezi tyto kroky patří vedle kroků definujících reakci programu
     *  na chybně zadané příkazy hráče, také příkaz žádající nápovědu
     *  a příkaz pro explicitní ukončení hry.
     *  Správce scénářů musí spravovat právě jeden scénář tohoto typu.
     */
    scMISTAKES("základní chybový scénář",
               "reakce na chyby při zadávání základních příkazů"),

    /** Scénář sloužící k otestování reakcí hry na chybně zadané příkazy
     *  spouštějící nestandardní akce. Musí obsahovat všechny typy akcí
     *  s podtypem {@code TypeOfStep.Subtype.stMISTAKE_NS}.
     *  Správce scénářů má poskytovat právě jeden scénář tohoto typu.
     */
    scMISTAKES_NS("doplňkový chybový scénář",
                  "reakce na chyby při zadávání nestandardních příkazů"),

    /** Dodatečný scénář, jenž je povinnou součástí
     *  řešení některých úloh při obhajobách a ukazuje možný průběh hry
     *  při použití zadané doplňkové akce.
     */
    scFOURTH("dodatečný scénář pro rozšíření",
              "alternativní možný průběh po přidání požadované akce"),

    /** Obecný scénář, podle nějž je možno testovat chod hry,
     *  ale který nepatří do žádného z výše uvedených dvou povinných typů.
     *  Tento scénář musí být testovatelný,
     *  a proto nesmí obsahovat žádný demonstrační krok.
     *  Správce scénářů může spravovat libovolný počet scénářů tohoto typu.
     */
    scGENERAL("obecný testovatelný scénář",
              "alternativní možný průběh hry"),

    /** Scénář sloužící pouze k demonstraci možné cesty
     *  a neumožňující testování chodu hry.
     *  Scénář smí obsahovat testovatelné kroky, avšak nesmí se podle nich
     *  testovat, protože mezi nimi mohou být i kroky čistě demonstrační.
     *  Správce scénářů může spravovat libovolný počet scénářů tohoto typu.
     */
    scDEMO("netestovatelný demonstrační scénář",
           "možný průběh hry pro simulace");



//##############################################################################
//\IC== CONSTANT INSTANCE ATTRIBUTES (FIELDS) ==================================

    /** Základní charakteristika scénářů daného typu. */
    private final String description;

    /** Stručný popis účelu scénářů daného typu. */
    private final String purpose;

    /** Požadovaný název povinně definovaného typu scénáře. */
    private final String requiredName;



//##############################################################################
//\II== INSTANCE CONSTRUCTORS AND SIMPLE (STATIC) FACTORY METHODS ==============

    /***************************************************************************
     * Vytvoří typ scénáře se stručnými popisy jeho charakteristiky a účelu.
     *
     * @param description   Stručný popis daného scénáře
     * @param purpose       Popis účelu daného scénáře
     */
    private TypeOfScenario(String description, String purpose)
    {
        this.description  = description;
        this.purpose      = purpose;
        this.requiredName = name().substring(2);
    }



//\IG== INSTANCE GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí popis základní charakteristiky scénářů daného typu.
     *
     * @return Základní charakteristika scénářů daného typu
     */
    public String description()
    {
        return description;
    }


    /***************************************************************************
     * Vrátí stručný popis účelu scénářů daného typu.
     *
     * @return Stručný popis účelu scénářů daného typu
     */
    public String purpose()
    {
        return purpose;
    }


    /***************************************************************************
     * Požadovaný název scénářů daného typu – platí pouze pro
     * povinně definované scénáře, tj. pro pvní tři.
     *
     * @return Požadovaný název scénářů daného typu
     */
    public String requiredName()
    {
        return requiredName;
    }

}
