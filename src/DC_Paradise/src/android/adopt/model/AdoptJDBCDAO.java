package android.adopt.model;

import java.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;

public class AdoptJDBCDAO implements AdoptDAO_interface {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String userid = "G2";
	String passwd = "654321";	

	private static final String GET_ALL_STMT = "SELECT Adopt_Project_No, Founder_No,Adopter_No, Adopt_Project_Name, Pet_Category, Adopt_Content, Adopt_Status, Adopt_Result, Sex, Age, Breed, Chip, Birth_Control, Founder_Location FROM ADOPT_PROJECT order by Adopt_Project_No DESC";
	private static final String FIND_BY_Adopt_Project_No = "SELECT * FROM ADOPT_PROJECT WHERE ADOPT_PROJECT_NO = ?";
	private static final String FIND_IMG_BY_Adopt_Project_No = "SELECT Picture FROM Adopt_Pic WHERE Adopt_Project_No = ? AND ROWNUM=1";
	
	@Override
	public byte[] getImage(String Adopt_Project_No) {
		byte[] picture = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(FIND_IMG_BY_Adopt_Project_No);
			
			pstmt.setString(1, Adopt_Project_No);
			
			rs = pstmt.executeQuery();

			if (rs.next()) {
				picture = rs.getBytes(1);
			}
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return picture;
	}
	
	@Override
	public AdoptVO findByAdoptProjectNo(String Adopt_Project_No) {

		AdoptVO adoptVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(FIND_BY_Adopt_Project_No);

			pstmt.setString(1, Adopt_Project_No);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				adoptVO = new AdoptVO();
				adoptVO.setAdopt_Project_No(rs.getString("Adopt_Project_No"));
				adoptVO.setFounder_No(rs.getString("Founder_No"));
				adoptVO.setAdopter_No(rs.getString("Adopter_No"));
				adoptVO.setAdopt_Project_Name(rs.getString("Adopt_Project_Name"));
				adoptVO.setPet_Category(rs.getInt("Pet_Category"));
				adoptVO.setAdopt_Content(rs.getString("Adopt_Content"));
				adoptVO.setAdopt_Status(rs.getInt("Adopt_Status"));
				adoptVO.setAdopt_Result(rs.getString("Adopt_Result"));
				adoptVO.setSex(rs.getInt("Sex"));
				adoptVO.setAge(rs.getString("Age"));
				adoptVO.setBreed(rs.getString("Breed"));
				adoptVO.setChip(rs.getInt("Chip"));
				adoptVO.setBirth_Control(rs.getInt("Birth_Control"));
				adoptVO.setFounder_Location(rs.getString("Founder_Location"));
	
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
		return adoptVO;
	}
	

	@Override
	public List<AdoptVO> getAll() {
		List<AdoptVO> list = new ArrayList<AdoptVO>();
		AdoptVO adoptVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				adoptVO = new AdoptVO();
				adoptVO.setAdopt_Project_No(rs.getString("Adopt_Project_No"));
				adoptVO.setFounder_No(rs.getString("Founder_No"));
				adoptVO.setAdopter_No(rs.getString("Adopter_No"));
				adoptVO.setAdopt_Project_Name(rs.getString("Adopt_Project_Name"));
				adoptVO.setPet_Category(rs.getInt("Pet_Category"));
				adoptVO.setAdopt_Content(rs.getString("Adopt_Content"));
				adoptVO.setAdopt_Status(rs.getInt("Adopt_Status"));
				adoptVO.setAdopt_Result(rs.getString("Adopt_Result"));
				adoptVO.setSex(rs.getInt("Sex"));
				adoptVO.setAge(rs.getString("Age"));
				adoptVO.setBreed(rs.getString("Breed"));
				adoptVO.setChip(rs.getInt("Chip"));
				adoptVO.setBirth_Control(rs.getInt("Birth_Control"));
				adoptVO.setFounder_Location(rs.getString("Founder_Location"));
				list.add(adoptVO); // Store the row in the list
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
		return list;
	}
	
//	public static void main(String[] args) {
//
//		AdoptJDBCDAO dao = new AdoptJDBCDAO();

		// 查詢
//		AdoptVO adoptVO3 = dao.findByPrimaryKey("DP0001");
//		System.out.print(adoptVO3.getAdopt_Project_No() + ",");
//		System.out.print(adoptVO3.getFounder_No() + ",");
//		System.out.print(adoptVO3.getAdopter_No() + ",");
//		System.out.print(adoptVO3.getAdopt_Project_Name() + ",");
//		System.out.print(adoptVO3.getPet_Category() + ",");
//		System.out.print(adoptVO3.getAdopt_Content() + ",");
//		System.out.print(adoptVO3.getAdopt_Status() + ",");
//		System.out.print(adoptVO3.getAdopt_Result() + ",");
//		System.out.print(adoptVO3.getSex() + ",");
//		System.out.print(adoptVO3.getAge() + ",");
//		System.out.print(adoptVO3.getBreed() + ",");
//		System.out.print(adoptVO3.getChip() + ",");
//		System.out.print(adoptVO3.getBirth_Control() + ",");
//		System.out.println(adoptVO3.getFounder_Location());
//		System.out.println("--------------------------------------------------------------");
//
		// 查詢
//		List<AdoptVO> list = dao.getAll();
//		for (AdoptVO aEmp : list) {
//			System.out.print(aEmp.getAdopt_Project_No() + ",");
//			System.out.print(aEmp.getFounder_No() + ",");
//			System.out.print(aEmp.getAdopter_No() + ",");
//			System.out.print(aEmp.getAdopt_Project_Name() + ",");
//			System.out.print(aEmp.getPet_Category() + ",");
//			System.out.print(aEmp.getAdopt_Content() + ",");
//			System.out.print(aEmp.getAdopt_Status() + ",");
//			System.out.print(aEmp.getAdopt_Result() + ",");
//			System.out.print(aEmp.getSex() + ",");
//			System.out.print(aEmp.getAge() + ",");
//			System.out.print(aEmp.getBreed() + ",");
//			System.out.print(aEmp.getChip() + ",");
//			System.out.print(aEmp.getBirth_Control() + ",");
//			System.out.print(aEmp.getFounder_Location());
//			System.out.println();
//		}
//		
//		byte[] picture = dao.getImage("");
//		if(picture == null || picture.length == 0) {
//			System.out.println("Picture not found");
//		}else {			
//				System.out.println("Get Picture");			
//		}
//			
//		
//	}
}