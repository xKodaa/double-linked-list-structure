package manager;

import data.Mereni;
import data.MereniElektrina;
import data.MereniVoda;
import structure.AbstrDoubleList;
import structure.IAbstrDoubleList;
import util.DataHandler;
import util.Generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

public class SpravceMereni implements ISpravceMereni {
    private final AbstrDoubleList<Mereni> abstrDoubleList;

    public SpravceMereni() {
        abstrDoubleList = new AbstrDoubleList<>();
    }

    @Override
    public void vlozMereni(Mereni mereni, Pozice pozice) {
        switch (pozice) {
            case PRVNI -> abstrDoubleList.vlozPrvni(mereni);
            case POSLEDNI -> abstrDoubleList.vlozPosledni(mereni);
            case NASLEDNIK -> abstrDoubleList.vlozNaslednika(mereni);
            case PREDCHUDCE -> abstrDoubleList.vlozPredchudce(mereni);
            case AKTUALNI -> System.err.println("Aktuální prvek zde nelze vkládat!");
        }
    }

    @Override
    public Mereni zpristupniMereni(Pozice pozice) {
        return switch (pozice) {
            case PRVNI -> abstrDoubleList.zpristupniPrvni();
            case POSLEDNI -> abstrDoubleList.zpristupniPosledni();
            case NASLEDNIK -> abstrDoubleList.zpristupniNaslednika();
            case PREDCHUDCE -> abstrDoubleList.zpristupniPredchudce();
            case AKTUALNI -> abstrDoubleList.zpristupniAktualni();
        };
    }

    @Override
    public Mereni odeberMereni(Pozice pozice) {
        return switch (pozice) {
            case PRVNI -> abstrDoubleList.odeberPrvni();
            case POSLEDNI -> abstrDoubleList.odeberPosledni();
            case NASLEDNIK -> abstrDoubleList.odeberNaslednika();
            case PREDCHUDCE -> abstrDoubleList.odeberPredchudce();
            case AKTUALNI -> abstrDoubleList.odeberAktualni();
        };
    }

    @Override
    public Iterator<Mereni> iterator() {
        return abstrDoubleList.iterator();
    }

    @Override
    public IAbstrDoubleList<Mereni> mereniDen(int idSenzoru, LocalDate datum) {
        IAbstrDoubleList<Mereni> newList = new AbstrDoubleList<>();
        Iterator<Mereni> iterator = iterator();
        while (iterator.hasNext()) {
            Mereni mereni = iterator.next();
            if (mereni.getIdSenzoru() == idSenzoru && mereni.getCasMereni().getDayOfMonth() == datum.getDayOfMonth()) {
                newList.vlozPrvni(mereni);
            }
        }
        return newList;
    }

    @Override
    public double maxSpotreba(int idSenzoru, LocalDateTime datumOd, LocalDateTime datumDo) {
        Iterator<Mereni> iterator = iterator();
        double maxSpotreba = 0.0;
        double spotreba;
        Mereni mereni = null;
        while (iterator.hasNext()) {
            mereni = iterator.next();
            if (checkIdAndInterval(idSenzoru, datumOd, datumDo, mereni)) {
                if (mereni instanceof MereniVoda mereniVoda) {  // spotřeba vody je čistě spotrebaM3
                    spotreba = mereniVoda.getSpotrebaM3();
                    if (spotreba > maxSpotreba) {
                        maxSpotreba = spotreba;
                    }
                } else if (mereni instanceof MereniElektrina mereniElektrina) { // spotřeba elektřiny je spotrebaNT + spotreba VT
                    spotreba = mereniElektrina.getSpotrebaNT() + mereniElektrina.getSpotrebaVT();
                    if (spotreba > maxSpotreba) {
                        maxSpotreba = spotreba;
                    }
                }
            }
        }
        if (mereni != null) {
            return mereni.zaokrouhliSpotrebu(maxSpotreba);
        }
        return 0.0;
    }

    @Override
    public double prumerSpotreba(int idSenzoru, LocalDateTime datumOd, LocalDateTime datumDo) {
        Iterator<Mereni> iterator = iterator();
        double prumernaSpotreba;
        double spotreba = 0.0;
        int pocetMereni = 0;
        Mereni mereni = null;
        while (iterator.hasNext()) {
            mereni = iterator.next();
            if (checkIdAndInterval(idSenzoru, datumOd, datumDo, mereni)) {
                if (mereni instanceof MereniVoda mereniVoda) {  // spotřeba vody je čistě spotrebaM3
                    spotreba += mereniVoda.getSpotrebaM3();
                    pocetMereni++;
                } else if (mereni instanceof MereniElektrina mereniElektrina) { // spotřeba elektřiny je spotrebaNT + spotreba VT
                    spotreba += mereniElektrina.getSpotrebaNT() + mereniElektrina.getSpotrebaVT();
                    pocetMereni++;
                }
            }
        }
        if (pocetMereni != 0) {
            prumernaSpotreba = spotreba / pocetMereni;
            return mereni.zaokrouhliSpotrebu(prumernaSpotreba);
        } else {
            System.err.println("Nelze zjistit průměrnou spotřebu, protože seznam je prázdný!");
            return 0.0;
        }
    }

    private boolean checkIdAndInterval(int idSenzoru, LocalDateTime datumOd, LocalDateTime datumDo, Mereni mereni) {
        LocalDateTime casMereni = mereni.getCasMereni();
        return mereni.getIdSenzoru() == idSenzoru && casMereni.isAfter(datumOd) && casMereni.isBefore(datumDo);
    }

    @Override
    public void ulozData() {
        DataHandler dataHandler = new DataHandler(abstrDoubleList);
        dataHandler.saveToCsv();
    }

    @Override
    public void nactiData() {
        DataHandler dataHandler = new DataHandler(abstrDoubleList);
        dataHandler.loadFromCsv();
    }

    @Override
    public void generujData() {
        Generator generator = new Generator(abstrDoubleList);
        generator.generate(LocalDateTime.now().minusDays(30));
    }

    @Override
    public void vypisData() {
        for (Mereni mereni: abstrDoubleList) {
            System.out.println(mereni.toString());
        }
    }

    @Override
    public void zrus() {
        abstrDoubleList.zrus();
    }

    public AbstrDoubleList<Mereni> getAbstrDoubleList() {
        return abstrDoubleList;
    }
}
