package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class Termination {

    private final LocalDate start;
    private final ChronoUnit frequency;
    private final LocalDate terminationDate;

    public LocalDate terminationDateInclusive() {
        return terminationDate;
    }

    public long numberOfOccurrences() {
        // Formule : Différence entre début et fin + 1 (pour inclure le premier jour)
        return frequency.between(start, terminationDate) + 1;
    }

    public Termination(LocalDate start, ChronoUnit frequency, LocalDate terminationInclusive) {
        this.start = start;
        this.frequency = frequency;
        this.terminationDate = terminationInclusive;
    }

    public Termination(LocalDate start, ChronoUnit frequency, long numberOfOccurrences) {
        this.start = start;
        this.frequency = frequency;
        // Si on a 1 occurrence, la date de fin est la date de début.
        // Si on a 2 occurrences, c'est début + 1 unité. Donc (n - 1).
        this.terminationDate = start.plus(numberOfOccurrences - 1, frequency);
    }
}