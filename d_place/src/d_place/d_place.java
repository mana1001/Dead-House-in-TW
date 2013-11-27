//we use the Design Pattern : Singleton
//大概會用到的class 有 空氣資訊 , 水質資訊 , 和 死因  分開寫在3個不同的class中
//只要資訊更新和取得資訊都會在這3個lcass中進行
//而這3個class在這個project是獨一無二的
package d_place;

import java.io.IOException;
import java.net.ConnectException;

import org.json.JSONException;

public class d_place {
	//
	public static void main(String[] args) throws IOException, JSONException
	{
		//set the download url
		String air_url = "http://opendata.epa.gov.tw/ws/Data/AQXDaily/?$orderby=MonitorDate%20desc&$skip=0&$top=1000&format=json";
		//set the new file save path
		String air_path = "C:/Users/Timuncle/Desktop/AIR_info.json";
		//create a new object
		AIR_info air = new AIR_info();
		//download the air information
		air.download_info(air_url , air_path);
		//air.show_info(air_path);
		
	}
}
