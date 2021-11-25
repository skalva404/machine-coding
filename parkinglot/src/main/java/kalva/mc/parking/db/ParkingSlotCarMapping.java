package kalva.mc.parking.db;

import kalva.mc.parking.ErrorConstants;
import kalva.mc.parking.ParkingError;
import kalva.mc.parking.SlotStatus;
import kalva.mc.parking.model.Car;
import kalva.mc.parking.model.ParkingSlot;

import java.util.*;
import java.util.stream.LongStream;

/**
 * Simulation of ParkingSlot-Car mapping table
 * This class is not a thread safe, caller should obtain lock for any concurrency issues.
 * Assumptions:
 * 1. There will be more reads for analytics and monitoring.
 * 2. Extra latency due to indexes are acceptable.
 * </pre>
 * 1. Create method prepares inmemory table and indexes for optimizing reads.
 */
public class ParkingSlotCarMapping implements Table {

    private Map<SlotStatus, List<ParkingSlot>> table = null;
    private Map<String, ParkingSlot> regNumToSlotIndex = null;
    private Map<String, List<ParkingSlot>> colorToCarIndex = null;

    private long size;

    public ParkingSlotCarMapping(Long size) {
        this.size = size;
    }

    @Override
    public String name() {
        return "ParkingSlotCarMapping";
    }

    @Override
    public ParkingSlotCarMapping createTable() {
        //Simulating creation of table and indexes
        table = new HashMap<>();
        colorToCarIndex = new HashMap<>();
        regNumToSlotIndex = new HashMap<>();
        LongStream.rangeClosed(1, size).forEach(id -> {
            List<ParkingSlot> slotList = table.computeIfAbsent(SlotStatus.FREE, ss -> new ArrayList<>());
            slotList.add(new ParkingSlot(id));
        });
        return this;
    }

    public ParkingSlot park(Car car) throws ParkingError {

        ParkingSlot slot = freeSlot();
        if (null == slot) {
            throw new ParkingError(ErrorConstants.SLOTS_FULL.message());
        }

        //Insert to mapping table
        slot.setCar(car);
        freeSlots().remove(slot);
        occupiedSlots().add(slot);

        //update read optimized indexes
        regNumToSlotIndex.put(car.regNumber(), slot);
        colorToCarIndex.computeIfAbsent(car.color(), s -> new ArrayList<>()).add(slot);
        return slot;
    }

    public ParkingSlot leave(Long slotId) throws ParkingError {
        Optional<ParkingSlot> first = occupiedSlots().stream()
                .filter(ps -> slotId.equals(ps.id()))
                .findFirst();
        if (first.isEmpty()) {
            throw new ParkingError(ErrorConstants.INVALID_ARGS.message() + slotId);
        }

        //Delete to mapping table
        ParkingSlot slot = first.get();
        occupiedSlots().remove(slot);
        freeSlots().add(slot);

        //update read optimized indexes
        regNumToSlotIndex.remove(slot.car().regNumber());
        colorToCarIndex.computeIfAbsent(slot.car().color(), s -> new ArrayList<>()).remove(slot);

        slot.unSetCar();
        return slot;
    }

    private ParkingSlot freeSlot() {
        if (0 == table.get(SlotStatus.FREE).size()) {
            return null;
        }
        table.get(SlotStatus.FREE).sort(Comparator.comparing(ParkingSlot::id));
        return table.get(SlotStatus.FREE).get(0);
    }

    public List<ParkingSlot> freeSlots() {
        return table.computeIfAbsent(SlotStatus.FREE, ss -> new ArrayList<>());
    }

    public List<ParkingSlot> occupiedSlots() {
        return table.computeIfAbsent(SlotStatus.OCCUPIED, ss -> new ArrayList<>());
    }

    public List<ParkingSlot> totalSlots() {
        List<ParkingSlot> slots = new ArrayList<>();
        if (null != freeSlots()) {
            slots.addAll(freeSlots());
        }
        if (null != occupiedSlots()) {
            slots.addAll(occupiedSlots());
        }
        return slots;
    }

    public Map<String, List<ParkingSlot>> colorToCarIndex() {
        return colorToCarIndex;
    }

    public Map<String, ParkingSlot> regNumToSlotIndex() {
        return regNumToSlotIndex;
    }

    @Override
    public void deleteTable() throws Exception {
        //Simulating deletion of table and indexes
        regNumToSlotIndex.clear();
        colorToCarIndex.clear();
        table.clear();
    }

}
