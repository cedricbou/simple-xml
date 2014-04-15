package foop.simple.xml;

import java.util.Map;

public class AttrNode extends WithPathBuilder implements MaybeNode {

	private final String value;

	protected AttrNode(final String value, final PathBuilder pathBuilder) {
		super(pathBuilder);
		this.value = value;
	}

	@Override
	public MaybeNode get(String name) {
		return NodeFactory.noneNode(pathBuilder());
	}

	@Override
	public MaybeNode get(int index) {
		return NodeFactory.noneNode(pathBuilder());
	}

	@Override
	public String text() {
		return value;
	}

	@Override
	public String toString() {
		return text();
	}

	@Override
	public MaybeNode withNS(Map<String, String> namespaces) {
		return this;
	}

	@Override
	public MaybeNode withNS(String namespace, String prefix) {
		return this;
	}
	
	@Override
	public boolean isNone() {
		return false;
	}

}
