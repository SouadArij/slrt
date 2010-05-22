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

/**
 *
 * @author Nick
 */
public class XMLWrite {

    private String letter;
    private String word;

    public void generateXMLLetters() {

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/db/letters/letters.txt")));

            try {
                XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
                FileWriter output = new FileWriter(new File("src/db/letters/letters.xml"));
                XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(output);

                xmlStreamWriter.writeStartElement("letters");

                while ((this.letter = br.readLine()) != null) {
                    xmlStreamWriter.writeStartElement("letter");
                    xmlStreamWriter.writeStartElement("name");
                    xmlStreamWriter.writeCharacters(this.letter);
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeStartElement("path");
                    xmlStreamWriter.writeCharacters("src/db/letters/" + this.letter + "/");
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeEndElement();
                }
                br.close();
                xmlStreamWriter.writeEndElement();

                xmlStreamWriter.flush();
                xmlStreamWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


public void generateXMLDictionary() {

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/db/dictionary/dictionary.txt")));

            try {
                XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
                FileWriter output = new FileWriter(new File("src/db/dictionary/dictionary.xml"));
                XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(output);

                xmlStreamWriter.writeStartElement("dictionary");

                while((this.word = br.readLine()) != null) {
                    xmlStreamWriter.writeStartElement("word");
                    xmlStreamWriter.writeStartElement("name");
                    xmlStreamWriter.writeCharacters(this.word);
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeStartElement("path");
                    xmlStreamWriter.writeCharacters("src/db/dictionary/" + this.word + ".jpg");
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeEndElement();
                }
                br.close();
                xmlStreamWriter.writeEndElement();

                xmlStreamWriter.flush();
                xmlStreamWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


