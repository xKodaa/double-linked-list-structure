package data;

import java.time.LocalDateTime;

public class MereniElektrina extends Mereni {
    private final double spotrebaVT;
    private final double spotrebaNT;

    public MereniElektrina(int idSenzoru, TypSenzoru typSenzoru, LocalDateTime casMereni, double spotrebaVT, double spotrebaNT) {
        super(idSenzoru, typSenzoru, casMereni);
        this.spotrebaVT = zaokrouhliSpotrebu(spotrebaVT);
        this.spotrebaNT = zaokrouhliSpotrebu(spotrebaNT);
    }

    public double getSpotrebaVT() {
        return spotrebaVT;
    }
    public double getSpotrebaNT() {
        return spotrebaNT;
    }

    @Override
    public String toString() {
        return "MereniElektrina {" +
                "idSenzoru = " + idSenzoru +
                ", typSenzoru = " + typSenzoru +
                ", casMereni = " + DATETIME_FORMATTER.format(casMereni) +
                ", spotrebaVT = " + spotrebaVT +
                ", spotrebaNT = " + spotrebaNT +
                '}';
    }
}
