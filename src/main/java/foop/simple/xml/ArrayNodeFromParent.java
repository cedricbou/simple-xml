package foop.simple.xml;

import java.util.Iterator;
import java.util.Map;

import org.apache.axiom.om.OMElement;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;

/**
 * A node representing an array of nodes.
 * 
 * @author Cedric
 * 
 */
public class ArrayNodeFromParent extends WithNamespaceRegistryAndPath implements
		MaybeNode {

	private final ParentIteratorBuilder iteratorBuilder;

	protected static interface ParentIteratorBuilder {
		public Iterator<OMElement> iterator();
	}

	public ArrayNodeFromParent(ParentIteratorBuilder iteratorBuilder,
			final Map<String, String> registry, final PathBuilder pathBuilder) {
		super(registry, pathBuilder);
		this.iteratorBuilder = iteratorBuilder;
	}

	@Override
	public MaybeNode get(String name) {
		return get(0).get(name);
	}

	@Override
	public MaybeNode get(int index) {
		return factory().newNode(
				Iterators.get(iteratorBuilder.iterator(), index),
				pathBuilder().atIndex(index));
	}

	@Override
	public String text() {
		return Joiner.on("").join(
				Iterators.transform(iteratorBuilder.iterator(),
						new Function<OMElement, String>() {
							@Override
							public String apply(OMElement el) {
								return el.getText();
							}
						}));
	}

	protected MaybeNode buildNodeWithNewNamespaceRegistry(
			ImmutableMap<String, String> registry) {
		return new ArrayNodeFromParent(iteratorBuilder, registry, pathBuilder());
	};

	@Override
	public String toString() {
		return Joiner.on("").join(
				Iterators.transform(iteratorBuilder.iterator(),
						new Function<OMElement, String>() {
							@Override
							public String apply(OMElement el) {
								return el.toString();
							}
						}));
	}
}
