//we use the Design Pattern : Singleton
//�j���|�Ψ쪺class �� �Ů��T , �����T , �M ���]  ���}�g�b3�Ӥ��P��class��
//�u�n��T��s�M���o��T���|�b�o3��lcass���i��
//�ӳo3��class�b�o��project�O�W�@�L�G��
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
