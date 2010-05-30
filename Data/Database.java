package Data;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
/**
 *
 * @author creata
 */
public class Database {

  
    public static final File LETTERS_XML_FILE = new File("src/db/letters/letters.xml");

    public static final File WORDS_XML_FILE = new File("src/db/dictionary/dictionary.xml");
    private Vector<Letter> letters;
    private Vector<Word> words;

    /**
     * Class contructor
     */
    public Database() {
    }

    /**
     * Parse XMLs of Letters and Words.
     * Then load preprocessed DB images from disk to memory.
     */
    public void load() {
        this.XMLparseLetters();
        this.XMLparseWords();

        //System.out.format("Database/load(): after parsing i have %d letters\n", this.letters.size());
        Iterator<Letter> itlt = this.letters.iterator();
        while (itlt.hasNext()) {
            Letter letter = itlt.next();
            File dirPath = letter.getDirPath();
            File[] files = dirPath.listFiles(new DBImageFileFilter());
            //System.out.format("Database/load(): have %d images for %s letter (filepaths)\n", files.length, letter.getName());
            Vector<DBImage> dbImages = new Vector();
            for (int i = 0; i < files.length; i++) {
                dbImages.add(DBImage.loadFromDisk(files[i]));
            }
            letter.setDBImages(dbImages);
            //System.out.format("Database/load(): finally that letter has %d images\n", letter.getDBImages().size());
        }        
    }

    /**
     * Parse XMLs of Letters and Words.
     * Then process bmp/jpg images from disk and load the processed images to memory.
     */
    public void process() {
        this.XMLparseLetters();
        this.XMLparseWords();

        if (this.letters == null) {
            System.out.format("Error: Database.letters = null\n");
            return;
        }

        Iterator<Letter> itlt = this.letters.iterator();
        while (itlt.hasNext()) {
            Letter letter = itlt.next();
            File dirPath = letter.getDirPath();
            File[] files = dirPath.listFiles(new DBImageFileFilter());
            Vector<DBImage> dbImages = new Vector();
            for (int i = 0; i < files.length; i++) {
                dbImages.add(DBImage.loadRawFromDisk(files[i]));
            }
            letter.setDBImages(dbImages);
        }
    }

    /**
     * Save processed DB images that are in memory to disk.
     */
    public void save() {
        if (this.letters == null) {
            System.out.format("Error: Database.letters = null\n");
            return;
        }

        Iterator<Letter> itlt = letters.iterator();
        while (itlt.hasNext()) {
            Letter letter = itlt.next();
            File dirPath = letter.getDirPath();
            Iterator<DBImage> itim = letter.getDBImages().iterator();
            int i = 0;
            while (itim.hasNext()) {
                DBImage dbIm = itim.next();
                File file = new File(dirPath.getPath() + "/" + i + "dbim");
                dbIm.saveToDisk(file);

                i++;
            }
        }
    }

