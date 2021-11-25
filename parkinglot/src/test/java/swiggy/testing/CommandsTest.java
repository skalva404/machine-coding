package swiggy.testing;

import kalva.mc.parking.ParkingError;
import kalva.mc.parking.commands.*;
import kalva.mc.parking.model.ParkingSlot;
import kalva.mc.parking.service.ParkingLotService;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static kalva.mc.parking.CommandType.create_parking_lot;
import static kalva.mc.parking.Utils.newInstance;

public class CommandsTest {

    private ParkingLotService parkingParkingLotService;

    @BeforeMethod
    public void setUp() throws Exception {
        CreateCommand command = new CreateCommand(Collections.singletonList("5"));
        command.execute();
        parkingParkingLotService = command.result();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        parkingParkingLotService = null;
    }

    @Test
    public void testCreate() throws ParkingError {

        CreateCommand command;
        Exception exception = null;
        try {
            newInstance(create_parking_lot.getImplementation(), null);
        } catch (Exception iae) {
            exception = iae;
        }
        Assert.assertNotNull(exception);
        exception = null;

        try {
            command = (CreateCommand) newInstance(create_parking_lot.getImplementation(),
                    Collections.singletonList("2"));
            command.execute();
            System.out.println(command.format());
            ParkingLotService parkingParkingLotService = command.result();
            Assert.assertNotNull(parkingParkingLotService);
            Assert.assertEquals(parkingParkingLotService.size(), 2l);
        } catch (Exception iae) {
            exception = iae;
        }
        Assert.assertNull(exception);
    }

    @Test
    public void testPark() throws ParkingError {

        for (int i = 1; i <= 5; i++) {
            ParkCommand parkCommand = new ParkCommand(Arrays.asList(String.valueOf(i), String.valueOf(i)));
            parkCommand.setSlotCarMapping(parkingParkingLotService.parkingSlotTable());
            parkCommand.execute();
            System.out.println(parkCommand.format());
        }
        Assert.assertEquals(parkingParkingLotService.parkingSlotTable().freeSlots().size(), 0);
        Assert.assertEquals(parkingParkingLotService.parkingSlotTable().occupiedSlots().size(), 5);
        Assert.assertEquals(parkingParkingLotService.parkingSlotTable().totalSlots().size(), 5);

        Exception exception = null;
        try {
            ParkCommand parkCommand = new ParkCommand(Arrays.asList(String.valueOf(6), String.valueOf(6)));
            parkCommand.setSlotCarMapping(parkingParkingLotService.parkingSlotTable());
            parkCommand.execute();
        } catch (Exception e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }

    @Test
    public void testLeave() throws ParkingError {

        for (int i = 1; i <= 5; i++) {
            ParkCommand parkCommand = new ParkCommand(Arrays.asList(String.valueOf(i), String.valueOf(i)));
            parkCommand.setSlotCarMapping(parkingParkingLotService.parkingSlotTable());
            parkCommand.execute();
            System.out.println(parkCommand.format());
        }

        for (int i = 1; i <= 5; i++) {
            LeaveCommand leaveCommand = new LeaveCommand(Collections.singletonList(String.valueOf(i)));
            leaveCommand.setSlotCarMapping(parkingParkingLotService.parkingSlotTable());
            leaveCommand.execute();
            Assert.assertEquals(leaveCommand.result().id(), Long.valueOf(i));
            System.out.println(leaveCommand.format());
        }
        Assert.assertEquals(0, parkingParkingLotService.parkingSlotTable().occupiedSlots().size());
        Assert.assertEquals(5, parkingParkingLotService.parkingSlotTable().freeSlots().size());
    }

    @Test
    public void testStatus() throws ParkingError {
        for (int i = 1; i <= 5; i++) {
            ParkCommand parkCommand = new ParkCommand(Arrays.asList(UUID.randomUUID().toString(), String.valueOf(i)));
            parkCommand.setSlotCarMapping(parkingParkingLotService.parkingSlotTable());
            parkCommand.execute();
            System.out.println(parkCommand.format());
        }
        StatusCommand statusCommand = new StatusCommand(new ArrayList<>());
        statusCommand.setSlotCarMapping(parkingParkingLotService.parkingSlotTable());
        statusCommand.execute();
        List<ParkingSlot> result = statusCommand.result();
        Assert.assertEquals(5, result.size());
        System.out.println(statusCommand.format());
    }

    @Test
    public void testGetRegNumbersByColor() throws ParkingError {
        for (int i = 1; i <= 5; i++) {
            ParkCommand parkCommand = new ParkCommand(Arrays.asList(UUID.randomUUID().toString(), String.valueOf(i)));
            parkCommand.setSlotCarMapping(parkingParkingLotService.parkingSlotTable());
            parkCommand.execute();
            System.out.println(parkCommand.format());
        }
        for (int i = 1; i <= 5; i++) {
            RegNumbersByColorCommand command = new RegNumbersByColorCommand(Collections.singletonList(String.valueOf(i)));
            command.setSlotCarMapping(parkingParkingLotService.parkingSlotTable());
            command.execute();
            System.out.println(command.format());
        }
    }
}
