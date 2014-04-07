package foop.simple.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

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
	public void readSoapXml() {
		final SimpleXml xml = new SimpleXml(FIXTURE_SOAP_XML);
		assertNotNull(xml.get("soap:Body"));
		assertTrue(xml.get("soap:Body").toString()
				.contains("<ChangeAngleUnitResult>1."));
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
