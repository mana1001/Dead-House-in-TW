//we use the Design Pattern : Singleton

package d_place;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
		
		/*
		 *  SORT RESULT
		 * 
		 * **/
		List<Map.Entry<String, Double>> list_AirResult = new ArrayList<Map.Entry<String, Double>>(AirResult.entrySet());
		
		Collections.sort(list_AirResult, new Comparator<Map.Entry<String, Double>>()
				{  
				    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)
				    {
				        return (int)(o2.getValue() - o1.getValue());
				    }
				});
		
		List<Map.Entry<String, Double>> list_WaterResult = new ArrayList<Map.Entry<String, Double>>(WaterResult.entrySet());
		
		Collections.sort(list_WaterResult, new Comparator<Map.Entry<String, Double>>()
				{  
				    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)
				    {
				        return (int)(o2.getValue() - o1.getValue());
				    }
				});
		
		
		/*
		 * 
		 * 
		 * write result HTML
		 * 
		 * *****************/
		File file;
		file = new File("output.html");
		String head = "<html><head><title>output result</title></head><body>";
		String tale = "</body></html>";
		String line;
		try  {
			FileOutputStream fop = new FileOutputStream(file);
			
				/*  write UTF8 BOM mark  */
	            final byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
	            fop.write(bom);
	         
			
			BufferedWriter out = 
					new BufferedWriter(new OutputStreamWriter(fop,"UTF8"));
			
			byte[] contentInBytes = head.getBytes();
			fop.write(contentInBytes);
			
			
			 ListIterator<Map.Entry<String, Double>> litr = list_AirResult.listIterator();
			 Map.Entry<String, Double> entry;
			line = "<h1>air danger (pollution * air cause rate)</h1> ";

			out.write(line);
			while (litr.hasNext()) {
				entry= litr.next();
				if(entry.getValue() < 1)
					break;
		        line = entry.getKey() + " = " + entry.getValue() + "<br>";

		        out.write(line);
		    }
			
			
			 litr = list_WaterResult.listIterator();
			line = "<h1>water danger (pollution * air cause rate)</h1> ";

			out.write(line);

			while (litr.hasNext()) {
				entry = litr.next();
				
				if(entry.getValue() < 1)
					break;
		        line =entry.getKey() + " = " + entry.getValue()+ "<br>";

		        out.write(line);
		    }
			

			out.write(line);
			out.flush();
			out.close();

			
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
