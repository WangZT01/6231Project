package Server;

import java.rmi.Remote;

public interface CenterServer {
    /**
 * remote interface method should throw java.rmi.RemoteException
     * @return
     */
    // all the method should throw RemoteException


    public boolean createTRecord (String managerID, String firstName, String lastName, String Address,
                                  String Phone, String Specialization, String Location);

    public boolean createSRecord (String managerID, String firstName, String lastName, String CoursesRegistered,
                                  String Status, String StatusDate);

    public boolean editRecord (String managerID, String recordID, String fieldName, String newValue);

    public boolean transferRecord(String managerID, String recordID, String remoteCenterServerName);

    public boolean printRecord (String ManagerID);

    public String getRecordCounts();


}
