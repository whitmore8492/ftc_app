/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all iterative OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "The teleop", group = "")  // @Autonomous(...) is the other common choice

public class Tele_Op extends OpMode {
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftShootMotor = null;
    private DcMotor rightShootMotor = null;
    private DcMotor rightDriveMotor = null;
    private DcMotor leftDriveMotor = null;
    private DcMotor sweeperMotor = null;
    private Servo shootTrigger;
    private Servo beaconServo;
    private boolean rightTriggerPressed;
    private boolean BeaconServoLeft;
    private boolean leftBummperPrevPressed;
    private ColorSensor colorSensor;
    private GyroSensor gyroSensor;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        leftDriveMotor = hardwareMap.dcMotor.get("leftDriveMotor");
        rightDriveMotor = hardwareMap.dcMotor.get("rightDriveMotor");
        leftShootMotor = hardwareMap.dcMotor.get("leftShootMotor");
        rightShootMotor = hardwareMap.dcMotor.get("rightShootMotor");
        leftShootMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightShootMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        sweeperMotor = hardwareMap.dcMotor.get("sweeperMotor");
        leftShootMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        beaconServo = hardwareMap.servo.get("bacon");
        shootTrigger = hardwareMap.servo.get("trigger");
        gyroSensor = hardwareMap.gyroSensor.get("gyroSensor");
        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        rightTriggerPressed = false;

        // eg: Set the drive motor directions:
        // Reverse the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        //  rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        // telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
        shootTrigger.setPosition(Settings.reset);
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        telemetry.addData("Status", shootTrigger.getPosition());
        telemetry.addData("Status", "Running: " + runtime.toString());
        leftDriveMotor.setPower(-gamepad1.left_stick_y);
        rightDriveMotor.setPower(-gamepad1.right_stick_y);
        if (gamepad2.right_trigger == 1 && rightTriggerPressed == false) {
            rightTriggerPressed = true;

        } else if (gamepad2.right_trigger == 1 && rightTriggerPressed == true) {
            rightTriggerPressed = false;
        }
        if (rightTriggerPressed) {
            leftShootMotor.setPower(Settings.shooterRPM);
            rightShootMotor.setPower(Settings.shooterRPM);
        } else {
            leftShootMotor.setPower(0);
            rightShootMotor.setPower(0);
        }
        if (gamepad2.left_bumper && leftBummperPrevPressed == false) {
            leftBummperPrevPressed = true;
            BeaconServoLeft = !BeaconServoLeft;
        } else if (!gamepad2.left_bumper && leftBummperPrevPressed == true) {
            leftBummperPrevPressed = false;
        }

        if (BeaconServoLeft) {
            beaconServo.setPosition(Settings.beaconRight);
        } else {

            beaconServo.setPosition(Settings.beaconLeft);
        }

        sweeperMotor.setPower(-gamepad2.left_stick_y);
        if (gamepad2.right_bumper) {
            shootTrigger.setPosition(Settings.launch);
        } else {
            shootTrigger.setPosition(Settings.reset);

        }

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
