package ru.javaops.masterjava.web;

import com.typesafe.config.Config;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceFeature;
import org.slf4j.event.Level;
import ru.javaops.masterjava.ExceptionType;
import ru.javaops.masterjava.config.Configs;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.Map;

public class WsClient<T> {

    private static Config HOSTS;
    private final Class<T> serviceClass;
    private final Service service;
    private HostConfig hostConfig;


    public static class HostConfig {
        public final String endpoint;
        public final Level clientDebugLevel;
        public final Level serverDebugLevel;
        public final String user;
        public final String password;
        public final String authHeader;

        public HostConfig(Config config, String endpointAddress) {
            endpoint = config.getString("endpoint") + endpointAddress;
            clientDebugLevel = config.getEnum(Level.class, "client.debugLevel");
            serverDebugLevel = config.getEnum(Level.class, "server.debugLevel");
            user = config.getString("user");
            password = config.getString("password");
            authHeader = (user != null && password != null) ? AuthUtil.encodeBasicAuthHeader(user, password) : null;
        }

        public boolean hasAuth() {
            return authHeader != null;
        }
    }

    static {
        HOSTS = Configs.getConfig("hosts.conf", "hosts");
    }

    public WsClient(URL wsdlUrl, QName qname, Class<T> serviceClass) {
        this.serviceClass = serviceClass;
        this.service = Service.create(wsdlUrl, qname);
    }

    public void init(String host, String endpointAddress) {
        this.hostConfig = new HostConfig(HOSTS.getConfig(host), endpointAddress);
    }

    public HostConfig getHostConfig() {
        return hostConfig;
    }

    //  Post is not thread-safe (http://stackoverflow.com/a/10601916/548473)
    public T getPort(WebServiceFeature... features) {
        T port = service.getPort(serviceClass, features);
        BindingProvider bp = (BindingProvider) port;
        Map<String, Object> requestContext = bp.getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, hostConfig.endpoint);
        if (hostConfig.hasAuth()) {
            setAuth(port, hostConfig.user, hostConfig.password);
        }
        return port;
    }

    public static <T> void setAuth(T port, String user, String password) {
        Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.put(BindingProvider.USERNAME_PROPERTY, user);
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, password);
    }

    public static WebStateException getWebStateException(Throwable t, ExceptionType type) {
        return (t instanceof WebStateException) ? (WebStateException) t : new WebStateException(t, type);
    }
}
