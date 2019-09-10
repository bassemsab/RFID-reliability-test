package com.dgreat.rfid;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Tag {
	DbCon dbc = new DbCon();
	
	public boolean add(String rssi, String epc, long times,String added){
		boolean ret = false;
		try{
			Connection cn = dbc.connect();
			
			PreparedStatement ps = cn.prepareStatement("INSERT INTO `tags` ( rssi, epc, times,added)" +	"VALUES(?,?,?,?)");
				ps.setString(1,rssi);
				ps.setString(2,epc);
				ps.setLong(3,times);
				ps.setString(4,added);



				
			ret = ps.executeUpdate()==1;
			cn.close();
		}catch(Exception e){System.out.println(e); e.printStackTrace();}
		return ret;
	}
	
	public static void main(String[] args, String[] args2, long args3,String [] args4) {
		Tag t = new Tag();
		System.out.println("add Tag");
	//	System.out.println(t.add("1"));
	}
}
