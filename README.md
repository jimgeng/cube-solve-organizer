# Rubiks Cube Timer & Tracker

## Background

The Rubik's cube is a puzzle that I became interested in junior high school. I was intrigued by the speed and precision that
my friends were able to showcase through their rubik's cube solving skills, so I also bought one for myself and begin to master
the solving techniques.

Rubiks cube solves are generally ranked based on the time the solve took. In competition scenarios,
competitors have 15 seconds to inspect the cube, before they are able to start the timer and solve the cube.

## Functionality

My application aims to recreate the competition scenarios to allow the user to track their solves.
The main feature is the implementation of a timer to track a user's solves, but customized to simulate competition conditions
in implementing the 15 second inspection within the timer. 

Apart from the timer functionality, the application should be able to keep, save, and load data of previous solves.
A history of the data of all previous solves should be available to show to the user.

## User Stories

- As a user, I want to be able to start a solve.
- As a user, I want to be able to end a solve, and thus view the resulting time of the solve. This will record the solve into a list of all other past solves.
- As a user, I want to be able to experience competition timing rules, thus going through a 15 second inspection time phase before starting the actual solve.
- As a user, I want to be able to end the inspection time early if I intend to.
- As a user, I want to be able to see previous solve data.
- As a user, When I select to quit the timer app, I want to be reminded to save my list of solve times to file and have the option to do so or not.
- As a user, When the app starts, I want to be able to be given the option to load previous solve times from file.

## Instructions
- You can generate the first required action related to adding Solves to a List of Solves by doing a solve.
- You can generate the second required action related to adding Solves to a List of Solves by doing another solve.
- You can locate my visual component by clicking on the history button, and seeing that all solves are showcased within a table of values.
- You can save the state of my application by clicking the close button on the top right corner, and selecting yes when a prompt window asks you to save your solves.
- You can reload the state of my application by selecting yes when being prompted to load previous data while launching the program.

To do a solve (with the graphical user interface): 
1. Hold the spacebar and the timer will become green, indicating that new solve will commence.
2. When the spacebar is released, the user will be given 15 seconds of inspection time.
3. Hold the spacebar. The timer will initially turn yellow. Once it turns green release the spacebar to start the timer.
4. After waiting an arbitrary amount of time, press the spacebar again to end the solve. This will automatically add the solve to the existing list of solves.
5. Click on the history button to see that the solve has been added to a table listing all previous solves.
6. Steps 1-5 can be repeated again to add another solve.