package RecordFile;


import java.io.Serializable;

public class Record implements Serializable {

    String recordID;
    private String firstName;
    private String lastName;

    public Record(String firstName,String lastName){
        this.firstName=firstName;
        this.lastName=lastName;

    }

    public String getID(){
        return this.recordID;
    }

    public String getName(){
        String Name = this.firstName + " " + this.lastName;
        return Name;
    }
    public String getLastName(){
        String Name = this.lastName;
        return Name;
    }
}