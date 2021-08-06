package Server;


import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
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

    public static void main(String args[]) throws RemoteException {

        FIFOBroadcast serverImpl = new FIFOBroadcast("MTL","127.0.0.1");
        FIFOListenerThread ListenerThread1 = new FIFOListenerThread(serverImpl,"127.0.0.1",5053);
        FIFOListenerThread ListenerThread2 = new FIFOListenerThread(serverImpl,"127.0.0.1",5063);
        FIFOListenerThread ListenerThread3 = new FIFOListenerThread(serverImpl,"127.0.0.1",5073);
        ListenerThread1.run();
        ListenerThread2.run();
        ListenerThread3.run();
    }

}