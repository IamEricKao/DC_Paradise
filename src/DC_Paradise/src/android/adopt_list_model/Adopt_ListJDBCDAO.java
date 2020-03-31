package android.adopt_list_model;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;

public class Adopt_ListJDBCDAO implements Adopt_ListDAO_interface {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String userid = "G2";
	String passwd = "654321";	

	private static final String INSERT_STMT = 
		"INSERT INTO ADOPT_LIST (Adopt_Project_No, Adopter_No, Real_Name, Phone, Age, ID_Card, Address, Email, Sex, Date_Of_Application, Status)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE = 
		"UPDATE ADOPT_LIST SET Adopt_Project_No=?, Adopter_No=?, Real_Name=?, Phone=?, Age=?, ID_Card=?, Address=?, Email=?, Sex=?, Date_Of_Application=?, Status=?"
		+ "WHERE Adopt_List_No=?";
	private static final String GET_ONE_Adopt_Project_No = 
		"SELECT Adopt_List_No, Adopt_Project_No, Adopter_No, Real_Name, Phone, Age, ID_Card, Address, Email, Sex, Date_Of_Application, Status FROM Adopt_List WHERE Adopt_Project_No = ? ORDER BY Adopt_List_No DESC";

	@Override
	public boolean add(Adopt_ListVO adopt_listVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean isAdded = false;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);			
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, adopt_listVO.getAdopt_Project_No());
			pstmt.setString(2, adopt_listVO.getAdopter_No());
			pstmt.setString(3, adopt_listVO.getReal_Name());
			pstmt.setString(4, adopt_listVO.getPhone());
			pstmt.setInt   (5, adopt_listVO.getAge());
			pstmt.setString(6, adopt_listVO.getID_Card());
			pstmt.setString(7, adopt_listVO.getAddress());
			pstmt.setString(8, adopt_listVO.getEmail());
			pstmt.setInt(9, adopt_listVO.getSex());
			pstmt.setDate  (10, adopt_listVO.getDate_Of_Application());
			pstmt.setInt   (11, adopt_listVO.getStatus());

			int count = pstmt.executeUpdate();
			isAdded = count > 0 ? true : false;

			System.out.println("Add = " + isAdded);
			
			// Handle any driver errors
		}catch (ClassNotFoundException e) {
				e.getMessage();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return isAdded;
	}
	
	@Override
	public boolean update(Adopt_ListVO adopt_listVO) {

		Connection con = null;
		PreparedStatement pstmt = null;
		boolean isUpdated = false;
		
		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, adopt_listVO.getAdopt_Project_No());
			pstmt.setString(2, adopt_listVO.getAdopter_No());
			pstmt.setString(3, adopt_listVO.getReal_Name());
			pstmt.setString(4, adopt_listVO.getPhone());
			pstmt.setInt   (5, adopt_listVO.getAge());
			pstmt.setString(6, adopt_listVO.getID_Card());
			pstmt.setString(7, adopt_listVO.getAddress());
			pstmt.setString(8, adopt_listVO.getEmail());
			pstmt.setInt   (9, adopt_listVO.getSex());
			pstmt.setDate  (10, adopt_listVO.getDate_Of_Application());
			pstmt.setInt   (11, adopt_listVO.getStatus());
			pstmt.setString(12, adopt_listVO.getAdopt_List_No());

			int count = pstmt.executeUpdate();
			isUpdated = count > 0 ? true : false; 	
			System.out.println("修改完成");
	
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return isUpdated;
	}
	
	@Override
	public List<Adopt_ListVO> findBy_Adopt_Project_No(String  Adopt_List_No) {
		
		List<Adopt_ListVO> Adopt_List = new ArrayList<Adopt_ListVO>();
		Adopt_ListVO adopt_listVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_Adopt_Project_No);

			pstmt.setString(1, Adopt_List_No);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// Adopt_ListVO 也稱為 Domain objects
				adopt_listVO = new Adopt_ListVO();
				adopt_listVO.setAdopt_List_No(rs.getString("Adopt_List_No"));
				adopt_listVO.setAdopt_Project_No(rs.getString("Adopt_Project_No"));
				adopt_listVO.setAdopter_No(rs.getString("Adopter_No"));
				adopt_listVO.setReal_Name(rs.getString("Real_Name"));
				adopt_listVO.setPhone(rs.getString("Phone"));
				adopt_listVO.setAge(rs.getInt("Age"));
				adopt_listVO.setID_Card(rs.getString("ID_Card"));
				adopt_listVO.setAddress(rs.getString("Address"));
				adopt_listVO.setEmail(rs.getString("Email"));
				adopt_listVO.setSex(rs.getInt("Sex"));
				adopt_listVO.setDate_Of_Application(rs.getDate("Date_Of_Application"));
				adopt_listVO.setStatus(rs.getInt("Status"));
				Adopt_List.add(adopt_listVO);
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return Adopt_List;
	}
		
	public static void main(String[] args) {

		Adopt_ListJDBCDAO dao = new Adopt_ListJDBCDAO();
		
		//新增
//		Adopt_ListVO adopt_listVO1 = new Adopt_ListVO();		
//		adopt_listVO1.setAdopt_Project_No("AP0001");
//		adopt_listVO1.setAdopter_No("M0006");
//		adopt_listVO1.setReal_Name("許展榮");
//		adopt_listVO1.setPhone("0917777549");
//		adopt_listVO1.setAge(31); 
//		adopt_listVO1.setID_Card("J156951733");
//		adopt_listVO1.setAddress("新竹市東區中興十路80號");
//		adopt_listVO1.setEmail("Areamithat1959@gmail.com");
//		adopt_listVO1.setSex(1);
//		adopt_listVO1.setDate_Of_Application(java.sql.Date.valueOf("2018-08-11"));
//		adopt_listVO1.setStatus(0);
//		dao.add(adopt_listVO1);
	
		//修改
//		Adopt_ListVO adopt_listVO2 = new Adopt_ListVO();
//
//		adopt_listVO2.setAdopt_Project_No("AP0008");
//		adopt_listVO2.setAdopter_No("M0001");
//		adopt_listVO2.setReal_Name("許展瑞");
//		adopt_listVO2.setPhone("0956273308");
//		adopt_listVO2.setAge(28); 
//		adopt_listVO2.setID_Card("N175362642");
//		adopt_listVO2.setAddress("屏東縣屏東市新北二街一段735號37樓");
//		adopt_listVO2.setEmail("Breamithat1959@gustr.com");
//		adopt_listVO2.setSex("男");
//		adopt_listVO2.setDate_Of_Application(java.sql.Date.valueOf("2001-11-15"));
//		adopt_listVO2.setStatus(1);
//		adopt_listVO2.setAdopt_List_No("APL0012");
//		
//		dao.update(adopt_listVO2);

		// 查詢
//		List<Adopt_ListVO> list = dao.getAll();
//		for (Adopt_ListVO aEmp : list) {
//			System.out.print(aEmp.getAdopt_List_No() + ",");
//			System.out.print(aEmp.getAdopt_Project_No() + ",");
//			System.out.print(aEmp.getAdopter_No() + ",");
//			System.out.print(aEmp.getAdopter_No() + ",");
//			System.out.print(aEmp.getReal_Name() + ",");
//			System.out.print(aEmp.getPhone() + ",");
//			System.out.print(aEmp.getAge() + ",");
//			System.out.print(aEmp.getID_Card() + ",");
//			System.out.print(aEmp.getAddress() + ",");
//			System.out.print(aEmp.getEmail() + ",");
//			System.out.print(aEmp.getDate_Of_Application() + ",");
//			System.out.print(aEmp.getStatus());
//			System.out.println();
//		}
	}
}