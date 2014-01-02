import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			Map<String,String> countymap = new HashMap<String,String>();
			FileInputStream countyFile = new FileInputStream("county.txt");	
			DataInputStream inCounty = new DataInputStream(countyFile);			  
			BufferedReader brCounty = new BufferedReader(new InputStreamReader(inCounty));
			String strLineCounty;
			Pattern pattern1 = Pattern.compile("(\\d{2})\\d{2},(.+?[縣市]).*");
			Matcher matcher1;
			while ((strLineCounty = brCounty.readLine()) != null) 
			{
				matcher1 = pattern1.matcher(strLineCounty);
				if(matcher1.find())
				{
					if(!countymap.containsKey(matcher1.group(1)))
					{
						countymap.put(matcher1.group(1),matcher1.group(2));
						System.out.println (matcher1.group(1)+matcher1.group(2));
					}
				}
			}
			countymap.put("03","臺中市");
			countymap.put("05","臺南市");
			countymap.put("07","高雄市");
			//new java.util.Scanner(System.in).nextLine();
			String[] file = new String[5];
			file[0] = "dead97.txt";
			file[1] = "dead98.txt";
			file[2] = "dead99.txt";
			file[3] = "dead100.txt";
			file[4] = "dead101.txt";
			
			Pattern pattern = Pattern.compile("\\d+,(\\d{2})\\d{2},(\\d+),.*");
			Matcher matcher;
			Map<String,county> myMap = new HashMap<String,county>();
			for(int i = 0;i<5;i++){
				// Open the file that is the first 	
				// command line parameter			
				FileInputStream fstream = new FileInputStream(file[i]);			  
				// Get the object of DataInputStream			  
				DataInputStream in = new DataInputStream(fstream);			  
				BufferedReader br = new BufferedReader(new InputStreamReader(in));			  
				String strLine;			  
				//Read File Line By Line		  
				while ((strLine = br.readLine()) != null)   {
					// Print the content on the console
					//System.out.println (strLine);
					
					matcher = pattern.matcher(strLine);
					
					while (matcher.find()) {
						/*
						System.out.println("matcher.group(1):\t"+matcher.group(1));
						System.out.println("matcher.group(2):\t"+matcher.group(2));
						*/
						/*
						new java.util.Scanner(System.in).nextLine();
						*/
						/*	matcher.group(1) --> county
						 *	matcher.group(2) --> cause
						 */
						if(!countymap.containsKey(matcher.group(1)))
						{
							new java.util.Scanner(System.in).nextLine();
						}
						
						String key = countymap.get(matcher.group(1));
						int cause = Integer.parseInt(matcher.group(2));
						if( myMap.containsKey(key) )
						{
							if(cause==21||cause==22||cause==23||cause==24||cause==25)
							{
								myMap.get(key).air++;
							}else if(cause==32)
							{
								myMap.get(key).water++;
							}
							
							myMap.get(key).all_cause++;
							
						}else
						{
							myMap.put(key, new county());
							if(cause==21||cause==22||cause==23||cause==24||cause==25)
							{
								myMap.get(key).air++;
							}else if(cause==32)
							{
								myMap.get(key).water++;
							}
								
						}
						
					}//while (matcher.find())
				}//while ((strLine = br.readLine()) != null)
				//Close the input stream			  
				in.close();	
			}
			
			
			for (Object key : myMap.keySet()) {
				myMap.get(key).air_rate =  myMap.get(key).air ;
				myMap.get(key).air_rate /=  myMap.get(key).all_cause;
				myMap.get(key).water_rate = myMap.get(key).water;
				myMap.get(key).water_rate /= myMap.get(key).all_cause;
				
				System.out.println("Key : " + key.toString() + " Value : "
					+ myMap.get(key).all_cause + "  "
					+ myMap.get(key).air+"  "+myMap.get(key).water + " "
					+ myMap.get(key).air_rate+"   "+myMap.get(key).water_rate );
				
				
			}
			
			
			Class.forName("org.postgresql.Driver").newInstance();
			
			String url = "jdbc:postgresql://210.61.10.89:9999/Team11";
			Connection con = DriverManager.getConnection(url,"Team11","2013postgres");
			
			
			//String sql = "INSERT INTO death_info VALUES ('county','air','water',
			//										'all','air_rate','water_rate');";
			//st.executeQuery(sql);
			Statement st = con.createStatement();
			for (Object key : myMap.keySet()) {	
				
				
				String sql = "INSERT INTO death_info VALUES ('"
							+ key +"','"+ myMap.get(key).air +
							"','"+myMap.get(key).water + "','"+ myMap.get(key).all_cause
							+"','"+myMap.get(key).air_rate+"','"+myMap.get(key).water_rate
							+"');"
						;
				st.execute(sql);
				/*st.executeQuery(sql);
				ResultSet rs = st.executeQuery(sql);
				rs.close();*/
			}
			st.close();
			
			con.close();
			
		}catch(Exception e)
		{
			System.out.print(e.toString());
		}
	}

}
