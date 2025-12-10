package agenda;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestGetterSetter {

    @Test
    public void testGettersEvent() {
        LocalDateTime debut = LocalDateTime.of(2020, 1, 1, 10, 0);
        Duration duree = Duration.ofHours(1);
        Event e = new Event("Titre test", debut, duree);

        assertEquals("Titre test", e.getTitle());
        assertEquals(debut, e.getStart());
        assertEquals(duree, e.getDuration());
        assertNotNull(e.toString());
    }

    @Test
    public void testClasseRepetition() {
        Repetition rep = new Repetition(ChronoUnit.DAYS);
        
        assertEquals(ChronoUnit.DAYS, rep.getFrequency());

        LocalDate dateEx = LocalDate.of(2020, 1, 1);
        rep.addException(dateEx);
        assertTrue(rep.isException(dateEx));
        assertFalse(rep.isException(LocalDate.of(2020, 1, 2)));

        Termination fin = new Termination(LocalDate.now(), ChronoUnit.DAYS, 5);
        rep.setTermination(fin);
        assertEquals(fin, rep.getTermination());
    }

    @Test
    public void testClasseTermination() {
        LocalDate debut = LocalDate.of(2020, 1, 1);
        
        // Test constructeur par date
        Termination termDate = new Termination(debut, ChronoUnit.WEEKS, LocalDate.of(2020, 1, 15));
        assertEquals(LocalDate.of(2020, 1, 15), termDate.terminationDateInclusive());
        assertTrue(termDate.numberOfOccurrences() > 0);

        // Test constructeur par nombre
        Termination termNb = new Termination(debut, ChronoUnit.DAYS, 10);
        assertEquals(10, termNb.numberOfOccurrences());
        assertNotNull(termNb.terminationDateInclusive());
    }
    
    @Test
    public void testAgendaVide() {
        Agenda a = new Agenda();
        assertTrue(a.eventsInDay(LocalDate.now()).isEmpty());
        
        Event e = new Event("Test", LocalDateTime.now(), Duration.ZERO);
        a.addEvent(e);
        assertFalse(a.eventsInDay(LocalDateTime.now().toLocalDate()).isEmpty());
    }
    
    @Test
    public void testCasLimitesEvent() {
        Event e = new Event("Simple", LocalDateTime.now(), Duration.ofHours(1));
        
        // Pas de répétition définie -> pas de fin
        assertNull(e.getTerminationDate());
        assertEquals(0, e.getNumberOfOccurrences());
        
        // Appel sans effet car pas de répétition active
        e.setTermination(10); 
        e.setTermination(LocalDate.now());
    }
}