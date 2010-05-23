package Data;

public class Word {

    private String wordName;
    private String imagePath;

    public Word(String name, String path) {
        this.wordName = name;
        this.imagePath = path;
    }

    public String getName() {
        return this.wordName;
    }

    public String getImagePath() {
        return this.imagePath;
    }
}
