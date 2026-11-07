package org.firstinspires.ftc.teamcode.examples;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.panels.PanelsUtil;

public class ExampleFieldCentricDriveTeleOp extends OpMode {
    private Follower follower;

    @Override
    public void init() {
        PanelsUtil.init();
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void start() {
        follower.setPose(OpModeStorage.autonomousEndPose);
        follower.update();
    }

    @Override
    public void loop() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                follower.pose().heading()
        );

        follower.manual(powers);
        follower.update();

        PanelsUtil.drawRobot(follower.pose());

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        PanelsUtil.update(telemetry);
    }

}
