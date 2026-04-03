package main.java.javalin;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;

import io.javalin.Javalin;
import io.javalin.websocket.WsConnectContext;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.InteractionBasic;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class SistemaSJavalApplMsgsQueued implements InteractionBasic {

	private record WorkTask(IApplMessage message, CompletableFuture<IApplMessage> future) {
	}

	protected static Vector<WsConnectContext> allConns = new Vector<WsConnectContext>();
	protected BlockingQueue<WorkTask> applMsgQueue = new LinkedBlockingDeque<WorkTask>(50);
	
	
	@Override
	public void forward(IApplMessage msg) throws Exception {
		WorkTask wt = new WorkTask( msg, null );
		applMsgQueue.add( wt );

	}

	@Override
	public IApplMessage request(IApplMessage msg) throws Exception {
		if( msg.isRequest() ) { //defensive
				CompletableFuture<IApplMessage> responseFuture = new CompletableFuture<>();
				WorkTask wt = new WorkTask( msg, responseFuture );
				applMsgQueue.add( wt );
				return responseFuture.get();
			}else throw new Exception("Msg not a request");
		
	}

	protected double eval(double x) {
		if (x > 4.0) {// simulazione delay
			CommUtils.delay(8000);
		}
		return Math.sin(x) + Math.cos(Math.sqrt(3) * x);
	}

	public void setWorkWS() {
		// Create and configure
		/* 2 */ var app = Javalin.create(config -> {
			config.bundledPlugins.enableCors(cors -> {
				cors.addRule(it -> it.anyHost());
			});
		}).start(8080);

//Parte WS -------------------------------
		app.ws("/eval", ws -> {
			ws.onConnect(ctx -> {
				/* 3 */ allConns.add(ctx);
				/* 4 */ emitEvent("welcome", ctx);
				/* 4 */ emitInfo("new connection established");
			});// onConnect

			/* 2 */ws.onMessage(ctx -> {
				IApplMessage am = readInputWS(ctx.message());
				/* 3 */CompletableFuture<IApplMessage> responseFuture = new CompletableFuture<>();
				/* 4 */responseFuture.thenAccept(res -> {
					/* 5 */ ctx.send(res.toJsonString());
				});
				/* 6 */WorkTask wt = new WorkTask(am, responseFuture);
				/* 7 */applMsgQueue.add(wt);
			});// onMessage

		}); // ws
	}

	protected IApplMessage readInputWS(String message) throws Exception {
		return ApplMessage.cvtJson(message);
	}

	protected void emitEvent(String s, WsConnectContext ctx) {
		String event = "{\"msg\" : \"EV\" }".replace("EV", s);
		ctx.send(event);
	}

	protected void emitInfo(String s) {
		allConns.forEach((conn) -> conn.send(s));
	}


	protected void setApplLogicWorker() {
		Runnable worker = new Runnable() {
	        @Override
	        public void run() {
	            while (true) {
	                try {
	                    WorkTask wt = applMsgQueue.take();
	                    IApplMessage inputMsg = wt.message;
	                    if (inputMsg.isRequest()) {
	                        doelabRequest(inputMsg, wt);
	                    } else if (inputMsg.isDispatch()) {
	                        elabDispatch(inputMsg);
	                    }
	                } catch (Exception e) { /* ... */ }
	            }
	        }
	    };
	    new Thread(worker).start();
	}
	
	
	public void configureInMemory() {
	    setApplLogicWorker();
	    CommUtils.outblue("server in-memory avviato");
	}
	
	protected void doelabRequest(IApplMessage req, WorkTask wt ) {
		IApplMessage replyMsg = elabRequest(req);
		wt.future.complete(replyMsg);
	}
	
	protected IApplMessage elabRequest(IApplMessage req ) {
		double x = Double.parseDouble(req.msgContent());
		double result = eval(x);
		
		IApplMessage replyMsg =
		CommUtils.buildReply("server", req.msgId(), "" + result,req.msgSender());
		return replyMsg;
	}
	
	protected void elabDispatch(IApplMessage m ) {
		CommUtils.outblue("ApplLogicThread elabDispatch:" + m);
	}
	
	 public void configureTheSystem() {   
	    	setWorkWS( );   
	    	setApplLogicWorker();
	        CommUtils.outblue("server avviato su ws://localhost:8080/eval");
	    }
	
	public static void main(String[] args) {
		new SistemaSJavalApplMsgsQueued().configureTheSystem();
	}

}
