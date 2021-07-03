package Server;


import ServerModule.Creator;
import ServerModule.CreatorHelper;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;


public class MTLServer {

    public static void main(String args[]) {
        try{
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            MethodImpl serverImpl = new MethodImpl("MTL");

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
            String name = "MTL";
            NameComponent path[] = ncRef.to_name( name );
            ncRef.rebind(path, href);

            System.out.println("HelloServer ready and waiting ...");

            // wait for invocations from clients
            //.run();

            try {
                DatagramSocket server = null;
                server = new DatagramSocket(5053);
                byte[] recvBuf = new byte[1000];


                while (true) {
                    DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
                    server.receive(recvPacket);
                    String recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
                    System.out.println("Hello World!" + recvStr);

                    int port = recvPacket.getPort();
                    System.out.println(port);

                    if(recvStr.startsWith("MTL")){

                        System.out.println(serverImpl.load("MTL"));
                        serverImpl.load("MTL");

                    }
                    if(recvStr.startsWith("LVL")){
                        System.out.println(serverImpl.load("LVL"));
                        serverImpl.load("MTL");
                    }
                    if(recvStr.startsWith("DDO")){
                        System.out.println(serverImpl.load("DDO"));
                        serverImpl.load("MTL");
                    }

                    InetAddress addr = recvPacket.getAddress();
                    String sendStr = "5053 HELLO";
                    byte[] sendBuf = sendStr.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, port);
                    server.send(sendPacket);

                }

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

        System.out.println("HelloServer Exiting ...");

    }

    public static MethodMTL loadServer() throws RemoteException {

        MethodMTL methodMTL = new MethodMTL();

        File MTLFile = new File("");
        String FilePath = MTLFile.getAbsolutePath();
        MTLFile = new File(FilePath + "\\" + "LogFile" + "\\" + "MTLFile" + "\\" + "MTLServer" + ".txt");
        if (!MTLFile.exists()) {
            try {
                MTLFile.createNewFile();
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("The Map is Empty!");
            }
        }

        try {

            ObjectInputStream l_ois = null;
            l_ois = new ObjectInputStream(new FileInputStream(FilePath + "\\" + "LogFile" + "\\" + "MTLFile" + "\\" + "MTLServer" + ".txt"));

            try {
                methodMTL = (MethodMTL) l_ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("The Map is Empty!");
        }
        return methodMTL;
    }

}