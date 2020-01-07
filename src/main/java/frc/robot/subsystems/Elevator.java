package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.Elevate;

import java.util.Optional;

public class Elevator extends Subsystem {
    private WPI_TalonSRX MainMotor;

    private DigitalInput topHFX;
    private Boolean isAtTop;
    private DigitalInput bottomHFX;
    private Boolean isAtBottom;

    private enum ElevatorControlState {
        HOLD (4),
        MANUAL_POSITIONING (3),
        MOTION_MAGIC (2),
        CALIBRATION (1);

        private final int stateCode;

        ElevatorControlState(int stateCode){
            this.stateCode = stateCode;
        }
    }

    private ElevatorControlState currentState;

    private Optional<Integer> ELEVATOR_BOTTOM_POSITION = Optional.empty();
    private Optional<Integer> ELEVATOR_TOP_POSITION = Optional.empty();

    private Optional<Integer> ELEVATOR_TARGET_POSITION = Optional.empty();

    public void setElevatorState(String state){
        switch(state){
            case("CALIBRATION"):
                this.currentState = ElevatorControlState.CALIBRATION;
                break;
            case("MOTION_MAGIC"):
                this.currentState = ElevatorControlState.MOTION_MAGIC;
                break;
            case("MANUAL_POSITIONING"):
                this.currentState = ElevatorControlState.MANUAL_POSITIONING;
                break;
            case("HOLD"):
                this.currentState = ElevatorControlState.HOLD;
                break;
            default:
                System.out.println("COULD NOT ASSIGN STATE - DEFAULTING TO HOLD");
                this.currentState = ElevatorControlState.HOLD;
        }
    }

    public void setElevatorState(int state){
        switch(state){
            case(1):
                this.currentState = ElevatorControlState.CALIBRATION;
                break;
            case(2):
                this.currentState = ElevatorControlState.MOTION_MAGIC;
                break;
            case(3):
                this.currentState = ElevatorControlState.MANUAL_POSITIONING;
                break;
            case(4):
                this.currentState = ElevatorControlState.HOLD;
                break;
            default:
                System.out.println("COULD NOT ASSIGN STATE - DEFAULTING TO HOLD");
                this.currentState = ElevatorControlState.HOLD;
        }
    }

