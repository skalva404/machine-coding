package kalva.mc.chess;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public class Player {

    private float score = 0f;

    private Map<Type, List<Piece>> pieces;

    public Player(Map<Type, List<Piece>> pieces) {
        this.pieces = pieces;
    }

    public void addScore(float newScore) {
        score += newScore;
    }
}
