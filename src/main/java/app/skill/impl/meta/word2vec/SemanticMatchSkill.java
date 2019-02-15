package app.skill.impl.meta.word2vec;

import app.controller.IBotController;
import app.entities.DialogChunk;
import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.Status;
import app.skill.ISkill;
import app.skill.impl.meta.typo.TypoCorrectionSkill;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.util.*;

public class SemanticMatchSkill implements ISkill {

    private static Random RANDOM = new Random(System.currentTimeMillis());
    private IBotController botController;

    // model
    private boolean isBuildingModel = false;
    private SemanticStringMap<Set<String>> synonymMap;

    public SemanticMatchSkill(IBotController botController) {
        this.botController = botController;
    }

    private void _buildModel() {
        if (isBuildingModel)
            return;
        isBuildingModel = true;

        List<String> sentences = new ArrayList<>();
        for (DialogChunk dc : botController.getDialogChunkRepository().findAll()) {
            if (dc.getOutput().isEmpty())
                continue;

            // do not build fuzzy logic on top of fuzzy logic
            List<String> tmp = Arrays.asList(dc.getInvokedSkills());
            if (tmp.contains(SemanticMatchSkill.class.getName()))
                continue;
            if (tmp.contains(TypoCorrectionSkill.class.getName()))
                continue;

            sentences.add(dc.getInput());
        }
        if(sentences.isEmpty())
            return;

        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        SentenceIterator iter = new CollectionSentenceIterator(sentences);

        AbstractCache<VocabWord> cache = new AbstractCache<>();

        LabelsSource source = new LabelsSource("SENTENCE_");

        // create the model
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

        // create synonyms
        synonymMap = new SemanticStringMap<>(out);
        for (String s : sentences) {
            if (synonymMap.containsKey(s))
                synonymMap.get(s).add(s);
            else
                synonymMap.put(s, new HashSet<>(Arrays.asList(s)));
        }

        // release lock
        isBuildingModel = false;
    }

    private void buildModel() {
        new Thread() {
            public void run() {
                _buildModel();
            }
        }.start();
    }

    @Override
    public boolean canHandle(IHandlerInput input) {
        if (synonymMap == null) {
            buildModel();
            return false;
        }
        return synonymMap.containsKey(input.getContent().toString(), 10);
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        Collection<Set<String>> tmp = synonymMap.get(input.getContent().toString(), 10);
        if (tmp.isEmpty())
            return null;

        List<String> syns = new ArrayList<>(tmp.iterator().next());
        String out = syns.get(RANDOM.nextInt(syns.size()));

        return new IHandlerResponse() {
            @Override
            public Status getStatus() {
                return Status.STATUS_303_SEE_OTHER;
            }

            @Override
            public Object getContent() {
                return out;
            }

            @Override
            public String getContentType(){return "text";}

            @Override
            public String[] getInvokedSkills() {
                return new String[]{SemanticMatchSkill.class.getName()};
            }
        };
    }
}
