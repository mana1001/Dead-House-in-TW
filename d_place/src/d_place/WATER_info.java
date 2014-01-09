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
	String water_url = "http://opendata.epa.gov.tw/ws/Data/WQXDam/?$orderby=SampleDate%20desc&$skip=0&$top=1000&format=json";

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

	// show all water information
	public void show_info(String path) {
		// read json
		JSONArray water_info_json;
		try {
			water_info_json = new JSONArray(new JSONTokener(new FileReader(
					new File(path))));

			JSONObject air_object;
			// print all json data
			for (int i = 0; i < water_info_json.length(); i++) {
				air_object = water_info_json.getJSONObject(i);
				System.out.println(air_object.get("SiteName") + "  "
						+ air_object.get("ItemName") + "  "
						+ air_object.get("ItemValue"));
			}
		} catch (FileNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// updata air information in local air data
	public void update() {
		this.connect_db();

		System.out.println("---begin update water_info");

		// download json file
		this.download_info(water_url);

		// read json
		JSONArray water_info_json;
		JSONObject water_object;

		int new_data = 0;
		try {
			water_info_json = new JSONArray(new JSONTokener(new FileReader(
					new File(path))));
			System.out.println("updateing.....");
			for (int i = 0; i < water_info_json.length(); i++) {
				water_object = water_info_json.getJSONObject(i);
				//only get 焊此计
				if (water_object.getString("ItemName").equals("焊此计")) {
					String select_sql = "SELECT * FROM water_info WHERE "
							+ "\"SiteName\" = '" + water_object.get("SiteName")
							+ "' AND " + "\"SampleDate\" = '"
							+ water_object.get("SampleDate") + "'";
					// System.out.println(sql);
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery(select_sql);
					// have new data to updata
					if (rs.next())
					{
						break;
					}else
					{
						String insert_sql = "INSERT INTO water_info VALUES ('"
								+ water_object.get("SiteName") + "'," + "'"
								+ water_object.get("County") + "'," + "'"
								+ water_object.get("Township") + "'," + "'"
								+ water_object.get("SampleDate") + "'," + "'"
								+ water_object.get("ItemName") + "',"
								+ water_object.get("ItemValue") + ")";
						st.executeUpdate(insert_sql);
						new_data++;
					}
					rs.close();
					st.close();
				}
				// find the data is always in table air_info

			}
			if (new_data == 0)
				System.out.println("have not new data to update");
			else
				System.out.println("have " + new_data + " data to update");

			System.out.println("---finish update water_info");
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
	// get PSI
		public double GetCarlson(String county) {
			// connect to database
			//this.connect_db();
			ResultSet rs;
			Statement st;
			double all = 0;
			int count = 0;
			try {
				// find all this place PSI
				String select_sql = "SELECT * FROM water_info WHERE "
						+ "\"County\" = '" + county + "'";
				st = conn.createStatement();
				rs = st.executeQuery(select_sql);

				while (rs.next()) {
					int i = rs.getInt("ItemValue");
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