    private void XMLparseLetters() {
        String letterName = null;
        File dirPath = null;

        this.letters = new Vector();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream input = new FileInputStream(LETTERS_XML_FILE);
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(input);

            while (xmlStreamReader.hasNext()) {
                int event = xmlStreamReader.next();

                if ((event == XMLStreamConstants.START_ELEMENT) && (xmlStreamReader.getLocalName().equals("letter"))) {
                    event = xmlStreamReader.next();
                    event = xmlStreamReader.next();
                    if (xmlStreamReader.getLocalName().equals("name"))//I am in a letter
                    {
                        event = xmlStreamReader.next();//use next to get the next atribute (name)
                        letterName = xmlStreamReader.getText();//nu intreba de cate ori ca nu iti pot explica
                        event = xmlStreamReader.next();//don't ask why
                        event = xmlStreamReader.next();//to get to path
                        event = xmlStreamReader.next();
                    }

                    if (xmlStreamReader.getLocalName().equals("path"))//I am in a letter
                    {
                        event = xmlStreamReader.next();//use next to get the next atribute (path)
                        dirPath = new File(xmlStreamReader.getText());
                    }
                    //add the letter to the vector;
                    this.letters.add(new Letter(letterName, dirPath));
                }//end of adding letter

            }//end while for letter
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void XMLparseWords() {
        String wordName = null;
        File imagePath = null;

        this.words = new Vector();
        try {
            XMLInputFactory inputFactory2 = XMLInputFactory.newInstance();
            InputStream input2 = new FileInputStream(WORDS_XML_FILE);
            XMLStreamReader xmlStreamReader2 = inputFactory2.createXMLStreamReader(input2);

            while (xmlStreamReader2.hasNext()) {
                int event = xmlStreamReader2.next();

                if ((event == XMLStreamConstants.START_ELEMENT) && (xmlStreamReader2.getLocalName().equals("word"))) {
                    event = xmlStreamReader2.next();
                    event = xmlStreamReader2.next();
                    if (xmlStreamReader2.getLocalName().equals("name")) {
                        event = xmlStreamReader2.next();
                        wordName = xmlStreamReader2.getText();
                        event = xmlStreamReader2.next();
                        event = xmlStreamReader2.next();
                        event = xmlStreamReader2.next();
                    }

                    if (xmlStreamReader2.getLocalName().equals("path")) {
                        event = xmlStreamReader2.next();
                        imagePath = new File(xmlStreamReader2.getText());
                    }


                    BufferedImage picture = null;
                    try {
                        picture = ImageIO.read(imagePath);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    this.words.add(new Word(wordName, picture));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   /**
    * Generates the Xml that contains the information for Letter objects
    */
    public static void generateXMLLetters() {
        String letter;

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/db/letters/letters.txt")));

            try {
                XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
                FileWriter output = new FileWriter(LETTERS_XML_FILE);
                XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(output);

                xmlStreamWriter.writeStartElement("letters");

                while ((letter = br.readLine()) != null) {
                    xmlStreamWriter.writeStartElement("letter");
                    xmlStreamWriter.writeStartElement("name");
                    xmlStreamWriter.writeCharacters(letter);
                    xmlStreamWriter.writeEndElement();
                    output.write("\n", 0, new String("\n").length());
                    xmlStreamWriter.writeStartElement("path");
                    xmlStreamWriter.writeCharacters("src/db/letters/" + letter + "/");
                    xmlStreamWriter.writeEndElement();
                    output.write("\n", 0, new String("\n").length());
                    xmlStreamWriter.writeEndElement();
                    output.write("\n", 0, new String("\n").length());
                }
                br.close();
                xmlStreamWriter.writeEndElement();
                output.write("\n", 0, new String("\n").length());

                xmlStreamWriter.flush();
                xmlStreamWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates the Xml that contains the information for Dictionary objects
     */
    public static void generateXMLDictionary() {
        String word;

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/db/dictionary/dictionary.txt")));
            try {
                XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
                FileWriter output = new FileWriter(WORDS_XML_FILE);
                XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(output);

                xmlStreamWriter.writeStartElement("dictionary");

                while ((word = br.readLine()) != null) {
                    xmlStreamWriter.writeStartElement("word");
                    xmlStreamWriter.writeStartElement("name");
                    xmlStreamWriter.writeCharacters(word);
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeStartElement("path");
                    xmlStreamWriter.writeCharacters("src/db/dictionary/" + word + ".jpg");
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

    /**
     * Gets the Vector cotaining all the Letters parsed
     * @return the Vector cotaining all the Letters parsed
     */
    public Vector<Letter> getLetters() {
        return this.letters;
    }

    /**
     * Gets  the Vector cotaining all the Words parsed
     * @return  the Vector cotaining all the Words parsed
     */
    public Vector<Word> getWords() {
        return this.words;
    }
/**
 * 
 * @param id
 * @return
 */
    public String id2String(int id) {
        if (this.letters == null) {
            return "_";
        }
        if (id - 1 < 0 || id - 1 > this.letters.size() - 1) {
            return "_";
        }
        return this.letters.get(id - 1).getName();
    }
}
