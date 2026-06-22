package adris.rinforzando.tasks.movement;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.tasksystem.Task;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalNear;
import net.minecraft.util.math.BlockPos;

public class GetWithinRangeOfBlockTask extends CustomBaritoneGoalTask {

    public final BlockPos blockPos;
    public final int range;

    public GetWithinRangeOfBlockTask(BlockPos blockPos, int range) {
        this.blockPos = blockPos;
        this.range = range;
    }

    @Override
    protected Goal newGoal(Rinforzando mod) {
        return new GoalNear(blockPos, range);
    }

    @Override
    protected boolean isEqual(Task other) {
        if (other instanceof GetWithinRangeOfBlockTask task) {
            return task.blockPos.equals(blockPos) && task.range == range;
        }
        return false;
    }

    @Override
    protected String toDebugString() {
        return "Getting within " + range + " blocks of " + blockPos.toShortString();
    }
}
