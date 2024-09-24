package ca.quadrilateral.jua.game;

import java.util.Collection;
import java.util.Set;
import ca.quadrilateral.jua.game.image.IGameImage;

public interface IGameCache {

	public abstract void clear();

	public abstract boolean containsKey(Object key);

	public abstract boolean containsValue(Object value);

	public abstract IGameImage get(Object key);

	public abstract boolean isEmpty();

	public abstract Set<String> keySet();

	public abstract IGameImage put(String key, IGameImage value);

	public abstract IGameImage remove(Object key);

	public abstract int size();

	public abstract Collection<IGameImage> values();

}