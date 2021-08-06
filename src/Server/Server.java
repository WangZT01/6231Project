package Server;

import MYFIFO.FIFOListenerThread;
import Replication.heartbeat.HeartBeat;

public class Server {

    private FIFOListenerThread fifoListenerThread;
    private HeartBeat heartBeat;

    public Server(FIFOListenerThread fifoListenerThread, HeartBeat heartBeat){
        this.fifoListenerThread = fifoListenerThread;
        this.heartBeat = heartBeat;
    }
    public void start(){
        fifoListenerThread.run();
        heartBeat.startUp();
    }
}
