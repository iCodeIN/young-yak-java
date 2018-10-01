package app.skill.impl.meta.word2vec;

import app.skill.impl.meta.typo.BKTree;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SemanticStringMap<T> implements Map<String, T> {

    private BKTree<SSMEntry> tree;
    private ParagraphVectors paragraphVectorModel;

    public SemanticStringMap(ParagraphVectors paragraphVectors) {
        this.paragraphVectorModel = paragraphVectors;
        this.tree = new BKTree<>(new BKTree.Metric<SSMEntry>() {
            @Override
            public int distance(SSMEntry obj0, SSMEntry obj1) {

                if (obj0.vector == null) {
                    String k0 = obj0.key.toUpperCase();
                    try {
                        obj0.vector = paragraphVectorModel.inferVector(k0).data().asDouble();
                    } catch (Exception ex) {
                    }
                }
                if (obj1.vector == null) {
                    String k1 = obj1.key.toUpperCase();
                    try {
                        obj1.vector = paragraphVectorModel.inferVector(k1).data().asDouble();
                    } catch (Exception ex) {
                    }
                }

                // exceptions
                if (obj0.vector == null || obj1.vector == null)
                    return 100;

                // cosine similarity
                double aa = 0;
                double bb = 0;
                double ab = 0;
                for (int i = 0; i < obj0.vector.length; i++) {
                    aa += java.lang.Math.pow(obj0.vector[i], 2);
                    bb += java.lang.Math.pow(obj1.vector[i], 2);
                    ab += (obj0.vector[i] * obj1.vector[i]);
                }
                double d = ab / (java.lang.Math.sqrt(aa) * java.lang.Math.sqrt(bb));

                // rescale
                d = (d + 1.0) / 2.0;

                // to int [ 0 .. 100 ]
                int d2 = (int) ((1.0 - d) * 100);
                d2 = d2 - (d2 % 5);
                return d2;
            }
        });
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object o) {
        if (o instanceof String)
            return tree.contains(new SSMEntry(o.toString(), null));
        return false;
    }

    public boolean containsKey(Object o, int maxDistance) {
        return !get(o, maxDistance).isEmpty();
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public T get(Object o) {
        if (!(o instanceof String))
            return null;
        Collection<SSMEntry> ens = tree.get(new SSMEntry(o.toString(), null), 1);
        if (ens.isEmpty())
            return null;
        return ens.iterator().next().value;
    }

    public Collection<T> get(Object o, int maxDistance) {
        if (!(o instanceof String))
            return null;
        Collection<SSMEntry> ens = tree.get(new SSMEntry(o.toString(), null), maxDistance);
        if (ens.isEmpty())
            return java.util.Collections.emptySet();
        Set<T> vals = new HashSet<>();
        for (SSMEntry en : ens)
            vals.add(en.value);
        return vals;
    }

    @Override
    public T put(String s, T t) {
        if (!containsKey(s))
            tree.add(new SSMEntry(s, t));
        return t;
    }

    @Override
    public T remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> map) {
        for (Entry en : map.entrySet()) {
            put((String) en.getKey(), (T) en.getValue());
        }
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public Set<String> keySet() {
        Set<String> keys = new HashSet<>();
        for (Object obj : tree.toArray()) {
            SSMEntry en = (SSMEntry) obj;
            keys.add(en.key);
        }
        return keys;
    }

    @Override
    public Collection<T> values() {
        return null;
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        return null;
    }

    class SSMEntry {
        public String key;
        public double[] vector;
        public T value;

        public SSMEntry(String key, T val) {
            this.key = key;
            this.value = val;
        }
    }
}
