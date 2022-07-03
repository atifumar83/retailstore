package org.retailstore.market.app.security;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UnauthorizedExceptionHandler implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);


        List<String> details = new ArrayList<>();


        details.add("Invalid Authorization details");
        try {
            response.getWriter().write(new JSONObject()
                    .put("timestamp", LocalDateTime.now())
                    .put("error", authException.getMessage())
                    .put("details", details)
                    .toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
