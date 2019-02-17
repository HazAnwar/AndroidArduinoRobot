#include <Servo.h>

// defines where each item is on the board
int motorLeft = 5;
int motorRight = 6;
int motorLeftForward = 7;
int motorLeftBack = 8;
int motorRightBack = 12;
int motorRightForward = 13;

// the input from the mobile device so it knows what to do i.e. move forward, back, right, left or stop
int inputData;

// define the iteration for input to know whether command or angle
int angleIteration;
int angleCommand;

// defines the servos that will control the robot arm and camera
Servo robotArmLower;
Servo robotArmUpper;
Servo robotClawRotate;
Servo robotClawOpening;
Servo robotArmRotate;

void setup() {
  // defines the motors are output so that we can write variables to them to adjust speed
  pinMode(motorLeft, OUTPUT);
  pinMode(motorRight, OUTPUT);
  pinMode(motorLeftForward, OUTPUT);
  pinMode(motorLeftBack, OUTPUT);
  pinMode(motorRightForward, OUTPUT);
  pinMode(motorRightBack, OUTPUT);

  // delay the rest of the code from running for 40 seconds whilst the WiFi card initialises
  delay(20000);
  delay(20000);

  // defines which servos are attached to which pin on the board and sets the default angles for them
  robotArmLower.attach(4);
  robotArmLower.write(100);
  
  robotArmRotate.attach(10);
  robotArmRotate.write(90);
  
  robotArmUpper.attach(2);
  robotArmUpper.write(110);

  robotClawOpening.attach(9);
  robotClawOpening.write(91);

  robotClawRotate.attach(11);
  robotClawRotate.write(180);

  // defines the iteration of 0 for the angle within the program for setup
  angleIteration = 0;

  // activates serial input that will receive from the mobile device over the WiFi connection at a 9600 baud rate
  Serial.begin(9600);
}

void loop() {
  // checking on loop if there is any input from the mobile device to control it
  if (Serial.available() > 0) {
    inputData = Serial.read();
    if (angleIteration == 0) {
      switch (inputData) {
        case 0:
          robotMoveStop();
          break;
        case 1:
          robotMoveForwardSlow();
          break;
        case 2:
          robotMoveForwardMedium();
          break;
        case 3:
          robotMoveForwardFast();
          break;
        case 4:
          robotMoveBackSlow();
          break;
        case 5:
          robotMoveBackMedium();
          break;
        case 6:
          robotMoveBackFast();
          break;
        case 7:
          robotMoveRightForwardSlow();
          break;
        case 8:
          robotMoveRightForwardMedium();
          break;
        case 9:
          robotMoveRightForwardFast();
          break;
        case 10:
          robotMoveRightSlow();
          break;
        case 11:
          robotMoveRightMedium();
          break;
        case 12:
          robotMoveRightFast();
          break;
        case 13:
          robotMoveRightBackSlow();
          break;
        case 14:
          robotMoveRightBackMedium();
          break;
        case 15:
          robotMoveRightBackFast();
          break;
        case 16:
          robotMoveLeftForwardSlow();
          break;
        case 17:
          robotMoveLeftForwardMedium();
          break;
        case 18:
          robotMoveLeftForwardFast();
          break;
        case 19:
          robotMoveLeftSlow();
          break;
        case 20:
          robotMoveLeftMedium();
          break;
        case 21:
          robotMoveLeftFast();
          break;
        case 22:
          robotMoveLeftBackSlow();
          break;
        case 23:
          robotMoveLeftBackMedium();
          break;
        case 24:
          robotMoveLeftBackFast();
          break;
        case 30:
          angleCommand = inputData;
          angleIteration = 1;
          break;
        case 31:
          angleCommand = inputData;
          angleIteration = 1;
          break;
        case 32:
          angleCommand = inputData;
          angleIteration = 1;
          break;
        case 33:
          angleCommand = inputData;
          angleIteration = 1;
          break;
        case 34:
          angleCommand = inputData;
          angleIteration = 1;
          break;
        default:
          robotMoveStop();
          break;
      }
    } else if (angleIteration == 1) {
      switch (angleCommand) {
        case 30:
          robotArmLower.write(inputData);
          break;
        case 31:
          robotArmUpper.write(inputData);
          break;
        case 32:
          robotClawRotate.write(inputData);
          break;
        case 33:
          robotClawOpening.write(inputData);
          break;
        case 34:
          robotArmRotate.write(inputData);
          break;
      }
      angleIteration = 0;
    }
  }
}

