package Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.stream.*;

public class Database {

    private static File LETTERS_XML_FILE = new File("db/letters.xml");
    private static File WORDS_XML_FILE = new File("db/dictionary.xml");

    private Vector<Letter> letters;
    private Vector<Word> words;

    public Database() {
        this.letters = null;
        this.words = null;
    }

    /*
     * Parse XMLs of Letters and Words.
     * Then load preprocessed DB images from disk to memory.
     */
    public void load() {
        Letter letter = null;
        File dirPath;
        File[] files;
        Vector<DBImage> dbImages = new Vector();
        int i;

        this.XMLparseLetters();
        this.XMLparseWords();

        Iterator<Letter> itlt = letters.iterator();
        while (itlt.hasNext()) {
            letter = itlt.next();
            dirPath = letter.getDirPath();
            files = dirPath.listFiles(new DBImageFileFilter());
            for (i = 0; i < files.length; i++) {
                dbImages.add(DBImage.loadFromDisk(files[i]));
            }
            letter.setDBImages(dbImages);
        }
    }

    /*
     * Parse XMLs of Letters and Words.
     * Then process bmp/jpg images from disk and load the processed images to memory.
     */
    public void process() {
        Letter letter = null;
        File dirPath;
        File[] files;
        Vector<DBImage> dbImages = new Vector();
        int i;

        this.XMLparseLetters();
        this.XMLparseWords();

        Iterator<Letter> itlt = letters.iterator();
        while (itlt.hasNext()) {
            letter = itlt.next();
            dirPath = letter.getDirPath();
            files = dirPath.listFiles(new DBImageFileFilter());
            for (i = 0; i < files.length; i++) {
                dbImages.add(DBImage.loadRawFromDisk(files[i]));
            }
            letter.setDBImages(dbImages);
        }
    }

    /*
     * Save processed DB images that are in memory to disk.
     */
    public void save() {
        Letter letter = null;
        DBImage dbIm;
        File file;
        int i = 0;
        File dirPath;

        Iterator<Letter> itlt = letters.iterator();
        while (itlt.hasNext()) {
            letter = itlt.next();
            dirPath = letter.getDirPath();
            Iterator<DBImage> itim = letter.getDBImages().iterator();
            while (itim.hasNext()) {
                dbIm = itim.next();
                file = new File(dirPath.getPath() + "/" + i + "dbim");
                dbIm.saveToDisk(file);
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
        String imagePath = null;

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

                    if (xmlStreamReader2.getLocalName().equals("path"))
                    {
                        event = xmlStreamReader2.next();
                        imagePath = xmlStreamReader2.getText();
                    }                    
                    this.words.add(new Word(wordName, imagePath));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Vector<Letter> getLetters() {
        return this.letters;
    }

    public Vector<Word> getMyImages() {
        return this.words;
    }
}
