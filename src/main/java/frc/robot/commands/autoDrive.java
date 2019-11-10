package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.RobotMap;

import edu.wpi.first.wpilibj.command.PIDCommand;

public class autoDrive extends PIDCommand {

    private double slowDown = .7;

    public autoDrive(double inches) {
        super(RobotMap.driveP, RobotMap.driveI, RobotMap.driveD);
        setSetpoint(inches);
    }

    public autoDrive(double inches, double speed) {
        super(RobotMap.driveP, RobotMap.driveI, RobotMap.driveD);

        setSetpoint(inches);
        this.slowDown = speed;
    }

    protected void initialize() {
        setTimeout(3);
    }

    @Override
    protected double returnPIDInput() {
        return (Robot.m_DriveBase.getRightTicks() + Robot.m_DriveBase.getLeftTicks())/2.0;
    }

    @Override
    protected void usePIDOutput(double speed) {
        Robot.m_DriveBase.driveBaseTank(speed * slowDown, speed * slowDown);
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut() || Math.abs(getSetpoint() - getPosition()) < 2;
    }

    protected void end() {
        setTimeout(0);
    }

    protected void interrupted() {
        end();
    }
}
