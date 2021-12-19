package kalva.mc.chess;

import java.util.List;
import java.util.Map;

public class Board {

    private Piece[][] board;

    public Board(Piece[][] board) {
        this.board = board;
    }

    public float move(Piece piece, Location source, Location destination) {

        float finalScore = 0f;
        for (MoveRule rule : piece.getMoveRules()) {
            if (!rule.apply(source, destination)) {
                return finalScore;
            }
        }

        Piece destPiece = board[destination.x()][destination.y()];
        if (null != destPiece) {
            if (piece.getColor().equals(destPiece.getColor())) {
                return finalScore;
            }
            destPiece.setKilled(true);
            List<ScoreRule> scoringRules = piece.getScoringRules();
            for (ScoreRule scoreRule : scoringRules) {
                finalScore += scoreRule.apply(piece, destPiece);
            }
        }

        board[destination.x()][destination.y()] = piece;
        return finalScore;
    }

    public void initialize(Map<Type, List<Piece>> player1, Map<Type, List<Piece>> player2) {

        List<Piece> pieces = player1.get(Type.PAWN);
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            board[1][i] = piece;
        }
        pieces = player2.get(Type.PAWN);
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            board[board.length - 2][i] = piece;
        }
        //re-arrange other pieces

    }
}
