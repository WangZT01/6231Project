package ServerLVL;


import Define.DefinePort;
import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.election.BullyElector1;
import Replication.election.BullyElector2;
import Replication.heartbeat.HeartBeat;
import Server.Server;

import java.rmi.RemoteException;


public class LVLServer2 extends Server {

    public LVLServer2(FIFOListenerThread fifoListenerThread, HeartBeat heartBeat) {
        super(fifoListenerThread, heartBeat);
    }

    public static void main(String args[]) throws RemoteException {


        FIFOBroadcast serverImpl = new FIFOBroadcast("LVL","127.0.0.1");
        FIFOListenerThread ListenerThread2 = new FIFOListenerThread(serverImpl,"127.0.0.1",5062);
        ListenerThread2.run();
        //bully
//        BullyElector2 bullyElector=new BullyElector2(DefinePort.LVL_OPEARION_PORT2-500,"LVL");
//        bullyElector.start();

    }
}
