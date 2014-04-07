package foop.simple.xml;

import java.util.Map;

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
}
