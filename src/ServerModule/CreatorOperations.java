package ServerModule;


/**
* ServerModule/CreatorOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从Server.idl
* 2021年6月30日 星期三 下午04时43分45秒 ADT
*/

public interface CreatorOperations 
{
  boolean createTRecord (String managerID, String firstName, String lastName, String Address, String Phone, String Specialization, String Location);
  boolean createSRecord (String managerID, String firstName, String lastName, String CoursesRegistered, String Status, String StatusDate);
  boolean editRecord (String managerID, String recordID, String fieldName, String newValue);
  boolean printRecord (String ManagerID);
  boolean transferRecord (String managerID, String recordID, String remoteCenterServerName);
  String getRecordCounts(String managerID);
} // interface CreatorOperations
