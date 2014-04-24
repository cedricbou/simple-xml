package foop.simple.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.io.ByteStreams;

public class HowToUseTest {

	private final String FIXTURE_SIMPLE_XML;

	private final String FIXTURE_DOCUMENT_SOAP_XML;

	private final String FIXTURE_SOAP_XML;

	public HowToUseTest() throws IOException {
		this.FIXTURE_SIMPLE_XML = new String(ByteStreams.toByteArray(this
				.getClass().getResourceAsStream("FixtureSimpleXml.xml")));
		this.FIXTURE_DOCUMENT_SOAP_XML = new String(
				ByteStreams.toByteArray(this.getClass().getResourceAsStream(
						"FixtureSimpleDocumentSoap.xml")));
		this.FIXTURE_SOAP_XML = new String(ByteStreams.toByteArray(this
				.getClass().getResourceAsStream("FixtureSoapXml.xml")));

	}

	@Test
	public void quickStartGuide() {
		// Parse a XML
		final SimpleXml xml = new SimpleXml(
				"<foo xmlns:classify=\"urn:shelves-classification-system\" xmlns:doc=\"urn:doc-marker\">"
						+ "  <name>John Doe</name>"
						+ "   <title lang=\"eng\">Unknown Personae</title>"
						+ "   <bars>"
						+ "      <bar>one</bar>"
						+ "      <bar>two</bar>"
						+ "   </bars>"
						+ "   <books>"
						+ "      <book isbn=\"123456789\" classify:shelves=\"F4:G7\">"
						+ "         <title>Simple XML in practice</title>"
						+ "         <doc:description>Simple XML in practice, get ready in two pages.</doc:description>"
						+ "      </book>"
						+ "      <book isbn=\"987654321\" classify:shelves=\"A3:C45\">"
						+ "         <title>Apache AXIOM : the definitive guide</title>"
						+ "         <doc:description>Using Apache AXIOM with ease, become an API expert.</doc:description>"
						+ "         <classify:description>This is the shelves left to the door from the second floor.</classify:description>"
						+ "      </book>"
						+ "   </books>" 
						+ "</foo>");

		// Read an attribute
		final String lang = xml.get("title").get("lang").text();
		assertEquals("eng", lang);

		// Read a node
		final String name = xml.get("name").text();
		assertEquals("John Doe", name);

		// Read an array node
		final String bar = xml.get("bars").get("bar").get(1).text();
		assertEquals("two", bar);

		// What if you try to read something not existing ?
		// Try-it, it will not throw NPE, it will just work but give
		// you a 'NoneNode'.
		final MaybeNode blackhole1 = xml.get("bars").get("bar").get(1000)
				.get("sub").get("foobar");
		assertTrue(blackhole1.isNone());

		// Get the path where it last found a known node.
		assertEquals("//bars/bar[1000]", blackhole1.path());

		final MaybeNode blackhole2 = xml.get("name").get("one").get("two")
				.get("some");
		assertTrue(blackhole2.isNone());
		assertEquals("//name/one", blackhole2.path());

		// Getting the text value would return null
		assertEquals(null, blackhole2.text());

		// And how can I read elements with namespaces ?
		// Just use the tag name
		assertEquals("Simple XML in practice, get ready in two pages.", xml
				.get("books").get("book").get(0).get("description").text());

		// You can actually use any prefix as the parser would ignore
		// it if it has not been explicitly asked in the parser (see below).
		assertEquals("Simple XML in practice, get ready in two pages.", xml
				.get("books").get("book").get(0).get("doc:description").text());

		assertEquals(
				"Simple XML in practice, get ready in two pages.",
				xml.get("books").get("book").get(0)
						.get("anythingunknown:description").text());

		// You can declare your own namespace prefix to resolve name conflicts
		final MaybeNode withNS = xml.withNS("myprefix_forshelves",
				"urn:shelves-classification-system").withNS("myprefix_fordoc",
				"urn:doc-marker");

		assertEquals(
				"This is the shelves left to the door from the second floor.",
				withNS.get("books").get("book").get(1)
						.get("myprefix_forshelves:description").text());
		assertEquals(
				"Using Apache AXIOM with ease, become an API expert.",
				withNS.get("books").get("book").get(1)
						.get("myprefix_fordoc:description").text());
		
		// You can find a node by its attribute and value 
		assertEquals("Apache AXIOM : the definitive guide",
				xml.get("books").get("book")
					.findByValue("isbn", "987654321")
					.get("title").text());

		assertEquals("Using Apache AXIOM with ease, become an API expert.",
				withNS.get("books").get("book")
					.findByValue("myprefix_forshelves:shelves", "A3:C45")
					.get("myprefix_fordoc:description").text());
		
				
	}

