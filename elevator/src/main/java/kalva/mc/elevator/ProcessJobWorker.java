package kalva.mc.elevator;

public record ProcessJobWorker(Elevator elevator) implements Runnable {

    @Override
    public void run() {
        elevator.startElevator();
    }
}
