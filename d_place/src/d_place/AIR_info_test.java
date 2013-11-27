/*
READ ME 
	execute: Run as Junit test
	test for down load data
 */

package d_place;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ConnectException;

import org.junit.Before;
import org.junit.Test;

public class AIR_info_test {

	private static AIR_info air = new AIR_info();
	
	@Before
	public void setUp() throws Exception {	
	}

	@Test
	public void testDownload_info() throws ConnectException, IOException {
	String air_url = "http://opendata.epa.gov.tw/ws/Data/AQXDaily/?$orderby=MonitorDate%20desc&$skip=0&$top=1000&format=json";
	String air_path = "C:/Users/Timuncle/Desktop/AIR_info.json";
	air.download_info(air_url, air_path);
	}

}
