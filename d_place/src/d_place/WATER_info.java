package d_place;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

import org.json.*;

public class WATER_info {

	private static WATER_info current_air_info = new WATER_info();
	// path of water_info json file
	private static String path = "WATER_info.json";
	// URL of water_info json file
	String water_url = "http://opendata.epa.gov.tw/ws/Data/WQXDam/?$orderby=SiteName&$skip=0&$top=1000&format=json";

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://210.61.10.89:9999/Team11";
	// database user and passwd
	static final String USER = "Team11";
	static final String PASS = "2013postgres";
	//
	private static Connection conn = null;

	// return object
	public static WATER_info getObject() {
		return current_air_info;
	}

	// class constructor
	public WATER_info() {
	}

	// get connect to database
	public void connect_db() {
		try {
			Class.forName(JDBC_DRIVER).newInstance();

			// connect to database
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Team11 database connected");

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close_db() {
		try {
			Class.forName(JDBC_DRIVER).newInstance();

			// closed
			conn.close();
			System.out.println("Team11 database closed");
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// download air information json file
	public void download_info(String url) {
		try {
			// use bufferedrwader get buffer length and get the json with UTF-8
			BufferedReader in = new BufferedReader(new InputStreamReader(
					(new URL(url).openStream()), "UTF-8"));
			// write the buffer in json file
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			char[] cbuf = new char[255];
			// get data and write data
			for (int length; (length = in.read(cbuf)) > 0; out.write(cbuf, 0,
					length))
				;
			// close reader and writer
			in.close();
			out.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("download finish");

	}

	// show all air information
	public void show_info(String path) {
		// read json
		JSONArray air_info_json;
		try {
			air_info_json = new JSONArray(new JSONTokener(new FileReader(
					new File(path))));

			JSONObject air_object;
			// print all json data
			for (int i = 0; i < air_info_json.length(); i++) {
				air_object = air_info_json.getJSONObject(i);
				System.out.println(air_object.get("SiteName") + "  "
						+ air_object.get("MonitorDate") + "  "
						+ air_object.get("PSI"));
			}
		} catch (FileNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// updata air information in local air data
	public void updata_air_info() {
		this.connect_db();
		
		System.out.println("---begin updata air_info");

		// download json file
		this.download_info(water_url);

		// read json
		JSONArray water_info_json;
		JSONObject water_object;
		
		int new_data = 0;
		try {
			water_info_json = new JSONArray(new JSONTokener(new FileReader(
					new File(path))));
			for (int i = 0; i < water_info_json.length(); i++) {
				water_object = water_info_json.getJSONObject(i);
				// find the data is always in table air_info
				String select_sql = "SELECT * FROM water_info WHERE "
						+ "\"SiteId\" = '" + water_object.getInt("SiteId")
						+ "' AND " + "\"MonitorDate\" = '"
						+ water_object.get("MonitorDate") + "'";
				// System.out.println(sql);
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(select_sql);
				// have not new data to updata
				if (rs.next()) {
					break;
				}
				// hava new data to updata
				else {
					String insert_sql = "INSERT INTO water_info VALUES ("
							+ water_object.getInt("SiteId") + ","
							+ "'" + water_object.get("SiteName")+ "',"
							+ "'" + water_object.get("MonitorDate")+ "',"
							+ water_object.getInt("PSI") + ")";
					st.executeUpdate(insert_sql);
					new_data++;
				}
				rs.close();
				st.close();
			}
			if(new_data == 0)
				System.out.println("have not new data to updata");
			else
				System.out.println("have " + new_data + " data to updata");

			System.out.println("---finish updata air_info");
			this.close_db();
		} catch (FileNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}