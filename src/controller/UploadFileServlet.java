package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.bean.Account;
import model.bean.Source;
import model.bo.SourceBo;
import model.dao.SourceDao;

@WebServlet("/UploadFileServlet")
@MultipartConfig
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SourceBo sourcebo = new SourceBo();

	public UploadFileServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//get account in the sessions
		Account account = (Account)request.getSession().getAttribute("account");
		
		//Get fileParts
		List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName()))
				.collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">

		for (Part filePart : fileParts) {
			System.out.println(filePart);
			// get fileName
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
			System.out.println(fileName);
			// MSIE fix
			fileName = fileName.substring(0, fileName.length() - 4);
			
			
			// get filecontent
			InputStream fileContent = filePart.getInputStream();
			Source newSource = new Source(fileName, false, account.getUsername());
			sourcebo.save(fileContent, newSource);
		}
		
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/mainform.jsp");
//		dispatcher.forward(request, response);
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/OptionalServlet?index=2"));
	}
	


}
