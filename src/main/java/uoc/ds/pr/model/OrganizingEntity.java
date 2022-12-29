package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;

public class OrganizingEntity {
    private String organizationId;
    private String description;
    private String name;
    private List<SportEvent> sportEventList;

    public OrganizingEntity(String organizationId, String name, String description) {
        this.organizationId = organizationId;
        this.name = name;
        this.description = description;
        sportEventList = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getDescription() {
        return description;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iterator<SportEvent> activities() {
        return sportEventList.values();
    }

    public void addEvent(SportEvent sportEvent) {
        sportEventList.insertEnd(sportEvent);
    }

    public int numEvents() {
        return sportEventList.size();
    }

    public boolean hasActivities() {
        return sportEventList.size() > 0;
    }

    public Iterator<SportEvent> sportEvents() {
        return sportEventList.values();
    }
}
