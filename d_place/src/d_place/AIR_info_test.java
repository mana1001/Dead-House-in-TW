/*
READ ME 
	execute: Run as Junit test
	test for	(1)down load data
			(2)read json file
 */
package d_place;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;
import org.json.*;
import org.json.JSONException;

public class AIR_info_test {

	private static AIR_info air = new AIR_info();
	
	@Before
	public void setUp() throws Exception {	
	}

	@Test
	public void testDownload_info() throws ConnectException, IOException, JSONException {
	String air_url = "http://opendata.epa.gov.tw/ws/Data/AQXDaily/?$orderby=MonitorDate%20desc&$skip=0&$top=1000&format=json";
	String air_path = "AIR_info.json";
	//test (1)
	air.download_info(air_url, air_path);
	//test (2)
	air.show_info(air_path);
	}
	


}
