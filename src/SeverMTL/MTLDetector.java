package SeverMTL;

import Define.DefinePort;
import Replication.heartbeat.FailureDetector;

import java.rmi.RemoteException;

public class MTLDetector {

    public static void main(String args[]) throws RemoteException {

        FailureDetector Listener = new FailureDetector("MTL", DefinePort.MTL_FD_PORT,5053);
        Listener.addServer(5053);
        Listener.addServer(5063);
        Listener.addServer(5073);
        Listener.run();

    }

}
