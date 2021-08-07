package ServerLVL;


import Define.DefinePort;
import Replication.heartbeat.FailureDetector;

import java.rmi.RemoteException;


public class LVLDetector {


    public static void main(String args[]) throws RemoteException {

        FailureDetector Listener = new FailureDetector("LVL", DefinePort.LVL_FD_PORT, 5052);
        Listener.addServer(5052);
        Listener.addServer(5062);
        Listener.addServer(5072);
        Listener.run();

    }


}
