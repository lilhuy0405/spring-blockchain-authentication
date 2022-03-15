package com.fcs.marathonbademo.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcs.marathonbademo.entity.User;
import com.fcs.marathonbademo.repository.UserRepository;
import com.fcs.marathonbademo.util.JwtUtil;
import com.fcs.marathonbademo.util.RESTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Order(2)
public class AuthorizeFilter implements Filter {
    private final UserRepository userRepository;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String requestUrl = req.getRequestURI();
        if (requestUrl.endsWith("/")) {
            requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
        }
        String requestMethod = req.getMethod();

        //check public url here
        if (requestMethod.equals("POST") && requestUrl.equals("/auth")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // get jwt token and decode to get username and check role of user in database
        String authorizationHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            resp.sendError(HttpStatus.FORBIDDEN.value(), "no jwt token found");
            return;
        }
        try {
            String token = authorizationHeader.replace("Bearer", "").trim();
            DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(token);
            String address = decodedJWT.getSubject();
            Optional<User> byUsername = userRepository.findByAddress(address);
            if (!byUsername.isPresent()) {
                throw new Exception("Wrong token");
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception ex) {
            //show error
            System.err.println(ex.getMessage());
            resp.setStatus(HttpStatus.FORBIDDEN.value());
            HashMap<String, Object> errorBody = new RESTResponse.CustomError()
                    .setCode(HttpStatus.FORBIDDEN.value())
                    .setMessage(ex.getMessage())
                    .build();
            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(resp.getOutputStream(), errorBody);
            return;
        }

    }
}
