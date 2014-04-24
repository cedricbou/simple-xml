package foop.simple.xml;

import java.util.Iterator;
import java.util.Map;

import org.apache.axiom.om.OMElement;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;

import foop.simple.xml.predicates.MatchNameAndValue;

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
		try {
			return factory().newNode(
					Iterators.get(iteratorBuilder.iterator(), index),
					pathBuilder().atIndex(index));
		} catch (IndexOutOfBoundsException ioobe) {
			return NodeFactory.noneNode(pathBuilder().atIndex(index));
		}
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

	@Override
	public MaybeNode find(final Predicate<MaybeNode> predicate) {
		final Iterator<OMElement> iterator = iteratorBuilder.iterator();
		final PathBuilder pathBuilder = pathBuilder().withFindPredicate(predicate.getClass().getName());
		
		while(iterator.hasNext()) {
			final MaybeNode node = factory().newNode(iterator.next(), pathBuilder);
			if(predicate.apply(node)) {
				return node;
			}
		}
		
		return NodeFactory.noneNode(pathBuilder);
	}
	
	@Override
	public MaybeNode findByValue(String name, String value) {
		return find(new MatchNameAndValue(name, value));
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

	@Override
	public boolean isNone() {
		return false;
	}
}
