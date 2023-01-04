package uoc.ds.pr.model;

import java.time.LocalDate;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;

public class Role {
    private String roleId;
    private String name;
    private List<Worker> workers;

    public Role(String roleId, String name) {
        this.roleId = roleId;
        this.name = name;
        workers = new LinkedList<Worker>();
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    public String getRoleId() {
        return roleId;
    }

    public void setDescription(String name) {
        this.name = name;
    }

    public String getDescription() {
        return name;
    }

    public boolean is(String roleId) {
        return this.roleId.equals(roleId);
    }

    public void addWorker(Worker worker) {
        workers.insertEnd(worker);
    }

    public int numWorkers() {
        return workers.size();
    }

    public boolean hasMembers() {
        return workers.size() > 0;
    }

    public Iterator<Worker> workers() {
        return workers.values();
    }
}
