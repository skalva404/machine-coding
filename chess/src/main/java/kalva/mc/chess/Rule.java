package kalva.mc.chess;

@FunctionalInterface
public interface Rule<I, O> {

    O apply(I source, I dest);
}
