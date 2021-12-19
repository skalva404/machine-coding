package kalva.mc.chess;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class ChessPiece implements Piece {

    private Type type;
    private Color color;
    private boolean killed = false;
    private List<MoveRule> moveRules;
    private List<ScoreRule> scoringRules;


    public ChessPiece(Type type, Color color,
                      List<MoveRule> moveRules, List<ScoreRule> scoringRules) {

        this.type = type;
        this.color = color;
        this.moveRules = moveRules;
        this.scoringRules = scoringRules;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }
}
