package ServerDDO;


import Define.DefinePort;
import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.election.BullyElector1;
import Replication.heartbeat.HeartBeat;
import Server.Server;

import java.rmi.RemoteException;


public class DDOServer extends Server {

    public DDOServer(FIFOListenerThread fifoListenerThread, HeartBeat heartBeat) {
        super(fifoListenerThread, heartBeat);
    }

    public static void main(String args[]) throws RemoteException {


        FIFOBroadcast serverImpl = new FIFOBroadcast("DDO","127.0.0.1");
        FIFOListenerThread ListenerThread1 = new FIFOListenerThread(serverImpl,"127.0.0.1",5051);
        ListenerThread1.run();
//        //bully
        BullyElector1 bullyElector=new BullyElector1(DefinePort.DDO_OPEARION_PORT1-500,"DDO");
        bullyElector.start();

    }
}
