package ServerLVL;


import Define.DefinePort;
import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.election.BullyElector1;
import Replication.election.BullyElector3;
import Replication.heartbeat.HeartBeat;
import Server.Server;

import java.rmi.RemoteException;


public class LVLServer3 extends Server {

    public LVLServer3(FIFOListenerThread fifoListenerThread, HeartBeat heartBeat) {
        super(fifoListenerThread, heartBeat);
    }

    public static void main(String args[]) throws RemoteException {


        FIFOBroadcast serverImpl = new FIFOBroadcast("LVL","127.0.0.1");
        FIFOListenerThread ListenerThread3 = new FIFOListenerThread(serverImpl,"127.0.0.1",5072);
        ListenerThread3.run();
        //bully
        BullyElector3 bullyElector=new BullyElector3(DefinePort.LVL_OPEARION_PORT3-500,"LVL");
        bullyElector.start();

    }
}
