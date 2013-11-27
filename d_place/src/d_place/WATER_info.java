package d_place;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
public class WATER_info {

	public void download_info(String url , String path)throws IOException, ConnectException
	{
	    try {
            //use bufferedrwader get buffer length and get the json with UTF-8
            BufferedReader in = new BufferedReader(new InputStreamReader((new URL(url).openStream()), "UTF-8"));
            //write the buffer in json file
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            char[] cbuf=new char[255];
            //get data and write data
            for (int length; (length = in.read(cbuf)) > 0; out.write(cbuf, 0, length));
            //close reader and writer
            in.close();
            out.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    System.out.println("download finish");
	    
	}
}