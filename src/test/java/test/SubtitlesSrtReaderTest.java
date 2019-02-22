package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.kemp.subtrans.Service.SubtitlesReader;
import pl.kemp.subtrans.Service.SubtitlesSrtReader;
import pl.kemp.subtrans.model.Subtitle;
import pl.kemp.subtrans.model.Subtitles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class SubtitlesSrtReaderTest {

    String subtitleText;
    SubtitlesReader subtitlesReader;
    BufferedReader bufferedReader;
    @Before
    public void prepareTest(){
            subtitleText = "1\n" +
                "00:00:00,334 --> 00:00:03,202\n" +
                "Nauka latania tym statkiem będzie\n" +
                "dla ciebie nową przygodą.\n" +
                "\n" +
                "2\n" +
                "00:00:03,237 --> 00:00:05,830\n" +
                "Dzięki temu będziesz mógł przewozić dla mnie różne przesyłki.\n" +
                "\n" +
                "3\n" +
                "00:00:05,855 --> 00:00:07,440\n" +
                "- Fajnie. \n" +
                "- Widzisz tamtą planetę?\n" +
                "\n"+
                "4\n"+
                "01:02:05,100 --> 01:02:10,100\n" +
                "- Fajnie. \n" +
                "- Widzisz tamtą planetę?\n" +
                "\n";

            subtitlesReader = new SubtitlesSrtReader();
            StringReader stringReader = new StringReader(subtitleText);
            bufferedReader =new BufferedReader(stringReader);

    }

    @Test
    public void testReadingSecondsDisappearTime() throws IOException {
        long excepted = 7440l;
        Subtitles subtitles = subtitlesReader.readSubtitles(bufferedReader);
        Assert.assertEquals(subtitles.getSubtitles().get(2).getDisappearTime(),excepted);
    }

    @Test
    public void testReadingSecondsAppearTime() throws IOException {
        long excepted = 3237l;
        Subtitles subtitles = subtitlesReader.readSubtitles(bufferedReader);
        Assert.assertEquals(subtitles.getSubtitles().get(1).getAppearTime(),excepted);
    }

    @Test
    public void testReadingHoursDisappearTime() throws IOException {
        long excepted = 1l*3600000l+2l*60000l+10l*1000l +100l;
        Subtitles subtitles = subtitlesReader.readSubtitles(bufferedReader);
        Assert.assertEquals(subtitles.getSubtitles().get(3).getDisappearTime(),excepted);
    }

    @Test
    public void testReadingHoursAppearTime() throws IOException {
        long excepted = 1l*3600000l+2l*60000l+5l*1000l +100l;
        Subtitles subtitles = subtitlesReader.readSubtitles(bufferedReader);
        Assert.assertEquals(subtitles.getSubtitles().get(3).getAppearTime(),excepted);
    }

    @Test
    public void testReadingFirstSubtitle() throws IOException {
        String excepted = "Nauka latania tym statkiem będzie";
        Subtitles subtitles = subtitlesReader.readSubtitles(bufferedReader);
        Assert.assertEquals(subtitles.getSubtitles().get(0).getContent().get(0),excepted);

    }
    @Test
    public void testReadingLastSubtitle() throws IOException {
        String excepted = "- Widzisz tamtą planetę?";
        Subtitles subtitles = subtitlesReader.readSubtitles(bufferedReader);
        Assert.assertEquals(subtitles.getSubtitles().get(3).getContent().get(1),excepted);
    }
}