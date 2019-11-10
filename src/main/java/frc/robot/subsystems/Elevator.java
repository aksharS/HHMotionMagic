package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPIMainMotorSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.RobotMap

import java.util.Optional;

public class Elevator {
    private static Elevator instance = new Elevator();

    private WPIMainMotorSRX MainMotor;

    private DigitalInput topHFX;
    private Boolean isAtTop;
    private DigitalInput bottomHFX;
    private Boolean isAtBottom;

    private enum ControlMode{
        MANUAL_POSITIONING,
        MOTION_MAGIC,
        CALIBRATION
    };

    private ControlMode currentState;

    private Optional<Integer> ELEVATOR_BOTTOM_POSITION;
    private Optional<Integer> ELEVATOR_TOP_POSITION;

    public Elevator() {
        WPIMainMotorSRX MainMotor = new WPIMainMotorSRX(RobotMap.elevatorMotor);
        
        MainMotor.configFactoryDefault();
        MainMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.elevatorLoopIdx, RobotMap.CtreTimeoutMs);
        MainMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, RobotMap.CtreTimeoutMs);
        MainMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, RobotMap.CtreTimeoutMs);

        /* Set the peak and nominal outputs */
        MainMotor.configNominalOutputForward(0, RobotMap.CtreTimeoutMs);
        MainMotor.configNominalOutputReverse(0, RobotMap.CtreTimeoutMs);
        MainMotor.configPeakOutputForward(1, RobotMap.CtreTimeoutMs);
        MainMotor.configPeakOutputReverse(-1, RobotMap.CtreTimeoutMs);

        /* Set Motion Magic gains in slot0 - see documentation */
        MainMotor.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
        MainMotor.config_kF(Constants.kSlotIdx, Constants.kGains.kF, RobotMap.CtreTimeoutMs);
        MainMotor.config_kP(Constants.kSlotIdx, Constants.kGains.kP, RobotMap.CtreTimeoutMs);
        MainMotor.config_kI(Constants.kSlotIdx, Constants.kGains.kI, RobotMap.CtreTimeoutMs);
        MainMotor.config_kD(Constants.kSlotIdx, Constants.kGains.kD, RobotMap.CtreTimeoutMs);

        /* Set acceleration and vcruise velocity - see documentation */
        MainMotor.configMotionCruiseVelocity(15000, RobotMap.CtreTimeoutMs);
        MainMotor.configMotionAcceleration(6000, RobotMap.CtreTimeoutMs);

        DigitalInput topHFX = new DigitalInput(RobotMap.topHallEffect);
        DigitalInput bottomHFX = new DigitalInput(RobotMap.bottomHallEffect);

        currentState = ControlMode.CALIBRATION;

    }

    public Boolean getTopHFX() {
        return topHFX.get();
    }

    public Boolean getBottomHFX() {
        return bottomHFX.get();
    }

    public int getElevatorPosition() {
        return MainMotor.getSelectedSensorPosition();
    }

    public void Calibrate() {
        if(getBottomHFX()) {
            ELEVATOR_BOTTOM_POSITION = Optional.of(getElevatorPosition());
            if(!ELEVATOR_TOP_POSITION.isPresent()) {
                ELEVATOR_TOP_POSITION = Optional.of(getElevatorPosition() + RobotMap.elevatorHeight * RobotMap.elevatorTicksPerInch);
            }
        } else if(getTopHFX()) {
            ELEVATOR_TOP_POSITION = Optional.of(getElevatorPosition());
            if(!ELEVATOR_BOTTOM_POSITION.isPresent()) {
                ELEVATOR_BOTTOM_POSITION = Optional.of(getElevatorPosition() - RobotMap.elevatorHeight * RobotMap.elevatorTicksPerInch);
            }
        }
    }

    public Boolean isCalibrated() {
        return ELEVATOR_BOTTOM_POSITION.isPresent() && ELEVATOR_TOP_POSITION.isPresent();
    }
}
