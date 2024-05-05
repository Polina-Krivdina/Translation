
import com.google.api.services.translate.Translate;
import com.google.cloud.translate.Translation;
import org.example.mycompany.MyTranslationService;
import org.example.mycompany.MyTranslationServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.base.CharMatcher.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class MyTranslationServiceTest_TODO {
    @Mock
    private Translate translate;

    @Mock
    private Translation translation;

/**
     * 1. Happy case test.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is equal to "ru",
     * `googleTranslate` dependency should be called and `translation.getTranslatedText()` returned.
     * No other interactions with `googleTranslate` dependency should be invoked apart from a single call to `googleTranslate.translate()`.
     */

    @Test
    void translateWithGoogle_anySentenceAndTargetLanguageIsRu_success() {
        var myTranslationService = new MyTranslationService((com.google.cloud.translate.Translate) translate);

        var sentence = "Some sentence";
        var targetLanguage = "ru";

        var expectedResult = "Какое-то предложение";

        Mockito.when(translate.translate(eq(sentence), any())).thenReturn(translation);
        Mockito.when(translation.getTranslatedText()).thenReturn(expectedResult);

        String actualResult = myTranslationService.translateWithGoogle(sentence, targetLanguage);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(translate).translate(eq(sentence), any());
        Mockito.verifyNoMoreInteractions(translate);

        Mockito.verify(translation).getTranslatedText();
        Mockito.verifyNoMoreInteractions(translation);
    }


/**
     * 2. Unhappy case test when target language is not supported.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is not equal to "ru",
     * `IllegalArgumentException` should be thrown. `googleTranslate` dependency should not be called at all.
     */

    @Test
    void translateWithGoogle_anySentenceAndTargetLanguageIsNotRu_failure() {
        var myTranslationService = new MyTranslationService((com.google.cloud.translate.Translate) translate);
        var sentence = "Some sentence";
        var targetLanguage = "es";
        assertThrows(
                IllegalArgumentException.class,
                () -> myTranslationService.translateWithGoogle(sentence, targetLanguage)
    );
        Mockito.verifyNoInteractions(translate);
        Mockito.verifyNoInteractions(translation);
    }


/**
     * 3. Unhappy case test when Google Translate call throws exception.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is equal to "ru",
     * `googleTranslate` dependency should be called. When `googleTranslate` dependency throws exception, it should be
     * wrapped into `MyTranslationServiceException` and the latter should be thrown from our method.
     */

    @Test
    void translateWithGoogle_googleTranslateThrowsException_failure() {
        var myTranslationService = new MyTranslationService((com.google.cloud.translate.Translate) translate);

        var sentence = "Some sentence";
        var targetLanguage = "ru";

        Mockito.when(translate.translate(eq(sentence), any())). thenThrow(new RuntimeException());
        assertThrows (
                MyTranslationServiceException.class,
                ()-> myTranslationService.translateWithGoogle(sentence, targetLanguage)
);
        Mockito.verify(translate).translate(eq(sentence), any());
        Mockito.verifyNoMoreInteractions(translate);
        Mockito.verifyNoInteractions(translation);
    }

}