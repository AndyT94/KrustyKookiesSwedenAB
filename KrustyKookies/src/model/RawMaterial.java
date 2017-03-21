package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RawMaterial {
	public String name;
	public double amount;
	public String unit;
	
	public RawMaterial(ResultSet rs) throws SQLException {
		this.name = rs.getString("material_name");
		this.amount = rs.getFloat("material_amount");
		this.unit = rs.getString("unit");
	}
}