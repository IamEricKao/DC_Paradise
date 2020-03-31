package android.member_info.model;

import java.util.*;


import java.io.*;
import java.sql.*;

public class Member_infoJDBCDAO implements Member_infoDAO_interface {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String userid = "G2";
	String passwd = "654321";
	
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
	
//	public static void main(String[] args) throws IOException {
//		Member_infoJDBCDAO dao = new Member_infoJDBCDAO();

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
//	}
}