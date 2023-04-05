---- MODULE invariants ----
(*--algorithm invariants

variables
    c = 0;

define
    NoOverflowInvariant == (c < 3)
end define;

begin
    c:=c+1;
end algorithm
*)
====
