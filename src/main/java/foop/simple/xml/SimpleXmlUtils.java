package foop.simple.xml;

import java.util.Map;

import javax.xml.namespace.QName;

public class SimpleXmlUtils {

	/**
	 * Transform a tag, local or prefixed with a namespace alias, to a local or
	 * full QName.
	 * 
	 * @param tag
	 *            The tag to convert, it can be local like <code>azerty</code>
	 *            or qualified like <code>ns1:azerty</code>.
	 * @param nsRegistry
	 *            The namespaces registry containing for an alias, the full
	 *            namespace name.
	 * @return the QName corresponding to the tag, it will be a local QName
	 *         except if a prefix was provided and it could be found in the
	 *         namespace registry.
	 */
	public static QName tagToQName(final String tag,
			final Map<String, String> nsRegistry) {
		final String[] split = tag.split("\\:");

		if (split.length <= 1) {
			return new QName(split[0]);
		} else {
			final String namespace = nsRegistry.get(split[0]);

			if (namespace != null) {
				System.out.println(namespace + ":" + split[1]);
				return new QName(namespace, split[1]);
			} else {
				return new QName(split[1]);
			}
		}
	}

}
