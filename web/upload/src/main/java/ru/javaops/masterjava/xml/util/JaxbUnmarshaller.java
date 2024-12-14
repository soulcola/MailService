package ru.javaops.masterjava.xml.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

public class JaxbUnmarshaller {
    private ThreadLocal<Unmarshaller> unmarshallerThreadLocal;

    public JaxbUnmarshaller(JAXBContext ctx) {
        unmarshallerThreadLocal = new ThreadLocal<Unmarshaller>(){
            @Override
            protected synchronized Unmarshaller initialValue() {
                try {
                    return ctx.createUnmarshaller();
                } catch (JAXBException e) {
                    throw new IllegalStateException("Unable to create unmarshaller");
                }
            }
        };
    }

    public void setSchema(Schema schema) {
        unmarshallerThreadLocal.get().setSchema(schema);
    }

    public Object unmarshal(InputStream is) throws JAXBException {
        return unmarshallerThreadLocal.get().unmarshal(is);
    }

    public Object unmarshal(Reader reader) throws JAXBException {
        return unmarshallerThreadLocal.get().unmarshal(reader);
    }

    public Object unmarshal(String str) throws JAXBException {
        return unmarshal(new StringReader(str));
    }

    public <T> T unmarshal(XMLStreamReader reader, Class<T> elementClass) throws JAXBException {
        return unmarshallerThreadLocal.get().unmarshal(reader, elementClass).getValue();
    }
}