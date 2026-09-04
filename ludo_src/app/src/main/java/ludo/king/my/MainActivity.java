package ludo.king.my;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    DisplayMetrics displayMetrics;
    double dpHeight;
    double dpWidth;
    double pxWidth;
    double pxHeight;
    ImageView ludoBoard,redHomeBlink,red1activecircle,red2activecircle,red3activecircle,red4activecircle;
    View step1,step2,step3,step4,step5,step6;
    ConstraintLayout completeBackground,redpiece1,redpiece2,redpiece3,redpiece4,greenpiece1,greenpiece2,greenpiece3,greenpiece4,bluepiece1,bluepiece2,bluepiece3,bluepiece4,yellowpiece1,yellowpiece2,yellowpiece3,yellowpiece4;

    float onePercentWidth,onePercentHeight;

    TextView playername1,playername2,playername3,playername4;

    EditText editText;

    int gametype = 1;

    int nop;

    double[] dx = {53.32,53.32,53.32,53.32,53.32,60.04,66.7,73.36,80.02,86.68,93.34,93.34,93.34,86.68,80.02,73.36,66.7 ,60.04,53.32,53.32,53.32,53.32,53.32,53.32,46.66,40   ,40   ,40   ,40   ,40   ,40
                  ,33.34,26.68,20.02,13.36,6.7  ,0.04 ,0.04 ,0.04,6.7  ,13.36,20.02,26.68,33.34, 40  , 40  , 40  , 40  , 40  , 40  ,46.66,53.32,46.66,46.66,46.66,46.66,46.66,46.66,86.68,80.02,73.36,66.7 ,60.04,53.38,46.66,46.66,46.66,46.66,46.66,46.66,6.66 ,13.32,19.98,26.64,33.3 ,39.96};
    double[] dy = { 6.66,13.32,19.98,26.64,33.3 , 40  , 40 , 40  ,  40 ,  40 ,  40 ,46.66,53.32,53.32,53.32,53.32,53.32,53.32,60.04,66.7 ,73.36,80.02,86.68,93.34,93.34,93.34,86.68,80.02,73.36,66.7 ,60.04
                  ,53.32,53.32,53.32,53.32,53.32,53.32,46.66, 40 , 40  , 40  , 40  , 40  , 40  ,33.34,26.68,20.02,13.36,6.7  ,0.04 ,0.04 ,0.04 ,6.66 ,13.32,19.98,26.64,33.3 ,39.96,46.66,46.66,46.66,46.66,46.66,46.66,86.68,80.02,73.36,66.7 ,60.04,53.38,46.66,46.66,46.66,46.66,46.66,46.66};

    float[] x = new float[dy.length];
    float[] y = new float[dy.length];

    float pushXForPieces;
    float pushYForPieces;

    ImageView mainDiceImageView,hintArrow;

    int reddicevalue,greendicevalue,bluedicevalue,yellowdicevalue;

    float pos1[][],pos2[][],pos3[][],pos4[][];

    float pieceWidth,pieceHeight;

    Drawable redCircle,greenCircle,blueCircle,yellowCircle;

    List<Player> players;

    static String currentPlayerColor = "";
    static  int currentPlayerPosition = 3;

    static int currentPlayerDice = 6;

    int currentPlayerIndex = 0;

    String currentPlayerName;

    int currentPlayerSelectedIndex = 0;

    List<Piece> rp,gp,bp,yp;

    int[] wBlocks52,wBlocks58,wBlocks64,wBlocks70;

    static boolean isDiceMovableExtraChance = false;
    Dice d;

    MediaPlayer gameStartSound,diceRollSound,stepSound,safeSound,deathSound,pantaSound,congratulationSound;
    int sizeOfBox;

    float blockSize;

    Piece autoMove;

    HashSet<Integer> safeSpots;

    boolean normalPiece = true;

    private AnimatorSet[] animatorSets;
    // Per-step pop-up/pop-down AnimatorSet created in move(). Stored as a
    // field so the previous one can be cancelled before a new one starts —
    // otherwise a 6-step move chains 6 short AnimatorSets that all tick the
    // Choreographer concurrently, causing UI-thread jank on lower-end devices.
    private AnimatorSet currentMovePopAnim;

    ImageView crownIndex1,crownIndex2,crownIndex3,crownIndex4;

    int currentWinnerPosition = 1;

    // ingame menu button and items
    ConstraintLayout ingamemenuitemslayout;
    ImageView ingamemenubtn,menuremoveplayersbtn,menuexitbtn;

    // ingameexitgamelayout
    ConstraintLayout quitgamelayout;
    ImageView ingameyesbtn,ingamenobtn,ingamesoundbtn,ingamemusicbtn;

    // ingame rmp layout
    ConstraintLayout ingamermplayout,rmp1bg,rmp2bg,rmp3bg,rmp4bg;
    ImageView rmpp1piece,rmpp2piece,rmpp3piece,rmpp4piece,rmpp1removeicon,rmpp2removeicon,rmpp3removeicon,rmpp4removeicon,rmpbackbtn,rmpmenubtn;
    TextView rmpp1name, rmpp2name,rmpp3name,rmpp4name;

    // ingmae rmp sublayout confirmrmplayout
    ConstraintLayout confirmrmplayout;
    ImageView selectedrmppiece,confirmrmpyesbtn,confirmrmpnobtn;

    int selectedconfirmrmpplayerindex = -1;

    int rmpindexforp1=1,rmpindexforp2=2,rmpindexforp3=3,rmpindexforp4=4;

    // players exit boxes
    ImageView p1exitbox,p2exitbox,p3exitbox,p4exitbox;
    String player1color,player2color,player3color,player4color;
    String player1name,player2name,player3name,player4name;

    int currentWinnerPlayerIndex = -1;

    // Congratulations screen
    ConstraintLayout congratulationslayout;
    ImageView congratsmenubtn,congratssoundbtn,congratssharebtn,congratsreplaybtn;

    ConstraintLayout winnerlistp1layout,winnerlistp2layout,winnerlistp3layout,winnerlistp4layout;
    ImageView wlistcrown1,wlistcrown2,wlistcrown3,wlistcrown4,wlistpiece1,wlistpiece2,wlistpiece3,wlistpiece4,wlistwinorlose1,wlistwinorlose2,wlistwinorlose3,wlistwinorlose4;
    TextView wlistname1,wlistname2,wlistname3,wlistname4;

    boolean isbluegreenreadytowin=false,isredyellowreadytowin=false;

    Handler globalHandler;
    // Hint-arrow infinite animator — kept as a field so onDestroy can cancel
    // it. Was previously a local variable, so it leaked the whole activity.
    private ValueAnimator hintArrowAnimator;
    // Blink runnable for the "game start" splash — kept so onDestroy can
    // cancel it instead of leaving it self-rescheduling forever.
    private Runnable blinkAnim;
    // Long-press time: 3 seconds (was 5s in earlier versions). Reduced
    // because the user wants a faster admin on/off toggle.
    private static final long SMART_DICE_LONG_PRESS_MS = 3000L;
    private final Handler smartDiceHandler = new Handler(Looper.getMainLooper());
    private Runnable smartDiceLongPressAction;
    // The actual scheduled lambda — kept as a field so removeCallbacks() can
    // cancel it. Without this, ACTION_UP would call removeCallbacks on a
    // *different* object reference (the inline lambda), which would never
    // remove anything — every dice tap fired the long-press 3 seconds later.
    // This was the root cause of the "admin silently re-assigns on every tap"
    // bug.
    private Runnable pendingSmartDiceLongPress;
    // Tracks which View received the ACTION_DOWN so the long-press knows
    // whether it came from the central dice (which overlaps a corner pad by
    // geometry) or from a real corner pad. Without this, every central-dice
    // long-press was mis-routed to `findPressedCornerPosition`, which then
    // re-assigned the admin to whatever corner the central dice happened
    // to be over (i.e. the current player's color).
    private boolean longPressSourceIsCentralDice = false;
    private Dice smartDiceOwner;
    private String smartDiceOwnerColor;
    private int smartDiceOwnerPlayerIndex = -1;
    private boolean smartDiceOwnerLocked = false;
    private boolean smartDiceEnabled = false;
    // Natural-pattern memory for the assisted roll engine (anti-detect guards).
    private final int[] smartRecentRolls = new int[12];
    private int smartRecentRollsIndex = 0;
    private int smartRecentRollsFilled = 0;
    private int smartSixStreak = 0;
    private int smartLastRollValue = 0;
    private int smartSecondLastRollValue = 0;
    // Screen position of the gesture that is currently running (long-press target lookup).
    private float smartLastTouchRawX = -1f;
    private float smartLastTouchRawY = -1f;
    // Real-ludo three-six rule: consecutive sixes rolled by the current player.
    private int tableConsecutiveSixes = 0;
    // Quota engine: owner rolls since the last six (keeps ~3 sixes per 12 rolls).
    private int smartRollsSinceLastSix = 0;
    // Fixed 12-roll quota block counters (guarantee at least 3 sixes per block).
    private int smartQuotaBlockRolls = 0;
    private int smartQuotaBlockSixes = 0;

    // --------------------------------------------------------------------
    // Per-face fairness engine — modeled on the real Ludo King app's
    // DiceController which has fields named maxLimit_1..maxLimit_6,
    // counterToGetSix, curRollCount, p2Blank / p4Blank / compBlank / myBlank,
    // and curRollCount234 / counterToGetSix234. The original game enforces
    // that NO dice face can come up too often (per-face max limits over a
    // balancing window), and injects a 6 when a player has gone too many
    // rolls without one (counterToGetSix). This eliminates the "green
    // went half the match without a 6" complaint because the balancing
    // is GLOBAL — applies to all players, not just the admin.
    // --------------------------------------------------------------------

    // Rolling 18-roll window of dice faces for ALL players. Each face's
    // count must stay under its maxLimit. Real Ludo King uses ~3-4 per
    // face over a similar window.
    private final int[] fairnessRecentFaces = new int[18];
    private int fairnessRecentFacesIndex = 0;
    private int fairnessRecentFacesFilled = 0;
    // Per-face caps — face 1 through 6 can each appear at most this many
    // times in the rolling window. Real Ludo King's default is around 4.
    private static final int FAIRNESS_FACE_MAX = 4;
    // Per-player "rolls since last 6" counter. When this exceeds the
    // threshold, the next roll is forced to a 6 (unless it would
    // overshoot the home track — that case is handled later).
    private final int[] playerRollsSinceLastSix = new int[4];
    private static final int PLAYER_FORCED_SIX_AFTER = 5; // matches Ludo King's counterToGetSix threshold
    // Per-player "blank rolls" counter (rolls where the player couldn't
    // move). When this exceeds the threshold, the next roll is biased
    // toward a useful value. Real Ludo King has p2Blank/p4Blank/etc.
    private final int[] playerBlankRolls = new int[4];
    private static final int PLAYER_BLANK_LIMIT = 3;


    SharedPreferences sharedPreferences;

    boolean isSoundOn=true,isMusicOn=false;

    int botwins=0,botloses=0;

    ImageView gameStartImageView;

    // Cached "current player" blink animation — was previously inflated via
    // AnimationUtils.loadAnimation() inside setActive() on every single turn
    // change (one XML inflation per turn). Cached here once at initViews().
    private Animation cachedBlinkAnimation;

    private static final String ACTIVE_GAME_SNAPSHOT_KEY = "active_ludo_game_snapshot_v1";
    private boolean isGameSessionActive = false;
    private boolean isQuitConfirmed = false;

    class Piece
    {
        // managed by constructor
        String colour;
        ConstraintLayout piece;
        ImageView readyToPick;
        int startPosition;

        int numberOfSteps = 0;

        int endPosition;
        float defX;
        float defY;

        int diceValue;

        // managed at runtime
        boolean isAlive = false;
        boolean isClickable = true;
        boolean isBotPiece;
        boolean isReadyToEnterWinnerZone = false;
        int currBlock = -1;

        boolean hasCompletedItsPurpose = false;

        int currWinnerBlock = 0;

        int[] winnerBlocks;

        ImageView pieceIcon,pieceStandIcon;
        boolean isThisPlayerWon = false;


        private final ObjectAnimator rotateAnimator;

        Piece(String color,ConstraintLayout piece,ImageView readyToPick,ImageView pieceIcon,ImageView pieceStandIcon,int startPosition,float defX,float defY,boolean isBotPiece)
        {
            this.colour = color;
            this.piece = piece;
            this.readyToPick = readyToPick;
            this.pieceIcon = pieceIcon;
            this.pieceStandIcon = pieceStandIcon;
            this.startPosition = startPosition;
            this.defX = defX;
            this.defY = defY;
            this.isBotPiece = isBotPiece;
            this.piece.setTranslationX(defX);
            this.piece.setTranslationY(defY);
            this.readyToPick.setVisibility(View.INVISIBLE);
            this.piece.setVisibility(View.VISIBLE);
            this.endPosition = startPosition!=0?startPosition-2:50;
            // NOTE: hardware layers are now toggled in activeState() /
            // inactiveState() so we don't keep 32 GPU textures live for the
            // whole game. Constructor leaves them at LAYER_TYPE_NONE.
            this.piece.setLayerType(View.LAYER_TYPE_NONE, null);
            this.readyToPick.setLayerType(View.LAYER_TYPE_NONE, null);
            if(!isBotPiece) {
                View.OnClickListener pieceClickListener = view -> {
                    // CLICK-THROUGH FIX: if this piece is NOT the current
                    // player's, the tap might have landed on an opponent's
                    // piece that was stacked on top of OUR piece at the same
                    // cell. In that case, find OUR movable piece at this
                    // cell and dispatch the click to it instead of silently
                    // swallowing the tap.
                    if (!currentPlayerColor.equals(colour)) {
                        // This is an opponent's piece. See if any of the
                        // CURRENT player's pieces shares this cell and is
                        // clickable — if so, redirect the tap there.
                        Piece ours = findCurrentPlayerPieceAtCell(this.currBlock);
                        if (ours != null && ours != this) {
                            ours.piece.performClick();
                            return;
                        }
                        // No current-player piece here — silently ignore.
                        return;
                    }
                    if (currentPlayerDice == -1) {
                        // Already consumed by a previous tap on this turn —
                        // a piece is mid-move. Ignore silently; the move
                        // will complete and the turn will advance.
                        return;
                    }
                    if (isAlive && isClickable) {
                        diceValue = currentPlayerDice;
                        currentPlayerDice = -1;  // consume — blocks further taps
                        // Only call inactiveState on the OTHER pieces of the same
                        // color — calling it on the moving piece right before
                        // move() would drop its hardware layer to NONE, causing
                        // software compositing during the move animation (a
                        // major source of the "choppy" feel). The moving piece's
                        // layer is re-promoted inside move() itself.
                        for (Piece p : getPiecesByColor(colour)) {
                            if (p != this) { p.inactiveState(); }
                        }
                        // Skip the redundant checkAdjustments here — move()
                        // calls it at its first line (line ~541) before
                        // animating, so this saved one full O(16) +
                        // ArrayList allocation per click.
                        move(diceValue);
                    } else if (!isAlive && currentPlayerDice == 6) {
                        currentPlayerDice = -1;  // consume
                        for (Piece p : getPiecesByColor(colour)) {
                            p.inactiveState();
                        }
                        makeAlive();
                    }
                };
                // A stacked token can put the parent container behind another
                // token. Binding the same action to the visible children makes
                // taps reliable without changing the board geometry.
                this.piece.setOnClickListener(pieceClickListener);
                this.pieceIcon.setOnClickListener(pieceClickListener);
                this.pieceStandIcon.setOnClickListener(pieceClickListener);
                this.readyToPick.setOnClickListener(pieceClickListener);
            }
            if(!normalPiece) {
                pieceIcon.setScaleX(0.8f);pieceIcon.setScaleY(0.8f);
                pieceIcon.setImageDrawable(getStylishIconDrawableByColor(this.colour));
                pieceStandIcon.setVisibility(GONE);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) readyToPick.getLayoutParams();
                params.topMargin = 0;
                params.topToTop = pieceIcon.getId();
                params.leftToLeft = pieceIcon.getId();
                params.bottomToBottom = pieceIcon.getId();
                params.rightToRight = pieceIcon.getId();
                this.readyToPick.setLayoutParams(params);
                readyToPick.setScaleX(1.2f);readyToPick.setScaleY(1.2f);
            }

            /*piece.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    die();
                    return false;
                }
            });*/
            rotateAnimator = ObjectAnimator.ofFloat(readyToPick,"rotation",360,0);
            rotateAnimator.setDuration(900);
            rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            rotateAnimator.setRepeatMode(ObjectAnimator.RESTART);
            rotateAnimator.setInterpolator(new LinearInterpolator());
            // Do NOT start the animator here — that was a major FPS leak:
            // all 16 pieces' animators ran forever even while readyToPick
            // was INVISIBLE. The animator is now started only in
            // activeState() (when the piece actually needs to spin) and
            // cancelled in inactiveState() + die() + onDestroy().
        }

        void onClickForBot() {
            if (isAlive && isClickable && currentPlayerColor.equals(colour)) {
                diceValue = currentPlayerDice;
                currentPlayerDice = -1;
                for (Piece p : getPiecesByColor(colour)) {
                    p.inactiveState();
                }
                checkAdjustments(currBlock);
                move(diceValue);
            } else if (!isAlive && currentPlayerColor.equals(colour) && currentPlayerDice == 6) {
                currentPlayerDice = -1;
                for (Piece p : getPiecesByColor(colour)) {
                    p.inactiveState();
                }
                makeAlive();
            }
        }

        void makeAlive()
        {
            isAlive = true;
            currBlock = startPosition;
            // ORIGINAL 400ms — restored after the previous version tightened
            // to 250ms. Matches the original Ludo King release-from-home feel.
            piece.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            piece.animate().translationX(x[startPosition]+pushXForPieces).translationY(y[startPosition]-pushYForPieces).setDuration(400).start();
            globalHandler.postDelayed(() -> {
                piece.setLayerType(View.LAYER_TYPE_NONE, null);
                isDiceMovableExtraChance = true;
                if(!isBotPiece) { hintArrow.setVisibility(View.VISIBLE); }
                checkAdjustments(currBlock);
                if(currBlock>=24 && currBlock<=51) {
                    piece.setElevation(51-currBlock);
                } else if(currBlock>=64 && currBlock<=69) {
                    piece.setElevation(70-currBlock);
                } else {
                    piece.setElevation(currBlock);
                }
            },400);
            globalHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isBotPiece) {
                        d.roll();
                    }
                }
            },550); // ORIGINAL 550ms — restored
        }

        void die()
        {
            isAlive = false;
            isClickable = false;
            isReadyToEnterWinnerZone = false;
            checkAdjustments(currBlock);
            currWinnerBlock=0;
            numberOfSteps = 0;
            // Stop the spinning indicator when the piece dies.
            if (rotateAnimator != null) { rotateAnimator.cancel(); }
            readyToPick.setVisibility(View.INVISIBLE);
            if(isSoundOn && deathSound != null) {
                try {
                    if (deathSound.isPlaying()) { deathSound.pause(); }
                    deathSound.seekTo(0);
                    deathSound.start();
                } catch (Exception ignored) {}
            }


            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if(currBlock!=startPosition)
                    {
                        if(currBlock==0) currBlock=51; else --currBlock;
                        piece.setTranslationX(x[currBlock]+pushXForPieces);
                        piece.setTranslationY(y[currBlock]-pushYForPieces);
                        globalHandler.postDelayed(this,20);
                    } else {
                        if(piece.getScaleX()<1.0f) {
                            piece.setScaleX(1.0f);
                            piece.setScaleY(1.0f);
                        }
                        piece.animate().translationX(defX).translationY(defY).setDuration(400).start();
                        globalHandler.removeCallbacks(this);
                    }
                }
            };
            globalHandler.post(r);
            //piece.animate().translationX(defX).translationY(defY).setDuration(400).start();
        }

        void activeState()
        {
            isClickable = true;
            // Re-allocate the hardware layer only while the piece is
            // actually animating — freed in inactiveState().
            piece.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            readyToPick.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            // Defensive: only start if not already running (calling start()
            // on a running animator restarts it from 0 — visible "jump").
            if (rotateAnimator != null && !rotateAnimator.isStarted()) {
                rotateAnimator.start();
            }
            readyToPick.setVisibility(View.VISIBLE);
            if(piece.getScaleX()<1.0f) {
                piece.setScaleX(0.95f);
                piece.setScaleY(0.95f);
            } else {
                piece.setScaleX(1.0f);
                piece.setScaleY(1.0f);
            }
        }

        void inactiveState()
        {
            isClickable = false;
            if (rotateAnimator != null) { rotateAnimator.cancel(); }
            readyToPick.setVisibility(View.INVISIBLE);
            // Drop the hardware layer when the piece isn't animating —
            // keeping 16 pieces × 2 views = 32 GPU textures permanently
            // allocated for the whole game was a major cause of growing
            // GPU-memory pressure on long sessions.
            piece.setLayerType(View.LAYER_TYPE_NONE, null);
            readyToPick.setLayerType(View.LAYER_TYPE_NONE, null);
            if(currBlock!=-1) {
                checkAdjustments(currBlock);
            }
        }

        void move(int n) {
            // Defensive guard: never run move() with a stale dice value.
            // The piece click listener now blocks currentPlayerDice == -1,
            // but a bot or a saved-state-resume path could still call here
            // with n <= 0 — that would crash showStep and freeze the turn.
            if (n <= 0) { return; }
            isClickable = false;
            // Step sound: restart on EVERY step. The previous version checked
            // `isPlaying()` and skipped the restart if the sound was still
            // going — but the step sound is ~470ms long and the step gap is
            // only 200ms, so step 1's sound was always still playing when
            // step 2 fired, and steps 2..N were silent. Now we forcibly
            // pause+seekTo+start on every step so the user hears one click
            // per step (matching the real Ludo King app's audio behavior).
            if(stepSound!=null && isSoundOn) {
                try {
                    if (stepSound.isPlaying()) {
                        stepSound.pause();
                    }
                    stepSound.seekTo(120);
                    stepSound.start();
                } catch (Exception ignored) {}
            }



            if (currBlock == -1) {
                currBlock = startPosition;
            }

            if(currBlock>=51)
                currBlock = 0;
            else
                currBlock++;

            if(currBlock==0) { checkAdjustments(51); } else { checkAdjustments(currBlock-1); }

            if(isReadyToEnterWinnerZone)
            {
                // Defensive bounds check: winnerBlocks is a 6-cell array. A
                // stale state or a check() that allowed an overshoot could
                // push currWinnerBlock past 5 and crash with AIOOBE, which
                // would freeze the turn and leave the piece visually stuck.
                if (winnerBlocks == null || currWinnerBlock < 0 || currWinnerBlock >= winnerBlocks.length) {
                    // Treat as finished — snap to last winner cell and bail.
                    currWinnerBlock = winnerBlocks != null ? winnerBlocks.length - 1 : 0;
                    currBlock = winnerBlocks != null ? winnerBlocks[currWinnerBlock] : currBlock;
                } else {
                    currBlock = winnerBlocks[currWinnerBlock];
                }
                currWinnerBlock++;
            }

            if(currBlock==endPosition) { winnerBlocks = getWinnerBlocks(endPosition); isReadyToEnterWinnerZone = true; }

            // Temporarily promote the moving piece to a hardware layer for the
            // duration of the animation — this avoids software compositing
            // during the move which was the #1 cause of the "choppy/laggy"
            // feeling on long games. inactiveState() (called before move())
            // had set the layer to NONE, so the piece was animating without
            // GPU acceleration. We re-promote here just for the move.
            piece.setLayerType(View.LAYER_TYPE_HARDWARE, null);

            // ORIGINAL speed (220ms) — restored after the previous version
            // made the game too fast (1.5-2x). This matches the original
            // Ludo King app's per-step animation timing.
            piece.animate().translationX(x[currBlock] + pushXForPieces).translationY(y[currBlock] - pushYForPieces).setDuration(220).start();
            /*if(!isReadyToEnterWinnerZone)
            {
                piece.animate().translationX(x[currBlock] + pushXForPieces).translationY(y[currBlock] - pushYForPieces).setDuration(300).start();
            } else {
                piece.animate().translationX(x[winnerBlocks[currWinnerBlock]] + pushXForPieces).translationY(y[winnerBlocks[currWinnerBlock]] - pushYForPieces).setDuration(300).start();
            }*/

            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 1.4f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 1.4f);
            ObjectAnimator popUpAnimator = ObjectAnimator.ofPropertyValuesHolder(piece, scaleX, scaleY);
            popUpAnimator.setDuration(100);
            scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.4f, 1.0f);
            scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.4f, 1.0f);
            ObjectAnimator popDownAnimator = ObjectAnimator.ofPropertyValuesHolder(piece, scaleX, scaleY);
            popDownAnimator.setDuration(90);

            /*ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f);
            alphaAnimator.setDuration(duration);*/

            // Cancel any leftover pop-animator from the previous step so
            // they don't pile up and tick the Choreographer concurrently.
            if (currentMovePopAnim != null && currentMovePopAnim.isRunning()) {
                currentMovePopAnim.cancel();
            }
            currentMovePopAnim = new AnimatorSet();
            currentMovePopAnim.playSequentially(popUpAnimator,popDownAnimator);
            currentMovePopAnim.start();




            globalHandler.postDelayed(() -> {
                // Drop the hardware layer we promoted at the start of this
                // step — back to NONE for idle. (Re-promoted on next step.)
                piece.setLayerType(View.LAYER_TYPE_NONE, null);

                if(n>1) {
                    int step = currBlock;
                    // Pre-showStep delay restored to 100ms (original timing).
                    globalHandler.postDelayed( ()-> {
                        showStep(n,this.colour,x[step],y[step]);
                    },100);
                    numberOfSteps++;
                    move(n - 1);
                } else {
                    showStep(n,this.colour,x[currBlock],y[currBlock]);
                    isThisPlayerWon = false;
                    if(currBlock>=24 && currBlock<=51) {
                        piece.setElevation(51-currBlock);
                    } else if(currBlock>=64 && currBlock<=69) {
                        piece.setElevation(70-currBlock);
                    } else {
                        piece.setElevation(currBlock);
                    }
                    stepSound.pause();
                    numberOfSteps++;

                    boolean isDeadChanceAvailable = false;

                    if(safeSpots.contains(currBlock) && isSoundOn && safeSound != null)
                    {
                        try {
                            if (safeSound.isPlaying()) { safeSound.pause(); }
                            safeSound.seekTo(0);
                            safeSound.start();
                        } catch (Exception ignored) {}
                    } else if(currWinnerBlock>0) {
                        int temp = 0;
                        if(currWinnerBlock>5)
                        {
                            hasCompletedItsPurpose = true;
                            if(isSoundOn && pantaSound != null) {
                                try {
                                    if (pantaSound.isPlaying()) { pantaSound.pause(); }
                                    pantaSound.seekTo(0);
                                    pantaSound.start();
                                } catch (Exception ignored) {}
                            }

                            List<Piece> pieces = getPiecesByColor(this.colour);
                            for(Piece p: pieces) { if(p.hasCompletedItsPurpose) { temp++; } }

                            int pantaValue = 4;
                            if(gametype==3) { pantaValue = 1; }

                            if(temp>=pantaValue) { //4
                                isThisPlayerWon = true;
                                isDeadChanceAvailable = isDiceMovableExtraChance = false;
                                if(players.size()==2) {
                                    int x;
                                    if(currentPlayerIndex==0) { x=players.size()-1; } else { x=currentPlayerIndex-1; }
                                    if(gametype!=3) {
                                        globalHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkPantaAdjustments(colour, currBlock);
                                            }
                                        }, 50);
                                    }
                                    redHomeBlink.clearAnimation();
                                    redHomeBlink.setVisibility(GONE);
                                    mainDiceImageView.setVisibility(GONE);
                                    players.remove(x);
                                    if(gametype==2) {
                                        for(Piece p: pieces) { p.piece.setVisibility(GONE); }
                                        if(colour.equals("red") || colour.equals("yellow")) {
                                            if(isredyellowreadytowin) {
                                                setRedYellowTeamAsWinners();
                                            } else { isredyellowreadytowin = true; }
                                        } else {
                                            if(isbluegreenreadytowin) {
                                                stopEverything();
                                                setBlueGreenTeamAsWinners();
                                                ((TextView)findViewById(R.id.team1name1)).setText(player3name);
                                                ((TextView)findViewById(R.id.team1name2)).setText(player2name);
                                                ((TextView)findViewById(R.id.team2name1)).setText(player1name);
                                                ((TextView)findViewById(R.id.team2name2)).setText(player4name);
                                                showGameOverScreen();
                                                mainDiceImageView.setVisibility(GONE);
                                                hintArrow.setVisibility(GONE);
                                            } else { isbluegreenreadytowin = true; }
                                        }
                                        return;
                                    } else {
                                        setCurrentPlayerAsWinnerAtCurrentPosition(currentPlayerSelectedIndex, colour, currentPlayerName);
                                    }
                                    String name = players.get(0).name,colour = players.get(0).color;
                                    int loserplayerselectedindex = players.get(0).index;

                                    if(gametype==4) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        if(loserplayerselectedindex!=0) {
                                            botwins += 1;
                                            editor.putInt("botwins",botwins);
                                        } else {
                                            botloses += 1;
                                            editor.putInt("botloses",botloses);
                                        }
                                        ((TextView)findViewById(R.id.userwins)).setText((botwins+""));
                                        ((TextView)findViewById(R.id.userloses)).setText((botloses+""));

                                        editor.apply();
                                    }

                                    hideThisPlayerDiceBg(loserplayerselectedindex);
                                    if(nop==2) {
                                        wlistcrown2.setVisibility(View.INVISIBLE);
                                        wlistname2.setText(name);
                                        wlistpiece2.setImageDrawable(getPieceDrawableByColor(colour));
                                        winnerlistp3layout.setVisibility(GONE);
                                        winnerlistp4layout.setVisibility(GONE);
                                    } else if(nop==3) {
                                        //makeThisPlayerLoserBasedOnSelectedIndexNOP3(colour,loserplayerselectedindex);
                                        wlistcrown3.setVisibility(View.INVISIBLE);
                                        wlistname3.setText(getLoserNameBasedOnSelectedIndex(loserplayerselectedindex));
                                        wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                                        winnerlistp4layout.setVisibility(GONE);
                                    } else {
                                        wlistcrown4.setVisibility(View.INVISIBLE);
                                        wlistname4.setText(getLoserNameBasedOnSelectedIndex(loserplayerselectedindex));
                                        wlistpiece4.setImageDrawable(getPieceDrawableByColor(colour));
                                    }
                                    showGameOverScreen();
                                    return;
                                } else {
                                    int x;
                                    if (currentPlayerIndex == 0) {
                                        x = players.size() - 1;
                                    } else {
                                        x = currentPlayerIndex - 1;
                                    }
                                    currentWinnerPlayerIndex = currentPlayerIndex;
                                    //checkAdjustments(currBlock);
                                    if(gametype==2) {
                                        for(Piece p: pieces) { p.piece.setVisibility(GONE); }
                                        if(colour.equals("red") || colour.equals("yellow")) {
                                            if(isredyellowreadytowin) {
                                                setRedYellowTeamAsWinners();
                                            } else { isredyellowreadytowin = true; }
                                        } else {
                                            if(isbluegreenreadytowin) {
                                                stopEverything();
                                                setBlueGreenTeamAsWinners();
                                                ((TextView)findViewById(R.id.team1name1)).setText(player3name);
                                                ((TextView)findViewById(R.id.team1name2)).setText(player2name);
                                                ((TextView)findViewById(R.id.team2name1)).setText(player1name);
                                                ((TextView)findViewById(R.id.team2name2)).setText(player4name);
                                                showGameOverScreen();
                                                mainDiceImageView.setVisibility(GONE);
                                                hintArrow.setVisibility(GONE);
                                            } else { isbluegreenreadytowin = true; }
                                        }
                                    } else {
                                        setCurrentPlayerAsWinnerAtCurrentPosition(currentPlayerSelectedIndex, colour, currentPlayerName);
                                    }

                                    currentPlayerIndex = x;
                                    menuremoveplayersbtn.setVisibility(GONE);
                                    players.remove(currentPlayerIndex);
                                    if (currentPlayerIndex == players.size()) {
                                        currentPlayerIndex=0;
                                    }
                                    //Toast.makeText(MainActivity.this, currentPlayerIndex + "", Toast.LENGTH_SHORT).show();
                                    switchPlayers();
                                    if(gametype!=3) {
                                        globalHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkPantaAdjustments(colour,currBlock);
                                            }
                                        }, 50);
                                    }
                                    d.isDiceClickable = true;
                                    return;
                                }
                            }
                        }
                    } else {
                        isDeadChanceAvailable = checkDeath(this,currBlock);
                    }

                    globalHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!isThisPlayerWon) { checkAdjustments(currBlock); }
                        }
                    },10);
                    //checkAdjustments(currBlock);

                    if(diceValue == 6 || currWinnerBlock>5 || isDeadChanceAvailable && !isThisPlayerWon) {
                        isDiceMovableExtraChance = true;
                        if(!isBotPiece) { hintArrow.setVisibility(View.VISIBLE); } else {
                            // Bot reroll delay restored to 150ms (original).
                            globalHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    d.roll();
                                }
                            },150);
                        }
                    } else {
                        switchPlayers();
                        d.isDiceClickable = true;
                    }
                }
            }, 200); // ORIGINAL 200ms — restored after the previous version
                    // tightened to 130ms which made the game too fast.
        }



        public boolean check(int diceValue) {
            if(diceValue == 6)
            {
                // Allow the winning move: a piece on step 51 can finish with a 6
                // (51 + 6 = 57 — the last winner cell). Use <= 57, NOT < 57.
                if(!isAlive || (numberOfSteps+diceValue)<=57)
                {
                    activeState();
                    return true;
                }
                // A 6 that cannot be played (would overshoot the home track) must
                // still deactivate the piece — otherwise isClickable stays true
                // from a previous state and the next tap calls move(6) which then
                // walks off the end of winnerBlocks[] and crashes the turn.
                inactiveState();
                return false;
            } else if(isAlive && (numberOfSteps+diceValue)<=57 && !hasCompletedItsPurpose && !isThisPlayerWon) {
                activeState();
                return true;
            } else {
                inactiveState();
                return false;
            }
        }
    }

    private void setBlueGreenTeamAsWinners() {
        ((ImageView)findViewById(R.id.imageView132)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team1header,null));

        ((ImageView)findViewById(R.id.imageView137)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team2header,null));

        ((ImageView)findViewById(R.id.imageView133)).setImageDrawable(getPieceDrawableByColor("blue"));
        ((ImageView)findViewById(R.id.imageView135)).setImageDrawable(getPieceDrawableByColor("green"));
        ((ImageView)findViewById(R.id.imageView138)).setImageDrawable(getPieceDrawableByColor("red"));
        ((ImageView)findViewById(R.id.imageView140)).setImageDrawable(getPieceDrawableByColor("yellow"));

        ((TextView)findViewById(R.id.team1name1)).setText(player3name);
        ((TextView)findViewById(R.id.team1name2)).setText(player2name);
        ((TextView)findViewById(R.id.team2name1)).setText(player1name);
        ((TextView)findViewById(R.id.team2name2)).setText(player4name);
    }

    private void setRedYellowTeamAsWinners() {
        stopEverything();
        showGameOverScreen();
        mainDiceImageView.setVisibility(GONE);
        hintArrow.setVisibility(GONE);

        ((ImageView)findViewById(R.id.imageView132)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team2header,null));

        ((ImageView)findViewById(R.id.imageView137)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team1header,null));

        ((ImageView)findViewById(R.id.imageView133)).setImageDrawable(getPieceDrawableByColor("red"));
        ((ImageView)findViewById(R.id.imageView135)).setImageDrawable(getPieceDrawableByColor("yellow"));
        ((ImageView)findViewById(R.id.imageView138)).setImageDrawable(getPieceDrawableByColor("blue"));
        ((ImageView)findViewById(R.id.imageView140)).setImageDrawable(getPieceDrawableByColor("green"));

        ((TextView)findViewById(R.id.team1name1)).setText(player1name);
        ((TextView)findViewById(R.id.team1name2)).setText(player4name);
        ((TextView)findViewById(R.id.team2name1)).setText(player3name);
        ((TextView)findViewById(R.id.team2name2)).setText(player2name);
    }

    private String getLoserNameBasedOnSelectedIndex(int loserplayerselectedindex) {
        switch (loserplayerselectedindex+1) {
            case 1: return player3name;
            case 2: return player1name;
            case 3: return player2name;
            case 4: return player4name;
        }
        return player3name;
    }

    private void showGameOverScreen() {
        isGameSessionActive = false;
        clearSavedGameSnapshot();
        congratulationslayout.setVisibility(View.VISIBLE);

        if(isSoundOn) {
            congratulationSound = MediaPlayer.create(this, R.raw.congratulations);
            congratulationSound.setLooping(true);
            congratulationSound.start();
        }

        ImageView imageView = findViewById(R.id.fireworksview);
        Glide.with(this)
                .asGif()
                .load(R.drawable.fireworksimg)
                .into(new DrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (resource instanceof GifDrawable) {
                            GifDrawable gifDrawable = (GifDrawable) resource;
                            gifDrawable.setLoopCount(GifDrawable.LOOP_FOREVER);
                            gifDrawable.start();
                        }
                    }
                }.getView());
    }

    private void makeThisPlayerLoserBasedOnSelectedIndexNOP3(String colour, int loserplayerselectedindex) {
        switch ((loserplayerselectedindex+1)) {
            case 1:
                if(nop==3) {
                    wlistcrown3.setVisibility(View.INVISIBLE);
                    wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                    wlistname3.setText(player3name);
                    wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.loser1, null));
                }
                hideThisPlayerDiceBg(1);
                break;
            case 2:
                if(nop==3) {
                    wlistcrown3.setVisibility(View.INVISIBLE);
                    wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                    wlistname3.setText(player1name);
                    wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.loser1, null));
                }
                hideThisPlayerDiceBg(2);
                break;
            case 3:
                if(nop==3) {
                    wlistcrown3.setVisibility(View.INVISIBLE);
                    wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                    wlistname3.setText(player2name);
                    wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.loser1, null));
                }
                hideThisPlayerDiceBg(3);
                break;
            case 4:
                if(nop==3) {
                    wlistcrown3.setVisibility(View.INVISIBLE);
                    wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                    wlistname3.setText(player4name);
                    wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.loser1, null));
                }
                hideThisPlayerDiceBg(4);
                break;
        }
    }

    private void setCurrentPlayerAsWinnerAtCurrentPosition(int currentPlayerSelectedIndex,String colour,String playername) {
        switch ((currentPlayerSelectedIndex+1)) {
            case 1:
                crownIndex1.setVisibility(View.VISIBLE);
                findViewById(R.id.winnerindexnumber1).setVisibility(View.VISIBLE);
                ((ImageView)findViewById(R.id.winnerindexnumber1)).setImageDrawable(getWinnerNumberDrawableByNumber(currentWinnerPosition));
                if(nop!=2) {
                    addCurrentPlayerToCongratsList(colour, player3name, currentWinnerPosition);
                }
                else {
                    addCurrentPlayerToCongratsList(colour, playername, currentWinnerPosition);
                }

                hideThisPlayerDiceBg(1);
                break;
            case 2:
                crownIndex2.setVisibility(View.VISIBLE);
                findViewById(R.id.winnerindexnumber2).setVisibility(View.VISIBLE);
                ((ImageView)findViewById(R.id.winnerindexnumber2)).setImageDrawable(getWinnerNumberDrawableByNumber(currentWinnerPosition));
                if(nop!=2) {
                    addCurrentPlayerToCongratsList(colour, player1name, currentWinnerPosition);
                }
                else {
                    addCurrentPlayerToCongratsList(colour, playername, currentWinnerPosition);
                }
                hideThisPlayerDiceBg(2);
                break;
            case 3:
                crownIndex3.setVisibility(View.VISIBLE);
                findViewById(R.id.winnerindexnumber3).setVisibility(View.VISIBLE);
                ((ImageView)findViewById(R.id.winnerindexnumber3)).setImageDrawable(getWinnerNumberDrawableByNumber(currentWinnerPosition));
                if(nop!=2) {
                    addCurrentPlayerToCongratsList(colour, player2name, currentWinnerPosition);
                }
                else {
                    addCurrentPlayerToCongratsList(colour, playername, currentWinnerPosition);
                }
                hideThisPlayerDiceBg(3);
                break;
            case 4:
                crownIndex4.setVisibility(View.VISIBLE);
                findViewById(R.id.winnerindexnumber4).setVisibility(View.VISIBLE);
                ((ImageView)findViewById(R.id.winnerindexnumber4)).setImageDrawable(getWinnerNumberDrawableByNumber(currentWinnerPosition));
                addCurrentPlayerToCongratsList(colour,playername,currentWinnerPosition);
                hideThisPlayerDiceBg(4);
                break;
        }
        currentWinnerPosition++;
    }

    private void addCurrentPlayerToCongratsList(String color,String name,int currentWinnerPosition) {
        switch (currentWinnerPosition) {
            case 1:
                wlistcrown1.setVisibility(View.VISIBLE);
                wlistcrown2.setVisibility(View.INVISIBLE);
                wlistpiece1.setImageDrawable(getPieceDrawableByColor(color));
                wlistname1.setText(name);
                wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.winner1,null));
                break;
            case 2:
                wlistcrown2.setVisibility(View.VISIBLE);
                wlistcrown3.setVisibility(View.INVISIBLE);
                wlistpiece2.setImageDrawable(getPieceDrawableByColor(color));
                wlistname2.setText(name);
                wlistwinorlose2.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.winner1,null));
                break;
            case 3:
                wlistcrown3.setVisibility(View.VISIBLE);
                wlistcrown4.setVisibility(View.INVISIBLE);
                wlistpiece3.setImageDrawable(getPieceDrawableByColor(color));
                wlistname3.setText(name);
                wlistwinorlose3.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.winner1,null));
                break;
            case 4:
                wlistcrown4.setVisibility(View.VISIBLE);
                wlistcrown4.setVisibility(View.INVISIBLE);
                wlistpiece1.setImageDrawable(getPieceDrawableByColor(color));
                wlistname1.setText(name);
                wlistwinorlose4.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.loser1,null));
                break;
        }
    }

    Drawable getWinnerNumberDrawableByNumber(int x) {
        switch (x) {
            case 1:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.winner1number,null);
            case 2:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.winner2number,null);
            case 3:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.winner3number,null);
            case 4:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.winner4number,null);
        }
        return null;
    }

    Drawable getWinnerCrownDrawableByNumber(int x) {
        switch (x) {
            case 1:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.listwinner1crown,null);
            case 2:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.listwinner2crown,null);
            case 3:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.listwinner3crown,null);
            case 4:
                return null;
        }
        return null;
    }

    private Drawable getStylishIconDrawableByColor(String colour) {
        switch(colour){
            case "red":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.redpiecestylish,null);
            case "blue":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.bluepiecestylish,null);
            case "green":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.greenpiecestylish,null);
            case "yellow":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.yellowpiecestylish,null);
        }
        return null;
    }

    void checkPantaAdjustments(String color,int targetBox) {
        int pantaBox=targetBox;
        boolean horizontal = (pantaBox >= 0 && pantaBox <= 4) || (pantaBox >= 18 && pantaBox <= 30) || (pantaBox >= 44 && pantaBox <= 51) || (pantaBox >= 52 && pantaBox <= 57) || (pantaBox >= 64 && pantaBox <= 69);
        boolean isWon = false;
        List<Piece> pairs = getPiecesByColor(color);
        for(Piece p : pairs) { if(p.hasCompletedItsPurpose) { isWon = true; } else {isWon = false; break;} }
        if(isWon) {
            if(horizontal) {
                float elevation = pairs.get(0).piece.getElevation();
                float extra = 4 * displayMetrics.density, yextra = 17 * displayMetrics.density;
                float xt = x[targetBox] + pushXForPieces, yt = y[targetBox];
                float scale = 100f / ((4) * 30 + 75);
                float gap = 100f - scale;
                //Toast.makeText(this, scale + "", Toast.LENGTH_SHORT).show();
                float space = blockSize - (pieceWidth * scale * 0.35f * 4 + pieceWidth * scale * 0.65f) / 2;
                if (space <= 0) {
                    space = 0;
                }
                space = 0;
                //scale = 0.70f;
                for (int i = 0; i < 4; i++) {
                    Piece p = pairs.get(i);
                    ConstraintLayout piece = p.piece;
                    piece.setElevation(elevation++);
                    piece.setScaleX(scale);
                    piece.setScaleY(scale);
                    piece.setTranslationX(xt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i) - (extra * scale * (4 / 2)) + space);
                    if(normalPiece) { piece.setTranslationY(yt - pieceHeight / 2); } else { piece.setTranslationY(yt - (pieceHeight/2) / 2); }
                }
            } else {
                float elevation = pairs.get(0).piece.getElevation();
                float extra = 4 * displayMetrics.density, yextra = 8 * displayMetrics.density;
                float xt = x[targetBox] + pushXForPieces, yt = y[targetBox]-pushYForPieces;
                float scale = 100f / ((4) * 30 + 75);
                float gap = 100f - scale;
                //Toast.makeText(this, scale + "", Toast.LENGTH_SHORT).show();
                float space = blockSize - (pieceWidth * scale * 0.35f * 4 + pieceWidth * scale * 0.65f) / 2;
                if (space <= 0) {
                    space = 0;
                }
                space = 0;
                //scale = 0.70f;
                for (int i = 0; i < 4; i++) {
                    Piece p = pairs.get(i);
                    ConstraintLayout piece = p.piece;
                    piece.setElevation(elevation++);
                    piece.setScaleX(scale);
                    piece.setScaleY(scale);
                    piece.setTranslationX(xt);
                    if(normalPiece) {piece.setTranslationY(yt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i)); }
                    else { piece.setTranslationY((yt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i))-(pieceWidth * scale * 0.30f)); }
                }
            }
        }
    }

    private void checkAdjustments(int targetBox) {
        int xi = currentPlayerIndex;
        if(currentWinnerPlayerIndex!=-1) {
            xi = currentWinnerPlayerIndex;
            currentWinnerPlayerIndex = -1;
        }
        List<Piece> pairs = new ArrayList<>();
        boolean horizontal = (targetBox >= 0 && targetBox <= 4) || (targetBox >= 18 && targetBox <= 30) || (targetBox >= 44 && targetBox <= 51) || (targetBox >= 52 && targetBox <= 57) || (targetBox >= 64 && targetBox <= 69);
        for(int i = 0; i<players.size(); i++)
        {
            if(xi>players.size()-1) { continue; }
            String colour = players.get(xi).getColor();
            List<Piece> pieces = getPiecesByColor(colour);
            if(horizontal)
                {
                    for (int j = 3; j >= 0; j--) {
                        Piece p = pieces.get(j);
                        if (p.currBlock == targetBox && targetBox != -1 && p.isAlive) {
                            pairs.add(p);
                        }
                    }
                } else {
                    for(Piece p : pieces) {
                        if (p.currBlock == targetBox && targetBox != -1 && p.isAlive) {
                            pairs.add(p);
                        }
                    }
                }

            if(xi>=players.size()-1) { xi = 0; } else { xi++; }
        }
        int n = pairs.size();
        if(n>1) {
            if(horizontal) {
                float extra = 4 * displayMetrics.density, yextra = 17 * displayMetrics.density;
                float xt = x[targetBox] + pushXForPieces, yt = y[targetBox];
                float scale = 100f / ((n) * 30 + 75);
                float gap = 100f - scale;
                //Toast.makeText(this, scale + "", Toast.LENGTH_SHORT).show();
                float space = blockSize - (pieceWidth * scale * 0.35f * n + pieceWidth * scale * 0.65f) / 2;
                if (space <= 0) {
                    space = 0;
                }
                space = 0;
                //scale = 0.70f;
                for (int i = 0; i < n; i++) {
                    Piece p = pairs.get(i);
                    ConstraintLayout piece = p.piece;
                    piece.setScaleX(scale);
                    piece.setScaleY(scale);
                    piece.setTranslationX(xt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i) - (extra * scale * (n / 2)) + space);
                    if(normalPiece) { piece.setTranslationY(yt - pieceHeight / 2); } else { piece.setTranslationY(yt - (pieceHeight/2) / 2); }
                }
            } else {
                float elevation = pairs.get(0).piece.getElevation();
                float extra = 4 * displayMetrics.density, yextra = 8 * displayMetrics.density;
                float xt = x[targetBox] + pushXForPieces, yt = y[targetBox]-pushYForPieces;
                float scale = 100f / ((n) * 30 + 75);
                float gap = 100f - scale;
                //Toast.makeText(this, scale + "", Toast.LENGTH_SHORT).show();
                float space = blockSize - (pieceWidth * scale * 0.35f * n + pieceWidth * scale * 0.65f) / 2;
                if (space <= 0) {
                    space = 0;
                }
                space = 0;
                //scale = 0.70f;
                for (int i = 0; i < n; i++) {
                    Piece p = pairs.get(i);
                    ConstraintLayout piece = p.piece;
                    piece.setElevation(elevation++);
                    piece.setScaleX(scale);
                    piece.setScaleY(scale);
                    piece.setTranslationX(xt);
                    if(normalPiece) {piece.setTranslationY(yt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i)); }
                    else { piece.setTranslationY((yt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i))-(pieceWidth * scale * 0.30f)); }
                }
            }
        } else if(pairs.size()==1) {
            Piece p = pairs.get(0);
            p.piece.setScaleX(1.0f);
            p.piece.setScaleY(1.0f);
            p.piece.setTranslationX(x[targetBox]+pushXForPieces);
            p.piece.setTranslationY(y[targetBox]-pushYForPieces);
        }
    }

    private boolean checkDeath(Piece killer, int targetBox) {
        int x = currentPlayerIndex;
        if(x>players.size()-1) { x = players.size()-1; }
        for(int i=0;i<players.size();i++)
        {
            String colour = players.get(x).getColor();
            if(!colour.equals(killer.colour))
            {
                List<Piece> pieces = getPiecesByColor(colour);
                for(Piece p : pieces)
                {
                    if(p.currBlock==targetBox)
                    {
                        if(gametype==2) {
                            if((killer.colour.equals("yellow") && p.colour.equals("red")) || (killer.colour.equals("red") && p.colour.equals("yellow")) || (killer.colour.equals("blue") && p.colour.equals("green")) || (killer.colour.equals("green") && p.colour.equals("blue")))
                            { continue; }
                        }
                        if(!safeSpots.contains(targetBox)) {
                            // Admin pieces are only cuttable in the last 12 steps
                            // before home; farther out they can never be killed.
                            if (isProtectedAdminPiece(p)) { continue; }
                            p.die();
                            return true;
                        }
                    }
                }
            }
            if(x>=players.size()-1) { x = 0; } else { x++; }
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Admin (smart-dice owner) protection & priority rules
    // ------------------------------------------------------------------

    /**
     * Sets up the admin at game start. Per the user's spec, the FIRST long
     * long-press chooses the admin color. So we leave smartDiceOwnerLocked
     * = false here, letting the first long-press pick the color. Cut-
     * protection rules check `smartDiceOwnerLocked && smartDiceOwnerColor !=
     * null`, so they remain inactive until a long-press happens — which is
     * the intended behavior (no admin = no cut-protection).
     */
    private void initAdminOwner() {
        // No pre-assignment — the first long-press chooses the admin.
        smartDiceOwner = d;
        smartDiceOwnerColor = null;
        smartDiceOwnerPlayerIndex = -1;
        smartDiceOwnerLocked = false;
        smartDiceEnabled = false;
        // Reset all assist memory too, so a previous match's history doesn't
        // bleed into this one.
        smartRecentRollsIndex = 0;
        smartRecentRollsFilled = 0;
        smartSixStreak = 0;
        smartLastRollValue = 0;
        smartSecondLastRollValue = 0;
        smartRollsSinceLastSix = 0;
        smartQuotaBlockRolls = 0;
        smartQuotaBlockSixes = 0;
        // Reset the per-face fairness engine for the new match too —
        // otherwise a previous match's face counts could block 6s from
        // appearing early in the new match.
        resetFairnessState();
    }

    /** Admin pieces can only be cut within the last 12 steps before home. */
    private boolean isProtectedAdminPiece(Piece p) {
        if (!smartDiceOwnerLocked || smartDiceOwnerColor == null) {
            return false;
        }
        if (!Objects.equals(p.colour, smartDiceOwnerColor)) {
            return false;
        }
        int stepsToHome = 57 - p.numberOfSteps;
        return stepsToHome > 12;
    }

    /**
     * True when the current roll must be kept from passing the player:
     * the smart-dice owner is still in the game, and this player is an
     * opponent whose FINAL token would finish with this value.
     */
    private boolean adminFinalPassBlockApplies() {
        if (!smartDiceOwnerLocked || smartDiceOwnerColor == null) {
            return false;
        }
        if (Objects.equals(currentPlayerColor, smartDiceOwnerColor)) {
            return false;
        }
        if (gametype == 2 && areSmartTeamMates(currentPlayerColor, smartDiceOwnerColor)) {
            return false;
        }
        return !adminPlayerHasPassed();
    }

    private boolean adminPlayerHasPassed() {
        for (Piece p : getPiecesByColor(smartDiceOwnerColor)) {
            if (!p.hasCompletedItsPurpose) {
                return false;
            }
        }
        return true;
    }

    /** Replaces the roll with a random value that neither passes this player
     *  nor lands on a protected admin token (no enemy stacking on the admin). */
    private int nonFinishingRollValue(String color, int original) {
        List<Integer> safeValues = new ArrayList<>();
        for (int v = 1; v <= 6; v++) {
            if (v == original) {
                continue;
            }
            if (rollWouldPassPlayer(color, v)) {
                continue;
            }
            if (rollLandsOnProtectedAdmin(color, v)) {
                continue;
            }
            safeValues.add(v);
        }
        if (safeValues.isEmpty()) {
            List<Integer> noPass = new ArrayList<>();
            for (int v = 1; v <= 6; v++) {
                if (v != original && !rollWouldPassPlayer(color, v)) {
                    noPass.add(v);
                }
            }
            if (!noPass.isEmpty()) {
                return noPass.get((int) (Math.random() * noPass.size()));
            }
            return (original == 6) ? 5 : original + 1;
        }
        return safeValues.get((int) (Math.random() * safeValues.size()));
    }

    /**
     * An opponent roll may never LAND exactly on a protected admin token's
     * square: the kill is blocked by rule, so the landing itself is blocked
     * too — otherwise the enemy would visually stack on top of the admin.
     */
    private boolean rollLandsOnProtectedAdmin(String color, int v) {
        if (!smartDiceOwnerLocked || smartDiceOwnerColor == null) {
            return false;
        }
        if (Objects.equals(color, smartDiceOwnerColor)) {
            return false;
        }
        for (Piece mine : getPiecesByColor(color)) {
            if (!mine.isAlive || mine.hasCompletedItsPurpose) {
                continue;
            }
            int destination = getSmartDestination(mine, v);
            if (destination < 0 || destination >= 52) {
                continue;
            }
            for (Piece adminPiece : getPiecesByColor(smartDiceOwnerColor)) {
                if (adminPiece.isAlive && adminPiece.currBlock == destination
                        && isProtectedAdminPiece(adminPiece)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if rolling value v would let the given color FINISH their
     * final remaining token (i.e. land on the true final cell, step 57) —
     * used by the admin-finishes-1st rule.
     *
     * NOTE: original code only matched `numberOfSteps + v == 56` (one short
     * of finish), so an opponent rolling the EXACT value to land on step 57
     * (the real final winner cell) was NOT rerouted — opponents could still
     * finish before the admin via the perfect roll. Now we match `== 57` so
     * the admin-finishes-first guarantee actually holds.
     */
    private boolean rollWouldPassPlayer(String color, int v) {
        int pantaValue = (gametype == 3) ? 1 : 4;
        int completed = 0;
        boolean completesNow = false;
        for (Piece p : getPiecesByColor(color)) {
            if (p.hasCompletedItsPurpose) {
                completed++;
                continue;
            }
            // Lands exactly on the final winner cell (step 57). This is the
            // "finish" trigger, not step 56.
            if (p.isAlive && (p.numberOfSteps + v) == 57) {
                completesNow = true;
            }
        }
        if (!completesNow) {
            return false;
        }
        return completed >= pantaValue - 1;
    }

    class Dice
    {
        ImageView diceImgView,tLeftImgColor,tRightImgColor,bLeftImgColor,bRightImgColor;
        AnimationDrawable diceAnimationDrawable;

        Handler diceHandler;
        boolean isRolling, isDiceClickable;

        ConstraintLayout tLeftLayout,tRightLayout,bLeftLayout,bRightLayout;

        String mainColor;

        int player = 0,numberOfPlayers;
        Dice(ImageView diceImgView,int nop,String color)
        {
            this.diceImgView = diceImgView;
            this.numberOfPlayers = nop;
            diceAnimationDrawable = new AnimationDrawable();
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0001,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0002,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0003,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0004,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0005,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0006,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0007,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0008,null),50);

            tLeftLayout = findViewById(R.id.tleftdicebg);
            tRightLayout = findViewById(R.id.trightdicebg);
            bLeftLayout = findViewById(R.id.bleftdicebg);
            bRightLayout = findViewById(R.id.brightdicebg);

            tLeftImgColor = findViewById(R.id.tleftpieceimage);
            tRightImgColor = findViewById(R.id.trightpieceimage);
            bLeftImgColor = findViewById(R.id.bleftpieceimage);
            bRightImgColor = findViewById(R.id.brightpieceimage);

            mainColor = color;

            diceHandler = new Handler(Looper.getMainLooper());
            isRolling = false;
            isDiceClickable = true;

            diceImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isRolling && (isDiceClickable || isDiceMovableExtraChance)) {
                        hintArrow.setVisibility(GONE);
                        roll();
                    }
                }
            });

            smartDiceLongPressAction = () -> {
                if (!diceImgView.isShown() || isRolling) {
                    return;
                }

                // Route based on which View received the ACTION_DOWN. The
                // central dice overlaps a corner pad by geometry (moveDice
                // constrains it to the current player's corner dicebg), so
                // routing purely by findPressedCornerPosition would ALWAYS
                // return the current player's corner — meaning every
                // central-dice tap silently re-assigned the admin to the
                // current player. Now: central-dice long-press binds/toggles
                // the CURRENT human player; corner-pad long-press binds the
                // human whose corner was pressed (the first press wins, see
                // activateSmartDiceForCorner).
                if (longPressSourceIsCentralDice) {
                    // Central dice long-press path.
                    if (isCurrentPlayerBindable()) {
                        // Current player is a human — toggle or assign admin
                        // to the current player (same rule as corner pads).
                        Player currentPlayerHuman = null;
                        for (Player player : players) {
                            if (Objects.equals(player.getColor(), currentPlayerColor)) {
                                currentPlayerHuman = player;
                                break;
                            }
                        }
                        if (currentPlayerHuman == null) { return; }

                        boolean sameAdmin = smartDiceOwnerLocked
                                && smartDiceOwnerPlayerIndex == currentPlayerSelectedIndex
                                && Objects.equals(smartDiceOwnerColor, currentPlayerColor);
                        if (sameAdmin) {
                            boolean wasEnabled = smartDiceEnabled;
                            smartDiceEnabled = !smartDiceEnabled;
                            showAdminToggleToast(currentPlayerHuman, smartDiceEnabled, wasEnabled);
                            return;
                        }
                        // First-time admin OR re-assign to current player.
                        smartDiceOwner = this;
                        smartDiceOwnerColor = currentPlayerColor;
                        smartDiceOwnerPlayerIndex = currentPlayerSelectedIndex;
                        smartDiceEnabled = true;
                        smartDiceOwnerLocked = true;
                        smartRecentRollsIndex = 0;
                        smartRecentRollsFilled = 0;
                        smartSixStreak = 0;
                        smartLastRollValue = 0;
                        smartSecondLastRollValue = 0;
                        smartRollsSinceLastSix = 0;
                        smartQuotaBlockRolls = 0;
                        smartQuotaBlockSixes = 0;
                        showAdminToast(currentPlayerHuman);
                        return;
                    }
                    // Current player is a bot — fall through to corner-pad
                    // path so the user can still activate admin on the bot's
                    // corner pad by long-pressing the central dice (which is
                    // positioned over that bot's corner).
                }

                // Corner-pad long-press path — figure out which corner.
                int pressedCorner = findPressedCornerPosition(smartLastTouchRawX, smartLastTouchRawY);
                if (pressedCorner > 0) {
                    activateSmartDiceForCorner(pressedCorner);
                    return;
                }
            };

            diceImgView.setOnTouchListener(new View.OnTouchListener() {
                private boolean longPressTriggered;

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            longPressTriggered = false;
                            smartLastTouchRawX = event.getRawX();
                            smartLastTouchRawY = event.getRawY();
                            longPressSourceIsCentralDice = true; // ← mark source
                            // Cancel any previous pending long-press using the
                            // STORED reference (was previously a different inline
                            // lambda — could never be cancelled — root cause
                            // of the silent admin re-assignment bug).
                            if (pendingSmartDiceLongPress != null) {
                                smartDiceHandler.removeCallbacks(pendingSmartDiceLongPress);
                            }
                            pendingSmartDiceLongPress = () -> {
                                longPressTriggered = true;
                                smartDiceLongPressAction.run();
                                pendingSmartDiceLongPress = null;
                            };
                            smartDiceHandler.postDelayed(pendingSmartDiceLongPress, SMART_DICE_LONG_PRESS_MS);
                            return false;
                        case MotionEvent.ACTION_UP:
                            if (pendingSmartDiceLongPress != null) {
                                smartDiceHandler.removeCallbacks(pendingSmartDiceLongPress);
                                pendingSmartDiceLongPress = null;
                            }
                            return longPressTriggered;
                        case MotionEvent.ACTION_CANCEL:
                            if (pendingSmartDiceLongPress != null) {
                                smartDiceHandler.removeCallbacks(pendingSmartDiceLongPress);
                                pendingSmartDiceLongPress = null;
                            }
                            longPressTriggered = false;
                            return false;
                        default:
                            return false;
                    }
                }
            });

            // Every corner dice pad listens for the 5s long press too, so the
            // owner can activate smart dice on their own color at any moment,
            // even while another player's turn is on screen.
            attachSmartCornerLongPress(tLeftLayout);
            attachSmartCornerLongPress(tRightLayout);
            attachSmartCornerLongPress(bLeftLayout);
            attachSmartCornerLongPress(bRightLayout);

            if(nop==2) {
                menuremoveplayersbtn.setVisibility(GONE);
                tRightLayout.setVisibility(View.VISIBLE);
                bLeftLayout.setVisibility(View.VISIBLE);
                tLeftLayout.setVisibility(GONE);
                bRightLayout.setVisibility(GONE);

                switch (mainColor) {
                    case "red":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("red"));
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("yellow"));
                        break;
                    case "green":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("green"));
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("blue"));
                        break;
                    case "blue":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue"));
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("green"));
                        break;
                    case "yellow":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow"));
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("red"));
                        break;
                }
            }
            else if(nop == 3) {
                rmp4bg.setAlpha(0.5f);
                rmpp4removeicon.setOnClickListener(null);
                tRightLayout.setVisibility(View.VISIBLE);
                bLeftLayout.setVisibility(View.VISIBLE);
                tLeftLayout.setVisibility(View.VISIBLE);
                bRightLayout.setVisibility(GONE);
                switch (mainColor) {
                    case "red":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("green")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //1
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //2
                        break;
                    case "green":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //2
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //3
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("red")); //1

                        break;
                    case "blue":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("red")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("green")); //2
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //1
                        break;
                    case "yellow":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //2
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("red")); //1
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("green")); //3
                        break;
                }
            }
            else {
                tRightLayout.setVisibility(View.VISIBLE);
                bLeftLayout.setVisibility(View.VISIBLE);
                tLeftLayout.setVisibility(View.VISIBLE);
                bRightLayout.setVisibility(View.VISIBLE);
                switch (mainColor) {
                    case "red":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("red")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("green")); //2
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //1
                        bRightImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //4
                        break;
                    case "green":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("green")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //1
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //2
                        bRightImgColor.setImageDrawable(getPieceDrawableByColor("red")); //4
                        break;
                    case "blue":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("red")); //2
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("green")); //1
                        bRightImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //4
                        break;
                    case "yellow":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //2
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //1
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("red")); //3
                        bRightImgColor.setImageDrawable(getPieceDrawableByColor("green")); //4
                        break;
                }
            }
        }

        /**
         * Long press on a corner dice pad: the FIRST human to long-press
         * in a match becomes the admin for the entire match. Pressing the
         * SAME admin's pad again toggles the assist off/on. Pressing any
         * OTHER human's pad is silently ignored.
         */
        void attachSmartCornerLongPress(View cornerBox) {
            cornerBox.setOnTouchListener(new View.OnTouchListener() {
                private boolean longPressTriggered;

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            longPressTriggered = false;
                            smartLastTouchRawX = event.getRawX();
                            smartLastTouchRawY = event.getRawY();
                            longPressSourceIsCentralDice = false; // ← it's a corner pad
                            if (pendingSmartDiceLongPress != null) {
                                smartDiceHandler.removeCallbacks(pendingSmartDiceLongPress);
                            }
                            pendingSmartDiceLongPress = () -> {
                                longPressTriggered = true;
                                smartDiceLongPressAction.run();
                                pendingSmartDiceLongPress = null;
                            };
                            smartDiceHandler.postDelayed(pendingSmartDiceLongPress, SMART_DICE_LONG_PRESS_MS);
                            return false;
                        case MotionEvent.ACTION_UP:
                            if (pendingSmartDiceLongPress != null) {
                                smartDiceHandler.removeCallbacks(pendingSmartDiceLongPress);
                                pendingSmartDiceLongPress = null;
                            }
                            return longPressTriggered;
                        case MotionEvent.ACTION_CANCEL:
                            if (pendingSmartDiceLongPress != null) {
                                smartDiceHandler.removeCallbacks(pendingSmartDiceLongPress);
                                pendingSmartDiceLongPress = null;
                            }
                            longPressTriggered = false;
                            return false;
                        default:
                            return false;
                    }
                }
            });
        }

        /**
         * Maps the finger position to a corner dice pad (1 top-left,
         * 2 top-right, 3 bottom-left, 4 bottom-right). Returns -1 when the
         * gesture did not land on any visible corner pad.
         */
        int findPressedCornerPosition(float rawX, float rawY) {
            View[] cornerBoxes = {tLeftLayout, tRightLayout, bLeftLayout, bRightLayout};
            int[] cornerPositions = {1, 2, 3, 4};
            for (int i = 0; i < cornerBoxes.length; i++) {
                View box = cornerBoxes[i];
                if (box == null || box.getVisibility() != View.VISIBLE || box.getWidth() == 0) {
                    continue;
                }
                Rect area = new Rect();
                box.getGlobalVisibleRect(area);
                if (area.contains((int) rawX, (int) rawY)) {
                    return cornerPositions[i];
                }
            }
            return -1;
        }

        void activateSmartDiceForCorner(int cornerPosition) {
            Player cornerPlayer = null;
            for (Player player : players) {
                if (player.getPosition() == cornerPosition) {
                    cornerPlayer = player;
                    break;
                }
            }
            if (cornerPlayer == null) {
                return;
            }
            // NOTE: Bot pads ARE eligible for admin assignment per the user's
            // spec — "kisi bhi blue green red yellow kisi pe bhi on kr saku"
            // means "I should be able to activate admin on ANY color pad".
            // The previous isBot check was the reason admin only activated
            // on Blue (the only human) in vs-Computer mode.

            // RULE (per spec): the FIRST long-press in a match chooses the
            // admin color. Pressing a DIFFERENT color's pad later RE-ASSIGNS
            // the admin to that color (the user wants freedom to switch the
            // admin color at any time). Pressing the SAME admin's pad
            // toggles the assist off/on.
            boolean sameAdmin = smartDiceOwnerLocked
                    && smartDiceOwnerPlayerIndex == cornerPlayer.getIndex()
                    && Objects.equals(smartDiceOwnerColor, cornerPlayer.getColor());

            if (sameAdmin) {
                // Toggle assist on/off. Admin color does NOT change.
                boolean wasEnabled = smartDiceEnabled;
                smartDiceEnabled = !smartDiceEnabled;
                showAdminToggleToast(cornerPlayer, smartDiceEnabled, wasEnabled);
                return;
            }

            // Either first-time admin selection OR re-assignment to a new
            // color. In both cases the admin becomes the pressed color.
            smartDiceOwner = this;
            smartDiceOwnerColor = cornerPlayer.getColor();
            smartDiceOwnerPlayerIndex = cornerPlayer.getIndex();
            smartDiceEnabled = true;
            smartDiceOwnerLocked = true;

            // Fresh quota window for the (new) admin.
            smartRecentRollsIndex = 0;
            smartRecentRollsFilled = 0;
            smartSixStreak = 0;
            smartLastRollValue = 0;
            smartSecondLastRollValue = 0;
            smartRollsSinceLastSix = 0;
            smartQuotaBlockRolls = 0;
            smartQuotaBlockSixes = 0;

            showAdminToast(cornerPlayer);
        }

        private void showAdminToast(Player admin) {
            // Toast removed — opponents could see "Admin: <name> (RED)" and
            // realize the hack is on. Silent activation is much safer.
            // Instead, give the user a haptic feedback (vibration) so they
            // KNOW the admin activated — silent for opponents, felt by user.
            vibrateAdminActivated();
        }

        private void showAdminToggleToast(Player admin, boolean nowEnabled, boolean wasEnabled) {
            // Toast removed — same reason. The admin toggle is now silent.
            // Different vibration pattern for ON vs OFF so the user knows.
            if (nowEnabled != wasEnabled) {
                if (nowEnabled) {
                    vibrateAdminActivated();  // single short pulse
                } else {
                    vibrateAdminToggledOff();  // double short pulse
                }
            }
        }

        // Haptic feedback for admin activation. Only the user feels this —
        // opponents watching the screen see nothing.
        private void vibrateAdminActivated() {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    VibratorManager vm = (VibratorManager) MainActivity.this.getSystemService(MainActivity.VIBRATOR_MANAGER_SERVICE);
                    if (vm != null && vm.getDefaultVibrator() != null) {
                        vm.getDefaultVibrator().vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                } else {
                    Vibrator v = (Vibrator) MainActivity.this.getSystemService(MainActivity.VIBRATOR_SERVICE);
                    if (v != null && v.hasVibrator()) {
                        v.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                }
            } catch (Exception ignored) {}
        }

        private void vibrateAdminToggledOff() {
            try {
                long[] pattern = {0, 40, 60, 40};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    VibratorManager vm = (VibratorManager) MainActivity.this.getSystemService(MainActivity.VIBRATOR_MANAGER_SERVICE);
                    if (vm != null && vm.getDefaultVibrator() != null) {
                        vm.getDefaultVibrator().vibrate(VibrationEffect.createWaveform(pattern, -1));
                    }
                } else {
                    Vibrator v = (Vibrator) MainActivity.this.getSystemService(MainActivity.VIBRATOR_SERVICE);
                    if (v != null && v.hasVibrator()) {
                        v.vibrate(VibrationEffect.createWaveform(pattern, -1));
                    }
                }
            } catch (Exception ignored) {}
        }

        boolean isCurrentPlayerBindable() {
            for (Player player : players) {
                if (Objects.equals(player.getColor(), currentPlayerColor)) {
                    return !player.isBot;
                }
            }
            return false;
        }

        void roll() {
            isRolling = true;
            isDiceClickable = false;
            isDiceMovableExtraChance = false;
            if(isSoundOn && diceRollSound != null) {
                // Restart the dice roll sound on EVERY roll. The previous
                // code checked 'isPlaying()' and skipped the restart if the
                // sound was still going — but the dice roll sound is ~470ms
                // long and the dice animation is only 350ms, so consecutive
                // rolls (e.g. in a fast bot sequence) were silent. Forcibly
                // pause+seekTo+start so EVERY roll has its sound (matching
                // the real Ludo King app).
                try {
                    if (diceRollSound.isPlaying()) {
                        diceRollSound.pause();
                    }
                    diceRollSound.seekTo(5);
                    diceRollSound.start();
                } catch (Exception ignored) {}
            }
            mainDiceImageView.setImageDrawable(diceAnimationDrawable);
            diceAnimationDrawable.setOneShot(true);
            diceAnimationDrawable.start();
            diceHandler.postDelayed(() -> {
                // Everyone rolls pure random. Only the smart-dice owner gets the
                // subtle assist below; all other players stay completely natural.
                int ch = 1 + (int) (Math.random() * 6);
                if (isSmartDiceRollActive(this)) {
                    ch = chooseSmartDiceValue();
                }

                // Admin-first rule: while the smart-dice owner has not finished,
                // an opponent's roll can never pass their FINAL token and never
                // LAND on a protected admin token (no stacking on the admin).
                // IMPORTANT: only reroll when the rolled value is actually
                // unsafe — natural (safe) rolls always stay untouched, so
                // opponents keep their normal dice and can cut the owner.
                if (adminFinalPassBlockApplies()
                        && (rollWouldPassPlayer(currentPlayerColor, ch)
                            || rollLandsOnProtectedAdmin(currentPlayerColor, ch))) {
                    ch = nonFinishingRollValue(currentPlayerColor, ch);
                }

                // Apply the per-face fairness engine (real Ludo King's
                // DiceController has maxLimit_1..6 + counterToGetSix +
                // p2Blank/p4Blank/etc. — modeled here). This runs for
                // EVERY roll (admin, non-admin, bot, human) and:
                //   • forces a 6 when the current player has gone ≥5
                //     rolls without one and a 6 is playable,
                //   • caps each face at 4 occurrences per 18-roll window,
                //     preventing any face from dominating (which fixes
                //     the "green went half the match without a 6" bug).
                ch = applyFairnessToRoll(ch);

                switch (ch) {
                    case 1:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice1, null));
                        break;
                    case 2:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice2, null));
                        break;
                    case 3:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice3, null));
                        break;
                    case 4:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice4, null));
                        break;
                    case 5:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice5, null));
                        break;
                    case 6:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice6, null));
                        break;
                }
                isRolling = false;

                currentPlayerDice = ch;

                // Real-ludo three-six rule (every player): the third consecutive
                // six voids the turn — the dice shows 6 but no move happens and
                // play passes to the next player. The smart-dice owner can never
                // even reach this because the roll engine caps six streaks first.
                if (ch == 6) {
                    tableConsecutiveSixes++;
                } else {
                    tableConsecutiveSixes = 0;
                }
                if (tableConsecutiveSixes >= 3) {
                    tableConsecutiveSixes = 0;
                    diceHandler.postDelayed(() -> {
                        switchPlayers();
                        isDiceClickable = true;
                    }, 600);
                    return;
                }

                List<Piece> pieces = getPiecesByColor(currentPlayerColor);
                int x;
                if(currentPlayerIndex==0) { x=players.size()-1; } else { x=currentPlayerIndex-1; }
                Player currentPlayer = players.get(x);
                //Toast.makeText(MainActivity.this, currentPlayer.color+"", Toast.LENGTH_SHORT).show();

                 int chances = 0;
                 int autoMoveBlock = -1;

                 List<Piece> movablePieces = new ArrayList<>();

                for(Piece p : pieces)
                {
                    if(p.check(ch))
                    {
                        if(chances==0) {
                            autoMoveBlock = p.currBlock;
                            autoMove = p;
                        }
                        else if(autoMoveBlock!=-1 && autoMoveBlock==p.currBlock) {

                        }
                        else {
                            autoMove = null;
                        }
                        if(currentPlayer.isBot) { movablePieces.add(p); }
                        ++chances;
                    }
                }

                currentPlayer.chances = chances;

                if(autoMove!=null) {

                    if(autoMove.isAlive && autoMove.isClickable) {
                        autoMove.diceValue = currentPlayerDice;
                        currentPlayerDice = -1;
                        for(Piece p : pieces) { p.inactiveState(); }
                        checkAdjustments(autoMove.currBlock);
                        autoMove.move(ch);
                        autoMove=null;
                        //Toast.makeText(MainActivity.this, x+"", Toast.LENGTH_SHORT).show();
                    }
                    else if(!autoMove.isAlive && currentPlayerDice==6) {
                        currentPlayerDice = -1;
                        for(Piece p : pieces) { p.inactiveState(); }
                        autoMove.makeAlive();
                    }
                } else if(currentPlayer.isBot && chances>0) {
                    Piece bestMovablePiece = movablePieces.get(0);
                    int bestKillingTargetSteps = 0;
                    int safetomovepoints = 15;
                    int pantapriority=0;
                    int maxprioritytoputinsafeplace = 0;
                    int safePoints = 0;
                    int y = currentPlayerIndex;

                    // choosing one with max steps blindly for faster panta minimum priority
                    int maxsteps = 0;
                    for(Piece p : movablePieces) {
                        if(p.isAlive && p.numberOfSteps>=maxsteps && !p.hasCompletedItsPurpose) {
                            maxsteps = p.numberOfSteps;
                            bestMovablePiece = p;
                        }
                    }

                    // making pieces alive minimum+0.5 priority
                    if(ch==6) {
                    for(Piece p : pieces) {
                        if(!p.isAlive) {
                            bestMovablePiece = p;
                            break;
                        }
                    } }


                    // panta chesing purpose min+1 priority
                    for(Piece p : movablePieces) {
                        if(p.isReadyToEnterWinnerZone) {
                            int thispantapriority = p.currWinnerBlock+ch;
                            if(thispantapriority>pantapriority) {
                                pantapriority = thispantapriority;
                                bestMovablePiece = p;
                            }
                        }
                    }

                    // moving to safe spots medium
                    for(Piece p : movablePieces) {
                        if(safeSpots.contains(p.currBlock+ch) && p.isAlive)
                        {
                            if(p.numberOfSteps>maxprioritytoputinsafeplace) {
                                maxprioritytoputinsafeplace = p.numberOfSteps;
                                bestMovablePiece = p;
                            }
                        }
                    }


                    // not going ahead for risky moves & running away from dangerous places max-1 priority
                    y = x;
                    for (int i = 0; i < players.size(); i++) {
                        String colour = players.get(y).getColor();
                        if (!colour.equals(currentPlayerColor)) {
                            List<Piece> enemypieces = getPiecesByColor(colour);
                            for (Piece tp : enemypieces) {
                                if (tp.isAlive) {
                                    for (Piece mp : movablePieces) {
                                        if (mp.isAlive) {
                                            int dangerdiff = mp.currBlock - tp.currBlock;
                                            int safetypoints = 10;
                                            if (safeSpots.contains(mp.currBlock)) {
                                                safetypoints = 15;
                                            }
                                            /*if (mp.currBlock == tp.currBlock && safeSpots.contains(mp.currBlock)) {
                                                if(bestMovablePiece.numberOfSteps<=mp.numberOfSteps) { safetypoints = 15; } else { safetypoints = 9; }
                                            } else*/ if (mp.currBlock >= 0 && mp.currBlock <= 10 && (tp.currBlock >= 51 - (10 - mp.currBlock))) {
                                                if (tp.currBlock >= 51 - (10 - mp.currBlock)) {
                                                    safetypoints -= 10 - mp.currBlock;
                                                }
                                            } else if (tp.currBlock >= 0 && (tp.currBlock >= mp.currBlock - 10 && tp.currBlock < mp.currBlock)) {
                                                safetypoints -= mp.currBlock - tp.currBlock;
                                            } else if (dangerdiff > 10) {
                                                if (safeSpots.contains(mp.currBlock)) {
                                                    if (mp.numberOfSteps < bestMovablePiece.numberOfSteps) {
                                                        safetypoints = 9;
                                                    } else {
                                                        safetypoints = 15;
                                                    }
                                                }
                                            } else if (dangerdiff <= -10) {
                                                if (dangerdiff + ch <= 0) {
                                                    if (safeSpots.contains(mp.currBlock) && safeSpots.contains(tp.currBlock)) {
                                                        safetypoints = 15;
                                                    } else if (!safeSpots.contains(mp.currBlock) && !safeSpots.contains(tp.currBlock)) {
                                                        safetypoints = 5;
                                                    } else if (!safeSpots.contains(mp.currBlock) && safeSpots.contains(tp.currBlock)) {
                                                        safetypoints = 15;
                                                    } else if (safeSpots.contains(mp.currBlock) && !safeSpots.contains(tp.currBlock)) {
                                                        safetypoints = 9;
                                                    }
                                                }
                                            }
                                        if (safetypoints < safetomovepoints) {
                                            bestMovablePiece = mp;
                                            safetomovepoints = safetypoints;
                                        }
                                    }
                                }
                                }
                            }
                            if (y >= (players.size() - 1)) {
                                y = 0;
                            } else {
                                y++;
                            }
                        }
                    }

                    // killing the best piece max priority
                    y = x;
                    for (int i = 0; i < players.size(); i++) {
                        String color = players.get(y).getColor();
                        if (!color.equals(currentPlayerColor)) {
                            List<Piece> enemypieces = getPiecesByColor(color);
                            for (Piece tp : enemypieces) {
                                if (tp.isAlive && !safeSpots.contains(tp.currBlock)) {
                                    for (Piece mp : movablePieces) {
                                        if (tp.currBlock == (mp.currBlock+ch)) {
                                            if (tp.numberOfSteps > bestKillingTargetSteps) {
                                                bestKillingTargetSteps = tp.numberOfSteps;
                                                bestMovablePiece = mp;  // should not be commented at deployment stage
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (y >= (players.size() - 1)) {
                            y = 0;
                        } else {
                            y++;
                        }
                    }
                    //Toast.makeText(MainActivity.this, bestMovablePiece.colour+","+bestMovablePiece.startPosition, Toast.LENGTH_SHORT).show();
                    bestMovablePiece.onClickForBot();
                } else if (chances == 0) {
                        diceHandler.postDelayed(() -> {
                            switchPlayers();
                            isDiceClickable = true;
                        }, 500); // ORIGINAL 500ms — restored
                    }
            }, 350); // ORIGINAL 350ms — restored. The dice roll animation is
                    // 8 frames × 50ms = 400ms; 350ms gives the user the full
                    // spinning feel before the result shows.
            }
    }

    private boolean isSmartDiceRollActive(Dice dice) {
        return smartDiceEnabled
                && smartDiceOwnerLocked
                && smartDiceOwner == dice
                && smartDiceOwnerPlayerIndex == currentPlayerSelectedIndex
                && Objects.equals(smartDiceOwnerColor, currentPlayerColor);
    }

    // --------------------------------------------------------------------
    // Per-face fairness engine — modeled on real Ludo King's DiceController
    // (maxLimit_1..6, counterToGetSix, curRollCount, p2Blank/p4Blank/etc.)
    // --------------------------------------------------------------------

    /** Records a rolled face in the rolling window (called for ALL rolls). */
    private void recordFairnessRoll(int face) {
        fairnessRecentFaces[fairnessRecentFacesIndex] = face;
        fairnessRecentFacesIndex = (fairnessRecentFacesIndex + 1) % fairnessRecentFaces.length;
        if (fairnessRecentFacesFilled < fairnessRecentFaces.length) {
            fairnessRecentFacesFilled++;
        }
    }

    /** Returns how many times `face` (1..6) has appeared in the rolling
     *  window. 0 if face is out of range. */
    private int fairnessCountFace(int face) {
        if (face < 1 || face > 6) { return 0; }
        int count = 0;
        for (int i = 0; i < fairnessRecentFacesFilled; i++) {
            if (fairnessRecentFaces[i] == face) { count++; }
        }
        return count;
    }

    /** Maps the current player's selectedIndex (1..4) to a playerRollsSinceLastSix /
     *  playerBlankRolls index 0..3. Defensive against nop=2 and other modes. */
    private int fairnessPlayerIndex() {
        int idx = currentPlayerSelectedIndex - 1;
        if (idx < 0 || idx >= playerRollsSinceLastSix.length) {
            // Fallback: use currentPlayerIndex (the list position)
            idx = (currentPlayerIndex >= 0 && currentPlayerIndex < playerRollsSinceLastSix.length)
                  ? currentPlayerIndex : 0;
        }
        return idx;
    }

    /**
     * Real-Ludo-King-style fairness adjustment applied to ALL rolls
     * (admin and non-admin, bot and human).
     *
     * Pipeline:
     *   1. Take the natural roll (or the admin-assisted value if smart dice
     *      is active).
     *   2. If the current player has gone >= PLAYER_FORCED_SIX_AFTER rolls
     *      without a 6 AND a 6 is playable (won't overshoot home track),
     *      override to 6.
     *   3. If the rolled face is over its FAIRNESS_FACE_MAX count in the
     *      rolling 18-roll window AND a different face can be played,
     *      re-pick a face under its limit.
     *   4. Record the result for next time.
     *
     * This eliminates the "green went half the match without a 6"
     * complaint: every player is guaranteed a 6 within roughly 5 rolls
     * when one is playable, and no face can dominate the dice.
     */
    private int applyFairnessToRoll(int naturalValue) {
        int pIdx = fairnessPlayerIndex();
        int ch = naturalValue;

        // Step 1: Forced-six rule (the "counterToGetSix" mechanism).
        // If the current player has gone PLAYER_FORCED_SIX_AFTER rolls
        // without a 6, and a 6 is actually playable for them right now
        // (either to bring a piece out of home or to move a piece without
        // overshooting), force the roll to 6.
        if (playerRollsSinceLastSix[pIdx] >= PLAYER_FORCED_SIX_AFTER && ch != 6) {
            if (isSixPlayableForCurrentPlayer()) {
                ch = 6;
            }
        }

        // Step 2: Per-face cap rule (the "maxLimit_N" mechanism).
        // If the rolled face is already at its cap in the rolling window,
        // try to swap to a different face that's under its cap AND is
        // playable. Only swap if the player has at least one playable
        // piece for the new face — otherwise leave the natural roll
        // (better to give an unplayable natural than an unplayable swap).
        if (fairnessCountFace(ch) >= FAIRNESS_FACE_MAX) {
            // Build a list of faces that are under their cap.
            List<Integer> underCapFaces = new ArrayList<>();
            for (int f = 1; f <= 6; f++) {
                if (f != ch && fairnessCountFace(f) < FAIRNESS_FACE_MAX) {
                    underCapFaces.add(f);
                }
            }
            // Among the under-cap faces, prefer ones that are playable.
            List<Integer> playableUnderCap = new ArrayList<>();
            for (int f : underCapFaces) {
                if (isFacePlayableForCurrentPlayer(f)) {
                    playableUnderCap.add(f);
                }
            }
            if (!playableUnderCap.isEmpty()) {
                ch = playableUnderCap.get((int) (Math.random() * playableUnderCap.size()));
            } else if (!underCapFaces.isEmpty()) {
                // No playable swap — but at least pick a face that's under
                // its cap (the player will skip their turn naturally).
                ch = underCapFaces.get((int) (Math.random() * underCapFaces.size()));
            }
            // If even underCapFaces is empty (all faces at cap), leave ch as-is.
        }

        // Step 3: Record the final face.
        recordFairnessRoll(ch);
        // Update per-player counters based on the final value.
        if (ch == 6) {
            playerRollsSinceLastSix[pIdx] = 0;
        } else {
            playerRollsSinceLastSix[pIdx]++;
        }
        return ch;
    }

    /** Returns true if a 6 is playable for the current player right now:
     *  either they have a piece in home (needs a 6 to come out) or they
     *  have a piece on the track that won't overshoot (numberOfSteps + 6 <= 57). */
    private boolean isSixPlayableForCurrentPlayer() {
        List<Piece> pieces = getPiecesByColor(currentPlayerColor);
        if (pieces == null || pieces.isEmpty()) { return false; }
        for (Piece p : pieces) {
            if (canMoveWithDice(p, 6)) { return true; }
        }
        return false;
    }

    /** Returns true if a given face is playable for the current player right now. */
    private boolean isFacePlayableForCurrentPlayer(int face) {
        List<Piece> pieces = getPiecesByColor(currentPlayerColor);
        if (pieces == null || pieces.isEmpty()) { return false; }
        for (Piece p : pieces) {
            if (canMoveWithDice(p, face)) { return true; }
        }
        return false;
    }

    // Resets the fairness state for a new match. Called from initAdminOwner.
    private void resetFairnessState() {
        for (int i = 0; i < fairnessRecentFaces.length; i++) { fairnessRecentFaces[i] = 0; }
        fairnessRecentFacesIndex = 0;
        fairnessRecentFacesFilled = 0;
        for (int i = 0; i < playerRollsSinceLastSix.length; i++) { playerRollsSinceLastSix[i] = 0; }
        for (int i = 0; i < playerBlankRolls.length; i++) { playerBlankRolls[i] = 0; }
    }

    private int chooseSmartDiceValue() {
        int baseline = 1 + (int) (Math.random() * 6);
        List<Piece> pieces = getPiecesByColor(currentPlayerColor);
        if (pieces == null || pieces.isEmpty()) {
            return finalizeSmartDiceValue(baseline);
        }

        // Collect only the genuinely useful rolls for THIS exact position.
        List<Integer> captureValues = new ArrayList<>();
        List<Integer> rescueValues = new ArrayList<>();
        List<Integer> finishValues = new ArrayList<>();
        List<Integer> safeValues = new ArrayList<>();
        boolean canBringPieceOutOfHome = false;
        boolean sixPlayable = false;

        for (int diceValue = 1; diceValue <= 6; diceValue++) {
            for (Piece piece : pieces) {
                if (!canMoveWithDice(piece, diceValue)) {
                    continue;
                }
                if (diceValue == 6) {
                    sixPlayable = true;
                }
                int destination = getSmartDestination(piece, diceValue);
                if (destination < 0) {
                    continue;
                }
                if (!captureValues.contains(diceValue)
                        && countSmartCaptures(destination) > 0) {
                    captureValues.add(diceValue);
                }
                if (!rescueValues.contains(diceValue) && piece.isAlive
                        && countSmartThreats(piece.currBlock) > 0
                        && countSmartThreats(destination) == 0) {
                    rescueValues.add(diceValue);
                }
                if (!finishValues.contains(diceValue) && piece.isAlive) {
                    int progressed = piece.numberOfSteps + diceValue;
                    if (progressed >= 52 && progressed < 57) {
                        finishValues.add(diceValue);
                    }
                }
                if (!safeValues.contains(diceValue) && destination < 52
                        && safeSpots.contains(destination)) {
                    safeValues.add(diceValue);
                }
                if (!piece.isAlive) {
                    canBringPieceOutOfHome = true;
                }
            }
        }

        // Subtle assist: only sometimes upgrade the natural roll, and each kind
        // of help has its own modest chance, so nothing feels systematic.
        // Quota: the owner still always sees roughly three sixes per twelve
        // rolls (spread out, never three in a row), landing on useful moments.
        int sixesInWindow = countRecentSixes();
        boolean sixAllowed = smartSixStreak < 2 && sixesInWindow < 4;
        boolean sixUseful = captureValues.contains(6) || rescueValues.contains(6)
                || finishValues.contains(6) || safeValues.contains(6)
                || canBringPieceOutOfHome;
        int needMin = 3 - sixesInWindow;

        Integer assisted = null;
        int quotaRemaining = 12 - smartQuotaBlockRolls;
        int quotaNeeded = 3 - smartQuotaBlockSixes;
        boolean quotaDeadline = sixPlayable && sixAllowed
                && quotaNeeded > 0 && quotaNeeded >= quotaRemaining;
        if (quotaDeadline || (sixPlayable && sixAllowed && smartRollsSinceLastSix >= 5)) {
            // Quota pace: never let the owner go long without a six.
            assisted = 6;
        } else if (sixPlayable && sixAllowed && needMin > 0 && sixUseful
                && smartRollsSinceLastSix >= 3 && smartChance(0.70)) {
            // Useful six while the quota is still pending.
            assisted = 6;
        } else if (!captureValues.isEmpty() && smartChance(0.70)) {
            assisted = captureValues.get(smartRandomIndex(captureValues.size()));
        } else if (!rescueValues.isEmpty() && smartChance(0.55)) {
            assisted = rescueValues.get(smartRandomIndex(rescueValues.size()));
        } else if (!finishValues.isEmpty() && smartChance(0.50)) {
            assisted = finishValues.get(smartRandomIndex(finishValues.size()));
        } else if (!safeValues.isEmpty() && smartChance(0.40)) {
            assisted = safeValues.get(smartRandomIndex(safeValues.size()));
        } else if (canBringPieceOutOfHome && smartChance(0.35)) {
            assisted = 6;
        }

        return finalizeSmartDiceValue(assisted != null ? assisted : baseline);
    }

    private boolean smartChance(double probability) {
        return Math.random() < probability;
    }

    private int smartRandomIndex(int size) {
        return size <= 1 ? 0 : (int) (Math.random() * size);
    }

    /**
     * Anti-pattern guard. Whatever the source of the roll (assist or plain
     * luck), it can never look systematic:
     *  - never a third six in a row (in real ludo that turn is voided anyway)
     *  - at most four sixes inside the last twelve owner rolls
     *  - never the same face three times in a row
     */
    private int finalizeSmartDiceValue(int value) {
        boolean sixBlocked = (smartSixStreak >= 2 || countRecentSixes() >= 4);
        int lo = 1;
        int hi = sixBlocked ? 5 : 6;

        int ch = value;
        if (ch < lo || ch > hi) {
            ch = lo + (int) (Math.random() * (hi - lo + 1));
        }

        if (ch == smartLastRollValue && ch == smartSecondLastRollValue) {
            int candidate = lo + (int) (Math.random() * (hi - lo + 1));
            int guard = 0;
            while (candidate == ch && guard++ < 12) {
                candidate = lo + (int) (Math.random() * (hi - lo + 1));
            }
            if (candidate == ch) {
                candidate = (ch == hi) ? lo : ch + 1;
            }
            ch = candidate;
        }

        smartRecentRolls[smartRecentRollsIndex] = ch;
        smartRecentRollsIndex = (smartRecentRollsIndex + 1) % smartRecentRolls.length;
        if (smartRecentRollsFilled < smartRecentRolls.length) {
            smartRecentRollsFilled++;
        }
        if (ch == 6) {
            smartSixStreak++;
            smartRollsSinceLastSix = 0;
            smartQuotaBlockSixes++;
        } else {
            smartSixStreak = 0;
            smartRollsSinceLastSix++;
        }
        smartQuotaBlockRolls++;
        if (smartQuotaBlockRolls >= 12) {
            smartQuotaBlockRolls = 0;
            smartQuotaBlockSixes = 0;
        }
        smartSecondLastRollValue = smartLastRollValue;
        smartLastRollValue = ch;
        return ch;
    }

    private int countRecentSixes() {
        int sixes = 0;
        for (int i = 0; i < smartRecentRollsFilled; i++) {
            if (smartRecentRolls[i] == 6) {
                sixes++;
            }
        }
        return sixes;
    }

    private boolean canMoveWithDice(Piece piece, int diceValue) {
        if (diceValue == 6) {
            return !piece.isAlive || (piece.numberOfSteps + diceValue) < 57;
        }
        return piece.isAlive
                && (piece.numberOfSteps + diceValue) < 57
                && !piece.hasCompletedItsPurpose
                && !piece.isThisPlayerWon;
    }

    private int getSmartDestination(Piece piece, int diceValue) {
        if (!piece.isAlive) {
            return piece.startPosition;
        }

        int destination = piece.currBlock;
        boolean inWinnerZone = piece.isReadyToEnterWinnerZone;
        int winnerBlockIndex = piece.currWinnerBlock;
        int[] winnerBlocks = piece.winnerBlocks;

        if (inWinnerZone && winnerBlocks == null) {
            winnerBlocks = getWinnerBlocks(piece.endPosition);
        }

        for (int step = 0; step < diceValue; step++) {
            if (inWinnerZone) {
                if (winnerBlocks == null || winnerBlockIndex >= winnerBlocks.length) {
                    return -1;
                }
                destination = winnerBlocks[winnerBlockIndex++];
            } else {
                if (destination < 0) {
                    destination = piece.startPosition;
                } else {
                    destination = destination >= 51 ? 0 : destination + 1;
                }
                if (destination == piece.endPosition) {
                    inWinnerZone = true;
                    winnerBlocks = getWinnerBlocks(piece.endPosition);
                    winnerBlockIndex = 0;
                }
            }
        }
        return destination;
    }

    private int countSmartCaptures(int destination) {
        if (destination < 0 || destination >= 52 || safeSpots.contains(destination)) {
            return 0;
        }

        int captures = 0;
        for (Player player : players) {
            if (isSmartOpponent(player.getColor())) {
                for (Piece enemy : getPiecesByColor(player.getColor())) {
                    if (enemy.isAlive && enemy.currBlock == destination) {
                        captures++;
                    }
                }
            }
        }
        return captures;
    }

    private int countSmartThreats(int destination) {
        if (destination < 0 || destination >= 52 || safeSpots.contains(destination)) {
            return 0;
        }

        int threats = 0;
        for (Player player : players) {
            if (!isSmartOpponent(player.getColor())) {
                continue;
            }
            for (Piece enemy : getPiecesByColor(player.getColor())) {
                if (!enemy.isAlive || enemy.currBlock < 0 || enemy.currBlock >= 52
                        || enemy.hasCompletedItsPurpose) {
                    continue;
                }
                int distance = (destination - enemy.currBlock + 52) % 52;
                if (distance >= 1 && distance <= 6
                        && enemy.numberOfSteps + distance < 57) {
                    threats++;
                }
            }
        }
        return threats;
    }

    private boolean isSmartOpponent(String color) {
        if (Objects.equals(color, currentPlayerColor)) {
            return false;
        }
        if (gametype != 2) {
            return true;
        }
        return !areSmartTeamMates(currentPlayerColor, color);
    }

    private boolean areSmartTeamMates(String firstColor, String secondColor) {
        return (("red".equals(firstColor) || "yellow".equals(firstColor))
                && ("red".equals(secondColor) || "yellow".equals(secondColor)))
                || (("blue".equals(firstColor) || "green".equals(firstColor))
                && ("blue".equals(secondColor) || "green".equals(secondColor)));
    }

    private Drawable getPieceDrawableByColor(String color) {
        switch (color) {
            case "red":
                if (normalPiece) {
                    return ResourcesCompat.getDrawable(getResources(), R.drawable.redpiece, null);
                } else {
                    return getStylishIconDrawableByColor(color);
                }
            case "green":
                if (normalPiece) {
                    return ResourcesCompat.getDrawable(getResources(), R.drawable.greenpiece, null);
                } else {
                    return getStylishIconDrawableByColor(color);
                }
            case "blue":
                if (normalPiece) {
                    return ResourcesCompat.getDrawable(getResources(), R.drawable.bluepiece, null);
                } else {
                    return getStylishIconDrawableByColor(color);
                }
            case "yellow":
                if (normalPiece) {
                    return ResourcesCompat.getDrawable(getResources(), R.drawable.yellowpiece, null);
                } else {
                    return getStylishIconDrawableByColor(color);
                }
        } return null;
    }

    private List<Piece> getPiecesByColor(String currentPlayerColor) {
        switch(currentPlayerColor)
        {
            case "red":
                return rp;
            case "green":
                return gp;
            case "blue":
                return bp;
            case "yellow":
                return yp;
        }
        return rp;
    }

    /**
     * CLICK-THROUGH BUG FIX:
     *
     * When pieces from different players share a cell, Android's view
     * dispatcher sends the click to whichever view has the highest
     * z-order at the tap point — which may be an opponent's piece.
     * The previous code silently swallowed those clicks, so the user
     * saw their piece "frozen" even though they were tapping it.
     *
     * This helper returns ONE of the current player's pieces that:
     *   • is on the same cell as `cellBlock` (or matches if cellBlock=-1)
     *   • is alive AND isClickable (i.e. movable right now)
     *   • is NOT a bot piece (only the human can click)
     *
     * Returns null if no current-player piece is clickable here.
     * Called from the piece click listener when an opponent's piece
     * receives the tap — the tap is redirected to OUR piece.
     */
    private Piece findCurrentPlayerPieceAtCell(int cellBlock) {
        if (currentPlayerColor == null || cellBlock == -1) { return null; }
        List<Piece> pieces = getPiecesByColor(currentPlayerColor);
        if (pieces == null || pieces.isEmpty()) { return null; }
        // First pass: prefer a piece that is both at this cell AND currently
        // clickable (i.e. highlighted by check()).
        for (Piece p : pieces) {
            if (p != null && !p.isBotPiece && p.isAlive
                    && p.isClickable && p.currBlock == cellBlock) {
                return p;
            }
        }
        // Second pass: if no clickable piece at this exact cell, return null
        // (the tap was on a piece that genuinely can't be moved).
        return null;
    }

    class Player {
        int position;
        String color;
        int index;
        String name;
        boolean isBot;
        TextView playerNameTextView;

        int chances = 0;

        public int getPosition() {
            return position;
        }

        public String getColor() {
            return color;
        }

        public int getDiceValue() {
            return diceValue;
        }

        int diceValue;

        public Player(int position, String color,String playerName,boolean isBot,int index) {
            this.position = position;
            this.color = color;
            this.name = playerName;
            this.index = index;
            this.isBot = isBot;
            playerNameTextView = getPlayerNameByPosition(this.position);
            if(this.isBot) {
                //name = "Bot";
                //playerNameTextView.setText(name);
            }
        }
        public int getIndex() {
            return index;
        }

        void setActive() {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(((int)(pxWidth*0.4)),((int)(pxWidth*0.4)));
            redHomeBlink.setLayoutParams(lp);

            //lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if(position == 1)
            {    lp.addRule(RelativeLayout.ALIGN_TOP,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_LEFT,R.id.imageView); }
            else if(position == 2)
            {    lp.addRule(RelativeLayout.ALIGN_TOP,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_RIGHT,R.id.imageView); }
            else if(position == 3)
            {    lp.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_LEFT,R.id.imageView);}
            else if(position == 4)
            {    lp.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_RIGHT,R.id.imageView);}

            redHomeBlink.setLayoutParams(lp);

            // Use the cached blink animation — was inflating the XML on every
            // turn change via AnimationUtils.loadAnimation(), which is slow.
            Animation blink = cachedBlinkAnimation;
            if (blink == null) {
                try { blink = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blinkanimation); }
                catch (Exception ignored) {}
            }
            if (blink != null) {
                try { blink.reset(); redHomeBlink.startAnimation(blink); }
                catch (Exception ignored) {}
            }
        }


    }

    private TextView getPlayerNameByPosition(int position) {
        switch (position)
        {
            case 1:
                return playername1;
            case 2:
                return playername2;
            case 3:
                return playername3;
            case 4:
                return playername4;
            default:
                return playername1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_main);
        applySystemBarInsets();
        //setContentView(new GameCanvas(this));
        Objects.requireNonNull(getSupportActionBar()).hide();

        initViews();
        registerBackNavigation();

        globalHandler = new Handler(Looper.getMainLooper());

        // Keep frequently animated surfaces on hardware-backed layers so
        // movement and dice transitions stay smooth on current devices.
        ludoBoard.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mainDiceImageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        hintArrow.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        redHomeBlink.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // Hint-arrow bounce — stored as a field so onDestroy can cancel it.
        // Previously this animator was a local and could never be cancelled,
        // which leaked the activity (Choreographer kept ticking it forever).
        hintArrowAnimator = ObjectAnimator.ofFloat(hintArrow, "translationX", -20, 20);
        hintArrowAnimator.setRepeatCount(ValueAnimator.INFINITE);
        hintArrowAnimator.setRepeatMode(ValueAnimator.REVERSE);
        hintArrowAnimator.setDuration(250);
        hintArrowAnimator.start();


        boolean isPLayer1Bot, isPLayer2Bot, isPLayer3Bot, isPLayer4Bot;

        Bundle extras = getIntent().getExtras();
        nop = extras.getInt("nop");
        String color = extras.getString("color");
        player1name = extras.getString("player1name");
        player2name = extras.getString("player2name");
        player3name = extras.getString("player3name");
        player4name = extras.getString("player4name");

        player1color = extras.getString("player1color");
        player2color = extras.getString("player2color");
        player3color = extras.getString("player3color");
        player4color = extras.getString("player4color");

        isPLayer1Bot = extras.getBoolean("player1bot");
        isPLayer2Bot = extras.getBoolean("player2bot");
        isPLayer3Bot = extras.getBoolean("player3bot");
        isPLayer4Bot = extras.getBoolean("player4bot");

        normalPiece = extras.getBoolean("normalPiece");

        gametype = extras.getInt("type"); // 1.classic , 2.teamup , 3.quick , 4.computer

        playername1.setText(player1name);
        playername2.setText(player2name);
        playername3.setText(player3name);
        playername4.setText(player4name);

        reddicevalue = greendicevalue = bluedicevalue = yellowdicevalue = 0;

        int length = dy.length;
        for (int i = 0; i < dy.length; i++) {
            x[i] = (float) ((pxWidth / 100) * dx[i]);
            y[i] = (float) ((pxWidth / 100) * dy[i]);
        }

        float total = onePercentWidth * 40;
        float homeCircleWidth = (float) ((total * 0.64) * 0.25);
        float pieceBlockSize = (float) (pxWidth * 0.065);
        float lifty = (float) (24.5 * displayMetrics.density);
        pieceWidth = 34 * displayMetrics.density;
        pieceHeight = 41 * displayMetrics.density;
        float pushx;
        if (homeCircleWidth >= pieceWidth) {
            pushx = (float) ((homeCircleWidth - pieceWidth) / 2);
        } else {
            pushx = (float) -((pieceWidth - homeCircleWidth) / 2);
        }
        if (pieceBlockSize >= pieceWidth) {
            pushXForPieces = (float) ((pieceBlockSize - pieceWidth) / 2);
        } else {
            pushXForPieces = (float) -((pieceWidth - pieceBlockSize) / 2);
        }
        pushYForPieces = lifty;
        if (!normalPiece) {
            pushx /= 1.5;
            lifty /= 3;
            pushXForPieces = pushx;
            pushYForPieces = lifty;
        }
        float base = (float) pxWidth - total;

        //

        crownIndex1.getLayoutParams().height = (int) total;
        crownIndex2.getLayoutParams().height = (int) total;
        crownIndex3.getLayoutParams().height = (int) total;
        crownIndex4.getLayoutParams().height = (int) total;

        p1exitbox.getLayoutParams().height = (int) (total * 0.985);
        p2exitbox.getLayoutParams().height = (int) (total * 0.985);
        p3exitbox.getLayoutParams().height = (int) (total * 0.985);
        p4exitbox.getLayoutParams().height = (int) (total * 0.985);
        p1exitbox.getLayoutParams().width = (int) (total * 0.985);
        p2exitbox.getLayoutParams().width = (int) (total * 0.985);
        p3exitbox.getLayoutParams().width = (int) (total * 0.985);
        p4exitbox.getLayoutParams().width = (int) (total * 0.985);

        if(gametype!=4) {
            playername1.getLayoutParams().width = (int) (total);
            playername1.getLayoutParams().height = (int) (total);
            playername1.setRotation(90);
            playername1.setGravity(Gravity.BOTTOM);
            playername1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ((ConstraintLayout.LayoutParams)playername1.getLayoutParams()).topMargin = (int) (30 * displayMetrics.density);

            playername2.setRotation(180);
            ((ConstraintLayout.LayoutParams)playername2.getLayoutParams()).topMargin =  (int) (30 * displayMetrics.density);

            playername4.getLayoutParams().width = (int) (total);
            playername4.getLayoutParams().height = (int) (total);
            playername4.setRotation(-90);
            playername4.setGravity(Gravity.BOTTOM);
            playername4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }


        pos1[0][0] = (float) (total * 0.18 + ((total * 0.64) * 0.12)) + pushx;
        pos1[0][1] = (float) (total * 0.18 + ((total * 0.64) * 0.12) - lifty);
        double corDist = total * 0.18 + ((total * 0.64) * 0.12) + ((total * 0.64) * 0.25);
        pos1[1][0] = (float) (total - corDist + pushx);
        pos1[1][1] = (float) pos1[0][1];
        pos1[2][0] = (float) (total * 0.18 + ((total * 0.64) * 0.12) + pushx);
        pos1[2][1] = (float) (total - corDist - lifty);
        pos1[3][0] = (float) pos1[1][0];
        pos1[3][1] = (float) (total - corDist - lifty);

        pos2[0][0] = (float) base + pos1[2][0];
        pos2[0][1] = (float) pos1[0][1];
        pos2[1][0] = (float) base + (pos1[1][0]);
        pos2[1][1] = (float) pos1[0][1];
        pos2[2][0] = (float) base + pos1[2][0];
        pos2[2][1] = (float) (total - corDist - lifty);
        pos2[3][0] = (float) base + (pos1[1][0]);
        pos2[3][1] = (float) (total - corDist - lifty);

        pos3[0][0] = (float) pos1[2][0];
        pos3[0][1] = (float) base + pos1[0][1];
        pos3[1][0] = (float) pos1[1][0];
        pos3[1][1] = (float) base + pos1[0][1];
        pos3[2][0] = (float) pos1[2][0];
        pos3[2][1] = (float) (base + total - corDist - lifty);
        pos3[3][0] = (float) pos1[1][0];
        pos3[3][1] = (float) (base + total - corDist - lifty);

        pos4[0][0] = (float) base + pos1[2][0];
        pos4[0][1] = (float) base + pos1[0][1];
        pos4[1][0] = (float) base + pos1[1][0];
        pos4[1][1] = (float) base + pos1[0][1];
        pos4[2][0] = (float) base + pos1[2][0];
        pos4[2][1] = (float) (base + total - corDist - lifty);
        pos4[3][0] = (float) base + pos1[1][0];
        pos4[3][1] = (float) (base + total - corDist - lifty);

        if (nop == 2) {
            winnerlistp3layout.setVisibility(GONE);
            winnerlistp4layout.setVisibility(GONE);
            wlistwinorlose2.setImageDrawable(getResources().getDrawable(R.drawable.loser1));
            player1name = player3name;
            player2name = player2name;
            switch (color) {
                case "red":
                    ludoBoard.setRotation(0);
                    createPieces("red", 3, player1name, false);
                    createPieces("yellow", 2, player2name, isPLayer2Bot);
                    break;
                case "blue":
                    ludoBoard.setRotation(90);
                    createPieces("blue", 3, player1name, false);
                    createPieces("green", 2, player2name, isPLayer2Bot);
                    break;
                case "green":
                    ludoBoard.setRotation(270);
                    createPieces("green", 3, player1name, false);
                    createPieces("blue", 2, player2name, isPLayer2Bot);
                    break;
                case "yellow":
                    ludoBoard.setRotation(180);
                    createPieces("yellow", 3, player1name, false);
                    createPieces("red", 2, player2name, isPLayer2Bot);
                    break;
            }
        } else if (nop == 3) {
            addPiecesToRmpLayout(color, "Player 4", 4);
            player4color = color;
            switch (color) {
                case "red":
                    ludoBoard.setRotation(270);
                    createPieces("green", 3, player1name, isPLayer1Bot);
                    player1color = "green";
                    createPieces("yellow", 1, player2name, isPLayer2Bot);
                    player2color = "yellow";
                    createPieces("blue", 2, player3name, isPLayer3Bot);
                    player3color = "blue";
                    break;
                case "green":
                    ludoBoard.setRotation(180);
                    createPieces("yellow", 3, player1name, isPLayer1Bot);
                    player1color = "yellow";
                    createPieces("blue", 1, player2name, isPLayer2Bot);
                    player2color = "blue";
                    createPieces("red", 2, player3name, isPLayer3Bot);
                    player3color = "red";
                    break;
                case "blue": // 0
                    ludoBoard.setRotation(0);
                    createPieces("red", 3, player1name, isPLayer1Bot);
                    player1color = "red";
                    createPieces("green", 1, player2name, isPLayer2Bot);
                    player2color = "green";
                    createPieces("yellow", 2, player3name, isPLayer3Bot);
                    player3color = "yellow";
                    break;
                case "yellow":// 90
                    ludoBoard.setRotation(90);
                    createPieces("blue", 3, player1name, isPLayer1Bot);
                    player1color = "blue";
                    createPieces("red", 1, player2name, isPLayer2Bot);
                    player2color = "red";
                    createPieces("green", 2, player3name, isPLayer3Bot);
                    player3color = "green";
                    break;
            }
        } else {
            //Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
            switch (color) {
                case "red":
                    ludoBoard.setRotation(0);
                    createPieces("red", 3, player1name, isPLayer1Bot);
                    player1color = "red";
                    createPieces("green", 1, player2name, isPLayer2Bot);
                    player2color = "green";
                    createPieces("yellow", 2, player3name, isPLayer3Bot);
                    player3color = "yellow";
                    createPieces("blue", 4, player4name, isPLayer4Bot);
                    player4color = "blue";
                    break;
                case "blue":
                    ludoBoard.setRotation(90);
                    createPieces("blue", 3, player1name, isPLayer1Bot);
                    player1color = "blue";
                    createPieces("red", 1, player2name, isPLayer2Bot);
                    player2color = "red";
                    createPieces("green", 2, player3name, isPLayer3Bot);
                    player3color = "green";
                    createPieces("yellow", 4, player4name, isPLayer4Bot);
                    player4color = "yellow";
                    break;
                case "green":
                    ludoBoard.setRotation(270);
                    createPieces("green", 3, player1name, isPLayer1Bot);
                    player1color = "green";
                    createPieces("yellow", 1, player2name, isPLayer2Bot);
                    player2color = "yellow";
                    createPieces("blue", 2, player3name, isPLayer3Bot);
                    player3color = "blue";
                    createPieces("red", 4, player4name, isPLayer4Bot);
                    player4color = "red";
                    break;
                case "yellow":
                    ludoBoard.setRotation(180);
                    createPieces("yellow", 3, player1name, isPLayer1Bot);
                    player1color = "yellow";
                    createPieces("blue", 1, player2name, isPLayer2Bot);
                    player2color = "blue";
                    createPieces("red", 2, player3name, isPLayer3Bot);
                    player3color = "red";
                    createPieces("green", 4, player4name, isPLayer4Bot);
                    player4color = "green";
                    break;
                default:
                    ludoBoard.setRotation(0);
                    createPieces("blue", 3, player4name, isPLayer4Bot);
                    player1color = "blue";
                    createPieces("red", 1, player1name, isPLayer1Bot);
                    player2color = "red";
                    createPieces("green", 2, player2name, isPLayer2Bot);
                    player3color = "green";
                    createPieces("yellow", 4, player3name, isPLayer3Bot);
                    player4color = "yellow";
            }
        }
        View.OnTouchListener clickEffect = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageView) v).setColorFilter(ResourcesCompat.getColor(getResources(), R.color.dim_color, null), PorterDuff.Mode.SRC_ATOP);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        ((ImageView) v).clearColorFilter();
                        break;
                }
                return false;
            }
        };

        ingamemenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingamemenuitemslayout.getVisibility() == View.VISIBLE) {
                    ingamemenuitemslayout.setVisibility(GONE);
                } else {
                    ingamemenuitemslayout.setVisibility(View.VISIBLE);
                }
            }
        });

        menuremoveplayersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingamemenuitemslayout.setVisibility(GONE);
                ingamermplayout.setScaleX(0.0f);
                ingamermplayout.setScaleY(0.0f);
                ingamermplayout.setVisibility(View.VISIBLE);
                ingamermplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ingamermplayout.animate().setListener(null);
                    }
                }).start();
            }
        });

        menuexitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingamemenuitemslayout.setVisibility(GONE);
                quitgamelayout.setScaleX(0.0f);
                quitgamelayout.setScaleY(0.0f);
                quitgamelayout.setVisibility(View.VISIBLE);
                quitgamelayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        quitgamelayout.animate().setListener(null);
                    }
                }).start();
            }
        });

        // exit layout views
        quitgamelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ingameyesbtn.setOnTouchListener(clickEffect);
        ingamenobtn.setOnTouchListener(clickEffect);
        ingamesoundbtn.setOnTouchListener(clickEffect);
        ingamemusicbtn.setOnTouchListener(clickEffect);

        ingameyesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isQuitConfirmed = true;
                isGameSessionActive = false;
                clearSavedGameSnapshot();
                stopEverything();
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }
        });

        ingamenobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitgamelayout.animate().scaleX(0.0f).scaleY(0.0f).setInterpolator(new AnticipateInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        quitgamelayout.setVisibility(GONE);
                    }
                }).start();
            }
        });

        ingamesoundbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSoundOn) {
                    isSoundOn = false;
                    sharedPreferences.edit().putBoolean("sound",false).apply();
                    ingamesoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundoff,null));
                    if(congratulationSound!=null) { if(congratulationSound.isPlaying()) { congratulationSound.pause(); } }
                } else {
                    isSoundOn = true;
                    sharedPreferences.edit().putBoolean("sound",true).apply();
                    ingamesoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundon,null));
                    if(congratulationSound!=null) { if(!congratulationSound.isPlaying()) { congratulationSound.start(); } }
                }
            }
        });

        ingamemusicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMusicOn) {
                    isMusicOn = false;
                    sharedPreferences.edit().putBoolean("music",false).apply();
                    ingamemusicbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.musicoff,null));
                } else {
                    isMusicOn = true;
                    sharedPreferences.edit().putBoolean("music",true).apply();
                    ingamemusicbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.musicon,null));
                }
            }
        });

        // rmplayout views
        rmpbackbtn.setOnTouchListener(clickEffect);
        rmpmenubtn.setOnTouchListener(clickEffect);
        ingamermplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        confirmrmplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        String finalPlayer1color = player1color;
        rmpp1removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.setScaleX(0.0f);
                confirmrmplayout.setScaleY(0.0f);
                confirmrmplayout.setVisibility(View.VISIBLE);
                confirmrmplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.animate().setListener(null);
                    }
                }).start();
                selectedconfirmrmpplayerindex = 1;
                selectedrmppiece.setImageDrawable(getPieceDrawableByColor(finalPlayer1color));
            }
        });

        String finalPlayer2color = player2color;
        rmpp2removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.setScaleX(0.0f);
                confirmrmplayout.setScaleY(0.0f);
                confirmrmplayout.setVisibility(View.VISIBLE);
                confirmrmplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.animate().setListener(null);
                    }
                }).start();
                selectedconfirmrmpplayerindex = 2;

                selectedrmppiece.setImageDrawable(getPieceDrawableByColor(finalPlayer2color));
            }
        });

        String finalPlayer3color = player3color;
        rmpp3removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.setScaleX(0.0f);
                confirmrmplayout.setScaleY(0.0f);
                confirmrmplayout.setVisibility(View.VISIBLE);
                confirmrmplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.animate().setListener(null);
                    }
                }).start();
                selectedconfirmrmpplayerindex = 3;
                selectedrmppiece.setImageDrawable(getPieceDrawableByColor(finalPlayer3color));
            }
        });

        String finalPlayer4color = player4color;
        rmpp4removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.setScaleX(0.0f);
                confirmrmplayout.setScaleY(0.0f);
                confirmrmplayout.setVisibility(View.VISIBLE);
                confirmrmplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.animate().setListener(null);
                    }
                }).start();
                selectedconfirmrmpplayerindex = 4;
                selectedrmppiece.setImageDrawable(getPieceDrawableByColor(finalPlayer4color));
            }
        });

        if (nop == 3) {
            rmp4bg.setAlpha(0.5f);
            rmpp4removeicon.setOnClickListener(null);
        }

        rmpbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingamermplayout.animate().scaleX(0.0f).scaleY(0.0f).setInterpolator(new AnticipateInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ingamermplayout.setVisibility(GONE);
                    }
                }).start();
            }
        });

        rmpmenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuexitbtn.callOnClick();
            }
        });

        // confirm rmp layout views
        confirmrmpyesbtn.setOnTouchListener(clickEffect);
        confirmrmpnobtn.setOnTouchListener(clickEffect);
        confirmrmpyesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeThisPlayerByIndex(selectedconfirmrmpplayerindex);
                confirmrmplayout.setVisibility(GONE);
                ingamermplayout.setVisibility(GONE);
            }
        });

        confirmrmpnobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.animate().scaleX(0.0f).scaleY(0.0f).setInterpolator(new AnticipateInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.setVisibility(GONE);
                    }
                }).start();
            }
        });

        // Congratulations screen
        congratulationslayout.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {}});

        congratsmenubtn.setOnTouchListener(clickEffect);
        congratsreplaybtn.setOnTouchListener(clickEffect);
        congratssoundbtn.setOnTouchListener(clickEffect);
        congratssharebtn.setOnTouchListener(clickEffect);
        congratsmenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingameyesbtn.callOnClick();
            }
        });

        congratssoundbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSoundOn) {
                    isSoundOn = false;
                    sharedPreferences.edit().putBoolean("sound",false).apply();
                    congratssoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundoff,null));
                    if(congratulationSound!=null) { if(congratulationSound.isPlaying()) { congratulationSound.pause(); } }
                } else {
                    isSoundOn = true;
                    sharedPreferences.edit().putBoolean("sound",true).apply();
                    congratssoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundon,null));
                    if(congratulationSound!=null) { if(!congratulationSound.isPlaying()) { congratulationSound.start(); } }
                }
            }
        });

        congratssharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        congratsreplaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopEverything();
                Intent i = getIntent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtras(i.getExtras());
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }
        });

        if(gametype==1) {
            findViewById(R.id.constraintLayout8).setVisibility(View.VISIBLE);
            findViewById(R.id.imageView114).setVisibility(View.GONE);
            findViewById(R.id.teamslayout).setVisibility(View.GONE);
        }

        if(gametype==2) {
            findViewById(R.id.constraintLayout8).setVisibility(GONE);
            findViewById(R.id.imageView114).setVisibility(View.GONE);
            findViewById(R.id.teamslayout).setVisibility(View.VISIBLE);
            ((ImageView)findViewById(R.id.imageView132)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team1header,null));

            ((ImageView)findViewById(R.id.imageView137)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team2header,null));

            ((ImageView)findViewById(R.id.imageView133)).setImageDrawable(getPieceDrawableByColor("blue"));
            ((ImageView)findViewById(R.id.imageView135)).setImageDrawable(getPieceDrawableByColor("green"));
            ((ImageView)findViewById(R.id.imageView138)).setImageDrawable(getPieceDrawableByColor("red"));
            ((ImageView)findViewById(R.id.imageView140)).setImageDrawable(getPieceDrawableByColor("yellow"));

            ((TextView)findViewById(R.id.team1name1)).setText(player3name);
            ((TextView)findViewById(R.id.team1name2)).setText(player2name);
            ((TextView)findViewById(R.id.team2name1)).setText(player1name);
            ((TextView)findViewById(R.id.team2name2)).setText(player4name);
        }

        if (gametype == 3) {
            findViewById(R.id.constraintLayout8).setVisibility(View.VISIBLE);
            findViewById(R.id.imageView114).setVisibility(GONE);
            findViewById(R.id.teamslayout).setVisibility(GONE);
            for (Player p : players) {
                List<Piece> pieces = getPiecesByColor(p.getColor());
                for (int i = 0; i < 2; i++) {
                    pieces.get(i).makeAlive();
                }
                globalHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkAdjustments(pieces.get(0).startPosition);
                    }
                },420);
            }
        }

        if(gametype==4) {
            findViewById(R.id.constraintLayout8).setVisibility(View.VISIBLE);
            findViewById(R.id.imageView114).setVisibility(View.VISIBLE);
            findViewById(R.id.teamslayout).setVisibility(GONE);
        }


            blinkAnim = new Runnable() {
                @Override
                public void run() {
                    if(gameStartImageView.getVisibility()==View.INVISIBLE) {
                        gameStartImageView.setVisibility(View.VISIBLE);
                    } else {
                        gameStartImageView.setVisibility(View.INVISIBLE);
                    }
                    globalHandler.postDelayed(this,220);
                }
            };

            globalHandler.post(blinkAnim);

            // Guard with isSoundOn — original code always played the intro
            // even with sound muted, wasting a MediaPlayer allocation that
            // also blocked the onCompletionListener cleanup path.
            if(isSoundOn) {
                gameStartSound = MediaPlayer.create(this,R.raw.gamestartsound);
                gameStartSound.start();
                gameStartSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if (blinkAnim != null) { globalHandler.removeCallbacks(blinkAnim); }
                        gameStartImageView.setVisibility(GONE);
                        mainDiceImageView.setVisibility(View.VISIBLE);
                        hintArrow.setVisibility(View.VISIBLE);
                        switchPlayers();
                        gameStartSound.release();
                        gameStartSound = null;
                    }
                });
            } else {
                // Sound is muted — replicate the onCompletion cleanup so the
                // game still proceeds after the blink duration.
                globalHandler.postDelayed(new Runnable() {
                    @Override public void run() {
                        if (blinkAnim != null) { globalHandler.removeCallbacks(blinkAnim); }
                        gameStartImageView.setVisibility(GONE);
                        mainDiceImageView.setVisibility(View.VISIBLE);
                        hintArrow.setVisibility(View.VISIBLE);
                        switchPlayers();
                    }
                }, 1500);
            }
            mainDiceImageView.setVisibility(GONE);
            hintArrow.setVisibility(GONE);
            d = new Dice(mainDiceImageView, nop, color);
            isGameSessionActive = true;
            isQuitConfirmed = false;
            clearSavedGameSnapshot();
            // The phone owner (first human player) is the admin from the very
            // start: protections & priority are always on. The 3s long press
            // only toggles the dice-assist rolls on/off for this owner.
            initAdminOwner();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(((int)(pxWidth*0.4)),((int)(pxWidth*0.4)));
        redHomeBlink.setLayoutParams(lp);
        lp.addRule(RelativeLayout.ALIGN_TOP,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_LEFT,R.id.imageView);

        }


    private void registerBackNavigation() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackNavigation();
            }
        });
    }

    private void handleBackNavigation() {
        // Always close the topmost in-game layer first. The final fallback is
        // the same quit dialog used by the in-game exit button; it never calls
        // finish() directly.
        if (confirmrmplayout.getVisibility() == View.VISIBLE) {
            confirmrmpnobtn.callOnClick();
        } else if (ingamermplayout.getVisibility() == View.VISIBLE) {
            rmpbackbtn.callOnClick();
        } else if (quitgamelayout.getVisibility() == View.VISIBLE) {
            ingamenobtn.callOnClick();
        } else if (ingamemenuitemslayout.getVisibility() == View.VISIBLE) {
            ingamemenuitemslayout.setVisibility(GONE);
        } else if (congratulationslayout.getVisibility() != View.VISIBLE) {
            menuexitbtn.callOnClick();
        }
    }

    private boolean shouldSaveGameSnapshot() {
        return isGameSessionActive
                && !isQuitConfirmed
                && d != null
                && (congratulationslayout == null
                || congratulationslayout.getVisibility() != View.VISIBLE);
    }

    private void clearSavedGameSnapshot() {
        if (sharedPreferences != null) {
            sharedPreferences.edit().remove(ACTIVE_GAME_SNAPSHOT_KEY).apply();
        }
    }

    /**
     * Save a compact persistent snapshot whenever Android sends the activity
     * to the background. This is intentionally separate from the quit flow:
     * pressing Home pauses the game and preserves the current board, while
     * choosing Yes explicitly abandons it.
     */
    private void saveGameSnapshot() {
        if (!shouldSaveGameSnapshot() || sharedPreferences == null) {
            return;
        }

        try {
            JSONObject snapshot = new JSONObject();
            snapshot.put("version", 1);
            snapshot.put("nop", nop);
            snapshot.put("gametype", gametype);
            snapshot.put("currentPlayerIndex", currentPlayerIndex);
            snapshot.put("currentPlayerPosition", currentPlayerPosition);
            snapshot.put("currentPlayerColor", currentPlayerColor);
            snapshot.put("currentPlayerName", currentPlayerName);
            snapshot.put("currentPlayerDice", currentPlayerDice);
            snapshot.put("isDiceMovableExtraChance", isDiceMovableExtraChance);
            snapshot.put("isSoundOn", isSoundOn);
            snapshot.put("isMusicOn", isMusicOn);

            JSONArray playerSnapshots = new JSONArray();
            if (players != null) {
                for (Player player : players) {
                    JSONObject playerSnapshot = new JSONObject();
                    playerSnapshot.put("position", player.position);
                    playerSnapshot.put("color", player.color);
                    playerSnapshot.put("index", player.index);
                    playerSnapshot.put("name", player.name);
                    playerSnapshot.put("isBot", player.isBot);
                    playerSnapshot.put("diceValue", player.diceValue);
                    playerSnapshot.put("chances", player.chances);
                    playerSnapshots.put(playerSnapshot);
                }
            }
            snapshot.put("players", playerSnapshots);

            JSONArray pieceSnapshots = new JSONArray();
            addPieceSnapshots(pieceSnapshots, rp);
            addPieceSnapshots(pieceSnapshots, gp);
            addPieceSnapshots(pieceSnapshots, bp);
            addPieceSnapshots(pieceSnapshots, yp);
            snapshot.put("pieces", pieceSnapshots);

            // commit() is deliberate here: onPause can be followed by the
            // process being killed before an asynchronous apply() completes.
            sharedPreferences.edit()
                    .putString(ACTIVE_GAME_SNAPSHOT_KEY, snapshot.toString())
                    .commit();
        } catch (Exception ignored) {
            // A save attempt must never interrupt gameplay or the lifecycle.
        }
    }

    private void addPieceSnapshots(JSONArray target, List<Piece> pieces) {
        if (pieces == null) {
            return;
        }
        for (int index = 0; index < pieces.size(); index++) {
            Piece piece = pieces.get(index);
            try {
                JSONObject pieceSnapshot = new JSONObject();
                pieceSnapshot.put("color", piece.colour);
                pieceSnapshot.put("index", index);
                pieceSnapshot.put("isAlive", piece.isAlive);
                pieceSnapshot.put("isClickable", piece.isClickable);
                pieceSnapshot.put("currBlock", piece.currBlock);
                pieceSnapshot.put("numberOfSteps", piece.numberOfSteps);
                pieceSnapshot.put("isReadyToEnterWinnerZone", piece.isReadyToEnterWinnerZone);
                pieceSnapshot.put("currWinnerBlock", piece.currWinnerBlock);
                pieceSnapshot.put("hasCompletedItsPurpose", piece.hasCompletedItsPurpose);
                pieceSnapshot.put("isThisPlayerWon", piece.isThisPlayerWon);
                pieceSnapshot.put("translationX", piece.piece.getTranslationX());
                pieceSnapshot.put("translationY", piece.piece.getTranslationY());
                pieceSnapshot.put("scaleX", piece.piece.getScaleX());
                pieceSnapshot.put("scaleY", piece.piece.getScaleY());
                target.put(pieceSnapshot);
            } catch (Exception ignored) {
                // Keep the other pieces in the snapshot if one view is in an
                // invalid transition state.
            }
        }
    }

    @Override
    protected void onPause() {
        saveGameSnapshot();
        super.onPause();
        // Pause all running animations + handler chains so we don't burn
        // CPU animating an invisible view hierarchy while the user has
        // backgrounded the app. Without this, the move() recursion + 16
        // infinite piece-rotate animators + infinite hint-arrow animator
        // keep ticking the Choreographer the entire time the app is in
        // the recents tray — a major source of "lag grows as game
        // progresses" complaints.
        if (globalHandler != null) {
            globalHandler.removeCallbacksAndMessages(null);
        }
        if (d != null && d.diceHandler != null) {
            d.diceHandler.removeCallbacksAndMessages(null);
        }
        // Cancel any pending long-press using the STORED reference so it
        // doesn't fire 3 seconds after the user backgrounded the app.
        if (smartDiceHandler != null) {
            if (pendingSmartDiceLongPress != null) {
                smartDiceHandler.removeCallbacks(pendingSmartDiceLongPress);
                pendingSmartDiceLongPress = null;
            }
            if (smartDiceLongPressAction != null) {
                smartDiceHandler.removeCallbacks(smartDiceLongPressAction);
            }
        }
        // Pause infinite animators so they stop ticking in background.
        if (hintArrowAnimator != null && hintArrowAnimator.isStarted()) {
            hintArrowAnimator.pause();
        }
        if (rp != null) { for (Piece p : rp) { if (p != null && p.rotateAnimator != null && p.rotateAnimator.isStarted()) p.rotateAnimator.pause(); } }
        if (gp != null) { for (Piece p : gp) { if (p != null && p.rotateAnimator != null && p.rotateAnimator.isStarted()) p.rotateAnimator.pause(); } }
        if (bp != null) { for (Piece p : bp) { if (p != null && p.rotateAnimator != null && p.rotateAnimator.isStarted()) p.rotateAnimator.pause(); } }
        if (yp != null) { for (Piece p : yp) { if (p != null && p.rotateAnimator != null && p.rotateAnimator.isStarted()) p.rotateAnimator.pause(); } }
        // Pause looping media (congrats) while backgrounded.
        if (congratulationSound != null && congratulationSound.isPlaying()) {
            congratulationSound.pause();
        }
        if (diceRollSound != null && diceRollSound.isPlaying()) {
            diceRollSound.pause();
        }
        if (stepSound != null && stepSound.isPlaying()) {
            stepSound.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullScreen();
        // Resume paused animators — but only if they were actually started.
        if (hintArrowAnimator != null && hintArrowAnimator.isPaused()) {
            hintArrowAnimator.resume();
        }
        // Piece rotate animators will be re-started by activeState() the
        // next time a piece becomes active; no need to resume them here
        // (and doing so would re-trigger the constructor-time bug where
        // animators spun while readyToPick was INVISIBLE).
        // Resume congrats sound if the win screen is showing.
        if (congratulationslayout != null
                && congratulationslayout.getVisibility() == View.VISIBLE
                && congratulationSound != null
                && isSoundOn
                && !congratulationSound.isPlaying()) {
            try { congratulationSound.start(); } catch (Exception ignored) {}
        }
    }

    @Override
    public void onUserLeaveHint() {
        // Home/app-switch gestures are best-effort signals; onPause remains
        // the authoritative lifecycle save below.
        saveGameSnapshot();
        super.onUserLeaveHint();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        saveGameSnapshot();
        super.onSaveInstanceState(outState);
    }

    private void stopEverything() {
        globalHandler.removeCallbacksAndMessages(null);
        d.diceHandler.removeCallbacksAndMessages(null);
        if (pendingSmartDiceLongPress != null) {
            smartDiceHandler.removeCallbacks(pendingSmartDiceLongPress);
            pendingSmartDiceLongPress = null;
        }
        if (smartDiceLongPressAction != null) {
            smartDiceHandler.removeCallbacks(smartDiceLongPressAction);
        }
        if(congratulationSound!=null) {
            if(congratulationSound.isPlaying()) {
                congratulationSound.stop();
            }
            try { congratulationSound.release(); } catch (Exception e) {}
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Aggressive teardown — the original onDestroy was empty, which
        // leaked: every infinite piece-rotate animator (16 of them), the
        // infinite hint-arrow animator, the globalHandler queue, the dice
        // handler queue, all the MediaPlayers, and the smart-dice handler.
        // These leaks accumulated across game restarts and were the
        // dominant cause of "lag grows as the game progresses".
        try { stopEverything(); } catch (Exception ignored) {}
        if (hintArrowAnimator != null) {
            try { hintArrowAnimator.cancel(); } catch (Exception ignored) {}
            hintArrowAnimator = null;
        }
        // Cancel every piece's infinite rotate animator.
        if (rp != null) { for (Piece p : rp) { if (p != null && p.rotateAnimator != null) { try { p.rotateAnimator.cancel(); } catch (Exception ignored) {} } } }
        if (gp != null) { for (Piece p : gp) { if (p != null && p.rotateAnimator != null) { try { p.rotateAnimator.cancel(); } catch (Exception ignored) {} } } }
        if (bp != null) { for (Piece p : bp) { if (p != null && p.rotateAnimator != null) { try { p.rotateAnimator.cancel(); } catch (Exception ignored) {} } } }
        if (yp != null) { for (Piece p : yp) { if (p != null && p.rotateAnimator != null) { try { p.rotateAnimator.cancel(); } catch (Exception ignored) {} } } }
        // Release MediaPlayers.
        releaseMediaPlayer(diceRollSound);
        releaseMediaPlayer(stepSound);
        releaseMediaPlayer(gameStartSound);
        releaseMediaPlayer(deathSound);
        releaseMediaPlayer(safeSound);
        releaseMediaPlayer(pantaSound);
        releaseMediaPlayer(congratulationSound);
        diceRollSound = null;
        stepSound = null;
        gameStartSound = null;
        deathSound = null;
        safeSound = null;
        pantaSound = null;
        congratulationSound = null;
        // Cancel the smart-dice handler.
        if (smartDiceHandler != null) {
            smartDiceHandler.removeCallbacksAndMessages(null);
        }
    }

    private void releaseMediaPlayer(MediaPlayer mp) {
        if (mp == null) { return; }
        try {
            if (mp.isPlaying()) { mp.stop(); }
        } catch (Exception ignored) {}
        try { mp.release(); } catch (Exception ignored) {}
    }

    private void hideThisPlayerDiceBg(int index) {
        switch (index) {
            case 1:
                findViewById(R.id.bleftdicebg).setVisibility(GONE);
                break;
            case 2:
                findViewById(R.id.tleftdicebg).setVisibility(GONE);
                break;
            case 3:
                findViewById(R.id.trightdicebg).setVisibility(GONE);
                break;
            case 4:
                findViewById(R.id.brightdicebg).setVisibility(GONE);
                break;
        }
    }

    private void removeThisPlayerByIndex(int index) {
        if(currentPlayerIndex!=0) { currentPlayerIndex--; }
        switch (index) {
            case 1:
                rmp1bg.setAlpha(0.5f);
                rmpp1removeicon.setOnClickListener(null);
                if(currentPlayerIndex==(rmpindexforp1-1)) {
                    players.remove(rmpindexforp1-1);
                    rmpindexforp2--;
                    rmpindexforp3--;
                    rmpindexforp4--;
                    switchPlayers();
                } else {
                    currentPlayerIndex++;
                    players.remove(rmpindexforp1-1);
                    rmpindexforp3--;
                    rmpindexforp4--;
                }
                hideThisPlayerDiceBg(1);
                for(Piece p : getPiecesByColor(player1color)) {
                    p.piece.setVisibility(GONE);
                }
                p1exitbox.setImageDrawable(getExitBoxByColor(player1color));
                p1exitbox.setVisibility(View.VISIBLE);
                break;
            case 2:
                rmp2bg.setAlpha(0.5f);
                rmpp2removeicon.setOnClickListener(null);
                if(currentPlayerIndex==(rmpindexforp2-1)) {
                    players.remove(rmpindexforp2-1);
                    rmpindexforp3--;
                    rmpindexforp4--;
                    switchPlayers();
                } else {
                    currentPlayerIndex++;
                    players.remove(rmpindexforp2-1);
                    rmpindexforp3--;
                    rmpindexforp4--;
                }
                hideThisPlayerDiceBg(2);
                for(Piece p : getPiecesByColor(player2color)) {
                    p.piece.setVisibility(GONE);
                }
                p2exitbox.setImageDrawable(getExitBoxByColor(player2color));
                p2exitbox.setVisibility(View.VISIBLE);
                break;
            case 3:
                rmp3bg.setAlpha(0.5f);
                rmpp3removeicon.setOnClickListener(null);
                if(currentPlayerIndex==(rmpindexforp3-1)) {
                    players.remove(rmpindexforp3-1);
                    rmpindexforp4--;
                    switchPlayers();
                } else {
                    currentPlayerIndex++;
                    players.remove(rmpindexforp3-1);
                    rmpindexforp4--;
                }
                hideThisPlayerDiceBg(3);
                for(Piece p : getPiecesByColor(player3color)) {
                    p.piece.setVisibility(GONE);
                }
                p3exitbox.setImageDrawable(getExitBoxByColor(player3color));
                p3exitbox.setVisibility(View.VISIBLE);
                break;
            case 4:
                rmp4bg.setAlpha(0.5f);
                rmpp4removeicon.setOnClickListener(null);
                if(currentPlayerIndex==(rmpindexforp4-1-1)) {
                    players.remove(rmpindexforp4-1);
                    switchPlayers();
                } else {
                    currentPlayerIndex++;
                    players.remove(rmpindexforp4-1);
                }
                hideThisPlayerDiceBg(4);
                for(Piece p : getPiecesByColor(player4color)) {
                    p.piece.setVisibility(GONE);
                }
                p4exitbox.setImageDrawable(getExitBoxByColor(player4color));
                p4exitbox.setVisibility(View.VISIBLE);
                break;
        }

        if(players.size()==2) {
            menuremoveplayersbtn.setVisibility(GONE);
        }

    }

    private Drawable getExitBoxByColor(String color) {
        switch (color)
        {
            case "red":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.redexitbox,null);
            case "blue":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.blueexitbox,null);
            case "green":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.greenexitbox,null);
            case "yellow":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.yellowexitbox,null);
        }
        return ResourcesCompat.getDrawable(getResources(),R.drawable.redexitbox,null);
    }

    void addPiecesToRmpLayout(String color,String name,int index) {
        switch (index) {
            case 1:
                rmpp1piece.setImageDrawable(getPieceDrawableByColor(color));
                rmpp1name.setText(name);
                break;
            case 2:
                rmpp2piece.setImageDrawable(getPieceDrawableByColor(color));
                rmpp2name.setText(name);
                break;
            case 3:
                rmpp3piece.setImageDrawable(getPieceDrawableByColor(color));
                rmpp3name.setText(name);
                break;
            case 4:
                rmpp4piece.setImageDrawable(getPieceDrawableByColor(color));
                rmpp4name.setText(name);
                break;
        }
    }


    void switchPlayers()
    {
        tableConsecutiveSixes = 0;
        // Deactivate the previous player's pieces FIRST so that stray taps
        // on their pads cannot consume currentPlayerDice (which would
        // otherwise cause the "taps don't register" / "wrong piece moved"
        // symptoms). This was missing — a piece that was isClickable=true
        // stayed tappable into the next player's turn.
        if (players != null && !players.isEmpty()) {
            String prevColor = currentPlayerColor;
            if (prevColor != null && !prevColor.isEmpty()) {
                List<Piece> prevPieces = getPiecesByColor(prevColor);
                if (prevPieces != null) {
                    for (Piece p : prevPieces) {
                        if (p != null) { p.inactiveState(); }
                    }
                }
            }
        }
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayerPosition = currentPlayer.getPosition();
        currentPlayerColor = currentPlayer.getColor();
        currentPlayerSelectedIndex = currentPlayer.getIndex();
        currentPlayerName = currentPlayer.name;
        //Toast.makeText(this, currentPlayerColor+"", Toast.LENGTH_SHORT).show();
        currentPlayer.setActive();
        if(currentPlayer.isBot) {
            hintArrow.setVisibility(GONE);
            moveDice(currentPlayerPosition);
            // Reuse the shared globalHandler instead of allocating a new
            // Handler+Runnable pair on every bot turn (which leaks one
            // Handler per turn and adds GC pressure as the game grows).
            // ORIGINAL 150ms — restored after the previous version tightened
            // to 80ms which made bot turns feel too fast.
            globalHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    d.roll();
                }
            },150);
        } else {
            moveDice(currentPlayerPosition);
            hintArrow.setVisibility(View.VISIBLE);
        }

        if(currentPlayerIndex>=(players.size()-1)) { currentPlayerIndex = 0; } else { currentPlayerIndex++; }
    }

    void moveDice(int position)
    {
        switch(position)
        {
            case 1:
                moveToTopLeft();
                break;
            case 2:
                moveToTopRight();
                break;
            case 3:
                moveToBottomLeft();
                break;
            case 4:
                moveToBottomRight();
                break;
        }

    }

    private void createPieces(String color, int position,String playername,boolean isBot) {
        if(nop==2) {
            if(players.size()==0)
            { players.add(new Player(position, color, playername, isBot, 0)); }
            else
            { players.add(new Player(position, color, playername, isBot, 2)); }
        }else {
            players.add(new Player(position, color, playername, isBot, players.size()));
            addPiecesToRmpLayout(color,getPlayerNameByPosition(position).getText().toString(),players.size());
        }
        float pos[][];
        int sPos[] = {39,0,26,13};
        switch (position) {case 1: pos = pos1; break; case 2: pos = pos2; break; case 3: pos = pos3; break; case 4: pos = pos4; break; default: pos = new float[4][2]; }
        switch (color) {
            case "red":
                rp.add(new Piece("red", redpiece1, findViewById(R.id.red1activecircle), findViewById(R.id.imageView6), findViewById(R.id.imageView7),  sPos[position - 1], pos[0][0], pos[0][1],isBot));
                rp.add(new Piece("red", redpiece2, findViewById(R.id.red2activecircle), findViewById(R.id.imageView62), findViewById(R.id.imageView72),  sPos[position - 1], pos[1][0], pos[1][1],isBot));
                rp.add(new Piece("red", redpiece3, findViewById(R.id.red3activecircle), findViewById(R.id.imageView63), findViewById(R.id.imageView73),  sPos[position - 1], pos[2][0], pos[2][1],isBot));
                rp.add(new Piece("red", redpiece4, findViewById(R.id.red4activecircle), findViewById(R.id.imageView64), findViewById(R.id.imageView74),  sPos[position - 1], pos[3][0], pos[3][1],isBot));
                break;
            case "green":
                gp.add(new Piece("green", greenpiece1, findViewById(R.id.green1activecircle), findViewById(R.id.imageView51), findViewById(R.id.imageView81),  sPos[position - 1], pos[0][0], pos[0][1],isBot));
                gp.add(new Piece("green", greenpiece2, findViewById(R.id.green2activecircle), findViewById(R.id.imageView52), findViewById(R.id.imageView82),  sPos[position - 1], pos[1][0], pos[1][1],isBot));
                gp.add(new Piece("green", greenpiece3, findViewById(R.id.green3activecircle), findViewById(R.id.imageView53), findViewById(R.id.imageView83),  sPos[position - 1], pos[2][0], pos[2][1],isBot));
                gp.add(new Piece("green", greenpiece4, findViewById(R.id.green4activecircle), findViewById(R.id.imageView54), findViewById(R.id.imageView84),  sPos[position - 1], pos[3][0], pos[3][1],isBot));
                break;
            case "blue":
                bp.add(new Piece("blue", bluepiece1, findViewById(R.id.blue1activecircle), findViewById(R.id.imageView41), findViewById(R.id.imageView91),  sPos[position - 1], pos[0][0], pos[0][1],isBot));
                bp.add(new Piece("blue", bluepiece2, findViewById(R.id.blue2activecircle), findViewById(R.id.imageView42), findViewById(R.id.imageView92),  sPos[position - 1], pos[1][0], pos[1][1],isBot));
                bp.add(new Piece("blue", bluepiece3, findViewById(R.id.blue3activecircle), findViewById(R.id.imageView43), findViewById(R.id.imageView93),  sPos[position - 1], pos[2][0], pos[2][1],isBot));
                bp.add(new Piece("blue", bluepiece4, findViewById(R.id.blue4activecircle), findViewById(R.id.imageView44), findViewById(R.id.imageView94),  sPos[position - 1], pos[3][0], pos[3][1],isBot));
                break;
            case "yellow":
                yp.add(new Piece("yellow", yellowpiece1, findViewById(R.id.yellow1activecircle), findViewById(R.id.imageView31), findViewById(R.id.imageView101),  sPos[position - 1], pos[0][0], pos[0][1],isBot));
                yp.add(new Piece("yellow", yellowpiece2, findViewById(R.id.yellow2activecircle), findViewById(R.id.imageView32), findViewById(R.id.imageView102),  sPos[position - 1], pos[1][0], pos[1][1],isBot));
                yp.add(new Piece("yellow", yellowpiece3, findViewById(R.id.yellow3activecircle), findViewById(R.id.imageView33), findViewById(R.id.imageView103),  sPos[position - 1], pos[2][0], pos[2][1],isBot));
                yp.add(new Piece("yellow", yellowpiece4, findViewById(R.id.yellow4activecircle), findViewById(R.id.imageView34), findViewById(R.id.imageView104),  sPos[position - 1], pos[3][0], pos[3][1],isBot));
                break;
        }
    }

    View getStepBubble(int n)
    {
        switch (n)
        {
            case 1:
                return step1;
            case 2:
                return step2;
            case 3:
                return step3;
            case 4:
                return step4;
            case 5:
                return step5;
            case 6:
                return step6;
        }
        return step1;
    }

    void initViews()
    {
        sharedPreferences = getSharedPreferences("LudoModUser",MODE_PRIVATE);

        botwins= sharedPreferences.getInt("botwins",0);
        botloses=sharedPreferences.getInt("botloses",0);
        ((TextView)findViewById(R.id.userwins)).setText((botwins+""));
        ((TextView)findViewById(R.id.userloses)).setText((botloses+""));

        isSoundOn = sharedPreferences.getBoolean("sound",true);
        isMusicOn = sharedPreferences.getBoolean("music",false);

        completeBackground = findViewById(R.id.activitybg);
        displayMetrics = getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels/displayMetrics.density;
        dpWidth = displayMetrics.widthPixels/displayMetrics.density;
        pxWidth = displayMetrics.widthPixels;
        pxHeight = displayMetrics.heightPixels-getStatusBarHeight();
        ludoBoard = findViewById(R.id.imageView);
        red1activecircle = findViewById(R.id.red1activecircle);
        red2activecircle = findViewById(R.id.red2activecircle);
        red3activecircle = findViewById(R.id.red3activecircle);
        red4activecircle = findViewById(R.id.red4activecircle);
        redHomeBlink = findViewById(R.id.imageView3);
        redpiece1 = findViewById(R.id.redpiece1);
        redpiece2 = findViewById(R.id.redpiece2);
        redpiece3 = findViewById(R.id.redpiece3);
        redpiece4 = findViewById(R.id.redpiece4);

        greenpiece1 = findViewById(R.id.greenpiece1);
        greenpiece2 = findViewById(R.id.greenpiece2);
        greenpiece3 = findViewById(R.id.greenpiece3);
        greenpiece4 = findViewById(R.id.greenpiece4);

        bluepiece1 = findViewById(R.id.bluepiece1);
        bluepiece2 = findViewById(R.id.bluepiece2);
        bluepiece3 = findViewById(R.id.bluepiece3);
        bluepiece4 = findViewById(R.id.bluepiece4);

        yellowpiece1 = findViewById(R.id.yellowpiece1);
        yellowpiece2 = findViewById(R.id.yellowpiece2);
        yellowpiece3 = findViewById(R.id.yellowpiece3);
        yellowpiece4 = findViewById(R.id.yellowpiece4);

        // player names 1 to 4
        playername1 = findViewById(R.id.pname1);
        playername2 = findViewById(R.id.pname2);
        playername3 = findViewById(R.id.pname3);
        playername4 = findViewById(R.id.pname4);

        mainDiceImageView = findViewById(R.id.maindiceimageview);
        hintArrow = findViewById(R.id.hintarrowleft);

        crownIndex1 = findViewById(R.id.winnercrownindex1);
        crownIndex2 = findViewById(R.id.winnercrownindex2);
        crownIndex3 = findViewById(R.id.winnercrownindex3);
        crownIndex4 = findViewById(R.id.winnercrownindex4);

        // ingame menu items
        ingamemenubtn = findViewById(R.id.ingamemenubtn);
        ingamemenuitemslayout = findViewById(R.id.ingamemenuitemslayout);
        menuremoveplayersbtn = findViewById(R.id.rmpitemmenu);
        menuexitbtn = findViewById(R.id.exititemmenu);

        // ingame exitlayout views
        quitgamelayout = findViewById(R.id.ingamequitlayout);
        ingameyesbtn = findViewById(R.id.ingamequitlayoutyesbtn);
        ingamenobtn = findViewById(R.id.ingamequitlayoutnobtn);
        ingamesoundbtn = findViewById(R.id.ingamequitlayoutsoundbtn);
        ingamemusicbtn = findViewById(R.id.ingamequitlayoutmusicbtn);

        //in game rmplayout
        ingamermplayout = findViewById(R.id.ingamermplayout);
        rmp1bg = findViewById(R.id.rmp1);
        rmp2bg = findViewById(R.id.rmp2);
        rmp3bg = findViewById(R.id.rmp3);
        rmp4bg = findViewById(R.id.rmp4);
        rmpp1piece = findViewById(R.id.rmp1piece);
        rmpp2piece = findViewById(R.id.rmp2piece);
        rmpp3piece = findViewById(R.id.rmp3piece);
        rmpp4piece = findViewById(R.id.rmp4piece);
        rmpp1removeicon = findViewById(R.id.rmp1btn);
        rmpp2removeicon = findViewById(R.id.rmp2btn);
        rmpp3removeicon = findViewById(R.id.rmp3btn);
        rmpp4removeicon = findViewById(R.id.rmp4btn);
        rmpbackbtn = findViewById(R.id.rmpbackbtn);
        rmpmenubtn = findViewById(R.id.rmpmenubtn);
        rmpp1name = findViewById(R.id.rmp1name);
        rmpp2name = findViewById(R.id.rmp2name);
        rmpp3name = findViewById(R.id.rmp3name);
        rmpp4name = findViewById(R.id.rmp4name);


        //in gmae rmp sublayout confirmrmplayout
        confirmrmplayout = findViewById(R.id.confirmrmplayout);
        selectedrmppiece = findViewById(R.id.confirmremovepieceview);
        confirmrmpyesbtn = findViewById(R.id.confirmrmpyesbtn);
        confirmrmpnobtn = findViewById(R.id.confirmrmpnobtn);

        // players exit boxes
        p1exitbox = findViewById(R.id.p1exitbox);
        p2exitbox = findViewById(R.id.p2exitbox);
        p3exitbox = findViewById(R.id.p3exitbox);
        p4exitbox = findViewById(R.id.p4exitbox);

        // congratulaions layout

        congratulationslayout = findViewById(R.id.congratulationsscreen);

        congratsmenubtn = findViewById(R.id.congratsscreenmenubtn);
        congratssoundbtn = findViewById(R.id.congratsscreensoundbtn);
        congratssharebtn = findViewById(R.id.congratssharebtn);
        congratsreplaybtn = findViewById(R.id.congratsscreenreplaybtn);

        winnerlistp1layout = findViewById(R.id.winnerpositionlayout1);
        winnerlistp2layout = findViewById(R.id.winnerpositionlayout2);
        winnerlistp3layout = findViewById(R.id.winnerpositionlayout3);
        winnerlistp4layout = findViewById(R.id.winnerpositionlayout4);

        wlistcrown1 = findViewById(R.id.winnerliscrown1);
        wlistcrown2 = findViewById(R.id.winnerliscrown2);
        wlistcrown3 = findViewById(R.id.winnerliscrown3);
        wlistcrown4 = findViewById(R.id.winnerliscrown4);
        wlistpiece1 = findViewById(R.id.winnerlistp1);
        wlistpiece2 = findViewById(R.id.winnerlistp2);
        wlistpiece3 = findViewById(R.id.winnerlistp3);
        wlistpiece4 = findViewById(R.id.winnerlistp4);
        wlistwinorlose1 = findViewById(R.id.winorlose1);
        wlistwinorlose2 = findViewById(R.id.winorlose2);
        wlistwinorlose3 = findViewById(R.id.winorlose3);
        wlistwinorlose4 = findViewById(R.id.winorlose4);

        wlistname1 = findViewById(R.id.winnerlistpname1);
        wlistname2 = findViewById(R.id.winnerlistpname2);
        wlistname3 = findViewById(R.id.winnerlistpname3);
        wlistname4 = findViewById(R.id.winnerlistpname4);

        gameStartImageView = findViewById(R.id.gamestartimageview);

        pos1 = new float[4][2];
        pos2 = new float[4][2];
        pos3 = new float[4][2];
        pos4 = new float[4][2];

        onePercentWidth = (float) pxWidth/100;
        onePercentHeight = (float) pxHeight/100;

        players = new ArrayList<>();

        rp = new ArrayList<>();
        gp = new ArrayList<>();
        bp = new ArrayList<>();
        yp = new ArrayList<>();

        safeSpots = new HashSet<>();

        safeSpots.add(0);safeSpots.add(8);safeSpots.add(13);safeSpots.add(21);safeSpots.add(26);safeSpots.add(34);safeSpots.add(39);safeSpots.add(47);
        //safeSpots.add(57);safeSpots.add(63);safeSpots.add(69);safeSpots.add(75);

        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        step4 = findViewById(R.id.step4);
        step5 = findViewById(R.id.step5);
        step6 = findViewById(R.id.step6);

        int boxSize = (int)(Math.ceil(pxWidth*0.0667)*1.3);
        sizeOfBox = (int)(Math.ceil(pxWidth*0.0667)*0.15);

        blockSize = (float) (pxWidth*0.0667);

        RelativeLayout.LayoutParams stepsParams = new RelativeLayout.LayoutParams(boxSize,boxSize);
        step1.setLayoutParams(stepsParams);
        step2.setLayoutParams(stepsParams);
        step3.setLayoutParams(stepsParams);
        step4.setLayoutParams(stepsParams);
        step5.setLayoutParams(stepsParams);
        step6.setLayoutParams(stepsParams);


        redCircle = AppCompatResources.getDrawable(this,R.drawable.red_circle_shaded);
        greenCircle = AppCompatResources.getDrawable(this,R.drawable.green_circle_shaded);
        blueCircle = AppCompatResources.getDrawable(this,R.drawable.blue_circle_shaded);
        yellowCircle = AppCompatResources.getDrawable(this,R.drawable.yellow_circle_shaded);

        wBlocks52 = new int[] {52, 53, 54, 55, 56, 57};
        wBlocks58 = new int[] {58, 59, 60, 61, 62, 63};
        wBlocks64 = new int[] {64, 65, 66, 67, 68, 69};
        wBlocks70 = new int[] {70, 71, 72, 73, 74, 75};

        diceRollSound = MediaPlayer.create(this, R.raw.diceroll);
        stepSound = MediaPlayer.create(this, R.raw.step);
        // Pre-create the short SFX players too — these were previously
        // created on every kill / safe-landing / win via MediaPlayer.create
        // (which opens a file descriptor and prepares a decoder on the UI
        // thread, causing the "click lag" the user reported). Pre-creating
        // them once means hot paths can just seekTo(0)+start() with no
        // allocation overhead.
        safeSound = MediaPlayer.create(this, R.raw.safe);
        deathSound = MediaPlayer.create(this, R.raw.death);
        pantaSound = MediaPlayer.create(this, R.raw.panta);
        if (safeSound != null) { try { safeSound.setLooping(false); } catch (Exception ignored) {} }
        if (deathSound != null) { try { deathSound.setLooping(false); } catch (Exception ignored) {} }
        if (pantaSound != null) { try { pantaSound.setLooping(false); } catch (Exception ignored) {} }
        // Pre-inflate the blink animation once so setActive() doesn't have
        // to AnimationUtils.loadAnimation() on every turn change.
        try { cachedBlinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blinkanimation); }
        catch (Exception ignored) {}

        if(isSoundOn) {
            ingamesoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundon,null));
            congratssoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundon,null));
        } else { ingamesoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundoff,null));
            congratssoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundoff,null));}

        if(isMusicOn) {
            ingamemusicbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.musicon,null));
        } else { ingamemusicbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.musicoff,null)); }


        editText = findViewById(R.id.editTextNumber);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(((int)(pxWidth)),((int)(pxWidth)));
        ludoBoard.setLayoutParams(layoutParams); // size of board set
    }

    void initializeAnimatorSets() {
        animatorSets = new AnimatorSet[6];

        for (int i = 0; i < animatorSets.length; i++) {
            View v = getStepBubble(i+1);
            v.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            animatorSets[i] = createAnimatorSet(v);
            animatorSets[i].addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    v.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    void showStep(int viewIndex,String color, float x, float y) {
        // Defensive clamp: if move() ever runs with a stale diceValue (-1
        // or 0), viewIndex would be -1 / 0 and animatorSets[viewIndex-1]
        // would throw ArrayIndexOutOfBoundsException, which crashes the
        // whole move chain and leaves the piece visually stuck. Clamp to
        // [1,6] so the animation silently no-ops instead of crashing.
        if (viewIndex < 1 || viewIndex > 6) {
            return;
        }
        if (animatorSets == null || animatorSets.length != 6) {
            initializeAnimatorSets();
        }

        View view = null;
        view = getStepBubble(viewIndex);

        if (view != null) {
            view.setBackground(getColorDrawable(color));
            view.setTranslationX(x-sizeOfBox);
            view.setTranslationY(y-sizeOfBox);

            AnimatorSet animatorSet = animatorSets[viewIndex - 1];
            view.setVisibility(View.VISIBLE);
            animatorSet.start();
        }
    }

    private AnimatorSet createAnimatorSet(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        float initialScale = 0.5f;
        float finalScale = 1.0f;
        // Removed dead `long duration = 700;` (was never used).
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, initialScale, finalScale);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, initialScale, finalScale);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY);
        scaleAnimator.setDuration(600); // ORIGINAL 600ms — restored

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f);
        alphaAnimator.setDuration(800); // ORIGINAL 800ms — restored

        animatorSet.playTogether(scaleAnimator, alphaAnimator);

        return animatorSet;
    }

    Drawable getColorDrawable(String color)
    {
        switch (color)
        {
            case "red":
                return redCircle;
            case "green":
                return greenCircle;
            case "blue":
                return blueCircle;
            case "yellow":
                return yellowCircle;
        }
        return redCircle;
    }

    private void applySystemBarInsets() {
        View root = findViewById(R.id.activitybg);
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(view.getPaddingLeft(), systemBars.top, view.getPaddingRight(), systemBars.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(root);
    }

    void setFullScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getColor(R.color.purple_700));
        getWindow().setNavigationBarColor(getColor(R.color.black));
        getWindow().getDecorView().setSystemUiVisibility(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            }
        }
    }

    float getNavHeight()
    {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public float getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // NOTE: onResume() was originally defined here AND again in the
    // lifecycle block above. The richer version above (which resumes
    // animators and congrats sound) supersedes this one — removed to
    // avoid the "method onResume() is already defined" compile error.

    void moveToTopLeft()
    {
        ConstraintLayout.LayoutParams lps = (ConstraintLayout.LayoutParams) mainDiceImageView.getLayoutParams();
        lps.startToStart = ConstraintLayout.LayoutParams.UNSET;
        lps.setMarginStart(-((int)displayMetrics.density));
        lps.endToEnd = R.id.tleftdicebg;
        lps.bottomToBottom = R.id.tleftdicebg;
        mainDiceImageView.setLayoutParams(lps);
        ConstraintLayout.LayoutParams arrowLps = (ConstraintLayout.LayoutParams) hintArrow.getLayoutParams();
        arrowLps.endToStart = ConstraintLayout.LayoutParams.UNSET;
        arrowLps.startToEnd = R.id.tleftdicebg;
        arrowLps.topToTop = R.id.tleftdicebg;
        arrowLps.bottomToBottom = R.id.tleftdicebg;
        hintArrow.setRotation(0);
        hintArrow.setLayoutParams(arrowLps);
    }

    void moveToTopRight()
    {
        ConstraintLayout.LayoutParams lps = (ConstraintLayout.LayoutParams) mainDiceImageView.getLayoutParams();
        lps.startToStart = R.id.trightdicebg;
        lps.setMarginStart(-((int)(2*displayMetrics.density)));
        lps.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        lps.bottomToBottom = R.id.trightdicebg;
        mainDiceImageView.setLayoutParams(lps);
        ConstraintLayout.LayoutParams arrowLps = (ConstraintLayout.LayoutParams) hintArrow.getLayoutParams();
        arrowLps.startToEnd = ConstraintLayout.LayoutParams.UNSET;
        arrowLps.endToStart = R.id.trightdicebg;
        arrowLps.topToTop = R.id.trightdicebg;
        arrowLps.bottomToBottom = R.id.trightdicebg;
        hintArrow.setRotation(180);
        hintArrow.setLayoutParams(arrowLps);
    }

    void moveToBottomRight()
    {
        ConstraintLayout.LayoutParams lps = (ConstraintLayout.LayoutParams) mainDiceImageView.getLayoutParams();
        lps.startToStart = R.id.brightdicebg;
        lps.setMarginStart(-((int)(2*displayMetrics.density)));
        lps.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        lps.bottomToBottom = R.id.brightdicebg;
        mainDiceImageView.setLayoutParams(lps);
        ConstraintLayout.LayoutParams arrowLps = (ConstraintLayout.LayoutParams) hintArrow.getLayoutParams();
        arrowLps.startToEnd = ConstraintLayout.LayoutParams.UNSET;
        arrowLps.endToStart = R.id.brightdicebg;
        arrowLps.topToTop = R.id.brightdicebg;
        arrowLps.bottomToBottom = R.id.brightdicebg;
        hintArrow.setRotation(180);
        hintArrow.setLayoutParams(arrowLps);
    }

    void moveToBottomLeft()
    {
        ConstraintLayout.LayoutParams lps = (ConstraintLayout.LayoutParams) mainDiceImageView.getLayoutParams();
        lps.startToStart = ConstraintLayout.LayoutParams.UNSET;
        lps.setMarginStart(-((int)displayMetrics.density));
        lps.endToEnd = R.id.bleftdicebg;
        lps.bottomToBottom = R.id.bleftdicebg;
        mainDiceImageView.setLayoutParams(lps);
        ConstraintLayout.LayoutParams arrowLps = (ConstraintLayout.LayoutParams) hintArrow.getLayoutParams();
        arrowLps.endToStart = ConstraintLayout.LayoutParams.UNSET;
        arrowLps.startToEnd = R.id.bleftdicebg;
        arrowLps.topToTop = R.id.bleftdicebg;
        arrowLps.bottomToBottom = R.id.bleftdicebg;
        hintArrow.setRotation(0);
        hintArrow.setLayoutParams(arrowLps);
    }

    int[] getWinnerBlocks(int n)
    {
        switch (n)
        {
            case 0:
                return wBlocks52;
            case 11:
                return wBlocks58;
            case 24:
                return wBlocks64;
            case 37:
                return wBlocks70;
            default:
                return wBlocks52;
        }
    }

    @Override
    public void onBackPressed() {
        // Route legacy button presses through the same dispatcher used by
        // Android 13+ gesture/predictive back. This prevents the activity
        // from finishing before the quit confirmation can be displayed.
        getOnBackPressedDispatcher().onBackPressed();
    }
}