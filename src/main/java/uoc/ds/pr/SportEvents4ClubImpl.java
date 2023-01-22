package uoc.ds.pr;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import edu.uoc.ds.adt.nonlinear.*;
import edu.uoc.ds.adt.sequential.QueueArrayImpl;
import edu.uoc.ds.adt.sequential.SetLinkedListImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.OrderedVector;
import edu.uoc.ds.adt.nonlinear.graphs.*;


public class SportEvents4ClubImpl implements SportEvents4Club {
    private Dictionary<String, Player> players;
    private int numPlayers;
    private HashTable<String, OrganizingEntity> organizingEntities;
    private int numOrganizingEntities;
    private PriorityQueue<File> files;
    private Dictionary<String, SportEvent> sportEvents;
    private int totalFiles;
    private int rejectedFiles;
    private Player mostActivePlayer;
    private SportEvent BestSportEvent;
    private OrderedVector<SportEvent> bestSportEvent;
    private Role[] roles;
    private int numRoles;
    private HashTable<String, Worker> workers;
    private int numWorkers;
    private OrderedVector<OrganizingEntity> best5OrganizingEntity;
    private OrderedVector<SportEvent> best10SportEvent;
    private DirectedGraph<Player, String> graph;
    Integer numEdges = 0;

    public SportEvents4ClubImpl() {
        players = new DictionaryAVLImpl<String, Player>();
        numPlayers = 0;
        organizingEntities = new HashTable<String, OrganizingEntity>(MAX_NUM_ORGANIZING_ENTITIES);
        numOrganizingEntities = 0;
        files = new PriorityQueue<File>(File.CMP);
        sportEvents = new DictionaryAVLImpl<String, SportEvent>();
        totalFiles = 0;
        rejectedFiles = 0;
        mostActivePlayer = null;
        bestSportEvent = new OrderedVector<SportEvent>(MAX_NUM_SPORT_EVENTS, SportEvent.CMP_V);
        roles = new Role[MAX_ROLES];
        numRoles = 0;
        workers = new HashTable<String, Worker>();
        numWorkers = 0;
        best5OrganizingEntity = new OrderedVector<OrganizingEntity>(MAX_ORGANIZING_ENTITIES_WITH_MORE_ATTENDERS, OrganizingEntity.COMP_ATTENDER);
        best10SportEvent = new OrderedVector<SportEvent>(MAX_NUM_SPORT_EVENTS, SportEvent.CMP_V);
        graph = new DirectedGraphImpl<>();

    }

    @Override
    public void addPlayer(String id, String name, String surname, LocalDate dateOfBirth) {
        Player player = players.get(id);

        if (player == null) {
            player = new Player(id, name, surname, dateOfBirth);
            players.put(player.getId(), player);
            numPlayers++;
        } else {
            player.setName(name);
            player.setSurname(surname);
            player.setBirthday(dateOfBirth);
        }
    }

    @Override
    public void addOrganizingEntity(String id, String name, String description) {
        OrganizingEntity organizingEntity = getOrganizingEntity(id);
        if (organizingEntity != null) {
            organizingEntity.setName(name);
            organizingEntity.setDescription(description);
        } else {
            organizingEntity = new OrganizingEntity(id, name, description);
            organizingEntities.put(id, organizingEntity);
            numOrganizingEntities++;
        }
    }

    public void addFile(String id, String eventId, String orgId, String description, Type type, byte resources, int max, LocalDate startDate, LocalDate endDate) throws OrganizingEntityNotFoundException {
        OrganizingEntity organizingEntity = getOrganizingEntity(orgId);
        if (organizingEntity == null) {
            throw new OrganizingEntityNotFoundException();
        }

        File file = new File(id, eventId, organizingEntity, description, type, resources, max, startDate, endDate);
        files.add(file);
        totalFiles++;
    }

    @Override
    public File updateFile(Status status, LocalDate date, String description) throws NoFilesException {
        File file = files.poll();
        if (file == null) {
            throw new NoFilesException();
        }

        file.update(status, date, description);
        if (file.isEnabled()) {
            SportEvent sportEvent = file.newSportEvent();
            sportEvents.put(sportEvent.getEventId(), sportEvent);
        }
        else {
            rejectedFiles++;
        }
        return file;
    }

