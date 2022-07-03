package org.retailstore.market.app.security;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccessDeniedExceptionHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException e) throws IOException, ServletException {
        System.out.println(e.getLocalizedMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);


        List<String> details = new ArrayList<>();


        details.add("Invalid Authorization details");
        try {
            response.getWriter().write(new JSONObject()
                    .put("timestamp", LocalDateTime.now())
                    .put("error", e.getMessage())
                    .put("details", details)
                    .toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
