package pl.kemp.subtrans.Service;

import pl.kemp.subtrans.model.Subtitle;
import pl.kemp.subtrans.model.SubtitleBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitializeTestSubs {

    public static List<Subtitle> testSubtitles;

    public InitializeTestSubs(){
        testSubtitles = new ArrayList<>();
        testSubtitles.add(new SubtitleBuilder()
                .setAppearTime(100)
                .setDisappearTime(2000)
                .setContent(Arrays.asList(new String[]{"some example subtitle is here","and there is another line of this subtitle"}))
                .createSubtitle());
    }

}
