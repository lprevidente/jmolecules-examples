package example;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;

public class MyAggregateRoot implements AggregateRoot<MyAggregateRoot, Identifier> {

	MyAggregateRoot aggregate;

	@Override
	public Identifier getId() {
		return null;
	}
}
