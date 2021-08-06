package Replication;

import Replication.election.electionThread;
import Replication.heartbeat.HeartbeatListenerThread;

import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.DatagramPacket;
        import java.net.InetAddress;
        import java.net.MulticastSocket;
        import java.util.ArrayList;
        import java.util.List;
import java.util.Map;


public class FifoBoardcastProcess
{
    //We must track which messages we have received.
    private List<Message> messagesReceived;

    //String that uniquely identifies the process
    protected String procID;

    //Number of messages initially sent by this process.
    protected int messageSentCount;


    //Connection details for this implementation.
    private InetAddress address;
    private MulticastSocket socket;
    private static int SOCKET = 4446;

    protected List<Message> msgBag;
    protected Map<String, Integer> messageSeqMap;
    protected HeartbeatListenerThread heartbeatListenerThread;
    protected electionThread electionThread;

    private String header = "null";
    /**
     Constructor.
     @param groupAddress  Address for this broadcast group.
     */
    public FifoBoardcastProcess(String ID, String groupAddress)
    {
        procID = ID;
        messageSentCount = 0;
        messagesReceived = new ArrayList<Message>();
        try
        {
            socket = new MulticastSocket(SOCKET);
            address = InetAddress.getByName(groupAddress);
            socket.joinGroup(address);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     Constructor.  Defaults to use address 230.0.0.1.
     */
    public FifoBoardcastProcess(String ID)
    {
        this(ID, "230.0.0.1");
    }

    /**
     Method that represents the send primitive.  It transmits a
     message to all processes, including itself.  Note that the text differed
     slightly from this in that its send primitive was responsible
     only for transmitting a message to a single process, rather
     than transmitting to all processes.  To capture this difference,
     we have renamed this "sendToAll".

     @param m  Message to transmit.
     */
    public void sendToAll(Message m)
    {
        byte[] buf = m.transmissionString().getBytes();
        try
        {
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, SOCKET);
            socket.send(packet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     Method that represents the receive primitive, following the
     text's algorithm.
     @param m  Message received.
     */
    public void receive(Message m)
    {
        //If the message has been received before, it is then ignored.
        if (!messagesReceived.contains(m))
        {
            //We must track messages that we receive so that we do not
            // relay them multiple times.
            messagesReceived.add(m);

            //Note that the message is relayed to all processes
            // before it is delivered.
            sendToAll(m);
            deliver(m);
        }
    }

    /**
     To simulate delivery, we just print out the message.
     @param m  Message to deliver.
     */
    public void deliver(Message m)
    {
        System.out.println("  Delivered: " + m);
    }

    /**
     This broadcasts a new message.  Note that at creation,
     the message is tagged with the process's ID and the
     sequence number of this message.

     @param messageText  Text of message to broadcast.
     */
    public void broadcast(String messageText)
    {
        Message m = new Message(messageText, procID, messageSentCount++);
        sendToAll(m);
    }

    private void fDeliverAllPossibleMessage(String sender){

    }

    private void fDeliver(Message m){
        System.out.println(m);
    }

    /**
     Starts the process running.  It will read from standard input and broadcast
     messages for text that the user enters.  Also, this will start the listener
     thread so that it will receive messages broadcast by other processes.
     */
    public void start()
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));



        startListenerThread();

//        while (true)
//        {
//            try
//            {
//                String messageText = reader.readLine();
//                broadcast(messageText);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     Starts a new thread to listen for broadcast messages.
     */
    protected void startListenerThread()
    {
        FifoBoardcastListenerThread thread = new FifoBoardcastListenerThread(this, address, SOCKET);
        thread.start();
    }

    protected  void startHeartbeatThread(){
        heartbeatListenerThread = new HeartbeatListenerThread(this);
        heartbeatListenerThread.start();
    }

    // This method can parse the received messages and parse them.
    protected void startRouter() {
        MessageRouter messageRouter = new MessageRouter(this);
        messageRouter.startRouter();
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public electionThread getElectionThread() {
        return electionThread;
    }

    public HeartbeatListenerThread getHeartbeatThread() {
        return heartbeatListenerThread;
    }


}