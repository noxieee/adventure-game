package cz.vse.java.kedv00.adventura.api;
/* Saved in UTF-8 codepage: Příliš žluťoučký kůň úpěl ďábelské ódy. ÷ × ¤ */

import java.util.EnumSet;



/*******************************************************************************
 * Instance třídy {@code TypeOfStep} představují
 * možné typy kroků zadávaných ve scénáři.
 * Znalost typu kroku umožňuje zkontrolovat správnost zadání dat
 * v jednotlivých krocích scénáře.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 2022-Winter
 */
public enum TypeOfStep
{
//\CE== VALUES OF THE ENUMERATION TYPE =========================================

    /** Typ kroku neurčeného pro zařazení do nějakého scénáře, ale pouze pro
     *  doplnění chybového hlášení a pomocné akce.*/tsNOT_SET(6,-1, null),

//Typy řádných kroků, které se musí všechny objevit v základním úspěšném scénáři
    /** Startovací krok s prázdným názvem.      */tsSTART    (0,-1, null),
    /** Hráč se přesune do sousedního prostoru. */tsGOTO     (0, 1, null),
    /** Úspěšné "zvednutí" objektu v prostoru.  */tsTAKE     (0, 1, null),
    /** Úspěšné položení objektu z batohu.      */tsPUT_DOWN (0, 1, null),
    /** Úspěšné ukončení hry.                   */tsSUCCESS  (9, 0, null),

//Na následující typy kroků musí hra umět zareagovat =>
//   musí být otestovány v základním chybovém scénáři

    /** Příkaz okamžitě ukončující hru.         */tsEND      (1, 0, null),
    /** Nápověda.                               */tsHELP     (1, 0, null),

//Problémy se správným zadáním příkazu
    /** Startovací příkaz není prázdný řetězec. */tsNOT_START(2,-1, null),
    /** Nestartovací zadání prázdného řetězce.  */tsEMPTY    (2,-1, tsSTART),
    /** Hra nezná zadanou akci.                 */tsUNKNOWN  (2,-1, null),

//Vyvolání některé ze základních akcí bez povinného parametru
    /** Nebylo zadáno, kam se přesunout. */tsMOVE_WA         (2, 0, tsGOTO),
    /** Nebylo zadáno, co se má zvednout.*/tsTAKE_WA         (2, 0, tsTAKE),
    /** Nebylo zadáno, co se má položit. */tsPUT_DOWN_WA     (2, 0, tsPUT_DOWN),

//Problémy se změnou místnosti
    /** Cílový prostor není sousedem aktuálního.*/tsBAD_NEIGHBOR(2, 1, tsGOTO),

//Problémy se zvednutím či položením předmětu
    /** Zadaný předmět v prostoru není.      */tsBAD_ITEM    (2, 1, tsTAKE),
    /** Zadaný předmět nelze zvednout.       */tsUNMOVABLE   (2, 1, tsTAKE),
    /** Další předmět se nevejde do batohu.  */tsBAG_FULL    (2, 1, tsTAKE),
    /** Zadaný předmět v batohu není.        */tsNOT_IN_BAG  (2, 1, tsPUT_DOWN),

//Nestandardní typy kroků
    /** Bezparametrická nestandardní akce, jejíž proveditelnost závisí
     *  na nějakém stavu hry. */               tsNS_0        (3, 0, null),

    /** Jednoparametrická nestandardní akce. */tsNS_1        (3, 1, null),

    /** Dvouparametrická nestandardní akce. */ tsNS_2        (3, 2, null),

    /** Tříparametrická nestandardní akce. */  tsNS_3        (3, 3, null),

//Problémy s předčasně zadaným korektním ukončením
    /** Neúspěšný pokus o úspěšné ukončení hry. */tsNOT_SUCCESS(4,0, tsSUCCESS),

//Špatně zadané příkazy pro nestandardní akce. */
    /** Bezparametrická nestandardní akce zadaná v okamžiku,
     *  kdy ji nelze provést. */           tsNS0_WrongCond   (4, 0, tsNS_0),
    /** Jednoparametrická nestandardní akce zadaná v okamžiku,
     *  kdy ji nelze provést. */           tsNS1_WrongCond   (4, 1, tsNS_1),
    /** Dvouparametrická nestandardní akce zadaná v okamžiku,
     *  kdy ji nelze provést. */           tsNS2_WrongCond   (4, 2, tsNS_2),
    /** Tříparametrická nestandardní akce zadaná v okamžiku,
     *  kdy ji nelze provést. */           tsNS3_WrongCond   (4, 2, tsNS_3),
    /** Jednoparametrická nestandardní akce
     *  s nezadaným argumentem. */         tsNS1_0Args       (4, 0, tsNS_1),
    /** Dvouparametrická nestandardní akce s malým počtem
     *  zadaných argumentů. */             tsNS2_1Args       (4, 1, tsNS_2),
    /** Tříparametrická nestandardní akce s malým počtem
     *  zadaných argumentů. */             tsNS3_012Args     (4, 2, tsNS_3),
//    /** Jednoparametrická nestandardní akce
//     *  se špatným argumentem. */          tsNS1_WRONG_ARG   (4, 1, tsNS_1),
//    /** Dvouparametrická nestandardní akce s nevhodným
//     *  prvním argumentem. */              tsNS2_WRONG_1stARG(4, 2, tsNS_2),
//    /** Dvouparametrická nestandardní akce s nevhodným
//     *  druhým argumentem. */              tsNS2_WRONG_2ndARG(4, 2, tsNS_2),
//    /** Tříparametrická nestandardní akce s nevhodným
//     *  prvním argumentem. */              tsNS3_WRONG_1stARG(4, 3, tsNS_3),
//    /** Tříparametrická nestandardní akce s nevhodným
//     *  druhým argumentem. */              tsNS3_WRONG_2ndARG(4, 3, tsNS_3),
//    /** Tříparametrická nestandardní akce s nevhodným
//     *  třetím argumentem. */              tsNS3_WRONG_3rdARG(4, 3, tsNS_3),
//

