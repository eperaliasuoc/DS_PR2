package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;

public class OrganizingEntity {
//public class OrganizingEntity implements Comparable<OrganizingEntity>{
  //  public static final Comparator<OrganizingEntity> COMP_ATTENDER =  (OrganizingEntity org1, OrganizingEntity org2)->Double.compare(org1.getAttenders(), org2.getAttenders());
    private String organizationId;
    private String description;
    private String name;
    private List<SportEvent> sportEventList;
    private List<Worker> workers;
    private double numRatings;

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
    //public Iterator<SportEvent> activities() {
    //    return sportEventList.values();
    //}
}
