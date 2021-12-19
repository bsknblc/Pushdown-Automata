# Pushdown-Automata
Pushdown Automata for CS410 Automata Theory and Formal Languages at Ozyegin University

## Input file example
2 *(number of variables in input alphabet)*  
2 *(number of variables in stack alphabet)*  
2 *(number of goal states)*  
4 *(number of states)*  
q1 q2 q3 q4 *(states)*  
q1 *(start state)*  
q1 q4 *(goal state(s))*  
X Y *(the stack alphabet)*  
X *(initial stack symbol)*  
0 1 *(the input alphabet )*  
q1 ε ε X q2 *(q1 state’inden ε ile q2 state’ine gidiyor, ε popluyor, X pushluyor.)*  
q2 0 ε Y q2 *(q2 state’inden 0 ile q2 state’ine gidiyor, ε popluyor, Y pushluyor.)*  
q2 1 Y ε q3 *(q2 state’inden 1 ile q3 state’ine gidiyor, Y popluyor, ε pushluyor.)*  
q3 1 Y ε q3 *(q3 state’inden 1 ile q3 state’ine gidiyor, Y popluyor, ε pushluyor.)*  
q3 ε X ε q4 *(q3 state’inden ε ile q4 state’ine gidiyor, X popluyor, ε pushluyor.)*  
0011 *(string to be detected)*  
0111 *(string to be detected)*  

## Output file example
q1 q2 q2 q2 q3 q3 q4 *(route taken)*  
Accepted  
q1 q2 q2 q3 q4 *(route taken)*  
Rejected  
