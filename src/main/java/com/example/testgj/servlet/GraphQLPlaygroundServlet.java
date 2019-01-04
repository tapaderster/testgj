package com.example.testgj.servlet;

import com.google.common.io.Resources;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

public class GraphQLPlaygroundServlet extends HttpServlet {

  private String html = Resources.toString(Resources.getResource(
      "graphql-playground.html"
      ), Charset.defaultCharset()
  );

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setStatus(HttpServletResponse.SC_OK);
    resp.getWriter().print(html);
  }

  public GraphQLPlaygroundServlet() throws IOException {
  }
}
