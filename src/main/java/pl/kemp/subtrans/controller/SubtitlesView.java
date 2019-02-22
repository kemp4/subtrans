package pl.kemp.subtrans.controller;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;
import pl.kemp.subtrans.Service.DeepLService;
import pl.kemp.subtrans.Service.TranslationService;
import pl.kemp.subtrans.model.Subtitle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SubtitlesView {

    private static final int FONT_SIZE = 40;
    private TranslationService translationService =new DeepLService();
    private Pane subsPane;

    private static final int SPACEWIDTH=10;
    private static final int INTERLINE=10;

    private int screenWidth = 350;
    private int screenHeight = 400;

    private String subTestContent = "this is some simple subtitles test for this application really very long test text for testing if multiline work очень хорошо";

    private String css = "    -fx-stroke: black; " +
            " -fx-stroke-width: 1; " +
            " -fx-fill: white; ";

    private List<Subtitle> subtitles;
    private List<Text> subtitlesTexts;
    private TabPane tabPane = new TabPane();
    private Tab tabB;

    public Pane getSubsPane(){

        subsPane = new Pane();

        subsPane.getChildren().addAll(convertSubtitlesLineToTextFields());
        return subsPane;

    }


    public void refreshSubtitles(double width, double height){
        screenWidth = (int) width;
        screenHeight = (int)height;
        subsPane.getChildren().clear();
        subsPane.getChildren().addAll(convertSubtitlesLineToTextFields());
    }

    private List<Text> convertSubtitlesLineToTextFields() {
        List<Text> result = new ArrayList<>();
        String testText = subTestContent;
        String[] words = testText.split(" ");

        int posX = 0;

        int bottomPaneHeight= isBottomPaneOn()?30:0;
        int posY = screenHeight-bottomPaneHeight;
        int lastLineIndex = 0;
        for (int i =0 ; i<words.length; i++){

            Text newText = new Text(words[i]);
            newText.setFont(Font.font("Tahoma",FONT_SIZE));
            newText.setStyle(css);
            newText.setX(posX);

            posX += newText.getBoundsInLocal().getWidth() + SPACEWIDTH;
            if (posX>screenWidth) {
                posX -= newText.getBoundsInLocal().getWidth() + SPACEWIDTH;
                centerLine(result.subList(lastLineIndex,i), (screenWidth - posX) >> 1);
                lastLineIndex=i;
                posX=0;
                newText.setX(posX);
                posX += newText.getBoundsInLocal().getWidth() + SPACEWIDTH;
                upLines(result.subList(0,i), (int)(newText.getBoundsInLocal().getHeight() + INTERLINE));
            }

            newText.setY(posY);
            newText.addEventFilter(MouseEvent.MOUSE_CLICKED, this::translateWord);

            result.add(newText);
        }
        centerLine(result.subList(lastLineIndex,result.size()), (screenWidth - posX) >> 1);

        return result;


    }

    private boolean isBottomPaneOn() {
        return true; //todo
    }

    private void translateWord(MouseEvent event) {
        try {
            Text source = (Text) event.getSource();
            String translated = translationService.translateWord(source.getText());
            System.out.println(translated);

            tabPane.getTabs().removeAll();

            tabPane = new TabPane();

            tabB = new Tab();
            tabB.setText("");
            tabPane.getTabs().add(tabB);
            tabB.setStyle("-fx-background-color: black;");

            Label label = new Label(translated);

            tabB.setGraphic(label);
            tabB.getGraphic().setStyle("-fx-text-fill: #fff;");
            subsPane.getChildren().add(tabPane);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void centerLine(List<Text> texts,int offset) {
        for(Text text : texts){
            text.setX(text.getX()+offset);
        }
    }
    private void upLines(List<Text> texts,int offset){
        for(Text text : texts){
            text.setY(text.getY()-offset);
        }
    }

    public void setSubtitles(List<Subtitle> subtitles) {

        this.subtitles = subtitles;

        if (this.subtitles.size()>0) {

            subTestContent = this.subtitles.get(0).getContent().stream().reduce("", (s1, s2) -> s1 +" "+ s2);
        }
    }
}
