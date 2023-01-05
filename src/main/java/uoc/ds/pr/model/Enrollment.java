package uoc.ds.pr.model;

import java.util.Comparator;

//public class Enrollment {
public class Enrollment implements Comparable<Enrollment> {

    public static final Comparator<Enrollment> CMP_RATING = (Enrollment enrollment1, Enrollment enrollment2)->enrollment1.compareTo(enrollment2);

    Player player;
    boolean isSubtitute;

    public Enrollment(Player player, boolean isSubstitute) {
        this.player = player;
        this.isSubtitute = isSubstitute;
    }

    public Player getPlayer() {
        return player;
    }

    public int compareTo(Enrollment enrollment2) {
        return Integer.compare(enrollment2.player.getNumRatings(), player.getNumRatings());
    }

}
