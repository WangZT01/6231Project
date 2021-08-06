package ServerDDO;


import Replication.heartbeat.FailureDetector;

import java.rmi.RemoteException;


public class DDODetector {


    public static void main(String args[]) throws RemoteException {

        FailureDetector Listener = new FailureDetector("DDO");
        Listener.addServer(5051);
        Listener.addServer(5061);
        Listener.addServer(5071);
        Listener.run();

    }


}
