package uoc.ds.pr.model;

import java.time.LocalDate;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.SportEvents4Club;

public class Player {
    private String id;
    private String name;
    private String surname;
    private List<SportEvent> events;
    private LocalDate birthday;
    private int numRatings;
    private int totalRatings;
    SportEvents4Club.Level level;
    //private List<Rating> ratings;

	public Player(String idUser, String name, String surname, LocalDate birthday) {
        this.id = idUser;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        events = new LinkedList<>();
        numRatings = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public boolean is(String playerID) {
        return id.equals(playerID);
    }

    public void addEvent(SportEvent sportEvent) {
        events.insertEnd(sportEvent);
        //SportEvents4Club.Level level = sportEvent.ge
    }

    public int numEvents() {
        return events.size();
    }

    public boolean isInSportEvent(String eventId) {
        boolean found = false;
        SportEvent sportEvent = null;
        Iterator<SportEvent> it = getEvents();
        while (it.hasNext() && !found) {
            sportEvent = it.next();
            found = sportEvent.is(eventId);
        }
        return found;
    }

    public int numSportEvents() {
        return events.size();
    }

    public Iterator<SportEvent> getEvents() {
        return events.values();
    }

    public boolean hasEvents() {
        return this.events.size()>0;
    }

    public SportEvents4Club.Level getLevel() {
        int rating = numRatings;
        return LevelHelper.getLevel(rating);
    }

   // public boolean hasNumRatings(int num) {
   //     return numRatings>=num;
   // }

    public void incNumRatings() {
        numRatings++;
    }

    public void addRating(SportEvents4Club.Rating rating) {
        incNumRatings();
        totalRatings+= rating.getValue();
    }

    public Integer getNumRatings() {
        return numRatings;
    }

    //public boolean hasLessThanNumRatings(int num) {
    //    return numRatings<=num;
    //}
}
