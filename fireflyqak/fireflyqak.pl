%====================================================================================
% fireflyqak description   
%====================================================================================
dispatch( build, build(X) ).
dispatch( start, arg(NUM) ).
%====================================================================================
context(ctxfirefly, "localhost",  "TCP", "8010").
 qactor( helper, ctxfirefly, "fireflyqak.helper").
 static(helper).
  qactor( creator, ctxfirefly, "it.unibo.creator.Creator").
 static(creator).
  qactor( firefly, ctxfirefly, "it.unibo.firefly.Firefly").
dynamic(firefly). %%Oct2023 
