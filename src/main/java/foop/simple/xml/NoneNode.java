package foop.simple.xml;

import java.util.Map;

/**
 * A none node represents a node not existing in the parsed XML.
 * 
 * It will allow you to attempt digging children elements without failing with a
 * NPE, it will simple return a none node. Getting value will return null The
 * path is blocked at the last known valid node.
 * 
 * @author Cedric
 * 
 */
public class NoneNode extends WithPathBuilder implements MaybeNode {

	protected NoneNode(final PathBuilder pathBuilder) {
		super(pathBuilder);
	}

	@Override
	public MaybeNode get(String name) {
		return this;
	}

	@Override
	public MaybeNode get(int index) {
		return this;
	}

	public String text() {
		return null;
	};

	@Override
	public MaybeNode withNS(Map<String, String> namespaces) {
		return this;
	}

	@Override
	public MaybeNode withNS(String namespace, String prefix) {
		return this;
	}

	@Override
	public String toString() {
		return "undef";
	}

	@Override
	public boolean isNone() {
		return true;
	}
}
