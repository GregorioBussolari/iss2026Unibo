%====================================================================================
% fireflyqak description   
%====================================================================================
dispatch( build, build(X) ).
dispatch( start, arg(NUM) ).
dispatch( cellstate, cellstate(X,Y,COLOR) ). %set color of cell X,Y
%====================================================================================
context(ctxfirefly, "localhost",  "TCP", "8040").
context(ctxgrid, "127.0.0.1",  "TCP", "8050").
 qactor( helper, ctxfirefly, "fireflyqak.helper").
 static(helper).
  qactor( creator, ctxfirefly, "it.unibo.creator.Creator").
 static(creator).
  qactor( griddisplay, ctxgrid, "external").
  qactor( firefly, ctxfirefly, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
