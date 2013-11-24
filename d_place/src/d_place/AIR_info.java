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


import org.json.*;
public class AIR_info {

	public void download_info(String url , String path)throws IOException, ConnectException
	{
	    InputStream is = new URL(url).openConnection().getInputStream();
	    
	   // FileOutputStream fos = new FileOutputStream(path);
	
	    //fos.close();
	    /*
	    StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line = bufferedReader.readLine();
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        
        while(line != null){
            inputStringBuilder.append(line);inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
            if(line != null)
            bw.write(line);
        }
        bw.flush();
        bw.close();   
	    
	    is.close();
	    */
	    try {
            
            BufferedReader in = new BufferedReader(new InputStreamReader((new URL(url).openStream()), "UTF-8"));
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            char[] cbuf=new char[255];
            while ((in.read(cbuf)) != -1) {
                out.write(cbuf);
            }
            in.close();
            out.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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