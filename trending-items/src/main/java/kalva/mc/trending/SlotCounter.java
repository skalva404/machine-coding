package kalva.mc.trending;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SlotCounter<T> implements Serializable {

    private final int numSlots;
    private final Map<T, long[]> objToCounts = new HashMap<>();

    public SlotCounter(int numSlots) {
        if (numSlots <= 0) {
            throw new IllegalArgumentException("Number of slots must be greater than zero (you requested " + numSlots + ")");
        }
        this.numSlots = numSlots;
    }

    public void incrementCount(T obj, int slot) {
        long[] counts = objToCounts.computeIfAbsent(obj, k -> new long[this.numSlots]);
        counts[slot]++;
    }

    public long getCount(T obj, int slot) {
        long[] counts = objToCounts.get(obj);
        if (counts == null) {
            return 0;
        } else {
            return counts[slot];
        }
    }

    public Map<T, Long> getCounts() {
        Map<T, Long> result = new HashMap<T, Long>();
        for (T obj : objToCounts.keySet()) {
            result.put(obj, computeTotalCount(obj));
        }
        return result;
    }

    private long computeTotalCount(T obj) {
        long[] curr = objToCounts.get(obj);
        long total = 0;
        for (long l : curr) {
            total += l;
        }
        return total;
    }

    public void wipeSlot(int slot) {
        for (T obj : objToCounts.keySet()) {
            resetSlotCountToZero(obj, slot);
        }
    }

    private void resetSlotCountToZero(T obj, int slot) {
        long[] counts = objToCounts.get(obj);
        counts[slot] = 0;
    }

    private boolean shouldBeRemovedFromCounter(T obj) {
        return computeTotalCount(obj) == 0;
    }

    public void wipeZeros() {
        Set<T> objToBeRemoved = new HashSet<T>();
        for (T obj : objToCounts.keySet()) {
            if (shouldBeRemovedFromCounter(obj)) {
                objToBeRemoved.add(obj);
            }
        }
        for (T obj : objToBeRemoved) {
            objToCounts.remove(obj);
        }
    }
}
