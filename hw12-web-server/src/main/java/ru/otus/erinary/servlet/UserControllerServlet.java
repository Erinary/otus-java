package ru.otus.erinary.servlet;

import com.google.gson.Gson;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserControllerServlet extends HttpServlet {

    private DBService<User> dbService;

    public UserControllerServlet(DBService<User> dbService) {
        super();
        this.dbService = dbService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        try {
            List<User> users = dbService.loadAll();
            writer.print(prepareJsonResponse(users));
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            writer.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            System.out.println("Internal error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private String prepareJsonResponse(List<User> users) {
        return new Gson().toJson(users);
    }
}
