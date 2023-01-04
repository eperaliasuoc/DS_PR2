package uoc.ds.pr.model;

import java.time.LocalDate;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;

//public class Worker extends Player {
public class Worker {

    private String id;
    private String name;
    private String surname;
    private LocalDate birthday;
    Role role;
    SportEvent sportEvent;


    public Worker(String userId, String name, String surname, LocalDate birthday, Role roleId) {
        this.id = userId;
        this.name = name;
        this.surname = surname;
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

    public void setRole(Role role) {
        this.role = role;
        this.role.addWorker(this);
    }

    public Role getRole() {
        return role;
    }

    public void setSportEvent(SportEvent sportEvent) {
        this.sportEvent = sportEvent;
    }

    public SportEvent getSportEvent() {
        return sportEvent;
    }

    public boolean is(Worker worker) {
        return is(worker.getId());
    }

    public String getRoleId() {
        return role.getRoleId();
    }

    public boolean is(String userId) {
        return id.equals(userId);
    }

    public Iterator<Worker> workers() {
        return workers();
    }
}
