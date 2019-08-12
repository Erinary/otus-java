package ru.otus.erinary.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;

public class UserControllerServlet extends HttpServlet {

    private DBService<User> dbService;
    private Gson gson;

    public UserControllerServlet(DBService<User> dbService) {
        super();
        this.dbService = dbService;
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Got 'GET' request");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Got 'POST' request");
        try {
            dbService.create(handleJsonRequest(request.getReader()));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            System.out.println("Internal error: " + e.getMessage());
        }
    }

    private String prepareJsonResponse(List<User> users) {
        String result = gson.toJson(users);
        System.out.println("Prepared json for response: " + result);
        return result;
    }

    private User handleJsonRequest(Reader reader) {
        User result = gson.fromJson(reader, User.class);
        System.out.println("Read json from POST request: " + result);
        return result;
    }
}
