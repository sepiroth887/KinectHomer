package uk.ac.stir.cs.homer.serviceDatabase.queryBuilder;

public class StringPair {

	private String a;
	private String b;

	public StringPair(String a, String b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof StringPair) {
			StringPair other = (StringPair) arg0;
			return this.a == other.a && this.b == other.b
			    || this.a == other.b && this.b == other.a;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return a.hashCode() ^ b.hashCode();
	}
	
	@Override
	public String toString() {
		return a + "->" + b;
	}
}
