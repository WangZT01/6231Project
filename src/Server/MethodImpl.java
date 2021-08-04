package Server;


import RecordFile.Record;
import RecordFile.StudentRecord;
import RecordFile.TeacherRecord;
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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The server implement the remote interface.
 * Must inherit UnicastRemoteObject to allow JVM to create remote stubs/proxy
 */
public class MethodImpl extends CreatorPOA implements Serializable{

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

    File loggingFile = new File("");
    String FilePath = loggingFile.getAbsolutePath();
    File loggingFileMTL = new File( FilePath + "\\" + "LogFile" + "\\" + "MTLFile"+ "\\" + "MTLLog" +".txt");
    File loggingFileLVL = new File( FilePath + "\\" + "LogFile" + "\\" + "LVLFile"+ "\\" + "LVLLog" +".txt");
    File loggingFileDDO = new File( FilePath + "\\" + "LogFile" + "\\" + "DDOFile"+ "\\" + "DDOLog" +".txt");

    int LVLcount = 0;
    int MTLcount = 0;
    int DDOcount = 0;
    public String name = "NULL";

    static HashMap<String, Integer> ServerPort = new HashMap<String, Integer>(){};
    static {
        ServerPort.put("MTL", 5053);
        ServerPort.put("LVL", 5052);
        ServerPort.put("DDO", 5051);
    }

    static HashMap<String, Integer> clientPort = new HashMap<String, Integer>(){};
    static {
        clientPort.put("MTL", 6053);
        clientPort.put("LVL", 6052);
        clientPort.put("DDO", 6051);
    }


    public MethodImpl() throws RemoteException {

        //super();
        load("LVL");
        load("DDO");
        load("MTL");
        this.name = name;
    }

    /**
     * Write the information to the logfile.
     * @param log the Operation details.
     */
    public void writeLog(String log, File file){
        if(!file.exists())
            return;
        try {
            synchronized (file) {
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(log);
                bufferedWriter.newLine();
                bufferedWriter.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

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

        ArrayList<Record> Recordlist = new ArrayList<>();

        String writeInLog = "ManagerID: " + managerID + "\n" +
                "Create Student Record." + "\n" +
                "Name: " + firstName + " " + lastName + "\n" +
                "Address: " + Address + " " + "\n" +
                "Phone: " + Phone + " " + "\n" +
                "Specialization: " + Specialization + " " + "\n" +
                "Location: " + Location + " " + "\n" +
                "Time: " + getTime() + " " + "\n" + "\n";

        if(managerID.startsWith("MTL")){

            MTLcount = 1 + getRecordCountsByInt(HashMapMTL);
            NewTRecord.setRecordID(MTLcount);
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
            writeLog(writeInLog, loggingFileMTL);
            save("MTL");

        }
        else if(managerID.startsWith("LVL")){

            LVLcount = 1 + getRecordCountsByInt(HashMapLVL);
            NewTRecord.setRecordID(LVLcount);
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
            writeLog(writeInLog, loggingFileLVL);
            save("LVL");
        }
        else if(managerID.startsWith("DDO")){

            DDOcount = 1 + getRecordCountsByInt(HashMapDDO);
            NewTRecord.setRecordID(DDOcount);
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
            writeLog(writeInLog, loggingFileDDO);
            save("DDO");
        }
        else{
            System.out.println("Access Deny!(ManagerID is invalid)");
            return false;
        }

        return true;
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
        System.out.println("toSTRING"+ NewSRecord.toString());
        ArrayList<Record> Recordlist = new ArrayList<>();

        String writeInLog = "ManagerID: " + managerID + "\n" +
                "Create Student Record." + "\n" +
                "Name: " + firstName + " " + lastName + "\n" +
                "CoursesRegister: " + CoursesRegistered + " " + "\n" +
                "Status: " + Status + " " + "\n" +
                "StatusDate: " + StatusDate + " " + "\n" +
                "Time: " + getTime() + " " + "\n" + "\n";

        if(managerID.startsWith("MTL")){

            MTLcount = 1 + getRecordCountsByInt(HashMapMTL);
            NewSRecord.setRecordID(MTLcount);
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
            writeLog(writeInLog, loggingFileMTL);
            save("MTL");
        }

        else if(managerID.startsWith("LVL")){

            LVLcount = getRecordCountsByInt(HashMapLVL) + 1;
            NewSRecord.setRecordID(LVLcount);
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
            writeLog(writeInLog, loggingFileLVL);
            save("LVL");
        }
        else if(managerID.startsWith("DDO")){

            DDOcount = getRecordCountsByInt(HashMapDDO) + 1;
            NewSRecord.setRecordID(DDOcount);
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
            writeLog(writeInLog, loggingFileDDO);
            save("DDO");
        }
        else{
            System.out.println("Access Deny!(ManagerID is invalid)");
            return false;
        }
        save("LVL");

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
                writeLog(writeInLog, loggingFileMTL);
                save("MTL");
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
                writeLog(writeInLog, loggingFileLVL);
                save("LVL");
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
                writeLog(writeInLog, loggingFileDDO);
                save("DDO");
            }
            else{
                System.out.println("No Record.");
                result = false;
            }

        }

        return result;
    }

