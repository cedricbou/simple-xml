package foop.simple.xml;

/**
 * Every XML node is seen as a MaybeNode, it can represent a real node, or a
 * none node. None nodes are nodes containing nothing, they are used if you try
 * to dig element that do not exist, this trick is useful to avoid null pointer
 * exception.
 * 
 * @author Cedric
 * 
 */
public interface MaybeNode extends NamespaceSupport, PathSupport {

	/**
	 * Returns the children with this element name, or a none node if there was
	 * no such child.
	 * 
	 * @param name
	 *            the element name.
	 * @return A node representing the child identified by the specified name.
	 */
	public MaybeNode get(final String name);

	/**
	 * Returns the child found at the specified index, or a none node if there
	 * was no node at the index.
	 * 
	 * @param index
	 * @return
	 */
	public MaybeNode get(final int index);

	/**
	 * Returns the text contained in this element, it is not recursive to
	 * children elements. For a none node, it would return null. For an array
	 * node, it returns all element texts concatened together.
	 */
	public String text();
	
	/**
	 * Returns true if the current node is a NoneNode.
	 * @return true if this instanceof NoneNode.
	 */
	public boolean isNone();

}
