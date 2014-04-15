SimpleXML
======

[![Build Status](https://travis-ci.org/cedricbou/simple-xml.png?branch=master)](https://travis-ci.org/cedricbou/simple-xml)

Easily play with XML content without having to use object mapping or complicated API. 

Inspiration
-----------

For FiddleWith.it, I was in need of an easy XML browsing API, which would map nicely with JRuby. 

Key Concepts
--------

- No object mapping
- Namespaces support
- Safe deep browsing (automatically protects from null pointer exception while browsing through nodes).
- Read only


Installation and Configuration
------------------------------

TODO : maven dependency once published.

Usage
-----

```java
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
assertEquals("Simple XML in practice, get ready in two pages.",
		xml.get("books").get("book").get(0).get("description").text());
		
// You can actually use any prefix as the parser would ignore 
// it if it has not been explicitly asked in the parser (see below). 
assertEquals("Simple XML in practice, get ready in two pages.",
		xml.get("books").get("book").get(0).get("doc:description").text());

assertEquals("Simple XML in practice, get ready in two pages.",
		xml.get("books").get("book").get(0).get("anythingunknown:description").text());
		
// You can declare your own namespace prefix to resolve name conflicts
final MaybeNode withNS = xml
		.withNS("myprefix_forshelves", "urn:shelves-classification-system")
		.withNS("myprefix_fordoc", "urn:doc-marker");

assertEquals("This is the shelves left to the door from the second floor.", withNS.get("books").get("book").get(1).get("myprefix_forshelves:description").text());
assertEquals("Using Apache AXIOM with ease, become an API expert.", withNS.get("books").get("book").get(1).get("myprefix_fordoc:description").text());
```