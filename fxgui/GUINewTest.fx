package fxgui;
import javafx.scene.Scene;
import javafx.animation.Timeline;
import javafx.lang.FX;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import javafx.scene.effect.*;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import SLRTr.SLRTModel;
import java.util.Observable;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.awt.image.BufferedImage;
                            
public class GUINewTest extends GUIInterface {

    var om: SLRTModel;
    var currImg: Image;
    var highlightedButtons: Boolean[];
    var pressedButtons : Boolean[];
    var img1:Image = Image {url: "{__DIR__}icons/closefirst.png"};
    var img2:Image = Image {url: "{__DIR__}icons/closesecond.png"};
    var img4:Image = Image {url: "{__DIR__}icons/playfirst.png"};
    var img5:Image = Image {url: "{__DIR__}icons/playsecond.png"};
    var img7:Image = Image {url: "{__DIR__}icons/stopfirst.png"};
    var img8:Image = Image {url: "{__DIR__}icons/stopsecond.png"};
    var img10:Image = Image {url: "{__DIR__}icons/nextwordfirst.png"};
    var img11:Image = Image {url: "{__DIR__}icons/nextwordsecond.png"};
    var img13:Image = Image {url: "{__DIR__}icons/backfirst.png"};
    var img14:Image = Image {url: "{__DIR__}icons/backsecond.png"};
    var imagePlay: Image;
    var imagePause: Image = img4;
    var imageStop: Image;
    var imageNext: Image = img10;
    var imageBack: Image = img13;
    var imageButtons: Image[];
    var string: String;
    var currentLetter: String;
    var fillColor: Color;
    var wordImage: BufferedImage;
    var processedImage: Image;
    



    //function that simulates the java constructor
    public override function GUI(model: SLRTModel) {
        this.om = model;
        this.imageButtons[0] = this.img4;
        this.imageButtons[1] = this.img10;
        this.imageButtons[2] = this.img13;
        this.imageButtons[3] = this.img7;
        //this.string = "";
     }

   /*
   public override function setDisplayedString(displayedWord : String ,  corect : Boolean) {
            this.string = displayedWord;
            this.correct = correct;
    }

    /*
    public override function setWordImage(wordImage: BufferedImage) {
        this.wordImage = wordImage;
    }*/

    
    public override function update(ob: Observable, args: Object){
    }

    //function for updating the button image when movement detected. Returns the array of images for all buttons
    function updateButtons(hB: Boolean[]):Image[]{
            var img:Image[];
            if (hB[0]){
                img[0] = img5;
                }
            else{
                img[0] = img4;
                }
            if (hB[1]){
                img[1] = img11;
                }
            else {
                img[1] = img10;
                }
            if (hB[2]){
                img[2] = img14;
                }
            else {
                img[2] = img13;
                }
            if (hB[3]){
                img[3] = img8;
                }
            else {
                img[3] = img7;
                }
            return img;
         }

    //function to return the boolean array that gives which button has been highlighted from the SLRTModel
    function getHighlightedButtons():Boolean[]
    {
        var result: Boolean[];
        var i: java.lang.Integer = 0;
        var hl: Boolean[] = om.getHighlightsFromMovementBrain();
        
        if (hl == null) {
            return result;
        }

        for (nr in hl) {
            result[i] = nr;
            i++;
        }

        return result;
    }
    
    //gets the pressed buttons from movementBrain
     function getPressedButtons():Boolean[]
    {
        var result:Boolean[];
        var i: java.lang.Integer = 0;
        for (nr in om.getPressedFromMovementBrain()){
            result[i] = nr;
            i++;}
        return result;

    }

    function isThisPossible() : Object {
        var buffIm : BufferedImage = om.getProcessedImageFromBrain();
        if (buffIm == null) {
            FX.print("FX:  processed image from brain is null;\n");
            return null;
        } else {
            FX.print("FX:  processed image from brain has changed: ");
            var hashCode : Integer = om.getProcessedImageFromBrain().hashCode();
            FX.print(hashCode.toString());
            FX.print("\n");
            this.processedImage => javafx.ext.swing.SwingUtils.toFXImage(om.getProcessedImageFromBrain());
        }

        return null;
    }


 


