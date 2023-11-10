package cz.vse.java.kedv00.adventura.api;

/*****************************************************
 * Interface, který vyžaduje implementaci metody
 * k registrování observeru u pozorovaného objektu.
 */
public interface Observable
{
    /*****************************
     * Metoda, která implementuje registrování observeru
     * @param typeOfChange Typ změny
     * @param observer Pozorovatel
     */
    void registerObserver(TypeOfChange typeOfChange, Observer observer);
}
