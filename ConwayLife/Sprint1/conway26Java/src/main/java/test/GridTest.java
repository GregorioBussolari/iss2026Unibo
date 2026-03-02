package main.java.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.conway.domain.Cell;
import main.java.conway.domain.Grid;
import main.java.conway.domain.IGrid;

public class GridTest {
	private IGrid g;
	
	@Before
	public void setup() {
		System.out.println("ConwayLifeTest setup");
		g = new Grid(10, 10);
	}
	
	@After
	public void down() {
		System.out.println("ConwayLifeTest down");
	}
	
	@Test
	public void testGridCellAlive() {
		System.out.println("ConwayLifeTest grid cell alive");
		g.setCellStatus(1, 2, true);
		boolean r = g.isCellAlive(1, 2);
		assertTrue(r);
	}
	
	@Test
	public void testGridCellDead() {
		System.out.println("ConwayLifeTest grid cell dead");
		g.setCellStatus(1, 2, false);
		boolean r = g.isCellAlive(1, 2);
		assertFalse(r);
	}
	
	@Test
	public void testNumRows() {
		System.out.println("ConwayLifeTest grid num rows");
		int rows = g.getNumRows();
		assertTrue(rows == 10);
	}
	
	@Test
	public void testNumCols() {
		System.out.println("ConwayLifeTest grid num cols");
		int cols = g.getNumCols();
		assertTrue(cols == 10);
	}

}
