package foop.simple.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.junit.Test;

public class SingleNodeTest {

	private final OMElement root1;
	
	private final OMElement root2;
	
	
	public SingleNodeTest() throws IOException {
		this.root1 = OMXMLBuilderFactory.createOMBuilder(
				Files.newInputStream(Paths.get("src/test/resources/foop/simple/xml", "FixtureSimpleXml.xml")))
				.getDocumentElement();

		this.root2 = OMXMLBuilderFactory.createOMBuilder(
				Files.newInputStream(Paths.get("src/test/resources/foop/simple/xml", "FixtureSimpleDocumentSoap.xml")))
				.getDocumentElement();
	}
	
	@Test public void getByNameAndCheckValues() {
		final SingleNode node = new SingleNode(root1, new PathBuilder());
		assertTrue(node.get("things").get("oneof") instanceof SingleNode);
		assertEquals("Hello World!", node.get("truc").get("foo").get("bar").text());
		assertEquals("This is  from outer space.", node.get("truc").get("foo").get("oof").text());
		assertEquals("Hello <p>paragraph</p> World!", node.get("truc").get("foo").get("rab").text());
	}

	@Test public void getByNameWithNamespaces() {
		final MaybeNode node = new SingleNode(root2, new PathBuilder()).withNS("stock", "http://www.example.org/stock");
		
		assertEquals("34.5", node.get("soap:Body").get("stock:GetStockPriceResponse").get("stock:Price").text());
		assertEquals("34.5", node.get("Body").get("GetStockPriceResponse").get("stock:Price").text());
		assertEquals("34.5", node.get("soap:Body").get("stock:GetStockPriceResponse").get("m:Price").text());

	}
	
}
