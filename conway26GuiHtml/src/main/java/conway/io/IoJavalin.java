package conway.io;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsMessageContext;
import main.java.conway.domain.GameController;
import main.java.conway.domain.IGrid;
import main.java.conway.domain.IOutDev;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;

public class IoJavalin implements IOutDev{
	
	private WsMessageContext pageCtx ;
	private GameController controller; //per iniettare la dipendenza
	public IoJavalin() {
        var app = Javalin.create(config -> {
			config.staticFiles.add(staticFiles -> {
				staticFiles.directory = "/page";
				staticFiles.location = Location.CLASSPATH; // Cerca dentro il JAR/Classpath
				/*
				 * i file sono "impacchettati" con il codice, non cercati sul disco rigido esterno.
				 */
		    });
		}).start(8080);
 
/*
 * --------------------------------------------
 * Parte HTTP        
 * --------------------------------------------
 */
        app.get("/", ctx -> {
    		//Path path = Path.of("./src/main/resources/page/ConwayInOutPage.html");    		    
        	/*
        	 * Java cercherà il file all'interno del Classpath 
        	 * (dentro il JAR o nelle cartelle dei sorgenti di Eclipse), 
        	 * rendendo il codice universale
         	 */
        	var inputStream = getClass().getResourceAsStream("/page/ConwayInOutPage.html");       	
        	if (inputStream != null) {
        		// Trasformiamo l'inputStream in stringa (o lo mandiamo come stream)
        	    String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        	    ctx.html(content);
        	} else {
		        ctx.status(404).result("File non trovato nel file system");
		    }
		    //ctx.result("Hello from Java!"));  //la forma più semplice di risposta
        }); 
        
        app.get("/greet/{name}", ctx -> {
            String name = ctx.pathParam("name");
            ctx.result("Hello, " + name + "!");
        }); //http://localhost:8080/greet/Alice
        
        app.get("/api/users", ctx -> {
            Map<String, Object> user = Map.of("id", 1, "name", "Bob");
            ctx.json(user); // Auto-converts to JSON
        });
        
        /*
         * Javalin v5+: Si passa solo la "promessa" (il Supplier del Future). 
         * Javalin è diventato più intelligente: se il Future restituisce una Stringa, 
         * lui fa ctx.result(stringa). Se restituisce un oggetto, lui fa ctx.json(oggetto).
         * 
         */
        app.get("/async", ctx -> {
        	ctx.future(() -> {
	        	// Creiamo il future
	            CompletableFuture<String> future = new CompletableFuture<>();
	            
	            // Eseguiamo il lavoro in un altro thread
	            new Thread(() -> { 
	                try {
	                    Thread.sleep(2000); // Simulazione calcolo pesante
	                    future.complete("IoJavalin | Risultato calcolato asincronamente");
	                } catch (Exception e) {
	                    future.completeExceptionally(e);
	                }
	            });
	            
	            return future; // Restituiamo il future a Javalin
        	});
        });
        
        app.get("/async1", ctx -> {
            ctx.future(() -> CompletableFuture.supplyAsync(() -> {
                // Simuliamo l'operazione lenta
                try {
                    Thread.sleep(2000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "IoJavalin | Risultato calcolato con supplyAsync";
            }));
        });
/*
 * --------------------------------------------
 * Parte Websocket
 * --------------------------------------------
 */
        
        app.ws("/chat", ws -> {
            ws.onConnect(ctx -> CommUtils.outgreen("Client connected chat!"));
            ws.onMessage(ctx -> {
                String message = ctx.message();
                CommUtils.outcyan("IoJavalin |  riceve:" + message);
                ctx.send("Echo: " + message);
            });
        });        
//        app.ws("/eval", ws -> {
//            ws.onConnect(ctx -> CommUtils.outgreen("IoJavalin | Client connected eval"));
//            ws.onMessage(ctx -> {
//                String message = ctx.message();     
//                CommUtils.outblue("IoJavalin |  eval receives:" + message );
//                try {
//                	IApplMessage m = new ApplMessage(message);
//                    CommUtils.outblue("IoJavalin |  eval:" + m.msgContent() );
//                    if( m.msgContent().equals("ready")) { 
//                    	pageCtx = ctx;  //memorizzo connession pagina
//                    }else if( m.msgContent().contains("cell(")) { 
//                    	//Funziona se arriva da CallerServerWs es. cell(5,6,1)
//                    	pageCtx.send( m.msgContent()); 
//                    	//TODO: inviare a LifeController
//                    }else ctx.send(m.msgContent());
//                }catch(Exception e) {
//                	CommUtils.outred("IoJavalin |  error:" + e.getMessage());
//                }               
//            });
//        }); 
        app.ws("/eval", ws -> {
            ws.onConnect(ctx -> CommUtils.outgreen("IoJavalin | Client connected"));
            ws.onMessage(ctx -> {
                String message = ctx.message();
                try {
                    IApplMessage m = new ApplMessage(message);
                    String content = m.msgContent();

                    switch (content) {
                        case "ready" -> {
                            pageCtx = ctx;
                            CommUtils.outgreen("IoJavalin | pagina pronta");
                        }
                        case "start" -> controller.onStart();
                        case "stop"  -> controller.onStop();
                        case "clear" -> controller.onClear();
                        default -> {
                            if (content.contains("cell(")) {
                                // Click su cella dalla pagina → cambia stato
                                // es. "cell(3,5)" → switchCellState(3,5)
                                String coords = content.replace("cell(","").replace(")","");
                                String[] parts = coords.split(",");
                                int r = Integer.parseInt(parts[0].trim());
                                int c = Integer.parseInt(parts[1].trim());
                                controller.switchCellState(r, c);
                                CommUtils.outgreen("Recived cell (" + r + ","+c+" )");
                            } else {
                                ctx.send("unknown: " + content);
                            }
                        }
                    }
                } catch (Exception e) {
                    CommUtils.outred("IoJavalin | error: " + e.getMessage());
                }
            });
        });
	}
	
	public void setController(GameController controller) {
		this.controller = controller;
	}
	
	
	@Override
	public void display(String msg) {
		if (pageCtx != null) {
	        pageCtx.send(msg);
	    }
	}

	@Override
	public void displayCell(IGrid grid, int x, int y) {
		if (pageCtx != null) {
	        int state = grid.getCell(x, y).isAlive() ? 0 : 1;
	        String payload = "cell(" + x + "," + y + "," + state + ")";
//	        IApplMessage cellMessage = CommUtils.buildDispatch("server", 
//	        													"displayCell", 	
//	        													payload , 
//	        													"client");
	        pageCtx.send(payload); 
	    }
	}

	@Override
	public void close() {
		if (pageCtx != null) {
	        pageCtx.session.close();
	    }
	}

	@Override
	public void displayGrid(IGrid grid) {
		if (pageCtx != null) {
			int rows = grid.getNumRows();
	        int cols = grid.getNumCols();

	        // Invio lo stato di ogni singola cella
	        for (int r = 0; r < rows; r++) {
	            for (int c = 0; c < cols; c++) {
	                displayCell(grid, r, c);
	            }
	        }
	    }
	}
	
//	public static void main(String[] args) {
//		var resource = IoJavalin.class.getResource("/pages");
//		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);
//		new IoJavalin();
//	}

}
