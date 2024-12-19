package ru.javaops.masterjava.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10) //10 MB in memory limit
public class UploadServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UploadServlet.class);
    private final UserProcessor userProcessor = new UserProcessor();
    private static final int BATCH_SIZE = 1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        webContext.setVariable("batchSize", BATCH_SIZE);
        engine.process("upload", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int batchSize = BATCH_SIZE;
        String message;
        try {
//            http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html
            batchSize = Integer.parseInt(req.getParameter("batchSize"));
            if (batchSize <= 0) {
                message = "Batch size must be greater than 0";
            } else {
                Part filePart = req.getPart("fileToUpload");
                try (InputStream is = filePart.getInputStream()) {
                    List<UserProcessor.FailedEmail> failed = userProcessor.processMulti(is, batchSize);
                    final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
                    webContext.setVariable("failed", failed);
                    engine.process("result", webContext, resp.getWriter());
                    return;
                }
            }
            doExport(req, resp, message, batchSize);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            message = e.toString();
            doExport(req, resp, message, batchSize);
        }
    }

    private void doExport(HttpServletRequest req, HttpServletResponse resp, String message, int batchSize) throws IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        webContext.setVariable("message", message);
        webContext.setVariable("batchSize", batchSize);
        engine.process("upload", webContext, resp.getWriter());
    }
}
