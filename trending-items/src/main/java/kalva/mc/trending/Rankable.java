package kalva.mc.trending;

public interface Rankable extends Comparable<Rankable> {

    String keyword();



    Long getCount();

    void updateCount(Long value);
}

