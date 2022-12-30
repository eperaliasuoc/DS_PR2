package uoc.ds.pr.model;

import uoc.ds.pr.SportEvents4Club;

import java.time.LocalDate;
import java.util.Comparator;

public class File implements Comparable<File> {

    public static final Comparator<File> CMP = (File f1, File f2)->f1.compareTo(f2);
    private final SportEvents4Club.Type type;
    private String eventId;
    private String description;
    private String recordId;
    private byte resources;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate dateStatus;
    private String descriptionStatus;
    private int num;
    private SportEvents4Club.Status status;
    private OrganizingEntity organizingEntity;
    //private String organization;

    public File(String id, String eventId, OrganizingEntity organization, String description, SportEvents4Club.Type type, byte resources, int max, LocalDate startDate, LocalDate endDate) {
        this.recordId = id;
        this.eventId = eventId;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.resources = resources;
        this.endDate = endDate;
        this.num = num;
        this.status = SportEvents4Club.Status.PENDING;
        this.organizingEntity = organization;
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

    public String getDescriptionStatus() {
        return descriptionStatus;
    }

    public void setDescriptionStatus(String descriptionStatus) {
        this.descriptionStatus = descriptionStatus;
    }

    public String getFileId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(LocalDate dateStatus) {
        this.dateStatus = dateStatus;
    }

    public void setStartDate(LocalDate date) {
        this.startDate = date;
    }

    public SportEvents4Club.Status getStatus() {
        return status;
    }

    public void setStatus(SportEvents4Club.Status status) {
        this.status = status;
    }

    public void update(SportEvents4Club.Status status, LocalDate date, String description) {
        this.setStatus(status);
        this.setDateStatus(date);
        this.setDescriptionStatus(description);
    }

    public boolean isEnabled() {
        return this.status == SportEvents4Club.Status.ENABLED;
    }

    public SportEvent newSportEvent() {
        SportEvent sportEvent = new SportEvent(this.eventId, this.description, this.type, this.startDate, this.endDate, this.num, this);
        organizingEntity.addEvent(sportEvent);
        sportEvent.setOrganizingEntity(organizingEntity);

        return sportEvent;
    }

    @Override
    public int compareTo(File f) {
        int result = this.startDate.compareTo(f.startDate);
        if (result == 0) {
            result = f.getType().compareTo(this.getType());
        }
        return result;
    }

    public SportEvents4Club.Type getType() {
        return type;
    }

}
