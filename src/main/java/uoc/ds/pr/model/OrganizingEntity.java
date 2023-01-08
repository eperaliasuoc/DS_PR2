package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import java.util.Comparator;

//public class OrganizingEntity {
public class OrganizingEntity implements Comparable<OrganizingEntity>{
    //public static final Comparator<OrganizingEntity> COMP_ATTENDER =  (OrganizingEntity org1, OrganizingEntity org2)->org1.compareTo(org2);
    public static final Comparator<OrganizingEntity> COMP_ATTENDER =  (OrganizingEntity org1, OrganizingEntity org2)->Integer.compare(org1.numAttenders(),org2.numAttenders());

    private String organizationId;
    private String description;
    private String name;
    private List<SportEvent> sportEventList;
    private List<Worker> workers;
    private int numAttenders;


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

    public int numAttenders() {
        return numAttenders;
    }

    public void incAttenders() {
        numAttenders++;
    }

    public int compareTo(OrganizingEntity org) {
        //int result = this.numAttenders().compareTo(org.numAttenders());
        int result = Integer.compare(numAttenders(), org.numAttenders());
        /*System.out.println("Organization1 del compareTo de  = " + this.getOrganizationId());
        System.out.println("Organization2 del compareTo de  = " + org.getOrganizationId()); */
        System.out.println("Organization1 numAttenders  = " + this.numAttenders());
        System.out.println("Organization2 numAttenders  = " + org.numAttenders());
        System.out.println("Resultado del compareTo de Organization = " + result);

        return result;
    }
}
