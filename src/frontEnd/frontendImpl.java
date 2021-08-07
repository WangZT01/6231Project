package frontEnd;


import Define.DefinePort;
import RecordFile.Record;
import ServerModule.CreatorPOA;
import org.omg.CORBA.ORB;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The server implement the remote interface.
 * Must inherit UnicastRemoteObject to allow JVM to create remote stubs/proxy
 */
public class frontendImpl extends CreatorPOA implements Serializable{

    private ORB orb;
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
    // implement shutdown() method
    public void shutdown() {
        orb.shutdown(false);
    }

    ConcurrentHashMap<Character, ArrayList<Record>> HashMapMTL = new ConcurrentHashMap<Character, ArrayList<Record>>();
    ConcurrentHashMap<Character, ArrayList<Record>> HashMapLVL = new ConcurrentHashMap<Character, ArrayList<Record>>();
    ConcurrentHashMap<Character, ArrayList<Record>> HashMapDDO = new ConcurrentHashMap<Character, ArrayList<Record>>();
    int LeaderPORT = 0;
    File loggingFile = new File("");
    String FilePath = loggingFile.getAbsolutePath();

    String name = "FE";
    String ManagerID;

     HashMap<String, Integer> ServerPort = new HashMap<String, Integer>(){};

    static HashMap<String, Integer> clientPort = new HashMap<String, Integer>(){};
    static {
        clientPort.put("MTL", 6053);
        clientPort.put("LVL", 6052);
        clientPort.put("DDO", 6051);
    }

    public static int messageID = 1;

    public frontendImpl(String name) throws RemoteException {

        super();
        this.name = name;
        ServerPort.put("MTL", 5053);
        ServerPort.put("LVL", 5052);
        ServerPort.put("DDO", 5051);

    }


    private int getMsgIdAndIncre() {

        messageID++;
        return messageID - 1;
    }

