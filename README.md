# lexical-analyzer
This is a group project of 3 people. The mission is coding a simple lexical analyzer.

Authors: @erenduyuk, @selnaydinn and @ItsYusufDemir

## About the Program
The program takes an input file that contains many lines as in the example below;  

(define (fibonacci n)  
(let fib ([prev 0]  
[cur 1]  
[i 0])  
(if (= i n)  
cur  
(fib cur (+ prev cur) (+ i 1)))))  

The aim is to scan for all the lexemes and print their appropriate token with its location.  
For example;  

LEFTPAR 1:1  
DEFINE 1:  
..  
...  
