package kalva.mc.trending;

import java.io.Serializable;

public class RankableKeyword implements Rankable, Serializable {

    private final String keyword;
    private Long count;

    public RankableKeyword(String keyword, long count) {
        if (keyword == null) {
            throw new IllegalArgumentException("The object must not be null");
        }
        if (count < 0) {
            throw new IllegalArgumentException("The count must be >= 0");
        }
        this.keyword = keyword;
        this.count = count;
    }

    public static RankableKeyword from(Tuple tuple) {
        return new RankableKeyword(tuple.key(), tuple.count());
    }

    public String keyword() {
        return keyword;
    }

    public Long getCount() {
        return count;
    }

    public void updateCount(Long value) {
        count += value;
    }

    @Override
    public int compareTo(Rankable other) {
        long delta = this.getCount() - other.getCount();
        if (delta > 0) {
            return 1;
        } else if (delta < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RankableKeyword other)) {
            return false;
        }
        return keyword.equals(other.keyword) && count == other.count;
    }

    @Override
    public int hashCode() {
        int result = 17;
        int countHash = (int) (count ^ (count >>> 32));
        result = 31 * result + countHash;
        result = 31 * result + keyword.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        buf.append(keyword);
        buf.append(" || ");
        buf.append(count);
        buf.append("]");
        return buf.toString();
    }
}
