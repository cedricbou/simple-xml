package foop.simple.xml;


public interface MaybeNode extends NamespaceSupport, PathSupport {

	public MaybeNode get(final String name);

	public MaybeNode get(final int index);
	
	public String text();

}
