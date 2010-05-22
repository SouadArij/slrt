/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.stream.*;
import javax.xml.stream.XMLOutputFactory;

/**
 *
 * @author Nick
 */
public class XMLWrite {

    private String letter;
    private String word;

    public void generateXMLLetters() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("db/letters.txt")));

            try {
                XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
                FileWriter output = new FileWriter(new File("db/letters.xml"));
                XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(output);
                xmlStreamWriter.writeStartDocument("UTF-8", "1.0");

                xmlStreamWriter.writeStartElement("letters");

                while ((this.letter = br.readLine()) != null) {
                    xmlStreamWriter.writeStartElement("letter");
                    xmlStreamWriter.writeStartElement("name");
                    xmlStreamWriter.writeCharacters(this.letter);
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeStartElement("path");
                    xmlStreamWriter.writeCharacters("db/letters/" + this.letter + "/");
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeEndElement();
                }
                br.close();
                xmlStreamWriter.writeEndElement();

                xmlStreamWriter.flush();
                xmlStreamWriter.close();

            } catch (FileNotFoundException e1) {

                e1.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();

            }

        } catch (FactoryConfigurationError e) {
        } catch (XMLStreamException e) {
        } catch (IOException e) {
        }
    }

    public void generateXMLDictionary() {

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("db/dictionary.txt")));

            try {
                XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
                FileWriter output = new FileWriter(new File("db/dictionary.xml"));
                XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(output);
                xmlStreamWriter.writeStartDocument("UTF-8", "1.0");

                xmlStreamWriter.writeStartElement("dictionary");

                while ((this.letter = br.readLine()) != null) {
                    xmlStreamWriter.writeStartElement("word");
                    xmlStreamWriter.writeStartElement("name");
                    xmlStreamWriter.writeCharacters(this.letter);
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeStartElement("path");
                    xmlStreamWriter.writeCharacters("db/dictionary/"+this.word+".jpg");
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeEndElement();
                }
                br.close();
                xmlStreamWriter.writeEndElement();

                xmlStreamWriter.flush();
                xmlStreamWriter.close();

            } catch (FileNotFoundException e1) {

                e1.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();

            }

        } catch (FactoryConfigurationError e) {
        } catch (XMLStreamException e) {
        } catch (IOException e) {
        }
    }
}
