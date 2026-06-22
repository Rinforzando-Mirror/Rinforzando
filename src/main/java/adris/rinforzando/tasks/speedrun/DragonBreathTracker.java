package adris.rinforzando.tasks.speedrun;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.BotBehaviour;
import adris.rinforzando.tasks.movement.CustomBaritoneGoalTask;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.helpers.WorldHelper;
import adris.rinforzando.util.progresscheck.MovementProgressChecker;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalRunAway;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public class DragonBreathTracker {
    private final HashSet<BlockPos> breathBlocks = new HashSet<>();

    public void updateBreath(Rinforzando mod) {
        breathBlocks.clear();
        for (AreaEffectCloudEntity cloud : mod.getEntityTracker().getTrackedEntities(AreaEffectCloudEntity.class)) {
            for (BlockPos bad : WorldHelper.getBlocksTouchingBox(cloud.getBoundingBox())) {
                breathBlocks.add(bad);
            }
        }
    }

    public boolean isTouchingDragonBreath(BlockPos pos) {
        return breathBlocks.contains(pos);
    }

    public Task getRunAwayTask() {
        return new RunAwayFromDragonsBreathTask();
    }

    private class RunAwayFromDragonsBreathTask extends CustomBaritoneGoalTask {

        @Override
        protected void onStart() {
            super.onStart();
            BotBehaviour botBehaviour = Rinforzando.getInstance().getBehaviour();

            botBehaviour.push();
            botBehaviour.setBlockPlacePenalty(Double.POSITIVE_INFINITY);
            // do NOT ever wander
            checker = new MovementProgressChecker((int) Float.POSITIVE_INFINITY);
        }

        @Override
        protected void onStop(Task interruptTask) {
            super.onStop(interruptTask);
            Rinforzando.getInstance().getBehaviour().pop();
        }

        @Override
        protected Goal newGoal(Rinforzando mod) {
            return new GoalRunAway(10, breathBlocks.toArray(BlockPos[]::new));
        }

        @Override
        protected boolean isEqual(Task other) {
            return other instanceof RunAwayFromDragonsBreathTask;
        }

        @Override
        protected String toDebugString() {
            return "ESCAPE Dragons Breath";
        }
    }
}
