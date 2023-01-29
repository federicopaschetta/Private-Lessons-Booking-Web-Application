package ServletApp;

import DAO.*;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "ServletBookLessonApp", value = "/ServletBookLessonApp")
public class ServletBookLessonApp extends HttpServlet {

    private DAO dao = null;
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        ServletContext ctx = config.getServletContext();
        dao = (DAO)ctx.getAttribute("DAO");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String teacherName, teacherSurname, courseTitle, selectedDay, selectedHour;
        int userID, teacherID, courseID;
        Course selectedCourse;
        Teacher selectedTeacher;
        String action = request.getParameter("Action");
        switch (action) {
            case "AvailableTeachers":
                courseTitle = request.getParameter("Course");
                if (courseTitle != null && courseTitle.length() > 0) {
                    selectedCourse = dao.getCourse(dao.getCourseID(courseTitle));
                    ArrayList<Teacher> availableTeachers = dao.getTeachersforCourse(selectedCourse);
                    String availableTeachersString = gson.toJson(availableTeachers);
                    out.print(availableTeachersString);
                } else {
                    out.print(-1);
                }
                break;
            case "AvailableHours": {
                courseTitle = request.getParameter("Course");
                selectedCourse = dao.getCourse(dao.getCourseID(courseTitle));
                String[] selectedTeacherStrings = request.getParameter("Teacher").split(",");
                teacherName = selectedTeacherStrings[1].trim();
                teacherSurname = selectedTeacherStrings[0];
                selectedTeacher = dao.getTeacher(teacherName, teacherSurname);
                selectedDay = request.getParameter("Day");
                ArrayList<Lesson> availableLesson = dao.getLessonsAvailableForTeacherandDay(selectedCourse, selectedTeacher, selectedDay);
                String availableSlots = gson.toJson(availableLesson);
                out.print(availableSlots);
                break;
            }
            case "BookLesson": {
                String username = request.getParameter("Username").toString();
                userID = dao.getUserID(username);
                String[] selectedTeacherStrings = request.getParameter("Teacher").split(",");
                teacherName = selectedTeacherStrings[1].trim();
                teacherSurname = selectedTeacherStrings[0];
                teacherID = dao.getTeacherID(teacherName, teacherSurname);
                courseTitle = request.getParameter("Course");
                courseID = dao.getCourseID(courseTitle);
                selectedHour = request.getParameter("Hour");
                selectedDay = request.getParameter("Day");
                Booking b = new Booking(courseID, teacherID, userID, selectedDay, selectedHour);
                if (dao.insertBooking(b)) {
                    out.print(gson.toJson(true));
                } else {
                    out.print(gson.toJson(false));
                }
                break;
            }
        }
    }
}
