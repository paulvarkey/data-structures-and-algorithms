data-structures-and-algorithms
==============================

An attempt to understand good ways to organize, transform and move data around in a modern computer.

LexiconGraph
------------

A package containing implementations of various ways of storing lexicons for fast access, update and removal.

The following tests were performed using the following hardware/software :  
1. Intel Xeon E31230 (4 core) @ 3.2 GHz machine with 8 GB RAM  
2. Windows 7 Enterprise 64-bit OS  
3. Eclipse Java EE IDE (version: Juno Service Release 2)  
4. Eclipse Memory Analyzer Version 1.3.0 

Time/space characteristics for complete ingestion of a sample lexicon containing 178691 words :

| Method | Time (in seconds) | Memory Footprint |
|:--------|:-------------------|:------------------|
| [Ternary Search Tree](https://en.wikipedia.org/wiki/Ternary_search_tree) (direct implementation) | 0.07 | 12.1 MB |
| Ternary Search Tree (using array-based maps) | 0.45 | 27.6 MB |
| [Hash Array Mapped Trie](https://en.wikipedia.org/wiki/Hash_array_mapped_trie) | 0.18 | 13.4 MB |
| [CTrie](https://en.wikipedia.org/wiki/Ctrie) (Concurrent Hash Trie)<sup>a</sup> | 0.11<sup>b</sup> | 19 MB |
<sup>a</sup> CTries are concurrent, lock-free versions of Hash Array Mapped Tries.  
<sup>b</sup> Ingestion was performed sequentially (i.e. no concurrency was used)

Running time for Nth word containment test from the same lexicon (after ingestion) :

| Method | Time (in seconds) | 
|:--------|:-------------------|
| Ternary Search Tree (direct implementation) | 0.03 |
| Ternary Search Tree (using array-based maps) | <i>failed</i> |
| Hash Array Mapped Trie | 0.05 |
| CTrie | 0.03 |
