package kalva.mc.chess;

public class KingMoveRule extends MoveRule {

    private final ScoreRule scoreRule;

    public KingMoveRule(ScoreRule scoreRule) {
        super(scoreRule);
        this.scoreRule = new KingKillingRule();
    }

    @Override
    public Boolean apply(Location source, Location dest) {
        int x = Math.abs(source.x() - dest.x());
        int y = Math.abs(source.y() - dest.y());
        return x + y == 1;
    }
}
