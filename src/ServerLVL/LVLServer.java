package ServerLVL;


import Define.DefinePort;
import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.election.BullyElector1;
import Replication.heartbeat.HeartBeat;
import Server.Server;

import java.rmi.RemoteException;


public class LVLServer extends Server {

    public LVLServer(FIFOListenerThread fifoListenerThread, HeartBeat heartBeat) {
        super(fifoListenerThread, heartBeat);
    }

    public static void main(String args[]) throws RemoteException {


        FIFOBroadcast serverImpl = new FIFOBroadcast("LVL","127.0.0.1");
        FIFOListenerThread ListenerThread1 = new FIFOListenerThread(serverImpl,"127.0.0.1",5052);
        ListenerThread1.run();
        //bully
        BullyElector1 bullyElector=new BullyElector1(DefinePort.LVL_OPEARION_PORT1-500,"LVL");
        bullyElector.start();

    }
}
