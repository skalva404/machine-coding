package kalva.mc.trending;

import java.io.Serializable;
import java.util.Map;

public class SlidingWindow<T> implements Serializable {

    private static final long serialVersionUID = -2645063988768785810L;

    private SlotCounter<T> objCounter;
    private int headSlot;
    private int tailSlot;
    private int windowLengthInSlots;

    public SlidingWindow(int windowLengthInSlots) {
        if (windowLengthInSlots < 2) {
            throw new IllegalArgumentException(
                    "Window length in slots must be at least two (you requested " + windowLengthInSlots + ")");
        }
        this.windowLengthInSlots = windowLengthInSlots;
        this.objCounter = new SlotCounter<T>(this.windowLengthInSlots);

        this.headSlot = 0;
        this.tailSlot = slotAfter(headSlot);
    }

    public void incrementCount(T obj) {
        objCounter.incrementCount(obj, headSlot);
    }

    public Map<T, Long> getCountsThenAdvanceWindow() {
        Map<T, Long> counts = objCounter.getCounts();
        objCounter.wipeZeros();
        objCounter.wipeSlot(tailSlot);
        advanceHead();
        return counts;
    }

    private void advanceHead() {
        headSlot = tailSlot;
        tailSlot = slotAfter(tailSlot);
    }

    private int slotAfter(int slot) {
        return (slot + 1) % windowLengthInSlots;
    }
}