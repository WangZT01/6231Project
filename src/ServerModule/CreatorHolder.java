package ServerModule;

/**
* ServerModule/CreatorHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从Server.idl
* 2021年6月30日 星期三 下午04时43分45秒 ADT
*/

public final class CreatorHolder implements org.omg.CORBA.portable.Streamable
{
  public ServerModule.Creator value = null;

  public CreatorHolder ()
  {
  }

  public CreatorHolder (ServerModule.Creator initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ServerModule.CreatorHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ServerModule.CreatorHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ServerModule.CreatorHelper.type ();
  }

}
