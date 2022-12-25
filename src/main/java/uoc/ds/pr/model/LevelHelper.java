package uoc.ds.pr.model;

import uoc.ds.pr.SportEvents4Club;
import uoc.ds.pr.model.Player;

public class LevelHelper {
    public static SportEvents4Club.Level getLevel(String playerId) {
        SportEvents4Club.Level level = null;
        Player player = null;
        player.setId(playerId);
//        player.setId(String.valueOf(playerId));

        if (player.hasNumRatings(15)) {
            level = SportEvents4Club.Level.LEGEND;
        } else if (player.hasNumRatings(10) && player.hasLessThanNumRatings(15)) {
            level = SportEvents4Club.Level.MASTER;
        } else if (player.hasNumRatings(5) && player.hasLessThanNumRatings(10)) {
            level = SportEvents4Club.Level.EXPERT;
        } else if (player.hasNumRatings(2) && player.hasLessThanNumRatings(5)) {
            level = SportEvents4Club.Level.PRO;
        } else if (player.hasLessThanNumRatings(2)) {
            level = SportEvents4Club.Level.ROOKIE;
        }
        return level;
    }
}
