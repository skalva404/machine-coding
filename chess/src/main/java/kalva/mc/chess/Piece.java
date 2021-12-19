package kalva.mc.chess;

import java.util.List;

public interface Piece {

    boolean isKilled();

    void setKilled(boolean isKilled);

    Color getColor();

    List<MoveRule> getMoveRules();

    List<ScoreRule> getScoringRules();

    Type getType();

}
