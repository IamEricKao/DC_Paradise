package android.member_info.model;

import java.util.*;


import java.io.*;
import java.sql.*;

public class Member_infoJDBCDAO implements Member_infoDAO_interface {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String userid = "G2";
	String passwd = "654321";
	
//	private static final String INSERT_STMT = 
//		"INSERT INTO member_info (member_name,member_account,member_password,bonus,phone,address,email,sex,birthday,member_pic,member_type,review,status,file_content,bank_account) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//	private static final String GET_ALL_STMT = 
//		"SELECT member_no,member_name,member_account,member_password,bonus,phone,address,email,sex,to_char(birthday,'yyyy-mm-dd') birthday,member_pic,member_type,review,status,file_content,bank_account FROM member_info order by member_no";
//	private static final String GET_ONE_STMT = 
//		"SELECT member_no,member_name,member_account,member_password,bonus,phone,address,email,sex,to_char(birthday,'yyyy-mm-dd') birthday,member_pic,member_type,review,status,file_content,bank_account FROM member_info where member_no = ?";
//	private static final String DELETE = 
//		"DELETE FROM member_info where member_no = ?";
//	private static final String UPDATE = 
//		"UPDATE member_info set member_name=?, member_account=?, member_password=?, bonus=?, phone=?, address=?, email=?, sex=?, birthday=?, member_pic=?, member_type=?, review=?, status=?, file_content=?, bank_account=? where member_no = ?";
	private static final String FIND_BY_ID_PASWD = "SELECT * FROM member_info WHERE member_account = ? AND member_password = ?";
	private static final String FIND_BY_MEMBER_NO = "SELECT member_no,member_name,member_account,member_password,bonus,phone,address,sex,to_char(birthday,'yyyy-mm-dd') birthday,member_pic,member_type,status,file_content,bank_account FROM member_info where member_account = ?";
		
	@Override
	public Member_infoVO findByMemberNo(String member_account) {
		
		Member_infoVO member_infoVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(FIND_BY_MEMBER_NO);

			pstmt.setString(1, member_account);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				member_infoVO = new Member_infoVO();
				member_infoVO.setMember_no(rs.getString("member_no"));
				member_infoVO.setMember_name(rs.getString("member_name"));
				member_infoVO.setMember_account(rs.getString("member_account"));
				member_infoVO.setMember_password(rs.getString("member_password"));
				member_infoVO.setBonus(rs.getInt("bonus"));
				member_infoVO.setPhone(rs.getString("phone"));
				member_infoVO.setAddress(rs.getString("address"));
				member_infoVO.setSex(rs.getInt("sex"));
				member_infoVO.setBirthday(rs.getDate("birthday"));
				member_infoVO.setMember_pic(rs.getBytes("member_pic"));
				member_infoVO.setMember_type(rs.getInt("member_type"));
				member_infoVO.setStatus(rs.getInt("status"));
				member_infoVO.setFile_content(rs.getBytes("file_content"));
				member_infoVO.setBank_account(rs.getString("bank_account"));
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
		return member_infoVO;
	}
	
	@Override
	public boolean isUserValid(String member_account, String member_password) {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean isUserValid = false;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userid,passwd);
			ps = conn.prepareStatement(FIND_BY_ID_PASWD);
			ps.setString(1, member_account);
			ps.setString(2, member_password);
			ResultSet rs = ps.executeQuery();
			isUserValid = rs.next();
			return isUserValid;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isUserValid;		
	}
	
