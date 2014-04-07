package foop.simple.xml;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class WithNamespaceRegistryAndPath extends WithPathBuilder implements NamespaceSupport, PathSupport {

	private final Map<String, String> registry;
	
	private final NodeFactory factory;

	protected WithNamespaceRegistryAndPath(final Map<String, String> initRegistry, final PathBuilder pathBuilder) {
		super(pathBuilder);
		this.registry = initRegistry;
		this.factory = new NodeFactory(initRegistry);
	}

	@Override
	public MaybeNode withNS(Map<String, String> namespaces) {
		return buildNodeWithNewNamespaceRegistry(ImmutableMap
				.<String, String> builder().putAll(registry).putAll(namespaces)
				.build());
	}

	@Override
	public MaybeNode withNS(String namespace, String prefix) {
		return buildNodeWithNewNamespaceRegistry(ImmutableMap
				.<String, String> builder().putAll(registry).put(namespace, prefix)
				.build());
	}

	protected abstract MaybeNode buildNodeWithNewNamespaceRegistry(
			final ImmutableMap<String, String> registry);

	protected Map<String, String> registry() {
		return registry;
	}
	
	protected NodeFactory factory() {
		return factory;
	}

}
