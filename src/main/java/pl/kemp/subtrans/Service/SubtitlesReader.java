package pl.kemp.subtrans.Service;

import pl.kemp.subtrans.model.Subtitles;

import java.io.BufferedReader;
import java.io.IOException;


public interface SubtitlesReader {
    Subtitles readSubtitles(BufferedReader bufferedReader) throws IOException;

}
