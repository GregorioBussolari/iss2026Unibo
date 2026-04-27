%====================================================================================
% qakdemo26 description   
%====================================================================================
dispatch( msg1, msg1(ARG) ).
%====================================================================================
context(ctxqakdemo26, "localhost",  "TCP", "8010").
 qactor( sender, ctxqakdemo26, "it.unibo.sender.Sender").
 static(sender).
  qactor( receiver, ctxqakdemo26, "it.unibo.receiver.Receiver").
 static(receiver).
