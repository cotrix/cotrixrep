package org.cotrix.tabular.mining;

/**
 * 
 * When mining, a list of relations is build.
 * 
 * @author Erik van Ingen
 * 
 */
public class RelationX {

	protected int fromColumn;
	protected int[] toColumns;

	public int getFromColumn() {
		return fromColumn;
	}

	public void setFromColumn(int fromColumn) {
		this.fromColumn = fromColumn;
	}

	public int[] getToColumns() {
		return toColumns;
	}

	public void setToColumns(int[] toColumns) {
		this.toColumns = toColumns;
	}

}
