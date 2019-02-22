package pl.kemp.subtrans.Service;

import org.springframework.stereotype.Service;
import pl.kemp.subtrans.model.Subtitle;
import pl.kemp.subtrans.model.SubtitleBuilder;
import pl.kemp.subtrans.model.Subtitles;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubtitlesSrtReader implements SubtitlesReader {

    @Override
    public Subtitles readSubtitles(BufferedReader bufferedReader) throws IOException {

        List<Subtitle> result = new ArrayList<>();

        RawSubtitle rawSubtitle;

        while((rawSubtitle = readRawSubtitle(bufferedReader))!=null){

            Subtitle subtitle = new SubtitleBuilder()
                    .setAppearTime(rawSubtitle.getAppearTime())
                    .setDisappearTime(rawSubtitle.getDisappearTime())
                    .setContent(rawSubtitle.getTextLines())
                    .createSubtitle();

            result.add(subtitle);

        }
        return new Subtitles(result);
    }

    private RawSubtitle readRawSubtitle(BufferedReader bufferedReader) throws IOException {
        List<String> rawSubtitleList = new ArrayList<>();
        String line;
        try {
            while (!(line = bufferedReader.readLine()).equals("")) {
                rawSubtitleList.add(line);
            }
        }catch (Exception e){
            return null;
        }
        return new RawSubtitle(rawSubtitleList);
    }




    private class RawSubtitle {

        List<String> lines;

        RawSubtitle(List<String> lines){
            this.lines=lines;
        }

        long getId() {
            return Long.parseLong(lines.get(0));
        }
        long getAppearTime() {
            return getMsFromString(lines.get(1).split(" --> ")[0]);
        }
        long getDisappearTime() {
            return getMsFromString(lines.get(1).split(" --> ")[1]);
        }

        public Long getMsFromString(String time) {
                LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss,SSS"));
                return localTime.getHour()*3600000L + localTime.getMinute()*60000L+ localTime.getSecond()*1000L + localTime.getNano()/1000000L;
        }

        List<String> getTextLines(){
            return lines.subList(2,lines.size());
        }
    }
}
