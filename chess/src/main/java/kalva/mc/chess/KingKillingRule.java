package kalva.mc.chess;

public class KingKillingRule extends ScoreRule {

    @Override
    public Float apply(Piece killer, Piece killed) {
        switch (killer.getType()) {
            case PAWN -> {
                return 1.0f;
            }
            case KNIGHT -> {
                return 3.0f;
            }
        }
        return 0.0f;
    }
}
