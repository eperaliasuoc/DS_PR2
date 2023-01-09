package uoc.ds.pr.model;

import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.nonlinear.PriorityQueue;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.adt.sequential.QueueArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.SportEvents4Club;

import java.time.LocalDate;
import java.util.Comparator;


import static uoc.ds.pr.SportEvents4Club.MAX_NUM_ENROLLMENT;

public class SportEvent implements Comparable<SportEvent> {
    //public static final Comparator<SportEvent> CMP_V = (SportEvent se1, SportEvent se2)->se1.compareTo(se2);

    public static final Comparator<SportEvent> CMP_V = (SportEvent se1, SportEvent se2)->Double.compare(se1.rating(), se2.rating());
    public static final Comparator<String> CMP_K = (k1, k2)-> k1.compareTo(k2);
    private String eventId;
    private String description;
    private SportEvents4Club.Type type;
    private LocalDate startDate;
    private LocalDate endDate;
    private int max;
    private File file;
    private List<Rating> ratings;
    private Queue<Enrollment> enrollments;
    private PriorityQueue<Enrollment> enrollmentsubs;
    private double sumRatings;
    private double numRatings;
    private int numSubstitutes;
    private HashTable<String, Attender> attenders;
    private List<Worker> workers;
    private OrganizingEntity organizingEntity;

    public SportEvent(String eventId, String description, SportEvents4Club.Type type, LocalDate startDate, LocalDate endDate, int max, File file) {
        setEventId(eventId);
        setDescription(description);
        setStartDate(startDate);
        setEndDate(endDate);
        setType(type);
        setMax(max);
        setFile(file);
        this.enrollments = new QueueArrayImpl<>(MAX_NUM_ENROLLMENT);
        this.enrollmentsubs = new PriorityQueue<Enrollment>(Enrollment.CMP_RATING);
        this.ratings = new LinkedList<Rating>();
        numSubstitutes = 0;
        attenders = new HashTable<String, Attender>();
        this.workers = new LinkedList<Worker>();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SportEvents4Club.Type getType() {
        return type;
    }

    public void setType(SportEvents4Club.Type type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public OrganizingEntity getOrganizingEntity() {
        return organizingEntity;
    }


    public double rating() {
        return (this.ratings.size()>0?(sumRatings / this.ratings.size()):0);
    }

    public void addRating(SportEvents4Club.Rating rating, String message, Player player) {
        Rating newRating = new Rating(rating, message, player);
        ratings.insertEnd(newRating);
        sumRatings+=rating.getValue();
        numRatings++;
    }

    public double getRating() {
        return (numRatings != 0 ? (double)sumRatings / numRatings : 0);
    }

    public boolean hasRatings() {
        return ratings.size() > 0;
    }

    public Iterator<Rating> ratings() {
        return ratings.values();
    }

    public void addEnrollment(Player player) {
        addEnrollment(player, false);
    }

    public void addEnrollment(Player player, boolean isSubstitute) {
        if (isSubstitute == false) {
            enrollments.add(new Enrollment(player, isSubstitute));
        } else {
            enrollmentsubs.add(new Enrollment(player, isSubstitute));
        }
    }

    public void incSubstitutes() {
        numSubstitutes++;
    }

    public void addEnrollmentAsSubstitute(Player player) {
        addEnrollment(player, true);
        incSubstitutes();
    }
    public Iterator<Enrollment> EnrollmentAsSubstitute() {
        return enrollmentsubs.values();
    }

    public int getNumSubstitutes() {
        return numSubstitutes;
    }

    public boolean is(String eventId) {
        return this.eventId.equals(eventId);
    }

    @Override
    public int compareTo(SportEvent se2) {
        return Double.compare(rating(), se2.rating() );
        //return this.getEventId().compareTo(se2.getEventId());
    }

    public boolean isFull() {
        return (enrollments.size() + attenders.size() >= max);
    }

    public int numPlayers() {
        int totalPlayers = enrollments.size() + enrollmentsubs.size();
        return totalPlayers;
    }

    public OrganizingEntity getOrganization() {
        return organizingEntity;
    }

    public void setOrganizingEntity(OrganizingEntity organization) {
        this.organizingEntity = organization;
    }

    public void addAttender(String phoneNumber, String name) {
        Attender newattender = new Attender (phoneNumber, name);
        attenders.put(newattender.getPhoneNumber(), newattender);
    }

    public boolean hasAttenders() {
        return attenders.size() > 0;
    }

    public boolean existsAttender(String phone) {
        boolean found = false;
        if (attenders.get(phone) != null) {
            found = true;
        }
        return found;
    }

    public int numAttenders() {
        return attenders.size();
    }

    public Iterator<Attender> attenders() {
        return attenders.values();
    }

    public Attender getAttender(String phonenumber) {
        Attender attender = attenders.get(phonenumber);
        return attender;
    }

    public void addWorker(Worker worker) {
        workers.insertEnd(worker);
    }

    //public void removeWorker(Worker worker) {
    //    Worker.removeWorker(workers, worker);
    //}

    public int numWorkers() {
        return workers.size();
    }

    public Iterator<Worker> workers() {
        return workers.values();
    }

    public boolean hasWorkers() {
        return workers.size() > 0;
    }

    public boolean isInSportEvent(String dni) {
        Iterator<Worker> it = workers.values();
        boolean found = false;
        Worker w = null;
        while (it.hasNext() && !found) {
            w = it.next();
            found = w.is(dni);
        }
        return found;
    }

}
