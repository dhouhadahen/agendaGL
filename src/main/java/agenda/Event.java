package agenda;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Event {

    private String myTitle;
    private LocalDateTime myStart;
    private Duration myDuration;
    
    // Gestion de la répétition et des exceptions
    private Repetition repetition;
    private Set<LocalDate> exceptions = new HashSet<>();

    /**
     * Constructs an event
     *
     * @param title the title of this event
     * @param start the start time of this event
     * @param duration the duration of this event
     */
    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.repetition = new Repetition(frequency);
    }

    public void addException(LocalDate date) {
        this.exceptions.add(date);
    }

    public void setTermination(LocalDate terminationInclusive) {
        if (repetition != null) {
            repetition.setTermination(new Termination(myStart.toLocalDate(), repetition.getFrequency(), terminationInclusive));
        }
    }

    public void setTermination(long numberOfOccurrences) {
        if (repetition != null) {
            repetition.setTermination(new Termination(myStart.toLocalDate(), repetition.getFrequency(), numberOfOccurrences));
        }
    }

    public int getNumberOfOccurrences() {
        if (repetition == null || repetition.getTermination() == null) {
            return 0;
        }
        return (int) repetition.getTermination().numberOfOccurrences();
    }

    public LocalDate getTerminationDate() {
        if (repetition == null || repetition.getTermination() == null) {
            return null;
        }
        return repetition.getTermination().terminationDateInclusive();
    }

    /**
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
    public boolean isInDay(LocalDate aDay) {
        // si le jour est exclu, c'est faux direct
        if (exceptions.contains(aDay)) {
            return false;
        }

        if (repetition != null) {
            LocalDate startDate = myStart.toLocalDate();
            
            // L'événement ne peut pas avoir lieu avant son début
            if (aDay.isBefore(startDate)) {
                return false;
            }

            // Calculer l'écart en jours
            long daysBetween = ChronoUnit.DAYS.between(startDate, aDay);
            long frequencyDays = repetition.getFrequency().getDuration().toDays();
            
            // est ce que ça tombe sur un jour de répétition
            if (daysBetween % frequencyDays != 0) {
                return false;
            }

            // Vérifier la terminaison si dispo
            Termination termination = repetition.getTermination();
            if (termination != null) {
                if (aDay.isAfter(termination.terminationDateInclusive())) {
                    return false;
                }
            }
            
            return true;
        }

        // 3. Cas d'un événement simple (non répétitif)
        // L'événement a lieu entre le début et la fin (inclus)
        LocalDate startDate = myStart.toLocalDate();
        LocalDate endDate = myStart.plus(myDuration).toLocalDate();
        
        return !aDay.isBefore(startDate) && !aDay.isAfter(endDate);
    }
    
    public String getTitle() {
        return myTitle;
    }

    public LocalDateTime getStart() {
        return myStart;
    }

    public Duration getDuration() {
        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}