package dbtLab3;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Performance {
	public String movie;
	public String date;
	public String theater;
	public int available_seats;
	
	public Performance(ResultSet rs) throws SQLException {
		this.movie = rs.getString("name");
		this.date = rs.getString("date");
		this.theater = rs.getString("theater_name");
		this.available_seats = rs.getInt("available_seats");
	}
}
