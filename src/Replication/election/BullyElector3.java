package Replication.election;


import Define.DefinePort;
import Define.Timeout;
import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;

public class BullyElector3 extends Thread {
    private int myBullyPort;
    private DatagramSocket datagramSocket;
    private InetAddress host;
    private final int FE_PORT=5000;
    private String Servername;
    public BullyElector3(int bullyPort,String servername){
        this.myBullyPort =bullyPort;
        this.Servername = servername;
        try {
            datagramSocket = new DatagramSocket(bullyPort);
            host = InetAddress.getByName("localhost");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket bullyMessage = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(bullyMessage);
                String message=new String(bullyMessage.getData());

                if(message.trim().equals("ELECTION")){//be notify to start election
                    if(bullyMessage.getPort()<myBullyPort && bullyMessage.getPort()!= DefinePort.FailureDetector)
                        sentMessage("NO",bullyMessage.getPort());

                    sentMessage(Servername, DefinePort.FE_OPEARION_PORT);
                    int p = myBullyPort + 500;
                    String LPORT = "NL" + String.valueOf(p);
                    System.out.println("LPORT" + LPORT);
                    //sentMessage("NL" + LPORT,myBullyPort+500);
                    sentMessage(LPORT,myBullyPort + 500 - 10);
                    sentMessage( LPORT,myBullyPort + 500 - 20);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("-------");
        }finally {
            if(datagramSocket != null)
                datagramSocket.close();
        }
    }


    public void sentMessage(String content, int targetBullyPort){
        try {
            byte[] message = content.getBytes();
            DatagramPacket replyPacket = new DatagramPacket(message, message.length, host,targetBullyPort);
            datagramSocket.send(replyPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean waiting(){
        boolean flag=true;

        Timeout timeout=new Timeout(1000);
        timeout.startUp();
        while (timeout.flag){
            try{
                byte[] buffer = new byte[1000];
                DatagramPacket message = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(message);
                if(message.getPort()>myBullyPort)
                    flag=false;
                String mess=new String(message.getData());
                System.out.println(mess);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static void main(String args[]) throws RemoteException {


        //bully
        BullyElector3 bullyElector=new BullyElector3(DefinePort.DDO_OPEARION_PORT3-500,"DDO");
        bullyElector.start();
        BullyElector3 bullyElector2=new BullyElector3(DefinePort.LVL_OPEARION_PORT3-500,"LVL");
        bullyElector2.start();
        BullyElector3 bullyElector3=new BullyElector3(DefinePort.MTL_OPEARION_PORT3-500,"MTL");
        bullyElector3.start();

    }

}
