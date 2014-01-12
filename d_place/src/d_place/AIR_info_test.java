/*
READ ME 
        execute: Run as Junit test
        test for        (1)down load data
                        (2)read json file
 */
package d_place;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class AIR_info_test {

	private static AIR_info air = AIR_info.getObject();
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDownload_info() throws ConnectException, IOException,
			JSONException {
		String air_url = "http://opendata.epa.gov.tw/ws/Data/AQXDaily/?$orderby=MonitorDate%20desc&$skip=0&$top=1000&format=json";
		air.download_info(air_url);
		JSONArray air_location_array = new JSONArray(new JSONTokener(new FileReader(
				new File("WATER_info.json"))));
		assertEquals(1000,air_location_array.length());
	}
	@Test
	public void test_connect_close_database() throws ConnectException, IOException,
			JSONException {
		//connect database
		air.connect_db();
		//close database
		air.close_db();
	}
	@Test
	public void test_GetPSI() throws ConnectException, IOException,
			JSONException, SQLException {
		String county = "雲林縣";
		air.connect_db();
		double result = air.GetPSI(county);
		if( result !=0 )
			System.out.println("PSI is not 0");
		air.close_db();
	}

}