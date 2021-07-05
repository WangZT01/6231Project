package Server;


import ServerModule.Creator;
import ServerModule.CreatorHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;


public class LVLServer {

    public static void main(String args[]) {
        try{
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            MethodImpl serverImpl = new MethodImpl("LVL");

            serverImpl.setORB(orb);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(serverImpl);
            Creator href = CreatorHelper.narrow(ref);

            // get the root naming context
            // NameService invokes the name service
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // bind the Object Reference in Naming
            String name = "LVL";
            NameComponent path[] = ncRef.to_name( name );
            ncRef.rebind(path, href);

            System.out.println("HelloServer ready and waiting ...");

            // wait for invocations from clients
            //orb.run();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    serverImpl.UDPServer(5052);
                }
            }).start();

        }

        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

        //System.out.println("HelloServer Exiting ...");

    }

    public static String getCountUDP(String location) {
        int port = 0;
        if (location.equals("DDO")) {
            port = 5051;
        } else if (location.equals("LVL")) {
            port = 5052;
        } else if (location.equals("MTL")) {
            port = 5053;
        }

        String recvStr = null;
        try {
            DatagramSocket client = new DatagramSocket();
            // sending process
            String sendStr = "Count";
            byte[] sendBuf = sendStr.getBytes();
            InetAddress addr = InetAddress.getByName("127.0.0.1");

            DatagramPacket sendPacketDDO = new DatagramPacket(sendBuf, sendBuf.length, addr, port);
            client.send(sendPacketDDO);

            // receiving process
            byte[] recvBuf = new byte[1000];
            DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
            client.receive(recvPacket);
            recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
            System.out.println(recvStr);
            client.close();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recvStr;
    }
}