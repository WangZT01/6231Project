package DDOServer;


import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Server.MethodImpl;
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


public class DDOServer extends MethodImpl {

    public DDOServer() throws RemoteException {
        super();
        name = this.getClass().getName();
        System.out.println(name);
    }

    public static void main(String args[]) throws RemoteException {

        FIFOBroadcast serverImpl = new FIFOBroadcast("DDO","127.0.0.1");
        FIFOListenerThread ListenerThread1 = new FIFOListenerThread(serverImpl,"127.0.0.1",5051);
        FIFOListenerThread ListenerThread2 = new FIFOListenerThread(serverImpl,"127.0.0.1",5061);
        FIFOListenerThread ListenerThread3 = new FIFOListenerThread(serverImpl,"127.0.0.1",5071);
        ListenerThread1.run();
        ListenerThread2.run();
        ListenerThread3.run();
    }


}
