package manager;

import data.Mereni;
import data.MereniElektrina;
import data.MereniVoda;
import data.TypSenzoru;
import structure.IAbstrDoubleList;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestMain {
    private static final ISpravceMereni spravce = new SpravceMereni();

    public static void main(String[] args) {
        spravce.generujData();
//        testVkladani();
        spravce.ulozData();
        spravce.vypisData();
        spravce.zrus();
        spravce.nactiData();
        System.out.println("------VYPIS------");
        spravce.vypisData();
//        testSpotreby();
//        testMereniDen();
//        testZpristupniMereni();
    }

    private static void testVkladani() {
        System.out.println("------VKLADANI------");
        Mereni mereni = new MereniVoda(1, TypSenzoru.VODA, LocalDateTime.now(), 15.0);
        spravce.vlozMereni(mereni, Pozice.PRVNI);

        mereni = new MereniVoda(2, TypSenzoru.VODA, LocalDateTime.now().minusDays(5), 40.0);
        spravce.vlozMereni(mereni, Pozice.POSLEDNI);

        mereni = new MereniVoda(2, TypSenzoru.VODA, LocalDateTime.now().minusDays(5), 15.0);
        spravce.vlozMereni(mereni, Pozice.PREDCHUDCE);

        mereni = new MereniElektrina(2, TypSenzoru.ELEKTRINA, LocalDateTime.now().minusDays(10), 15.0, 16.0);
        spravce.vlozMereni(mereni, Pozice.PRVNI);

        mereni = new MereniElektrina(2, TypSenzoru.ELEKTRINA, LocalDateTime.now().minusDays(5), 15.0, 16.0);
        spravce.vlozMereni(mereni, Pozice.PRVNI);

    }

    private static void testZpristupniMereni() {
        System.out.println("------ZPRISTUPNENI_PRVKU------");
        System.out.println(spravce.zpristupniMereni(Pozice.PRVNI).toString());
        System.out.println(spravce.zpristupniMereni(Pozice.POSLEDNI).toString());
    }

    private static void testSpotreby() {
        System.out.println("------SPOTREBA------");
        double max = spravce.maxSpotreba(2, LocalDateTime.now().minusDays(30), LocalDateTime.now());
        System.out.println("Max spotřeba: " + max);
        double prumer = spravce.prumerSpotreba(2, LocalDateTime.now().minusDays(30), LocalDateTime.now());
        System.out.println("Průměrná spotřeba: " + prumer);
    }

    private static void testMereniDen() {
        System.out.println("------MERENI_DEN------");
        IAbstrDoubleList<Mereni> list = spravce.mereniDen(2, LocalDate.of(2023,7,2));
        spravce.vypisData();
    }

}
