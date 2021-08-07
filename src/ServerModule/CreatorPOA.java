package ServerModule;


import java.io.Serializable;

/**
* ServerModule/CreatorPOA.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从Server.idl
* 2021年6月30日 星期三 下午04时43分45秒 ADT
*/

public abstract class CreatorPOA extends org.omg.PortableServer.Servant
 implements ServerModule.CreatorOperations, org.omg.CORBA.portable.InvokeHandler, Serializable
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("createTRecord", new java.lang.Integer (0));
    _methods.put ("createSRecord", new java.lang.Integer (1));
    _methods.put ("editRecord", new java.lang.Integer (2));
    _methods.put ("printRecord", new java.lang.Integer (3));
    _methods.put ("transferRecord", new java.lang.Integer (4));
    _methods.put ("getRecordCounts", new java.lang.Integer (5));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // ServerModule/Creator/createTRecord
       {
         String managerID = in.read_string ();
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String Address = in.read_string ();
         String Phone = in.read_string ();
         String Specialization = in.read_string ();
         String Location = in.read_string ();
         boolean $result = false;
         $result = this.createTRecord (managerID, firstName, lastName, Address, Phone, Specialization, Location);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 1:  // ServerModule/Creator/createSRecord
       {
         String managerID = in.read_string ();
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String CoursesRegistered = in.read_string ();
         String Status = in.read_string ();
         String StatusDate = in.read_string ();
         boolean $result = false;
         $result = this.createSRecord (managerID, firstName, lastName, CoursesRegistered, Status, StatusDate);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 2:  // ServerModule/Creator/editRecord
       {
         String managerID = in.read_string ();
         String recordID = in.read_string ();
         String fieldName = in.read_string ();
         String newValue = in.read_string ();
         boolean $result = false;
         $result = this.editRecord (managerID, recordID, fieldName, newValue);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 3:  // ServerModule/Creator/printRecord
       {
         String ManagerID = in.read_string ();
         boolean $result = false;
         $result = this.printRecord (ManagerID);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 4:  // ServerModule/Creator/transferRecord
       {
         String managerID = in.read_string ();
         String recordID = in.read_string ();
         String remoteCenterServerName = in.read_string ();
         boolean $result = false;
         $result = this.transferRecord (managerID, recordID, remoteCenterServerName);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 5:  // ServerModule/Creator/getRecordCounts
       {
         String ManagerID = in.read_string ();
         String $result = null;
         $result = this.getRecordCounts (ManagerID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ServerModule/Creator:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Creator _this() 
  {
    return CreatorHelper.narrow(
    super._this_object());
  }

  public Creator _this(org.omg.CORBA.ORB orb) 
  {
    return CreatorHelper.narrow(
    super._this_object(orb));
  }


} // class CreatorPOA
