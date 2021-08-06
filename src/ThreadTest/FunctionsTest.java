package ThreadTest;


import Server.MethodImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;

public class FunctionsTest {

    Server.MethodImpl methodImpl;
    File MTLFile = new File("");
    String FilePath = MTLFile.getAbsolutePath();

    /**
     * Loading the database in DDO.
     */
    @Before
    public void before(){
        try {
            methodImpl = new MethodImpl("MTL");
            methodImpl.name = "MTL";
            methodImpl.load("MTL");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * The test is finish.
     */
    @After
    public void after(){

        System.out.println("This test is OK.");

    }


    /**
     * Test for creating the teacher record.
     */
    @Test
    public void ServerTrecordTest(){
        boolean result;
        result = methodImpl.createTRecord(
                "MTL11111","firstName", "lastName",
                "Address", "Phone", "Specialization", "mtl");
        Assert.assertEquals(result, true);

        result = methodImpl.createTRecord(
                "MTL11111","firstName", "lastName",
                "Address", "Phone", "Specialization", "XXX");
        Assert.assertEquals(result, false);

    }

    /**
     * Test for creating the student record.
     */
    @Test
    public void ServerSrecordTest(){
        boolean result;
        result = methodImpl.createSRecord(
                "MTL11111","firstName", "lastName",
                "CoursesRegistered", "active", "StatusDate");
        Assert.assertEquals(result, true);

        result = methodImpl.createSRecord(
                "MTL11111","firstName", "lastName",
                "CoursesRegistered", "XXX", "StatusDate");
        Assert.assertEquals(result, false);

    }

    /**
     * Test for editing record.
     */
    @Test
    public void DDOServerEditrecordTest() throws RemoteException {
        boolean result;
        methodImpl.createSRecord(
                "MTL11111","firstName", "lastName",
                "CoursesRegistered", "active", "StatusDate");
        result = methodImpl.editRecord("MTL11111", "TR10001","Location","mtl");
        Assert.assertEquals(result, true);

    }

}
