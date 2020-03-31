package android.adopt_list.model.controller;

import android.adopt_list_model.Adopt_ListDAO_interface;
import android.adopt_list_model.Adopt_ListJDBCDAO;
import android.adopt_list_model.Adopt_ListVO;
import android.member_info.model.Member_infoDAO_interface;
import android.member_info.model.Member_infoJDBCDAO;
import android.member_info.model.Member_infoVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Android_AdoptListServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("input: " + jsonIn);
		Adopt_ListDAO_interface dao = new Adopt_ListJDBCDAO();
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();		

		if (action.equals("add")) {
			Adopt_ListVO adopt_listVO = gson.fromJson(jsonObject.get("adopt_list").getAsString(), Adopt_ListVO.class);
			writeText(res, String.valueOf(dao.add(adopt_listVO)));
		}else if(action.equals("findBy_Adopt_Project_No")){
			String Adopt_Project_No = jsonObject.get("Adopt_Project_No").getAsString();
			List<Adopt_ListVO> adopt_listVO = dao.findBy_Adopt_Project_No(Adopt_Project_No);
			writeText(res, gson.toJson(adopt_listVO));
		}else if(action.equals("update")) {
			Adopt_ListVO adopt_listVO = gson.fromJson(jsonObject.get("adopt_List").getAsString(), Adopt_ListVO.class);
			writeText(res, String.valueOf(dao.update(adopt_listVO)));
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
