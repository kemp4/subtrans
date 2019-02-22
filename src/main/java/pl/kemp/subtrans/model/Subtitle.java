package pl.kemp.subtrans.model;

import java.util.List;
import java.util.Objects;

public class Subtitle {

    private long appearTime;
    private long disappearTime;
    private List<String> content;

    public Subtitle(long appearTime, long disappearTime, List<String> content) {
        this.appearTime = appearTime;
        this.disappearTime = disappearTime;
        this.content = content;
    }
    public boolean startsAfter(long time){
        return (appearTime<time);
    }
    public boolean endsBefore(long time){
        return (disappearTime>time);
    }
    //List<Word> wordsList;

    public long getAppearTime() {
        return appearTime;
    }

    public void setAppearTime(long appearTime) {
        this.appearTime = appearTime;
    }

    public long getDisappearTime() {
        return disappearTime;
    }

    public void setDisappearTime(long disappearTime) {
        this.disappearTime = disappearTime;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtitle subtitle = (Subtitle) o;
        return appearTime == subtitle.appearTime &&
                disappearTime == subtitle.disappearTime &&
                Objects.equals(content, subtitle.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appearTime, disappearTime, content);
    }
}
