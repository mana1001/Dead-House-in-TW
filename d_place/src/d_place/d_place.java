//we use the Design Pattern : Simple Factory

package d_place;

import java.io.IOException;
import java.net.ConnectException;

import org.json.JSONException;

public class d_place {
	//
	public static void main(String[] args) throws IOException, JSONException
	{
		String air_url = "http://opendata.epa.gov.tw/ws/Data/AQXDaily/?$orderby=MonitorDate%20desc&$skip=0&$top=1000&format=json";
		String air_path = "C:/Users/Timuncle/Desktop/AIR_info.json";
		AIR_info air = new AIR_info();
		
		air.download_info(air_url , air_path);
		//air.show_info(air_path);
		
	}
}
