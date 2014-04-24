package foop.simple.xml.predicates;

import com.google.common.base.Predicate;

import foop.simple.xml.MaybeNode;

public class MatchNameAndValue implements Predicate<MaybeNode> {

	private final String name;
	private final String value;
	
	public MatchNameAndValue(final String name, final String value) {
		this.name = name;
		this.value = value;
	}
	
	public boolean apply(final MaybeNode node) {
		final MaybeNode target = node.get(name);
		if(!target.isNone()) {
			return value.equals(target.text());
		}
		
		return false;
	}
}
