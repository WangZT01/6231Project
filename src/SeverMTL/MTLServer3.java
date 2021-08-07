package SeverMTL;


import Define.DefinePort;
import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.election.BullyElector1;
import Replication.election.BullyElector3;

import java.rmi.RemoteException;


public class MTLServer3 {

    public static void main(String args[]) throws RemoteException {


        FIFOBroadcast serverImpl = new FIFOBroadcast("MTL","127.0.0.1");
        FIFOListenerThread ListenerThread3 = new FIFOListenerThread(serverImpl,"127.0.0.1",5053);
        ListenerThread3.run();
        //bully
//        BullyElector3 bullyElector=new BullyElector3(DefinePort.MTL_OPEARION_PORT3-500,"MTL");
//        bullyElector.start();

    }

}