// define how the robot will move right by saying which motors to turn on and which to leave off
void robotMoveRightForwardSlow() {
  analogWrite(motorLeft, 150);
  analogWrite(motorRight, 100);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move right by saying which motors to turn on and which to leave off
void robotMoveRightForwardMedium() {
  analogWrite(motorLeft, 200);
  analogWrite(motorRight, 150);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move right by saying which motors to turn on and which to leave off
void robotMoveRightForwardFast() {
  analogWrite(motorLeft, 250);
  analogWrite(motorRight, 200);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move right by saying which motors to turn on and which to leave off
void robotMoveRightSlow() {
  analogWrite(motorLeft, 100);
  analogWrite(motorRight, 100);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move right by saying which motors to turn on and which to leave off
void robotMoveRightMedium() {
  analogWrite(motorLeft, 175);
  analogWrite(motorRight, 175);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move right by saying which motors to turn on and which to leave off
void robotMoveRightFast() {
  analogWrite(motorLeft, 250);
  analogWrite(motorRight, 250);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move right by saying which motors to turn on and which to leave off
void robotMoveRightBackSlow() {
  analogWrite(motorLeft, 150);
  analogWrite(motorRight, 100);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move right by saying which motors to turn on and which to leave off
void robotMoveRightBackMedium() {
  analogWrite(motorLeft, 200);
  analogWrite(motorRight, 150);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move right by saying which motors to turn on and which to leave off
void robotMoveRightBackFast() {
  analogWrite(motorLeft, 250);
  analogWrite(motorRight, 200);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move left by saying which motors to turn on and which to leave off
void robotMoveLeftForwardSlow() {
  analogWrite(motorLeft, 100);
  analogWrite(motorRight, 150);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move left by saying which motors to turn on and which to leave off
void robotMoveLeftForwardMedium() {
  analogWrite(motorLeft, 150);
  analogWrite(motorRight, 200);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move left by saying which motors to turn on and which to leave off
void robotMoveLeftForwardFast() {
  analogWrite(motorLeft, 200);
  analogWrite(motorRight, 250);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move left by saying which motors to turn on and which to leave off
void robotMoveLeftSlow() {
  analogWrite(motorLeft, 100);
  analogWrite(motorRight, 100);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move left by saying which motors to turn on and which to leave off
void robotMoveLeftMedium() {
  analogWrite(motorLeft, 175);
  analogWrite(motorRight, 175);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move left by saying which motors to turn on and which to leave off
void robotMoveLeftFast() {
  analogWrite(motorLeft, 250);
  analogWrite(motorRight, 250);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move left by saying which motors to turn on and which to leave off
void robotMoveLeftBackSlow() {
  analogWrite(motorLeft, 100);
  analogWrite(motorRight, 150);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move left by saying which motors to turn on and which to leave off
void robotMoveLeftBackMedium() {
  analogWrite(motorLeft, 150);
  analogWrite(motorRight, 200);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move left by saying which motors to turn on and which to leave off
void robotMoveLeftBackFast() {
  analogWrite(motorLeft, 200);
  analogWrite(motorRight, 250);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move back by saying which motors to turn on and which to leave off
void robotMoveBackSlow() {
  analogWrite(motorLeft, 100);
  analogWrite(motorRight, 100);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move back by saying which motors to turn on and which to leave off
void robotMoveBackMedium() {
  analogWrite(motorLeft, 175);
  analogWrite(motorRight, 175);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move back by saying which motors to turn on and which to leave off
void robotMoveBackFast() {
  analogWrite(motorLeft, 250);
  analogWrite(motorRight, 250);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, HIGH);
  digitalWrite(motorLeftBack, HIGH);
}

// define how the robot will move forward by saying which motors to turn on and which to leave off
void robotMoveForwardSlow() {
  analogWrite(motorLeft, 75);
  analogWrite(motorRight, 75);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move forward by saying which motors to turn on and which to leave off
void robotMoveForwardMedium() {
  analogWrite(motorLeft, 175);
  analogWrite(motorRight, 175);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move forward by saying which motors to turn on and which to leave off
void robotMoveForwardFast() {
  analogWrite(motorLeft, 250);
  analogWrite(motorRight, 250);

  digitalWrite(motorRightForward, HIGH);
  digitalWrite(motorLeftForward, HIGH);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}

// define how the robot will move stop by saying turn off all the motors
void robotMoveStop() {
  analogWrite(motorLeft, 0);
  analogWrite(motorRight, 0);

  digitalWrite(motorRightForward, LOW);
  digitalWrite(motorLeftForward, LOW);
  digitalWrite(motorRightBack, LOW);
  digitalWrite(motorLeftBack, LOW);
}
