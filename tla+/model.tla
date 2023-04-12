------------------------- MODULE model -------------------------

EXTENDS TLC, Integers, Sequences

(*--algorithm model

variables mutex = 1,
    strings = << "file0", "file1", "file2", "file3", "file4", "file5", "file6", "file7", "file8", "file9">>,
    counted_strings = 0,
    counted_chars = 0,
    i = 1;

define
    MutualExclusion == []~((pc["p1"] = "CS" /\ pc["p2"] = "CS") \/
                           (pc["p1"] = "CS" /\ pc["p3"] = "CS") \/
                           (pc["p2"] = "CS" /\ pc["p3"] = "CS") \/
                           (pc["p1"] = "CS" /\ pc["p2"] = "CS" /\ pc["p3"] = "CS"))
    ProperFinalFilesCounter == <>(counted_strings = Len(strings))
    ProperFinalCharsCounter == <>(counted_chars = Len(strings) * Len(strings[1]))
end define;

macro wait(s) begin
  await s > 0;
  s := s - 1;
end macro;

macro signal(s) begin
  s := s + 1;
end macro;


macro updateCounters(n) begin
  counted_chars := counted_chars + n;
  counted_strings := counted_strings + 1;
end macro;


fair process thread \in {"p1", "p2", "p3"}
begin MainLoop:

  while counted_strings < Len(strings) do
    NCS: skip;
    wait(mutex);

    CS:
    if i <= Len(strings) then
    updateCounters(Len(strings[i]));
    else skip;
    end if;
    i := i + 1;
    signal(mutex);


  end while;
end process;


end algorithm;*)

\* BEGIN TRANSLATION (chksum(pcal) = "1ab55dcd" /\ chksum(tla) = "eed9e343")
VARIABLES mutex, files, counted_files, counted_chars, i, pc

(* define statement *)
MutualExclusion == []~((pc["p1"] = "CS" /\ pc["p2"] = "CS") \/
                       (pc["p1"] = "CS" /\ pc["p3"] = "CS") \/
                       (pc["p2"] = "CS" /\ pc["p3"] = "CS") \/
                       (pc["p1"] = "CS" /\ pc["p2"] = "CS" /\ pc["p3"] = "CS"))
ProperFinalFileCounter == <>(counted_files = Len(files))
ProperFinalCharCounter == <>(counted_chars = Len(files) * Len(files[1]))


vars == << mutex, files, counted_files, counted_chars, i, pc >>

ProcSet == ({"p1", "p2", "p3"})

Init == (* Global variables *)
        /\ mutex = 1
        /\ files = << "file0", "file1", "file2", "file3", "file4", "file5", "file6", "file7", "file8", "file9">>
        /\ counted_files = 0
        /\ counted_chars = 0
        /\ i = 1
        /\ pc = [self \in ProcSet |-> "MainLoop"]

MainLoop(self) == /\ pc[self] = "MainLoop"
                  /\ IF counted_files < Len(files)
                        THEN /\ pc' = [pc EXCEPT ![self] = "NCS"]
                        ELSE /\ pc' = [pc EXCEPT ![self] = "Done"]
                  /\ UNCHANGED << mutex, files, counted_files, counted_chars, 
                                  i >>

NCS(self) == /\ pc[self] = "NCS"
             /\ TRUE
             /\ mutex > 0
             /\ mutex' = mutex - 1
             /\ pc' = [pc EXCEPT ![self] = "CS"]
             /\ UNCHANGED << files, counted_files, counted_chars, i >>

CS(self) == /\ pc[self] = "CS"
            /\ IF i <= Len(files)
                  THEN /\ counted_chars' = counted_chars + (Len(files[i]))
                       /\ counted_files' = counted_files + 1
                  ELSE /\ TRUE
                       /\ UNCHANGED << counted_files, counted_chars >>
            /\ i' = i + 1
            /\ mutex' = mutex + 1
            /\ pc' = [pc EXCEPT ![self] = "MainLoop"]
            /\ files' = files

thread(self) == MainLoop(self) \/ NCS(self) \/ CS(self)

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == (\E self \in {"p1", "p2", "p3"}: thread(self))
           \/ Terminating

Spec == /\ Init /\ [][Next]_vars
        /\ \A self \in {"p1", "p2", "p3"} : WF_vars(thread(self))

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION

=============================================================================
