package kalva.mc.chess;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Game {

    private Player player1;
    private Player player2;

    private Board board;

    public Game(Player player1, Player player2, Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
    }

    public Game arrange() {
        board.initialize(player1.pieces(), player2.pieces());
        return this;
    }

    public Game move(Player player, Piece piece, Location source, Location dest) {
        player.addScore(board.move(piece, source, dest));
        return this;
    }

    public static void main(String[] args) {

        Board board = new Board(new ChessPiece[][]{});
        Player player1 = new Player(Maps.newHashMap());
        Player player2 = new Player(Maps.newHashMap());

        Game game = new Game(player1, player2, board)
                .arrange()
                .move(player1, player1.pieces().get(Type.KNIGHT).get(0), new Location(0, 0), new Location(1, 2))
                .move(player1, player1.pieces().get(Type.KNIGHT).get(0), new Location(0, 0), new Location(1, 2));
    }
}
