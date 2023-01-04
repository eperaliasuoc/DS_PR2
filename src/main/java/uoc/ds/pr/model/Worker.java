package uoc.ds.pr.model;

import java.time.LocalDate;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.Traversal;

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

    public void setId(String dni) {
        this.id = id;
    }
    public String getDni() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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
        return is(worker.getDni());
    }

    public boolean is(String userId) {
        return id.equals(userId);
    }

    public String getRoleId() {
        return role.getRoleId();
    }

    public Iterator<Worker> workers() {
        return workers();
    }

    public static void removeWorker(List<Worker> workers, Worker worker) {
        Traversal<Worker> r = workers.positions();
        boolean found = false;
        Position<Worker> p = null;

        while (!found && r.hasNext()) {
            p = r.next();
            found = p.getElem().is(worker);
        }

        if (found) workers.delete(p);
    }

    /*public boolean isInSportEvent(String eventId) {
        Iterator<Worker> it = sportEvent.workers();
        boolean found = false;
        Worker w = null;
        while (it.hasNext() && !found) {
            w = it.next();
            found = w.is(eventId);
        }
        return found;
    }*/
}
