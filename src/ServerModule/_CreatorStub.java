package ServerModule;


/**
* ServerModule/_CreatorStub.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从Server.idl
* 2021年6月30日 星期三 下午04时43分45秒 ADT
*/

public class _CreatorStub extends org.omg.CORBA.portable.ObjectImpl implements ServerModule.Creator
{

  public boolean createTRecord (String managerID, String firstName, String lastName, String Address, String Phone, String Specialization, String Location)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("createTRecord", true);
                $out.write_string (managerID);
                $out.write_string (firstName);
                $out.write_string (lastName);
                $out.write_string (Address);
                $out.write_string (Phone);
                $out.write_string (Specialization);
                $out.write_string (Location);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return createTRecord (managerID, firstName, lastName, Address, Phone, Specialization, Location        );
            } finally {
                _releaseReply ($in);
            }
  } // createTRecord

  public boolean createSRecord (String managerID, String firstName, String lastName, String CoursesRegistered, String Status, String StatusDate)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("createSRecord", true);
                $out.write_string (managerID);
                $out.write_string (firstName);
                $out.write_string (lastName);
                $out.write_string (CoursesRegistered);
                $out.write_string (Status);
                $out.write_string (StatusDate);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return createSRecord (managerID, firstName, lastName, CoursesRegistered, Status, StatusDate        );
            } finally {
                _releaseReply ($in);
            }
  } // createSRecord

  public boolean editRecord (String managerID, String recordID, String fieldName, String newValue)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("editRecord", true);
                $out.write_string (managerID);
                $out.write_string (recordID);
                $out.write_string (fieldName);
                $out.write_string (newValue);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return editRecord (managerID, recordID, fieldName, newValue        );
            } finally {
                _releaseReply ($in);
            }
  } // editRecord

  public boolean printRecord (String ManagerID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("printRecord", true);
                $out.write_string (ManagerID);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return printRecord (ManagerID        );
            } finally {
                _releaseReply ($in);
            }
  } // printRecord

  public boolean transferRecord (String managerID, String recordID, String remoteCenterServerName)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("transferRecord", true);
                $out.write_string (managerID);
                $out.write_string (recordID);
                $out.write_string (remoteCenterServerName);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return transferRecord (managerID, recordID, remoteCenterServerName        );
            } finally {
                _releaseReply ($in);
            }
  } // transferRecord

  public String getRecordCounts ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getRecordCounts", true);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getRecordCounts (        );
            } finally {
                _releaseReply ($in);
            }
  } // getRecordCounts

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ServerModule/Creator:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _CreatorStub
