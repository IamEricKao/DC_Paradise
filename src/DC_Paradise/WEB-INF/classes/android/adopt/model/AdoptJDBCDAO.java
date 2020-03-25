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

//	private static final String INSERT_STMT = 
//		"INSERT INTO ADOPT_PROJECT (Founder_No, Adopter_No, Adopt_Project_Name, Pet_Category, Adopt_Content, Adopt_Status, Adopt_Result, Sex, Age, Breed, Chip, Birth_Control, Founder_Location)"
//				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT Adopt_Project_No, Founder_No,Adopter_No, Adopt_Project_Name, Pet_Category, Adopt_Content, Adopt_Status, Adopt_Result, Sex, Age, Breed, Chip, Birth_Control, Founder_Location FROM ADOPT_PROJECT order by Adopt_Project_No DESC";
	//	private static final String GET_ONE_STMT = 
//		"SELECT Adopt_Project_No,Founder_No,Adopter_No,Adopt_Project_Name,Pet_Category,Adopt_Content,Adopt_Status,Adopt_Result,Sex,Age,Breed,Chip,Birth_Control,Founder_Location FROM ADOPT_PROJECT where Adopt_Project_No = ?";
//	private static final String DELETE = 
//		"DELETE FROM ADOPT_PROJECT where Adopt_Project_No = ?";
//	private static final String UPDATE = 
//		"UPDATE ADOPT_PROJECT set Founder_No=?, Adopter_No=?, Adopt_Project_Name=?, Pet_Category=?, Adopt_Content=?, Adopt_Status=?, Adopt_Result=?, Sex=?, Age=?, Breed=?, Chip=?, Birth_Control=?, Founder_Location=?"
//		+ "where Adopt_Project_No=?";
	private static final String FIND_BY_Adopt_Project_No =  
			"SELECT * FROM ADOPT_PROJECT WHERE ADOPT_PROJECT_NO = ?";
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
	
//	@Override
//	public void insert(AdoptVO adoptVO) {
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);			
//			pstmt = con.prepareStatement(INSERT_STMT);
//			
//			pstmt.setString(1, adoptVO.getFounder_No());
//			pstmt.setString(2, adoptVO.getAdopter_No());
//			pstmt.setString(3, adoptVO.getAdopt_Project_Name());
//			pstmt.setInt   (4, adoptVO.getPet_Category());
//			pstmt.setString(5, adoptVO.getAdopt_Content());
//			pstmt.setInt   (6, adoptVO.getAdopt_Status());
//			pstmt.setString(7, adoptVO.getAdopt_Result());
//			pstmt.setInt   (8, adoptVO.getSex());
//			pstmt.setString(9, adoptVO.getAge());
//			pstmt.setString(10, adoptVO.getBreed());
//			pstmt.setInt   (11, adoptVO.getChip());
//			pstmt.setInt   (12, adoptVO.getBirth_Control());
//			pstmt.setString(13, adoptVO.getFounder_Location());
//			
//			pstmt.executeUpdate();
//			 
//			System.out.println("新增成功");
//			
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. "
//					+ e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. "
//					+ se.getMessage());		
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//
//	}

//	@Override
//	public void update(AdoptVO adoptVO) {
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(UPDATE);
//
//			pstmt.setString(1, adoptVO.getFounder_No());
//			pstmt.setString(2, adoptVO.getAdopter_No());
//			pstmt.setString(3, adoptVO.getAdopt_Project_Name());
//			pstmt.setInt   (4, adoptVO.getPet_Category());
//			pstmt.setString(5, adoptVO.getAdopt_Content());
//			pstmt.setInt   (6, adoptVO.getAdopt_Status());
//			pstmt.setString(7, adoptVO.getAdopt_Result());
//			pstmt.setInt   (8, adoptVO.getSex());
//			pstmt.setString(9, adoptVO.getAge());
//			pstmt.setString(10, adoptVO.getBreed());
//			pstmt.setInt   (11, adoptVO.getChip());
//			pstmt.setInt   (12, adoptVO.getBirth_Control());
//			pstmt.setString(13, adoptVO.getFounder_Location());
//			pstmt.setString(14, adoptVO.getAdopt_Project_No());
//
//			pstmt.executeUpdate();
//
//			System.out.println("修改完成");
//	
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. "
//					+ e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. "
//					+ se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//
//	}

//	@Override
//	public void delete(String Adopt_Project_No) {
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(DELETE);
//
//			pstmt.setString(1, Adopt_Project_No);
//
//			pstmt.executeUpdate();
//			
//			System.out.println("刪除成功");
//			
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. "
//					+ e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. "
//					+ se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//
//	}

