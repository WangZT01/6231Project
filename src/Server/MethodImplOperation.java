package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;

public class MethodImplOperation extends MethodImpl {


    public MethodImplOperation(String name) throws RemoteException {
        super(name);
    }

    public String operating(String message){
        if (!message.equals("")) {
            byte[] reply = null;
            String[] strings = message.split(",");
            //int messageId = Integer.parseInt(strings[0]);


            String result = null;
            switch (strings[1]) {
                case "1":
                    boolean flag = this.createTRecord(strings[2], strings[3], strings[4], strings[5], strings[6], strings[7], strings[8]);
                    result = castBoolean2Return(flag);
                    break;
                case "2":
                    flag = this.createSRecord(strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);
                    result = castBoolean2Return(flag);
                    break;
                case "3":
                        result = this.getRecordCounts();
                        System.out.println("getRecordCounts: " + result);
                        break;
                case "4":
                    flag = this.editRecord(strings[2], strings[3], strings[4], strings[5]);
                    result = castBoolean2Return(flag);
                    break;
                case "5":
                    flag = this.transferRecord(strings[2], strings[3], strings[4]);
                    result = castBoolean2Return(flag);
                    break;
                case "6":
                    System.out.println(strings[2]);
                    flag = this.printRecord(strings[2]);
                    result = castBoolean2Return(flag);
                    break;
                default:
                    System.out.println("Server - receive invalid message");
            }
            //results.add(result);
            return result;
        }
        return "UDP ERROR ";
    }

    private String castBoolean2Return(boolean b){
        if(b)
            return "SUCCESS";
        else
            return "Fail";
    }

}
