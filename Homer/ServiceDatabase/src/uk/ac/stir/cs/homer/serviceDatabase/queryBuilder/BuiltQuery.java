package uk.ac.stir.cs.homer.serviceDatabase.queryBuilder;

import java.util.List;

public class BuiltQuery {
	public final String queryStr;
	public final List<String> args;

	public BuiltQuery(String queryStr, List<String> args) {
		this.queryStr = queryStr;
		this.args = args;
	}
}
