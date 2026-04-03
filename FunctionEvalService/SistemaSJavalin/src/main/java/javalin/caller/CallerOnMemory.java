package main.java.javalin.caller;

import main.java.javalin.SistemaSJavalApplMsgsQueued;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class CallerOnMemory {
    private SistemaSJavalApplMsgsQueued server;

    public CallerOnMemory() {
        server = new SistemaSJavalApplMsgsQueued();
        server.configureInMemory();
    }

    public void doJob() {
        CommUtils.outblue("doJob ...");
        String startMsg = "msg(cmd,dispatch,callerinmem,server,start,0)";

        /*1*/ try {
			server.forward(new ApplMessage(startMsg));
		} catch (Exception e) {
			e.printStackTrace();
		}

        /*2*/ new Thread() {
            public void run() {
                try {
                    String reqMsg = "msg(eval,request,callerinmem,server,5.0,0)";
                    IApplMessage answer = server.request(new ApplMessage(reqMsg));
                    CommUtils.outblue("answer=" + answer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        /*3*/ new Thread() {
            public void run() {
                try {
                    String reqMsg = "msg(eval,request,callerinmem,server,4.0,0)";
                    IApplMessage answer = server.request(new ApplMessage(reqMsg));
                    CommUtils.outgreen("answer=" + answer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        System.out.println("Java.version=" + System.getProperty("java.version"));
        new CallerOnMemory().doJob();
    }
}
