package cz.vse.java.kedv00.adventura.api;

/*****************************************************
 * Interface, který vyžaduje implementaci metody
 * k updatování, pokud dojde ke změně u pozorovaného objektu.
 */
public interface Observer
{
    /******************************************
     * Metoda, která zajišťuje aktualizaci.
     */
    void update();
}
