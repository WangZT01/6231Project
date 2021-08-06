package Replication;

public class MessageRouter {
    private final int procID;
    FifoBoardcastProcess fbp;

    public MessageRouter(FifoBoardcastProcess fbp) {
        this.fbp = fbp;
        procID = Integer.parseInt(fbp.procID);
    }

    public void startRouter() {

    }
}
