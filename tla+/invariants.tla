------------------------- MODULE invariants -------------------------

EXTENDS TLC, Integers, Sequences

(*--algorithm invariants

(*--
counted_files = intero che conta i file processati fino ad ora
files = array di stringhe che modellano i files

counted_files SEMPRE >= di Len(files)
counted_files PRIMA O POI = di Len(files) // lost update

N Thread (process) che leggono da files e scrivono in counted_files

*)

variables
    counted_files = 0;
    files = << "main() {print('Hello World!')}",
    "class Test extends Interface { private int test = 0}",
    "interface Interface { void printHelloWorld(); }" >>;

    threads = 1..3;

define
    NoOverflowCountedFiles == (counted_files <= Len(files))
\*    NoLostUpdates == <>(counted_files = Len(files))

    NoLostUpdates == \A t \in threads: <>(counted_files = Len(files))

end define;

fair process thread \in threads
begin
label1: counted_files := counted_files + 20;
end process

end algorithm;*)

\* BEGIN TRANSLATION (chksum(pcal) = "76c56370" /\ chksum(tla) = "7ae59979")
VARIABLES counted_files, files, threads, pc

(* define statement *)
NoOverflowCountedFiles == (counted_files <= Len(files))


NoLostUpdates == \A t \in threads: <>(counted_files = Len(files))


vars == << counted_files, files, threads, pc >>

ProcSet == (threads)

Init == (* Global variables *)
        /\ counted_files = 0
        /\ files =         << "main() {print('Hello World!')}",
                   "class Test extends Interface { private int test = 0}",
                   "interface Interface { void printHelloWorld(); }" >>
        /\ threads = 1..3
        /\ pc = [self \in ProcSet |-> "label1"]

label1(self) == /\ pc[self] = "label1"
                /\ counted_files' = counted_files + 20
                /\ pc' = [pc EXCEPT ![self] = "Done"]
                /\ UNCHANGED << files, threads >>

thread(self) == label1(self)

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == (\E self \in threads: thread(self))
           \/ Terminating

Spec == /\ Init /\ [][Next]_vars
        /\ \A self \in threads : WF_vars(thread(self))

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION

=============================================================================
\* Modification History
\* Last modified Sun Mar 28 19:13:30 CEST 2021 by aricci
\* Created Sun Mar 28 06:28:17 CEST 2021 by aricci


=============================================================================
\* Modification History
\* Created Sun Mar 28 19:10:48 CEST 2021 by aricci
