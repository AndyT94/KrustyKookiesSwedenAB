package dbtLab3;

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

	public boolean userExists(String userId) {
		try {
			String sql = "SELECT * FROM users WHERE username = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<Movie> getMovies() {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		try {
			String sql = "SELECT * FROM movies";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				movies.add(new Movie(rs));
			}
			return movies;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movies;
	}

	public ArrayList<String> getDates(String movieName) {
		ArrayList<String> dates = new ArrayList<String>();
		try {
			String sql = "SELECT date FROM performances WHERE name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, movieName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dates.add(rs.getString("date"));
			}
			return dates;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dates;
	}

	public Performance getPerformance(String movieName, String date) {
		try {
			String sql = "SELECT * FROM performances WHERE name = ? AND date = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, movieName);
			ps.setString(2, date);
			ResultSet rs = ps.executeQuery();
			return new Performance(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean makeReservation(String movieName, String date) {
		try {
			conn.setAutoCommit(false);
			
			String sql = "SELECT available_seats FROM performances WHERE name = ? AND date = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, movieName);
			ps.setString(2, date);
			ResultSet rs = ps.executeQuery();

			int seats = rs.getInt("available_seats");
			if (seats > 0) {
				sql = "UPDATE performances SET available_seats = ? WHERE name = ? AND date = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, seats - 1);
				ps.setString(2, movieName);
				ps.setString(3, date);
				ps.executeUpdate();

				sql = "INSERT INTO reservations (username, movie_name, date) VALUES (?, ?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, CurrentUser.instance().getCurrentUserId());
				ps.setString(2, movieName);
				ps.setString(3, date);
				ps.executeUpdate();
				
				conn.commit();
				return true;
			} else {
				conn.rollback();
			}
			
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
