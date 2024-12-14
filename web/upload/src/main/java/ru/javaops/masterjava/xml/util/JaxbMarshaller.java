package ru.javaops.masterjava.xml.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.validation.Schema;
import java.io.StringWriter;
import java.io.Writer;

public class JaxbMarshaller {
    private ThreadLocal<Marshaller> marshallerThreadLocal;

    public JaxbMarshaller(JAXBContext ctx) throws PropertyException {
        marshallerThreadLocal = ThreadLocal.withInitial(() -> {
            try {
                return ctx.createMarshaller();
            } catch (JAXBException e) {
                throw new IllegalStateException("Unable to create Unmarshaller", e);
            }
        });
        marshallerThreadLocal.get().setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerThreadLocal.get().setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshallerThreadLocal.get().setProperty(Marshaller.JAXB_FRAGMENT, true);
    }

    public void setProperty(String prop, Object value) throws PropertyException {
        marshallerThreadLocal.get().setProperty(prop, value);
    }

    public void setSchema(Schema schema) {
        marshallerThreadLocal.get().setSchema(schema);
    }

    public String marshal(Object instance) throws JAXBException {
        StringWriter sw = new StringWriter();
        marshal(instance, sw);
        return sw.toString();
    }

    public void marshal(Object instance, Writer writer) throws JAXBException {
        marshallerThreadLocal.get().marshal(instance, writer);
    }

}
