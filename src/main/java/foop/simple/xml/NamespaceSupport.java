package foop.simple.xml;

import java.util.Map;

public interface NamespaceSupport {

	public MaybeNode withNS(final String namespace, final String prefix);
	
	public MaybeNode withNS(final Map<String, String> namespaces);

}
