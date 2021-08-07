package MYFIFO;


import Server.MethodImplOperation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 This class represents a FIFO Broadcast Process, built on top of
 Reliable Broadcast.  The broadcast algorithm is unchanged, but
 the delivery algorithm is overridden.

 @author Tom Austin
 */
public class FIFOBroadcast extends MethodImplOperation
{
    //Messages that have been R-delivered (meaning that they were delivered
    // by Reliable Broadcast) but have not yet been delayed by this algorithm.
    protected LinkedBlockingQueue<String> messages;
    protected List<String> messagesReceived;
    protected String porID;

    //Connection details for this implementation.
    private InetAddress address;
    private DatagramSocket socket;
    //private static int SOCKET = 4446;
    /**
     Constructor
     */
    public FIFOBroadcast(String ID,String groupAddress) throws RemoteException {
        super(ID);
        porID = ID;
        messages = new LinkedBlockingQueue<String>();
        try
        {
            socket = new DatagramSocket();
            address = InetAddress.getByName(groupAddress);
            //socket.joinGroup(address);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     When this method is called, the message has finally
     been F-delivered to the process.
     */
    protected void fDeliver(String m)
    {
        System.out.println("  Delivered: " + m);
    }


    public void receive(String m,int leaderport) throws IOException {
        //If the message has been received before, it is then ignored.
        if (!messages.contains(m))
        {
            //We must track messages that we receive so that we do not
            // relay them multiple times.
            messages.offer(m);

            //Note that the message is relayed to all processes
            // before it is delivered.
            sendToAll(leaderport);

        }
    }

    public void sendToAll(int leaderport) throws IOException {

        String m = messages.poll();
        String receStr = null;
        byte[] buf = m.getBytes();
        if(this.porID.equals("DDO")){
            int port  = 5051;
            for (int i = 0; i < 3; i++) {
                if(port == leaderport){
                    continue;
                }
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                port = port + 10;
            }
        }
        if(this.porID.equals("LVL")){
            int port  = 5052;
            for (int i = 0; i < 3; i++) {
                if(port == leaderport){
                    continue;
                }
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                port = port + 10;
            }
        }
        if(this.porID.equals("MTL")){
            int port  = 5053;
            for (int i = 0; i < 3; i++) {
                if(port == leaderport){
                    continue;
                }
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                port = port + 10;
            }
        }
    }


}
