import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.example.mycompany.MyTranslationService;
import org.example.mycompany.MyTranslationServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static com.google.common.base.CharMatcher.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyTranslationServiceTest {

    @Mock
    private Translate googleTranslate;

    private MyTranslationService translationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        translationService = new MyTranslationService(googleTranslate);
    }

    @Test
    void testTranslateWithGoogleHappyCase() {
        var sentence = "Hello";
        var targetLanguage = "ru";
        var translatedText = "Привет";

        when(googleTranslate.translate(sentence, Translate.TranslateOption.targetLanguage(targetLanguage)))
                .thenReturn(Translation.newBuilder().setTranslatedText(translatedText).build());

        String actualResult = translationService.translateWithGoogle(sentence, targetLanguage);

        assertEquals(translatedText, actualResult);
        verify(googleTranslate, times(1)).translate(sentence, Translate.TranslateOption.targetLanguage(targetLanguage));
    }

    @Test
    void testTranslateWithGoogleUnsupportedLanguage() {
        String sentence = "Hello";
        String targetLanguage = "en";

        assertThrows(IllegalArgumentException.class, () -> translationService.translateWithGoogle(sentence, targetLanguage));

        // googleTranslate.translate() should not be called
        verifyNoInteractions(googleTranslate);
    }

    @Test
    void testTranslateWithGoogleException() {
        String sentence = "Hello";
        String targetLanguage = "ru";

        when(googleTranslate.translate(sentence, Translate.TranslateOption.targetLanguage(targetLanguage)))
                .thenThrow(new RuntimeException("Google Translate API error"));

        assertThrows(MyTranslationServiceException.class, () -> translationService.translateWithGoogle(sentence, targetLanguage));

        verify(googleTranslate, times(1)).translate(sentence, Translate.TranslateOption.targetLanguage(targetLanguage));
    }
}

