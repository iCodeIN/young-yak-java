package app.skill.impl.meta.word2vec;

import app.handler.IHandlerInput;
import app.handler.IHandlerResponse;
import app.handler.impl.HandlerResponseImpl;
import app.skill.ISkill;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FAQSkill implements ISkill {

    // prefix
    private static String[] PREFIX = {  "I found some links for you.",
                                        "Maybe this is helpful;",
                                        "I hope this helps;",
                                        "I think I may have found something for you.",
                                        "These seem useful;"};
    private static Random RANDOM = new Random(System.currentTimeMillis());

    // NLP
    private ParagraphVectors paragraphVectorModel;
    private SemanticStringMap<String> frequentlyAskedQuestions;

    public FAQSkill(String[][] faqTuples){
        // build paragraph vector model
        String[] sentences = new String[faqTuples.length];
        for(int i=0;i<faqTuples.length;i++)
            sentences[i] = faqTuples[i][0];
        this.paragraphVectorModel = rebuildModel(sentences);

        // build semantic map
        this.frequentlyAskedQuestions = new SemanticStringMap<>(paragraphVectorModel);
        for(String[] faq : faqTuples){
            this.frequentlyAskedQuestions.put(faq[0], faq[1]);
        }
    }

    public FAQSkill(InputStream xmlDocument){
        try {
            Element root = new SAXBuilder().build(xmlDocument).getRootElement();
            List<Element> children = root.getChildren();
            int N = children.size();
            String[] qs = new String[N];
            String[] as = new String[N];
            for(int i=0;i<children.size();i++){
                qs[i] = children.get(i).getChildText("q");
                as[i] = children.get(i).getChildText("a");
            }
            paragraphVectorModel = rebuildModel(qs);
            this.frequentlyAskedQuestions = new SemanticStringMap<>(paragraphVectorModel);
            for(int i=0;i<qs.length;i++){
                this.frequentlyAskedQuestions.put(qs[i], as[i]);
            }
        } catch (JDOMException | IOException e) { }
    }

    private ParagraphVectors rebuildModel(String[] sentences){
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        SentenceIterator iter = new CollectionSentenceIterator(Arrays.asList(sentences));

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

        // return
        return out;
    }

    @Override
    public boolean canHandle(IHandlerInput input) {
        return frequentlyAskedQuestions.containsKey(input.getContent().toString(), 10);
    }

    @Override
    public IHandlerResponse invoke(IHandlerInput input) {
        Collection<String> answers = frequentlyAskedQuestions.get(input.getContent().toString(), 10);
        if(answers.isEmpty())
            return null;

        // build answer
        String out = PREFIX[RANDOM.nextInt(PREFIX.length)] + "<ul>";
        for (String answer : answers)
            out += ("<li><a href='" + answer + "'>link</a></li>");
        out += "</ul>";

        // return
        return new HandlerResponseImpl(out);
    }

}