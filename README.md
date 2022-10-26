# Simple_2048
A copy of the famous 2048 Android app adapted as a graphical desktop app, keeping the same appearance and game logic. The field cells are stored as a 2D array. As an addition, random move and auto move were added.

Auto move constructs all possible Game states which could be gotten from the current Game state (Taking into account all possible moves), determines the best one, and takes the first move which is required to get there.

Uses:
MVC pattern
JPanel
SOLID principles
Intelligent movement prediction

