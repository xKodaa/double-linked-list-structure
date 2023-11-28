package data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Mereni {
    protected int idSenzoru;
    protected TypSenzoru typSenzoru;
    protected LocalDateTime casMereni;
    protected static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected Mereni(int idSenzoru, TypSenzoru typSenzoru, LocalDateTime casMereni) {
        this.idSenzoru = idSenzoru;
        this.typSenzoru = typSenzoru;
        this.casMereni = casMereni;
    }

    public double zaokrouhliSpotrebu(double num) {
        return Math.round(num * 100.0) / 100.0;
    }

    public int getIdSenzoru() {
        return idSenzoru;
    }
    public TypSenzoru getTypSenzoru() {
        return typSenzoru;
    }
    public LocalDateTime getCasMereni() {
        return casMereni;
    }
}
