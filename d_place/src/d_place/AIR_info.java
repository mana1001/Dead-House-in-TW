package d_place;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;


import java.util.HashMap;
import java.util.Map;

import org.json.*;
public class AIR_info {

	//all air information <Date , Local , PSI>
	private Map<String, HashMap<String, Integer>> air_info;
	//class construction
	AIR_info()
	{
		air_info = new HashMap<String ,HashMap<String , Integer>>();
		this.read_info();
	}
	//read local air information
	private void read_info()
	{
		//first time to read air information
		if(air_info.isEmpty())
		{

			
		}
	}
	
	//download air information json file
	public void download_info(String url , String path)throws IOException, ConnectException
	{
		//download air_info.json with url
	    InputStream is = new URL(url).openConnection().getInputStream();
	    try {
            //use bufferedrwader get buffer length and get the json with UTF-8
            BufferedReader in = new BufferedReader(new InputStreamReader((new URL(url).openStream()), "UTF-8"));
            //write the buffer in json file
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            char[] cbuf=new char[255];
            //get data and write data
            for (int length; (length = in.read(cbuf)) > 0; out.write(cbuf, 0, length));
            in.close();
            out.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    System.out.println("download finish");
	    
	}
	//show all air information
	public void show_info(String path) throws FileNotFoundException, JSONException
	{
		//read json
		JSONArray air_info_json = new JSONArray(new JSONTokener(new FileReader(new File(path))));
		JSONObject air_object ;
		//print all json data
		for(int i = 0 ; i < air_info_json.length() ; i ++)
		{
			air_object = air_info_json.getJSONObject(i);
			System.out.println(air_object.get("SiteName") + "  "+ air_object.get("MonitorDate") + "  " + air_object.get("PSI"));
		}
	}
	//updata air information in local air data
	public void updata_air_info()
	{
		
	
	}
}