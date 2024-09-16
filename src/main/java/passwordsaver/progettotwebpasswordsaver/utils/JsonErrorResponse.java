package passwordsaver.progettotwebpasswordsaver.utils;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JsonErrorResponse {
    public static void sendJsonError(HttpServletResponse response, int statusCode, String message, String detailedMessage) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);

        PrintWriter out = response.getWriter();
        JsonObject res = new JsonObject();

        res.addProperty("error", message);
        res.addProperty("message", detailedMessage);
        out.println(res);
        out.flush();
    }
}
