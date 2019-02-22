package test;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import pl.kemp.subtrans.Service.SubtitlesReader;
import pl.kemp.subtrans.Service.SubtitlesSrtReader;
import pl.kemp.subtrans.model.Subtitle;
import pl.kemp.subtrans.model.SubtitleBuilder;
import pl.kemp.subtrans.model.Subtitles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SubtitlesTest {

    private String subtitleText;
    private SubtitlesReader subtitlesReader;
    private BufferedReader bufferedReader;
    private Subtitles subtitles;

    @Before
        public void prepareTest() throws IOException {
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
                    "\n";

            subtitlesReader = new SubtitlesSrtReader();
            StringReader stringReader = new StringReader(subtitleText);
            bufferedReader =new BufferedReader(stringReader);
            subtitles = subtitlesReader.readSubtitles(bufferedReader);
        }

        @Test
        public void testGettingSubtitles() throws IOException {
            String testContent = "Dzięki temu będziesz mógł przewozić dla mnie różne przesyłki.";

            List<Subtitle> resultSubtitles = subtitles.getSubtitlesAt(3333);
            Subtitle resultSubtitle = resultSubtitles.get(0);
            List<String> resultContent = resultSubtitle.getContent();
            String result = resultContent.get(0);
            Assert.assertEquals(testContent,result);
        }
        @Test
        public void testCalendar() throws ParseException {
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dt.parse("2018-09-01");
                System.out.println(date);
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(date);
                cal.add(Calendar.DATE, -1);
                Date time = cal.getTime();
                System.out.println(time);

        }
   }


