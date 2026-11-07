package org.firstinspires.ftc.teamcode.teleops;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.panels.PanelsUtil;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.GrabberSubsystem;

@TeleOp(name = "Main TeleOp")
public class MainTeleOp extends OpMode {
    private Follower follower;
    private ElevatorSubsystem elevator;
    private GrabberSubsystem grabber;

    private Gamepad currentGamepad1, currentGamepad2, previousGamepad1, previousGamepad2;

    @Override
    public void init() {
        PanelsUtil.init();

        follower = Constants.create(hardwareMap);
        elevator = new ElevatorSubsystem(hardwareMap);
        grabber = new GrabberSubsystem(hardwareMap);

        Scheduler.reset();

        // Initialize Gamepad states
        this.currentGamepad1 = new Gamepad();
        this.currentGamepad2 = new Gamepad();
        this.previousGamepad1 = new Gamepad();
        this.previousGamepad2 = new Gamepad();

    }

    @Override
    public void start() {
        follower.setPose(OpModeStorage.autonomousEndPose);
        follower.update();
    }

    @Override
    public void loop() {
        // Capture previous gamepad state
        previousGamepad1.copy(currentGamepad1);
        previousGamepad2.copy(currentGamepad2);
        // Capture current gamepad state
        currentGamepad1.copy(gamepad1);
        currentGamepad2.copy(gamepad2);

        // Field-centric driving that holds position
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                follower.pose().heading()
        );
        ManualDrive.driveOrHold(follower, powers);

        // Button triggers, trigger only on single presses, not constant presses
        if(currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
            Scheduler.schedule(elevator.liftBox());
        }
        else if(currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
            Scheduler.schedule(elevator.lowerBox());
        }
        else {
            Scheduler.schedule(elevator.stopBox());
        }

        if(currentGamepad2.x && !previousGamepad2.x) {
            Scheduler.schedule(elevator.boxHoldPosition());
        }
        if(currentGamepad2.b && !previousGamepad2.b) {
            Scheduler.schedule(elevator.boxDumpPosition());
        }
        if(currentGamepad1.left_trigger > 0.2 && previousGamepad1.left_trigger <= 0.2) {
            Scheduler.schedule(grabber.closeGrabber());
        }
        if(currentGamepad1.right_trigger > 0.2 && previousGamepad1.right_trigger <= 0.2) {
            Scheduler.schedule(grabber.openGrabber());
        }
        if(currentGamepad1.back && !previousGamepad1.back) {
            Scheduler.schedule(grabber.lowerArmCommand());
        }
        else if (currentGamepad1.left_bumper && !previousGamepad1.left_bumper) {
            Scheduler.schedule(grabber.raiseArmCommand());
        }
        else {
            Scheduler.schedule(grabber.stopArmCommand());
        }

        // If the robot drifts, use this to reset the follower heading
        if (currentGamepad1.options && !previousGamepad1.options) {
            follower.setPose(new Pose(follower.pose().x(), follower.pose().y(), 0));
        }
        if (currentGamepad1.back && currentGamepad1.start && !previousGamepad1.back && !previousGamepad1.start) {
            // Cancel any moving commands
            Scheduler.cancel(elevator.liftBox());
            Scheduler.cancel(elevator.lowerBox());
            Scheduler.cancel(grabber.lowerArmCommand());
            Scheduler.cancel(grabber.raiseArmCommand());

            // Set motors to stop
            Scheduler.schedule(grabber.stopArmCommand());
            Scheduler.schedule(elevator.stopBox());
        }

        // Update robot poses and path following
        follower.update();
        // Run or update commands
        Scheduler.execute();

        // Draw robot on FTC Panels field map
        PanelsUtil.drawRobot(follower.pose());

        // Update telemetry with helpful info
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        PanelsUtil.update(telemetry);
    }
}
