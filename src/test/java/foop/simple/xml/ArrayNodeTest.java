package foop.simple.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.junit.Test;

public class ArrayNodeTest {

	private final OMElement root1;

	private final OMElement root2;

	public ArrayNodeTest() throws IOException {
		this.root1 = OMXMLBuilderFactory.createOMBuilder(
				Files.newInputStream(Paths.get(
						"src/test/resources/foop/simple/xml",
						"FixtureSimpleXml.xml"))).getDocumentElement();

		this.root2 = OMXMLBuilderFactory.createOMBuilder(
				Files.newInputStream(Paths
						.get("src/test/resources/foop/simple/xml",
								"sample-huge.xml"))).getDocumentElement();
	}

	@Test
	public void getByNameAndCheckValues() {
		final SingleNode node = new SingleNode(root1, new PathBuilder());
		assertTrue(node.get("dependencies").get("dependency") instanceof ArrayNodeFromParent);
		assertEquals("axiom-api", node.get("dependencies").get("dependency")
				.get("artifactId").text());
	}

	@Test
	public void getByIndex() {
		final SingleNode node = new SingleNode(root2, new PathBuilder());

		final MaybeNode speech = node.get("ACT").get("SCENE").get(0)
				.get("SPEECH");

		for (int i = 0; i < 10000; ++i) {
			assertEquals("Bernardo?", speech.get(3).get("LINE").text());
			assertEquals("It is offended.", speech.get(36).get("LINE").text());
			assertEquals("What, has this thing appear'd again to-night?",
					speech.get(21).get("LINE").text());
			assertEquals(
					"In what particular thought to work I know not;But in the gross and scope of my opinion,This bodes some strange eruption to our state.",
					speech.get(45).get("LINE").text());
		}
	}
}
