package kalva.mc.elevator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.TreeSet;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class Elevator {

    private int currentFloor = 0;
    private State currentState = State.IDLE;
    private Direction currentDirection = Direction.UP;

    /**
     * jobs which are being processed
     */
    private TreeSet<Request> currentJobs = new TreeSet<>();
    /**
     * up jobs which cannot be processed now so put in pending queue
     */
    private TreeSet<Request> upPendingJobs = new TreeSet<>();
    /**
     * down jobs which cannot be processed now so put in pending queue
     */
    private TreeSet<Request> downPendingJobs = new TreeSet<>();

    public void startElevator() {
        while (true) {
            if (currentJobs.isEmpty()) {
                continue;
            }
            Request request = currentJobs.pollFirst();
            if (null == request) {
                continue;
            }
            if (currentDirection == Direction.UP) {
                processUpRequest(request);
                if (currentJobs.isEmpty()) {
                    addPendingDownJobsToCurrentJobs();
                }
            } else if (currentDirection == Direction.DOWN) {
                processDownRequest(request);
                if (currentJobs.isEmpty()) {
                    addPendingUpJobsToCurrentJobs();
                }
            }
        }
    }

    public void addJob(Request request) {
        if (currentState == State.IDLE) {
            currentState = State.MOVING;
            currentDirection = request.externalRequest().directionToGo();
            currentJobs.add(request);
        } else if (currentState == State.MOVING) {
            if (request.externalRequest().directionToGo() != currentDirection) {
                addtoPendingJobs(request);
            } else if (request.externalRequest().directionToGo() == currentDirection) {
                if (currentDirection == Direction.UP
                        && request.internalRequest().destinationFloor() < currentFloor) {
                    addtoPendingJobs(request);
                } else if (currentDirection == Direction.DOWN
                        && request.internalRequest().destinationFloor() > currentFloor) {
                    addtoPendingJobs(request);
                } else {
                    currentJobs.add(request);
                }
            }
        }
    }

    private void processUpRequest(Request request) {
        if (currentFloor < request.externalRequest().sourceFloor()) {
            for (int i = currentFloor; i <= request.externalRequest().sourceFloor(); i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("We have reached floor -- " + i);
                currentFloor = i;
            }
        }

        // The elevator is now on the floor where the person has requested it i.e. source floor.
        // User can enter and go to the destination floor.
        System.out.println("Reached Source Floor--opening door");
        for (int i = currentFloor; i <= request.internalRequest().destinationFloor(); i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("We have reached floor -- " + i);
            currentFloor = i;
            if (checkIfNewJobCanBeProcessed(request)) {
                break;
            }
        }
    }

    private void processDownRequest(Request request) {
        if (currentFloor < request.externalRequest().sourceFloor()) {
            for (int i = currentFloor; i <= request.externalRequest().sourceFloor(); i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("We have reached floor -- " + i);
                currentFloor = i;
            }
        }

        System.out.println("Reached Source Floor--opening door");
        for (int i = currentFloor - 1; i >= request.internalRequest().destinationFloor(); i--) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("We have reached floor -- " + i);
            currentFloor = i;
            if (checkIfNewJobCanBeProcessed(request)) {
                break;
            }
        }
    }

    private boolean checkIfNewJobCanBeProcessed(Request currentRequest) {
        if (currentJobs.isEmpty()) {
            return false;
        }
        if (currentDirection == Direction.UP) {
            Request request = currentJobs.pollFirst();
            if (request.internalRequest().destinationFloor() <
                    currentRequest.internalRequest().destinationFloor()) {
                currentJobs.add(request);
                currentJobs.add(currentRequest);
                return true;
            }
            currentJobs.add(request);

        }

        if (currentDirection == Direction.DOWN) {
            Request request = currentJobs.pollLast();
            if (request.internalRequest().destinationFloor() >
                    currentRequest.internalRequest().destinationFloor()) {
                currentJobs.add(request);
                currentJobs.add(currentRequest);
                return true;
            }
            currentJobs.add(request);

        }
        return false;
    }

    private void addPendingDownJobsToCurrentJobs() {
        if (!downPendingJobs.isEmpty()) {
            currentJobs = downPendingJobs;
            currentDirection = Direction.DOWN;
        } else {
            currentState = State.IDLE;
        }
    }

    private void addPendingUpJobsToCurrentJobs() {
        if (!upPendingJobs.isEmpty()) {
            currentJobs = upPendingJobs;
            currentDirection = Direction.UP;
        } else {
            currentState = State.IDLE;
        }
    }

    private void addtoPendingJobs(Request request) {
        if (request.externalRequest().directionToGo() == Direction.UP) {
            System.out.println("Add to pending up jobs");
            upPendingJobs.add(request);
        } else {
            System.out.println("Add to pending down jobs");
            downPendingJobs.add(request);
        }
    }
}
