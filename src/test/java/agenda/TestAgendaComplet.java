package agenda;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestAgendaComplet {

    @Test
    public void testTerminaisonSansRepetition() {
        LocalDateTime debut = LocalDateTime.of(2020, 1, 1, 10, 0);
        Event event = new Event("Simple", debut, Duration.ofHours(1));

        // On essaie de définir une fin sans avoir activé la répétition
        event.setTermination(LocalDate.of(2020, 1, 10)); 
        event.setTermination(10);

        // Cela ne doit rien changer
        assertNull(event.getTerminationDate());
        assertEquals(0, event.getNumberOfOccurrences());
    }

    @Test
    public void testAgendaMelange() {
        Agenda agenda = new Agenda();
        LocalDate aujourdhui = LocalDate.of(2020, 1, 1);
        LocalDateTime debut = aujourdhui.atTime(10, 0);

        Event present = new Event("Présent", debut, Duration.ofHours(1));
        Event absent = new Event("Absent", debut.minusDays(1), Duration.ofHours(1));

        agenda.addEvent(present);
        agenda.addEvent(absent);

        List<Event> resultat = agenda.eventsInDay(aujourdhui);
        
        // Vérifie que seul l'événement du jour est retourné
        assertEquals(1, resultat.size());
        assertTrue(resultat.contains(present));
        assertFalse(resultat.contains(absent));
    }

    @Test
    public void testCalculUneSeuleOccurrence() {
        LocalDate debut = LocalDate.of(2020, 1, 1);
        // Une terminaison le jour même = 1 occurrence
        Termination fin = new Termination(debut, ChronoUnit.DAYS, debut);
        
        assertEquals(1, fin.numberOfOccurrences());
    }
}