    /** Zadaný krok nepopisuje klasickou akci,
     *  ale je součástí rozhovoru hráče s nějakou postavou či zařízením hry
     *  nebo nějaké obdobné činnosti. */             tsDIALOG(5,-1, null),

//Typy kroku nepoužitelných pro testování reakce hry na zadaný příkaz
    /** Krok tohoto typu nebude možno použít pro test správné funkce hry,
     *  protože neobsahuje data o stavu po provedení příkazu.
     *  Krok je určen pouze pro předvedení funkce hry.
     *  Demonstrační kroky se používají např. při testování funkce
     *  uživatelského rozhraní.                  */   tsDEMO (6,-1, null),

//Typy kroku používané v modifikačních zadáních při obhajobách
    /** Modifikovaný krok v zadáních při obhajobách.*/tsMOD_A(8,-1, null),
    /** Modifikovaný krok v zadáních při obhajobách.*/tsMOD_B(8,-1, null),
    /** Modifikovaný krok v zadáních při obhajobách.*/tsMOD_C(8,-1, null),
    ;



//##############################################################################
//\CC== CLASS CONSTANTS (CONSTANT CLASS/STATIC ATTRIBUTES/FIELDS) ==============

    /** Základní, standardní typy kroků povinně definovaných akcí. */
    public static final EnumSet<TypeOfStep> BASIC_ACTIONS;

    /** Typy kroků, které musí být povinně obsaženy
     *  v základním úspěšném scénáři. */
    public static final EnumSet<TypeOfStep> HAPPY_ACTIONS;

    /** Typy kroků, jež musí být povinně obsaženy
     *  v základním chybovém scénáři.
     *  Sem patří nápověda + nestandardní (předčasné) ukončení hry +
     *  různé druhy chybně zadaných příkazů, které nesmí hru rozhodit.
     *  Na všechny tyto typy nestandardních příkazů musí hra umět správně
     *  reagovat, a základní chybový scénář slouží k testu těchto reakcí. */
    public static final EnumSet<TypeOfStep> MISTAKE_ACTIONS;

    /** Typy kroků, jež musí být povinně obsaženy v uživatelském chybovém scénáři.
     *  Sem patří nápověda + nestandardní (předčasné) ukončení hry +
     *  různé druhy chybně zadaných příkazů, které nesmí hru rozhodit.
     *  Na všechny tyto typy nestandardních příkazů musí hra umět správně
     *  reagovat, a základní chybový scénář slouží k testu těchto reakcí. */
    public static final EnumSet<TypeOfStep> MISTAKE_NS_ACTIONS;



//##############################################################################
//\CI== CLASS (STATIC) INITIALIZER (CLASS CONSTRUCTOR) =========================

