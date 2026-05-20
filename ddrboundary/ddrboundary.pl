%====================================================================================
% ddrboundary description   
%====================================================================================
dispatch( move, move(M) ).
request( cmd, cmd(MOVE,T) ).
reply( cmddone, cmddone(R) ).  %%for cmd
reply( cmdfailed, cmdfailed(T,CAUSE) ).  %%for cmd
request( step, step(TIME) ).
reply( stepdone, stepdone(V) ).  %%for step
reply( stepfailed, stepfailed(DURATION,CAUSE) ).  %%for step
%====================================================================================
context(ctxboundary, "localhost",  "TCP", "8125").
context(ctxrobotservice26, "127.0.0.1",  "TCP", "8125").
 qactor( robotactor, ctxrobotservice26, "external").
  qactor( boundaryworker, ctxboundary, "it.unibo.boundaryworker.Boundaryworker").
 static(boundaryworker).
