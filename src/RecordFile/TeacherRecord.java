package RecordFile;

public class TeacherRecord extends Record{

    private static int idCounter=10000;

    String Address;
    String Phone;
    String Specialization;
    String Location;

    //constructor
    public TeacherRecord(String firstName, String lastName, String Address,
                         String Phone, String Specialization, String Location) {

        super(firstName, lastName);
        this.recordID="TR"+String.valueOf(idCounter);
        this.Address = Address;
        this.Phone = Phone;
        this.Location = Location;
        this.Specialization = Specialization;
    }

    public void setRecordID(int counter){
        this.recordID="TR"+String.valueOf(idCounter + counter);
    }

    public String getAddress() {
        return Address;
    }

    public String getPhone() {
        return Phone;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public String getLocation() {
        return Location;
    }

    //determin if the value can be changed. When the edit record is executed, the value of the corresponding fiel is updated according to the fieldName。
    public boolean changeValue(String fieldName, String newValue){

        if(fieldName.equalsIgnoreCase("address")){
            System.out.println("Old value:" + this.Address);
            this.Address=newValue;
            System.out.println("New value:" + this.Address);
            return true;
        }
        else if(fieldName.equalsIgnoreCase("phone")){
            System.out.println("Old value:" + this.Phone);
            this.Phone=newValue;
            System.out.println("New value:" + this.Phone);
            return true;
        }
        else if(fieldName.equalsIgnoreCase("location")){
            if(newValue.equals("mtl")||newValue.equals("lvl")||newValue.equals("ddo")){
                System.out.println("Old value:" + this.Location);
                this.Location=newValue;
                System.out.println("New value:" + this.Location);
                return true;
            }
            else
                System.out.println("The Location is invalid.(mtl/lvl/ddo)");
            return false;
        }
        else {
            return false;
        }
    }
}