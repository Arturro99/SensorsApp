package com.g09;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OperateOnXml {

    public void saveToXmlFile(String name, Object obj) throws IOException {
        XStream xStream = new XStream(new DomDriver());
        File file = new File(name);
        String xml = xStream.toXML(obj);

        file.createNewFile();
        FileWriter xmlStream = new FileWriter(file);
        xmlStream.write(xml.toString());
        xmlStream.close();
    }

    public Object loadFromXmlFile(String name, Object obj) {
        XStream xstream = new XStream(new DomDriver());
        File file = new File(name);
        obj = (Object) xstream.fromXML(file);
        return obj;
    }
}
