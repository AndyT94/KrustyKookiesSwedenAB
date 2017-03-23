package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ingredient {
	public String material;
	public double quantity;
	
	public Ingredient(ResultSet rs) throws SQLException {
		this.material = rs.getString("material_name");
		this.quantity = rs.getDouble("quantity");
	}
}