    static {
        EnumSet<TypeOfStep> basic     = EnumSet.noneOf(TypeOfStep.class);
        EnumSet<TypeOfStep> happy     = EnumSet.noneOf(TypeOfStep.class);
        EnumSet<TypeOfStep> mistake   = EnumSet.noneOf(TypeOfStep.class);
        EnumSet<TypeOfStep> mistakens = EnumSet.noneOf(TypeOfStep.class);
        for (TypeOfStep stepType : TypeOfStep.values()) {
            if (null != stepType.subtype) {
                switch (stepType.subtype) {
                    case stCORRECT -> {
                        basic.add(stepType);
                        happy.add(stepType);
                    }
                    case stSTOP -> {
                        basic.add(stepType);
                        mistake.add(stepType);
                    }
                    case stMISTAKE -> {
                        mistake.add(stepType);
                    }
                    case stNONSTANDARD -> {
                        if ((stepType == tsNS_0) || (stepType == tsNS_1)) {
                            happy.add(stepType);
                        }
                    }
                    case stMISTAKE_NS -> {
                        if ((stepType.group == tsNS_0)
                        || (stepType.group == tsNS_1))
                        {
                            mistakens.add(stepType);
                        }
                    }
                    case stSUCCESS -> {
                        happy.add(stepType);
                    }
                    case stHELP -> {
                        basic.add(stepType);
                    }
                    case stDEMO, stDIALOG, stMODIFIED -> {
                    }
                    default -> throw new RuntimeException(
                            "\nNeznámý podtyp kroku: " + stepType
                                    + " - " + stepType.subtype);
                }
            }
        }

        BASIC_ACTIONS      = EnumSet.copyOf(basic);
        HAPPY_ACTIONS      = EnumSet.copyOf(happy);
        MISTAKE_ACTIONS    = EnumSet.copyOf(mistake);
        MISTAKE_NS_ACTIONS = EnumSet.copyOf(mistakens);
    }


//##############################################################################
//\IC== INSTANCE CONSTANTS (CONSTANT INSTANCE ATTRIBUTES/FIELDS) ===============

    /** Skupina ekvivalentních typů = typů aktivujících stejnou akci. */
    private final TypeOfStep group;

    /** Počet parametrů očekávaný v daném typu kroku. */
    private final int paramCount;

    /** Podtyp daného typu kroku scénáře. */
    private final Subtype subtype;



//##############################################################################
//\II== INSTANCE INITIALIZERS (CONSTRUCTORS) ===================================

    /***************************************************************************
     * Definuje nový typ kroku a na základě hodnoty parametru
     * mu přiřadí příslušný podtyp a požadovaný počet parametrů.
     *
     * @param subtype    Ordinální číslo podtypu (aby byl zápis kratší)
     * @param paramCount Požadovaný počet parametrů. <br>
     *                   Hodnota {@code -1} se používá u typů příkazů,
     *                   u nichž nemá smysl zkoumat počet parametrů
     * @param group      Skupina ekvivalentních typů příkazů
     */
    private TypeOfStep(int subtype, int paramCount, TypeOfStep group)
    {
        this.subtype    = Subtype.values()[subtype];
        this.paramCount = paramCount;
        this.group      = (group == null)  ?  this  :  group;
    }



//\IG== INSTANCE GETTERS AND SETTERS ===========================================

    /***************************************************************************
     * Vrátí skupinu ekvivalentních typů = typů aktivujících stejnou akci.
     *
     * @return Skupina ekvivalentních typů = typů aktivujících stejnou akci
     */
    public TypeOfStep group()
    {
        return group;
    }


    /***************************************************************************
     * Vrátí počet parametrů očekávaný v daném typu kroku.
     *
     * @return Počet parametrů očekávaný v daném typu kroku
     */
    public int paramCount()
    {
        return paramCount;
    }


    /***************************************************************************
     * Vrátí podtyp příslušného kroku scénáře.
     *
     * @return Podtyp daného kroku scénáře
     */
    public Subtype subtype()
    {
        return subtype;
    }



//\IP== INSTANCE PRIVATE AND AUXILIARY METHODS =================================



//##############################################################################
//\NT== NESTED DATA TYPES ======================================================

    /***************************************************************************
     * Typy kroků scénářů jsou rozděleny do několika podtypů.
     * Tento výčtový typ je definuje.
     */
    static //Modifikátor static není třeba pro interní výčtové typy uvádět
    public enum Subtype  //Tyto typy jsou automaticky deklalovány jako statické
    {
        /** Správně volaná akce povinně zařazená do HAPPY.  0 */ stCORRECT,
        /** Správně volaná akce STOP.                       1 */ stSTOP,
        /** Chybně volaná akce povinně zařazená do MISTAKE. 2 */ stMISTAKE,
        /** Správně volaná doplňková akce.                  3 */ stNONSTANDARD,
        /** Chybně volaná doplňková akce.                   4 */ stMISTAKE_NS,

        /** Součást rozhovoru.                              5 */ stDIALOG,
        /** Demonstrační krok bez doprovodných informací,
         *  který proto nelze použít k testu funkce hry.    6 */ stDEMO,
        /** Krok vytvořený pro pomocné práce.               7 */ stUNDEFINED,
        /** Typ kroku používaný v modifikačních zadáních
         *  v obhajobách.                                   8 */ stMODIFIED,
        /** Úspěšné ukončení hry.                           9 */ stSUCCESS,
        /** Správně volaná akce HELP.                      10 */ stHELP;
    }

//    //Odkomentovává se při ladění
//    static {
//        System.out.println("Definované typy kroků:");
//        for (TypeOfStep tos : values()) {
//            System.out.printf(
//                "%20s: subtype=%-13s group=%-11s paramCount=%2d\n",
//                tos, tos.subtype, tos.group, tos.paramCount);
//        }
//        System.out.println("==============================================");
//    }
}
