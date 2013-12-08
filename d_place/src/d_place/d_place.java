//we use the Design Pattern : Singleton

package d_place;

import java.io.IOException;
import org.json.JSONException;

public class d_place {
	//
	public static void main(String[] args) throws IOException, JSONException
	{
		//set the download url
		String air_url = "http://opendata.epa.gov.tw/ws/Data/AQXDaily/?$orderby=MonitorDate%20desc&$skip=0&$top=1000&format=json";
		//set the new file save path
		String air_path = "AIR_info.json";
		//create a new object
		AIR_info air = AIR_info.getObject();
		//download the air information
		air.download_info(air_url , air_path);
		//air.show_info(air_path);
		
	}
}
