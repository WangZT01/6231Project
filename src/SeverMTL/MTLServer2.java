package SeverMTL;


import Define.DefinePort;
import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.election.BullyElector1;
import Replication.election.BullyElector2;

import java.rmi.RemoteException;


public class MTLServer2 {

    public static void main(String args[]) throws RemoteException {


        FIFOBroadcast serverImpl = new FIFOBroadcast("MTL","127.0.0.1");
        FIFOListenerThread ListenerThread2 = new FIFOListenerThread(serverImpl,"127.0.0.1",5053);
        ListenerThread2.run();
        //bully
        BullyElector2 bullyElector=new BullyElector2(DefinePort.MTL_OPEARION_PORT2-500,"MTL");
        bullyElector.start();

    }

}