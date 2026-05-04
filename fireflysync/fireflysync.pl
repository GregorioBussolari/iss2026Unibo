%====================================================================================
% fireflysync description   
%====================================================================================
event( sync, args(X) ).
event( unsync, argu(X) ).
dispatch( cellstate, cellstate(X,Y,COLOR) ).
%====================================================================================
context(ctxfirefly, "localhost",  "TCP", "8040").
context(ctxgrid, "127.0.0.1",  "TCP", "8050").
 qactor( creator, ctxfirefly, "it.unibo.creator.Creator").
 static(creator).
  qactor( sonarsimulator, ctxfirefly, "it.unibo.sonarsimulator.Sonarsimulator").
 static(sonarsimulator).
  qactor( griddisplay, ctxgrid, "external").
  qactor( firefly, ctxfirefly, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
