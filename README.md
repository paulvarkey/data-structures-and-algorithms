data-structures-and-algorithms
==============================

An attempt to understand how to organize, transform and move data around in a modern computer.

LexiconGraph
------------

A package containing implementations of various ways of storing lexicons for fast access, update and removal.

For a sample lexicon containing 178691 words, the following results were obtained :

| Method | Time (in seconds) | Memory Footprint |
|:--------|:-------------------|:------------------|
| TST | 0.093968271 | 12.1 MB |
| TST2 | 0.514310877 | 27.6 MB |
| HAMT | 0.212815164 | 13.4 MB |
| CTrie | 0.150503307 | 19 MB |
