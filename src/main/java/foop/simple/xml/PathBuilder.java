package foop.simple.xml;

public class PathBuilder {

	private final String currentPath;
	
	protected PathBuilder(final String seedPath) {
		this.currentPath = seedPath;
	}
	
	protected PathBuilder() {
		this.currentPath = "/";
	}
	
	public PathBuilder withPart(final String part) {
		return new PathBuilder(currentPath + "/" + part);
	}
	
	public PathBuilder atIndex(final int index) {
		return new PathBuilder(currentPath + "[" + index + "]");
	}
	
	public PathBuilder withAttribute(final String attr) {
		return new PathBuilder(currentPath + "/@" + attr);
	}
	
	public PathBuilder withFindPredicate(final String predicate) {
		return new PathBuilder(currentPath + "[" + predicate + "]");
	}
	
	public String path() {
		return currentPath;
	}
}
