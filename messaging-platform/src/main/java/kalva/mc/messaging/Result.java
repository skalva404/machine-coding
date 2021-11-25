package kalva.mc.messaging;

public record Result(String data) {

    public static final Result EMPTY = new Result("");
}
