package main.java.conway.domain;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Life implements LifeInterface{
	private final int rows;
    private final int cols;
    
    // Due matrici distinte
    private Grid gridA;
    private Grid gridB;
    
 // Un riferimento che punta sempre alla griglia che contiene lo stato attuale
    private Grid currentGrid;
    private Grid nextGrid;
    
   public static LifeInterface CreateGameRules() {
	   return new Life(5, 5); 
	   // Dimensioni di default, possono essere 
	   //lette da un file di configurazione o passate come parametri
   }

    // Costruttore che accetta una griglia pre-configurata (utile per i test)
//    public Life(boolean[][] initialGrid) {
//    	this.rows = initialGrid.length;
//        this.cols = initialGrid[0].length;
//        
//        // Inizializziamo entrambe le matrici
//        this.gridA = new Grid(rows, cols);
//        this.gridB = new Grid(rows, cols);
//        
//        this.gridA = deepCopyJava8(initialGrid);
//        this.currentGrid = gridA;
//        this.nextGrid    = gridB;   
//    }

    // Costruttore che crea una griglia vuota di dimensioni specifiche
    public Life(int rows, int cols) {
    	this.rows = rows;
        this.cols = cols;
        this.gridA = new Grid(rows, cols);
        this.gridB = new Grid(rows, cols);
        this.currentGrid = gridA;
        this.nextGrid    = gridB;   
    }

    // Calcola la generazione successiva applicando le 4 regole di Conway
    public void nextGeneration() {
    	// Applichiamo le regole leggendo da currentGrid e scrivendo in nextGrid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int neighbors = countNeighborsLive(r, c);
                boolean isAlive = currentGrid.isCellAlive(r, c);
                //apply rules
                if (isAlive) {
                    nextGrid.setCellStatus(r, c, (neighbors == 2 || neighbors == 3));
                } else {
                	nextGrid.setCellStatus(r, c, (neighbors == 3));
                }
            }
        }

        // --- IL PING-PONG ---
        // Scambiamo i riferimenti: ciò che era 'next' diventa 'current'
        Grid temp = currentGrid;
        currentGrid      = nextGrid;
        nextGrid         = temp;
        // Nota: non abbiamo creato nuovi oggetti, abbiamo solo spostato i puntatori
    }
    
    protected int countNeighborsLive(int row, int col) {
        int count = 0, global_row, global_col;
        
        for(int i = -1; i <=1; i++) {
        	for (int j = -1; j <= 1; j++) {
        		if (i != 0 || j != 0) {
	                global_row = row + i;
	                global_col = col + j;
	                
	                if (global_row >= 0 && global_row < rows && 
	                    global_col >= 0 && global_col < cols) {
	                    
	                    if (currentGrid.isCellAlive(global_row, global_col)) {
	                        count++;
	                    }
	                }
                }
            }
        }
        
        //System.out.println("Cell (" + row + "," + col + ") has " + count + " live neighbors.");
        return count;
    }


    // Metodi di utilità per i test
    public void setCell(int r, int c, boolean state) { currentGrid.setCellStatus(r, c, state);; }
    public Grid getGrid() { return currentGrid; }

	@Override
	public boolean isAlive(int row, int col) {
		return currentGrid.isCellAlive(row, col);
	}

	@Override
	public int getRows() {
 		return rows;
	}

	@Override
	public int getCols() {
 		return cols;
	}
	
	//Versione NAIVE
//	private boolean[][] deepCopy(boolean[][] original) {
//	    if (original == null) return null;
//
//	    boolean[][] result = new boolean[original.length][];
//	    for (int i = 0; i < original.length; i++) {
//	        // Creiamo una nuova riga e copiamo i valori della riga originale
//	        result[i] = original[i].clone(); 
//	        // Nota: clone() su un array di primitivi (boolean) è sicuro 
//	        // perché i primitivi vengono copiati per valore.
//	    }
//	    return result;
//	}
	

//	private boolean[][] deepCopyJava8(boolean[][] original) {
//	    return Arrays.stream(original)
//	                 .map(boolean[]::clone)
//	                 .toArray(boolean[][]::new);
//	}
	
	@Override
	public String gridRep( ) {
	    return currentGrid.toString();
	}

	@Override
    public ICell getCell(int r, int c) { return gridA.getCell(r,c); }
	
    @Override
    public void resetGrid() { 
        gridA.reset();  
        gridB.reset();  
    }
}
