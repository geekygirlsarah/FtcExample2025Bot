package org.firstinspires.ftc.teamcode.examples;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.panels.PanelsUtil;

@Autonomous(group = "Examples", name = "Example Autonomous")
public class ExampleAuto extends OpMode {
    // Set up bot
    private Follower follower;

    // Set up paths and poses
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose startPose = poseFactory.of(24, 24, 0);
    private final Pose scorePose = poseFactory.of(48, 48, 90);
    private final Pose parkPose = poseFactory.of(72, 48, 90);

    // Path methods
    private Path startToScore() {
        return line(startPose, scorePose).linear(startPose, scorePose);
    }

    private Path park(){
        return line(scorePose, parkPose).linear(scorePose, parkPose);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, startToScore()),
                // Add mechanism commands here.
                follow(follower, park())
        );
    }

    @Override
    public void init() {
        PanelsUtil.init();
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        PanelsUtil.drawRobot(follower.pose());

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        PanelsUtil.update(telemetry);
    }

    public void stop()
    {
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}
