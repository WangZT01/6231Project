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
        this.timer= new Timer(5000,this);
        this.replicaNo=portNo;
        this.serverName = serverName;
    }

    public void startUp(){
        this.timer.start();
    }

    public void showDown(){
        this.timer.stop();
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

            DatagramPacket heartBeatPacket = new DatagramPacket(message, message.length,host, DefinePort.FailureDetector);
            datagramSocket.send(heartBeatPacket);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(datagramSocket != null)
                datagramSocket.close();
        }
    }
}
