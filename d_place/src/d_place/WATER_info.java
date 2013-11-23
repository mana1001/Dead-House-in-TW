package d_place;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.io.InputStream;
import java.io.FileOutputStream;
public class WATER_info {

	public void download_info(String url , String path)throws IOException, ConnectException
	{
	    InputStream is = new URL(url).openConnection().getInputStream();
	    FileOutputStream fos = new FileOutputStream(path);
	    byte[] buffer = new byte[1024];
	    for (int length; (length = is.read(buffer)) > 0; fos.write(buffer, 0, length));
	    fos.close();
	    is.close();
	    System.out.println("download finish");
	}
}