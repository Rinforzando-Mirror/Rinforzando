package adris.rinforzando.tasks.movement;

import adris.rinforzando.Rinforzando;
import adris.rinforzando.chains.MobDefenseChain;
import adris.rinforzando.tasksystem.Task;
import adris.rinforzando.util.baritone.GoalRunAwayFromEntities;
import baritone.api.pathing.goals.Goal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class RunAwayFromCreepersTask extends CustomBaritoneGoalTask {

    private final double _distanceToRun;

    public RunAwayFromCreepersTask(double distance) {
        _distanceToRun = distance;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    protected boolean isEqual(Task other) {
        if (other instanceof RunAwayFromCreepersTask task) {
            //if (task._mob.getPos().squaredDistanceTo(_mob.getPos()) > 0.5) return false;
            if (Math.abs(task._distanceToRun - _distanceToRun) > 1) return false;
            return true;
        }
        return false;
    }

    @Override
    protected String toDebugString() {
        return "Run " + _distanceToRun + " blocks away from creepers";
    }

    @Override
    protected Goal newGoal(Rinforzando mod) {
        // We want to run away NOW
        mod.getClientBaritone().getPathingBehavior().forceCancel();
        return new GoalRunAwayFromCreepers(mod, _distanceToRun);
    }

    private static class GoalRunAwayFromCreepers extends GoalRunAwayFromEntities {

        public GoalRunAwayFromCreepers(Rinforzando mod, double distance) {
            super(mod, distance, false, 10);
        }

        @Override
        protected List<Entity> getEntities(Rinforzando mod) {
            return new ArrayList<>(mod.getEntityTracker().getTrackedEntities(CreeperEntity.class));
        }

        @Override
        protected double getCostOfEntity(Entity entity, int x, int y, int z) {
            if (entity instanceof CreeperEntity) {
                return MobDefenseChain.getCreeperSafety(new Vec3d(x + 0.5, y + 0.5, z + 0.5), (CreeperEntity) entity);
            }
            return super.getCostOfEntity(entity, x, y, z);
        }
    }
}
