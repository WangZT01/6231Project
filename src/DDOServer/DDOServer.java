package DDOServer;


import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.heartbeat.HeartBeat;
import Server.MethodImpl;
import Server.MethodImplOperation;
import Server.Server;
import ServerModule.Creator;
import ServerModule.CreatorHelper;
import frontEnd.frontendImpl;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class DDOServer extends Server {

    public DDOServer(FIFOListenerThread fifoListenerThread, HeartBeat heartBeat) {
        super(fifoListenerThread, heartBeat);
    }

    public static void main(String args[]) throws RemoteException {


        FIFOBroadcast serverImpl = new FIFOBroadcast("DDO","127.0.0.1");
        FIFOListenerThread ListenerThread1 = new FIFOListenerThread(serverImpl,"127.0.0.1",5051);
        ListenerThread1.run();

    }
}
