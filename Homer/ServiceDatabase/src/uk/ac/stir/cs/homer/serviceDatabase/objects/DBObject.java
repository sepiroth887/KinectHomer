package uk.ac.stir.cs.homer.serviceDatabase.objects;

public abstract class DBObject {

	public abstract int getConstructorSize();
	public abstract void init(String[] params);
}
