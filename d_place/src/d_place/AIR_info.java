package d_place;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.URL;

import org.json.*;
public class AIR_info {

	public void download_info(String url , String path)throws IOException, ConnectException
	{
	    InputStream is = new URL(url).openConnection().getInputStream();
	    byte[] buffer = new byte[1024];
	   // FileOutputStream fos = new FileOutputStream(path);
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "ASCII"));
	    for (int length; (length = is.read(buffer)) > 0; writer.write(buffer.toString()));
	    //fos.close();
	    writer.close();
	    is.close();
	    System.out.println("download finish");
	    
	}
	public void show_info(String path) throws FileNotFoundException, JSONException
	{
		JSONArray air_info = new JSONArray(new JSONTokener(new FileReader(new File(path))));
		JSONObject air_object ;
		for(int i = 0 ; i < air_info.length() ; i ++)
		{
			air_object = air_info.getJSONObject(i);
			if(air_object.get("SiteName").equals("³Á¼d"))
			System.out.println(air_object.get("SiteName") + "  "+ air_object.get("MonitorDate") + "  " + air_object.get("PSI"));
		}
	}
}