    public Elevator() {
        this.MainMotor = new WPI_TalonSRX(RobotMap.elevatorMotor);
        currentState = ElevatorControlState.MANUAL_POSITIONING;

        MainMotor.configFactoryDefault();
        MainMotor.setInverted(true);
        MainMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.elevatorLoopIdx, RobotMap.CtreTimeoutMs);
        MainMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, RobotMap.CtreTimeoutMs);
        MainMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, RobotMap.CtreTimeoutMs);

        /* Set the peak and nominal outputs */
        MainMotor.configNominalOutputForward(0, RobotMap.CtreTimeoutMs);
        MainMotor.configNominalOutputReverse(0, RobotMap.CtreTimeoutMs);
        MainMotor.configPeakOutputForward(1, RobotMap.CtreTimeoutMs);
        MainMotor.configPeakOutputReverse(-1, RobotMap.CtreTimeoutMs);

        /* Set Motion Magic gains in slot0 - see documentation */
        MainMotor.selectProfileSlot(RobotMap.elevatorMotorSlotIdx, RobotMap.elevatorLoopIdx);
        MainMotor.config_kF(RobotMap.elevatorMotorSlotIdx, RobotMap.kF, RobotMap.CtreTimeoutMs);
        MainMotor.config_kP(RobotMap.elevatorMotorSlotIdx, RobotMap.kP, RobotMap.CtreTimeoutMs);
        MainMotor.config_kI(RobotMap.elevatorMotorSlotIdx, RobotMap.kI, RobotMap.CtreTimeoutMs);
        MainMotor.config_kD(RobotMap.elevatorMotorSlotIdx, RobotMap.kD, RobotMap.CtreTimeoutMs);

        MainMotor.setSelectedSensorPosition(0, RobotMap.kPidLoopIdx, RobotMap.CtreTimeoutMs);

        /* Set acceleration and vcruise velocity - see documentation */
        MainMotor.configMotionCruiseVelocity(35000, RobotMap.CtreTimeoutMs);
        MainMotor.configMotionAcceleration(100000, RobotMap.CtreTimeoutMs);

        topHFX = new DigitalInput(RobotMap.topHallEffect);
        bottomHFX = new DigitalInput(RobotMap.bottomHallEffect);
    }

    public void update(){
        // System.out.println(currentState);
        switch(currentState){
            case CALIBRATION:
                ELEVATOR_BOTTOM_POSITION = Optional.of(getElevatorPosition());
                System.out.println("Beginning Auto Calibration, please clear area surrounding elevator");

                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    System.out.print("Could not interrupt thread " + e);
                }

                while (getTopHFX()){
                    MainMotor.set(.25);
                }

                ELEVATOR_TOP_POSITION = Optional.of(getElevatorPosition());
                System.out.println("Auto Calibration Complete");
                System.out.printf("Top Value: %d \n Bottom Value %d", ELEVATOR_TOP_POSITION.get(), ELEVATOR_BOTTOM_POSITION.get());
                break;
            case MOTION_MAGIC:
                // TODO: motion magic timing
                break;
            case MANUAL_POSITIONING: break; // Do nothing
            case HOLD:
                if (!getBottomHFX()) {
                    MainMotor.set(0);
                } else {
                    if (ELEVATOR_TARGET_POSITION.isPresent()) {
                        MainMotor.set(ControlMode.Position, ELEVATOR_TARGET_POSITION.get());
                    } else {
                        int currentLoc = getElevatorPosition();
                        MainMotor.set(ControlMode.Position, currentLoc);
                    }
                }
                break;
            default: break; // Do nothing
        }
    }

    public WPI_TalonSRX getMainMotor() {
        return MainMotor;
    }

    public Boolean getTopHFX() {
        return topHFX.get();
    }

    public Boolean getBottomHFX() {
        return bottomHFX.get();
    }

    public int getElevatorPosition() {
        try {
            return MainMotor.getSelectedSensorPosition(0);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public ElevatorControlState getState() {
        return currentState;
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

    public void setBottomPosition(Optional<Integer> value) {
        ELEVATOR_BOTTOM_POSITION = value;
    }

    public Optional<Integer> getBottomPosition() {
        return ELEVATOR_BOTTOM_POSITION;
    }

    public void setTopPosition(Optional<Integer> value) {
        ELEVATOR_TOP_POSITION = value;
    }

    public Optional<Integer> getTopPosition() {
        return ELEVATOR_TOP_POSITION;
    }

    public void setELEVATOR_TARGET_POSITION(Optional<Integer> value) {
        ELEVATOR_TARGET_POSITION = value;
    }

    public Optional<Integer> getELEVATOR_TARGET_POSITION() {
        return ELEVATOR_TARGET_POSITION;
    }

    public void setElevatorMotor(double power) {
        if (!getBottomHFX() && power > 0) {
            MainMotor.set(power);
        }
        else if (!getBottomHFX() && power <= 0) {
            MainMotor.set(0);
        }
        else if (!getTopHFX() && power < 0) {
            MainMotor.set(power);
        }
        else if (!getTopHFX() && power >= 0) {
            MainMotor.set(0);
        }
        else{
            MainMotor.set(power);
        }
    }

    public Boolean isCalibrated() {
        return ELEVATOR_BOTTOM_POSITION.isPresent() && ELEVATOR_TOP_POSITION.isPresent();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new Elevate());
    }

    public void MagicSetMotor(double setpoint){
        MainMotor.set(ControlMode.MotionMagic, setpoint);
    }
}
