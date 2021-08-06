package DDOServer;


import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.heartbeat.HeartBeat;

import java.rmi.RemoteException;
import java.util.ArrayList;


public class DDOServer2 {


    public static void main(String args[]) throws RemoteException {

        FIFOBroadcast serverImpl = new FIFOBroadcast("DDO","127.0.0.1");


        FIFOListenerThread ListenerThread2 = new FIFOListenerThread(serverImpl,"127.0.0.1",5061);

        ListenerThread2.run();

    }


}