    @Override
    public boolean createTRecord(String managerID, String firstName, String lastName, String address, String phone, String specialization, String location){
        ManagerID = managerID;
        boolean flag = false;
        String messageString = "operation" + ",1," + managerID + "," + firstName + "," + lastName + "," + address + "," + phone + "," + specialization + "," + location;
        String reply = sendUdpMessageWithRet(messageString);
        if (reply.equals("SUCCESS")){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean createSRecord(String managerID, String firstName, String lastName, String coursesRegistered, String status, String date) {
        ManagerID = managerID;
        boolean flag = false;
        String messageString = "operation" + ",2," + managerID + "," + firstName + "," + lastName + "," + coursesRegistered + "," + status + "," + date;
        String reply = sendUdpMessageWithRet(messageString);
        if (reply.equals("SUCCESS")){
            flag = true;
        }
        return flag;
    }



    @Override
    public boolean editRecord(String managerID, String recordID, String fieldName, String newValue) {
        ManagerID = managerID;
        boolean flag = false;
        String messageString = "operation" + ",4," + managerID + "," + recordID + "," + fieldName + "," + newValue;
        String reply = sendUdpMessageWithRet(messageString);

        System.out.println(reply);
        flag = true;
        return flag;
    }

    @Override
    public boolean transferRecord(String managerID, String recordID, String remoteCenterServerName){
        ManagerID = managerID;
        boolean flag = false;
        String messageString = "operation" + ",5," + managerID + "," + recordID + "," + remoteCenterServerName;
        String reply = sendUdpMessageWithRet(messageString);
        if (reply.equals("SUCCESS")) {
            flag = true;
        }
        return flag;
    }

    @Override
    public String getRecordCounts() {

        String messageString = "operation" + ",3,";
        String result = sendUdpMessageWithRet(messageString);

        return result;
    }

    //Print the record to the server in the corresponding region
    @Override
    public boolean printRecord(String managerID)   {
        ManagerID = managerID;
        boolean flag = false;
        String messageString = "operation" + ",6," + managerID;
        System.out.println("print order send");
        String reply = sendUdpMessageWithRet(messageString);
        if (reply.equals("SUCCESS")) {
            flag = true;
        }
        return flag;
    }


    public  String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = date.toString();
        return time;
    }

    /**
     * Save the Server.
     */
    public void save(String Location){


        try {
            FileOutputStream l_saveFile = null;
            if(Location.equals("LVL")){
                l_saveFile = new FileOutputStream(FilePath + "\\" + "LogFile" + "\\" + "LVLFile" + "\\" + "LVLServer" + ".txt");
                ObjectOutputStream l_Save = new ObjectOutputStream(l_saveFile);
                synchronized(this){
                    l_Save.writeObject(HashMapLVL);
                }
                l_Save.flush();
                l_Save.close();
            }
            else if(Location.equals("MTL")){
                l_saveFile = new FileOutputStream(FilePath + "\\" + "LogFile" + "\\" + "MTLFile" + "\\" + "MTLServer" + ".txt");
                ObjectOutputStream l_Save = new ObjectOutputStream(l_saveFile);
                synchronized(this){
                    l_Save.writeObject(HashMapMTL);
                }
                l_Save.flush();
                l_Save.close();
            }
            else if(Location.equals("DDO")){
                l_saveFile = new FileOutputStream(FilePath + "\\" + "LogFile" + "\\" + "DDOFile" + "\\" + "ServerDDO" + ".txt");
                ObjectOutputStream l_Save = new ObjectOutputStream(l_saveFile);
                synchronized(this){
                    l_Save.writeObject(HashMapDDO);
                }
                l_Save.flush();
                l_Save.close();
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("write object success!");
    }

    public String sendUdpMessageWithRet(String message) {
        int port = 0;
        if(ManagerID.startsWith("DDO")){
            port = ServerPort.get("DDO");
        }
        if(ManagerID.startsWith("LVL")){
            port = ServerPort.get("LVL");
        }
        if(ManagerID.startsWith("MTL")){
            port = ServerPort.get("MTL");
        }
        System.out.println("send port" + port);
        String recvStr = "";
        DatagramSocket clientSocket = null;

        System.out.println("ManagerID: "+ ManagerID);
        try {


            clientSocket = new DatagramSocket();
            byte[] sendData = new byte[1000];
            sendData = message.getBytes();
            InetAddress clientHost = InetAddress.getByName("127.0.0.1");

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientHost, port);
            clientSocket.send(sendPacket);

            // receiving process
            byte[] recvBuf = new byte[1000];
            DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
            clientSocket.receive(recvPacket);
            recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
            System.out.println(recvStr);
            if(recvStr.equals("Transfer success")){
                save(this.name);
            }
            clientSocket.close();

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
                clientSocket = null;
            }
        }

        return recvStr;
    }

    public void UDPServer(int port) {

        try {
            DatagramSocket server = null;
            server = new DatagramSocket(port);
            byte[] recvBuf = new byte[1000];
            String sendStr = "5051 HELLO";

            while (true) {
                DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
                server.receive(recvPacket);
                String recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
                System.out.println(recvStr);
                int Sendport = recvPacket.getPort();
                InetAddress addr = recvPacket.getAddress();


                if(recvStr.equals("DDO")){
                    LeaderPORT = recvPacket.getPort() + 500;
                    System.out.println("NewLeaderPORT"+ LeaderPORT);
                    ServerPort.remove("DDO");
                    ServerPort.put("DDO",LeaderPORT);
                    sendStr = "NL" + String.valueOf(LeaderPORT);
                    System.out.println("frontend sendStr"+sendStr);
                    Sendport = LeaderPORT;
                    byte[] sendBuf = sendStr.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, DefinePort.DDO_FD_PORT);
                    server.send(sendPacket);
                    sentMessageForNewLeader(sendStr,ServerPort.get("LVL"));
                    sentMessageForNewLeader(sendStr,ServerPort.get("MTL"));

                }
                else if(recvStr.equals("LVL")){
                    LeaderPORT = recvPacket.getPort() + 500;
                    System.out.println("NewLeaderPORT"+ LeaderPORT);
                    ServerPort.remove("LVL");
                    ServerPort.put("LVL",LeaderPORT);
                    sendStr = "NL" + String.valueOf(LeaderPORT);
                    Sendport = LeaderPORT;
                    byte[] sendBuf = sendStr.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, DefinePort.LVL_FD_PORT);
                    server.send(sendPacket);
                    sentMessageForNewLeader(sendStr,ServerPort.get("DDO"));
                    sentMessageForNewLeader(sendStr,ServerPort.get("MTL"));

                }
                else if(recvStr.equals("MTL")){
                    LeaderPORT = recvPacket.getPort() + 500;
                    System.out.println("NewLeaderPORT"+ LeaderPORT);
                    ServerPort.remove("MTL");
                    ServerPort.put("MTL",LeaderPORT);
                    sendStr = "NL" + String.valueOf(LeaderPORT);
                    Sendport = LeaderPORT;
                    byte[] sendBuf = sendStr.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, DefinePort.MTL_FD_PORT);
                    server.send(sendPacket);
                    sentMessageForNewLeader(sendStr,ServerPort.get("LVL"));
                    sentMessageForNewLeader(sendStr,ServerPort.get("DDO"));

                }
                byte[] sendBuf = sendStr.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, Sendport);
                server.send(sendPacket);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sentMessageForNewLeader(String sendstr, int targetPort){
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress inetAddress=InetAddress.getByName("localhost");
            byte[] message =sendstr.getBytes();
            DatagramPacket replyPacket = new DatagramPacket(message, message.length,inetAddress,targetPort);
            datagramSocket.send(replyPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Changing imformation is sent.";
    }

}
