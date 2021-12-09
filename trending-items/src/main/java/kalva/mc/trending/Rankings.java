package kalva.mc.trending;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Rankings implements Serializable {

    private static final int DEFAULT_COUNT = 10;

    private final int maxSize;
    private final List<Rankable> rankedItems = Lists.newArrayList();
    private final Map<String, Rankable> rankedIndex = Maps.newConcurrentMap();

    public Rankings(int topN) {
        if (topN < 1) {
            throw new IllegalArgumentException("topN must be >= 1");
        }
        maxSize = topN;
    }

    public List<Rankable> getRankings() {
        return rankedItems;
    }

    public void updateWith(Rankable r) {
        synchronized (rankedItems) {
            addOrReplace(r);
            rerank();
            shrinkRankingsIfNeeded();
        }
    }

    private void addOrReplace(Rankable r) {
        Integer rank = rankOf(r);
        if (rank != null) {
            Rankable existing = rankedIndex.get(r.keyword());
            r.updateCount(existing.getCount());
            rankedItems.set(rank, r);
        } else {
            rankedItems.add(r);
        }
        rankedIndex.put(r.keyword(), r);
    }

    public Integer rankOf(Rankable r) {
        String tag = r.keyword();
        for (int rank = 0; rank < rankedItems.size(); rank++) {
            String cur = rankedItems.get(rank).keyword();
            if (cur.equals(tag)) {
                return rank;
            }
        }
        return null;
    }

    private void rerank() {
        Collections.sort(rankedItems);
        Collections.reverse(rankedItems);
    }

    private void shrinkRankingsIfNeeded() {
        if (rankedItems.size() > maxSize) {
            rankedItems.remove(maxSize);
        }
    }

    public void pruneZeroCounts() {
        int i = 0;
        while (i < rankedItems.size()) {
            if (rankedItems.get(i).getCount() == 0) {
                rankedItems.remove(i);
            } else {
                i++;
            }
        }
    }

    public String toString() {
        return rankedItems.toString();
    }
}
