package ManagerFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Manager {

    private String ManagerID;
    private File loggingFile = new File("");;


    public void setManagerID(String managerID) {
        ManagerID = managerID;
        String filePath = loggingFile.getAbsolutePath();
        loggingFile = new File(filePath + "/LogFile/ManagerLog/"+ ManagerID + ".txt");
    }

    public void writeLog(String log){
        if(!this.loggingFile.exists()){
            try {
                this.loggingFile.createNewFile();
                FileWriter fileWriter = new FileWriter(this.loggingFile, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(log);
                bufferedWriter.newLine();
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                synchronized (this.loggingFile) {
                    System.out.println("find");
                    FileWriter fileWriter = new FileWriter(this.loggingFile, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(log);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                    fileWriter.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}