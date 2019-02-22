package pl.kemp.subtrans.model;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Subtitles {

    List<Subtitle> subtitles;

    public List<Subtitle> getSubtitles() {
        return subtitles;
    }

    public Subtitles(){
        //subtitles=new ArrayList<>();
    }
    public Subtitles(List<Subtitle> subtitles){
        this.subtitles=subtitles;
    }
    public List<Subtitle> getSubtitlesAt(long time) {
        return subtitles.stream().filter(
                subtitle -> subtitle.getAppearTime()<time&&subtitle.getDisappearTime()>time)
                .collect(Collectors.toList());
    }

    public void addSubtitle(Subtitle subtitle) {
        subtitles.add(subtitle);
    }

}