//	@Override
//	public void insert(Member_infoVO member_infoVO) {
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
//			pstmt.setString(1, member_infoVO.getMember_name());
//			pstmt.setString(2, member_infoVO.getMember_account());
//			pstmt.setString(3, member_infoVO.getMember_password());
//			pstmt.setInt(4, member_infoVO.getBonus());
//			pstmt.setString(5, member_infoVO.getPhone());
//			pstmt.setString(6, member_infoVO.getAddress());
//			pstmt.setString(7, member_infoVO.getEmail());
//			pstmt.setInt(8, member_infoVO.getSex());
//			pstmt.setDate(9, member_infoVO.getBirthday());
//			pstmt.setBytes(10, member_infoVO.getMember_pic());
//			pstmt.setInt(11, member_infoVO.getMember_type());
//			pstmt.setInt(12, member_infoVO.getReview());
//			pstmt.setInt(13, member_infoVO.getStatus());
//			pstmt.setBytes(14, member_infoVO.getFile_content());
//			pstmt.setString(15, member_infoVO.getBank_account());
//			pstmt.executeUpdate();
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
//		
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
//	}
//
//	@Override
//	public void update(Member_infoVO member_infoVO) {
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
//			
//			pstmt.setString(1, member_infoVO.getMember_name());
//			pstmt.setString(2, member_infoVO.getMember_account());
//			pstmt.setString(3, member_infoVO.getMember_password());
//			pstmt.setInt(4, member_infoVO.getBonus());
//			pstmt.setString(5, member_infoVO.getPhone());
//			pstmt.setString(6, member_infoVO.getAddress());
//			pstmt.setString(7, member_infoVO.getEmail());
//			pstmt.setInt(8, member_infoVO.getSex());
//			pstmt.setDate(9, member_infoVO.getBirthday());
//			pstmt.setBytes(10, member_infoVO.getMember_pic());
//			pstmt.setInt(11, member_infoVO.getMember_type());
//			pstmt.setInt(12, member_infoVO.getReview());
//			pstmt.setInt(13, member_infoVO.getStatus());
//			pstmt.setBytes(14, member_infoVO.getFile_content());
//			pstmt.setString(15, member_infoVO.getBank_account());
//			pstmt.setString(16, member_infoVO.getMember_no());
//			
//			pstmt.executeUpdate();
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
//
//	@Override
//	public void delete(String member_no) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(DELETE);
//
//			pstmt.setString(1, member_no);
//
//			pstmt.executeUpdate();
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
//		
//	}
//
//	@Override
//	public Member_infoVO findByPrimaryKey(String member_no) {
//		
//		Member_infoVO member_infoVO = null;
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
//			pstmt.setString(1, member_no);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				// empVo 也稱為 Domain objects
//				member_infoVO = new Member_infoVO();
//				member_infoVO.setMember_no(rs.getString("member_no"));
//				member_infoVO.setMember_name(rs.getString("member_name"));
//				member_infoVO.setMember_account(rs.getString("member_account"));
//				member_infoVO.setMember_password(rs.getString("member_password"));
//				member_infoVO.setBonus(rs.getInt("bonus"));
//				member_infoVO.setPhone(rs.getString("phone"));
//				member_infoVO.setAddress(rs.getString("address"));
//				member_infoVO.setEmail(rs.getString("email"));
//				member_infoVO.setSex(rs.getInt("sex"));
//				member_infoVO.setBirthday(rs.getDate("birthday"));
//				member_infoVO.setMember_pic(rs.getBytes("member_pic"));
//				member_infoVO.setMember_type(rs.getInt("member_type"));
//				member_infoVO.setReview(rs.getInt("review"));
//				member_infoVO.setStatus(rs.getInt("status"));
//				member_infoVO.setFile_content(rs.getBytes("file_content"));
//				member_infoVO.setBank_account(rs.getString("bank_account"));
//			}
	

