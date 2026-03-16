package conway26appl;
import conway.io.IoJavalin;
import main.java.conway.domain.*;
//import main.java.conway.devices.OutInWs;
//import main.java.conway.domain.*;
import unibo.basicomm23.utils.CommUtils;

public class MainConwayGui  {
   	private static final int nRows=20;
	private static final int nCols=20;
  	
    public static void main(String[] args) {
    	
    	//init
    	IoJavalin server = new IoJavalin();
    	LifeInterface life = new Life(nRows, nCols);
    	
    	//dependecies Injectrion
    	GameController controller = new LifeController(life, server);
    	server.setController(controller);
    	
	    System.out.println("MainConway | STARTS " );   	
	    
		var resource = MainConwayGui.class.getResource("/page");
		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);

	    MainConwayGui app = new MainConwayGui();
	    System.out.println("MainConway | ENDS " );  
    }

}