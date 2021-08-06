package ServerDDO;


import Define.DefinePort;
import MYFIFO.FIFOBroadcast;
import MYFIFO.FIFOListenerThread;
import Replication.election.BullyElector2;

import java.rmi.RemoteException;


public class DDOServer2 {


    public static void main(String args[]) throws RemoteException {

        FIFOBroadcast serverImpl = new FIFOBroadcast("DDO","127.0.0.1");


        FIFOListenerThread ListenerThread2 = new FIFOListenerThread(serverImpl,"127.0.0.1",5061);

        ListenerThread2.run();
       BullyElector2 bullyElector=new BullyElector2(DefinePort.DDO_OPEARION_PORT2-500,serverImpl.name);
        bullyElector.start();

    }


}
