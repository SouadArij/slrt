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

    var img3: Image;
    var visibleMenu: Boolean;
    var onFileSelect: Boolean;
    var exitFileMenu: Boolean;
    var onEditSelect: Boolean;
    var settingsEditMenu: Boolean;
    var onMenuSelect: Boolean;
    var playMenuMenu: Boolean;
    var stopMenuMenu: Boolean;
    var nextWordMenuMenu: Boolean;
    var clearLetterMenuMenu: Boolean;
    var onHelpSelect: Boolean;
    var viewHelpHelpMenu: Boolean;
    var aboutHelpMenu: Boolean;
    var menuButtonsPressed: Boolean[];

    var processedImage: Image;


    //function that simulates the java constructor
    public override function GUI(model: SLRTModel) {
        this.om = model;
        this.imageButtons[0] = this.img4;
        this.imageButtons[1] = this.img10;
        this.imageButtons[2] = this.img13;
        this.imageButtons[3] = this.img7;
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
            keyFrames: [
                at(0s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(10s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(20s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(30s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(40s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(50s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(60s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(70s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(80s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(90s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(100s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(110s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(120s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(130s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                },
                at(140s) {
                    this.currImg => javafx.ext.swing.SwingUtils.toFXImage(om.getCapturedImageFromEye());
                    this.highlightedButtons => getHighlightedButtons();
                    this.imageButtons => updateButtons(this.highlightedButtons);
                    this.string => om.getBuildingWord();
                    this.currentLetter => om.getCurrentLetter();
                    this.wordImage => om.getWordImage();
                }
         ]
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

    //application grid + close button
    var grid : Group = Group {
        content: [
                Rectangle { //up rectangle
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
                Rectangle { //left rectangle
                        y: 40
                        height: 480
                        width: 3
                }
                Rectangle { //right rectangle
                        x: 643
                        y: 40
                        height: 480
                        width: 3
                }
                Rectangle { //down rectangle
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
                ImageView { //close button
                        layoutX: 600
                        layoutY: -5
                        scaleX: 0.8
                        scaleY: 0.8
                        image: bind img1
                        fitWidth: 50
                        fitHeight: 50
                        opacity: 0.7
                        onMouseEntered: function (e){
                            img3 = img1;
                            img1 = img2;
                        }
                        onMouseExited: function(e){
                            img1 = img3;
                        }
                        onMouseClicked: function(e) {
                            FX.exit();
                        }
                    }
        ]
    }

    //the play, next word, back and stop buttons
    var appButtons: Group = Group {
        content: [
                ImageView { //play button
                        layoutX: 10
                        layoutY: 44
                        image: bind imageButtons[0]
                        fitWidth: 80
                        fitHeight: 80
                        opacity: 0.6
                }
                ImageView { //stop button
                        layoutX: 555
                        layoutY: 44
                        image: bind imageButtons[3]
                        fitWidth: 80
                        fitHeight: 80
                        opacity: 0.6
                }
                ImageView { //next word button
                        layoutX: 195
                        layoutY: 44
                        image: bind imageButtons[1]
                        fitWidth: 80
                        fitHeight: 80
                        opacity: 0.6
                }
                ImageView { //back button - delete last letter
                        layoutX: 380
                        layoutY: 44
                        image: bind imageButtons[2]
                        fitWidth: 80
                        fitHeight: 80
                        opacity: 0.6
                }
       ]
    }

    var displayGroup: Group = Group {
        content: [
                Path { //the rectangle where the user makes signs to be translated
                    elements: path
                    stroke: Color.web("#99ccff");
                    strokeWidth: 1
                    strokeDashArray: [ 2 ]
                    strokeDashOffset: 1
                    effect: DropShadow{}
                    visible: bind highlightedButtons[0] //visible only when play button is highlighted
                }
                Text { //the word to be displayed
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
                Text { //the current ltter to be displayed
/* yet to be finished*/x: bind (120 + (this.string.length() * 35)) //not final
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
                ImageView { //the image of the word to be diplayed
                    layoutX: 13
                    layoutY: 400
                    scaleX: 0.8
                    scaleY: 0.8
                    opacity: 0.76
                    effect: DropShadow{}
                    image: bind javafx.ext.swing.SwingUtils.toFXImage(this.wordImage);
                    visible: bind highlightedButtons[0] //visible only when play button is highlighted
                }
                Rectangle { //displayed word grid
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
        ]
    }

    /*
    //menu in Swing
    var menuBar = new JMenuBar();

    var menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);
    menuBar.add(menu);


    var menuItem = new JMenuItem("Evaluation");
    menuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    //menuItem.addActionListener(handler);
    menu.add(menuItem);

    menuItem = new JMenuItem("Exit");
    menuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
    //menuItem.addActionListener(handler);
    menu.add(menuItem);

    menu = new JMenu("Edit");
    menu.setMnemonic(KeyEvent.VK_B);
    menuBar.add(menu);

    menuItem = new JMenuItem("Settings");
    menuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
    //menuItem.addActionListener(handler);
    menu.add(menuItem);

    menu = new JMenu("Menu");
    menu.setMnemonic(KeyEvent.VK_M);
    menuBar.add(menu);

    menuItem = new JMenuItem("Play");
    menuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
    //menuItem.addActionListener(handler);
    menu.add(menuItem);

    menuItem = new JMenuItem("Next Word");
    menuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));
    //menuItem.addActionListener(handler);
    menu.add(menuItem);


    menuItem = new JMenuItem("Clear letter");
    menuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.ALT_MASK));
    //menuItem.addActionListener(handler);
    menu.add(menuItem);

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
f    }
>>>>>>> .r67

    menuItem = new JMenuItem("Stop");
    menuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_7, ActionEvent.ALT_MASK));
    //menuItem.addActionListener(handler);
    menu.add(menuItem);

    menu = new JMenu("Help");
    menu.setMnemonic(KeyEvent.VK_H);
    menuBar.add(menu);

    menuItem = new JMenuItem("View Help");
    menuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_8, ActionEvent.ALT_MASK));
    //menuItem.addActionListener(handler);
    menu.add(menuItem);

    menuItem = new JMenuItem("About SLRT");
    menuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_9, ActionEvent.ALT_MASK));
    //menuItem.addActionListener(handler);
    menu.add(menuItem);

    var fxMenuBar : SwingComponent = SwingComponent.wrap(menuBar);
    fxMenuBar.width = 600;
    fxMenuBar.height = 31;
    fxMenuBar.layoutX = 5;
    fxMenuBar.layoutY = 4;
    */

    



    //menu in javafx

    var menuBarRect: Rectangle = Rectangle {
        x: 5
        y: 4
        height: 30
        width: 600
        arcWidth: 10
        arcHeight: 10
        fill: Color.GREY;
        opacity: 0.0
        onMouseEntered: function (e) {
            this.visibleMenu = true;
        }
        //onMouseExited: function (e) {
        //    this.visibleMenu = false;
        //}


    }


    var menuFX: Group = Group {
        content: [
              Rectangle { //the menu bar
                    x: 5
                    y: 4
                    height: 30
                    width: 600
                    arcWidth: 10
                    arcHeight: 10
                    fill: Color.GREY;
                    opacity: 0.7
                    

                    //visible: bind visibleMenu
              }
              Group { // for file
                  content: [
                           Rectangle { //file menu rectangle on select
                                x: 8
                                y: 9
                                height: 20
                                width: 40
                                arcWidth: 5//10
                                arcHeight: 5//10
                                stroke: Color.BLACK
                                strokeWidth: 0.8
                                fill: Color.GREY;
                                opacity: 0.3
                                visible: bind this.onFileSelect
                                onMouseClicked: function (e) {
                                        this.exitFileMenu = true;
                                        this.settingsEditMenu = false;
                                        this.playMenuMenu = false;
                                        this.nextWordMenuMenu = false;
                                        this.clearLetterMenuMenu = false;
                                        this.stopMenuMenu = false;
                                        this.aboutHelpMenu = false;
                                        this.viewHelpHelpMenu = false;
                                }
                           }
                          Text { //the file menu
                                x: 15
                                y: 25
                                fill: Color.SKYBLUE;
                                font: Font {
                                    name: "Arial"
                                    size: 16
                                }
                                effect: DropShadow{}
                                content: "File"
                                visible: true
                           }
                           Rectangle { //rect for the file menu
                               x: 10
                               y: 9
                               height: 20
                               width: 40
                               fill: Color.GREY;
                               opacity: 0.0
                               visible: true
                               onMouseEntered: function (e) {
                                        this.onFileSelect = true;
                                }
                                onMouseExited: function (e) {
                                        this.onFileSelect = false;
                                }
                                
                           }
                           Group { //the file menu expanded
                               content: [
                                        Rectangle { //file menu rectangle
                                                x: 8
                                                y: 34
                                                height: 30
                                                width: 70
                                                arcWidth: 5//10
                                                arcHeight: 5//10
                                                stroke: Color.BLACK
                                                strokeWidth: 0.8
                                                fill: Color.GREY;
                                                opacity: 0.7
                                                visible: true
                                                onMouseExited: function (e) {
                                                    this.exitFileMenu = false;
                                                }

                                                onMouseClicked: function (e) {
                                                        FX.exit();
                                                }

                                        }
                                        Text { //the exit menu button
                                                x: 15
                                                y: 55
                                                fill: Color.SKYBLUE;
                                                font: Font {
                                                        name: "Arial"
                                                        size: 16
                                                }
                                                effect: DropShadow{}
                                                content: "Exit"
                                                visible: true
                                        }
                               ]
                               visible: bind this.exitFileMenu;
                          }
                  ]
              }
              Group { // edit menu
                  content: [
                           Rectangle { //edit menu rectangle on select
                                x: 60
                                y: 9
                                height: 20
                                width: 40
                                arcWidth: 5//10
                                arcHeight: 5//10
                                stroke: Color.BLACK
                                strokeWidth: 0.8
                                fill: Color.GREY;
                                opacity: 0.3
                                visible: bind this.onEditSelect
                                onMouseClicked: function (e) {
                                        this.exitFileMenu = false;
                                        this.settingsEditMenu = true;
                                        this.playMenuMenu = false;
                                        this.nextWordMenuMenu = false;
                                        this.clearLetterMenuMenu = false;
                                        this.stopMenuMenu = false;
                                        this.aboutHelpMenu = false;
                                        this.viewHelpHelpMenu = false;
                                }
                           }
                           Text { //the edit menu
                                x: 65
                                y: 25
                                fill: Color.SKYBLUE;
                                font: Font {
                                        name: "Arial"
                                        size: 16
                                }
                                //stroke: Color.WHITE
                                //strokeWidth: 0.1
                                effect: DropShadow{}
                                content: "Edit"
                                visible: true
                           }
                           Rectangle { //rect for the edit menu
                               x: 60
                               y: 9
                               height: 20
                               width: 40
                               fill: Color.GREY;
                               opacity: 0.0
                               visible: true
                               onMouseEntered: function (e) {
                                        this.onEditSelect = true;
                                }
                                onMouseExited: function (e) {
                                        this.onEditSelect = false;
                                }
                           }
                           Group { //edit menu expanded
                                content: [
                                        Rectangle { //edit menu rectangle
                                                x: 55
                                                y: 34
                                                height: 30
                                                width: 70
                                                arcWidth: 5//10
                                                arcHeight: 5//10
                                                stroke: Color.BLACK
                                                strokeWidth: 0.8
                                                fill: Color.GREY;
                                                opacity: 0.7
                                                onMouseExited: function (e) {
                                                    this.settingsEditMenu = false;
                                                }
                                                onMouseClicked: function (e) {
                                                        this.settingsEditMenu = false;
                                                }
                                        }
                                        Text { //the settings menu button
                                                x: 60
                                                y: 55
                                                fill: Color.SKYBLUE;
                                                font: Font {
                                                        name: "Arial"
                                                        size: 16
                                                }
                                                effect: DropShadow{}
                                                content: "Settings"
                                                visible: true
                                        }
                                ]
                                visible: bind this.settingsEditMenu;
                          }
                  ]
              }
              Group { // for menu
                  content: [
                          Rectangle { //menu menu rectangle on select
                                x: 110
                                y: 9
                                height: 20
                                width: 48
                                arcWidth: 5//10
                                arcHeight: 5//10
                                stroke: Color.BLACK
                                strokeWidth: 0.8
                                fill: Color.GREY;
                                opacity: 0.3
                                visible: bind this.onMenuSelect
                                onMouseClicked: function (e) {
                                        this.exitFileMenu = false;
                                        this.settingsEditMenu = false;
                                        this.playMenuMenu = true;
                                        this.nextWordMenuMenu = true;
                                        this.clearLetterMenuMenu = true;
                                        this.stopMenuMenu = true;
                                        this.aboutHelpMenu = false;
                                        this.viewHelpHelpMenu = false;
                                }
                         }
                         Text { //the meniu menu
                                x: 115
                                y: 25
                                fill: Color.SKYBLUE;
                                font: Font {
                                        name: "Arial"
                                        size: 16
                                }
                                //stroke: Color.WHITE
                                //strokeWidth: 0.1
                                effect: DropShadow{}
                                content: "Menu"
                                visible: true
                         }
                         Rectangle { //rect for the menu menu
                               x: 110
                               y: 9
                               height: 20
                               width: 40
                               fill: Color.GREY;
                               opacity: 0.0
                               visible: true
                               onMouseEntered: function (e) {
                                        this.onMenuSelect = true;
                                }
                                onMouseExited: function (e) {
                                        this.onMenuSelect = false;
                                }
                         }
                         Group { //menu menu expanded
                                content: [
                                         Group { //play button from menu
                                                content: [
                                                         Rectangle { //play menu rectangle
                                                                x: 110
                                                                y: 34
                                                                height: 30
                                                                width: 92
                                                                arcWidth: 5//10
                                                                arcHeight: 5//10
                                                                stroke: Color.BLACK
                                                                fill: Color.GREY
                                                                strokeWidth: 0.8
                                                                opacity: 0.7
                                                                visible: true
                                                                onMouseClicked: function (e) {
                                                                        this.menuButtonsPressed[0] = true;
                                                                        this.menuButtonsPressed[1] = false;
                                                                        this.menuButtonsPressed[2] = false;
                                                                        this.menuButtonsPressed[3] = false;
                                                                        om.setNewResultFromGui(this.menuButtonsPressed);
                                                                        this.playMenuMenu = false;
                                                                        this.nextWordMenuMenu = false;
                                                                        this.clearLetterMenuMenu = false;
                                                                        this.stopMenuMenu = false;

                                                                }
                                                         }
                                                         Text { //the play menu button
                                                                x: 115
                                                                y: 55
                                                                fill: Color.SKYBLUE;
                                                                font: Font {
                                                                        name: "Arial"
                                                                        size: 16
                                                                }
                                                                effect: DropShadow{}
                                                                content: "Play"
                                                                visible: true
                                                         }
                                                ]
                                                visible: bind this.playMenuMenu;
                                        }
                                        Group { //next word button from menu
						content: [
                                                      Rectangle { //menu menu rectangle
                                                                x: 110
                                                                y: 63
								height: 30
								width: 92
								arcWidth: 5//10
								arcHeight: 5//10
								stroke: Color.BLACK
								strokeWidth: 0.8
								fill: Color.GREY;
								opacity: 0.7
								onMouseClicked: function (e) {
                                                                        //this.menuButtonsPressed[0] = false;
                                                                        this.menuButtonsPressed[1] = true;
                                                                        this.menuButtonsPressed[2] = false;
                                                                        //this.menuButtonsPressed[3] = false;
                                                                        om.setNewResultFromGui(this.menuButtonsPressed);
                                                                        this.playMenuMenu = false;
                                                                        this.nextWordMenuMenu = false;
                                                                        this.clearLetterMenuMenu = false;
                                                                        this.stopMenuMenu = false;
								}
                                                      }
                                                      Text { //the next word menu button
                                                                x: 115
								y: 84
								fill: Color.SKYBLUE;
								font: Font {
                                                                        name: "Arial"
                                                                        size: 16
								}
								effect: DropShadow{}
								content: "Next Word"
								visible: true
                                                       }
						]
						visible: bind this.nextWordMenuMenu;
					}
					Group { //back button from menu
                                            content: [
                                                    Rectangle { //back button rectangle
								x: 110
								y: 92
								height: 30
								width: 92
								arcWidth: 5//10
								arcHeight: 5//10
								stroke: Color.BLACK
								strokeWidth: 0.8
								fill: Color.GREY;
								opacity: 0.7
								onMouseClicked: function (e) {
                                                                        //this.menuButtonsPressed[0] = false;
                                                                        this.menuButtonsPressed[1] = false;
                                                                        this.menuButtonsPressed[2] = true;
                                                                        //this.menuButtonsPressed[3] = false;
                                                                        om.setNewResultFromGui(this.menuButtonsPressed);
                                                                        this.playMenuMenu = false;
                                                                        this.nextWordMenuMenu = false;
                                                                        this.clearLetterMenuMenu = false;
                                                                        this.stopMenuMenu = false;
								}
                                                    }
						    Text { //the clear letter menu button
								x: 115
								y: 113
								fill: Color.SKYBLUE;
								font: Font {
									name: "Arial"
									size: 16
								}
								effect: DropShadow{}
								content: "Clear Letter"
								visible: true
						    }
					    ]
                                            visible: bind this.clearLetterMenuMenu;
					}
					Group { //stop button from menu
                                            content: [
                                                    Rectangle { //stop button menu rectangle
								x: 110
								y: 121
								height: 30
								width: 92
								arcWidth: 5//10
								arcHeight: 5//10
								stroke: Color.BLACK
								strokeWidth: 0.8
								fill: Color.GREY;
								opacity: 0.7
								onMouseClicked: function (e) {
                                                                        this.menuButtonsPressed[0] = false;
                                                                        this.menuButtonsPressed[1] = false;
                                                                        this.menuButtonsPressed[2] = false;
                                                                        this.menuButtonsPressed[3] = true;
                                                                        om.setNewResultFromGui(this.menuButtonsPressed);
                                                                        this.playMenuMenu = false;
                                                                        this.nextWordMenuMenu = false;
                                                                        this.clearLetterMenuMenu = false;
                                                                        this.stopMenuMenu = false;
								}
                                                    }
                                                    Text { //the stop menu button
                                                    		x: 115
								y: 142
								fill: Color.SKYBLUE;
								font: Font {
									name: "Arial"
									size: 16
								}
								effect: DropShadow{}
								content: "Stop"
								visible: true
                                                    }
                                            ]
                                            visible: bind this.stopMenuMenu;
					}
                                 ]
                                 onMouseExited: function (e) {
                                           this.playMenuMenu = false;
                                           this.nextWordMenuMenu = false;
                                           this.clearLetterMenuMenu = false;
                                           this.stopMenuMenu = false;
                                 }
				 // visible: bind this.playMenuMenu;
                           }
                   ]
                }
                Group { // for help menu
                    content: [
                          Rectangle { //help menu rectangle on select
                                x: 170
                                y: 9
                                height: 20
                                width: 40
                                arcWidth: 5//10
                                arcHeight: 5//10
                                stroke: Color.BLACK
                                strokeWidth: 0.8
                                fill: Color.GREY;
                                opacity: 0.3
                                visible: bind this.onHelpSelect
                                onMouseClicked: function (e) {
                                        this.exitFileMenu = false;
                                        this.settingsEditMenu = false;
                                        this.playMenuMenu = false;
                                        this.nextWordMenuMenu = false;
                                        this.clearLetterMenuMenu = false;
                                        this.stopMenuMenu = false;
                                        this.aboutHelpMenu = true;
                                        this.viewHelpHelpMenu = true;
                                }
                           }
                           Text { //the help menu
                                x: 175
                                y: 25
                                fill: Color.SKYBLUE;
                                font: Font {
                                        name: "Arial"
                                        size: 16
                                }
                                //stroke: Color.WHITE
                                //strokeWidth: 0.1
                                effect: DropShadow{}
                                content: "Help"
                                visible: true
                         }
                         Rectangle { //rect for the help menu
                               x: 165
                               y: 9
                               height: 20
                               width: 40
                               fill: Color.GREY;
                               opacity: 0.0
                               visible: true
                               onMouseEntered: function (e) {
                                        this.onHelpSelect = true;
                                }
                                onMouseExited: function (e) {
                                        this.onHelpSelect = false;
                                }
                         }
			 Group { //help menu expanded
                                content: [
                                       Group { //view menu button help menu
                                            content: [
                                            		Rectangle { //view help rectangle
								x: 170
								y: 34
								height: 30
								width: 92
								arcWidth: 5//10
								arcHeight: 5//10
								stroke: Color.BLACK
								strokeWidth: 0.8
								fill: Color.GREY;
								opacity: 0.7
								onMouseClicked: function (e) {
                                                                        this.aboutHelpMenu = false;
                                                                        this.viewHelpHelpMenu = false;
								}
							}
							Text { //the view help menu button
                                                                x: 175
								y: 55
								fill: Color.SKYBLUE;
								font: Font {
                                                                    name: "Arial"
                                                                    size: 16
								}
								effect: DropShadow{}
								content: "View Help"
								visible: true
							}
                                            ]
                                            visible: bind this.viewHelpHelpMenu;
					}
					Group { //about button help menu
                                            content: [
						Rectangle { //about rectangle
							x: 170
							y: 63
							height: 30
							width: 92	
							arcWidth: 5//10
							arcHeight: 5//10
							stroke: Color.BLACK
							strokeWidth: 0.8
							fill: Color.GREY;
							opacity: 0.7
							onMouseClicked: function (e) {
                                                                this.aboutHelpMenu = false;
                                                                this.viewHelpHelpMenu = false;
							}
						}
						Text { //the about menu button
							x: 175
							y: 84
							fill: Color.SKYBLUE;
							font: Font {
                                                                name: "Arial"
                                                                size: 16
							}
							effect: DropShadow{}
							content: "About"
							visible: true
						}
                                            ]
                                            visible: bind this.aboutHelpMenu;
					}
				]
                                onMouseExited: function(e) {
                                    this.aboutHelpMenu = false;
                                    this.viewHelpHelpMenu = false;
                                }

			}
                    ]
                }
        ]

        visible: bind this.visibleMenu

        /*onMouseEntered: function (e) {
                    this.visibleMenu = true;
        }
        onMouseExited: function (e) {
                    this.visibleMenu = false;
        }*/
        
    }





    Stage {
        x: bind sceneX
        y: bind sceneY
        resizable: false
        style: StageStyle.UNDECORATED
        width: 646
        height: 560
        scene: Scene {
                content: [
                    webcamView, grid, appButtons, displayGroup, menuBarRect, menuFX
                ]
        }
    }
  }
}