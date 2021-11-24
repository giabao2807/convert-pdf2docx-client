package model.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.bean.Source;
import utils.ConnectionProvider;

public class SourceDao {
	private Connection connection;
	private ResultSet rs;
	private Statement st;
	private PreparedStatement pst;

	public SourceDao() {
		connection = ConnectionProvider.getConnection();
	}

	public List<Source> getAll() {
		List<Source> sources = new ArrayList<Source>();
		String query = "select * from source";
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				Source source = new Source(rs.getInt("id"),rs.getString("filename"),rs.getBoolean("status"),rs.getString("username"));
				System.out.println(source);
				sources.add(source);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sources;
	}
	
	public List<Source> get(String username){
		List<Source> sources = new ArrayList<Source>();
		String query = "select * from source where username=?";
		try {
			pst = connection.prepareStatement(query);
			pst.setString(1,username);
			rs = pst.executeQuery();
			while (rs.next()) {
				Source source = new Source(rs.getInt("id"),rs.getString("filename"),rs.getBoolean("status"),rs.getString("username"));
				System.out.println(source);
				sources.add(source);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sources;
	}

	public boolean save(InputStream is, Source source) {
		try {
			pst = connection.prepareStatement("insert into source(filename,pdf_text,status,username) values(?,?,?,?)");
			pst.setString(1, source.getFilename());
			pst.setBlob(2, is);
			pst.setBoolean(3, source.isStatus());
			pst.setString(4, source.getUsername());
			int affectesRows = pst.executeUpdate();
			return affectesRows > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
