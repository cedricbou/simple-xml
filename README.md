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
	"<foo><name>John Doe</name>"
	 + "<title lang=\"eng\">Unknown Personae</title>"
	 + "<bars><bar>one</bar><bar>two</bar></bars></foo>");

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
final MaybeNode blackhole1 = xml.get("bars").get("bar").get(1000).get("sub").get("foobar");
assertTrue(blackhole1.isNone());

// Get the path where it last found a known node.
assertEquals("/bars/bar[1000]", blackhole1.path());

final MaybeNode blackhole2 = xml.get("name").get("one").get("two").get("some");
assertTrue(blackhole2.isNone());
assertEquals("/name", blackhole2.path());

// Getting the text value would return null
assertEquals(null, blackhole2.text());
```