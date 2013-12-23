//we use the Design Pattern : Singleton

package d_place;

import java.sql.*;
import java.util.Timer;

public class d_place {
	//
	public static void main(String[] args)
	{
		//create a new object
		AIR_info air = AIR_info.getObject();
		WATER_info water = WATER_info.getObject();
		//updata air_info
		//air.updata_air_info();	

	}
}
