package DDOServer;


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


public class DDOServer1 extends MethodImpl {

    public DDOServer1() throws RemoteException {
        super();
        name = this.getClass().getName();
        System.out.println(name);
    }

    public static void main(String args[]) throws RemoteException {

            MethodImplOperation serverImpl = new MethodImplOperation();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    serverImpl.UDPServer(5051);
                }
            }).start();
        }


}
