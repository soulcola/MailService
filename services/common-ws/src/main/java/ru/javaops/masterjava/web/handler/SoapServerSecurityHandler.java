package ru.javaops.masterjava.web.handler;


import com.google.common.io.Resources;
import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import com.sun.xml.ws.api.handler.MessageHandlerContext;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.typesafe.config.Config;
import jakarta.xml.ws.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.web.AuthUtil;
import ru.javaops.masterjava.web.WsClient;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Refactored from:
 *
 * @see {http://weblogs.java.net/blog/ramapulavarthi/archive/2007/12/extend_your_web.html
 * http://fisheye5.cenqua.com/browse/jax-ws-sources/jaxws-ri/samples/efficient_handler/src/efficient_handler/common/LoggingHandler.java?r=MAIN}
 * <p/>
 * This simple LoggingHandler will log the contents of incoming
 * and outgoing messages. This is implemented as a MessageHandler
 * for better performance over SOAPHandler.
 */
@Slf4j
public class SoapServerSecurityHandler extends SoapBaseHandler {

    private static Config HOSTS;
    public static String USER;
    public static String PASSWORD;

    static {
        HOSTS = Configs.getConfig("hosts.conf", "hosts").getConfig("mail");
        USER = HOSTS.getString("user");
        PASSWORD = HOSTS.getString("password");

    }

    public static String AUTH_HEADER = AuthUtil.encodeBasicAuthHeader(USER, PASSWORD);

    @Override
    public boolean handleMessage(MessageHandlerContext mhc) {
        Map<String, List<String>> headers = (Map<String, List<String>>) mhc.get(MessageContext.HTTP_REQUEST_HEADERS);

        int code = AuthUtil.checkBasicAuth(headers, AUTH_HEADER);
        if (code != 0) {
            mhc.put(MessageContext.HTTP_RESPONSE_CODE, code);
            throw new SecurityException();
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext mhc) {
        return true;
    }
}