	@Test
	public void readSoapXml() {
		final SimpleXml xml = new SimpleXml(FIXTURE_SOAP_XML);
		assertNotNull(xml.get("soap:Body"));
		assertTrue(xml.get("soap:Body").toString()
				.contains("<ChangeAngleUnitResult>1."));
	}

	@Test
	public void testFindByAttrValue() {
		final SimpleXml xml = new SimpleXml(FIXTURE_SIMPLE_XML);
		
		assertNotNull(xml.get("nodes").get("node").get(1).get("poi")
				.findByValue("type", "attraction"));
		assertTrue(xml.get("nodes").get("node").get(1).get("poi")
				.findByValue("type", "attraction") instanceof SingleNode);
		assertEquals("ParadiZoo",
				xml.get("nodes").get("node").get(1).get("poi")
						.findByValue("type", "attraction").get("name").text());
		
		assertTrue(xml.get("things") instanceof SingleNode);
		assertEquals("here", xml.get("things").findByValue("them", "is present").text());
		assertTrue(xml.get("things").findByValue("them", "is absent").isNone());

	}

	@Test
	public void testFind() {
		final SimpleXml xml = new SimpleXml(FIXTURE_SIMPLE_XML);
		
		final MaybeNode node = xml.get("nodes").get("node").get(1).get("poi").find(new Predicate<MaybeNode>() {
			public boolean apply(final MaybeNode node) {
				final MaybeNode type = node.get("type");
				if(!type.isNone()) {
					if("attraction".equals(type.text())) {
						return true;
					}
				}
				return false;
			}
		} );
		
		assertNotNull(node);
		assertTrue(node instanceof SingleNode);
		assertEquals("ParadiZoo", node.get("name").text());
	}


	@Test
	public void testArrayToString() {
		final SimpleXml xml = new SimpleXml(FIXTURE_SIMPLE_XML);
		assertNotNull(xml.get("dependencies").get("dependency"));
		assertTrue(xml.get("dependencies").get("dependency") instanceof ArrayNodeFromParent);
		assertTrue(xml.get("dependencies").get("dependency").toString()
				.contains("<artifactId>"));
	}

	@Test
	public void readSimpleXml() {
		final SimpleXml xml = new SimpleXml(FIXTURE_SIMPLE_XML);

		assertEquals("1.1.1", xml.get("build").get("xml.version").text());

		final MaybeNode node = xml.get("dependencies").get("dependency").get(1);

		assertEquals("axiom-impl", node.get("artifactId").text());
		assertEquals("runtime", node.get("scope").text());
		assertTrue(node.get("azerty") instanceof NoneNode);

		assertEquals("is present", xml.get("things").get("oneof").get("them")
				.text());
	}

	@Test
	public void readNamespacedXml() {
		final SimpleXml xml = new SimpleXml(FIXTURE_DOCUMENT_SOAP_XML);

		final Stopwatch stopwatch = Stopwatch.createStarted();

		final int ITER = 1000000;

		for (int i = 0; i < ITER; ++i) {

			assertEquals("34.5",
					xml.withNS("http://www.w3.org/2001/12/soap-envelope", "ws")
							.withNS("http://www.example.org/stock", "stock")
							.get("ws:Body").get("stock:GetStockPriceResponse")
							.get("stock:Price").text());
		}

		System.out
				.println("Throughput : readnampacesxml : "
						+ ((double) ITER
								/ (double) stopwatch
										.elapsed(TimeUnit.NANOSECONDS) * 1000000000)
						+ " iterations/sec");
	}

}
