package Server;


import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Server.MethodImplOperation;
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

    public static void main(String args[]) throws RemoteException {

        FIFOBroadcast serverImpl = new FIFOBroadcast("LVL","127.0.0.1");
        FIFOListenerThread ListenerThread1 = new FIFOListenerThread(serverImpl,"127.0.0.1",5052);
        FIFOListenerThread ListenerThread2 = new FIFOListenerThread(serverImpl,"127.0.0.1",5062);
        FIFOListenerThread ListenerThread3 = new FIFOListenerThread(serverImpl,"127.0.0.1",5072);
        ListenerThread1.run();
        ListenerThread2.run();
        ListenerThread3.run();
    }

}