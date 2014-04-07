package foop.simple.xml;

import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

import foop.simple.xml.ArrayNodeFromParent.ParentIteratorBuilder;

public class NodeFactory {

	private final Map<String, String> nsRegistry;
		
	public NodeFactory(final Map<String, String> nsRegistry) {
		this.nsRegistry = nsRegistry;
	}
	
	protected static MaybeNode noneNode(final PathBuilder pathBuilder) {
		return new NoneNode(pathBuilder);
	}
	
	@SuppressWarnings("unchecked")
	protected MaybeNode newNode(final OMElement parent, final QName qname, PathBuilder pathBuilder) {
		final Iterator<OMElement> elements = parent.getChildrenWithName(qname);
		
		if(elements != null && elements.hasNext()) {
			final OMElement first = elements.next();
			
			if(!elements.hasNext()) {
				return new SingleNode(first, nsRegistry, pathBuilder);
			}
			else {
				return new ArrayNodeFromParent(new ParentIteratorBuilder() {
					
					@Override
					public Iterator<OMElement> iterator() {
						return (Iterator<OMElement>)parent.getChildrenWithName(qname);
					}
				}, nsRegistry, pathBuilder);
			}
		}
		else {
			return noneNode(pathBuilder);
		}
	}
	
	protected MaybeNode newNode(final OMElement element, final PathBuilder pathBuilder) {
		if(element != null) {
			return new SingleNode(element, nsRegistry, pathBuilder);
		}
		else {
			return noneNode(pathBuilder);
		}
	}
}
