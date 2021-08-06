package DDOServer;


import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.heartbeat.HeartBeat;

import java.rmi.RemoteException;


public class DDOServer3 {


    public static void main(String args[]) throws RemoteException {

        FIFOBroadcast serverImpl = new FIFOBroadcast("DDO","127.0.0.1");


        FIFOListenerThread ListenerThread3 = new FIFOListenerThread(serverImpl,"127.0.0.1",5071);
        //HeartBeat heartBeat3 = new HeartBeat(5071,"DDO");
        //heartBeat3.startUp();

        ListenerThread3.run();
    }


}
