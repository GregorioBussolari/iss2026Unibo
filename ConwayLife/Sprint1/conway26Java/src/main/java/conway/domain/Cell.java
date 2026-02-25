package main.java.conway.domain;

public class Cell implements ICell{
	/*definisco la rappresentazione concreta di una cella
	la classe nasconde come è fatta la cella. Principio dell'information hiding
	classe è la rappresentazione della cella*/
	private boolean status;

	@Override
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public boolean isAlive() {
		return status;
	}
}
