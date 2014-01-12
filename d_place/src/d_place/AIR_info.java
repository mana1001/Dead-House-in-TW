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

public class AIR_info {

	private static AIR_info current_air_info = new AIR_info();
	// path of air_info json file
	private static String path = "AIR_info.json";
	// URL of air_info json file
	String air_url = "http://opendata.epa.gov.tw/ws/Data/AQXDaily/?$orderby=MonitorDate%20desc&$skip=0&$top=1000&format=json";

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://210.61.10.89:9999/Team11";
	// database user and passwd
	static final String USER = "Team11";
	static final String PASS = "2013postgres";
	//
	private static Connection conn = null;

	// return object
	public static AIR_info getObject() {
		return current_air_info;
	}

	// class constructor
	public AIR_info() {
	}

	// get connect to database
	public void connect_db() {
		try {
			Class.forName(JDBC_DRIVER).newInstance();

			// connect to database
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Team11 database table air_info connected");

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
			System.out.println("Team11 database table air_info closed");
			//System.out.println("");

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// download air information json file
	public void download_info(String url) {
		try {
			System.out.println("downloading.....");
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

	// download air location json file
	public void download_location() {
		String url = "http://opendata.epa.gov.tw/ws/Data/AQXSite/?$orderby=MonitorDate&$skip=0&$top=1000&format=json";
		try {
			// use bufferedrwader get buffer length and get the json with UTF-8
			BufferedReader in = new BufferedReader(new InputStreamReader(
					(new URL(url).openStream()), "UTF-8"));
			// write the buffer in json file
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"AIR_location.json"));
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
	public void update() {
		this.connect_db();

		System.out.println("---begin update air_info");

		// download json file
		this.download_info(air_url);

		// air_info json
		JSONArray air_info_json;
		JSONObject air_object;

		int new_data = 0;
		int PSI = 0;
		try {
			// read air_info json
			air_info_json = new JSONArray(new JSONTokener(new FileReader(
					new File(path))));
			System.out.println("updateing.....");
			for (int i = 0; i < air_info_json.length(); i++) {
				air_object = air_info_json.getJSONObject(i);
				// find the data is always in table air_info
				String select_sql = "SELECT * FROM air_info WHERE "
						+ "\"SiteId\" = '" + air_object.getInt("SiteId")
						+ "' AND " + "\"MonitorDate\" = '"
						+ air_object.get("MonitorDate") + "'";
				// System.out.println(sql);
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(select_sql);
				// have not new data to updata
				if (rs.next()) {
					break;
				}
				// hava new data to updata
				else {
					if (air_object.get("PSI").toString().equals(""))
						PSI = 0;
					else
						PSI = Integer
								.parseInt(air_object.get("PSI").toString());
					String insert_sql = "INSERT INTO air_info VALUES ("
							+ air_object.getInt("SiteId")
							+ ","
							+ "'"
							+ air_object.get("SiteName")
							+ "',"
							+ "'"
							+ air_object.get("MonitorDate")
							+ "',"
							+ PSI
							+ ","
							+ "'"
							+ this.get_location(air_object
									.getString("SiteName"))[0]
							+ "',"
							+ "'"
							+ this.get_location(air_object
									.getString("SiteName"))[1] + "')";
					st.executeUpdate(insert_sql);
					new_data++;
				}
				rs.close();
				st.close();
			}
			if (new_data == 0)
				System.out.println("have not new data to update");
			else
				System.out.println("have " + new_data + " data to update");

			System.out.println("---finish update air_info");
			this.close_db();
			System.out.println("");

		} catch (FileNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String[] get_location(String sitename) {
		// air_location json
		JSONArray air_location_array;
		JSONObject air_location_object;
		String county[] = new String[2];
		try {
			air_location_array = new JSONArray(new JSONTokener(new FileReader(
					new File("AIR_location.json"))));
			for (int i = 0; i < air_location_array.length(); i++) {
				air_location_object = air_location_array.getJSONObject(i);
				if (sitename.equals(air_location_object.getString("SiteName"))) {
					county[0] = air_location_object.getString("County");
					county[1] = air_location_object.getString("Township");
					return county;
				}
			}
		} catch (FileNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		county[0] = "";
		county[1] = "";
		return null;

	}

	// get PSI
	public double GetPSI(String county) {
		// connect to database
		//this.connect_db();
		ResultSet rs;
		Statement st;
		double all = 0;
		int count = 0;
		try {
			// find all this place PSI
			String select_sql = "SELECT * FROM air_info WHERE "
					+ "\"County\" = '" + county + "'";
			st = conn.createStatement();
			rs = st.executeQuery(select_sql);

			while (rs.next()) {
				int i = rs.getInt("PSI");
				all = all + i;
				count++;
			}
			if (count != 0)
				all = all / count;
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		// close connect
		//this.close_db();
		// return the result
		return all;
	}
}