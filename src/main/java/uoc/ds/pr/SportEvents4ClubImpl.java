package uoc.ds.pr;

import java.time.LocalDate;
import java.util.Date;

import edu.uoc.ds.adt.nonlinear.Dictionary;
import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.adt.sequential.QueueArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DictionaryOrderedVector;
import uoc.ds.pr.util.OrderedVector;

public class SportEvents4ClubImpl implements SportEvents4Club {
    private Player[] players;
    private int numPlayers;

    private OrganizingEntity[] organizingEntities;
    private int numOrganizingEntities;

    private Queue<File> files;
    private Dictionary<String, SportEvent> sportEvents;

    private int totalFiles;
    private int rejectedFiles;

    private Player mostActivePlayer;
    private OrderedVector<SportEvent> bestSportEvent;

    public SportEvents4ClubImpl() {
        players = new Player[MAX_NUM_PLAYER];
        numPlayers = 0;
        organizingEntities = new OrganizingEntity[MAX_NUM_ORGANIZING_ENTITIES];
        numOrganizingEntities = 0;
        files = new QueueArrayImpl<>();
        sportEvents = new DictionaryOrderedVector<String, SportEvent>(MAX_NUM_SPORT_EVENTS, SportEvent.CMP_K);
        totalFiles = 0;
        rejectedFiles = 0;
        mostActivePlayer = null;
        bestSportEvent = new OrderedVector<SportEvent>(MAX_NUM_SPORT_EVENTS, SportEvent.CMP_V);
    }

    @Override
    public void addPlayer(String id, String name, String surname, LocalDate dateOfBirth) {
        Player u = getPlayer(id);
        if (u != null) {
            u.setName(name);
            u.setSurname(surname);
            u.setBirthday(dateOfBirth);
        } else {
            u = new Player(id, name, surname, dateOfBirth);
            addUser(u);
        }
    }

    public void addUser(Player player) {
        players[numPlayers++] = player;
    }

    @Override
    public void addOrganizingEntity(String id, String name, String description) {
        OrganizingEntity organizingEntity = getOrganizingEntity(id);
        if (organizingEntity != null) {
            organizingEntity.setName(name);
            organizingEntity.setDescription(description);
        } else {
            organizingEntity = new OrganizingEntity(id, name, description);
            organizingEntities[Integer.parseInt(id)]= organizingEntity;
            numOrganizingEntities++;
        }
    }

    public void addFile(String id, String eventId, String orgId, String description, Type type, byte resources, int max, LocalDate startDate, LocalDate endDate) throws OrganizingEntityNotFoundException {
        if (Integer.parseInt(orgId) >= organizingEntities.length) {
            throw new OrganizingEntityNotFoundException();
        }
        OrganizingEntity organization = getOrganizingEntity(orgId);
        if (organization == null) {
            throw new OrganizingEntityNotFoundException();
        }

        files.add(new File(id, eventId, description, type, startDate, endDate, resources, max, organization));
        totalFiles++;
    }

    @Override
    public File updateFile(Status status, LocalDate date, String description) throws NoFilesException {
        return null;
    }

    @Override
    public void signUpEvent(String playerId, String eventId) throws PlayerNotFoundException, SportEventNotFoundException, LimitExceededException {

    }

    @Override
    public double getRejectedFiles() {
        return 0;
    }

    @Override
    public Iterator<SportEvent> getSportEventsByOrganizingEntity(String organizationId) throws NoSportEventsException {
        return null;
    }

    @Override
    public Iterator<SportEvent> getAllEvents() throws NoSportEventsException {
        return null;
    }

    @Override
    public Iterator<SportEvent> getEventsByPlayer(String playerId) throws NoSportEventsException {
        return null;
    }

    @Override
    public void addRating(String playerId, String eventId, Rating rating, String message) throws SportEventNotFoundException, PlayerNotFoundException, PlayerNotInSportEventException {

    }

    @Override
    public Iterator<uoc.ds.pr.model.Rating> getRatingsByEvent(String eventId) throws SportEventNotFoundException, NoRatingsException {
        return null;
    }

    @Override
    public Player mostActivePlayer() throws PlayerNotFoundException {
        return null;
    }

    @Override
    public SportEvent bestSportEvent() throws SportEventNotFoundException {
        return null;
    }

    @Override
    public void addRole(String roleId, String description) {

    }

    @Override
    public void addWorker(String dni, String name, String surname, LocalDate birthDay, String roleId) {

    }

    @Override
    public void assignWorker(String dni, String eventId) throws WorkerNotFoundException, WorkerAlreadyAssignedException, SportEventNotFoundException {

    }

