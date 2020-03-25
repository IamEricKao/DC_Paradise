package android.member_info.model.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.member_info.model.Member_infoDAO_interface;
import android.member_info.model.Member_infoJDBCDAO;
import android.member_info.model.Member_infoVO;

public class MemberServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("input: " + jsonIn);
		Member_infoDAO_interface member_infoDAO_interface = new Member_infoJDBCDAO();
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();

		if (action.equals("isUserValid")) {
			String member_account = jsonObject.get("member_account").getAsString();
			String member_password = jsonObject.get("member_password").getAsString();
			writeText(res,	String.valueOf(member_infoDAO_interface.isUserValid(member_account, member_password)));
		}else if (action.equals("findByMemberNo")) {			
			String member_account = jsonObject.get("member_account").getAsString();
			Member_infoVO member_infoVO = member_infoDAO_interface.findByMemberNo(member_account);
			writeText(res, gson.toJson(member_infoVO));
//		} else if (action.equals("isUserIdExist")) {
//			String userId = jsonObject.get("userId").getAsString();
//			writeText(res, String.valueOf(memberDao.isUserIdExist(userId)));
//		} else if (action.equals("add")) {
//			Member_infoVO member = gson.fromJson(jsonObject.get("Member_infoVO").getAsString(), Member_infoVO.class);
//			writeText(res, String.valueOf(Member_infoJDBCDAO.add(member)));
//		} else if (action.equals("findById")) {
//			String userId = jsonObject.get("userId").getAsString();
//			Member_infoVO member = Member_infoJDBCDAO.findById(userId);
//			writeText(res, member == null ? "" : gson.toJson(member));
//		} else if (action.equals("update")) {
//			Member_infoVO member = gson.fromJson(jsonObject.get("Member_infoVO").getAsString(), Member_infoVO.class);
//			writeText(res, String.valueOf(memberDao.update(member)));
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	private void writeText(HttpServletResponse res, String outText) throws IOException {
		res.setContentType(CONTENT_TYPE);
		PrintWriter out = res.getWriter();
		out.print(outText);
		out.close();
		System.out.println("outText: " + outText);

	}
}
