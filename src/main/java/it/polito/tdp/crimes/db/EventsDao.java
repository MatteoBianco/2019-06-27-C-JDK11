package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<String> getAllVertices(String category, LocalDate date) {
		String sql = "SELECT DISTINCT offense_type_id " + 
				"FROM EVENTS " + 
				"WHERE offense_category_id = ? " + 
				"AND DATE(reported_date) = ? " ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			st.setString(1, category);
			st.setDate(2, Date.valueOf(date));
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("offense_type_id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Integer getWeightForVertices(String v1, String v2, String category, LocalDate date) {
		String sql = "SELECT COUNT(DISTINCT e1.precinct_id) as weight " + 
				"FROM EVENTS AS e1, EVENTS AS e2 " + 
				"WHERE e1.offense_type_id = ? AND e2.offense_type_id = ? " + 
				"AND e1.precinct_id = e2.precinct_id AND e1.offense_category_id = e2.offense_category_id " + 
				"AND DATE(e1.reported_date) = DATE(e2.reported_date) " + 
				"AND e1.offense_category_id = ? " + 
				"AND DATE(e1.reported_date) = ? " ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
						
			st.setString(1, v1);
			st.setString(2, v2);
			st.setString(3, category);
			st.setDate(4, Date.valueOf(date));
			
			Integer weight = null;
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				try {
					weight = res.getInt("weight");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return weight;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getAllCategories() {
		String sql = "SELECT DISTINCT offense_category_id FROM events ORDER BY offense_category_id" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("offense_category_id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<LocalDate> getAllDates() {
		String sql = "SELECT DISTINCT DATE(reported_date) AS `date` " + 
				"FROM EVENTS " + 
				"ORDER BY `date`" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<LocalDate> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getDate("date").toLocalDate());
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}		
	}
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

}