    @Override
    public Iterator<Worker> getWorkersBySportEvent(String eventId) throws SportEventNotFoundException, NoWorkersException {
        return null;
    }

    @Override
    public Iterator<Worker> getWorkersByRole(String roleId) throws NoWorkersException {
        return null;
    }

    @Override
    public Level getLevel(String playerId) throws PlayerNotFoundException {
        return null;
    }

    @Override
    public Iterator<Enrollment> getSubstitutes(String eventId) throws SportEventNotFoundException, NoSubstitutesException {
        return null;
    }

    @Override
    public void addAttender(String phone, String name, String eventId) throws AttenderAlreadyExistsException, SportEventNotFoundException, LimitExceededException {

    }

    @Override
    public Attender getAttender(String phone, String sportEventId) throws SportEventNotFoundException, AttenderNotFoundException {
        return null;
    }

    @Override
    public Iterator<Attender> getAttenders(String eventId) throws SportEventNotFoundException, NoAttendersException {
        return null;
    }

    @Override
    public Iterator<OrganizingEntity> best5OrganizingEntities() throws NoAttendersException {
        return null;
    }

    @Override
    public SportEvent bestSportEventByAttenders() throws NoSportEventsException {
        return null;
    }

    @Override
    public void addFollower(String playerId, String playerFollowerId) throws PlayerNotFoundException {

    }

    @Override
    public Iterator<Player> getFollowers(String playerId) throws PlayerNotFoundException, NoFollowersException {
        return null;
    }

    @Override
    public Iterator<Player> getFollowings(String playerId) throws PlayerNotFoundException, NoFollowingException {
        return null;
    }

    @Override
    public Iterator<Player> recommendations(String playerId) throws PlayerNotFoundException, NoFollowersException {
        return null;
    }

    @Override
    public Iterator<Post> getPosts(String playerId) throws PlayerNotFoundException, NoPostsException {
        return null;
    }

    @Override
    public int numPlayers() {
        return 0;
    }

    @Override
    public int numOrganizingEntities() {
        return 0;
    }

    @Override
    public int numFiles() {
        return 0;
    }

    @Override
    public int numRejectedFiles() {
        return 0;
    }

    @Override
    public int numPendingFiles() {
        return 0;
    }

    @Override
    public int numSportEvents() {
        return 0;
    }

    @Override
    public int numSportEventsByPlayer(String playerId) {
        return 0;
    }

    @Override
    public int numPlayersBySportEvent(String sportEventId) {
        return 0;
    }

    @Override
    public int numSportEventsByOrganizingEntity(String orgId) {
        return 0;
    }

    @Override
    public int numSubstitutesBySportEvent(String sportEventId) {
        return 0;
    }

    @Override
    public Player getPlayer(String playerId) {
        for (Player u : players) {
            if (u == null) {
                return null;
            } else if (u.is(playerId)){
                return u;
            }
        }
        return null;
    }

    @Override
    public SportEvent getSportEvent(String eventId) {
        return null;
    }

    @Override
    public OrganizingEntity getOrganizingEntity(String id) {
        return organizingEntities[Integer.parseInt(id)];
    }
    public void addFile(String id, String eventId, int orgId, String description,
                        Type type, byte resources, int max, LocalDate startDate, LocalDate endDate) throws OrganizingEntityNotFoundException {
        if (orgId >= organizingEntities.length) {
            throw new OrganizingEntityNotFoundException();
        }
        OrganizingEntity organization = getOrganizingEntity(String.valueOf(orgId));
        if (organization == null) {
            throw new OrganizingEntityNotFoundException();
        }

        files.add(new File(id, eventId, description, type, startDate, endDate, resources, max, organization));
        totalFiles++;    }

    @Override
    public File currentFile() {
        return null;
    }

    @Override
    public int numRoles() {
        return 0;
    }

    @Override
    public Role getRole(String roleId) {
        return null;
    }

    @Override
    public int numWorkers() {
        return 0;
    }

    @Override
    public Worker getWorker(String dni) {
        return null;
    }

    @Override
    public int numWorkersByRole(String roleId) {
        return 0;
    }

    @Override
    public int numWorkersBySportEvent(String sportEventId) {
        return 0;
    }

    @Override
    public int numRatings(String playerId) {
        return 0;
    }

    @Override
    public int numAttenders(String sportEventId) {
        return 0;
    }

    @Override
    public int numFollowers(String playerId) {
        return 0;
    }

    @Override
    public int numFollowings(String playerId) {
        return 0;
    }
}
