//we use the Design Pattern : Singleton

package d_place;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class d_place {
	public int i = 0;

	//
	public static void main(String[] args) {
		// set timer to update air and water information
		// every 60 Second to update
		Timer timer = new Timer();
		timer.schedule(new RemindTask(), 0, 600000);
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
		
		
		File file;
		file = new File("output.html");
		String head = "<html><head><title>output result</title></head><body>";
		String tale = "</body></html>";
		String line;
		try (FileOutputStream fop = new FileOutputStream(file)) {
			 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = head.getBytes();
			fop.write(contentInBytes);
			
			Iterator it = AirResult.entrySet().iterator();
			line = "<h1>air danger (pollution * air cause rate)</h1> ";
			contentInBytes = line.getBytes();
		    fop.write(contentInBytes);
			while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        line = pairs.getKey() + " = " + pairs.getValue() + "<br>";
		        contentInBytes = line.getBytes();
		        fop.write(contentInBytes);
		    }
			
			line = "<h1>water danger (pollution * air cause rate)</h1> ";
			contentInBytes = line.getBytes();
		    fop.write(contentInBytes);
			  it = WaterResult.entrySet().iterator();
			while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        line =pairs.getKey() + " = " + pairs.getValue()+ "<br>";
		        contentInBytes = line.getBytes();
		        fop.write(contentInBytes);
		    }
			
			
			contentInBytes = tale.getBytes();
			fop.write(contentInBytes);
			
			fop.flush();
			fop.close();
 
			
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
