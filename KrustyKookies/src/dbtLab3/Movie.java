package dbtLab3;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Movie {
	public String name;
	
	public Movie(ResultSet rs) throws SQLException {
		this.name = rs.getString("name");
	}
}
