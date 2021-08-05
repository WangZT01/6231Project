package MYFIFO;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;


/**
 This class listens for broadcast messages and reports them to the
 ReliableBroadcastProcss that spawned it.

 @author Tom Austin
 */
public class FIFOListenerThread extends Thread {
    //RB process that this class corresponds to.
    private FIFOBroadcast rbp;

    //Network connection details.
    private int socketNum;
    private String address;

    /**
     Constructor.

     @param rbp  Process that this thread belongs to.
     @param address  Address of the broadcast group.
     @param socketNum  Socket on which to listen.
     */
    public FIFOListenerThread(FIFOBroadcast rbp, String address, int socketNum)
    {
        this.rbp = rbp;
        this.address = address;
        this.socketNum = socketNum;
    }

    /**
     Starts the thread.  It will listen for broadcast messages and report
     them to its corresponding process.

     @see Runnable#run()
     */
    public void run()
    {
        DatagramSocket server;
        try
        {
            server = new DatagramSocket(socketNum);
            //InetAddress groupaddress = groupaddress.getHostName(address);
            //server.joinGroup(groupaddress);
            DatagramPacket recvPacket;
            String sendStr = null;
            int Sendport = 0;
            while (true)
            {
                byte[] recvBuf = new byte[1000];
                recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
                server.receive(recvPacket);
                String received = new String(recvPacket.getData(), 0, recvPacket.getLength());
                //The message is passed to the RB process here.
                if(received.startsWith("operation")){
                    rbp.receive(received);
                    Sendport = recvPacket.getPort();
                    sendStr = rbp.operating(received);
                }
                InetAddress addr = recvPacket.getAddress();
                byte[] sendBuf = sendStr.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, Sendport);
                server.send(sendPacket);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
