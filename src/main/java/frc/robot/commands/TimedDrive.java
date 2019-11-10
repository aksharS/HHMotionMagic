/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class TimedDrive extends TimedCommand {

  double moveSpeed;

  /**
   * Add your docs here.
   */
  public TimedDrive(double timeout, double moveSpeed) {
    super(timeout);
    requires(Robot.m_DriveBase);
    this.moveSpeed = moveSpeed;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.m_DriveBase.arcadeDrive(moveSpeed, 0);
  }

  // Called once after timeout
  @Override
  protected void end() {
    Robot.m_DriveBase.arcadeDrive(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
