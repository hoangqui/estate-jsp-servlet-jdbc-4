package com.laptrinhweb.controller.admin.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptrinhweb.dto.UserDTO;
import com.laptrinhweb.model.UserModel;
import com.laptrinhweb.utils.HttpUtil;

@WebServlet(urlPatterns=("/api-admin-user"))
public class UserAPI extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
//		HttpUtil.of(req.getReader());
	
		UserDTO userModel = HttpUtil.of(req.getReader()).toModel(UserDTO.class);
		mapper.writeValue(resp.getOutputStream(), userModel);
		System.out.println("user " + userModel);
	}
	
	
}