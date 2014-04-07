package foop.simple.xml;


public class WithPathBuilder {

	private final PathBuilder pathBuilder;

	protected WithPathBuilder(final PathBuilder pathBuilder) {
		this.pathBuilder = pathBuilder;
	}
	
	protected PathBuilder pathBuilder() {
		return pathBuilder;
	}
	
	public String path() {
		return pathBuilder.path();
	}
	
}
