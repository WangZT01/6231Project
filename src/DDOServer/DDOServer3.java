package DDOServer;


import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Server.MethodImplOperation;
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


public class DDOServer3 {

    public static void main(String args[]) throws RemoteException {

        FIFOBroadcast serverImpl = new FIFOBroadcast("DDO","127.0.0.1");

        FIFOListenerThread ListenerThread3 = new FIFOListenerThread(serverImpl,"127.0.0.1",5071);

        ListenerThread3.run();

    }

}