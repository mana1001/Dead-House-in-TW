//we use the Design Pattern : Singleton

package d_place;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class d_place {
	public int i = 0;
	private static AIR_info air = AIR_info.getObject();
	private static WATER_info water = WATER_info.getObject();

	//
	public static void main(String[] args) {
		// set timer to update air and water information
		// every 20 Second to update
		Timer timer = new Timer();
		timer.schedule(new RemindTask(), 0, 20000);
	}

}

class RemindTask extends TimerTask {
	private static AIR_info air = AIR_info.getObject();
	private static WATER_info water = WATER_info.getObject();

	@Override
	public void run() {
		// Update air_info
		air.update();
		// Update water_info
		water.update();
	}
}