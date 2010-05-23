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
import slrt.OpticalModel;
import java.util.Observable;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.awt.image.BufferedImage;
                            
public class GUINewTest extends GUIInterface {

    var om: OpticalModel;
    var currImg: Image;
    var highlightedButtons: Boolean[];
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
    var string: String = "";
    var correct: Boolean;
    var fillColor: Color;
    var wordImage: BufferedImage;




   
    public override function GUI(model: OpticalModel) {
        this.om = model;
        this.imageButtons[0] = this.img4;
        this.imageButtons[3] = this.img7;
        this.string = "";
     }

    public override function setDisplayedString(displayedWord : String ,  corect : Boolean) {
            this.string = displayedWord;
            this.correct = correct;
    }

    public override function setWordImage(wordImage: BufferedImage) {
        this.wordImage = wordImage;
    }

    public override function update(ob: Observable, args: Object){
            //var ceva: java.lang.Byte = bind args as java.lang.Byte;
           // if (args == 4)
           /// this.indexOfButtonPressed = om.getIndexOfButtonFromMovementBrain();
            //updateButtons();
            //println("Value of arg passed: {this.indexOfButtonPressed}");
            }
    function updateButtons(hB: Boolean[]):Image[]{
            var img:Image[];
            if (hB[0]){
                img[0] = img5;
                }
            else{
                img[0] = img4;
                }

            if (hB[3]){
                img[3] = img8;
                }
            else{
                img[3] = img7;
                }
            return img;
         }

    function getHighlightedButtons():Boolean[]
    {
        var result:Boolean[];
        var i: java.lang.Integer = 0;
        for (nr in om.getResultFromMovementBrain()){
            result[i] = nr;
            i++;}
        return result;

    }

    public override function run() {
   // println("Value of arg passed image initial: {this.imagePlay}");

   Timeline {
    repeatCount: Timeline.INDEFINITE
    keyFrames: [at(0s)
            {
            this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(10s)
            {
            this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(20s)
            {
            this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(30s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(40s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(50s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(60s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(70s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(80s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(90s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(100s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(110s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(120s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(130s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            },
                at(140s)
            {
            currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
            this.highlightedButtons => getHighlightedButtons();
            this.imageButtons => updateButtons(this.highlightedButtons);
            }]
    }.play();
    


    def path = [
    MoveTo {
        x: 350
        y: 150
    }
    LineTo {
        x: 350
        y: 350
    }

        LineTo {
        x: 617
        y: 350
    }

        LineTo {
        x: 617
        y: 150
    }
           LineTo {
        x: 350
        y: 150
    }
];

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

    var sceneX : Number = 300;
    var sceneY : Number = 200;
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
        y: 515
        height: 40
        width: 646
        fill: LinearGradient {
            startX: 0.0, startY: 0.0, endX: 0.0, endY: 1.0
            proportional: true
            stops: [
                Stop { offset: 0.5 color: Color.BLACK },
                Stop { offset: 0.9 color: Color.BLACK }
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
}
/*
    var bigRectangle : Rectangle = Rectangle {
        y: 0
        height: 202
        width: 268
        styleClass: "dashedRectangle"

    }

    var smallRectangle : Rectangle = Rectangle {
        x: 1
        y: 1
        height: 200
        width: 266
    }

    var border = ShapeSubtract {
            layoutX: 350
            layoutY: 100
            fill: Color.DARKRED
            a: bigRectangle
           
            effect: DropShadow {}
           
    }*/

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
        //scaleX: 2.0
        //scaleY: 2.0
        image: bind imageButtons[0]
        fitWidth: 80
        fitHeight: 80
        opacity: 0.6
    }


    var stopImage: ImageView = ImageView {
        layoutX: 555
        layoutY: 44
        //scaleX: 2.0
        //scaleY: 2.0
        image: bind imageButtons[3]
        fitWidth: 80
        fitHeight: 80
        opacity: 0.6
        }

   var img12:Image = img10;
    var nextWordImage: ImageView = ImageView {
        layoutX: 170
        layoutY: 44
        //scaleX: 2.0
        //scaleY: 2.0
        image: bind img12
        fitWidth: 80
        fitHeight: 80
        opacity: 0.6
        onMouseEntered: function (e){
        img12 = img11;
        }
        onMouseExited: function(e){
        img12 = img10;
        }
    }


    var img15:Image = img13;
    var backImage: ImageView = ImageView {
        layoutX: 320
        layoutY: 44
        //scaleX: 2.0
        //scaleY: 2.0
        image: bind img15
        fitWidth: 80
        fitHeight: 80
        opacity: 0.6
        onMouseEntered: function (e){
        img15 = img14;
        }
        onMouseExited: function(e){
        img15 = img13;
        }
    }

    setDisplayedString(string, correct);

    if (correct == true) {
        fillColor = Color.LIGHTSEAGREEN;
    }
    else {
        fillColor = Color.RED;
    }


    
    var word: Text = Text {
        x: 250
        y: 450
        font: Font {
            name: "Comic Sans MS"
            size: 50
        }
        fill: bind fillColor
        stroke: Color.WHITE
        strokeWidth: 0.5
        content: bind this.string
    }

    setWordImage(wordImage);

    var imageWord : ImageView = ImageView {
        layoutX: 80
        layoutY: 360
        scaleX: 0.8
        scaleY: 0.8
        image: bind javafx.ext.swing.SwingUtils.toFXImage(this.wordImage);
        }


    var scene : Scene = Scene {
    stylesheets: "{__DIR__}dashedRectangle.css";
    content: [
        webcamView, upRect, rail ,downRect, leftRect, rightRect, closeImage, playImage, stopImage,
        nextWordImage, backImage, word, imageWord
        ]
    };

    

    Stage {
        //title: "Application title"
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