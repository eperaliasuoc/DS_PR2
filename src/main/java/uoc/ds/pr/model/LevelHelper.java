package uoc.ds.pr.model;

import uoc.ds.pr.SportEvents4Club;
import uoc.ds.pr.model.Player;

public class LevelHelper {
    public static SportEvents4Club.Level getLevel(Integer rating) {
        SportEvents4Club.Level level = null;
        Player player = null;
       // player.setId(playerId);
//        player.setId(String.valueOf(playerId));

        if (rating >= 15) {
            level = SportEvents4Club.Level.LEGEND;
        } else if ((rating >= 10) && (rating < 15)) {
            level = SportEvents4Club.Level.MASTER;
        } else if ((rating >= 5) && (rating < 10)) {
            level = SportEvents4Club.Level.EXPERT;
        } else if ((rating >= 2) && (rating < 5)) {
            level = SportEvents4Club.Level.PRO;
        } else if (rating < 2) {
            level = SportEvents4Club.Level.ROOKIE;
        }
        return level;
    }
}
