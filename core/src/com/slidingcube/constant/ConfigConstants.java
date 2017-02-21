package com.slidingcube.constant;

/**
 * Configuration constants.
 *
 * @author bgamard
 */
public class ConfigConstants {
    private ConfigConstants() {
        // private class
    }

    public static final boolean DEBUG = true; // debug mode
    public static final int COUNTDOWN = 6000; // countdown timer in milliseconds

    public static float GRAVITY = -20f; // world gravity in m/s²
    public static float HORIZONTAL_CAMERA_MARGIN = 0.02f; // horizontal margin factor between screen and players
    public static float VERTICAL_CAMERA_MARGIN = 0f; // vertical margin factor between screen and players

    public static float GROUND_FRICTION = 0.1f; // ground friction
    public static int GROUND_WIDTH = 1000; // ground width
    public static float GROUND_SLOPE_FACTOR = 1000f; // slope factor
    public static float GROUND_SLOPE_MULTIPLIER = 0.5f; // slope multiplier
    public static int GROUND_FLAT_WIDTH = 50; // ground is flat at beginning and end

    public static float HELP_FORCE_MULTIPLIER = 100f; // help force factor
    public static float JUMP_VERTICAL = 4000f; // vertical jump force in N
    public static float JUMP_HORIZONTAL = 4000f; // horizontal jump force in N
    public static int JUMP_INTERVAL = 400; // minimum time between 2 jumps
    public static final float PLAYER_DENSITY = 20f; // player density
    public static final float HELP_FORCE = 0.5f; // help force multiplier
    public static float JUMP_PUSH = -500f; // downward push when a player jump on another in N

    public static Integer FIXTURE_FOOT_ID = 0; // foot fixture ID
}