    @Override
    public void signUpEvent(String playerId, String eventId) throws PlayerNotFoundException, SportEventNotFoundException, LimitExceededException {
        Player player = getPlayer(playerId);
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        player.addEvent(sportEvent);
        if (!sportEvent.isFull()) {
            sportEvent.addEnrollment(player);
        } else {
            sportEvent.addEnrollmentAsSubstitute(player);
            throw new LimitExceededException();
        }
        updateMostActivePlayer(player);
    }

    private void updateMostActivePlayer(Player player) {
        if (mostActivePlayer == null) {
            mostActivePlayer = player;
        }
        else if (player.numSportEvents() > mostActivePlayer.numSportEvents()) {
            mostActivePlayer = player;
        }
    }

    @Override
    public double getRejectedFiles() {
        return (double) rejectedFiles / totalFiles;
    }

    @Override
    public Iterator<SportEvent> getSportEventsByOrganizingEntity(String organizationId) throws NoSportEventsException {
        OrganizingEntity organizingEntity = getOrganizingEntity(organizationId);

        if (organizingEntity==null || !organizingEntity.hasActivities()) {
            throw new NoSportEventsException();
        }
        return organizingEntity.sportEvents();
    }

    @Override
    public Iterator<SportEvent> getAllEvents() throws NoSportEventsException {
        Iterator<SportEvent> it = sportEvents.values();
        if (!it.hasNext()) throw new NoSportEventsException();
        return it;
    }

    @Override
    public Iterator<SportEvent> getEventsByPlayer(String playerId) throws NoSportEventsException {
        Player player = getPlayer(playerId);
        if (player==null || !player.hasEvents()) {
            throw new NoSportEventsException();
        }
        Iterator<SportEvent> it = player.getEvents();
        return it;
    }

    @Override
    public void addRating(String playerId, String eventId, Rating rating, String message) throws SportEventNotFoundException, PlayerNotFoundException, PlayerNotInSportEventException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        Player player = getPlayer(playerId);
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        if (!player.isInSportEvent(eventId)) {
            throw new PlayerNotInSportEventException();
        }

        sportEvent.addRating(rating, message, player);
        updateBestSportEvent(sportEvent);

