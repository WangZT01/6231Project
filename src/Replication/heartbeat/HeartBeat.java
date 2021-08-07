package Replication.heartbeat;

import Define.DefinePort;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class HeartBeat implements ActionListener{

    private String serverName;
    private Timer timer;
    private int replicaNo;


    public HeartBeat(int portNo, String serverName){
        this.timer= new Timer(3000,this);
        this.replicaNo=portNo;
        this.serverName = serverName;
    }

    public void startUp(){
        this.timer.start();
    }

    public void showDown(){
        this.timer.stop();
    }

    public void setReplicaNo(int replicaNo) {
        this.replicaNo = replicaNo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sentHeartBeat();
    }


    public void sentHeartBeat() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            byte[] message = String.valueOf(replicaNo).getBytes();
            InetAddress host = InetAddress.getByName("localhost");
            int port = 0;
            if (serverName.equals("DDO")){
                port = 6001;
            }
            if (serverName.equals("LVL")){
                port = 6002;
            }
            if (serverName.equals("MTL")){
                port = 6003;
            }

            DatagramPacket heartBeatPacket = new DatagramPacket(message, message.length,host, port);
            datagramSocket.send(heartBeatPacket);
            //System.out.println(heartBeatPacket.getData());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(datagramSocket != null)
                datagramSocket.close();
        }
    }
}
