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

public static int elevatorMotor = 2;
public static int topHallEffect = 8;
public static int bottomHallEffect = 9;
public static int elevatorTicksPerInch = 4096;
public static int elevatorHeight = 51; //inches

public static int elevatorLoopIdx = 0;
public static int elevatorMotorSlotIdx = 0;

public static double kF = 0.2;
public static double kP = 1;
public static double kI = 0;
public static double kD = 0;

public static int kPidLoopIdx = 0;


  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;
}