    @Override
    public boolean transferRecord(String managerID, String recordID, String remoteCenterServerName) {
        Record targetRecord=null;
        Collection<ArrayList<Record>> arrayListsSet;

        if(managerID.trim().startsWith("MTL")){
            arrayListsSet=HashMapMTL.values();
        }
        else if(managerID.trim().startsWith("DDO")){
            arrayListsSet=HashMapDDO.values();
        }
        else{
            arrayListsSet=HashMapLVL.values();
        }

        for(ArrayList<Record> recordArrayListSet : arrayListsSet){
            for(Record record:recordArrayListSet){
                if(record.getID().equalsIgnoreCase(recordID.trim()))
                    targetRecord=record;
                break;
            }
        }

        if(targetRecord==null){
            //log
            String log=(new Date().toString()+" - "+managerID+" - transferring the record - "+recordID+" - "+
                    "Error:record not exist");

            if(managerID.trim().startsWith("MTL")){
                writeLog(log,loggingFileMTL);
            }
            else if(managerID.trim().startsWith("DDO")){
                writeLog(log,loggingFileDDO);
            }
            else{
                writeLog(log,loggingFileLVL);
            }
            return false;
        }
        else{
            //remove
            ArrayList<Record> theArrayList = null;
            synchronized (targetRecord){
                if(managerID.trim().startsWith("MTL")){
                    HashMapMTL.get(targetRecord.getLastName().charAt(0)).remove(targetRecord);
                    save("MTL");
                }
                else if(managerID.trim().startsWith("DDO")){
                    HashMapDDO.get(targetRecord.getLastName().charAt(0)).remove(targetRecord);
                    save("DDO");
                }
                else{
                    HashMapLVL.get(targetRecord.getLastName().charAt(0)).remove(targetRecord);
                    save("LVL");
                }
            }

            //add
            boolean flag = true;
            if(remoteCenterServerName.trim().startsWith("DDO")) {
                //storingRecord(targetRecord, HashMapDDO);
                //save("DDO");
                String sendRecord = recordJugdement(targetRecord);
                sendUdpMessageWithRet(sendRecord,5051);


            }
            else if(remoteCenterServerName.trim().startsWith("LVL")){
                //storingRecord(targetRecord,HashMapLVL);
                //save("LVL");
                String sendRecord = recordJugdement(targetRecord);
                sendUdpMessageWithRet(sendRecord,5052);

            }

            else if(remoteCenterServerName.trim().startsWith("MTL")){
                //storingRecord(targetRecord,HashMapMTL);
                //save("MTL");
                String sendRecord = recordJugdement(targetRecord);
                sendUdpMessageWithRet(sendRecord,5053);

            }
            else
                flag=false;
            //log
            if(flag){
                String log=(new Date().toString()+" - "+managerID+" - transferring the record - "+recordID+" - "+
                        "Success");
                if(managerID.startsWith("MTL")){
                    writeLog(log,loggingFileMTL);
                }
                else if(managerID.startsWith("DDO")){
                    writeLog(log,loggingFileDDO);
                }
                else{
                    writeLog(log,loggingFileLVL);
                }
            }
            else{
                String log=(new Date().toString()+" - "+managerID+" - transferring the record - "+recordID+" - "+
                        "Fail");
                if(managerID.trim().startsWith("MTL")){
                    writeLog(log,loggingFileMTL);
                }
                else if(managerID.trim().startsWith("DDO")){
                    writeLog(log,loggingFileDDO);
                }
                else{
                    writeLog(log,loggingFileLVL);
                }
            }
            return flag;
        }
    }

