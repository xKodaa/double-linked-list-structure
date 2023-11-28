package data;

import java.time.LocalDateTime;

public class MereniVoda extends Mereni {
    private final double spotrebaM3;

    public MereniVoda(int idSenzoru, TypSenzoru typSenzoru, LocalDateTime casMereni, double spotrebaM3) {
        super(idSenzoru, typSenzoru, casMereni);
        this.spotrebaM3 = zaokrouhliSpotrebu(spotrebaM3);
    }

    public double getSpotrebaM3() {
        return spotrebaM3;
    }

    @Override
    public String toString() {
        return "MereniVoda {" +
                "idSenzoru = " + idSenzoru +
                ", typSenzoru = " + typSenzoru +
                ", casMereni = " + DATETIME_FORMATTER.format(casMereni) +
                ", spotrebaM3 = " + spotrebaM3 +
                '}';
    }
}
