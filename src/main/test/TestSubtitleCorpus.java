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
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestSubtitleCorpus {

    public List<String> getCorpus() throws FileNotFoundException {
        List<String> corpus = new ArrayList<>();
        for (File srtFile : SRTFile.getFiles(new File("/home/joris/Downloads/srt/"))) {
            for (SRTFile.SRTEntry en : new SRTFile(srtFile).getEntries()) {
                corpus.add(en.getText());
            }
        }
        return corpus;
    }

    public ParagraphVectors getParagraphVectors() throws FileNotFoundException {
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        SentenceIterator iter = new CollectionSentenceIterator(getCorpus());

        AbstractCache<VocabWord> cache = new AbstractCache<>();

        LabelsSource source = new LabelsSource("SENTENCE_");

        // create the model
        ParagraphVectors paragraphVectorModel = new ParagraphVectors.Builder()
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

        paragraphVectorModel.fit();

        return paragraphVectorModel;
    }

    @Test
    public void testSimilarity() throws FileNotFoundException {

        ParagraphVectors paragraphVectorModel = getParagraphVectors();
        SemanticStringMap<Set<String>> stringSemanticStringMap = new SemanticStringMap<>(paragraphVectorModel);

        int i = 0;
        List<String> corpus = getCorpus();
        int N = corpus.size();
        for (String s : corpus) {
            if (!stringSemanticStringMap.containsKey(s, 10)) {
                Set<String> tmp = new HashSet<>();
                tmp.add(s);
                stringSemanticStringMap.put(s, tmp);
            } else {
                for (Set<String> key : stringSemanticStringMap.get(s, 10)) {
                    key.add(s);
                }
            }
            i++;
            if (i % (N / 100) == 0)
                System.out.println(String.format("%d of %d", i, N));
        }

        for (String s : stringSemanticStringMap.keySet()) {
            Set<String> similar = stringSemanticStringMap.get(s);
            if (similar == null)
                continue;
            if (similar.size() >= 10) {
                System.out.println(similar);
            }
        }

    }
}
