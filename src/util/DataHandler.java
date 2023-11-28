package util;

import data.Mereni;
import data.MereniElektrina;
import data.MereniVoda;
import data.TypSenzoru;
import structure.IAbstrDoubleList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class DataHandler {
    private static final String FILENAME = "mereni.csv";
    private final IAbstrDoubleList<Mereni> abstrDoubleList;

    public DataHandler(IAbstrDoubleList<Mereni> abstrDoubleList) {
        this.abstrDoubleList = abstrDoubleList;
    }

    public void saveToCsv() {
        try (FileWriter writer = new FileWriter(FILENAME)) {
            for (Mereni mereni : abstrDoubleList) {
                writer.write(formatMereni(mereni) + "\n");
            }
            System.out.println("Data byla uložena do souboru " + FILENAME);
        } catch (IOException e) {
            System.err.println("Chyba při ukládání dat do souboru " + FILENAME);
        }
    }

    private String formatMereni(Mereni mereni) {
        String csv = "";
        csv += mereni.getIdSenzoru() + ";";
        csv += mereni.getTypSenzoru() + ";";
        csv += mereni.getCasMereni() + ";";
        if (mereni instanceof MereniVoda mereniVoda) {
            csv += mereniVoda.getSpotrebaM3();
        } else if (mereni instanceof MereniElektrina mereniElektrina) {
            csv += mereniElektrina.getSpotrebaVT() + ";" + mereniElektrina.getSpotrebaNT();
        }
        return csv;
    }

    public void loadFromCsv() {
        abstrDoubleList.zrus();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    int idSenzoru = Integer.parseInt(parts[0]);
                    TypSenzoru typSenzoru = TypSenzoru.valueOf(parts[1]);
                    LocalDateTime casMereni = LocalDateTime.parse(parts[2]);

                    if (typSenzoru == TypSenzoru.VODA && parts.length == 4) {
                        double spotrebaM3 = Double.parseDouble(parts[3]);
                        Mereni mereniVoda = new MereniVoda(idSenzoru, typSenzoru, casMereni, spotrebaM3);
                        abstrDoubleList.vlozPosledni(mereniVoda);
                    } else if (typSenzoru == TypSenzoru.ELEKTRINA && parts.length == 5) {
                        double spotrebaVT = Double.parseDouble(parts[3]);
                        double spotrebaNT = Double.parseDouble(parts[4]);
                        Mereni mereniElektrina = new MereniElektrina(idSenzoru, typSenzoru, casMereni, spotrebaVT, spotrebaNT);
                        abstrDoubleList.vlozPosledni(mereniElektrina);
                    }
                }
            }
            System.out.println("Data byla načtena ze souboru " + FILENAME);
        } catch (IOException e) {
            System.err.println("Chyba při načítání dat ze souboru " + FILENAME);
        }
    }
}
