------------------------- MODULE invariants -------------------------

EXTENDS TLC, Integers, Sequences

(*--algorithm invariants

(*--
counted_files = intero che conta i file processati fino ad ora
files = << "main() {print('Hello World!')}",
    "class Test extends Interface { private int test = 0}",
    "interface Interface { void printHelloWorld(); }" >>;

counted_files SEMPRE >= di Len(files)
counted_files PRIMA O POI = di Len(files) // lost update

N Thread (process) che leggono da files e scrivono in counted_files
define
    NoOverflowCountedFiles == (counted_files <= Len(files))
\*    NoLostUpdates == <>(counted_files = Len(files))

    NoLostUpdates == \A t \in threads: <>(counted_files = Len(files))

end define;
*)
variables mutex = 1,
    files = << "file1", "file2", "file3", "file4", "file4", "file5", "file6" >>,
    counted_files = 0;

define
    MutualExclusion == []~(pc["p1"] = "CS" /\ pc["p2"] = "CS" /\
                           pc["p3"] = "CS" /\ pc["p4"] = "CS" /\ pc["p5"] = "CS")
    ProperFinalValue == <>(counted_files = Len(files))
    testProperty == [](5 = Len(files[1]))
end define;

macro wait(s) begin
  await s > 0;
  s := s - 1;
end macro;

macro signal(s) begin
  s := s + 1;
end macro;


macro updateCountedFiles() begin
  counted_files := counted_files + 1;
end macro;


fair process thread \in {"p1", "p2", "p3", "p4", "p5"}
begin MainLoop:
  while counted_files < Len(files) do

    NCS: skip;
    wait(mutex);

    CS: updateCountedFiles();
    signal(mutex);
  end while;
end process;


end algorithm;*)

\* BEGIN TRANSLATION (chksum(pcal) = "b4bc2ab9" /\ chksum(tla) = "3cb8c9d8")
VARIABLES mutex, files, counted_files, pc

(* define statement *)
MutualExclusion == []~(pc["p1"] = "CS" /\ pc["p2"] = "CS" /\
                       pc["p3"] = "CS" /\ pc["p4"] = "CS" /\ pc["p5"] = "CS")
ProperFinalValue == <>(counted_files = Len(files))


vars == << mutex, files, counted_files, pc >>

ProcSet == ({"p1", "p2", "p3", "p4", "p5"})

Init == (* Global variables *)
        /\ mutex = 1
        /\ files = << "file1", "file2", "file3", "file4", "file4", "file5", "file6" >>
        /\ counted_files = 0
        /\ pc = [self \in ProcSet |-> "MainLoop"]

MainLoop(self) == /\ pc[self] = "MainLoop"
                  /\ IF counted_files < Len(files)
                        THEN /\ pc' = [pc EXCEPT ![self] = "NCS"]
                        ELSE /\ pc' = [pc EXCEPT ![self] = "Done"]
                  /\ UNCHANGED << mutex, files, counted_files >>

NCS(self) == /\ pc[self] = "NCS"
             /\ TRUE
             /\ mutex > 0
             /\ mutex' = mutex - 1
             /\ pc' = [pc EXCEPT ![self] = "CS"]
             /\ UNCHANGED << files, counted_files >>

CS(self) == /\ pc[self] = "CS"
            /\ counted_files' = counted_files + 1
            /\ mutex' = mutex + 1
            /\ pc' = [pc EXCEPT ![self] = "MainLoop"]
            /\ files' = files

thread(self) == MainLoop(self) \/ NCS(self) \/ CS(self)

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == (\E self \in {"p1", "p2", "p3", "p4", "p5"}: thread(self))
           \/ Terminating

Spec == /\ Init /\ [][Next]_vars
        /\ \A self \in {"p1", "p2", "p3", "p4", "p5"} : WF_vars(thread(self))

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION

=============================================================================
