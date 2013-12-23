//we use the Design Pattern : Singleton

package d_place;

import java.sql.*;

public class d_place {
	//
	public static void main(String[] args)
	{
		//create a new object
		AIR_info air = AIR_info.getObject();
		//connect to table air_info 
		air.connect_db();
		//updata air_info
		air.updata_air_info();		
		//close connect table air_info
		air.close_db();
		//air.show_info(air_path);
		
	}
}
