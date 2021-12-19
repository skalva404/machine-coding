package kalva.mc.chess;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public abstract class MoveRule implements Rule<Location, Boolean> {

    private final ScoreRule scoreRule;

    protected MoveRule(ScoreRule scoreRule) {
        this.scoreRule = scoreRule;
    }
}
