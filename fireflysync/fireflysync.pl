%====================================================================================
% fireflysync description   
%====================================================================================
event( sync, time(X) ).
%====================================================================================
context(ctxfirefly, "localhost",  "TCP", "8040").
 qactor( creator, ctxfirefly, "it.unibo.creator.Creator").
 static(creator).
  qactor( fireflyqueen, ctxfirefly, "it.unibo.fireflyqueen.Fireflyqueen").
dynamic(fireflyqueen). %%Oct2023 
  qactor( firefly, ctxfirefly, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
