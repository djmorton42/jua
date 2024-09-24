package ca.quadrilateral.jua.game;

import java.util.Collection;
import java.util.Set;
import org.apache.commons.collections.map.ReferenceMap;
import ca.quadrilateral.jua.game.image.IGameImage;

public class GameImageCache extends ReferenceMap implements IGameCache {
    private static final long serialVersionUID = 1L;

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public IGameImage get(Object key) {
        return (IGameImage) super.get(key);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> keySet() {
        return super.keySet();
    }


    public IGameImage put(String key, IGameImage value) {
        return (IGameImage) super.put(key, value);
    }

    @Override
    public IGameImage remove(Object key) {
        return (IGameImage) super.remove(key);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<IGameImage> values() {
        return super.values();
    }
}
