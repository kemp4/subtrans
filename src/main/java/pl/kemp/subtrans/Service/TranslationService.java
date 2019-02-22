package pl.kemp.subtrans.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface TranslationService {

    String translateWord(String word) throws IOException;

}