        player.addRating(rating);
    }

    private void updateBestSportEvent(SportEvent sportEvent) {
        bestSportEvent.update(sportEvent);
        //best10SportEvent.update(sportEvent);
    }


    @Override
    public Iterator<uoc.ds.pr.model.Rating> getRatingsByEvent(String eventId) throws SportEventNotFoundException, NoRatingsException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent  == null) {
            throw new SportEventNotFoundException();
        }

        if (!sportEvent.hasRatings()) {
            throw new NoRatingsException();
        }
        return sportEvent.ratings();
    }

    @Override
    public Player mostActivePlayer() throws PlayerNotFoundException {
        if (mostActivePlayer == null) {
            throw new PlayerNotFoundException();
        }
        return mostActivePlayer;
    }

    @Override
    public SportEvent bestSportEvent() throws SportEventNotFoundException {
        if (bestSportEvent.size() == 0) {
            throw new SportEventNotFoundException();
        }
        return bestSportEvent.elementAt(0);
    }

    @Override
    public void addRole(String roleId, String description) {
        Role role = getRole(roleId);
        if (role != null) {
            role.setDescription(description);
        } else {
            role = new Role(roleId, description);
            addRole(role);
        }
    }

    public void addRole(Role role) {
        roles[numRoles++] = role;
    }

    @Override
    public void addWorker(String dni, String name, String surname, LocalDate birthDay, String roleId) {
        //Worker worker = null;
        Role role = getRole(roleId);
        Worker worker = getWorker(dni);

        if (worker == null) {
            worker = new Worker(dni, name, surname, birthDay, role);
            workers.put(worker.getDni(), worker);
            worker.setRole(role);
            numWorkers++;
        } else if (worker != null) {
            worker.setName(name);
            worker.setSurname(surname);
            worker.setBirthday(birthDay);
            workers.put(worker.getDni(), worker);
            if ((worker.getDni() == dni) && (worker.getRoleId() != roleId)) {
                worker.getRole().removeWorker(worker);
                worker.setRole(role);
            }
        }
    }

    @Override
    public void assignWorker(String dni, String eventId) throws WorkerNotFoundException, WorkerAlreadyAssignedException, SportEventNotFoundException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        Worker worker = getWorker(dni);
        if (worker == null) {
            throw new WorkerNotFoundException();
        }
        if (sportEvent.isInSportEvent(dni)) {
            throw new WorkerAlreadyAssignedException();
        }

        worker.setSportEvent(sportEvent);
        sportEvent.addWorker(worker);
    }

    @Override
    public Iterator<Worker> getWorkersBySportEvent(String eventId) throws SportEventNotFoundException, NoWorkersException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }
        if (!sportEvent.hasWorkers()) {
            throw new NoWorkersException();
        }

        Iterator<Worker> it = sportEvent.workers();
        return it;
    }

    @Override
    public Iterator<Worker> getWorkersByRole(String roleId) throws NoWorkersException {
        Role role = getRole(roleId);

        if (!role.hasMembers()) {
            throw new NoWorkersException();
        }
        return role.workers();
    }

    @Override
    public Level getLevel(String playerId) throws PlayerNotFoundException {
        Player player = players.get(playerId);
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        Level level = player.getLevel();
        return level;
    }

    @Override
    public Iterator<Enrollment> getSubstitutes(String eventId) throws SportEventNotFoundException, NoSubstitutesException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        if (sportEvent.getNumSubstitutes() == 0) {
            throw new NoSubstitutesException();
        }
        return sportEvent.EnrollmentAsSubstitute();
    }

    @Override
    public void addAttender(String phone, String name, String eventId) throws AttenderAlreadyExistsException, SportEventNotFoundException, LimitExceededException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        if (sportEvent.existsAttender(phone)) {
            throw new AttenderAlreadyExistsException();
        }

        if (sportEvent.isFull()) {
            throw new LimitExceededException();
        }
        sportEvent.addAttender(phone, name);
        updateBestSportEvent(sportEvent);

        OrganizingEntity organizingEntity = sportEvent.getOrganizingEntity();
        organizingEntity.incAttenders();
        best5OrganizingEntity.update(organizingEntity);
    }

    @Override
    public Attender getAttender(String phone, String sportEventId) throws SportEventNotFoundException, AttenderNotFoundException {
        SportEvent sportEvent = getSportEvent(sportEventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        Attender attender = sportEvent.getAttender(phone);
        if (attender == null) {
            throw new AttenderNotFoundException();
        }
        return attender;
    }

    @Override
    public Iterator<Attender> getAttenders(String eventId) throws SportEventNotFoundException, NoAttendersException {
        SportEvent sportEvent = getSportEvent(eventId);
        if (sportEvent == null) {
            throw new SportEventNotFoundException();
        }

        if (!sportEvent.hasAttenders()) {
            throw new NoAttendersException();
        }
        return sportEvent.attenders();
    }

    @Override
    public Iterator<OrganizingEntity> best5OrganizingEntities() throws NoAttendersException {
        if (best5OrganizingEntity.isEmpty()) {
            throw new NoAttendersException();
        }
        return best5OrganizingEntity.values();
    }

    @Override
    public SportEvent bestSportEventByAttenders() throws NoSportEventsException {
        if (bestSportEvent.size() == 0) {
            throw new NoSportEventsException();
        }
        return bestSportEvent.elementAt(0);
    }

    @Override
    public void addFollower(String playerId, String playerFollowerId) throws PlayerNotFoundException {
        Player player = getPlayer(playerId);
        Player follower = getPlayer(playerFollowerId);
        if ((player == null) || (follower == null)) {
            throw new PlayerNotFoundException();
        } else if ((graph.getVertex(player) == null) && (graph.getVertex(follower) == null)) {
            Vertex<Player> vPlayer = graph.newVertex(player);
            Vertex<Player> vFollower = graph.newVertex(follower);
            DirectedEdge<String, Player> edge = graph.newEdge(vPlayer, vFollower);
            numEdges++;
            edge.setLabel("follower");
        } else if ((graph.getVertex(player) != null)) {
            Vertex<Player> vPlayer = graph.getVertex(player);
            Vertex<Player> vFollowed = null;
            if (graph.getVertex(follower) == null) {
                vFollowed = graph.newVertex(follower);
            } else if (graph.getVertex(follower) != null) {
                vFollowed = graph.getVertex(follower);
            }
            DirectedEdge<String, Player> edge = graph.newEdge(vPlayer, vFollowed);
            numEdges++;
            edge.setLabel("follower");
        }
    }

    @Override
    public int numFollowers(String playerId) {
        int numFollowers = 0;
        Vertex<Player> vFollower = graph.getVertex(getPlayer(playerId));
        Iterator<Edge<String, Player>> it = graph.edgesWithSource(vFollower);

        while (it.hasNext()) {
           it.next();
           numFollowers++;
        }
       return numFollowers;
    }

    @Override
    public int numFollowings(String playerId) {
        int numFollowings = 0;
        Vertex<Player> vFollowed = graph.getVertex(getPlayer(playerId));
        Iterator<Edge<String, Player>> it = graph.edgedWithDestA(vFollowed);

        while(it.hasNext()) {
            it.next();
            numFollowings++;
        }
        return numFollowings;
    }

    @Override
    public Iterator<Player> getFollowers(String playerId) throws PlayerNotFoundException, NoFollowersException {
        //SetLinkedListImpl<Player> followers = new SetLinkedListImpl<Player>();
        QueueArrayImpl<Player> followers = new QueueArrayImpl<Player>();
        Player follower = getPlayer(playerId);
        if (follower == null) {
            throw new PlayerNotFoundException();
        }

        DirectedVertexImpl vFollower = (DirectedVertexImpl) graph.getVertex(follower);
        Iterator<Edge> itEdges = vFollower.edges();
        DirectedEdge<String, Player> edge = (DirectedEdge<String, Player>)itEdges.next();
        if (!itEdges.hasNext()) {
            throw new NoFollowersException();
        }

        while (itEdges.hasNext()) {
            if (edge.getVertexSrc().getValue().getId().equals(playerId)) {
                followers.add(edge.getVertexDst().getValue());
            }
            edge = (DirectedEdge<String, Player>)itEdges.next();
        }

        return followers.values();
    }

    @Override
    public Iterator<Player> getFollowings(String playerId) throws PlayerNotFoundException, NoFollowingException {
        QueueArrayImpl<Player> followeds = new QueueArrayImpl<Player>();
        Player followed = getPlayer(playerId);
        if (followed == null) {
            throw new PlayerNotFoundException();
        }

        DirectedVertexImpl vFollowed = (DirectedVertexImpl) graph.getVertex(followed);
        if (vFollowed == null) {
            throw new NoFollowingException();
        }

        Iterator<Edge> itEdges = vFollowed.edges();

        while (itEdges.hasNext()) {
            DirectedEdge<String, Player> edge = (DirectedEdge<String, Player>)itEdges.next();
            if (edge.getVertexDst().getValue().getId().equals(playerId)) {
                followeds.add(edge.getVertexSrc().getValue());
            }
        }

        return followeds.values();
    }

    @Override
    public Iterator<Player> recommendations(String playerId) throws PlayerNotFoundException, NoFollowersException {
        SetLinkedListImpl<Player> followeds = new SetLinkedListImpl<Player>();
        Player follower = getPlayer(playerId);
        if (follower == null) {
            throw new PlayerNotFoundException();
        }

        DirectedVertexImpl vFollowed = (DirectedVertexImpl) graph.getVertex(follower);
        if (vFollowed == null) {
            throw new NoFollowersException();
        }

        Iterator<Edge> itEdges = vFollowed.edges();
        while (itEdges.hasNext()) {
            DirectedEdge<String, Player> edge = (DirectedEdge<String, Player>) itEdges.next();

            if (edge.getVertexSrc().getValue().getId().equals(playerId)) {
            DirectedVertexImpl vFollowed1 = (DirectedVertexImpl) graph.getVertex(edge.getVertexDst().getValue());
            Iterator<Edge> itEdges1 = vFollowed1.edges();
                while (itEdges1.hasNext()&&vFollowed1 != null) {
                    DirectedEdge<String, Player> edge1 = (DirectedEdge<String, Player>) itEdges1.next();
                    if (edge.getVertexDst().getValue().equals(edge1.getVertexSrc().getValue())&&(!edge.getVertexSrc().getValue().equals(edge1.getVertexDst().getValue()))) {
                        if (followeds.contains(edge.getVertexDst().getValue()) && (!followeds.contains(edge1.getVertexDst().getValue()))) {
                            followeds.add(edge1.getVertexDst().getValue());
                            followeds.delete(edge.getVertexDst().getValue());
                        } else {
                            followeds.add(edge1.getVertexDst().getValue());
                        }
                    }
                }
            }
            if (followeds.contains(edge.getVertexDst().getValue())) {
                followeds.delete(edge.getVertexDst().getValue());
            } else if (followeds.contains(edge.getVertexSrc().getValue())) {
                followeds.delete(edge.getVertexSrc().getValue());
            }
        }
        return followeds.values();
    }

    @Override
    public Iterator<Post> getPosts(String playerId) throws PlayerNotFoundException, NoPostsException {
        QueueArrayImpl<Player> followeds = new QueueArrayImpl<Player>();
        Player follower = getPlayer(playerId);
        if (follower == null) {
            throw new PlayerNotFoundException();
        }

        DirectedVertexImpl vFollowed = (DirectedVertexImpl) graph.getVertex(follower);
        if (vFollowed == null) {
            throw new NoPostsException();
        }

        Iterator<Edge> itEdges = vFollowed.edges();

        while (itEdges.hasNext()) {
            DirectedEdge<String, Player> edge = (DirectedEdge<String, Player>)itEdges.next();
            //if (edge.getVertexDst().getValue().getId().equals(playerId)) {
                //edge.getVertexDst(Iterator<SportEvent> itevents = follower.getEvents();
            Iterator<SportEvent> itevents = edge.getVertexDst().getValue().getEvents();
            //System.out.println("Numero de enventos "+ edge.getVertexDst().getValue().getEvents());
            Integer numEvents= 0;
            while (itevents.hasNext()) {
                System.out.println("Eventos del playerId= " + itevents.next().getEventId());
                System.out.println("playerId= " + edge.getVertexDst().getValue().getId());


                itevents.next();
                System.out.println("Numero de eventos "+ numEvents);
                numEvents++;
                //followeds.add(edge.getVertexSrc().getValue());
            }

        }



        //follower.isInSportEvent();
        return null;
    }

    @Override
    public int numPlayers() {
        return numPlayers;
    }

    @Override
    public int numOrganizingEntities() {
        return numOrganizingEntities;
    }

    @Override
    public int numFiles() {
        return totalFiles;
    }

    @Override
    public int numRejectedFiles() {
        return rejectedFiles;
    }

    @Override
    public int numPendingFiles() {
        return files.size();
    }

    @Override
    public int numSportEvents() {
        return sportEvents.size();
    }

    @Override
    public int numSportEventsByPlayer(String playerId) {
        Player player = getPlayer(playerId);
        return (player!=null?player.numEvents():0);
    }

    @Override
    public int numPlayersBySportEvent(String sportEventId) {
        SportEvent sportEvent = getSportEvent(sportEventId);
        return (sportEvent!=null?sportEvent.numPlayers(): 0);
    }

    @Override
    public int numSportEventsByOrganizingEntity(String orgId) {
        OrganizingEntity organization = getOrganizingEntity(orgId);
        return organization.numEvents();
    }

    @Override
    public int numSubstitutesBySportEvent(String sportEventId) {
        SportEvent sportEvent = getSportEvent(sportEventId);
        return (sportEvent!=null?sportEvent.getNumSubstitutes():0);
    }

    public SportEvent getSportEvent(String eventId) {
        return sportEvents.get(eventId);
    }

    @Override
    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    @Override
    public OrganizingEntity getOrganizingEntity(String id) {
        return organizingEntities.get(id);
    }

    @Override
    public File currentFile() {
        return (files.size() > 0 ? files.peek() : null);
    }

    @Override
    public int numRoles() {
        return numRoles;
    }

    @Override
    public Role getRole(String roleId) {
        for (Role r : roles) {
            if (r == null) {
                return null;
            } else if (r.is(roleId)){
                return r;
            }
        }
        return null;
    }

    @Override
    public int numWorkers() {
        return numWorkers;
    }

    @Override
    public Worker getWorker(String dni) {
        return workers.get(dni);
    }

    @Override
    public int numWorkersByRole(String roleId) {
        Role role = getRole(roleId);
        return (role!=null?role.numWorkers(): 0);
    }

    @Override
    public int numWorkersBySportEvent(String sportEventId) {
        SportEvent sportEvent = getSportEvent(sportEventId);
        return (sportEvent!=null?sportEvent.numWorkers(): 0);
    }

    @Override
    public int numRatings(String playerId) {
        return getPlayer(playerId).getNumRatings();
    }

    @Override
    public int numAttenders(String sportEventId) {
        if (getSportEvent(sportEventId) == null) {
            return 0;
        } else {
            Integer numAttenders = getSportEvent(sportEventId).numAttenders();
            return numAttenders;
        }
    }
}
