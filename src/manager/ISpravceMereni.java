package manager;

import data.Mereni;
import structure.IAbstrDoubleList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

public interface ISpravceMereni {
    void vlozMereni(Mereni mereni, Pozice pozice);
    Mereni zpristupniMereni(Pozice pozice);
    Mereni odeberMereni (Pozice pozice);
    Iterator<Mereni> iterator();
    IAbstrDoubleList<Mereni> mereniDen(int idSenzoru, LocalDate datum);
    double maxSpotreba(int idSenzoru, LocalDateTime datumOd, LocalDateTime datumDo);
    double prumerSpotreba(int idSenzoru, LocalDateTime datumOd, LocalDateTime datumDo);
    void ulozData();
    void nactiData();
    void vypisData();
    void generujData();
    void zrus();

}
