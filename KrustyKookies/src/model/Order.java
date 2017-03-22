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
		this.id = rs.getInt("order_id");
		this.recipe = rs.getString("recipe_name");
		this.amount = rs.getInt("amount");
		this.customer = rs.getString("customer_name");
		this.date = rs.getString("deliver_by_date");
	}
}
