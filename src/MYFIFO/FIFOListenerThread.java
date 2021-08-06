package MYFIFO;

import Define.DefinePort;
import Replication.heartbeat.HeartBeat;
import Define.Timeout;
import java.io.IOException;
import java.net.*;
import java.util.*;


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
    private int leaderPort;
    private HeartBeat heartBeat;
    private ArrayList<Integer> DDOport;
    private ArrayList<Integer> LVLport;
    private ArrayList<Integer> MTLport;

    /**
     Constructor.

     @param rbp  Process that this thread belongs to.
     @param address  Address of the broadcast group.
     @param socketNum  Socket on which to listen.
     */
    public FIFOListenerThread(FIFOBroadcast rbp, String address, int socketNum)
    {

        DDOport = new ArrayList<Integer>(){};
        DDOport.add(5051);
        DDOport.add(5061);
        DDOport.add(5071);

        LVLport = new ArrayList<Integer>(){};
        LVLport.add(5052);
        LVLport.add(5062);
        LVLport.add(5072);

        MTLport = new ArrayList<Integer>(){};
        MTLport.add(5053);
        MTLport.add(5063);
        MTLport.add(5073);

        this.rbp = rbp;
        this.address = address;
        this.socketNum = socketNum;
        if(rbp.porID.equals("DDO")){
            leaderPort = 5051;
        }
        if(rbp.porID.equals("LVL")){
            leaderPort = 5052;
        }
        if(rbp.porID.equals("MTL")){
            leaderPort = 5053;
        }
        heartBeat = new HeartBeat(this.socketNum,rbp.name);
        //heartBeat.startUp();
    }

    /**
     Starts the thread.  It will listen for broadcast messages and report
     them to its corresponding process.

     @see Runnable#run()
     */
    public void run() {

        heartBeat.startUp();
        UDPServer();
    }

    public void setLeader(int leaderPort) {
        this.leaderPort = leaderPort;
    }

    public void UDPServer(){
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
                System.out.println(received);
                if(received.startsWith("operation")){
                    if(leaderPort == socketNum){
                        rbp.receive(received,leaderPort);
                        Sendport = recvPacket.getPort();
                        sendStr = rbp.operating(received);
                    }
                }
                if (received != null && received.startsWith("getCount")) {

                    sendStr = rbp.getCountForUDP(received);
                    Sendport = recvPacket.getPort();
                    System.out.println("sendStr : "+ sendStr);

                } else if (received != null && received.startsWith("Transfer")) {
                    boolean result = rbp.transferForUDP(received);
                    if(result){
                        sendStr = "Transfer success";
                    }else{
                        sendStr = "Transfer fail";
                    }
                    Sendport = recvPacket.getPort();
                }
                if(received.startsWith("NL")){
                    String rece = received.substring(2,6);
                    int newleader = Integer.parseInt(rece);
                    leaderPort = newleader;
                    sendStr = "leader changed";
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

    private void sendElectionMessage(int socketNum) throws UnknownHostException {

        String result = null;
        int l_leaderPort = this.leaderPort;
        if(this.rbp.porID.equals("DDO")){

            //DDOport.remove(l_leaderPort);
            //for (int i = 0; i < DDOport.size(); i++) { }
            if(socketNum<5061){
                String electionMessage="VOTE";
                sentMessage(electionMessage,5061);
                System.out.println("server1:sent election message to server2");
                sentMessage(electionMessage,5071);
                System.out.println("server1:sent election message to server3");
                if (waiting()){
                    sentMessage(this.rbp.porID, DefinePort.FE_OPEARION_PORT);
                    setLeader(socketNum);
                }
            }else if(socketNum<5071){
                String electionMessage="VOTE";
                sentMessage(electionMessage,5071);
                System.out.println("server1:sent election message to server3");
                if (waiting()){
                    sentMessage(this.rbp.porID, DefinePort.FE_OPEARION_PORT);
                    setLeader(socketNum);
                }
            }else if(socketNum==5071){
                sentMessage(this.rbp.porID, DefinePort.FE_OPEARION_PORT);
            }
        }
        else if(this.rbp.porID.equals("LVL")){

        }
        else if(this.rbp.porID.equals("MTL")){

        }

        //return result;
    }

    public void sentMessage(String content, int targetBullyPort) throws UnknownHostException {
        InetAddress host = InetAddress.getByName("127.0.0.1");
        try {
            byte[] message = content.getBytes();
            DatagramPacket replyPacket = new DatagramPacket(message, message.length, host,targetBullyPort);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(replyPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean waiting(){
        boolean flag=true;
        DatagramSocket datagramSocket = null;
        Timeout timeout=new Timeout(1000);
        timeout.startUp();
        while (timeout.flag){
            try{
                byte[] buffer = new byte[1000];
                DatagramPacket message = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(message);
                if(message.getPort()>socketNum)
                    flag=false;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return flag;
    }
}
