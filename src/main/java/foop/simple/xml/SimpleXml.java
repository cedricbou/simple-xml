package foop.simple.xml;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.apache.axiom.om.OMXMLBuilderFactory;

import com.google.common.collect.ImmutableMap;

public class SimpleXml extends WithNamespaceRegistryAndPath implements MaybeNode {

	private final MaybeNode root;

	public SimpleXml(final String content) {
		super(ImmutableMap.<String, String>of(), new PathBuilder());
		this.root = buildRoot(content, Charset.defaultCharset());
	}

	public SimpleXml(final String content, final Charset charset) {
		super(ImmutableMap.<String, String>of(), new PathBuilder());
		this.root = buildRoot(content, charset);
	}

	
	private SimpleXml(final MaybeNode root, final ImmutableMap<String, String> ns, final PathBuilder pathBuilder) {
		super(ns, pathBuilder);
		this.root = root;
	}

	private MaybeNode buildRoot(final String content, final Charset charset) {
		return new NodeFactory(registry()).newNode((OMXMLBuilderFactory.createOMBuilder(
				new ByteArrayInputStream(content.getBytes(charset)))
				.getDocumentElement()), pathBuilder());
	}
	
	@Override
	public MaybeNode get(String name) {
		return root.get(name);
	}

	@Override
	public MaybeNode get(int index) {
		return root.get(index);
	}
	
	@Override
	public String text() {
		return root.text();
	}

	@Override
	protected MaybeNode buildNodeWithNewNamespaceRegistry(
			ImmutableMap<String, String> registry) {
		return new SimpleXml(root, registry, pathBuilder());
	}
	
	@Override
	public String toString() {
		return root.toString();
	}
}
