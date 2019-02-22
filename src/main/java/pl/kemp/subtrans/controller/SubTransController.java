package pl.kemp.subtrans.controller;

import com.sun.jna.Memory;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kemp.subtrans.Service.DeepLService;
import pl.kemp.subtrans.Service.SubtitlesReader;
import pl.kemp.subtrans.Service.TranslationService;
import pl.kemp.subtrans.model.Subtitle;
import pl.kemp.subtrans.model.Subtitles;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class SubTransController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private Pane playerPane;

    SubtitlesReader subtitlesReader;
    private SubtitlesView subtitlesView = new SubtitlesView();

    @Autowired
    void setSubtitlesReader(SubtitlesReader subtitlesReader){
        this.subtitlesReader=subtitlesReader;
    }



    private ImageView imageView;
    private WritableImage writableImage;
    private WritablePixelFormat<ByteBuffer> pixelFormat;
    private FloatProperty videoSourceRatioProperty;
    private CanvasPlayerComponent mediaPlayerComponent;

    private Subtitles subtitles;

    @FXML
    public void openVideoFile() {
        try {
            setKeysListeners();
            Path newFilePath = askForFilePath();
            if (newFilePath != null) {
                // to another class? Model.playNewVideo

                playVideo(newFilePath.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void openSubtitleFile() {
        try {
            Path newFilePath = askForFilePath();
            if (newFilePath != null) {
                subtitles = subtitlesReader.readSubtitles(new BufferedReader(new FileReader(newFilePath.toFile())));
                // to another class? Model.playNewVideo
                //playVideo(newFilePath.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private Path askForFilePath() {
        FileChooser chooser = new FileChooser();
        Window window = root.getScene().getWindow();
        return chooser.showOpenDialog(window).toPath();
    }

    private void setKeysListeners() {
        root.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE){
                System.out.println("Space pressed");
                mediaPlayerComponent.getMediaPlayer().pause();
            }
        });
    }

    //Model.initialize *********************************************************
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        videoSourceRatioProperty = new SimpleFloatProperty(0.4f);
        pixelFormat =PixelFormat.getByteBgraPreInstance();
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        writableImage = new WritableImage((int) visualBounds.getWidth(), (int) visualBounds.getHeight());
        imageView = new ImageView(writableImage);
        playerPane.getChildren().add(imageView);
        playerPane.getChildren().add(subtitlesView.getSubsPane());
        playerPane.widthProperty().addListener((observable, oldValue, newValue) -> fitImageViewSize(newValue.floatValue(), (float) playerPane.getHeight()));
        playerPane.heightProperty().addListener((observable, oldValue, newValue) -> fitImageViewSize((float) playerPane.getWidth(), newValue.floatValue()));
        videoSourceRatioProperty.addListener((observable, oldValue, newValue) -> fitImageViewSize((float) playerPane.getWidth(), (float) playerPane.getHeight()));

    }



    private void playVideo(String videoPath){
        if(mediaPlayerComponent==null) {
            mediaPlayerComponent = new CanvasPlayerComponent();
        }
        mediaPlayerComponent.getMediaPlayer().prepareMedia(videoPath);
        mediaPlayerComponent.getMediaPlayer().start();
    }


    private void fitImageViewSize(float width, float height) {
        subtitlesView.refreshSubtitles(width, height);
        Platform.runLater(() -> {

            float fitHeight = videoSourceRatioProperty.get() * width;
            if (fitHeight > height) {
                imageView.setFitHeight(height);
                double fitWidth = height / videoSourceRatioProperty.get();
                imageView.setFitWidth(fitWidth);
                imageView.setX((width - fitWidth) / 2);
                imageView.setY(0);
            }
            else {
                imageView.setFitWidth(width);
                imageView.setFitHeight(fitHeight);
                imageView.setY((height - fitHeight) / 2);
                imageView.setX(0);
            }
        });
    }
    private class CanvasPlayerComponent extends DirectMediaPlayerComponent {
        public CanvasPlayerComponent() {
            super((sourceWidth, sourceHeight) -> {
                Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
                Platform.runLater(() -> videoSourceRatioProperty.set((float) sourceHeight / (float) sourceWidth));
                return new RV32BufferFormat((int) visualBounds.getWidth(), (int) visualBounds.getHeight());
            });
        }

        PixelWriter pixelWriter = null;

        private PixelWriter getPW() {
            if (pixelWriter == null) {
                pixelWriter = writableImage.getPixelWriter();
            }
            return pixelWriter;
        }

        private String oldSubtitles="";

        private void generateSubtitlesChangedEvents(){
                if(subtitles!=null){
                try {
                    long actualTime = mediaPlayerComponent.getMediaPlayer().getTime();

                    List<Subtitle> actualSubtitles = subtitles.getSubtitlesAt(actualTime);
                    if (actualSubtitles.size()>0) {

                        String actualSubsStr = actualSubtitles.get(0).getContent().get(0);
                        if (!oldSubtitles.equals(actualSubsStr)) {
                            subtitlesView.setSubtitles(actualSubtitles);
                            subtitlesView.refreshSubtitles(playerPane.getWidth(), playerPane.getHeight());
                            oldSubtitles= actualSubsStr;
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {

            if (writableImage == null) {
                return;
            }

            Platform.runLater(() -> {
                generateSubtitlesChangedEvents();
                Memory nativeBuffer = mediaPlayer.lock()[0];
                try {
                    ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
                    getPW().setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);
                }
                finally {
                    mediaPlayer.unlock();
                }
            });
        }
    }
}
