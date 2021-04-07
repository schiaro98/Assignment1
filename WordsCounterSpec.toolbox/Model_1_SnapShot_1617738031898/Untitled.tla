------------------------------ MODULE Untitled ------------------------------
EXTENDS Naturals, TLC

NT == 2
FLAG == 1..NT
WORDS == 25
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

fair process mainCounting \in 1..NT
variables words = WORDS;
begin MainLoop:
        while words/=0 do
        l2: acquire(rankLock);  print words;\*---Init CS---
        CS: count(words, freq);
        l6: release(rankLock); assert words >=0 /\ freq <= TOT_WORDS \*---End CS--- 
    end while;
end process; 
end algorithm;*)
=============================================================================
\* Modification History
\* Last modified Tue Apr 06 21:39:58 CEST 2021 by davide
\* Created Tue Apr 06 10:03:01 CEST 2021 by davide
