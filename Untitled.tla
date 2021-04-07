------------------------------ MODULE Untitled ------------------------------
EXTENDS Naturals, TLC

NT == 2
FLAG == 1..NT
WORDS == 5
TOT_WORDS == NT*WORDS

(* --algorithm WordsCounter

variables rankLock=1, freq = 0;

define
    FinalValue == <>(freq = TOT_WORDS)
    MutualExclusion == []~(\A i \in FLAG: pc[i] = "CS") \* Se NT <= 2
end define;

macro acquire(s) begin
  await s = 1;
  s := 0;
end macro;

macro release(s) begin
  s := 1;
end macro;

macro count(wrd, frq) begin
    wrd := wrd - 1;
    frq := frq + 1;
end macro;

fair process mainCounting \in FLAG
variables words = WORDS;
begin MainLoop:
        while words/=0 do
        l2: acquire(rankLock);\*---Init CS---
        CS: count(words, freq);
        l6: release(rankLock); \* ---End CS--- 
    end while;
end process; 
end algorithm;*)
\* BEGIN TRANSLATION (chksum(pcal) = "7008f7ca" /\ chksum(tla) = "1453aea")
VARIABLES rankLock, freq, pc

(* define statement *)
FinalValue == <>(freq = TOT_WORDS)
MutualExclusion == []~(\A i \in FLAG: pc[i] = "CS")

VARIABLE words

vars == << rankLock, freq, pc, words >>

ProcSet == (FLAG)

Init == (* Global variables *)
        /\ rankLock = 1
        /\ freq = 0
        (* Process mainCounting *)
        /\ words = [self \in FLAG |-> WORDS]
        /\ pc = [self \in ProcSet |-> "MainLoop"]

MainLoop(self) == /\ pc[self] = "MainLoop"
                  /\ IF words[self]/=0
                        THEN /\ pc' = [pc EXCEPT ![self] = "l2"]
                        ELSE /\ pc' = [pc EXCEPT ![self] = "Done"]
                  /\ UNCHANGED << rankLock, freq, words >>

l2(self) == /\ pc[self] = "l2"
            /\ rankLock = 1
            /\ rankLock' = 0
            /\ pc' = [pc EXCEPT ![self] = "CS"]
            /\ UNCHANGED << freq, words >>

CS(self) == /\ pc[self] = "CS"
            /\ words' = [words EXCEPT ![self] = words[self] - 1]
            /\ freq' = freq + 1
            /\ pc' = [pc EXCEPT ![self] = "l6"]
            /\ UNCHANGED rankLock

l6(self) == /\ pc[self] = "l6"
            /\ rankLock' = 1
            /\ pc' = [pc EXCEPT ![self] = "MainLoop"]
            /\ UNCHANGED << freq, words >>

mainCounting(self) == MainLoop(self) \/ l2(self) \/ CS(self) \/ l6(self)

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == (\E self \in FLAG: mainCounting(self))
           \/ Terminating

Spec == /\ Init /\ [][Next]_vars
        /\ \A self \in FLAG : WF_vars(mainCounting(self))

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION 
=============================================================================
\* Modification History
\* Last modified Wed Apr 07 10:21:19 CEST 2021 by davide
\* Created Tue Apr 06 10:03:01 CEST 2021 by davide