    public override function myRun() {
            var notUsed : Object;

   //timeline to define the way FX gets its values from SLRTModel and updates the Scene
   Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [at(0s)
            {
            this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible();
            },
                at(10s)
            {
            this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(20s)
            {
            this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(30s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(40s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(50s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(60s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(70s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(80s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(90s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(100s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(110s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(120s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(130s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); },
                at(140s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            this.string => om.getBuildingWord();
            this.currentLetter => om.getCurrentLetter();
            this.wordImage => om.getWordImage();
            notUsed => isThisPossible(); }]
    }.play();

    var sceneX : Number = 300;
    var sceneY : Number = 200;

    //path for drawing the frame where the sign must be made
    def path = [MoveTo {x: 350 y: 150} LineTo {x: 350 y: 350} LineTo {x: 617 y: 350} LineTo {x: 617 y: 150} LineTo {x: 350 y: 150}];

    //Image view for the webcam image to be displayed
    var webcamView: ImageView = ImageView {
                layoutX: 3
                layoutY: 40.0
                scaleX: -1.0
                image:bind currImg;
                fitWidth: 640
                fitHeight: 480
                preserveRatio: true
                smooth: true
                cache: true
    }

    //Rectangle to be displayed on the top of the webcam image
    var upRect : Rectangle = Rectangle {
        y: 0
        height: 40
        width: 646
        fill: LinearGradient {
            startX: 0.0, startY: 0.0, endX: 0.0, endY: 1.0
            proportional: true
            stops: [
                Stop { offset: 0.0 color: Color.DARKSLATEGRAY },
                Stop { offset: 0.5 color: Color.BLACK }
            ]
        }

        onMouseDragged: function( e: MouseEvent ):Void {
            sceneX += e.dragX;
            sceneY += e.dragY;
        }
    }
        var leftRect : Rectangle = Rectangle {
        y: 40
        height: 480
        width: 3
    }

        var rightRect : Rectangle = Rectangle {
        x: 643
        y: 40
        height: 480
        width: 3
    }

        var downRect : Rectangle = Rectangle {
        y: 520
        height: 40
        width: 646
        fill: LinearGradient {
            startX: 0.0, startY: 1.0, endX: 0.0, endY: 0.0
            proportional: true
            stops: [
                Stop { offset: 0.0 color: Color.DARKSLATEGREY },
                Stop { offset: 0.5 color: Color.BLACK }
            ]
        }
    }

            var buttonRect : Rectangle = Rectangle {
        y: 50
        x: 10
        height: 80
        width: 80
        fill: LinearGradient {
            startX: 0.0, startY: 0.0, endX: 0.0, endY: 1.0
            proportional: true
            stops: [
                Stop { offset: 0.5 color: Color.BLACK },
                Stop { offset: 0.9 color: Color.BLACK }
            ]
        }
    }



    var rail: Path = Path {
    elements: path
    stroke: Color.web("#99ccff");
    strokeWidth: 1
    strokeDashArray: [ 2 ]
    strokeDashOffset: 1
    effect: DropShadow{}
    visible: bind highlightedButtons[0] //visible only when play button is highlighted
}

     var closeImage: ImageView = ImageView {
        layoutX: 600
        layoutY: -5
        scaleX: 0.8
        scaleY: 0.8
        image: bind img1
        fitWidth: 50
        fitHeight: 50
        opacity: 0.7
        onMouseEntered: function (e){
        //img3 = img2;
        }
        onMouseExited: function(e){
       // img3 = img1;
        }
        onMouseClicked: function(e) {
            FX.exit();
        }
    }

    var playImage: ImageView = ImageView {
        layoutX: 10
        layoutY: 44
        image: bind imageButtons[0]
        fitWidth: 80
        fitHeight: 80
        opacity: 0.6
    }


    var stopImage: ImageView = ImageView {
        layoutX: 555
        layoutY: 44
        image: bind imageButtons[3]
        fitWidth: 80
        fitHeight: 80
        opacity: 0.6
        }

    var nextWordImage: ImageView = ImageView {
        layoutX: 195
        layoutY: 44
        image: bind imageButtons[1]
        fitWidth: 80
        fitHeight: 80
        opacity: 0.6
    }


    //var img15:Image = img13;
    var backImage: ImageView = ImageView {
        layoutX: 380
        layoutY: 44
        image: bind imageButtons[2]
        fitWidth: 80
        fitHeight: 80
        opacity: 0.6
    }

    var processedImage: ImageView = ImageView {
        layoutX: 80;
        layoutY: 60;
        image: bind this.processedImage;
        fitWidth: 80;
        fitHeight: 60;
    }

    var word: Text = Text {
        x: 120
        y: 467
        font: Font {
            name: "Comic Sans MS"
            size: 50
        }
        fill: Color.SKYBLUE
        stroke: Color.DARKBLUE
        strokeWidth: 0.4
        effect: DropShadow{}
        content: bind this.string
        visible: bind highlightedButtons[0] //visible only when play button is highlighted
        //opacity: 0.95
    }

        var currentLetter: Text = Text {
        x: 120
        y: 467
        font: Font {
            name: "Comic Sans MS"
            size: 50
        }
        fill: Color.rgb(242, 57, 104)
        stroke: Color.DARKRED
        strokeWidth: 0.4
        effect: DropShadow{}
        content: bind this.currentLetter
        visible: bind highlightedButtons[0] //visible only when play button is highlighted
        //opacity: 0.95
    }

    var imageWord : ImageView = ImageView {
        layoutX: 13
        layoutY: 400
        scaleX: 0.8
        scaleY: 0.8
        opacity: 0.76
        effect: DropShadow{}
        image: bind javafx.ext.swing.SwingUtils.toFXImage(this.wordImage);
        visible: bind highlightedButtons[0] //visible only when play button is highlighted
        }

    var wordGrid : Rectangle = Rectangle {
        x: 110
        y: 409
        width: 515
        height: 84
        arcWidth:10
        arcHeight:10
        fill: Color.GREY//WHITESMOKE
        opacity: 0.2
        effect: DropShadow{}
        visible: bind highlightedButtons[0] //visible only when play button is highlighted
        
    }

    /*var menuBar: MenuBar = MenuBar {

    }*/


    var scene : Scene = Scene {
    content: [
        webcamView, upRect, rail ,downRect, leftRect,
        rightRect, closeImage, playImage, stopImage,
        nextWordImage, backImage, wordGrid, word, imageWord,

        processedImage
        ]
    };

    

    Stage {
        x: bind sceneX
        y: bind sceneY
        resizable: false
        style: StageStyle.UNDECORATED
        width: 646
        height: 560
        scene: scene
    }
  }
}