//we use the Design Pattern : Singleton

package d_place;

import java.sql.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class d_place {
	public int i = 0;

	//
	public static void main(String[] args) {
		// set timer to update air and water information
		// every 60 Second to update
		Timer timer = new Timer();
		timer.schedule(new RemindTask(), 0, 60000);
	}

}

class RemindTask extends TimerTask {
	private static AIR_info air = AIR_info.getObject();
	private static WATER_info water = WATER_info.getObject();
	private static death_info death = death_info.getObject();

	@Override
	public void run() {
		// Update air_info
		// air.update();
		// Update water_info
		// water.update();
		// Updata ranking

		HashMap<String, Double> AirResult = new HashMap<String, Double>();
		HashMap<String, Double> WaterResult = new HashMap<String, Double>();
		String[] county = death.get_death_info_county();
		double air_death_rate = 0;
		double water_death_rate = 0;
		double PSI = 0;
		double Carlson = 0;
		air.connect_db();
		water.connect_db();
		for (int i = 0; i < county.length; i++) {
			air_death_rate = death.get_air_death_rate(county[i]);
			water_death_rate = death.get_water_death_rate(county[i]);
			PSI = air.GetPSI(county[i]);
			Carlson = water.GetCarlson(county[i]);
			AirResult.put(county[i], PSI * air_death_rate * 100);
			WaterResult.put(county[i], Carlson * water_death_rate * 100);
		}
		air.close_db();
		water.close_db();
		

	}
}