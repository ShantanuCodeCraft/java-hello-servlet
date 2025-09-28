package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class HelloServletTest {

    private HelloServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new HelloServlet();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        Mockito.when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet() throws Exception {
        servlet.doGet(request, response);

        writer.flush(); // flush writer to stringWriter
        String result = stringWriter.toString();

        assertTrue(result.contains("Hello World 🚀 from Java Servlet!"),
            "Servlet should return Hello World message");
    }
}
