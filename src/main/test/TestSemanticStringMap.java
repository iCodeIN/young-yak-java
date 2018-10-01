import app.skill.impl.meta.word2vec.SemanticStringMap;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TestSemanticStringMap {

    private static String[] SENTENCES = {"What is the difference between iText, JasperReports and Adobe LC?",
            "Where is the origin (x,y) of a PDF page?",
            "How to get the page number of an arbitrary PDF object?",
            "How should I interpret the coordinates of a rectangle in PDF?",
            "How to allow page extraction when setting password security?",
            "How to encrypt PDF using a certificate?",
            "Why are PDF files different even if the content is the same?",
            "How to disable the save button and hide the menu bar in Adobe Reader?",
            "Does a PDF file have styles, headers and footers?",
            "How to extract a page number from a PDF file?",
            "How to close a PDF file to recreate it? (File in use problem)",
            "What is the size limit of pdf file?",
            "How to hide the Adobe floating toolbar when showing a PDF in browser?",
            "Can I change the page count by changing internal metadata?",
            "How do I rotate a PDF page to an arbitrary angle?",
            "How to protect an already existing PDF with a password?",
            "How to protect a PDF with a username and password?",
            "Why do PDFs change when processing them?",
            "What is the connection between LTV and document timestamps?",
            "What does \"Not LTV-enabled\" mean?"};

    private ParagraphVectors trainModel(String[] sentences) {

        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        SentenceIterator iter = new CollectionSentenceIterator(Arrays.asList(sentences));

        AbstractCache<VocabWord> cache = new AbstractCache<>();

        LabelsSource source = new LabelsSource("SENTENCE_");

        // we load externally originated model
        ParagraphVectors out = new ParagraphVectors.Builder()
                .minWordFrequency(1)
                .iterations(100)
                .epochs(1)
                .layerSize(64)
                .learningRate(0.025)
                .labelsSource(source)
                .windowSize(5)
                .iterate(iter)
                .trainWordVectors(false)
                .vocabCache(cache)
                .tokenizerFactory(t)
                .sampling(0)
                .build();

        out.fit();

        // return
        return out;
    }

    @Test
    public void testSemanticMap() {

        // train model
        ParagraphVectors paragraphVectors = trainModel(SENTENCES);

        // create dictionary
        SemanticStringMap<Integer> integerSemanticStringMap = new SemanticStringMap<>(paragraphVectors);
        for (int i = 0; i < SENTENCES.length; i++)
            integerSemanticStringMap.put(SENTENCES[i], i);

        Assert.assertTrue(isMatch(SENTENCES[0], "what is the difference between iText and JasperReports", integerSemanticStringMap));

        Assert.assertTrue(isMatch(SENTENCES[1], "where can I find the origin of a PDF page", integerSemanticStringMap));
    }

    private boolean isMatch(String s0, String s1, SemanticStringMap<Integer> integerSemanticStringMap) {
        Integer i0 = integerSemanticStringMap.get(s0, 1).iterator().next();
        for (Integer i1 : integerSemanticStringMap.get(s1, 20)) {
            if (i0 == i1)
                return true;
        }
        return false;
    }
}
