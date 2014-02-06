data-structures-and-algorithms
==============================

An attempt to understand good ways to organize, transform and move data around in a modern computer.

LexiconGraph
------------

A package containing implementations of various ways of storing lexicons for fast access, update and removal.

For a sample lexicon containing 178691 words, the following results were obtained :

| Method | Time (in seconds) | Memory Footprint |
|:--------|:-------------------|:------------------|
| Ternary Search Tree (direct implementation) | 0.093968271 | 12.1 MB |
| Ternary Search Tree (using array-based maps) | 0.514310877 | 27.6 MB |
| Hash Array Mapped Trie | 0.212815164 | 13.4 MB |
| CTrie (Concurrent Hash Trie)* | 0.150503307 | 19 MB |
* CTries are concurrent, lock-free versions of Hash Array Mapped Tries.
