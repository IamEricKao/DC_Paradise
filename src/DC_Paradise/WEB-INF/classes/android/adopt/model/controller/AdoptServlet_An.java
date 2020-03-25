package android.adopt.model.controller;

import android.adopt.model.AdoptDAO_interface;
import android.adopt.model.AdoptJDBCDAO;
import android.adopt.model.AdoptVO;
import android.main.ImageUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AdoptServlet_An extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		ServletContext context = getServletContext();
		AdoptDAO_interface dao = new AdoptJDBCDAO();
		Gson gson = new Gson();

		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("input: " + jsonIn);
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();

		if ("getAll".equals(action)) {
			List<AdoptVO> adoptlist = dao.getAll();
			writeText(res, gson.toJson(adoptlist));
		} else if ("getImage".equals(action)) {
			OutputStream os = res.getOutputStream();
			String Adopt_Project_No = jsonObject.get("Adopt_Project_No").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = dao.getImage(Adopt_Project_No);
			if (image != null) {
				// ч╕ох?? in server side
				image = ImageUtil.shrink(image, imageSize);
				res.setContentType("image/jpeg");
				res.setContentLength(image.length);
			}
			os.write(image);

		} else {
			writeText(res, "");
		}

	}

	private void writeText(HttpServletResponse res, String outText) throws IOException {
		res.setContentType(CONTENT_TYPE);
		PrintWriter out = res.getWriter();
		out.print(outText);
		out.close();
		System.out.println("outText: " + outText);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		AdoptDAO_interface dao = new AdoptJDBCDAO();
		List<AdoptVO> adoptlist = dao.getAll();
		writeText(res, new Gson().toJson(adoptlist));
	}

}
