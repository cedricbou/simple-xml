package foop.simple.xml;

import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

import com.google.common.collect.ImmutableMap;

/**
 * A node without neighbours.
 * @author Cedric
 *
 */
public class SingleNode extends WithNamespaceRegistryAndPath implements MaybeNode {

	private final OMElement element;

	protected SingleNode(final OMElement element, final PathBuilder pathBuilder) {
		super(ImmutableMap.<String, String> of(), pathBuilder);
		this.element = element;
	}

	protected SingleNode(final OMElement element,
			final Map<String, String> registry, final PathBuilder pathBuilder) {
		super(registry, pathBuilder);
		this.element = element;
	}

	@Override
	public MaybeNode get(String name) {
		final QName qname = SimpleXmlUtils.tagToQName(name, registry());

		final String attr = element.getAttributeValue(qname);

		if (attr == null) {
			return new NodeFactory(registry())
					.newNode(element, qname, pathBuilder().withPart(name));
		} else {
			return new AttrNode(attr, pathBuilder().withAttribute(name));
		}
	}

	/**
	 * If index is 0 it will return the first child element, else it returns the
	 * NONE_NODE.
	 */
	@Override
	public MaybeNode get(int index) {
		if (index == 0) {
			final OMElement first = element.getFirstElement();
			return factory().newNode(first, pathBuilder().withPart("any").atIndex(0));
		}
		return NodeFactory.noneNode(pathBuilder());
	}

	@Override
	public String text() {
		return element.getText().trim();
	}

	@Override
	protected MaybeNode buildNodeWithNewNamespaceRegistry(
			ImmutableMap<String, String> registry) {
		return new SingleNode(element, registry, pathBuilder());
	}

	@Override
	public String toString() {
		return element.toString();
	}
}
