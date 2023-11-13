package ra.usermanager.controller;

import ra.usermanager.model.User;
import ra.usermanager.service.IUserService;
import ra.usermanager.service.UserServiceMPL;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", value = "/UserServlet")
public class UserServlet extends HttpServlet {
   private static final long serialVersionUID =1L;
   private IUserService userService;
   public void init(){
       userService = new UserServiceMPL();
   }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
            }catch (SQLException ex){
       throw new ServletException(ex);
        }

}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertUser(req, resp);
                    break;
                case "edit":
                    updateUser(req, resp);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }

    }
    private void listUser(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException,IOException,ServletException{
       List<User> listUser = userService.selectAllUsers();
       req.setAttribute("listUser",listUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(req, resp);
    }
    private void showNewForm(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
       RequestDispatcher dispatcher = req.getRequestDispatcher("user/create.jsp");
       dispatcher.forward(req, resp);
    }
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
       int id =Integer.parseInt(req.getParameter("id"));
       User existingUser = userService.selectUser(id);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/edit.jsp");
        req.setAttribute("user",existingUser);
        dispatcher.forward(req, resp);
    }
    private void insertUser(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException, ServletException{
       String name = req.getParameter("name");
       String email = req.getParameter("email");
       String country = req.getParameter("country");
       User newUser = new User(name,email,country);
       userService.insertUser(newUser);
       RequestDispatcher dispatcher = req.getRequestDispatcher("user/create.jsp");
       dispatcher.forward(req, resp);
    }
    private void updateUser(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException, ServletException{
       int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");
        User book = new User(id, name, email, country);
        userService.updateUser(book);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/edit.jsp");
        dispatcher.forward(req, resp);
    }
    private void deleteUser(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException, ServletException{
       int id = Integer.parseInt(req.getParameter("id"));
       userService.deleteUser(id);
       List<User> listUser = userService.selectAllUsers();
       req.setAttribute("listUser",listUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void destroy() {

    }
}