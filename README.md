#Connect Four Game in Java

This was my final Project for Udacity's [Intro to Java Programming Course](https://www.udacity.com/course/intro-to-java-programming--cs046). **Completed May 8, 2015**

##How to run the program
1. Clone the GitHub repository in desktop or download as a zip
2. Use a Java IDE to run 'Main.java' (in the class we used [BlueJ](http://www.bluej.org/))

##Project Overview
In this project, I was provided all of the assets and graphical components (e.g. game board, pieces, etc.) were provided. I had to implement the game-playing logic for my player, *My Agent*.

##My Approach
The logic for my player agent followed this order:

1. If I can win by playing a move, play it.
2. If the opponent can win in her next move, play that move (a block!).
3. If I am two moves from a win and the move does not set the opponent up for a win, play that move.
4. If the opponent is two moves from a win and the move does not set the opponent up for a win, play that move (a block!).
5. Play a random move but make sure I am not setting the opponent up for a win.

##What I learned
Before taking this Udacity course, it had been about 15 years since I had done any formal learning for programming and I loved it. The class was very strong in how it clearly explained Java programming and syntax as well as Object Oriented programming.

Above all, I learned not to give up. I also learned to break down a program into small pieces and test them as I went.