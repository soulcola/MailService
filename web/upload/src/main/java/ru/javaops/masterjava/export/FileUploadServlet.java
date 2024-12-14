package ru.javaops.masterjava.export;

import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static ru.javaops.masterjava.export.ThymeleafListener.engine;

@WebServlet("/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    private final UserExport userExport = new UserExport();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        WebContext webContext = new WebContext(req, res, getServletContext());
        webContext.setVariable("templateEngine", engine);
        engine.process("fileUpload", webContext, res.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, res, getServletContext());
        try {
            Part file = req.getPart("fileToUpload");
            try (InputStream is = file.getInputStream()) {
                List<User> users = userExport.process(is);
                webContext.setVariable("users", users);
                engine.process("view", webContext, res.getWriter());
            }
        } catch (XMLStreamException | JAXBException e) {
            webContext.setVariable("exception", e);
            engine.process("exception", webContext, res.getWriter());
        }
    }
}