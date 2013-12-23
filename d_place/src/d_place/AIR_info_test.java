/*
READ ME 
        execute: Run as Junit test
        test for        (1)down load data
                        (2)read json file
 */
package d_place;

import java.io.IOException;
import java.net.ConnectException;

import org.junit.Before;
import org.junit.Test;
import org.json.JSONException;

public class AIR_info_test {

	private static AIR_info air = AIR_info.getObject();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDownload_info() throws ConnectException, IOException,
			JSONException {
		String air_url = "http://opendata.epa.gov.tw/ws/Data/AQXDaily/?$orderby=MonitorDate%20desc&$skip=0&$top=1000&format=json";
		String air_path = "AIR_info.json";
		// test (1)
		air.download_info(air_url);
		// test (2)
		air.show_info(air_path);
	}
	@Test
	public void connect_close_database() throws ConnectException, IOException,
			JSONException {
		//connect database
		air.connect_db();
		//close database
		air.close_db();
	}

}