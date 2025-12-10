package agenda;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestLimites {

    @Test
    public void testRepetitionInfinie() {
        LocalDateTime debut = LocalDateTime.of(2020, 1, 1, 12, 0);
        Event eventInfini = new Event("Infini", debut, Duration.ofHours(1));
        
        // Répétition active mais pas de date de fin
        eventInfini.setRepetition(ChronoUnit.DAYS);

        assertEquals(0, eventInfini.getNumberOfOccurrences());
        assertNull(eventInfini.getTerminationDate());
    }

    @Test
    public void testMauvaiseFrequence() {
        LocalDateTime debut = LocalDateTime.of(2020, 1, 1, 12, 0); // Mercredi
        Event eventHebdo = new Event("Hebdo", debut, Duration.ofHours(1));
        eventHebdo.setRepetition(ChronoUnit.WEEKS);

        // Test le Jeudi (J+1), ne doit pas correspondre à la semaine
        assertFalse(eventHebdo.isInDay(LocalDate.of(2020, 1, 2)));
    }

    @Test
    public void testBornesExactes() {
        LocalDateTime debut = LocalDateTime.of(2020, 1, 1, 12, 0);
        Event event = new Event("Bornes", debut, Duration.ofHours(1));
        event.setRepetition(ChronoUnit.DAYS);
        
        // Fin après 3 occurrences (Jan 1, 2, 3)
        event.setTermination(3); 

        // Le dernier jour est inclus
        assertTrue(event.isInDay(LocalDate.of(2020, 1, 3)));
        
        // Le jour d'après est exclu
        assertFalse(event.isInDay(LocalDate.of(2020, 1, 4)));
    }

    @Test
    public void testDatesHorsLimites() {
        LocalDateTime debut = LocalDateTime.of(2020, 1, 1, 12, 0);
        Event simple = new Event("Simple", debut, Duration.ofHours(2));

        // Date future lointaine
        assertFalse(simple.isInDay(LocalDate.of(2021, 1, 1)));
        
        // Date passée lointaine
        assertFalse(simple.isInDay(LocalDate.of(2019, 1, 1)));
    }
    
    @Test
    public void testRepetitionDirecte() {
        // Vérifie simplement que Repetition gère bien le null par défaut
        Repetition r = new Repetition(ChronoUnit.MONTHS);
        assertNull(r.getTermination()); 
    }
}