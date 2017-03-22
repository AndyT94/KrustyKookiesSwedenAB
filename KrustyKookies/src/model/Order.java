package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Order {
	public int id;
	public String recipe;
	public int amount;
	public String customer;
	public String date;
	
	public Order(ResultSet rs) throws SQLException {
		
	}
}
