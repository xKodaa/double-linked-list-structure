package util;

import data.Mereni;
import data.MereniElektrina;
import data.MereniVoda;
import data.TypSenzoru;
import structure.IAbstrDoubleList;

import java.time.LocalDateTime;
import java.util.Random;

public class Generator {
    private final IAbstrDoubleList<Mereni> abstrDoubleList;
    private final Random random;

    public Generator(IAbstrDoubleList<Mereni> abstrDoubleList) {
        this.abstrDoubleList = abstrDoubleList;
        this.random = new Random();
    }

    public void generate(LocalDateTime date) {
        while (date.isBefore(LocalDateTime.now())) {
            date = shiftDate(date);
            Mereni mereni;
            int randomTypMereni = random.nextInt(0,2);
            if (randomTypMereni == 0) { // VODA
                mereni = new MereniVoda(getRandomIdSenzoru(), TypSenzoru.VODA, date, getRandomSpotreba());
            } else {  // ELEKTRINA
                mereni = new MereniElektrina(getRandomIdSenzoru(), TypSenzoru.ELEKTRINA, date, getRandomSpotreba(), getRandomSpotreba());
            }
            abstrDoubleList.vlozPrvni(mereni);
        }
    }

    public Mereni generateMereni() {
        LocalDateTime date = getRandomDate();
        int randomTypMereni = random.nextInt(0,2);
        if (randomTypMereni == 0) { // VODA
            return new MereniVoda(getRandomIdSenzoru(), TypSenzoru.VODA, date, getRandomSpotreba());
        } else {  // ELEKTRINA
            return new MereniElektrina(getRandomIdSenzoru(), TypSenzoru.ELEKTRINA, date, getRandomSpotreba(), getRandomSpotreba());
        }
    }

    private LocalDateTime getRandomDate() {
        return LocalDateTime.now().minusDays(random.nextLong(1,30));
    }

    private double getRandomSpotreba() {
        return random.nextDouble(10.0, 100.0);
    }

    private LocalDateTime shiftDate(LocalDateTime date) {
        return date.plusHours(random.nextInt(2,24));
    }

    private int getRandomIdSenzoru() {
        return random.nextInt(1,21);
    }
}