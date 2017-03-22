package model;

import java.sql.*;
import java.util.*;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC.
 */
public class Database {

	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 */
	public boolean openConnection(String filename) {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + filename);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public List<RawMaterial> getRawMaterials() {
		LinkedList<RawMaterial> materials = new LinkedList<RawMaterial>();
		try {
			String sql = "SELECT material_name, material_amount, unit FROM RawMaterials ORDER BY material_name";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				materials.add(new RawMaterial(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return materials;
	}

	public Pallet getPallet(String pallet_id) {
		try {
			String sql = "SELECT * FROM pallets WHERE pallet_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pallet_id);
			ResultSet rs = ps.executeQuery();
			return new Pallet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<RawMaterialDelivery> getRawMaterialsDeliveries() {
		LinkedList<RawMaterialDelivery> deliveries = new LinkedList<RawMaterialDelivery>();
		try {
			String sql = "SELECT delivery_date, material_name, delivery_amount FROM RawDeliveries ORDER BY delivery_date DESC";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				deliveries.add(new RawMaterialDelivery(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deliveries;
	}

	public ArrayList<Pallet> getPallets() {
		ArrayList<Pallet> list = new ArrayList<Pallet>();
		try {
			String sql = "SELECT * FROM pallets";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Pallet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean hasRawMaterial(String material) {
		try {
			String sql = "SELECT * FROM RawMaterials WHERE material_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, material);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void insertDelivery(String date, String material, String amount) {
		try {
			conn.setAutoCommit(false);
			
			String sql = "INSERT INTO RawDeliveries (delivery_date, material_name, delivery_amount) VALUES (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			ps.setString(2, material);
			ps.setString(3, amount);
			ps.executeUpdate();
			
			sql = "SELECT material_amount FROM RawMaterials WHERE material_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, material);
			ResultSet rs = ps.executeQuery();
			double currAmount = rs.getFloat("material_amount");
			Double newAmount = currAmount + Double.parseDouble(amount);

			sql = "UPDATE RawMaterials SET material_amount = ? WHERE material_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, newAmount.toString());
			ps.setString(2, material);
			ps.executeUpdate();
			
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Pallet> getAllBlockedPallets() {
		ArrayList<Pallet> list = new ArrayList<Pallet>();
		try {
			String sql = "SELECT * FROM pallets WHERE blocked = 1";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Pallet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	
}
