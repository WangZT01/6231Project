package Server;

import RecordFile.Record;
import RecordFile.StudentRecord;
import RecordFile.TeacherRecord;
import ServerModule.CreatorPOA;
import org.omg.CORBA.ORB;

import java.io.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

class ServerImpl extends CreatorPOA{
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }


    // implement shutdown() method
    public void shutdown() {
        orb.shutdown(false);
    }


    HashMap<Character, ArrayList<Record>> HashMapMTL = new HashMap<Character, ArrayList<Record>>();
    HashMap<Character, ArrayList<Record>> HashMapLVL = new HashMap<Character, ArrayList<Record>>();
    HashMap<Character, ArrayList<Record>> HashMapDDO = new HashMap<Character, ArrayList<Record>>();

    File loggingFile = new File("");
    String FilePath = loggingFile.getAbsolutePath();
    int MTLcount = 0;

    /**
     * Determine whether teacher records can be created
     * @param managerID managerID
     * @param firstName firstName
     * @param lastName lastName
     * @param Address Address
     * @param Phone Phone
     * @param Specialization Specialization
     * @param Location Location
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean createTRecord(String managerID, String firstName, String lastName, String Address, String Phone, String Specialization, String Location)   {

        if(!(Location.equals("mtl")||Location.equals("lvl")||Location.equals("ddo"))){
            System.out.println("The location is invalid.");
            return false;
        }
        TeacherRecord NewTRecord = new TeacherRecord(firstName, lastName, Address, Phone, Specialization, Location);
        MTLcount++;
        NewTRecord.setRecordID(MTLcount);
        ArrayList<Record> Recordlist = new ArrayList<>();


        if(managerID.startsWith("MTL")){

            // Get the first letter.
            char Mark;
            Mark = lastName.charAt(0);

            //Put new record in the record list.
            if(HashMapMTL.containsKey(Mark)){
                Recordlist = HashMapMTL.get(Mark);
                Recordlist.add(NewTRecord);
                HashMapMTL.replace(Mark, Recordlist);

            }

            //Create new first letter key and the record list.
            else{
                Recordlist.add(NewTRecord);
                HashMapMTL.put(Mark, Recordlist);
            }

        }
        else if(managerID.startsWith("LVL")){

            char Mark;
            Mark = lastName.charAt(0);
            if(HashMapLVL.containsKey(Mark)){
                Recordlist = HashMapLVL.get(Mark);
                Recordlist.add(NewTRecord);
                HashMapLVL.replace(Mark, Recordlist);

            }
            else{
                Recordlist.add(NewTRecord);
                HashMapLVL.put(Mark, Recordlist);
            }
        }
        else if(managerID.startsWith("DDO")){

            char Mark;
            Mark = lastName.charAt(0);
            if(HashMapDDO.containsKey(Mark)){
                Recordlist = HashMapDDO.get(Mark);
                Recordlist.add(NewTRecord);
                HashMapDDO.replace(Mark, Recordlist);

            }
            else{
                Recordlist.add(NewTRecord);
                HashMapDDO.put(Mark, Recordlist);
            }
        }
        else{
            System.out.println("Access Deny!(ManagerID is invalid)");
            return false;
        }

        String writeInLog = "ManagerID: " + managerID + "\n" +
                "Create Teacher Record." + "\n" +
                "Name: " + firstName + " " + lastName + "\n" +
                "Address: " + Address + " " + "\n" +
                "Phone: " + Phone + " " + "\n" +
                "Specialization: " + Specialization + " " + "\n" +
                "Location: " + Location + " " + "\n" +
                "Time: " + getTime() + " " + "\n" + "\n";
        writeLog(writeInLog);
        save();

        return true;
    }

    private void writeLog(String writeInLog) {
    }

    /**
     * Determine whether student records can be created
     * @param managerID managerID
     * @param firstName firstName
     * @param lastName lastName
     * @param CoursesRegistered CoursesRegistered
     * @param Status Status
     * @param StatusDate StatusDate
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean createSRecord(String managerID, String firstName, String lastName, String CoursesRegistered, String Status, String StatusDate)   {

        if(!(Status.equals("active")||Status.equals("inactive"))){
            System.out.println("The Status is invalid.");
            return false;
        }
        StudentRecord NewSRecord = new StudentRecord(firstName, lastName, CoursesRegistered, Status, StatusDate);
        MTLcount++;
        NewSRecord.setRecordID(MTLcount);
        ArrayList<Record> Recordlist = new ArrayList<>();

        if(managerID.startsWith("MTL")){
            char Mark;
            Mark = lastName.charAt(0);
            if(HashMapMTL.containsKey(Mark)){
                Recordlist = HashMapMTL.get(Mark);
                Recordlist.add(NewSRecord);
                HashMapMTL.replace(Mark, Recordlist);

            }
            else{
                Recordlist.add(NewSRecord);
                HashMapMTL.put(Mark, Recordlist);
            }
        }

        else if(managerID.startsWith("LVL")){

            char Mark;
            Mark = lastName.charAt(0);
            if(HashMapLVL.containsKey(Mark)){
                Recordlist = HashMapLVL.get(Mark);
                Recordlist.add(NewSRecord);
                HashMapLVL.replace(Mark, Recordlist);

            }
            else{
                Recordlist.add(NewSRecord);
                HashMapLVL.put(Mark, Recordlist);
            }
        }
        else if(managerID.startsWith("DDO")){

            char Mark;
            Mark = lastName.charAt(0);
            if(HashMapDDO.containsKey(Mark)){
                Recordlist = HashMapDDO.get(Mark);
                Recordlist.add(NewSRecord);
                HashMapDDO.replace(Mark, Recordlist);

            }
            else{
                Recordlist.add(NewSRecord);
                HashMapDDO.put(Mark, Recordlist);
            }
        }
        else{
            System.out.println("Access Deny!(ManagerID is invalid)");
            return false;
        }

        String writeInLog = "ManagerID: " + managerID + "\n" +
                "Create Student Record." + "\n" +
                "Name: " + firstName + " " + lastName + "\n" +
                "CoursesRegister: " + CoursesRegistered + " " + "\n" +
                "Status: " + Status + " " + "\n" +
                "StatusDate: " + StatusDate + " " + "\n" +
                "Time: " + getTime() + " " + "\n" + "\n";
        this.writeLog(writeInLog);
        save();

        return true;

    }

    //Determine whether the teacher record can be edited or changed
    @Override
    public boolean editRecord(String managerID, String recordID, String fieldName, String newValue)   {

        Collection<ArrayList<Record>> allRecord = new ArrayList<>();
        int mark = 0;
        boolean result = false;
        Record target = null;
        if(managerID.startsWith("MTL")){

            allRecord = HashMapMTL.values();

            for(ArrayList<Record> recordList : allRecord){
                for(Record record : recordList){

                    if (record.getID().equals(recordID)){
                        target = record;
                        mark = 1;
                    }
                    break;
                }
                if(mark == 1){
                    break;
                }
            }
            if(target != null){
                if(target instanceof TeacherRecord){
                    synchronized (target) {
                        result = ((TeacherRecord) target).changeValue(fieldName, newValue);
                        System.out.println(target);
                    }
                }
                else {
                    synchronized (target) {

                        result = ((StudentRecord) target).changeValue(fieldName, newValue);
                        System.out.println(target);
                    }
                }
                String writeInLog = "Edit Record." + "\n" +
                        "ManagerID: " + managerID + "\n" +
                        "RecordID: " + target.getID() + "\n" +
                        "fieldName: " + fieldName + " " + "\n" +
                        "newValue: " + newValue + " " + "\n" +
                        "Time: " + getTime() + " " + "\n" + "\n";
                writeLog(writeInLog);
                save();
            }
            else{
                System.out.println("No Record.");
                result = false;
            }
        }


        else if(managerID.startsWith("LVL")){
            allRecord = HashMapLVL.values();

            for(ArrayList<Record> recordList : allRecord){
                for(Record record : recordList){

                    if (record.getID().equals(recordID)){
                        target = record;
                        mark = 1;
                    }
                    break;
                }
                if(mark == 1){
                    break;
                }
            }
            if(target != null){
                if(target instanceof TeacherRecord){
                    synchronized (target) {
                        result = ((TeacherRecord) target).changeValue(fieldName, newValue);
                        System.out.println(target);
                    }
                }
                else {
                    synchronized (target) {
                        result =  ((StudentRecord) target).changeValue(fieldName, newValue);
                        System.out.println(target);
                    }
                }
                String writeInLog = "Edit Record." + "\n" +
                        "ManagerID: " + managerID + "\n" +
                        "RecordID: " + target.getID() + "\n" +
                        "fieldName: " + fieldName + " " + "\n" +
                        "newValue: " + newValue + " " + "\n" +
                        "Time: " + getTime() + " " + "\n" + "\n";
                writeLog(writeInLog);
                save();
            }
            else{
                System.out.println("No Record.");
                result = false;
            }


        }


        else if (managerID.startsWith("DDO")){

            allRecord = HashMapDDO.values();

            for(ArrayList<Record> recordList : allRecord){
                for(Record record : recordList){

                    if (record.getID().equals(recordID)){
                        target = record;
                        mark = 1;
                    }
                    break;
                }
                if(mark == 1){
                    break;
                }
            }
            if(target != null){
                if(target instanceof TeacherRecord){
                    synchronized (target) {
                        result = ((TeacherRecord) target).changeValue(fieldName, newValue);
                        System.out.println(target);
                    }
                }
                else {
                    synchronized (target) {
                        result = ((StudentRecord) target).changeValue(fieldName, newValue);
                        System.out.println(target);
                    }
                }
                String writeInLog = "Edit Record." + "\n" +
                        "ManagerID: " + managerID + "\n" +
                        "RecordID: " + target.getID() + "\n" +
                        "fieldName: " + fieldName + " " + "\n" +
                        "newValue: " + newValue + " " + "\n" +
                        "Time: " + getTime() + " " + "\n" + "\n";
                writeLog(writeInLog);
                save();
            }
            else{
                System.out.println("No Record.");
                result = false;
            }

        }

        return result;
    }

    //Print the record to the server in the corresponding region
    @Override
    public boolean printRecord(String managerID)  {

        ArrayList<Record> Recordlist = new ArrayList<>();
        if(managerID.startsWith("MTL")){

            for(char key: HashMapMTL.keySet()) {

                System.out.print("\n" + key + ", ");
                Recordlist = HashMapMTL.get(key);
                for (int i = 0; i < Recordlist.size(); i++) {
                    System.out.print("{ID:" + Recordlist.get(i).getID() + " Name: " + Recordlist.get(i).getName() + "} ");
                }
                System.out.println("\n");
            }

        }
        else if(managerID.startsWith("LVL")){

            for(char key: HashMapLVL.keySet()) {

                System.out.print("\n" + key + ", ");
                Recordlist = HashMapLVL.get(key);
                for (int i = 0; i < Recordlist.size(); i++) {
                    System.out.print("{ID:" + Recordlist.get(i).getID() + " Name: " + Recordlist.get(i).getName() + "} ");
                }
                System.out.println("\n");
            }
        }
        else if(managerID.startsWith("DDO")){

            for(char key: HashMapDDO.keySet()) {

                System.out.print("\n" + key + ", ");
                Recordlist = HashMapDDO.get(key);
                for (int i = 0; i < Recordlist.size(); i++) {
                    System.out.print("{ID:" + Recordlist.get(i).getID() + " Name: " + Recordlist.get(i).getName() + "} ");
                }
                System.out.println("\n");
            }

        }

        return true;
    }

    @Override
    public boolean transferRecord(String managerID, String recordID, String remoteCenterServerName) {
        return false;
    }

    //Get the number of records of all servers（include current server） from the current server
    @Override
    public String getRecordCounts(){
        String sendStr = "MTL " + String.valueOf(MTLcount);
        return sendStr;
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
    public void save(){
        try {
            FileOutputStream l_saveFile = null;
            l_saveFile = new FileOutputStream(FilePath + "\\" + "LogFile" + "\\" + "MTLFile" + "\\" + "MTLServer" + ".txt");
            ObjectOutputStream l_Save = new ObjectOutputStream(l_saveFile);
            l_Save.writeObject(this);
            l_Save.flush();
            l_Save.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("write object success!");
    }
}