    //Print the record to the server in the corresponding region
    @Override
    public boolean printRecord(String managerID)   {

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

    //Get the number of records of all servers（include current server） from the current server
    @Override
    public String getRecordCounts()   {


        String sendStrLVL = null;
        String sendStrMTL = null;
        String sendStrDDO = null;
        if(this.name.equals("MTL")){
            int countMTL = getRecordCountsByInt(HashMapMTL);
            sendStrMTL = "MTL:" + String.valueOf(countMTL);
            sendStrLVL = sendUdpMessageWithRet("getCount",ServerPort.get("LVL"));
            sendStrDDO = sendUdpMessageWithRet("getCount",ServerPort.get("DDO"));

        }else if (this.name.equals("LVL")){
            int countLVL = getRecordCountsByInt(HashMapLVL);
            sendStrLVL = "LVL:" + String.valueOf(countLVL);
            sendStrDDO = sendUdpMessageWithRet("getCount",ServerPort.get("DDO"));
            sendStrMTL = sendUdpMessageWithRet("getCount",ServerPort.get("MTL"));

        }else if (this.name.equals("DDO")){
            int countDDO = getRecordCountsByInt(HashMapDDO);
            sendStrDDO = "DDO:" + String.valueOf(countDDO);
            sendStrMTL = sendUdpMessageWithRet("getCount",ServerPort.get("MTL"));
            sendStrLVL = sendUdpMessageWithRet("getCount",ServerPort.get("LVL"));


        }

        String sendStr = sendStrMTL + " " + sendStrLVL + " " + sendStrDDO;

        return sendStr;
    }

    public int getRecordCountsByInt(ConcurrentHashMap hashmap){

        int count = 0;
        ArrayList<Record> Recordlist = new ArrayList<>();
        for(Object key: hashmap.keySet()) {

            Recordlist = (ArrayList<Record>) hashmap.get(key);
            for (int i = 0; i < Recordlist.size(); i++) {
                count++;
            }
        }
        return count;
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
                l_saveFile = new FileOutputStream(FilePath + "\\" + "LogFile" + "\\" + "DDOFile" + "\\" + "DDOServer" + ".txt");
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

    /**
     * Load the Server.
     */
    public boolean load(String Location){

        boolean flag = false;

        try {

            ObjectInputStream l_ois = null;
            if(Location.equals("LVL")){
                l_ois = new ObjectInputStream(new FileInputStream(FilePath + "\\" + "LogFile" + "\\" + "LVLFile" + "\\" + "LVLServer" + ".txt"));
                this.HashMapLVL = (ConcurrentHashMap<Character, ArrayList<Record>>) l_ois.readObject();
                flag = true;
            }
            else if(Location.equals("MTL")){
                l_ois = new ObjectInputStream(new FileInputStream(FilePath + "\\" + "LogFile" + "\\" + "MTLFile" + "\\" + "MTLServer" + ".txt"));
                this.HashMapMTL = (ConcurrentHashMap<Character, ArrayList<Record>>) l_ois.readObject();
                flag = true;
            }
            else if(Location.equals("DDO")){
                l_ois = new ObjectInputStream(new FileInputStream(FilePath + "\\" + "LogFile" + "\\" + "DDOFile" + "\\" + "DDOServer" + ".txt"));
                this.HashMapDDO = (ConcurrentHashMap<Character, ArrayList<Record>>) l_ois.readObject();
                flag = true;
            }

        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println("The Map is Empty!");
        }
        return flag;
    }

    private void storingRecord(Record record,ConcurrentHashMap<Character,ArrayList<Record>> hashMap){
        // Get the first letter.
        char Mark;
        Mark = record.getLastName().charAt(0);
        ArrayList<Record> Recordlist = new ArrayList<>();

        //Put new record in the record list.
        if(hashMap.containsKey(Mark)){
            Recordlist = hashMap.get(Mark);
            Recordlist.add(record);
            hashMap.replace(Mark, Recordlist);

        }

        //Create new first letter key and the record list.
        else{
            Recordlist.add(record);
            hashMap.put(Mark, Recordlist);
        }
    }

    public String sendUdpMessage(String message, int serverPort) {
        String serverMsg = "";
        DatagramSocket clientSocket = null;
        try {
            int clientP = 0;
            clientP = clientPort.get(this.name);
            clientSocket = new DatagramSocket(clientP);
            byte[] sendData = new byte[1000];
            sendData = message.getBytes();
            InetAddress clientHost = InetAddress.getByName("127.0.0.1");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientHost, serverPort);
            clientSocket.send(sendPacket);

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
        return serverMsg;
    }

    public String sendUdpMessageWithRet(String message, int serverPort) {
        String recvStr = "";
        DatagramSocket clientSocket = null;
        try {


            clientSocket = new DatagramSocket();
            byte[] sendData = new byte[1000];
            sendData = message.getBytes();
            InetAddress clientHost = InetAddress.getByName("127.0.0.1");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientHost, serverPort);
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


    private String getCountForUDP(String recvStr) {

        String count = null;
        if(this.name.equals("MTL")){
            int countMTL = getRecordCountsByInt(HashMapMTL);
            count = "MTL " + String.valueOf(countMTL);
            return count;
        }
        if(this.name.equals("LVL")){
            int countLVL = getRecordCountsByInt(HashMapLVL);
            count = "LVL " + String.valueOf(countLVL);
            return count;
        }
        if(this.name.equals("DDO")){
            int countDDO = getRecordCountsByInt(HashMapDDO);
            count = "DDO " + String.valueOf(countDDO);
            return count;
        }
        return count;
    }

    private boolean transferForUDP(String recvStr) {
        String[] values = recvStr.split("&");
        boolean result = false;
        String SuperManager = this.name + "9999";
        if(values[1].startsWith("T")){
            result = this.createTRecord(SuperManager,values[2],values[3],values[4], values[5],values[6], values[7]);

        }
        else if (values[1].startsWith("S")){
            result = this.createSRecord(SuperManager,values[2],values[3],values[4], values[5],values[6]);
        }
        return result;
    }

    private String recordJugdement(Record record){
        String data = null;
        if(record.getID().startsWith("T")){
            System.out.println("TransferID :"+ record.getID());
            TeacherRecord Trecord = (TeacherRecord) record;
            data = "Transfer&" + Trecord.getID()
                    + "&" + Trecord.getFirstName() + "&"
                    + Trecord.getLastName() + "&"
                    + Trecord.getAddress() + "&"
                    + Trecord.getPhone() + "&"
                    + Trecord.getSpecialization() + "&"
                    + Trecord.getLocation();
        }
        else{
            StudentRecord Srecord = (StudentRecord) record;
            System.out.println("TransferID :"+ record.getID());
            data = "Transfer&" + Srecord.getID()
                    + "&" + Srecord.getFirstName() + "&"
                    + Srecord.getLastName() + "&"
                    + Srecord.getCoursesRegistered() + "&"
                    + Srecord.getStatus() + "&"
                    + Srecord.getStatusDate();
        }
        return data;
    }
}
