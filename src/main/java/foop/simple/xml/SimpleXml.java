package foop.simple.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.util.StAXParserConfiguration;

import com.google.common.collect.ImmutableMap;

/**
 * Simple XML Parser, it represents the first node of the XML file.
 * 
 * It uses Axiom API to build a first node from which it is possible to browse to children nodes.
 * It can be build from an XML String, or an input stream representing an XML content.
 * 
 * @author Cedric
 *
 */
public class SimpleXml extends WithNamespaceRegistryAndPath implements
		MaybeNode {

	private final MaybeNode root;

	/**
	 * Create a simple XML object from an XML in a String, default system charset is assumed.
	 * @param content the XML as a string, encoded with system default charset.
	 */
	public SimpleXml(final String content) {
		this(content, Charset.defaultCharset());
	}

	/**
	 * Create a simple XML object from a XML in a String, it will use specified charset to parse the file.
	 * @param content the XML as a string, encoded with the specified charset.
	 * @param charset the charset used to encode the string.
	 */
	public SimpleXml(final String content, final Charset charset) {
		super(ImmutableMap.<String, String> of(), new PathBuilder());
		this.root = buildRoot(content, charset);
	}

	/**
	 * Create a simple XML object from a XML input stream.
	 * @param in the input stream representing the XML.
	 */
	public SimpleXml(final InputStream in) {
		super(ImmutableMap.<String, String> of(), new PathBuilder());
		this.root = buildRoot(in);
	}

	private SimpleXml(final MaybeNode root,
			final ImmutableMap<String, String> ns, final PathBuilder pathBuilder) {
		super(ns, pathBuilder);
		this.root = root;
	}

	private MaybeNode buildRoot(final String content, final Charset charset) {
		return new NodeFactory(registry()).newNode((OMXMLBuilderFactory
				.createOMBuilder(StAXParserConfiguration.STANDALONE, new ByteArrayInputStream(content
						.getBytes(charset))).getDocumentElement()),
				pathBuilder());
	}

	private MaybeNode buildRoot(final InputStream in) {
		return new NodeFactory(registry()).newNode(
				(OMXMLBuilderFactory.createOMBuilder(StAXParserConfiguration.STANDALONE, in).getDocumentElement()),
				pathBuilder());
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
