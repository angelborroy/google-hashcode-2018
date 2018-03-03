
Google Hashcode 2018
========================

Sample solution for [Google Hashcode 2018](https://hashcode.withgoogle.com/) developed at **Hash Code Zaragoza Spain** during the session available using `ContestStepEngine.java`

Just an skeleton to start to work with.

Session scorings:

* A - example - 10
* B - should be easy - 175,671
* C - no hurry - 8,016,571
* D - metropolis - 6,994,842
* E - high bonus - 20,828,717


Extra material
--------------

Three additional Step Engines have been added for learning purposes.


**BasicStepEngine**

`E - high bonus     13,661,075 points`

* Ensure that movements are correct
* Any available truck is selected to start a ride


**ScoringStepEngine**

`E - high bonus     20,828,717 points`

Consider bonus conditions to pick a truck for a ride

* To complete a ride before its latest finish
* To start a ride exactly in its earliest allowed start step


**BestChoiceStepEngine**

`E - high bonus     13,699,510 points` 

Evaluate and score every available ride for a free truck


Notes
-----
Problem statement, input & output files are available at `src/main/resources` folder.

To run the project, just use scripts available at `scripts` folder:

* `basic-run-*` to use **BASIC** Step Engine
* `scoring-run-*` to use **SCORING** Step Engine
* `best-run-*` to use **BEST_CHOICE** Step Engine

Contributors
------------
* Alex Oarga
* Eduardo Gimeno
* Pedro Andrés Gavín