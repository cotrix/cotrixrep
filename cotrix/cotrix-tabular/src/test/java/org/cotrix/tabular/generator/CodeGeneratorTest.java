package org.cotrix.tabular.generator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CodeGeneratorTest {

	CodeGenerator g = new CodeGenerator();

	@Test
	public void testGenerateCodeColumns() {
		int r = 2;
		int c = 2;
		List<List<String>> columns = g.generateCodeColumns(r, c);
		for (List<String> column : columns) {
			assertEquals(c, column.size());
			for (String cell : column) {
				// System.out.print(cell + " ");
			}
			// System.out.println();
			// System.out.println("new column ");
		}

	}

	@Test
	public void testCalculateColumnValues() {
		int rows = 2;
		int column = 2;
		double uniqueness = 0;
		assertEquals(1, g.calculateColumnValues(rows, column, uniqueness).size());

		uniqueness = 1;
		assertEquals(2, g.calculateColumnValues(rows, column, uniqueness).size());

		rows = 4;
		uniqueness = 0;
		assertEquals(1, g.calculateColumnValues(rows, column, uniqueness).size());

		uniqueness = 0.5;
		assertEquals(2, g.calculateColumnValues(rows, column, uniqueness).size());

		uniqueness = 1;
		assertEquals(4, g.calculateColumnValues(rows, column, uniqueness).size());

	}

	@Test
	public void testCalculateColumnValues2() {
		int rows = 2;
		int column = 0;
		double uniqueness = 0.01;
		assertEquals(1, g.calculateColumnValues(rows, column, uniqueness).size());
	}

	@Test
	public void testCalculateColumnValuesPrint() {
		int rows = 5;
		int column = 2;
		double uniqueness = 0;
		List<String> list = g.calculateColumnValues(rows, column, uniqueness);
		for (String cell : list) {
			// System.out.println(cell);
		}

	}

	@Test
	public void testSpreadValues1() {
		int rows = 5;
		List<String> list = new ArrayList<String>();
		list.add("A");
		g.spreadValues(list, rows);
		assertEquals(rows, list.size());
	}

	@Test
	public void testSpreadValues2() {
		int rows = 5;
		List<String> list = new ArrayList<String>();
		list.add("A");
		list.add("B");
		g.spreadValues(list, rows);
		assertEquals(rows, list.size());
	}

	@Test
	public void testSpreadValues3() {
		int rows = 25;
		List<String> list = new ArrayList<String>();
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		list.add("F");
		list.add("G");
		list.add("H");
		list.add("I");
		list.add("J");
		list.add("K");
		g.spreadValues(list, rows);
		assertEquals(rows, list.size());
	}

	@Test
	public void testSpreadValue3() {
		int rows = 4;
		List<String> list = new ArrayList<String>();
		list.add("A");
		list.add("B");
		g.spreadValues(list, rows);
		assertEquals(rows, list.size());
	}

	@Test
	public void testSpreadValue4() {
		int rows = 2;
		List<String> list = new ArrayList<String>();
		list.add("A");
		g.spreadValues(list, rows);
		assertEquals(rows, list.size());
	}

}
