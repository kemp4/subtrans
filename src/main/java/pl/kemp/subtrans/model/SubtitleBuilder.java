package pl.kemp.subtrans.model;

import java.util.List;

public class SubtitleBuilder {
    private long appearTime;
    private long disappearTime;
    private List<String> content;

    public SubtitleBuilder setAppearTime(long appearTime) {
        this.appearTime = appearTime;
        return this;
    }

    public SubtitleBuilder setDisappearTime(long disappearTime) {
        this.disappearTime = disappearTime;
        return this;
    }

    public SubtitleBuilder setContent(List<String> content) {
        this.content = content;
        return this;
    }

    public Subtitle createSubtitle() {
        return new Subtitle(appearTime, disappearTime, content);
    }
}