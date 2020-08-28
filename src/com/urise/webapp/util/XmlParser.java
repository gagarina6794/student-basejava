package com.urise.webapp.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.Writer;


public class XmlParser {
    //private final Marshaller marshaller;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller; //тут должно быть файнал

    public XmlParser(Class... classesToBeBound){
        try {
            JAXBContext ctx = JAXBContext.newInstance(classesToBeBound);
            marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            unmarshaller = ctx.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
            //throw new IllegalStateException(e); тут что-то непонятненькое
        }
    }

    public <T> T unmarshall(Reader reader){
        try {
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void marshall(Object instance, Writer writer){
        try {
            marshaller.marshal(instance, writer);
        } catch (JAXBException e) {
           e.printStackTrace();
        }
    }
}
