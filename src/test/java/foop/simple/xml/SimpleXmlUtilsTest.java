package foop.simple.xml;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class SimpleXmlUtilsTest {

	private final static Map<String, String> namespaces = ImmutableMap.of("ns1", "urn:namespace1", "ns2", "urn:namespace2");
	
	@Test public void localNamespaceQName() {
		final QName expected = new QName("azerty");
		
		assertEquals(expected, SimpleXmlUtils.tagToQName("azerty", ImmutableMap.<String, String>of()));
	}

	@Test public void withNamespaceQName() {
		final QName expected = new QName("urn:namespace2", "azerty");
		
		assertEquals(expected, SimpleXmlUtils.tagToQName("ns2:azerty", namespaces));
	}
	
	@Test public void unknownNamespaceQNameFallbackToLocalQName() {
		final QName expected = new QName("azerty");
		
		assertEquals(expected, SimpleXmlUtils.tagToQName("ns99:azerty", namespaces));
	}

}
