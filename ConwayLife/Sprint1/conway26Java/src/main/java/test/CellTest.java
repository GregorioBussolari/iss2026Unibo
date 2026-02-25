package main.java.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.conway.domain.Cell;
import main.java.conway.domain.ICell;

public class CellTest {
	private ICell c; // definisco un simbolo che rispetta il constratto delle ICell
	//ho vincolato questo simbolo a esprimere entità che rispettano il contratto ICell
	//in questo caso non stiamo usando un'istanza di cell ma solo un simbolo utile per denotare le celle
	
	@Before
	public void setup() {
		System.out.println("ConwayLifeTest setup");
		c = new Cell();
	}
	
	@After
	public void down() {
		System.out.println("ConwayLifeTest down");
	}
	
	@Test
	public void testCellAlive() {
		System.out.println("ConwayLifeTest doing alive");
		c.setStatus(true);
		boolean r = c.isAlive();
		assertTrue(r);
	}
	
	@Test
	public void testCellDead() {
		System.out.println("ConwayLifeTest doing dead");
		c.setStatus(false);
		boolean r = c.isAlive();
		assertFalse(r);
	}

}
