package d_place;

import java.sql.*;

public class death_info {

	private static death_info current_death_info = new death_info();
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://210.61.10.89:9999/Team11";
	// database user and passwd
	static final String USER = "Team11";
	static final String PASS = "2013postgres";
	private static Connection conn = null;

	public static death_info getObject() {
		return current_death_info;
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

	/*
	 * 去資料庫拿死因的資料 輸入縣市後回傳該縣市的死因數量與總死因量的比例
	 */

	public double get_air_death_rate(String county) {
		double data = -1;
		// this.connect_db();
		ResultSet rs;
		Statement st;
		try {
			// find all this place PSI
			String select_sql = "SELECT * FROM death_info WHERE "
					+ "\"county\" = '" + county + "';";
			st = conn.createStatement();
			rs = st.executeQuery(select_sql);
			if (rs.next())
				data = rs.getDouble("air_rate");
			else {
				// System.out.println("Cannot find "+county);
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		// this.close_db();
		return data;
	}

	public double get_water_death_rate(String county) {
		double data = -1;
		// this.connect_db();
		ResultSet rs;
		Statement st;
		try {
			// find all this place PSI
			String select_sql = "SELECT * FROM death_info WHERE "
					+ "\"county\" = '" + county + "';";
			st = conn.createStatement();
			rs = st.executeQuery(select_sql);

			if (rs.next()) {
				data = rs.getDouble("water_rate");
				rs.close();
				st.close();
			} else {
				// System.out.println("Cannot find "+county);
				rs.close();
				st.close();
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

		// this.close_db();
		return data;
	}

	private String[] countys = { "嘉義市", "新北市", "屏東縣", "高雄縣", "嘉義縣", "雲林縣",
			"臺南市", "新竹縣", "彰化縣", "臺南縣", "臺中縣", "臺中市", "宜蘭縣", "金門縣", "基隆市",
			"連江縣", "高雄市", "台東縣", "臺北市", "新竹市", "澎湖縣", "南投縣", "苗栗縣", "花蓮縣",
			"桃園縣" };

	public String[] get_death_info_county() {
		return countys;
	}
}
