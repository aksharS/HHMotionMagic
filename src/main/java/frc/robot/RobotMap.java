/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

public static int CtreTimeoutMs = 30;

public static int FrontLeftTalonPort = 1;
public static int FrontRightTalonPort = 4;
public static int BackLeftTalonPort = 3;
public static int BackRightTalonPort = 2;

public static int elevatorMotor = 5;
public static int topHallEffect = 4;
public static int bottomHallEffect = 5;
public static int elevatorTicksPerInch = 4096;
public static int elevatorHeight = 51; //inches
public static int elevatorLoopIdx = 0;

public static double ticksPerRevolution = 128.1;
public static double wheelCircumference = 20.25; // Inches

public static double driveP = 0.05;
public static double driveI = 0.00;
public static double driveD = 0.00;

public static int leftEncoderPort1 = 0;
public static int leftEncoderPort2 = 1;
public static int rightEncoderPort1 = 2;
public static int rightEncoderPort2 = 3;

public static double straightLineP = 0.0001;
public static double straightLineI = 0;
public static double straightLineD = 0;

  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;
}
