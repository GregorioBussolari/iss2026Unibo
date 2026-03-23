package javalin.io;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import unibo.basicomm23.utils.CommUtils;

public class SistemaSJavalin {
	private static AtomicInteger pageCounter = new AtomicInteger(0);
	private String firstCaller        = null;
	
	public void doJob() {
		Javalin app = Javalin.create(config -> {
			
            //config.http.asyncTimeout = 300000L; // 5 minuti in millisecondi
        	config.jetty.modifyWebSocketServletFactory(factory -> {
                // Imposta il timeout (ad esempio 5 minuti)
                factory.setIdleTimeout(Duration.ofMinutes(30));
            });
		}).start(8080);
		
		
		//PARTE WS ------------------------------------
			app.ws("/eval", ws -> {
				ws.onConnect(
			            ctx -> { //myctx=ctx; 
	    	        int idAssegnato = pageCounter.incrementAndGet();
	    	        String callerName = "caller"+idAssegnato;
	     			if( firstCaller == null ) {
	     				firstCaller = callerName;
	     			}
	     			CommUtils.outmagenta("connected ..." + callerName);
	     			 
	     			// Ogni 20 secondi invia un segnale per "svegliare" i proxy
//			    	heartbeatTask = executor.scheduleAtFixedRate(
//			            () -> { if(ctx.session.isOpen()) sendsafe(ctx,"PING");}, //lambda // CommUtils.outcyan("PING");
//			                    20,  //QUANTO ASPETTARE LA PRIMA VOLTA. Se 0, il primo PING parte istantaneamente (inutile)
//			                    20,  //OGNI QUANTO RIPETERE
//			                    TimeUnit.SECONDS
//			            );
	     			
			            });
					
					
					ws.onMessage(ctx -> {
							String message = ctx.message();
							try {
								double x = Double.parseDouble(message);
								double r = Math.sin(x)+Math.cos(Math.sqrt(3)*x);
								String result = "Result : " + r + ", Input : " + x;
								ctx.send(result);
							}catch (NumberFormatException e) {
								ctx.send("Errore: num non valido");
							}
					});
					
					app.get("/", ctx -> {
			        	var inputStream = getClass().getResourceAsStream("/CallerBasic.html");     
			        	if (inputStream != null) {
			        		// Trasformiamo l'inputStream in stringa (o lo mandiamo come stream)
			        	    String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
			        	    ctx.html(content);
			        	} else {
					        ctx.status(404).result("File non trovato nel file system");
					    }
					    //ctx.result("Hello from Java!"));  //la forma più semplice di risposta
			        }); 
					
					app.get("/eval", ctx -> {
								double x = Double.parseDouble(
								ctx.queryParam("x"));
								double r =
								Math.sin(x)+Math.cos(Math.sqrt(3)*x);
								ctx.json(Map.of(
									"fullUrl", ctx.fullUrl(),
									"result", r));
					});
							
					app.post("/evaluate", ctx -> {//CORS
								org.json.simple.JSONObject m =
								CommUtils.parseForJson(ctx.body());
								String xs= ""+m.get("x");
								try {
									double x = Double.parseDouble(xs);
									double r =
									Math.sin(x)+Math.cos(Math.sqrt(3)*x);
									// Invia risposta in JSON
									ctx.json(Map.of(
									"fullUrl", ctx.fullUrl(),
										"body", ctx.body(), "result", r));
								} catch (NumberFormatException e) {
									ctx.json(Map.of(
									"fullUrl", ctx.fullUrl(),
									"body", ctx.body(),
									"result", "num non valido"));
								}
							});					
					
					
					ws.onClose(
							ctx->{System.out.println("sbye");}
					);
			});//ws
				
	}////doJob
		
	
		
		
	/*	--MAIN--*/
	public static void main(String[] args) {
		new SistemaSJavalin().doJob();
	}
}
