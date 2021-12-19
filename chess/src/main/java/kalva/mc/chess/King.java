package kalva.mc.chess;

import lombok.Getter;

import java.util.List;

@Getter
public class King extends ChessPiece {

    public King(Type type, Color color,
                List<MoveRule> moveRules, List<ScoreRule> scoringRules) {
        super(type, color, moveRules, scoringRules);
    }
}