//
//	@Override
//	public List<Member_infoVO> getAll() {
//
//		List<Member_infoVO> list = new ArrayList<Member_infoVO>();
//		Member_infoVO member_infoVO = null;
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ALL_STMT);
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				// empVO 也稱為 Domain objects
//				member_infoVO = new Member_infoVO();
//				member_infoVO.setMember_no(rs.getString("member_no"));
//				member_infoVO.setMember_name(rs.getString("member_name"));
//				member_infoVO.setMember_account(rs.getString("member_account"));
//				member_infoVO.setMember_password(rs.getString("member_password"));
//				member_infoVO.setBonus(rs.getInt("bonus"));
//				member_infoVO.setPhone(rs.getString("phone"));
//				member_infoVO.setAddress(rs.getString("address"));
//				member_infoVO.setEmail(rs.getString("email"));
//				member_infoVO.setSex(rs.getInt("sex"));
//				member_infoVO.setBirthday(rs.getDate("birthday"));
//				member_infoVO.setMember_pic(rs.getBytes("member_pic"));
//				member_infoVO.setMember_type(rs.getInt("member_type"));
//				member_infoVO.setReview(rs.getInt("review"));
//				member_infoVO.setStatus(rs.getInt("status"));
//				member_infoVO.setFile_content(rs.getBytes("file_content"));
//				member_infoVO.setBank_account(rs.getString("bank_account"));
//				list.add(member_infoVO); // Store the row in the list
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
//		return list;
//	}
//	
	public static void main(String[] args) throws IOException {
		Member_infoJDBCDAO dao = new Member_infoJDBCDAO();
//
//		// 新增
//		Member_infoVO member_infoVO1 = new Member_infoVO();
//		member_infoVO1.setMember_name("吳永志1");
//		member_infoVO1.setMember_account("peter123@gmail.com");		
//		member_infoVO1.setMember_password("2005010101");
//		member_infoVO1.setBonus(0);
//		member_infoVO1.setPhone("0910123456");
//		member_infoVO1.setAddress("桃園市中壢區中大路300號");
//		member_infoVO1.setEmail("peter123@gmail.com");
//		member_infoVO1.setSex(0);
//		member_infoVO1.setBirthday(java.sql.Date.valueOf("2000-01-01"));
//		byte[] pic = getPictureByteArray("items/doggy.jpg");
//		member_infoVO1.setMember_pic(pic);
//		member_infoVO1.setMember_type(0);
//		member_infoVO1.setReview(1);
//		member_infoVO1.setStatus(0);
//		member_infoVO1.setFile_content(null);
//		member_infoVO1.setBank_account(null);
//		dao.insert(member_infoVO1);

//		// 修改
//		Member_infoVO member_infoVO2 = new Member_infoVO();
//		member_infoVO2.setMember_no("M0001");
//		member_infoVO2.setMember_name("吳吳555");
//		member_infoVO2.setMember_account("peter456@gmail.com");
//		member_infoVO2.setMember_password("222555111");
//		member_infoVO2.setBonus(0);
//		member_infoVO2.setPhone("0987456456");
//		member_infoVO2.setAddress("桃園市中壢區中大路333號");
//		member_infoVO2.setEmail("peter123@gmail.com");
//		member_infoVO2.setSex(0);
//		member_infoVO2.setBirthday(java.sql.Date.valueOf("1999-11-11"));
//		member_infoVO2.setMember_pic(null);
//		member_infoVO2.setMember_type(0);
//		member_infoVO2.setReview(1);
//		member_infoVO2.setStatus(0);
//		member_infoVO2.setFile_content(null);
//		member_infoVO2.setBank_account(null);
//		dao.update(member_infoVO2);
//
//		// 刪除
//		dao.delete("M0002");
//
		// 查詢
//		Member_infoVO member_infoVO3 = dao.findByPrimaryKey("M0007");
//		System.out.print(member_infoVO3.getMember_no() + ",");
//		System.out.print(member_infoVO3.getMember_name() + ",");
//		System.out.print(member_infoVO3.getMember_account() + ",");
//		System.out.print(member_infoVO3.getMember_password() + ",");
//		System.out.print(member_infoVO3.getBonus() + ",");
//		System.out.print(member_infoVO3.getPhone() + ",");
//		System.out.print(member_infoVO3.getAddress() + ",");
//		System.out.print(member_infoVO3.getEmail() + ",");
//		System.out.print(member_infoVO3.getSex() + ",");
//		System.out.print(member_infoVO3.getBirthday() + ",");
//		System.out.print(member_infoVO3.getMember_pic() + ",");
//		System.out.print(member_infoVO3.getMember_type() + ",");
//		System.out.print(member_infoVO3.getReview() + ",");
//		System.out.print(member_infoVO3.getStatus() + ",");
//		System.out.print(member_infoVO3.getFile_content() + ",");
//		System.out.println(member_infoVO3.getBank_account());
//		System.out.println("---------------------");
	
		// 查詢
//		Member_infoVO member_infoVO3 = dao.findByMemberNo("david");
//		System.out.print(member_infoVO3.getMember_no() + ",");
//		System.out.print(member_infoVO3.getMember_name() + ",");
//		System.out.print(member_infoVO3.getMember_account() + ",");
//		System.out.print(member_infoVO3.getMember_password() + ",");
//		System.out.print(member_infoVO3.getBonus() + ",");
//		System.out.print(member_infoVO3.getPhone() + ",");
//		System.out.print(member_infoVO3.getAddress() + ",");
//		System.out.print(member_infoVO3.getSex() + ",");
//		System.out.print(member_infoVO3.getBirthday() + ",");
//		System.out.print(member_infoVO3.getMember_pic() + ",");
//		System.out.print(member_infoVO3.getMember_type() + ",");
//		System.out.print(member_infoVO3.getStatus() + ",");
//		System.out.print(member_infoVO3.getFile_content() + ",");
//		System.out.println(member_infoVO3.getBank_account());
//		System.out.println("---------------------");
	
	
//
//		// 查詢
//		List<Member_infoVO> list = dao.getAll();
//		for (Member_infoVO aMember_info : list) {
//			System.out.print(aMember_info.getMember_no() + ",");
//			System.out.print(aMember_info.getMember_name() + ",");
//			System.out.print(aMember_info.getMember_account() + ",");
//			System.out.print(aMember_info.getMember_password() + ",");
//			System.out.print(aMember_info.getBonus() + ",");
//			System.out.print(aMember_info.getPhone() + ",");			
//			System.out.print(aMember_info.getAddress()+ ",");
//			System.out.print(aMember_info.getEmail() );
//			System.out.println();
//		}
		
		
//	}
//	public static byte[] getPictureByteArray(String path) throws IOException {
//		File file = new File(path);
//		FileInputStream fis = new FileInputStream(file);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();//本就內建BYTE陣列，直接拿來裝
//		byte[] buffer = new byte[8192];
//		int i;//自訂緩衝，做分段讀取
//		while ((i = fis.read(buffer)) != -1) {
//			baos.write(buffer, 0, i);
//		}
//		baos.close();
//		fis.close();
//
//		return baos.toByteArray();
	}

	
}