package kalva.mc.elevator;

public class TestElevator {

    public static void main(String args[]) {

        Elevator elevator = new Elevator();

        ProcessJobWorker processJobWorker = new ProcessJobWorker(elevator);
        Thread t2 = new Thread(processJobWorker);
        t2.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ExternalRequest er = new ExternalRequest(0, Direction.UP);
        InternalRequest ir = new InternalRequest(5);
        Request request1 = new Request(ir, er);


        new Thread(new AddJobWorker(request1, elevator)).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