//	@Override
//	public AdoptVO findByPrimaryKey(String Adopt_Project_No) {
//
//		AdoptVO adoptVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ONE_STMT);
//
//			pstmt.setString(1, Adopt_Project_No);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				// empVo 也稱為 Domain objects
//				adoptVO = new AdoptVO();
//				adoptVO.setAdopt_Project_No(rs.getString("Adopt_Project_No"));
//				adoptVO.setFounder_No(rs.getString("Founder_No"));
//				adoptVO.setAdopter_No(rs.getString("Adopter_No"));
//				adoptVO.setAdopt_Project_Name(rs.getString("Adopt_Project_Name"));
//				adoptVO.setPet_Category(rs.getInt("Pet_Category"));
//				adoptVO.setAdopt_Content(rs.getString("Adopt_Content"));
//				adoptVO.setAdopt_Status(rs.getInt("Adopt_Status"));
//				adoptVO.setAdopt_Result(rs.getString("Adopt_Result"));
//				adoptVO.setSex(rs.getInt("Sex"));
//				adoptVO.setAge(rs.getString("Age"));
//				adoptVO.setBreed(rs.getString("Breed"));
//				adoptVO.setChip(rs.getInt("Chip"));
//				adoptVO.setBirth_Control(rs.getInt("Birth_Control"));
//				adoptVO.setFounder_Location(rs.getString("Founder_Location"));
//			}
//
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. "
//					+ e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. "
//					+ se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//		return adoptVO;
//	}

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
	
	public static void main(String[] args) {
//
		AdoptJDBCDAO dao = new AdoptJDBCDAO();
//		
		// 新增
//		AdoptVO adoptVO1 = new AdoptVO();
//		adoptVO1.setFounder_No("M0001");
//		adoptVO1.setAdopter_No("M0013");
//		adoptVO1.setAdopt_Project_Name("見人就翻肚幼母犬");
//		adoptVO1.setPet_Category(1);
//		adoptVO1.setAdopt_Content(null);
//		adoptVO1.setAdopt_Status(0);
//		adoptVO1.setAdopt_Result(null);
//		adoptVO1.setSex(0);
//		adoptVO1.setAge("12w5d");
//		adoptVO1.setBreed("混種犬");
//		adoptVO1.setChip(1);
//		adoptVO1.setBirth_Control(1);
//		adoptVO1.setFounder_Location("台東市");
//		dao.insert(adoptVO1);
		
//		
//		
//		// 修改
//		AdoptVO adoptVO2 = new AdoptVO();
//		adoptVO2.setFounder_No("M0002");
//		adoptVO2.setAdopter_No("M0004");
//		adoptVO2.setAdopt_Project_Name("浪浪回家");
//		adoptVO2.setPet_Category(0);
//		adoptVO2.setAdopt_Content(null); 
//		adoptVO2.setAdopt_Status(1);
//		adoptVO2.setAdopt_Result(null);
//		adoptVO2.setSex(1);
//		adoptVO2.setAge("5y3m");
//		adoptVO2.setBreed("貴賓狗");
//		adoptVO2.setChip(0);
//		adoptVO2.setBirth_Control(1);
//		adoptVO2.setFounder_Location("新北市");
//		adoptVO2.setAdopt_Project_No("DP0013");
//		
//		dao.update(adoptVO2);
//
//		// 刪除
//		dao.delete("DP0007");
//
//		// 查詢
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
		List<AdoptVO> list = dao.getAll();
		for (AdoptVO aEmp : list) {
			System.out.print(aEmp.getAdopt_Project_No() + ",");
			System.out.print(aEmp.getFounder_No() + ",");
			System.out.print(aEmp.getAdopter_No() + ",");
			System.out.print(aEmp.getAdopt_Project_Name() + ",");
			System.out.print(aEmp.getPet_Category() + ",");
			System.out.print(aEmp.getAdopt_Content() + ",");
			System.out.print(aEmp.getAdopt_Status() + ",");
			System.out.print(aEmp.getAdopt_Result() + ",");
			System.out.print(aEmp.getSex() + ",");
			System.out.print(aEmp.getAge() + ",");
			System.out.print(aEmp.getBreed() + ",");
			System.out.print(aEmp.getChip() + ",");
			System.out.print(aEmp.getBirth_Control() + ",");
			System.out.print(aEmp.getFounder_Location());
			System.out.println();
		}
		
		byte[] picture = dao.getImage("");
		if(picture == null || picture.length == 0) {
			System.out.println("Picture not found");
		}else {			
				System.out.println("Get Picture");			
		}
			
		
	}
	
	// 使用String
	public static String getLongString(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		StringBuilder sb = new StringBuilder(); // StringBuffer is thread-safe!
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
			sb.append("\n");
		}
		br.close();

		return sb.toString();
	}

	// 使用資料流
	public static Reader getLongStringStream(String path) throws IOException {
		return new FileReader(path);

	}
}