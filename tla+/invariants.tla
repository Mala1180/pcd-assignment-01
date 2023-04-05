---- MODULE invariants ----
EXTENDS TLC, Integers


(*--algorithm invariants

variables
    c = 0;

define
    NoOverflowInvariant == (c < 3)
end define;

begin

        while (c < 3)
        do
            c := c + 1;
        end while;

end algorithm
*)
\* BEGIN TRANSLATION (chksum(pcal) = "9e9adcb4" /\ chksum(tla) = "210758cc")
VARIABLES c, pc

(* define statement *)
NoOverflowInvariant == (c < 3)


vars == << c, pc >>

Init == (* Global variables *)
        /\ c = 0
        /\ pc = "Lbl_1"

Lbl_1 == /\ pc = "Lbl_1"
         /\ IF (c < 3)
               THEN /\ c' = c + 1
                    /\ pc' = "Lbl_1"
               ELSE /\ pc' = "Done"
                    /\ c' = c

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == pc = "Done" /\ UNCHANGED vars

Next == Lbl_1
           \/ Terminating

Spec == Init /\ [][Next]_vars

Termination == <>(pc = "Done")

\* END TRANSLATION 
====
